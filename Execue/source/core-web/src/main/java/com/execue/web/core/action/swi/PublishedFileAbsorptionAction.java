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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.core.common.bean.qi.QIConversion;
import com.execue.core.common.bean.qi.suggest.SuggestionUnit;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConceptBaseType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.GranularityType;
import com.execue.core.common.type.PublishedFileType;
import com.execue.core.common.type.PublisherDataType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIPublishedFileInfo;
import com.execue.handler.bean.UIPublishedFileTableDetail;
import com.execue.handler.bean.UIPublisherJobStatus;
import com.execue.handler.swi.IConversionServiceHandler;
import com.execue.handler.swi.IUploadServiceHandler;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.exception.SWIException;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author JTiwari
 * @since 04/11/2009
 */

public class PublishedFileAbsorptionAction extends ActionSupport implements SessionAware {

   private static final long                serialVersionUID = 1L;
   private IUploadServiceHandler            uploadServiceHandler;
   private IConversionServiceHandler        conversionServiceHandler;
   private List<UIPublishedFileTableDetail> populations;
   private List<UIPublishedFileTableDetail> distributions;
   private Long                             fileTableInfoId;
   private List<Long>                       selectedPopulations;
   private List<Long>                       selectedDistributions;
   private List<UIPublishedFileTableDetail> existingPopulations;
   private List<UIPublishedFileTableDetail> existingDistributions;
   private Map                              httpSessionData;
   private String                           requestedPage;
   public static final int                  PAGE_SIZE        = 8;
   private List<UIPublishedFileTableDetail> subListForEvaluatedColumsInfo;
   private List<UIPublisherJobStatus>       jobsRequestStatus;
   private Long                             fileInfoId;
   private Long                             jobRequestId;
   private List<PublishedFileTableInfo>     publishedFileTables;
   private Long                             publishedFileId;
   private String                           columnsUpdateStatus;
   private List<String>                     updateValidationErrorMessages;
   private Long                             publishedFileTableId;
   private List<ColumnType>                 columnTypes;
   private PublishedFileType                sourceType;
   private String                           wizardBased;
   private UIPublishedFileInfo              fileInfo;
   private List<GranularityType>            granularityTypes;

