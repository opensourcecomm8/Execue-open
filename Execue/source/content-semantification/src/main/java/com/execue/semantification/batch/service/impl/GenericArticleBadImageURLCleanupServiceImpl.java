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


package com.execue.semantification.batch.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.type.CheckType;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.unstructured.IUnstructuredWarehouseManagementWrapperService;
import com.execue.semantification.batch.service.IGenericArticleBadImageURLCleanupService;
import com.execue.semantification.configuration.ISemantificationConfiguration;
import com.execue.semantification.exception.SemantificationException;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.service.IUnstructuredWarehouseManagementService;
import com.execue.uswh.service.IUnstructuredWarehouseRetrievalService;

public class GenericArticleBadImageURLCleanupServiceImpl implements IGenericArticleBadImageURLCleanupService {

   private static final Logger                            log = Logger
                                                                       .getLogger(GenericArticleBadImageURLCleanupServiceImpl.class);

   private ISemantificationConfiguration                  semantificationConfiguration;
   private IUnstructuredWarehouseRetrievalService         unstructuredWarehouseRetrievalService;
   private IUnstructuredWarehouseManagementService        unstructuredWarehouseManagementService;
   private IUnstructuredWarehouseManagementWrapperService unstructuredWarehouseManagementWrapperService;

   /**
    * Get the list of malware marked domains form configuration For each of the domain, 
    *    pull the ids from semantified content table forwhich the image url is form these domains.
    * Accumulate all the semantified content ids.
    * For each of the Semantified Content id,
    *    Update image url to null on Semantified Content.
    *    Update the image present flag to 'N' on SCFI and Solr Document
    * 
    * @throws CraigslistException
    */
   public void cleanupArticleBadImageURLs (Long contextId) throws SemantificationException {
      List<String> badImageURLDomainNames = getSemantificationConfiguration().getBadImageURLDomainNames();
      List<Long> semantifiedContentIdsFromBadDomains = new ArrayList<Long>();
      try {
         for (String badImageURLDomainName : badImageURLDomainNames) {

            semantifiedContentIdsFromBadDomains.addAll(getUnstructuredWarehouseRetrievalService()
                     .getSemantifiedContentIdsByBadImageUrlLikeDomain(contextId, badImageURLDomainName));

         }

         for (Long scIdForBadImageURLArticle : semantifiedContentIdsFromBadDomains) {

            // Update Semantified Content's Image URL to NULL for this id
            getUnstructuredWarehouseManagementService().setImageURLToNullInSemantifiedContent(contextId,
                     scIdForBadImageURLArticle);
            // Update Semantified Content Feature Info's Image Present Flag to 'N' for this id
            getUnstructuredWarehouseManagementService().setImagePresentInSemantifedContentFeatureInfo(contextId,
                     scIdForBadImageURLArticle, CheckType.NO);
            // Update Solr Document's image present flag to 'N' for this id
            getUnstructuredWarehouseManagementWrapperService().updateImagePresentForFacets(contextId,
                     scIdForBadImageURLArticle, CheckType.NO);

         }
      } catch (UnstructuredWarehouseException uswhException) {
         throw new SemantificationException(uswhException.getCode(), uswhException);
      } catch (PlatformException platformException) {
         throw new SemantificationException(platformException.getCode(), platformException);
      }

   }

   public ISemantificationConfiguration getSemantificationConfiguration () {
      return semantificationConfiguration;
   }

   public void setSemantificationConfiguration (ISemantificationConfiguration semantificationConfiguration) {
      this.semantificationConfiguration = semantificationConfiguration;
   }

   public IUnstructuredWarehouseRetrievalService getUnstructuredWarehouseRetrievalService () {
      return unstructuredWarehouseRetrievalService;
   }

   public void setUnstructuredWarehouseRetrievalService (
            IUnstructuredWarehouseRetrievalService unstructuredWarehouseRetrievalService) {
      this.unstructuredWarehouseRetrievalService = unstructuredWarehouseRetrievalService;
   }

   public IUnstructuredWarehouseManagementService getUnstructuredWarehouseManagementService () {
      return unstructuredWarehouseManagementService;
   }

   public void setUnstructuredWarehouseManagementService (
            IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService) {
      this.unstructuredWarehouseManagementService = unstructuredWarehouseManagementService;
   }

   public IUnstructuredWarehouseManagementWrapperService getUnstructuredWarehouseManagementWrapperService () {
      return unstructuredWarehouseManagementWrapperService;
   }

   public void setUnstructuredWarehouseManagementWrapperService (
            IUnstructuredWarehouseManagementWrapperService unstructuredWarehouseManagementWrapperService) {
      this.unstructuredWarehouseManagementWrapperService = unstructuredWarehouseManagementWrapperService;
   }

}
