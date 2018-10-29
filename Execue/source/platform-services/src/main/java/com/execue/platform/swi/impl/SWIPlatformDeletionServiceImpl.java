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


package com.execue.platform.swi.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.helper.PublishedFileJDBCHelper;
import com.execue.platform.swi.ISWIPlatformDeletionService;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IPublishedFileManagementService;
import com.execue.swi.service.IPublishedFileRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class SWIPlatformDeletionServiceImpl implements ISWIPlatformDeletionService {

   private ISDXRetrievalService            sdxRetrievalService;
   private IPublishedFileRetrievalService  publishedFileRetrievalService;
   private IPublishedFileManagementService publishedFileManagementService;
   private PublishedFileJDBCHelper         publishedFileJDBCHelper;

   public void deleteUploadedDataset (PublishedFileInfo publishedFileInfo) throws SWIException {
      try {
         deleteUploadedFile(publishedFileInfo.getFileLocation());

         Long dataSourceId = publishedFileInfo.getDatasourceId();

         List<PublishedFileTableInfo> publishedFileTableInfoList = getPublishedFileRetrievalService()
                  .getPublishedFileTableInfoByFileId(publishedFileInfo.getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(publishedFileTableInfoList)) {
            List<String> tableNames = new ArrayList<String>();
            for (PublishedFileTableInfo publishedFileTableInfo : publishedFileTableInfoList) {
               if (!StringUtils.isBlank(publishedFileTableInfo.getTempTableName())) {
                  tableNames.add(publishedFileTableInfo.getTempTableName());
               }
               if (!StringUtils.isBlank(publishedFileTableInfo.getEvaluatedTableName())) {
                  tableNames.add(publishedFileTableInfo.getEvaluatedTableName());
               }
            }
            deleteUploadedTables(dataSourceId, tableNames);
         }

         getPublishedFileManagementService().deletePublishedFileTableDetails(publishedFileInfo.getId());
         getPublishedFileManagementService().deletePublishedFileDetails(publishedFileInfo.getId());
      } catch (PublishedFileException publishedFileException) {
         throw new SWIException(publishedFileException.getCode(), publishedFileException);
      }
   }

   private void deleteUploadedFile (String filePath) throws PublishedFileException {
      try {
         File file = new File(filePath);
         if (file.exists()) {
            file.delete();
         }
      } catch (Exception e) {
         throw new PublishedFileException(SWIExceptionCodes.UPLOADED_FILE_DELETE_FAILED, e);
      }
   }

   private void deleteUploadedTables (Long dataSourceId, List<String> tableNames) throws PublishedFileException {
      try {
         DataSource dataSource = getSdxRetrievalService().getDataSourceById(dataSourceId);
         getPublishedFileJDBCHelper().dropTables(dataSource, tableNames);
      } catch (DataAccessException e) {
         throw new PublishedFileException(e.getCode(), e);
      } catch (SDXException e) {
         throw new PublishedFileException(e.getCode(), e);
      }
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

   public IPublishedFileManagementService getPublishedFileManagementService () {
      return publishedFileManagementService;
   }

   public void setPublishedFileManagementService (IPublishedFileManagementService publishedFileManagementService) {
      this.publishedFileManagementService = publishedFileManagementService;
   }

   public PublishedFileJDBCHelper getPublishedFileJDBCHelper () {
      return publishedFileJDBCHelper;
   }

   public void setPublishedFileJDBCHelper (PublishedFileJDBCHelper publishedFileJDBCHelper) {
      this.publishedFileJDBCHelper = publishedFileJDBCHelper;
   }

}
