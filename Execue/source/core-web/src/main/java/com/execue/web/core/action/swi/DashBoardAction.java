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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIPublishedFileInfo;
import com.execue.handler.swi.IDashBoardServiceHandler;
import com.execue.web.core.util.ExecueWebConstants;

@SuppressWarnings ("serial")
public class DashBoardAction extends SWIAction {

   private static final Logger       log                            = Logger.getLogger(DashBoardAction.class);
   private SDXStatus                 sdxStatus;
   List<UIPublishedFileInfo>         uiPublishedFileInfoList        = new ArrayList<UIPublishedFileInfo>();
   List<DataSource>                  dataSources                    = new ArrayList<DataSource>();
   List<Application>                 applications                   = new ArrayList<Application>();
   List<Asset>                       assets                         = new ArrayList<Asset>();
   List<Concept>                     concepts                       = new ArrayList<Concept>();
   Long                              applicationId                  = null;
   boolean                           applicationContextAvailability = false;
   boolean                           selectApp                      = false;
   private IDashBoardServiceHandler  dashBoardServiceHandler;
   private List<UIPublishedFileInfo> publishedFilesInfo             = new ArrayList<UIPublishedFileInfo>();
   private CheckType                 isPublisher                    = CheckType.NO;

   public String getAssetDashBoardDetails () {
      try {
         dataSources = getDashBoardServiceHandler().getDataSources();
         // publishedFilesInfo = getUploadServiceHandler().getPublishedFiles();
      } catch (ExeCueException exeCueException) {
         exeCueException.printStackTrace();
         sdxStatus.setMessage(exeCueException.getMessage());
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   public String getAppDashBoardDetails () {
      try {
         applications = getDashBoardServiceHandler().getApplications();        
         if(ExecueCoreUtil.isCollectionNotEmpty(applications)){
            for (Application application : applications) {
               if (!PublishAssetMode.NONE.equals(application.getPublishMode())) {
                  setIsPublisher(CheckType.YES);
                  break;
               }
            }
         }else{
            applications = new ArrayList<Application>();
         }
         
      } catch (ExeCueException exeCueException) {
         sdxStatus.setMessage(exeCueException.getMessage());
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   public String selectApp () {
      selectApp = true;
      return getAppDashBoardDetails();
   }

   public String getKnowledgeDashBoardDetails () {
      try {
         applicationId = getApplicationContext().getAppId();
         List<Model> models = getDashBoardServiceHandler().getModelsByApplicationId(applicationId);
         concepts = getDashBoardServiceHandler().getConceptsByPopularity(models.get(0).getId());
         log.debug("Concepts size  ::" + concepts.size());

      } catch (ExeCueException exeCueException) {
         sdxStatus.setMessage(exeCueException.getMessage());
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   public boolean isEligibleForAdvancedMenu () {
      boolean eligibleForAdvancedMenu = false;
      for (String role : getUserRoles()) {
         if (ExecueWebConstants.ADMIN_ROLE.equals(role) ||
                  ExecueWebConstants.ADVANCED_PUBLISHER_ROLE.equals(role)) {
            eligibleForAdvancedMenu = true;
         }
      }
      return eligibleForAdvancedMenu;
   }
   
   public SDXStatus getSdxStatus () {
      return sdxStatus;
   }

   public void setSdxStatus (SDXStatus sdxStatus) {
      this.sdxStatus = sdxStatus;
   }

   public List<Application> getApplications () {
      return applications;
   }

   public void setApplications (List<Application> applications) {
      this.applications = applications;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   public List<Concept> getConcepts () {
      return concepts;
   }

   public void setConcepts (List<Concept> concepts) {
      this.concepts = concepts;
   }

   public List<DataSource> getDataSources () {
      return dataSources;
   }

   public void setDataSources (List<DataSource> dataSources) {
      this.dataSources = dataSources;
   }

   public boolean isApplicationContextAvailability () {
      return applicationContextAvailability;
   }

   public void setApplicationContextAvailability (boolean applicationContextAvailability) {
      this.applicationContextAvailability = applicationContextAvailability;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public boolean getSelectApp () {
      return selectApp;
   }

   public void setSelectApp (boolean selectApp) {
      this.selectApp = selectApp;
   }

   public IDashBoardServiceHandler getDashBoardServiceHandler () {
      return dashBoardServiceHandler;
   }

   public void setDashBoardServiceHandler (IDashBoardServiceHandler dashBoardServiceHandler) {
      this.dashBoardServiceHandler = dashBoardServiceHandler;
   }

   public List<UIPublishedFileInfo> getPublishedFilesInfo () {
      return publishedFilesInfo;
   }

   public void setPublishedFilesInfo (List<UIPublishedFileInfo> publishedFilesInfo) {
      this.publishedFilesInfo = publishedFilesInfo;
   }

   public CheckType getIsPublisher () {
      return isPublisher;
   }

   public void setIsPublisher (CheckType isPublisher) {
      this.isPublisher = isPublisher;
   }

}
