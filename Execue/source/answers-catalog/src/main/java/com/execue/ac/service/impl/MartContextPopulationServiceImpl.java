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


package com.execue.ac.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.ac.bean.AnswersCatalogConfigurationContext;
import com.execue.ac.bean.BasicSamplingAlgorithmStaticInput;
import com.execue.ac.bean.BatchCountAlgorithmStaticInput;
import com.execue.ac.bean.ConceptColumnMapping;
import com.execue.ac.bean.MartConfigurationContext;
import com.execue.ac.bean.MartCreationInputInfo;
import com.execue.ac.bean.MartCreationPopulatedContext;
import com.execue.ac.bean.MartInputParametersContext;
import com.execue.ac.bean.SamplingAlgorithmStaticInput;
import com.execue.ac.bean.SlicingAlgorithmStaticInput;
import com.execue.ac.bean.SourceAssetMappingInfo;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.MartContextPopulationException;
import com.execue.ac.helper.MartCreationServiceHelper;
import com.execue.ac.service.IAnswersCatalogConstants;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.ac.service.IMartContextPopulationService;
import com.execue.ac.service.IMartOperationalConstants;
import com.execue.ac.service.IMartTotalSlicesCalculatorService;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.ac.util.SampleSizeFormulaUtil;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.platform.ISamplingStrategyIdentificationService;
import com.execue.platform.exception.PlatformException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.swi.service.IUserManagementService;

