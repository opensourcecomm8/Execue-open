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


package com.execue.handler.swi.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.PageSearch;
import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.JobHistoryOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileInfoDetails;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.core.common.bean.publisher.PublisherDataAbsorptionContext;
import com.execue.core.common.bean.publisher.PublisherDataEvaluationContext;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.AppCreationType;
import com.execue.core.common.type.CSVEmptyField;
import com.execue.core.common.type.CSVStringEnclosedCharacterType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.CurrentPublisherFlowStatusType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.JobType;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.common.type.PublishedFileType;
import com.execue.core.common.type.PublishedOperationType;
import com.execue.core.common.type.PublisherDataType;
import com.execue.core.common.type.PublisherFlowOperationType;
import com.execue.core.common.type.PublisherProcessType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.common.util.XLFileTransformer;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExeCueXMLUtils;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIPublishedFileInfo;
import com.execue.handler.bean.UIPublishedFileTableDetail;
import com.execue.handler.bean.UIPublisherJobStatus;
import com.execue.handler.bean.UIUploadStatus;
import com.execue.handler.swi.IUploadServiceHandler;
import com.execue.platform.swi.IApplicationManagementWrapperService;
import com.execue.platform.swi.ISWIPlatformDeletionService;
import com.execue.publisher.IPublisherConstants;
import com.execue.publisher.configuration.IPublisherConfigurationService;
import com.execue.publisher.evaluate.IPublisherDataEvaluationService;
import com.execue.publisher.validator.IPublisherColumnValidator;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.scheduler.service.IPublisherDataAbsorbtionJobService;
import com.execue.scheduler.service.IPublisherDataEvaluationJobService;
import com.execue.security.UserContextService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPublishedFileManagementService;
import com.execue.swi.service.IPublishedFileRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.util.FileUtilities;

public class UploadServiceHandlerImpl extends UserContextService implements IUploadServiceHandler {

   private static final Logger                  log = Logger.getLogger(UploadServiceHandlerImpl.class);
   private IPublisherConfigurationService       publisherConfigurationService;
   private IPublisherDataAbsorbtionJobService   publisherDataAbsorbtionJobService;
   private IPublishedFileRetrievalService       publishedFileRetrievalService;
   private IPublishedFileManagementService      publishedFileManagementService;
   private IJobDataService                      jobDataService;
   private IKDXRetrievalService                 kdxRetrievalService;
   private IPublisherDataEvaluationJobService   publisherDataEvaluationJobService;
   private IPublisherDataEvaluationService      publisherDataEvaluationService;
   private IApplicationManagementWrapperService applicationManagementWrapperService;
   private IPublisherColumnValidator            publisherColumnValidator;
   private ISDXRetrievalService                 sdxRetrievalService;
   private ISWIPlatformDeletionService          swiPlatformDeletionService;
   private IApplicationRetrievalService         applicationRetrievalService;

   public PublisherDataAbsorptionContext absorbData (PublishedFileInfo publishedFileInfo, boolean isColumnsAvailable,
            String dataDelimeter, CSVEmptyField csvEmptyField,
            CSVStringEnclosedCharacterType stringEnclosedCharacterType) throws ExeCueException {
      PublisherDataAbsorptionContext publisherDataAbsorptionContext = new PublisherDataAbsorptionContext();
      Long applicationId = null;
      Long modelId = null;

      if (PublisherProcessType.SIMPLIFIED_PUBLISHER_PROCESS.equals(publishedFileInfo.getPublisherProcessType())) {
         Application application = new Application();
         application.setName(publishedFileInfo.getFileName());
         application.setDescription(publishedFileInfo.getFileDescription());
         application.setStatus(StatusEnum.ACTIVE);
         application.setCreationType(AppCreationType.UNIFIED_PROCESS);
         User user = new User();
         user.setId(publishedFileInfo.getUserId());
         application.setOwner(user);
         application.setCreatedDate(new Date());
         application.setPopularity(100L);
         // check if application exist with this name. if yes set the app name with user Id and random string
         if (getApplicationRetrievalService().isApplicationExist(publishedFileInfo.getFileName())) {
            application.setName(publishedFileInfo.getFileName() + "_" + publishedFileInfo.getUserId()
                     + RandomStringUtils.randomAlphabetic(3));
         }
         getApplicationManagementWrapperService().createApplicationHierarchy(application);
         applicationId = application.getId();
         modelId = getKdxRetrievalService().getModelsByApplicationId(application.getId()).get(0).getId();
         publisherDataAbsorptionContext.setApplicationId(applicationId);
         publisherDataAbsorptionContext.setModelId(modelId);

         publishedFileInfo.setApplicationId(applicationId);
         publishedFileInfo.setModelId(modelId);
         getPublishedFileManagementService().updatePublishedFileInfo(publishedFileInfo);

      } else {
         publisherDataAbsorptionContext.setApplicationId(publishedFileInfo.getApplicationId());
         publisherDataAbsorptionContext.setModelId(publishedFileInfo.getModelId());
      }

      publisherDataAbsorptionContext.setFileName(publishedFileInfo.getFileName());
      publisherDataAbsorptionContext.setFileDescription(publishedFileInfo.getFileDescription());
      publisherDataAbsorptionContext.setOriginalFileName(publishedFileInfo.getOriginalFileName());
      publisherDataAbsorptionContext.setFilePath(publishedFileInfo.getFileLocation());
      publisherDataAbsorptionContext.setPublishedOperationType(publishedFileInfo.getPublishedOperationType());
      publisherDataAbsorptionContext.setSourceType(publishedFileInfo.getSourceType());

      publisherDataAbsorptionContext.setUserId(publishedFileInfo.getUserId());
      publisherDataAbsorptionContext.setColumnsAvailable(isColumnsAvailable);
      publisherDataAbsorptionContext.setDataDelimeter(dataDelimeter);
      publisherDataAbsorptionContext.setCsvEmptyField(csvEmptyField);
      publisherDataAbsorptionContext.setCsvStringEnclosedCharacterType(stringEnclosedCharacterType);
      publisherDataAbsorptionContext.setFileInfoId(publishedFileInfo.getId());
      publisherDataAbsorptionContext.setDatasetCollectionCreation(ExecueBeanUtil
               .findCorrespondingBooleanValue(publishedFileInfo.getDatasetCollectionCreation()));
      publisherDataAbsorptionContext.setPublisherProcessType(publishedFileInfo.getPublisherProcessType());
      getPublisherDataAbsorbtionJobService().schedulePublisherDataAbsorbtion(publisherDataAbsorptionContext);
      return publisherDataAbsorptionContext;
   }

   public PublisherDataAbsorptionContext absorbPublishedFile (UIPublishedFileInfo uiPublishedFileInfo)
            throws ExeCueException {
      PublishedFileInfo publishedFileInfo = getPublishedFileRetrievalService().getPublishedFileInfoById(
               Long.valueOf(uiPublishedFileInfo.getFileId()));
      return absorbPublishedFile(publishedFileInfo);
   }

