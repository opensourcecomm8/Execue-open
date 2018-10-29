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


package com.execue.swi.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.ApplicationDetail;
import com.execue.core.common.bean.entity.ApplicationExample;
import com.execue.core.common.bean.entity.ApplicationOperation;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.UnstructuredApplicationDetail;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.common.bean.entity.VerticalAppExample;
import com.execue.core.common.bean.entity.VerticalAppWeight;
import com.execue.core.common.bean.entity.VerticalEntityBasedRedirection;
import com.execue.core.common.bean.entity.comparator.ApplicationExampleQueryNameComparator;
import com.execue.core.common.bean.entity.wrapper.AppDashBoardInfo;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.FacetNatureType;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IApplicationRetrievalService;

public class ApplicationRetrievalServiceImpl implements IApplicationRetrievalService {

   private IKDXDataAccessManager kdxDataAccessManager;

   public Application getApplicationById (Long applicationId) throws KDXException {
      return getKdxDataAccessManager().getApplicationById(applicationId);
   }

   public List<Application> getApplications (Long userId) throws KDXException {
      return getKdxDataAccessManager().getApplications(userId);
   }

   public List<Application> getAllApplications () throws KDXException {
      return getKdxDataAccessManager().getAllExistingApplications();
   }

   public List<Application> getApplicationsByType (AppSourceType appSourceType) throws KDXException {
      return getKdxDataAccessManager().getApplicationsByType(appSourceType);
   }

   public List<Application> getAllActiveApplications () throws KDXException {
      return getKdxDataAccessManager().getAllActiveApplications();
   }

   public List<Application> getAllActiveStructuredApplications () throws KDXException {
      return getKdxDataAccessManager().getAllActiveStructuredApplications();
   }

   @Override
   public Application getApplicationByName (String applicationName) throws KDXException {
      Application application = getKdxDataAccessManager().getApplicationByName(applicationName);
      navigateApplication(application);
      return application;
   }

   @Override
   public Application getApplicationByKey (String applicationKey) throws KDXException {
      Application application = getKdxDataAccessManager().getApplicationByKey(applicationKey);
      navigateApplication(application);
      return application;
   }

   public ApplicationDetail getApplicationDetail (Long applicationId, Long appImageId) throws KDXException {
      return getKdxDataAccessManager().getImageByApplicationId(applicationId);
   }

   public List<Application> getApplicationsByPage (Page pageDetail) throws KDXException {
      return getKdxDataAccessManager().getApplicationsByPage(pageDetail);
   }

   public List<Application> getApplicationsByImageId (Long imageId) throws KDXException {
      return getKdxDataAccessManager().getApplicationsByImageId(imageId);
   }

   public Long getAllExistingApplicationsCount () throws KDXException {
      return getKdxDataAccessManager().getAllExistingApplicationsCount();
   }

   /**
    * @param applicationId
    * @return the boolean true/false if the app source type is structured
    * @throws KDXException
    */
   public boolean isModelEvaluationRequiredByAppSourceType (Long applicationId) throws KDXException {
      Application application = getKdxDataAccessManager().getApplicationById(applicationId);
      boolean isModelEvaluationRequiredByAppSourceType = application.getSourceType() == AppSourceType.STRUCTURED;
      return isModelEvaluationRequiredByAppSourceType;
   }

   public boolean isUnstructuredWHManagementRequiredByAppSourceType (Long applicationId) throws KDXException {
      Application application = getKdxDataAccessManager().getApplicationById(applicationId);
      boolean isModelEvaluationRequiredByAppSourceType = application.getSourceType() == AppSourceType.UNSTRUCTURED;
      return isModelEvaluationRequiredByAppSourceType;
   }

   @Override
   public Application getApplicationWithExample (Long applicationId) throws KDXException {
      Application application = getKdxDataAccessManager().getApplicationById(applicationId);
      if (application != null) {
         Set<Model> models = application.getModels();
         if (ExecueCoreUtil.isCollectionNotEmpty(models)) {
            for (Model model : models) {
               model.getName();
            }
         }
         Set<ApplicationExample> appQueryExamples = application.getAppQueryExamples();
         if (ExecueCoreUtil.isCollectionNotEmpty(appQueryExamples)) {
            for (ApplicationExample applicationExample : appQueryExamples) {
               applicationExample.getQueryName();
            }
            Collections.sort(new ArrayList<ApplicationExample>(appQueryExamples),
                     new ApplicationExampleQueryNameComparator());
         }
      }
      return application;
   }

