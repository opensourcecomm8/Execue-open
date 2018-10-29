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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.unstructured.Feature;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueDAOUtil;
import com.execue.dataaccess.configuration.USWHDataAccessConfigurationConstants;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.uswhda.dataaccess.dao.BaseUnstructuredWHDAO;
import com.execue.uswhda.dataaccess.dao.IFeatureDAO;

/**
 * @author vishay
 */
public class FeatureDAOImpl extends BaseUnstructuredWHDAO implements IFeatureDAO {

   private static Map<String, String> queriesMap = new HashMap<String, String>();

   private static final Logger        log        = Logger.getLogger(FeatureDAOImpl.class);

   static {
      queriesMap = ExecueDAOUtil
               .loadUSWHQueriesMap(USWHDataAccessConfigurationConstants.STATIC_FEATURE_DAO_QUERIES_KEY);
   }

   @SuppressWarnings ("unchecked")
   public List<Feature> getFeaturesByContextIdAndNames (Long contextId, Collection<String> featureNames)
            throws DataAccessException {
      try {
         return getHibernateTemplate(contextId).findByNamedParam(queriesMap.get("QUERY_GET_FEATURE_BY_NAMES"),
                  new String[] { "contextId", "featureNames" }, new Object[] { contextId, featureNames });
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }

   }

   @SuppressWarnings ("unchecked")
   public List<Feature> getAllFeatures (Long contextId) throws DataAccessException {
      try {
         return getHibernateTemplate(contextId).find(queriesMap.get("QUERY_ALL_FEATURES"));
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   public List<Feature> getFeaturesByContextId (Long contextId) throws DataAccessException {
      try {
         return getHibernateTemplate(contextId).findByNamedParam(queriesMap.get("QUERY_ALL_FEATURES_BY_CONTEXT_ID"),
                  "contextId", contextId);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<Feature> getFeaturesByContextIdAndFeatureBedIds (Long contextId, Collection<Long> featureBedIds)
            throws DataAccessException {
      try {
         return getHibernateTemplate(contextId).findByNamedParam(queriesMap.get("QUERY_GET_FEATURE_BY_FEATURE_BE_IDS"),
                  new String[] { "featureBedIds", "contextId" }, new Object[] { featureBedIds, contextId });
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<Long> getAllMultiValuedFeatures (Long contextId) throws DataAccessException {
      CheckType yesMultiValued = CheckType.YES;
      try {
         return getHibernateTemplate(contextId).findByNamedParam(queriesMap.get("QUERY_GET_ALL_MULTI_VALUED_FEATURES"),
                  new String[] { "contextId", "yesMultiValued" }, new Object[] { contextId, yesMultiValued });
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   public Feature getFeatureByFeatureBEDID (Long contextId, Long bedId) throws DataAccessException {
      try {
         List<Feature> features = getHibernateTemplate(contextId).findByNamedParam(
                  queriesMap.get("QUERY_GET_FEATURE__BY_BE_ID"), "featureBedId", bedId);
         if (!CollectionUtils.isEmpty(features)) {
            return features.get(0);
         } else {
            return null;
         }

      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

}
