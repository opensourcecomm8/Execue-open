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


/**
 * 
 */
package com.execue.semantification.batch.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.qdata.NewsItem;
import com.execue.core.common.bean.qdata.UDXAttribute;
import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.type.AttributeType;
import com.execue.core.common.type.NewsCategory;
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.qdata.exception.RFXException;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.service.IRFXService;
import com.execue.qdata.service.IUDXService;
import com.execue.semantification.batch.service.IArticleSemantificationService;
import com.execue.semantification.beanfactory.ArticleSemantificationFactory;
import com.execue.semantification.configuration.impl.SemantificationConfigurationImpl;
import com.execue.semantification.exception.InvalidNewsItemException;
import com.execue.semantification.exception.SemantificationException;
import com.execue.semantification.unstructured.service.IArticlePopulationService;

/**
 * @author Nitesh
 */
public class ArticleSemantificationServiceImpl implements IArticleSemantificationService {

   private static Logger                    logger = Logger.getLogger(ArticleSemantificationServiceImpl.class);

   private IRFXService                      rfxService;
   private IUDXService                      udxService;
   private SemantificationConfigurationImpl semantificationConfiguration;
   private Long                             batchId;

   public void semantifiArticles () {
      int batchSize = getSemantificationConfiguration().getSemantificationArticleBatchsize();
      List<Long> latestNewsItemIds = new ArrayList<Long>(1);
      do {
         try {
            // Update the existing P articles to N for the given batch id
            getRfxService().updateNewsItemProcessedStateByByBatchId(ProcessingFlagType.NOT_PROCESSED, getBatchId(),
                     ProcessingFlagType.PROCESSING);

            // get articles in batches while there are no more articles left for semantification
            latestNewsItemIds = getRfxService().getLatestNewsItemIds(batchSize);
            if (ExecueCoreUtil.isCollectionEmpty(latestNewsItemIds)) {
               break;
            }
            // Note: Since we are ordering by id descending, we will have the max id first and min id last
            getRfxService().updateNewsItemProcessedState(ProcessingFlagType.PROCESSING,
                     latestNewsItemIds.get(latestNewsItemIds.size() - 1), latestNewsItemIds.get(0), getBatchId());
         } catch (RFXException rfxException) {
            // Skip and continue to next news item
            logger.error(rfxException.getMessage());
         }
         NewsItem newsItem = null;
         for (Long newsItemId : latestNewsItemIds) {
            try {
               newsItem = getRfxService().getNewsItemById(newsItemId);
               IArticlePopulationService articlePopulationService = ArticleSemantificationFactory.getInstance()
                        .getSemantificationService(newsItem.getCategory());
               articlePopulationService.semantifiNewsArticle(newsItem);
               // Set the flag to processed and update the news item
               newsItem.setProcessed(ProcessingFlagType.PROCESSED);
               getRfxService().updateNewsItem(newsItem);
            } catch (InvalidNewsItemException e) {
               // Set the flag to NOt Enough Information and update the news item
               newsItem.setProcessed(ProcessingFlagType.NOT_ENOUGH_INFORMATION);
               newsItem.setFailureCause(e.getMessage());
               try {
                  getRfxService().updateNewsItem(newsItem);
               } catch (RFXException rfxException) {
                  // Skip and continue to next news item
                  logger.error(rfxException.getMessage());
               }
            } catch (Exception exception) {
               logger.error("Failed News Item Id: " + newsItem.getId(), exception);
               newsItem.setFailureCause(exception.toString());
               newsItem.setProcessed(ProcessingFlagType.FAILED);
               try {
                  getRfxService().updateNewsItem(newsItem);
               } catch (RFXException rfxException) {
                  // Skip and continue to next news item
                  logger.error(rfxException.getMessage());
               }
            }
         }
      } while (batchSize >= latestNewsItemIds.size());
   }

