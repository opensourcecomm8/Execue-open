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


package com.execue.ac.algorithm.optimaldset.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetDimensionInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetMartStaticLevelInputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetMeasureInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetPastUsageDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetSpaceCalculationInputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetStaticLevelInputInfo;
import com.execue.ac.algorithm.optimaldset.helper.OptimalDSetHelper;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetContextPopulationService;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetSpaceCalculationService;
import com.execue.ac.algorithm.optimaldset.util.OptimalDSetUtil;
import com.execue.ac.configuration.IAnswersCatalogConfigurationService;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.acqh.exception.AnswersCatalogQueryHistoryException;
import com.execue.acqh.service.QueryHistoryRetrievalServiceFactory;
import com.execue.acqh.type.QueryHistoryRetrievalMethodType;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.qdata.QueryHistoryBusinessEntityInfo;
import com.execue.core.common.bean.qdata.QueryHistoryBusinessEntityPatternInfo;
import com.execue.core.common.bean.qdata.QueryHistoryDimensionPatternInfo;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * @author Vishay
 */
public class OptimalDSetContextPopulationServiceImpl implements IOptimalDSetContextPopulationService {

   private IAnswersCatalogConfigurationService answersCatalogConfigurationService;
   private ICoreConfigurationService           coreConfigurationService;
   private IOptimalDSetSpaceCalculationService optimalDSetSpaceCalculationService;
   private IKDXRetrievalService                kdxRetrievalService;
   private ISDXRetrievalService                sdxRetrievalService;
   private IMappingRetrievalService            mappingRetrievalService;
   private IApplicationRetrievalService        applicationRetrievalService;
   private OptimalDSetHelper                   optimalDSetHelper;

   /**
    * This method builds the information needed later for doing projected space calculation for cubes. It gets the
    * information from swi and populates the space calculation bean. This has the information about population column
    * per cell, dimension column per cell for all distinct dimension, dimension column per table for all the dimensions,
    * stat column per cell and per table, measure columns per row of the cube fact table
    */
   @Override
   public OptimalDSetSpaceCalculationInputInfo populateSpaceCalculationInputInfo (Long parentAssetId,
            OptimalDSetStaticLevelInputInfo staticLevelInputInfo) throws AnswersCatalogException {
      try {
         Integer statsSize = getAnswersCatalogConfigurationService().getDefaultStatNames().size();
         Integer statColumnLength = getAnswersCatalogConfigurationService().getStatColumnPrecision();
         Integer descriptionColumnLength = getAnswersCatalogConfigurationService().getDescriptionColumnPrecision();
         Integer dimensionColumnMinimumLength = getAnswersCatalogConfigurationService()
                  .getDimensionColumnMinimunPrecision();
         Integer measureColumnPrecision = getAnswersCatalogConfigurationService().getMeasureColumnPrecision();
         Integer measureColumnScale = getAnswersCatalogConfigurationService().getMeasureColumnScale();
         Integer measureColumnLength = measureColumnPrecision + 1 + measureColumnScale;
         // total number of measures
         Integer measureConceptsCount = getMeasureConceptsCount(parentAssetId, staticLevelInputInfo);

         List<OptimalDSetDimensionInfo> distinctDimensions = staticLevelInputInfo.getDistinctDimensions();

         // stat length calculation
         Double statFactColumnSizePerCell = OptimalDSetUtil.convertToGigaBytes(statColumnLength.doubleValue());
         Double statLookupTableSizePerRow = OptimalDSetUtil.convertToGigaBytes(statColumnLength.doubleValue()
                  + descriptionColumnLength);
         Double statLookupTableTotalSize = statLookupTableSizePerRow.doubleValue() * statsSize;
         // population column length
         Double populationColumnSizePerCell = OptimalDSetUtil.convertToGigaBytes(measureColumnPrecision.doubleValue());
         // measure length calculation
         Double allMeasureColumnsSizePerRow = OptimalDSetUtil.convertToGigaBytes(measureColumnLength.doubleValue()
                  * measureConceptsCount);
         // dimension length calculation
         Map<Long, Double> dimensionFactColumnSizePerRow = new HashMap<Long, Double>();
         Map<Long, Double> dimensionLookupTableTotalSize = new HashMap<Long, Double>();

         populateDimensionSizeInformation(dimensionFactColumnSizePerRow, dimensionLookupTableTotalSize, parentAssetId,
                  distinctDimensions, staticLevelInputInfo, dimensionColumnMinimumLength, descriptionColumnLength,
                  measureColumnLength);

         return new OptimalDSetSpaceCalculationInputInfo(dimensionFactColumnSizePerRow, dimensionLookupTableTotalSize,
                  allMeasureColumnsSizePerRow, statFactColumnSizePerCell, statLookupTableTotalSize, statsSize,
                  populationColumnSizePerCell);

      } catch (KDXException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      } catch (MappingException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      }
   }

