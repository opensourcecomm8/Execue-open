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


package com.execue.sforce.absorption.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.api.clientsource.dao.IClientSourceDAO;
import com.execue.core.common.api.querygen.IQueryGenerationUtilService;
import com.execue.core.common.api.querygen.QueryGenerationUtilServiceFactory;
import com.execue.core.common.api.swi.IGenericJDBCService;
import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.SQLTable;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.ConstraintType;
import com.execue.core.common.util.ExecueBeanUtils;
import com.execue.core.exception.dataaccess.DataAccessException;
import com.execue.core.exception.swi.SQLGenerationException;
import com.execue.sforce.absorption.ISforceAbsorptionService;
import com.execue.sforce.bean.SObjectNormalizedData;
import com.execue.sforce.bean.SObjectNormalizedMeta;
import com.execue.sforce.exception.SforceException;
import com.execue.sforce.exception.SforceExceptionCodes;
import com.execue.sforce.helper.SforceUtilityHelper;

/**
 * This interface contains the absorption services to target data source
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public class SforceAbsorptionServiceImpl implements ISforceAbsorptionService {

   private IGenericJDBCService               genericJDBCService;
   private QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory;
   private IClientSourceDAO                  clientSourceDAO;

   /**
    * This method will absorb the sObjectNormalizedData at the data source location specified. Returns status about the
    * progress
    * 
    * @param dataSourceName
    * @param sObjectNormalizedData
    * @return boolean value
    * @throws SforceException
    */
   public boolean absorbSObjectData (DataSource dataSource, SObjectNormalizedData sObjectNormalizedData)
            throws SforceException {
      boolean absorbDataSuccess = false;
      try {
         List<Integer> parameterTypes = SforceUtilityHelper.getParameterTypes(sObjectNormalizedData
                  .getNormalizedQueryColumns());
         SQLTable sqlTable = ExecueBeanUtils.prepareSQLTableFromQueryColumns(sObjectNormalizedData.getSObjectTable()
                  .getTableName(), sObjectNormalizedData.getNormalizedQueryColumns());
         String dmlStatement = getQueryGenerationUtilService(dataSource.getProviderType()).createInsertStatement(
                  sqlTable, false);
         List<Integer> executeManipulationStatements = genericJDBCService.executeManipulationStatements(dataSource
                  .getName(), dmlStatement, sObjectNormalizedData.getNormalizedDataPoints(), parameterTypes);
         // TODO -VG- : analyse the output and return corresponding value
         return absorbDataSuccess;
      } catch (SQLGenerationException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_SOBJECT_ABSORPTION_FAILED_EXCEPTION_CODE, e);
      }
   }

   public boolean executeManipulationStatement (DataSource dataSource, String dmlStatement,
            List<Object> parameterValueRecords, List<Integer> parameterTypes) throws SforceException {
      boolean isSuccess = true;
      try {
         int executeManipulationStatement = genericJDBCService.executeManipulationStatement(dataSource.getName(),
                  dmlStatement, parameterValueRecords, parameterTypes);
      } catch (SQLGenerationException e) {
         isSuccess = false;
         throw new SforceException(SforceExceptionCodes.SFORCE_DEFAULT_EXCEPTION_CODE, e);
      }
      return isSuccess;
   }

   public boolean executeManipulationStatements (DataSource dataSource, String dmlStatement,
            List<List<Object>> parameterValueRecords, List<Integer> parameterTypes) throws SforceException {
      boolean isSuccess = true;
      try {
         List<Integer> rowCount = genericJDBCService.executeManipulationStatements(dataSource.getName(), dmlStatement,
                  parameterValueRecords, parameterTypes);
      } catch (SQLGenerationException e) {
         isSuccess = false;
         throw new SforceException(SforceExceptionCodes.SFORCE_DEFAULT_EXCEPTION_CODE, e);
      }
      return isSuccess;
   }

   public boolean executeDefinitionStatement (DataSource dataSource, String ddlStatement) throws SforceException {
      boolean isSuccess = true;
      try {
         genericJDBCService.executeDefinitionStatement(dataSource.getName(), ddlStatement);
      } catch (SQLGenerationException e) {
         isSuccess = false;
         throw new SforceException(SforceExceptionCodes.SFORCE_DEFAULT_EXCEPTION_CODE, e);
      }
      return isSuccess;
   }

   public ExeCueResultSet executeSelectStatement (DataSource dataSource, String selectStatement) throws SforceException {
      // TODO: -SM- need to revist this as client source DAO takes asset but uses only the data source inside it
      Asset asset = new Asset();
      ExeCueResultSet result;
      asset.setDataSource(dataSource);
      try {
         result = clientSourceDAO.executeQuery(asset, selectStatement);
      } catch (DataAccessException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_DEFAULT_EXCEPTION_CODE, e);
      }

      return result;

   }

   /**
    * This method will absorb the objectNormalizedMeta at the data source location specified. Returns status about the
    * progress
    * 
    * @param dataSource
    * @param objectNormalizedMeta
    * @return status
    * @throws SforceException
    */
   public boolean absorbSObjectMeta (DataSource dataSource, SObjectNormalizedMeta objectNormalizedMeta)
            throws SforceException {
      boolean status = true;
      try {
         IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
                  .getProviderType());
         SQLTable sqlTable = ExecueBeanUtils.prepareSQLTableFromQueryColumns(objectNormalizedMeta.getSObjectTable()
                  .getTableName(), objectNormalizedMeta.getNormalizedQueryColumns());
         String ddlStatement = queryGenerationUtilService.createTableCreateStatement(sqlTable, true);
         genericJDBCService.executeDefinitionStatement(dataSource.getName(), ddlStatement);
         for (QueryColumn queryColumn : objectNormalizedMeta.getNormalizedQueryColumns()) {
            if (queryColumn.isDistinct()) {
               // unique constraint required on column
               List<QueryColumn> uniqueQueryColumns = new ArrayList<QueryColumn>();
               uniqueQueryColumns.add(queryColumn);
               SQLTable constraintSQLTable = ExecueBeanUtils.prepareSQLTableFromQueryColumns(objectNormalizedMeta
                        .getSObjectTable().getTableName(), uniqueQueryColumns);
               String uniqueConstraintStatement = queryGenerationUtilService.createConstraintStatement(
                        ConstraintType.UNIQUE_KEY, queryColumn.getColumnName(), constraintSQLTable, null);
               genericJDBCService.executeDefinitionStatement(dataSource.getName(), uniqueConstraintStatement);
            }
         }
      } catch (SQLGenerationException e) {
         status = false;
         throw new SforceException(SforceExceptionCodes.SFORCE_SOBJECT_ABSORPTION_FAILED_EXCEPTION_CODE, e);
      }
      return status;
   }

   public IGenericJDBCService getGenericJDBCService () {
      return genericJDBCService;
   }

   public void setGenericJDBCService (IGenericJDBCService genericJDBCService) {
      this.genericJDBCService = genericJDBCService;
   }

   private IQueryGenerationUtilService getQueryGenerationUtilService (AssetProviderType assetProviderType) {
      return getQueryGenerationUtilServiceFactory().getQueryGenerationUtilService(assetProviderType);
   }

   public QueryGenerationUtilServiceFactory getQueryGenerationUtilServiceFactory () {
      return queryGenerationUtilServiceFactory;
   }

   public void setQueryGenerationUtilServiceFactory (QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory) {
      this.queryGenerationUtilServiceFactory = queryGenerationUtilServiceFactory;
   }

   public IClientSourceDAO getClientSourceDAO () {
      return clientSourceDAO;
   }

   public void setClientSourceDAO (IClientSourceDAO clientSourceDAO) {
      this.clientSourceDAO = clientSourceDAO;
   }

}
