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


package com.execue.acqh.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.execue.acqh.bean.QueryEntityInformation;
import com.execue.acqh.bean.QueryInformation;
import com.execue.acqh.bean.ThirdPartyQueryHistoryInfo;
import com.execue.acqh.exception.AnswersCatalogQueryHistoryException;
import com.execue.acqh.exception.AnswersCatalogQueryHistoryExceptionCodes;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.qdata.OptimalDSetSWIInfo;
import com.execue.core.common.bean.qdata.QueryHistoryBusinessEntityInfo;
import com.execue.core.common.bean.qdata.QueryHistoryBusinessEntityPatternInfo;
import com.execue.core.common.bean.qdata.QueryHistoryDimensionPatternInfo;
import com.execue.core.common.type.ColumnType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.exception.RangeSuggestionException;
import com.execue.platform.helper.RangeSuggestionServiceHelper;
import com.execue.platform.swi.IRangeSuggestionService;
import com.execue.swi.exception.MappingException;
import com.execue.swi.service.IMappingRetrievalService;

public abstract class ThirdPartyQueryHistoryRetrievalService implements IQueryHistoryRetrievalService {

   private static final Logger      logger = Logger.getLogger(ThirdPartyQueryHistoryRetrievalService.class);

   private IMappingRetrievalService mappingRetrievalService;

   private IRangeSuggestionService  rangeSuggestionService;

   public abstract ThirdPartyQueryHistoryInfo populateThirdPartyQueryHistoryInfo ()
            throws AnswersCatalogQueryHistoryException;

   @Override
   public List<QueryHistoryDimensionPatternInfo> buildQueryHistoryDimensionPatternInfo (Long assetId, Long modelId,
            Date queryExecutionDate) throws AnswersCatalogQueryHistoryException {
      ThirdPartyQueryHistoryInfo queryHistoryInfo = populateThirdPartyQueryHistoryInfo();
      List<QueryHistoryDimensionPatternInfo> queryHistoryDimensionPatternInfos = new ArrayList<QueryHistoryDimensionPatternInfo>();
      Map<String, String> dimensions = new HashMap<String, String>();
      Map<String, String> measures = new HashMap<String, String>();
      Map<String, String> names = new HashMap<String, String>();
      // Here we are populating to maps ie dimension map and measure map
      populateMaps(queryHistoryInfo.getQueryEntityInformations(), dimensions, measures, names);
      Map<String, Long> queryHistoryPatternsMap = new HashMap<String, Long>();
      // Here we iterate over file and populate the queryHistoryPatternsMap
      Long totalQueryCount = populateQueryHistoryPatterns(dimensions, measures, queryHistoryPatternsMap,
               queryHistoryInfo.getQueryInformations());
      // populate QueryHistoryDimensionPatternInfo
      queryHistoryDimensionPatternInfos = populateQueryHistoryDimensionPattern(totalQueryCount, names, dimensions,
               measures, queryHistoryPatternsMap, assetId, modelId);
      return queryHistoryDimensionPatternInfos;
   }

   private void populateMaps (List<QueryEntityInformation> demoCardsConceptList, Map<String, String> dimensions,
            Map<String, String> measures, Map<String, String> names) {
      for (QueryEntityInformation democardsConcept : demoCardsConceptList) {
         if (democardsConcept.getKdxType().equalsIgnoreCase(ColumnType.SIMPLE_LOOKUP.getValue())
                  || democardsConcept.getKdxType().equalsIgnoreCase(ColumnType.DIMENSION.getValue())) {
            for (String varationName : democardsConcept.getVariations()) {
               dimensions.put(varationName, democardsConcept.getBedId());
               names.put(democardsConcept.getBedId(), democardsConcept.getName());
            }

         } else if (democardsConcept.getKdxType().equalsIgnoreCase(ColumnType.MEASURE.getValue())) {
            for (String varationName : democardsConcept.getVariations()) {
               measures.put(varationName, democardsConcept.getBedId());
               names.put(democardsConcept.getBedId(), democardsConcept.getName());
            }
         }
      }
   }

