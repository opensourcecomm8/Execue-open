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


package com.execue.platform.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.common.bean.swi.PopularityInfo;
import com.execue.core.common.bean.swi.PopularityRestorationContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.PopularityTermType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.IPopularityDispersionService;
import com.execue.platform.IPopularityJBDCService;
import com.execue.platform.helper.PopularityServiceHelper;
import com.execue.platform.swi.IKDXMaintenanceService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.dataaccess.IPopularityDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IApplicationManagementService;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

public class PopularityDispersionServiceImpl implements IPopularityDispersionService {

   private IPopularityJBDCService        popularityJBDCService;

   private ISDXRetrievalService          sdxRetrievalService;

   private ISDXManagementService         sdxManagementService;

   private IKDXManagementService         kdxManagementService;

   private IKDXRetrievalService          kdxRetrievalService;

   private IApplicationManagementService applicationManagementService;

   private IApplicationRetrievalService  applicationRetrievalService;

   private IJobDataService               jobDataService;

   private IPopularityDataAccessManager  popularityDataAccessManager;

   private ISWIConfigurationService      swiConfigurationService;

   private IKDXMaintenanceService        kdxMaintenanceService;

   private PopularityServiceHelper       popularityServiceHelper;

   public PopularityServiceHelper getPopularityServiceHelper () {
      return popularityServiceHelper;
   }

   public void setPopularityServiceHelper (PopularityServiceHelper popularityServiceHelper) {
      this.popularityServiceHelper = popularityServiceHelper;
   }

   public IPopularityDataAccessManager getPopularityDataAccessManager () {
      return popularityDataAccessManager;
   }

   public void setPopularityDataAccessManager (IPopularityDataAccessManager popularityDataAccessManager) {
      this.popularityDataAccessManager = popularityDataAccessManager;
   }

