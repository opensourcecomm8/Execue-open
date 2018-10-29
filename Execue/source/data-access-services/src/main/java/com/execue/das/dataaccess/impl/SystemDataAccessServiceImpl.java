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


package com.execue.das.dataaccess.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SQLIndex;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.das.dataaccess.ISystemDataAccessConstants;
import com.execue.das.dataaccess.ISystemDataAccessService;
import com.execue.das.jdbc.service.IGenericJDBCService;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

public class SystemDataAccessServiceImpl implements ISystemDataAccessService, ISystemDataAccessConstants {

   private static final Logger               logger = Logger.getLogger(SystemDataAccessServiceImpl.class);

   private IGenericJDBCService               genericJDBCService;

   private QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory;

   public void dropTable (DataSource targetDataSource, String tableName) throws DataAccessException {

      IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(targetDataSource
               .getProviderType());
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName, null, targetDataSource.getOwner());
      boolean tableExists = isTableExists(targetDataSource, tableName);
      if (tableExists) {
         String sqlTableDropStatement = queryGenerationUtilService.createTableDropStatement(queryTable);
         executeDDLStatement(targetDataSource.getName(), sqlTableDropStatement);
      }
   }

   public void dropTables (DataSource targetDataSource, List<String> tables) throws DataAccessException {
      IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(targetDataSource
               .getProviderType());

      for (String tableName : tables) {
         boolean tableExists = isTableExists(targetDataSource, tableName);
         if (tableExists) {
            QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName, null, targetDataSource
                     .getOwner());
            String sqlTableDropStatement = queryGenerationUtilService.createTableDropStatement(queryTable);
            executeDDLStatement(targetDataSource.getName(), sqlTableDropStatement);
         }
      }
   }

   public ExeCueResultSet executeSQLQuery (String dataSourceName, SelectQueryInfo selectQueryInfo)
            throws DataAccessException {
      return getGenericJDBCService().executeQuery(dataSourceName, selectQueryInfo);
   }

   public void executeDDLStatement (String dataSourceName, String ddlStatement) throws DataAccessException {
      genericJDBCService.executeDefinitionStatement(dataSourceName, ddlStatement);

   }

   public List<Integer> executeDMLStatements (String dataSourceName, String dmlStatement,
            List<List<Object>> dataPoints, List<Integer> parameterTypes) throws DataAccessException {

      return genericJDBCService.executeManipulationStatements(dataSourceName, dmlStatement, dataPoints, parameterTypes);

   }

   public Integer executeDMLStatement (String dataSourceName, String dmlStatement, List<Object> dataPoints,
            List<Integer> parameterTypes) throws DataAccessException {
      return genericJDBCService.executeManipulationStatement(dataSourceName, dmlStatement, dataPoints, parameterTypes);

   }

   public Integer executeDMLStatement (String dataSourceName, String dmlStatement) throws DataAccessException {
      return genericJDBCService.executeManipulationStatement(dataSourceName, dmlStatement, null, null);

   }

   public boolean isTableExists (DataSource dataSource, String tableName) throws DataAccessException {
      boolean tableExists = true;
      try {
         IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
                  .getProviderType());
         QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName, null, dataSource.getOwner());
         SelectQueryInfo pingSelectQueryInfo = queryGenerationUtilService.createPingSelectStatement(queryTable);
         executeSQLQuery(dataSource.getName(), pingSelectQueryInfo);
      } catch (Exception exp) {
         tableExists = false;
      }
      return tableExists;
   }

   public boolean isIndexExists (DataSource dataSource, String indexName, String tableName) throws DataAccessException {
      boolean indexExists = false;
      try {
         IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
                  .getProviderType());
         QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName, null, dataSource.getOwner());
         String indexExistsQuery = queryGenerationUtilService.createIndexPingStatement(indexName, queryTable);
         ExeCueResultSet exeCueResultSet = executeSQLQuery(dataSource.getName(), new SelectQueryInfo(indexExistsQuery));
         indexExists = exeCueResultSet.next();
      } catch (Exception exp) {
         indexExists = false;
      }
      return indexExists;
   }

   public Long getStatBasedColumnValue (DataSource dataSource, QueryTable table, QueryColumn queryColumn, StatType stat)
            throws DataAccessException {
      Long statColumnValue = 0L;
      try {
         IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
                  .getProviderType());
         SelectQueryInfo statBasedQueryInfo = queryGenerationUtilService.createStatBasedSelectStatement(table,
                  queryColumn, stat);
         ExeCueResultSet resultSet = executeSQLQuery(dataSource.getName(), statBasedQueryInfo);
         if (resultSet.next()) {
            statColumnValue = resultSet.getLong(0);
         }
      } catch (Exception e) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return statColumnValue;
   }

   public Long executeCountQuery (String dataSourceName, SelectQueryInfo countQueryInfo) throws DataAccessException {
      Long value = 0L;
      try {
         ExeCueResultSet resultSet = executeSQLQuery(dataSourceName, countQueryInfo);
         if (resultSet.next()) {
            value = resultSet.getLong(0);
         }
      } catch (Exception e) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return value;
   }

   public String getUniqueNonExistentTableName (DataSource dataSource, String tableName,
            List<String> existingTableNames, int maxDBObjectLength) throws DataAccessException {
      boolean uniqueNonExistentTableFound = false;
      String normalizedOriginalTableName = ExecueStringUtil.getNormalizedNameByTrimmingFromMiddle(tableName,
               existingTableNames, maxDBObjectLength);
      String normalizedTableName = normalizedOriginalTableName;
      try {
         do {
            normalizedTableName = normalizedTableName.toUpperCase();
            normalizedTableName = ExecueStringUtil.getNormalizedNameByTrimmingFromMiddle(normalizedTableName,
                     existingTableNames, maxDBObjectLength);
            uniqueNonExistentTableFound = !isTableExists(dataSource, normalizedTableName);
            if (uniqueNonExistentTableFound) {
               break;
            } else {
               normalizedTableName = normalizedOriginalTableName + UNDERSCORE
                        + ExecueCoreUtil.getRandomNumberLessThanHundred();
            }
         } while (!uniqueNonExistentTableFound);
      } catch (DataAccessException dataAccessException) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
      return normalizedTableName;
   }

   public String createIndexOnTable (DataSource dataSource, SQLIndex sqlIndex, int maxDBObjectLength)
            throws DataAccessException {

      synchronized (this) {
         String tableName = sqlIndex.getTableName();

         String indexName = SQL_INDEX_PREFIX + ExecueCoreUtil.joinCollection(sqlIndex.getColumnNames(), UNDERSCORE)
                  + UNDERSCORE + tableName;
         indexName = getUniqueNonExistentIndexName(dataSource, indexName, tableName, maxDBObjectLength);
         sqlIndex.setName(indexName);

         String indexStatement = getQueryGenerationUtilService(dataSource.getProviderType()).createIndexStatement(
                  sqlIndex, false);

         if (logger.isDebugEnabled()) {
            logger.debug("Index Creation Statement : " + indexStatement);
         }

         executeDDLStatement(dataSource.getName(), indexStatement);
         return indexName;
      }

   }

   public void createMultipleIndexesOnTable (DataSource dataSource, List<SQLIndex> indexes, int maxDBObjectLength)
            throws DataAccessException {
      if (ExecueCoreUtil.isCollectionNotEmpty(indexes)) {
         if (!createMultipleIndexesOnTableWithSingleStatement(dataSource, indexes, maxDBObjectLength)) {
            for (SQLIndex indx : indexes) {
               createIndexOnTable(dataSource, indx, maxDBObjectLength);
            }
         }
      }
   }

   private boolean createMultipleIndexesOnTableWithSingleStatement (DataSource dataSource, List<SQLIndex> indexes,
            int maxDBObjectLength) throws DataAccessException {
      synchronized (this) {
         boolean indexesCreated = false;
         if (getQueryGenerationUtilService(dataSource.getProviderType())
                  .isMultipleIndexesWithSingleStatementSupported()) {
            String tempIndexName = null;
            String tableName = indexes.get(0).getTableName();
            List<String> existingIndexNames = new ArrayList<String>();
            for (SQLIndex indx : indexes) {
               tempIndexName = SQL_INDEX_PREFIX + ExecueCoreUtil.joinCollection(indx.getColumnNames(), UNDERSCORE)
                        + UNDERSCORE + tableName;
               tempIndexName = getUniqueNonExistentIndexName(dataSource, tempIndexName, tableName, existingIndexNames,
                        maxDBObjectLength);
               indx.setName(tempIndexName);
               existingIndexNames.add(tempIndexName);
            }

            String indexCreationStatement = getQueryGenerationUtilService(dataSource.getProviderType())
                     .createMultipleIndexsWithSingleStatement(indexes);

            if (logger.isDebugEnabled()) {
               logger.debug("Index Creation Statement [Single DDL] : " + indexCreationStatement);
            }

            executeDDLStatement(dataSource.getName(), indexCreationStatement);
            indexesCreated = true;
         }
         return indexesCreated;
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.ISystemDataAccessService#getUniqueNonExistentIndexName(com.execue.core.common.bean.entity.DataSource,
    *      java.lang.String, java.lang.String, int)
    */
   public String getUniqueNonExistentIndexName (DataSource dataSource, String indexName, String tableName,
            int maxDBObjectLength) throws DataAccessException {
      return getUniqueNonExistentIndexName(dataSource, indexName, tableName, new ArrayList<String>(), maxDBObjectLength);
   }

   public String getUniqueNonExistentIndexName (DataSource dataSource, String indexName, String tableName,
            List<String> existingIndexNames, int maxDBObjectLength) throws DataAccessException {
      boolean uniqueNonExistentIndexFound = false;
      String normalizedOriginalIndexName = ExecueStringUtil.getNormalizedNameByTrimmingFromMiddle(indexName,
               existingIndexNames, maxDBObjectLength);
      String normalizedIndexName = normalizedOriginalIndexName;
      try {
         do {
            normalizedIndexName = normalizedIndexName.toUpperCase();
            normalizedIndexName = ExecueStringUtil.getNormalizedNameByTrimmingFromMiddle(normalizedIndexName,
                     existingIndexNames, maxDBObjectLength);
            uniqueNonExistentIndexFound = !isIndexExists(dataSource, normalizedIndexName, tableName);
            if (uniqueNonExistentIndexFound) {
               break;
            } else {
               normalizedIndexName = normalizedOriginalIndexName + UNDERSCORE
                        + ExecueCoreUtil.getRandomNumberLessThanHundred();
            }
         } while (!uniqueNonExistentIndexFound);
      } catch (DataAccessException dataAccessException) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
      return normalizedIndexName;
   }

   public IGenericJDBCService getGenericJDBCService () {
      return genericJDBCService;
   }

   public void setGenericJDBCService (IGenericJDBCService genericJDBCService) {
      this.genericJDBCService = genericJDBCService;
   }

   protected IQueryGenerationUtilService getQueryGenerationUtilService (AssetProviderType assetProviderType) {
      return getQueryGenerationUtilServiceFactory().getQueryGenerationUtilService(assetProviderType);
   }

   public QueryGenerationUtilServiceFactory getQueryGenerationUtilServiceFactory () {
      return queryGenerationUtilServiceFactory;
   }

   public void setQueryGenerationUtilServiceFactory (QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory) {
      this.queryGenerationUtilServiceFactory = queryGenerationUtilServiceFactory;
   }

}
