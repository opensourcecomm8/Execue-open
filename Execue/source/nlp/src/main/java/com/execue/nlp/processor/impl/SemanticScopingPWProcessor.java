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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.util.ExecueStringUtil;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.EntityFactory;
import com.execue.nlp.bean.entity.InflectionEntity;
import com.execue.nlp.bean.entity.PWEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.SFLEntity;
import com.execue.nlp.bean.matrix.Summary;
import com.execue.nlp.helper.NLPServiceHelper;
import com.execue.nlp.processor.IndividualRecognitionProcessor;
import com.execue.nlp.service.impl.IndexFarmService;
import com.execue.nlp.util.NLPUtilities;
import com.execue.swi.exception.KDXException;
import com.execue.util.MathUtil;
import com.execue.util.algorithm.Inflector;

/**
 * @author Nihar
 * @since Nov 06, 2009 -- 3:20:34 PM
 */
public class SemanticScopingPWProcessor extends IndividualRecognitionProcessor {

   private static Logger    logger = Logger.getLogger(SemanticScopingPWProcessor.class);
   private IndexFarmService indexFarmService;
   private NLPServiceHelper nlpServiceHelper;

   @Override
   protected void execute (ProcessorInput processorInput) {

      List<IWeightedEntity> recognitionEntities = processorInput.getRecognitionEntities();
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return;
      }

      // Add the Inflection recognition i.e singular/plural form and verb form entities to the output list of
      // recognition entities
      processInflectionEntities(processorInput);

