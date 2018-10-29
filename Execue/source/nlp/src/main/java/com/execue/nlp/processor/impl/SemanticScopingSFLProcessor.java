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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.nlp.SFLInfo;
import com.execue.core.util.CombinationGenerator;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.ClusterInformation;
import com.execue.nlp.bean.entity.EntityFactory;
import com.execue.nlp.bean.entity.HitsUpdateInfo;
import com.execue.nlp.bean.entity.PWEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.SFLEntity;
import com.execue.nlp.bean.matrix.Summary;
import com.execue.nlp.bean.snowflake.SFLCluster;
import com.execue.nlp.bean.snowflake.SFLClusterPostionInformation;
import com.execue.nlp.bean.snowflake.SFLProcessorInput;
import com.execue.nlp.bean.snowflake.SFLSummary;
import com.execue.nlp.bean.snowflake.SFLWord;
import com.execue.nlp.configuration.INLPConfigurationService;
import com.execue.nlp.engine.barcode.token.TokenUtility;
import com.execue.nlp.exception.NLPExceptionCodes;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.helper.NLPServiceHelper;
import com.execue.nlp.processor.IndividualRecognitionProcessor;
import com.execue.nlp.util.NLPUtilities;
import com.execue.ontology.exceptions.OntologyException;
import com.execue.util.MathUtil;

/**
 * @version removed unused methods getGroupByList and getSflSummariesByCluster
 * @author Nihar
 */

public class SemanticScopingSFLProcessor extends IndividualRecognitionProcessor {

   private static final Logger      log                       = Logger.getLogger(SemanticScopingSFLProcessor.class);
   private INLPConfigurationService nlpConfigurationService;
   private NLPServiceHelper         nlpServiceHelper;
   private Long                     contextidForSingleWordSfl = -1l;

   /**
    * This method initializes the following list and map(s): SFL processor input recognition entities list i.e. without
    * ignore word related entities Existing sfls by sfl id map Existing rec entities by position map Ignore word rec
    * entities by position map
    */
   @Override
   protected ProcessorInput initializeProcessorInput (ProcessorInput input) {
      SFLProcessorInput sflProcessorInput = new SFLProcessorInput();
      sflProcessorInput.setSearchFilter(input.getSearchFilter());
      sflProcessorInput.setRecEntitiesToReConsider(input.getRecEntitiesToReConsider());

      Map<Long, List<SFLEntity>> existingSFLsById = sflProcessorInput.getExistingSFlsById();
      Map<Integer, List<RecognitionEntity>> existingEntitiesByPosition = sflProcessorInput
               .getExistingEntitiesByPosition();
      Map<Integer, List<RecognitionEntity>> ignoreWordEntitiesByPosition = sflProcessorInput.getTagEntities();
      List<IWeightedEntity> sflProcessorInputRecEntities = sflProcessorInput.getRecognitionEntities();

      List<IWeightedEntity> inputRecognitionEntities = input.getRecognitionEntities();
      for (IWeightedEntity weightedEntity : inputRecognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         if (isIgnoreEntity(recEntity)) {
            List<RecognitionEntity> tagRecEntities = ignoreWordEntitiesByPosition.get(recEntity.getPosition());
            if (tagRecEntities == null) {
               tagRecEntities = new ArrayList<RecognitionEntity>(1);
               ignoreWordEntitiesByPosition.put(recEntity.getStartPosition(), tagRecEntities);
            }
            tagRecEntities.add(recEntity);
         } else {
            sflProcessorInputRecEntities.add(recEntity);
         }
         if (recEntity.getEntityType() == RecognitionEntityType.SFL_ENTITY) {
            SFLEntity sflEntity = (SFLEntity) recEntity;
            List<SFLEntity> sfls = existingSFLsById.get(sflEntity.getSflTermId());
            if (sfls == null) {
               sfls = new ArrayList<SFLEntity>(1);
               existingSFLsById.put(sflEntity.getSflTermId(), sfls);
            }
            if (!sfls.contains(sflEntity)) {
               sfls.add(sflEntity);
            }
         } else {
            List<RecognitionEntity> existingRecEntities = existingEntitiesByPosition.get(recEntity.getStartPosition());
            if (existingRecEntities == null) {
               existingRecEntities = new ArrayList<RecognitionEntity>(1);
               existingEntitiesByPosition.put(recEntity.getStartPosition(), existingRecEntities);
            }
            existingRecEntities.add(recEntity);
         }
      }
      return sflProcessorInput;
   }

   /**
    * Method to process the input and finally add the processed output sfl recognition entities to the
    * outputRecognitionEntities list of the given processorInput
    */
   @Override
   protected void execute (ProcessorInput processorInput) {
      SFLProcessorInput input = (SFLProcessorInput) processorInput;

      // NOTE: NK: secondary words are not coming and not getting used as of now, same holds for sfl words list
      // Need to realize and rewrite in case if we want to use them
      if (input.getSflwordList().isEmpty()) {
         updateSFLWordListFromSecondary(input.getSflSecondaryWordList(), input.getSflwordList());
      }
      printList("SFL Ignore Word List", input.getTagEntities().values());
      printList("SFL Secondary Word List", input.getSflSecondaryWordList());

      // Get the sfl summaries by context (model group id)
      Map<Long, List<SFLSummary>> sflSummariesByContext = getSFLSummaries(input);

      if (MapUtils.isEmpty(sflSummariesByContext)) {
         return;
      }

      // Remove the single word sfl summaries as they are not needed in cluster logic
      List<SFLSummary> singleWordSfls = sflSummariesByContext.remove(contextidForSingleWordSfl);
      if (singleWordSfls == null) {
         singleWordSfls = new ArrayList<SFLSummary>();
      }
      for (Entry<Long, List<SFLSummary>> entry : sflSummariesByContext.entrySet()) {

         // Get the list of sfl summaries grouped under sfl cluster
         Map<SFLCluster, List<SFLSummary>> sflSummariesBySFLCluster = getSFLSummariesByCluster(input, entry.getValue(),
                  singleWordSfls);

         // Print cluster summary information
         if (log.isDebugEnabled()) {
            for (SFLCluster cluster : sflSummariesBySFLCluster.keySet()) {
               log.debug("Cluster ID " + cluster.getId());
               log.debug("Cluster Positions " + cluster.getPositions());
               printList("Cluster Summary List", sflSummariesBySFLCluster.get(cluster));
            }
         }

         // Apply the filter for each list of summaries in the cluster  
         Map<SFLCluster, List<SFLSummary>> sflSummariesBySFLClusterFinal = getFilteredSflSummariesBySFLCluster(sflSummariesBySFLCluster);

         Set<SFLSummary> filteredSflSummariesSet = ExecueCoreUtil.mergeCollectionAsSet(sflSummariesBySFLClusterFinal
                  .values());
         List<SFLSummary> filteredSflSummaries = new ArrayList<SFLSummary>(filteredSflSummariesSet);

         // Again get the list of sfl summaries grouped under sfl cluster
         // TODO: NK:: Not sure why we need to group the sfl summaries into the cluster second time???
         sflSummariesBySFLCluster = getSFLSummariesByCluster(input, filteredSflSummaries, singleWordSfls);

         // Print new summary
         if (log.isDebugEnabled()) {
            for (SFLCluster cluster : sflSummariesBySFLCluster.keySet()) {
               log.debug("Cluster ID " + cluster.getId());
               log.debug("Cluster Positions " + cluster.getPositions());
               printList("Cluster Summary List", sflSummariesBySFLCluster.get(cluster));
            }
         }

         // TODO Filtering the SFL list again as cluster may get changes and in new cluster
         // we may have SFL term with higher weight. For query "net sales, net income, total assets" problem was
         // visible as cluster after the second clustering we need to remove sflTerms for only total.
         sflSummariesBySFLClusterFinal = getFilteredSflSummariesBySFLCluster(sflSummariesBySFLCluster);

         // Finally, update the output with the cluster information for the each set of sfl summaries
         for (Entry<SFLCluster, List<SFLSummary>> sflSummariesForContextEntry : sflSummariesBySFLClusterFinal
                  .entrySet()) {
            SFLCluster cluster = sflSummariesForContextEntry.getKey();
            List<SFLSummary> sflSummaries = sflSummariesForContextEntry.getValue();
            if (log.isDebugEnabled()) {
               log.debug("Final List For Cluster : " + cluster.getId());
               log.debug(sflSummaries);
            }
            updateOutput(input, sflSummaries, cluster);
         }
      }
      if (CollectionUtils.isNotEmpty(singleWordSfls)) {
         updateOutputForSingleWordEntities(input, singleWordSfls);
      }
   }

   /**
    * @param sflSummariesBySFLCluster
    * @return the Map<SFLCluster, List<SFLSummary>>
    */
   private Map<SFLCluster, List<SFLSummary>> getFilteredSflSummariesBySFLCluster (
            Map<SFLCluster, List<SFLSummary>> sflSummariesBySFLCluster) {
      Map<SFLCluster, List<SFLSummary>> filteredSflSummariesBySFLCluster = new LinkedHashMap<SFLCluster, List<SFLSummary>>();
      if (MapUtils.isEmpty(sflSummariesBySFLCluster)) {
         return filteredSflSummariesBySFLCluster;
      }
      for (Entry<SFLCluster, List<SFLSummary>> entry : sflSummariesBySFLCluster.entrySet()) {
         SFLCluster cluster = entry.getKey();
         List<SFLSummary> sflSummaries = entry.getValue();
         List<SFLSummary> filteredSflSummaries = filterSflSummaries(sflSummaries);
         if (CollectionUtils.isEmpty(filteredSflSummaries)) {
            continue;
         }
         if (log.isDebugEnabled()) {
            log.debug("Filtered List For Cluster : " + cluster.getId());
            log.debug(filteredSflSummaries);
         }
         filteredSflSummariesBySFLCluster.put(cluster, filteredSflSummaries);
      }
      return filteredSflSummariesBySFLCluster;
   }

   private Map<Long, List<SFLSummary>> getSFLSummaries (SFLProcessorInput input) {

      Map<Long, List<SFLSummary>> summariesByContextId = new HashMap<Long, List<SFLSummary>>();
      // Creating the list of recognition entities which contains the normal recEntities and the tagEntities like 'of'
      // etc also.
      List<IWeightedEntity> recognitionEntities = new ArrayList<IWeightedEntity>();
      recognitionEntities.addAll(input.getRecognitionEntities());
      for (List<RecognitionEntity> tagRecEntities : input.getTagEntities().values()) {
         recognitionEntities.addAll(tagRecEntities);
      }

      // Guard condition to return if the input list is empty
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return summariesByContextId;
      }

      // Also add the recognition entities to reconsider to the actual recognition entities list
      recognitionEntities.addAll(input.getRecEntitiesToReConsider());

      Set<String> sflWords = new HashSet<String>(1);
      Map<String, List<Integer>> wordPositionMap = new HashMap<String, List<Integer>>(1);
      Map<String, List<RecognitionEntity>> recEntitiesByWordMap = new HashMap<String, List<RecognitionEntity>>();
      Map<String, List<RecognitionEntity>> recEntitiesByLinguisticRootMap = new HashMap<String, List<RecognitionEntity>>(
               1);

