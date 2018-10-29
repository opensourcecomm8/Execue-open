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


package com.execue.swi.dataaccess;

import java.util.List;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileInfoDetails;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.swi.exception.PublishedFileException;

/**
 * @author JTiwari
 * @since 29/10/09
 */
public interface IPublishedFileDataAccessManager {

   /**
    * This method return the PublishedFileInfo object by ID
    * 
    * @param id
    * @return
    * @throws PublishedFileInfo
    */
   public PublishedFileInfo getPublishedFileInfoById (Long id) throws PublishedFileException;

   /**
    * This method return the PublishedFileInfoDetails object by ID
    * 
    * @param id
    * @return
    * @throws PublishedFileInfoDetails
    */

   public PublishedFileInfoDetails getyIdPublishedFileInfoDetailById (Long id) throws PublishedFileException;

   /**
    * This method return the PublishedFileTableInfo object by ID
    * 
    * @param id
    * @return
    * @throws PublishedFileTableInfo
    */

   public PublishedFileTableInfo getPublishedFileTableInfoById (Long id) throws PublishedFileException;

   /**
    * This method return the PublishedFileTableDetails object by ID
    * 
    * @param id
    * @return
    * @throws PublishedFileTableDetails
    */
   public PublishedFileTableDetails getPublishedFileTableDetailsById (Long id) throws PublishedFileException;

   /**
    * This method return the list of PublishedFileInfoDetails by fileID(FK)
    * 
    * @param fileId
    * @return
    * @throws PublishedFileException
    */
   public List<PublishedFileInfoDetails> getPublishedFileInfoDetailByFileId (Long fileId) throws PublishedFileException;

   /**
    * This method return the list of PublishedFileTableInfo by fileID(FK)
    * 
    * @param fileId
    * @return
    * @throws PublishedFileException
    */

   public List<PublishedFileTableInfo> getPublishedFileTableInfoByFileId (Long fileId) throws PublishedFileException;

   /**
    * This method return the list of PublishedFileTableInfo by tableId(FK)
    * 
    * @param tableId
    * @return
    * @throws PublishedFileException
    */
   public List<PublishedFileTableDetails> getPublishedFileTableDetailsByTableId (Long tableId)
            throws PublishedFileException;

   /**
    * This method use to persist the PublishedFileInfo object.
    * 
    * @param publishedFileInfo
    * @throws PublishedFileException
    */
   public void persistPublishedFileInfo (PublishedFileInfo publishedFileInfo) throws PublishedFileException;

   /**
    * This method use to update the PublishedFileInfo object.
    * 
    * @param publishedFileInfo
    * @throws PublishedFileException
    */
   public void updatePublishedFileInfo (PublishedFileInfo publishedFileInfo) throws PublishedFileException;

   /**
    * This method use to persist the PublishedFileInfoDetails object.
    * 
    * @param publishedFileInfoDetail
    * @throws PublishedFileException
    */
   public void persistPublishedFileInfoDetails (List<PublishedFileInfoDetails> publishedFileInfoDetails)
            throws PublishedFileException;

   /**
    * This method use to update the PublishedFileInfoDetails object.
    * 
    * @param publishedFileInfoDetail
    * @throws PublishedFileException
    */
   public void updatePublishedFileInfoDetails (List<PublishedFileInfoDetails> publishedFileInfoDetails)
            throws PublishedFileException;

   /**
    * This method use to persist the PublishedFileTableInfo object.
    * 
    * @param publishedFileTableInfo
    * @throws PublishedFileException
    */
   public void persistPublishedFileTableInfo (List<PublishedFileTableInfo> publishedFileTableInfos)
            throws PublishedFileException;

   /**
    * This method use to persist the PublishedFileTableInfo object.
    * 
    * @param publishedFileTableInfo
    * @throws PublishedFileException
    */
   public void persistPublishedFileTableInfo (PublishedFileTableInfo publishedFileTableInfo)
            throws PublishedFileException;

   /**
    * This method use to update the PublishedFileTableInfo object.
    * 
    * @param publishedFileTableInfo
    * @throws PublishedFileException
    */
   public void updatePublishedFileTableInfo (List<PublishedFileTableInfo> publishedFileTableInfos)
            throws PublishedFileException;

   public void updatePublishedFileTableInfo (PublishedFileTableInfo publishedFileTableInfo)
            throws PublishedFileException;

   /**
    * This method use to persist the PublishedFileTableDetails object.
    * 
    * @param publishedFileTableDetail
    * @throws PublishedFileException
    */
   public void persistPublishedFileTableDetails (List<PublishedFileTableDetails> publishedFileTableDetails)
            throws PublishedFileException;

   /**
    * This method use to update the PublishedFileTableDetails object.
    * 
    * @param publishedFileTableDetail
    * @throws PublishedFileException
    */
   public void updatePublishedFileTableDetails (List<PublishedFileTableDetails> publishedFileTableDetails)
            throws PublishedFileException;

   public List<PublishedFileTableInfo> getFileTableInfoListByFileInfoId (Long fileInfoId) throws PublishedFileException;

   public List<PublishedFileInfo> getPublishedFileInfoByApplicationId (Long applicationId)
            throws PublishedFileException;

   public PublishedFileInfo getPublishedFileInfoByTableInfoId (Long publishedFileTableInfoId)
            throws PublishedFileException;

   public List<PublishedFileInfo> getPublishedFilesInfoEligibleForAbsorption (Long userId)
            throws PublishedFileException;

   public List<PublishedFileInfo> getPublishedFilesInfoNotEligibleForAbsorption (Long userId)
            throws PublishedFileException;

   public List<PublishedFileInfo> getPublishedFileInfoByUserId (Long userId) throws PublishedFileException;

   public boolean isFileExist (Long userId, String fileName) throws PublishedFileException;

   public List<PublishedFileTableInfo> getPublishedFileTablesInfo (Long userId, List<String> evaluatedTableNames)
            throws PublishedFileException;

   public List<PublishedFileInfo> getPublishedFilesByPage (Page page) throws PublishedFileException;

   public PublishedFileTableInfo getPublishedFileTableInfo (Long userId, String evaluatedTableName)
            throws PublishedFileException;

   public void deletePublishedFileDetails (Long fileId) throws PublishedFileException;

   public void deletePublishedFileTableDetails (Long fileId) throws PublishedFileException;

   public List<PublishedFileTableDetails> getPublishedFileTableDetailsByPage (Long fileTableId, Page page)
            throws PublishedFileException;

   public List<String> getPublishedFileTempTableBaseColumnNamesByPage (Long fileTableId, Page page)
            throws PublishedFileException;

   public List<Long> getOtherFileTableDetailIdsByDisplayName (Long fileTableDetailId, String fileTableDetailDisplayName)
            throws PublishedFileException;

   public List<String> getColumnDisplayNames (Long fileTableDetailId)
            throws PublishedFileException;

}
