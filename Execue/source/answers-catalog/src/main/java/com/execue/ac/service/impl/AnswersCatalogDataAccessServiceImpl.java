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

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.das.dataaccess.impl.SystemDataAccessServiceImpl;
import com.execue.dataaccess.clientsource.IClientSourceDAO;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.util.querygen.IQueryGenerationUtilService;

/**
 * This service contains methods for database access required for mart and cube.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class AnswersCatalogDataAccessServiceImpl extends SystemDataAccessServiceImpl implements
         IAnswersCatalogDataAccessService {

   private IClientSourceDAO clientSourceDAO;

   public Integer executeUpdateStatement (DataSource dataSource, QueryTable targetTable, List<QueryTable> sourceTables,
            List<ConditionEntity> setConditions, List<ConditionEntity> whereConditions) throws AnswersCatalogException {
      try {
         String dmlStatement = getQueryGenerationUtilService(dataSource.getProviderType()).createUpdateStatement(
                  targetTable, sourceTables, setConditions, whereConditions);
         return executeDMLStatement(dataSource.getName(), dmlStatement);
      } catch (DataAccessException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_JDBC_EXCEPTION_CODE, e);
      }
   }

   public Integer executeUpdateStatement (DataSource dataSource, QueryTable targetTable,
            List<ConditionEntity> setConditions) throws AnswersCatalogException {
      try {
         String dmlStatement = getQueryGenerationUtilService(dataSource.getProviderType()).createUpdateStatement(
                  targetTable, setConditions);
         return executeDMLStatement(dataSource.getName(), dmlStatement);
      } catch (DataAccessException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_JDBC_EXCEPTION_CODE, e);
      }
   }

   public Integer executeUpdateStatement (DataSource dataSource, QueryTable targetTable,
            List<ConditionEntity> setConditions, List<ConditionEntity> whereConditions) throws AnswersCatalogException {
      try {
         String dmlStatement = getQueryGenerationUtilService(dataSource.getProviderType()).createUpdateStatement(
                  targetTable, setConditions, whereConditions);
         return executeDMLStatement(dataSource.getName(), dmlStatement);
      } catch (DataAccessException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_JDBC_EXCEPTION_CODE, e);
      }
   }

   public Integer fetchMaxDataLength (DataSource dataSource, QueryTable queryTable, QueryColumn queryColumn)
            throws AnswersCatalogException {
      Integer maxDataLength = 0;
      try {
         IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
                  .getProviderType());
         String maxDataLengthQuery = queryGenerationUtilService.createMaximumDataLengthSelectStatement(queryTable,
                  queryColumn);
         SelectQueryInfo selectQueryInfo = new SelectQueryInfo(maxDataLengthQuery);
         List<Integer> countColumnIndexes = new ArrayList<Integer>();
         countColumnIndexes.add(0);
         selectQueryInfo.setCountColumnIndexes(countColumnIndexes);
         ExeCueResultSet resultSet = executeSQLQuery(dataSource.getName(), selectQueryInfo);
         // move to record
         if (resultSet.next()) {
            maxDataLength = resultSet.getInt(0);
         }
      } catch (Exception e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_JDBC_EXCEPTION_CODE, e);
      }
      return maxDataLength;
   }

   @Override
   public ExeCueResultSet executeSQLQuery (DataSource dataSource, Query query, String queryString)
            throws DataAccessException {
      return getClientSourceDAO().executeQuery(dataSource, query, queryString);
   }

   public IClientSourceDAO getClientSourceDAO () {
      return clientSourceDAO;
   }

   public void setClientSourceDAO (IClientSourceDAO clientSourceDAO) {
      this.clientSourceDAO = clientSourceDAO;
   }

}