   public PublisherDataAbsorptionContext absorbPublishedFile (Long publishedFileInfoId) throws ExeCueException {
      PublishedFileInfo publishedFileInfo = getPublishedFileRetrievalService().getPublishedFileInfoById(
               publishedFileInfoId);
      return absorbPublishedFile(publishedFileInfo);
   }

   private PublisherDataAbsorptionContext absorbPublishedFile (PublishedFileInfo publishedFileInfo)
            throws ExeCueException {
      List<PublishedFileInfoDetails> publishedFileInfoDetails = getPublishedFileRetrievalService()
               .getPublishedFileInfoDetailByFileId(publishedFileInfo.getId());
      boolean isColumnsAvailable = Boolean.valueOf(getPublishedFileInfoProperty(publishedFileInfoDetails,
               IPublisherConstants.IS_COLUMN_AVAILABLE));
      String dataDelimeter = getPublishedFileInfoProperty(publishedFileInfoDetails, IPublisherConstants.CSV_DELIMETER);
      CSVEmptyField csvEmptyField = CSVEmptyField.getType(getPublishedFileInfoProperty(publishedFileInfoDetails,
               IPublisherConstants.CSV_EMPTY_FIELD));
      CSVStringEnclosedCharacterType stringEnclosedCharacterType = CSVStringEnclosedCharacterType
               .getType(getPublishedFileInfoProperty(publishedFileInfoDetails,
                        IPublisherConstants.CSV_ENCLOSED_CHAR_TYPE));
      return absorbData(publishedFileInfo, isColumnsAvailable, dataDelimeter, csvEmptyField,
               stringEnclosedCharacterType);
   }

   private String getPublishedFileInfoProperty (List<PublishedFileInfoDetails> publishedFileInfoDetails,
            String propertyName) {
      String propertyValue = "";
      for (PublishedFileInfoDetails publishedFileInfoDetail : publishedFileInfoDetails) {
         if (propertyName.equalsIgnoreCase(publishedFileInfoDetail.getPropertyName())) {
            propertyValue = publishedFileInfoDetail.getPropertyValue();
            break;
         }
      }
      return propertyValue;
   }

   public Long evaluateDataset (Long jobRequestId) throws ExeCueException {
      JobRequest dataAbsorptionJobRequest = getJobDataService().getJobRequestById(jobRequestId);
      PublisherDataAbsorptionContext publisherDataAbsorptionContext = (PublisherDataAbsorptionContext) ExeCueXMLUtils
               .getObjectFromXMLString(dataAbsorptionJobRequest.getRequestData());
      PublisherDataEvaluationContext publisherDataEvaluationContext = new PublisherDataEvaluationContext();
      publisherDataEvaluationContext.setApplicationId(publisherDataAbsorptionContext.getApplicationId());
      publisherDataEvaluationContext.setModelId(publisherDataAbsorptionContext.getModelId());

      Asset asset = new Asset();
      asset.setDisplayName(publisherDataAbsorptionContext.getFileName());
      asset.setDescription(publisherDataAbsorptionContext.getFileDescription());
      publisherDataEvaluationContext.setAsset(asset);
      publisherDataEvaluationContext.setFileInfoId(publisherDataAbsorptionContext.getFileInfoId());
      publisherDataEvaluationContext.setUserId(publisherDataAbsorptionContext.getUserId());
      publisherDataEvaluationContext.setOriginalFileName(publisherDataAbsorptionContext.getOriginalFileName());
      publisherDataEvaluationContext.setDatasetCollectionCreation(publisherDataAbsorptionContext
               .isDatasetCollectionCreation());
      publisherDataEvaluationContext.setPublisherProcessType(publisherDataAbsorptionContext.getPublisherProcessType());
      return scheduleDataEvaluationJob(publisherDataEvaluationContext);
   }

   public Long scheduleDataEvaluationJob (PublisherDataEvaluationContext publisherDataEvaluationContext)
            throws ExeCueException {
      PublishedFileInfo publishedFileInfo = getPublishedFileRetrievalService().getPublishedFileInfoById(
               publisherDataEvaluationContext.getFileInfoId());
      Long applicationId = publisherDataEvaluationContext.getApplicationId();
      Long modelId = publisherDataEvaluationContext.getModelId();

      publisherDataEvaluationContext.setApplicationId(applicationId);
      publisherDataEvaluationContext.setModelId(modelId);
      publishedFileInfo.setApplicationId(publisherDataEvaluationContext.getApplicationId());
      publishedFileInfo.setModelId(publisherDataEvaluationContext.getModelId());

      publishedFileInfo.setCurrentOperation(PublisherFlowOperationType.METADATA_CREATION);
      publishedFileInfo.setCurrentOperationStatus(JobStatus.PENDING);
      getPublishedFileManagementService().updatePublishedFileInfo(publishedFileInfo);
      return getPublisherDataEvaluationJobService().schedulePublisherDataEvaluation(publisherDataEvaluationContext);
   }

   public UIPublishedFileInfo getFileInfo (JobType jobType, String jobReqeustData) throws ExeCueException {
      UIPublishedFileInfo uiPublishedFileInfo = new UIPublishedFileInfo();
      PublisherDataAbsorptionContext publisherDataAbsorptionContext = null;
      PublisherDataEvaluationContext publisherDataEvaluationContext = null;
      Long fileInfoId = null;
      if (JobType.PUBLISHER_DATA_ABSORPTION.equals(jobType)) {
         publisherDataAbsorptionContext = (PublisherDataAbsorptionContext) ExeCueXMLUtils
                  .getObjectFromXMLString(jobReqeustData);
         fileInfoId = publisherDataAbsorptionContext.getFileInfoId();
      } else {
         publisherDataEvaluationContext = (PublisherDataEvaluationContext) ExeCueXMLUtils
                  .getObjectFromXMLString(jobReqeustData);
         fileInfoId = publisherDataEvaluationContext.getFileInfoId();
      }
      uiPublishedFileInfo.setFileId(String.valueOf(fileInfoId));
      PublishedFileInfo publishedFileInfo = getPublishedFileRetrievalService().getPublishedFileInfoById(fileInfoId);
      uiPublishedFileInfo.setOriginalFileName(publishedFileInfo.getOriginalFileName());
      uiPublishedFileInfo.setApplicationId(publishedFileInfo.getApplicationId());
      uiPublishedFileInfo.setSourceType(publishedFileInfo.getSourceType());
      return uiPublishedFileInfo;
   }

