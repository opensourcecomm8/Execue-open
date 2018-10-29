/**
 * Licensed to the Execue Software Foundation (ESF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ESF licenses this file
 * to you under the Execue License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 *
 */
package com.execue.nlp.engine.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.common.bean.nlp.CandidateEntity;
import com.execue.core.common.bean.nlp.NLPInformation;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.nlp.TokenCandidate;
import com.execue.core.common.bean.swi.PopularityHit;
import com.execue.core.common.type.TermType;
import com.execue.core.util.ExecueStringUtil;
import com.execue.nlp.bean.NLPToken;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.bean.entity.HitsUpdateInfo;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.PWEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.SFLEntity;
import com.execue.nlp.bean.matrix.Iteration;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPopularityService;

/**
 * @author Nihar
 */
public class SFLTermHitsUpdateHelper {

   private IKDXRetrievalService  kdxRetrievalService;
   private IKDXManagementService kdxManagementService;
   private IPopularityService    popularityService;
   private Logger                logger = Logger.getLogger(SFLTermHitsUpdateHelper.class);

   public void updatePopularityHitsForToken (List<Possibility> possibilities) throws SWIException {

      List<PopularityHit> popularityHits = new ArrayList<PopularityHit>();
      for (Possibility possibility : possibilities) {
         for (IWeightedEntity entity : possibility.getRecognitionEntities()) {
            if (entity instanceof OntoEntity) {
               OntoEntity ontoEntity = (OntoEntity) entity;
               RecognitionEntity recEntity = ontoEntity.getRecEntity();
               // TODO changed the code for SFL hits population. Similar change will be needed in Ri parallel word
               // hits population too.
               if (ontoEntity.getHitsUpdateInfo() != null) {
                  HitsUpdateInfo hitsUpdateInfo = ontoEntity.getHitsUpdateInfo();
                  for (Long tokenId : hitsUpdateInfo.getSflTokensIds()) {
                     popularityHits.add(getPopularityForToken(TermType.SFL_TERM_TOKEN_ENTITY, tokenId));
                  }
               } else if (recEntity != null && recEntity.getEntityType().equals(RecognitionEntityType.PW_ENTITY)) {
                  PWEntity pwEntity = (PWEntity) recEntity;
                  String word = ontoEntity.getDisplayName();
                  Map<String, RIParallelWord> parallelWords = pwEntity.getWordValues();
                  for (RIParallelWord parallelWord : parallelWords.values()) {
                     if (logger.isDebugEnabled()) {
                        logger.debug("Parallel Word is " + parallelWord.getWord() + " equivalantWord is "
                                 + parallelWord.getEquivalentWord() + " NLP tokens is " + word);
                     }
                     String parallelWordString = ExecueStringUtil.compactString(parallelWord.getEquivalentWord());
                     if (parallelWordString.equalsIgnoreCase(word)) {
                        // Check for parallel Word for singular and plural entities.
                        popularityHits
                                 .add(getPopularityForToken(TermType.RI_PARALLEL_TERM_ENTITY, parallelWord.getId()));
                     }
                  }
               } else {
                  // NK: Skipping it for now
               }
            }
         }
      }

      // update all the popularity hits now
      if (!CollectionUtils.isEmpty(popularityHits)) {
         getPopularityService().saveAll(popularityHits);
      }
   }

   private PopularityHit getPopularityForToken (TermType termType, Long termId) throws SWIException {
      PopularityHit hit = new PopularityHit();
      hit.setType(termType);
      hit.setCreatedDate(new Date());
      hit.setHits(1L);
      hit.setProcessingState("N");
      hit.setTermId(termId);
      return hit;
   }

