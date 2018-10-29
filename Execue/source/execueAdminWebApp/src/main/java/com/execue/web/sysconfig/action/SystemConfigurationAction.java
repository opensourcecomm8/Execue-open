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


package com.execue.web.sysconfig.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.web.core.action.swi.SWIAction;
import com.execue.web.sysconfig.action.bean.ActionStatus;
import com.execue.web.sysconfig.bean.AnswersCatalogConfigurationViewInfo;
import com.execue.web.sysconfig.bean.CommonConfigurationViewInfo;
import com.execue.web.sysconfig.bean.ReportAggregationConfigurationViewInfo;
import com.execue.web.sysconfig.bean.ReportPresentationConfigurationViewInfo;
import com.execue.web.sysconfig.handler.ISystemConfigurationHandler;

public class SystemConfigurationAction extends SWIAction {

   private static final Logger                     logger = Logger.getLogger(SystemConfigurationAction.class);

   private ISystemConfigurationHandler             systemConfigurationHandler;

   private CommonConfigurationViewInfo             commonConfig;
   private AnswersCatalogConfigurationViewInfo     acConfig;
   private ReportAggregationConfigurationViewInfo  aggConfig;
   private ReportPresentationConfigurationViewInfo repConfig;

   private ActionStatus                            actionStatus;

   public String showSystemConfiguration () {
      
      setCommonConfig(getSystemConfigurationHandler().getCommonConfigurations());
      setAcConfig(getSystemConfigurationHandler().getAnswersCatalogConfigurations());
      setAggConfig(getSystemConfigurationHandler().getReportAggregationConfigurations());
      setRepConfig(getSystemConfigurationHandler().getReportPresentationConfigurations());

      return "success";
   }

   public String saveCommonConfigurations () {
      try {
         getSystemConfigurationHandler().saveCommonConfigurations(getCommonConfig());
      } catch (Exception exp) {
         populateActionStatus("failure", "Operation failed !!!");
         return "error";
      }
      populateActionStatus();
      return "success";
   }

   public String saveAnswersCatalogConfigurations () {
      try {
         getSystemConfigurationHandler().saveAnswersCatalogConfigurations(getAcConfig());
      } catch (Exception exp) {
         populateActionStatus("failure", "Operation failed !!!");
         return "error";
      }
      populateActionStatus();
      return "success";
   }

   public String saveReportAggregationConfigurations () {
      try {
         getSystemConfigurationHandler().saveReportAggregationConfigurations(getAggConfig());
      } catch (Exception exp) {
         populateActionStatus("failure", "Operation failed !!!");
         return "error";
      }
      populateActionStatus();
      return "success";
   }

   public String saveReportPresentationConfigurations () {
      try {
         getSystemConfigurationHandler().saveReportPresentationConfigurations(getRepConfig());
      } catch (Exception exp) {
         populateActionStatus("failure", "Operation failed !!!");
         return "error";
      }
      populateActionStatus();
      return "success";
   }

   private void populateActionStatus (String status, String message) {
      actionStatus = new ActionStatus();
      actionStatus.setStatus(status);
      actionStatus.setMessage(message);
   }
   
   private void populateActionStatus () {
      actionStatus = new ActionStatus();
   }
   
   public List<String> getBooleanTypes () {
      List<String> booleanTypes = new ArrayList<String>();
      booleanTypes.add("No");
      booleanTypes.add("Yes");
      return booleanTypes;
   }
   
   public List<String> getStatisticalTypes () {
      List<String> statisticalTypes = new ArrayList<String>();
      statisticalTypes.add("Sum");
      statisticalTypes.add("Average");
      return statisticalTypes;
   }
   
   public ISystemConfigurationHandler getSystemConfigurationHandler () {
      return systemConfigurationHandler;
   }

   public void setSystemConfigurationHandler (ISystemConfigurationHandler systemConfigurationHandler) {
      this.systemConfigurationHandler = systemConfigurationHandler;
   }

   public CommonConfigurationViewInfo getCommonConfig () {
      return commonConfig;
   }

   public void setCommonConfig (CommonConfigurationViewInfo commonConfig) {
      this.commonConfig = commonConfig;
   }

   public AnswersCatalogConfigurationViewInfo getAcConfig () {
      return acConfig;
   }

   public void setAcConfig (AnswersCatalogConfigurationViewInfo acConfig) {
      this.acConfig = acConfig;
   }

   public ReportAggregationConfigurationViewInfo getAggConfig () {
      return aggConfig;
   }

   public void setAggConfig (ReportAggregationConfigurationViewInfo aggConfig) {
      this.aggConfig = aggConfig;
   }

   public ReportPresentationConfigurationViewInfo getRepConfig () {
      return repConfig;
   }

   public void setRepConfig (ReportPresentationConfigurationViewInfo repConfig) {
      this.repConfig = repConfig;
   }

   public ActionStatus getActionStatus () {
      return actionStatus;
   }

   public void setActionStatus (ActionStatus actionStatus) {
      this.actionStatus = actionStatus;
   }

}
