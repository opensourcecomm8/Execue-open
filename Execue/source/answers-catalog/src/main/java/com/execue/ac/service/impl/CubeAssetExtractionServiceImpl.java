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
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.ac.bean.AnswersCatalogConfigurationContext;
import com.execue.ac.bean.ConceptColumnMapping;
import com.execue.ac.bean.CubeCreationOutputInfo;
import com.execue.ac.bean.CubeCreationPopulatedContext;
import com.execue.ac.bean.CubeFactTableStructure;
import com.execue.ac.bean.CubeLookupTableStructure;
import com.execue.ac.bean.CubeLookupTableStructureInfo;
import com.execue.ac.bean.CubeSourceColumnMapping;
import com.execue.ac.bean.CubeUpdationDimensionInfo;
import com.execue.ac.bean.CubeUpdationInputInfo;
import com.execue.ac.bean.CubeUpdationOutputInfo;
import com.execue.ac.bean.CubeUpdationPopulatedContext;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.CubeAssetExtractionException;
import com.execue.ac.helper.CubeCreationServiceHelper;
import com.execue.ac.service.ICubeAssetExtractionService;
import com.execue.ac.service.ICubeCreationConstants;
import com.execue.ac.service.ICubeOperationalConstants;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetCreationInfo;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Join;
import com.execue.core.common.bean.entity.JoinDefinition;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.JoinType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.PrimaryMappingType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.exception.JoinException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IJoinService;
import com.execue.swi.service.IMappingManagementService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * This service used to extract the asset from cube created
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class CubeAssetExtractionServiceImpl implements ICubeAssetExtractionService, ICubeCreationConstants,
         ICubeOperationalConstants {

   private CubeCreationServiceHelper cubeCreationServiceHelper;
   private IMappingManagementService mappingManagementService;
   private IMappingRetrievalService  mappingRetrievalService;
   private ISDXRetrievalService      sdxRetrievalService;
   private ISDXDeletionService       sdxDeletionService;
   private ISDXManagementService     sdxManagementService;
   private IBaseKDXRetrievalService  baseKDXRetrievalService;
   private IJoinService              joinService;
   private static final Logger       logger = Logger.getLogger(CubeAssetExtractionServiceImpl.class);

   @Override
   public boolean updateAsset (CubeUpdationOutputInfo cubeUpdationOutputInfo) throws CubeAssetExtractionException {
      boolean assetUpdationSuccess = true;
      CubeUpdationInputInfo cubeUpdationInputInfo = cubeUpdationOutputInfo.getCubeUpdationInputInfo();
      CubeUpdationPopulatedContext cubeUpdationPopulatedContext = cubeUpdationInputInfo
               .getCubeUpdationPopulatedContext();
      JobRequest jobRequest = cubeUpdationPopulatedContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  DELETE_DELETED_MEMBERS_FROM_SWI);
         deleteMembersFromSWI(cubeUpdationPopulatedContext);
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  ADD_ADDED_MEMBERS_TO_SWI);
         addMembersToSWI(cubeUpdationPopulatedContext);
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  DEFAULT_DYNAMIC_VALUES_UPDATION);
         defaultDynamicValuesUpdation(cubeUpdationPopulatedContext);
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

      } catch (AnswersCatalogException e) {
         assetUpdationSuccess = false;
         exception = e;
         throw new CubeAssetExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_ASSET_UPDATION_EXCEPTION_CODE, e);
      } catch (SDXException e) {
         assetUpdationSuccess = false;
         exception = e;
         throw new CubeAssetExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_ASSET_UPDATION_EXCEPTION_CODE, e);
      } catch (MappingException e) {
         assetUpdationSuccess = false;
         exception = e;
         throw new CubeAssetExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_ASSET_UPDATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubeAssetExtractionException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return assetUpdationSuccess;
   }

   private ConceptColumnMapping getMatchedLookupTableExistingCubeConceptColumnMapping (
            CubeUpdationPopulatedContext cubeUpdationPopulatedContext,
            CubeUpdationDimensionInfo cubeUpdationDimensionInfo) {
      List<ConceptColumnMapping> updatedDimensionsExistingCubeLookupTableMappings = cubeUpdationPopulatedContext
               .getUpdatedDimensionsExistingCubeLookupTableMappings();
      String dimensionName = cubeUpdationDimensionInfo.getDimensionName();
      // get the matching concept column mapping for dimension
      ConceptColumnMapping matchedDimensionExistingCubeLookupTableMapping = AnswersCatalogUtil
               .getMatchedDimensionMapping(dimensionName, updatedDimensionsExistingCubeLookupTableMappings);
      return matchedDimensionExistingCubeLookupTableMapping;
   }

   private ConceptColumnMapping getMatchedLookupTableSourceAssetConceptColumnMapping (
            CubeUpdationPopulatedContext cubeUpdationPopulatedContext,
            CubeUpdationDimensionInfo cubeUpdationDimensionInfo) {
      List<ConceptColumnMapping> sourceSimpleLookupDimensions = cubeUpdationPopulatedContext
               .getExistingCubeCreationPopulatedContext().getPopulatedSimpleLookupDimensions();
      String dimensionName = cubeUpdationDimensionInfo.getDimensionName();
      // get the matching concept column mapping for dimension
      ConceptColumnMapping sourceLookupTableMapping = AnswersCatalogUtil.getMatchedDimensionMapping(dimensionName,
               sourceSimpleLookupDimensions);
      return sourceLookupTableMapping;
   }

   private void defaultDynamicValuesUpdation (CubeUpdationPopulatedContext cubeUpdationPopulatedContext)
            throws AnswersCatalogException {
      CubeCreationPopulatedContext existingCubeCreationPopulatedContext = cubeUpdationPopulatedContext
               .getExistingCubeCreationPopulatedContext();
      List<ConceptColumnMapping> updatedDimensionsExistingCubeLookupTableMappings = cubeUpdationPopulatedContext
               .getUpdatedDimensionsExistingCubeLookupTableMappings();
      List<BusinessEntityDefinition> conceptBedsToRefresh = new ArrayList<BusinessEntityDefinition>();
      for (ConceptColumnMapping conceptColumnMapping : updatedDimensionsExistingCubeLookupTableMappings) {
         conceptBedsToRefresh.add(conceptColumnMapping.getBusinessEntityDefinition());
      }
      getCubeCreationServiceHelper().refreshDefaultDynamicValues(existingCubeCreationPopulatedContext.getModelId(),
               existingCubeCreationPopulatedContext.getSourceAsset(),
               existingCubeCreationPopulatedContext.getTargetAsset(), conceptBedsToRefresh);
   }

   private void addMembersToSWI (CubeUpdationPopulatedContext cubeUpdationPopulatedContext) throws SDXException,
            AnswersCatalogException, MappingException {
      List<CubeUpdationDimensionInfo> cubeUpdationDimensionInfoList = cubeUpdationPopulatedContext
               .getCubeUpdationDimensionInfoList();
      for (CubeUpdationDimensionInfo cubeUpdationDimensionInfo : cubeUpdationDimensionInfoList) {
         List<Membr> addedMembers = cubeUpdationDimensionInfo.getAddedMembers();
         if (ExecueCoreUtil.isCollectionNotEmpty(addedMembers)) {
            ConceptColumnMapping existingCubeConceptColumnMapping = getMatchedLookupTableExistingCubeConceptColumnMapping(
                     cubeUpdationPopulatedContext, cubeUpdationDimensionInfo);
            AssetEntityDefinition existingAssetEntityDefinition = existingCubeConceptColumnMapping
                     .getAssetEntityDefinition();
            Asset existingAsset = existingAssetEntityDefinition.getAsset();
            Tabl existingTable = existingAssetEntityDefinition.getTabl();
            Colum existingColum = existingAssetEntityDefinition.getColum();
            List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(existingTable);

            ConceptColumnMapping sourceAssetConceptColumnMapping = getMatchedLookupTableSourceAssetConceptColumnMapping(
                     cubeUpdationPopulatedContext, cubeUpdationDimensionInfo);
            AssetEntityDefinition sourceAssetEntityDefinition = sourceAssetConceptColumnMapping
                     .getAssetEntityDefinition();
            Asset sourceAsset = sourceAssetEntityDefinition.getAsset();
            Tabl sourceTable = sourceAssetEntityDefinition.getTabl();
            Colum sourceColumn = sourceAssetEntityDefinition.getColum();

            getSdxManagementService().createMembers(existingAsset, existingTable, columns, addedMembers);
            List<Mapping> addedMemberMappings = new ArrayList<Mapping>();
            for (Membr addedMember : addedMembers) {
               Membr sourceMember = getSdxRetrievalService().getMemberByLookupValue(sourceAsset, sourceTable,
                        sourceColumn, addedMember.getLookupValue());
               getCubeCreationServiceHelper().copyMemberProperties(sourceMember, addedMember);
               BusinessEntityDefinition sourceMemberMappedInstanceBed = getMappingRetrievalService()
                        .getMappedInstanceForMember(sourceAsset.getId(), sourceTable.getId(), sourceMember.getId());
               AssetEntityDefinition existingMemberAed = getSdxRetrievalService().getAssetEntityDefinitionByIds(
                        existingAsset, existingTable, existingColum, addedMember);
               Mapping addedMemberMapping = new Mapping();
               addedMemberMapping.setAssetEntityDefinition(existingMemberAed);
               addedMemberMapping.setBusinessEntityDefinition(sourceMemberMappedInstanceBed);
               addedMemberMapping.setPrimary(PrimaryMappingType.PRIMARY);
               addedMemberMappings.add(addedMemberMapping);
            }
            getMappingManagementService().createMappings(addedMemberMappings);
         }
      }

      // TODO Auto-generated method stub
      // create
      // copy from source member
      // map to source member bedId

   }

   private void deleteMembersFromSWI (CubeUpdationPopulatedContext cubeUpdationPopulatedContext) throws SDXException {
      List<CubeUpdationDimensionInfo> cubeUpdationDimensionInfoList = cubeUpdationPopulatedContext
               .getCubeUpdationDimensionInfoList();
      for (CubeUpdationDimensionInfo cubeUpdationDimensionInfo : cubeUpdationDimensionInfoList) {
         List<Membr> deletedMembers = cubeUpdationDimensionInfo.getDeletedMembers();
         if (ExecueCoreUtil.isCollectionNotEmpty(deletedMembers)) {
            getSdxDeletionService().deleteMembers(deletedMembers);
         }
      }
   }

   public boolean extractAsset (CubeCreationOutputInfo cubeCreationOutputInfo) throws CubeAssetExtractionException {
      boolean assetExtractionSuccess = true;
      JobRequest jobRequest = cubeCreationOutputInfo.getCubeCreationInputInfo().getCubeCreationContext()
               .getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  CREATE_ASSET_TABLE_COLUMN_MEMBER);
         if (logger.isDebugEnabled()) {
            logger.debug("Creating Asset entities into swi.");
         }
         if (createAssetTableColumns(cubeCreationOutputInfo)) {
            getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

            jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                     COPY_ASSET_TABLE_COLUMN_MEMBER_INFO);
            if (logger.isDebugEnabled()) {
               logger.debug("Copy Asset entities info from source asset.");
            }
            copyAssetTableColumMemberInfo(cubeCreationOutputInfo);
            getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

            jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest, CREATE_JOINS);
            if (logger.isDebugEnabled()) {
               logger.debug("Creating the Joins from source Asset.");
            }
            createJoins(cubeCreationOutputInfo);
            getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

            jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                     CREATE_MAPPINGS_AND_GRAIN);
            if (logger.isDebugEnabled()) {
               logger.debug("Creating Mappings and Grain for Mart asset.");
            }
            createMappingsAndGrain(cubeCreationOutputInfo);
            getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

            jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                     ASSET_READINESS_AND_ACTIVATION);
            if (logger.isDebugEnabled()) {
               logger.debug("Asset Readiness and Activation");
            }
            prepareCubeInSWI(cubeCreationOutputInfo);
            getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
         } else {
            assetExtractionSuccess = false;
         }
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new CubeAssetExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_ASSET_EXTRACTION_EXCEPTION_CODE, e);
      } catch (SDXException e) {
         exception = e;
         throw new CubeAssetExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_ASSET_EXTRACTION_EXCEPTION_CODE, e);
      } catch (JoinException e) {
         exception = e;
         throw new CubeAssetExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_ASSET_EXTRACTION_EXCEPTION_CODE, e);
      } catch (KDXException e) {
         exception = e;
         throw new CubeAssetExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_ASSET_EXTRACTION_EXCEPTION_CODE, e);
      } catch (MappingException e) {
         exception = e;
         throw new CubeAssetExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_ASSET_EXTRACTION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubeAssetExtractionException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return assetExtractionSuccess;
   }

   /**
    * This method copies the mappings and grain from source asset to target asset
    * 
    * @param cubeCreationOutputInfo
    * @throws SDXException
    * @throws KDXException
    * @throws MappingException
    */
   private void createMappingsAndGrain (CubeCreationOutputInfo cubeCreationOutputInfo) throws SDXException,
            KDXException, MappingException {
      CubeCreationPopulatedContext cubeCreationPopulatedContext = cubeCreationOutputInfo.getCubeCreationInputInfo()
               .getCubeCreationPopulatedContext();
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = cubeCreationOutputInfo
               .getCubeCreationInputInfo().getAnswersCatalogConfigurationContext();
      Asset sourceAsset = cubeCreationPopulatedContext.getSourceAsset();
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();
      Application application = cubeCreationPopulatedContext.getApplication();
      Long modelId = cubeCreationPopulatedContext.getModelId();
      String statisticsConceptName = answersCatalogConfigurationContext.getStatisticsConceptName();

      // process fact table
      CubeFactTableStructure cubeFactTableStructure = cubeCreationOutputInfo.getCubeFactTableStructure();
      List<Mapping> factTableColumnLevelMappings = new ArrayList<Mapping>();
      // for each frequency measure and measure column of the fact table except lookup and stat columns we will copy the
      // mapping from source asset and add new aed into it.
      for (CubeSourceColumnMapping cubeSourceColumnMapping : cubeFactTableStructure
               .getCombinedColumnsExceptLookupAndStat()) {
         factTableColumnLevelMappings.add(prepareColumnLevelMapping(cubeSourceColumnMapping
                  .getSourceConceptColumnMapping().getMapping(), application.getName(), targetAsset.getName(),
                  cubeFactTableStructure.getTableName(), cubeSourceColumnMapping.getQueryColumn().getColumnName()));
      }
      // for each lookup column except stat we will copy the mapping from source asset and add new aed into it. We will
      // make grain type null as corresponding lookup column will have grain type attached
      for (CubeSourceColumnMapping cubeSourceColumnMapping : cubeFactTableStructure
               .getCombinedLookupColumnsExceptStat()) {
         Mapping columnLevelMapping = prepareColumnLevelMapping(cubeSourceColumnMapping.getSourceConceptColumnMapping()
                  .getMapping(), application.getName(), targetAsset.getName(), cubeFactTableStructure.getTableName(),
                  cubeSourceColumnMapping.getQueryColumn().getColumnName());
         columnLevelMapping.setAssetGrainType(null);
         factTableColumnLevelMappings.add(columnLevelMapping);
      }

      // map the stat column to statistics concept
      factTableColumnLevelMappings.add(prepareStatColumnMapping(statisticsConceptName, application.getName(),
               targetAsset.getName(), cubeFactTableStructure.getTableName(), cubeFactTableStructure.getStatColumn()
                        .getColumnName()));

      // creating the fact table concept level mappings.
      getMappingManagementService().createMappings(factTableColumnLevelMappings);

      // process lookup tables except stat
      CubeLookupTableStructure cubeLookupTableStructure = cubeCreationOutputInfo.getCubeLookupTableStructure();
      for (CubeLookupTableStructureInfo cubeLookupTableStructureInfo : cubeLookupTableStructure
               .getCombinedTablesExceptStat()) {
         String lookupTableName = cubeLookupTableStructureInfo.getTableName();
         String lookupValueColumn = cubeLookupTableStructureInfo.getLookupValueColumn().getColumnName();
         List<Mapping> lookupTableColumnLevelMapping = new ArrayList<Mapping>();
         // mapping lookup value column for each of the lookup table.
         lookupTableColumnLevelMapping.add(prepareColumnLevelMapping(cubeLookupTableStructureInfo
                  .getSourceConceptColumnMapping().getMapping(), application.getName(), targetAsset.getName(),
                  lookupTableName, lookupValueColumn));
         getMappingManagementService().createMappings(lookupTableColumnLevelMapping);

         // if parent exists in swi, then members will be there, we need to map members also.
         if (cubeLookupTableStructureInfo.isParentTableExistsInSWI()) {
            ConceptColumnMapping sourceConceptColumnMapping = cubeLookupTableStructureInfo
                     .getSourceConceptColumnMapping();
            Colum sourceColumn = sourceConceptColumnMapping.getColumn();
            Tabl sourceTable = sourceConceptColumnMapping.getTabl();
            // total count of members from swi.
            Long membersCount = getSdxRetrievalService().getTotalMembersCountOfColumn(sourceColumn);
            // process batch by batch
            Long pageSize = MEMBER_MAPPING_BATCH_SIZE;
            Long pageCount = new Double(Math.ceil(membersCount / new Double(pageSize))).longValue();
            for (Long pageNumber = 1L; pageNumber <= pageCount; pageNumber++) {
               List<Mapping> memberLevelMappings = new ArrayList<Mapping>();
               List<Membr> batchMembers = getSdxRetrievalService().getColumnMembersByPage(sourceColumn, pageNumber,
                        pageSize);
               // for each member, get the mapping from source asset
               for (Membr member : batchMembers) {
                  Mapping sourceMapping = getMappingRetrievalService().getPrimaryMapping(modelId, sourceAsset.getId(),
                           sourceTable.getId(), sourceColumn.getId(), member.getId());
                  if (sourceMapping != null) {
                     // clone the mapping and set the new aed of cube asset into it.
                     Mapping clonedMapping = ExecueBeanCloneUtil.cloneMapping(sourceMapping);
                     AssetEntityDefinition targetAED = getSdxRetrievalService()
                              .getUnPopulatedAssetEntityDefinitionByNames(application.getName(), targetAsset.getName(),
                                       lookupTableName, lookupValueColumn, member.getLookupValue());
                     if (targetAED != null) {
                        clonedMapping.setAssetEntityDefinition(targetAED);
                        memberLevelMappings.add(clonedMapping);
                     }
                  }
               }
               // create member level mappings.
               getMappingManagementService().createMappings(memberLevelMappings);
            }
         }
      }

      // process stat lookup table
      // TODO : -VG- need to check whether we need to map stat member with instance of statistics
      List<Mapping> statLookupTableColumnLevelMappings = new ArrayList<Mapping>();
      CubeLookupTableStructureInfo statLookupTable = cubeLookupTableStructure.getStatLookupTable();
      statLookupTableColumnLevelMappings.add(prepareStatColumnMapping(statisticsConceptName, application.getName(),
               targetAsset.getName(), statLookupTable.getTableName(), statLookupTable.getLookupValueColumn()
                        .getColumnName()));
      getMappingManagementService().createMappings(statLookupTableColumnLevelMappings);

   }

   /**
    * This method prepares the stat column mapping which will be mapped to statistics concept
    * 
    * @param statisticsConceptNameString
    * @param application
    * @param targetAsset
    * @param targetTable
    * @param targetColumn
    * @return statisticsMapping
    * @throws KDXException
    */
   private Mapping prepareStatColumnMapping (String statisticsConceptNameString, String application,
            String targetAsset, String targetTable, String targetColumn) throws KDXException {
      BusinessEntityDefinition statisticsConceptBED = getBaseKDXRetrievalService().getRealizedTypeBEDByName(
               statisticsConceptNameString);
      AssetEntityDefinition statisticsAED = getSdxRetrievalService().getUnPopulatedAssetEntityDefinitionByNames(
               application, targetAsset, targetTable, targetColumn, null);
      Mapping statisticsMapping = new Mapping();
      statisticsMapping.setAssetEntityDefinition(statisticsAED);
      statisticsMapping.setBusinessEntityDefinition(statisticsConceptBED);
      return statisticsMapping;
   }

   /**
    * This method takes source mapping and gets the new aed from swi and returns the modified target mapping.
    * 
    * @param sourceMapping
    * @param application
    * @param targetAsset
    * @param targetTable
    * @param targetColumn
    * @return clonedMapping
    */
   private Mapping prepareColumnLevelMapping (Mapping sourceMapping, String application, String targetAsset,
            String targetTable, String targetColumn) {
      Mapping clonedMapping = ExecueBeanCloneUtil.cloneMapping(sourceMapping);
      AssetEntityDefinition targetAED = getSdxRetrievalService().getUnPopulatedAssetEntityDefinitionByNames(
               application, targetAsset, targetTable, targetColumn, null);
      clonedMapping.setAssetEntityDefinition(targetAED);
      return clonedMapping;
   }

   /**
    * This method prepares the cube asset newly built for miscellaneous information
    * 
    * @param cubeCreationOutputInfo
    * @throws AnswersCatalogException
    */
   private void prepareCubeInSWI (CubeCreationOutputInfo cubeCreationOutputInfo) throws AnswersCatalogException {
      CubeCreationPopulatedContext cubeCreationPopulatedContext = cubeCreationOutputInfo.getCubeCreationInputInfo()
               .getCubeCreationPopulatedContext();
      Asset sourceAsset = cubeCreationPopulatedContext.getSourceAsset();
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();
      Application application = cubeCreationPopulatedContext.getApplication();
      Long modelId = cubeCreationPopulatedContext.getModelId();
      Long userId = cubeCreationPopulatedContext.getUserId();
      if (logger.isDebugEnabled()) {
         logger.debug("Correct Mappings.");
      }
      getCubeCreationServiceHelper().correctMappings(application.getId(), targetAsset);
      if (logger.isDebugEnabled()) {
         logger.debug("Creating Default Metrics");
      }
      getCubeCreationServiceHelper().createDefaultMetrics(modelId, sourceAsset, targetAsset, AssetType.Cube);
      if (logger.isDebugEnabled()) {
         logger.debug("Creating Default Dynamic Values");
      }
      getCubeCreationServiceHelper().createDefaultDynamicValues(modelId, sourceAsset, targetAsset);
      if (logger.isDebugEnabled()) {
         logger.debug("Activating the Asset");
      }
      getCubeCreationServiceHelper().activateAsset(targetAsset);
      if (logger.isDebugEnabled()) {
         logger.debug("Applying Asset Permissions");
      }
      getCubeCreationServiceHelper().applyAssetPermissions(application.getId(), userId, sourceAsset, targetAsset);

   }

   /**
    * This method create joins for newly built cube asset
    * 
    * @param cubeCreationOutputInfo
    * @throws JoinException
    */
   private void createJoins (CubeCreationOutputInfo cubeCreationOutputInfo) throws JoinException {
      CubeCreationPopulatedContext cubeCreationPopulatedContext = cubeCreationOutputInfo.getCubeCreationInputInfo()
               .getCubeCreationPopulatedContext();
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();
      CubeFactTableStructure cubeFactTableStructure = cubeCreationOutputInfo.getCubeFactTableStructure();
      CubeLookupTableStructure cubeLookupTableStructure = cubeCreationOutputInfo.getCubeLookupTableStructure();
      String factTableName = cubeFactTableStructure.getTableName();

      // assumption here is combined lookup columns are in simple, range and stat lookup order
      // lookup tables are also in same order
      List<Join> cubeAssetDirectJoins = new ArrayList<Join>();
      int index = 0;
      for (QueryColumn queryColumn : cubeFactTableStructure.getCombinedLookupColumns()) {
         // fact table column name
         String factTableColumnName = queryColumn.getColumnName();
         // corresponding lookup table for this dimension column
         CubeLookupTableStructureInfo cubeLookupTableStructureInfo = cubeLookupTableStructure.getCombinedTables().get(
                  index++);
         // lookup table name
         String lookupTableName = cubeLookupTableStructureInfo.getTableName();
         // lookup table value column
         String lookupTableValueColumnName = cubeLookupTableStructureInfo.getLookupValueColumn().getColumnName();
         // build join between fact table and lookup table in picture.
         Join join = new Join();
         join.setSourceTableName(factTableName);
         join.setDestTableName(lookupTableName);
         join.setAsset(targetAsset);
         // create joinDefintions for join
         List<JoinDefinition> joinDefinitions = new ArrayList<JoinDefinition>();
         JoinDefinition joinDefinition = new JoinDefinition();
         joinDefinition.setSourceTableName(factTableName);
         joinDefinition.setDestTableName(lookupTableName);
         joinDefinition.setAsset(targetAsset);
         joinDefinition.setSourceColumnName(factTableColumnName);
         joinDefinition.setDestColumnName(lookupTableValueColumnName);
         joinDefinition.setType(JoinType.INNER);
         joinDefinitions.add(joinDefinition);
         join.setJoinDefinitions(new HashSet<JoinDefinition>(joinDefinitions));
         cubeAssetDirectJoins.add(join);
      }
      // creating the joins.
      getJoinService().createJoins(targetAsset.getId(), cubeAssetDirectJoins);
   }

   /**
    * This method copies the information from source asset entities to target asset entities.
    * 
    * @param cubeCreationOutputInfo
    * @throws AnswersCatalogException
    * @throws SDXException
    */
   private void copyAssetTableColumMemberInfo (CubeCreationOutputInfo cubeCreationOutputInfo)
            throws AnswersCatalogException, SDXException {
      CubeCreationPopulatedContext cubeCreationPopulatedContext = cubeCreationOutputInfo.getCubeCreationInputInfo()
               .getCubeCreationPopulatedContext();

      Asset sourceAsset = cubeCreationPopulatedContext.getSourceAsset();
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();

      // update asset properties.
      AssetCreationInfo assetCreationInfo = getCubeCreationServiceHelper().buildAssetCreationInfo(
               cubeCreationOutputInfo);
      getCubeCreationServiceHelper().updateAssetProperties(sourceAsset, targetAsset, assetCreationInfo);

      // processing fact table.

      // update fact table of cube for system level metrics. As there is only one fact table in cube, we will mark this
      // table as eligible for default metric
      CubeFactTableStructure cubeFactTableStructure = cubeCreationOutputInfo.getCubeFactTableStructure();
      Tabl cubeFactTable = getSdxRetrievalService().getAssetTable(targetAsset.getId(),
               cubeFactTableStructure.getTableName());
      getCubeCreationServiceHelper().updateCubeFactTableProperties(cubeFactTable);

      // for all the columns of fact table except frequency measure and stat column - we will copy information from
      // source column to target column.
      // frequency measure can be a lookup in source asset, we dont want to copy that information and stat doesn't even
      // exist in source asset
      for (CubeSourceColumnMapping cubeSourceColumnMapping : cubeFactTableStructure
               .getCombinedColumnsExceptFrequencyMeasuresAndStat()) {
         copyColumnInformation(cubeSourceColumnMapping.getSourceConceptColumnMapping().getColumn(),
                  cubeSourceColumnMapping.getQueryColumn(), targetAsset, cubeFactTable);
      }

      // stat column of fact table, we put the kdx type to be simple_lookup
      Colum statTargetColumn = getSdxRetrievalService().getAssetTableColum(targetAsset.getId(),
               cubeFactTable.getName(), cubeFactTableStructure.getStatColumn().getColumnName());
      getCubeCreationServiceHelper().updateCubeColumnKDXType(targetAsset.getId(), cubeFactTable.getId(),
               statTargetColumn, ColumnType.SIMPLE_LOOKUP);

      // for all frequency measure columns of fact table, we put the kdx type to be ID
      for (CubeSourceColumnMapping cubeSourceColumnMapping : cubeFactTableStructure.getFrequencyMeasureColumns()) {
         Colum targetColumn = getSdxRetrievalService().getAssetTableColum(targetAsset.getId(), cubeFactTable.getName(),
                  cubeSourceColumnMapping.getQueryColumn().getColumnName());
         getCubeCreationServiceHelper().updateCubeColumnKDXType(targetAsset.getId(), cubeFactTable.getId(),
                  targetColumn, ColumnType.ID);
      }

      // for all lookup columns of fact table, we put the kdx type to be DIMENSION
      for (CubeSourceColumnMapping cubeSourceColumnMapping : cubeFactTableStructure
               .getCombinedLookupColumnsExceptStat()) {
         Colum targetColumn = getSdxRetrievalService().getAssetTableColum(targetAsset.getId(), cubeFactTable.getName(),
                  cubeSourceColumnMapping.getQueryColumn().getColumnName());
         getCubeCreationServiceHelper().updateCubeColumnKDXType(targetAsset.getId(), cubeFactTable.getId(),
                  targetColumn, ColumnType.DIMENSION);
      }

      // processing lookup tables.
      CubeLookupTableStructure cubeLookupTableStructure = cubeCreationOutputInfo.getCubeLookupTableStructure();
      // for all the tables except stat as stat table doesn't exist on source asset - we will copy table level column
      // level and member level information
      for (CubeLookupTableStructureInfo cubeLookupTableStructureInfo : cubeLookupTableStructure
               .getCombinedTablesExceptStat()) {
         ConceptColumnMapping sourceConceptColumnMapping = cubeLookupTableStructureInfo.getSourceConceptColumnMapping();
         // if corresponding source table exists in swi, e.g simple lookup table was there in source, or virtual lookup
         // table got created from some fact table
         // range lookup table exists in source also, but dynamic range lookup got generated from measure at run time,
         // in that case we cannot copy information as range lookup source table doesn't exist
         if (cubeLookupTableStructureInfo.isParentTableExistsInSWI()) {
            Tabl sourceTable = sourceConceptColumnMapping.getTabl();
            Tabl targetTable = getSdxRetrievalService().getAssetTable(targetAsset.getId(),
                     cubeLookupTableStructureInfo.getTableName());
            // copy table properties
            getCubeCreationServiceHelper().copyTableProperties(sourceTable, targetTable);
            QueryColumn lookupValueColumn = cubeLookupTableStructureInfo.getLookupValueColumn();
            // copy lookup value column information
            copyColumnInformation(sourceConceptColumnMapping.getColumn(), lookupValueColumn, targetAsset, targetTable);
            List<Membr> sourceMembers = getSdxRetrievalService().getColumnMembers(
                     sourceConceptColumnMapping.getColumn());
            Colum targetColumn = getSdxRetrievalService().getAssetTableColum(targetAsset.getId(),
                     targetTable.getName(), lookupValueColumn.getColumnName());
            for (Membr sourceMember : sourceMembers) {
               Membr targetMember = getSdxRetrievalService().getMemberByLookupValue(targetAsset, targetTable,
                        targetColumn, sourceMember.getLookupValue());
               if (targetMember != null) {
                  // copy member information
                  getCubeCreationServiceHelper().copyMemberProperties(sourceMember, targetMember);
               }
            }
         }
      }
   }

   /**
    * This method extracts the asset as cube in swi.
    * 
    * @param cubeCreationOutputInfo
    * @throws AnswersCatalogException
    */
   private boolean createAssetTableColumns (CubeCreationOutputInfo cubeCreationOutputInfo)
            throws AnswersCatalogException {
      CubeCreationPopulatedContext cubeCreationPopulatedContext = cubeCreationOutputInfo.getCubeCreationInputInfo()
               .getCubeCreationPopulatedContext();
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();
      Application application = cubeCreationPopulatedContext.getApplication();
      targetAsset.setApplication(application);
      List<Tabl> tablesList = new ArrayList<Tabl>();
      CubeFactTableStructure cubeFactTableStructure = cubeCreationOutputInfo.getCubeFactTableStructure();
      CubeLookupTableStructure cubeLookupTableStructure = cubeCreationOutputInfo.getCubeLookupTableStructure();
      String factTableName = cubeFactTableStructure.getTableName();
      Tabl factTable = new Tabl();
      factTable.setOwner(targetAsset.getDataSource().getOwner());
      factTable.setName(factTableName);
      factTable.setDisplayName(factTableName);
      factTable.setLookupType(LookupType.None);
      // added fact table to list of tables to be extracted
      tablesList.add(factTable);
      // all the lookup tables be added to the list of tables to be extracted
      for (CubeLookupTableStructureInfo cubeLookupTableStructureInfo : cubeLookupTableStructure.getCombinedTables()) {
         tablesList.add(prepareLookupTable(cubeLookupTableStructureInfo, targetAsset.getDataSource()));
      }
      return getCubeCreationServiceHelper().extractAsset(targetAsset, tablesList);
   }

   /**
    * This method copies the information from source asset column to target column.
    * 
    * @param sourceColumn
    * @param targetQueryColumn
    * @param targetAsset
    * @param targetTable
    * @throws SDXException
    * @throws AnswersCatalogException
    */
   private void copyColumnInformation (Colum sourceColumn, QueryColumn targetQueryColumn, Asset targetAsset,
            Tabl targetTable) throws SDXException, AnswersCatalogException {
      Colum targetColumn = getSdxRetrievalService().getAssetTableColum(targetAsset.getId(), targetTable.getName(),
               targetQueryColumn.getColumnName());
      getCubeCreationServiceHelper().copyColumnProperties(sourceColumn, targetAsset, targetTable, targetColumn);
   }

   /**
    * This method prepares the TABL object which will be used by asset extraction process.
    * 
    * @param cubeLookupTableStructureInfo
    * @param dataSource 
    * @return
    */
   private Tabl prepareLookupTable (CubeLookupTableStructureInfo cubeLookupTableStructureInfo, DataSource dataSource) {
      String lookupTableName = cubeLookupTableStructureInfo.getTableName();
      LookupType lookupType = cubeLookupTableStructureInfo.getLookupType();
      Tabl lookupTable = new Tabl();
      lookupTable.setOwner(dataSource.getOwner());
      lookupTable.setName(lookupTableName);
      lookupTable.setDisplayName(lookupTableName);
      lookupTable.setLookupType(lookupType);
      lookupTable.setLookupValueColumn(cubeLookupTableStructureInfo.getLookupValueColumn().getColumnName());
      lookupTable.setLookupDescColumn(cubeLookupTableStructureInfo.getLookupDescColumn().getColumnName());
      if (LookupType.RANGE_LOOKUP.equals(lookupType)) {
         lookupTable.setLowerLimitColumn(cubeLookupTableStructureInfo.getLowerLimitColumn().getColumnName());
         lookupTable.setUpperLimitColumn(cubeLookupTableStructureInfo.getUpperLimitColumn().getColumnName());
      }
      return lookupTable;
   }

   public CubeCreationServiceHelper getCubeCreationServiceHelper () {
      return cubeCreationServiceHelper;
   }

   public void setCubeCreationServiceHelper (CubeCreationServiceHelper cubeCreationServiceHelper) {
      this.cubeCreationServiceHelper = cubeCreationServiceHelper;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IJoinService getJoinService () {
      return joinService;
   }

   public void setJoinService (IJoinService joinService) {
      this.joinService = joinService;
   }

   public IMappingManagementService getMappingManagementService () {
      return mappingManagementService;
   }

   public void setMappingManagementService (IMappingManagementService mappingManagementService) {
      this.mappingManagementService = mappingManagementService;
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public ISDXDeletionService getSdxDeletionService () {
      return sdxDeletionService;
   }

   public void setSdxDeletionService (ISDXDeletionService sdxDeletionService) {
      this.sdxDeletionService = sdxDeletionService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

}