   public void populateTokenCandidates (List<Possibility> possibilities, NLPInformation nlpInfo,
            List<NLPToken> nlpTokens) {
      for (Possibility possibility : possibilities) {
         // get The first iteration with ONToENtity i.e iteration where we have run the ontoRecognitionProcessor .
         int iterationId = getFirstIterationWithOntoEntity(possibility.getIterations());
         if (iterationId <= -1) {
            continue;
         }
         Iteration iteration = possibility.getIterations().get(iterationId - 1);
         for (NLPToken nlpToken : iteration.getInput().getNLPTokens()) {
            for (RecognitionEntity entity : nlpToken.getRecognitionEntities()) {
               if (entity instanceof OntoEntity) {
                  OntoEntity ontoEntity = (OntoEntity) entity;
                  RecognitionEntity recEntity = ontoEntity.getRecEntity();
                  if (recEntity == null) {
                     continue;
                  }
                  if (recEntity.getEntityType().equals(RecognitionEntityType.SFL_ENTITY)) {
                     SFLEntity sflEntity = (SFLEntity) recEntity;
                     addOrUpdateTokencandidate(nlpInfo, sflEntity.getSflTermId(), recEntity.getWord(), nlpTokens,
                              nlpToken.getOriginalPositions(), RecognitionEntityType.SFL_ENTITY);
                  } else if (recEntity.getEntityType().equals(RecognitionEntityType.PW_ENTITY)) {
                     PWEntity pwEntity = (PWEntity) recEntity;
                     String word = ontoEntity.getWord();
                     Map<String, RIParallelWord> parallelWords = pwEntity.getWordValues();
                     for (RIParallelWord parallelWord : parallelWords.values()) {
                        if (logger.isDebugEnabled()) {
                           logger.debug("Parallel Word is " + parallelWord.getWord() + " equivalantWord is "
                                    + parallelWord.getEquivalentWord() + " NLP tokens is " + word);
                        }
                        String parallelWordString = ExecueStringUtil.compactString(parallelWord.getEquivalentWord());
                        if (parallelWordString.equalsIgnoreCase(word)) {
                           addOrUpdateTokencandidate(nlpInfo, parallelWord.getId(), recEntity.getWord(), nlpTokens,
                                    nlpToken.getOriginalPositions(), RecognitionEntityType.PW_ENTITY);
                        }
                     }
                  } else {
                     // TODO:NK: Need to check with NA/AP to use getConceptBedId or not??
                     addOrUpdateTokencandidate(nlpInfo, ((ConceptEntity) ontoEntity).getConceptBedId(),
                              ((ConceptEntity) ontoEntity).getConceptDisplayName(), nlpTokens, nlpToken
                                       .getOriginalPositions(), RecognitionEntityType.CONCEPT_ENTITY);
                  }
               }
            }
         }
      }
   }

   private void addOrUpdateTokencandidate (NLPInformation nlpInfo, Long entityId, String recEntityName,
            List<NLPToken> nlpTokens, List<Integer> tokenOriginalPositions, RecognitionEntityType entityType) {
      Collections.sort(tokenOriginalPositions);
      if (StringUtils.isEmpty(recEntityName) || entityId == null) {
         return;
      }
      List<TokenCandidate> tokenCandidates = nlpInfo.getTokenCandidates();
      for (Integer pos : tokenOriginalPositions) {
         if (!tokenOriginalPositions.contains(pos)) {
            pos = tokenOriginalPositions.get(0);
         }
         TokenCandidate tokenCandidate = null;
         for (TokenCandidate addedTokenCandidate : tokenCandidates) {
            if (addedTokenCandidate.getPosition().equals(pos)) {
               tokenCandidate = addedTokenCandidate;
            }
         }
         if (tokenCandidate == null) {
            tokenCandidate = new TokenCandidate();
            nlpInfo.addTokenCandidate(tokenCandidate);
         }

         List<String> words = new ArrayList<String>();
         // List<Integer> referedTokenPositions = tokenPositions;
         for (Integer position : tokenOriginalPositions) {
            if (nlpTokens != null && nlpTokens.size() > position) {
               words.add(nlpTokens.get(position).getWord());
            }
         }
         tokenCandidate.setPosition(pos);
         String word = nlpTokens.get(pos).getWord();
         // tokenCandidate.setWord(iteration.getMatrix().getNLPTokens().get(pos).getWord());
         tokenCandidate.setWord(word);
         CandidateEntity candEntity = new CandidateEntity();
         candEntity.setWords(words);
         candEntity.setId(entityId);
         candEntity.setName(recEntityName);
         candEntity.setType(entityType);
         tokenCandidate.addEntity(candEntity);
         // candEntity.set

      }
   }

