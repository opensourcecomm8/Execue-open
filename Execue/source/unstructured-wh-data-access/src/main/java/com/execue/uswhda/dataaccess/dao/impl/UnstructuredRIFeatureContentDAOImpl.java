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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.execue.core.common.bean.entity.unstructured.RIFeatureContent;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.DisplayableFeatureAlignmentType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueDAOUtil;
import com.execue.dataaccess.NativeQueryHolder;
import com.execue.dataaccess.callback.HibernateCallbackParameterInfo;
import com.execue.dataaccess.callback.HibernateCallbackParameterValueType;
import com.execue.dataaccess.callback.HibernateCallbackUtility;
import com.execue.dataaccess.callback.HibernateStatementCallback;
import com.execue.dataaccess.callback.NativeQueryCallback;
import com.execue.dataaccess.configuration.USWHDataAccessConfigurationConstants;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.uswhda.dataaccess.dao.BaseUnstructuredWHDAO;
import com.execue.uswhda.dataaccess.dao.IUnstructuredRIFeatureContentDAO;

/**
 * @author jitendra
 */
public class UnstructuredRIFeatureContentDAOImpl extends BaseUnstructuredWHDAO implements
         IUnstructuredRIFeatureContentDAO {

   private static Map<String, String> queriesMap = new HashMap<String, String>();

   private static final Logger        log        = Logger.getLogger(UnstructuredRIFeatureContentDAOImpl.class);

   static {
      queriesMap = ExecueDAOUtil
               .loadUSWHQueriesMap(USWHDataAccessConfigurationConstants.STATIC_UNSTRUCTURED_RI_FEATURE_CONTENT_DAO_QUERIES_KEY);
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<RIFeatureContent> getAllRIFeatureContentByContextId (Long contextId) throws DataAccessException {
      List<RIFeatureContent> riFeatureContents = new ArrayList<RIFeatureContent>();
      try {
         String queryGetAllRIFeatureContent = queriesMap.get("QUERY_GET_ALL_RI_FEATURE_CONTENT");
         riFeatureContents = getHibernateTemplate(contextId).findByNamedParam(queryGetAllRIFeatureContent, "contextId",
                  contextId);

      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
      return riFeatureContents;
   }

   @SuppressWarnings ("unchecked")
   @Override
   public void deleteRIFeatureContentByContextId (Long contextId) throws DataAccessException {
      try {
         String query = queriesMap.get("QUERY_DELETE_RI_FEATURE_CONTENT_BY_CONTEXT_ID");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("contextId", contextId,
                  HibernateCallbackParameterValueType.OBJECT));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }

   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<RIFeatureContent> populateRIFeatureContents (Long contextId) throws DataAccessException {
      List<RIFeatureContent> riFeatureContents = new ArrayList<RIFeatureContent>();
      try {
         String queryRIFeatureContentPopulation = NativeQueryHolder
                  .getUnstructuredWHNativeQuery("QUERY_POPULATE_RI_FEATURE_CONTENT");

         List<HibernateCallbackParameterInfo> queryParams = HibernateCallbackUtility
                  .prepareListForParameters(new HibernateCallbackParameterInfo("contextId", contextId,
                           HibernateCallbackParameterValueType.OBJECT));
         LinkedHashMap<String, Type> scalarValues = new LinkedHashMap<String, Type>();
         scalarValues.put("CONTEXT_ID", StandardBasicTypes.LONG);
         scalarValues.put("FEATURE_ID", StandardBasicTypes.LONG);
         scalarValues.put("FEATURE_NAME", StandardBasicTypes.STRING);
         scalarValues.put("FEATURE_DISPLAY_NAME", StandardBasicTypes.STRING);
         scalarValues.put("FEATURE_SYMBOL", StandardBasicTypes.STRING);
         scalarValues.put("FEATURE_BE_ID", StandardBasicTypes.LONG);
         scalarValues.put("FEATURE_TYPE", StandardBasicTypes.STRING);
         scalarValues.put("LOCATION_BASED", StandardBasicTypes.CHARACTER);
         scalarValues.put("MULTI_VALUED", StandardBasicTypes.CHARACTER);
         scalarValues.put("MULTI_VALUED_GLOBAL_PENALTY", StandardBasicTypes.CHARACTER);
         scalarValues.put("FIELD_NAME", StandardBasicTypes.STRING);
         scalarValues.put("DISPLAYABLE", StandardBasicTypes.CHARACTER);
         scalarValues.put("DISP_COLUMN_NAME", StandardBasicTypes.STRING);
         scalarValues.put("DISP_ORDER", StandardBasicTypes.INTEGER);
         scalarValues.put("SORTABLE", StandardBasicTypes.CHARACTER);
         scalarValues.put("DEFAULT_SORT_ORDER", StandardBasicTypes.STRING);
         scalarValues.put("ALIGNMENT", StandardBasicTypes.STRING);
         scalarValues.put("DATA_HEADER", StandardBasicTypes.CHARACTER);
         scalarValues.put("FACET", StandardBasicTypes.CHARACTER);
         scalarValues.put("FACET_ORDER", StandardBasicTypes.INTEGER);
         scalarValues.put("FACET_DEPENDENCY", StandardBasicTypes.STRING);
         scalarValues.put("DEFAULT_VALUE", StandardBasicTypes.DOUBLE);
         scalarValues.put("MINIMUM_VALUE", StandardBasicTypes.DOUBLE);
         scalarValues.put("MAXIMUM_VALUE", StandardBasicTypes.DOUBLE);
         scalarValues.put("MINIMUM_IMPACT_VALUE", StandardBasicTypes.DOUBLE);

         NativeQueryCallback nativeCallback = new NativeQueryCallback(queryRIFeatureContentPopulation, queryParams,
                  scalarValues);

         List<Object[]> obj = (List<Object[]>) getHibernateTemplate(contextId).execute(nativeCallback);
         if (ExecueCoreUtil.isCollectionNotEmpty(obj)) {
            for (Object[] objects : obj) {
               RIFeatureContent riFeatureContent = new RIFeatureContent();
               riFeatureContent.setContextId((Long) objects[0]);
               riFeatureContent.setFeatureId((Long) objects[1]);
               riFeatureContent.setFeatureName((String) objects[2]);
               riFeatureContent.setFeatureDisplayName((String) objects[3]);
               riFeatureContent.setFeatureSymbol((String) objects[4]);
               riFeatureContent.setFeatureBedId((Long) objects[5]);
               riFeatureContent.setFeatureValueType(FeatureValueType.getType((String) objects[6]));
               riFeatureContent.setLocationBased(CheckType.getType((Character) objects[7]));
               riFeatureContent.setMultiValued(CheckType.getType((Character) objects[8]));
               riFeatureContent.setMultiValuedGlobalPenalty(CheckType.getType((Character) objects[9]));
               riFeatureContent.setFieldName((String) objects[10]);
               riFeatureContent.setDisplayable(CheckType.getType((Character) objects[11]));
               riFeatureContent.setDisplayableColumnName((String) objects[12]);
               riFeatureContent.setDisplayableDisplayOrder((Integer) objects[13]);
               riFeatureContent.setSortable(CheckType.getType((Character) objects[14]));
               riFeatureContent.setDefaultSortOrder(OrderEntityType.getOrderEntityType((String) objects[15]));
               riFeatureContent.setDisplayableFeatureAlignmentType(DisplayableFeatureAlignmentType
                        .getType((String) objects[16]));
               riFeatureContent.setDataHeader(CheckType.getType((Character) objects[17]));
               riFeatureContent.setFacet(CheckType.getType((Character) objects[18]));
               riFeatureContent.setFacetDisplayOrder((Integer) objects[19]);
               riFeatureContent.setFacetDependency((String) objects[20]);

               riFeatureContent.setRangeDefaultValue((Double) objects[21]);
               riFeatureContent.setRangeMinimumValue((Double) objects[22]);
               riFeatureContent.setRangeMaximumValue((Double) objects[23]);
               riFeatureContent.setRangeMinimumImpactValue((Double) objects[24]);

               riFeatureContents.add(riFeatureContent);
            }
         }
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
      return riFeatureContents;
   }
}
