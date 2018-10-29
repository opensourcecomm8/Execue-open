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

import org.apache.log4j.Logger;

import com.execue.ac.bean.AnswersCatalogConfigurationContext;
import com.execue.ac.bean.ConceptColumnMapping;
import com.execue.ac.bean.CubeConfigurationContext;
import com.execue.ac.bean.CubeCreationInputInfo;
import com.execue.ac.bean.CubeCreationOutputInfo;
import com.execue.ac.bean.CubeCreationPopulatedContext;
import com.execue.ac.bean.CubeLookupTableStructure;
import com.execue.ac.bean.CubeLookupTableStructureInfo;
import com.execue.ac.bean.CubeUpdationDimensionInfo;
import com.execue.ac.bean.CubeUpdationInputInfo;
import com.execue.ac.bean.CubeUpdationOutputInfo;
import com.execue.ac.bean.CubeUpdationPopulatedContext;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.CubeLookupPopulationException;
import com.execue.ac.helper.CubeCreationServiceHelper;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.ac.service.IAnswersCatalogSQLQueryGenerationService;
import com.execue.ac.service.ICubeCreationConstants;
import com.execue.ac.service.ICubeLookupPopulationService;
import com.execue.ac.service.ICubeOperationalConstants;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

/**
 * This service is used for lookup table preparation.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class CubeLookupPopulationServiceImpl implements ICubeLookupPopulationService, ICubeCreationConstants,
         ICubeOperationalConstants {

   private IAnswersCatalogDataAccessService         answersCatalogDataAccessService;
   private IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService;
   private CubeCreationServiceHelper                cubeCreationServiceHelper;
   private ISDXRetrievalService                     sdxRetrievalService;
   private QueryGenerationUtilServiceFactory        queryGenerationUtilServiceFactory;
   private static final Logger                      logger = Logger.getLogger(CubeLookupPopulationServiceImpl.class);

   /**
    * This method prepares lookup tables one each for SL, one each for RL and one for STAT column.
    */
   public CubeLookupTableStructure createCubeLookupTables (CubeCreationOutputInfo cubeCreationOutputInfo)
            throws CubeLookupPopulationException {
      CubeCreationInputInfo cubeCreationInputInfo = cubeCreationOutputInfo.getCubeCreationInputInfo();
      CubeCreationPopulatedContext cubeCreationPopulatedContext = cubeCreationInputInfo
               .getCubeCreationPopulatedContext();
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = cubeCreationInputInfo
               .getAnswersCatalogConfigurationContext();
      CubeConfigurationContext cubeConfigurationContext = cubeCreationInputInfo.getCubeConfigurationContext();

      CubeLookupTableStructure cubeLookupTableStructure = null;
      JobRequest jobRequest = cubeCreationPopulatedContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         List<String> existingTableNames = new ArrayList<String>();
         cubeLookupTableStructure = new CubeLookupTableStructure();

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  GENERATING_SIMPLE_LOOKUP_TABLES);
         if (logger.isDebugEnabled()) {
            logger.debug("Creating Simple Lookup Tables");
         }
         cubeLookupTableStructure.setSimpleLookupTables(prepareSimpleLookupTableStructure(cubeConfigurationContext,
                  answersCatalogConfigurationContext, cubeCreationPopulatedContext, existingTableNames));
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  GENERATING_RANGE_LOOKUP_TABLES);
         if (logger.isDebugEnabled()) {
            logger.debug("Creating Range Lookup Tables");
         }
         cubeLookupTableStructure.setRangeLookupTables(prepareRangeLookupTableStructure(cubeConfigurationContext,
                  answersCatalogConfigurationContext, cubeCreationPopulatedContext, existingTableNames));
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  GENERATING_STAT_LOOKUP_TABLE);
         if (logger.isDebugEnabled()) {
            logger.debug("Creating Stat Lookup Table");
         }
         cubeLookupTableStructure.setStatLookupTable(prepareStatLookupTableStructure(cubeConfigurationContext,
                  answersCatalogConfigurationContext, cubeCreationPopulatedContext, existingTableNames));
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  CREATING_INDEXES_ON_LOOKUP_TABLES);
         if (logger.isDebugEnabled()) {
            logger.debug("Creating Indexes on Lookup Tables");
         }
         createIndexesOnCubeLookupTables(cubeCreationOutputInfo.getCubeCreationInputInfo(), cubeLookupTableStructure);
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

      } catch (DataAccessException e) {
         exception = e;
         throw new CubeLookupPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_LOOKUP_TABLE_POPULATION_EXCEPTION_CODE, e);
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new CubeLookupPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_LOOKUP_TABLE_POPULATION_EXCEPTION_CODE, e);
      } catch (SDXException e) {
         exception = e;
         throw new CubeLookupPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_LOOKUP_TABLE_POPULATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubeLookupPopulationException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return cubeLookupTableStructure;
   }

   @Override
   public void manageCubeLookupTable (CubeUpdationOutputInfo cubeUpdationOutputInfo)
            throws CubeLookupPopulationException {
      CubeUpdationInputInfo cubeUpdationInputInfo = cubeUpdationOutputInfo.getCubeUpdationInputInfo();
      CubeUpdationPopulatedContext cubeUpdationPopulatedContext = cubeUpdationInputInfo
               .getCubeUpdationPopulatedContext();
      List<CubeUpdationDimensionInfo> cubeUpdationDimensionInfoList = cubeUpdationPopulatedContext
               .getCubeUpdationDimensionInfoList();
      JobRequest jobRequest = cubeUpdationPopulatedContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  DELETEING_DELETED_MEMBERS_AND_ADDING_ADDED_MEMBERS_TO_EXISTING_LOOKUP_TABLE_PER_DIMENSION);
         // for each dimension, delete the members from the actual lookup table if deleted members list is not empty and
         // insert the added members
         for (CubeUpdationDimensionInfo cubeUpdationDimensionInfo : cubeUpdationDimensionInfoList) {
            // get the corresponding fact table mapping for current dimension
            ConceptColumnMapping existingCubeLookupTableConceptColumnMapping = getMatchedLookupTableConceptColumnMapping(
                     cubeUpdationPopulatedContext, cubeUpdationDimensionInfo);

            // delete the deleted members from the lookup table
            deleteMembersFromExistingLookupTable(existingCubeLookupTableConceptColumnMapping,
                     cubeUpdationPopulatedContext, cubeUpdationDimensionInfo);

            // insert the added members to the existing lookup table.
            insertMembersToExistingLookupTable(existingCubeLookupTableConceptColumnMapping,
                     cubeUpdationPopulatedContext, cubeUpdationDimensionInfo);
         }
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
      } catch (DataAccessException e) {
         exception = e;
         throw new CubeLookupPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_LOOKUP_TABLE_POPULATION_EXCEPTION_CODE, e);
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new CubeLookupPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_LOOKUP_TABLE_POPULATION_EXCEPTION_CODE, e);
      } catch (SDXException e) {
         exception = e;
         throw new CubeLookupPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_LOOKUP_TABLE_POPULATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubeLookupPopulationException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
   }

   /**
    * This method builds the simple lookup tables. We will get the data from source table query and add 'all' value by
    * using insert into values
    * 
    * @param cubeConfigurationContext
    * @param answersCatalogConfigurationContext
    * @param cubeCreationPopulatedContext
    * @param existingTableNames
    * @return simpleLookupTableStructureList
    * @throws DataAccessException
    * @throws AnswersCatalogException
    * @throws SDXException
    */
   private List<CubeLookupTableStructureInfo> prepareSimpleLookupTableStructure (
            CubeConfigurationContext cubeConfigurationContext,
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            CubeCreationPopulatedContext cubeCreationPopulatedContext, List<String> existingTableNames)
            throws DataAccessException, AnswersCatalogException, SDXException {
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();
      Asset sourceAsset = cubeCreationPopulatedContext.getSourceAsset();
      DataSource sourceDatasource = sourceAsset.getDataSource();
      DataSource targetDatasource = targetAsset.getDataSource();
      AssetProviderType targetProviderType = targetDatasource.getProviderType();
      int maxDbObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();
      String simpleLookupColumnPrefix = cubeConfigurationContext.getSimpleLookupColumnPrefix();
      String cubeAllValueRepresentation = cubeConfigurationContext.getCubeAllValueRepresentation();
      boolean targetDataSourceSameAsSourceDataSource = cubeCreationPopulatedContext
               .isTargetDataSourceSameAsSourceDataSource();

      List<CubeLookupTableStructureInfo> simpleLookupTableStructureList = new ArrayList<CubeLookupTableStructureInfo>();
      for (ConceptColumnMapping conceptColumnMapping : cubeCreationPopulatedContext
               .getPopulatedSimpleLookupDimensions()) {
         if (logger.isDebugEnabled()) {
            logger.debug("Creating Simple Lookup Table for Concept " + conceptColumnMapping.getConcept().getName());
         }

         QueryColumn simpleLookupSourceQueryColumn = conceptColumnMapping.getQueryColumn();
         Tabl simpleLookupSourceTable = conceptColumnMapping.getTabl();
         String simpleLookupTableName = simpleLookupColumnPrefix + UNDERSCORE + simpleLookupSourceTable.getName();
         if (!LookupType.SIMPLE_LOOKUP.equals(simpleLookupSourceTable.getLookupType())) {
            simpleLookupTableName = simpleLookupTableName + UNDERSCORE + simpleLookupSourceQueryColumn.getColumnName();
         }
         simpleLookupTableName = simpleLookupTableName + UNDERSCORE + targetAsset.getName();
         simpleLookupTableName = getAnswersCatalogDataAccessService().getUniqueNonExistentTableName(targetDatasource,
                  simpleLookupTableName, existingTableNames, maxDbObjectLength);
         existingTableNames.add(simpleLookupTableName);

         List<String> existingColumnNames = new ArrayList<String>();

         QueryColumn lookupValueColumn = prepareLookupValueColumn(simpleLookupSourceQueryColumn, existingColumnNames,
                  cubeConfigurationContext, maxDbObjectLength);

         QueryColumn lookupDescColumn = prepareLookupDescColumn(simpleLookupSourceQueryColumn, existingColumnNames,
                  cubeConfigurationContext, maxDbObjectLength);

         List<QueryColumn> queryColumns = new ArrayList<QueryColumn>();
         queryColumns.add(lookupValueColumn);
         queryColumns.add(lookupDescColumn);

         QueryTable simpleLookupQueryTable = ExecueBeanManagementUtil.prepareQueryTable(simpleLookupTableName, null,
                  targetDatasource.getOwner());

         String simpleLookupTableCreateQuery = answersCatalogSQLQueryGenerationService.prepareCreateTableQuery(
                  targetProviderType, simpleLookupQueryTable, queryColumns);
         if (logger.isDebugEnabled()) {
            logger.debug("Create Table Query " + simpleLookupTableCreateQuery);
         }

         if (CheckType.YES == conceptColumnMapping.getTabl().getVirtual()) {
            String simpleLookupTableInsertQueryWithValuesSection = answersCatalogSQLQueryGenerationService
                     .prepareInsertQueryWithValuesSection(targetProviderType, simpleLookupQueryTable, queryColumns);
            if (logger.isDebugEnabled()) {
               logger.debug("Insert Query With Values Section Of Virtual Table "
                        + simpleLookupTableInsertQueryWithValuesSection);
            }
            List<Integer> parameterTypes = new ArrayList<Integer>();
            parameterTypes.add(AnswersCatalogUtil.getSQLDataType(lookupValueColumn));
            parameterTypes.add(AnswersCatalogUtil.getSQLDataType(lookupDescColumn));
            
            List<List<Object>> parameterValuesList = new ArrayList<List<Object>>();
            List<Membr> members = getSdxRetrievalService().getColumnMembers(conceptColumnMapping.getColumn());
            if (ExecueCoreUtil.isCollectionNotEmpty(members)) {
               for (Membr membr : members) {
                  List<Object> parameterValues = new ArrayList<Object>();
                  parameterValues.add(membr.getLookupValue());
                  parameterValues.add(membr.getLookupDescription());
                  parameterValuesList.add(parameterValues);
               }
            }
            getCubeCreationServiceHelper().transferLocalData(targetAsset.getDataSource(), simpleLookupTableName,
                     simpleLookupTableCreateQuery, simpleLookupTableInsertQueryWithValuesSection, parameterTypes,
                     parameterValuesList);
         } else {
            Query selectQueryObject = prepareSelectQueryForSimpleLookupTable(simpleLookupSourceTable);
            String selectQueryString = answersCatalogSQLQueryGenerationService.prepareSelectQuery(sourceAsset,
                     selectQueryObject);
            if (logger.isDebugEnabled()) {
               logger.debug("Select Query " + selectQueryString);
            }
            if (targetDataSourceSameAsSourceDataSource) {
               String simpleLookupTableInsertQuery = answersCatalogSQLQueryGenerationService.prepareInsertQuery(
                        targetProviderType, simpleLookupQueryTable, queryColumns);
               if (logger.isDebugEnabled()) {
                  logger.debug("Insert into select Table Query " + simpleLookupTableInsertQuery);
               }
               getCubeCreationServiceHelper().transferLocalData(targetDatasource, simpleLookupTableName,
                        simpleLookupTableCreateQuery, simpleLookupTableInsertQuery, selectQueryString);

            } else {
               Map<String, String> selectQueryAliases = new HashMap<String, String>();
               selectQueryAliases.put(lookupValueColumn.getColumnName(), selectQueryObject.getSelectEntities().get(0)
                        .getAlias());
               selectQueryAliases.put(lookupDescColumn.getColumnName(), selectQueryObject.getSelectEntities().get(1)
                        .getAlias());
               String simpleLookupTableInsertETLQuery = answersCatalogSQLQueryGenerationService.prepareETLInsertQuery(
                        targetProviderType, simpleLookupQueryTable, queryColumns, selectQueryAliases);
               if (logger.isDebugEnabled()) {
                  logger.debug("Insert ETL Query " + simpleLookupTableInsertETLQuery);
               }
               getCubeCreationServiceHelper().transferRemoteData(sourceDatasource, targetDatasource,
                        simpleLookupTableInsertETLQuery, selectQueryString, simpleLookupTableCreateQuery,
                        simpleLookupTableName);
            }
         }

         // preparing the data for simple lookup table
         // inserting "all" value to simple lookup table.
         String simpleLookupTableInsertQueryWithValuesSection = answersCatalogSQLQueryGenerationService
                  .prepareInsertQueryWithValuesSection(targetProviderType, simpleLookupQueryTable, queryColumns);
         if (logger.isDebugEnabled()) {
            logger.debug("Insert Query With Values Section " + simpleLookupTableInsertQueryWithValuesSection);
         }
         List<Integer> parameterTypes = new ArrayList<Integer>();
         parameterTypes.add(AnswersCatalogUtil.getSQLDataType(lookupValueColumn));
         parameterTypes.add(AnswersCatalogUtil.getSQLDataType(lookupDescColumn));

         List<List<Object>> parameterValuesList = new ArrayList<List<Object>>();
         List<Object> parameterValues = new ArrayList<Object>();
         parameterValues.add(cubeAllValueRepresentation);
         parameterValues.add(cubeAllValueRepresentation);
         parameterValuesList.add(parameterValues);
         getCubeCreationServiceHelper().transferLocalData(targetAsset.getDataSource(), simpleLookupTableName,
                  simpleLookupTableCreateQuery, simpleLookupTableInsertQueryWithValuesSection, parameterTypes,
                  parameterValuesList);

         CubeLookupTableStructureInfo simpleLookupTableStructureInfo = new CubeLookupTableStructureInfo();
         simpleLookupTableStructureInfo.setTableName(simpleLookupTableName);
         simpleLookupTableStructureInfo.setLookupType(LookupType.SIMPLE_LOOKUP);
         simpleLookupTableStructureInfo.setLookupValueColumn(lookupValueColumn);
         simpleLookupTableStructureInfo.setLookupDescColumn(lookupDescColumn);
         simpleLookupTableStructureInfo.setSourceConceptColumnMapping(conceptColumnMapping);
         simpleLookupTableStructureInfo.setParentTableExistsInSWI(true);
         simpleLookupTableStructureList.add(simpleLookupTableStructureInfo);
      }
      return simpleLookupTableStructureList;
   }

   /**
    * This method prepares the select query for simple lookup table.
    * 
    * @param simpleLookupSourceTable
    * @return query
    * @throws SDXException
    */
   private Query prepareSelectQueryForSimpleLookupTable (Tabl simpleLookupSourceTable) throws SDXException {
      List<Colum> sourceColumns = getSdxRetrievalService().getColumnsOfTable(simpleLookupSourceTable);
      Colum sourceLookupValueColumn = ExecueBeanUtil.findCorrespondingColumn(sourceColumns, simpleLookupSourceTable
               .getLookupValueColumn());
      Colum sourceLookupDescColumn = ExecueBeanUtil.findCorrespondingColumn(sourceColumns, simpleLookupSourceTable
               .getLookupDescColumn());
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(simpleLookupSourceTable);
      List<String> existingAliases = new ArrayList<String>();

      SelectEntity simpleLookupValueColumnSelectEntity = new SelectEntity();
      simpleLookupValueColumnSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      simpleLookupValueColumnSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(
               ExecueBeanManagementUtil.prepareQueryColumn(sourceLookupValueColumn), queryTable));
      simpleLookupValueColumnSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
      
      // NOTE: Date to string is needed on SLs as the target is converted 
      //    to String column always in order to support 'all' value
      if (DataType.DATE == sourceLookupValueColumn.getDataType() ||
               DataType.DATETIME == sourceLookupValueColumn.getDataType() ||
               DataType.TIME == sourceLookupValueColumn.getDataType()) {
         simpleLookupValueColumnSelectEntity.setDateAsStringRequired(true);
      }

      SelectEntity simpleLookupDescColumnSelectEntity = new SelectEntity();
      simpleLookupDescColumnSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      simpleLookupDescColumnSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(
               ExecueBeanManagementUtil.prepareQueryColumn(sourceLookupDescColumn), queryTable));
      simpleLookupDescColumnSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));

      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      selectEntities.add(simpleLookupValueColumnSelectEntity);
      selectEntities.add(simpleLookupDescColumnSelectEntity);
      Query query = new Query();
      query.setSelectEntities(selectEntities);
      return query;
   }

   /**
    * This method prepares the range lookups.All the range definitions will be parsed and become table data. We will add
    * 'all' value also with lowerlimit and upperlimit as null
    * 
    * @param cubeConfigurationContext
    * @param answersCatalogConfigurationContext
    * @param cubeCreationPopulatedContext
    * @param existingTableNames
    * @return rangeLookupTableStructureList
    * @throws DataAccessException
    * @throws AnswersCatalogException
    */
   private List<CubeLookupTableStructureInfo> prepareRangeLookupTableStructure (
            CubeConfigurationContext cubeConfigurationContext,
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            CubeCreationPopulatedContext cubeCreationPopulatedContext, List<String> existingTableNames)
            throws DataAccessException, AnswersCatalogException {
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();
      DataSource targetDataSource = targetAsset.getDataSource();
      AssetProviderType targetProviderType = targetDataSource.getProviderType();
      int maxDbObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();
      String rangeLookupColumnPrefix = cubeConfigurationContext.getRangeLookupColumnPrefix();
      String cubeAllValueRepresentation = cubeConfigurationContext.getCubeAllValueRepresentation();
      QueryColumn configurationRangeLookupLowerLimitColumn = cubeConfigurationContext.getRangeLookupLowerLimitColumn();
      QueryColumn configurationRangeLookupUpperLimitColumn = cubeConfigurationContext.getRangeLookupUpperLimitColumn();

      List<CubeLookupTableStructureInfo> rangeLookupTableStructureList = new ArrayList<CubeLookupTableStructureInfo>();
      for (ConceptColumnMapping conceptColumnMapping : cubeCreationPopulatedContext.getPopulatedRangeLookupDimensions()) {
         if (logger.isDebugEnabled()) {
            logger.debug("Creating Range Lookup Table for Concept " + conceptColumnMapping.getConcept().getName());
         }
         QueryColumn rangeLookupSourceQueryColumn = conceptColumnMapping.getQueryColumn();
         Tabl rangeLookupSourceTable = conceptColumnMapping.getTabl();

         String rangeLookupTableName = rangeLookupColumnPrefix + UNDERSCORE + rangeLookupSourceTable.getName();
         if (!LookupType.RANGE_LOOKUP.equals(rangeLookupSourceTable.getLookupType())) {
            rangeLookupTableName = rangeLookupTableName + UNDERSCORE + rangeLookupSourceQueryColumn.getColumnName();
         }
         rangeLookupTableName = rangeLookupTableName + UNDERSCORE + targetAsset.getName();
         rangeLookupTableName = getAnswersCatalogDataAccessService().getUniqueNonExistentTableName(targetDataSource,
                  rangeLookupTableName, existingTableNames, maxDbObjectLength);
         existingTableNames.add(rangeLookupTableName);

         List<String> existingColumnNames = new ArrayList<String>();

         QueryColumn lookupValueColumn = prepareLookupValueColumn(rangeLookupSourceQueryColumn, existingColumnNames,
                  cubeConfigurationContext, maxDbObjectLength);

         QueryColumn lookupDescColumn = prepareLookupDescColumn(rangeLookupSourceQueryColumn, existingColumnNames,
                  cubeConfigurationContext, maxDbObjectLength);

         QueryColumn lowerLimitColumn = ExecueBeanCloneUtil.cloneQueryColumn(configurationRangeLookupLowerLimitColumn);
         AnswersCatalogUtil.maintainColumnUniqueness(lowerLimitColumn, existingColumnNames, maxDbObjectLength);

         QueryColumn upperLimitColumn = ExecueBeanCloneUtil.cloneQueryColumn(configurationRangeLookupUpperLimitColumn);
         AnswersCatalogUtil.maintainColumnUniqueness(upperLimitColumn, existingColumnNames, maxDbObjectLength);

         boolean parentTableExistsInSWI = true;
         if (!LookupType.RANGE_LOOKUP.equals(rangeLookupSourceTable.getLookupType())) {
            parentTableExistsInSWI = false;
         }

         List<QueryColumn> queryColumns = new ArrayList<QueryColumn>();
         queryColumns.add(lookupValueColumn);
         queryColumns.add(lookupDescColumn);
         queryColumns.add(lowerLimitColumn);
         queryColumns.add(upperLimitColumn);

         QueryTable rangeLookupQueryTable = ExecueBeanManagementUtil.prepareQueryTable(rangeLookupTableName, null,
                  targetDataSource.getOwner());

         //         SQLTable rangeLookupSQLTable = ExecueBeanManagementUtil.prepareSQLTableFromQueryColumns(rangeLookupTableName,
         //                  queryColumns);

         String rangeLookupTableCreateQuery = answersCatalogSQLQueryGenerationService.prepareCreateTableQuery(
                  targetProviderType, rangeLookupQueryTable, queryColumns);
         if (logger.isDebugEnabled()) {
            logger.debug("Create Table Query " + rangeLookupTableCreateQuery);
         }

         String rangeLookupTableInsertQuery = answersCatalogSQLQueryGenerationService
                  .prepareInsertQueryWithValuesSection(targetProviderType, rangeLookupQueryTable, queryColumns);
         if (logger.isDebugEnabled()) {
            logger.debug("Insert Table Query " + rangeLookupTableInsertQuery);
         }

         // preparing the data for range lookup table
         List<Integer> parameterTypes = new ArrayList<Integer>();
         parameterTypes.add(AnswersCatalogUtil.getSQLDataType(lookupValueColumn));
         parameterTypes.add(AnswersCatalogUtil.getSQLDataType(lookupDescColumn));
         parameterTypes.add(AnswersCatalogUtil.getSQLDataType(lowerLimitColumn));
         parameterTypes.add(AnswersCatalogUtil.getSQLDataType(upperLimitColumn));

         List<List<Object>> parameterValuesList = new ArrayList<List<Object>>();
         List<Object> parameterValues = new ArrayList<Object>();
         parameterValues.add(cubeAllValueRepresentation);
         parameterValues.add(cubeAllValueRepresentation);
         parameterValues.add(null);
         parameterValues.add(null);
         parameterValuesList.add(parameterValues);

         for (RangeDetail rangeDetail : conceptColumnMapping.getRangeDefinition().getRangeDetails()) {
            parameterValues = new ArrayList<Object>();
            parameterValues.add(rangeDetail.getValue());
            parameterValues.add(rangeDetail.getDescription());
            parameterValues.add(AnswersCatalogUtil.getRoundedValue(rangeDetail.getLowerLimit(), lowerLimitColumn
                     .getScale()));
            parameterValues.add(AnswersCatalogUtil.getRoundedValue(rangeDetail.getUpperLimit(), upperLimitColumn
                     .getScale()));
            parameterValuesList.add(parameterValues);
         }

         getCubeCreationServiceHelper().transferLocalData(targetAsset.getDataSource(), rangeLookupTableName,
                  rangeLookupTableCreateQuery, rangeLookupTableInsertQuery, parameterTypes, parameterValuesList);

         CubeLookupTableStructureInfo rangeLookupTableStructureInfo = new CubeLookupTableStructureInfo();
         rangeLookupTableStructureInfo.setTableName(rangeLookupTableName);
         rangeLookupTableStructureInfo.setLookupType(LookupType.RANGE_LOOKUP);
         rangeLookupTableStructureInfo.setLookupValueColumn(lookupValueColumn);
         rangeLookupTableStructureInfo.setLookupDescColumn(lookupDescColumn);
         rangeLookupTableStructureInfo.setLowerLimitColumn(cubeConfigurationContext.getRangeLookupLowerLimitColumn());
         rangeLookupTableStructureInfo.setUpperLimitColumn(cubeConfigurationContext.getRangeLookupUpperLimitColumn());
         rangeLookupTableStructureInfo.setSourceConceptColumnMapping(conceptColumnMapping);
         rangeLookupTableStructureInfo.setParentTableExistsInSWI(parentTableExistsInSWI);
         rangeLookupTableStructureList.add(rangeLookupTableStructureInfo);
      }
      return rangeLookupTableStructureList;
   }

   /**
    * This method prepares the stat lookup table.All the stat will be parsed and become table data. We will add 'all'
    * value also to the table data
    * 
    * @param cubeConfigurationContext
    * @param answersCatalogConfigurationContext
    * @param cubeCreationPopulatedContext
    * @param existingTableNames
    * @return statLookupTableStructureInfo
    * @throws DataAccessException
    * @throws AnswersCatalogException
    */
   private CubeLookupTableStructureInfo prepareStatLookupTableStructure (
            CubeConfigurationContext cubeConfigurationContext,
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            CubeCreationPopulatedContext cubeCreationPopulatedContext, List<String> existingTableNames)
            throws DataAccessException, AnswersCatalogException {
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();
      DataSource targetDataSource = targetAsset.getDataSource();
      AssetProviderType targetProviderType = targetAsset.getDataSource().getProviderType();
      int maxDbObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();
      String cubeAllValueRepresentation = cubeConfigurationContext.getCubeAllValueRepresentation();
      List<StatType> applicableStats = cubeConfigurationContext.getApplicableStats();

      QueryColumn configurationStatColumn = cubeConfigurationContext.getStatColumn();

      String statLookupTableName = configurationStatColumn.getColumnName() + UNDERSCORE + targetAsset.getName();
      statLookupTableName = getAnswersCatalogDataAccessService().getUniqueNonExistentTableName(targetDataSource,
               statLookupTableName, existingTableNames, maxDbObjectLength);
      existingTableNames.add(statLookupTableName);

      List<String> existingColumnNames = new ArrayList<String>();

      QueryColumn lookupValueColumn = ExecueBeanCloneUtil.cloneQueryColumn(configurationStatColumn);
      AnswersCatalogUtil.maintainColumnUniqueness(lookupValueColumn, existingColumnNames, maxDbObjectLength);

      QueryColumn lookupDescColumn = prepareLookupDescColumn(configurationStatColumn, existingColumnNames,
               cubeConfigurationContext, maxDbObjectLength);

      List<QueryColumn> queryColumns = new ArrayList<QueryColumn>();
      queryColumns.add(lookupValueColumn);
      queryColumns.add(lookupDescColumn);
      QueryTable statLookupQueryTable = ExecueBeanManagementUtil.prepareQueryTable(statLookupTableName, null,
               targetDataSource.getOwner());

      //      SQLTable statLookupSQLTable = ExecueBeanManagementUtil.prepareSQLTableFromQueryColumns(statLookupTableName,
      //               queryColumns);

      String statLookupTableCreateQuery = answersCatalogSQLQueryGenerationService.prepareCreateTableQuery(
               targetProviderType, statLookupQueryTable, queryColumns);
      if (logger.isDebugEnabled()) {
         logger.debug("Create Table Query " + statLookupTableCreateQuery);
      }

      String statLookupTableInsertQuery = answersCatalogSQLQueryGenerationService.prepareInsertQueryWithValuesSection(
               targetProviderType, statLookupQueryTable, queryColumns);
      if (logger.isDebugEnabled()) {
         logger.debug("Insert Table Query " + statLookupTableInsertQuery);
      }

      // preparing the data for stat lookup table
      List<Integer> parameterTypes = new ArrayList<Integer>();
      parameterTypes.add(AnswersCatalogUtil.getSQLDataType(lookupValueColumn));
      parameterTypes.add(AnswersCatalogUtil.getSQLDataType(lookupDescColumn));

      List<List<Object>> parameterValuesList = new ArrayList<List<Object>>();
      List<Object> parameterValues = new ArrayList<Object>();
      for (StatType stat : applicableStats) {
         parameterValues = new ArrayList<Object>();
         parameterValues.add(stat.getValue());
         parameterValues.add(stat.getDescription());
         parameterValuesList.add(parameterValues);
      }

      /*
       * parameterValues = new ArrayList<Object>(); parameterValues.add(cubeAllValueRepresentation);
       * parameterValues.add(cubeAllValueRepresentation); parameterValuesList.add(parameterValues);
       */

      getCubeCreationServiceHelper().transferLocalData(targetAsset.getDataSource(), statLookupTableName,
               statLookupTableCreateQuery, statLookupTableInsertQuery, parameterTypes, parameterValuesList);

      CubeLookupTableStructureInfo statLookupTableStructureInfo = new CubeLookupTableStructureInfo();
      statLookupTableStructureInfo.setTableName(statLookupTableName);
      statLookupTableStructureInfo.setLookupType(LookupType.SIMPLE_LOOKUP);
      statLookupTableStructureInfo.setLookupValueColumn(lookupValueColumn);
      statLookupTableStructureInfo.setLookupDescColumn(lookupDescColumn);
      statLookupTableStructureInfo.setParentTableExistsInSWI(false);
      return statLookupTableStructureInfo;
   }

   /**
    * This method prepares the lookup value column.
    * 
    * @param queryColumn
    * @param existingColumnNames
    * @param cubeConfigurationContext
    * @param maxDbObjectLength
    * @return lookupValueColumn
    */
   private QueryColumn prepareLookupValueColumn (QueryColumn queryColumn, List<String> existingColumnNames,
            CubeConfigurationContext cubeConfigurationContext, Integer maxDbObjectLength) {
      QueryColumn lookupValueColumn = ExecueBeanCloneUtil.cloneQueryColumn(queryColumn);
      AnswersCatalogUtil.cleanQueryColumn(lookupValueColumn);
      // adjust the precision and data type for dimension based value columns
      getCubeCreationServiceHelper().adjustDimensionColumnPrecision(lookupValueColumn,
               cubeConfigurationContext.getDimensionDataType(), cubeConfigurationContext.getMinDimensionPrecision());
      AnswersCatalogUtil.maintainColumnUniqueness(lookupValueColumn, existingColumnNames, maxDbObjectLength);
      return lookupValueColumn;
   }

   /**
    * This method prepares the lookup desc column.
    * 
    * @param queryColumn
    * @param existingColumnNames
    * @param cubeConfigurationContext
    * @param maxDbObjectLength
    * @return lookupDescColumn
    */
   private QueryColumn prepareLookupDescColumn (QueryColumn queryColumn, List<String> existingColumnNames,
            CubeConfigurationContext cubeConfigurationContext, Integer maxDbObjectLength) {
      // clone the value column
      QueryColumn lookupDescColumn = ExecueBeanCloneUtil.cloneQueryColumn(queryColumn);
      // clean the value column
      AnswersCatalogUtil.cleanQueryColumn(lookupDescColumn);
      // append desc to it
      lookupDescColumn.setColumnName(lookupDescColumn.getColumnName() + UNDERSCORE
               + cubeConfigurationContext.getDescriptionColumnSuffix());
      lookupDescColumn.setDataType(cubeConfigurationContext.getDescriptionColumnDataType());
      lookupDescColumn.setPrecision(cubeConfigurationContext.getDescriptionColumnPrecision());
      lookupDescColumn.setScale(0);
      AnswersCatalogUtil.maintainColumnUniqueness(lookupDescColumn, existingColumnNames, maxDbObjectLength);
      return lookupDescColumn;
   }

   /**
    * This method creates indexes on all the lookup tables value column.
    * 
    * @param cubeCreationInputInfo
    * @param cubeLookupTableStructure
    * @throws DataAccessException
    */
   private void createIndexesOnCubeLookupTables (CubeCreationInputInfo cubeCreationInputInfo,
            CubeLookupTableStructure cubeLookupTableStructure) throws DataAccessException {
      DataSource dataSource = cubeCreationInputInfo.getCubeCreationPopulatedContext().getTargetAsset().getDataSource();
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = cubeCreationInputInfo
               .getAnswersCatalogConfigurationContext();
      int maxDBObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();
      // for each lookup table, we create index on value column of lookup table including stat lookup table.
      for (CubeLookupTableStructureInfo cubeLookupTableStructureInfo : cubeLookupTableStructure.getCombinedTables()) {
         getAnswersCatalogDataAccessService().createIndexOnTable(
                  dataSource,
                  ExecueBeanManagementUtil.prepareSQLIndex(cubeLookupTableStructureInfo.getTableName(),
                           cubeLookupTableStructureInfo.getLookupValueColumn().getColumnName()), maxDBObjectLength);
      }
   }

   public IAnswersCatalogDataAccessService getAnswersCatalogDataAccessService () {
      return answersCatalogDataAccessService;
   }

   public void setAnswersCatalogDataAccessService (IAnswersCatalogDataAccessService answersCatalogDataAccessService) {
      this.answersCatalogDataAccessService = answersCatalogDataAccessService;
   }

   public CubeCreationServiceHelper getCubeCreationServiceHelper () {
      return cubeCreationServiceHelper;
   }

   public void setCubeCreationServiceHelper (CubeCreationServiceHelper cubeCreationServiceHelper) {
      this.cubeCreationServiceHelper = cubeCreationServiceHelper;
   }

   public IAnswersCatalogSQLQueryGenerationService getAnswersCatalogSQLQueryGenerationService () {
      return answersCatalogSQLQueryGenerationService;
   }

   public void setAnswersCatalogSQLQueryGenerationService (
            IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService) {
      this.answersCatalogSQLQueryGenerationService = answersCatalogSQLQueryGenerationService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   private void insertMembersToExistingLookupTable (ConceptColumnMapping existingCubeLookupTableConceptColumnMapping,
            CubeUpdationPopulatedContext cubeUpdationPopulatedContext,
            CubeUpdationDimensionInfo cubeUpdationDimensionInfo) throws CubeLookupPopulationException,
            DataAccessException, SDXException {
      DataSource targetDataSource = cubeUpdationPopulatedContext.getTargetAsset().getDataSource();
      AssetProviderType targetProviderType = targetDataSource.getProviderType();
      List<Membr> addedMembers = cubeUpdationDimensionInfo.getAddedMembers();
      if (ExecueCoreUtil.isCollectionNotEmpty(addedMembers)) {
         Tabl lookupTable = existingCubeLookupTableConceptColumnMapping.getTabl();
         List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(lookupTable);
         Colum lookupTableValueColumn = ExecueBeanUtil.findCorrespondingColumn(columns, lookupTable
                  .getLookupValueColumn());
         Colum lookupTableDescColumn = ExecueBeanUtil.findCorrespondingColumn(columns, lookupTable
                  .getLookupDescColumn());
         List<Colum> orderedColumns = new ArrayList<Colum>();
         orderedColumns.add(lookupTableValueColumn);
         orderedColumns.add(lookupTableDescColumn);
         //         SQLTable lookupSQLTable = ExecueBeanManagementUtil.prepareSQLTableFromColumns(lookupTable.getName(),
         //                  orderedColumns);

         List<QueryColumn> orderedQueryColumns = ExecueBeanManagementUtil.prepareQueryColumns(columns);
         QueryTable lookupQueryTable = ExecueBeanManagementUtil.prepareQueryTable(lookupTable.getName(), null,
                  targetDataSource.getOwner());
         String lookupTableInsertStatement = getQueryGenerationUtilService(targetProviderType).createInsertStatement(
                  lookupQueryTable, orderedQueryColumns, true);

         if (logger.isDebugEnabled()) {
            logger.debug("Insert Statement on Lookup table" + lookupTableInsertStatement);
         }

         List<Integer> parameterTypes = new ArrayList<Integer>();
         parameterTypes.add(AnswersCatalogUtil.getSQLDataType(lookupTableValueColumn));
         parameterTypes.add(AnswersCatalogUtil.getSQLDataType(lookupTableDescColumn));

         List<List<Object>> parameterValuesList = new ArrayList<List<Object>>();
         List<Object> parameterValues = new ArrayList<Object>();
         for (Membr addedMember : addedMembers) {
            parameterValues.add(addedMember.getLookupValue());
            parameterValues.add(addedMember.getLookupDescription());
         }
         parameterValuesList.add(parameterValues);

         getAnswersCatalogDataAccessService().executeDMLStatements(targetDataSource.getName(),
                  lookupTableInsertStatement, parameterValuesList, parameterTypes);
      }
   }

   private ConceptColumnMapping getMatchedLookupTableConceptColumnMapping (
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

   private void deleteMembersFromExistingLookupTable (ConceptColumnMapping existingCubeLookupTableConceptColumnMapping,
            CubeUpdationPopulatedContext cubeUpdationPopulatedContext,
            CubeUpdationDimensionInfo cubeUpdationDimensionInfo) throws CubeLookupPopulationException,
            DataAccessException {
      DataSource targetDataSource = cubeUpdationPopulatedContext.getTargetAsset().getDataSource();
      AssetProviderType targetProviderType = targetDataSource.getProviderType();
      List<Membr> deletedMembers = cubeUpdationDimensionInfo.getDeletedMembers();
      if (ExecueCoreUtil.isCollectionNotEmpty(deletedMembers)) {
         List<String> deletedMemberValues = new ArrayList<String>();
         deletedMemberValues.addAll(AnswersCatalogUtil.getMemberValues(deletedMembers));
         ConditionEntity conditionEntity = AnswersCatalogUtil.prepareConditionEntity(
                  existingCubeLookupTableConceptColumnMapping.getQueryTable(),
                  existingCubeLookupTableConceptColumnMapping.getQueryColumn(), deletedMemberValues);

         String deleteQuery = getQueryGenerationUtilService(targetProviderType).createTableDeleteStatement(
                  conditionEntity);
         if (logger.isDebugEnabled()) {
            logger.debug("Delete Query on Lookup table" + deleteQuery);
         }
         getAnswersCatalogDataAccessService().executeDDLStatement(targetDataSource.getName(), deleteQuery);
      }
   }

   public QueryGenerationUtilServiceFactory getQueryGenerationUtilServiceFactory () {
      return queryGenerationUtilServiceFactory;
   }

   public void setQueryGenerationUtilServiceFactory (QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory) {
      this.queryGenerationUtilServiceFactory = queryGenerationUtilServiceFactory;
   }

   private IQueryGenerationUtilService getQueryGenerationUtilService (AssetProviderType providerType) {
      return getQueryGenerationUtilServiceFactory().getQueryGenerationUtilService(providerType);
   }
}