   private int getFirstIterationWithOntoEntity (List<Iteration> iterations) {
      int firstIterationWithOntoEntity = -1;
      int i = 0;
      for (Iteration iteration : iterations) {
         // Matrix matrix = iteration.getMatrix();
         for (NLPToken nlpToken : iteration.getInput().getNLPTokens()) {
            for (RecognitionEntity multiRecognitionEntity : nlpToken.getRecognitionEntities()) {
               if (multiRecognitionEntity instanceof OntoEntity) {
                  return iteration.getId();
               }
            }
         }
      }
      return firstIterationWithOntoEntity;
   }

   // public void updateTokenCandedateForEntities (List<Possibility> possibilities, NLPInformation nlpInfo,
   // List<NLPToken> nlpTokens) throws KDXException {
   // logger.debug("Inside updateTokenCandedateForEntities");
   // Map<Integer, List<OntoEntity>> positionOntoEntityMap = new HashMap<Integer, List<OntoEntity>>();
   // for (Possibility possibility : possibilities) {
   // List<Iteration> iterations = possibility.getIterations();
   // int lastIterationWithSFL = getLastIterationWithEntity(iterations, EntityType.SFL_ENTITY);
   // if (lastIterationWithSFL != -1) {
   // Iteration iteration = possibility.getIterations().get(lastIterationWithSFL);
   // // Matrix matrix = iteration.getMatrix();
   // for (NLPToken nlpToken : iteration.getInput().getNLPTokens()) {
   // if ((nlpToken.getRecognitionEntities() != null) && !nlpToken.getRecognitionEntities().isEmpty()) {
   // IWeightedEntity entity = nlpToken.getDefaultRecognitionEntity();
   // if (entity instanceof SFLEntity) {
   // addTokenCandidate(iteration, nlpInfo, entity, nlpTokens, nlpToken.getOriginalPositions());
   // }
   //
   // }
   //
   // }
   // }
   //
   // int lastIterationWithInstanceEntity = getLastIterationWithEntity(iterations, EntityType.INSTANCE_ENTITY);
   // int lastIterationWithConceptEntity = getLastIterationWithEntity(iterations, EntityType.CONCEPT_ENTITY);
   // int lastIterationWithOntoEntity = lastIterationWithConceptEntity >= lastIterationWithInstanceEntity ?
   // lastIterationWithConceptEntity
   // : lastIterationWithInstanceEntity;
   // if (lastIterationWithOntoEntity != -1) {
   // Iteration iteration = possibility.getIterations().get(lastIterationWithOntoEntity);
   // // Matrix matrix = iteration.getMatrix();
   // for (NLPToken nlpToken : iteration.getInput().getNLPTokens()) {
   // if ((nlpToken.getRecognitionEntities() != null) && !nlpToken.getRecognitionEntities().isEmpty()) {
   // IWeightedEntity entity = nlpToken.getDefaultRecognitionEntity();
   // if (entity instanceof InstanceEntity) {
   // addTokenCandidateForOntoEntity(iteration, nlpInfo, entity, nlpTokens, nlpToken
   // .getOriginalPositions(), positionOntoEntityMap);
   // } else if (entity instanceof ConceptEntity) {
   // addTokenCandidateForOntoEntity(iteration, nlpInfo, entity, nlpTokens, nlpToken
   // .getOriginalPositions(), positionOntoEntityMap);
   // }
   //
   // }
   //
   // }
   // }
   // int lastIterationWithPW = getLastIterationWithEntity(iterations, EntityType.PW_ENTITY);
   // if (lastIterationWithPW != -1) {
   // Iteration iteration = possibility.getIterations().get(lastIterationWithPW);
   // // Matrix matrix = iteration.getMatrix();
   // for (NLPToken nlpToken : iteration.getInput().getNLPTokens()) {
   // if ((nlpToken.getRecognitionEntities() != null) && !nlpToken.getRecognitionEntities().isEmpty()) {
   // IWeightedEntity entity = nlpToken.getDefaultRecognitionEntity();
   // if (entity instanceof PWEntity) {
   // // iterate Through the iterations after the current Iteration to see if the entity at the postion
   // // of the current entity is OI
   // // entity.
   // PWEntity pwEntity = (PWEntity) entity;
   // for (lastIterationWithPW = lastIterationWithPW + 1; lastIterationWithPW < possibility
   // .getIterations().size(); lastIterationWithPW++) {
   // Iteration nextIteration = possibility.getIterations().get(lastIterationWithPW);
   // NLPToken nlpToken2 = nextIteration.getInput().getNLPTokens().get(
   // ((PWEntity) entity).getPosition());
   // if (nlpToken2.getDefaultRecognitionEntity() instanceof OntoEntity) {
   // OntoEntity entity2 = (OntoEntity) nlpToken2.getDefaultRecognitionEntity();
   // String word = entity2.getWord();
   // Map<String, RIParallelWord> parallelWords = pwEntity.getWordValues();
   // for (RIParallelWord parallelWord : parallelWords.values()) {
   // if (logger.isDebugEnabled()) {
   // logger.debug("Parallel Word is " + parallelWord.getWord() + " equivalantWord is "
   // + parallelWord.getEquivalentWord() + " NLP tokens is " + word);
   // }
   // String parallelWordString = ExecueStringUtil.compactString(parallelWord
   // .getEquivalentWord());
   // if (parallelWordString.equalsIgnoreCase(word)) {
   // addTokenCandedateForPWEntity(parallelWord, pwEntity, iteration, nlpInfo, nlpTokens,
   // nlpToken2.getOriginalPositions());
   // }
   // }
   // break;
   // } else if (nlpToken2.getDefaultRecognitionEntity() instanceof SFLEntity) {
   // // SFL is recognized with parallel word so we can skipp adding for parallel word for now.
   // break;
   // }
   // }
   //
   // }
   //
   // }
   //
   // }
   // }
   //
   // }
   //
   // }
   //
   // private void addTokenCandidateForOntoEntity (Iteration iteration, NLPInformation nlpInfo, IWeightedEntity entity,
   // List<NLPToken> nlpTokens, List<Integer> originalPositions,
   // Map<Integer, List<OntoEntity>> positionOntoEntityMap) {
   //
   // OntoEntity ontoEntity = (OntoEntity) entity;
   // List<Integer> tokenPositions = ontoEntity.getReferedTokenPositions();
   // if (CollectionUtils.isEmpty(tokenPositions) || CollectionUtils.isEmpty(originalPositions)) {
   // return;
   // }
   // List<TokenCandidate> tokenCandidates = nlpInfo.getTokenCandidates();
   // for (Integer pos : tokenPositions) {
   // if (!originalPositions.contains(pos)) {
   // pos = originalPositions.get(0);
   // }
   // TokenCandidate tokenCandidate = null;
   // for (TokenCandidate addedTokenCandidate : tokenCandidates) {
   // if (addedTokenCandidate.getPosition().equals(pos)) {
   // tokenCandidate = addedTokenCandidate;
   // }
   // }
   // if (tokenCandidate == null) {
   // tokenCandidate = new TokenCandidate();
   // nlpInfo.addTokenCandidate(tokenCandidate);
   // tokenCandidate.setPosition(pos);
   // String word = nlpTokens.get(pos).getWord();
   // // tokenCandidate.setWord(iteration.getMatrix().getNLPTokens().get(pos).getWord());
   // tokenCandidate.setWord(word);
   // }
   // List<OntoEntity> ontList = positionOntoEntityMap.get(tokenCandidate.getPosition());
   // if (CollectionUtils.isEmpty(ontList)) {
   // ontList = new ArrayList<OntoEntity>();
   // }
   // if (ontList.contains(ontoEntity)) {
   // return;
   // }
   // for (CandidateEntity candidateEntity : tokenCandidate.getEntities()) {
   // // TODO: NK: check with NA/AP on how to get the display name??
   // /*
   // * if (candidateEntity.getType().equals(EntityType.SFL_ENTITY) &&
   // * candidateEntity.getName().equalsIgnoreCase(ontoEntity.getDisplayName())) { ontList.add(ontoEntity);
   // * positionOntoEntityMap.put(tokenCandidate.getPosition(), ontList); return; }
   // */
   // }
   // // tokenCandidate.setWord(iteration.getMatrix().getNLPTokens().get(pos).getWord());
   // CandidateEntity candEntity = new CandidateEntity();
   // List<Integer> referedTokenPositions = ontoEntity.getReferedTokenPositions();
   // List<String> words = new ArrayList<String>();
   // for (Integer position : referedTokenPositions) {
   // if (!originalPositions.contains(position)) {
   // position = originalPositions.get(0);
   // }
   //
   // if ((nlpTokens != null) && (nlpTokens.size() > position)) {
   // words.add(nlpTokens.get(position).getWord());
   // }
   // }
   // candEntity.setWords(words);
   // if (ontoEntity instanceof InstanceEntity) {
   // InstanceEntity instanceEntity = (InstanceEntity) ontoEntity;
   // if (instanceEntity.getInstanceBedId() == null) {
   // return;
   // }
   // candEntity.setId(instanceEntity.getInstanceBedId());
   // if (!StringUtils.isEmpty(((InstanceEntity) ontoEntity).getInstanceDisplayName())) {
   // candEntity.setName(((InstanceEntity) ontoEntity).getInstanceDisplayName());
   // } else {
   // candEntity.setName(ontoEntity.getWord());
   // }
   //
   // candEntity.setType(EntityType.INSTANCE_ENTITY);
   // tokenCandidate.addEntity(candEntity);
   // } else if (ontoEntity instanceof ConceptEntity) {
   // ConceptEntity conceptEntity = (ConceptEntity) ontoEntity;
   // candEntity.setId(conceptEntity.getConceptBedId());
   // candEntity.setName(conceptEntity.getWord());
   // candEntity.setType(EntityType.CONCEPT_ENTITY);
   // tokenCandidate.addEntity(candEntity);
   // }
   //
   // }
   // }

