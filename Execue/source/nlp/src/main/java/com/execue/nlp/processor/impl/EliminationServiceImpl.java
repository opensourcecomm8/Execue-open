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


package com.execue.nlp.processor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.nlp.bean.NLPToken;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.PWEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.SFLEntity;
import com.execue.nlp.bean.matrix.IterationSummary;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.nlp.bean.matrix.Summary;
import com.execue.nlp.processor.IEliminationSerive;
import com.execue.util.MathUtil;

public class EliminationServiceImpl implements IEliminationSerive {

   private static Logger logger = Logger.getLogger(EliminationServiceImpl.class);

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.processor.IEliminationSerive#filterSFLEntities(java.util.List)
    */
   public Summary filterSFLEntities (Summary summary) {
      List<NLPToken> nlpTokens = summary.getNLPTokens();
      Summary newSummary = new Summary();
      for (int i = 0; i < nlpTokens.size(); i++) {
         NLPToken curTok = nlpTokens.get(i);
         NLPToken newTok = new NLPToken(curTok.getWord(), curTok.getOriginalPositions().get(0));
         List<RecognitionEntity> recEntities = curTok.getRecognitionEntities();
         List<RecognitionEntity> pwEntities = new ArrayList<RecognitionEntity>();
         List<RecognitionEntity> sflEntities = new ArrayList<RecognitionEntity>();
         List<RecognitionEntity> otherEntities = new ArrayList<RecognitionEntity>();
         for (RecognitionEntity recEntity : recEntities) {
            if (recEntity instanceof SFLEntity) {
               sflEntities.add(recEntity);
            } else if (recEntity instanceof PWEntity) {
               pwEntities.add(recEntity);
               otherEntities.add(recEntity);
            } else {
               otherEntities.add(recEntity);
            }
         }
         List<RecognitionEntity> entitiesToRemove = new ArrayList<RecognitionEntity>();
         for (RecognitionEntity sflEntity : sflEntities) {
            for (RecognitionEntity pwEntity : pwEntities) {
               PWEntity entity = (PWEntity) pwEntity;
               Map<String, RIParallelWord> parallelWords = entity.getWordValues();
               if (parallelWords.get(sflEntity.getWord().toLowerCase()) != null) {
                  entitiesToRemove.add(sflEntity);
               }
            }
         }
         sflEntities.removeAll(entitiesToRemove);
         otherEntities.addAll(sflEntities);
         newTok.setRecognitionEntities(otherEntities);
         newSummary.addNLPToken(newTok);
      }
      return newSummary;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.processor.IEliminationSerive#filterOntoEntitiesByTopCluster(java.util.List)
    */
   public void filterOntoEntitiesByTopCluster (List<Possibility> possibilities) {
      for (Possibility possibility : possibilities) {
         List<NLPToken> tokens = possibility.getCurrentIteration().getMatrix().getNLPTokens();
         logger.debug("Iterate through tokens to filter OntoEntities");
         for (NLPToken nlpToken : tokens) {
            List<Double> ontoEntitiesWeights = new ArrayList<Double>();
            List<Double> topOntoEntitiesWeights = new ArrayList<Double>();
            List<RecognitionEntity> topOntoEntities = new ArrayList<RecognitionEntity>();
            // dont need a top cluster if only one entity exist for token.
            if (nlpToken.getRecognitionEntities().size() <= 1) {
               continue;
            }
            for (RecognitionEntity recEntity : nlpToken.getRecognitionEntities()) {
               ontoEntitiesWeights.add(recEntity.getWeightInformation().getRecognitionQuality());
            }
            if (!CollectionUtils.isEmpty(ontoEntitiesWeights) && ontoEntitiesWeights.size() > 1) {
               topOntoEntitiesWeights = MathUtil.getTopCluster(ontoEntitiesWeights);
            } else {
               topOntoEntitiesWeights = ontoEntitiesWeights;
            }
            List<Double> popularityList = new ArrayList<Double>();
            for (RecognitionEntity recEntity : nlpToken.getRecognitionEntities()) {
               if (topOntoEntitiesWeights.contains(recEntity.getWeightInformation().getRecognitionQuality())) {
                  topOntoEntities.add(recEntity);
                  long popularity = ((OntoEntity) recEntity).getPopularity();
                  popularityList.add(Long.valueOf(popularity).doubleValue());
               }
            }
            List<Double> topOntoEntitiesPopularity = new ArrayList<Double>();
            if (!CollectionUtils.isEmpty(popularityList) && popularityList.size() > 1) {
               topOntoEntitiesPopularity = MathUtil.getTopCluster(popularityList);
            } else {
               topOntoEntitiesPopularity = popularityList;
            }
            List<RecognitionEntity> finalOntoEntities = new ArrayList<RecognitionEntity>();
            for (RecognitionEntity recEntity : topOntoEntities) {
               long popularity = ((OntoEntity) recEntity).getPopularity();
               if (topOntoEntitiesPopularity.contains(Long.valueOf(popularity).doubleValue())) {
                  finalOntoEntities.add(recEntity);
               }
            }

            nlpToken.setRecognitionEntities(finalOntoEntities);

         }
      }
   }

   /**
    * Method to check for the group recognitions in rthe NLP Tokens. if one of the recognition entity of consecutive NLP
    * tokens refer to same SFL term, This recognition entity should be used.
    * 
    * @param nlpTokens
    * @return
    */
   public List<NLPToken> addGroupRecognition (List<NLPToken> nlpTokens) {
      List<NLPToken> copyTokens = new ArrayList<NLPToken>();
      for (NLPToken nlpToken : nlpTokens) {
         NLPToken copyToken = new NLPToken(nlpToken.getWord(), nlpToken.getOriginalPositions().get(0));
         for (RecognitionEntity multiRecognitionEntity : nlpToken.getRecognitionEntities()) {
            /*
             * if(!(multiRecognitionEntity.getDefaultRecognitionEntity() instanceof SFLEntity)){
             * copyToken.addMultiRecognitionEntity(multiRecognitionEntity); }
             */
         }
         copyTokens.add(copyToken);
      }
      for (NLPToken nlpToken : nlpTokens) {
         int index = nlpTokens.indexOf(nlpToken);
         List<RecognitionEntity> multiRecs = nlpToken.getRecognitionEntities();
         List<SFLEntity> sflEntities = new ArrayList<SFLEntity>();
         for (RecognitionEntity muRecognitionEntity : multiRecs) {
            if (muRecognitionEntity instanceof SFLEntity) {
               sflEntities.add((SFLEntity) muRecognitionEntity);
            }
         }
         // if no SFL entity no need to check for the group recognition
         if (CollectionUtils.isEmpty(sflEntities)) {
            copyTokens.get(index).setRecognitionEntities(nlpToken.getRecognitionEntities());
            continue;
         }
         int i = -1;
         for (int j = nlpTokens.indexOf(nlpToken) + 1; j < nlpTokens.size(); j++) {
            NLPToken nextToken = nlpTokens.get(j);
            List<RecognitionEntity> multiRecs1 = nextToken.getRecognitionEntities();
            List<SFLEntity> commonEntities = new ArrayList<SFLEntity>();
            for (RecognitionEntity multiRecognitionEntity : multiRecs1) {
               if (multiRecognitionEntity instanceof SFLEntity) {
                  if (isCommonEntity((SFLEntity) multiRecognitionEntity, sflEntities)) {
                     commonEntities.add((SFLEntity) multiRecognitionEntity);
                  }
               }
            }
            if (!CollectionUtils.isEmpty(commonEntities)) {
               sflEntities = commonEntities;
               i = nlpTokens.indexOf(nextToken);
            } else {
               break;
            }

         }

         if (i != -1) {
            for (SFLEntity sflEntity : sflEntities) {
               for (int j = index; j <= i; j++) {
                  NLPToken copyToken = copyTokens.get(j);
                  copyToken.addRecognitionEntity(sflEntity);
               }
            }
         } else {
            if (checkIfNoGroupRecognitionExists(copyTokens.get(index))) {
               copyTokens.get(index).setRecognitionEntities(nlpToken.getRecognitionEntities());
            }
         }
      }

      return copyTokens;
   }

   private boolean checkIfNoGroupRecognitionExists (NLPToken token) {
      for (RecognitionEntity recognitionEntity : token.getRecognitionEntities()) {
         if (recognitionEntity instanceof SFLEntity) {
            return false;
         }
      }
      return true;
   }

   /**
    * Method to check if the SFL entity passed and one of the SFL entity in the list Orefer to same SFL term.
    * 
    * @param defaultRecognitionEntity
    * @param sflEntities
    * @return
    */
   private boolean isCommonEntity (SFLEntity defaultRecognitionEntity, List<SFLEntity> sflEntities) {
      for (SFLEntity sflEntity : sflEntities) {
         if (sflEntity.getSflTermId().equals(defaultRecognitionEntity.getSflTermId())) {
            return true;
         }
      }
      return false;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.processor.IEliminationSerive#filterIndividualRecognitionsBasedOnQuality(com.execue.nlp.bean.matrix.IterationSummary)
    */
   public void filterIndividualRecognitionsBasedOnQuality (IterationSummary iterationSummary) {
      Summary groupRecSummary = null;
      Summary individualRecSummary = null;
      for (Summary summary : iterationSummary.getSummaries()) {
         if (summary.isGroupRecognitionSummary()) {
            groupRecSummary = summary;
         } else {
            individualRecSummary = summary;
         }
      }
      if (individualRecSummary == null || groupRecSummary == null) {
         return;
      }
      Map<ReferedTokenPosition, Double> referedTokenPositionQualityMap = new HashMap<ReferedTokenPosition, Double>(1);
      for (NLPToken nlpToken : groupRecSummary.getNLPTokens()) {
         ReferedTokenPosition referedTokenPosition = new ReferedTokenPosition(nlpToken.getDefaultRecognitionEntity()
                  .getReferedTokenPositions());
         if (referedTokenPosition.getReferedTokenPositions().size() > 1) {
            double quality = 0.0;
            for (RecognitionEntity multiRecognitionEntity : nlpToken.getRecognitionEntities()) {
               if (multiRecognitionEntity.getRecognitionQuality() > quality) {
                  quality = multiRecognitionEntity.getRecognitionQuality();
               }
            }
            referedTokenPositionQualityMap.put(referedTokenPosition, quality);
         }
      }
      boolean dropIndividualRecSummary = true;
      for (Entry<ReferedTokenPosition, Double> entry : referedTokenPositionQualityMap.entrySet()) {
         if (!dropIndividualRecSummary) {
            break;
         }
         ReferedTokenPosition referedTokenPosition = entry.getKey();
         double quality = 0.0;
         for (Integer position : referedTokenPosition.getReferedTokenPositions()) {
            for (NLPToken nlpToken : individualRecSummary.getNLPTokens()) {
               if (position.equals(nlpToken.getDefaultRecognitionEntity().getPosition())) {
                  double tokenQuality = 0.0;
                  for (RecognitionEntity multiRecognitionEntity : nlpToken.getRecognitionEntities()) {
                     if (multiRecognitionEntity.getRecognitionQuality() > tokenQuality) {
                        tokenQuality = multiRecognitionEntity.getRecognitionQuality();
                     }
                  }
                  quality = quality + tokenQuality;
                  break;
               }
            }
         }
         quality = quality / referedTokenPosition.getReferedTokenPositions().size();
         if (quality >= entry.getValue()) {
            dropIndividualRecSummary = false;
            break;
         }
      }
      if (dropIndividualRecSummary) {
         iterationSummary.getSummaries().remove(individualRecSummary);
      }
   }
}