   private Long populateQueryHistoryPatterns (Map<String, String> dimensions, Map<String, String> measures,
            Map<String, Long> queryHistoryPatternsMap, List<QueryInformation> queryInformations)
            throws AnswersCatalogQueryHistoryException {
      Long totalQueryCount = 0L;
      Integer count = 0;
      for (QueryInformation queryInformation : queryInformations) {
         Set<String> patternStrings = new TreeSet<String>();
         for (String dimensionName : queryInformation.getWhere()) {
            if (dimensions.containsKey(dimensionName)) {
               patternStrings.add(dimensions.get(dimensionName));
            }
         }

         for (String dimensionName : queryInformation.getSelect()) {
            if (dimensions.containsKey(dimensionName)) {
               patternStrings.add(dimensions.get(dimensionName));
            }
         }
         for (String dimensionName : queryInformation.getGroupBy()) {
            if (dimensions.containsKey(dimensionName)) {
               patternStrings.add(dimensions.get(dimensionName));
            }
            if (measures.containsKey(dimensionName)) {
               patternStrings.add(measures.get(dimensionName));
            }
         }

         if (ExecueCoreUtil.isCollectionEmpty(patternStrings)) {
            logger.error("------------------------------------");
            logger.error("Select columns::" + queryInformation.getSelect() + ":Where coulmns:"
                     + queryInformation.getWhere() + ":Group by columns:" + queryInformation.getGroupBy());
            count++;
            continue;
         }
         String patternString = ExecueCoreUtil.joinCollection(patternStrings, "~");
         Long usageCount = queryHistoryPatternsMap.get(patternString);
         if (usageCount == null) {
            usageCount = queryInformation.getUsageCount();
         } else {
            usageCount += queryInformation.getUsageCount();
         }
         queryHistoryPatternsMap.put(patternString, usageCount);
         totalQueryCount += queryInformation.getUsageCount();
      }
      logger.debug("total number of records discarded::::" + count);
      return totalQueryCount;
   }

