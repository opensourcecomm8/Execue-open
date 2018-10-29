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


package com.execue.semantification.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.type.NewsCategory;
import com.execue.core.exception.SystemException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.qdata.exception.RFXException;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.service.IRFXService;
import com.execue.qdata.service.IUDXService;
import com.execue.semantification.configuration.impl.SemantificationConfigurationImpl;
import com.execue.semantification.exception.ArticleMaintainanceException;
import com.execue.semantification.unstructured.service.IArticleMaintenanceService;

public class BaseArticleMaintenanceService implements IArticleMaintenanceService {

   private static Logger                    logger = Logger.getLogger(BaseArticleMaintenanceService.class);
   private IRFXService                      rfxService;
   private IUDXService                      udxService;
   private SemantificationConfigurationImpl semantificationConfiguration;

   public void cleanupOldNewsArticleTrace (String contentSourceType) {
      int newsItemCleanupDays = getSemantificationConfiguration().getSemantificationArticleCleanupDays();
      Date contentDate = getCleanupDate(newsItemCleanupDays);

      try {
         udxService.deleteRIUnstructuredIndexesByContentDate(contentDate, contentSourceType);
         udxService.deleteUdxAttributesByContentDate(contentDate);
         rfxService.deleteRFXValuesByContentDate(contentDate);
         rfxService.deleteRFXByContentDate(contentDate);
         udxService.deleteUDXByContentDate(contentDate, contentSourceType);
         cleanupCategorySpecificOldNewsArticleTrace(contentDate);
      } catch (UDXException swiException) {
         throw new SystemException(swiException.getCode(), swiException);
      } catch (RFXException rfxException) {
         throw new SystemException(rfxException.getCode(), rfxException);
      } catch (ArticleMaintainanceException e) {
         throw new SystemException(e.getCode(), e);
      }
   }

   public void cleanupInvalidNewsArticleTrace (NewsCategory newsItemCategory) {
      int batchSize = getSemantificationConfiguration().getSemantificationArticleBatchsize();
      Long batchNum = 1L;
      List<UnStructuredIndex> udxList = new ArrayList<UnStructuredIndex>(1);
      do {
         try {
            List<Long> invalidUDXIds = null;
            invalidUDXIds = new ArrayList<Long>();

            udxList = getUdxService().getUnstructuredIndexesByContentSourceType(newsItemCategory.getValue(), batchNum,
                     batchSize);
            if (ExecueCoreUtil.isCollectionEmpty(udxList)) {
               break;
            }
            for (UnStructuredIndex unStructuredIndex : udxList) {
               String url = unStructuredIndex.getUrl();
               if (!isValidURL(url)) {
                  invalidUDXIds.add(unStructuredIndex.getId());
               }
            }
            if (!CollectionUtils.isEmpty(invalidUDXIds)) {
               udxService.deleteRIUnstructuredIndexesByUdxIds(invalidUDXIds);
               udxService.deleteUdxAttributesByUdxIds(invalidUDXIds);
               rfxService.deleteRFXValuesByUdxIds(invalidUDXIds);
               rfxService.deleteRFXByUdxIds(invalidUDXIds);
               udxService.deleteUDXById(invalidUDXIds);
               cleanupCategorySpecificInvalidArticleTrace(invalidUDXIds);
            }
         } catch (UDXException swiException) {
            // Skip and continue to next news item
            logger.error(swiException.getMessage());
         } catch (IOException ioException) {
            // Skip and continue to next news item
            logger.error(ioException.getMessage());
         } catch (RFXException rfxException) {
            // Skip and continue to next news item
            logger.error(rfxException.getMessage());
         } catch (Exception exception) {
            // Skip and continue to next news item
            logger.error(exception.getMessage());
         } finally {
            batchNum++;
         }
      } while (batchSize >= udxList.size());
   }

   protected void cleanupCategorySpecificInvalidArticleTrace (List<Long> invalidUDXIds)
            throws ArticleMaintainanceException {
      // Do nothing
   }

   protected void cleanupCategorySpecificOldNewsArticleTrace (Date contentDate) throws ArticleMaintainanceException {
      // Do nothing
   }

   private Date getCleanupDate (int newsItemCleanupDays) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -newsItemCleanupDays);
      return cal.getTime();
   }

   private boolean isValidURL (String url) throws IOException {
      List<String> invalidArticleTexts = getSemantificationConfiguration().getInvalidArticleTexts();
      BufferedReader bufferedReader = null;
      boolean isValidURL = true;
      try {
         URL srcURL = new URL(url);
         URLConnection connection = srcURL.openConnection();
         InputStream inputStream = connection.getInputStream();
         bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
         String line = null;
         while ((line = bufferedReader.readLine()) != null) {
            for (String invalidArticle : invalidArticleTexts) {
               if (line.contains(invalidArticle)) {
                  isValidURL = false;
               }
            }
            if (!isValidURL) {
               break;
            }
         }
         logger.info("Valid url check : " + isValidURL);
         return isValidURL;
      } finally {
         if (bufferedReader != null) {
            bufferedReader.close();
         }
      }
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

}