   public UIPublishedFileInfo getFileInfo (Long publishedFileInfoId) throws PublishedFileException {
      UIPublishedFileInfo uiPublishedFileInfo = new UIPublishedFileInfo();
      uiPublishedFileInfo.setFileId(String.valueOf(publishedFileInfoId));
      PublishedFileInfo publishedFileInfo = getPublishedFileRetrievalService().getPublishedFileInfoById(
               publishedFileInfoId);
      uiPublishedFileInfo.setOriginalFileName(publishedFileInfo.getOriginalFileName());
      uiPublishedFileInfo.setApplicationId(publishedFileInfo.getApplicationId());
      uiPublishedFileInfo.setSourceType(publishedFileInfo.getSourceType());
      uiPublishedFileInfo.setDatasetCollectionCreation(publishedFileInfo.getDatasetCollectionCreation());
      return uiPublishedFileInfo;
   }

   public Map<String, List<UIPublishedFileTableDetail>> getPublishedFileTableDetails (Long fileInfoTableId)
            throws PublishedFileException {
      Map<String, List<UIPublishedFileTableDetail>> grainDefinitions = new HashMap<String, List<UIPublishedFileTableDetail>>();
      List<PublishedFileTableDetails> publishedFileTableDetails = getPublishedFileRetrievalService()
               .getPublishedFileTableDetailsByTableId(fileInfoTableId);

      if (ExecueCoreUtil.isCollectionNotEmpty(publishedFileTableDetails)) {
         grainDefinitions.put("population", getPopulations(publishedFileTableDetails));
         grainDefinitions.put("distribution", getDistributions(publishedFileTableDetails));
         grainDefinitions.put("existingDistribution", getSelectedDistributions(publishedFileTableDetails));
         grainDefinitions.put("existingPopulation", getSelectedPopulations(publishedFileTableDetails));
      }
      return grainDefinitions;
   }

   private List<UIPublishedFileTableDetail> getSelectedPopulations (
            List<PublishedFileTableDetails> publishedFileTableDetails) {
      List<UIPublishedFileTableDetail> existingPopulations = new ArrayList<UIPublishedFileTableDetail>();
      for (PublishedFileTableDetails publishedFileTableDetail : publishedFileTableDetails) {
         if (publishedFileTableDetail.getPopulation().equals(CheckType.YES)) {
            UIPublishedFileTableDetail uiPublishedFileTableDetail = new UIPublishedFileTableDetail();
            uiPublishedFileTableDetail.setId(publishedFileTableDetail.getId());
            uiPublishedFileTableDetail.setEvaluatedColumnName(publishedFileTableDetail.getEvaluatedColumnName());
            existingPopulations.add(uiPublishedFileTableDetail);
         }
      }
      return existingPopulations;
   }

   private List<UIPublishedFileTableDetail> getSelectedDistributions (
            List<PublishedFileTableDetails> publishedFileTableDetails) {
      List<UIPublishedFileTableDetail> existingDistributions = new ArrayList<UIPublishedFileTableDetail>();
      for (PublishedFileTableDetails publishedFileTableDetail : publishedFileTableDetails) {
         if (publishedFileTableDetail.getDistribution().equals(CheckType.YES)) {
            UIPublishedFileTableDetail uiPublishedFileTableDetail = new UIPublishedFileTableDetail();
            uiPublishedFileTableDetail.setId(publishedFileTableDetail.getId());
            uiPublishedFileTableDetail.setEvaluatedColumnName(publishedFileTableDetail.getEvaluatedColumnName());
            existingDistributions.add(uiPublishedFileTableDetail);
         }
      }
      return existingDistributions;
   }

   private List<UIPublishedFileTableDetail> getPopulations (List<PublishedFileTableDetails> publishedFileTableDetails) {
      List<UIPublishedFileTableDetail> populations = new ArrayList<UIPublishedFileTableDetail>();
      for (PublishedFileTableDetails publishedFileTableDetail : publishedFileTableDetails) {
         UIPublishedFileTableDetail uiPublishedFileTableDetail = new UIPublishedFileTableDetail();
         uiPublishedFileTableDetail.setId(publishedFileTableDetail.getId());
         uiPublishedFileTableDetail.setEvaluatedColumnName(publishedFileTableDetail.getEvaluatedColumnName());
         populations.add(uiPublishedFileTableDetail);
      }
      return populations;

   }

   private List<UIPublishedFileTableDetail> getDistributions (List<PublishedFileTableDetails> publishedFileTableDetails) {
      List<UIPublishedFileTableDetail> distributions = new ArrayList<UIPublishedFileTableDetail>();
      for (PublishedFileTableDetails publishedFileTableDetail : publishedFileTableDetails) {
         if (publishedFileTableDetail.getEvaluatedDataType().equals(DataType.DATE)
         // || publishedFileTableDetail.getIsLocationBased().equals(CheckType.YES)
         // || publishedFileTableDetail.getIsTimeBased().equals(CheckType.YES)
         ) {
            UIPublishedFileTableDetail uiPublishedFileTableDetail = new UIPublishedFileTableDetail();
            uiPublishedFileTableDetail.setId(publishedFileTableDetail.getId());
            uiPublishedFileTableDetail.setEvaluatedColumnName(publishedFileTableDetail.getEvaluatedColumnName());
            distributions.add(uiPublishedFileTableDetail);
         }
      }
      return distributions;
   }

   public void updatePublishedFileTableDetails (Long fileTableInfoId, List<Long> selectedPopulations,
            List<Long> selectedDistributions) throws PublishedFileException {
      log.debug("fileTableInfoId:-" + fileTableInfoId);
      log.debug("selectedPopulations:-" + selectedPopulations);
      log.debug("selectedDistributions:-" + selectedDistributions);
      List<PublishedFileTableDetails> publishedFileTableDetails = getPublishedFileRetrievalService()
               .getPublishedFileTableDetailsByTableId(fileTableInfoId);
      if (ExecueCoreUtil.isCollectionNotEmpty(publishedFileTableDetails)) {
         if (ExecueCoreUtil.isCollectionNotEmpty(selectedPopulations)) {
            prepareUpdatePupulationList(selectedPopulations, publishedFileTableDetails);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(selectedDistributions)) {
            prepareUpdateDistributionsList(selectedDistributions, publishedFileTableDetails);
         }
         for (PublishedFileTableDetails publishedFileTableDetail : publishedFileTableDetails) {
            log.debug("Id::" + publishedFileTableDetail.getId());
            log.debug("IsPopulation::" + publishedFileTableDetail.getPopulation());
            log.debug("IsDistribution::" + publishedFileTableDetail.getDistribution());
         }
         getPublishedFileManagementService().updatePublishedFileTableDetails(publishedFileTableDetails);
      }
   }

   private void prepareUpdateDistributionsList (List<Long> selectedDistributions,
            List<PublishedFileTableDetails> publishedFileTableDetails) {
      // set the default value 'N' for IsDestribution
      List<PublishedFileTableDetails> publishedFileTableDetailsWithDefaultValue = getDistributionsWithDefaultValue(publishedFileTableDetails);
      for (Long distributionId : selectedDistributions) {
         for (PublishedFileTableDetails publishedFileTableDetail : publishedFileTableDetailsWithDefaultValue) {
            if (publishedFileTableDetail.getId().equals(distributionId)) {
               // set the default value 'Y' for IsDestribution for the selected distributions
               publishedFileTableDetail.setDistribution(CheckType.YES);
               break;
            }
         }
      }

   }

