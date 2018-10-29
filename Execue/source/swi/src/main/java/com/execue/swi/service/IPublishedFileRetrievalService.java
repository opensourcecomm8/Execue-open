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


package com.execue.swi.service;

import java.util.List;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileInfoDetails;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.swi.exception.PublishedFileException;

public interface IPublishedFileRetrievalService {

   /**
    * This method return the PublishedFileInfo object by ID
    * 
    * @param id
    * @return
    * @throws PublishedFileInfo
    */
   public PublishedFileInfo getPublishedFileInfoById (Long id) throws PublishedFileException;

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

   public List<PublishedFileTableInfo> getFileTableInfoListByFileInfoId (Long fileInfoId) throws PublishedFileException;

   public List<PublishedFileInfo> getPublishedFileInfoByApplicationId (Long applicationId)
            throws PublishedFileException;

   public List<PublishedFileInfo> getPublishedFilesInfoEligibleForAbsorption (Long userId)
            throws PublishedFileException;

   public List<PublishedFileInfo> getPublishedFilesInfoNotEligibleForAbsorption (Long userId)
            throws PublishedFileException;

   public List<PublishedFileInfo> getPublishedFileInfoByUserId (Long userId) throws PublishedFileException;

   public PublishedFileInfo getPublishedFileInfoByTableInfoId (Long publishedFileTableInfoId)
            throws PublishedFileException;

   public boolean isFileExist (Long userId, String fileName) throws PublishedFileException;

   public List<PublishedFileTableInfo> getPublishedFileTablesInfo (Long userId, List<String> evaluatedTableNames)
            throws PublishedFileException;

   public List<PublishedFileInfo> getPublishedFilesByPage (Page page) throws PublishedFileException;

   public PublishedFileTableInfo getPublishedFileTableInfo (Long userId, String evaluatedTableName)
            throws PublishedFileException;

   public List<PublishedFileTableDetails> getPublishedFileTableDetailsByPage (Long fileTableId, Page page)
            throws PublishedFileException;

   public List<String> getPublishedFileTempTableBaseColumnNamesByPage (Long fileTableId, Page page)
            throws PublishedFileException;

   public boolean isColumnDisplayNameUnique (Long fileTableDetailId, String fileTableDetailDisplayName)
            throws PublishedFileException;

   public List<String> getColumnDisplayNames (Long fileTableId) throws PublishedFileException;

}
