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


package com.execue.web.core.action.swi;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.execue.core.common.bean.ApplicationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.bean.UIApplicationModelInfo;
import com.execue.handler.swi.IApplicationServiceHandler;
import com.execue.handler.swi.IPreferencesServiceHandler;

public class ConsoleAction extends SWIAction implements ServletRequestAware {

   private Long                         applicationId;
   private Long                         modelId;
   private String                       applicationName;
   private static final Logger          log = Logger.getLogger(ConsoleAction.class);
   private IPreferencesServiceHandler   preferencesServiceHandler;
   private List<Asset>                  assets;
   private IApplicationServiceHandler   applicationServiceHandler;
   private List<UIApplicationModelInfo> applications;
   private HttpServletRequest           servletRequest;
   private String                       actionName;
   private String                       appSourceType;

   // action methods
   @Override
   public String input () {
      try {
         setAssets(getSdxServiceHandler().getAllAssets(applicationId));
         applications = applicationServiceHandler.getAllApplications();
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   public String setApplicationDetails () {
      if (getApplicationId() != null) {
         ApplicationContext applicationContext = ExecueBeanManagementUtil.prepareApplicationContext(getModelId(),
                  getApplicationId(), getApplicationName(), null, null, AppSourceType.getType(getAppSourceType()));
         setApplicationContext(applicationContext);
      } else if (getApplicationContext() != null) {
         // removing from session in case the user selects back "select app"
         getHttpSession().remove("APPLICATION");
      }
      return SUCCESS;
   }

   @Override
   public IPreferencesServiceHandler getPreferencesServiceHandler () {
      return preferencesServiceHandler;
   }

   @Override
   public void setPreferencesServiceHandler (IPreferencesServiceHandler preferencesServiceHandler) {
      this.preferencesServiceHandler = preferencesServiceHandler;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public List<UIApplicationModelInfo> getApplications () {
      return applications;
   }

   public void setApplications (List<UIApplicationModelInfo> applications) {
      this.applications = applications;
   }

   public IApplicationServiceHandler getApplicationServiceHandler () {
      return applicationServiceHandler;
   }

   public void setApplicationServiceHandler (IApplicationServiceHandler applicationServiceHandler) {
      this.applicationServiceHandler = applicationServiceHandler;
   }

   public String getApplicationName () {
      return applicationName;
   }

   public void setApplicationName (String applicationName) {
      this.applicationName = applicationName;
   }

   public HttpServletRequest getServletRequest () {
      return servletRequest;
   }

   public void setServletRequest (HttpServletRequest servletRequest) {
      this.servletRequest = servletRequest;
   }

   public String getActionName () {
      return actionName;
   }

   public void setActionName (String actionName) {
      this.actionName = actionName;
   }

   /**
    * @return the appSourceType
    */
   public String getAppSourceType () {
      return appSourceType;
   }

   /**
    * @param appSourceType
    *           the appSourceType to set
    */
   public void setAppSourceType (String appSourceType) {
      this.appSourceType = appSourceType;
   }

}
