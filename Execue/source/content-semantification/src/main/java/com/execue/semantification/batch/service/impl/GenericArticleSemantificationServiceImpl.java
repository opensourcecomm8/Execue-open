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

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.unstructured.SemantifiedContent;
import com.execue.core.common.bean.entity.unstructured.SourceContent;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.unstructured.IUnstructuredWarehouseManagementWrapperService;
import com.execue.semantification.batch.service.IGenericArticleSemantificationService;
import com.execue.semantification.bean.SemantificationInput;
import com.execue.semantification.configuration.impl.SemantificationConfigurationImpl;
import com.execue.semantification.exception.SemantificationException;
import com.execue.semantification.unstructured.service.IGenericArticlePopulationService;
import com.execue.semantification.util.SemantificationUtil;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.service.IUnstructuredWarehouseManagementService;
import com.execue.uswh.service.IUnstructuredWarehouseRetrievalService;

public class GenericArticleSemantificationServiceImpl implements IGenericArticleSemantificationService {

   private static Logger                                  logger = Logger
                                                                          .getLogger(ArticleSemantificationServiceImpl.class);
   private SemantificationConfigurationImpl               semantificationConfiguration;
   private IUnstructuredWarehouseRetrievalService         unstructuredWarehouseRetrievalService;
   private IUnstructuredWarehouseManagementService        unstructuredWarehouseManagementService;
   private IGenericArticlePopulationService               classifiedArticlePopulationService;
   private IUnstructuredWarehouseManagementWrapperService unstructuredWarehouseManagementWrapperService;

   private Long                                           batchId;

   @Override
   public void populateSemantifiedArticleImages (Long contextId) {
      int batchSize = getSemantificationConfiguration().getSemantificationArticleBatchsize();
      List<Long> semantifiedContentIds = new ArrayList<Long>(1);

      try {
         // Update the existing P articles to N for the given batch id
         getUnstructuredWarehouseManagementService().updateSemantifiedContentImageURLProcessedStateByBatchId(contextId,
                  ProcessingFlagType.NOT_PROCESSED, getBatchId(), ProcessingFlagType.PROCESSING);

      } catch (UnstructuredWarehouseException unstructuredWarehouseException) {
         logger.error(unstructuredWarehouseException.getMessage());
      }

      do {
         try {
            semantifiedContentIds = getUnstructuredWarehouseRetrievalService()
                     .getSemantifiedContentIdsByImageUrlProcessedState(contextId, ProcessingFlagType.NOT_PROCESSED,
                              batchSize);

            if (ExecueCoreUtil.isCollectionEmpty(semantifiedContentIds)) {
               break;
            }
            getUnstructuredWarehouseManagementService().updateSemantifiedContentImageURLProcessedState(contextId,
                     ProcessingFlagType.PROCESSING, semantifiedContentIds.get(semantifiedContentIds.size() - 1),
                     semantifiedContentIds.get(0), getBatchId());
         } catch (UnstructuredWarehouseException unstructuredWarehouseException) {
            logger.error(unstructuredWarehouseException.getMessage());
         }
         SemantifiedContent semantifiedContent = null;
         for (Long semantifiedContentId : semantifiedContentIds) {
            try {
               semantifiedContent = getUnstructuredWarehouseRetrievalService().getSemantifiedContentById(contextId,
                        semantifiedContentId);
            } catch (UnstructuredWarehouseException e) {
               logger.error(e.getMessage());
               continue;
            }
            try {
               List<String> validImageUrls = getClassifiedArticlePopulationService().getValidImageUrls(
                        semantifiedContent);
               if (!CollectionUtils.isEmpty(validImageUrls)) {
                  semantifiedContent.setImageUrl(validImageUrls.get(0));
                  semantifiedContent.setImageUrlProcessed(ProcessingFlagType.PROCESSED);

                  // Set the image present to Y in the semantified content feature information for the given semantified content id
                  getUnstructuredWarehouseManagementService().setImagePresentInSemantifedContentFeatureInfo(contextId,
                           semantifiedContentId, CheckType.YES);
                  getUnstructuredWarehouseManagementWrapperService().updateImagePresentForFacets(contextId, semantifiedContentId, CheckType.YES);
               } else {
                  semantifiedContent.setImageUrlProcessed(ProcessingFlagType.NOT_ENOUGH_INFORMATION);
               }

            } catch (SemantificationException e) {
               logger.error(e.getMessage());
               semantifiedContent.setImageUrlProcessed(ProcessingFlagType.FAILED);

            } catch (UnstructuredWarehouseException e) {
               logger.error(e.getMessage());
               semantifiedContent.setImageUrlProcessed(ProcessingFlagType.FAILED);
            } catch (PlatformException e) {
               logger.error(e.getMessage());
               semantifiedContent.setImageUrlProcessed(ProcessingFlagType.FAILED);
            } catch (Exception exception) {
               logger.error(exception.getMessage());
               semantifiedContent.setImageUrlProcessed(ProcessingFlagType.FAILED);
            }

            try {
               // Update the semantified content
               getUnstructuredWarehouseManagementService().updateSemantifiedContent(contextId, semantifiedContent);
            } catch (UnstructuredWarehouseException e) {
               logger.error(e.getMessage());
            }
         }

      } while (batchSize >= semantifiedContentIds.size());

   }

