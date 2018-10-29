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


package com.execue.web.sysconfig.handler.impl;

import com.execue.web.sysconfig.bean.AnswersCatalogConfigurationViewInfo;
import com.execue.web.sysconfig.bean.CommonConfigurationViewInfo;
import com.execue.web.sysconfig.bean.ReportAggregationConfigurationViewInfo;
import com.execue.web.sysconfig.bean.ReportPresentationConfigurationViewInfo;
import com.execue.web.sysconfig.handler.ISystemConfigurationHandler;
import com.execue.web.sysconfig.service.ISystemConfigurationService;

public class SystemConfigurationHandlerImpl implements ISystemConfigurationHandler {

   private ISystemConfigurationService systemConfigurationService;

   @Override
   public AnswersCatalogConfigurationViewInfo getAnswersCatalogConfigurations () {
      return getSystemConfigurationService().getAnswersCatalogConfigurations();
   }

   @Override
   public CommonConfigurationViewInfo getCommonConfigurations () {
      return getSystemConfigurationService().getCommonConfigurations();
   }

   @Override
   public ReportAggregationConfigurationViewInfo getReportAggregationConfigurations () {
      return getSystemConfigurationService().getReportAggregationConfigurations();
   }

   @Override
   public ReportPresentationConfigurationViewInfo getReportPresentationConfigurations () {
      return getSystemConfigurationService().getReportPresentationConfigurations();
   }

   @Override
   public void saveAnswersCatalogConfigurations (AnswersCatalogConfigurationViewInfo answersCatalogConfoguration) {
      getSystemConfigurationService().saveAnswersCatalogConfigurations(answersCatalogConfoguration);
   }

   @Override
   public void saveCommonConfigurations (CommonConfigurationViewInfo commonConfoguration) {
      getSystemConfigurationService().saveCommonConfigurations(commonConfoguration);
   }

   @Override
   public void saveReportAggregationConfigurations (
            ReportAggregationConfigurationViewInfo reportAggregationConfoguration) {
      getSystemConfigurationService().saveReportAggregationConfigurations(reportAggregationConfoguration);
   }

   @Override
   public void saveReportPresentationConfigurations (
            ReportPresentationConfigurationViewInfo reportPresentationConfoguration) {
      getSystemConfigurationService().saveReportPresentationConfigurations(reportPresentationConfoguration);
   }

   public ISystemConfigurationService getSystemConfigurationService () {
      return systemConfigurationService;
   }

   public void setSystemConfigurationService (ISystemConfigurationService systemConfigurationService) {
      this.systemConfigurationService = systemConfigurationService;
   }

}
