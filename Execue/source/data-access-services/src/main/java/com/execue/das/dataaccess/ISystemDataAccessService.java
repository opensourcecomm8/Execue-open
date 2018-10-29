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


package com.execue.das.dataaccess;

import java.util.List;

import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SQLIndex;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.StatType;
import com.execue.dataaccess.exception.DataAccessException;

public interface ISystemDataAccessService {

   public ExeCueResultSet executeSQLQuery (String dataSourceName, SelectQueryInfo selectQueryInfo)
            throws DataAccessException;

   public void dropTable (DataSource targetDataSource, String tableName) throws DataAccessException;

   public void dropTables (DataSource targetDataSource, List<String> tables) throws DataAccessException;

   public boolean isTableExists (DataSource targetDataSource, String tableName) throws DataAccessException;

   public boolean isIndexExists (DataSource dataSource, String indexName, String tableName) throws DataAccessException;

   public void executeDDLStatement (String dataSourceName, String ddlStatement) throws DataAccessException;

   public Integer executeDMLStatement (String dataSourceName, String dmlStatement) throws DataAccessException;

   public List<Integer> executeDMLStatements (String dataSourceName, String dmlStatement,
            List<List<Object>> dataPoints, List<Integer> parameterTypes) throws DataAccessException;

   public Integer executeDMLStatement (String dataSourceName, String dmlStatement, List<Object> dataPoints,
            List<Integer> parameterTypes) throws DataAccessException;

   public Long getStatBasedColumnValue (DataSource dataSource, QueryTable table, QueryColumn queryColumn, StatType stat)
            throws DataAccessException;

   public Long executeCountQuery (String dataSourceName, SelectQueryInfo countQueryInfo) throws DataAccessException;

   public String getUniqueNonExistentTableName (DataSource dataSource, String tableName,
            List<String> existingTableNames, int maxDBObjectLength) throws DataAccessException;

   public String createIndexOnTable (DataSource dataSource, SQLIndex sqlIndex, int maxDBObjectLength)
            throws DataAccessException;

   public void createMultipleIndexesOnTable (DataSource dataSource, List<SQLIndex> indexes, int maxDBObjectLength)
            throws DataAccessException;

   /**
    * Trim the indexName to maxDBObjectLength. Check the indexName for non-existance in source in loop by appending a
    * random number in each iteration. Return the amended indexName which does not exist on source.
    * 
    * @param dataSource
    * @param indexName
    * @param tableName
    * @param maxDBObjectLength
    * @return
    * @throws DataAccessException
    */
   public String getUniqueNonExistentIndexName (DataSource dataSource, String indexName, String tableName,
            int maxDBObjectLength) throws DataAccessException;

}