   public void dispersePopularityInfo (PopularityRestorationContext popularityRestorationContext) throws SWIException {
      String url = getSwiConfigurationService().getPopularityRestorationJobUrl();
      String driverClass = getSwiConfigurationService().getPopularityRestorationJobDriverClassName();
      String username = getSwiConfigurationService().getPopularityRestorationJobUserName();
      String password = getSwiConfigurationService().getPopularityRestorationJobPassword();
      String delimeter = getSwiConfigurationService().getPopularityRestorationJobDelimeter();
      String providerType = getSwiConfigurationService().getPopularityRestorationJobProviderType();
      Connection connection = getPopularityServiceHelper().getConnection(url, driverClass, username, password);

      JobOperationalStatus jobOperationalStatus = null;
      JobRequest jobRequest = popularityRestorationContext.getJobRequest();
      try {
         // read from popularity info using type. it means for each type hit it
         // get the application id by using application name and then update that application with popularity
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Dispersing Popularity Info for Application", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         List<PopularityInfo> applicationPopularityInfo = popularityJBDCService.getPopularityInfo(connection,
                  PopularityTermType.APPLICATION);
         if (ExecueCoreUtil.isCollectionNotEmpty(applicationPopularityInfo)) {
            for (PopularityInfo popularityInfo : applicationPopularityInfo) {
               List<String> tokens = getPopularityServiceHelper().tokenizeFullyQualifiedName(
                        popularityInfo.getFullyQualifiedName(), delimeter);
               String applicationName = tokens.get(0);
               updateApplication(applicationName, popularityInfo.getHits());
            }
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         // as we know the type we have to split and get application name and asset name
         // use get asset entity definition to get "AED" and update the popularity of it
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Dispersing Popularity Info for Asset", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         List<PopularityInfo> assetPopularityInfo = popularityJBDCService.getPopularityInfo(connection,
                  PopularityTermType.ASSET);
         if (ExecueCoreUtil.isCollectionNotEmpty(assetPopularityInfo)) {
            for (PopularityInfo popularityInfo : assetPopularityInfo) {
               List<String> tokens = getPopularityServiceHelper().tokenizeFullyQualifiedName(
                        popularityInfo.getFullyQualifiedName(), delimeter);
               String applicationName = tokens.get(0);
               String assetName = tokens.get(1);
               updateAEDPopularity(applicationName, assetName, null, null, null, popularityInfo.getHits());
            }
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         // as we know the type we have to split and get application name and asset name and table name
         // use get asset entity definition to get "AED" and update the popularity of it
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Dispersing Popularity Info for Table", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         List<PopularityInfo> tablPopularityInfo = popularityJBDCService.getPopularityInfo(connection,
                  PopularityTermType.TABL);
         if (ExecueCoreUtil.isCollectionNotEmpty(tablPopularityInfo)) {
            for (PopularityInfo popularityInfo : tablPopularityInfo) {
               List<String> tokens = getPopularityServiceHelper().tokenizeFullyQualifiedName(
                        popularityInfo.getFullyQualifiedName(), delimeter);
               String applicationName = tokens.get(0);
               String assetName = tokens.get(1);
               String tableName = tokens.get(2);
               updateAEDPopularity(applicationName, assetName, tableName, null, null, popularityInfo.getHits());
            }
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         // as we know the type we have to split and get application name and asset name and table name and column name
         // use get asset entity definition to get "AED" and update the popularity of it
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Dispersing Popularity Info for Column", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         List<PopularityInfo> columPopularityInfo = popularityJBDCService.getPopularityInfo(connection,
                  PopularityTermType.COLUM);
         if (ExecueCoreUtil.isCollectionNotEmpty(columPopularityInfo)) {
            for (PopularityInfo popularityInfo : columPopularityInfo) {
               List<String> tokens = getPopularityServiceHelper().tokenizeFullyQualifiedName(
                        popularityInfo.getFullyQualifiedName(), delimeter);
               String applicationName = tokens.get(0);
               String assetName = tokens.get(1);
               String tableName = tokens.get(2);
               String columnName = tokens.get(3);
               updateAEDPopularity(applicationName, assetName, tableName, columnName, null, popularityInfo.getHits());
            }
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         // as we know the type we have to split and get application name and asset name and table name and column name
         // and member name use get asset entity definition to get "AED" and update the popularity of it
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Dispersing Popularity Info for Member", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         List<PopularityInfo> membrPopularityInfo = popularityJBDCService.getPopularityInfo(connection,
                  PopularityTermType.MEMBR);
         if (ExecueCoreUtil.isCollectionNotEmpty(membrPopularityInfo)) {
            for (PopularityInfo popularityInfo : membrPopularityInfo) {
               List<String> tokens = getPopularityServiceHelper().tokenizeFullyQualifiedName(
                        popularityInfo.getFullyQualifiedName(), delimeter);
               String applicationName = tokens.get(0);
               String assetName = tokens.get(1);
               String tableName = tokens.get(2);
               String columnName = tokens.get(3);
               String memberName = tokens.get(4);
               updateAEDPopularity(applicationName, assetName, tableName, columnName, memberName, popularityInfo
                        .getHits());
            }
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Model Dispersing Popularity Info for Model", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         // get the model id by using model name and then update that model with popularity
         List<PopularityInfo> modelPopularityInfo = popularityJBDCService.getPopularityInfo(connection,
                  PopularityTermType.MODEL);
         if (ExecueCoreUtil.isCollectionNotEmpty(modelPopularityInfo)) {
            for (PopularityInfo popularityInfo : modelPopularityInfo) {
               List<String> tokens = getPopularityServiceHelper().tokenizeFullyQualifiedName(
                        popularityInfo.getFullyQualifiedName(), delimeter);
               String modelName = tokens.get(0);
               updateModel(modelName, popularityInfo.getHits());
            }
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         // split and get the model group name and concept name and get the "BED" for that and update it
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Dispersing Popularity Info for Concept", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         List<PopularityInfo> conceptPopularityInfo = popularityJBDCService.getPopularityInfo(connection,
                  PopularityTermType.CONCEPT);
         if (ExecueCoreUtil.isCollectionNotEmpty(conceptPopularityInfo)) {
            List<BusinessEntityDefinition> conceptBEDS = new ArrayList<BusinessEntityDefinition>();
            for (PopularityInfo popularityInfo : conceptPopularityInfo) {
               List<String> tokens = getPopularityServiceHelper().tokenizeFullyQualifiedName(
                        popularityInfo.getFullyQualifiedName(), delimeter);
               String modelName = tokens.get(0);
               String conceptName = tokens.get(1);
               BusinessEntityDefinition conceptBED = updateBEDPopularity(modelName, conceptName, null, popularityInfo
                        .getHits());
               if (conceptBED != null) {
                  conceptBEDS.add(conceptBED);
               }
            }
            /*
             * if (ExecueCoreUtil.isCollectionNotEmpty(conceptBEDS)) { updateRIOntoTermsForBEDS(conceptBEDS); }
             */

         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Dispersing Popularity Info for Instance", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         // split and get the model group name and concept name and instance name and get the "BED" for that and update
         // it

         List<PopularityInfo> instancePopularityInfo = popularityJBDCService.getPopularityInfo(connection,
                  PopularityTermType.INSTANCE);
         if (ExecueCoreUtil.isCollectionNotEmpty(instancePopularityInfo)) {
            List<BusinessEntityDefinition> instanceBEDS = new ArrayList<BusinessEntityDefinition>();
            for (PopularityInfo popularityInfo : instancePopularityInfo) {
               List<String> tokens = getPopularityServiceHelper().tokenizeFullyQualifiedName(
                        popularityInfo.getFullyQualifiedName(), delimeter);
               String modelName = tokens.get(0);
               String conceptName = tokens.get(1);
               String instanceName = tokens.get(2);
               BusinessEntityDefinition instanceBED = updateBEDPopularity(modelName, conceptName, instanceName,
                        popularityInfo.getHits());
               if (instanceBED != null) {
                  instanceBEDS.add(instanceBED);
               }
            }
            /*
             * if (ExecueCoreUtil.isCollectionNotEmpty(instanceBEDS)) { updateRIOntoTermsForBEDS(instanceBEDS); }
             */
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Dispersing Popularity Info for SFL TERM TOKEN", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         // split the term and token and get the token from sfl term and token table and update the token table
         List<PopularityInfo> sflTermTokenPopularityInfo = popularityJBDCService.getPopularityInfo(connection,
                  PopularityTermType.SFL_TERM_TOKEN);
         if (ExecueCoreUtil.isCollectionNotEmpty(sflTermTokenPopularityInfo)) {
            for (PopularityInfo popularityInfo : sflTermTokenPopularityInfo) {
               List<String> tokens = getPopularityServiceHelper().tokenizeFullyQualifiedName(
                        popularityInfo.getFullyQualifiedName(), delimeter);
               String sflTermName = tokens.get(0);
               String sflTermTokenName = tokens.get(1);
               try {
                  List<SFLTermToken> sfltermTokens = getKdxRetrievalService().getSFLTermTokensByLookupWord(sflTermName);
                  SFLTermToken matchedSFLTermToken = getMatchedSFLTermToken(sfltermTokens, sflTermTokenName);
                  if (matchedSFLTermToken != null) {
                     matchedSFLTermToken.setHits(popularityInfo.getHits());
                     matchedSFLTermToken.setWeight(popularityInfo.getWeight());
                     getKdxManagementService().updateSFLTermToken(matchedSFLTermToken);
                  }
               } catch (KDXException kdxException) {

               }
            }
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         // update the sfl weights based on hits modified
         /*
          * jobOperationalStatus = ExecueBeanCloneUtil.prepareJobOperationalStatus(jobRequest, "SFL Weight Adjustment
          * based on Hits", JobStatus.INPROGRESS, null, new Date());
          * getJobDataService().createJobOperationStatus(jobOperationalStatus); SFLTermTokenWeightContext
          * sflTermTokenWeightContext = new SFLTermTokenWeightContext();
          * sflTermTokenWeightContext.setJobRequest(jobRequest);
          * sflTermTokenWeightContext.setModelId(popularityRestorationContext.getModelId());
          * kdxMaintenanceService.updateSFLTermTokensWeightOnHits(sflTermTokenWeightContext); jobOperationalStatus =
          * ExecueBeanCloneUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.SUCCESS, null, new Date());
          * getJobDataService().updateJobOperationStatus(jobOperationalStatus);
          */
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Deleting Popularity Info table", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         String dropStatement = getPopularityServiceHelper().preparePopularityInfoDropStatement(
                  getPopularityServiceHelper().getAssetProviderType(Integer.parseInt(providerType)));
         getPopularityJBDCService().dropPopularityInfo(connection, dropStatement);
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
      } catch (Exception exception) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                     .getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException queryDataException) {
               throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, queryDataException);
            }
         }
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
   }

   private void updateAEDPopularity (String applicationName, String assetName, String tableName, String columnName,
            String memberName, Long popularityHits) throws SDXException {
      AssetEntityDefinition assetEntityDefinition = getSdxRetrievalService().getAssetEntityDefinitionByNames(
               applicationName, assetName, tableName, columnName, memberName);
      if (assetEntityDefinition != null) {
         assetEntityDefinition.setPopularity(popularityHits);
         getSdxManagementService().updateAssetEntityDefinition(assetEntityDefinition);
      }
   }

   private BusinessEntityDefinition updateBEDPopularity (String modelName, String conceptName, String instanceName,
            Long popularityHits) throws KDXException {
      BusinessEntityDefinition businessEntityDefinition = getKdxRetrievalService().getBusinessEntityDefinitionByNames(
               modelName, conceptName, instanceName);
      if (businessEntityDefinition != null) {
         businessEntityDefinition.setPopularity(popularityHits);
         getKdxManagementService().updateBusinessEntityDefinition(businessEntityDefinition);
      }
      return businessEntityDefinition;
   }

   /*
    * private void updateRIOntoTermsForBEDS (List<BusinessEntityDefinition> businessEntityDefinitions) throws
    * KDXException { try { getPopularityDataAccessManager().updateRIOntoTermsPopularity(businessEntityDefinitions); }
    * catch (SWIException swiException) { throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE,
    * swiException); } }
    */
   private void updateApplication (String applicationName, Long hits) throws KDXException {
      Application application = getApplicationRetrievalService().getApplicationByName(applicationName);
      if (application != null) {
         application.setPopularity(hits);
         getApplicationManagementService().updateApplication(application);
      }
   }

   private void updateModel (String modelName, Long hits) throws KDXException {
      Model model = getKdxRetrievalService().getModelByName(modelName);
      if (model != null) {
         model.setPopularity(hits);
         getKdxManagementService().updateModel(model);
      }
   }

   private SFLTermToken getMatchedSFLTermToken (List<SFLTermToken> sflTermTokens, String sflTermTokenName) {
      SFLTermToken matchedSFLTermToken = null;
      for (SFLTermToken sflTermToken : sflTermTokens) {
         if (sflTermToken.getBusinessTermToken().equalsIgnoreCase(sflTermTokenName)) {
            matchedSFLTermToken = sflTermToken;
            break;
         }
      }
      return matchedSFLTermToken;
   }

   /**
    * @return the popularityJBDCService
    */
   public IPopularityJBDCService getPopularityJBDCService () {
      return popularityJBDCService;
   }

   /**
    * @param popularityJBDCService
    *           the popularityJBDCService to set
    */
   public void setPopularityJBDCService (IPopularityJBDCService popularityJBDCService) {
      this.popularityJBDCService = popularityJBDCService;
   }

   /**
    * @return the kdxManagementService
    */
   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   /**
    * @param kdxManagementService
    *           the kdxManagementService to set
    */
   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
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

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public IKDXMaintenanceService getKdxMaintenanceService () {
      return kdxMaintenanceService;
   }

   public void setKdxMaintenanceService (IKDXMaintenanceService kdxMaintenanceService) {
      this.kdxMaintenanceService = kdxMaintenanceService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

   public IApplicationManagementService getApplicationManagementService () {
      return applicationManagementService;
   }

   public void setApplicationManagementService (IApplicationManagementService applicationManagementService) {
      this.applicationManagementService = applicationManagementService;
   }

}
