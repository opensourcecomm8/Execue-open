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


package com.execue.handler.reports;

import com.execue.core.common.bean.AssetResult;
import com.execue.core.exception.HandlerException;
import com.execue.handler.IHandler;

public interface IReportsHandler extends IHandler {

   public String getReportStatus (Long messageId) throws HandlerException;

   public AssetResult getReportSummaryDetails (AssetResult assetResult, long queryId) throws HandlerException;
}
