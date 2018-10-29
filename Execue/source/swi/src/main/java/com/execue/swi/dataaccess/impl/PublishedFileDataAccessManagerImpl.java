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


package com.execue.swi.dataaccess.impl;

import java.util.List;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileInfoDetails;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.PublishedFileException;
import com.execue.dataaccess.qdata.dao.IPublishedFileDAO;
import com.execue.swi.dataaccess.IPublishedFileDataAccessManager;

/**
 * @author JTiwari
 * @since 29/10/09
 */
public class PublishedFileDataAccessManagerImpl implements IPublishedFileDataAccessManager {

   private IPublishedFileDAO publishedFileDAO;

   public IPublishedFileDAO getPublishedFileDAO () {
      return publishedFileDAO;
   }

   public void setPublishedFileDAO (IPublishedFileDAO publishedFileDAO) {
      this.publishedFileDAO = publishedFileDAO;
   }

   public void persistPublishedFileInfo (PublishedFileInfo publishedFileInfo) throws PublishedFileException {
      try {
         publishedFileDAO.create(publishedFileInfo);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void persistPublishedFileInfoDetails (List<PublishedFileInfoDetails> publishedFileInfoDetails)
            throws PublishedFileException {
      try {
         publishedFileDAO.createAll(publishedFileInfoDetails);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void persistPublishedFileTableDetails (List<PublishedFileTableDetails> publishedFileTableDetails)
            throws PublishedFileException {
      try {
         publishedFileDAO.createAll(publishedFileTableDetails);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public void persistPublishedFileTableInfo (List<PublishedFileTableInfo> publishedFileTableInfos)
            throws PublishedFileException {
      try {
         publishedFileDAO.createAll(publishedFileTableInfos);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updatePublishedFileInfo (PublishedFileInfo publishedFileInfo) throws PublishedFileException {
      try {
         publishedFileDAO.update(publishedFileInfo);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updatePublishedFileInfoDetails (List<PublishedFileInfoDetails> publishedFileInfoDetails)
            throws PublishedFileException {
      try {
         publishedFileDAO.updateAll(publishedFileInfoDetails);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updatePublishedFileTableDetails (List<PublishedFileTableDetails> publishedFileTableDetails)
            throws PublishedFileException {
      try {
         publishedFileDAO.updateAll(publishedFileTableDetails);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updatePublishedFileTableInfo (List<PublishedFileTableInfo> publishedFileTableInfos)
            throws PublishedFileException {
      try {
         publishedFileDAO.updateAll(publishedFileTableInfos);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updatePublishedFileTableInfo (PublishedFileTableInfo publishedFileTableInfo)
            throws PublishedFileException {
      try {
         publishedFileDAO.update(publishedFileTableInfo);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<PublishedFileInfoDetails> getPublishedFileInfoDetailByFileId (Long fileId) throws PublishedFileException {
      try {
         return publishedFileDAO.getPublishedFileInfoDetailByFileId(fileId);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
   }

   public List<PublishedFileTableInfo> getPublishedFileTableInfoByFileId (Long fileId) throws PublishedFileException {
      try {
         return publishedFileDAO.getPublishedFileTableInfoByFileId(fileId);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
   }

   public List<PublishedFileTableDetails> getPublishedFileTableDetailsByTableId (Long tableId)
            throws PublishedFileException {
      try {
         return publishedFileDAO.getPublishedFileTableDetailsByTableId(tableId);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
   }

   public PublishedFileInfo getPublishedFileInfoById (Long id) throws PublishedFileException {
      try {
         return publishedFileDAO.getById(id, PublishedFileInfo.class);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
   }

   public PublishedFileTableDetails getPublishedFileTableDetailsById (Long id) throws PublishedFileException {
      try {
         return publishedFileDAO.getById(id, PublishedFileTableDetails.class);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
   }

   public PublishedFileTableInfo getPublishedFileTableInfoById (Long id) throws PublishedFileException {
      try {
         return publishedFileDAO.getById(id, PublishedFileTableInfo.class);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
   }

   public PublishedFileInfoDetails getyIdPublishedFileInfoDetailById (Long id) throws PublishedFileException {
      try {
         return publishedFileDAO.getById(id, PublishedFileInfoDetails.class);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
   }

   public void persistPublishedFileTableInfo (PublishedFileTableInfo publishedFileTableInfo)
            throws PublishedFileException {
      try {
         publishedFileDAO.create(publishedFileTableInfo);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public List<PublishedFileTableInfo> getFileTableInfoListByFileInfoId (Long fileInfoId) throws PublishedFileException {
      try {
         return getPublishedFileDAO().getFileTableInfoListByFileInfoId(fileInfoId);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<PublishedFileInfo> getPublishedFileInfoByApplicationId (Long applicationId)
            throws PublishedFileException {
      try {
         return getPublishedFileDAO().getPublishedFileInfoByApplicationId(applicationId);
      } catch (DataAccessException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public PublishedFileInfo getPublishedFileInfoByTableInfoId (Long publishedFileTableInfoId)
            throws PublishedFileException {
      try {
         return getPublishedFileDAO().getPublishedFileInfoByTableInfoId(publishedFileTableInfoId);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

   public List<PublishedFileInfo> getPublishedFilesInfoEligibleForAbsorption (Long userId)
            throws PublishedFileException {
      try {
         return getPublishedFileDAO().getPublishedFilesInfoEligibleForAbsorption(userId);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

   public List<PublishedFileInfo> getPublishedFilesInfoNotEligibleForAbsorption (Long userId)
            throws PublishedFileException {
      try {
         return getPublishedFileDAO().getPublishedFilesInfoNotEligibleForAbsorption(userId);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

   public List<PublishedFileInfo> getPublishedFileInfoByUserId (Long userId) throws PublishedFileException {
      try {
         return getPublishedFileDAO().getPublishedFileInfoByUserId(userId);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

   public boolean isFileExist (Long userId, String fileName) throws PublishedFileException {
      try {
         return getPublishedFileDAO().isFileExist(userId, fileName);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

   public List<PublishedFileTableInfo> getPublishedFileTablesInfo (Long userId, List<String> evaluatedTableNames)
            throws PublishedFileException {
      try {
         return getPublishedFileDAO().getPublishedFileTablesInfo(userId, evaluatedTableNames);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

   public List<PublishedFileInfo> getPublishedFilesByPage (Page page) throws PublishedFileException {
      try {
         return getPublishedFileDAO().getPublishedFilesByPage(page);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

   public PublishedFileTableInfo getPublishedFileTableInfo (Long userId, String evaluatedTableName)
            throws PublishedFileException {
      try {
         return getPublishedFileDAO().getPublishedFileTableInfo(userId, evaluatedTableName);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

   public void deletePublishedFileDetails (Long fileId) throws PublishedFileException {
      try {
         getPublishedFileDAO().deletePublishedFileDetails(fileId);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

   public void deletePublishedFileTableDetails (Long fileId) throws PublishedFileException {
      try {
         getPublishedFileDAO().deletePublishedFileTableDetails(fileId);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

   public List<PublishedFileTableDetails> getPublishedFileTableDetailsByPage (Long fileTableId, Page page)
            throws PublishedFileException {
      try {
         return getPublishedFileDAO().getPublishedFileTableDetailsByPage(fileTableId, page);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

   public List<String> getPublishedFileTempTableBaseColumnNamesByPage (Long fileTableId, Page page)
            throws PublishedFileException {
      try {
         return getPublishedFileDAO().getPublishedFileTempTableBaseColumnNamesByPage(fileTableId, page);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

   public List<Long> getOtherFileTableDetailIdsByDisplayName (Long fileTableDetailId, String fileTableDetailDisplayName)
            throws PublishedFileException {
      try {
         return getPublishedFileDAO().getOtherFileTableDetailIdsByDisplayName(fileTableDetailId,
                  fileTableDetailDisplayName);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

   public List<String> getColumnDisplayNames (Long fileTableId)
            throws PublishedFileException {
      try {
         return getPublishedFileDAO().getColumnDisplayNames(fileTableId);
      } catch (DataAccessException daeException) {
         throw new PublishedFileException(daeException.getCode(), daeException);
      }
   }

}