   private void prepareUpdatePupulationList (List<Long> selectedPopulations,
            List<PublishedFileTableDetails> publishedFileTableDetails) {
      // set the default value 'N' for IsPopulation
      List<PublishedFileTableDetails> publishedFileTableDetailsWithDefaultValue = getPopulationWithDefaultValue(publishedFileTableDetails);
      for (Long populationId : selectedPopulations) {
         for (PublishedFileTableDetails publishedFileTableDetail : publishedFileTableDetailsWithDefaultValue) {
            // set the default value 'Y' for IsPopulation for the selected population
            if (publishedFileTableDetail.getId().equals(populationId)) {
               publishedFileTableDetail.setPopulation(CheckType.YES);
               break;
            }
         }
      }

   }

   private List<PublishedFileTableDetails> getDistributionsWithDefaultValue (
            List<PublishedFileTableDetails> publishedFileTableDetails) {
      for (PublishedFileTableDetails publishedFileTableDetail : publishedFileTableDetails) {
         publishedFileTableDetail.setDistribution(CheckType.NO);
      }
      return publishedFileTableDetails;
   }

   private List<PublishedFileTableDetails> getPopulationWithDefaultValue (
            List<PublishedFileTableDetails> publishedFileTableDetails) {
      for (PublishedFileTableDetails publishedFileTableDetail : publishedFileTableDetails) {
         publishedFileTableDetail.setPopulation(CheckType.NO);
      }
      return publishedFileTableDetails;
   }

   public Long getFileTableInfoId (Long fileInfoId) throws PublishedFileException {
      Long fileTableInfoId = null;
      List<PublishedFileTableInfo> fileTableInfos = getPublishedFileRetrievalService()
               .getFileTableInfoListByFileInfoId(fileInfoId);
      if (ExecueCoreUtil.isCollectionNotEmpty(fileTableInfos)) {
         fileTableInfoId = fileTableInfos.get(0).getId(); // TODO: -RG- Correct this when multiple tables comes int
         // the flow
      }
      return fileTableInfoId;
   }

   public List<UIPublishedFileTableDetail> getEvaluatedColumns (Long fileTableInfoId) throws PublishedFileException {
      List<UIPublishedFileTableDetail> uiPublishedFileTableDetails = new ArrayList<UIPublishedFileTableDetail>();
      List<PublishedFileTableDetails> publishedFileTableDetails = getPublishedFileRetrievalService()
               .getPublishedFileTableDetailsByTableId(fileTableInfoId);
      if (ExecueCoreUtil.isCollectionNotEmpty(publishedFileTableDetails)) {
         uiPublishedFileTableDetails = transformUIPublishedFileTableDetail(publishedFileTableDetails);
      }
      return uiPublishedFileTableDetails;
   }

   private List<UIPublishedFileTableDetail> transformUIPublishedFileTableDetail (
            List<PublishedFileTableDetails> publishedFileTableDetails) {
      List<UIPublishedFileTableDetail> uIPublishedFileTableDetails = new ArrayList<UIPublishedFileTableDetail>();
      for (PublishedFileTableDetails publishedFileTableDetail : publishedFileTableDetails) {
         UIPublishedFileTableDetail uiPublishedFileTableDetail = new UIPublishedFileTableDetail();
         uiPublishedFileTableDetail.setId(publishedFileTableDetail.getId());
         uiPublishedFileTableDetail.setBaseColumnName(publishedFileTableDetail.getBaseColumnName());
         uiPublishedFileTableDetail.setEvaluatedColumnName(publishedFileTableDetail.getEvaluatedColumnName());
         uiPublishedFileTableDetail.setBaseDataType(getPublisherDataTypeFromDataType(publishedFileTableDetail
                  .getBaseDataType()));
         uiPublishedFileTableDetail.setEvaluatedDataType(getPublisherDataTypeFromDataType(publishedFileTableDetail
                  .getEvaluatedDataType()));
         uiPublishedFileTableDetail.setBasePrecision(publishedFileTableDetail.getBasePrecision());
         uiPublishedFileTableDetail.setEvaluatedPrecision(publishedFileTableDetail.getEvaluatedPrecision());
         uiPublishedFileTableDetail.setBaseScale(publishedFileTableDetail.getBaseScale());
         uiPublishedFileTableDetail.setEvaluatedScale(publishedFileTableDetail.getEvaluatedScale());
         uiPublishedFileTableDetail.setKdxDataType(publishedFileTableDetail.getKdxDataType());
         uiPublishedFileTableDetail.setIsDistribution(publishedFileTableDetail.getDistribution());
         uiPublishedFileTableDetail.setIsPopulation(publishedFileTableDetail.getPopulation());
         uiPublishedFileTableDetail.setFormat(publishedFileTableDetail.getFormat());
         uiPublishedFileTableDetail.setUnit(publishedFileTableDetail.getUnit());
         uiPublishedFileTableDetail.setUnitType(publishedFileTableDetail.getUnitType());
         uiPublishedFileTableDetail.setColumnIndex(publishedFileTableDetail.getColumnIndex());
         uiPublishedFileTableDetail.setGranularity(publishedFileTableDetail.getGranularity());
         uiPublishedFileTableDetail.setDefaultMetric(publishedFileTableDetail.getDefaultMetric());
         uIPublishedFileTableDetails.add(uiPublishedFileTableDetail);
      }
      return uIPublishedFileTableDetails;

   }

