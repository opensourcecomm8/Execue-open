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
package com.execue.ac.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.execue.ac.bean.ConceptColumnMapping;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.service.IAnswersCatalogConstants;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.ac.service.IAnswersCatalogSQLQueryGenerationService;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetCreationInfo;
import com.execue.core.common.bean.entity.AssetDetail;
import com.execue.core.common.bean.entity.AssetExtendedDetail;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.DefaultDynamicValue;
import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.AssetExtractionInput;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExeCueXMLUtils;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.das.datatransfer.etl.bean.DataTransferStatus;
import com.execue.das.datatransfer.etl.exception.RemoteDataTransferException;
import com.execue.das.datatransfer.etl.helper.RemoteDataTransferHelper;
import com.execue.das.datatransfer.etl.service.IRemoteDataTransferService;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.platform.security.ISecurityDefinitionPublishWrapperService;
import com.execue.platform.swi.IAssetExtractionService;
import com.execue.platform.swi.IAssetPublishService;
import com.execue.platform.swi.ICorrectMappingService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.security.exception.SecurityException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IAssetDetailService;
import com.execue.swi.service.IDefaultDynamicValueService;
import com.execue.swi.service.IDefaultMetricService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingManagementService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * This abstract class contains helper methods needed by both the cube and mart processes.
 */
public class AnswersCatalogServiceHelper implements IAnswersCatalogConstants {

   private ISDXRetrievalService                     sdxRetrievalService;
   private ISDXManagementService                    sdxManagementService;
   private IMappingRetrievalService                 mappingRetrievalService;
   private IMappingManagementService                mappingManagementService;
   private IKDXRetrievalService                     kdxRetrievalService;
   private IDefaultDynamicValueService              defaultDynamicValueService;
   private ICorrectMappingService                   correctMappingService;
   private IDefaultMetricService                    defaultMetricService;
   private IAssetPublishService                     assetPublishService;
   private IAssetExtractionService                  assetExtractionService;
   private IAssetDetailService                      assetDetailService;
   private ICoreConfigurationService                coreConfigurationService;
   private IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService;
   private IAnswersCatalogDataAccessService         answersCatalogDataAccessService;
   private IRemoteDataTransferService               remoteDataTransferService;
   private IJobDataService                          jobDataService;
   private RemoteDataTransferHelper                 remoteDataTransferHelper;
   private ISecurityDefinitionPublishWrapperService securityDefinitionPublishWrapperService;

   /**
    * This method builds the concept column mapping information for concept within asset and model scope.
    * 
    * @param conceptName
    * @param modelId
    * @param assetId
    * @return conceptColumnMapping
    * @throws AnswersCatalogException
    */
   public ConceptColumnMapping buildConceptColumnMapping (String conceptName, Long modelId, Long assetId)
            throws AnswersCatalogException {
      Concept concept = populateConcept(conceptName, modelId);
      return populateConceptColumnMapping(concept, modelId, assetId);
   }

   /**
    * This method will return the list of concept ConceptColumnMapping for the given concepts names, modelId, assetId
    * 
    * @param concepts
    * @param modelId
    * @param assetId
    * @return the List<ConceptColumnMapping>
    * @throws AnswersCatalogException
    */
   public List<ConceptColumnMapping> buildConceptColumnMapping (List<String> concepts, Long modelId, Long assetId)
            throws AnswersCatalogException {
      List<ConceptColumnMapping> conceptColumnMappings = new ArrayList<ConceptColumnMapping>(1);
      List<Concept> populatedConcepts = populateConcepts(concepts, modelId);
      for (Concept concept : populatedConcepts) {
         conceptColumnMappings.add(populateConceptColumnMapping(concept, modelId, assetId));
      }
      return conceptColumnMappings;
   }

   public List<ConceptColumnMapping> buildFactTableConceptColumnNonPrimaryMapping (List<String> concepts, Long modelId,
            Long assetId) throws AnswersCatalogException {
      List<ConceptColumnMapping> conceptColumnMappings = new ArrayList<ConceptColumnMapping>(1);
      List<Concept> populatedConcepts = populateConcepts(concepts, modelId);
      for (Concept concept : populatedConcepts) {
         conceptColumnMappings.add(populateConceptColumnNonPrimaryMapping(concept, modelId, assetId));
      }
      return conceptColumnMappings;
   }

