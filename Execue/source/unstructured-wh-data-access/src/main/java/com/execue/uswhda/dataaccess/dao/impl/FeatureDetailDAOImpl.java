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

import com.execue.core.common.bean.entity.unstructured.FeatureDetail;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.DisplayableFeatureAlignmentType;
import com.execue.core.common.type.FeatureDetailType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueDAOUtil;
import com.execue.dataaccess.configuration.USWHDataAccessConfigurationConstants;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.uswhda.dataaccess.dao.BaseUnstructuredWHDAO;
import com.execue.uswhda.dataaccess.dao.IFeatureDetailDAO;

/**
 * @author vishay
 */
public class FeatureDetailDAOImpl extends BaseUnstructuredWHDAO implements IFeatureDetailDAO {

   private static Map<String, String> queriesMap = new HashMap<String, String>();

   private static final Logger        log        = Logger.getLogger(FeatureDetailDAOImpl.class);

   static {
      queriesMap = ExecueDAOUtil
               .loadUSWHQueriesMap(USWHDataAccessConfigurationConstants.STATIC_FEATURE_DETAIL_DAO_QUERIES_KEY);
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<FeatureDetail> getAllFeatureDetails (Long contextId) throws DataAccessException {
      List<FeatureDetail> featureDetails = new ArrayList<FeatureDetail>();
      try {
         String queryGetAllFeatureDetails = queriesMap.get("QUERY_GET_ALL_FEATURE_DETAILS");
         featureDetails = getHibernateTemplate(contextId).findByNamedParam(queryGetAllFeatureDetails, "contextId",
                  contextId);

      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
      return featureDetails;
   }

   @SuppressWarnings ("unchecked")
   @Override
   public FeatureDetail getFeatureDetailByFeatureId (Long contextId, Long featureId) throws DataAccessException {
      FeatureDetail featureDetail = null;
      try {
         String queryGetFeatureDetailByFeatureId = queriesMap.get("QUERY_GET_FEATURE_DETAIL_BY_FEATURE_ID");
         List<FeatureDetail> featureDetails = getHibernateTemplate(contextId).findByNamedParam(
                  queryGetFeatureDetailByFeatureId, new String[] { "contextId", "featureId" },
                  new Object[] { contextId, featureId });
         if (ExecueCoreUtil.isCollectionNotEmpty(featureDetails)) {
            featureDetail = featureDetails.get(0);
         }

      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
      return featureDetail;
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<FeatureDetail> getAllFeatureDetailWithAdditionalFeartureInfo (Long contextId) throws DataAccessException {
      List<FeatureDetail> featureDetails = new ArrayList<FeatureDetail>();
      FeatureDetailType displayableFeatureType = FeatureDetailType.DISPLAYABLE_FEATURE;
      try {
         String queryGetAllFeatureDetails = queriesMap
                  .get("QUERY_GET_ALL_FEATURE_DETAILS_WITH_ADDITIONAL_FEATURE_INFO");
         List<Object[]> obj = getHibernateTemplate(contextId).findByNamedParam(queryGetAllFeatureDetails,
                  new String[] { "contextId", "displayableFeatureType" },
                  new Object[] { contextId, displayableFeatureType });
         if (ExecueCoreUtil.isCollectionNotEmpty(obj)) {
            for (Object[] objects : obj) {
               FeatureDetail featureDetail = new FeatureDetail();
               featureDetail.setId((Long) objects[0]);
               featureDetail.setContextId((Long) objects[1]);
               featureDetail.setFeatureId((Long) objects[2]);
               featureDetail.setColumnName((String) objects[3]);
               featureDetail.setDisplayOrder((Integer) objects[4]);
               featureDetail.setSortable((CheckType) objects[5]);
               featureDetail.setDefaultSortOrder((OrderEntityType) objects[6]);
               featureDetail.setFeatureDetailType((FeatureDetailType) objects[7]);
               featureDetail.setDiplayableFeatureAlignmentType((DisplayableFeatureAlignmentType) objects[8]);
               featureDetail.setFeatureName((String) objects[9]);
               featureDetail.setFeatureDisplayName((String) objects[10]);
               featureDetail.setFeatureSymbol((String) objects[11]);
               featureDetail.setFeatureValueType((FeatureValueType) objects[12]);
               featureDetails.add(featureDetail);
            }
         }
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
      return featureDetails;
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<FeatureDetail> getAllFeatureDetailsByDetailType (Long contextId, FeatureDetailType detailType)
            throws DataAccessException {
      try {
         String query = queriesMap.get("QUERY_ALL_FEATURE_DETAIL_BY_DETAIL_TYPE");
         return getHibernateTemplate(contextId).findByNamedParam(query, new String[] { "contextId", "detailType" },
                  new Object[] { contextId, detailType });

      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
   }

}