      // TODO: NK: Currently we are just populating the linguisticRootPW Map
      // but not using it anywhere. Need to check if really need this??
      Map<String, RIParallelWord> linguisticRootPWMap = new HashMap<String, RIParallelWord>();

      // Populate the required information for the above collections and maps from the input recognition entities
      populateRequiredInfoFromRecognitionEntities(recognitionEntities, sflWords, wordPositionMap, recEntitiesByWordMap,
               recEntitiesByLinguisticRootMap, linguisticRootPWMap);

      if (CollectionUtils.isEmpty(sflWords)) {
         return summariesByContextId;
      }

      List<SFLInfo> sflInfos = getSFLInfos(input, sflWords);

      if (CollectionUtils.isEmpty(sflInfos)) {
         return summariesByContextId;
      }

      // Populate the SFL summaries from the SFLInfo list
      summariesByContextId = populateSFLSummariesFromInfoList(sflInfos, wordPositionMap, recEntitiesByWordMap,
               recEntitiesByLinguisticRootMap);
      return summariesByContextId;
   }

   /**
    * @param input
    * @param sflWords
    * @return the List<SFLInfo>
    */
   private List<SFLInfo> getSFLInfos (SFLProcessorInput input, Set<String> sflWords) {
      List<SFLInfo> sflInfos = new ArrayList<SFLInfo>();
      long starttime = System.currentTimeMillis();
      try {
         // Get the model group Ids for App specified in the search filter(if any)
         Set<Long> modelGroupIds = getNlpServiceHelper().getModelGroupIdsBySearchFilter(input.getSearchFilter());
         sflInfos = getKdxRetrievalService().getSFLInfoForWords(new ArrayList<String>(sflWords), modelGroupIds);
      } catch (Exception e) {
         log.error(e);
      }
      long endtime = System.currentTimeMillis();
      if (log.isInfoEnabled()) {
         log.info("Time Take to run DB Query to SFL # " + (endtime - starttime));
      }
      return sflInfos;
   }

   /**
    * @param recognitionEntities
    * @param allWords
    * @param wordPositionMap
    * @param recEntitiesByWordMap
    * @param recEntitiesByLinguisticRootMap
    * @param linguisticRootPWMap
    */
   private void populateRequiredInfoFromRecognitionEntities (List<IWeightedEntity> recognitionEntities,
            Set<String> allWords, Map<String, List<Integer>> wordPositionMap,
            Map<String, List<RecognitionEntity>> recEntitiesByWordMap,
            Map<String, List<RecognitionEntity>> recEntitiesByLinguisticRootMap,
            Map<String, RIParallelWord> linguisticRootPWMap) {
      // Here we are taking all the words and its linguistic root word from recognitionEntities.
      for (IWeightedEntity recognitionEntity : recognitionEntities) {
         Set<String> currentWords = new HashSet<String>();
         RecognitionEntity recEntity = (RecognitionEntity) recognitionEntity;
         List<String> possibleWords = TokenUtility.getAllPossibleWordValuesForSFLSemanticScoping(recEntity,
                  recEntitiesByWordMap);
         currentWords.addAll(possibleWords);
         List<String> possibleLinguisticRootWords = TokenUtility
                  .getAllPossibleLinguisticRootWordValuesForSFLSemanticScoping(recEntity, linguisticRootPWMap,
                           recEntitiesByLinguisticRootMap);
         currentWords.addAll(possibleLinguisticRootWords);

         for (String word : currentWords) {
            List<Integer> wordPositions = wordPositionMap.get(word.toLowerCase());
            if (wordPositions == null) {
               wordPositions = new ArrayList<Integer>(1);
               wordPositionMap.put(word.toLowerCase(), wordPositions);
            }
            wordPositions.add(recEntity.getPosition());
         }

         allWords.addAll(currentWords);
      }
   }

   /**
    * Method to populate the SFL Summaries from input SFL info list.
    * 
    * @param sflInfos
    * @param wordPositionMap
    * @param recEntitiesByWordMap
    * @param recEntitiesByLinguisticRootMap
    * @return the Map<Long, List<SFLSummary>>
    */
   private Map<Long, List<SFLSummary>> populateSFLSummariesFromInfoList (List<SFLInfo> sflInfos,
            Map<String, List<Integer>> wordPositionMap, Map<String, List<RecognitionEntity>> recEntitiesByWordMap,
            Map<String, List<RecognitionEntity>> recEntitiesByLinguisticRootMap) {
      List<SFLSummary> allSflSummaries = new ArrayList<SFLSummary>();
      for (SFLInfo sflInfo : sflInfos) {
         SFLSummary sflSummary = new SFLSummary();
         sflSummary.setSFLId(sflInfo.getSflId());
         sflSummary.setContextId(sflInfo.getContextId());
         sflSummary.setSFLName(sflInfo.getSflTerm());
         sflSummary.setSumWeight(sflInfo.getTotalWeight());
         sflSummary.setRequiredCount(sflInfo.getRequiredCount());
         int tokenIndex = 0;
         for (String sflTermToken : sflInfo.getTokens()) {
            List<Integer> positions = wordPositionMap.get(sflTermToken.toLowerCase());
            List<RecognitionEntity> recEntities = recEntitiesByWordMap.get(sflTermToken.toLowerCase());
            if (CollectionUtils.isEmpty(recEntities)) {
               recEntities = recEntitiesByLinguisticRootMap.get(sflTermToken.toLowerCase());
               sflSummary.setFromAlteredToken(true);
            }

            List<IWeightedEntity> weightedEntities = new ArrayList<IWeightedEntity>();
            if (!CollectionUtils.isEmpty(recEntities)) {
               weightedEntities.addAll(recEntities);
            }
            Map<Integer, IWeightedEntity> recEntityByPositionMap = NLPUtilities
                     .getRecognitionEntityByPositionMap(weightedEntities);

            boolean fromLinguisticRoot = false;
            if (!CollectionUtils.isEmpty(positions)) {
               for (Integer posInteger : positions) {
                  // Adjust the weight with the corresponding rec entity weight
                  double weight = sflInfo.getWeights().get(tokenIndex);
                  RecognitionEntity fromEntity = (RecognitionEntity) recEntityByPositionMap.get(posInteger);
                  if (fromEntity != null) {
                     weight = weight * getAdjustedWeightForToken(fromEntity, sflTermToken, fromLinguisticRoot);
                  }
                  sflSummary.addWordPosition(posInteger);
                  sflSummary.addWeight(weight);
                  sflSummary.putPositionOrder(posInteger, sflInfo.getOrders().get(tokenIndex));
                  sflSummary.putPositionWeight(posInteger, weight);
                  sflSummary.putTokenPosition(sflTermToken, posInteger);
                  sflSummary.putRequiredTokenPosition(posInteger, sflInfo.getRequiredTokens().get(tokenIndex));
                  sflSummary.putPrimaryTokenPosition(posInteger, sflInfo.getPrimaryTokens().get(tokenIndex));
                  // add the token id for the position this will come handy while preparing the HitsUpdateInfo object.
                  sflSummary.putPositionTokenId(posInteger, sflInfo.getTokenIds().get(tokenIndex));

                  // Check only if fromAlteredToken flag is not yet set in the sflSummary
                  if (!sflSummary.isFromAlteredToken() && fromEntity != null) {
                     sflSummary.setFromAlteredToken(fromEntity.isTokenAltered());
                  }
               }
            }
            tokenIndex++;
         }
         // If the SflSummary positions are empty or the weight is not assigned to any of the positions simply ignore
         // the SFL. This was done to take care of SFLs from different char set where SQL matches the terms but java
         // can't compare.
         if (CollectionUtils.isEmpty(sflSummary.getWordPositions())
                  || CollectionUtils.isEmpty(sflSummary.getPositionWeight().keySet())) {
            continue;
         }

         // Split the SFL Term based on the current frequency of a token in sfl summary and the actual number of times
         // that should appear in the sfl term.
         // This is an important step where we remove the Unwanted positions from SFL summary. i.e if a token is twice
         // in user query but only once then one position from the Summary needs to be removed.
         List<SFLSummary> sflSummaries = splitSummaryByTokenFrequency(sflSummary);

         // Filters the summary with non-continuous word positions
         sflSummaries = filterSummariesIfContinuousSFLFound(sflSummaries);

         // Remove the conjunction position if present as start or end in the sfl summary
         // We also update the sfl summary based on the current word positions we have in them
         sflSummaries = removeConjunctionPositionIfAtStartOrEnd(sflSummaries);

         // Here we keep splitting the sfl summary until we have the tokens within proximity limit in each sfl summary
         for (SFLSummary summary : sflSummaries) {
            allSflSummaries.addAll(splitSummaryByTokenProximity(summary));
         }
      }

      Map<Long, List<SFLSummary>> sflSummariesByContextId = getSFLSummaryByContextId(allSflSummaries);
      return sflSummariesByContextId;
   }

   /**
    * Method returns the map where key is context id(model group id) or -1(for single word sfls) 
    * and value is list of sfl summaries belong to that context. It also filters out some of the sfl summaries if 
    * they do not refer to any word positions or required token count is not present.
    * 
    * @param allSflSummaries
    * @return the Map<Long, List<SFLSummary>>   
    */
   private Map<Long, List<SFLSummary>> getSFLSummaryByContextId (List<SFLSummary> allSflSummaries) {
      // Store the single word sfl summary and multi word sfl summary in the map where context id(model group id) is key
      // For single word sfl summary set the context id as -1
      Map<Long, List<SFLSummary>> sflSummariesByContextId = new HashMap<Long, List<SFLSummary>>(1);
      for (SFLSummary summary : allSflSummaries) {

         // Skip the summary if no word positions are referred
         if (CollectionUtils.isEmpty(summary.getWordPositions())
                  || CollectionUtils.isEmpty(summary.getPositionWeight().keySet())) {
            continue;
         }

         // Filter the summary if required number of tokens are not present in the summary
         boolean filterSummary = filterSummaryByRequiredTokenCount(summary);
         if (filterSummary) {
            continue;
         }

         // Re-calculate the weight of the summary
         perfromRecalculationForWeights(summary);

         Long contextId = null;
         if (summary.getWordPositions().size() > 1) {
            contextId = summary.getContextId();
         } else {
            contextId = contextidForSingleWordSfl;
         }
         List<SFLSummary> sflSummaries = sflSummariesByContextId.get(contextId);
         if (sflSummaries == null) {
            sflSummaries = new ArrayList<SFLSummary>(1);
            sflSummariesByContextId.put(contextId, sflSummaries);
         }
         sflSummaries.add(summary);
      }
      return sflSummariesByContextId;
   }

   private boolean checkAndUpdateExistingSfls (SFLProcessorInput input, SFLSummary sfSummary) {
      boolean summaryEnhanced = false;
      Map<Long, List<SFLEntity>> existingSFlsById = input.getExistingSFlsById();
      if (existingSFlsById.containsKey(sfSummary.getSFLId())) {
         List<SFLEntity> sflEntities = existingSFlsById.get(sfSummary.getSFLId());
         for (SFLEntity sflEntity : sflEntities) {
            ReferedTokenPosition newRTP = new ReferedTokenPosition(sflEntity.getReferedTokenPositions());
            if (newRTP.isOverLap(new ReferedTokenPosition(sfSummary.getWordPositions()))) {
               continue;
            }
            if (sflEntity.getConsideredParts().containsAll(sfSummary.getPositionOrder().values())) {
               continue;
            }
            newRTP.addAll(sfSummary.getWordPositions());
            if (CollectionUtils.isEmpty(newRTP.getInBetweenPos())) {
               sflEntity.setReferedTokenPositions(newRTP.getReferedTokenPositionsList());
               double quality = sflEntity.getWeightInformation().getRecognitionQuality() + sfSummary.getSumWeight()
                        / 100;
               double weight = sflEntity.getWeightInformation().getRecognitionWeight() + NLPConstants.MAX_WEIGHT
                        * sfSummary.getWordPositions().size();
               sflEntity.getWeightInformation().setRecognitionQuality(quality);
               sflEntity.getWeightInformation().setRecognitionWeight(weight);
               addEntityInOuput(input, sflEntity.getClusterInformation(), sflEntity);
               summaryEnhanced = true;
            }
         }
      }
      return summaryEnhanced;
   }

   private List<SFLSummary> filterSummaryListAndCheckForEnhancement (SFLProcessorInput input,
            List<SFLSummary> sflSummaryList) {
      List<SFLSummary> summariesToBeRemoved = new ArrayList<SFLSummary>(1);
      Map<Long, Map<Double, List<SFLSummary>>> sflsByWeightForContext = new HashMap<Long, Map<Double, List<SFLSummary>>>(
               1);
      for (SFLSummary summary : sflSummaryList) {
         if (CollectionUtils.isEmpty(summary.getWordPositions())) {
            summariesToBeRemoved.add(summary);
            continue;
         }

         // Filter the summary by required token count
         boolean filter = filterSummaryByRequiredTokenCount(summary);
         if (filter) {
            summariesToBeRemoved.add(summary);
            continue;
         }

         boolean summaryExisting = checkIfRecEntityExists(input.getExistingEntitiesByPosition(), summary);
         if (summaryExisting) {
            summariesToBeRemoved.add(summary);
         } else {
            boolean summaryUsed = checkAndUpdateExistingSfls(input, summary);
            if (summaryUsed) {
               summariesToBeRemoved.add(summary);
            } else {
               Map<Double, List<SFLSummary>> summariesByWeight = sflsByWeightForContext.get(summary.getContextId());
               if (summariesByWeight == null) {
                  summariesByWeight = new HashMap<Double, List<SFLSummary>>(1);
                  sflsByWeightForContext.put(summary.getContextId(), summariesByWeight);
               }
               List<SFLSummary> summaryList = summariesByWeight.get(summary.getSumWeight());
               if (summaryList == null) {
                  summaryList = new ArrayList<SFLSummary>(1);
                  summariesByWeight.put(summary.getSumWeight(), summaryList);
               }
               summaryList.add(summary);
            }
         }
      }
      sflSummaryList.removeAll(summariesToBeRemoved);
      List<SFLSummary> filterSummaryList = new ArrayList<SFLSummary>();
      for (Map<Double, List<SFLSummary>> summariesInContext : sflsByWeightForContext.values()) {
         List<Double> weightVals = new ArrayList<Double>(1);
         for (Entry<Double, List<SFLSummary>> entry : summariesInContext.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
               weightVals.add(entry.getKey());
            }
         }
         Set<Double> topCluster = new HashSet<Double>(1);
         topCluster.addAll(MathUtil.getTopCluster(weightVals));
         for (Double weDouble : topCluster) {
            filterSummaryList.addAll(summariesInContext.get(weDouble));
         }
      }
      printList("After Filter Top Cluster List", filterSummaryList);

      return filterSummaryList;
   }

   private boolean checkIfRecEntityExists (Map<Integer, List<RecognitionEntity>> existingEntitiesByPosition,
            SFLSummary summary) {
      List<RecognitionEntity> entities = existingEntitiesByPosition.get(summary.getWordPositions().get(0));
      if (!CollectionUtils.isEmpty(entities)) {
         for (RecognitionEntity recognitionEntity : entities) {
            if (recognitionEntity.getEntityType() == RecognitionEntityType.PW_ENTITY) {
               PWEntity pwEntity = (PWEntity) recognitionEntity;
               Map<String, RIParallelWord> wordValues = pwEntity.getWordValues();
               for (RIParallelWord pwWord : wordValues.values()) {
                  if (pwWord.getEquivalentWord().equalsIgnoreCase(summary.getSFLName())
                           || pwWord.getWord().equalsIgnoreCase(summary.getSFLName())) {
                     return true;
                  }
               }
            }
         }
      }
      return false;
   }

   /**
    * Method to prepare the HitsUpdateInfo object for SFL entity. it will contain all the participating SFl tokens.
    * 
    * @param entity
    * @param tokenIds
    */
   private void populateHitsInfo (RecognitionEntity entity, Collection<Long> tokenIds) {
      if (CollectionUtils.isEmpty(tokenIds)) {
         return;
      }
      HitsUpdateInfo hitsUpdateInfo = new HitsUpdateInfo();
      for (Long tokenId : tokenIds) {
         hitsUpdateInfo.getSflTokensIds().add(tokenId);
      }
      entity.setHitsUpdateInfo(hitsUpdateInfo);
   }

   private Map<List<Integer>, List<SFLSummary>> getSFLSummariesByPosition (List<SFLSummary> summaryList) {
      Map<List<Integer>, List<SFLSummary>> positionsSummaryMap = new HashMap<List<Integer>, List<SFLSummary>>(1);
      for (SFLSummary summary : summaryList) {
         List<SFLSummary> summaries = positionsSummaryMap.get(summary.getWordPositions());
         if (CollectionUtils.isEmpty(summaries)) {
            summaries = new ArrayList<SFLSummary>(1);
         }
         summaries.add(summary);
         positionsSummaryMap.put(summary.getWordPositions(), summaries);
      }
      return positionsSummaryMap;
   }

   private void updateOutput (SFLProcessorInput input, List<SFLSummary> sflSummaries, SFLCluster cluster) {
      if (CollectionUtils.isEmpty(sflSummaries)) {
         return;
      }
      int startPos = cluster.getPositions().get(0);
      int endPos = cluster.getPositions().get(cluster.getPositions().size() - 1);
      int clusterId = cluster.getId();
      ClusterInformation clusterInformation = new ClusterInformation();
      clusterInformation.setClusterId(clusterId);
      clusterInformation.setClusterStartPosition(startPos);
      clusterInformation.setClusterEndPosition(endPos);
      for (SFLSummary summary : sflSummaries) {
         // TODO -NA- how to take the previous recognition quality in consideration. i.e. if SFL is recognized from
         // parallel word.consider the quality of tokens participating. from there previous recognitions(PW or direct
         // token.)
         RecognitionEntity entity = getSFLEntityForSummary(input, summary);
         // We will not set clusterInformation in the entity for that entity whose referedTokenPosoition are same as the
         // one in cluster.
         if (!entity.getReferedTokenPositions().containsAll(cluster.getPositions())) {
            entity.setClusterInformation(clusterInformation);
         }
         // Here we are adding the entity into processorInput's outputRecognitionEntities.
         input.addOutputRecognitionEntity(entity);
      }
   }

   private void updateOutputForSingleWordEntities (SFLProcessorInput input, List<SFLSummary> summaryList) {
      Map<List<Integer>, List<SFLSummary>> positionSummaryMap = getSFLSummariesByPosition(summaryList);
      for (Entry<List<Integer>, List<SFLSummary>> entry : positionSummaryMap.entrySet()) {
         List<SFLSummary> sflSummaryList = entry.getValue();
         sflSummaryList = filterSummaryListAndCheckForEnhancement(input, sflSummaryList);
         for (SFLSummary summary : sflSummaryList) {
            // TODO -NA- how to take the previous recognition quality in consideration. i.e. if SFL is recognized from
            // parallel word.consider the quality of tokens participating. from there previous recognitions(PW or direct
            // token.)
            RecognitionEntity entity = getSFLEntityForSummary(input, summary);
            input.addOutputRecognitionEntity(entity);
         }
      }
   }

   /**
    * This method prepares the SFL Entity with the help of information in the given SFLProcessorInput and SFLSummary
    * 
    * @param input
    *           as SFLProcessorInput
    * @param summary
    *           as SFLSummary
    * @return the SFLEntity as RecognitionEntity
    */
   private RecognitionEntity getSFLEntityForSummary (SFLProcessorInput input, SFLSummary summary) {
      String sflName = summary.getSFLName();

      List<Integer> existingParts = new ArrayList<Integer>(1);
      existingParts.addAll(summary.getPositionOrder().values());

      // Reset the word positions in the summary using rtp to avoid duplicate positions and sort the word positions
      // present in the sfl summary
      ReferedTokenPosition rtp = new ReferedTokenPosition(summary.getWordPositions());
      summary.setWordPositions(rtp.getReferedTokenPositionsList());

      WeightInformation weightInformation = getWeightInformationFromSummary(summary);
      RecognitionEntity entity = EntityFactory.getRecognitionEntity(RecognitionEntityType.SFL_ENTITY, sflName,
               NLPConstants.NLP_TAG_SFL, input.getGroup().getNewGroupIdForEntity(NLPConstants.NLP_TAG_SFL));

      ((SFLEntity) entity).setSflTermId(summary.getSFLId());
      ((SFLEntity) entity).setContextId(summary.getContextId());
      ((SFLEntity) entity).setConsideredParts(existingParts);
      entity.setWeightInformation(weightInformation);
      entity.setTokenAltered(summary.isFromAlteredToken());
      entity.setReferedTokenPositions(summary.getWordPositions());
      entity.setStartPosition(rtp.getReferedTokenPositions().first());
      entity.setEndPosition(rtp.getReferedTokenPositions().last());

      populateHitsInfo(entity, summary.getPositionTokenId().values());
      return entity;
   }

   private WeightInformation getWeightInformationFromSummary (SFLSummary summary) {
      // Calculate the recognition quality and weight from the sfl summary and return the weight information
      double qualityFactor = NLPConstants.MAX_QUALITY;
      double recognitionQuality = summary.getSumWeight() / 100 * qualityFactor;
      double recognitionWeight = summary.getWordPositions().size() * NLPConstants.MAX_WEIGHT;
      WeightInformation weightInformation = new WeightInformation();
      weightInformation.setRecognitionWeight(recognitionWeight);
      weightInformation.setRecognitionQuality(recognitionQuality);
      return weightInformation;
   }

   /**
    * @param data
    * @param clusterInformation
    * @param recEntity
    */
   private void addEntityInOuput (SFLProcessorInput input, ClusterInformation clusterInformation,
            RecognitionEntity recEntity) {
      List<Integer> referedTokenPositions = new ArrayList<Integer>();
      referedTokenPositions.addAll(recEntity.getReferedTokenPositions());
      recEntity.setClusterInformation(clusterInformation);
      Collections.sort(referedTokenPositions);
      recEntity.setStartPosition(referedTokenPositions.get(0));
      recEntity.setEndPosition(referedTokenPositions.get(referedTokenPositions.size() - 1));
      recEntity.setReferedTokenPositions(referedTokenPositions);
      // Here we are adding the entity into processorInput's outputRecognitionEntities.
      input.addOutputRecognitionEntity(recEntity);
   }

   private void updateSFLWordListFromSecondary (List<SFLWord> sflSecondaryWordList, List<SFLWord> sflwordList) {
      sflwordList.addAll(sflSecondaryWordList);
      sflSecondaryWordList.clear();
   }

   /**
    * Gate keeper code For SnowFlake If any one token has as recognition entity don't run the processor
    */
   @Override
   protected boolean isValidForExecution (Summary input) {

      return true;
   }

   private void printList (String name, Collection<?> list) {
      if (log.isDebugEnabled()) {
         log.debug("List Name: " + name);
         for (Object aList : list) {
            log.debug(aList);
         }
         log.debug("End Printing List");
      }
   }

   /**
    * This method filters the sfl summaries based on below rules:
    * Filter by required token count
    * Filter if no primary tokens are present
    * Filter by word position i.e. Subset filtering
    * Filter by top cluster
    * 
    * @param sflsummaries
    * @return List<SFLSummary>
    */
   private List<SFLSummary> filterSflSummaries (List<SFLSummary> sflsummaries) {
      List<SFLSummary> filteredSflSummaries = new ArrayList<SFLSummary>();
      filteredSflSummaries.addAll(sflsummaries);
      List<SFLSummary> sflSummariesToBeRemoved = new ArrayList<SFLSummary>();

      for (SFLSummary summary : filteredSflSummaries) {
         // Filter by required token count
         boolean filter = filterSummaryByRequiredTokenCount(summary);
         if (filter) {
            sflSummariesToBeRemoved.add(summary);
         } else {
            // Filter if no primary tokens are present
            boolean isPrimaryTokenPresent = checkIfPrimaryTokenPresent(summary);
            if (!isPrimaryTokenPresent) {
               sflSummariesToBeRemoved.add(summary);
            }
         }
      }
      filteredSflSummaries.removeAll(sflSummariesToBeRemoved);

      // Filter by word position i.e. Subset filtering
      SFLSummary maxPositionSummary = null;
      List<SFLSummary> sflsummaryFinalList = new ArrayList<SFLSummary>();
      while ((maxPositionSummary = getMaxPositionSummary(filteredSflSummaries)) != null) {
         List<Integer> maxWordPostion = maxPositionSummary.getWordPositions();
         List<SFLSummary> sflsummaryGroupList = new ArrayList<SFLSummary>();
         sflSummariesToBeRemoved = new ArrayList<SFLSummary>();
         for (SFLSummary summary : filteredSflSummaries) {
            List<Integer> wordPostionList = summary.getWordPositions();

            if (maxWordPostion.containsAll(wordPostionList)) {
               if (wordPostionList.size() == maxWordPostion.size()) {
                  sflsummaryGroupList.add(summary);
               } else {
                  sflSummariesToBeRemoved.add(summary);
               }
            }
         }
         printList("Group List", sflsummaryGroupList);

         filteredSflSummaries.removeAll(sflSummariesToBeRemoved);
         filteredSflSummaries.removeAll(sflsummaryGroupList);

         Double minWeightForAddedSummaries = getMinWeightFromList(sflsummaryGroupList);
         for (SFLSummary sflSummary : sflSummariesToBeRemoved) {
            if (sflSummary.getSumWeight() > minWeightForAddedSummaries) {
               sflsummaryGroupList.add(sflSummary);
            }
         }
         sflsummaryFinalList.addAll(sflsummaryGroupList);
      }

      // Filter by top cluster
      List<SFLSummary> finalSFLSummaryList = filterByCluster(sflsummaryFinalList);

      // Sort the final sfl summaries by sum weight
      Collections.sort(finalSFLSummaryList, new Comparator<SFLSummary>() {

         public int compare (SFLSummary o1, SFLSummary o2) {
            return (int) (o2.getSumWeight() - o1.getSumWeight());
         }
      });

      return finalSFLSummaryList;
   }

   private Double getMinWeightFromList (List<SFLSummary> sflsummaryGroupList) {
      Double weight = null;
      for (SFLSummary summary : sflsummaryGroupList) {
         if (weight == null) {
            weight = summary.getSumWeight();
         } else if (weight > summary.getSumWeight()) {
            weight = summary.getSumWeight();
         }
      }
      return weight;

   }

   /**
    * Calling this new Method to Filter SFL summaries instead of filterByPercentile. filterByPercentile is still there
    * but is not getting called.
    * 
    * @param summaryList
    * @return the List<SFLSummary> 
    */
   private List<SFLSummary> filterByCluster (List<SFLSummary> summaryList) {
      List<SFLSummary> filterSummaryList = new ArrayList<SFLSummary>();
      List<Double> weightVals = new ArrayList<Double>();
      for (SFLSummary summary : summaryList) {
         weightVals.add(summary.getSumWeight());
      }
      List<Double> topCluster = MathUtil.getLiberalTopCluster(weightVals);
      for (SFLSummary summary : summaryList) {
         if (topCluster.contains(summary.getSumWeight())) {
            filterSummaryList.add(summary);
         }
      }
      printList("Filter by Liberal Top Cluster", filterSummaryList);
      return filterSummaryList;
   }

   private SFLSummary getMaxPositionSummary (List<SFLSummary> sflsummaryList) {
      SFLSummary retObject = null;
      if (sflsummaryList.size() > 0) {
         retObject = sflsummaryList.get(0);
         for (SFLSummary sflSummary : sflsummaryList) {
            if (sflSummary.getWordPositions().size() > retObject.getWordPositions().size()) {
               retObject = sflSummary;
            }
         }
      }
      return retObject;
   }

   private Map<SFLCluster, List<SFLSummary>> scopeSFLSummaryByCluster (List<SFLCluster> clusterList,
            List<SFLSummary> summaryList, List<Integer> ignoreWordPositions) {
      Map<SFLCluster, List<SFLSummary>> sflSummariesByCluster = new HashMap<SFLCluster, List<SFLSummary>>();
      for (SFLSummary summary : summaryList) {
         for (SFLCluster cluster : clusterList) {
            SFLSummary newSummary = null;
            if (cluster.getPositions().containsAll(summary.getWordPositions())) {
               newSummary = new SFLSummary();
               for (Integer position : summary.getWordPositions()) {
                  Double weight = summary.getPositionWeight().get(position);
                  if (weight != null) {
                     newSummary.addWordPosition(position);
                     newSummary.addWeight(weight);
                     newSummary.putPositionOrder(position, summary.getOrderByPosition(position));
                     newSummary.putPositionWeight(position, summary.getWeightByPosition(position));
                     newSummary.putPositionTokenId(position, summary.getTokenIdByPosition(position));
                     newSummary.putParticiaptingWordQuality(position, summary.getParticipatingWordQuality().get(
                              position));
                     newSummary.putRequiredTokenPosition(position, summary.getRequiredTokenByPosition().get(position));
                     newSummary.putPrimaryTokenPosition(position, summary.getPrimaryTokenByPosition().get(position));
                  }
               }
            } else {
               List<Integer> positionsInCluster = new ArrayList<Integer>();
               positionsInCluster.addAll(summary.getWordPositions());
               positionsInCluster.retainAll(cluster.getPositions());

               // If SFL contains only Ignore Word position then SFL should be discarded
               if (!CollectionUtils.isEmpty(positionsInCluster) && ignoreWordPositions.containsAll(positionsInCluster)) {
                  positionsInCluster.clear();
               }

               if (!CollectionUtils.isEmpty(positionsInCluster)) {
                  newSummary = new SFLSummary();
                  for (Integer position : positionsInCluster) {
                     newSummary.addWordPosition(position);
                     newSummary.addWeight(summary.getWeightByPosition(position));
                     newSummary.putPositionOrder(position, summary.getOrderByPosition(position));
                     newSummary.putPositionWeight(position, summary.getWeightByPosition(position));
                     newSummary.putPositionTokenId(position, summary.getTokenIdByPosition(position));
                     newSummary.putParticiaptingWordQuality(position, summary.getParticipatingWordQuality().get(
                              position));
                     newSummary.putRequiredTokenPosition(position, summary.getRequiredTokenByPosition().get(position));
                     newSummary.putPrimaryTokenPosition(position, summary.getPrimaryTokenByPosition().get(position));

                  }
               }
            }
            // Skip, if couldn't get the new summary for the cluster position as summary left could be only with ignore
            // word position(s), etc
            if (newSummary == null) {
               continue;
            }
            // TODO -AP- Temporary Fix for Words repeating in the Snowflakes.
            // Should check if Word is multiple times in Snowflakes then add weight that many times, else not
            // Temporary Fix Start
            for (Entry<String, List<Integer>> entry : summary.getTokenPositionsMap().entrySet()) {
               List<Integer> positionList = entry.getValue();
               positionList.retainAll(newSummary.getWordPositions());
               if (!CollectionUtils.isEmpty(positionList)) {
                  newSummary.getTokenPositionsMap().put(entry.getKey(), positionList);
               }
            }
            double weightReduction = Double.parseDouble(getNlpConfigurationService()
                     .getSnowflakeOutOfOrderWeightReduction());
            performWeightCorrectionIfSFLNotContinuous(newSummary, ignoreWordPositions);
            performWeightCorrectionForUnorderedSFLs(newSummary, weightReduction);
            if (newSummary.getSumWeight() > 100) {
               newSummary.setSumWeight(100.00);
               // Temporary Fix End
            }

            //            Map<Integer, Integer> requiredPositionMap = new HashMap<Integer, Integer>(1);
            //            requiredPositionMap.putAll(summary.getRequiredTokenByPosition());
            //
            //            Map<Integer, Integer> orderPositionMap = new HashMap<Integer, Integer>(1);
            //            orderPositionMap.putAll(summary.getPositionOrder());
            //
            //            Map<Integer, Integer> primaryTokenByPosition = new HashMap<Integer, Integer>(1);
            //            primaryTokenByPosition.putAll(summary.getPrimaryTokenByPosition());

            newSummary.setSFLId(summary.getSFLId());
            newSummary.setContextId(summary.getContextId());
            newSummary.setSFLName(summary.getSFLName());
            newSummary.setRequiredCount(summary.getRequiredCount());
            //            newSummary.setPositionOrder(orderPositionMap);
            //            newSummary.setRequiredTokenByPosition(requiredPositionMap);
            //            newSummary.setPrimaryTokenByPosition(primaryTokenByPosition);

            // validate the new sfl summary
            removeConjunctionPositionIfAtStartOrEnd(newSummary);
            // If the summary word position is empty, then continue
            if (CollectionUtils.isEmpty(newSummary.getWordPositions())) {
               continue;
            }

            boolean summaryFiltered = filterSummaryByRequiredTokenCount(newSummary);
            if (summaryFiltered) {
               continue;
            }

            // TODO: NK: Can check if the new SFL summary is spanning only single word position,
            // then should add it to the single word sfls instead of adding it to the cluster summary list
            // Now, anyways later in the logic, these single word sfl summary gets filtered from this cluster and
            // get copied to singleWordSfls list.
            List<SFLSummary> clusterSummaryList = sflSummariesByCluster.get(cluster);
            if (clusterSummaryList == null) {
               clusterSummaryList = new ArrayList<SFLSummary>();
               sflSummariesByCluster.put(cluster, clusterSummaryList);
            }
            // sort
            Collections.sort(newSummary.getWordPositions());
            clusterSummaryList.add(newSummary);
         }
      }
      return sflSummariesByCluster;
   }

   /**
    * Get the cluster list for the the passed list of SFL summaries.
    * 
    * @param data
    * @param sflSummaries
    * @return the Map<SFLCluster, List<SFLSummary>>
    * @throws OntologyException
    */
   private Map<SFLCluster, List<SFLSummary>> getSFLSummariesByCluster (SFLProcessorInput input,
            List<SFLSummary> sflSummaries, List<SFLSummary> singleWordSfls) {
      Map<SFLCluster, List<SFLSummary>> sflSummariesByCluster = new HashMap<SFLCluster, List<SFLSummary>>();
      if (CollectionUtils.isEmpty(sflSummaries)) {
         return sflSummariesByCluster;
      }
      // Sort the summary list to get summaries with less position on top
      Collections.sort(sflSummaries, new Comparator<SFLSummary>() {

         public int compare (SFLSummary s1, SFLSummary s2) {
            if (s1.getWordPositions().size() > s2.getWordPositions().size()) {
               return 1;
            }
            if (s1.getWordPositions().size() < s2.getWordPositions().size()) {
               return -1;
            }
            // TODO -NA- As of now just comparing the first position in the SFL Summary.
            // need to find a logic to put it correctly to compare all the positions.
            // this is done to eliminate randomness in results
            if (s1.getWordPositions().get(0) > s2.getWordPositions().get(0)) {
               return 1;
            }
            if (s1.getWordPositions().get(0) < s2.getWordPositions().get(0)) {
               return -1;
            }
            return 0;
         }
      });

      // List of possible cluster position information objects.
      List<SFLClusterPostionInformation> possibleClustersInfo = new ArrayList<SFLClusterPostionInformation>(1);

      // Map to keep the list of SFL Summaries participating in a possible Cluster.
      Map<SFLClusterPostionInformation, List<SFLSummary>> sflSummariesByClusterPositionInfo = new HashMap<SFLClusterPostionInformation, List<SFLSummary>>();

      // Create the non overlapping cluster first and populate the List of possibleCluster Info objects
      createPossibleClusterInfoForNonOverlappingPositions(sflSummaries, sflSummariesByClusterPositionInfo,
               possibleClustersInfo);

      // Create the other possible cluster info which intersects with other cluster info for the given summary list
      createPossibleClusterInfoForOverlappingPositions(sflSummaries, sflSummariesByClusterPositionInfo,
               possibleClustersInfo);

      // Method to add the ignore word positions in the Cluster position sets.
      addIgnoreWordPositionsToCluster(input.getTagEntities(), possibleClustersInfo);

      // Prepare the list for ignore word positions, currently we are setting the positions covered by the tag entities
      List<Integer> ignoreWordPositions = new ArrayList<Integer>(1);
      ignoreWordPositions.addAll(input.getTagEntities().keySet());

      // Create the SFLCluster based on the Position Set Created.
      sflSummariesByCluster = createSFLCLustersBasedOnPositionSets(possibleClustersInfo,
               sflSummariesByClusterPositionInfo, ignoreWordPositions);

      // Prepare the list of sfl cluster from the key of the map
      List<SFLCluster> clusterList = new ArrayList<SFLCluster>();
      clusterList.addAll(sflSummariesByCluster.keySet());

      // Remove the duplicate clusters
      removeDuplicateSFLClusters(clusterList, sflSummariesByCluster);

      // If Cluster contains only Ignore Word position then cluster should be discarded
      // TODO: NK: can get rid off it, as this case should not arise, as we are already removing the
      // SFL summaries with only ignore words during the createSFLCLustersBasedOnPositionSets
      removeClusterWithOnlyIgnoreWords(clusterList, sflSummariesByCluster, ignoreWordPositions);
      printList("Cluster List", clusterList);

      // Check if the cluster is for single word entities then add these entities to singleWordSfls.
      // Also update the sflSummariesByCluster accordingly
      checkAndRemoveSingleWordClusters(clusterList, sflSummariesByCluster, singleWordSfls);
      return sflSummariesByCluster;
   }

   /**
    * @param clusterList
    * @param sflSummariesByCluster
    * @param singleWordSfls
    */
   private void checkAndRemoveSingleWordClusters (List<SFLCluster> clusterList,
            Map<SFLCluster, List<SFLSummary>> sflSummariesByCluster, List<SFLSummary> singleWordSfls) {
      // If the cluster is for single word entities add these entities to singleWordSfls.
      // Will also populate the clustersToBeRemoved list for the valid cluster which are either
      // related to cluster spanning over only one position or cluster having all the single word sfl summaries
      List<SFLCluster> clustersToBeRemoved = new ArrayList<SFLCluster>();
      for (SFLCluster cluster : clusterList) {
         if (cluster.getPositions().size() == 1) {
            // In this we do not need to add to the clustersToBeRemoved list as we are directly removing if from
            // sflSummariesByCluster map and adding it to the singleWordSfls
            singleWordSfls.addAll(sflSummariesByCluster.remove(cluster));
         } else {
            List<SFLSummary> summaryListForCluster = sflSummariesByCluster.get(cluster);
            List<SFLSummary> summaryListToBeRemoved = new ArrayList<SFLSummary>(1);

            // If cluster size is not 1 then check if any of the participating summaries size is one and remove these
            // from cluster and add to the singleWordSFls.
            for (SFLSummary sflSummary : summaryListForCluster) {
               if (sflSummary.getWordPositions().size() == 1) {
                  singleWordSfls.add(sflSummary);
                  summaryListToBeRemoved.add(sflSummary);
               }
            }

            if (!CollectionUtils.isEmpty(summaryListToBeRemoved)) {
               // Remove all the summaryListToBeRemoved from the summaryListForCluster
               summaryListForCluster.removeAll(summaryListToBeRemoved);
               // If summaryListForCluster is empty, then add the cluster to the clustersToBeRemoved list
               // to eventually remove it from the sflSummariesByCluster map
               if (CollectionUtils.isEmpty(summaryListForCluster)) {
                  clustersToBeRemoved.add(cluster);
               }
            }
         }
      }

      // Remove the cluster with single word from the cluster summary map as they no longer are cluster and already
      // added as single word sfls
      for (SFLCluster clusterToBeRemoved : clustersToBeRemoved) {
         sflSummariesByCluster.remove(clusterToBeRemoved);
      }
   }

   /**
    * @param sflSummaries
    * @param sflSummariesByClusterPositionInfoMap
    * @param possibleClustersInfo
    */
   private void createPossibleClusterInfoForOverlappingPositions (List<SFLSummary> sflSummaries,
            Map<SFLClusterPostionInformation, List<SFLSummary>> sflSummariesByClusterPositionInfoMap,
            List<SFLClusterPostionInformation> possibleClustersInfo) {
      if (CollectionUtils.isEmpty(sflSummaries)) {
         return;
      }
      for (SFLSummary summary : sflSummaries) {
         Collections.sort(summary.getWordPositions());
         List<Integer> wordPositions = summary.getWordPositions();
         boolean clusterExistForSummary = false;
         // check if cluster already exists for the summary, then just add the summary to the ClusterPosition.
         for (SFLClusterPostionInformation clusterPostionInformation : possibleClustersInfo) {
            Set<Integer> positionSet = clusterPostionInformation.getClusterPositionSet();
            if (positionSet.containsAll(wordPositions)
                     && intersectWithSummary(clusterPostionInformation, wordPositions)) {
               clusterExistForSummary = true;
               // add the Summary to SummaryMap for the cluster.
               List<SFLSummary> existingSflSummaries = sflSummariesByClusterPositionInfoMap
                        .get(clusterPostionInformation);
               if (existingSflSummaries == null) {
                  existingSflSummaries = new ArrayList<SFLSummary>();
                  sflSummariesByClusterPositionInfoMap.put(clusterPostionInformation, existingSflSummaries);
               }
               existingSflSummaries.add(summary);
               // add the Summary Positions to SummaryPositionMap for the cluster.
               clusterPostionInformation.getSummaryPosList().add(summary.getWordPositions());
               clusterPostionInformation.getIntersectingPosition().retainAll(summary.getWordPositions());
               clusterPostionInformation.getClusterPositionSet().addAll(summary.getWordPositions());
            }
         }

         // continue, if cluster already exists
         if (clusterExistForSummary) {
            continue;
         }

         // If cluster does not exist for the SFL Summary try to get intersecting clusters.
         Set<Integer> intersectingPositionSet = new TreeSet<Integer>();
         List<SFLClusterPostionInformation> interSectingCluster = new ArrayList<SFLClusterPostionInformation>(1);
         List<List<Integer>> summariesPositionsInNewCluster = new ArrayList<List<Integer>>();
         List<SFLSummary> sflSummariesInNewCluster = new ArrayList<SFLSummary>();
         for (SFLClusterPostionInformation clusterPostionInformation : possibleClustersInfo) {
            Set<Integer> positionSet = clusterPostionInformation.getClusterPositionSet();
            if (intersectWithSummary(clusterPostionInformation, wordPositions)) {
               intersectingPositionSet.addAll(positionSet);
               interSectingCluster.add(clusterPostionInformation);
               summariesPositionsInNewCluster.addAll(clusterPostionInformation.getSummaryPosList());
               sflSummariesInNewCluster.addAll(sflSummariesByClusterPositionInfoMap.get(clusterPostionInformation));
            }
         }

         // If intersecting clusters found
         if (CollectionUtils.isNotEmpty(interSectingCluster)) {
            // If there is only one intersecting cluster then will create a new cluster with union of existing
            // cluster and summary word positions.
            if (interSectingCluster.size() == 1) {
               intersectingPositionSet.addAll(wordPositions);
               summariesPositionsInNewCluster.add(wordPositions);
               sflSummariesInNewCluster.add(summary);
               // add the entry to the map to contain the new cluster and list of participating summaries.
               SFLClusterPostionInformation clusterPostionInformation = interSectingCluster.get(0);
               clusterPostionInformation.setSummaryPosList(summariesPositionsInNewCluster);
               clusterPostionInformation.getIntersectingPosition().retainAll(wordPositions);
               clusterPostionInformation.getClusterPositionSet().addAll(wordPositions);
               sflSummariesByClusterPositionInfoMap.put(clusterPostionInformation, sflSummariesInNewCluster);
            } else {
               // if summary spans over more then one cluster then a new cluster will get created for Union of
               // each cluster with summary Position.
               for (SFLClusterPostionInformation clusterPostionInformation : interSectingCluster) {
                  Set<Integer> positionSet = clusterPostionInformation.getClusterPositionSet();
                  // for each intersecting cluster create a new cluster with union of SFL summary and cluster
                  // position set.
                  Set<Integer> newPositionSet = new TreeSet<Integer>();
                  newPositionSet.addAll(wordPositions);
                  newPositionSet.addAll(positionSet);
                  List<SFLSummary> summariesForNewCluster = new ArrayList<SFLSummary>();
                  summariesForNewCluster.addAll(sflSummariesByClusterPositionInfoMap.get(clusterPostionInformation));
                  List<List<Integer>> summaryPositionsInNewCluster = new ArrayList<List<Integer>>();
                  summaryPositionsInNewCluster.addAll(clusterPostionInformation.getSummaryPosList());
                  summariesForNewCluster.add(summary);
                  summaryPositionsInNewCluster.add(wordPositions);
                  // add the new cluster in the map. In this scenario as old cluster and new cluster both will be
                  // there.
                  SFLClusterPostionInformation clusterPostionInformationNew = new SFLClusterPostionInformation();
                  clusterPostionInformationNew.setSummaryPosList(summaryPositionsInNewCluster);
                  // get The InterSecting positions for the old Cluster. set the new InterSecting position for new
                  // Cluster Information by taking intersection of oldInterSection list with new Summary position.
                  List<Integer> interSectingPosition = new ArrayList<Integer>();
                  interSectingPosition.addAll(clusterPostionInformation.getIntersectingPosition());
                  interSectingPosition.retainAll(wordPositions);
                  // set the intersecting positions in the new created clusterPositionInfo object.
                  clusterPostionInformationNew.setIntersectingPosition(interSectingPosition);
                  clusterPostionInformationNew.setClusterPositionSet(newPositionSet);
                  possibleClustersInfo.add(clusterPostionInformationNew);
                  sflSummariesByClusterPositionInfoMap.put(clusterPostionInformationNew, summariesForNewCluster);
               }
            }
         } else {
            // If no intersecting clusters found, then create the new cluster
            createPossibleCluster(sflSummariesByClusterPositionInfoMap, possibleClustersInfo,
                     new ArrayList<SFLSummary>(), summary);
         }
      }
   }

   /**
    * Method to populate the list of possibleClustersInfo and map of sflSummariesByClusterPositionInfo for the non
    * overlapping positions in the input list of SFL summaries
    * 
    * @param sflSummaries
    * @param sflSummariesByClusterPositionInfo
    * @param possibleClustersInfo
    */
   private void createPossibleClusterInfoForNonOverlappingPositions (List<SFLSummary> sflSummaries,
            Map<SFLClusterPostionInformation, List<SFLSummary>> sflSummariesByClusterPositionInfo,
            List<SFLClusterPostionInformation> possibleClustersInfo) {
      if (CollectionUtils.isEmpty(sflSummaries)) {
         return;
      }
      List<SFLSummary> addressedSummaryList = new ArrayList<SFLSummary>();
      for (SFLSummary summary : sflSummaries) {
         Collections.sort(summary.getWordPositions());
         List<Integer> wordPositions = summary.getWordPositions();

         boolean clusterExistForSummary = false;
         for (SFLClusterPostionInformation clusterPostionInformation : possibleClustersInfo) {
            Set<Integer> positionSet = clusterPostionInformation.getClusterPositionSet();
            // check if the summary is of same size of the existing cluster and spans over the same positions.
            if (positionSet.containsAll(wordPositions) && positionSet.size() == wordPositions.size()) {
               clusterExistForSummary = true;
               // add the summary to SummaryMap for the cluster.
               List<SFLSummary> existingSflSummaries = sflSummariesByClusterPositionInfo.get(clusterPostionInformation);
               if (CollectionUtils.isEmpty(existingSflSummaries)) {
                  existingSflSummaries = new ArrayList<SFLSummary>();
               }
               existingSflSummaries.add(summary);
               addressedSummaryList.add(summary);
               sflSummariesByClusterPositionInfo.put(clusterPostionInformation, existingSflSummaries);
               // add the Summary Positions to SummaryPositionMap for the cluster.
               clusterPostionInformation.getSummaryPosList().add(wordPositions);
               clusterPostionInformation.getIntersectingPosition().retainAll(wordPositions);
               clusterPostionInformation.getClusterPositionSet().addAll(wordPositions);
               break;
            }
         }

         if (clusterExistForSummary) {
            continue;
         }

         boolean overlappingCluster = false;
         for (SFLClusterPostionInformation clusterPostionInformation : possibleClustersInfo) {
            if (intersectWithSummary(clusterPostionInformation, wordPositions)) {
               overlappingCluster = true;
               break;
            }
         }
         if (!overlappingCluster) {
            createPossibleCluster(sflSummariesByClusterPositionInfo, possibleClustersInfo, addressedSummaryList,
                     summary);
         }
      }
      sflSummaries.removeAll(addressedSummaryList);
   }

   private void createPossibleCluster (Map<SFLClusterPostionInformation, List<SFLSummary>> clusterSummaryIdsMap,
            List<SFLClusterPostionInformation> possibleClustersInfo, List<SFLSummary> addressedSummaryList,
            SFLSummary summary) {
      // If no intersecting cluster is there create a new cluster for the summary word positions
      List<Integer> wordPositions = summary.getWordPositions();
      List<List<Integer>> summaryPositions = new ArrayList<List<Integer>>();
      summaryPositions.add(wordPositions);

      List<Integer> intersectingPositions = new ArrayList<Integer>();
      intersectingPositions.addAll(wordPositions);

      // Create a new SFLClusterPostionInformation Object
      SFLClusterPostionInformation clusterPositionInformation = new SFLClusterPostionInformation();
      clusterPositionInformation.setSummaryPosList(summaryPositions);
      clusterPositionInformation.setIntersectingPosition(intersectingPositions);
      clusterPositionInformation.getClusterPositionSet().addAll(wordPositions);

      // Add to the possibleClustersInfo list
      possibleClustersInfo.add(clusterPositionInformation);

      // Add the summary in the clusterSummaryIdsMap
      List<SFLSummary> existingSummaries = clusterSummaryIdsMap.get(clusterPositionInformation);
      if (existingSummaries == null) {
         existingSummaries = new ArrayList<SFLSummary>();
         clusterSummaryIdsMap.put(clusterPositionInformation, existingSummaries);
      }
      existingSummaries.add(summary);

      // Also add the summary to the addressed summary list
      addressedSummaryList.add(summary);
   }

   /**
    * Method to remove the Cluster Which just Contains the Ignore Words.
    * 
    * @param clusterList
    * @param clusterSummaryListMap
    * @param ignoreWordPositions
    */
   private void removeClusterWithOnlyIgnoreWords (List<SFLCluster> clusterList,
            Map<SFLCluster, List<SFLSummary>> clusterSummaryListMap, List<Integer> ignoreWordPositions) {
      List<SFLCluster> ignoreWordClusters = new ArrayList<SFLCluster>();
      for (SFLCluster cluster : clusterList) {
         if (ignoreWordPositions.containsAll(cluster.getPositions())) {
            ignoreWordClusters.add(cluster);
         }
      }

      for (SFLCluster ignoreWordCluster : ignoreWordClusters) {
         clusterList.remove(ignoreWordCluster);
         clusterSummaryListMap.remove(ignoreWordCluster);
      }
   }

   /**
    * Method to merge The SFL cluster which spans over the same positions. o
    * 
    * @param clusterList
    * @param clusterSummaryListMap
    */
   private void removeDuplicateSFLClusters (List<SFLCluster> clusterList,
            Map<SFLCluster, List<SFLSummary>> clusterSummaryListMap) {

      // Guard condition, to skip the remove duplicate cluster logic
      if (CollectionUtils.isEmpty(clusterList) || clusterList.size() <= 1) {
         return;
      }

      List<SFLCluster> duplicateSFLClusters = new ArrayList<SFLCluster>();
      for (int i = 0; i < clusterList.size(); i++) {
         SFLCluster sflCluster1 = clusterList.get(i);
         for (int j = i + 1; j < clusterList.size(); j++) {
            SFLCluster sflCluster2 = clusterList.get(j);
            if (sflCluster1.getPositions().equals(sflCluster2.getPositions())) {
               // Add the first cluster to the duplicate clusters list
               duplicateSFLClusters.add(sflCluster1);

               // Group the SFL summaries from each of the cluster into the set of groupedClusterSummary
               Set<SFLSummary> groupedClusterSummary = new HashSet<SFLSummary>();
               List<SFLSummary> cluster1Summaries = clusterSummaryListMap.get(sflCluster1);
               List<SFLSummary> cluster2Summaries = clusterSummaryListMap.get(sflCluster2);
               if (!CollectionUtils.isEmpty(cluster1Summaries)) {
                  groupedClusterSummary.addAll(cluster1Summaries);
               }
               if (!CollectionUtils.isEmpty(cluster2Summaries)) {
                  groupedClusterSummary.addAll(cluster2Summaries);
               }
               List<SFLSummary> newSummaries = new ArrayList<SFLSummary>();
               newSummaries.addAll(groupedClusterSummary);
               clusterSummaryListMap.put(sflCluster2, newSummaries);
               break;
            }
         }
      }

      for (SFLCluster duplicateSFLCluster : duplicateSFLClusters) {
         clusterList.remove(duplicateSFLCluster);
         clusterSummaryListMap.remove(duplicateSFLCluster);
      }
   }

   /**
    * Method to create the SFL Cluster based on the position sets created. For each position set a new Cluster will be
    * created if the position set in continuous. If the positions in the position set are not consecutive then more then
    * one clusters will get created for the same position set. And the SFL summaries which belong to that position set
    * will be scoped based on the created cluster positions.
    * 
    * @param clusterPositionList
    * @param clusterSummaryIdsMap
    * @param ignoreWordPositions
    * @return the Map<SFLCluster, List<SFLSummary>>
    */
   private Map<SFLCluster, List<SFLSummary>> createSFLCLustersBasedOnPositionSets (
            List<SFLClusterPostionInformation> clusterPositionList,
            Map<SFLClusterPostionInformation, List<SFLSummary>> clusterSummaryIdsMap, List<Integer> ignoreWordPositions) {
      int clusterId = 1;
      Map<SFLCluster, List<SFLSummary>> clusterSummaryListMap = new HashMap<SFLCluster, List<SFLSummary>>();
      for (SFLClusterPostionInformation clusterPostionInformation : clusterPositionList) {
         Set<Integer> clusterPositions = clusterPostionInformation.getClusterPositionSet();
         List<List<Integer>> continuousPositionsList = NLPUtilities.getContinuousPositionsList(clusterPositions);
         // Prepare the cluster for each continuous position(s)
         List<SFLCluster> continuousPositionClusters = new ArrayList<SFLCluster>();
         for (List<Integer> continuousPositions : continuousPositionsList) {
            SFLCluster cluster = new SFLCluster();
            cluster.setId(clusterId);
            cluster.setPositions(continuousPositions);
            continuousPositionClusters.add(cluster);
            clusterId++;
         }
         // Scope each continuous position cluster with the corresponding list of participating SFL summaries
         clusterSummaryListMap.putAll(scopeSFLSummaryByCluster(continuousPositionClusters, clusterSummaryIdsMap
                  .get(clusterPostionInformation), ignoreWordPositions));
      }
      return clusterSummaryListMap;
   }

   /**
    * @param tagTokenList
    * @param clusterPositionList
    * @param possibleClustersInfo
    * @throws OntologyException
    */
   private void addIgnoreWordPositionsToCluster (Map<Integer, List<RecognitionEntity>> ignoreEntitiesByPosition,
            List<SFLClusterPostionInformation> possibleClustersInfo) {
      for (SFLClusterPostionInformation clusterPostionInformation : possibleClustersInfo) {
         Set<Integer> positionSet = clusterPostionInformation.getClusterPositionSet();
         Iterator<Integer> iterator = positionSet.iterator();
         int previousPosition = -10;
         int currentPosition = -10;
         Set<Integer> ignorePositions = new HashSet<Integer>();
         while (iterator.hasNext()) {
            previousPosition = currentPosition;
            currentPosition = iterator.next();
            // Check within distance limit of 2
            if (currentPosition - previousPosition == 2) {
               // Check for the previous position entity if it is in the ignore word position
               int ignoreEntityPosition = currentPosition - 1;
               if (ignoreEntitiesByPosition.containsKey(ignoreEntityPosition)) {
                  List<RecognitionEntity> recognitionEntities = ignoreEntitiesByPosition.get(ignoreEntityPosition);
                  for (RecognitionEntity recEntity : recognitionEntities) {
                     // Currently we are only adding conjunction entities in the ignore word positions
                     if (checkForConjunctions(recEntity.getWord())) {
                        ignorePositions.add(ignoreEntityPosition);
                     }
                  }
               }
            }
         }
         positionSet.addAll(ignorePositions);
         clusterPostionInformation.setClusterPositionSet(positionSet);
      }
   }

   /**
    * return true only if all the participating summaries intersects with the given summary. if one of the summary does
    * not intersect return false;
    * 
    * @param positionSet
    * @param wordPosition
    * @param possibleClustersInfo
    * @return
    */
   private boolean intersectWithSummary (SFLClusterPostionInformation clusterPostionInformation,
            List<Integer> wordPosition) {

      boolean interSects = false;
      List<Integer> interSectingPositions = new ArrayList<Integer>();
      interSectingPositions.addAll(clusterPostionInformation.getIntersectingPosition());
      interSectingPositions.retainAll(wordPosition);
      if (!CollectionUtils.isEmpty(interSectingPositions)) {
         interSects = true;
      }
      return interSects;
   }

   private void performWeightCorrectionIfSFLNotContinuous (SFLSummary sflSummary, List<Integer> ignoreWordPositions) {
      ReferedTokenPosition rtp = new ReferedTokenPosition(sflSummary.getWordPositions());
      List<Integer> inBetweenPos = rtp.getInBetweenPos();
      double weightReduction = Double.parseDouble(getNlpConfigurationService().getSnowflakeOProximityWeightReduction());

      if (!CollectionUtils.isEmpty(inBetweenPos)) {
         if (!CollectionUtils.isEmpty(ignoreWordPositions)) {
            inBetweenPos.removeAll(ignoreWordPositions);
         }
         double sumWeight = sflSummary.getSumWeight() - sflSummary.getSumWeight() * weightReduction / 100
                  * inBetweenPos.size();
         sflSummary.setSumWeight(sumWeight);
      }

   }

   /**
    * @param sflSummary
    * @param weightReduction
    */
   private void performWeightCorrectionForUnorderedSFLs (SFLSummary sflSummary, double weightReduction) {

      List<Integer> positions = sflSummary.getWordPositions();
      Collections.sort(positions);
      if (sflSummary.getPositionOrder() == null) {
         return;
      }
      List<Integer> orders = new ArrayList<Integer>();
      for (Integer order : sflSummary.getPositionOrder().values()) {
         orders.add(order);
      }
      Collections.sort(orders);
      // weightReduction is the percentage Weight to be removed if SFl is out of Order.
      for (int i = 0; i < positions.size(); i++) {
         Integer position = positions.get(i);
         Integer orderByPos = sflSummary.getOrderByPosition(position);
         if (orderByPos != null && i < orders.size()) {
            Integer orderAtIndex = orders.get(i);
            if (!orderAtIndex.equals(orderByPos)) {
               sflSummary.setSumWeight(sflSummary.getSumWeight() - sflSummary.getSumWeight() * weightReduction / 100);
               break;
            }
         }
      }
   }

   /**
    * Method to divide the SFL summaries by the proximity. Maximum allowed proximity for a SFl term has been configured
    * under NLP configuration. If any SFL summary is having token with position difference among them greater then this
    * value the SFL Summary will be divided.
    * 
    * @param summary
    * @return the List<SFLSummary>
    */
   private List<SFLSummary> splitSummaryByTokenProximity (SFLSummary summary) {
      int maximumAllowedProximity = getNlpConfigurationService().getMaximumProximityForSFL();
      List<SFLSummary> finalList = new ArrayList<SFLSummary>(1);
      List<Set<Integer>> validPositionsSets = new ArrayList<Set<Integer>>(1);
      ReferedTokenPosition rtp = new ReferedTokenPosition(summary.getWordPositions());
      int prevPos = rtp.getReferedTokenPositions().first();
      Set<Integer> validPositions = new TreeSet<Integer>();
      validPositions.add(prevPos);
      validPositionsSets.add(validPositions);

      for (Integer position : rtp.getReferedTokenPositions()) {
         if (position.equals(prevPos)) {
            continue;
         }
         if (position - prevPos <= maximumAllowedProximity) {
            validPositions.add(position);
         } else {
            validPositions = new TreeSet<Integer>();
            validPositions.add(position);
            validPositionsSets.add(validPositions);
         }
         prevPos = position;
      }
      if (validPositionsSets.size() == 1 && validPositionsSets.get(0).containsAll(summary.getWordPositions())) {
         finalList.add(summary);
         return finalList;
      }
      // iterate over all the valid position sets and create 1 SFl summary each for set.
      for (Set<Integer> validSet : validPositionsSets) {
         try {
            SFLSummary sflSummary = (SFLSummary) summary.clone();
            sflSummary.setWordPositions(new ArrayList<Integer>(validSet));
            updateSFLSummaryBasedOnWordPositions(sflSummary);
            finalList.add(sflSummary);
         } catch (CloneNotSupportedException e) {
            throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
         }
      }
      return finalList;
   }

   /**
    * Method to check if a SFl Summary needs to be filtered based on required token count in SFL Term and required Token
    * Present in query for this SFl.
    * 
    * @param summary
    * @return the boolean true/false
    */
   private boolean filterSummaryByRequiredTokenCount (SFLSummary summary) {
      if (summary.getRequiredCount().equals(0)) {
         return false;
      }
      int existingRequiredCount = 0;
      Map<Integer, Integer> positionRequired = summary.getRequiredTokenByPosition();
      for (Integer position : summary.getWordPositions()) {
         Integer required = positionRequired.get(position);
         if (required != null && required.equals(1)) {
            existingRequiredCount++;
         }
      }
      if (existingRequiredCount >= summary.getRequiredCount()) {
         return false;
      }
      return true;
   }

   private boolean checkIfPrimaryTokenPresent (SFLSummary summary) {
      Map<Integer, Integer> primaryTokenByPosition = summary.getPrimaryTokenByPosition();
      boolean primaryTokenPresent = false;
      for (Entry<Integer, Integer> entry : primaryTokenByPosition.entrySet()) {
         Integer primary = entry.getValue();
         if (primary != null && primary.equals(1)) {
            primaryTokenPresent = true;
            break;
         }
      }
      return primaryTokenPresent;
   }

   private void perfromRecalculationForWeights (SFLSummary summary) {
      double weight = 0.0;
      for (Entry<Integer, Double> entry : summary.getPositionWeight().entrySet()) {
         weight += entry.getValue();
      }
      summary.setSumWeight(weight);
   }

   private double getAdjustedWeightForToken (RecognitionEntity fromEntity, String sflTermToken,
            boolean fromLinguisticRoot) {
      if (fromEntity.getEntityType().equals(RecognitionEntityType.PW_ENTITY)) {
         // TODO NA as of now hard coding the value for PW word quality. need to figure out a way to get the correct PW
         // word if entity is from linguistic root.
         if (fromLinguisticRoot) {
            return 0.85 * 0.9;
         }
         PWEntity pwEntity = (PWEntity) fromEntity;
         Map<String, RIParallelWord> parallelWords = pwEntity.getWordValues();
         if (parallelWords.containsKey(sflTermToken.toLowerCase())) {
            RIParallelWord riParallelWord = parallelWords.get(sflTermToken.toLowerCase());
            // if the entity is from the linguistic root of the word get
            return riParallelWord.getQuality();
         }
      } else {
         return 1;
      }
      return 1;
   }

   /**
    * This method removes the conjunction position if it is there in start or end word of the sfl summary but not in
    * actual sfl We also update the sfl summary based on the current word positions we have in it.
    * 
    * @param sflSummary
    * @return the List<SFLSummary>
    */
   private List<SFLSummary> removeConjunctionPositionIfAtStartOrEnd (List<SFLSummary> sflSummaries) {
      List<SFLSummary> finalSummaryList = new ArrayList<SFLSummary>(1);
      for (SFLSummary summary : sflSummaries) {
         removeConjunctionPositionIfAtStartOrEnd(summary);
         if (CollectionUtils.isNotEmpty(summary.getWordPositions())) {
            updateSFLSummaryBasedOnWordPositions(summary);
            finalSummaryList.add(summary);
         }
      }
      return finalSummaryList;
   }

   private void updateSFLSummaryBasedOnWordPositions (SFLSummary summary) {
      summary.getPositionOrder().keySet().retainAll(summary.getWordPositions());
      summary.getPositionWeight().keySet().retainAll(summary.getWordPositions());
      summary.getPositionTokenId().keySet().retainAll(summary.getWordPositions());
      summary.getRequiredTokenByPosition().keySet().retainAll(summary.getWordPositions());
      summary.getPrimaryTokenByPosition().keySet().retainAll(summary.getWordPositions());
      summary.getParticipatingWordQuality().keySet().retainAll(summary.getWordPositions());
      Collection<List<Integer>> values = summary.getTokenPositionsMap().values();
      for (List<Integer> posiList : values) {
         posiList.retainAll(summary.getWordPositions());
      }
   }

   /**
    * This method splits the sfl summary based on the frequency of each token that could appear in the sfl. Eg: User
    * query was "Net Sales and Other Sales" Now there is SFLTerm "Net Sales". And we get SFL Summary which says Net(at
    * position 0) appearing once but sales (at position 1 and 4) appearing twice on two different positions. Then we
    * split the sfl summary into two Net Sales at 0,1 position and 0,4 position respectively.
    * 
    * @param recCloudEntities
    * @return the List<SFLSummary>
    */
   public List<SFLSummary> splitSummaryByTokenFrequency (SFLSummary sflSummary) {
      List<SFLSummary> allSFLSummaries = new ArrayList<SFLSummary>(1);
      Map<String, Integer> tokensWithMoreFrequency = new HashMap<String, Integer>(1);
      List<Integer> positionsWithMatchedFrequency = new ArrayList<Integer>(1);

      // Prepare the list of tokens which appear more in the SFLTerm.
      for (Entry<String, List<Integer>> entry : sflSummary.getTokenPositionsMap().entrySet()) {
         String token = entry.getKey();
         Integer currentFrequency = entry.getValue().size();
         int tokenCountInTerm = getTokenCountInTerm(sflSummary.getSFLName(), entry.getKey());
         if (tokenCountInTerm < currentFrequency) {
            tokensWithMoreFrequency.put(token, tokenCountInTerm);
         } else {
            positionsWithMatchedFrequency.addAll(entry.getValue());
         }
      }

      if (!MapUtils.isEmpty(tokensWithMoreFrequency)) {
         try {
            SFLSummary clonedSummary = (SFLSummary) sflSummary.clone();

            // First set the positions with the matched frequency to the summary
            clonedSummary.setWordPositions(positionsWithMatchedFrequency);

            // Add the summary to summary list and use this list for split
            List<SFLSummary> sflSummaries = new ArrayList<SFLSummary>(1);
            sflSummaries.add(clonedSummary);

            // Now iterate over each token with more frequency and split the sfl summary based on the frequency
            for (Entry<String, Integer> entry : tokensWithMoreFrequency.entrySet()) {
               String token = entry.getKey();
               int tokenCountInTerm = entry.getValue();
               // For each token which appear more split the SFL summary.
               sflSummaries = splitSFLSummary(sflSummaries, sflSummary.getTokenPositionsMap().get(token),
                        tokenCountInTerm);
            }
            allSFLSummaries.addAll(sflSummaries);
         } catch (CloneNotSupportedException e) {
            throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
         }
      } else {
         allSFLSummaries.add(sflSummary);
      }
      return allSFLSummaries;
   }

   private List<SFLSummary> splitSFLSummary (List<SFLSummary> sflSummaries, List<Integer> positions, int requiredCount) {

      List<SFLSummary> clonedSummaryList = new ArrayList<SFLSummary>(1);
      for (SFLSummary sflSummary : sflSummaries) {
         // Create the combination generator to get the combination of the word position.
         CombinationGenerator cg = new CombinationGenerator(positions.size(), requiredCount);
         int[] a = null;
         while ((a = cg.getNext()) != null) {
            try {
               SFLSummary clonedSummary = (SFLSummary) sflSummary.clone();
               boolean invalidClone = false;
               for (int i : a) {
                  if (!clonedSummary.getWordPositions().contains(positions.get(i))) {
                     clonedSummary.addWordPosition(positions.get(i));
                  } else {
                     invalidClone = true;
                     break;
                  }
               }
               if (!invalidClone) {
                  clonedSummaryList.add(clonedSummary);
               }
            } catch (CloneNotSupportedException e) {
               throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
            }
         }
      }
      return clonedSummaryList;
   }

   /**
    * SFL summary should not start or end with conjunctions
    * 
    * @param sflSummary
    * @throws OntologyException
    */
   private void removeConjunctionPositionIfAtStartOrEnd (SFLSummary sflSummary) {
      List<Integer> positions = sflSummary.getWordPositions();

      for (Entry<String, List<Integer>> entry : sflSummary.getTokenPositionsMap().entrySet()) {
         if (CollectionUtils.isEmpty(positions)) {
            return;
         }

         // Multiple conjunctions can come at start or end, so again check for min and max position even if we deleted
         // the conjunction position in the previous iteration
         String token = entry.getKey().toLowerCase();
         Integer maxPos = Collections.max(positions);
         Integer minPos = Collections.min(positions);

         if (checkForConjunctions(token)) {
            for (Integer integer : entry.getValue()) {
               if (integer == minPos || integer == maxPos) {
                  boolean removePos = true;
                  if (integer == minPos && sflSummary.getSFLName().toLowerCase().startsWith(token)) {
                     removePos = false;
                  } else if (integer == maxPos && sflSummary.getSFLName().toLowerCase().endsWith(token)) {
                     removePos = false;
                  }
                  if (!removePos) {
                     break;
                  }
                  positions.remove(integer);
                  sflSummary.getPositionOrder().remove(integer);
                  sflSummary.getPositionWeight().remove(integer);
                  sflSummary.getPositionTokenId().remove(integer);
                  sflSummary.getRequiredTokenByPosition().remove(integer);
                  sflSummary.getPrimaryTokenByPosition().remove(integer);
               }
            }
         }
      }
   }

   /**
    * Method to get a token count in the SFL term
    * 
    * @param name
    * @param key
    * @return
    */
   private int getTokenCountInTerm (String name, String key) {
      // "\\b" is used to set the word boundaries. i.e sales will matched only to sales but not sales1
      // Need to escape the regex special chars in the input key they were not matching.
      // If there are no regex chars in the key, then only will have the word boundary attached.
      String[] regexChars = new String[] { ".", "-", "$", "," };
      boolean wordBoundaryAdded = false;
      for (String regexChar : regexChars) {
         if (key.equals(regexChar)) {
            wordBoundaryAdded = true;
            key = key.replace(regexChar, "\\" + regexChar);
         } else if (key.startsWith(regexChar)) {
            wordBoundaryAdded = true;
            key = key.replace(regexChar, "\\" + regexChar) + "\\b";
         } else if (key.endsWith(regexChar)) {
            wordBoundaryAdded = true;
            key = "\\b" + key.replace(regexChar, "\\" + regexChar);
         } else if (key.contains(regexChar)) {
            key = key.replace(regexChar, "\\" + regexChar);
         }
      }
      if (!wordBoundaryAdded) {
         key = "\\b" + key + "\\b";
      }
      Pattern p = Pattern.compile(key, Pattern.CASE_INSENSITIVE);

      Matcher m = p.matcher(name); // get a matcher object
      int count = 0;
      while (m.find()) {
         count++;
      }
      return count;
   }

   /**
    * Method to check if the passed token is an instance of the conjunction.
    * 
    * @param word
    * @return
    * @throws OntologyException
    */
   private boolean checkForConjunctions (String word) {
      boolean wordAdded = false;
      if (getNlpConfigurationService().getPosContext().getConjAndByConjTermNames().contains(word)) {
         wordAdded = true;
      }
      return wordAdded;
   }

   private boolean isIgnoreEntity (RecognitionEntity recEntity) {
      boolean ignore = false;
      if (recEntity.getNLPtag().equals(NLPConstants.NLP_TAG_POS_COORDINATING_CONJUNCTION)
               || recEntity.getNLPtag().equals(NLPConstants.NLP_TAG_POS_MODAL_AUXILIARY)
               || recEntity.getNLPtag().equals(NLPConstants.NLP_TAG_POS_TO)
               || recEntity.getNLPtag().equals(NLPConstants.NLP_TAG_POS_DETERMINER)
               || recEntity.getNLPtag().equals(NLPConstants.NLP_TAG_POS_COMMA)
               || recEntity.getNLPtag().equals(NLPConstants.NLP_TAG_POS_CARDINAL_NUMBER)
               || recEntity.getNLPtag().equals(NLPConstants.NLP_TAG_POS_SYMBOL)) {
         return true;
      }
      ignore = checkForConjunctions(recEntity.getWord());
      return ignore;
   }

   /**
    * This method iterates over each sfl summary and filters the summary with non-continuous word positions
    * 
    * @param sflSummaries
    * @return the List<SFLSummary>
    */
   private List<SFLSummary> filterSummariesIfContinuousSFLFound (List<SFLSummary> sflSummaries) {
      List<SFLSummary> continuousSFLs = new ArrayList<SFLSummary>();
      for (SFLSummary sflSummary : sflSummaries) {
         ReferedTokenPosition rtp = new ReferedTokenPosition(sflSummary.getWordPositions());
         List<Integer> inBetweenPos = rtp.getInBetweenPos();
         if (CollectionUtils.isEmpty(inBetweenPos)) {
            continuousSFLs.add(sflSummary);
         }
      }
      if (!CollectionUtils.isEmpty(continuousSFLs)) {
         return continuousSFLs;
      }
      return sflSummaries;
   }

   @Override
   protected void updateProcessorInput (ProcessorInput input, ProcessorInput processorInput) {
      SFLProcessorInput sflProcessorInput = (SFLProcessorInput) processorInput;
      // Also add the entities to reconsider from the SFL processor input to the processor input, so that is available
      // for next iteration
      input.getRecEntitiesToReConsider().addAll(getEntitiesToReConsder(sflProcessorInput));

      // Here we are checking the below condition because only for sfl processor we are creating SFLProcessorInput
      // object and sending it, for remaining processor we send the ProcessorInput object only, so for that reason we to
      // have copy the contents of SFLProcessorInput into ProcessorInput object.
      List<IWeightedEntity> outputRecognitionEntities = sflProcessorInput.getOutputRecognitionEntities();
      if (!CollectionUtils.isEmpty(outputRecognitionEntities)) {
         input.getOutputRecognitionEntities().addAll(outputRecognitionEntities);
      }
   }

   /**
    * Method to return the collection of entities which should be reconsider in the next iteration. Currently we are
    * adding only the tag entities. In future, we can add other information as and when required like secondary words,
    * etc.
    * 
    * @param processorInput
    * @return the Collection<? extends IWeightedEntity>
    */
   private Collection<? extends IWeightedEntity> getEntitiesToReConsder (SFLProcessorInput processorInput) {
      List<IWeightedEntity> entitiesToBeConsider = new ArrayList<IWeightedEntity>();
      Collection<List<RecognitionEntity>> tagEntitiesList = processorInput.getTagEntities().values();
      if (CollectionUtils.isEmpty(tagEntitiesList)) {
         return entitiesToBeConsider;
      }
      for (List<RecognitionEntity> tagEntities : tagEntitiesList) {
         entitiesToBeConsider.addAll(tagEntities);
      }
      return entitiesToBeConsider;
   }

   /**
    * @return the nlpConfigurationService
    */
   @Override
   public INLPConfigurationService getNlpConfigurationService () {
      return nlpConfigurationService;
   }

   /**
    * @param nlpConfigurationService
    *           the nlpConfigurationService to set
    */
   @Override
   public void setNlpConfigurationService (INLPConfigurationService nlpConfigurationService) {
      this.nlpConfigurationService = nlpConfigurationService;
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
}