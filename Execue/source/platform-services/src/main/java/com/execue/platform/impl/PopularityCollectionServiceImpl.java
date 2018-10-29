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

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.common.bean.swi.PopularityAssetEntityDefinitionInfo;
import com.execue.core.common.bean.swi.PopularityBusinessEntityDefinitionInfo;
import com.execue.core.common.bean.swi.PopularityInfo;
import com.execue.core.common.bean.swi.PopularityRestorationContext;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.PopularityTermType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.platform.IPopularityCollectionService;
import com.execue.platform.IPopularityJBDCService;
import com.execue.platform.helper.PopularityServiceHelper;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class PopularityCollectionServiceImpl implements IPopularityCollectionService {

   private IApplicationRetrievalService applicationRetrievalService;
   private ISDXRetrievalService         sdxRetrievalService;
   private IKDXRetrievalService         kdxRetrievalService;
   private IPopularityJBDCService       popularityJBDCService;
   private IJobDataService              jobDataService;
   private ISWIConfigurationService     swiConfigurationService;
   private PopularityServiceHelper      popularityServiceHelper;
   private static final Logger          logger = Logger.getLogger(PopularityCollectionServiceImpl.class);

   public void collectPopularityInfo (PopularityRestorationContext popularityRestorationContext) throws SWIException {
      JobOperationalStatus jobOperationalStatus = null;
      JobRequest jobRequest = popularityRestorationContext.getJobRequest();
      try {
         String url = getSwiConfigurationService().getPopularityRestorationJobUrl();
         String driverClass = getSwiConfigurationService().getPopularityRestorationJobDriverClassName();
         String username = getSwiConfigurationService().getPopularityRestorationJobUserName();
         String password = getSwiConfigurationService().getPopularityRestorationJobPassword();
         String delimeter = getSwiConfigurationService().getPopularityRestorationJobDelimeter();
         String providerType = getSwiConfigurationService().getPopularityRestorationJobProviderType();
         Connection connection = getPopularityServiceHelper().getConnection(url, driverClass, username, password);
         List<PopularityInfo> popularityInfoList = new ArrayList<PopularityInfo>();
         PopularityInfo popularityInfo = null;

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Creating Popularity_Info table  ", JobStatus.INPROGRESS, null, new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);
         AssetProviderType assetProviderType = getPopularityServiceHelper().getAssetProviderType(
                  Integer.parseInt(providerType));
         String createTableStatement = getPopularityServiceHelper().preparePopularityInfoCreateStatement(
                  assetProviderType);
         if (logger.isDebugEnabled()) {
            logger.debug("create table statement ::" + createTableStatement);
         }
         getPopularityJBDCService().createPopularityInfo(connection, createTableStatement);
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Collecting Popularity Info for Application", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         Application application = getApplicationRetrievalService().getApplicationById(
                  popularityRestorationContext.getApplicationId());
         // popularity info updation for application
         if (application.getPopularity() > 0) {
            popularityInfo = new PopularityInfo();
            popularityInfo.setFullyQualifiedName(application.getName());
            popularityInfo.setHits(application.getPopularity());
            popularityInfo.setPopularityTermType(PopularityTermType.APPLICATION);
            popularityInfoList.add(popularityInfo);
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Collecting Popularity Info for AssetEntities ", JobStatus.INPROGRESS, null, new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);
         // popularity info updation for assetentities
         List<Asset> assets = getSdxRetrievalService().getAllAssets(popularityRestorationContext.getApplicationId());
         for (Asset asset : assets) {
            List<PopularityAssetEntityDefinitionInfo> popularityAssetEntityList = getSdxRetrievalService()
                     .getPopularityAssetEntityInfoForAssetId(asset.getId());
            for (PopularityAssetEntityDefinitionInfo popularityAssetEntityDefinitionInfo : popularityAssetEntityList) {
               String fullyQualifiedName = null;
               switch (popularityAssetEntityDefinitionInfo.getAssetEntityType()) {
                  case ASSET:
                     fullyQualifiedName = application.getName() + delimeter
                              + popularityAssetEntityDefinitionInfo.getAssetName();
                     break;
                  case TABLE:
                     fullyQualifiedName = application.getName() + delimeter
                              + popularityAssetEntityDefinitionInfo.getAssetName() + delimeter
                              + popularityAssetEntityDefinitionInfo.getTableName();
                     break;
                  case COLUMN:
                     fullyQualifiedName = application.getName() + delimeter
                              + popularityAssetEntityDefinitionInfo.getAssetName() + delimeter
                              + popularityAssetEntityDefinitionInfo.getTableName() + delimeter
                              + popularityAssetEntityDefinitionInfo.getColumnName();
                     break;
                  case MEMBER:
                     fullyQualifiedName = application.getName() + delimeter
                              + popularityAssetEntityDefinitionInfo.getAssetName() + delimeter
                              + popularityAssetEntityDefinitionInfo.getTableName() + delimeter
                              + popularityAssetEntityDefinitionInfo.getColumnName() + delimeter
                              + popularityAssetEntityDefinitionInfo.getMembrName();
               }
               if (fullyQualifiedName != null) {
                  popularityInfo = new PopularityInfo();
                  popularityInfo.setFullyQualifiedName(fullyQualifiedName);
                  popularityInfo.setHits(popularityAssetEntityDefinitionInfo.getPopularity());
                  popularityInfo.setPopularityTermType(getPopularityServiceHelper().getAssetPopularityTermType(
                           popularityAssetEntityDefinitionInfo.getAssetEntityType()));
                  popularityInfoList.add(popularityInfo);
               }
            }
            logger
                     .debug("PopularityInfoList size :: " + popularityInfoList.size() + " Asset Name ::"
                              + asset.getName());
         }

         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         List<Model> models = kdxRetrievalService.getModelsByApplicationId(popularityRestorationContext
                  .getApplicationId());
         // TODO : -VG- only one model is there for one application
         Model model = models.get(0);
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Collecting Popularity Info for Model ", JobStatus.INPROGRESS, null, new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);

         // popularity info updation for model
         if (model.getPopularity() > 0) {
            popularityInfo = new PopularityInfo();
            popularityInfo.setFullyQualifiedName(model.getName());
            popularityInfo.setHits(model.getPopularity());
            popularityInfo.setPopularityTermType(PopularityTermType.MODEL);
            popularityInfoList.add(popularityInfo);
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Collecting Popularity Info for BusinessEntities ", JobStatus.INPROGRESS, null, new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);

         // popularity info updation for business entities

         List<ModelGroup> modelGroups = kdxRetrievalService.getUserModelGroupsByModelId(model.getId());
         for (ModelGroup modelGroup : modelGroups) {
            List<PopularityBusinessEntityDefinitionInfo> popularityBEDList = getKdxRetrievalService()
                     .getPopularityInfoForModelGroup(modelGroup.getId());
            for (PopularityBusinessEntityDefinitionInfo popularityBusinessEntityDefinitionInfo : popularityBEDList) {
               String fullyQualifiedName = null;
               switch (popularityBusinessEntityDefinitionInfo.getBusinessEntityType()) {
                  case CONCEPT:
                     fullyQualifiedName = model.getName() + delimeter
                              + popularityBusinessEntityDefinitionInfo.getConceptName();
                     break;
                  case CONCEPT_LOOKUP_INSTANCE:
                     fullyQualifiedName = model.getName() + delimeter
                              + popularityBusinessEntityDefinitionInfo.getConceptName() + delimeter
                              + popularityBusinessEntityDefinitionInfo.getInstanceName();
               }
               if (fullyQualifiedName != null) {
                  popularityInfo = new PopularityInfo();
                  popularityInfo.setFullyQualifiedName(fullyQualifiedName);
                  popularityInfo.setHits(popularityBusinessEntityDefinitionInfo.getPopularity());
                  popularityInfo.setPopularityTermType(getPopularityServiceHelper().getBusinessPopularityTermType(
                           popularityBusinessEntityDefinitionInfo.getBusinessEntityType()));
                  popularityInfoList.add(popularityInfo);
               }
            }
            logger.debug("PopularityInfoList size :: " + popularityInfoList.size() + " modelGroup Name ::"
                     + modelGroup.getName());
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Collecting Popularity Info for SFLTermToken ", JobStatus.INPROGRESS, null, new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);

         // popularity info updation for sflterm token

         List<Long> sflTermIds = getKdxRetrievalService().getSFLTermIdsTobeDeleted();
         logger.debug("Total sflTermIds size ::" + sflTermIds.size());
         for (Long sflTermId : sflTermIds) {
            SFLTerm sflTerm = getKdxRetrievalService().getSFLTermById(sflTermId);
            List<SFLTermToken> sflTermTokens = getKdxRetrievalService().getSFLTermTokensByLookupWord(
                     sflTerm.getBusinessTerm());
            logger.debug("sflTermTokenIds size ::" + sflTermTokens.size() + " for SFLTermId ::"
                     + sflTerm.getBusinessTerm());
            for (SFLTermToken sflTermToken : sflTermTokens) {
               popularityInfo = new PopularityInfo();
               String fullyQualifiedName = sflTerm.getBusinessTerm() + delimeter + sflTermToken.getBusinessTermToken();
               popularityInfo.setFullyQualifiedName(fullyQualifiedName);
               popularityInfo.setHits(sflTermToken.getHits());
               popularityInfo.setWeight(sflTermToken.getWeight());
               popularityInfo.setPopularityTermType(PopularityTermType.SFL_TERM_TOKEN);
               popularityInfoList.add(popularityInfo);
            }

         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Updating the Collected Popularities to Popularity_Info table ", JobStatus.INPROGRESS, null,
                  new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);

         List<Integer> sqlTypes = getPopularityServiceHelper().populateSQLTypesForPopularityInfo();
         List<List<Object>> popularityObjectInfo = getPopularityServiceHelper().getPopularityObjectList(
                  popularityInfoList);
         getPopularityJBDCService().insertIntoPopularityInfo(connection, popularityObjectInfo, sqlTypes);
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
         exception.printStackTrace();
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IPopularityJBDCService getPopularityJBDCService () {
      return popularityJBDCService;
   }

   public void setPopularityJBDCService (IPopularityJBDCService popularityJBDCService) {
      this.popularityJBDCService = popularityJBDCService;
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

   public PopularityServiceHelper getPopularityServiceHelper () {
      return popularityServiceHelper;
   }

   public void setPopularityServiceHelper (PopularityServiceHelper popularityServiceHelper) {
      this.popularityServiceHelper = popularityServiceHelper;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

}
