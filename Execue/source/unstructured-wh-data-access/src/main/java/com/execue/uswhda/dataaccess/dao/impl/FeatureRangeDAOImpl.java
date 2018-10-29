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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.unstructured.FeatureRange;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueDAOUtil;
import com.execue.dataaccess.callback.HibernateCallbackParameterInfo;
import com.execue.dataaccess.callback.HibernateCallbackParameterValueType;
import com.execue.dataaccess.callback.HibernateStatementCallback;
import com.execue.dataaccess.configuration.USWHDataAccessConfigurationConstants;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.uswhda.dataaccess.dao.BaseUnstructuredWHDAO;
import com.execue.uswhda.dataaccess.dao.IFeatureRangeDAO;

/**
 * @author vishay
 */
public class FeatureRangeDAOImpl extends BaseUnstructuredWHDAO implements IFeatureRangeDAO {

   private static Map<String, String> queriesMap = new HashMap<String, String>();

   private static final Logger        log        = Logger.getLogger(FeatureRangeDAOImpl.class);

   static {
      queriesMap = ExecueDAOUtil
               .loadUSWHQueriesMap(USWHDataAccessConfigurationConstants.STATIC_FEATURE_RANGE_DAO_QUERIES_KEY);
   }

   public void deleteFeatureRangeByFeatureId (Long contextId, Long featureId) throws DataAccessException {
      try {
         String query = queriesMap.get("QUERY_DELETE_FEATURE_RANGES_BY_FEATURE_ID");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("featureId", featureId,
                  HibernateCallbackParameterValueType.OBJECT));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<FeatureRange> getFeatureRangesByFeatureId (Long contextId, Long featureId) throws DataAccessException {
      try {
         return getHibernateTemplate(contextId).findByNamedParam(
                  queriesMap.get("QUERY_GET_FEATURE_RANGES_BY_FEATURE_ID"), "featureId", featureId);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<FeatureRange> getFeatureRangesByFeatureIdExcludingDefault (Long contextId, Long featureId)
            throws DataAccessException {
      try {
         return getHibernateTemplate(contextId).findByNamedParam(
                  queriesMap.get("QUERY_GET_FEATURE_RANGES_BY_FEATURE_ID_EXCLUDING_DEFAULT"), "featureId", featureId);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public FeatureRange getFeatureRangeByFeatureIdAndRangeName (Long contextId, Long featureId, String rangeName)
            throws DataAccessException {
      FeatureRange featureRange = null;
      try {
         List<FeatureRange> FeatureRanges = getHibernateTemplate(contextId).findByNamedParam(
                  queriesMap.get("QUERY_GET_FEATURE_RANGE_BY_FEATURE_ID_AND_RANGE_NAME"),
                  new String[] { "featureId", "rangeName" }, new Object[] { featureId, rangeName });
         if (ExecueCoreUtil.isCollectionNotEmpty(FeatureRanges)) {
            featureRange = FeatureRanges.get(0);
         }
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
      return featureRange;
   }

}