   /**
    * Method to filter Tokencandidate if the CandidateEntity List is not greated then one.
    * 
    * @param nlpInfo
    */
   public void filterTokenCandidates (NLPInformation nlpInfo) {
      List<TokenCandidate> tokenCandidateList = new ArrayList<TokenCandidate>();
      if (CollectionUtils.isEmpty(nlpInfo.getTokenCandidates())) {
         return;
      }
      // TODO Checking on the basis of string as opf now need to remove this.
      for (TokenCandidate tokenCandidate : nlpInfo.getTokenCandidates()) {
         if (CollectionUtils.size(tokenCandidate.getEntities()) > 1) {
            List<CandidateEntity> removeList = new ArrayList<CandidateEntity>();
            for (CandidateEntity ontoEntity : tokenCandidate.getEntities()) {
               if (ontoEntity.getType().equals(RecognitionEntityType.CONCEPT_ENTITY)
                        || ontoEntity.getType().equals(RecognitionEntityType.INSTANCE_ENTITY)) {
                  for (CandidateEntity sflEntity : tokenCandidate.getEntities()) {
                     if (sflEntity.getType().equals(RecognitionEntityType.SFL_ENTITY)) {
                        if (ExecueStringUtil.compactString(sflEntity.getName()).equalsIgnoreCase(ontoEntity.getName())) {
                           removeList.add(ontoEntity);
                           break;
                        }
                     }
                  }
               }
            }
            tokenCandidate.getEntities().removeAll(removeList);
            if (CollectionUtils.size(tokenCandidate.getEntities()) > 1) {
               tokenCandidateList.add(tokenCandidate);
            }
         }

      }
      nlpInfo.setTokenCandidates(tokenCandidateList);

   }

