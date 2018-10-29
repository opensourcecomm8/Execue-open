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


package com.execue.platform.swi.operation.analysis;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.swi.AssetAnalysisReport;
import com.execue.core.common.bean.swi.AssetAnalysisReportInfo;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.platform.exception.AssetAnalysisException;

public interface IAssetAnalysisService {

   public boolean populateAssetAnalysisReport (Long assetId, PublishAssetMode publishAssetMode)
            throws AssetAnalysisException;

   public AssetAnalysisReport fetchAssetAnalysisReport (Long assetId) throws AssetAnalysisException;

   public AssetAnalysisReportInfo analyseAssetForGrain (Asset asset) throws AssetAnalysisException;

   public AssetAnalysisReportInfo analyseColumnWithoutColumnType (Asset asset) throws AssetAnalysisException;

   public AssetAnalysisReportInfo analyseLookupTablesWithoutMembers (Asset asset) throws AssetAnalysisException;

   public AssetAnalysisReportInfo analyseTablesWithoutDefaultMetrics (Asset asset) throws AssetAnalysisException;

   public AssetAnalysisReportInfo analyseTablesWithoutJoins (Asset asset) throws AssetAnalysisException;

   public AssetAnalysisReportInfo analyseUnmappedColumns (Asset asset) throws AssetAnalysisException;

   public AssetAnalysisReportInfo analyseUnmappedMembers (Asset asset) throws AssetAnalysisException;

}
