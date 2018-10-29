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


package com.execue.das.jdbc.dataaccess.impl;

import java.util.List;

import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.das.jdbc.dataaccess.IGenericJDBCDataAccessManager;
import com.execue.dataaccess.IGenericJDBCDAO;
import com.execue.dataaccess.exception.DataAccessException;

/**
 * JDBC Data Access Manager
 * 
 * @author Vishay
 * @version 1.0
 */
public class GenericJDBCDataAccessManagerImpl implements IGenericJDBCDataAccessManager {

   private IGenericJDBCDAO genericJDBCDAO;

   public void executeDefinitionStatement (String dataSourceName, String ddlStatement) throws DataAccessException {
      getGenericJDBCDAO().executeDefinitionStatement(dataSourceName, ddlStatement);
   }

   public int executeManipulationStatement (String dataSourceName, String dmlStatement, List<Object> parameterValues,
            List<Integer> parameterTypes) throws DataAccessException {
      return getGenericJDBCDAO().executeManipulationStatement(dataSourceName, dmlStatement, parameterValues,
               parameterTypes);
   }

   public List<Integer> executeManipulationStatements (String dataSourceName, String dmlStatement,
            List<List<Object>> parameterValueRecords, List<Integer> parameterTypes) throws DataAccessException {

      return getGenericJDBCDAO().executeManipulationStatements(dataSourceName, dmlStatement, parameterValueRecords,
               parameterTypes);

   }

   public ExeCueResultSet executeQuery (String dataSourceName, SelectQueryInfo selectQueryInfo)
            throws DataAccessException {
      return getGenericJDBCDAO().executeQuery(dataSourceName, selectQueryInfo);

   }

   public ExeCueResultSet executeQuery (String dataSourceName, SelectQueryInfo selectQueryInfo,
            List<Object> parameterValues, List<Integer> parameterTypes) throws DataAccessException {
      return getGenericJDBCDAO().executeQuery(dataSourceName, selectQueryInfo, parameterValues, parameterTypes);

   }

   public IGenericJDBCDAO getGenericJDBCDAO () {
      return genericJDBCDAO;
   }

   public void setGenericJDBCDAO (IGenericJDBCDAO genericJDBCDAO) {
      this.genericJDBCDAO = genericJDBCDAO;
   }
}