   public void populateUDXImages (NewsCategory newsCategory) {
      int batchSize = getSemantificationConfiguration().getSemantificationArticleBatchsize();
      List<UnStructuredIndex> udxList = new ArrayList<UnStructuredIndex>(1);
      IArticlePopulationService articlePopulationService = ArticleSemantificationFactory.getInstance()
               .getSemantificationService(newsCategory);
      // Update the existing P articles to N for the given batch id
      try {
         getUdxService().updateUDXImageURLProcessedStateByBatchId(ProcessingFlagType.NOT_PROCESSED, getBatchId(),
                  ProcessingFlagType.PROCESSING);
      } catch (UDXException udxException) {
         udxException.printStackTrace();
         logger.error(udxException.getMessage());
      }
      do {
         try {
            udxList = getUdxService().getUnstructuredIndexesByProcessedState(ProcessingFlagType.NOT_PROCESSED,
                     batchSize, newsCategory.getValue());
            if (ExecueCoreUtil.isCollectionEmpty(udxList)) {
               break;
            }
            // Note: Since we are ordering by id descending, we will have the max id first and min id last
            getUdxService().updateUDXImageURLProcessedState(ProcessingFlagType.PROCESSING,
                     udxList.get(udxList.size() - 1).getId(), udxList.get(0).getId(), getBatchId());
         } catch (UDXException udxException) {
            udxException.printStackTrace();
            // Skip and continue to next news item
            logger.error(udxException.getMessage());
         }
         logger.debug("Batch Id :" + getBatchId() + " Batch Size :" + batchSize);

         List<Long> imagePresentUdxIds = new ArrayList<Long>(1);
         List<UnStructuredIndex> unstructuredIndexes = new ArrayList<UnStructuredIndex>(1);
         for (UnStructuredIndex unStructuredIndex : udxList) {
            logger.debug("processing UDXId :" + unStructuredIndex.getId());
            try {
               List<String> validImageUrls = articlePopulationService.getValidImageUrls(unStructuredIndex);
               if (!CollectionUtils.isEmpty(validImageUrls)) {
                  // Set the first image to udx and the rest to udx_attribute
                  unStructuredIndex.setImageUrl(validImageUrls.get(0));

                  List<UDXAttribute> udxAttributes = getUdxImageAttributes(validImageUrls.subList(1, validImageUrls
                           .size()), unStructuredIndex);
                  unStructuredIndex.setUdxAttributes(new HashSet<UDXAttribute>(udxAttributes));
                  unStructuredIndex.setImageUrlProcessed(ProcessingFlagType.PROCESSED);
                  imagePresentUdxIds.add(unStructuredIndex.getId());
               } else {
                  unStructuredIndex.setImageUrlProcessed(ProcessingFlagType.NOT_ENOUGH_INFORMATION);
               }
               unStructuredIndex.setBatchId(getBatchId());
            } catch (Exception exception) {
               exception.printStackTrace();
               unStructuredIndex.setBatchId(getBatchId());
               unStructuredIndex.setImageUrlProcessed(ProcessingFlagType.FAILED);
            }
            unstructuredIndexes.add(unStructuredIndex);
         }

         if (!CollectionUtils.isEmpty(unstructuredIndexes)) {
            try {
               getUdxService().updateUnstructuredIndexes(unstructuredIndexes);
            } catch (UDXException e) {
               e.printStackTrace();
               // Skip and continue to next batch
               logger.error(e.getMessage());
            }
         }

         if (!CollectionUtils.isEmpty(imagePresentUdxIds)) {
            try {
               articlePopulationService.updateCategorySpecificImageInformation(imagePresentUdxIds);
            } catch (SemantificationException e) {
               // Skip and continue to next batch
               logger.error(e.getMessage());
               e.printStackTrace();
            }
         }
      } while (batchSize >= udxList.size());
   }

   private List<UDXAttribute> getUdxImageAttributes (List<String> imageUrls, UnStructuredIndex udx) {
      List<UDXAttribute> udxAttributes = new ArrayList<UDXAttribute>(1);
      if (CollectionUtils.isEmpty(imageUrls)) {
         return udxAttributes;
      }
      for (String imageUrl : imageUrls) {
         UDXAttribute udxAttribute = new UDXAttribute();
         udxAttribute.setUdx(udx);
         udxAttribute.setAttributeType(AttributeType.IMAGE_URL);
         udxAttribute.setAttributeValue(imageUrl);
         udxAttributes.add(udxAttribute);
      }
      return udxAttributes;
   }

   /**
    * @return the rfxService
    */
   public IRFXService getRfxService () {
      return rfxService;
   }

   /**
    * @param rfxService
    *           the rfxService to set
    */
   public void setRfxService (IRFXService rfxService) {
      this.rfxService = rfxService;
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
    * @return the udxService
    */
   public IUDXService getUdxService () {
      return udxService;
   }

   /**
    * @param udxService
    *           the udxService to set
    */
   public void setUdxService (IUDXService udxService) {
      this.udxService = udxService;
   }
}