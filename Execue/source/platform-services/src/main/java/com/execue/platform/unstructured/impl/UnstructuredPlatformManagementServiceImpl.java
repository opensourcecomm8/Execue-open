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


package com.execue.platform.unstructured.impl;

import java.util.List;

import com.execue.core.common.bean.entity.unstructured.RIFeatureContent;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.das.configuration.IDataAccessServicesConfigurationService;
import com.execue.das.dataaccess.ISystemDataAccessService;
import com.execue.das.solr.bean.SolrFieldConfigurationInfo;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.unstructured.IUnstructuredPlatformManagementService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.service.IUnstructuredWarehouseManagementService;
import com.execue.uswh.service.IUnstructuredWarehouseRetrievalService;

/**
 * @author Vishay
 */
public class UnstructuredPlatformManagementServiceImpl implements IUnstructuredPlatformManagementService {

   private IDataAccessServicesConfigurationService dataAccessServicesConfigurationService;
   private IUnstructuredWarehouseRetrievalService  unstructuredWarehouseRetrievalService;
   private IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService;
   private ISDXRetrievalService                    sdxRetrievalService;
   private ISystemDataAccessService                systemDataAccessService;

   private static final String                     CONSTANT_CONTENT_DATE                                 = "<contentDate>";
   private static final String                     CONSTANT_CONTEXT                                      = "<contextId>";

   private static final String                     STATEMENT_DELETE_FROM_SEMANTIED_LINKS_BY_CONTENT_DATE = "delete sl from semantifi_cat sc,semantifi_feeds sf, semantifi_links sl"
                                                                                                                  + " where sl.feed_id = sf.id and sf.cat_id = sc.id "
                                                                                                                  + "and sc.name = '<contextId>' and sl.pubdate <= '<contentDate>'";

   @Override
   public void refreshRIFeatureContents (Long contextId) throws PlatformException {
      try {
         // populate RIFeatureContent by contextId
         List<RIFeatureContent> riFeatureContents = getUnstructuredWarehouseRetrievalService()
                  .populateRIFeatureContents(contextId);
         // delete existing RIFeatureContent by contextId
         getUnstructuredWarehouseManagementService().deleteRIFeatureContentByContextId(contextId);
         // populate the field name for solr
         for (RIFeatureContent featureContent : riFeatureContents) {
            populateFacetFieldName(featureContent);
         }
         // create RIFeatureContent
         getUnstructuredWarehouseManagementService().createRIFeatureContents(contextId, riFeatureContents);
      } catch (UnstructuredWarehouseException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   public void populateFacetFieldName (RIFeatureContent featureContent) {
      SolrFieldConfigurationInfo solrFieldConfigurationInfo = getDataAccessServicesConfigurationService()
               .getSolrFieldConfigurationInfo();
      Long featureId = featureContent.getFeatureId();
      CheckType multiValued = featureContent.getMultiValued();
      FeatureValueType featureValueType = featureContent.getFeatureValueType();
      String fieldName = featureId.toString();
      if (CheckType.YES.equals(multiValued)) {
         fieldName += solrFieldConfigurationInfo.getMultivaluedSeperatorColumnSuffix();
      } else {
         if (FeatureValueType.VALUE_NUMBER.equals(featureValueType)) {
            fieldName += solrFieldConfigurationInfo.getNumberColumnSuffix();
         } else if (FeatureValueType.VALUE_STRING.equals(featureValueType)) {
            fieldName += solrFieldConfigurationInfo.getStringColumnSuffix();
         }
      }
      featureContent.setFieldName(fieldName);
   }

   public IDataAccessServicesConfigurationService getDataAccessServicesConfigurationService () {
      return dataAccessServicesConfigurationService;
   }

   public void setDataAccessServicesConfigurationService (
            IDataAccessServicesConfigurationService dataAccessServicesConfigurationService) {
      this.dataAccessServicesConfigurationService = dataAccessServicesConfigurationService;
   }

   /**
    * @return the unstructuredWarehouseRetrievalService
    */
   public IUnstructuredWarehouseRetrievalService getUnstructuredWarehouseRetrievalService () {
      return unstructuredWarehouseRetrievalService;
   }

   /**
    * @param unstructuredWarehouseRetrievalService the unstructuredWarehouseRetrievalService to set
    */
   public void setUnstructuredWarehouseRetrievalService (
            IUnstructuredWarehouseRetrievalService unstructuredWarehouseRetrievalService) {
      this.unstructuredWarehouseRetrievalService = unstructuredWarehouseRetrievalService;
   }

   /**
    * @return the unstructuredWarehouseManagementService
    */
   public IUnstructuredWarehouseManagementService getUnstructuredWarehouseManagementService () {
      return unstructuredWarehouseManagementService;
   }

   /**
    * @param unstructuredWarehouseManagementService the unstructuredWarehouseManagementService to set
    */
   public void setUnstructuredWarehouseManagementService (
            IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService) {
      this.unstructuredWarehouseManagementService = unstructuredWarehouseManagementService;
   }

   /**
    * @return the sdxRetrievalService
    */
   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   /**
    * @param sdxRetrievalService the sdxRetrievalService to set
    */
   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   /**
    * @return the systemDataAccessService
    */
   public ISystemDataAccessService getSystemDataAccessService () {
      return systemDataAccessService;
   }

   /**
    * @param systemDataAccessService the systemDataAccessService to set
    */
   public void setSystemDataAccessService (ISystemDataAccessService systemDataAccessService) {
      this.systemDataAccessService = systemDataAccessService;
   }

}
