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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.OperationRequestLevel;
import com.execue.core.common.type.SyncRequestLevel;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIJobHistory;
import com.execue.handler.bean.UIJobRequestResult;
import com.execue.handler.bean.UIJobRequestStatus;
import com.execue.handler.bean.UIPublishedFileInfo;
import com.execue.handler.swi.IJobMaintenanceServiceHandler;
import com.execue.handler.swi.ISDXServiceHandler;

/**
 * @author JTiwari
 * @since 05/01/2010
 */
public class JobMaintenanceAction extends SWIAction {

   /**
    * 
    */
   private static final long             serialVersionUID    = 1L;
   private static final Logger           log                 = Logger.getLogger(JobMaintenanceAction.class);

   private File                          sourceData;
   private String                        sourceDataContentType;
   private String                        sourceDataFileName;
   private boolean                       generateSFLTerms    = true;
   private boolean                       generateRIOntoterms = true;
   private UIPublishedFileInfo           fileInfo;
   private UIJobRequestStatus            jobRequest;
   private List<UIJobHistory>            jobStatusSteps;
   private Long                          jobRequestId;
   private UIJobRequestStatus            evaluationJobRequest;
   private Long                          evaluationJobRequestId;
   private IJobMaintenanceServiceHandler jobMaintenanceServiceHandler;
   private ISDXServiceHandler            sdxServiceHandler;
   private List<Asset>                   assets;
   private List<Long>                    assetIds;
   private String                        modelName;
   private List<Model>                   models;
   private Long                          selectedModelId;
   private Long                          selectedAssetId;
   private UIJobRequestResult            jobRequestResult;
   private SyncRequestLevel              syncRequestLevel;
   private OperationRequestLevel         operationRequestLevel;
   private Long                          assetId;

   // TODO : this variable is here only to support invokeUploadedFileAbsorption() method
   private Long                          publishedFileInfoId;

   // action