   private void populateDimensionSizeInformation (Map<Long, Double> dimensionFactColumnSizePerRow,
            Map<Long, Double> dimensionLookupTableTotalSize, Long parentAssetId,
            List<OptimalDSetDimensionInfo> distinctDimensions, OptimalDSetStaticLevelInputInfo staticLevelInputInfo,
            Integer dimensionColumnMinimumLength, Integer descriptionColumnLength, Integer measureColumnLength)
            throws MappingException {
      if (!staticLevelInputInfo.isDeduceSpaceAtRuntime()) {
         populateConfiguredDimensionSizeInformation(dimensionFactColumnSizePerRow, dimensionLookupTableTotalSize,
                  distinctDimensions, staticLevelInputInfo, dimensionColumnMinimumLength, descriptionColumnLength,
                  measureColumnLength);
         return;
      }
      // for each bed, mapped to primary column aed.
      Map<Long, AssetEntityDefinition> bedIdAEDMap = populateMappings(parentAssetId, distinctDimensions);
      for (Long bedId : bedIdAEDMap.keySet()) {
         AssetEntityDefinition assetEntityDefinition = bedIdAEDMap.get(bedId);
         Colum colum = assetEntityDefinition.getColum();
         Double lookupValueColumnSizePerCell = OptimalDSetUtil.calculateLengthOfColumnForCube(colum,
                  dimensionColumnMinimumLength);
         Double lookupTableTotalSize = 0d;
         Double lookupTableSizePerRow = lookupValueColumnSizePerCell + descriptionColumnLength;
         OptimalDSetDimensionInfo matchedDimension = OptimalDSetUtil.getMatchingDimension(distinctDimensions, bedId);
         Integer recordCount = matchedDimension.getNumMembers();
         if (matchedDimension.getRange() != null) {
            // lower limit, upper limit
            lookupTableSizePerRow += measureColumnLength + measureColumnLength;
         }
         lookupTableSizePerRow = OptimalDSetUtil.convertToGigaBytes(lookupTableSizePerRow);
         lookupTableTotalSize = recordCount * lookupTableSizePerRow;
         dimensionFactColumnSizePerRow.put(bedId, OptimalDSetUtil.convertToGigaBytes(lookupValueColumnSizePerCell));
         dimensionLookupTableTotalSize.put(bedId, lookupTableTotalSize);
      }
   }

   private void populateConfiguredDimensionSizeInformation (Map<Long, Double> dimensionFactColumnSizePerRow,
            Map<Long, Double> dimensionLookupTableTotalSize, List<OptimalDSetDimensionInfo> distinctDimensions,
            OptimalDSetStaticLevelInputInfo staticLevelInputInfo, Integer dimensionColumnMinimumLength,
            Integer descriptionColumnLength, Integer measureColumnLength) throws MappingException {
      for (OptimalDSetDimensionInfo optimalDSetDimensionInfo : distinctDimensions) {
         Colum colum = new Colum();
         colum.setPrecision(staticLevelInputInfo.getConfiguredDimensionLookupValueColumnSize());
         colum.setScale(0);
         Double lookupValueColumnSizePerCell = OptimalDSetUtil.calculateLengthOfColumnForCube(colum,
                  dimensionColumnMinimumLength);

         Double lookupTableTotalSize = 0d;
         Double lookupTableSizePerRow = lookupValueColumnSizePerCell + descriptionColumnLength;
         Integer recordCount = optimalDSetDimensionInfo.getNumMembers();
         if (optimalDSetDimensionInfo.getRange() != null) {
            // lower limit, upper limit
            lookupTableSizePerRow += measureColumnLength + measureColumnLength;
         }

         lookupTableSizePerRow = OptimalDSetUtil.convertToGigaBytes(lookupTableSizePerRow);
         lookupTableTotalSize = recordCount * lookupTableSizePerRow;
         dimensionFactColumnSizePerRow.put(optimalDSetDimensionInfo.getBedId(), OptimalDSetUtil
                  .convertToGigaBytes(lookupValueColumnSizePerCell));
         dimensionLookupTableTotalSize.put(optimalDSetDimensionInfo.getBedId(), lookupTableTotalSize);
      }
   }

   private Integer getMeasureConceptsCount (Long parentAssetId, OptimalDSetStaticLevelInputInfo staticLevelInputInfo)
            throws KDXException {
      Integer measureConceptsCount = 0;
      if (staticLevelInputInfo.isDeduceSpaceAtRuntime()) {
         measureConceptsCount = getKdxRetrievalService().getMeasureConceptsCountForAsset(parentAssetId);
      } else {
         measureConceptsCount = staticLevelInputInfo.getConfiguredNumberOfMeasuresInParentAsset();
      }
      return measureConceptsCount;
   }

