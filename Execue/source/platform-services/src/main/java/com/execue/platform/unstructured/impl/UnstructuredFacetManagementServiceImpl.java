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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.entity.unstructured.SemantifiedContentFeatureInformation;
import com.execue.das.solr.bean.SolrConditionEntity;
import com.execue.das.solr.bean.SolrDocument;
import com.execue.das.solr.exception.SolrException;
import com.execue.das.solr.service.ISolrManagementService;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.unstructured.IUnstructuredFacetManagementService;
import com.execue.platform.unstructured.UnstructuredFacetServiceHelper;

/**
 * @author Vishay
 */
public class UnstructuredFacetManagementServiceImpl implements IUnstructuredFacetManagementService {

   private ISolrManagementService         solrManagementService;
   private UnstructuredFacetServiceHelper unstructuredFacetServiceHelper;

   @Override
   public Integer cleanUpOldFacets (Long contextId, Date contentDate) throws PlatformException {
      List<SolrConditionEntity> conditionEntities = new ArrayList<SolrConditionEntity>();
      conditionEntities.add(getUnstructuredFacetServiceHelper().buildContextIdConditionEntity(contextId));
      conditionEntities.add(getUnstructuredFacetServiceHelper().buildContentDateUpperBoundRangeConditionEntity(
               contentDate));
      try {
         return getSolrManagementService().deleteSolrDocuments(contextId, conditionEntities);
      } catch (SolrException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public Integer deleteFacetInfoById (Long contextId, Long semantifiedContentId) throws PlatformException {
      try {
         String documentId = getUnstructuredFacetServiceHelper().buildDocumentId(contextId, semantifiedContentId);
         return getSolrManagementService().deleteSolrDocumentById(contextId, documentId);
      } catch (SolrException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public Integer storeFacetInfo (Long contextId, List<SemantifiedContentFeatureInformation> featureInfoList)
            throws PlatformException {
      SolrDocument solrDocument = getUnstructuredFacetServiceHelper().buildSolrDocument(featureInfoList);
      try {
         return getSolrManagementService().createSolrDocument(contextId, solrDocument);
      } catch (SolrException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public Integer storeFacetsInfo (Long contextId, List<List<SemantifiedContentFeatureInformation>> featureInfosList)
            throws PlatformException {
      List<SolrDocument> solrDocumentList = new ArrayList<SolrDocument>();
      for (List<SemantifiedContentFeatureInformation> featureInfoList : featureInfosList) {
         SolrDocument solrDocument = getUnstructuredFacetServiceHelper().buildSolrDocument(featureInfoList);
         solrDocumentList.add(solrDocument);
      }
      try {
         return getSolrManagementService().createSolrDocuments(contextId, solrDocumentList);
      } catch (SolrException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public Integer updateFacetInfo (Long contextId, Long existingFacetInfoId,
            List<SemantifiedContentFeatureInformation> freshFeatureInfoList) throws PlatformException {
      SolrDocument solrDocument = getUnstructuredFacetServiceHelper().buildSolrDocument(freshFeatureInfoList);
      try {
         return getSolrManagementService().updateSolrDocument(contextId, solrDocument);
      } catch (SolrException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   public ISolrManagementService getSolrManagementService () {
      return solrManagementService;
   }

   public void setSolrManagementService (ISolrManagementService solrManagementService) {
      this.solrManagementService = solrManagementService;
   }

   public UnstructuredFacetServiceHelper getUnstructuredFacetServiceHelper () {
      return unstructuredFacetServiceHelper;
   }

   public void setUnstructuredFacetServiceHelper (UnstructuredFacetServiceHelper unstructuredFacetServiceHelper) {
      this.unstructuredFacetServiceHelper = unstructuredFacetServiceHelper;
   }

}
