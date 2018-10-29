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

import java.util.List;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.IHandler;
import com.execue.handler.UIPublishedFileColumnInfo;
import com.execue.handler.UIPublishedFileEvaluatedColumnDetail;
import com.execue.handler.bean.UIMember;
import com.execue.handler.bean.UITable;
import com.execue.swi.exception.PublishedFileException;

public interface IPublishedFileColumnEvaluationServiceHandler extends IHandler {

   public List<UIPublishedFileEvaluatedColumnDetail> getEvaluatedColumns (Long fileTableInfoId)
            throws PublishedFileException;

   public List<PublishedFileTableInfo> getPublishedFileTables (Long fileId) throws PublishedFileException;

   public List<String> updateEvaluatedColumns (Long fileTableInfoId,
            List<UIPublishedFileEvaluatedColumnDetail> evaluatedColumns) throws PublishedFileException;

   public List<String> updateAssetTableColumns (Long modelId, Long assetId, Long tableId,
            List<UIPublishedFileEvaluatedColumnDetail> evaluatedColumns) throws ExeCueException;

   public void updateAssetTableMembers (Long modelId, List<UIMember> members) throws ExeCueException;

   public UIPublishedFileColumnInfo getModifiedUIPublishedFileColumnInfo (
            UIPublishedFileColumnInfo uiPublishedFileColumnInfo) throws PublishedFileException;

   public List<UIPublishedFileEvaluatedColumnDetail> getEvaluatedColumnsByPage (Long fileTableId, Page pageDetail)
            throws PublishedFileException;

   public List<UIPublishedFileEvaluatedColumnDetail> getAssetTableColumns (Long assetId, Long tableId)
            throws ExeCueException;

   public List<UIPublishedFileEvaluatedColumnDetail> getAssetTableColumnsByPage (Long assetId, Long tableId, Page page)
            throws ExeCueException;

   public List<UIMember> getAssetTableMembersByPage (Long assetId, Long tableId, Page page) throws ExeCueException;

   public List<List<String>> getUploadedFileDataFromSource (Long fileId, Long tableId, Page metaPageDetail,
            Page pageDetail) throws ExeCueException;

   public List<List<String>> getAssetTableDataFromSourceByPage (Long assetId, Long tableId, Page metaPageDetail,
            Page pageDetail) throws ExeCueException;

   public List<UITable> getAssetLookupTables (Long assetId) throws ExeCueException;

   public Asset getApplicationAsset (Long applicationId) throws ExeCueException;

   public PublishedFileInfo getApplicationPublishedFileInfo (Long applicationId) throws ExeCueException;

   public PublishedFileInfo getPublishedFileInfoByFileId (Long fileId) throws ExeCueException;

}
