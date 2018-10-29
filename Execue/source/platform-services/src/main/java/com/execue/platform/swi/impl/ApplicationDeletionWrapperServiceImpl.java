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


package com.execue.platform.swi.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.ApplicationDetail;
import com.execue.core.common.bean.entity.ApplicationExample;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.ModelGroupMapping;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.VerticalAppExample;
import com.execue.core.common.bean.entity.VerticalAppWeight;
import com.execue.core.common.bean.entity.VerticalEntityBasedRedirection;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.swi.ApplicationDeletionContext;
import com.execue.core.common.bean.swi.ApplicationScopeIndexDetail;
import com.execue.core.common.bean.swi.AssetDeletionContext;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.swi.IApplicationDeletionWrapperService;
import com.execue.platform.swi.IAssetDeletionWrapperService;
import com.execue.platform.swi.IRICloudsAbsorptionWrapperService;
import com.execue.platform.swi.ISWIPlatformDeletionService;
import com.execue.platform.unstructured.IUnstructuredApplicationConnectionManagementService;
import com.execue.platform.unstructured.IUnstructuredWarehouseManagementWrapperService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PreferencesException;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IApplicationDeletionService;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IEASIndexService;
import com.execue.swi.service.IKDXCloudDeletionService;
import com.execue.swi.service.IKDXCloudManagementService;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXDeletionService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathDefinitionDeletionService;
import com.execue.swi.service.IPathDefinitionRetrievalService;
import com.execue.swi.service.IPreferencesDeletionService;
import com.execue.swi.service.IPreferencesManagementService;
import com.execue.swi.service.IPreferencesRetrievalService;
import com.execue.swi.service.IPublishedFileRetrievalService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.swi.service.IUserManagementService;

public class ApplicationDeletionWrapperServiceImpl implements IApplicationDeletionWrapperService {

   private IApplicationRetrievalService                        applicationRetrievalService;
   private IJobDataService                                     jobDataService;
   private IUserManagementService                              userManagementService;
   private IKDXRetrievalService                                kdxRetrievalService;
   private IKDXDeletionService                                 kdxDeletionService;
   private IKDXCloudDeletionService                            kdxCloudDeletionService;
   private IKDXCloudManagementService                          kdxCloudManagementService;
   private IKDXCloudRetrievalService                           kdxCloudRetrievalService;
   private IBusinessEntityMaintenanceService                   businessEntityMaintenanceService;
   private IPathDefinitionDeletionService                      pathDefinitionDeletionService;
   private IPathDefinitionRetrievalService                     pathDefinitionRetrievalService;
   private IPreferencesRetrievalService                        preferencesRetrievalService;
   private IPreferencesManagementService                       preferencesManagementService;
   private IPreferencesDeletionService                         preferencesDeletionService;
   private IUnstructuredWarehouseManagementWrapperService      unstructuredWarehouseManagementWrapperService;
   private ISDXRetrievalService                                sdxRetrievalService;
   private ISDXDeletionService                                 sdxDeletionService;
   private IEASIndexService                                    easIndexService;
   private IApplicationDeletionService                         applicationDeletionService;
   private IPublishedFileRetrievalService                      publishedFileRetrievalService;
   private IBaseKDXRetrievalService                            baseKDXRetrievalService;
   private IRICloudsAbsorptionWrapperService                   riCloudsAbsorptionWrapperService;
   private IAssetDeletionWrapperService                        assetDeletionWrapperService;
   private ISWIPlatformDeletionService                         swiPlatformDeletionService;
   private IUnstructuredApplicationConnectionManagementService unstructuredApplicationConnectionManagementService;

   private static final Logger                                 logger = Logger
                                                                               .getLogger(ApplicationDeletionWrapperServiceImpl.class);