   private Map<Long, AssetEntityDefinition> populateMappings (Long parentAssetId,
            List<OptimalDSetDimensionInfo> distinctDimensions) throws MappingException {
      List<Long> distinctDimensionBedIds = OptimalDSetUtil.getDistinctDimensionBedIds(distinctDimensions);
      List<Mapping> primaryMappings = getMappingRetrievalService().getPrimaryMapping(distinctDimensionBedIds,
               parentAssetId);
      Map<Long, AssetEntityDefinition> bedIdAEDMap = new HashMap<Long, AssetEntityDefinition>();
      for (Mapping mapping : primaryMappings) {
         bedIdAEDMap.put(mapping.getBusinessEntityDefinition().getId(), mapping.getAssetEntityDefinition());
      }
      return bedIdAEDMap;
   }

   /**
    * This method populates the static information required per level.
    */
   @Override
   public OptimalDSetStaticLevelInputInfo populateStaticLevelInputInfo (Long parentAssetId, Long modelId)
            throws AnswersCatalogException {
      OptimalDSetStaticLevelInputInfo staticLevelInputInfo = new OptimalDSetStaticLevelInputInfo();

      staticLevelInputInfo.setApplyConstraints(getAnswersCatalogConfigurationService()
               .applyContraintsInOptimalDSetAlgorithm());
      staticLevelInputInfo.setDeduceSpaceAtRuntime(getAnswersCatalogConfigurationService()
               .deduceSpaceAtRuntimeInOptimalDSetAlgorithm());
      staticLevelInputInfo.setConfiguredParentAssetSpace(getAnswersCatalogConfigurationService()
               .getConfiguredParentAssetSpaceForOptimalDSetAlgorithm());
      staticLevelInputInfo.setConfiguredNumberOfMeasuresInParentAsset(getAnswersCatalogConfigurationService()
               .getConfiguredNumberOfMeasuresInParentAsset());
      staticLevelInputInfo.setConfiguredDimensionLookupValueColumnSize(getAnswersCatalogConfigurationService()
               .getConfiguredDimensionLookupValueColumnSize());

      List<OptimalDSetPastUsageDimensionPattern> allPastUsagePatterns = new ArrayList<OptimalDSetPastUsageDimensionPattern>();
      // TODO: NK: Once the actual flow is tested then can get rid off below dummy data preparation calls
      // allPastUsagePatterns = buildPastUsagePatternsForDemoCards();
      allPastUsagePatterns = buildPastUsagePatterns();
      // allPastUsagePatterns = buildPastUsagePatternsForOldDiscover();

      try {
         QueryHistoryRetrievalMethodType methodType = QueryHistoryRetrievalMethodType.QDATA;
         if (!getAnswersCatalogConfigurationService().buildQueryHistoryFromQdata()) {
            methodType = QueryHistoryRetrievalMethodType.FILE_BASED;
         }
         List<QueryHistoryDimensionPatternInfo> queryHistoryDimensionPatternInfos = QueryHistoryRetrievalServiceFactory
                  .getInstance().getQueryHistoryRetrievalService(methodType).buildQueryHistoryDimensionPatternInfo(
                           parentAssetId, modelId, getQueryExecutionDateForCube());
         allPastUsagePatterns = getOptimalDSetHelper().transformQueryHistoryDimensionPatternInfo(
                  queryHistoryDimensionPatternInfos);
      } catch (AnswersCatalogQueryHistoryException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      }

      staticLevelInputInfo.setAllPastUsagePatterns(allPastUsagePatterns);
      staticLevelInputInfo.setDistinctDimensions(OptimalDSetUtil.getDistinctDimensions(staticLevelInputInfo
               .getAllPastUsagePatterns()));
      staticLevelInputInfo.setMaxSpacePercentage(getAnswersCatalogConfigurationService().getMaxSpacePercentage());
      staticLevelInputInfo.setMinUsagePercentage(getAnswersCatalogConfigurationService().getMinUsagePercentage());
      staticLevelInputInfo.setParentAssetId(parentAssetId);
      staticLevelInputInfo.setParentAssetSpace(getOptimalDSetSpaceCalculationService().computeSpaceForParentAsset(
               parentAssetId, staticLevelInputInfo));

      return staticLevelInputInfo;
   }

