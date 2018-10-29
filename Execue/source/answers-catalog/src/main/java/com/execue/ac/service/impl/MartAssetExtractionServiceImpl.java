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
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.ac.bean.MartConfigurationContext;
import com.execue.ac.bean.MartCreationInputInfo;
import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.bean.MartCreationPopulatedContext;
import com.execue.ac.bean.MartWarehouseTableStructure;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.MartAssetExtractionException;
import com.execue.ac.helper.MartCreationServiceHelper;
import com.execue.ac.service.IMartAssetExtractionService;
import com.execue.ac.service.IMartCreationConstants;
import com.execue.ac.service.IMartOperationalConstants;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetCreationInfo;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Join;
import com.execue.core.common.bean.entity.JoinDefinition;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.PrimaryMappingType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.swi.exception.JoinException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IJoinService;
import com.execue.swi.service.IMappingManagementService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * This service represents the step7 of mart creation process. Extraction of asset and carry the source asset
 * information to parent asset happens here.
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/01/2011
 */
public class MartAssetExtractionServiceImpl implements IMartAssetExtractionService, IMartCreationConstants,
         IMartOperationalConstants {

   private ISDXRetrievalService      sdxRetrievalService;
   private IJoinService              joinService;
   private IMappingRetrievalService  mappingRetrievalService;
   private IMappingManagementService mappingManagementService;
   private IBaseKDXRetrievalService  baseKDXRetrievalService;
   private MartCreationServiceHelper martCreationServiceHelper;
   private static final Logger       logger = Logger.getLogger(MartAssetExtractionServiceImpl.class);

   /**
    * This method extracts the asset from the data mart created. The mart creation output info contains all the
    * information required to extract the asset as data mart in swi
    * 
    * @param martCreationOutputInfo
    * @throws MartAssetExtractionException
    */
   public boolean extractAsset (MartCreationOutputInfo martCreationOutputInfo) throws MartAssetExtractionException {
      boolean assetExtractionSuccess = true;
      MartCreationInputInfo martCreationInputInfo = martCreationOutputInfo.getMartCreationInputInfo();
      MartCreationContext martCreationContext = martCreationInputInfo.getMartCreationContext();
      JobRequest jobRequest = martCreationContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         MartCreationPopulatedContext martCreationPopulatedContext = martCreationInputInfo
                  .getMartCreationPopulatedContext();
         MartConfigurationContext martConfigurationContext = martCreationInputInfo.getMartConfigurationContext();
         List<MartWarehouseTableStructure> warehouseTableStructureList = martCreationOutputInfo
                  .getWarehouseTableStructure();

         // Building the map between source table and corresponding mart table name. As mart structure is same as
         // source, there will always be one to one association of parent to mart table as well as columns.
         Map<String, String> sourceMartTableNameMap = populateSourceMartTableNameMap(warehouseTableStructureList);

         // creating asset table and columns into swi.
         if (logger.isDebugEnabled()) {
            logger.debug("Creating Asset entities into swi.");
         }
         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  CREATE_ASSET_TABLE_COLUMN_MEMBER);
         if (createAssetTableColumns(martCreationPopulatedContext, warehouseTableStructureList)) {
            getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

            // creating virtual tables into swi from source.
            if (logger.isDebugEnabled()) {
               logger.debug("Creating Virtual Tables.");
            }
            jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                     CREATE_VIRTUAL_TABLES);
            createVirtualTables(martCreationPopulatedContext, warehouseTableStructureList, sourceMartTableNameMap);
            getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

            // copy the asset table column member information from source to mart asset
            if (logger.isDebugEnabled()) {
               logger.debug("Copy Asset entities info from source asset.");
            }
            jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                     COPY_ASSET_TABLE_COLUMN_MEMBER_INFO);
            copyAssetTableColumMemberInfo(martCreationOutputInfo, warehouseTableStructureList);
            getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

            // creating the joins(copying the joins from source asset to mart asset)
            if (logger.isDebugEnabled()) {
               logger.debug("Copying the Joins from source Asset.");
            }
            jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest, CREATE_JOINS);
            createJoins(martCreationPopulatedContext, sourceMartTableNameMap);
            getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

            // creating mappings(copying the mapping and grain from source asset to mart asset)
            if (logger.isDebugEnabled()) {
               logger.debug("Creating Mappings and Grain for Mart asset.");
            }
            jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                     CREATE_MAPPINGS_AND_GRAIN);
            createMappingsAndGrain(martCreationPopulatedContext, martConfigurationContext, warehouseTableStructureList);
            getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

            // add miscellaneous(copy miscellaneous information from source asset to mart).
            if (logger.isDebugEnabled()) {
               logger.debug("Asset Readiness and Activation");
            }
            jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                     ASSET_READINESS_AND_ACTIVATION);
            prepareDataMartInSWI(martCreationPopulatedContext);
            getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
         } else {
            assetExtractionSuccess = false;
         }
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new MartAssetExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_ASSET_EXTRACTION_EXCEPTION_CODE, e);
      } catch (SDXException e) {
         exception = e;
         throw new MartAssetExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_ASSET_EXTRACTION_EXCEPTION_CODE, e);
      } catch (JoinException e) {
         exception = e;
         throw new MartAssetExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_ASSET_EXTRACTION_EXCEPTION_CODE, e);
      } catch (MappingException e) {
         exception = e;
         throw new MartAssetExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_ASSET_EXTRACTION_EXCEPTION_CODE, e);
      } catch (KDXException e) {
         exception = e;
         throw new MartAssetExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_ASSET_EXTRACTION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getMartCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new MartAssetExtractionException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return assetExtractionSuccess;
   }

   /**
    * This method prepares the newly built asset for answering the questions. 1. It corrects the mappings 2. It creates
    * default metrics. 3. It creates default dynamic values 4. It activates the asset 5. It applies the permissions on
    * the asset
    * 
    * @param martCreationPopulatedContext
    * @throws AnswersCatalogException
    */
   private void prepareDataMartInSWI (MartCreationPopulatedContext martCreationPopulatedContext)
            throws AnswersCatalogException {
      Asset sourceAsset = martCreationPopulatedContext.getSourceAsset();
      Asset targetAsset = martCreationPopulatedContext.getTargetAsset();
      Application application = martCreationPopulatedContext.getApplication();
      Long modelId = martCreationPopulatedContext.getModelId();
      Long userId = martCreationPopulatedContext.getUserId();
      if (logger.isDebugEnabled()) {
         logger.debug("Correct Mappings.");
      }
      martCreationServiceHelper.correctMappings(application.getId(), targetAsset);
      if (logger.isDebugEnabled()) {
         logger.debug("Creating Default Metrics");
      }
      martCreationServiceHelper.createDefaultMetrics(modelId, sourceAsset, targetAsset, AssetType.Mart);
      if (logger.isDebugEnabled()) {
         logger.debug("Creating Default Dynamic Values");
      }
      martCreationServiceHelper.createDefaultDynamicValues(modelId, sourceAsset, targetAsset);
      if (logger.isDebugEnabled()) {
         logger.debug("Activating the Asset");
      }
      martCreationServiceHelper.activateAsset(targetAsset);
      if (logger.isDebugEnabled()) {
         logger.debug("Applying Asset Permissions");
      }

      getMartCreationServiceHelper().applyAssetPermissions(application.getId(), userId, sourceAsset, targetAsset);
   }

   /**
    * This method creates(copies) virtual tables from source asset into mart asset.
    * 
    * @param martCreationPopulatedContext
    * @param warehouseTableStructureList
    * @param sourceMartTableNameMap
    * @throws AnswersCatalogException
    * @throws SDXException
    */
   private void createVirtualTables (MartCreationPopulatedContext martCreationPopulatedContext,
            List<MartWarehouseTableStructure> warehouseTableStructureList, Map<String, String> sourceMartTableNameMap)
            throws AnswersCatalogException, SDXException {
      // 1. find the actual name of the virtual table in source
      // 2. load the actual table.
      // 3. from the actual table name in source, find the table in mart and populate it
      // 4. Step 3 table is my final parent table for mart virtual table.
      // 5. Need to prepare virtual table
      // 6. Clone virtual table from source
      // 7. set new name
      // 8. value and desc column population - it needs to be the value colum from parent table
      // 9. how to find value column from parent is question
      // 10.Go to parent of virtual table in mart, get the corresponding Mart table structure
      // 11. From this object, find the matching value column
      // 12. get the corresponding column name for mart parent table.
      // 13. use this name for lookup value and desc column.
      Asset targetAsset = martCreationPopulatedContext.getTargetAsset();
      for (MartWarehouseTableStructure warehouseTableStructure : warehouseTableStructureList) {
         Tabl sourceTable = warehouseTableStructure.getSourceTable();
         if (CheckType.YES.equals(sourceTable.getVirtual())) {
            String actualTableNameMart = sourceMartTableNameMap.get(sourceTable.getActualName());
            // mart based parent table.
            Tabl actualFactTableMart = getSdxRetrievalService().getAssetTable(targetAsset.getId(), actualTableNameMart);
            // table structure of parent tables.
            MartWarehouseTableStructure actualTableMartStructure = findCorrespondingTableStructure(
                     warehouseTableStructureList, actualFactTableMart.getName());
            // clone the virtual table of parent.
            Tabl clonedVirtualTable = ExecueBeanCloneUtil.cloneTable(sourceTable);
            clonedVirtualTable.setName(sourceMartTableNameMap.get(sourceTable.getName()));
            // get the corresponding value column from parent table.
            Colum valueColumnFromFactSource = ExecueBeanUtil.findCorrespondingColumn(actualTableMartStructure
                     .getSourceColumns(), clonedVirtualTable.getLookupValueColumn());
            String valueColumnFromFactMart = actualTableMartStructure.getSourceMartColumnMap().get(
                     valueColumnFromFactSource.getName()).getColumnName();
            clonedVirtualTable.setLookupValueColumn(valueColumnFromFactMart);
            clonedVirtualTable.setLookupDescColumn(valueColumnFromFactMart);
            TableInfo virtualTableInfo = getMartCreationServiceHelper().createVirtualTable(targetAsset,
                     actualFactTableMart, clonedVirtualTable);
            // sync the colum map inside mart table structure.
            Tabl table = virtualTableInfo.getTable();
            List<Colum> columns = virtualTableInfo.getColumns();
            Colum finalLookupValueColumn = ExecueBeanUtil
                     .findCorrespondingColumn(columns, table.getLookupValueColumn());
            QueryColumn finalLookupValueQueryColumn = ExecueBeanManagementUtil
                     .prepareQueryColumn(finalLookupValueColumn);
            Colum finalLookupDescColumn = ExecueBeanUtil.findCorrespondingColumn(columns, table.getLookupDescColumn());
            QueryColumn finalLookupDescQueryColumn = ExecueBeanManagementUtil.prepareQueryColumn(finalLookupDescColumn);
            warehouseTableStructure.getSourceMartColumnMap().clear();
            warehouseTableStructure.getMartTableColumns().clear();
            warehouseTableStructure.getMartTableColumns().add(finalLookupValueQueryColumn);
            warehouseTableStructure.getMartTableColumns().add(finalLookupDescQueryColumn);
            warehouseTableStructure.getSourceMartColumnMap().put(sourceTable.getLookupValueColumn(),
                     finalLookupValueQueryColumn);
            warehouseTableStructure.getSourceMartColumnMap().put(sourceTable.getLookupDescColumn(),
                     finalLookupDescQueryColumn);
         }
      }
   }

   /**
    * For given table name(of newly built mart), find the corresponding MartWarehouseTableStructure.
    * 
    * @param warehouseTableStructureList
    * @param martTableName
    * @return matchedWarehouseTableStructure
    */
   private MartWarehouseTableStructure findCorrespondingTableStructure (
            List<MartWarehouseTableStructure> warehouseTableStructureList, String martTableName) {
      MartWarehouseTableStructure matchedWarehouseTableStructure = null;
      for (MartWarehouseTableStructure warehouseTableStructure : warehouseTableStructureList) {
         if (warehouseTableStructure.getMartTableName().equalsIgnoreCase(martTableName)) {
            matchedWarehouseTableStructure = warehouseTableStructure;
            break;
         }
      }
      return matchedWarehouseTableStructure;
   }

   /**
    * This method creates(copies) joins from source asset to mart asset.
    * 
    * @param martCreationPopulatedContext
    * @param sourceMartTableNameMap
    * @throws JoinException
    */
   private void createJoins (MartCreationPopulatedContext martCreationPopulatedContext,
            Map<String, String> sourceMartTableNameMap) throws JoinException {
      Asset sourceAsset = martCreationPopulatedContext.getSourceAsset();
      Asset targetAsset = martCreationPopulatedContext.getTargetAsset();
      List<Join> martAssetJoins = new ArrayList<Join>();
      // get all the joins from source.(direct + indirect)
      List<Join> sourceAssetJoins = getJoinService().getAllAssetJoins(sourceAsset.getId());
      // for each source join
      for (Join join : sourceAssetJoins) {
         // get the existing join definitions
         List<JoinDefinition> existingJoinDefinitions = getJoinService().getAllExistingJoinDefinitions(
                  sourceAsset.getId(), join.getSourceTableName(), join.getDestTableName());
         // prepare mart join by replacing asset, table.
         Join martAssetJoin = AnswersCatalogUtil.prepareJoin(join, targetAsset, sourceMartTableNameMap);
         // prepare mart join by replacing asset, table and columns.
         Set<JoinDefinition> martAssetJoinDefinitions = AnswersCatalogUtil.prepareJoinDefinition(
                  existingJoinDefinitions, targetAsset, sourceMartTableNameMap);
         martAssetJoin.setJoinDefinitions(martAssetJoinDefinitions);
         martAssetJoins.add(martAssetJoin);
      }
      // creating the joins.
      getJoinService().createJoins(targetAsset.getId(), martAssetJoins);
   }

   /**
    * This method creates(copies) the mappings and grain from source asset to mart asset.
    * 
    * @param martCreationPopulatedContext
    * @param martConfigurationContext
    * @param warehouseTableStructureList
    * @throws MappingException
    * @throws KDXException
    * @throws SDXException
    */
   private void createMappingsAndGrain (MartCreationPopulatedContext martCreationPopulatedContext,
            MartConfigurationContext martConfigurationContext,
            List<MartWarehouseTableStructure> warehouseTableStructureList) throws MappingException, KDXException,
            SDXException {
      Asset sourceAsset = martCreationPopulatedContext.getSourceAsset();
      Asset targetAsset = martCreationPopulatedContext.getTargetAsset();
      Application application = martCreationPopulatedContext.getApplication();
      Long modelId = martCreationPopulatedContext.getModelId();
      String scalingFactorConceptName = martConfigurationContext.getScalingFactorConceptName();
      // for each table , create column level mappings and for each lookup table create member level mappings also.
      for (MartWarehouseTableStructure warehouseTableStructure : warehouseTableStructureList) {
         Map<String, QueryColumn> sourceMartColumnMap = warehouseTableStructure.getSourceMartColumnMap();
         Tabl sourceTable = warehouseTableStructure.getSourceTable();
         String martTableName = warehouseTableStructure.getMartTableName();
         List<Colum> sourceColumns = warehouseTableStructure.getSourceColumns();

         // column level mappings
         List<Mapping> columnLevelMappings = new ArrayList<Mapping>();
         for (Colum colum : sourceColumns) {
            // get the existing mapping of the column from source asset
            Mapping sourceMapping = getMappingRetrievalService().getPrimaryMapping(modelId, sourceAsset.getId(),
                     sourceTable.getId(), colum.getId());
            if (sourceMapping != null) {
               // clone the mapping and set the new AED which is mart based into it.
               Mapping clonedMapping = ExecueBeanCloneUtil.cloneMapping(sourceMapping);
               AssetEntityDefinition targetAED = getSdxRetrievalService().getUnPopulatedAssetEntityDefinitionByNames(
                        application.getName(), targetAsset.getName(), martTableName,
                        sourceMartColumnMap.get(colum.getName()).getColumnName(), null);
               clonedMapping.setAssetEntityDefinition(targetAED);
               columnLevelMappings.add(clonedMapping);
            }
         }

         // As population table will have sFactor column, we need to map that column to SCALINGFACTOR concept.
         if (warehouseTableStructure.isPopulationTable()) {
            BusinessEntityDefinition scalingFactorBED = getBaseKDXRetrievalService().getConceptBEDByName(
                     scalingFactorConceptName);
            AssetEntityDefinition sFactorAED = getSdxRetrievalService().getUnPopulatedAssetEntityDefinitionByNames(
                     application.getName(), targetAsset.getName(), martTableName,
                     warehouseTableStructure.getPopulationSfactorColumn().getColumnName(), null);
            Mapping scalingFactorMapping = new Mapping();
            scalingFactorMapping.setAssetEntityDefinition(sFactorAED);
            scalingFactorMapping.setBusinessEntityDefinition(scalingFactorBED);
            scalingFactorMapping.setPrimary(PrimaryMappingType.PRIMARY);
            columnLevelMappings.add(scalingFactorMapping);
         }

         // creating column level mappings.
         getMappingManagementService().createMappings(columnLevelMappings);

         // if table is lookup, we need to create member mappings also.
         if (!LookupType.None.equals(sourceTable.getLookupType())) {
            // get the value column.
            Colum lookupValueColumn = ExecueBeanUtil.findCorrespondingColumn(sourceColumns, sourceTable
                     .getLookupValueColumn());
            // total count of members from swi.
            Long membersCount = getSdxRetrievalService().getTotalMembersCountOfColumn(lookupValueColumn);
            // process batch by batch
            Long pageSize = MEMBER_MAPPING_BATCH_SIZE;
            Long pageCount = new Double(Math.ceil(membersCount / new Double(pageSize))).longValue();
            for (Long pageNumber = 1L; pageNumber <= pageCount; pageNumber++) {
               List<Mapping> memberLevelMappings = new ArrayList<Mapping>();
               List<Membr> batchMembers = getSdxRetrievalService().getColumnMembersByPage(lookupValueColumn,
                        pageNumber, pageSize);
               // for each member, get the mapping from source asset
               for (Membr member : batchMembers) {
                  Mapping sourceMapping = getMappingRetrievalService().getPrimaryMapping(modelId, sourceAsset.getId(),
                           sourceTable.getId(), lookupValueColumn.getId(), member.getId());
                  if (sourceMapping != null) {
                     // clone the mapping and set the new aed of mart asset into it.
                     Mapping clonedMapping = ExecueBeanCloneUtil.cloneMapping(sourceMapping);
                     AssetEntityDefinition targetAED = getSdxRetrievalService()
                              .getUnPopulatedAssetEntityDefinitionByNames(application.getName(), targetAsset.getName(),
                                       martTableName,
                                       sourceMartColumnMap.get(lookupValueColumn.getName()).getColumnName(),
                                       member.getLookupValue());
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
   }

   /**
    * This method copies the asset, table, colum and member entities properties from source asset to mart asset.
    * 
    * @param martCreationPopulatedContext
    * @param warehouseTableStructureList
    * @throws SDXException
    * @throws AnswersCatalogException
    */
   private void copyAssetTableColumMemberInfo (MartCreationOutputInfo martCreationOutputInfo,
            List<MartWarehouseTableStructure> warehouseTableStructureList) throws SDXException, AnswersCatalogException {
      Asset sourceAsset = martCreationOutputInfo.getMartCreationInputInfo().getMartCreationPopulatedContext()
               .getSourceAsset();
      Asset targetAsset = martCreationOutputInfo.getMartCreationInputInfo().getMartCreationPopulatedContext()
               .getTargetAsset();

      // update asset properties.
      AssetCreationInfo assetCreationInfo = getMartCreationServiceHelper().buildAssetCreationInfo(
               martCreationOutputInfo);
      getMartCreationServiceHelper().updateAssetProperties(sourceAsset, targetAsset, assetCreationInfo);

      for (MartWarehouseTableStructure warehouseTableStructure : warehouseTableStructureList) {
         Map<String, QueryColumn> sourceMartColumnMap = warehouseTableStructure.getSourceMartColumnMap();
         Tabl sourceTable = warehouseTableStructure.getSourceTable();
         String martTableName = warehouseTableStructure.getMartTableName();
         List<Colum> sourceColumns = warehouseTableStructure.getSourceColumns();
         Tabl targetTable = getSdxRetrievalService().getAssetTable(targetAsset.getId(), martTableName);
         // copy table properties.
         getMartCreationServiceHelper().copyTableProperties(sourceTable, targetTable);
         List<Colum> targetColumns = new ArrayList<Colum>();
         for (Colum colum : sourceColumns) {
            String targetColumnName = sourceMartColumnMap.get(colum.getName()).getColumnName();
            Colum targetColumn = getSdxRetrievalService().getAssetTableColum(targetAsset.getId(), martTableName,
                     targetColumnName);
            targetColumns.add(targetColumn);
            // copy column properties
            getMartCreationServiceHelper().copyColumnProperties(colum, targetAsset, targetTable, targetColumn);
         }
         Colum sourceLookupValueColumn = ExecueBeanUtil.findCorrespondingColumn(sourceColumns, sourceTable
                  .getLookupValueColumn());
         List<Membr> sourceMembers = getSdxRetrievalService().getColumnMembers(sourceLookupValueColumn);
         Colum targetLookupValueColumn = ExecueBeanUtil.findCorrespondingColumn(targetColumns, targetTable
                  .getLookupValueColumn());

         for (Membr sourceMember : sourceMembers) {
            Membr targetMember = getSdxRetrievalService().getMemberByLookupValue(targetAsset, targetTable,
                     targetLookupValueColumn, sourceMember.getLookupValue());
            getMartCreationServiceHelper().copyMemberProperties(sourceMember, targetMember, sourceTable.getVirtual());
         }
      }
   }

   /**
    * This method perform the asset extraction for the newly built mart into swi.
    * 
    * @param martCreationPopulatedContext
    * @param warehouseTableStructureList
    * @throws AnswersCatalogException
    */
   private boolean createAssetTableColumns (MartCreationPopulatedContext martCreationPopulatedContext,
            List<MartWarehouseTableStructure> warehouseTableStructureList) throws AnswersCatalogException {
      Asset targetAsset = martCreationPopulatedContext.getTargetAsset();
      Application application = martCreationPopulatedContext.getApplication();
      targetAsset.setApplication(application);
      List<Tabl> tablesList = new ArrayList<Tabl>();
      // for each of the non-virtual table, we create TABL object to be extracted into swi.
      for (MartWarehouseTableStructure warehouseTableStructure : warehouseTableStructureList) {
         Tabl sourceTable = warehouseTableStructure.getSourceTable();
         if (CheckType.NO.equals(sourceTable.getVirtual())) {
            String martTableName = warehouseTableStructure.getMartTableName();
            List<Colum> sourceColumns = warehouseTableStructure.getSourceColumns();
            Map<String, QueryColumn> sourceMartColumnMap = warehouseTableStructure.getSourceMartColumnMap();
            Tabl tabl = new Tabl();
            tabl.setOwner(targetAsset.getDataSource().getOwner());
            tabl.setName(martTableName);
            tabl.setDisplayName(sourceTable.getDisplayName());
            tabl.setLookupType(LookupType.None);
            if (LookupType.SIMPLE_LOOKUP.equals(sourceTable.getLookupType())) {
               Colum sourceValueColumn = ExecueBeanUtil.findCorrespondingColumn(sourceColumns, sourceTable
                        .getLookupValueColumn());
               Colum sourceDescColumn = ExecueBeanUtil.findCorrespondingColumn(sourceColumns, sourceTable
                        .getLookupDescColumn());
               tabl.setLookupType(LookupType.SIMPLE_LOOKUP);
               tabl.setLookupValueColumn(sourceMartColumnMap.get(sourceValueColumn.getName()).getColumnName());
               tabl.setLookupDescColumn(sourceMartColumnMap.get(sourceDescColumn.getName()).getColumnName());
            }
            tablesList.add(tabl);
         }
      }
      return getMartCreationServiceHelper().extractAsset(targetAsset, tablesList);
   }

   /**
    * Building the map between source table and corresponding mart table name. As mart structure is same as source,
    * there will always be one to one association of parent to mart table as well as columns.
    * 
    * @param warehouseTableStructureList
    * @return sourceMartTableNameMap
    */
   private Map<String, String> populateSourceMartTableNameMap (
            List<MartWarehouseTableStructure> warehouseTableStructureList) {
      Map<String, String> sourceMartTableNameMap = new HashMap<String, String>();
      for (MartWarehouseTableStructure warehouseTableStructure : warehouseTableStructureList) {
         sourceMartTableNameMap.put(warehouseTableStructure.getSourceTable().getName(), warehouseTableStructure
                  .getMartTableName());
      }
      return sourceMartTableNameMap;
   }

   public MartCreationServiceHelper getMartCreationServiceHelper () {
      return martCreationServiceHelper;
   }

   public void setMartCreationServiceHelper (MartCreationServiceHelper martCreationServiceHelper) {
      this.martCreationServiceHelper = martCreationServiceHelper;
   }

   public IJoinService getJoinService () {
      return joinService;
   }

   public void setJoinService (IJoinService joinService) {
      this.joinService = joinService;
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

}