   private void addTokenCandedateForPWEntity (RIParallelWord parallelWord, PWEntity pwEntity, Iteration iteration,
            NLPInformation nlpInfo, List<NLPToken> nlpTokens, List<Integer> originalPositions) {

      // List<CandidateEntity> sflCandidates = pwEntity.getCandidates();

      List<Integer> tokenPositions = pwEntity.getReferedTokenPositions();
      Long termId = parallelWord.getId();
      List<TokenCandidate> tokenCandidates = nlpInfo.getTokenCandidates();
      for (Integer pos : tokenPositions) {
         TokenCandidate tokenCandidate = null;
         for (TokenCandidate addedTokenCandidate : tokenCandidates) {
            if (addedTokenCandidate.getPosition().equals(pos)) {
               tokenCandidate = addedTokenCandidate;
            }
         }
         if (tokenCandidate == null) {
            tokenCandidate = new TokenCandidate();
            nlpInfo.addTokenCandidate(tokenCandidate);
         }
         tokenCandidate.setPosition(pos);
         String word = nlpTokens.get(pos).getWord();
         // tokenCandidate.setWord(iteration.getMatrix().getNLPTokens().get(pos).getWord());
         tokenCandidate.setWord(word);

         // tokenCandidate.setWord(iteration.getMatrix().getNLPTokens().get(pos).getWord());
         CandidateEntity candEntity = new CandidateEntity();
         List<Integer> referedTokenPositions = pwEntity.getReferedTokenPositions();
         List<String> words = new ArrayList<String>();
         for (Integer position : referedTokenPositions) {
            if (!originalPositions.contains(position)) {
               position = originalPositions.get(0);
            }

            if (nlpTokens != null && nlpTokens.size() > position) {
               words.add(nlpTokens.get(position).getWord());
            }
         }
         candEntity.setWords(words);
         candEntity.setId(termId);
         candEntity.setName(pwEntity.getWord());
         candEntity.setType(RecognitionEntityType.PW_ENTITY);
         tokenCandidate.addEntity(candEntity);
         // candEntity.set
      }
   }