   @Override
   public OptimalDSetMartStaticLevelInputInfo populateMartStaticLevelInputInfo (Long parentAssetId, Long modelId)
            throws AnswersCatalogException {
      OptimalDSetMartStaticLevelInputInfo martStaticLevelInputInfo = new OptimalDSetMartStaticLevelInputInfo();
      martStaticLevelInputInfo.setParentAssetId(parentAssetId);

      List<OptimalDSetDimensionInfo> pastUsageDimensions = new ArrayList<OptimalDSetDimensionInfo>();
      List<OptimalDSetMeasureInfo> pastUsageMeasures = new ArrayList<OptimalDSetMeasureInfo>();

      // TODO: NK: Once the actual flow is tested then can get rid off below dummy data preparation calls
      // pastUsageDimensions = buildPastUsageDimensions();
      // pastUsageMeasures = buildPastUsageMeasures();
      try {
         QueryHistoryRetrievalMethodType methodType = QueryHistoryRetrievalMethodType.QDATA;
         if (!getAnswersCatalogConfigurationService().buildQueryHistoryFromQdata()) {
            methodType = QueryHistoryRetrievalMethodType.FILE_BASED;
         }
         QueryHistoryBusinessEntityPatternInfo pastUsageBusinessEntityInfo = QueryHistoryRetrievalServiceFactory
                  .getInstance().getQueryHistoryRetrievalService(methodType)
                  .buildQueryHistoryBusinessEntityPatternInfo(parentAssetId, modelId, getQueryExecutionDateForMart(),
                           getAnswersCatalogConfigurationService().getMartMaxDimensions());

         List<QueryHistoryBusinessEntityInfo> qdataDimensionsInfo = pastUsageBusinessEntityInfo.getDimensions();
         List<QueryHistoryBusinessEntityInfo> qdataMeasuresInfo = pastUsageBusinessEntityInfo.getMeasures();

         pastUsageDimensions = getOptimalDSetHelper().transformPastUsageDimensionInfo(qdataDimensionsInfo);
         pastUsageMeasures = getOptimalDSetHelper().transformPastUsageMeasureInfo(qdataMeasuresInfo);
      } catch (AnswersCatalogQueryHistoryException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      }

      martStaticLevelInputInfo.setPastUsageDimensions(pastUsageDimensions);
      martStaticLevelInputInfo.setPastUsageMeasures(pastUsageMeasures);
      martStaticLevelInputInfo.setMaxEligibleDimensions(getAnswersCatalogConfigurationService().getMartMaxDimensions());
      martStaticLevelInputInfo.setMaxEligibleMeasures(getAnswersCatalogConfigurationService().getMartMaxMeasures());
      return martStaticLevelInputInfo;
   }

