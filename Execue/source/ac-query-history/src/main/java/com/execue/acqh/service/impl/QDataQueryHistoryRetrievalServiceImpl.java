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
package com.execue.acqh.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.execue.acqh.exception.AnswersCatalogQueryHistoryException;
import com.execue.acqh.service.IQueryHistoryRetrievalService;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.qdata.OptimalDSetSWIInfo;
import com.execue.core.common.bean.qdata.QueryHistoryBusinessEntityInfo;
import com.execue.core.common.bean.qdata.QueryHistoryBusinessEntityPatternInfo;
import com.execue.core.common.bean.qdata.QueryHistoryDimensionPatternInfo;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.exception.RangeSuggestionException;
import com.execue.platform.helper.RangeSuggestionServiceHelper;
import com.execue.platform.swi.IRangeSuggestionService;
import com.execue.qdata.configuration.IQueryDataConfigurationService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IQueryDataService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.PreferencesException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.IPreferencesRetrievalService;

/**
 * @author Nitesh
 */
public class QDataQueryHistoryRetrievalServiceImpl implements IQueryHistoryRetrievalService {

   private static final Logger            logger = Logger.getLogger(QDataQueryHistoryRetrievalServiceImpl.class);

   private ICoreConfigurationService      coreConfigurationService;
   private IQueryDataConfigurationService queryDataConfigurationService;
   private IQueryDataService              queryDataService;
   private IMappingRetrievalService       mappingRetrievalService;
   private IPreferencesRetrievalService   preferencesRetrievalService;
   private IRangeSuggestionService        rangeSuggestionService;
   private IKDXRetrievalService           kdxRetrievalService;

   @Override
   public List<QueryHistoryDimensionPatternInfo> buildQueryHistoryDimensionPatternInfo (Long assetId, Long modelId,
            Date queryExecutionDate) throws AnswersCatalogQueryHistoryException {
      List<QueryHistoryDimensionPatternInfo> pastUsageDimensionPatterns = new ArrayList<QueryHistoryDimensionPatternInfo>();

      try {
         // Delete the optimal dset swi info for the given asset id
         getQueryDataService().deleteOptimalDSetSwiInfoByAssetId(assetId);

         // Get the existing mapped columns
         List<Mapping> loadedPrimaryMappings = getMappingRetrievalService().getPrimaryMappingsForConcepts(modelId,
                  assetId);

         if (ExecueCoreUtil.isCollectionEmpty(loadedPrimaryMappings)) {
            logger.error("No Mapping exists, hence cannot build past usage dimension pattern information");
            return pastUsageDimensionPatterns;
         }

         // Get the existing user defined ranges(static)
         List<Range> ranges = getPreferencesRetrievalService().getRanges(modelId);

         // Multiple static ranges can exist for the same bed id, so group them together
         Map<Long, List<Range>> staticRangesByBedId = prepareRangesByBedIdMap(ranges);

         // Populate the optimal dset swi infos
         List<OptimalDSetSWIInfo> optimalDSetSwiInfos = populateOptimalDSetSwiInfos(loadedPrimaryMappings,
                  staticRangesByBedId, assetId);

         // Save the optimal dset swi infos into the db
         getQueryDataService().createAllOptimalDSetSwiInfos(optimalDSetSwiInfos);

         // Prepare the map of optimal dset swi info against bed id, if range id is present then append it to the bed id key as well 
         Map<String, OptimalDSetSWIInfo> optimalDSetInfoByBedIdRangeIdMap = prepateOptimalDSetInfoByBedIdRangeIdMap(optimalDSetSwiInfos);

         // Get the past usage dimension pattern info from the qdata 
         Map<String, Double> patternInfos = getQueryDataService().getPastUsagePatternInfoMap(assetId,
                  queryExecutionDate);

         // Populate the qdata past usage dimension patterns
         pastUsageDimensionPatterns = populatePastUsageDimensionPatterns(optimalDSetInfoByBedIdRangeIdMap,
                  patternInfos, assetId, modelId);

         // Populate dynamic ranges for the past usage dimension patterns
         populateDynamicRanges(pastUsageDimensionPatterns, assetId, modelId);

         // Finally, delete the optimal dset swi info for the given asset id
         getQueryDataService().deleteOptimalDSetSwiInfoByAssetId(assetId);

      } catch (MappingException e) {
         throw new AnswersCatalogQueryHistoryException(e.getCode(), e);
      } catch (QueryDataException e) {
         throw new AnswersCatalogQueryHistoryException(e.getCode(), e);
      } catch (PreferencesException e) {
         throw new AnswersCatalogQueryHistoryException(e.getCode(), e);
      } catch (CloneNotSupportedException e) {
         throw new AnswersCatalogQueryHistoryException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      } catch (SWIException e) {
         throw new AnswersCatalogQueryHistoryException(e.getCode(), e);
      } catch (RangeSuggestionException e) {
         throw new AnswersCatalogQueryHistoryException(e.getCode(), e);
      }

      return pastUsageDimensionPatterns;
   }

