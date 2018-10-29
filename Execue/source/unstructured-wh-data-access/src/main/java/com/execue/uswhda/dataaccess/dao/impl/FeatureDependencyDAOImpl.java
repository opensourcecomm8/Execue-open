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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.execue.core.common.bean.entity.unstructured.FeatureDependency;
import com.execue.core.common.type.FeatureDependencyType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueDAOUtil;
import com.execue.dataaccess.NativeQueryHolder;
import com.execue.dataaccess.callback.HibernateCallbackParameterInfo;
import com.execue.dataaccess.callback.HibernateCallbackParameterValueType;
import com.execue.dataaccess.callback.HibernateStatementCallback;
import com.execue.dataaccess.callback.NativeQueryCallback;
import com.execue.dataaccess.configuration.USWHDataAccessConfigurationConstants;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.uswhda.dataaccess.dao.BaseUnstructuredWHDAO;
import com.execue.uswhda.dataaccess.dao.IFeatureDependencyDAO;

/**
 * @author vishay
 */
public class FeatureDependencyDAOImpl extends BaseUnstructuredWHDAO implements IFeatureDependencyDAO {

   private static Map<String, String> queriesMap = new HashMap<String, String>();

   private static final Logger        log        = Logger.getLogger(FeatureDependencyDAOImpl.class);

   static {
      queriesMap = ExecueDAOUtil
               .loadUSWHQueriesMap(USWHDataAccessConfigurationConstants.STATIC_FEATURE_DEPENDENCY_DAO_QUERIES_KEY);
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<FeatureDependency> getAllFeatureDependenciesByType (Long contextId,
            FeatureDependencyType featureDependencyType) throws DataAccessException {
      List<FeatureDependency> featureDependencies = new ArrayList<FeatureDependency>();
      try {
         String queryGetAllFeatureDependency = queriesMap.get("QUERY_GET_ALL_FEATURE_DEPENDENCY_BY_TYPE");
         featureDependencies = getHibernateTemplate(contextId).findByNamedParam(queryGetAllFeatureDependency,
                  new String[] { "contextId", "dependencyType" }, new Object[] { contextId, featureDependencyType });

      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
      return featureDependencies;
   }

   @Override
   public void deleteFeatureDependencyByFeatureId (Long contextId, Long featureId) throws DataAccessException {
      try {
         String query = queriesMap.get("DELETE_FEATURE_DEPENDENCY_BY_FEATURE_ID");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("contextId", contextId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("featureId", featureId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("dependencyFeatureId", featureId,
                  HibernateCallbackParameterValueType.OBJECT));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public Map<Long, List<Long>> getFeatureDependencyMapByType (Long contextId,
            FeatureDependencyType featureDependencyType) throws DataAccessException {
      Map<Long, List<Long>> featureDependencyMap = new HashMap<Long, List<Long>>();
      try {
         String query = NativeQueryHolder.getUnstructuredWHNativeQuery("QUERY_GET_FEATURE_DEPENDENCY_BY_TYPE");
         List<HibernateCallbackParameterInfo> queryParams = new ArrayList<HibernateCallbackParameterInfo>();
         queryParams.add(new HibernateCallbackParameterInfo("contextId", contextId,
                  HibernateCallbackParameterValueType.OBJECT));
         queryParams.add(new HibernateCallbackParameterInfo("dependencyType", featureDependencyType,
                  HibernateCallbackParameterValueType.ENUMERATION));
         LinkedHashMap<String, Type> scalarValues = new LinkedHashMap<String, Type>();
         scalarValues.put("FEATURE_ID", StandardBasicTypes.LONG);
         scalarValues.put("DEPENDENCY_FEATURE_ID", StandardBasicTypes.STRING);
         NativeQueryCallback nativeQueryCallback = new NativeQueryCallback(query, queryParams, scalarValues);
         List<Object[]> obj = (List<Object[]>) getHibernateTemplate(contextId).execute(nativeQueryCallback);
         if (ExecueCoreUtil.isCollectionNotEmpty(obj)) {
            for (Object[] objects : obj) {
               Long faceId = (Long) objects[0];
               String featureDependencyStringWithSeparator = (String) objects[1];
               if (!ExecueCoreUtil.isEmpty(featureDependencyStringWithSeparator)) {
                  List<Long> facetDependencies = ExecueCoreUtil.getLongListFromString(
                           featureDependencyStringWithSeparator, "##");
                  // order the dependencies through code and not through query
                  Collections.sort(facetDependencies);
                  featureDependencyMap.put(faceId, facetDependencies);
               } else {
                  featureDependencyMap.put(faceId, null);
               }
            }
         }
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
      return featureDependencyMap;
   }
}
