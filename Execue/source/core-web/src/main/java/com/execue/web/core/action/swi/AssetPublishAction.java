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

import com.execue.core.common.bean.ApplicationContext;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.swi.AssetAnalysisReport;
import com.execue.core.common.bean.swi.AssetAnalysisReportInfo;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.AssetAnalysisOperationType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIJobRequestResult;
import com.execue.handler.bean.UIJobRequestStatus;
import com.execue.handler.swi.IApplicationServiceHandler;
import com.execue.handler.swi.IAssetPublishServiceHandler;
import com.execue.handler.swi.ISDXServiceHandler;

/**
 * @author jitendra
 */
public class AssetPublishAction extends SWIAction {

   /**
    * 
    */
   private static final long                serialVersionUID               = 1L;
   private Long                             selectedAssetId;
   private PublishAssetMode                 publishMode;
   private UIJobRequestResult               jobRequestResult;
   private UIJobRequestStatus               jobRequest;
   private Long                             jobRequestId;
   private List<AssetAnalysisOperationType> assetAnalysisOperationTypes;
   private AssetAnalysisOperationType       assetAnalysisOperationType;
   private AssetAnalysisReportInfo          assetAnalysisReportInfo;
   private List<Asset>                      assets;
   private IAssetPublishServiceHandler      assetPublishServiceHandler;
   private ISDXServiceHandler               sdxServiceHandler;
   private CheckType                        isAssetAnalysisReportExists    = CheckType.NO;
   private CheckType                        isAssetAnalysisReportGenerated = CheckType.NO;
   private CheckType                        runAssetAnalysisReoprt         = CheckType.YES;
   private static final String              ASSET_ANALYSIS_REPORT_KEY      = "assetAnalysisReport";
   private String                           appName;
   private Long                             applicationId;
   private IApplicationServiceHandler       applicationServiceHandler;
   private AppSourceType                    appSourceType;

   // Action methods

   public String showPublish () {
      try {
         if (getApplicationId() == null) {
            setApplicationId(getApplicationContext().getAppId());
         }
         adjustApplicationContext();
      } catch (Exception e) {
         return ERROR;
      }
      return SUCCESS;
   }

   @Override
   public String input () {
      try {
         if (getApplicationId() == null) {
            setApplicationId(getApplicationContext().getAppId());
         }
         adjustApplicationContext();
         assets = sdxServiceHandler.getAllAssets(getApplicationContext().getAppId());
         appName = getApplicationContext().getApplicationName();
         applicationId = getApplicationContext().getAppId();
         appSourceType = getApplicationContext().getAppSourceType();
         if (ExecueCoreUtil.isCollectionNotEmpty(assets)) {
            selectedAssetId = assets.get(0).getId();
            getAssetAnalysisReportByAssetId();
         }
      } catch (ExeCueException e) {
         return ERROR;
      }
      return SUCCESS;
   }

   public String list () {
      try {
         AssetAnalysisReport assetAnalysisReport = fetchAssetAnalysisReport();
         populateAssetAnalysisOperationTypes(assetAnalysisReport);
         if (assetAnalysisReport.getSelectedOperationalType() != null) {
            assetAnalysisOperationType = assetAnalysisReport.getSelectedOperationalType();
         }
      } catch (HandlerException handlerException) {
         return ERROR;
      }
      return SUCCESS;
   }

   public String showExistingReport () {
      try {
         AssetAnalysisReport assetAnalysisReport = fetchAssetAnalysisReport();
         assetAnalysisReport.setSelectedPublishAssetMode(publishMode);
         populateAssetAnalysisOperationTypes(assetAnalysisReport);
         setAssetIntoContext();
      } catch (HandlerException handlerException) {
         return ERROR;
      }
      return SUCCESS;
   }