      // Add the parallel word recognition entities to the output list of recognition entities
      processParallelWordEntities(processorInput);
   }

   /**
    * Method to add the singular/plural and verb form inflector entity(ies) to the processor input.
    * 
    * @param ProcessorInput
    */
   private void processInflectionEntities (ProcessorInput processorInput) {
      List<IWeightedEntity> recognitionEntities = processorInput.getRecognitionEntities();

      for (IWeightedEntity recognitionEntity : recognitionEntities) {
         if (recognitionEntity instanceof PWEntity) {
            continue;
         }
         RecognitionEntity recEntity = (RecognitionEntity) recognitionEntity;
         if (recEntity.getEntityType() == RecognitionEntityType.GENERAL_ENTITY) {
            // Singular or Plurals
            InflectionEntity inflectionEntity = getIndexFarmService().processForInflectorEntity(recEntity);
            if (inflectionEntity != null) {
               inflectionEntity.setStartPosition(recEntity.getPosition());
               inflectionEntity.setEndPosition(recEntity.getPosition());
               inflectionEntity.setReferedTokenPositions(recEntity.getReferedTokenPositions());
               processorInput.addOutputRecognitionEntity(inflectionEntity);
            }
            // Here we will be getting the verb forms of the given words like price it gives pricing and etc.
            List<InflectionEntity> verbFormEntities = getIndexFarmService().processForVerbForms(recEntity);
            if (!CollectionUtils.isEmpty(verbFormEntities)) {
               for (InflectionEntity verbFormEntity : verbFormEntities) {
                  verbFormEntity.setStartPosition(recEntity.getPosition());
                  verbFormEntity.setEndPosition(recEntity.getPosition());
                  verbFormEntity.setReferedTokenPositions(recEntity.getReferedTokenPositions());
                  processorInput.addOutputRecognitionEntity(verbFormEntity);
               }
            }
         }
      }
   }

   /**
    * Method to add the parallel word entity(ies) to the processor input.
    * 
    * @param processorInput
    */
   private void processParallelWordEntities (ProcessorInput processorInput) {

      List<IWeightedEntity> recognitionEntities = processorInput.getRecognitionEntities();

      // Get the possible list of words for each recognition entity
      Map<RecognitionEntity, List<String>> wordsByRecEntity = getWordsByRecognitionEntity(recognitionEntities);

      // Get the parallelWordsByRecEntity map based for the given wordsByRecEntity map
      Map<RecognitionEntity, List<RIParallelWord>> parallelWordsByRecEntity = getParallelWordsByLookupWord(
               wordsByRecEntity, processorInput.getSearchFilter());

      // Add the parallel word entity to the processorInput's outputRecognitionEntities.
      addPWEntity(processorInput, parallelWordsByRecEntity);
   }

   /**
    * @param recognitionEntities
    * @return the Map<RecognitionEntity, List<String>>
    */
   private Map<RecognitionEntity, List<String>> getWordsByRecognitionEntity (List<IWeightedEntity> recognitionEntities) {
      Map<RecognitionEntity, List<String>> wordsByRecEntity = new HashMap<RecognitionEntity, List<String>>();
      for (IWeightedEntity recognitionEntity : recognitionEntities) {
         if (recognitionEntity instanceof PWEntity) {
            continue;
         }
         List<String> possibleWords = getWordForEntity(recognitionEntity);
         wordsByRecEntity.put((RecognitionEntity) recognitionEntity, possibleWords);
      }
      return wordsByRecEntity;
   }

   /**
    * Here we will be adding the parallel word entity to the processorInput's outputRecognitionEntities.
    * 
    * @param processorData
    * @param parallelWordsByRecEntity
    */
   private void addPWEntity (ProcessorInput processorInput,
            Map<RecognitionEntity, List<RIParallelWord>> parallelWordsByRecEntity) {
      if (MapUtils.isEmpty(parallelWordsByRecEntity)) {
         return;
      }
      for (Entry<RecognitionEntity, List<RIParallelWord>> parallelWordsByRecognitionEntry : parallelWordsByRecEntity
               .entrySet()) {
         List<RIParallelWord> parallelWords = parallelWordsByRecognitionEntry.getValue();
         RecognitionEntity recEntity = parallelWordsByRecognitionEntry.getKey();

         if (logger.isDebugEnabled()) {
            logger.debug("Token " + recEntity.getWord());
         }

         // Scope the parallel words with the context id into the map
         Map<Long, List<RIParallelWord>> parallelWordsByContextId = getParallelWordsByContextIdMap(parallelWords);

         // Add a PW entity for list parallel word in each modelGroup.
         for (Entry<Long, List<RIParallelWord>> parallelWordsByContextEntry : parallelWordsByContextId.entrySet()) {
            List<RIParallelWord> contextBasedParallelWords = parallelWordsByContextEntry.getValue();
            Long contextId = parallelWordsByContextEntry.getKey();

            // sort the parallel words by quality
            if (contextBasedParallelWords.size() > 1) {
               Collections.sort(contextBasedParallelWords, new Comparator<RIParallelWord>() {

                  public int compare (RIParallelWord pw1, RIParallelWord pw2) {
                     return (int) (pw2.getQuality() - pw1.getQuality());
                  }

               });
               if (CollectionUtils.isEmpty(contextBasedParallelWords)) {
                  if (logger.isDebugEnabled()) {
                     logger.debug("parallel Word List is empty");
                  }
               }
            }

            // Apply the filter on parallel words
            contextBasedParallelWords = applyFilterOnParallelWords(contextBasedParallelWords);

            // Re-adjust the quality in the parallel words based on the current recognition quality
            for (RIParallelWord riParallelWord : contextBasedParallelWords) {
               riParallelWord.setQuality(recEntity.getRecognitionQuality() * riParallelWord.getQuality());
            }

            // Get the information required to populate the PW Entity
            WeightInformation weightInformation = NLPUtilities.getDefaultWeightInformation();
            int startPos = recEntity.getReferedTokenPositions().get(0);
            int endPos = recEntity.getReferedTokenPositions().size() > 1 ? recEntity.getReferedTokenPositions().get(
                     recEntity.getReferedTokenPositions().size() - 1) : startPos;

            // Create one PW Entity having these parallel words as list
            PWEntity entity = (PWEntity) EntityFactory.getParallelWordEntity(RecognitionEntityType.PW_ENTITY,
                     contextBasedParallelWords, NLPConstants.NLP_TAG_PRALLEL_WORD, null);
            entity.setContextId(contextId);
            entity.setClusterInformation(recEntity.getClusterInformation());
            entity.setWord(recEntity.getWord());
            entity.setWeightInformation(weightInformation);
            entity.setTokenAltered(true);
            entity.setPosition(startPos);
            entity.setStartPosition(startPos);
            entity.setEndPosition(endPos);
            entity.setReferedTokenPositions(recEntity.getReferedTokenPositions());
            processorInput.addOutputRecognitionEntity(entity);
         }
      }
   }

   /**
    * Method to scope the input list of parallel words by context id (i.e. model group id)
    * 
    * @param parallelWords
    * @return the Map<Long, List<RIParallelWord>>
    */
   private Map<Long, List<RIParallelWord>> getParallelWordsByContextIdMap (List<RIParallelWord> parallelWords) {
      Map<Long, List<RIParallelWord>> parallelWordsByContextId = new HashMap<Long, List<RIParallelWord>>(1);
      for (RIParallelWord parallelWord : parallelWords) {
         List<RIParallelWord> contextParallelWords = parallelWordsByContextId.get(parallelWord.getModelGroupId());
         if (contextParallelWords == null) {
            contextParallelWords = new ArrayList<RIParallelWord>(1);
            parallelWordsByContextId.put(parallelWord.getModelGroupId(), contextParallelWords);
         }
         contextParallelWords.add(parallelWord);
      }
      return parallelWordsByContextId;
   }

   /**
    * In this method we will prepare list of RecognitionEntities based wordsByRecEntity.
    * 
    * @param parallelWord
    * @param wordsByRecEntity
    * @return the List<RecognitionEntity>
    */
   private List<RecognitionEntity> getRecEntitiesForParallelWord (RIParallelWord parallelWord,
            Map<RecognitionEntity, List<String>> wordsByRecEntity) {
      List<RecognitionEntity> recEntities = new ArrayList<RecognitionEntity>(1);
      for (Entry<RecognitionEntity, List<String>> entry : wordsByRecEntity.entrySet()) {
         List<String> wordList = entry.getValue();
         for (String word : wordList) {
            if (word.equalsIgnoreCase(parallelWord.getWord())) {
               recEntities.add(entry.getKey());
               break;
            }
         }
      }
      return recEntities;
   }

   /**
    * Filtering the PW List based on the weights and then the hits. PWList Size would not be greater than a configured
    * number, if it is then this filtering is done.
    * 
    * @param parallelWords
    * @return the List<RIParallelWord>
    */
   private List<RIParallelWord> applyFilterOnParallelWords (List<RIParallelWord> parallelWords) {
      int maxPWWords = getNlpConfigurationService().getMaxNoOfPWWords();
      // Apply top quality filter only if we have more parallel words than the maximum allowed parallel words for one
      // recognition
      if (parallelWords.size() > maxPWWords) {
         List<RIParallelWord> topQualityPWs = applyFilterByQuality(parallelWords);

         // Apply priority filter only if we have more top quality parallel words than the maximum allowed parallel
         // words
         if (topQualityPWs.size() > maxPWWords) {
            List<RIParallelWord> topHitsPws = applyFilterByPriority(topQualityPWs);
            // Again check it the top priority parallel words are more than the maximum allowed parallel words, then get
            // the maximum allowed parallel words based on the alphabetical order
            if (topHitsPws.size() > maxPWWords) {
               List<RIParallelWord> finalPWs = applyFilterByAlphabeticalOrder(topHitsPws, maxPWWords);
               return finalPWs;
            } else {
               return topHitsPws;
            }
         } else {
            return topQualityPWs;
         }
      }
      return parallelWords;
   }

   /**
    * Method to filter the input parallel words by alphabetical order of parallel word
    * 
    * @param parallelWords
    * @param maxPWWords
    * @return the List<RIParallelWord>
    */
   private List<RIParallelWord> applyFilterByAlphabeticalOrder (List<RIParallelWord> parallelWords, int maxPWWords) {

      // Sort by equivalent word of parallel word
      Collections.sort(parallelWords, new Comparator<RIParallelWord>() {

         public int compare (RIParallelWord o1, RIParallelWord o2) {
            return o1.getEquivalentWord().compareTo(o2.getEquivalentWord());
         }
      });

      // Get the maximum allowed parallel words
      List<RIParallelWord> finalPWs = new ArrayList<RIParallelWord>(1);
      for (int i = 0; i < maxPWWords; i++) {
         finalPWs.add(parallelWords.get(i));
      }
      return finalPWs;
   }

   /**
    * @param parallelWords
    * @return the List<RIParallelWord>
    */
   private List<RIParallelWord> applyFilterByQuality (List<RIParallelWord> parallelWords) {
      List<RIParallelWord> topQualityPWs = new ArrayList<RIParallelWord>(1);

      // Scope the parallel words by quality
      Map<Double, List<RIParallelWord>> parallelWordsByQuality = new HashMap<Double, List<RIParallelWord>>(1);
      for (RIParallelWord parallelWord : parallelWords) {
         List<RIParallelWord> words = parallelWordsByQuality.get(parallelWord.getQuality());
         if (words == null) {
            words = new ArrayList<RIParallelWord>(1);
            parallelWordsByQuality.put(parallelWord.getQuality(), words);

         }
         words.add(parallelWord);
      }

      // Prepare the parallel word quality list
      List<Double> pwQualityList = new ArrayList<Double>(1);
      pwQualityList.addAll(parallelWordsByQuality.keySet());

      // Apply the top cluster on quality
      List<Double> topQualityList = MathUtil.getTopCluster(pwQualityList);
      for (Entry<Double, List<RIParallelWord>> entry : parallelWordsByQuality.entrySet()) {
         if (topQualityList.contains(entry.getKey())) {
            topQualityPWs.addAll(entry.getValue());
         }
      }
      return topQualityPWs;
   }

   /**
    * @param topQualityPWs
    * @return the List<RIParallelWord>
    */
   private List<RIParallelWord> applyFilterByPriority (List<RIParallelWord> topQualityPWs) {
      List<RIParallelWord> topHitsPws = new ArrayList<RIParallelWord>(1);
      Map<Double, List<RIParallelWord>> parallelWordsByPriority = new HashMap<Double, List<RIParallelWord>>(1);

      // Now scope the top quality parallel words by priority
      for (RIParallelWord parallelWord : topQualityPWs) {
         List<RIParallelWord> words = parallelWordsByPriority.get(parallelWord.getHits().doubleValue());
         if (words == null) {
            words = new ArrayList<RIParallelWord>(1);
            parallelWordsByPriority.put(parallelWord.getHits().doubleValue(), words);
         }
         words.add(parallelWord);
      }

      // Apply the top cluster on priority
      List<Double> pwPriorityList = new ArrayList<Double>(1);
      pwPriorityList.addAll(parallelWordsByPriority.keySet());
      List<Double> topPriorityList = MathUtil.getTopCluster(pwPriorityList);

      for (Entry<Double, List<RIParallelWord>> entry : parallelWordsByPriority.entrySet()) {
         if (topPriorityList.contains(entry.getKey())) {
            topHitsPws.addAll(entry.getValue());
         }
      }
      return topHitsPws;
   }

   /**
    * Method to get the list of parallel words for each recognition entity
    * 
    * @param wordsByRecEntity
    * @param processorInput(input)
    * @return the Map<RecognitionEntity, List<RIParallelWord>>
    */
   private Map<RecognitionEntity, List<RIParallelWord>> getParallelWordsByLookupWord (
            Map<RecognitionEntity, List<String>> wordsByRecEntity, SearchFilter searchFilter) {

      Map<RecognitionEntity, List<RIParallelWord>> parallelWordsByRecEntity = new HashMap<RecognitionEntity, List<RIParallelWord>>(
               1);
      if (wordsByRecEntity.isEmpty()) {
         return parallelWordsByRecEntity;
      }

      Set<Long> modelGroupIds = getNlpServiceHelper().getModelGroupIdsBySearchFilter(searchFilter);
      // Prepare the input words
      List<String> words = new ArrayList<String>(1);
      for (Entry<RecognitionEntity, List<String>> entry : wordsByRecEntity.entrySet()) {
         words.addAll(entry.getValue());
      }

      List<RIParallelWord> pwList = new ArrayList<RIParallelWord>(1);
      try {
         if (!modelGroupIds.isEmpty()) {
            pwList = getKdxRetrievalService().getParallelWordsByLookupWords(words, modelGroupIds);
         } else {
            pwList = getKdxRetrievalService().getParallelWordsByLookupWords(words);
         }
      } catch (KDXException e) {
         e.printStackTrace();
      }
      if (CollectionUtils.isEmpty(pwList)) {
         return parallelWordsByRecEntity;
      }
      // Populate the pw list for each rec entity in the riPWWordByRecEntity
      for (RIParallelWord pwWord : pwList) {
         boolean considerWord = true;
         // Add the parallel word to the pw list of all the eligible rec entity
         List<RecognitionEntity> recEntities = getRecEntitiesForParallelWord(pwWord, wordsByRecEntity);
         for (RecognitionEntity recognitionEntity : recEntities) {
            if (recognitionEntity.getEntityType() == RecognitionEntityType.SFL_ENTITY) {
               SFLEntity sflEntity = (SFLEntity) recognitionEntity;
               // IF SFL entity, then has to be from the same model group
               if (!sflEntity.getContextId().equals(pwWord.getModelGroupId())) {
                  continue;
               }
            }
            for (String word : wordsByRecEntity.get(recognitionEntity)) {
               List<String> tokens = ExecueStringUtil.getAsList(word.toLowerCase());
               // If the equivalent word matches any of tokens in the recognition entity, then do not consider this
               // recognition
               if (tokens.size() > 1 && tokens.contains(pwWord.getEquivalentWord().toLowerCase())) {
                  considerWord = false;
                  break;
               }
            }
            if (considerWord) {
               List<RIParallelWord> pwListForRec = parallelWordsByRecEntity.get(recognitionEntity);
               if (pwListForRec == null) {
                  pwListForRec = new ArrayList<RIParallelWord>(1);
                  parallelWordsByRecEntity.put(recognitionEntity, pwListForRec);
               }
               pwListForRec.add(pwWord);
            }
         }
      }
      return parallelWordsByRecEntity;
   }

   /**
    * @param entity
    * @return the list of words
    */
   private List<String> getWordForEntity (IWeightedEntity entity) {
      List<String> wordList = new ArrayList<String>(1);
      if (entity instanceof PWEntity) {
         return wordList;
      }

      // TODO: NK/VG: Should we use sfl name to singularize/pluralize here??
      String word = entity instanceof SFLEntity ? ((SFLEntity) entity).getSflName() : ((RecognitionEntity) entity)
               .getWord();
      wordList.add(word);
      Inflector inflector = new Inflector();
      String singular = inflector.singularize(word);
      if (!singular.equalsIgnoreCase(word)) {
         wordList.add(singular);
      }
      String plural = inflector.pluralize(word);
      if (!plural.equalsIgnoreCase(word)) {
         wordList.add(plural);
      }
      return wordList;
   }

   @Override
   protected ProcessorInput initializeProcessorInput (ProcessorInput processorInput) {
      return processorInput;
   }

   @Override
   protected boolean isValidForExecution (Summary input) {
      return true;
   }

   @Override
   protected void updateProcessorInput (ProcessorInput input, ProcessorInput processorInput) {
      // Do nothing
   }

   /**
    * @return the nlpServiceHelper
    */
   public NLPServiceHelper getNlpServiceHelper () {
      return nlpServiceHelper;
   }

   /**
    * @param nlpServiceHelper
    *           the nlpServiceHelper to set
    */
   public void setNlpServiceHelper (NLPServiceHelper nlpServiceHelper) {
      this.nlpServiceHelper = nlpServiceHelper;
   }

   /**
    * @return the indexFarmService
    */
   public IndexFarmService getIndexFarmService () {
      return indexFarmService;
   }

   /**
    * @param indexFarmService
    *           the indexFarmService to set
    */
   public void setIndexFarmService (IndexFarmService indexFarmService) {
      this.indexFarmService = indexFarmService;
   }
}