   private List<QueryHistoryDimensionPatternInfo> populateQueryHistoryDimensionPattern (Long totalQueryCount,
            Map<String, String> names, Map<String, String> dimensions, Map<String, String> measures,
            Map<String, Long> pastUsagePatternMap, Long assetId, Long modelId)
            throws AnswersCatalogQueryHistoryException {

      List<QueryHistoryDimensionPatternInfo> queryHistoryDimensionPatternInfos = new ArrayList<QueryHistoryDimensionPatternInfo>();
      Map<String, Range> rangeMap = new HashMap<String, Range>();

      for (Entry<String, Long> entry : pastUsagePatternMap.entrySet()) {
         List<OptimalDSetSWIInfo> optimalDSetSwiInfos = new ArrayList<OptimalDSetSWIInfo>();
         Double usagePercentage = entry.getValue().doubleValue() / totalQueryCount.doubleValue() * 100;
         String[] tokens = entry.getKey().split("~");
         for (String bed : tokens) {
            try {
               if (dimensions.containsValue(bed)) {
                  OptimalDSetSWIInfo optimalDSetDimensionInfo = new OptimalDSetSWIInfo();
                  optimalDSetDimensionInfo.setBedId(Long.parseLong(bed));
                  optimalDSetDimensionInfo.setConceptName(names.get(bed));
                  Long numMembers = getMappingRetrievalService().getMappedMemberCount(assetId, Long.parseLong(bed));
                  optimalDSetDimensionInfo.setMemberCount(numMembers.intValue());
                  optimalDSetSwiInfos.add(optimalDSetDimensionInfo);
               } else if (measures.containsValue(bed)) {
                  OptimalDSetSWIInfo optimalDSetDimensionInfo = new OptimalDSetSWIInfo();
                  optimalDSetDimensionInfo.setBedId(Long.parseLong(bed));
                  optimalDSetDimensionInfo.setConceptName(names.get(bed));
                  Range range = null;
                  if (rangeMap.get(bed) == null) {
                     range = getRangeSuggestionService().deduceRange(
                              RangeSuggestionServiceHelper.populateDynamicRangeInput(modelId, assetId, Long
                                       .parseLong(bed)));
                     rangeMap.put(bed, range);
                  } else {
                     range = rangeMap.get(bed);
                  }
                  optimalDSetDimensionInfo.setRange(range);
                  optimalDSetSwiInfos.add(optimalDSetDimensionInfo);
               }
            } catch (MappingException e) {
               throw new AnswersCatalogQueryHistoryException(e.getCode(), e);
            } catch (RangeSuggestionException e) {
               throw new AnswersCatalogQueryHistoryException(e.getCode(), e);
            } catch (NumberFormatException e) {
               throw new AnswersCatalogQueryHistoryException(
                        AnswersCatalogQueryHistoryExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(optimalDSetSwiInfos)) {
            QueryHistoryDimensionPatternInfo queryHistoryDimensionPattern = new QueryHistoryDimensionPatternInfo();
            queryHistoryDimensionPattern.setUsagePercentage(usagePercentage);
            queryHistoryDimensionPattern.setOptimalDSetSwiInfos(optimalDSetSwiInfos);
            queryHistoryDimensionPatternInfos.add(queryHistoryDimensionPattern);
         }
      }

      return queryHistoryDimensionPatternInfos;
   }

   @Override
   public QueryHistoryBusinessEntityPatternInfo buildQueryHistoryBusinessEntityPatternInfo (Long assetId, Long modelId,
            Date queryExecutionDate, Integer maxLimit) throws AnswersCatalogQueryHistoryException {

      ThirdPartyQueryHistoryInfo queryHistoryInfo = populateThirdPartyQueryHistoryInfo();

      QueryHistoryBusinessEntityPatternInfo queryHistoryBusinessEntityPatternInfo = new QueryHistoryBusinessEntityPatternInfo();

      Map<String, String> dimensions = new HashMap<String, String>();
      Map<String, String> measures = new HashMap<String, String>();
      Map<String, String> names = new HashMap<String, String>();
      // Here we are populating to maps ie dimension map and measure map
      populateMaps(queryHistoryInfo.getQueryEntityInformations(), dimensions, measures, names);

      Map<String, Long> queryHistoryDimensionUsageMap = new HashMap<String, Long>();
      Map<String, Long> queryHistoryMeasureUsageMap = new HashMap<String, Long>();
      // Here we iterate over file and populate the queryHistoryPatternsMap
      Long totalQueryCount = populateQueryHistoryBusinessEntityInfo(dimensions, measures,
               queryHistoryDimensionUsageMap, queryHistoryMeasureUsageMap, queryHistoryInfo.getQueryInformations());
      // populate QueryHistoryDimensionPatternInfo
      queryHistoryBusinessEntityPatternInfo = populateQueryHistoryBusinessEntityPatternInfo(totalQueryCount, names,
               dimensions, measures, queryHistoryDimensionUsageMap, queryHistoryMeasureUsageMap, assetId, modelId, maxLimit);
      return queryHistoryBusinessEntityPatternInfo;
   }

   private Long populateQueryHistoryBusinessEntityInfo (Map<String, String> dimensions, Map<String, String> measures,
            Map<String, Long> queryHistoryDimensionUsageMap, Map<String, Long> queryHistoryMeasureUsageMap,
            List<QueryInformation> queryInformations) {
      Long totalQueryCount = 0L;
      Integer discardedCount = 0;
      for (QueryInformation queryInformation : queryInformations) {
         Set<String> dimensionBEDIDs = new TreeSet<String>();
         Set<String> measureBEDIDs = new TreeSet<String>();
         for (String conceptName : queryInformation.getWhere()) {
            if (dimensions.containsKey(conceptName)) {
               dimensionBEDIDs.add(dimensions.get(conceptName));
            }
            if (measures.containsKey(conceptName)) {
               measureBEDIDs.add(measures.get(conceptName));
            }
         }

         for (String conceptName : queryInformation.getSelect()) {
            if (dimensions.containsKey(conceptName)) {
               dimensionBEDIDs.add(dimensions.get(conceptName));
            }
            if (measures.containsKey(conceptName)) {
               measureBEDIDs.add(measures.get(conceptName));
            }
         }

         for (String conceptName : queryInformation.getGroupBy()) {
            if (dimensions.containsKey(conceptName)) {
               dimensionBEDIDs.add(dimensions.get(conceptName));
            }
            if (measures.containsKey(conceptName)) {
               measureBEDIDs.add(measures.get(conceptName));
            }
         }

         if (ExecueCoreUtil.isCollectionEmpty(dimensionBEDIDs)
                  && ExecueCoreUtil.isCollectionEmpty(measureBEDIDs)) {
            logger.error("------------------------------------");
            logger.error("Select columns::" + queryInformation.getSelect() + ":Where coulmns:"
                     + queryInformation.getWhere() + ":Group by columns:" + queryInformation.getGroupBy());
            discardedCount++;
            continue;
         }
         
         Long QueryInfoUsageCount = queryInformation.getUsageCount();
         
         if (ExecueCoreUtil.isCollectionNotEmpty(dimensionBEDIDs)) {
            for (String dimensionBEDID : dimensionBEDIDs) {
               Long usageCount = queryHistoryDimensionUsageMap.get(dimensionBEDID);
               if (usageCount != null) {
                  usageCount += QueryInfoUsageCount;
               } else {
                  usageCount = QueryInfoUsageCount;
               }
               queryHistoryDimensionUsageMap.put(dimensionBEDID, usageCount);
            }
         }

         if (ExecueCoreUtil.isCollectionNotEmpty(measureBEDIDs)) {
            for (String measureBEDID : measureBEDIDs) {
               Long usageCount = queryHistoryMeasureUsageMap.get(measureBEDID);
               if (usageCount != null) {
                  usageCount += QueryInfoUsageCount;
               } else {
                  usageCount = QueryInfoUsageCount;
               }
               queryHistoryMeasureUsageMap.put(measureBEDID, usageCount);
            }
         }

         totalQueryCount += queryInformation.getUsageCount();
      }
      logger.debug("total number of records discarded::::" + discardedCount);
      return totalQueryCount;
   }

   private QueryHistoryBusinessEntityPatternInfo populateQueryHistoryBusinessEntityPatternInfo (Long totalQueryCount,
            Map<String, String> names, Map<String, String> dimensions, Map<String, String> measures,
            Map<String, Long> queryHistoryDimensionUsageMap, Map<String, Long> queryHistoryMeasureUsageMap,
            Long assetId, Long modelId, Integer maxLimit) {
      QueryHistoryBusinessEntityPatternInfo queryHistoryBusinessEntityPatternInfo = new QueryHistoryBusinessEntityPatternInfo();

      List<QueryHistoryBusinessEntityInfo> dimensionInfos = new ArrayList<QueryHistoryBusinessEntityInfo>();
      List<QueryHistoryBusinessEntityInfo> measureInfos = new ArrayList<QueryHistoryBusinessEntityInfo>();
      
      for (String dimensionBEDID : queryHistoryDimensionUsageMap.keySet()) {
         Double usagePercentage = queryHistoryDimensionUsageMap.get(dimensionBEDID).doubleValue() / totalQueryCount.doubleValue() * 100;
         String dimensionConceptName = names.get(dimensionBEDID);
         QueryHistoryBusinessEntityInfo queryHistoryBusinessEntityInfo = new QueryHistoryBusinessEntityInfo();
         queryHistoryBusinessEntityInfo.setName(dimensionConceptName);
         queryHistoryBusinessEntityInfo.setBusinessEntityId(Long.parseLong(dimensionBEDID));
         queryHistoryBusinessEntityInfo.setUsagePercentage(usagePercentage);
         dimensionInfos.add(queryHistoryBusinessEntityInfo);
      }
      
      
      for (String measureBEDID : queryHistoryMeasureUsageMap.keySet()) {
         Double usagePercentage = queryHistoryMeasureUsageMap.get(measureBEDID).doubleValue() / totalQueryCount.doubleValue() * 100;
         String measureConceptName = names.get(measureBEDID);
         QueryHistoryBusinessEntityInfo queryHistoryBusinessEntityInfo = new QueryHistoryBusinessEntityInfo();
         queryHistoryBusinessEntityInfo.setName(measureConceptName);
         queryHistoryBusinessEntityInfo.setBusinessEntityId(Long.parseLong(measureBEDID));
         queryHistoryBusinessEntityInfo.setUsagePercentage(usagePercentage);
         measureInfos.add(queryHistoryBusinessEntityInfo);
      }

      if (logger.isDebugEnabled()) {
         logger.debug("Dimensions : "+dimensionInfos);
      }
      
      if (logger.isDebugEnabled()) {
         logger.debug("Measures : "+measureInfos);
      }
      
      Collections.sort(dimensionInfos);
      Collections.sort(measureInfos);
      
      if (logger.isDebugEnabled()) {
         logger.debug("Dimensions : "+dimensionInfos);
      }
      
      if (logger.isDebugEnabled()) {
         logger.debug("Measures : "+measureInfos);
      }
      
      for (int i = 0; i < maxLimit; i++) {
         if (i < dimensionInfos.size()) {
            queryHistoryBusinessEntityPatternInfo.getDimensions().add(dimensionInfos.get(i));
         }
         if (i < measureInfos.size()) {
            queryHistoryBusinessEntityPatternInfo.getMeasures().add(measureInfos.get(i));
         }
      }
      
      return queryHistoryBusinessEntityPatternInfo;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public IRangeSuggestionService getRangeSuggestionService () {
      return rangeSuggestionService;
   }

   public void setRangeSuggestionService (IRangeSuggestionService rangeSuggestionService) {
      this.rangeSuggestionService = rangeSuggestionService;
   }

}