   public List<UIPublisherJobStatus> getJobRequestStatus () throws PublishedFileException {
      List<UIPublisherJobStatus> uiJobRequestStatus = new ArrayList<UIPublisherJobStatus>();
      try {
         List<JobRequest> jobRequests = getJobDataService().getJobRequestByUser(getUserContext().getUser().getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(jobRequests)) {
            for (JobRequest jobRequest : jobRequests) {
               UIPublisherJobStatus uiJobStatus = new UIPublisherJobStatus();
               PublisherDataAbsorptionContext publisherDataAbsorptionContext = null;
               PublisherDataEvaluationContext publisherDataEvaluationContext = null;
               if (JobType.PUBLISHER_DATA_ABSORPTION.equals(jobRequest.getJobType())) {
                  publisherDataAbsorptionContext = (PublisherDataAbsorptionContext) ExeCueXMLUtils
                           .getObjectFromXMLString(jobRequest.getRequestData());
                  uiJobStatus.setFileName(publisherDataAbsorptionContext.getOriginalFileName());
                  uiJobStatus.setAppName(getApplicationName(publisherDataAbsorptionContext.getApplicationId()));
                  uiJobStatus.setDataSetName(publisherDataAbsorptionContext.getFileName());
               }
               if (JobType.PUBLISHER_DATA_EVALUATION.equals(jobRequest.getJobType())) {
                  publisherDataEvaluationContext = (PublisherDataEvaluationContext) ExeCueXMLUtils
                           .getObjectFromXMLString(jobRequest.getRequestData());
                  uiJobStatus.setFileName(publisherDataEvaluationContext.getOriginalFileName());
                  uiJobStatus.setAppName(getApplicationName(publisherDataEvaluationContext.getApplicationId()));
                  uiJobStatus.setDataSetName(publisherDataEvaluationContext.getAsset().getDisplayName());
               }
               uiJobStatus.setId(jobRequest.getId());
               uiJobStatus.setJobType(jobRequest.getJobType());
               uiJobStatus.setJobStatus(jobRequest.getJobStatus());

               if (JobStatus.FAILURE.equals(jobRequest.getJobStatus())) {
                  List<JobHistoryOperationalStatus> jobHistory = getJobDataService().getJobHistoryOperationalStatus(
                           jobRequest.getId());
                  if (ExecueCoreUtil.isCollectionNotEmpty(jobHistory)) {
                     for (JobHistoryOperationalStatus jobHistoryOperationalStatus : jobHistory) {
                        if (jobRequest.getJobStatus().equals(jobHistoryOperationalStatus.getJobStatus())) {
                           String remark = jobHistoryOperationalStatus.getStatusDetail();
                           if (ExecueCoreUtil.isNotEmpty(remark)) {
                              uiJobStatus.setRemark(remark);
                           }
                        }
                     }
                  }
               }
               uiJobRequestStatus.add(uiJobStatus);
            }
         }
      } catch (QueryDataException e) {
         e.printStackTrace();
      } catch (KDXException e) {
         e.printStackTrace();
      }
      return uiJobRequestStatus;
   }

   private String getApplicationName (Long applicationId) throws KDXException {
      String applicationName = null;
      Application application = getApplicationRetrievalService().getApplicationById(applicationId);
      if (application != null) {
         applicationName = application.getName();
      }
      return applicationName;
   }