   // Action Methods
   public String showGrainDefinitions () {
      try {
         Map<String, List<UIPublishedFileTableDetail>> publishedFileTableDetails = uploadServiceHandler
                  .getPublishedFileTableDetails(fileTableInfoId);
         if (publishedFileTableDetails != null && publishedFileTableDetails.size() > 0) {
            populations = publishedFileTableDetails.get("population");
            distributions = publishedFileTableDetails.get("distribution");
            existingPopulations = publishedFileTableDetails.get("existingPopulation");
            existingDistributions = publishedFileTableDetails.get("existingDistribution");
         }
      } catch (PublishedFileException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String updateGrainDefinitions () {
      try {
         uploadServiceHandler.updatePublishedFileTableDetails(fileTableInfoId, selectedPopulations,
                  selectedDistributions);
      } catch (PublishedFileException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String getEvaluatedColumns () {
      try {
         // fileTableInfoId = getUploadServiceHandler().getFileTableInfoId(fileInfoId);
         List<UIPublishedFileTableDetail> evaluatedColums = uploadServiceHandler.getEvaluatedColumns(fileTableInfoId);
         if (ExecueCoreUtil.isCollectionNotEmpty(evaluatedColums)) {
            prepareUnitAndFormatList(evaluatedColums);
            preparePaginationForEvaluatedColums(evaluatedColums);
         }
         if (columnsUpdateStatus != null) {
            if ("success".equalsIgnoreCase(columnsUpdateStatus)) {
               addActionMessage(getText("execue.evaluatedcolumn.update.success"));
            } else {
               addActionError(getText("execue.evaluatedcolumn.update.failure"));
               for (String validationErrorMessage : updateValidationErrorMessages) {
                  addActionError(validationErrorMessage);
               }
            }
         }
      } catch (PublishedFileException e) {
         e.printStackTrace();
      } catch (SWIException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   private void prepareUnitAndFormatList (List<UIPublishedFileTableDetail> evaluatedColums) throws SWIException {
      for (UIPublishedFileTableDetail publishedFileTableDetail : evaluatedColums) {
         QIConversion qiConversion = null;
         if (publishedFileTableDetail.getUnitType() != null) {
            qiConversion = conversionServiceHandler.getQIConversion(publishedFileTableDetail.getUnitType().getValue());
         }
         if (qiConversion != null) {
            if (ExecueCoreUtil.isCollectionEmpty(qiConversion.getDataFormats())) {
               qiConversion.setDataFormats(new ArrayList<SuggestionUnit>());
            }
            if (ExecueCoreUtil.isCollectionEmpty(qiConversion.getUnits())) {
               qiConversion.setUnits(new ArrayList<SuggestionUnit>());
            }
         } else {
            qiConversion = new QIConversion();
            qiConversion.setUnits(new ArrayList<SuggestionUnit>());
            qiConversion.setDataFormats(new ArrayList<SuggestionUnit>());
         }
         publishedFileTableDetail.setQiConversion(qiConversion);
      }
   }

   private void preparePaginationForEvaluatedColums (List<UIPublishedFileTableDetail> evaluatedColums) {
      if (httpSessionData.containsKey("EVALUATED_COLUMN_LIST")) {
         httpSessionData.remove("EVALUATED_COLUMN_LIST");
         setRequestedPage("1");
      }
      if (requestedPage == null) {
         requestedPage = "1";
      }
      httpSessionData.put("EVALUATED_COLUMN_LIST", evaluatedColums);
      int tempSize = 0;
      if (evaluatedColums.size() <= PAGE_SIZE) {
         tempSize = evaluatedColums.size();
      } else {
         tempSize = PAGE_SIZE;
      }
      subListForEvaluatedColumsInfo = evaluatedColums.subList(0, tempSize);
   }

   public String showEvaluatedSubColumnInfo () {
      int reqPageNo = Integer.parseInt(getRequestedPage());
      int fromIndex = 1;
      int toIndex = 1;
      List<UIPublishedFileTableDetail> evalColumnList = (List<UIPublishedFileTableDetail>) httpSessionData
               .get("EVALUATED_COLUMN_LIST");
      int tempTotCount = (evalColumnList.size() / PAGE_SIZE);
      int rmndr = evalColumnList.size() % PAGE_SIZE;
      if (rmndr != 0) {
         tempTotCount++;
      }

      if (reqPageNo <= tempTotCount) {
         fromIndex = ((reqPageNo - 1) * PAGE_SIZE);
         toIndex = reqPageNo * PAGE_SIZE;
         if (toIndex > evalColumnList.size())
            toIndex = (evalColumnList.size());
      }
      subListForEvaluatedColumsInfo = evalColumnList.subList(fromIndex, toIndex);
      return SUCCESS;
   }

   public String showJobRequestStatus () {
      try {
         jobsRequestStatus = uploadServiceHandler.getJobRequestStatus();
      } catch (PublishedFileException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String showPublishedFileTables () {
      try {
         PublishedFileTableInfo publishedFileTableInfo = null;
         PublishedFileInfo publishedFileInfo = null;
         publishedFileTables = uploadServiceHandler.getPublishedFileTables(publishedFileId);
         if (ExecueCoreUtil.isCollectionNotEmpty(publishedFileTables) && publishedFileTables.size() == 1) {
            publishedFileTableInfo = publishedFileTables.get(0);
            publishedFileTableId = publishedFileTableInfo.getId();
            publishedFileInfo = publishedFileTables.get(0).getPublishedFileInfo();
         } else if (ExecueCoreUtil.isCollectionNotEmpty(publishedFileTables)) {
            publishedFileInfo = publishedFileTables.get(0).getPublishedFileInfo();
         }
         fileInfo = getUploadServiceHandler().getFileInfo(publishedFileInfo.getId());
      } catch (PublishedFileException e) {
         // addActionError("Tables retrieval failed");
         e.printStackTrace();
      }
      return SUCCESS;
   }

   /**
    * @return the uploadServiceHandler
    */
   public IUploadServiceHandler getUploadServiceHandler () {
      return uploadServiceHandler;
   }

   /**
    * @param uploadServiceHandler
    *           the uploadServiceHandler to set
    */
   public void setUploadServiceHandler (IUploadServiceHandler uploadServiceHandler) {
      this.uploadServiceHandler = uploadServiceHandler;
   }

   /**
    * @return the distributions
    */
   public List<UIPublishedFileTableDetail> getDistributions () {
      return distributions;
   }

   /**
    * @param distributions
    *           the distributions to set
    */
   public void setDistributions (List<UIPublishedFileTableDetail> distributions) {
      this.distributions = distributions;
   }

   /**
    * @return the populations
    */
   public List<UIPublishedFileTableDetail> getPopulations () {
      return populations;
   }

   /**
    * @param populations
    *           the populations to set
    */
   public void setPopulations (List<UIPublishedFileTableDetail> populations) {
      this.populations = populations;
   }

   /**
    * @return the fileTableInfoId
    */
   public Long getFileTableInfoId () {
      return fileTableInfoId;
   }

   /**
    * @param fileTableInfoId
    *           the fileTableInfoId to set
    */
   public void setFileTableInfoId (Long fileTableInfoId) {
      this.fileTableInfoId = fileTableInfoId;
   }

   /**
    * @return the selectedPopulations
    */
   public List<Long> getSelectedPopulations () {
      return selectedPopulations;
   }

   /**
    * @param selectedPopulations
    *           the selectedPopulations to set
    */
   public void setSelectedPopulations (List<Long> selectedPopulations) {
      this.selectedPopulations = selectedPopulations;
   }

   /**
    * @return the selectedDistributions
    */
   public List<Long> getSelectedDistributions () {
      return selectedDistributions;
   }

   /**
    * @param selectedDistributions
    *           the selectedDistributions to set
    */
   public void setSelectedDistributions (List<Long> selectedDistributions) {
      this.selectedDistributions = selectedDistributions;
   }

   /**
    * @return the existingPopulations
    */
   public List<UIPublishedFileTableDetail> getExistingPopulations () {
      return existingPopulations;
   }

   /**
    * @param existingPopulations
    *           the existingPopulations to set
    */
   public void setExistingPopulations (List<UIPublishedFileTableDetail> existingPopulations) {
      this.existingPopulations = existingPopulations;
   }

   /**
    * @return the existingDistributions
    */
   public List<UIPublishedFileTableDetail> getExistingDistributions () {
      return existingDistributions;
   }

   /**
    * @param existingDistributions
    *           the existingDistributions to set
    */
   public void setExistingDistributions (List<UIPublishedFileTableDetail> existingDistributions) {
      this.existingDistributions = existingDistributions;
   }

   public List<CheckType> getCheckTypes () {
      return Arrays.asList(CheckType.values());
   }

   public List<PublisherDataType> getDataTypes () {
      return Arrays.asList(PublisherDataType.values());
   }

   public List<ConceptBaseType> getConceptBaseTypes () {
      return Arrays.asList(ConceptBaseType.values());
   }

   public List<ColumnType> getColumnTypes () {
      if (this.columnTypes != null && this.columnTypes.size() > 0) {
         return this.columnTypes;
      } else {
         this.columnTypes = new ArrayList<ColumnType>();
         List<ColumnType> tempColumTypes = Arrays.asList(ColumnType.values());
         for (ColumnType columnType : tempColumTypes) {
            if (ColumnType.NULL.getValue().equals(columnType.getValue())
                     || ColumnType.ID.getValue().equals(columnType.getValue())
                     || ColumnType.MEASURE.getValue().equals(columnType.getValue())
                     || ColumnType.DIMENSION.getValue().equals(columnType.getValue())) {
               this.columnTypes.add(columnType);
            }
         }
         return this.columnTypes;
      }
   }

   public Map getHttpSessionData () {
      return httpSessionData;
   }

   public void setSession (Map httpSessionData) {
      this.httpSessionData = httpSessionData;
   }

   public String getRequestedPage () {
      return requestedPage;
   }

   public void setRequestedPage (String requestedPage) {
      this.requestedPage = requestedPage;
   }

   public List<UIPublishedFileTableDetail> getSubListForEvaluatedColumsInfo () {
      return subListForEvaluatedColumsInfo;
   }

   public void setSubListForEvaluatedColumsInfo (List<UIPublishedFileTableDetail> subListForEvaluatedColumsInfo) {
      this.subListForEvaluatedColumsInfo = subListForEvaluatedColumsInfo;
   }

   public List<ConversionType> getConversionTypes () {
      List<ConversionType> displayConversionTypes = new ArrayList<ConversionType>();
      List<ConversionType> conversionTypes = Arrays.asList(ConversionType.values());
      for (ConversionType conversionType : conversionTypes) {
         if (!ConversionType.DEFAULT.equals(conversionType) && !ConversionType.NULL.equals(conversionType)) {
            displayConversionTypes.add(conversionType);
         }
      }
      displayConversionTypes.add(0, ConversionType.NULL);
      return displayConversionTypes;
   }

   /**
    * @return the conversionServiceHandler
    */
   public IConversionServiceHandler getConversionServiceHandler () {
      return conversionServiceHandler;
   }

   /**
    * @param conversionServiceHandler
    *           the conversionServiceHandler to set
    */
   public void setConversionServiceHandler (IConversionServiceHandler conversionServiceHandler) {
      this.conversionServiceHandler = conversionServiceHandler;
   }

   /**
    * @return the jobsRequestStatus
    */
   public List<UIPublisherJobStatus> getJobsRequestStatus () {
      return jobsRequestStatus;
   }

   /**
    * @param jobsRequestStatus
    *           the jobsRequestStatus to set
    */
   public void setJobsRequestStatus (List<UIPublisherJobStatus> jobsRequestStatus) {
      this.jobsRequestStatus = jobsRequestStatus;
   }

   public Long getFileInfoId () {
      return fileInfoId;
   }

   public void setFileInfoId (Long fileInfoId) {
      this.fileInfoId = fileInfoId;
   }

   public Long getJobRequestId () {
      return jobRequestId;
   }

   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   /**
    * @return the publishedFileTables
    */
   public List<PublishedFileTableInfo> getPublishedFileTables () {
      return publishedFileTables;
   }

   /**
    * @param publishedFileTables
    *           the publishedFileTables to set
    */
   public void setPublishedFileTables (List<PublishedFileTableInfo> publishedFileTables) {
      this.publishedFileTables = publishedFileTables;
   }

   /**
    * @return the publishedFileId
    */
   public Long getPublishedFileId () {
      return publishedFileId;
   }

   /**
    * @param publishedFileId
    *           the publishedFileId to set
    */
   public void setPublishedFileId (Long publishedFileId) {
      this.publishedFileId = publishedFileId;
   }

   /**
    * @return the columnsUpdateStatus
    */
   public String getColumnsUpdateStatus () {
      return columnsUpdateStatus;
   }

   /**
    * @param columnsUpdateStatus
    *           the columnsUpdateStatus to set
    */
   public void setColumnsUpdateStatus (String columnsUpdateStatus) {
      this.columnsUpdateStatus = columnsUpdateStatus;
   }

   /**
    * @return the publishedFileTableId
    */
   public Long getPublishedFileTableId () {
      return publishedFileTableId;
   }

   /**
    * @param publishedFileTableId
    *           the publishedFileTableId to set
    */
   public void setPublishedFileTableId (Long publishedFileTableId) {
      this.publishedFileTableId = publishedFileTableId;
   }

   public List<String> getUpdateValidationErrorMessages () {
      return updateValidationErrorMessages;
   }

   public void setUpdateValidationErrorMessages (List<String> updateValidationErrorMessages) {
      this.updateValidationErrorMessages = updateValidationErrorMessages;
   }

   public PublishedFileType getSourceType () {
      return sourceType;
   }

   public void setSourceType (PublishedFileType sourceType) {
      this.sourceType = sourceType;
   }

   public String getWizardBased () {
      return wizardBased;
   }

   public void setWizardBased (String wizardBased) {
      this.wizardBased = wizardBased;
   }

   public UIPublishedFileInfo getFileInfo () {
      return fileInfo;
   }

   public void setFileInfo (UIPublishedFileInfo fileInfo) {
      this.fileInfo = fileInfo;
   }

   public void setGranularityTypes (List<GranularityType> granularityTypes) {
      this.granularityTypes = granularityTypes;
   }

   public List<GranularityType> getGranularityTypes () {
      List<GranularityType> granularityTypes = Arrays.asList(GranularityType.values());
      return granularityTypes;

   }

}
