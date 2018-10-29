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


package com.execue.report.configuration.service;

import com.execue.core.common.bean.reports.view.data.ChartAppearanceMeta;
import com.execue.core.common.bean.reports.view.data.GridAppearanceMeta;

public interface IPresentationConfigurationService {

   public String AXIS_LABEL_MAX_LENGTH = "reports.configuration.axis-label-max-length";

   public ChartAppearanceMeta getChartLayoutObject ();

   public GridAppearanceMeta getGridLayoutObject ();

   public boolean isRevertToDefaultGriid ();

   public boolean isSaveReportsToDB ();

   public String getImageRetrievalAction ();

   public String getFirstSeriesOnLeftAxis ();

   public String getBarSize ();

   public String getRuleTitle ();

   public String getSameColoredMeasure ();

   public String getDistinctLegends ();

   public Integer getAxisLabelMaxLength ();

   public String getImageLocation ();

}
