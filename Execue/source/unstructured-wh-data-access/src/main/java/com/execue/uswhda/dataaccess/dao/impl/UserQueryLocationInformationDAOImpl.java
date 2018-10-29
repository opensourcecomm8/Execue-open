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


package com.execue.uswhda.dataaccess.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueDAOUtil;
import com.execue.dataaccess.callback.HibernateCallbackParameterInfo;
import com.execue.dataaccess.callback.HibernateCallbackParameterValueType;
import com.execue.dataaccess.callback.HibernateStatementCallback;
import com.execue.dataaccess.configuration.USWHDataAccessConfigurationConstants;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.uswhda.dataaccess.dao.BaseUnstructuredWHDAO;
import com.execue.uswhda.dataaccess.dao.IUserQueryLocationInformationDAO;

/**
 * @author vishay
 */
public class UserQueryLocationInformationDAOImpl extends BaseUnstructuredWHDAO implements
         IUserQueryLocationInformationDAO {

   private static Map<String, String> queriesMap = new HashMap<String, String>();

   private static final Logger        log        = Logger.getLogger(UserQueryLocationInformationDAOImpl.class);

   static {
      queriesMap = ExecueDAOUtil
               .loadUSWHQueriesMap(USWHDataAccessConfigurationConstants.STATIC_USER_QUERY_LOCATION_INFORMATION_DAO_QUERIES_KEY);
   }

   @Override
   public void deleteUserQuerLocationInfoByExecutionDate (Long contextId, Date executionDate)
            throws DataAccessException {
      try {
         String query = queriesMap.get("DELETE_USER_QUERY_LOCATION_INFO_BY_EXECUTION_DATE");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("executionDate", executionDate,
                  HibernateCallbackParameterValueType.OBJECT));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public Long getUserQueryLocationInfoMaxExecutionDate (Long contextId) throws DataAccessException {
      Long maxUserQueryLocationInfoExecutionDate = null;
      List<Timestamp> dates = getHibernateTemplate(contextId).find(
               queriesMap.get("QUERY_USER_QUERY_LOCATION_INFO_MAX_EXECUTION_DATE"));
      if (ExecueCoreUtil.isCollectionNotEmpty(dates)) {
         if (dates.get(0) != null) {
            maxUserQueryLocationInfoExecutionDate = dates.get(0).getTime();
         }
      }
      return maxUserQueryLocationInfoExecutionDate;
   }

}