   @Override
   public void semantifiArticles (Long contextId) {
      int batchSize = getSemantificationConfiguration().getSemantificationArticleBatchsize();

      try {
         // Update the existing P articles to N for the given batch id
         getUnstructuredWarehouseManagementService().updateSourceContentProcessedStateByBatchId(contextId,
                  getBatchId(), ProcessingFlagType.PROCESSING, ProcessingFlagType.NOT_PROCESSED);

      } catch (UnstructuredWarehouseException unstructuredWarehouseException) {
         logger.error("Unable to mark the existing Processing(P) source content as Not Processed(N)",
                  unstructuredWarehouseException);
      }

      List<Long> latestSourceContentIds = new ArrayList<Long>(1);
      do {
         try {
            // Get articles in batches while there are no more articles left for semantification
            latestSourceContentIds = getUnstructuredWarehouseRetrievalService().getLatestSourceContentIds(contextId,
                     batchSize);
            if (ExecueCoreUtil.isCollectionEmpty(latestSourceContentIds)) {
               break;
            }
            // Note: Since we are ordering by id descending, we will have the max id first and min id last
            getUnstructuredWarehouseManagementService().updateSourceContentByProcessingState(contextId, getBatchId(),
                     latestSourceContentIds.get(latestSourceContentIds.size() - 1), latestSourceContentIds.get(0),
                     ProcessingFlagType.PROCESSING);
         } catch (UnstructuredWarehouseException unstructuredWarehouseException) {
            logger.error("Unable to retrieve the latest source content for semantification",
                     unstructuredWarehouseException);
         }

         SourceContent sourceContent = null;
         for (Long sourceContentId : latestSourceContentIds) {
            try {
               sourceContent = getUnstructuredWarehouseRetrievalService().getSourceContentById(contextId,
                        sourceContentId);

               // preparing the semantificationinput from sourceContect.
               SemantificationInput semantificationInput = SemantificationUtil.getSemantificationInput(sourceContent);

               // Call the classified article population service
               getClassifiedArticlePopulationService().semantifiContent(contextId, semantificationInput);

               // Set the flag to processed and update the news item
               sourceContent.setProcessed(ProcessingFlagType.PROCESSED);
            } catch (UnstructuredWarehouseException exception) {
               logger.error("Unable to retrieve the source content with ID: " + sourceContent.getId(), exception);
               sourceContent.setFailureCause(exception.getMessage());
               sourceContent.setProcessed(ProcessingFlagType.FAILED);
            } catch (SemantificationException exception) {
               logger.error("Unable to semantify the source content with ID: " + sourceContent.getId(), exception);
               sourceContent.setFailureCause(exception.getMessage());
               sourceContent.setProcessed(ProcessingFlagType.FAILED);
            } catch (Exception exception) {
               logger.error("Unable to semantify the source content with ID: " + sourceContent.getId(), exception);
               sourceContent.setFailureCause(exception.getMessage());
               sourceContent.setProcessed(ProcessingFlagType.FAILED);
            }

            try {
               // Update the source content
               getUnstructuredWarehouseManagementService().updateSourceContent(contextId, sourceContent);
            } catch (UnstructuredWarehouseException exception) {
               logger.error("Unable to update the source content with ID: " + sourceContent.getId(), exception);
            }
         }
      } while (batchSize >= latestSourceContentIds.size());
   }

   /**
    * @return the batchId
    */
   public Long getBatchId () {
      return batchId;
   }

   /**
    * @param batchId
    *           the batchId to set
    */
   public void setBatchId (Long batchId) {
      this.batchId = batchId;
   }

   /**
    * @return the semantificationConfiguration
    */
   public SemantificationConfigurationImpl getSemantificationConfiguration () {
      return semantificationConfiguration;
   }

   /**
    * @param semantificationConfiguration
    *           the semantificationConfiguration to set
    */
   public void setSemantificationConfiguration (SemantificationConfigurationImpl semantificationConfiguration) {
      this.semantificationConfiguration = semantificationConfiguration;
   }

   /**
    * @return the classifiedArticlePopulationService
    */
   public IGenericArticlePopulationService getClassifiedArticlePopulationService () {
      return classifiedArticlePopulationService;
   }

   /**
    * @param classifiedArticlePopulationService
    *           the classifiedArticlePopulationService to set
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

   public IUnstructuredWarehouseManagementWrapperService getUnstructuredWarehouseManagementWrapperService () {
      return unstructuredWarehouseManagementWrapperService;
   }

   public void setUnstructuredWarehouseManagementWrapperService (
            IUnstructuredWarehouseManagementWrapperService unstructuredWarehouseManagementWrapperService) {
      this.unstructuredWarehouseManagementWrapperService = unstructuredWarehouseManagementWrapperService;
   }

}
