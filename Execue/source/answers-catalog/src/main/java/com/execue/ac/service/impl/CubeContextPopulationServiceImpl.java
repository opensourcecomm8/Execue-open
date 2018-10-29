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
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.ac.bean.AnswersCatalogConfigurationContext;
import com.execue.ac.bean.ConceptColumnMapping;
import com.execue.ac.bean.CubeConfigurationContext;
import com.execue.ac.bean.CubeCreationInputInfo;
import com.execue.ac.bean.CubeCreationPopulatedContext;
import com.execue.ac.bean.CubeUpdationContext;
import com.execue.ac.bean.CubeUpdationDimensionInfo;
import com.execue.ac.bean.CubeUpdationInputInfo;
import com.execue.ac.bean.CubeUpdationPopulatedContext;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.CubeContextPopulationException;
import com.execue.ac.helper.CubeCreationServiceHelper;
import com.execue.ac.service.IAnswersCatalogConstants;
import com.execue.ac.service.ICubeContextPopulationService;
import com.execue.ac.service.ICubeOperationalConstants;
import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.ac.CubeRangeDimensionInfo;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.security.exception.SecurityException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IUserManagementService;

/**
 * This service used to populate the cube creation context
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class CubeContextPopulationServiceImpl extends AnswersCatalogContextPopulationServiceImpl implements
         ICubeContextPopulationService, IAnswersCatalogConstants, ICubeOperationalConstants {

   private IKDXRetrievalService         kdxRetrievalService;
   private IApplicationRetrievalService applicationRetrievalService;
   private IUserManagementService       userManagementService;
   private CubeCreationServiceHelper    cubeCreationServiceHelper;
   private static final Logger          logger = Logger.getLogger(CubeContextPopulationServiceImpl.class);

   public CubeCreationInputInfo populateCubeCreationInputInfo (CubeCreationContext cubeCreationContext)
            throws CubeContextPopulationException {
      CubeCreationInputInfo cubeCreationInputInfo = null;
      JobRequest jobRequest = cubeCreationContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  POPULATE_GENERIC_ANSWERS_CATALOG_CONFIGURATION_CONTEXT);
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the Answers Catalog Context from configuration");
         }
         AnswersCatalogConfigurationContext answersCatalogConfigurationContext = populateAnswersCatalogConfigurationContext();
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  POPULATE_SPECIFIC_CUBE_CONFIGURATION_CONTEXT);
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the cube Context from configuration");
         }
         CubeConfigurationContext cubeConfigurationContext = populateCubeConfigurationContext(answersCatalogConfigurationContext);
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  POPULATE_CUBE_CREATION_CONTEXT_FROM_SWI);
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the cube Context from swi");
         }
         CubeCreationPopulatedContext cubeCreationPopulatedContext = populateCubeCreationPopulatedContext(cubeCreationContext);
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         cubeCreationInputInfo = new CubeCreationInputInfo();
         cubeCreationInputInfo.setAnswersCatalogConfigurationContext(answersCatalogConfigurationContext);
         cubeCreationInputInfo.setCubeConfigurationContext(cubeConfigurationContext);
         cubeCreationInputInfo.setCubeCreationPopulatedContext(cubeCreationPopulatedContext);
         cubeCreationInputInfo.setCubeCreationContext(cubeCreationContext);
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new CubeContextPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_CONEXT_POPULATION_EXCEPTION_CODE, e);
      } catch (SWIException e) {
         exception = e;
         throw new CubeContextPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_CONEXT_POPULATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubeContextPopulationException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return cubeCreationInputInfo;
   }

   @Override
   public CubeUpdationInputInfo populateCubeUpdationInputInfo (CubeUpdationContext cubeUpdationContext)
            throws CubeContextPopulationException {
      CubeUpdationInputInfo cubeUpdationInputInfo = null;
      JobRequest jobRequest = cubeUpdationContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  POPULATE_GENERIC_ANSWERS_CATALOG_CONFIGURATION_CONTEXT_FOR_CUBE_UPDATE);
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the Answers Catalog Context from configuration");
         }
         AnswersCatalogConfigurationContext answersCatalogConfigurationContext = populateAnswersCatalogConfigurationContext();
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  POPULATE_SPECIFIC_CUBE_CONFIGURATION_CONTEXT_FOR_CUBE_UPDATE);
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the cube Context from configuration");
         }
         CubeConfigurationContext cubeConfigurationContext = populateCubeConfigurationContext(answersCatalogConfigurationContext);
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  POPULATE_CUBE_UPDATION_CONTEXT_FROM_SWI);
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the cube Updation context from swi");
         }
         CubeUpdationPopulatedContext cubeUpdationPopulatedContext = populateCubeUpdationPopulatedContext(
                  cubeUpdationContext, cubeConfigurationContext.getCubeAllValueRepresentation());
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
         cubeUpdationInputInfo = new CubeUpdationInputInfo();
         cubeUpdationInputInfo.setAnswersCatalogConfigurationContext(answersCatalogConfigurationContext);
         cubeUpdationInputInfo.setCubeConfigurationContext(cubeConfigurationContext);
         cubeUpdationInputInfo.setCubeUpdationContext(cubeUpdationContext);
         cubeUpdationInputInfo.setCubeUpdationPopulatedContext(cubeUpdationPopulatedContext);
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new CubeContextPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_CONEXT_POPULATION_EXCEPTION_CODE, e);

      } catch (SWIException e) {
         exception = e;
         throw new CubeContextPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_CONEXT_POPULATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubeContextPopulationException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return cubeUpdationInputInfo;
   }

   /**
    * This method reads the configuration and populates the cubeconfiguration context bean
    * 
    * @param answersCatalogConfigurationContext
    * @return CubeConfigurationContext
    */
   private CubeConfigurationContext populateCubeConfigurationContext (
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext) {
      CubeConfigurationContext cubeConfigurationContext = new CubeConfigurationContext();
      cubeConfigurationContext.setCubeAllValueRepresentation(getCoreConfigurationService().getCubeAllValue());
      cubeConfigurationContext.setDimensionDataType(DataType.getType(getAnswersCatalogConfigurationService()
               .getDimensionColumnDataType()));
      cubeConfigurationContext.setMinDimensionPrecision(getAnswersCatalogConfigurationService()
               .getDimensionColumnMinimunPrecision());
      cubeConfigurationContext.setFrequencyColumnPrefix(getAnswersCatalogConfigurationService()
               .getFrequencyColumnPrefix());

      cubeConfigurationContext.setSimpleLookupColumnPrefix(getAnswersCatalogConfigurationService()
               .getSimpleLookupDimensionPrefix());
      cubeConfigurationContext.setRangeLookupColumnPrefix(getAnswersCatalogConfigurationService()
               .getRangeLookupDimensionPrefix());

      String rangeLookupLowerLimitColumnName = getAnswersCatalogConfigurationService()
               .getRangeLookupLowerLimitColumnName();
      String rangeLookupUpperLimitColumnName = getAnswersCatalogConfigurationService()
               .getRangeLookupUpperLimitColumnName();

      DataType measureColumnDataType = answersCatalogConfigurationContext.getMeasureColumnDataType();
      Integer measurePrecisionValue = answersCatalogConfigurationContext.getMeasurePrecisionValue();
      Integer measureScaleValue = answersCatalogConfigurationContext.getMeasureScaleValue();

      QueryColumn rangeLookupLowerLimitColumn = ExecueBeanManagementUtil.prepareQueryColumn(
               rangeLookupLowerLimitColumnName, measureColumnDataType, measurePrecisionValue, measureScaleValue);
      QueryColumn rangeLookupUpperLimitColumn = ExecueBeanManagementUtil.prepareQueryColumn(
               rangeLookupUpperLimitColumnName, measureColumnDataType, measurePrecisionValue, measureScaleValue);
      cubeConfigurationContext.setRangeLookupLowerLimitColumn(rangeLookupLowerLimitColumn);
      cubeConfigurationContext.setRangeLookupUpperLimitColumn(rangeLookupUpperLimitColumn);

      cubeConfigurationContext.setDescriptionColumnSuffix(getAnswersCatalogConfigurationService()
               .getDescriptionColumnPrefix());
      cubeConfigurationContext.setDescriptionColumnDataType(DataType.getType(getAnswersCatalogConfigurationService()
               .getDescriptionColumnDataType()));
      cubeConfigurationContext.setDescriptionColumnPrecision(getAnswersCatalogConfigurationService()
               .getDescriptionColumnPrecision());

      String statColumnName = getCoreConfigurationService().getStatisticsColumnName();
      QueryColumn statQueryColumn = ExecueBeanManagementUtil.prepareQueryColumn(statColumnName,
               cubeConfigurationContext.getDimensionDataType(), getAnswersCatalogConfigurationService()
                        .getStatColumnPrecision(), 0);
      cubeConfigurationContext.setStatColumn(statQueryColumn);
      cubeConfigurationContext.setCubeFactTablePrefix(getAnswersCatalogConfigurationService().getCubeFactTablePrefix());
      cubeConfigurationContext.setCubePreFactTableSuffix(getAnswersCatalogConfigurationService()
               .getCubePreFactTableSuffix());

      String queryIdColumnName = getAnswersCatalogConfigurationService().getQueryIdColumnName();
      DataType queryIdColumnDataType = DataType.getType(getAnswersCatalogConfigurationService()
               .getQueryIdColumnDataType());
      Integer queryIdColumnPrecision = getAnswersCatalogConfigurationService().getQueryIdColumnPrecision();

      QueryColumn queryIdQueryColumn = ExecueBeanManagementUtil.prepareQueryColumn(queryIdColumnName,
               queryIdColumnDataType, queryIdColumnPrecision, 0);
      cubeConfigurationContext.setQueryIdColumn(queryIdQueryColumn);

      List<String> stats = getAnswersCatalogConfigurationService().getDefaultStatNames();
      List<StatType> applicableStats = new ArrayList<StatType>();
      for (String stat : stats) {
         applicableStats.add(StatType.getStatType(stat));
      }
      cubeConfigurationContext.setApplicableStats(applicableStats);
      cubeConfigurationContext.setDataTransferQueriesExecutionThreadPoolSize(getAnswersCatalogConfigurationService()
               .getDataTransferQueriesExecutionThreadPoolSize());
      cubeConfigurationContext.setCleanTemporaryResources(getAnswersCatalogConfigurationService()
               .isCleanTemporaryResourcesOnCube());
      cubeConfigurationContext.setThreadWaitTime(getAnswersCatalogConfigurationService().getThreadWaitTimeForCube());
      return cubeConfigurationContext;
   }

   /**
    * This method uses swi to populate the cube updation context
    * 
    * @param cubeUpdationContext
    * @param memberValue
    * @return CubeUpdationPopulatedContext
    * @throws AnswersCatalogException
    * @throws SWIException
    * @throws SecurityException
    */
   private CubeUpdationPopulatedContext populateCubeUpdationPopulatedContext (CubeUpdationContext cubeUpdationContext,
            String memberValue) throws AnswersCatalogException, SWIException {
      CubeUpdationPopulatedContext cubeUpdationPopulatedContext = new CubeUpdationPopulatedContext();
      CubeCreationContext cubeCreationContext = cubeUpdationContext;
      CubeCreationPopulatedContext cubeCreationPopulatedContext = populateCubeCreationPopulatedContext(cubeCreationContext);
      copyOriginalContext(cubeUpdationContext, cubeUpdationPopulatedContext);
      Long modelId = cubeUpdationContext.getModelId();
      Long existingAssetId = cubeUpdationContext.getExistingAssetId();
      List<String> dimensions = new ArrayList<String>();
      for (CubeUpdationDimensionInfo cubeUpdationDimensionInfo : cubeUpdationContext.getCubeUpdationDimensionInfoList()) {
         dimensions.add(cubeUpdationDimensionInfo.getDimensionName());
      }
      List<ConceptColumnMapping> updatedDimensionsExistingCubeFactTableMappings = getCubeCreationServiceHelper()
               .buildFactTableConceptColumnNonPrimaryMapping(dimensions, modelId, existingAssetId);
      List<ConceptColumnMapping> updatedDimensionsExistingCubeLookupTableMappings = getCubeCreationServiceHelper()
               .buildConceptColumnMapping(dimensions, modelId, existingAssetId);
      cubeUpdationPopulatedContext.setExistingCubeCreationPopulatedContext(cubeCreationPopulatedContext);
      cubeUpdationPopulatedContext
               .setUpdatedDimensionsExistingCubeFactTableMappings(updatedDimensionsExistingCubeFactTableMappings);
      cubeUpdationPopulatedContext
               .setUpdatedDimensionsExistingCubeLookupTableMappings(updatedDimensionsExistingCubeLookupTableMappings);
      return cubeUpdationPopulatedContext;
   }

   /**
    * This method uses swi to populate the cube creation context
    * 
    * @param cubeCreationContext
    * @return CubeCreationPopulatedContext
    * @throws AnswersCatalogException
    * @throws KDXException
    * @throws SecurityException
    */
   private CubeCreationPopulatedContext populateCubeCreationPopulatedContext (CubeCreationContext cubeCreationContext)
            throws AnswersCatalogException, KDXException, SWIException {
      Long modelId = cubeCreationContext.getModelId();
      Long sourceAssetId = cubeCreationContext.getSourceAsset().getId();
      CubeCreationPopulatedContext cubeCreationPopulatedContext = new CubeCreationPopulatedContext();
      copyOriginalContext(cubeCreationContext, cubeCreationPopulatedContext);
      cubeCreationPopulatedContext.setPopulatedFrequencyMeasures(getCubeCreationServiceHelper()
               .buildConceptColumnMapping(cubeCreationContext.getFrequencyMeasures(), modelId, sourceAssetId));
      cubeCreationPopulatedContext.setPopulatedSimpleLookupDimensions(getCubeCreationServiceHelper()
               .buildConceptColumnMapping(cubeCreationContext.getSimpleLookupDimensions(), modelId, sourceAssetId));
      cubeCreationPopulatedContext.setPopulatedMeasures(getCubeCreationServiceHelper().buildConceptColumnMapping(
               cubeCreationContext.getMeasures(), modelId, sourceAssetId));
      List<ConceptColumnMapping> rangeLookupDimensions = new ArrayList<ConceptColumnMapping>(1);
      for (CubeRangeDimensionInfo cubeRangeDimensionInfo : cubeCreationContext.getRangeLookupDimensions()) {
         ConceptColumnMapping rangeLookupDimension = getCubeCreationServiceHelper().buildConceptColumnMapping(
                  cubeRangeDimensionInfo.getDimensionName(), modelId, sourceAssetId);
         rangeLookupDimension.setRangeDefinition(cubeRangeDimensionInfo.getRange());
         rangeLookupDimensions.add(rangeLookupDimension);

      }
      cubeCreationPopulatedContext.setPopulatedRangeLookupDimensions(rangeLookupDimensions);
      cubeCreationPopulatedContext.setModel(getKdxRetrievalService().getModelById(cubeCreationContext.getModelId()));
      cubeCreationPopulatedContext.setApplication(getApplicationRetrievalService().getApplicationById(
               cubeCreationContext.getApplicationId()));
      cubeCreationPopulatedContext.setUser(getUserManagementService().getUserById(cubeCreationContext.getUserId()));

      return cubeCreationPopulatedContext;
   }

   private void copyOriginalContext (CubeUpdationContext cubeUpdationContext,
            CubeUpdationPopulatedContext cubeUpdationPopulatedContext) {
      cubeUpdationPopulatedContext.setJobRequest(cubeUpdationContext.getJobRequest());
      cubeUpdationPopulatedContext.setApplicationId(cubeUpdationContext.getApplicationId());
      cubeUpdationPopulatedContext.setModelId(cubeUpdationContext.getModelId());
      cubeUpdationPopulatedContext.setUserId(cubeUpdationContext.getUserId());
      cubeUpdationPopulatedContext.setSourceAsset(cubeUpdationContext.getSourceAsset());
      cubeUpdationPopulatedContext.setTargetAsset(cubeUpdationContext.getTargetAsset());
      cubeUpdationPopulatedContext.setSimpleLookupDimensions(cubeUpdationContext.getSimpleLookupDimensions());
      cubeUpdationPopulatedContext.setRangeLookupDimensions(cubeUpdationContext.getRangeLookupDimensions());
      cubeUpdationPopulatedContext.setFrequencyMeasures(cubeUpdationContext.getFrequencyMeasures());
      cubeUpdationPopulatedContext.setMeasures(cubeUpdationContext.getMeasures());
      cubeUpdationPopulatedContext.setTargetDataSourceSameAsSourceDataSource(cubeUpdationContext
               .isTargetDataSourceSameAsSourceDataSource());
      cubeUpdationPopulatedContext.setCubeUpdationDimensionInfoList(cubeUpdationContext
               .getCubeUpdationDimensionInfoList());
      cubeUpdationPopulatedContext.setExistingAssetId(cubeUpdationContext.getExistingAssetId());
   }

   private void copyOriginalContext (CubeCreationContext cubeCreationContext,
            CubeCreationPopulatedContext cubeCreationPopulatedContext) {
      cubeCreationPopulatedContext.setJobRequest(cubeCreationContext.getJobRequest());
      cubeCreationPopulatedContext.setApplicationId(cubeCreationContext.getApplicationId());
      cubeCreationPopulatedContext.setModelId(cubeCreationContext.getModelId());
      cubeCreationPopulatedContext.setUserId(cubeCreationContext.getUserId());
      cubeCreationPopulatedContext.setSourceAsset(cubeCreationContext.getSourceAsset());
      cubeCreationPopulatedContext.setTargetAsset(cubeCreationContext.getTargetAsset());
      cubeCreationPopulatedContext.setSimpleLookupDimensions(cubeCreationContext.getSimpleLookupDimensions());
      cubeCreationPopulatedContext.setRangeLookupDimensions(cubeCreationContext.getRangeLookupDimensions());
      cubeCreationPopulatedContext.setFrequencyMeasures(cubeCreationContext.getFrequencyMeasures());
      cubeCreationPopulatedContext.setMeasures(cubeCreationContext.getMeasures());
      cubeCreationPopulatedContext.setTargetDataSourceSameAsSourceDataSource(cubeCreationContext
               .isTargetDataSourceSameAsSourceDataSource());
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService
    *           the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the userManagementService
    */
   public IUserManagementService getUserManagementService () {
      return userManagementService;
   }

   /**
    * @param userManagementService
    *           the userManagementService to set
    */
   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   /**
    * @return the cubeCreationServiceHelper
    */
   public CubeCreationServiceHelper getCubeCreationServiceHelper () {
      return cubeCreationServiceHelper;
   }

   /**
    * @param cubeCreationServiceHelper
    *           the cubeCreationServiceHelper to set
    */
   public void setCubeCreationServiceHelper (CubeCreationServiceHelper cubeCreationServiceHelper) {
      this.cubeCreationServiceHelper = cubeCreationServiceHelper;
   }

   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

}
