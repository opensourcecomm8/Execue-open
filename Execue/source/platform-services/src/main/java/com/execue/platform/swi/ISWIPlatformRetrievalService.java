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


package com.execue.platform.swi;

import java.util.List;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.swi.exception.SWIException;

public interface ISWIPlatformRetrievalService {

   public List<List<String>> getUploadedFileDataFromSource (Long fileId, Long tableId, List<String> columnNames,
            LimitEntity limitEntity, Page page) throws SWIException;

   public List<List<String>> getAssetTableDataFromSourceByPage (Long assetId, Long tableId,
            List<String> requestedColumnNames, LimitEntity limitEntity, Page page) throws SWIException;

   public boolean hasLocationBasedEntities (Long applicationId) throws SWIException;

}