   public String list () {
      try {
         if (getApplicationContext().getAssetId() != null) {
            selectedAssetId = getApplicationContext().getAssetId();
         }
         assets = sdxServiceHandler.getAllAssets(getApplicationContext().getAppId());
         models = getKdxServiceHandler().getModelsByApplicationId(getApplicationContext().getAppId());
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String showParentAssets () {
      try {
         if (getApplicationContext().getAssetId() != null) {
            selectedAssetId = getApplicationContext().getAssetId();
         }
         assets = sdxServiceHandler.getAllParentAssets(getApplicationContext().getAppId());
         models = getKdxServiceHandler().getModelsByApplicationId(getApplicationContext().getAppId());
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String showModelName () {
      try {
         Long modelId = getApplicationContext().getModelId();
         modelName = getKdxServiceHandler().getModel(modelId).getName();
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String absorbFileOntology () {
      try {
         String filePath = saveDataFile(getSourceData(), "OWL");
         Long jobRequestId = jobMaintenanceServiceHandler.absorbFileOntology(filePath, generateSFLTerms,
                  generateRIOntoterms, getApplicationContext().getModelId(), getKdxServiceHandler().getDefaultAppCloud(
                           getApplicationContext().getModelId()).getId());
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         addActionMessage("Upload Successful");
         showJobStatus();
      } catch (ExeCueException exeCueException) {
         exeCueException.printStackTrace();
         log.error(exeCueException, exeCueException);
         addActionError("An error occurred while importing source data. Please contact customer support.");
         return ERROR;
      } catch (Exception exception) {
         exception.printStackTrace();
         addActionError("An error occurred while importing source data. Please contact customer support.");
         return ERROR;
      }
      return SUCCESS;
   }

   public String associateConceptWithType () {
      try {
         String filePath = saveDataFile(getSourceData(), "OTHER");
         Long jobRequestId = jobMaintenanceServiceHandler.associateConceptWithType(filePath, getApplicationContext()
                  .getModelId());
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));

         addActionMessage("Upload Successful");
         showJobStatus();
      } catch (ExeCueException exeCueException) {
         exeCueException.printStackTrace();
         log.error(exeCueException, exeCueException);
         addActionError("An error occurred while importing source data. Please contact customer support.");
         return ERROR;
      } catch (Exception exception) {
         exception.printStackTrace();
         addActionError("An error occurred while importing source data. Please contact customer support.");
         return ERROR;
      }
      return SUCCESS;
   }

   public String showJobStatus () {
      if (log.isDebugEnabled()) {
         log.debug("Job Request Id : " + jobRequest.getId());
      }
      try {
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(getJobRequest().getId()));
         if (JobStatus.SUCCESS.equals(getJobRequest().getJobStatus())
                  || JobStatus.FAILURE.equals(getJobRequest().getJobStatus())) {
            setJobStatusSteps(getJobStatusServiceHandler().getJobHistoryOperationalStatus(getJobRequest().getId()));
         } else {
            setJobStatusSteps(getJobStatusServiceHandler().getJobOperationalStatus(getJobRequest().getId()));
         }
      } catch (ExeCueException exception) {
         if (log.isDebugEnabled()) {
            log.error(exception, exception);
         }
         addActionError("An error occurred while extracting request information");
      }

      return SUCCESS;
   }

   public String correctMappingMaintenaceJob () {
      try {
         Long jobRequestId = jobMaintenanceServiceHandler.scheduleCorrectMappingMaintenanceJob(getApplicationContext()
                  .getAppId(), assetIds);
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         showJobStatus();
      } catch (ExeCueException e) {
         e.printStackTrace();
         addActionError("An error occurred while scheduling job. Please contact customer support.");
         return ERROR;
      }
      return SUCCESS;
   }

   public String popularityHitMaintenaceJob () {
      try {
         Long jobRequestId = jobMaintenanceServiceHandler.schedulePopularityHitMaintenanceJob();
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         showJobStatus();
      } catch (ExeCueException e) {
         e.printStackTrace();
         addActionError("An error occurred while scheduling job. Please contact customer support.");
      }
      return SUCCESS;

   }

   public String sdxSynchronizationJob () {
      try {

         // TODO : -RG- Below hard coding should be removed and be coming from the user
         // TODO : -RG- there should only be one id, either appId or assetId be passed as "id" along with below two parameters
         //            Every thing else should get populated in the Job itself
         // Correct these points once the screen is enhanced
         //JT- It is assumed if there is no asset is selected then default selection will be 'All'. 
         //TODO :- JT- we need to send "syncRequestLevel" from UI but we need to solidify screen first, may be we need to have radio button so user can decide the level of synch(member/column/table) for a asset of application he wants 
         syncRequestLevel = SyncRequestLevel.MEMBER;
         Long id = getApplicationContext().getAppId();
         operationRequestLevel = OperationRequestLevel.APPLICATION;
         if (assetId > 0) {
            operationRequestLevel = OperationRequestLevel.ASSET;
            id = assetId;
         }
         Long jobRequestId = jobMaintenanceServiceHandler.scheduleSDXSynchronizationeJob(id, getApplicationContext()
                  .getAppId(), getApplicationContext().getModelId(), syncRequestLevel, operationRequestLevel);
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         showJobStatus();
      } catch (ExeCueException e) {
         e.printStackTrace();
         addActionError("An error occurred while scheduling job. Please contact customer support.");
      }
      return SUCCESS;

   }

   public String scheduleOptimalDSetJob () {
      try {
         Long id = getApplicationContext().getAppId();
         operationRequestLevel = OperationRequestLevel.APPLICATION;
         if (assetId > 0) {
            operationRequestLevel = OperationRequestLevel.ASSET;
            id = assetId;
         }
         Long jobRequestId = jobMaintenanceServiceHandler.scheduleOptimalDSetJob(id, getApplicationContext()
                  .getModelId(), operationRequestLevel);
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         showJobStatus();
      } catch (ExeCueException e) {
         e.printStackTrace();
         addActionError("An error occurred while scheduling job. Please contact customer support.");
      }
      return SUCCESS;

   }

   public String riOntoTermPopularityHitMaintenaceJob () {
      try {
         Long modelId = getApplicationContext().getModelId();
         Long jobRequestId = jobMaintenanceServiceHandler.scheduleRIOntoTermPopularityHitMaintenanceJob(modelId);
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         showJobStatus();
      } catch (ExeCueException e) {
         e.printStackTrace();
         addActionError("An error occurred while scheduling job. Please contact customer support.");
      }
      return SUCCESS;

   }

   public String popularityCollectionMaintenaceJob () {

      Long applicationId = getApplicationContext().getAppId();
      Long modelId = getApplicationContext().getModelId();
      try {
         Long jobRequestId = jobMaintenanceServiceHandler.schedulePopularityCollectionMaintenanceJob(applicationId,
                  modelId);
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         showJobStatus();
      } catch (ExeCueException execueException) {
         execueException.printStackTrace();
         addActionError("An error occurred while scheduling job. Please contact customer support.");
      }

      return SUCCESS;

   }

   public String popularityDispersionMaintenaceJob () {
      Long applicationId = getApplicationContext().getAppId();
      Long modelId = getApplicationContext().getModelId();
      try {
         Long jobRequestId = jobMaintenanceServiceHandler.schedulePopularityDispersionMaintenanceJob(applicationId,
                  modelId);
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         showJobStatus();
      } catch (ExeCueException execueException) {
         execueException.printStackTrace();
         addActionError("An error occurred while scheduling job. Please contact customer support.");
      }

      return SUCCESS;

   }

   public String sflTermTokenWeightMaintenaceJob () {
      // the SFL term token weight adjustment is model ignorant
      // Long applicationId = getApplicationContext().getAppId();
      // Long modelId = getApplicationContext().getModelId();
      try {
         Long jobRequestId = jobMaintenanceServiceHandler.scheduleSflTermTokenWeightMaintenaceJob();
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         showJobStatus();
      } catch (ExeCueException execueException) {
         execueException.printStackTrace();
         addActionError("An error occurred while scheduling job. Please contact customer support.");
      }

      return SUCCESS;
   }

   public String sflWeightUpdationBySecondaryWordJob () {
      Long modelId = getApplicationContext().getModelId();
      try {
         Long jobRequestId = jobMaintenanceServiceHandler
                  .scheduleSflWeightUpdationBySecondaryWordMaintenaceJob(modelId);
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         showJobStatus();
      } catch (ExeCueException execueException) {
         execueException.printStackTrace();
         addActionError("An error occurred while scheduling job. Please contact customer support.");
      }

      return SUCCESS;
   }

   public String indexFormManagementJob () {
      Long applicationId = getApplicationContext().getAppId();
      Long modelId = getApplicationContext().getModelId();
      try {
         Long jobRequestId = jobMaintenanceServiceHandler.scheduleIndexFormManagementJob(applicationId, modelId);
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         showJobStatus();
      } catch (ExeCueException execueException) {
         execueException.printStackTrace();
         addActionError("An error occurred while scheduling job. Please contact customer support.");
      }
      return SUCCESS;
   }

   private String saveDataFile (File sourceData, String fileType) throws ExeCueException {
      String destinationFileName = null;
      if (fileType.equalsIgnoreCase("OWL"))
         destinationFileName = jobMaintenanceServiceHandler.getOWLFileStoragePath(fileType);
      else
         destinationFileName = jobMaintenanceServiceHandler.getOtherFileStoragePath(fileType);
      destinationFileName = destinationFileName + "/" + getSourceDataFileName();
      File destination = null;
      destination = new File(destinationFileName);
      if (destination.exists()) {
         destination.delete();
         destination = new File(destinationFileName);
      }
      boolean moved = sourceData.renameTo(destination);
      if (!moved) {
         log.error("File rename failed");
         throw new ExeCueException(1001, "Could not save the file");
      }
      return destinationFileName;
   }

   public String getAllInactiveAssets () {
      try {
         assets = sdxServiceHandler.getAllInactiveAssets(getApplicationContext().getAppId());
         if (ExecueCoreUtil.isCollectionEmpty(assets)) {
            assets = new ArrayList<Asset>();
         }
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   /**
    * @return the sourceData
    */
   public File getSourceData () {
      return sourceData;
   }

   /**
    * @param sourceData
    *           the sourceData to set
    */
   public void setSourceData (File sourceData) {
      this.sourceData = sourceData;
   }

   /**
    * @return the sourceDataFileName
    */
   public String getSourceDataFileName () {
      return sourceDataFileName;
   }

   /**
    * @param sourceDataFileName
    *           the sourceDataFileName to set
    */
   public void setSourceDataFileName (String sourceDataFileName) {
      this.sourceDataFileName = sourceDataFileName;
   }

   /**
    * @return the generateSFLTerms
    */
   public boolean isGenerateSFLTerms () {
      return generateSFLTerms;
   }

   /**
    * @param generateSFLTerms
    *           the generateSFLTerms to set
    */
   public void setGenerateSFLTerms (boolean generateSFLTerms) {
      this.generateSFLTerms = generateSFLTerms;
   }

   /**
    * @return the generateRIOntoterms
    */
   public boolean isGenerateRIOntoterms () {
      return generateRIOntoterms;
   }

   /**
    * @param generateRIOntoterms
    *           the generateRIOntoterms to set
    */
   public void setGenerateRIOntoterms (boolean generateRIOntoterms) {
      this.generateRIOntoterms = generateRIOntoterms;
   }

   /**
    * @return the jobRequest
    */
   public UIJobRequestStatus getJobRequest () {
      return jobRequest;
   }

   /**
    * @param jobRequest
    *           the jobRequest to set
    */
   public void setJobRequest (UIJobRequestStatus jobRequest) {
      this.jobRequest = jobRequest;
   }

   /**
    * @return the jobStatusSteps
    */
   public List<UIJobHistory> getJobStatusSteps () {
      return jobStatusSteps;
   }

   /**
    * @param jobStatusSteps
    *           the jobStatusSteps to set
    */
   public void setJobStatusSteps (List<UIJobHistory> jobStatusSteps) {
      this.jobStatusSteps = jobStatusSteps;
   }

   /**
    * @return the jobRequestId
    */
   public Long getJobRequestId () {
      return jobRequestId;
   }

   /**
    * @param jobRequestId
    *           the jobRequestId to set
    */
   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   /**
    * @return the evaluationJobRequest
    */
   public UIJobRequestStatus getEvaluationJobRequest () {
      return evaluationJobRequest;
   }

   /**
    * @param evaluationJobRequest
    *           the evaluationJobRequest to set
    */
   public void setEvaluationJobRequest (UIJobRequestStatus evaluationJobRequest) {
      this.evaluationJobRequest = evaluationJobRequest;
   }

   /**
    * @return the sourceDataContentType
    */
   public String getSourceDataContentType () {
      return sourceDataContentType;
   }

   /**
    * @param sourceDataContentType
    *           the sourceDataContentType to set
    */
   public void setSourceDataContentType (String sourceDataContentType) {
      this.sourceDataContentType = sourceDataContentType;
   }

   /**
    * @return the fileInfo
    */
   public UIPublishedFileInfo getFileInfo () {
      return fileInfo;
   }

   /**
    * @param fileInfo
    *           the fileInfo to set
    */
   public void setFileInfo (UIPublishedFileInfo fileInfo) {
      this.fileInfo = fileInfo;
   }

   /**
    * @return the evaluationJobRequestId
    */
   public Long getEvaluationJobRequestId () {
      return evaluationJobRequestId;
   }

   /**
    * @param evaluationJobRequestId
    *           the evaluationJobRequestId to set
    */
   public void setEvaluationJobRequestId (Long evaluationJobRequestId) {
      this.evaluationJobRequestId = evaluationJobRequestId;
   }

   /**
    * @return the jobMaintenanceServiceHandler
    */
   public IJobMaintenanceServiceHandler getJobMaintenanceServiceHandler () {
      return jobMaintenanceServiceHandler;
   }

   /**
    * @param jobMaintenanceServiceHandler
    *           the jobMaintenanceServiceHandler to set
    */
   public void setJobMaintenanceServiceHandler (IJobMaintenanceServiceHandler jobMaintenanceServiceHandler) {
      this.jobMaintenanceServiceHandler = jobMaintenanceServiceHandler;
   }

   /**
    * @return the assets
    */
   public List<Asset> getAssets () {
      return assets;
   }

   /**
    * @param assets
    *           the assets to set
    */
   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   /**
    * @return the sdxServiceHandler
    */
   @Override
   public ISDXServiceHandler getSdxServiceHandler () {
      return sdxServiceHandler;
   }

   /**
    * @param sdxServiceHandler
    *           the sdxServiceHandler to set
    */
   @Override
   public void setSdxServiceHandler (ISDXServiceHandler sdxServiceHandler) {
      this.sdxServiceHandler = sdxServiceHandler;
   }

   /**
    * @return the assetIds
    */
   public List<Long> getAssetIds () {
      return assetIds;
   }

   /**
    * @param assetIds
    *           the assetIds to set
    */
   public void setAssetIds (List<Long> assetIds) {
      this.assetIds = assetIds;
   }

   public String getModelName () {
      return modelName;
   }

   public void setModelName (String modelName) {
      this.modelName = modelName;
   }

   public List<Model> getModels () {
      return models;
   }

   public void setModels (List<Model> models) {
      this.models = models;
   }

   public Long getSelectedModelId () {
      return selectedModelId;
   }

   public void setSelectedModelId (Long selectedModelId) {
      this.selectedModelId = selectedModelId;
   }

   public Long getSelectedAssetId () {
      return selectedAssetId;
   }

   public void setSelectedAssetId (Long selectedAssetId) {
      this.selectedAssetId = selectedAssetId;
   }

   public UIJobRequestResult getJobRequestResult () {
      return jobRequestResult;
   }

   public void setJobRequestResult (UIJobRequestResult jobRequestResult) {
      this.jobRequestResult = jobRequestResult;
   }

   public Long getPublishedFileInfoId () {
      return publishedFileInfoId;
   }

   public void setPublishedFileInfoId (Long publishedFileInfoId) {
      this.publishedFileInfoId = publishedFileInfoId;
   }

   public SyncRequestLevel getSyncRequestLevel () {
      return syncRequestLevel;
   }

   public void setSyncRequestLevel (SyncRequestLevel syncRequestLevel) {
      this.syncRequestLevel = syncRequestLevel;
   }

   public OperationRequestLevel getOperationRequestLevel () {
      return operationRequestLevel;
   }

   public void setOperationRequestLevel (OperationRequestLevel operationRequestLevel) {
      this.operationRequestLevel = operationRequestLevel;
   }

   /**
    * @return the assetId
    */
   public Long getAssetId () {
      return assetId;
   }

   /**
    * @param assetId the assetId to set
    */
   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

}