   public String showPublishDataset () {
      try {
         setAssets(sdxServiceHandler.getAllAssets(getApplicationContext().getAppId()));
         AssetAnalysisReport assetAnalysisReport = fetchAssetAnalysisReport();
         setPublishMode(assetAnalysisReport.getSelectedPublishAssetMode());
         setSelectedAssetId(assetAnalysisReport.getOperationAsset().getId());
         setRunAssetAnalysisReoprt(CheckType.NO);
         setIsAssetAnalysisReportExists(CheckType.YES);
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String getAssetAnalysisReportByOperationType () {
      try {
         AssetAnalysisReport assetAnalysisReport = fetchAssetAnalysisReport();
         assetAnalysisReport.setSelectedOperationalType(assetAnalysisOperationType);
         setAssetAnalysisReportInfo(getAssetAnalysisReport(assetAnalysisReport));
      } catch (HandlerException handlerException) {
         return ERROR;
      }
      return assetAnalysisOperationType.getValue();
   }

   public String generateAssetAnalysisReport () {
      try {
         isAssetAnalysisReportGenerated = ExecueBeanUtil.getCorrespondingCheckTypeValue(assetPublishServiceHandler
                  .populateAssetAnalysisReport(selectedAssetId, publishMode));
      } catch (HandlerException e) {
         return ERROR;
      }
      return SUCCESS;
   }

   public String getAssetAnalysisReportByAssetId () {
      try {
         AssetAnalysisReport assetAnalysisReport = getAssetPublishServiceHandler().fetchAssetAnalysisReport(
                  selectedAssetId);
         if (assetAnalysisReport != null) {
            getHttpSession().put(ASSET_ANALYSIS_REPORT_KEY, assetAnalysisReport);
            setIsAssetAnalysisReportExists(CheckType.YES);
         }
      } catch (HandlerException e) {
         return ERROR;
      }

      return SUCCESS;
   }

   public String invokePublishAssetsMaintenanceJob () {
      try {
         jobRequestResult = new UIJobRequestResult();
         Long applicationId = getApplicationContext().getAppId();
         Long modelId = getApplicationContext().getModelId();

         if (ExecueCoreUtil.isEmpty(appName)) {
            jobRequestResult.setErrMsg(getText("execue.asset.publish.app.name.error"));
            return SUCCESS;
         }
         if (updateApplication()) {
            jobRequestResult.setErrMsg(getText("execue.asset.publish.app.exists.error"));
            return SUCCESS;
         }

         // TODO : -RG- Publish is feasible if the asset entities are mapped at least one of them is mapped,
         // else it should not be requested for publishing.
         if (assetPublishServiceHandler.checkPublishEligibility(applicationId, selectedAssetId)) {
            Long jobRequestId = assetPublishServiceHandler.schedulePublishAssetsJob(applicationId, modelId,
                     selectedAssetId, publishMode);
            setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
            getJobRequestResult().setStatus(getJobRequest().getJobStatus());
            getJobRequestResult().setJobId(getJobRequest().getId());

         } else {
            getJobRequestResult().setErrMsg(getText("execue.asset.publish.mappings.required"));
            return SUCCESS;
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
         getJobRequestResult().setErrMsg(getText("execue.asset.publish.error"));
         getJobRequestResult().setStatus(JobStatus.FAILURE);
         return ERROR;
      }
      return SUCCESS;
   }

   private boolean updateApplication () throws HandlerException {
      // if old name is same as new name, do nothing(no update)
      // if name is different, then check if the application with new name exists
      // if yes, show error message and dont publish further
      // if no, set the new name in application and update

      boolean isApplicationExists = false;
      Application application = getApplicationServiceHandler().getApplicationById(getApplicationContext().getAppId());
      String oldAppName = application.getName();
      String newAppName = appName.trim();
      if (!oldAppName.equalsIgnoreCase(newAppName)) {
         if (getApplicationServiceHandler().isApplicationExist(newAppName)) {
            isApplicationExists = true;
         } else {
            application.setName(newAppName);
            getApplicationServiceHandler().updateApplication(application);
            setAppContext(application);

         }
      }

      return isApplicationExists;
   }

   private void setAppContext (Application application) throws HandlerException {
      List<Model> models = applicationServiceHandler.getModelsByApplicationId(application.getId());
      if (ExecueCoreUtil.isCollectionNotEmpty(models)) {
         ApplicationContext applicationContext = ExecueBeanManagementUtil.prepareApplicationContext(models.get(0)
                  .getId(), application.getId(), application.getName(), null, null, application.getSourceType());
         setApplicationContext(applicationContext);
      }
   }

   private void setAssetIntoContext () {
      if (getApplicationContext().getAssetId() == null) {
         getApplicationContext().setAssetId(selectedAssetId);
      } else if (!getApplicationContext().getAssetId().equals(selectedAssetId)) {
         getApplicationContext().setAssetId(selectedAssetId);
      }
   }

   private AssetAnalysisReportInfo getAssetAnalysisReport (AssetAnalysisReport assetAnalysisReport) {
      AssetAnalysisReportInfo matchedAssetAnalysisReport = null;
      for (AssetAnalysisReportInfo assetAnalysisReportInfo : assetAnalysisReport.getAssetAnalysisReportInfoList()) {
         if (assetAnalysisOperationType.equals(assetAnalysisReportInfo.getAssetAnalysisOperationType())) {
            matchedAssetAnalysisReport = assetAnalysisReportInfo;
            break;
         }
      }
      return matchedAssetAnalysisReport;
   }

   private AssetAnalysisReport fetchAssetAnalysisReport () throws HandlerException {
      AssetAnalysisReport assetAnalysisReport = (AssetAnalysisReport) getHttpSession().get(ASSET_ANALYSIS_REPORT_KEY);
      if (assetAnalysisReport == null) {
         assetAnalysisReport = getAssetPublishServiceHandler().fetchAssetAnalysisReport(selectedAssetId);
         getHttpSession().put(ASSET_ANALYSIS_REPORT_KEY, assetAnalysisReport);
      } else if (selectedAssetId != null && !assetAnalysisReport.getOperationAsset().getId().equals(selectedAssetId)) {
         getHttpSession().remove(ASSET_ANALYSIS_REPORT_KEY);
         assetAnalysisReport = getAssetPublishServiceHandler().fetchAssetAnalysisReport(selectedAssetId);
         getHttpSession().put(ASSET_ANALYSIS_REPORT_KEY, assetAnalysisReport);
      }
      return assetAnalysisReport;
   }

   private void populateAssetAnalysisOperationTypes (AssetAnalysisReport assetAnalysisReport) {
      assetAnalysisOperationTypes = new ArrayList<AssetAnalysisOperationType>();
      for (AssetAnalysisReportInfo assetAnalysisReportInfo : assetAnalysisReport.getAssetAnalysisReportInfoList()) {
         assetAnalysisOperationTypes.add(assetAnalysisReportInfo.getAssetAnalysisOperationType());
      }
   }

   private void adjustApplicationContext () {
      try {
         ApplicationContext ctx = getApplicationContext();
         if (ctx != null && getApplicationId().equals(ctx.getAppId())) {
            return;
         } else {
            Application application = getApplicationServiceHandler().getApplicationById(getApplicationId());
            Model model = getApplicationServiceHandler().getModelsByApplicationId(application.getId()).get(0);
            ApplicationContext applicationContext = ExecueBeanManagementUtil.prepareApplicationContext(model.getId(),
                     application.getId(), application.getName(), null, null, application.getSourceType());
            setApplicationContext(applicationContext);
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   public Long getSelectedAssetId () {
      return selectedAssetId;
   }

   public void setSelectedAssetId (Long selectedAssetId) {
      this.selectedAssetId = selectedAssetId;
   }

   public PublishAssetMode getPublishMode () {
      return publishMode;
   }

   public void setPublishMode (PublishAssetMode publishMode) {
      this.publishMode = publishMode;
   }

   public UIJobRequestResult getJobRequestResult () {
      return jobRequestResult;
   }

   public void setJobRequestResult (UIJobRequestResult jobRequestResult) {
      this.jobRequestResult = jobRequestResult;
   }

   public IAssetPublishServiceHandler getAssetPublishServiceHandler () {
      return assetPublishServiceHandler;
   }

   public void setAssetPublishServiceHandler (IAssetPublishServiceHandler assetPublishServiceHandler) {
      this.assetPublishServiceHandler = assetPublishServiceHandler;
   }

   public UIJobRequestStatus getJobRequest () {
      return jobRequest;
   }

   public void setJobRequest (UIJobRequestStatus jobRequest) {
      this.jobRequest = jobRequest;
   }

   public Long getJobRequestId () {
      return jobRequestId;
   }

   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   public List<AssetAnalysisOperationType> getAssetAnalysisOperationTypes () {
      return assetAnalysisOperationTypes;
   }

   public void setAssetAnalysisOperationTypes (List<AssetAnalysisOperationType> assetAnalysisOperationTypes) {
      this.assetAnalysisOperationTypes = assetAnalysisOperationTypes;
   }

   public AssetAnalysisOperationType getAssetAnalysisOperationType () {
      return assetAnalysisOperationType;
   }

   public void setAssetAnalysisOperationType (AssetAnalysisOperationType assetAnalysisOperationType) {
      this.assetAnalysisOperationType = assetAnalysisOperationType;
   }

   public AssetAnalysisReportInfo getAssetAnalysisReportInfo () {
      return assetAnalysisReportInfo;
   }

   public void setAssetAnalysisReportInfo (AssetAnalysisReportInfo assetAnalysisReportInfo) {
      this.assetAnalysisReportInfo = assetAnalysisReportInfo;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   @Override
   public ISDXServiceHandler getSdxServiceHandler () {
      return sdxServiceHandler;
   }

   @Override
   public void setSdxServiceHandler (ISDXServiceHandler sdxServiceHandler) {
      this.sdxServiceHandler = sdxServiceHandler;
   }

   public CheckType getIsAssetAnalysisReportExists () {
      return isAssetAnalysisReportExists;
   }

   public void setIsAssetAnalysisReportExists (CheckType isAssetAnalysisReportExists) {
      this.isAssetAnalysisReportExists = isAssetAnalysisReportExists;
   }

   public CheckType getRunAssetAnalysisReoprt () {
      return runAssetAnalysisReoprt;
   }

   public void setRunAssetAnalysisReoprt (CheckType runAssetAnalysisReoprt) {
      this.runAssetAnalysisReoprt = runAssetAnalysisReoprt;
   }

   public CheckType getIsAssetAnalysisReportGenerated () {
      return isAssetAnalysisReportGenerated;
   }

   public void setIsAssetAnalysisReportGenerated (CheckType isAssetAnalysisReportGenerated) {
      this.isAssetAnalysisReportGenerated = isAssetAnalysisReportGenerated;
   }

   public String getAppName () {
      return appName;
   }

   public void setAppName (String appName) {
      this.appName = appName;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public IApplicationServiceHandler getApplicationServiceHandler () {
      return applicationServiceHandler;
   }

   public void setApplicationServiceHandler (IApplicationServiceHandler applicationServiceHandler) {
      this.applicationServiceHandler = applicationServiceHandler;
   }

   /**
    * @return the appSourceType
    */
   public AppSourceType getAppSourceType () {
      return appSourceType;
   }

   /**
    * @param appSourceType
    *           the appSourceType to set
    */
   public void setAppSourceType (AppSourceType appSourceType) {
      this.appSourceType = appSourceType;
   }

}