   /**
    * @param iterations
    * @return
    */
   private int getLastIterationWithEntity (List<Iteration> iterations, RecognitionEntityType entityType) {
      int lastIterationWithSFL = -1;
      int i = 0;
      for (Iteration iteration : iterations) {
         // Matrix matrix = iteration.getMatrix();
         for (NLPToken nlpToken : iteration.getInput().getNLPTokens()) {
            if (nlpToken.getRecognitionEntities() != null && !nlpToken.getRecognitionEntities().isEmpty()) {
               IWeightedEntity entity = nlpToken.getDefaultRecognitionEntity();
               if (entityType.equals(RecognitionEntityType.SFL_ENTITY) && entity instanceof SFLEntity) {
                  lastIterationWithSFL = i;
                  break;
               }
               if (entityType.equals(RecognitionEntityType.PW_ENTITY) && entity instanceof PWEntity) {
                  lastIterationWithSFL = i;
                  break;
               }
               if (entityType.equals(RecognitionEntityType.INSTANCE_ENTITY) && entity instanceof InstanceEntity) {
                  lastIterationWithSFL = i;
                  break;
               }
            }

         }
         i++;
      }
      return lastIterationWithSFL;
   }

   private void addTokenCandidate (Iteration iteration, NLPInformation nlpInfo, IWeightedEntity entity,
            List<NLPToken> nlpTokens, List<Integer> tokenOriginalPositions) {

      if (entity instanceof SFLEntity) {
         SFLEntity sflEntity = (SFLEntity) entity;
         List<Integer> tokenPositions = sflEntity.getReferedTokenPositions();
         Long termId = sflEntity.getSflTermId();
         List<TokenCandidate> tokenCandidates = nlpInfo.getTokenCandidates();
         for (Integer pos : tokenPositions) {
            if (!tokenOriginalPositions.contains(pos)) {
               pos = tokenOriginalPositions.get(0);
            }
            TokenCandidate tokenCandidate = null;
            for (TokenCandidate addedTokenCandidate : tokenCandidates) {
               if (addedTokenCandidate.getPosition().equals(pos)) {
                  tokenCandidate = addedTokenCandidate;
               }
            }
            if (tokenCandidate == null) {
               tokenCandidate = new TokenCandidate();
               nlpInfo.addTokenCandidate(tokenCandidate);
            }

            List<String> words = new ArrayList<String>();
            List<Integer> referedTokenPositions = sflEntity.getReferedTokenPositions();
            for (Integer position : referedTokenPositions) {
               if (!tokenOriginalPositions.contains(position)) {
                  position = tokenOriginalPositions.get(0);
               }

               if (nlpTokens != null && nlpTokens.size() > position) {
                  words.add(nlpTokens.get(position).getWord());
               }
            }
            /*
             * for(CandidateEntity candidateEntity : sflCandidates){ candidateEntity.setWords(words);
             * tokenCandidate.addEntity(candidateEntity); }
             */

            tokenCandidate.setPosition(pos);
            String word = nlpTokens.get(pos).getWord();
            // tokenCandidate.setWord(iteration.getMatrix().getNLPTokens().get(pos).getWord());
            tokenCandidate.setWord(word);
            CandidateEntity candEntity = new CandidateEntity();

            candEntity.setWords(words);
            candEntity.setId(termId);
            candEntity.setName(sflEntity.getSflName());
            candEntity.setType(RecognitionEntityType.SFL_ENTITY);
            tokenCandidate.addEntity(candEntity);
            // candEntity.set
         }
      }
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
   }