   public List<ApplicationExample> getAllAppExampleForApplication (Long appId) throws KDXException {
      return getKdxDataAccessManager().getAllAppExampleForApplication(appId);
   }

   @Override
   public List<Application> getVerticalApplicationsByRank (Long verticalId, Long limit) throws KDXException {
      List<Application> applications = getKdxDataAccessManager().getVerticalApplicationsByRank(verticalId, limit);
      if (ExecueCoreUtil.isCollectionNotEmpty(applications)) {
         for (Application application : applications) {
            navigateApplication(application);
            navigateApplicationExample(application);
         }
      }
      return applications;
   }

   @Override
   public List<Application> getApplicationsByRank (Long limit) throws KDXException {
      List<Application> applications = getKdxDataAccessManager().getApplicationsByRank(limit);
      if (ExecueCoreUtil.isCollectionNotEmpty(applications)) {
         for (Application application : applications) {
            navigateApplication(application);
            navigateApplicationExample(application);
         }
      }
      return applications;
   }

   private void navigateApplicationExample (Application application) {
      Set<ApplicationExample> appQueryExamples = application.getAppQueryExamples();
      if (ExecueCoreUtil.isCollectionNotEmpty(appQueryExamples)) {
         for (ApplicationExample applicationExample : appQueryExamples) {
            applicationExample.getQueryName();
         }
      }
   }

   public List<Application> getAllApplicationsOrderedByName () throws KDXException {
      return getKdxDataAccessManager().getAllApplicationsOrderedByName();
   }

   @Override
   public FacetNatureType getFacetNatureByApplicationId (Long applicationId) throws KDXException {
      FacetNatureType facetNatureType = FacetNatureType.COMBINATION;
      UnstructuredApplicationDetail unstructuredApplicationDetail = getUnstructuredApplicationDetailByApplicationId(applicationId);
      if (unstructuredApplicationDetail != null) {
         facetNatureType = unstructuredApplicationDetail.getFacetNatureType();
      }
      return facetNatureType;
   }

   @Override
   public CheckType getLocationFromContentByApplicationId (Long applicationId) throws KDXException {
      CheckType locationFromContent = CheckType.NO;
      UnstructuredApplicationDetail unstructuredApplicationDetail = getUnstructuredApplicationDetailByApplicationId(applicationId);
      if (unstructuredApplicationDetail != null) {
         locationFromContent = unstructuredApplicationDetail.getLocationFromContent();
      }
      return locationFromContent;
   }

   @Override
   public UnstructuredApplicationDetail getUnstructuredApplicationDetailByApplicationId (Long applicationId)
            throws KDXException {
      return getKdxDataAccessManager().getUnstructuredApplicationDetailByApplicationId(applicationId);
   }

   @Override
   public List<Vertical> getAppVerticals (Long applicationId) throws KDXException {
      return getKdxDataAccessManager().getAppVerticals(applicationId);
   }

   @Override
   public List<Application> getApplicationsByVerticalId (Long verticalId) throws KDXException {
      List<Application> applications = getKdxDataAccessManager().getApplicationsByVerticalId(verticalId);
      for (Application application : applications) {
         navigateApplication(application);
      }
      return applications;
   }

   @Override
   public List<String> getApplicationNamesForVertical (String verticalName) throws KDXException {
      return getKdxDataAccessManager().getApplicationNamesForVertical(verticalName);
   }

   @Override
   public VerticalAppWeight getVerticalAppWeightByApplicationId (Long id) throws KDXException {
      return getKdxDataAccessManager().getVerticalAppWeightByApplicationId(id);
   }

   @Override
   public List<ApplicationOperation> getApplicationOperationsByApplicationId (Long applicationId) throws KDXException {
      return getKdxDataAccessManager().getApplicationOperationsByApplicationId(applicationId);
   }

   @Override
   public List<VerticalEntityBasedRedirection> getVerticalRedirectionEntitiesByApplicationId (Long id)
            throws KDXException {
      return getKdxDataAccessManager().getVerticalRedirectionEntitiesByApplicationId(id);
   }