   public void deleteApplication (ApplicationDeletionContext applicationDeletionContext) throws KDXException {
      JobOperationalStatus jobOperationalStatus = null;
      try {
         deleteApplicationHierarchy(applicationDeletionContext, jobOperationalStatus);
         List<Application> applications = getApplicationRetrievalService().getApplications(
                  applicationDeletionContext.getUserId());
         if (ExecueCoreUtil.isCollectionEmpty(applications)) {
            updateUserForPublisher(applicationDeletionContext.getUserId());
         }
      } catch (Exception exception) {
         ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                  .getMessage(), new Date());
         try {
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         } catch (QueryDataException queryDataException) {
            throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, queryDataException);
         }
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }

   }

   private void updateUserForPublisher (Long userId) throws SDXException {
      try {
         User user = userManagementService.getUserById(userId);
         if (user != null) {
            user.setIsPublisher(CheckType.NO);
            userManagementService.updateUser(user);
         }
      } catch (SWIException e) {
         throw new SDXException(SWIExceptionCodes.ENTITY_UPDATE_FAILED, e);
      }

   }

   private void deleteApplicationHierarchy (ApplicationDeletionContext applicationDeletionContext,
            JobOperationalStatus jobOperationalStatus2) throws KDXException {
      JobOperationalStatus jobOperationalStatus = null;
      JobRequest jobRequest = applicationDeletionContext.getJobRequest();
      Long applicationId = applicationDeletionContext.getApplicationId();
      try {
         Application application = getApplicationRetrievalService().getApplicationById(applicationId);
         // cleaning SDX part
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest, "SDX Cleanup ",
                  JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         // delete only parent assets as internally that will delete child assets if any
         List<Asset> assets = getSdxRetrievalService().getAllParentAssets(applicationId);
         if (logger.isDebugEnabled()) {
            logger.debug("Total Number Of Assets ::" + assets.size());
         }
         for (Asset asset : assets) {
            AssetDeletionContext assetDeletionContext = new AssetDeletionContext();
            assetDeletionContext.setAssetId(asset.getId());
            assetDeletionContext.setUserId(applicationDeletionContext.getUserId());
            assetDeletionContext.setJobRequest(applicationDeletionContext.getJobRequest());
            getAssetDeletionWrapperService().deleteAsset(assetDeletionContext);
         }
         if (logger.isDebugEnabled()) {
            logger.debug("After Deletion Of Assets");
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         List<ModelGroup> userModelGroups = new ArrayList<ModelGroup>();
         List<Model> models = getKdxRetrievalService().getModelsByApplicationId(applicationId);
         // TODO : -VG- only one model is there for one application
         Model model = models.get(0);
         // TODO : -NK- only one cloud is there for one model
         Cloud appCloud = getKdxCloudRetrievalService().getDefaultAppCloud(model.getId());
         userModelGroups.addAll(getKdxRetrievalService().getUserModelGroupsByModelId(model.getId()));
         ModelGroup baseGroup = baseKDXRetrievalService.getBaseGroup();
         if (ExecueCoreUtil.isCollectionNotEmpty(userModelGroups)) {
            // cleaning maintenance tables
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning Cloud Components", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);
            deleteCloudDetails(appCloud);
            if (logger.isDebugEnabled()) {
               logger.debug("After Cleaning Up Cloud Components");
            }
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);

            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning PathDefinition Heirarchy", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);
            deletePathDefinitionHierarchy(appCloud);
            deleteEntityTripleDefinitions(userModelGroups, baseGroup);
            if (logger.isDebugEnabled()) {
               logger.debug("After Cleaning Up PathDefinitionHierarchy");
            }
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);

            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning Keyword Hierarchy", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);
            deleteKeywordHierarchy(model);
            if (logger.isDebugEnabled()) {
               logger.debug("After Cleaning Up keyword Hierarchy");
            }
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);

            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning Reverse Indexes", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);
            getKdxDeletionService().deleteRIOntoTerms(userModelGroups);
            getRiCloudsAbsorptionWrapperService().truncateRICloudByCloudId(appCloud.getId());
            if (logger.isDebugEnabled()) {
               logger.debug("After Cleaning Up Reverse Indexes");
            }
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);

            // List<Long> sflTermIds = kdxRetrievalService.getSFLTermIdsTobeDeleted();
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning SFLTerms", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);
            deleteSFLTerms(userModelGroups);
            if (logger.isDebugEnabled()) {
               logger.debug("After Cleaning Up SFLTerms");
            }
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning remaining miscellaneous Tables", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);
            deleteMiscellaneousTables(userModelGroups, model.getId());
            if (logger.isDebugEnabled()) {
               logger.debug("After Cleaning Up Miscellaneous Tables");
            }
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);

            // cleaning KDX

            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning KDX Hierarchy", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);
            deleteProfiles(userModelGroups);
            if (logger.isDebugEnabled()) {
               logger.debug("After Cleaning Up Profiles");
            }
            List<Concept> concepts = kdxRetrievalService.getConcepts(userModelGroups);
            if (ExecueCoreUtil.isCollectionNotEmpty(concepts)) {
               getKdxDeletionService().deleteConcepts(concepts);
            }
            List<Relation> relations = kdxRetrievalService.getRelations(userModelGroups);
            if (ExecueCoreUtil.isCollectionNotEmpty(relations)) {
               getKdxDeletionService().deleteRelations(relations);
            }
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         }
         // cleaning application, model and mapping
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Cleaning application,model & their mapping", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         deleteApplicationModelMapping(applicationId);
         if (logger.isDebugEnabled()) {
            logger.debug("After Cleaning Up ApplicationModelMapping");
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         // if app is of unstructured nature, then delete the mapping from swi and remove from pool.
         if (AppSourceType.UNSTRUCTURED.equals(application.getSourceType())) {
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning Warehouse Hierarchy", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);
            getUnstructuredWarehouseManagementWrapperService().deleteApplicationInfo(applicationId);
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            getSdxDeletionService().deleteAppDataSourceMappings(applicationId);
            //Clean data source pool
            getUnstructuredApplicationConnectionManagementService().deletePool(applicationId);
         }
      } catch (Exception exception) {
         exception.printStackTrace();
         ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                  .getMessage(), new Date());
         try {
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         } catch (QueryDataException queryDataException) {
            throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, queryDataException);
         }
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
   }

   private void deleteProfiles (List<ModelGroup> userModelGroups) throws PreferencesException {
      List<Profile> profiles = new ArrayList<Profile>();
      List<Profile> conceptProfiles = getPreferencesRetrievalService().getConceptProfiles(userModelGroups);
      if (ExecueCoreUtil.isCollectionNotEmpty(conceptProfiles)) {
         profiles.addAll(conceptProfiles);
      }
      List<Profile> instanceProfiles = getPreferencesRetrievalService().getInstanceProfiles(userModelGroups);
      if (ExecueCoreUtil.isCollectionEmpty(instanceProfiles)) {
         profiles.addAll(instanceProfiles);
      }
      for (Profile profile : profiles) {
         getPreferencesManagementService().cleanProfile(profile);
      }
   }

   private void deleteCloudDetails (Cloud cloud) throws SWIException {
      cloud = getKdxCloudRetrievalService().getCloudByName(cloud.getName());
      if (ExecueCoreUtil.isCollectionNotEmpty(cloud.getCloudAllowedBehavior())) {
         cloud.setCloudAllowedBehavior(null);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(cloud.getCloudAllowedComponents())) {
         cloud.setCloudAllowedComponents(null);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(cloud.getCloudValidationRules())) {
         cloud.setCloudValidationRules(null);
      }
      getKdxCloudManagementService().updateCloud(cloud);
      getKdxCloudDeletionService().deleteCloudComponentsByCloudId(cloud.getId());
   }

   /**
    * This method deletes all the path definitions hierarchy for the given cloud
    * 
    * @param cloud
    * @throws SWIException
    */
   private void deletePathDefinitionHierarchy (Cloud cloud) throws SWIException {
      getPathDefinitionDeletionService().deleteAllPathDefinitionsForCloud(cloud.getId());
   }

   private void deleteKeywordHierarchy (Model model) throws PreferencesException {
      List<KeyWord> keyWords = getPreferencesRetrievalService().getAllKeyWords(model.getId());
      for (KeyWord keyWord : keyWords) {
         getPreferencesDeletionService().deleteKeywordHeirarchy(keyWord);
      }
   }

   private void deleteSFLTerms (List<ModelGroup> userModelGroups) throws KDXException {
      for (ModelGroup mg : userModelGroups) {
         List<Long> termIdsByContextId = getKdxRetrievalService().getSFLTermIdsByContextId(mg.getId());
         if (!CollectionUtils.isEmpty(termIdsByContextId)) {
            getKdxDeletionService().deleteSFLTerms(termIdsByContextId);
         }
      }
   }

   private void deleteMiscellaneousTables (List<ModelGroup> userModelGroups, Long modelId) throws KDXException {
      getKdxDeletionService().deleteDefaultDynamicValues(userModelGroups);
      getBusinessEntityMaintenanceService().deleteBusinessEntityMaintenanceDetails(modelId);
   }

   private void deleteApplicationModelMapping (Long applicationId) throws PublishedFileException, SWIException {
      Application application = getApplicationRetrievalService().getApplicationById(applicationId);
      List<Model> models = kdxRetrievalService.getModelsByApplicationId(applicationId);
      for (Model model : models) {
         List<ModelGroupMapping> modelGroupMappings = kdxRetrievalService.getPopulatedModelGroupMapping(model.getId());
         List<Cloud> clouds = getKdxCloudRetrievalService().getAllCloudsByModelId(model.getId());
         List<ModelGroup> userModelGroups = kdxRetrievalService.getUserModelGroupsByModelId(model.getId());
         getKdxDeletionService().deleteModelGroupMappings(modelGroupMappings);
         getKdxDeletionService().deleteModelGroups(userModelGroups);
         getKdxCloudDeletionService().deleteClouds(clouds);
      }
      application.setModels(null);
      ApplicationDetail applicationDetail = applicationRetrievalService.getImageByApplicationId(application.getId());
      if (applicationDetail != null) {
         getApplicationDeletionService().deleteApplicationImage(applicationDetail);
      }
      VerticalAppWeight verticalAppWeight = applicationRetrievalService.getVerticalAppWeightByApplicationId(application
               .getId());
      if (verticalAppWeight != null) {
         getKdxDeletionService().deleteVerticalAppWeight(verticalAppWeight);
      }
      List<VerticalEntityBasedRedirection> verticalEntityBasedRedirectionList = applicationRetrievalService
               .getVerticalRedirectionEntitiesByApplicationId(application.getId());
      if (verticalEntityBasedRedirectionList != null) {
         getKdxDeletionService().deleteVerticalRedirectionEntities(verticalEntityBasedRedirectionList);
      }
      List<VerticalAppExample> verticalAppExamples = applicationRetrievalService
               .getVerticalAppExamplesByApplicationId(application.getId());
      if (verticalAppExamples != null) {
         getKdxDeletionService().deleteVerticalAppExamples(verticalAppExamples);
      }

      /*
       * List<ApplicationOperation> applicationOperations = kdxRetrievalService
       * .getApplicationOperationsByApplicationId(application.getId()); if (applicationOperations != null) {
       * kdxManagementService.deleteApplicationOperations(applicationOperations); }
       */

      List<ApplicationExample> applicationExamples = applicationRetrievalService
               .getAllAppExampleForApplication(application.getId());
      if (applicationExamples != null) {
         getApplicationDeletionService().deleteApplicationExamples(applicationExamples);
      }

      ApplicationScopeIndexDetail applicationScopeIndexDetail = getEasIndexService()
               .getApplicationScopeIndexDetailByAppId(applicationId);
      if (applicationScopeIndexDetail != null) {
         getEasIndexService().deleteApplicationScopeIndexDetail(applicationScopeIndexDetail);
      }
      List<PublishedFileInfo> publishedFileInfoList = getPublishedFileRetrievalService()
               .getPublishedFileInfoByApplicationId(application.getId());
      for (PublishedFileInfo publishedFileInfo : publishedFileInfoList) {
         getSwiPlatformDeletionService().deleteUploadedDataset(publishedFileInfo);
      }
      getApplicationDeletionService().deleteApplication(application);
      getKdxDeletionService().deleteModels(models);
   }

   private void deleteEntityTripleDefinitions (List<ModelGroup> userModelGroups, ModelGroup baseModelGroup)
            throws SWIException {
      List<Long> allEntityTripleDefinitions = new ArrayList<Long>();
      List<Long> nonBaseEntityTripleDefinitions = getPathDefinitionRetrievalService()
               .getNonBaseEntityTripleDefinitions(userModelGroups);
      if (ExecueCoreUtil.isCollectionNotEmpty(nonBaseEntityTripleDefinitions)) {
         allEntityTripleDefinitions.addAll(nonBaseEntityTripleDefinitions);
      }
      List<Long> baseToNonBaseEntityTripleDefinitions = getPathDefinitionRetrievalService()
               .getBaseToNonBaseEntityTripleDefinitions(baseModelGroup, userModelGroups);
      if (ExecueCoreUtil.isCollectionNotEmpty(baseToNonBaseEntityTripleDefinitions)) {
         allEntityTripleDefinitions.addAll(baseToNonBaseEntityTripleDefinitions);
      }
      List<Long> nonBaseToBaseEntityTripeDefinitions = getPathDefinitionRetrievalService()
               .getNonBaseToBaseEntityTripleDefinitions(userModelGroups, baseModelGroup);
      if (ExecueCoreUtil.isCollectionNotEmpty(nonBaseToBaseEntityTripeDefinitions)) {
         allEntityTripleDefinitions.addAll(nonBaseToBaseEntityTripeDefinitions);
      }

      if (ExecueCoreUtil.isCollectionNotEmpty(allEntityTripleDefinitions)) {
         getPathDefinitionDeletionService().deleteEntityTripleDefinitions(allEntityTripleDefinitions);
      }
   }

   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IUserManagementService getUserManagementService () {
      return userManagementService;
   }

   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IKDXDeletionService getKdxDeletionService () {
      return kdxDeletionService;
   }

   public void setKdxDeletionService (IKDXDeletionService kdxDeletionService) {
      this.kdxDeletionService = kdxDeletionService;
   }

   public IKDXCloudDeletionService getKdxCloudDeletionService () {
      return kdxCloudDeletionService;
   }

   public void setKdxCloudDeletionService (IKDXCloudDeletionService kdxCloudDeletionService) {
      this.kdxCloudDeletionService = kdxCloudDeletionService;
   }

   public IBusinessEntityMaintenanceService getBusinessEntityMaintenanceService () {
      return businessEntityMaintenanceService;
   }

   public void setBusinessEntityMaintenanceService (IBusinessEntityMaintenanceService businessEntityMaintenanceService) {
      this.businessEntityMaintenanceService = businessEntityMaintenanceService;
   }

   public IPathDefinitionDeletionService getPathDefinitionDeletionService () {
      return pathDefinitionDeletionService;
   }

   public void setPathDefinitionDeletionService (IPathDefinitionDeletionService pathDefinitionDeletionService) {
      this.pathDefinitionDeletionService = pathDefinitionDeletionService;
   }

   public IPathDefinitionRetrievalService getPathDefinitionRetrievalService () {
      return pathDefinitionRetrievalService;
   }

   public void setPathDefinitionRetrievalService (IPathDefinitionRetrievalService pathDefinitionRetrievalService) {
      this.pathDefinitionRetrievalService = pathDefinitionRetrievalService;
   }

   public IKDXCloudManagementService getKdxCloudManagementService () {
      return kdxCloudManagementService;
   }

   public void setKdxCloudManagementService (IKDXCloudManagementService kdxCloudManagementService) {
      this.kdxCloudManagementService = kdxCloudManagementService;
   }

   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return preferencesRetrievalService;
   }

   public void setPreferencesRetrievalService (IPreferencesRetrievalService preferencesRetrievalService) {
      this.preferencesRetrievalService = preferencesRetrievalService;
   }

   public IPreferencesManagementService getPreferencesManagementService () {
      return preferencesManagementService;
   }

   public void setPreferencesManagementService (IPreferencesManagementService preferencesManagementService) {
      this.preferencesManagementService = preferencesManagementService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public ISDXDeletionService getSdxDeletionService () {
      return sdxDeletionService;
   }

   public void setSdxDeletionService (ISDXDeletionService sdxDeletionService) {
      this.sdxDeletionService = sdxDeletionService;
   }

   public IEASIndexService getEasIndexService () {
      return easIndexService;
   }

   public void setEasIndexService (IEASIndexService easIndexService) {
      this.easIndexService = easIndexService;
   }

   public IApplicationDeletionService getApplicationDeletionService () {
      return applicationDeletionService;
   }

   public void setApplicationDeletionService (IApplicationDeletionService applicationDeletionService) {
      this.applicationDeletionService = applicationDeletionService;
   }

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   public IPreferencesDeletionService getPreferencesDeletionService () {
      return preferencesDeletionService;
   }

   public void setPreferencesDeletionService (IPreferencesDeletionService preferencesDeletionService) {
      this.preferencesDeletionService = preferencesDeletionService;
   }

   public IAssetDeletionWrapperService getAssetDeletionWrapperService () {
      return assetDeletionWrapperService;
   }

   public void setAssetDeletionWrapperService (IAssetDeletionWrapperService assetDeletionWrapperService) {
      this.assetDeletionWrapperService = assetDeletionWrapperService;
   }

   public ISWIPlatformDeletionService getSwiPlatformDeletionService () {
      return swiPlatformDeletionService;
   }

   public void setSwiPlatformDeletionService (ISWIPlatformDeletionService swiPlatformDeletionService) {
      this.swiPlatformDeletionService = swiPlatformDeletionService;
   }

   public IRICloudsAbsorptionWrapperService getRiCloudsAbsorptionWrapperService () {
      return riCloudsAbsorptionWrapperService;
   }

   public void setRiCloudsAbsorptionWrapperService (IRICloudsAbsorptionWrapperService riCloudsAbsorptionWrapperService) {
      this.riCloudsAbsorptionWrapperService = riCloudsAbsorptionWrapperService;
   }

   /**
    * @return the unstructuredWarehouseManagementWrapperService
    */
   public IUnstructuredWarehouseManagementWrapperService getUnstructuredWarehouseManagementWrapperService () {
      return unstructuredWarehouseManagementWrapperService;
   }

   /**
    * @param unstructuredWarehouseManagementWrapperService
    *           the unstructuredWarehouseManagementWrapperService to set
    */
   public void setUnstructuredWarehouseManagementWrapperService (
            IUnstructuredWarehouseManagementWrapperService unstructuredWarehouseManagementWrapperService) {
      this.unstructuredWarehouseManagementWrapperService = unstructuredWarehouseManagementWrapperService;
   }

   /**
    * @return the unstructuredApplicationConnectionManagementService
    */
   public IUnstructuredApplicationConnectionManagementService getUnstructuredApplicationConnectionManagementService () {
      return unstructuredApplicationConnectionManagementService;
   }

   /**
    * @param unstructuredApplicationConnectionManagementService the unstructuredApplicationConnectionManagementService to set
    */
   public void setUnstructuredApplicationConnectionManagementService (
            IUnstructuredApplicationConnectionManagementService unstructuredApplicationConnectionManagementService) {
      this.unstructuredApplicationConnectionManagementService = unstructuredApplicationConnectionManagementService;
   }

}
