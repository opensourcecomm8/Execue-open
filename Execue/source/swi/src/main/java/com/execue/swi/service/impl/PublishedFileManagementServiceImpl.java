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


package com.execue.swi.service.impl;

import java.util.List;

import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileInfoDetails;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.dataaccess.IPublishedFileDataAccessManager;
import com.execue.swi.service.IPublishedFileManagementService;
import com.execue.swi.service.IPublishedFileRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class PublishedFileManagementServiceImpl implements IPublishedFileManagementService {

   private IPublishedFileDataAccessManager publishedFileDataAccessManager;

   private IPublishedFileRetrievalService  publishedFileRetrievalService;

   private ISDXRetrievalService            sdxRetrievalService;

   public void persistPublishedFileInfo (PublishedFileInfo publishedFileInfo) throws PublishedFileException {
      publishedFileDataAccessManager.persistPublishedFileInfo(publishedFileInfo);
   }

   public void updatePublishedFileInfo (PublishedFileInfo publishedFileInfo) throws PublishedFileException {
      publishedFileDataAccessManager.updatePublishedFileInfo(publishedFileInfo);
   }

   public void persistPublishedFileInfoDetails (List<PublishedFileInfoDetails> publishedFileInfoDetails)
            throws PublishedFileException {
      publishedFileDataAccessManager.persistPublishedFileInfoDetails(publishedFileInfoDetails);
   }

   public void updatePublishedFileInfoDetails (List<PublishedFileInfoDetails> PublishedFileInfoDetails)
            throws PublishedFileException {
      publishedFileDataAccessManager.updatePublishedFileInfoDetails(PublishedFileInfoDetails);
   }

   public void persistPublishedFileTableInfo (List<PublishedFileTableInfo> publishedFileTableInfos)
            throws PublishedFileException {
      publishedFileDataAccessManager.persistPublishedFileTableInfo(publishedFileTableInfos);
   }

   public void persistPublishedFileTableInfo (PublishedFileTableInfo publishedFileTableInfo)
            throws PublishedFileException {
      publishedFileDataAccessManager.persistPublishedFileTableInfo(publishedFileTableInfo);
   }

   public void updatePublishedFileTableInfo (List<PublishedFileTableInfo> publishedFileTableInfos)
            throws PublishedFileException {
      publishedFileDataAccessManager.updatePublishedFileTableInfo(publishedFileTableInfos);
   }

   public void updatePublishedFileTableInfo (PublishedFileTableInfo publishedFileTableInfo)
            throws PublishedFileException {
      publishedFileDataAccessManager.updatePublishedFileTableInfo(publishedFileTableInfo);
   }

   public void persistPublishedFileTableDetails (List<PublishedFileTableDetails> publishedFileTableDetails)
            throws PublishedFileException {
      publishedFileDataAccessManager.persistPublishedFileTableDetails(publishedFileTableDetails);
   }

   public void updatePublishedFileTableDetails (List<PublishedFileTableDetails> publishedFileTableDetails)
            throws PublishedFileException {
      publishedFileDataAccessManager.updatePublishedFileTableDetails(publishedFileTableDetails);
   }

   public void deletePublishedFileDetails (Long fileId) throws PublishedFileException {
      publishedFileDataAccessManager.deletePublishedFileDetails(fileId);
   }

   public void deletePublishedFileTableDetails (Long fileId) throws PublishedFileException {
      publishedFileDataAccessManager.deletePublishedFileTableDetails(fileId);
   }

   public IPublishedFileDataAccessManager getPublishedFileDataAccessManager () {
      return publishedFileDataAccessManager;
   }

   public void setPublishedFileDataAccessManager (IPublishedFileDataAccessManager publishedFileDataAccessManager) {
      this.publishedFileDataAccessManager = publishedFileDataAccessManager;
   }

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

}
