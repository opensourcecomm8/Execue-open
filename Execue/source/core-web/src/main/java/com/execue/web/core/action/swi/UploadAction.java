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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.ApplicationContext;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.publisher.PublisherDataAbsorptionContext;
import com.execue.core.common.type.CSVEmptyField;
import com.execue.core.common.type.CSVStringEnclosedCharacterType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.PublishedFileType;
import com.execue.core.common.type.PublishedOperationType;
import com.execue.core.common.type.PublisherProcessType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.handler.bean.UIApplicationModelInfo;
import com.execue.handler.bean.UIJobHistory;
import com.execue.handler.bean.UIJobRequestResult;
import com.execue.handler.bean.UIJobRequestStatus;
import com.execue.handler.bean.UIPublishedFileInfo;
import com.execue.handler.bean.UIUploadStatus;
import com.execue.handler.swi.IApplicationServiceHandler;
import com.execue.handler.swi.IDashBoardServiceHandler;
import com.execue.swi.exception.PublishedFileException;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class UploadAction extends SWIAction {

   private static final Logger            log                    = Logger.getLogger(UploadAction.class);

   // File upload properties
   private File                           sourceData;
   private String                         sourceDataContentType;
   private String                         sourceDataFileName;

   // Default is double quotes (") - Could be any other character
   private CSVStringEnclosedCharacterType stringEnclosure;

   // Default is BLANK - Could be \N, (null) or NULL
   private CSVEmptyField                  nullIdentifier;
   private String                         dataDelimeter;
   // Default is true
   private boolean                        columnNamesPresent     = true;

   // Comma Separated or Tab Separated - Default is CSV
   private PublishedFileType              sourceType;
   private String                         wizardBased;
   private PublishedOperationType         operationType          = PublishedOperationType.ADDITION;

   private UIJobRequestStatus             jobRequest;
   private List<UIJobHistory>             jobStatusSteps;
   private UIPublishedFileInfo            fileInfo;
   private Long                           jobRequestId;
   private Long                           evaluationJobRequestId;
   private UIJobRequestStatus             evaluationJobRequest;
   private String                         tag;
   private String                         sourceURL;
   private CheckType                      absorbDataset          = CheckType.NO;
   private UIApplicationModelInfo         selectedApp;
   private List<UIApplicationModelInfo>   applications           = new ArrayList<UIApplicationModelInfo>();
   private IDashBoardServiceHandler       dashBoardServiceHandler;
   private String                         fileName;
   private String                         fileDescription;
   private InputStream                    inputStream;
   private UIJobRequestResult             jobRequestResult;
   // TODO : this variable is here only to support invokeUploadedFileAbsorption() method
   private Long                           publishedFileInfoId;
   private UIUploadStatus                 operationStatus;
   private PublisherProcessType           publisherProcessType   = PublisherProcessType.DETAILED_PUBLISHER_PROCESS;

   private CheckType                      uploadAsCompressedFile = CheckType.NO;
   private IApplicationServiceHandler     applicationServiceHandler;

   // Attributes for further enhancements
   // private Application application;

   @Override
   public String input () {
      try {
         setDefaults();
         setSourceType(PublishedFileType.CSV);
         if (PublisherProcessType.DETAILED_PUBLISHER_PROCESS.equals(publisherProcessType)) {
            applications = transformUIApplication(dashBoardServiceHandler.getApplications());
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String inputXL () {
      setDefaults();
      setSourceType(PublishedFileType.EXCEL);
      return SUCCESS;
   }

   public String uploadCSVData () {
      UIUploadStatus uploadStatus = new UIUploadStatus();
      try {
         if (PublisherProcessType.SIMPLIFIED_PUBLISHER_PROCESS.equals(publisherProcessType)) {
            setAbsorbDataset(CheckType.YES);
            setFileName(ExecueCoreUtil.removeSpecialCharacters(getSourceDataFileName()));
            if (getUploadServiceHandler().isFileExist(getFileName())) {
               setFileName(getFileName() + "_" + RandomStringUtils.randomAlphabetic(3));
            }
         } else {
            setFileName(ExecueStringUtil.getNormalizedName(getFileName()));
         }
         validateUpload(uploadStatus);
         if (ExecueCoreUtil.isCollectionNotEmpty(uploadStatus.getErrorMessages())) {
            inputStream = new ByteArrayInputStream(JSONUtil.serialize(uploadStatus).getBytes());
            return SUCCESS;
         }

         populateUIUploadStatus(uploadStatus);
         // TODO : -RG- Need to consider if selected

         // Upload the file
         PublishedFileInfo publishedFileInfo = null;
         if (PublisherProcessType.DETAILED_PUBLISHER_PROCESS.equals(publisherProcessType)) {
            Long applicationId = null;
            Long modelId = null;
            if (getSelectedApp() != null) {
               applicationId = getSelectedApp().getApplicationId();
               modelId = getSelectedApp().getModelId();
            }
            publishedFileInfo = getUploadServiceHandler().saveDataFile(getSourceData(), getFileName(),
                     getFileDescription(), getSourceDataFileName(), isColumnNamesPresent(), getCSVDelimeter(),
                     getNullIdentifier(), getStringEnclosure(), getOperationType(), getSourceType(), getSourceURL(),
                     getTag(), applicationId, modelId, getAbsorbDataset(),
                     PublisherProcessType.DETAILED_PUBLISHER_PROCESS, uploadAsCompressedFile);
         } else {
            // Simlified file upload
            publishedFileInfo = getUploadServiceHandler().saveDataFile(getSourceData(), getFileName(),
                     getFileDescription(), getSourceDataFileName(), isColumnNamesPresent(), getCSVDelimeter(),
                     getNullIdentifier(), getStringEnclosure(), getOperationType(), getSourceType(), getSourceURL(),
                     null, null, null, getAbsorbDataset(), PublisherProcessType.SIMPLIFIED_PUBLISHER_PROCESS,
                     uploadAsCompressedFile);

         }

         uploadStatus.setPublishedFileInfoId(publishedFileInfo.getId());
         uploadStatus.setStatus(true);
         if (CheckType.YES.equals(absorbDataset)) {
            if (PublishedFileType.EXCEL.equals(sourceType)) {
               setStringEnclosure(CSVStringEnclosedCharacterType.DOUBLE_QUOTE);
            }
            PublisherDataAbsorptionContext context = getUploadServiceHandler().absorbData(publishedFileInfo,
                     isColumnNamesPresent(), getCSVDelimeter(), getNullIdentifier(), getStringEnclosure());
            Long jobRequestId = context.getJobRequest().getId();
            setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
            uploadStatus.setJobRequestId(getJobRequest().getId());
            uploadStatus.setIsCompressedFile(uploadAsCompressedFile);
            if (PublisherProcessType.DETAILED_PUBLISHER_PROCESS.equals(publisherProcessType)) {
               uploadStatus.setApplicationName(getSelectedApp().getApplicationName());
            } else {
               Application application = getApplicationServiceHandler().getApplicationById(context.getApplicationId());
               uploadStatus.setApplicationName(application.getName());
               setCurrentApplicationInContext(application);
            }
         }
         uploadStatus.addMessage(getText("execue.upload.main.file.transfer.success"));
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         uploadStatus.setStatus(false);
         uploadStatus
                  .addErrorMessage("An error occurred while importing source data. Please contact customer support.");
      } catch (Exception exception) {
         exception.printStackTrace();
         uploadStatus.setStatus(false);
         uploadStatus
                  .addErrorMessage("An error occurred while importing source data. Please contact customer support.");
      }
      try {
         inputStream = new ByteArrayInputStream(JSONUtil.serialize(uploadStatus).getBytes());
      } catch (JSONException e) {
         // TODO: -RG- handle this scenario at front end
         inputStream = new ByteArrayInputStream(
                  new String(
                           "{\"errorMessages\":[\"An error occurred while importing source data. Please contact customer support\"],\"status\":false}")
                           .getBytes());
      }
      return SUCCESS;
   }

   private void setCurrentApplicationInContext (Application application) {
      try {
         Model model = getApplicationServiceHandler().getModelsByApplicationId(application.getId()).get(0);
         ApplicationContext applicationContext = ExecueBeanManagementUtil.prepareApplicationContext(model.getId(),
                  application.getId(), application.getName(), null, null, application.getSourceType());
         setApplicationContext(applicationContext);
      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   private void validateUpload (UIUploadStatus uploadStatus) throws ExeCueException {

      if (ExecueCoreUtil.isNotEmpty(dataDelimeter) && dataDelimeter.length() > 1) {
         uploadStatus.addErrorMessage(getText("execue.upload.main.file.delimeter.length"));
      }
      // if process is DETAILED_PUBLISHER_PROCESS and requested for absorbing the asset, file to be selected for upload
      if (PublisherProcessType.DETAILED_PUBLISHER_PROCESS.equals(publisherProcessType)) {
         // file name is mandatory
         if (ExecueCoreUtil.isEmpty(fileName)) {
            uploadStatus.addErrorMessage(getText("execue.upload.main.file.name.required"));
         } else if (fileName.length() > 255 || fileName.length() < 5) {
            uploadStatus.addErrorMessage(getText("execue.upload.main.file.name.length.mismatch"));
         }
         // file description is mandatory
         if (ExecueCoreUtil.isEmpty(fileDescription)) {
            uploadStatus.addErrorMessage(getText("execue.upload.main.file.description.required"));
         }
         if (CheckType.YES.equals(absorbDataset)) {
            if (sourceData == null) {
               uploadStatus.addErrorMessage(getText("execue.upload.main.file.selection.required"));
            }
         } else {// either file to be selected for upload or an outside URL to be provided.
            if (sourceData == null && ExecueCoreUtil.isEmpty(sourceURL)) {
               uploadStatus.addErrorMessage(getText("execue.upload.main.file.required"));
            }
         }
         // if asset absorption requested then app should be selected and file name to be unique with in the application
         // scope as asset name
         if (CheckType.YES.equals(absorbDataset)
                  && PublisherProcessType.DETAILED_PUBLISHER_PROCESS.equals(publisherProcessType)
                  && ExecueCoreUtil.isCollectionEmpty(uploadStatus.getErrorMessages())) {
            if (selectedApp.getApplicationId() < 0) {
               uploadStatus.addErrorMessage(getText("execue.upload.main.application.selection.required"));
            } else if (getSdxServiceHandler().getAsset(selectedApp.getApplicationId(), getFileName()) != null) {
               uploadStatus.addErrorMessage(getText("execue.upload.main.dataset.exists"));
            }
         }
         if (ExecueCoreUtil.isNotEmpty(tag) && tag.length() > 255) {
            uploadStatus.addErrorMessage(getText("execue.upload.main.tag.length.mismatch"));
         }
         // file name is unique for a user
         if (getUploadServiceHandler().isFileExist(getFileName())) {
            uploadStatus.addErrorMessage(getText("execue.upload.main.file.exists"));
         }

      } else {
         if (sourceData == null) {
            uploadStatus.addErrorMessage(getText("execue.upload.main.file.selection.required"));
         }
      }

   } // End of validateUpload()

   private void populateUIUploadStatus (UIUploadStatus uploadStatus) {
      if (CheckType.YES.equals(absorbDataset)) {
         uploadStatus.setAbsorbAsset(true);
      }
      uploadStatus.setColumnNamesPresent(isColumnNamesPresent());
      uploadStatus.setDelimiter(getCSVDelimeter());
      uploadStatus.setFielDescription(getFileDescription());
      uploadStatus.setFileName(getFileName());
      uploadStatus.setFileURL(getSourceURL());
      uploadStatus.setNullIdentifier(getNullIdentifier().getDescription());
      uploadStatus.setOperationType(getOperationType().getDescription());
      uploadStatus.setSourceDataFileName(getSourceDataFileName());
      uploadStatus.setSourceType(getSourceType().getDescription());
      uploadStatus.setStringEnclosure(getStringEnclosure().getDescription());
      uploadStatus.setTag(getTag());
   }

   public String absorbUploadedPublishedFile () {
      try {
         PublisherDataAbsorptionContext context = getUploadServiceHandler().absorbPublishedFile(fileInfo);
         Long jobRequestId = context.getJobRequest().getId();
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         addActionMessage(getText("execue.upload.main.file.transfer.evaluation"));
         showUploadStatus();
      } catch (ExeCueException e) {
         e.printStackTrace();
         return ERROR;
      }
      return SUCCESS;
   }

   public String invokeUploadedFileAbsorption () {
      try {
         jobRequestResult = new UIJobRequestResult();
         PublisherDataAbsorptionContext context = getUploadServiceHandler().absorbPublishedFile(
                  getPublishedFileInfoId());
         Long jobRequestId = context.getJobRequest().getId();
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         addActionMessage(getText("execue.upload.main.file.transfer.evaluation"));
         getJobRequestResult().setStatus(getJobRequest().getJobStatus());
         getJobRequestResult().setJobId(getJobRequest().getId());
      } catch (ExeCueException e) {
         e.printStackTrace();
         getJobRequestResult().setErrMsg(getText("execue.upload.main.file.absorption.job.invocation.failed"));
         getJobRequestResult().setStatus(JobStatus.FAILURE);
         return ERROR;
      }
      return SUCCESS;
   }

   // public String uploadXLData () {
   // try {
   // // Upload the file
   // String excelFilePath = saveDataFile(getSourceData());
   // String csvFileStoragePath = getUploadServiceHandler().getFileStoragePath(PublishedFileType.CSV.getValue());
   // String csvFilePath = getUploadServiceHandler().convertXLFileToCSV(excelFilePath,
   // getApplicationContext().getAppId(), csvFileStoragePath);
   // /*
   // * Application app = new Application(); app.setId(getApplicationId()); asset.setApplication(app);
   // */
   // Long jobRequestId = getUploadServiceHandler().absorbData(csvFilePath, isColumnNamesPresent(),
   // getApplicationContext().getAppId(), CSVDelimeter.COMMA, nullIdentifier,
   // CSVStringEnclosedCharacterType.DOUBLE_QUOTE, getOperationType(), getSourceDataFileName(),
   // getApplicationContext().getModelId(), asset, getSourceType());
   //
   // setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
   //
   // addActionMessage(getText("upload.file.transfer.success"));
   //
   // showUploadStatus();
   // } catch (ExeCueException exeCueException) {
   // log.error(exeCueException, exeCueException);
   // addActionError("An error occurred while importing source data. Please contact customer support.");
   // return ERROR;
   // } catch (Exception exception) {
   //
   // addActionError("An error occurred while importing source data. Please contact customer support.");
   // return ERROR;
   // }
   // return SUCCESS;
   //
   // }

   public String absorbEvaluatedData () {
      try {
         evaluationJobRequestId = getUploadServiceHandler().evaluateDataset(jobRequestId);
         setEvaluationJobRequest(getJobStatusServiceHandler().getJobRequestStatus(evaluationJobRequestId));
         setJobRequest(evaluationJobRequest);
         setJobRequestId(evaluationJobRequestId);

      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         addActionError("An error occurred while absorbing evaluated data. Please contact customer support.");
         return ERROR;
      } catch (Exception exception) {
         addActionError("An error occurred while absorbing evaluated data. Please contact customer support.");
         return ERROR;
      }
      return SUCCESS;
   }

   // Method to return status through JSON
   public String invokeAbsorbEvaluatedData () {
      try {
         jobRequestResult = new UIJobRequestResult();
         evaluationJobRequestId = getUploadServiceHandler().evaluateDataset(jobRequestId);
         setEvaluationJobRequest(getJobStatusServiceHandler().getJobRequestStatus(evaluationJobRequestId));
         getJobRequestResult().setStatus(getEvaluationJobRequest().getJobStatus());
         getJobRequestResult().setJobId(getEvaluationJobRequest().getId());
      } catch (ExeCueException e) {
         e.printStackTrace();
         getJobRequestResult().setErrMsg(getText("execue.upload.main.file.absorption.job.invocation.failed"));
         getJobRequestResult().setStatus(JobStatus.FAILURE);
         return ERROR;
      }

      return SUCCESS;
   }

   public String showUploadStatusDetails () {
      try {
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(getJobRequest().getId()));
         if (JobStatus.SUCCESS.equals(getJobRequest().getJobStatus())
                  || JobStatus.FAILURE.equals(getJobRequest().getJobStatus())) {
            setJobStatusSteps(getJobStatusServiceHandler().getJobHistoryOperationalStatus(getJobRequest().getId()));
         } else {
            setJobStatusSteps(getJobStatusServiceHandler().getJobOperationalStatus(getJobRequest().getId()));
         }

         setFileInfo(getUploadServiceHandler().getFileInfo(getJobRequest().getJobType(),
                  getJobRequest().getRequestData()));
         setSourceType(getFileInfo().getSourceType());
         // getApplicationContext().setAppId(getFileInfo().getApplicationId());
      } catch (ExeCueException exception) {
         log.error(exception, exception);
         addActionError("An error occurred while extracting request information");
      }
      return SUCCESS;
   }

   public String showUploadStatus () {
      if (log.isDebugEnabled()) {
         log.debug("Job Request Id : " + jobRequest.getId());
      }
      try {
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(getJobRequest().getId()));
      } catch (ExeCueException exception) {
         log.error(exception, exception);
         addActionError("An error occurred while extracting request information");
      }

      return SUCCESS;
   }

   public String deleteUploadedDataset () {
      operationStatus = new UIUploadStatus();
      try {
         PublishedFileInfo publishedFileInfo = getUploadServiceHandler().getPublishedFileInfoById(publishedFileInfoId);
         operationStatus = getUploadServiceHandler().validatePublishedFileInfoForDeletion(operationStatus,
                  publishedFileInfo);
         if (ExecueCoreUtil.isCollectionNotEmpty(operationStatus.getErrorMessages())) {
            return SUCCESS;
         }
         getUploadServiceHandler().deleteUploadedDataset(publishedFileInfo);
         operationStatus.addMessage(getText("execue.table.delete.success"));
      } catch (PublishedFileException e) {
         e.printStackTrace();
         operationStatus.addMessage("An error occurred while deleting this record. Please contact customer support.");
      }
      return SUCCESS;
   }

   private String getCSVDelimeter () {
      String finalDataDelimeter = null;
      if (ExecueCoreUtil.isEmpty(dataDelimeter)) {
         if (PublishedFileType.CSV.equals(getSourceType()) || PublishedFileType.EXCEL.equals(getSourceType())) {
            finalDataDelimeter = ",";
         } else {
            finalDataDelimeter = "\t";
         }
      } else {
         finalDataDelimeter = dataDelimeter.trim();
      }
      return finalDataDelimeter;
   }

   private void setDefaults () {
      setSourceType(PublishedFileType.CSV);
      setColumnNamesPresent(true);
      getCSVEmptyFields();
      getCSVStringEnclosedCharacterTypes();
      setWizardBased("Y");
      // TODO: -RG- should be coming from screen - hard coded till the functionality is implemented
      setOperationType(PublishedOperationType.ADDITION);
   }

   private List<UIApplicationModelInfo> transformUIApplication (List<Application> applications) {
      List<UIApplicationModelInfo> uiApplications = new ArrayList<UIApplicationModelInfo>();
      for (Application application : applications) {
         UIApplicationModelInfo uiApplication = new UIApplicationModelInfo();
         uiApplication.setApplicationId(application.getId());
         uiApplication.setApplicationName(application.getName());
         uiApplication.setApplicationURL(application.getApplicationURL());
         // TODO:- VG- as off now only one model for one application.
         for (Model model : application.getModels()) {
            uiApplication.setModelId(model.getId());
         }
         uiApplications.add(uiApplication);
      }
      return uiApplications;

   }

   public File getSourceData () {
      return sourceData;
   }

   public void setSourceData (File sourceData) {
      this.sourceData = sourceData;
   }

   public String getSourceDataContentType () {
      return sourceDataContentType;
   }

   public void setSourceDataContentType (String sourceDataContentType) {
      this.sourceDataContentType = sourceDataContentType;
   }

   public String getSourceDataFileName () {
      return sourceDataFileName;
   }

   public void setSourceDataFileName (String sourceDataFileName) {
      this.sourceDataFileName = sourceDataFileName;
   }

   public boolean isColumnNamesPresent () {
      return columnNamesPresent;
   }

   public void setColumnNamesPresent (boolean columnNamesPresent) {
      this.columnNamesPresent = columnNamesPresent;
   }

   public String getWizardBased () {
      return wizardBased;
   }

   public void setWizardBased (String wizardBased) {
      this.wizardBased = wizardBased;
   }

   public PublishedOperationType getOperationType () {
      return operationType;
   }

   public void setOperationType (PublishedOperationType operationType) {
      this.operationType = operationType;
   }

   public UIJobRequestStatus getJobRequest () {
      return jobRequest;
   }

   public void setJobRequest (UIJobRequestStatus jobRequest) {
      this.jobRequest = jobRequest;
   }

   public List<UIJobHistory> getJobStatusSteps () {
      return jobStatusSteps;
   }

   public void setJobStatusSteps (List<UIJobHistory> jobStatusSteps) {
      this.jobStatusSteps = jobStatusSteps;
   }

   public UIPublishedFileInfo getFileInfo () {
      return fileInfo;
   }

   public void setFileInfo (UIPublishedFileInfo fileInfo) {
      this.fileInfo = fileInfo;
   }

   public Map<String, String> getCsvTypes () {
      Map<String, String> csvTypes = new LinkedHashMap<String, String>();
      csvTypes.put("CSV", "CSV");
      csvTypes.put("TSV", "TSV");
      csvTypes.put("EXCEL", "EXCEL");
      csvTypes.put("OTHER", "OTHER");
      return csvTypes;
   }

   public List<CSVEmptyField> getCSVEmptyFields () {
      return Arrays.asList(CSVEmptyField.values());
   }

   public List<CSVStringEnclosedCharacterType> getCSVStringEnclosedCharacterTypes () {
      return Arrays.asList(CSVStringEnclosedCharacterType.values());
   }

   /**
    * @return the stringEnclosure
    */
   public CSVStringEnclosedCharacterType getStringEnclosure () {
      return stringEnclosure;
   }

   /**
    * @param stringEnclosure
    *           the stringEnclosure to set
    */
   public void setStringEnclosure (CSVStringEnclosedCharacterType stringEnclosure) {
      this.stringEnclosure = stringEnclosure;
   }

   /**
    * @return the nullIdentifier
    */
   public CSVEmptyField getNullIdentifier () {
      return nullIdentifier;
   }

   /**
    * @param nullIdentifier
    *           the nullIdentifier to set
    */
   public void setNullIdentifier (CSVEmptyField nullIdentifier) {
      this.nullIdentifier = nullIdentifier;
   }

   public Long getJobRequestId () {
      return jobRequestId;
   }

   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   public Long getEvaluationJobRequestId () {
      return evaluationJobRequestId;
   }

   public void setEvaluationJobRequestId (Long evaluationJobRequestId) {
      this.evaluationJobRequestId = evaluationJobRequestId;
   }

   public UIJobRequestStatus getEvaluationJobRequest () {
      return evaluationJobRequest;
   }

   public void setEvaluationJobRequest (UIJobRequestStatus evaluationJobRequest) {
      this.evaluationJobRequest = evaluationJobRequest;
   }

   public PublishedFileType getSourceType () {
      return sourceType;
   }

   public void setSourceType (PublishedFileType sourceType) {
      this.sourceType = sourceType;
   }

   /**
    * @return the tag
    */
   public String getTag () {
      return tag;
   }

   /**
    * @param tag
    *           the tag to set
    */
   public void setTag (String tag) {
      this.tag = tag;
   }

   /**
    * @return the sourceURL
    */
   @Override
   public String getSourceURL () {
      return sourceURL;
   }

   /**
    * @param sourceURL
    *           the sourceURL to set
    */
   @Override
   public void setSourceURL (String sourceURL) {
      this.sourceURL = sourceURL;
   }

   /**
    * @return the applications
    */
   public List<UIApplicationModelInfo> getApplications () {
      return applications;
   }

   /**
    * @param applications
    *           the applications to set
    */
   public void setApplications (List<UIApplicationModelInfo> applications) {
      this.applications = applications;
   }

   /**
    * @return the dashBoardServiceHandler
    */
   public IDashBoardServiceHandler getDashBoardServiceHandler () {
      return dashBoardServiceHandler;
   }

   /**
    * @param dashBoardServiceHandler
    *           the dashBoardServiceHandler to set
    */
   public void setDashBoardServiceHandler (IDashBoardServiceHandler dashBoardServiceHandler) {
      this.dashBoardServiceHandler = dashBoardServiceHandler;
   }

   /**
    * @return the selectedApp
    */
   public UIApplicationModelInfo getSelectedApp () {
      return selectedApp;
   }

   /**
    * @param selectedApp
    *           the selectedApp to set
    */
   public void setSelectedApp (UIApplicationModelInfo selectedApp) {
      this.selectedApp = selectedApp;
   }

   /**
    * @return the absorbDataset
    */
   public CheckType getAbsorbDataset () {
      return absorbDataset;
   }

   /**
    * @param absorbDataset
    *           the absorbDataset to set
    */
   public void setAbsorbDataset (CheckType absorbDataset) {
      this.absorbDataset = absorbDataset;
   }

   /**
    * @return the fileName
    */
   public String getFileName () {
      return fileName;
   }

   /**
    * @param fileName
    *           the fileName to set
    */
   public void setFileName (String fileName) {
      this.fileName = fileName;
   }

   /**
    * @return the fileDescription
    */
   public String getFileDescription () {
      return fileDescription;
   }

   /**
    * @param fileDescription
    *           the fileDescription to set
    */
   public void setFileDescription (String fileDescription) {
      this.fileDescription = fileDescription;
   }

   public InputStream getInputStream () {
      return inputStream;
   }

   public void setInputStream (InputStream inputStream) {
      this.inputStream = inputStream;
   }

   /**
    * @return the jobRequestResult
    */
   public UIJobRequestResult getJobRequestResult () {
      return jobRequestResult;
   }

   /**
    * @param jobRequestResult
    *           the jobRequestResult to set
    */
   public void setJobRequestResult (UIJobRequestResult jobRequestResult) {
      this.jobRequestResult = jobRequestResult;
   }

   /**
    * @return the publishedFileInfoId
    */
   public Long getPublishedFileInfoId () {
      return publishedFileInfoId;
   }

   /**
    * @param publishedFileInfoId
    *           the publishedFileInfoId to set
    */
   public void setPublishedFileInfoId (Long publishedFileInfoId) {
      this.publishedFileInfoId = publishedFileInfoId;
   }

   public UIUploadStatus getOperationStatus () {
      return operationStatus;
   }

   public void setOperationStatus (UIUploadStatus operationStatus) {
      this.operationStatus = operationStatus;
   }

   public PublisherProcessType getPublisherProcessType () {
      return publisherProcessType;
   }

   public void setPublisherProcessType (PublisherProcessType publisherProcessType) {
      this.publisherProcessType = publisherProcessType;
   }

   public CheckType getUploadAsCompressedFile () {
      return uploadAsCompressedFile;
   }

   public void setUploadAsCompressedFile (CheckType uploadAsCompressedFile) {
      this.uploadAsCompressedFile = uploadAsCompressedFile;
   }

   public IApplicationServiceHandler getApplicationServiceHandler () {
      return applicationServiceHandler;
   }

   public void setApplicationServiceHandler (IApplicationServiceHandler applicationServiceHandler) {
      this.applicationServiceHandler = applicationServiceHandler;
   }

   public String getDataDelimeter () {
      return dataDelimeter;
   }

   public void setDataDelimeter (String dataDelimeter) {
      this.dataDelimeter = dataDelimeter;
   }

}
