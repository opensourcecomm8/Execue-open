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


package com.execue.handler.presentation;

import java.util.List;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.qdata.QDataCachedReportResults;
import com.execue.core.common.bean.reports.prsntion.HierarchicalReportInfo;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.grid.UIColumnGrid;
import com.execue.handler.bean.grid.UIDataBrowserGrid;

/**
 * @author John Mallavalli
 */
public interface IPresentationHandler {

   public String getTkrNameByAggregatedQuery (Long aggQueryId, Long applicationId) throws HandlerException;

   public String processReportRequest (Long aggregateQueryId) throws HandlerException;

   public List<UIDataBrowserGrid> processReportRequest (Long aggregateQueryId, Page pageDetail) throws HandlerException;

   public UIColumnGrid processDetailReportHeaderRequest (Long aggregateQueryId) throws HandlerException;

   public QDataCachedReportResults getCachedReportResultsById (Long cachedReportId) throws HandlerException;

   public boolean getConfigurationRevertToDefaultGridFlag () throws HandlerException;

   public HierarchicalReportInfo getHierarchicalReportInfo (Long aggregateQueryId) throws HandlerException;

   public String getReportXMLData (Long aggregateQueryId) throws HandlerException;
}
