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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.unstructured.FeatureValue;
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
import com.execue.uswhda.dataaccess.dao.IFeatureValueDAO;

/**
 * @author vishay
 */
public class FeatureValueDAOImpl extends BaseUnstructuredWHDAO implements IFeatureValueDAO {

   private static Map<String, String> queriesMap = new HashMap<String, String>();

   private static final Logger        log        = Logger.getLogger(FeatureValueDAOImpl.class);

   static {
      queriesMap = ExecueDAOUtil
               .loadUSWHQueriesMap(USWHDataAccessConfigurationConstants.STATIC_FEATURE_VALUE_DAO_QUERIES_KEY);
   }

   @SuppressWarnings ("unchecked")
   public List<FeatureValue> getFeatureValue (Long contextId, Collection<Long> featureValueBedIds)
            throws DataAccessException {
      try {
         List<FeatureValue> featureValues = getHibernateTemplate(contextId).findByNamedParam(
                  queriesMap.get("QUERY_GET_FEATURE_VALUE_BY_BE_IDS"), "featureValueBedIds", featureValueBedIds);
         return featureValues;
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   public FeatureValue getFeatureValueByFeatureValueBEDID (Long contextId, Long featureValueBEDId)
            throws DataAccessException {
      try {
         List<FeatureValue> featureValuess = getHibernateTemplate(contextId).findByNamedParam(
                  queriesMap.get("QUERY_GET_FEATURE_VALUE_BY_FEATURE_VALUE_BE_ID"), "featureValueBedId",
                  featureValueBEDId);
         if (!CollectionUtils.isEmpty(featureValuess)) {
            return featureValuess.get(0);
         } else {
            return null;
         }

      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   public void deleteFeatureValuesByFeatureId (Long contextId, Long featureId) throws DataAccessException {
      try {
         String query = queriesMap.get("QUERY_DELETE_FEATURE_VALUES_BY_FEATURE_ID");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("featureId", featureId,
                  HibernateCallbackParameterValueType.OBJECT));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   // public List<FeatureValue> getFeatureValueByFeatureIdAndBatchSize (Long contextId, Long featureId, int batchSize)
   // throws DataAccessException {
   // List<FeatureValue> featureValues = new ArrayList<FeatureValue>(1);
   // try {
   // RetrievalLimitHibernateCallBack callback = new RetrievalLimitHibernateCallBack(
   // QUERY_GET_FEATURE_VALUES_BY_FEATURE_ID);
   // callback.addParameter("featureId", featureId);
   // callback.setMaxResults(batchSize);
   // featureValues = (List<FeatureValue>) getHibernateTemplate(contextId).execute(callback);
   // } catch (org.springframework.dao.DataAccessException dae) {
   // throw new DataAccessException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
   // }
   // return featureValues;
   // }

   @SuppressWarnings ("unchecked")
   @Override
   public FeatureValue getFeatureValueByFeatureIdAndFeatureValue (Long contextId, Long featureId, String featureValue)
            throws DataAccessException {
      FeatureValue fValue = null;
      try {
         List<FeatureValue> featureValues = getHibernateTemplate(contextId).findByNamedParam(
                  queriesMap.get("QUERY_GET_FEATURE_VALUE_BY_FEATURE_ID_AND_FEATURE_VALUE"),
                  new String[] { "featureId", "featureValue" }, new Object[] { featureId, featureValue });
         if (ExecueCoreUtil.isCollectionNotEmpty(featureValues)) {
            fValue = featureValues.get(0);
         }
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
      return fValue;
   }

}
