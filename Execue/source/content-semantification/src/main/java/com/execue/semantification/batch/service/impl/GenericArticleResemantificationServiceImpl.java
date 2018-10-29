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

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.unstructured.SemantifiedContent;
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.semantification.batch.service.IGenericArticleResemantificationService;
import com.execue.semantification.bean.SemantificationInput;
import com.execue.semantification.exception.SemantificationException;
import com.execue.semantification.unstructured.service.IGenericArticlePopulationService;
import com.execue.semantification.util.SemantificationUtil;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.service.IUnstructuredWarehouseManagementService;
import com.execue.uswh.service.IUnstructuredWarehouseRetrievalService;

public class GenericArticleResemantificationServiceImpl implements IGenericArticleResemantificationService {

   private static Logger                           logger = Logger
                                                                   .getLogger(GenericArticleResemantificationServiceImpl.class);
   private IGenericArticlePopulationService        classifiedArticlePopulationService;
   private IUnstructuredWarehouseRetrievalService  unstructuredWarehouseRetrievalService;
   private IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService;

   @Override
   public void semantifiArticles (Long contextId, Long userQueryId) {
      try {
         List<Long> semantifiedContentIds = getUnstructuredWarehouseRetrievalService()
                  .getSemantifiedContentIdsByUserQueryId(contextId, userQueryId);
         SemantifiedContent semantifiedContent = null;
         for (Long semantifiedContentId : semantifiedContentIds) {
            semantifiedContent = getUnstructuredWarehouseRetrievalService().getSemantifiedContentById(contextId,
                     semantifiedContentId);

            SemantificationInput semantificationInput = SemantificationUtil
                     .getSemantificationInputForExistingSemantifiedContent(semantifiedContent);
            try {
               getClassifiedArticlePopulationService().semantifiContent(contextId, semantificationInput);
            } catch (SemantificationException semantificationException) {
               logger.error("Failed while resemantifying: " + semantifiedContent.getId(), semantificationException);
            }
         }

         getUnstructuredWarehouseManagementService().updateSemantifiedContentProcessedStateByUserQueryId(contextId,
                  userQueryId, ProcessingFlagType.PROCESSED);
      } catch (UnstructuredWarehouseException unstructuredWarehouseException) {
         logger.error(unstructuredWarehouseException.getMessage());
      }
   }

   /**
    * @return the classifiedArticlePopulationService
    */
   public IGenericArticlePopulationService getClassifiedArticlePopulationService () {
      return classifiedArticlePopulationService;
   }

   /**
    * @param classifiedArticlePopulationService the classifiedArticlePopulationService to set
    */
   public void setClassifiedArticlePopulationService (
            IGenericArticlePopulationService classifiedArticlePopulationService) {
      this.classifiedArticlePopulationService = classifiedArticlePopulationService;
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

}