   @Override
   public QueryHistoryBusinessEntityPatternInfo buildQueryHistoryBusinessEntityPatternInfo (Long assetId, Long modelId,
            Date queryExecutionDate, Integer maxLimit) throws AnswersCatalogQueryHistoryException {
      QueryHistoryBusinessEntityPatternInfo qDataPastUsageBusinessEntityPatternInfo = new QueryHistoryBusinessEntityPatternInfo();

      try {
         List<QueryHistoryBusinessEntityInfo> qdaList = new ArrayList<QueryHistoryBusinessEntityInfo>();
         // Delete the optimal dset swi info for the given asset id
         getQueryDataService().deleteOptimalDSetSwiInfoByAssetId(assetId);

         // Get the existing mapped columns
         List<Mapping> loadedPrimaryMappings = getMappingRetrievalService().getPrimaryMappingsForConcepts(modelId,
                  assetId);

         if (ExecueCoreUtil.isCollectionEmpty(loadedPrimaryMappings)) {
            logger.error("No Mapping exists, hence cannot build past usage dimension pattern information");
            return qDataPastUsageBusinessEntityPatternInfo;
         }

         // For Mart, we dont really worry about ranges, so prepare dummy map to use the same code as in cube input preparation
         Map<Long, List<Range>> staticRangesByBedId = new HashMap<Long, List<Range>>();

         // Populate the optimal dset swi infos
         List<OptimalDSetSWIInfo> optimalDSetSwiInfos = populateOptimalDSetSwiInfos(loadedPrimaryMappings,
                  staticRangesByBedId, assetId);

         // Save the optimal dset swi infos into the db
         getQueryDataService().createAllOptimalDSetSwiInfos(optimalDSetSwiInfos);

         // Get the past usage business entity infos for dimensions
         List<QueryHistoryBusinessEntityInfo> dimensions = getQueryDataService().getPastUsageProminentBusinessEntities(
                  assetId, queryExecutionDate, ColumnType.DIMENSION, maxLimit);

         // Get the past usage business entity infos for measures
         List<QueryHistoryBusinessEntityInfo> measures = getQueryDataService().getPastUsageProminentBusinessEntities(
                  assetId, queryExecutionDate, ColumnType.MEASURE, maxLimit);

         qDataPastUsageBusinessEntityPatternInfo.setDimensions(dimensions);
         qDataPastUsageBusinessEntityPatternInfo.setMeasures(measures);

         // Finally, delete the optimal dset swi info for the given asset id
         getQueryDataService().deleteOptimalDSetSwiInfoByAssetId(assetId);

      } catch (MappingException e) {
         throw new AnswersCatalogQueryHistoryException(e.getCode(), e);
      } catch (QueryDataException e) {
         throw new AnswersCatalogQueryHistoryException(e.getCode(), e);
      } catch (KDXException e) {
         throw new AnswersCatalogQueryHistoryException(e.getCode(), e);
      } catch (CloneNotSupportedException e) {
         throw new AnswersCatalogQueryHistoryException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

      return qDataPastUsageBusinessEntityPatternInfo;
   }

   private List<QueryHistoryDimensionPatternInfo> populatePastUsageDimensionPatterns (
            Map<String, OptimalDSetSWIInfo> optimalDSetInfoByBedIdRangeIdMap, Map<String, Double> patternInfos,
            Long assetId, Long modelId) {

      List<QueryHistoryDimensionPatternInfo> pastUsageDimensionPatterns = new ArrayList<QueryHistoryDimensionPatternInfo>();

      if (MapUtils.isEmpty(patternInfos)) {
         return pastUsageDimensionPatterns;
      }

      for (Entry<String, Double> entry : patternInfos.entrySet()) {
         String patternInfo = entry.getKey();
         Double usagePercentage = entry.getValue();
         List<OptimalDSetSWIInfo> optimalDSetSwiInfos = getMatchedOptimalDSetSwiInfos(patternInfo,
                  optimalDSetInfoByBedIdRangeIdMap);
         if (ExecueCoreUtil.isCollectionEmpty(optimalDSetSwiInfos)) {
            // NOTE: This should never come, once we maintain the qdata history in synch with the current state of the
            // asset and model
            logger
                     .error("Query pattern cannot be supported by the current system as one or more entities might not exists.. "
                              + patternInfo);
            continue;
         }
         QueryHistoryDimensionPatternInfo pastUsageDimensionPattern = new QueryHistoryDimensionPatternInfo();
         pastUsageDimensionPattern.setUsagePercentage(usagePercentage);
         pastUsageDimensionPattern.setOptimalDSetSwiInfos(optimalDSetSwiInfos);
         pastUsageDimensionPatterns.add(pastUsageDimensionPattern);
      }

      return pastUsageDimensionPatterns;
   }

   private void populateDynamicRanges (List<QueryHistoryDimensionPatternInfo> pastUsageDimensionPatterns, Long assetId,
            Long modelId) throws RangeSuggestionException {
      if (ExecueCoreUtil.isCollectionEmpty(pastUsageDimensionPatterns)) {
         return;
      }
      Map<Long, Range> dymanicRangeByBedId = new HashMap<Long, Range>();
      for (QueryHistoryDimensionPatternInfo pastUsageDimensionPattern : pastUsageDimensionPatterns) {
         List<OptimalDSetSWIInfo> optimalDSetSwiInfos = pastUsageDimensionPattern.getOptimalDSetSwiInfos();
         for (OptimalDSetSWIInfo optimalDSetSWIInfo : optimalDSetSwiInfos) {
            if (optimalDSetSWIInfo.getKdxDataType() == ColumnType.MEASURE
                     && getCoreConfigurationService().getDynamicRangeId().equals(optimalDSetSWIInfo.getRangeId())) {
               Long conceptBedId = optimalDSetSWIInfo.getBedId();
               Range range = dymanicRangeByBedId.get(conceptBedId);
               if (range == null) {
                  range = getRangeSuggestionService().deduceRange(
                           RangeSuggestionServiceHelper.populateDynamicRangeInput(modelId, assetId, conceptBedId));
                  dymanicRangeByBedId.put(conceptBedId, range);
               }
               optimalDSetSWIInfo.setRange(range);
            }
         }
      }
   }

   private List<OptimalDSetSWIInfo> getMatchedOptimalDSetSwiInfos (String patternInfo,
            Map<String, OptimalDSetSWIInfo> optimalDSetInfoByBedIdRangeIdMap) {
      List<OptimalDSetSWIInfo> optimalDSetSwiInfos = new ArrayList<OptimalDSetSWIInfo>();
      String[] patternInfoArray = patternInfo.split(getQueryDataConfigurationService()
               .getSqlGroupConcatDefaultDelimeter());
      for (String pattern : patternInfoArray) {
         OptimalDSetSWIInfo optimalDSetSWIInfo = optimalDSetInfoByBedIdRangeIdMap.get(pattern);
         if (optimalDSetSWIInfo == null) {
            // NOTE: This should never come, once we maintain the qdata history in synch with the current state of the
            // asset and model
            if (logger.isDebugEnabled()) {
               logger.debug("Unable to build dimesion pattern info as Bed Id Info no more exists.. " + pattern);
            }
            // TODO:: NK: Should we still continue or break and send empty list as this is not a valid current pattern
            // which
            // can be supported
            continue;
         }

         // NOTE: NK:: Handle the dynamic ranges

         optimalDSetSwiInfos.add(optimalDSetSWIInfo);
      }
      return optimalDSetSwiInfos;
   }

   private Map<String, OptimalDSetSWIInfo> prepateOptimalDSetInfoByBedIdRangeIdMap (
            List<OptimalDSetSWIInfo> optimalDSetSwiInfos) {
      Map<String, OptimalDSetSWIInfo> optimalDSetInfoByBedIdRangeIdMap = new HashMap<String, OptimalDSetSWIInfo>();
      if (ExecueCoreUtil.isCollectionEmpty(optimalDSetSwiInfos)) {
         return optimalDSetInfoByBedIdRangeIdMap;
      }
      for (OptimalDSetSWIInfo optimalDSetSWIInfo : optimalDSetSwiInfos) {
         String optimalDSetSwiInfoKey = getOptimalDSetSwiInfoKey(optimalDSetSWIInfo);
         optimalDSetInfoByBedIdRangeIdMap.put(optimalDSetSwiInfoKey, optimalDSetSWIInfo);
      }
      return optimalDSetInfoByBedIdRangeIdMap;
   }

   private String getOptimalDSetSwiInfoKey (OptimalDSetSWIInfo optimalDSetSWIInfo) {
      String key = "";
      if (optimalDSetSWIInfo.getKdxDataType() == ColumnType.DIMENSION) {
         key = optimalDSetSWIInfo.getBedId().toString();
      } else {
         key = optimalDSetSWIInfo.getBedId() + getQueryDataConfigurationService().getSqlConcatDelimeter()
                  + optimalDSetSWIInfo.getRangeId();
      }
      return key;
   }

   private Map<Long, List<Range>> prepareRangesByBedIdMap (List<Range> allRanges) {
      Map<Long, List<Range>> rangesByBedIdMap = new HashMap<Long, List<Range>>();
      for (Range range : allRanges) {
         Long conceptBedId = range.getConceptBedId();
         List<Range> bedRanges = rangesByBedIdMap.get(conceptBedId);
         if (bedRanges == null) {
            bedRanges = new ArrayList<Range>();
            rangesByBedIdMap.put(conceptBedId, bedRanges);
         }
         bedRanges.add(range);
      }
      return rangesByBedIdMap;
   }

   private List<OptimalDSetSWIInfo> populateOptimalDSetSwiInfos (List<Mapping> existingMappingsForColumns,
            Map<Long, List<Range>> staticRangesByBedId, Long assetId) throws MappingException,
            CloneNotSupportedException, KDXException {
      List<OptimalDSetSWIInfo> optimalDSetSwiInfos = new ArrayList<OptimalDSetSWIInfo>();
      if (ExecueCoreUtil.isCollectionEmpty(existingMappingsForColumns)) {
         return optimalDSetSwiInfos;
      }

      for (Mapping mapping : existingMappingsForColumns) {
         AssetEntityDefinition columnAED = mapping.getAssetEntityDefinition();
         BusinessEntityDefinition columnBED = mapping.getBusinessEntityDefinition();
         ColumnType columnType = columnAED.getColum().getKdxDataType();

         if (columnType == null) {
            if (ExecueConstants.MEASURABLE_ENTITY_TYPE.equals(columnBED.getType().getName())) {
               columnType = ColumnType.MEASURE;
            } else if (getKdxRetrievalService().isConceptMatchedBehavior(columnBED.getId(), BehaviorType.ENUMERATION)) {
               columnType = ColumnType.DIMENSION;
            }
         }

         // Skip if the column type is null or is of type other than dimension or measure
         if (columnType == null
                  || !(ExecueBeanUtil.isColumnTypeDimension(columnType) || ExecueBeanUtil
                           .isColumnTypeMeasure(columnType))) {
            continue;
         }

         OptimalDSetSWIInfo optimalDSetSWIInfo = new OptimalDSetSWIInfo();
         optimalDSetSWIInfo.setAssetId(assetId);
         optimalDSetSWIInfo.setBedId(columnBED.getId());
         optimalDSetSWIInfo.setConceptName(columnBED.getConcept().getName());

         if (ExecueBeanUtil.isColumnTypeDimension(columnType)) {
            optimalDSetSWIInfo.setKdxDataType(ColumnType.DIMENSION);
            Long columnMemberCount = getMappingRetrievalService().getMappedMemberCount(columnAED.getId());
            if (columnMemberCount.intValue() == 0) {
               if (logger.isDebugEnabled()) {
                  logger.debug("Column: " + columnAED.getColum().getId() + " " + columnAED.getColum().getName()
                           + "\tConcept: " + columnBED.getConcept().getId() + " " + columnBED.getConcept().getName());
               }
            }
            optimalDSetSWIInfo.setMemberCount(columnMemberCount.intValue());
            optimalDSetSwiInfos.add(optimalDSetSWIInfo);
         } else if (ExecueBeanUtil.isColumnTypeMeasure(columnType)) {
            optimalDSetSWIInfo.setKdxDataType(ColumnType.MEASURE);
            // Check if the range is defined
            List<Range> ranges = staticRangesByBedId.get(columnBED.getId());
            if (ExecueCoreUtil.isCollectionEmpty(ranges)) {
               // Set the dynamic range id
               // NOTE: We are not deducing the dynamic range here, as its costly and can be done at the end when we know 
               // its being used in past usage patterns
               optimalDSetSWIInfo.setRangeId(getCoreConfigurationService().getDynamicRangeId());
               optimalDSetSwiInfos.add(optimalDSetSWIInfo);
            } else {
               // Cloned and set the static range id and range into the optimalDSetSWIInfo 
               for (Range range : ranges) {
                  OptimalDSetSWIInfo clonedOptimalDSetSWIInfo = (OptimalDSetSWIInfo) optimalDSetSWIInfo.clone();
                  clonedOptimalDSetSWIInfo.setRangeId(range.getId());
                  clonedOptimalDSetSWIInfo.setRange(range);
                  optimalDSetSwiInfos.add(clonedOptimalDSetSWIInfo);
               }
               // NOTE: We are not deducing the dynamic range here, as its costly and can be done at the end when we know 
               // its being used in past usage patterns
               optimalDSetSWIInfo.setRangeId(getCoreConfigurationService().getDynamicRangeId());
               optimalDSetSwiInfos.add(optimalDSetSWIInfo);
            }
         }
      }
      return optimalDSetSwiInfos;
   }

   /**
    * @return the mappingRetrievalService
    */
   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   /**
    * @param mappingRetrievalService
    *           the mappingRetrievalService to set
    */
   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   /**
    * @return the queryDataService
    */
   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   /**
    * @param queryDataService
    *           the queryDataService to set
    */
   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

   /**
    * @return the preferencesRetrievalService
    */
   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return preferencesRetrievalService;
   }

   /**
    * @param preferencesRetrievalService
    *           the preferencesRetrievalService to set
    */
   public void setPreferencesRetrievalService (IPreferencesRetrievalService preferencesRetrievalService) {
      this.preferencesRetrievalService = preferencesRetrievalService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService
    *           the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   /**
    * @return the queryDataConfigurationService
    */
   public IQueryDataConfigurationService getQueryDataConfigurationService () {
      return queryDataConfigurationService;
   }

   /**
    * @param queryDataConfigurationService
    *           the queryDataConfigurationService to set
    */
   public void setQueryDataConfigurationService (IQueryDataConfigurationService queryDataConfigurationService) {
      this.queryDataConfigurationService = queryDataConfigurationService;
   }

   /**
    * @return the rangeSuggestionService
    */
   public IRangeSuggestionService getRangeSuggestionService () {
      return rangeSuggestionService;
   }

   /**
    * @param rangeSuggestionService the rangeSuggestionService to set
    */
   public void setRangeSuggestionService (IRangeSuggestionService rangeSuggestionService) {
      this.rangeSuggestionService = rangeSuggestionService;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }
}