   public void refreshDefaultDynamicValues (Long modelId, Asset sourceAsset, Asset targetAsset,
            List<BusinessEntityDefinition> conceptBedsToRefresh) throws AnswersCatalogException {
      try {
         List<Long> timeFrameBEDIdsToRefresh = new ArrayList<Long>();
         for (BusinessEntityDefinition conceptBedToRefresh : conceptBedsToRefresh) {
            if (getKdxRetrievalService().isConceptMatchedBusinessEntityType(conceptBedToRefresh.getConcept().getId(),
                     modelId, ExecueConstants.TIME_FRAME_TYPE)) {
               timeFrameBEDIdsToRefresh.add(conceptBedToRefresh.getId());
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(timeFrameBEDIdsToRefresh)) {
            // get the existing DDVs from the source asset
            List<DefaultDynamicValue> sourceDefaultDynamicValues = getDefaultDynamicValueService()
                     .getDefaultDynamicValues(sourceAsset.getId(), timeFrameBEDIdsToRefresh);
            // get the existing DDVs from the source asset
            List<DefaultDynamicValue> existingTargetDefaultDynamicValues = getDefaultDynamicValueService()
                     .getDefaultDynamicValues(targetAsset.getId(), timeFrameBEDIdsToRefresh);
            if (ExecueCoreUtil.isCollectionNotEmpty(sourceDefaultDynamicValues)
                     && ExecueCoreUtil.isCollectionNotEmpty(existingTargetDefaultDynamicValues)) {
               List<DefaultDynamicValue> updatedTargetDefaultDynamicValues = new ArrayList<DefaultDynamicValue>();
               // clone them and replace asset id and create for the target asset
               for (DefaultDynamicValue ddv : sourceDefaultDynamicValues) {
                  DefaultDynamicValue defaultDynamicValue = ExecueBeanCloneUtil.cloneDefaultDynamicValue(ddv);
                  defaultDynamicValue.setAssetId(targetAsset.getId());
                  updatedTargetDefaultDynamicValues.add(defaultDynamicValue);
               }
               getDefaultDynamicValueService().deleteDefaultDynamicValues(existingTargetDefaultDynamicValues);
               getDefaultDynamicValueService().createDefaultDynamicValues(updatedTargetDefaultDynamicValues);
            }
         }
      } catch (MappingException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      } catch (KDXException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method creates the default dynamic values from source asset to the target asset
    * 
    * @param modelId
    * @param sourceAsset
    * @param targetAsset
    * @throws AnswersCatalogException
    */
   public void createDefaultDynamicValues (Long modelId, Asset sourceAsset, Asset targetAsset)
            throws AnswersCatalogException {
      try {
         // get all mapped time frame concepts from target asset
         List<Mapping> mappings = getMappingRetrievalService().getMappedConceptsOfParticularType(targetAsset.getId(),
                  modelId, ExecueConstants.TIME_FRAME_TYPE);
         if (ExecueCoreUtil.isCollectionNotEmpty(mappings)) {
            // in source asset we can have 10 time frame concepts we are interested in time frame concepts in target
            // asset
            List<Long> timeFrameBEDIds = new ArrayList<Long>();
            for (Mapping mapping : mappings) {
               timeFrameBEDIds.add(mapping.getBusinessEntityDefinition().getId());
            }
            // get the existing DDVs from the source asset
            List<DefaultDynamicValue> sourceDefaultDynamicValues = getDefaultDynamicValueService()
                     .getDefaultDynamicValues(sourceAsset.getId(), timeFrameBEDIds);
            if (ExecueCoreUtil.isCollectionNotEmpty(sourceDefaultDynamicValues)) {
               List<DefaultDynamicValue> targetDefaultDynamicValues = new ArrayList<DefaultDynamicValue>();
               // clone them and replace asset id and create for the target asset
               for (DefaultDynamicValue ddv : sourceDefaultDynamicValues) {
                  DefaultDynamicValue defaultDynamicValue = ExecueBeanCloneUtil.cloneDefaultDynamicValue(ddv);
                  defaultDynamicValue.setAssetId(targetAsset.getId());
                  targetDefaultDynamicValues.add(defaultDynamicValue);
               }

               getDefaultDynamicValueService().createDefaultDynamicValues(targetDefaultDynamicValues);
            }
         }
      } catch (MappingException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method creates default metrics from source asset to target asset
    * 
    * @param modelId
    * @param sourceAsset
    * @param targetAsset
    * @param assetType
    * @throws AnswersCatalogException
    */
   public void createDefaultMetrics (Long modelId, Asset sourceAsset, Asset targetAsset, AssetType assetType)
            throws AnswersCatalogException {
      try {
         List<DefaultMetric> sourceAssetDefaultMetrics = null;
         // target asset type is cube, then we will get max from configuration.
         if (AssetType.Cube.equals(assetType)) {
            int maxMetrics = getCoreConfigurationService().getMaxDefaultMetrics();
            sourceAssetDefaultMetrics = getDefaultMetricService().getValidDefaultMetricsForAsset(sourceAsset.getId(),
                     maxMetrics);
         }
         // if it is mart then we can copy entire source default metrics
         else if (AssetType.Mart.equals(assetType)) {
            sourceAssetDefaultMetrics = getDefaultMetricService().getAllDefaultMetricsForAsset(sourceAsset.getId());
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(sourceAssetDefaultMetrics)) {
            List<DefaultMetric> targetAssetDefaultMetrics = new ArrayList<DefaultMetric>();
            // clone the mapping and replace with target information.
            for (DefaultMetric defaultMetric : sourceAssetDefaultMetrics) {
               Long sourceMappingId = defaultMetric.getMappingId();
               Mapping mapping = getMappingRetrievalService().getMapping(sourceMappingId);
               Concept concept = mapping.getBusinessEntityDefinition().getConcept();
               Mapping targetMapping = getMappingRetrievalService().getPrimaryMapping(concept.getId(), modelId,
                        targetAsset.getId());

               DefaultMetric clonedDefaultMetric = ExecueBeanCloneUtil.cloneDefaultMetric(defaultMetric);
               clonedDefaultMetric.setAedId(targetMapping.getAssetEntityDefinition().getColum().getId());
               clonedDefaultMetric.setTableId(targetMapping.getAssetEntityDefinition().getTabl().getId());
               clonedDefaultMetric.setMappingId(targetMapping.getId());
               targetAssetDefaultMetrics.add(clonedDefaultMetric);
            }
            getMappingManagementService().createDefaultMetrics(targetAssetDefaultMetrics);
         }

      } catch (SWIException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method corrects the mapping for asset passed.
    * 
    * @param applicationId
    * @param asset
    * @throws AnswersCatalogException
    */
   public void correctMappings (Long applicationId, Asset asset) throws AnswersCatalogException {
      try {
         getCorrectMappingService().correctMappingsForAsset(applicationId, asset.getId());
      } catch (SWIException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method activates the asset for answering questions
    * 
    * @param targetAsset
    * @throws AnswersCatalogException
    */
   public void activateAsset (Asset targetAsset) throws AnswersCatalogException {
      try {
         getSdxManagementService().updateAssetStatus(targetAsset.getId(), StatusEnum.ACTIVE);
      } catch (SDXException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method activates the asset for answering questions
    * 
    * @param targetAsset
    * @throws AnswersCatalogException
    */
   public void inActivateAsset (Asset targetAsset) throws AnswersCatalogException {
      try {
         getSdxManagementService().updateAssetStatus(targetAsset.getId(), StatusEnum.INACTIVE);
      } catch (SDXException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method apply the permissions from source asset to target asset
    * 
    * @param applicationId
    * @param userId
    * @param sourceAsset
    * @param targetAsset
    * @throws AnswersCatalogException
    */
   public void applyAssetPermissions (Long applicationId, Long userId, Asset sourceAsset, Asset targetAsset)
            throws AnswersCatalogException {
      try {
         getSecurityDefinitionPublishWrapperService().applyRolePermissionOnAssetWithPropagation(userId,
                  targetAsset.getId());

         getAssetPublishService()
                  .publishAsset(applicationId, targetAsset.getId(), userId, sourceAsset.getPublishMode());
      } catch (SWIException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      } catch (SecurityException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method extract the asset from source location into swi
    * 
    * @param asset
    * @param tables
    * @throws AnswersCatalogException
    */
   public boolean extractAsset (Asset asset, List<Tabl> tables) throws AnswersCatalogException {
      try {
         AssetExtractionInput assetExtractionInput = prepareAssetRegistrationInput(asset, tables);
         return ExecueBeanUtil.getCorrespondingBooleanValue(getAssetExtractionService().registerAsset(
                  assetExtractionInput));
      } catch (SWIException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method copies the information from source asset to target asset
    * 
    * @param sourceAsset
    * @param targetAsset
    * @throws AnswersCatalogException
    */
   public void copyAssetDetailProperties (Asset sourceAsset, Asset targetAsset, AssetCreationInfo assetCreationInfo)
            throws AnswersCatalogException {
      try {
         AssetDetail assetDetailInfo = getAssetDetailService().getAssetDetailInfo(sourceAsset.getId());
         AssetExtendedDetail assetExtendedDetailInfo = getAssetDetailService().getAssetExtendedDetailInfo(
                  assetDetailInfo.getId());
         assetCreationInfo.setLastModifiedTime(new Date());
         String creationInfo = ExeCueXMLUtils.getXMLStringFromObject(assetCreationInfo);
         AssetExtendedDetail targetAssetExtendedDetailInfo = null;
         AssetDetail targetAssetDetailInfo = getAssetDetailService().getAssetDetailInfo(targetAsset.getId());
         if (targetAssetDetailInfo == null) {
            targetAssetDetailInfo = assetDetailInfo;
            targetAssetDetailInfo.setId(null);
            targetAssetExtendedDetailInfo = assetExtendedDetailInfo;
            targetAssetExtendedDetailInfo.setId(null);
            targetAssetDetailInfo.setAssetId(targetAsset.getId());
            targetAssetDetailInfo.setAssetExtendedDetail(targetAssetExtendedDetailInfo);
         } else {
            targetAssetDetailInfo.setShortNote(assetDetailInfo.getShortNote());
            targetAssetDetailInfo.setShortDisclaimer(assetDetailInfo.getShortDisclaimer());
            targetAssetExtendedDetailInfo = getAssetDetailService()
                     .getAssetExtendedDetailByAssetId(targetAsset.getId());
            targetAssetExtendedDetailInfo.setCreationInfo(assetExtendedDetailInfo.getCreationInfo());
            targetAssetExtendedDetailInfo.setExtendedNote(assetExtendedDetailInfo.getExtendedNote());
            targetAssetExtendedDetailInfo.setExtendedDisclaimer(assetExtendedDetailInfo.getExtendedDisclaimer());
            targetAssetDetailInfo.setAssetExtendedDetail(targetAssetExtendedDetailInfo);
         }
         targetAssetExtendedDetailInfo.setCreationInfo(creationInfo);
         getAssetDetailService().createUpdateAssetDetail(targetAssetDetailInfo);
      } catch (SDXException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method copies the information from source table to target table
    * 
    * @param sourceTable
    * @param targetTable
    * @throws AnswersCatalogException
    */
   public void copyTableProperties (Tabl sourceTable, Tabl targetTable) throws AnswersCatalogException {
      try {
         targetTable.setEligibleDefaultMetric(sourceTable.getEligibleDefaultMetric());
         targetTable.setIndicator(sourceTable.getIndicator());
         getSdxManagementService().updateTable(targetTable);
      } catch (SDXException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   public void copyMemberProperties (Membr sourceMember, Membr targetMember) throws SDXException {
      targetMember.setIndicatorBehavior(sourceMember.getIndicatorBehavior());
      targetMember.setLookupDescription(sourceMember.getLookupDescription());
      getSdxManagementService().updateMember(targetMember);
   }

   /**
    * This methods copies the information from source column to target column.
    * 
    * @param sourceColumn
    * @param targetAsset
    * @param targetTable
    * @param targetColumn
    * @throws AnswersCatalogException
    */
   public void copyColumnProperties (Colum sourceColumn, Asset targetAsset, Tabl targetTable, Colum targetColumn)
            throws AnswersCatalogException {
      try {
         targetColumn.setKdxDataType(sourceColumn.getKdxDataType());
         targetColumn.setConversionType(sourceColumn.getConversionType());
         targetColumn.setDataFormat(sourceColumn.getDataFormat());
         targetColumn.setUnit(sourceColumn.getUnit());
         targetColumn.setRequired(sourceColumn.getRequired());
         targetColumn.setDefaultValue(sourceColumn.getDefaultValue());
         targetColumn.setGranularity(sourceColumn.getGranularity());
         targetColumn.setIndicator(sourceColumn.getIndicator());
         getSdxManagementService().updateColumn(targetAsset.getId(), targetTable.getId(), targetColumn);
      } catch (SDXException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * Prepares the asset registration input.
    * 
    * @param asset
    * @param tables
    * @return assetRegistrationInput
    */
   private AssetExtractionInput prepareAssetRegistrationInput (Asset asset, List<Tabl> tables) {
      AssetExtractionInput assetRegistrationInput = new AssetExtractionInput();
      assetRegistrationInput.setRemainingTablesAreFacts(true);
      assetRegistrationInput.setSourceAsset(asset);
      assetRegistrationInput.setTables(tables);
      return assetRegistrationInput;
   }

   /**
    * This method transfers remote data using ETL process
    * 
    * @param sourceAsset
    * @param targetAsset
    * @param targetInsertQuery
    * @param sourceSelectQuery
    * @param targetCreateQuery
    * @param targetTable
    * @return dataTransferStatus
    * @throws AnswersCatalogException
    */
   public DataTransferStatus transferRemoteData (DataSource sourceDataSource, DataSource targetDataSource,
            String targetInsertQuery, String sourceSelectQuery, String targetCreateQuery, String targetTable)
            throws AnswersCatalogException {
      DataTransferStatus dataTransferStatus = null;
      try {
         dataTransferStatus = getRemoteDataTransferService().transferRemoteData(sourceDataSource, targetDataSource,
                  targetInsertQuery, sourceSelectQuery, targetCreateQuery, targetTable);
      } catch (RemoteDataTransferException remoteDataTransferException) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_EXCEPTION_CODE,
                  remoteDataTransferException);
      }
      return dataTransferStatus;
   }

   /**
    * This method transfers remote data using ETL process. It considers the rollback query passed.
    * 
    * @param sourceAsset
    * @param targetAsset
    * @param targetInsertQuery
    * @param sourceSelectQuery
    * @param targetCreateQuery
    * @param targetRollBackQuery
    * @param targetTable
    * @return dataTransferStatus
    * @throws AnswersCatalogException
    */
   public DataTransferStatus transferRemoteData (DataSource sourceDataSource, DataSource targetDataSource,
            String targetInsertQuery, String sourceSelectQuery, String targetCreateQuery, String targetRollBackQuery,
            String targetTable) throws AnswersCatalogException {
      DataTransferStatus dataTransferStatus = null;
      try {
         dataTransferStatus = getRemoteDataTransferService().transferRemoteData(sourceDataSource, targetDataSource,
                  targetInsertQuery, sourceSelectQuery, targetCreateQuery, targetRollBackQuery, targetTable);
      } catch (RemoteDataTransferException remoteDataTransferException) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_EXCEPTION_CODE,
                  remoteDataTransferException);
      }
      return dataTransferStatus;
   }

   /**
    * This method transfers local data using insert into select
    * 
    * @param dataSource
    * @param createQuery
    * @param insertQuery
    * @param selectQuery
    * @return count of records
    * @throws AnswersCatalogException
    */
   public Integer transferLocalData (DataSource dataSource, String createQuery, String insertQuery, String selectQuery)
            throws AnswersCatalogException {
      try {
         String dataTransferQuery = insertQuery + SQL_SPACE_DELIMETER + selectQuery;
         getAnswersCatalogDataAccessService().executeDDLStatement(dataSource.getName(), createQuery);
         return getAnswersCatalogDataAccessService().executeDMLStatement(dataSource.getName(), dataTransferQuery, null,
                  null);
      } catch (DataAccessException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method transfers local data using insert into select
    * 
    * @param dataSource
    * @param createQuery
    * @param insertQuery
    * @param selectQuery
    * @return count of records
    * @throws AnswersCatalogException
    */
   public Integer transferLocalData (DataSource dataSource, String tableName, String createQuery, String insertQuery,
            String selectQuery) throws AnswersCatalogException {
      try {
         String dataTransferQuery = insertQuery + SQL_SPACE_DELIMETER + selectQuery;
         if (!getAnswersCatalogDataAccessService().isTableExists(dataSource, tableName)) {
            getAnswersCatalogDataAccessService().executeDDLStatement(dataSource.getName(), createQuery);
         }
         return getAnswersCatalogDataAccessService().executeDMLStatement(dataSource.getName(), dataTransferQuery, null,
                  null);
      } catch (DataAccessException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method transfers local data using insert into select
    * 
    * @param dataSource
    * @param createQuery
    * @param insertQuery
    * @param selectQuery
    * @return count of records
    * @throws AnswersCatalogException
    */
   public List<Integer> transferLocalData (DataSource dataSource, String tableName, String createQuery,
            String insertQuery, List<Integer> parameterTypes, List<List<Object>> parameterValues)
            throws AnswersCatalogException {
      try {
         if (!getAnswersCatalogDataAccessService().isTableExists(dataSource, tableName)) {
            getAnswersCatalogDataAccessService().executeDDLStatement(dataSource.getName(), createQuery);
         }
         return getAnswersCatalogDataAccessService().executeDMLStatements(dataSource.getName(), insertQuery,
                  parameterValues, parameterTypes);
      } catch (DataAccessException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method populates the concept column mapping
    * 
    * @param concept
    * @param modelId
    * @param assetId
    * @return conceptColumnMapping
    * @throws AnswersCatalogException
    */
   private ConceptColumnMapping populateConceptColumnMapping (Concept concept, Long modelId, Long assetId)
            throws AnswersCatalogException {
      ConceptColumnMapping conceptColumnMapping = null;
      try {
         Mapping primaryMapping = getMappingRetrievalService().getPrimaryMapping(concept.getId(), modelId, assetId);
         if (primaryMapping != null) {
            conceptColumnMapping = AnswersCatalogUtil.prepareConceptColumnMapping(primaryMapping);
         }
      } catch (MappingException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
      return conceptColumnMapping;
   }

   private ConceptColumnMapping populateConceptColumnNonPrimaryMapping (Concept concept, Long modelId, Long assetId)
            throws AnswersCatalogException {
      ConceptColumnMapping conceptColumnMapping = null;
      try {
         Mapping primaryMapping = getMappingRetrievalService().getNonPrimaryMapping(concept.getId(), modelId, assetId);
         if (primaryMapping != null) {
            conceptColumnMapping = AnswersCatalogUtil.prepareConceptColumnMapping(primaryMapping);
         }
      } catch (MappingException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
      return conceptColumnMapping;
   }

   /**
    * This method populates the concepts from strings(concept names)
    * 
    * @param requestedConcepts
    * @param modelId
    * @return populatedConcepts
    * @throws AnswersCatalogException
    */
   private List<Concept> populateConcepts (List<String> requestedConcepts, Long modelId) throws AnswersCatalogException {
      List<Concept> populatedConcepts = new ArrayList<Concept>(1);
      for (String conceptName : requestedConcepts) {
         populatedConcepts.add(populateConcept(conceptName, modelId));
      }
      return populatedConcepts;
   }

   /**
    * This method populates the concept from concept name
    * 
    * @param conceptName
    * @param modelId
    * @return concept
    * @throws AnswersCatalogException
    */
   private Concept populateConcept (String conceptName, Long modelId) throws AnswersCatalogException {
      try {
         return getKdxRetrievalService().getConceptByName(modelId, conceptName);
      } catch (SWIException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   public JobOperationalStatus createJobOperationalStatus (JobRequest jobRequest, String statusMessage)
            throws AnswersCatalogException {
      JobOperationalStatus jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               statusMessage, JobStatus.INPROGRESS, null, new Date());
      try {
         jobDataService.createJobOperationStatus(jobOperationalStatus);
      } catch (QueryDataException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
      return jobOperationalStatus;
   }

   public void updateJobOperationalStatus (JobOperationalStatus jobOperationalStatus, JobRequest jobRequest)
            throws AnswersCatalogException {
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      try {
         jobDataService.updateJobOperationStatus(jobOperationalStatus);
      } catch (QueryDataException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   public void updateJobOperationalStatusForFailure (JobOperationalStatus jobOperationalStatus, JobRequest jobRequest,
            String statusMessage) throws AnswersCatalogException {
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.FAILURE, statusMessage, new Date());
      try {
         jobDataService.updateJobOperationStatus(jobOperationalStatus);
      } catch (QueryDataException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   public void createPreFactTableOnTarget (DataSource targetDataSource, String targetTableName,
            String targetTableCreateStatement) throws AnswersCatalogException {
      try {
         getRemoteDataTransferHelper().createTargetTable(targetDataSource, targetTableName, targetTableCreateStatement);
      } catch (RemoteDataTransferException rdtException) {
         throw new AnswersCatalogException(rdtException.getCode(), rdtException);
      }
   }

   public List<Long> getBEDIDsFromConceptColumnMappings (List<ConceptColumnMapping> conceptColumnMappings) {
      List<Long> bedIDS = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(conceptColumnMappings)) {
         for (ConceptColumnMapping conceptColumnMapping : conceptColumnMappings) {
            bedIDS.add(conceptColumnMapping.getBusinessEntityDefinition().getId());
         }
      }
      return bedIDS;
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

   public IAnswersCatalogDataAccessService getAnswersCatalogDataAccessService () {
      return answersCatalogDataAccessService;
   }

   public void setAnswersCatalogDataAccessService (IAnswersCatalogDataAccessService answersCatalogDataAccessService) {
      this.answersCatalogDataAccessService = answersCatalogDataAccessService;
   }

   public IDefaultDynamicValueService getDefaultDynamicValueService () {
      return defaultDynamicValueService;
   }

   public void setDefaultDynamicValueService (IDefaultDynamicValueService defaultDynamicValueService) {
      this.defaultDynamicValueService = defaultDynamicValueService;
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

   public IDefaultMetricService getDefaultMetricService () {
      return defaultMetricService;
   }

   public void setDefaultMetricService (IDefaultMetricService defaultMetricService) {
      this.defaultMetricService = defaultMetricService;
   }

   public ICorrectMappingService getCorrectMappingService () {
      return correctMappingService;
   }

   public void setCorrectMappingService (ICorrectMappingService correctMappingService) {
      this.correctMappingService = correctMappingService;
   }

   public IAssetPublishService getAssetPublishService () {
      return assetPublishService;
   }

   public void setAssetPublishService (IAssetPublishService assetPublishService) {
      this.assetPublishService = assetPublishService;
   }

   public IAssetExtractionService getAssetExtractionService () {
      return assetExtractionService;
   }

   public void setAssetExtractionService (IAssetExtractionService assetExtractionService) {
      this.assetExtractionService = assetExtractionService;
   }

   public IAssetDetailService getAssetDetailService () {
      return assetDetailService;
   }

   public void setAssetDetailService (IAssetDetailService assetDetailService) {
      this.assetDetailService = assetDetailService;
   }

   public IAnswersCatalogSQLQueryGenerationService getAnswersCatalogSQLQueryGenerationService () {
      return answersCatalogSQLQueryGenerationService;
   }

   public void setAnswersCatalogSQLQueryGenerationService (
            IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService) {
      this.answersCatalogSQLQueryGenerationService = answersCatalogSQLQueryGenerationService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IRemoteDataTransferService getRemoteDataTransferService () {
      return remoteDataTransferService;
   }

   public void setRemoteDataTransferService (IRemoteDataTransferService remoteDataTransferService) {
      this.remoteDataTransferService = remoteDataTransferService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IMappingManagementService getMappingManagementService () {
      return mappingManagementService;
   }

   public void setMappingManagementService (IMappingManagementService mappingManagementService) {
      this.mappingManagementService = mappingManagementService;
   }

   public RemoteDataTransferHelper getRemoteDataTransferHelper () {
      return remoteDataTransferHelper;
   }

   public void setRemoteDataTransferHelper (RemoteDataTransferHelper remoteDataTransferHelper) {
      this.remoteDataTransferHelper = remoteDataTransferHelper;
   }

   /**
    * @return the securityDefinitionPublishWrapperService
    */
   public ISecurityDefinitionPublishWrapperService getSecurityDefinitionPublishWrapperService () {
      return securityDefinitionPublishWrapperService;
   }

   /**
    * @param securityDefinitionPublishWrapperService the securityDefinitionPublishWrapperService to set
    */
   public void setSecurityDefinitionPublishWrapperService (
            ISecurityDefinitionPublishWrapperService securityDefinitionPublishWrapperService) {
      this.securityDefinitionPublishWrapperService = securityDefinitionPublishWrapperService;
   }

}