/**
 * This service will populate the mart context. It means populate the input needed for mart creation.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartContextPopulationServiceImpl extends AnswersCatalogContextPopulationServiceImpl implements
         IMartContextPopulationService, IAnswersCatalogConstants, IMartOperationalConstants {

   private MartCreationServiceHelper              martCreationServiceHelper;
   private IApplicationRetrievalService           applicationRetrievalService;
   private IKDXRetrievalService                   kdxRetrievalService;
   private IBaseKDXRetrievalService               baseKDXRetrievalService;
   private IUserManagementService                 userManagementService;
   private IAnswersCatalogDataAccessService       answersCatalogDataAccessService;
   private IMartTotalSlicesCalculatorService      martTotalSlicesCalculatorService;
   private ISDXRetrievalService                   sdxRetrievalService;
   private IMappingRetrievalService               mappingRetrievalService;
   private ISamplingStrategyIdentificationService samplingStrategyIdentificationService;
   private static final Logger                    logger = Logger.getLogger(MartContextPopulationServiceImpl.class);

   /**
    * This method populates the input required for building the mart. It populates the information from swi,
    * configuration etc.
    * 
    * @param martCreationContext
    * @return mart creation input info
    * @throws MartContextPopulationException
    */
   public MartCreationInputInfo populateMartCreationInputInfo (MartCreationContext martCreationContext)
            throws MartContextPopulationException {
      MartCreationInputInfo martCreationInputInfo = null;
      JobRequest jobRequest = martCreationContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         boolean useBasicSamplingAlgo = getAnswersCatalogConfigurationService().useBasicSamplingAlgo();
         BasicSamplingAlgorithmStaticInput basicSamplingAlgorithmStaticInput = null;
         SamplingAlgorithmStaticInput samplingAlgorithmStaticInput = null;
         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  POPULATE_GENERIC_ANSWERS_CATALOG_CONFIGURATION_CONTEXT);
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the Answers Catalog Context from configuration");
         }
         AnswersCatalogConfigurationContext answersCatalogConfigurationContext = populateAnswersCatalogConfigurationContext();
         getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  POPULATE_SPECIFIC_MART_CONFIGURATION_CONTEXT);
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the mart configuration Context from configuration");
         }
         MartConfigurationContext martConfigurationContext = populateMartConfigurationContext();
         getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  POPULATE_MART_CREATION_CONTEXT_FROM_SWI);
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the mart Context from swi");
         }
         MartCreationPopulatedContext martCreationPopulatedContext = populateMartCreationContext(martCreationContext,
                  martConfigurationContext, answersCatalogConfigurationContext);
         getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  POPULATE_SLICING_ALGORITHM_STATIC_INPUT);
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the slicing algo static input information");
         }
         SlicingAlgorithmStaticInput slicingAlgorithmStaticInput = populateSlicingAlgorithmStaticInput();
         getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  POPULATE_MART_INPUT_PARAMETERS_CONTEXT);
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the input parameters required for mart creation");
         }
         MartInputParametersContext martCreationInputParametersContext = populateMartInputParametersContext(
                  martCreationPopulatedContext, slicingAlgorithmStaticInput);
         getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  POPULATE_SAMPLING_ALGORITHM_STATIC_INPUT);
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the sampling algo static input information");
         }
         if (useBasicSamplingAlgo) {
            basicSamplingAlgorithmStaticInput = populateBasicSamplingAlgorithmStaticInput(martCreationInputParametersContext
                     .getNumberOfSlices());
         } else {
            samplingAlgorithmStaticInput = populateSamplingAlgorithmStaticInput();
         }
         getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  POPULATE_BATCH_ALGORITHM_STATIC_INPUT);
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the Batch count algo static input information");
         }
         BatchCountAlgorithmStaticInput batchCountAlgorithmStaticInput = populateBatchCountAlgorithmStaticInput(martCreationInputParametersContext
                  .getPopulationMaxDataLength());
         getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         martCreationInputInfo = new MartCreationInputInfo();
         martCreationInputInfo.setMartCreationContext(martCreationContext);
         martCreationInputInfo.setAnswersCatalogConfigurationContext(answersCatalogConfigurationContext);
         martCreationInputInfo.setMartConfigurationContext(martConfigurationContext);
         martCreationInputInfo.setMartCreationPopulatedContext(martCreationPopulatedContext);
         martCreationInputInfo.setMartCreationInputParametersContext(martCreationInputParametersContext);
         martCreationInputInfo.setBasicSamplingAlgorithmStaticInput(basicSamplingAlgorithmStaticInput);
         martCreationInputInfo.setSamplingAlgorithmStaticInput(samplingAlgorithmStaticInput);
         martCreationInputInfo.setSlicingAlgorithmStaticInput(slicingAlgorithmStaticInput);
         martCreationInputInfo.setBatchCountAlgorithmStaticInput(batchCountAlgorithmStaticInput);
         martCreationInputInfo.setUseBasicSamplingAlgorithm(useBasicSamplingAlgo);
      } catch (SWIException e) {
         exception = e;
         throw new MartContextPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_CONEXT_POPULATION_EXCEPTION_CODE, e);
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new MartContextPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_CONEXT_POPULATION_EXCEPTION_CODE, e);
      } catch (DataAccessException e) {
         exception = e;
         throw new MartContextPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_CONEXT_POPULATION_EXCEPTION_CODE, e);
      } catch (SecurityException e) {
         exception = e;
         throw new MartContextPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_CONEXT_POPULATION_EXCEPTION_CODE, e);
      } catch (PlatformException e) {
         exception = e;
         throw new MartContextPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_CONEXT_POPULATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getMartCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new MartContextPopulationException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return martCreationInputInfo;
   }

   /**
    * This method reads the configuration file and populates the mart configuration context bean.
    * 
    * @return martConfigurationContext
    * @throws MartContextPopulationException
    */
   private MartConfigurationContext populateMartConfigurationContext () throws MartContextPopulationException {
      MartConfigurationContext martConfigurationContext = new MartConfigurationContext();

      boolean randomNumberGeneratorDBApproach = getAnswersCatalogConfigurationService()
               .isRandomNumberGeneratedByDBApproach();

      String scalingFactorConceptName = getAnswersCatalogConfigurationService().getScallingFactorConceptName();

      List<String> stats = getAnswersCatalogConfigurationService().getMartApplicableStats();
      List<StatType> applicableStats = new ArrayList<StatType>();
      for (String stat : stats) {
         applicableStats.add(StatType.getStatType(stat));
      }

      // population table constants
      String populationTableName = getAnswersCatalogConfigurationService().getPopulationTableName();

      String populationTableUniqueIdColumnName = getAnswersCatalogConfigurationService()
               .getPopulationTableUniqueIdColumnName();
      DataType populationTableUniqueIdColumnDataType = DataType.getType(getAnswersCatalogConfigurationService()
               .getPopulationTableUniqueIdColumnDataType());
      Integer populationTableUniqueIdColumnLength = getAnswersCatalogConfigurationService()
               .getPopulationTableUniqueIdColumnLength();
      QueryColumn populationTableUniqueIdColumn = ExecueBeanManagementUtil.prepareQueryColumn(
               populationTableUniqueIdColumnName, populationTableUniqueIdColumnDataType,
               populationTableUniqueIdColumnLength, 0);

      String populationTableSliceNumberColumnName = getAnswersCatalogConfigurationService()
               .getPopulationTableSliceNumberColumnName();
      DataType populationTableSliceNumberColumnDataType = DataType.getType(getAnswersCatalogConfigurationService()
               .getPopulationTableSliceNumberColumnDataType());
      Integer populationTableSliceNumberColumnLength = getAnswersCatalogConfigurationService()
               .getPopulationTableSliceNumberColumnLength();
      QueryColumn populationTableSliceNumberColumn = ExecueBeanManagementUtil.prepareQueryColumn(
               populationTableSliceNumberColumnName, populationTableSliceNumberColumnDataType,
               populationTableSliceNumberColumnLength, 0);

      // fractional temp table constants
      String fractionalTempTableNotation = getAnswersCatalogConfigurationService().getFractionalTempTableNotation();

      String fractionalTempTableUniqueIdColumnName = getAnswersCatalogConfigurationService()
               .getFractionalTempTableUniqueIdColumnName();
      DataType fractionalTempTableUniqueIdColumnDataType = DataType.getType(getAnswersCatalogConfigurationService()
               .getFractionalTempTableUniqueIdColumnDataType());
      Integer fractionalTempTableUniqueIdColumnLength = getAnswersCatalogConfigurationService()
               .getFractionalTempTableUniqueIdColumnLength();
      QueryColumn fractionalTempTableUniqueIdColumn = ExecueBeanManagementUtil.prepareQueryColumn(
               fractionalTempTableUniqueIdColumnName, fractionalTempTableUniqueIdColumnDataType,
               fractionalTempTableUniqueIdColumnLength, 0);

      String fractionalTempTableSliceCountColumnName = getAnswersCatalogConfigurationService()
               .getFractionalTempTableSliceCountColumnName();
      DataType fractionalTempTableSliceCountColumnDataType = DataType.getType(getAnswersCatalogConfigurationService()
               .getFractionalTempTableSliceCountColumnDataType());
      Integer fractionalTempTableSliceCountColumnLength = getAnswersCatalogConfigurationService()
               .getFractionalTempTableSliceCountColumnLength();
      QueryColumn fractionalTempTableSliceCountColumn = ExecueBeanManagementUtil.prepareQueryColumn(
               fractionalTempTableSliceCountColumnName, fractionalTempTableSliceCountColumnDataType,
               fractionalTempTableSliceCountColumnLength, 0);

      // fractional table constants
      String fractionalTableNotation = getAnswersCatalogConfigurationService().getFractionalTableNotation();
      String fractionalTableSfactorColumnName = getAnswersCatalogConfigurationService()
               .getFractionalTableSFactorColumnName();
      DataType fractionalTableSfactorColumnDataType = DataType.getType(getAnswersCatalogConfigurationService()
               .getFractionalTableSFactorColumnDataType());
      Integer fractionalTableSfactorColumnPrecision = getAnswersCatalogConfigurationService()
               .getFractionalTableSFactorColumnPrecision();
      Integer fractionalTableSfactorColumnScale = getAnswersCatalogConfigurationService()
               .getFractionalTableSFactorColumnScale();
      QueryColumn fractionalTableSfactorColumn = ExecueBeanManagementUtil.prepareQueryColumn(
               fractionalTableSfactorColumnName, fractionalTableSfactorColumnDataType,
               fractionalTableSfactorColumnPrecision, fractionalTableSfactorColumnScale);

      // fractional population table constants.
      String fractionalPopulationTempTableName = getAnswersCatalogConfigurationService()
               .getFractionalPopulationTempTableName();
      String fractionalPopulationTableName = getAnswersCatalogConfigurationService().getFractionalPopulationTableName();

      Integer warehouseExtractionThreadPoolSize = getAnswersCatalogConfigurationService()
               .getWarehouseExtractionThreadPoolSize();

      Integer batchDataTransferThreadPoolSize = getAnswersCatalogConfigurationService()
               .getBatchDataTransferThreadPoolSize();

      Integer fractionalTablePopulationThreadPoolSize = getAnswersCatalogConfigurationService()
               .getFractionalTablePopulationThreadPoolSize();

      Integer maxTasksAllowedPerThreadPool = getAnswersCatalogConfigurationService().getMaxTasksAllowedPerThreadPool();
      Integer threadWaitTime = getAnswersCatalogConfigurationService().getThreadWaitTimeForMart();

      boolean cleanTemporaryResources = getAnswersCatalogConfigurationService().isCleanTemporaryResourcesOnMart();

      boolean splitBatchDataTransferToAvoidSubConditions = getAnswersCatalogConfigurationService()
               .isSplitBatchDataTransferToAvoidSubConditions();

      martConfigurationContext.setRandomNumberGeneratorDBApproach(randomNumberGeneratorDBApproach);
      martConfigurationContext.setScalingFactorConceptName(scalingFactorConceptName);
      martConfigurationContext.setApplicableStats(applicableStats);

      martConfigurationContext.setPopulationTableName(populationTableName);
      martConfigurationContext.setPopulationTableIdColumn(populationTableUniqueIdColumn);
      martConfigurationContext.setPopulationTableSliceNumberColumn(populationTableSliceNumberColumn);

      martConfigurationContext.setFractionalTempTableNotation(fractionalTempTableNotation);
      martConfigurationContext.setFractionalTempIdColumn(fractionalTempTableUniqueIdColumn);
      martConfigurationContext.setFractionalTempSliceCountColumn(fractionalTempTableSliceCountColumn);

      martConfigurationContext.setFractionalTableNotation(fractionalTableNotation);
      martConfigurationContext.setFractionalTableSfactorColumn(fractionalTableSfactorColumn);

      martConfigurationContext.setFractionalPopulationTempTableName(fractionalPopulationTempTableName);
      martConfigurationContext.setFractionalPopulationTableName(fractionalPopulationTableName);

      martConfigurationContext.setWarehouseExtractionThreadPoolSize(warehouseExtractionThreadPoolSize);
      martConfigurationContext.setBatchDataTransferThreadPoolSize(batchDataTransferThreadPoolSize);
      martConfigurationContext
               .setSplitBatchDataTransferToAvoidSubConditions(splitBatchDataTransferToAvoidSubConditions);
      martConfigurationContext.setFractionalTablePopulationThreadPoolSize(fractionalTablePopulationThreadPoolSize);
      martConfigurationContext.setCleanTemporaryResources(cleanTemporaryResources);
      martConfigurationContext.setMaxTasksAllowedPerThreadPool(maxTasksAllowedPerThreadPool);
      martConfigurationContext.setThreadWaitTime(threadWaitTime);

      return martConfigurationContext;
   }

   /**
    * This method populates the mart creation populated context using swi information. It populates the information
    * required from plain concept names came in the context
    * 
    * @param martCreationContext
    * @param martConfigurationContext
    * @param answersCatalogConfigurationContext
    * @return martCreationPopulatedContext
    * @throws SecurityException
    * @throws AnswersCatalogException
    * @throws MappingException
    * @throws SDXException
    * @throws KDXException
    * @throws PlatformException
    * @throws SecurityException
    * @throws MartContextPopulationException
    */
   private MartCreationPopulatedContext populateMartCreationContext (MartCreationContext martCreationContext,
            MartConfigurationContext martConfigurationContext,
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext) throws KDXException, SDXException,
            MappingException, AnswersCatalogException, SWIException, SecurityException, PlatformException {
      MartCreationPopulatedContext martCreationPopulatedContext = new MartCreationPopulatedContext();
      // copy the original context
      copyOriginalContext(martCreationContext, martCreationPopulatedContext);
      // populate the context using swi
      populateSWIContextInformation(martCreationContext, martCreationPopulatedContext,
               answersCatalogConfigurationContext, martConfigurationContext);
      return martCreationPopulatedContext;
   }

   private void copyOriginalContext (MartCreationContext martCreationContext,
            MartCreationPopulatedContext martCreationPopulatedContext) {
      martCreationPopulatedContext.setJobRequest(martCreationContext.getJobRequest());
      martCreationPopulatedContext.setApplicationId(martCreationContext.getApplicationId());
      martCreationPopulatedContext.setModelId(martCreationContext.getModelId());
      martCreationPopulatedContext.setUserId(martCreationContext.getUserId());
      martCreationPopulatedContext.setSourceAsset(martCreationContext.getSourceAsset());
      martCreationPopulatedContext.setTargetAsset(martCreationContext.getTargetAsset());
      martCreationPopulatedContext.setPopulation(martCreationContext.getPopulation());
      martCreationPopulatedContext.setDistributions(martCreationContext.getDistributions());
      martCreationPopulatedContext.setProminentMeasures(martCreationContext.getProminentMeasures());
      martCreationPopulatedContext.setProminentDimensions(martCreationContext.getProminentDimensions());
      martCreationPopulatedContext.setTargetDataSourceSameAsSourceDataSource(martCreationContext
               .isTargetDataSourceSameAsSourceDataSource());
   }

   /**
    * This method populates the context using swi
    * 
    * @param martCreationContext
    * @param martCreationPopulatedContext
    * @param answersCatalogConfigurationContext
    * @param martConfigurationContext
    * @throws AnswersCatalogException
    * @throws SecurityException
    * @throws SWIException
    * @throws PlatformException
    * @throws MartContextPopulationException
    */
   private void populateSWIContextInformation (MartCreationContext martCreationContext,
            MartCreationPopulatedContext martCreationPopulatedContext,
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            MartConfigurationContext martConfigurationContext) throws AnswersCatalogException, SecurityException,
            SWIException, PlatformException {
      // prepare source asset mapping info for all dimensions and using source asset.
      SourceAssetMappingInfo sourceAssetMappingInfo = populateSourceAssetMappingInfo(martCreationContext,
               martCreationContext.getSourceAsset(), null);
      // build the map for asset mapping info for cube if cube can answer that dimension.
      Map<String, SourceAssetMappingInfo> prominentDimensionCubeMap = getProminentDimensionCubeMap(martCreationContext,
               answersCatalogConfigurationContext, martConfigurationContext.getApplicableStats(),
               sourceAssetMappingInfo);
      martCreationPopulatedContext.setSourceAssetMappingInfo(sourceAssetMappingInfo);
      martCreationPopulatedContext.setProminentDimensionCubeMap(prominentDimensionCubeMap);
      martCreationPopulatedContext.setApplication(getApplicationRetrievalService().getApplicationById(
               martCreationContext.getApplicationId()));
      martCreationPopulatedContext.setModel(getKdxRetrievalService().getModelById(martCreationContext.getModelId()));
      martCreationPopulatedContext.setUser(getUserManagementService().getUserById(martCreationContext.getUserId()));
   }

   /**
    * This method populates the mapping level information for plain concept names. Whole of the information will be
    * populates except for all dimensions if no dimension is specified
    * 
    * @param martCreationContext
    * @param asset
    * @param dimensionName
    * @return sourceAssetMappingInfo
    * @throws AnswersCatalogException
    * @throws PlatformException
    */
   private SourceAssetMappingInfo populateSourceAssetMappingInfo (MartCreationContext martCreationContext, Asset asset,
            String dimensionName) throws AnswersCatalogException, PlatformException {
      Long modelId = martCreationContext.getModelId();
      Long assetId = asset.getId();
      ConceptColumnMapping populatedPopulation = getMartCreationServiceHelper().buildConceptColumnMapping(
               martCreationContext.getPopulation(), modelId, assetId);
      List<ConceptColumnMapping> populatedDistributions = getMartCreationServiceHelper().buildConceptColumnMapping(
               martCreationContext.getDistributions(), modelId, assetId);
      List<String> dimensionsToBeConsidered = new ArrayList<String>();
      if (dimensionName == null) {
         dimensionsToBeConsidered.addAll(martCreationContext.getProminentDimensions());
      } else {
         dimensionsToBeConsidered.add(dimensionName);
      }
      List<ConceptColumnMapping> populatedProminentDimensions = getMartCreationServiceHelper()
               .buildConceptColumnMapping(dimensionsToBeConsidered, modelId, assetId);
      List<ConceptColumnMapping> populatedProminentMeasures = getMartCreationServiceHelper().buildConceptColumnMapping(
               martCreationContext.getProminentMeasures(), modelId, assetId);
      // for each measure, check if sampling strategy is defined on concept else we need to make sure samping strategy
      // is defined
      for (ConceptColumnMapping measureConceptColumnMapping : populatedProminentMeasures) {
         Concept concept = measureConceptColumnMapping.getConcept();
         if (concept.getDataSamplingStrategy() == null) {
            concept.setDataSamplingStrategy(getSamplingStrategyIdentificationService().identifyCorrectSampingStrategy(
                     modelId, concept.getId(), assetId));
         }
      }
      return AnswersCatalogUtil.prepareSourceAssetMappingInfo(asset, populatedPopulation, populatedDistributions,
               populatedProminentMeasures, populatedProminentDimensions);
   }

   /**
    * This method populates the asset mapping information for each dimension at a time from cubes which has that
    * dimension so that we can build fractional dataset from cube rather than going to source.
    * 
    * @param martCreationContext
    * @param answersCatalogConfigurationContext
    * @param stats
    * @param sourceAssetMapping
    * @return prominentDimensionCubeMap
    * @throws AnswersCatalogException
    * @throws SDXException
    * @throws KDXException
    * @throws MappingException
    * @throws PlatformException
    */
   private Map<String, SourceAssetMappingInfo> getProminentDimensionCubeMap (MartCreationContext martCreationContext,
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext, List<StatType> stats,
            SourceAssetMappingInfo sourceAssetMapping) throws AnswersCatalogException, SDXException, KDXException,
            MappingException, PlatformException {
      Map<String, SourceAssetMappingInfo> prominentDimensionCubeMap = new HashMap<String, SourceAssetMappingInfo>();
      for (ConceptColumnMapping conceptColumnMapping : sourceAssetMapping.getPopulatedProminentDimensions()) {
         Long conceptBedId = conceptColumnMapping.getBusinessEntityDefinition().getId();
         String conceptName = conceptColumnMapping.getConcept().getName();
         // get the assets of type cube which has this dimension concept mapped.
         List<Asset> assets = getMartCreationServiceHelper().getCubeAssetsHavingMappedConceptBedId(conceptBedId,
                  martCreationContext.getSourceAsset().getId());
         if (!CollectionUtils.isEmpty(assets)) {
            // pick the least id asset to maintain consistency
            Asset asset = pickLeastIdAsset(assets);
            // check this cube asset has required stats defined or not.
            BusinessEntityDefinition statisticsConceptBED = getBaseKDXRetrievalService().getRealizedTypeBEDByName(
                     answersCatalogConfigurationContext.getStatisticsConceptName());
            List<Mapping> mappings = getMappingRetrievalService().getAssetEntities(statisticsConceptBED, asset.getId());
            if (ExecueCoreUtil.isCollectionNotEmpty(mappings)) {
               boolean allStatsExist = false;
               Mapping primaryMapping = mappings.get(0);
               List<Membr> columnMembers = getSdxRetrievalService().getColumnMembers(
                        primaryMapping.getAssetEntityDefinition().getColum());
               for (StatType statType : stats) {
                  if (!isStatExists(columnMembers, statType)) {
                     allStatsExist = false;
                     break;
                  }
               }
               // if all the required stats exists, we can populate cube asset mapping information for this dimension.
               if (allStatsExist) {
                  if (logger.isDebugEnabled()) {
                     logger.debug("For Concept " + conceptName + " , Cube asset picked " + asset.getName());
                  }
                  asset.setDataSource(getSdxRetrievalService().getDataSourceByAssetId(asset.getId()));
                  SourceAssetMappingInfo sourceAssetMappingInfo = populateSourceAssetMappingInfo(martCreationContext,
                           asset, conceptName);
                  prominentDimensionCubeMap.put(conceptName, sourceAssetMappingInfo);
               }
            }
         }
      }
      return prominentDimensionCubeMap;
   }

   private boolean isStatExists (List<Membr> members, StatType statType) {
      boolean statExists = false;
      for (Membr membr : members) {
         if (membr.getLookupValue().equalsIgnoreCase(statType.getValue())) {
            statExists = true;
            break;
         }
      }
      return statExists;
   }

   /**
    * Pick the lease Id asset to maintain consistency as more than one cube can have the dimension to be answered
    * 
    * @param assets
    * @return leastIdAsset
    */
   private Asset pickLeastIdAsset (List<Asset> assets) {
      Asset leastIdAsset = assets.get(0);
      for (Asset asset : assets) {
         if (asset.getId() < leastIdAsset.getId()) {
            leastIdAsset = asset;
         }
      }
      return leastIdAsset;
   }

   /**
    * This method populates the slicing algorithm static input which will be used for calculating total number of slices
    * 
    * @return slicingAlgorithmStaticInput
    */
   private SlicingAlgorithmStaticInput populateSlicingAlgorithmStaticInput () {
      SlicingAlgorithmStaticInput slicingAlgorithmStaticInput = new SlicingAlgorithmStaticInput();
      List<String> slicingEligiblityCriteriaRecords = getAnswersCatalogConfigurationService()
               .getSlicingAlgorithmEligibilityCriteriaRecords();
      List<String> slicingEligiblityCriteriaPercentages = getAnswersCatalogConfigurationService()
               .getSlicingAlgorithmEligibilityCriteriaPercentages();
      Integer minSlices = getAnswersCatalogConfigurationService().getSlicingAlgorithmMinSlices();
      Integer maxSlices = getAnswersCatalogConfigurationService().getSlicingAlgorithmMaxSlices();
      Integer avgSlices = getAnswersCatalogConfigurationService().getSlicingAlgorithmAvgSlices();
      slicingAlgorithmStaticInput.setSlicingEligiblityCriteriaRecords(slicingEligiblityCriteriaRecords);
      slicingAlgorithmStaticInput.setSlicingEligiblityCriteriaPercentages(slicingEligiblityCriteriaPercentages);
      slicingAlgorithmStaticInput.setMinSlices(minSlices);
      slicingAlgorithmStaticInput.setMaxSlices(maxSlices);
      slicingAlgorithmStaticInput.setAvgSlices(avgSlices);
      return slicingAlgorithmStaticInput;
   }

   private SamplingAlgorithmStaticInput populateSamplingAlgorithmStaticInput () {
      SamplingAlgorithmStaticInput samplingAlgorithmStaticInput = new SamplingAlgorithmStaticInput();
      samplingAlgorithmStaticInput.setConfidenceLevelPercentage(getAnswersCatalogConfigurationService()
               .getSamplingAlgoConfidenceLevelPercentage());
      samplingAlgorithmStaticInput
               .setConfidenceLevelValue(samplingAlgorithmStaticInput.getConfidenceLevelPercentage() / 100);
      samplingAlgorithmStaticInput.setErrorRatePercentage(getAnswersCatalogConfigurationService()
               .getSamplingAlgoErrorRatePercentage());
      samplingAlgorithmStaticInput.setErrorRateValue(samplingAlgorithmStaticInput.getErrorRatePercentage() / 100);
      samplingAlgorithmStaticInput.setMaxSamplePercentageOfPopulationAllowed(getAnswersCatalogConfigurationService()
               .getSamplingAlgoMaxSamplePercentageOfPopulationAllowed());
      samplingAlgorithmStaticInput.setMinSamplePercentageOfPopulation(getAnswersCatalogConfigurationService()
               .getSamplingAlgoMinSamplePercentageOfPopulation());
      samplingAlgorithmStaticInput.setZValue(SampleSizeFormulaUtil.calculateZValue(samplingAlgorithmStaticInput
               .getConfidenceLevelValue()));
      samplingAlgorithmStaticInput
               .setSubGroupingMinPopulationPercentageRequired(getAnswersCatalogConfigurationService()
                        .getSamplingAlgoSubGroupingMinPopulationPercentageRequired());
      samplingAlgorithmStaticInput
               .setSubGroupingMaxCoefficientOfVarianceAllowed(getAnswersCatalogConfigurationService()
                        .getSamplingAlgoSubGroupingMaxCoefficientOfVarianceAllowed());
      samplingAlgorithmStaticInput
               .setSubGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups(getAnswersCatalogConfigurationService()
                        .getSamplingAlgoSubGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups());
      return samplingAlgorithmStaticInput;
   }

   /**
    * This method reads the configuration and populates the basic sampling algorithm input information.
    * 
    * @param numberOfSlices
    * @return samplingAlgorithmStaticInput
    * @throws MartContextPopulationException
    */
   private BasicSamplingAlgorithmStaticInput populateBasicSamplingAlgorithmStaticInput (Integer numberOfSlices) {
      BasicSamplingAlgorithmStaticInput samplingAlgorithmStaticInput = new BasicSamplingAlgorithmStaticInput();
      samplingAlgorithmStaticInput.setNumberOfSlices(numberOfSlices);
      samplingAlgorithmStaticInput.setConfidenceLevel(getAnswersCatalogConfigurationService()
               .getBasicSamplingConfidenceLevel() / 100);
      samplingAlgorithmStaticInput
               .setErrorRate(getAnswersCatalogConfigurationService().getBasicSamplingErrorRate() / 100);
      samplingAlgorithmStaticInput.setFunctionUseAlone(getAnswersCatalogConfigurationService()
               .useBasicSamplingFunctionAlone());
      samplingAlgorithmStaticInput.setSlicePercentage((double) 1 / (double) numberOfSlices);
      samplingAlgorithmStaticInput.setMinPopulation(getAnswersCatalogConfigurationService()
               .getBasicSamplingMinimumPopulationSize());
      Double defaultNumberOfSlicesFactor = getAnswersCatalogConfigurationService()
               .getBasicSamplingDefaultNumberOfSlicesFactor();
      samplingAlgorithmStaticInput.setDefaultNumberOfSlices((long) Math.ceil(defaultNumberOfSlicesFactor
               * (double) numberOfSlices));
      samplingAlgorithmStaticInput.setFloorSettingRequired(getAnswersCatalogConfigurationService()
               .isBasicSamplingFloorSettingRequired());
      samplingAlgorithmStaticInput.setFloorMultiplicationFactor(getAnswersCatalogConfigurationService()
               .getBasicSamplingMultiplicationFactorForFloor());
      samplingAlgorithmStaticInput.setMaxSamplePercentageOfPopulation(getAnswersCatalogConfigurationService()
               .getBasicSamplingMaxSamplePercentageOfPopulation());
      samplingAlgorithmStaticInput.setLdConstantOne(getAnswersCatalogConfigurationService()
               .getBasicSamplingLdConstantOne());
      samplingAlgorithmStaticInput.setLdConstantTwo(getAnswersCatalogConfigurationService()
               .getBasicSamplingLdConstantTwo());
      Double pLow = getAnswersCatalogConfigurationService().getBasicSamplingPLowValue();
      samplingAlgorithmStaticInput.setPLow(pLow);
      samplingAlgorithmStaticInput.setPHigh(1 - pLow);
      return samplingAlgorithmStaticInput;
   }

   /**
    * This method reads the configuration and populates the batch count algorithm input information. It will be used to
    * calculate the batch size.
    * 
    * @param populationMaxDataLength
    * @return batchCountAlgorithmStaticInput
    */
   private BatchCountAlgorithmStaticInput populateBatchCountAlgorithmStaticInput (Integer populationMaxDataLength) {
      BatchCountAlgorithmStaticInput batchCountAlgorithmStaticInput = new BatchCountAlgorithmStaticInput();
      batchCountAlgorithmStaticInput.setPopulationMaxDataLength(populationMaxDataLength);
      batchCountAlgorithmStaticInput.setSqlQueryMaxSize(getAnswersCatalogConfigurationService()
               .getBatchCountAlgoSQLQueryMaxSize());
      batchCountAlgorithmStaticInput.setEmptyWhereConditionSize(getAnswersCatalogConfigurationService()
               .getBatchCountAlgoSQLQueryEmptyWhereConditionSize());
      batchCountAlgorithmStaticInput.setWhereConditionRecordBufferLength(getAnswersCatalogConfigurationService()
               .getBatchCountAlgoSQLQueryEmptyWhereConditionRecordBufferLength());
      batchCountAlgorithmStaticInput.setMaxAllowedExpressionsInCondition(getAnswersCatalogConfigurationService()
               .getBatchCountAlgoSQLMaxAllowedExpressionsPerCondition());
      return batchCountAlgorithmStaticInput;
   }

   /**
    * This method populates the input parameters like total record count, max data length of population etc which are
    * needed for mart creation.
    * 
    * @param martCreationPopulatedContext
    * @param slicingAlgorithmStaticInput
    * @return martInputParametersContext
    * @throws DataAccessException
    * @throws AnswersCatalogException
    * @throws MartContextPopulationException
    */
   private MartInputParametersContext populateMartInputParametersContext (
            MartCreationPopulatedContext martCreationPopulatedContext,
            SlicingAlgorithmStaticInput slicingAlgorithmStaticInput) throws DataAccessException,
            AnswersCatalogException {
      MartInputParametersContext martInputParametersContext = null;
      DataSource sourceDataSource = martCreationPopulatedContext.getSourceAsset().getDataSource();
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(martCreationPopulatedContext
               .getSourceAssetMappingInfo().getPopulatedPopulation().getTabl());
      QueryColumn populationQueryColumn = martCreationPopulatedContext.getSourceAssetMappingInfo()
               .getPopulatedPopulation().getQueryColumn();
      // get the count of total number of population records.
      Long totalPopulationRecordCount = getAnswersCatalogDataAccessService().getStatBasedColumnValue(sourceDataSource,
               queryTable, populationQueryColumn, StatType.COUNT);
      // get the length of the max data point in the population.
      Integer populationMaxDataLength = getAnswersCatalogDataAccessService().fetchMaxDataLength(sourceDataSource,
               queryTable, populationQueryColumn);
      // calculate the number of slices based on number of records in population.
      Integer numberOfSlices = getMartTotalSlicesCalculatorService().calculateTotalSlices(slicingAlgorithmStaticInput,
               totalPopulationRecordCount);
      martInputParametersContext = new MartInputParametersContext();
      martInputParametersContext.setTotalPopulationRecordCount(totalPopulationRecordCount);
      martInputParametersContext.setPopulationMaxDataLength(populationMaxDataLength);
      martInputParametersContext.setNumberOfSlices(numberOfSlices);
      return martInputParametersContext;
   }

   public IMartTotalSlicesCalculatorService getMartTotalSlicesCalculatorService () {
      return martTotalSlicesCalculatorService;
   }

   public void setMartTotalSlicesCalculatorService (IMartTotalSlicesCalculatorService martTotalSlicesCalculatorService) {
      this.martTotalSlicesCalculatorService = martTotalSlicesCalculatorService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IAnswersCatalogDataAccessService getAnswersCatalogDataAccessService () {
      return answersCatalogDataAccessService;
   }

   public void setAnswersCatalogDataAccessService (IAnswersCatalogDataAccessService answersCatalogDataAccessService) {
      this.answersCatalogDataAccessService = answersCatalogDataAccessService;
   }

   public MartCreationServiceHelper getMartCreationServiceHelper () {
      return martCreationServiceHelper;
   }

   public void setMartCreationServiceHelper (MartCreationServiceHelper martCreationServiceHelper) {
      this.martCreationServiceHelper = martCreationServiceHelper;
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   public IUserManagementService getUserManagementService () {
      return userManagementService;
   }

   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
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

   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

   public ISamplingStrategyIdentificationService getSamplingStrategyIdentificationService () {
      return samplingStrategyIdentificationService;
   }

   public void setSamplingStrategyIdentificationService (
            ISamplingStrategyIdentificationService samplingStrategyIdentificationService) {
      this.samplingStrategyIdentificationService = samplingStrategyIdentificationService;
   }

}