   public IPopularityService getPopularityService () {
      return popularityService;
   }

   public void setPopularityService (IPopularityService popularityService) {
      this.popularityService = popularityService;
   }

   /**
    * @param wordWeightMap
    * @throws KDXException
    */
   public void updateSFLTokenWeights (Map<String, Double> wordWeightMap) throws KDXException {
      List<String> words = new ArrayList<String>();
      words.addAll(wordWeightMap.keySet());
      List<SFLTermToken> sfls = getKdxRetrievalService().getSFLTermTokensByLookupWords(words);
      for (SFLTermToken sflTermToken : sfls) {
         Double weight = wordWeightMap.get(sflTermToken.getBusinessTermToken().toUpperCase());
         Double weightToDistribute = sflTermToken.getWeight() - weight;
         SFLTerm sflTerm = getKdxRetrievalService().getSFLTermById(sflTermToken.getSflTermId());
         Set<SFLTermToken> tokens = new HashSet<SFLTermToken>();
         for (SFLTermToken sfltoken : sflTerm.getSflTermTokens()) {
            if (!words.contains(sfltoken.getBusinessTermToken().toUpperCase())) {
               tokens.add(sfltoken);
            }
         }
         tokens.add(sflTermToken);
         double increasedWeight = 0;
         double remainingWeight = 0;
         if (tokens.size() > 1) {
            increasedWeight = (int) (weightToDistribute / (tokens.size() - 1));
            remainingWeight = weightToDistribute % (tokens.size() - 1);
         } else {
            increasedWeight = weightToDistribute;
         }
         for (SFLTermToken token : tokens) {
            if (token.getId().equals(sflTermToken.getId())) {
               token.setWeight(weight);
            } else {
               token.setWeight(token.getWeight() + increasedWeight + remainingWeight);
               remainingWeight = 0;
            }
         }
         List<SFLTermToken> finalTokens = new ArrayList<SFLTermToken>();
         finalTokens.addAll(tokens);
         getKdxManagementService().updateSFLTermTokens(finalTokens);
      }

   }

}
