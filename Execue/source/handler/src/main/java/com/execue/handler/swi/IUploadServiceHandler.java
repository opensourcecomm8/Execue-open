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


package com.execue.handler.swi;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.core.common.bean.publisher.PublisherDataAbsorptionContext;
import com.execue.core.common.type.CSVEmptyField;
import com.execue.core.common.type.CSVStringEnclosedCharacterType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.JobType;
import com.execue.core.common.type.PublishedFileType;
import com.execue.core.common.type.PublishedOperationType;
import com.execue.core.common.type.PublisherProcessType;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.IHandler;
import com.execue.handler.bean.UIPublishedFileInfo;
import com.execue.handler.bean.UIPublishedFileTableDetail;
import com.execue.handler.bean.UIPublisherJobStatus;
import com.execue.handler.bean.UIUploadStatus;
import com.execue.security.IUserContextService;
import com.execue.swi.exception.PublishedFileException;

public interface IUploadServiceHandler extends IHandler, IUserContextService {

   public PublisherDataAbsorptionContext absorbData (PublishedFileInfo publishedFileInfo, boolean isColumnsAvailable,
            String dataDelimeter, CSVEmptyField csvEmptyField,
            CSVStringEnclosedCharacterType stringEnclosedCharacterType) throws ExeCueException;

   public Long evaluateDataset (Long jobRequestId) throws ExeCueException;

   public UIPublishedFileInfo getFileInfo (JobType jobType, String jobReqeustData) throws ExeCueException;

   public UIPublishedFileInfo getFileInfo (Long publishedFileInfoId) throws PublishedFileException;

   public Map<String, List<UIPublishedFileTableDetail>> getPublishedFileTableDetails (Long fileInfoTableId)
            throws PublishedFileException;

   public void updatePublishedFileTableDetails (Long fileTableInfoId, List<Long> selectedPopulations,
            List<Long> selectedDistributions) throws PublishedFileException;

   public List<UIPublishedFileTableDetail> getEvaluatedColumns (Long fileTableInfoId) throws PublishedFileException;

   // public List<String> updateEvaluatedColumns (Long fileTableInfoId, List<UIPublishedFileTableDetail>
   // evaluatedColumns)
   // throws PublishedFileException;

   public List<UIPublisherJobStatus> getJobRequestStatus () throws PublishedFileException;

   public Long getFileTableInfoId (Long fileInfoId) throws PublishedFileException;

   /**
    * This method returns a list of tables for a published file Id
    * 
    * @param fileId
    * @return List<PublishedFileTableInfo>
    * @throws PublishedFileException
    */
   public List<PublishedFileTableInfo> getPublishedFileTables (Long fileId) throws PublishedFileException;

   public String convertXLFileToCSV (String excelFilePath, Long applicationId, String csvFileStoragePath)
            throws PublishedFileException;

   public PublishedFileInfo saveDataFile (File sourceData, String fileName, String fileDescription,
            String originalFileName, boolean isColumnsAvailable, String dataDelimeter, CSVEmptyField csvEmptyField,
            CSVStringEnclosedCharacterType stringEnclosedCharacterType, PublishedOperationType operationType,
            PublishedFileType sourceType, String fileLink, String tags, Long applicationId, Long modelId,
            CheckType absorbDataset, PublisherProcessType publisherProcessType, CheckType uploadAsCompressedFile)
            throws PublishedFileException;

   public List<UIPublishedFileInfo> getPublishedFiles () throws ExeCueException;

   public List<UIPublishedFileInfo> getPublishedFilesByPage (Page page) throws PublishedFileException;

   public PublisherDataAbsorptionContext absorbPublishedFile (UIPublishedFileInfo uiPublishedFileInfo)
            throws ExeCueException;

   public PublisherDataAbsorptionContext absorbPublishedFile (Long publishedFileInfoId) throws ExeCueException;

   public boolean isFileExist (String fileName) throws ExeCueException;

   public PublishedFileInfo getPublishedFileInfoById (Long id) throws PublishedFileException;

   public void deleteUploadedDataset (PublishedFileInfo publishedFileInfo) throws PublishedFileException;

   public List<PublishedFileTableInfo> getPublishedFileTableInfoByFileId (Long fileId) throws PublishedFileException;

   public UIUploadStatus validatePublishedFileInfoForDeletion (UIUploadStatus operationStatus,
            PublishedFileInfo publishedFileInfo) throws PublishedFileException;

   public String getFileName (String originalFileName) throws PublishedFileException;

   public String getFileStoragePath (PublishedFileType sourceType) throws PublishedFileException;

}
