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

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileInfoDetails;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.swi.exception.PublishedFileException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.dataaccess.IPublishedFileDataAccessManager;
import com.execue.swi.service.IPublishedFileRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class PublishedFileRetrievalServiceImpl implements IPublishedFileRetrievalService {

   private IPublishedFileDataAccessManager publishedFileDataAccessManager;

   private ISDXRetrievalService            sdxRetrievalService;

   public PublishedFileInfo getPublishedFileInfoById (Long id) throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFileInfoById(id);
   }

   public PublishedFileTableInfo getPublishedFileTableInfoById (Long id) throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFileTableInfoById(id);
   }

   public PublishedFileTableDetails getPublishedFileTableDetailsById (Long id) throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFileTableDetailsById(id);
   }

   public List<PublishedFileInfoDetails> getPublishedFileInfoDetailByFileId (Long fileId) throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFileInfoDetailByFileId(fileId);
   }

   public List<PublishedFileTableInfo> getPublishedFileTableInfoByFileId (Long fileId) throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFileTableInfoByFileId(fileId);
   }

   public List<PublishedFileTableDetails> getPublishedFileTableDetailsByTableId (Long tableId)
            throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFileTableDetailsByTableId(tableId);
   }

   public List<PublishedFileTableInfo> getFileTableInfoListByFileInfoId (Long fileInfoId) throws PublishedFileException {
      return getPublishedFileDataAccessManager().getFileTableInfoListByFileInfoId(fileInfoId);
   }

   public List<PublishedFileInfo> getPublishedFileInfoByApplicationId (Long applicationId)
            throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFileInfoByApplicationId(applicationId);
   }

   public List<PublishedFileInfo> getPublishedFilesInfoEligibleForAbsorption (Long userId)
            throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFilesInfoEligibleForAbsorption(userId);
   }

   public List<PublishedFileInfo> getPublishedFilesInfoNotEligibleForAbsorption (Long userId)
            throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFilesInfoNotEligibleForAbsorption(userId);
   }

   public List<PublishedFileInfo> getPublishedFileInfoByUserId (Long userId) throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFileInfoByUserId(userId);
   }

   public PublishedFileInfo getPublishedFileInfoByTableInfoId (Long publishedFileTableInfoId)
            throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFileInfoByTableInfoId(publishedFileTableInfoId);
   }

   public boolean isFileExist (Long userId, String fileName) throws PublishedFileException {
      return getPublishedFileDataAccessManager().isFileExist(userId, fileName);
   }

   public List<PublishedFileTableInfo> getPublishedFileTablesInfo (Long userId, List<String> evaluatedTableNames)
            throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFileTablesInfo(userId, evaluatedTableNames);
   }

   public List<PublishedFileInfo> getPublishedFilesByPage (Page page) throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFilesByPage(page);
   }

   public PublishedFileTableInfo getPublishedFileTableInfo (Long userId, String evaluatedTableName)
            throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFileTableInfo(userId, evaluatedTableName);
   }

   public List<PublishedFileTableDetails> getPublishedFileTableDetailsByPage (Long fileTableId, Page page)
            throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFileTableDetailsByPage(fileTableId, page);
   }

   public List<String> getPublishedFileTempTableBaseColumnNamesByPage (Long fileTableId, Page page)
            throws PublishedFileException {
      return getPublishedFileDataAccessManager().getPublishedFileTempTableBaseColumnNamesByPage(fileTableId, page);
   }

   public boolean isColumnDisplayNameUnique (Long fileTableDetailId, String fileTableDetailDisplayName)
            throws PublishedFileException {
      boolean columnDisplayNameUnique = true;
      List<Long> otherFileTableDetailIdsByDisplayName = getPublishedFileDataAccessManager()
               .getOtherFileTableDetailIdsByDisplayName(fileTableDetailId, fileTableDetailDisplayName);
      if (ExecueCoreUtil.isCollectionNotEmpty(otherFileTableDetailIdsByDisplayName)) {
         columnDisplayNameUnique = false;
      }
      return columnDisplayNameUnique;
   }

   public List<String> getColumnDisplayNames (Long fileTableId) throws PublishedFileException {
      return getPublishedFileDataAccessManager().getColumnDisplayNames(fileTableId);
   }

   public IPublishedFileDataAccessManager getPublishedFileDataAccessManager () {
      return publishedFileDataAccessManager;
   }

   public void setPublishedFileDataAccessManager (IPublishedFileDataAccessManager publishedFileDataAccessManager) {
      this.publishedFileDataAccessManager = publishedFileDataAccessManager;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

}