   @Override
   public List<VerticalAppExample> getVerticalAppExamplesByApplicationId (Long applicationId) throws KDXException {
      return getKdxDataAccessManager().getVerticalAppExamplesByApplicationId(applicationId);
   }

   @Override
   public List<Application> getApplicationsForVertical (String verticalName) throws KDXException {
      List<Application> applications = getKdxDataAccessManager().getApplicationsForVertical(verticalName);
      for (Application application : applications) {
         navigateApplication(application);
      }
      return applications;
   }

   @Override
   public List<AppDashBoardInfo> getAppDashBoardInfosByPage (Page pageDetail, boolean advancedMenu) throws KDXException {
      return getKdxDataAccessManager().getAppDashBoardInfosByPage(pageDetail, advancedMenu);
   }

   @Override
   public List<AppDashBoardInfo> getAppDashBoardInfosByPage (Page pageDetail) throws KDXException {
      return getKdxDataAccessManager().getAppDashBoardInfosByPage(pageDetail);
   }

   @Override
   public ApplicationDetail getImageByAppliationImageId (Long applicationId, Long imageId) throws KDXException {
      return getKdxDataAccessManager().getImageByApplicationImageId(applicationId, imageId);
   }

   @Override
   public ApplicationDetail getImageByApplicationId (Long applicationId) throws KDXException {
      return getKdxDataAccessManager().getImageByApplicationId(applicationId);
   }

   @Override
   public List<Application> getApplicationsByModelId (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getApplicationsByModelId(modelId);
   }

   @Override
   public boolean isApplicationExist (String applicationName) throws KDXException {
      return getKdxDataAccessManager().isApplicationExist(applicationName);
   }

   @Override
   public Application getApplicationByModelId (Long modelId) throws KDXException {
      List<Application> applications = getApplicationsByModelId(modelId);
      Application application = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(applications)) {
         application = applications.get(0);
      }
      return application;
   }

   @Override
   public List<Application> getAllActiveStructuredApplicationsByPublishMode (PublishAssetMode publishMode)
            throws KDXException {
      return getKdxDataAccessManager().getAllActiveStructuredApplicationsByPublishMode(publishMode);
   }

   @Override
   public List<Application> getAllActiveStructuredEligibleApplicationsByUserId (Long userId,
            boolean skipOtherCommunityApps) throws KDXException {
      return getKdxDataAccessManager().getAllActiveStructuredEligibleApplicationsByUserId(userId,
               skipOtherCommunityApps);
   }

   @Override
   public List<Application> getApplicationsForByPublishModeOrderByRank (PublishAssetMode publishMode)
            throws KDXException {
      List<Application> applications = getKdxDataAccessManager()
               .getApplicationsForByPublishModeOrderByRank(publishMode);

      if (ExecueCoreUtil.isCollectionNotEmpty(applications)) {
         for (Application application : applications) {
            navigateApplication(application);
            navigateApplicationExample(application);
         }
      }
      return applications;
   }

   @Override
   public List<Application> getAllEligibleApplicationsByUserId (Long userId, boolean skipOtherCommunityApps)
            throws KDXException {
      List<Application> applications = getKdxDataAccessManager().getAllEligibleApplicationsByUserId(userId,
               skipOtherCommunityApps);
      if (ExecueCoreUtil.isCollectionNotEmpty(applications)) {
         for (Application application : applications) {
            navigateApplication(application);
            navigateApplicationExample(application);
         }
      }
      return applications;
   }

   private void navigateApplication (Application application) {
      application.getOwner().getFirstName();
      for (Model model : application.getModels()) {
         Set<ModelGroup> modelGroups = model.getModelGroups();
         for (ModelGroup mg : modelGroups) {
            mg.getName();
         }
      }
   }

   /**
    * @return the kdxDataAccessManager
    */
   public IKDXDataAccessManager getKdxDataAccessManager () {
      return kdxDataAccessManager;
   }

   /**
    * @param kdxDataAccessManager
    *           the kdxDataAccessManager to set
    */
   public void setKdxDataAccessManager (IKDXDataAccessManager kdxDataAccessManager) {
      this.kdxDataAccessManager = kdxDataAccessManager;
   }

}