   private Date getQueryExecutionDateForCube () {
      int days = getAnswersCatalogConfigurationService().getPastPatternsToConsiderForOptimalDSetInDaysForCube();
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -days);
      return cal.getTime();
   }

   private Date getQueryExecutionDateForMart () {
      int days = getAnswersCatalogConfigurationService().getPastPatternsToConsiderForOptimalDSetInDaysForMart();
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -days);
      return cal.getTime();
   }

   private List<OptimalDSetMeasureInfo> buildPastUsageMeasures () {
      List<OptimalDSetMeasureInfo> measures = new ArrayList<OptimalDSetMeasureInfo>();
      OptimalDSetMeasureInfo a = new OptimalDSetMeasureInfo(10001l, "A", 5d);
      OptimalDSetMeasureInfo b = new OptimalDSetMeasureInfo(10001l, "B", 5d);
      OptimalDSetMeasureInfo c = new OptimalDSetMeasureInfo(10001l, "D", 5d);
      OptimalDSetMeasureInfo d = new OptimalDSetMeasureInfo(10001l, "D", 5d);
      OptimalDSetMeasureInfo e = new OptimalDSetMeasureInfo(10001l, "E", 5d);
      OptimalDSetMeasureInfo f = new OptimalDSetMeasureInfo(10001l, "F", 5d);
      OptimalDSetMeasureInfo g = new OptimalDSetMeasureInfo(10001l, "G", 5d);
      OptimalDSetMeasureInfo h = new OptimalDSetMeasureInfo(10001l, "H", 5d);
      OptimalDSetMeasureInfo i = new OptimalDSetMeasureInfo(10001l, "I", 5d);
      measures.add(a);
      measures.add(b);
      measures.add(c);
      measures.add(d);
      measures.add(e);
      measures.add(f);
      measures.add(g);
      measures.add(h);
      measures.add(i);
      return measures;
   }

   private List<OptimalDSetDimensionInfo> buildPastUsageDimensions () {
      List<OptimalDSetDimensionInfo> dimensions = new ArrayList<OptimalDSetDimensionInfo>();
      OptimalDSetDimensionInfo a = new OptimalDSetDimensionInfo(10001l, "Currency", 5d);
      OptimalDSetDimensionInfo b = new OptimalDSetDimensionInfo(10022l, "Customer", 5d);
      OptimalDSetDimensionInfo d = new OptimalDSetDimensionInfo(10007l, "FOB", 3d);
      OptimalDSetDimensionInfo e = new OptimalDSetDimensionInfo(10004l, "FreightCarrier", 5d);
      OptimalDSetDimensionInfo f = new OptimalDSetDimensionInfo(10005l, "FreightTerm", 2d);
      OptimalDSetDimensionInfo g = new OptimalDSetDimensionInfo(10006l, "ABC", 10d);
      OptimalDSetDimensionInfo h = new OptimalDSetDimensionInfo(10007l, "DEF", 12d);
      OptimalDSetDimensionInfo i = new OptimalDSetDimensionInfo(10008l, "GHI", 11d);
      OptimalDSetDimensionInfo j = new OptimalDSetDimensionInfo(10009l, "JKL", 2d);
      dimensions.add(a);
      dimensions.add(b);
      dimensions.add(d);
      dimensions.add(e);
      dimensions.add(f);
      dimensions.add(g);
      dimensions.add(h);
      dimensions.add(i);
      dimensions.add(j);
      return dimensions;
   }

   private List<OptimalDSetPastUsageDimensionPattern> buildPastUsagePatternsForDemoCards () {

      // A = REVOLVE_FLAG = Revolving = 10041 = 2 ( RANGE ) min(0) max(1) band(1)
      // B = DEL_BAL_FLAG = Delinquent = 10046 = 2 ( RANGE ) min(0) max(1) band(1)
      // C = BEH_SCORE_NUMBER = BehaviorScore = 10026 = 10 ( RANGE ) min(0) max(999) band(100)
      // P = MRC_AMT = MerchandiseAmount = 10017 = 5 ( RANGE ) min(0) max(1000000) band(200000)
      // Q = EXT_STATUS = AccountStatus = 10012 = 10 ( SL ) min() max() band()
      // R = OPEN_IND = Open = 10086 = 2 ( RANGE ) min(0) max(1) band(1)
      // S = CREDIT_LINE = TotalCreditLimit = 10058 = 8 ( RANGE ) min(0) max(5000) band(675)
      // X = MRC_BAL = MerchandiseBalance = 10016 = 8 ( RANGE ) min(0) max(1000000) band(125000)
      // Y = FICO_SCORE_NUMBER = FicoScore = 10025 = 20 ( RANGE ) min(0) max(999) band(50)
      // Z = PERF_YM = PerformanceMonth = 10309 = 27 ( SL ) min() max() band()

      Range revolvingRange = new Range();
      Set<RangeDetail> rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(buildRangeDetail("0", "Revolving", 1d, 1d));
      rangeDetails.add(buildRangeDetail("1", "Non Revolving", 2d, 2d));
      revolvingRange.setRangeDetails(rangeDetails);

      Range delinqRange = new Range();
      rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(buildRangeDetail("0", "Delinquent", 1d, 1d));
      rangeDetails.add(buildRangeDetail("1", "Non Delinquent", 2d, 2d));
      delinqRange.setRangeDetails(rangeDetails);

      Range openIndRange = new Range();
      rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(buildRangeDetail("0", "Open", 1d, 1d));
      rangeDetails.add(buildRangeDetail("1", "Closed", 2d, 2d));
      openIndRange.setRangeDetails(rangeDetails);

      Range behRange = new Range();
      rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(buildRangeDetail("0", "0", 0d, 100d));
      rangeDetails.add(buildRangeDetail("1", "1", 101d, 200d));
      rangeDetails.add(buildRangeDetail("2", "2", 201d, 300d));
      rangeDetails.add(buildRangeDetail("3", "3", 301d, 400d));
      rangeDetails.add(buildRangeDetail("4", "4", 401d, 500d));
      rangeDetails.add(buildRangeDetail("5", "5", 501d, 600d));
      rangeDetails.add(buildRangeDetail("6", "6", 601d, 700d));
      rangeDetails.add(buildRangeDetail("7", "7", 701d, 800d));
      rangeDetails.add(buildRangeDetail("8", "8", 801d, 900d));
      rangeDetails.add(buildRangeDetail("9", "9", 901d, 999d));
      behRange.setRangeDetails(rangeDetails);

      Range amountRange = new Range();
      rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(buildRangeDetail("0", "0", 0d, 200000d));
      rangeDetails.add(buildRangeDetail("1", "1", 200001d, 400000d));
      rangeDetails.add(buildRangeDetail("2", "2", 400001d, 600000d));
      rangeDetails.add(buildRangeDetail("3", "3", 600001d, 800000d));
      rangeDetails.add(buildRangeDetail("4", "4", 800001d, 1000000d));
      amountRange.setRangeDetails(rangeDetails);

      Range creditLimitRange = new Range();
      rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(buildRangeDetail("0", "0", 0d, 625d));
      rangeDetails.add(buildRangeDetail("1", "1", 626d, 1250d));
      rangeDetails.add(buildRangeDetail("2", "2", 1251d, 1875d));
      rangeDetails.add(buildRangeDetail("3", "3", 1876d, 2500d));
      rangeDetails.add(buildRangeDetail("4", "4", 2501d, 3125d));
      rangeDetails.add(buildRangeDetail("5", "5", 3126d, 3750d));
      rangeDetails.add(buildRangeDetail("6", "6", 3756d, 4375d));
      rangeDetails.add(buildRangeDetail("7", "7", 4376d, 5000d));
      creditLimitRange.setRangeDetails(rangeDetails);

      Range balanceRange = new Range();
      rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(buildRangeDetail("0", "0", 0d, 125000d));
      rangeDetails.add(buildRangeDetail("1", "1", 125001d, 250000d));
      rangeDetails.add(buildRangeDetail("2", "2", 250001d, 375000d));
      rangeDetails.add(buildRangeDetail("3", "3", 375001d, 500000d));
      rangeDetails.add(buildRangeDetail("4", "4", 500001d, 625000d));
      rangeDetails.add(buildRangeDetail("5", "5", 625001d, 750000d));
      rangeDetails.add(buildRangeDetail("6", "6", 750001d, 875000d));
      rangeDetails.add(buildRangeDetail("7", "7", 875001d, 1000000d));
      balanceRange.setRangeDetails(rangeDetails);

      Range ficoRange = new Range();
      rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(buildRangeDetail("0", "0", 0d, 50d));
      rangeDetails.add(buildRangeDetail("1", "1", 51d, 100d));
      rangeDetails.add(buildRangeDetail("2", "2", 101d, 150d));
      rangeDetails.add(buildRangeDetail("3", "3", 151d, 200d));
      rangeDetails.add(buildRangeDetail("4", "4", 201d, 250d));
      rangeDetails.add(buildRangeDetail("5", "5", 251d, 300d));
      rangeDetails.add(buildRangeDetail("6", "6", 301d, 350d));
      rangeDetails.add(buildRangeDetail("7", "7", 351d, 400d));
      rangeDetails.add(buildRangeDetail("8", "8", 401d, 450d));
      rangeDetails.add(buildRangeDetail("9", "9", 451d, 500d));
      rangeDetails.add(buildRangeDetail("10", "10", 501d, 550d));
      rangeDetails.add(buildRangeDetail("11", "11", 551d, 600d));
      rangeDetails.add(buildRangeDetail("12", "12", 601d, 650d));
      rangeDetails.add(buildRangeDetail("13", "13", 651d, 700d));
      rangeDetails.add(buildRangeDetail("14", "14", 701d, 750d));
      rangeDetails.add(buildRangeDetail("15", "15", 751d, 800d));
      rangeDetails.add(buildRangeDetail("16", "16", 801d, 850d));
      rangeDetails.add(buildRangeDetail("17", "17", 851d, 900d));
      rangeDetails.add(buildRangeDetail("18", "18", 901d, 950d));
      rangeDetails.add(buildRangeDetail("19", "19", 951d, 999d));
      ficoRange.setRangeDetails(rangeDetails);

      OptimalDSetDimensionInfo a = new OptimalDSetDimensionInfo(10041l, "Revolving", revolvingRange);
      OptimalDSetDimensionInfo b = new OptimalDSetDimensionInfo(10046l, "Delinquent", delinqRange);
      OptimalDSetDimensionInfo c = new OptimalDSetDimensionInfo(10026l, "BehaviorScore", behRange);
      OptimalDSetDimensionInfo p = new OptimalDSetDimensionInfo(10017l, "MerchandiseAmount", amountRange);
      OptimalDSetDimensionInfo q = new OptimalDSetDimensionInfo(10012l, "AccountStatus", 10);
      OptimalDSetDimensionInfo r = new OptimalDSetDimensionInfo(10086l, "Open", openIndRange);
      OptimalDSetDimensionInfo s = new OptimalDSetDimensionInfo(10058l, "TotalCreditLimit", creditLimitRange);
      OptimalDSetDimensionInfo x = new OptimalDSetDimensionInfo(10016l, "MerchandiseBalance", balanceRange);
      OptimalDSetDimensionInfo y = new OptimalDSetDimensionInfo(10025l, "FicoScore", ficoRange);
      OptimalDSetDimensionInfo z = new OptimalDSetDimensionInfo(10309l, "PerformanceMonth", 27);

      List<OptimalDSetPastUsageDimensionPattern> pastUsageDimensionPatterns = new ArrayList<OptimalDSetPastUsageDimensionPattern>();
      // pastUsageDimensionPatterns.add(buildPastUsagePattern(2d, a, x, p));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(4d, q, a, z, y));
      // pastUsageDimensionPatterns.add(buildPastUsagePattern(3d, z, x, a));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(5d, r, y));
      // pastUsageDimensionPatterns.add(buildPastUsagePattern(1d, b, c));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(6d, a, x, z, s));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(4d, s, p, a));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(8d, x, a, y));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(4d, b, z, x));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(4d, c, b, r, a));
      // pastUsageDimensionPatterns.add(buildPastUsagePattern(2d, q, y, b));
      // pastUsageDimensionPatterns.add(buildPastUsagePattern(3d, p, x, a));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(5d, q, z, b));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(5d, y, b, a, c));
      // pastUsageDimensionPatterns.add(buildPastUsagePattern(2d, b, y, z));
      // pastUsageDimensionPatterns.add(buildPastUsagePattern(1d, s, a, x));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(6d, a, p, z));
      // pastUsageDimensionPatterns.add(buildPastUsagePattern(2d, p, a, r));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(7d, r, y, p));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(2d, x, b, c));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(3d, q, b, z));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(5d, c, b, a));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(5d, b, a, x));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(9d, z, c, s));
      // pastUsageDimensionPatterns.add(buildPastUsagePattern(2d, r, q, r, s));
      return pastUsageDimensionPatterns;
   }

   private List<OptimalDSetPastUsageDimensionPattern> buildPastUsagePatterns () {

      // column_name,concept_name,bed_id,num_members
      // A = currency_code = Currency = 10001 = 3
      // B = custome_name = Customer = 10022 = 70
      // D = fob_code = FOB = 10007 = 9
      // E = freight_carrier_code = FreightCarrier = 10004 = 9
      // F = frieght_terms_code = FreightTerm = 10005 = 8
      // G = order_total = TotalPrice = 10012 = max(range) = 32236320 band_value = 10745440
      // J = gl-mbr_code = GLMBR = 10021 = 12
      List<OptimalDSetPastUsageDimensionPattern> pastUsageDimensionPatterns = new ArrayList<OptimalDSetPastUsageDimensionPattern>();
      OptimalDSetDimensionInfo a = new OptimalDSetDimensionInfo(10001l, "Currency", 3);
      OptimalDSetDimensionInfo b = new OptimalDSetDimensionInfo(10022l, "Customer", 70);
      OptimalDSetDimensionInfo d = new OptimalDSetDimensionInfo(10007l, "FOB", 9);
      OptimalDSetDimensionInfo e = new OptimalDSetDimensionInfo(10004l, "FreightCarrier", 9);
      OptimalDSetDimensionInfo f = new OptimalDSetDimensionInfo(10005l, "FreightTerm", 8);
      Range range = new Range();
      Set<RangeDetail> rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(buildRangeDetail("Low", "Low", 0d, 10745440d));
      rangeDetails.add(buildRangeDetail("Mid", "Mid", 10745441d, 21490880d));
      rangeDetails.add(buildRangeDetail("High", "High", 21490881d, 32236320d));
      range.setRangeDetails(rangeDetails);
      OptimalDSetDimensionInfo g = new OptimalDSetDimensionInfo(10012l, "TotalPrice", range);
      OptimalDSetDimensionInfo j = new OptimalDSetDimensionInfo(10021l, "GLMBR", 12);

      pastUsageDimensionPatterns.add(buildPastUsagePattern(5.0, a));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(15.0, d));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(15.0, e));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(5.0, f));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(5.0, a, f));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(15.0, b, g));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(5.0, a, b, e));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(15.0, b, e));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(5.0, j));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(15.0, d, f));
      return pastUsageDimensionPatterns;
   }

   private List<OptimalDSetPastUsageDimensionPattern> buildPastUsagePatternsForOldDiscover () {

      // 10086 Open 2
      // 10309 PerformanceMonth 27
      // 10048 Product 5
      // 10014 BillingBalance 3
      // 10041 Revolving 2
      // 10025 FicoScore 3
      // 10052 PortfolioSegmentGroup 5
      // 10023 PortfolioSegment 20
      // 10254 JoiningMonth 336

      Range revolvingRange = new Range();
      Set<RangeDetail> rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(buildRangeDetail("0", "Revolving", 1d, 1d));
      rangeDetails.add(buildRangeDetail("1", "Non Revolving", 2d, 2d));
      revolvingRange.setRangeDetails(rangeDetails);

      Range openIndRange = new Range();
      rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(buildRangeDetail("0", "Open", 1d, 1d));
      rangeDetails.add(buildRangeDetail("1", "Closed", 2d, 2d));
      openIndRange.setRangeDetails(rangeDetails);

      Range balanceRange = new Range();
      rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(buildRangeDetail("0", "0", 0d, 125000d));
      rangeDetails.add(buildRangeDetail("1", "1", 125001d, 250000d));
      rangeDetails.add(buildRangeDetail("2", "2", 250001d, 375000d));
      rangeDetails.add(buildRangeDetail("3", "3", 375001d, 500000d));
      rangeDetails.add(buildRangeDetail("4", "4", 500001d, 625000d));
      rangeDetails.add(buildRangeDetail("5", "5", 625001d, 750000d));
      rangeDetails.add(buildRangeDetail("6", "6", 750001d, 875000d));
      rangeDetails.add(buildRangeDetail("7", "7", 875001d, 1000000d));
      balanceRange.setRangeDetails(rangeDetails);

      Range ficoRange = new Range();
      rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(buildRangeDetail("0", "0", 0d, 50d));
      rangeDetails.add(buildRangeDetail("1", "1", 51d, 100d));
      rangeDetails.add(buildRangeDetail("2", "2", 101d, 150d));
      rangeDetails.add(buildRangeDetail("3", "3", 151d, 200d));
      rangeDetails.add(buildRangeDetail("4", "4", 201d, 250d));
      rangeDetails.add(buildRangeDetail("5", "5", 251d, 300d));
      rangeDetails.add(buildRangeDetail("6", "6", 301d, 350d));
      rangeDetails.add(buildRangeDetail("7", "7", 351d, 400d));
      rangeDetails.add(buildRangeDetail("8", "8", 401d, 450d));
      rangeDetails.add(buildRangeDetail("9", "9", 451d, 500d));
      rangeDetails.add(buildRangeDetail("10", "10", 501d, 550d));
      rangeDetails.add(buildRangeDetail("11", "11", 551d, 600d));
      rangeDetails.add(buildRangeDetail("12", "12", 601d, 650d));
      rangeDetails.add(buildRangeDetail("13", "13", 651d, 700d));
      rangeDetails.add(buildRangeDetail("14", "14", 701d, 750d));
      rangeDetails.add(buildRangeDetail("15", "15", 751d, 800d));
      rangeDetails.add(buildRangeDetail("16", "16", 801d, 850d));
      rangeDetails.add(buildRangeDetail("17", "17", 851d, 900d));
      rangeDetails.add(buildRangeDetail("18", "18", 901d, 950d));
      rangeDetails.add(buildRangeDetail("19", "19", 951d, 999d));
      ficoRange.setRangeDetails(rangeDetails);

      OptimalDSetDimensionInfo a = new OptimalDSetDimensionInfo(10086l, "Open", openIndRange);
      OptimalDSetDimensionInfo b = new OptimalDSetDimensionInfo(10309l, "PerformanceMonth", 27);
      OptimalDSetDimensionInfo c = new OptimalDSetDimensionInfo(10048l, "Product", 5);
      OptimalDSetDimensionInfo d = new OptimalDSetDimensionInfo(10014l, "BillingBalance", balanceRange);
      OptimalDSetDimensionInfo e = new OptimalDSetDimensionInfo(10041l, "Revolving", revolvingRange);
      OptimalDSetDimensionInfo f = new OptimalDSetDimensionInfo(10025l, "FicoScore", ficoRange);
      OptimalDSetDimensionInfo g = new OptimalDSetDimensionInfo(10052l, "PortfolioSegmentGroup", 5);
      OptimalDSetDimensionInfo h = new OptimalDSetDimensionInfo(10023l, "PortfolioSegment", 20);
      OptimalDSetDimensionInfo i = new OptimalDSetDimensionInfo(10254l, "JoiningMonth", 336);

      List<OptimalDSetPastUsageDimensionPattern> pastUsageDimensionPatterns = new ArrayList<OptimalDSetPastUsageDimensionPattern>();

      pastUsageDimensionPatterns.add(buildPastUsagePattern(62.57d, i, b));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(8.28d, f, i, b));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(7.3d, b));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(3.19d, c, i, b));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(3.04d, f, b));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(2.99d, c, b));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(1.39d, d, i, b));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(1.099d, a, b));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(1.094d, g, b));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(0.97d, e, i, b));
      pastUsageDimensionPatterns.add(buildPastUsagePattern(0.77d, h, b));

      return pastUsageDimensionPatterns;
   }

   private RangeDetail buildRangeDetail (String value, String description, Double lowerLimit, Double upperLimit) {
      RangeDetail rangeDetail = new RangeDetail();
      rangeDetail.setValue(value);
      rangeDetail.setDescription(description);
      rangeDetail.setLowerLimit(lowerLimit);
      rangeDetail.setUpperLimit(upperLimit);
      return rangeDetail;
   }

   private OptimalDSetPastUsageDimensionPattern buildPastUsagePattern (Double usagePercentage,
            OptimalDSetDimensionInfo... dimensions) {
      return new OptimalDSetPastUsageDimensionPattern(Arrays.asList(dimensions), usagePercentage);
   }

   public IAnswersCatalogConfigurationService getAnswersCatalogConfigurationService () {
      return answersCatalogConfigurationService;
   }

   public void setAnswersCatalogConfigurationService (
            IAnswersCatalogConfigurationService answersCatalogConfigurationService) {
      this.answersCatalogConfigurationService = answersCatalogConfigurationService;
   }

   public IOptimalDSetSpaceCalculationService getOptimalDSetSpaceCalculationService () {
      return optimalDSetSpaceCalculationService;
   }

   public void setOptimalDSetSpaceCalculationService (
            IOptimalDSetSpaceCalculationService optimalDSetSpaceCalculationService) {
      this.optimalDSetSpaceCalculationService = optimalDSetSpaceCalculationService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   /**
    * @return the optimalDSetHelper
    */
   public OptimalDSetHelper getOptimalDSetHelper () {
      return optimalDSetHelper;
   }

   /**
    * @param optimalDSetHelper
    *           the optimalDSetHelper to set
    */
   public void setOptimalDSetHelper (OptimalDSetHelper optimalDSetHelper) {
      this.optimalDSetHelper = optimalDSetHelper;
   }

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService
    *           the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

}