   public String convertXLFileToCSV (String excelFilePath, Long applicationId, String csvFileStoragePath)
            throws PublishedFileException {
      String csvFilePath;
      try {
         XLFileTransformer xlFileTransformer = new XLFileTransformer();
         csvFilePath = xlFileTransformer.convertXLFileToCSV(excelFilePath, applicationId, csvFileStoragePath);
      } catch (ExeCueException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return csvFilePath;
   }

   private PublisherDataType getPublisherDataTypeFromDataType (DataType dataType) {
      PublisherDataType publisherDataType = null;
      switch (dataType) {
         case CHARACTER:
            publisherDataType = PublisherDataType.CHARACTER;
            break;
         case DATE:
            publisherDataType = PublisherDataType.DATE;
            break;
         case DATETIME:
            publisherDataType = PublisherDataType.DATETIME;
            break;

         case INT:
            publisherDataType = PublisherDataType.INT;
            break;

         case NUMBER:
            publisherDataType = PublisherDataType.NUMBER;
            break;

         case STRING:
            publisherDataType = PublisherDataType.STRING;
            break;
         case TIME:
            publisherDataType = PublisherDataType.TIME;
            break;
         case LARGE_INTEGER:
            publisherDataType = PublisherDataType.LARGE_INTEGER;
            break;
      }
      return publisherDataType;
   }

   public List<PublishedFileTableInfo> getPublishedFileTables (Long fileId) throws PublishedFileException {
      return getPublishedFileRetrievalService().getPublishedFileTableInfoByFileId(fileId);
   }

   public IPublisherConfigurationService getPublisherConfigurationService () {
      return publisherConfigurationService;
   }

   public void setPublisherConfigurationService (IPublisherConfigurationService publisherConfigurationService) {
      this.publisherConfigurationService = publisherConfigurationService;
   }

   public IPublisherDataAbsorbtionJobService getPublisherDataAbsorbtionJobService () {
      return publisherDataAbsorbtionJobService;
   }

   public void setPublisherDataAbsorbtionJobService (
            IPublisherDataAbsorbtionJobService publisherDataAbsorbtionJobService) {
      this.publisherDataAbsorbtionJobService = publisherDataAbsorbtionJobService;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService
    *           the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IPublisherDataEvaluationJobService getPublisherDataEvaluationJobService () {
      return publisherDataEvaluationJobService;
   }

   public void setPublisherDataEvaluationJobService (
            IPublisherDataEvaluationJobService publisherDataEvaluationJobService) {
      this.publisherDataEvaluationJobService = publisherDataEvaluationJobService;
   }

   public IPublisherDataEvaluationService getPublisherDataEvaluationService () {
      return publisherDataEvaluationService;
   }

   public void setPublisherDataEvaluationService (IPublisherDataEvaluationService publisherDataEvaluationService) {
      this.publisherDataEvaluationService = publisherDataEvaluationService;
   }

   public PublishedFileInfo saveDataFile (File sourceData, String fileName, String fileDescription,
            String originalFileName, boolean isColumnsAvailable, String dataDelimeter, CSVEmptyField csvEmptyField,
            CSVStringEnclosedCharacterType stringEnclosedCharacterType, PublishedOperationType operationType,
            PublishedFileType sourceType, String fileLink, String tags, Long applicationId, Long modelId,
            CheckType absorbDataSet, PublisherProcessType publisherProcessType, CheckType uploadAsCompressedFile)
            throws PublishedFileException {
      CheckType isFileLink = CheckType.YES;
      String destinationFileName = null;

      if (sourceData != null) {

         isFileLink = CheckType.NO;
         String destinationFilePath = getFileStoragePath(sourceType);
         String csvDestinationFilePath = getFileStoragePath(PublishedFileType.CSV);
         destinationFileName = getFileName(originalFileName);
         destinationFileName = destinationFilePath + "/" + destinationFileName.toLowerCase();

         File destination = null;
         destination = new File(destinationFileName);
         if (destination.exists()) {
            destination.delete();
            destination = new File(destinationFileName);
         }
         try {
            ExecueCoreUtil.copyFile(sourceData, destination);
            if (CheckType.YES.equals(uploadAsCompressedFile)) {
               destinationFileName = FileUtilities.unzipFile(destinationFileName, destinationFilePath);
            }
            if (PublishedFileType.EXCEL.equals(sourceType)) {
               destinationFileName = convertXLFileToCSV(destinationFileName, null, csvDestinationFilePath);
            }
         } catch (Exception e) {
            log.error("File rename failed to [" + destinationFileName + "], [" + uploadAsCompressedFile + "], ["
                     + sourceType + "], " + e.getMessage());
            throw new PublishedFileException(1001, e);
         }

      }
      PublishedFileInfo publishedFileInfo = new PublishedFileInfo();
      publishedFileInfo.setFileName(fileName);
      publishedFileInfo.setFileDescription(fileDescription);
      publishedFileInfo.setOriginalFileName(originalFileName);
      publishedFileInfo.setFileLocation(destinationFileName);
      publishedFileInfo.setUserId(getUserContext().getUser().getId());
      publishedFileInfo.setApplicationId(applicationId);
      publishedFileInfo.setModelId(modelId);
      publishedFileInfo.setPublishedOperationType(operationType);
      publishedFileInfo.setSourceType(sourceType);
      publishedFileInfo.setCurrentOperation(PublisherFlowOperationType.FILE_TRANSFER);
      publishedFileInfo.setCurrentOperationStatus(JobStatus.SUCCESS);
      publishedFileInfo.setDatasetCollectionCreation(absorbDataSet);
      publishedFileInfo.setPublisherProcessType(publisherProcessType);
      // TODO : Change the status
      publishedFileInfo.setFileLink(isFileLink);
      getPublishedFileManagementService().persistPublishedFileInfo(publishedFileInfo);
      List<PublishedFileInfoDetails> publishedFileInfoDetails = persistPublishedFileInfoDetails(publishedFileInfo,
               isColumnsAvailable, dataDelimeter, csvEmptyField, stringEnclosedCharacterType, fileLink, tags);
      publishedFileInfo.setPublishedFileInfoDetails(new HashSet<PublishedFileInfoDetails>(publishedFileInfoDetails));
      return publishedFileInfo;
   }

   public String getFileName (String originalFileName) {
      return ExecueCoreUtil.getFormattedSystemCurrentMillis() + "_" + RandomStringUtils.randomAlphabetic(3) + "_"
               + getUserContext().getUser().getId() + "_" + originalFileName;
   }

   public String getFileStoragePath (PublishedFileType publishedFileType) {
      return getPublisherConfigurationService().getPublisherFileStoragePath(publishedFileType.name());

   }

   private List<PublishedFileInfoDetails> persistPublishedFileInfoDetails (PublishedFileInfo publishedFileInfo,
            boolean isColumnsAvailable, String dataDelimeter, CSVEmptyField csvEmptyField,
            CSVStringEnclosedCharacterType csvStringEnclosedCharacterType, String fileLink, String tags)
            throws PublishedFileException {
      List<PublishedFileInfoDetails> publishedFileInfoDetailsList = new ArrayList<PublishedFileInfoDetails>();
      PublishedFileInfoDetails publishedFileInfoDetail = null;

      publishedFileInfoDetail = new PublishedFileInfoDetails();

      publishedFileInfoDetail.setPropertyName(IPublisherConstants.IS_COLUMN_AVAILABLE);
      publishedFileInfoDetail.setPropertyValue(isColumnsAvailable + "");
      publishedFileInfoDetail.setPublishedFileInfo(publishedFileInfo);

      publishedFileInfoDetailsList.add(publishedFileInfoDetail);

      publishedFileInfoDetail = new PublishedFileInfoDetails();

      publishedFileInfoDetail.setPropertyName(IPublisherConstants.CSV_DELIMETER);
      publishedFileInfoDetail.setPropertyValue(dataDelimeter);
      publishedFileInfoDetail.setPublishedFileInfo(publishedFileInfo);

      publishedFileInfoDetailsList.add(publishedFileInfoDetail);

      publishedFileInfoDetail = new PublishedFileInfoDetails();

      publishedFileInfoDetail.setPropertyName(IPublisherConstants.CSV_EMPTY_FIELD);
      publishedFileInfoDetail.setPropertyValue(csvEmptyField.getValue());
      publishedFileInfoDetail.setPublishedFileInfo(publishedFileInfo);

      publishedFileInfoDetailsList.add(publishedFileInfoDetail);

      publishedFileInfoDetail = new PublishedFileInfoDetails();

      publishedFileInfoDetail.setPropertyName(IPublisherConstants.CSV_ENCLOSED_CHAR_TYPE);
      publishedFileInfoDetail.setPropertyValue(csvStringEnclosedCharacterType.getValue());
      publishedFileInfoDetail.setPublishedFileInfo(publishedFileInfo);

      publishedFileInfoDetailsList.add(publishedFileInfoDetail);

      publishedFileInfoDetail = new PublishedFileInfoDetails();

      publishedFileInfoDetail.setPropertyName(IPublisherConstants.FILE_LINK);
      publishedFileInfoDetail.setPropertyValue(fileLink);
      publishedFileInfoDetail.setPublishedFileInfo(publishedFileInfo);

      publishedFileInfoDetailsList.add(publishedFileInfoDetail);

      publishedFileInfoDetail = new PublishedFileInfoDetails();

      publishedFileInfoDetail.setPropertyName(IPublisherConstants.FILE_TAGS);
      publishedFileInfoDetail.setPropertyValue(tags);
      publishedFileInfoDetail.setPublishedFileInfo(publishedFileInfo);

      publishedFileInfoDetailsList.add(publishedFileInfoDetail);

      getPublishedFileManagementService().persistPublishedFileInfoDetails(publishedFileInfoDetailsList);
      return publishedFileInfoDetailsList;
   }

   public List<UIPublishedFileInfo> getPublishedFiles () throws ExeCueException {
      List<PublishedFileInfo> publishedFilesInfoEligibleForAbsorption = getPublishedFileRetrievalService()
               .getPublishedFilesInfoEligibleForAbsorption(getUserContext().getUser().getId());
      List<PublishedFileInfo> publishedFilesInfoNotEligibleForAbsorption = getPublishedFileRetrievalService()
               .getPublishedFilesInfoNotEligibleForAbsorption(getUserContext().getUser().getId());

      List<UIPublishedFileInfo> uiPublishedFilesInfo = new ArrayList<UIPublishedFileInfo>();

      for (PublishedFileInfo publishedFileInfo : publishedFilesInfoEligibleForAbsorption) {
         uiPublishedFilesInfo.add(transformPublishedFilesInfo(publishedFileInfo, true));
      }

      for (PublishedFileInfo publishedFileInfo : publishedFilesInfoNotEligibleForAbsorption) {
         uiPublishedFilesInfo.add(transformPublishedFilesInfo(publishedFileInfo, false));
      }
      return uiPublishedFilesInfo;
   }

   // public List<UIPublishedFileInfo> getPublishedFiles () throws ExeCueException {
   // List<PublishedFileInfo> publishedFilesInfo = getPublishedFileService().getPublishedFileInfoByUserId(
   // getUserContext().getUser().getId());
   // List<UIPublishedFileInfo> uiPublishedFilesInfo = transformPublishedFilesInfo(publishedFilesInfo);
   // return uiPublishedFilesInfo;
   // }

   public List<UIPublishedFileInfo> getPublishedFilesByPage (Page page) throws PublishedFileException {
      List<UIPublishedFileInfo> uiPublishedFilesInfo = new ArrayList<UIPublishedFileInfo>();
      // set the user info into the Page object
      if (!"ROLE_ADMIN".equalsIgnoreCase(getUserContext().getRole())) {
         List<PageSearch> searchList = page.getSearchList();
         if (ExecueCoreUtil.isCollectionEmpty(searchList)) {
            searchList = new ArrayList<PageSearch>();
            page.setSearchList(searchList);
         }
         PageSearch userIdSearch = new PageSearch();
         userIdSearch.setField("userId");
         userIdSearch.setType(PageSearchType.EQUALS);
         userIdSearch.setString(getUserContext().getUser().getId().toString());
         searchList.add(userIdSearch);
      }
      List<PublishedFileInfo> publishedFilesInfoByPage = getPublishedFileRetrievalService().getPublishedFilesByPage(
               page);
      uiPublishedFilesInfo = transformPublishedFilesInfo(publishedFilesInfoByPage);
      return uiPublishedFilesInfo;
   }

   private List<PublishedFileTableDetails> transformPublishedFileTableDetail (
            List<UIPublishedFileTableDetail> evaluatedColumns) {
      List<PublishedFileTableDetails> publishedFileTableDetails = new ArrayList<PublishedFileTableDetails>();

      for (UIPublishedFileTableDetail uiPublishedFileTableDetail : evaluatedColumns) {
         PublishedFileTableDetails publishedFileTableDetail = new PublishedFileTableDetails();
         publishedFileTableDetail.setId(uiPublishedFileTableDetail.getId());
         publishedFileTableDetail.setBaseColumnName(uiPublishedFileTableDetail.getBaseColumnName());
         publishedFileTableDetail.setEvaluatedColumnName(uiPublishedFileTableDetail.getEvaluatedColumnName());
         publishedFileTableDetail.setBaseDataType(getDataTypeFromPublisherDataType(uiPublishedFileTableDetail
                  .getBaseDataType()));
         publishedFileTableDetail.setEvaluatedDataType(getDataTypeFromPublisherDataType(uiPublishedFileTableDetail
                  .getEvaluatedDataType()));
         publishedFileTableDetail.setBasePrecision(uiPublishedFileTableDetail.getBasePrecision());
         publishedFileTableDetail.setEvaluatedPrecision(uiPublishedFileTableDetail.getEvaluatedPrecision());
         publishedFileTableDetail.setBaseScale(uiPublishedFileTableDetail.getBaseScale());
         publishedFileTableDetail.setEvaluatedScale(uiPublishedFileTableDetail.getEvaluatedScale());
         publishedFileTableDetail.setKdxDataType(uiPublishedFileTableDetail.getKdxDataType());
         publishedFileTableDetail.setGranularity(uiPublishedFileTableDetail.getGranularity());
         publishedFileTableDetail.setDefaultMetric(uiPublishedFileTableDetail.getDefaultMetric());
         if (!CheckType.YES.equals(uiPublishedFileTableDetail.getIsDistribution())) {
            publishedFileTableDetail.setDistribution(CheckType.NO);
         } else {
            publishedFileTableDetail.setDistribution(uiPublishedFileTableDetail.getIsDistribution());
         }
         /*
          * if (!CheckType.YES.equals(uiPublishedFileTableDetail.getIsLocationBased())) {
          * publishedFileTableDetail.setIsLocationBased(CheckType.NO); } else {
          * publishedFileTableDetail.setIsLocationBased(uiPublishedFileTableDetail.getIsLocationBased()); }
          */
         if (!CheckType.YES.equals(uiPublishedFileTableDetail.getIsPopulation())) {
            publishedFileTableDetail.setPopulation(CheckType.NO);
         } else {
            publishedFileTableDetail.setPopulation(uiPublishedFileTableDetail.getIsPopulation());
         }
         /*
          * if (!CheckType.YES.equals(uiPublishedFileTableDetail.getIsTimeBased())) {
          * publishedFileTableDetail.setIsTimeBased(CheckType.NO); } else {
          * publishedFileTableDetail.setIsTimeBased(uiPublishedFileTableDetail.getIsTimeBased()); }
          */
         publishedFileTableDetail.setFormat(uiPublishedFileTableDetail.getFormat());
         publishedFileTableDetail.setUnit(uiPublishedFileTableDetail.getUnit());
         publishedFileTableDetail.setUnitType(uiPublishedFileTableDetail.getUnitType());
         publishedFileTableDetail.setColumnIndex(uiPublishedFileTableDetail.getColumnIndex());
         publishedFileTableDetails.add(publishedFileTableDetail);
      }
      return publishedFileTableDetails;
   }

   private DataType getDataTypeFromPublisherDataType (PublisherDataType publisherDataType) {
      DataType dataType = null;
      switch (publisherDataType) {
         case CHARACTER:
            dataType = DataType.CHARACTER;
            break;
         case DATE:
            dataType = DataType.DATE;
            break;
         case DATETIME:
            dataType = DataType.DATETIME;
            break;

         case INT:
            dataType = DataType.INT;
            break;

         case NUMBER:
            dataType = DataType.NUMBER;
            break;

         case STRING:
            dataType = DataType.STRING;
            break;
         case TIME:
            dataType = DataType.TIME;
            break;
         case LOCATION:
            dataType = DataType.STRING;
            break;
         case LARGE_INTEGER:
            dataType = DataType.LARGE_INTEGER;
            break;
      }
      return dataType;
   }

   private List<UIPublishedFileInfo> transformPublishedFilesInfo (List<PublishedFileInfo> publishedFilesInfo) {
      boolean isEligibleForAbsorption = false;
      List<UIPublishedFileInfo> uiPublishedFilesInfo = new ArrayList<UIPublishedFileInfo>();
      for (PublishedFileInfo publishedFileInfo : publishedFilesInfo) {
         isEligibleForAbsorption = checkEligibleForAbsorption(publishedFileInfo);
         uiPublishedFilesInfo.add(transformPublishedFilesInfo(publishedFileInfo, isEligibleForAbsorption));
      }
      return uiPublishedFilesInfo;
   }

   private boolean checkEligibleForAbsorption (PublishedFileInfo publishedFileInfo) {
      boolean isEligibleForAbsorption = false;
      if (CheckType.NO.equals(publishedFileInfo.getFileLink())
               && CheckType.NO.equals(publishedFileInfo.getFileAbsorbed())
               && !PublishedFileType.OTHER.equals(publishedFileInfo.getSourceType())) {
         isEligibleForAbsorption = true;
      }
      return isEligibleForAbsorption;
   }

   private UIPublishedFileInfo transformPublishedFilesInfo (PublishedFileInfo publishedFileInfo,
            boolean isEligibleForAbsorption) {

      UIPublishedFileInfo uiPublishedFileInfo = new UIPublishedFileInfo();
      uiPublishedFileInfo.setFileId("" + publishedFileInfo.getId());
      uiPublishedFileInfo.setFileName(publishedFileInfo.getFileName());
      uiPublishedFileInfo.setFileDescription(publishedFileInfo.getFileDescription());
      uiPublishedFileInfo.setOriginalFileName(publishedFileInfo.getOriginalFileName());
      uiPublishedFileInfo.setFileLocation(publishedFileInfo.getFileLocation());
      uiPublishedFileInfo.setApplicationId(publishedFileInfo.getApplicationId());
      uiPublishedFileInfo.setModelId(publishedFileInfo.getModelId());
      uiPublishedFileInfo.setUserId(publishedFileInfo.getUserId());
      uiPublishedFileInfo.setDataSourceId(publishedFileInfo.getDatasourceId());
      uiPublishedFileInfo.setSourceType(publishedFileInfo.getSourceType());
      uiPublishedFileInfo.setEligibleForAbsorption(isEligibleForAbsorption);
      uiPublishedFileInfo.setCurrentOperation(publishedFileInfo.getCurrentOperation());
      uiPublishedFileInfo.setCurrentOperationStatus(publishedFileInfo.getCurrentOperationStatus());
      uiPublishedFileInfo.setFileAbsorbed(publishedFileInfo.getFileAbsorbed());
      uiPublishedFileInfo.setDatasetCollectionCreation(publishedFileInfo.getDatasetCollectionCreation());
      Long jobRequestId = publishedFileInfo.getAbsorbtionJobRequestId();
      uiPublishedFileInfo.setJobRequestId(jobRequestId);
      CurrentPublisherFlowStatusType currentPublisherFlowStatusType = CurrentPublisherFlowStatusType.UN_SUPPORTED;
      if (uiPublishedFileInfo.isEligibleForAbsorption()) {
         if (PublisherFlowOperationType.FILE_TRANSFER.equals(uiPublishedFileInfo.getCurrentOperation())) {
            currentPublisherFlowStatusType = CurrentPublisherFlowStatusType.ANALYZE;
         } else if (JobStatus.PENDING.equals(uiPublishedFileInfo.getCurrentOperationStatus())
                  || JobStatus.INPROGRESS.equals(uiPublishedFileInfo.getCurrentOperationStatus())) {
            if (PublisherFlowOperationType.ABSORPTION_AND_DATA_ANALYSIS.equals(uiPublishedFileInfo
                     .getCurrentOperation())) {
               currentPublisherFlowStatusType = CurrentPublisherFlowStatusType.ANALYZING;
            } else {
               currentPublisherFlowStatusType = CurrentPublisherFlowStatusType.ENABLING;
            }
         } else {
            currentPublisherFlowStatusType = CurrentPublisherFlowStatusType.CONFIRM_AND_ENABLE;
         }
      } else if (CheckType.YES.equals(uiPublishedFileInfo.getFileAbsorbed())) {
         currentPublisherFlowStatusType = CurrentPublisherFlowStatusType.ENABLED;
      } else {
         currentPublisherFlowStatusType = CurrentPublisherFlowStatusType.UN_SUPPORTED;
      }

      uiPublishedFileInfo.setStatus(currentPublisherFlowStatusType);
      return uiPublishedFileInfo;
   }

   public boolean isFileExist (String fileName) throws ExeCueException {
      return getPublishedFileRetrievalService().isFileExist(getUserContext().getUser().getId(), fileName);
   }

   public IPublisherColumnValidator getPublisherColumnValidator () {
      return publisherColumnValidator;
   }

   public void setPublisherColumnValidator (IPublisherColumnValidator publisherColumnValidator) {
      this.publisherColumnValidator = publisherColumnValidator;
   }

   private List<String> getAssetNamesUsingTableForUser (String tableName) throws PublishedFileException {
      try {
         return getSdxRetrievalService().getAssetNamesUsingTableForUser(getUserContext().getUser().getId(), tableName);
      } catch (SDXException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<PublishedFileTableInfo> getPublishedFileTableInfoByFileId (Long fileId) throws PublishedFileException {
      return getPublishedFileRetrievalService().getPublishedFileTableInfoByFileId(fileId);
   }

   public void deleteUploadedDataset (PublishedFileInfo publishedFileInfo) throws PublishedFileException {
      try {
         getSwiPlatformDeletionService().deleteUploadedDataset(publishedFileInfo);
      } catch (SWIException e) {
         throw new PublishedFileException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public PublishedFileInfo getPublishedFileInfoById (Long id) throws PublishedFileException {
      return getPublishedFileRetrievalService().getPublishedFileInfoById(id);
   }

   public UIUploadStatus validatePublishedFileInfoForDeletion (UIUploadStatus operationStatus,
            PublishedFileInfo publishedFileInfo) throws PublishedFileException {
      UIPublishedFileInfo uiPublishedFileInfo = transformPublishedFilesInfo(publishedFileInfo,
               checkEligibleForAbsorption(publishedFileInfo));
      if (CurrentPublisherFlowStatusType.ANALYZING.equals(uiPublishedFileInfo.getStatus())
               || CurrentPublisherFlowStatusType.ENABLING.equals(uiPublishedFileInfo.getStatus())) {
         operationStatus.addErrorMessage("An operation on this dataset is in progress");
      } else if (CurrentPublisherFlowStatusType.ENABLED.equals(uiPublishedFileInfo.getStatus())) {
         List<PublishedFileTableInfo> publishedFileTables = getPublishedFileTableInfoByFileId(publishedFileInfo.getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(publishedFileTables)) {
            for (PublishedFileTableInfo publishedFileTableInfo : publishedFileTables) {
               List<String> assetNames = getAssetNamesUsingTableForUser(publishedFileTableInfo.getEvaluatedTableName());
               if (ExecueCoreUtil.isCollectionNotEmpty(assetNames)) {
                  operationStatus
                           .addErrorMessage("Can not perform delete operation as dataset is in use with asset(s) ["
                                    + getStringMessage(assetNames) + "]");
                  break;
               }
            }
         }
      }
      return operationStatus;
   }

   private String getStringMessage (List<String> assetNames) {
      StringBuffer assetName = new StringBuffer();
      if (assetNames.size() == 1) {
         assetName.append(assetNames.get(0));
      } else {
         int cnt = 0;
         int size = assetNames.size();
         for (String name : assetNames) {
            if (cnt == (size - 1)) {
               assetName.append(name);
            } else {
               assetName.append(name + ", ");
            }
            cnt++;
         }
      }
      return assetName.toString();
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

   public IPublishedFileManagementService getPublishedFileManagementService () {
      return publishedFileManagementService;
   }

   public void setPublishedFileManagementService (IPublishedFileManagementService publishedFileManagementService) {
      this.publishedFileManagementService = publishedFileManagementService;
   }

   public IApplicationManagementWrapperService getApplicationManagementWrapperService () {
      return applicationManagementWrapperService;
   }

   public void setApplicationManagementWrapperService (
            IApplicationManagementWrapperService applicationManagementWrapperService) {
      this.applicationManagementWrapperService = applicationManagementWrapperService;
   }

   public ISWIPlatformDeletionService getSwiPlatformDeletionService () {
      return swiPlatformDeletionService;
   }

   public void setSwiPlatformDeletionService (ISWIPlatformDeletionService swiPlatformDeletionService) {
      this.swiPlatformDeletionService = swiPlatformDeletionService;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

}
