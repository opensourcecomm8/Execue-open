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

import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileInfoDetails;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.swi.exception.PublishedFileException;

public interface IPublishedFileManagementService {

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

   public void deletePublishedFileDetails (Long fileId) throws PublishedFileException;

   public void deletePublishedFileTableDetails (Long fileId) throws PublishedFileException;

}
