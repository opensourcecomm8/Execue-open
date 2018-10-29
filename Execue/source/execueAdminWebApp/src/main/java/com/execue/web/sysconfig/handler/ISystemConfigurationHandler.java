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


package com.execue.web.sysconfig.handler;

import com.execue.web.sysconfig.bean.AnswersCatalogConfigurationViewInfo;
import com.execue.web.sysconfig.bean.CommonConfigurationViewInfo;
import com.execue.web.sysconfig.bean.ReportAggregationConfigurationViewInfo;
import com.execue.web.sysconfig.bean.ReportPresentationConfigurationViewInfo;

public interface ISystemConfigurationHandler {

   public CommonConfigurationViewInfo getCommonConfigurations ();

   public AnswersCatalogConfigurationViewInfo getAnswersCatalogConfigurations ();

   public ReportAggregationConfigurationViewInfo getReportAggregationConfigurations ();

   public ReportPresentationConfigurationViewInfo getReportPresentationConfigurations ();

   public void saveCommonConfigurations (CommonConfigurationViewInfo commonConfoguration);

   public void saveAnswersCatalogConfigurations (AnswersCatalogConfigurationViewInfo answersCatalogConfoguration);

   public void saveReportAggregationConfigurations (ReportAggregationConfigurationViewInfo reportAggregationConfoguration);

   public void saveReportPresentationConfigurations (ReportPresentationConfigurationViewInfo reportPresentationConfoguration);
}
