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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.ApplicationDetail;
import com.execue.core.common.bean.entity.ApplicationExample;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.UnstructuredApplicationDetail;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.common.bean.entity.VerticalAppExample;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.swi.ApplicationDeletionContext;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.handler.bean.UIApplicationExample;
import com.execue.handler.bean.UIApplicationInfo;
import com.execue.handler.bean.UIApplicationModelInfo;
import com.execue.handler.swi.IApplicationServiceHandler;
import com.execue.platform.swi.IApplicationManagementWrapperService;
import com.execue.scheduler.service.IApplicationDeletionJobService;
import com.execue.security.UserContextService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationDeletionService;
import com.execue.swi.service.IApplicationManagementService;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IUserManagementService;

/**
 * @author JTiwari
 * @since 13/11/09
 */
public class ApplicationServiceHandlerImpl extends UserContextService implements IApplicationServiceHandler {

   private IApplicationRetrievalService         applicationRetrievalService;
   private IApplicationManagementService        applicationManagementService;
   private IApplicationManagementWrapperService applicationManagementWrapperService;
   private IKDXRetrievalService                 kdxRetrievalService;
   private IKDXManagementService                kdxManagementService;
   private IApplicationDeletionJobService       applicationDeletionJobService;
   private IUserManagementService               userManagementService;
   private ICoreConfigurationService            coreConfigurationService;
   private IApplicationDeletionService          applicationDeletionService;

   public List<UIApplicationModelInfo> getAllApplications () throws HandlerException {
      List<UIApplicationModelInfo> uiApplications = new ArrayList<UIApplicationModelInfo>();
      try {
         List<Application> applications = getApplicationRetrievalService().getAllApplications();
         if (ExecueCoreUtil.isCollectionNotEmpty(applications)) {
            uiApplications = transformUIApplication(applications);
         }
      } catch (KDXException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
      return uiApplications;
   }

   public void createApplication (Application application) throws HandlerException {
      try {
         Date createdDate = new Date();
         application.setCreatedDate(createdDate);
         User user = new User();
         user.setId(getUserContext().getUser().getId());
         application.setOwner(user);
         getApplicationManagementWrapperService().createApplicationHierarchy(application);
         updateUserForPublisher();
      } catch (KDXException e) {
         if (e.getCode() == ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS) {
            throw new HandlerException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, e);
         } else {
            throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
         }
      }
   }

   public void updateApplication (Application application) throws HandlerException {
      try {
         getApplicationManagementWrapperService().updateApplicationHierarchy(application);
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
   }

   public Application getApplicationById (Long applicationId) throws HandlerException {
      Application application = null;
      try {
         application = getApplicationRetrievalService().getApplicationById(applicationId);
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
      return application;
   }

   public List<Model> getModelsByApplicationId (Long applicationId) throws HandlerException {
      List<Model> models = null;
      try {
         models = kdxRetrievalService.getModelsByApplicationId(applicationId);
         if (ExecueCoreUtil.isCollectionEmpty(models)) {
            models = new ArrayList<Model>();
         }
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
      return models;
   }

   public List<UIApplicationModelInfo> getApplications () throws KDXException {
      List<Application> applications = null;
      if ("ROLE_ADMIN".equalsIgnoreCase(getUserContext().getRole())) {
         applications = getApplicationRetrievalService().getAllApplications();
      } else if ("ROLE_PUBLISHER".equalsIgnoreCase(getUserContext().getRole())) {
         applications = getApplicationRetrievalService().getApplications(getUserContext().getUser().getId());
      }
      return transformUIApplication(applications);
   }

   public Long deleteApplication (Long applicationId) throws KDXException {
      Long jobRequestId = null;
      ApplicationDeletionContext applicationDeletionContext = new ApplicationDeletionContext();
      applicationDeletionContext.setApplicationId(applicationId);
      applicationDeletionContext.setUserId(getUserContext().getUser().getId());

      try {
         jobRequestId = getApplicationDeletionJobService().scheduleApplicationDeletionJob(applicationDeletionContext);
      } catch (SWIException sdxException) {
         sdxException.printStackTrace();
         throw new KDXException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, sdxException);
      }
      return jobRequestId;
   }

   public void absorbApplicationImage (Long applicationId, File imageFile, String sourceDataFileName, String imageType)
            throws ExeCueException {
      getApplicationManagementService().absorbApplicationImage(applicationId, imageFile, sourceDataFileName, imageType);
   }

   public ApplicationDetail getApplicationDetail (Long applicationId, Long appImageId) throws KDXException {
      return getApplicationRetrievalService().getApplicationDetail(applicationId, appImageId);
   }

   public List<UIApplicationInfo> getUIApplicationInfo () throws KDXException {
      List<UIApplicationInfo> uiApplicationInfoList = null;
      List<Application> applications = getApplicationRetrievalService().getAllApplications();
      if (ExecueCoreUtil.isCollectionNotEmpty(applications)) {
         uiApplicationInfoList = transformUIApplicationInfoList(applications);
      }
      return uiApplicationInfoList;
   }

   public List<UIApplicationInfo> getApplicationsByPage (Page pageDetail) throws KDXException {
      List<Application> applications = getApplicationRetrievalService().getApplicationsByPage(pageDetail);
      List<UIApplicationInfo> uiApplications = new ArrayList<UIApplicationInfo>();
      for (Application application : applications) {
         UIApplicationInfo uiApplicationInfo = new UIApplicationInfo();
         uiApplicationInfo.setApplicationId(application.getId());
         uiApplicationInfo.setApplicationName(application.getName());
         uiApplicationInfo.setApplicationDescription(application.getDescription());
         uiApplicationInfo.setApplicationURL(application.getApplicationURL());
         uiApplicationInfo.setApplicationImageId(application.getImageId());
         uiApplicationInfo.setPublisherName(getPublisherName(application.getOwner().getId()));
         uiApplications.add(uiApplicationInfo);
      }
      return uiApplications;
   }

   private String getPublisherName (Long id) {
      String publisherName = "";
      try {
         User user = getUserManagementService().getUserById(id);
         if (user != null) {
            publisherName = user.getFirstName() + " " + user.getLastName();
         }
      } catch (SWIException e) {
         e.printStackTrace();
      }
      return publisherName;
   }

   public Long getAllExistingApplicationsCount () throws HandlerException {
      try {
         return getApplicationRetrievalService().getAllExistingApplicationsCount();
      } catch (KDXException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public Application getApplicationWithExample (Long applicationId) throws HandlerException {
      try {
         return getApplicationRetrievalService().getApplicationWithExample(applicationId);
      } catch (KDXException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
   }

   public ApplicationExample createApplicationExample (ApplicationExample applicationExample) throws HandlerException {
      try {
         return getApplicationManagementService().createApplicationExample(applicationExample);
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
   }

   public void deleteApplicationExample (ApplicationExample applicationExample) throws HandlerException {
      try {
         getApplicationDeletionService().deleteApplicationExample(applicationExample);
      } catch (SWIException e) {
         throw new HandlerException(e.getCode(), e);
      }

   }

   public void updateApplicationExample (ApplicationExample applicationExample) throws HandlerException {
      try {
         getApplicationManagementService().updateApplicationExample(applicationExample);
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }

   }

   public List<UIApplicationExample> getAllAppExampleForApplication (Long appId) throws HandlerException {
      try {
         List<UIApplicationExample> appExamples = new ArrayList<UIApplicationExample>();
         List<ApplicationExample> allAppExampleForApplication = getApplicationRetrievalService()
                  .getAllAppExampleForApplication(appId);
         if (ExecueCoreUtil.isCollectionNotEmpty(allAppExampleForApplication)) {
            for (ApplicationExample applicationExample : allAppExampleForApplication) {
               appExamples.add(transformAppExample(applicationExample));
            }
         }
         return appExamples;
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
   }

   public UIApplicationInfo getApplicationDetailByAppId (Long appId) throws HandlerException {
      UIApplicationInfo uiApplicationInfo = null;
      try {
         Application application = getApplicationRetrievalService().getApplicationWithExample(appId);
         // TODO -KA- Temp fix to avoid Craigslist app visibility to users.
         // if (application != null
         // && !(application.getSourceType() == AppSourceType.UNSTRUCTURED && !getUserContext().getUser().getId()
         // .equals(application.getOwner().getId()))) {
         int truncatedStringLength = Integer.valueOf(getCoreConfigurationService()
                  .getAppSpecificExampleTruncatedLength());
         int appExampleRetrivalLimit = Integer.valueOf(getCoreConfigurationService()
                  .getAppSpecificExampleRetrievalLimit());
         uiApplicationInfo = tranformUIApplicationInfo(application, truncatedStringLength, appExampleRetrivalLimit);
         // }
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
      return uiApplicationInfo;
   }

   public List<UIApplicationInfo> getVerticalApplicationsByRank (Long verticalId) throws HandlerException {
      List<UIApplicationInfo> uiApplications = null;
      try {
         List<Application> applications = getApplicationRetrievalService().getVerticalApplicationsByRank(verticalId,
                  Long.valueOf(getCoreConfigurationService().getApplicationRetrievalLimit()));
         if (ExecueCoreUtil.isCollectionNotEmpty(applications)) {
            uiApplications = transformUIApplicationInfoList(applications);
         }
      } catch (SWIException exception) {
         throw new HandlerException(exception.getCode(), exception);
      }
      return uiApplications;
   }

   public List<UIApplicationInfo> getApplicationsByRank () throws HandlerException {
      List<UIApplicationInfo> uiApplications = null;
      try {
         List<Application> applications = getApplicationRetrievalService().getApplicationsByRank(
                  Long.valueOf(getCoreConfigurationService().getApplicationRetrievalLimit()));
         if (ExecueCoreUtil.isCollectionNotEmpty(applications)) {
            uiApplications = transformUIApplicationInfoList(applications);
         }
      } catch (SWIException exception) {
         throw new HandlerException(exception.getCode(), exception);
      }
      return uiApplications;
   }

   private UIApplicationInfo tranformUIApplicationInfo (Application application, int truncatedStringLength,
            int appExampleRetrivalLimit) {
      UIApplicationInfo uiApplicationInfo = new UIApplicationInfo();
      uiApplicationInfo.setApplicationId(application.getId());
      uiApplicationInfo.setApplicationName(application.getName());
      uiApplicationInfo.setApplicationDescription(application.getDescription());
      uiApplicationInfo.setApplicationURL(application.getApplicationURL());
      uiApplicationInfo.setApplicationImageId(application.getImageId());
      uiApplicationInfo.setApplicationTag(application.getApplicationTag());
      uiApplicationInfo.setApplicationTitle(application.getApplicationTitle());
      uiApplicationInfo.setPublisherName(getPublisherName(application.getOwner().getId()));
      uiApplicationInfo.setPublishMode(application.getPublishMode());
      uiApplicationInfo.setSourceType(application.getSourceType());
      if (ExecueCoreUtil.isCollectionNotEmpty(application.getAppQueryExamples())) {
         List<ApplicationExample> appExamples = new ArrayList<ApplicationExample>(application.getAppQueryExamples());
         uiApplicationInfo.setAppExamples(transformTruncatedUIAppExamples(appExamples, truncatedStringLength,
                  appExampleRetrivalLimit));
      } else {
         uiApplicationInfo.setAppExamples(new ArrayList<UIApplicationExample>());
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(application.getModels())) {
         List<Model> modeList = new ArrayList<Model>(application.getModels());
         uiApplicationInfo.setModelId(modeList.get(0).getId());
      }
      return uiApplicationInfo;
   }

   private List<UIApplicationExample> transformTruncatedUIAppExamples (List<ApplicationExample> appExamples,
            int truncatedStringLength, int appExampleRetrivalLimit) {
      List<UIApplicationExample> uiApplicationExamples = new ArrayList<UIApplicationExample>();
      for (ApplicationExample applicationExample : appExamples) {
         // TODO:-We will not show more than 10 example in UI. We already have a condition that user can not save more
         // than 10 application example from the admin console, so need to see whether this check is required for now I
         // need put this check because we already have records(more than 10 example for an app) in DB.
         if (uiApplicationExamples.size() >= appExampleRetrivalLimit) {
            break;
         }
         UIApplicationExample uiApplicationExample = transformAppExample(applicationExample);
         if (applicationExample.getQueryName() != null) {
            uiApplicationExample.setTruncatedQueryName(ExecueStringUtil.getTruncatedString(applicationExample
                     .getQueryName(), truncatedStringLength));
         }
         uiApplicationExamples.add(uiApplicationExample);
      }
      return uiApplicationExamples;
   }

   private void updateUserForPublisher () throws HandlerException {
      try {
         User user = getUserManagementService().getUserById(getUserContext().getUser().getId());
         if (user != null) {
            user.setIsPublisher(CheckType.YES);
            getUserManagementService().updateUser(user);
         }
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.USER_UPDATION_FAILED, e);
      }
   }

   private List<UIApplicationModelInfo> transformUIApplication (List<Application> applications) {
      List<UIApplicationModelInfo> uiApplications = new ArrayList<UIApplicationModelInfo>();
      for (Application application : applications) {
         UIApplicationModelInfo uiApplication = new UIApplicationModelInfo();
         uiApplication.setApplicationId(application.getId());
         uiApplication.setApplicationName(application.getName());
         uiApplication.setApplicationURL(application.getApplicationURL());
         uiApplication.setSourceType(application.getSourceType());
         // TODO:- VG- as off now only one model for one application.
         for (Model model : application.getModels()) {
            uiApplication.setModelId(model.getId());
         }
         uiApplications.add(uiApplication);

      }
      return uiApplications;
   }

   private UIApplicationExample transformAppExample (ApplicationExample appExamples) {
      UIApplicationExample uiApplicationExample = new UIApplicationExample();
      uiApplicationExample.setId(appExamples.getId());
      uiApplicationExample.setQueryName(appExamples.getQueryName());
      uiApplicationExample.setQueryValue(appExamples.getQueryValue());
      uiApplicationExample.setType(appExamples.getType());
      uiApplicationExample.setApplicationId(appExamples.getApplication().getId());
      return uiApplicationExample;
   }

   private List<UIApplicationInfo> transformUIApplicationInfoList (List<Application> applications) {
      List<UIApplicationInfo> uiApplications = new ArrayList<UIApplicationInfo>();
      int truncatedStringLength = Integer.valueOf(getCoreConfigurationService().getApplicationExampleTruncatedLength());
      int appExampleRetrivalLimit = Integer
               .valueOf(getCoreConfigurationService().getApplicationExampleRetrievalLimit());
      for (Application application : applications) {
         UIApplicationInfo uiApplicationInfo = tranformUIApplicationInfo(application, truncatedStringLength,
                  appExampleRetrivalLimit);
         if (uiApplicationInfo.getApplicationDescription() != null) {
            uiApplicationInfo.setCompleteAppDescription(uiApplicationInfo.getApplicationDescription());
            uiApplicationInfo.setApplicationDescription(ExecueStringUtil.getTruncatedString(uiApplicationInfo
                     .getApplicationDescription(), Integer.valueOf(getCoreConfigurationService()
                     .getApplicationDescriptionTruncatedLength())));
         }
         uiApplications.add(uiApplicationInfo);
      }
      return uiApplications;
   }

   public boolean isApplicationExist (String applicationName) throws HandlerException {
      try {
         return getApplicationRetrievalService().isApplicationExist(applicationName);
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
   }

   public Map<String, List<VerticalAppExample>> getVerticalAppExamples () throws HandlerException {
      try {
         int day = Calendar.getInstance().get(Calendar.DATE);
         Map<String, List<VerticalAppExample>> map = getKdxRetrievalService().getVerticalAppExamplesByDay(day);
         List<String> verticalWithNoExmaples = new ArrayList<String>();
         for (Map.Entry<String, List<VerticalAppExample>> entrySet : map.entrySet()) {
            List<VerticalAppExample> valueList = entrySet.getValue();
            if (ExecueCoreUtil.isCollectionNotEmpty(valueList)) {
               for (VerticalAppExample verticalAppExample : valueList) {
                  // TODO: need to get length from the configuration
                  verticalAppExample.setTruncatedQueryExample(ExecueStringUtil.getTruncatedString(verticalAppExample
                           .getQueryExample(), 50));
               }
            } else {
               verticalWithNoExmaples.add(entrySet.getKey());
            }
         }
         // remove vertical with no examples
         for (String key : verticalWithNoExmaples) {
            map.remove(key);
         }
         return map;
      } catch (SWIException swiException) {
         throw new HandlerException(swiException.getCode(), swiException);
      }
   }

   public List<Vertical> getAppVerticals (Long applicationId) throws HandlerException {
      try {
         return getApplicationRetrievalService().getAppVerticals(applicationId);
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
   }

   public Application getApplicationByName (String applicationName) throws HandlerException {
      try {
         return getApplicationRetrievalService().getApplicationByName(applicationName);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<UIApplicationModelInfo> getAllApplicationsOrderedByName () throws HandlerException {
      List<UIApplicationModelInfo> uiApplications = new ArrayList<UIApplicationModelInfo>();
      try {
         List<Application> applications = getApplicationRetrievalService().getAllActiveStructuredApplications();
         if (ExecueCoreUtil.isCollectionNotEmpty(applications)) {
            uiApplications = transformUIApplication(applications);
         }
      } catch (KDXException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
      return uiApplications;
   }

   public String isUnstructuredAppVisibleToUser () {
      return getCoreConfigurationService().getUnstructuredAppUserVisibility().toString();

   }

   @Override
   public UnstructuredApplicationDetail getUnstructuredApplicationDetailByApplicationId (Long applicationId)
            throws HandlerException {
      try {
         return getApplicationRetrievalService().getUnstructuredApplicationDetailByApplicationId(applicationId);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   @Override
   public List<UIApplicationModelInfo> getAllActiveStructuredCommunityApplicationsIncludingUserApps ()
            throws HandlerException {
      List<UIApplicationModelInfo> uiApplications = new ArrayList<UIApplicationModelInfo>();
      try {
         List<Application> eligibleApplications = new ArrayList<Application>();
         if (getUserContext().getUser() != null && getUserContext().getUser().getId() > 0) {
            eligibleApplications = getApplicationRetrievalService().getAllActiveStructuredEligibleApplicationsByUserId(
                     getUserContext().getUser().getId(), false);
         } else {
            eligibleApplications = getApplicationRetrievalService().getAllActiveStructuredApplicationsByPublishMode(
                     PublishAssetMode.COMMUNITY);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(eligibleApplications)) {
            uiApplications = transformUIApplication(eligibleApplications);
         }
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
      return uiApplications;
   }

   @Override
   public List<UIApplicationInfo> getAllCommunityApplicationsIncludingUserApps () throws HandlerException {
      List<UIApplicationInfo> uiApplications = new ArrayList<UIApplicationInfo>();
      try {
         List<Application> eligibleApplications = new ArrayList<Application>();
         if (getUserContext().getUser() != null && getUserContext().getUser().getId() > 0) {
            eligibleApplications = getApplicationRetrievalService().getAllEligibleApplicationsByUserId(
                     getUserContext().getUser().getId(), false);

         } else {
            eligibleApplications = getApplicationRetrievalService().getApplicationsForByPublishModeOrderByRank(
                     PublishAssetMode.COMMUNITY);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(eligibleApplications)) {
            uiApplications = transformUIApplicationInfoList(eligibleApplications);
         }
      } catch (KDXException exception) {
         throw new HandlerException(exception.getCode(), exception);
      }
      return uiApplications;
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
    * @return the userManagementService
    */
   public IUserManagementService getUserManagementService () {
      return userManagementService;
   }

   /**
    * @param userManagementService
    *           the userManagementService to set
    */
   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService
    *           the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
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

   public boolean isAdvancedPublisher () {
      return getUserContext().getRoles().contains("ROLE_ADV_PUBLISHER");
   }

   public IApplicationDeletionService getApplicationDeletionService () {
      return applicationDeletionService;
   }

   public void setApplicationDeletionService (IApplicationDeletionService applicationDeletionService) {
      this.applicationDeletionService = applicationDeletionService;
   }

   public IApplicationManagementWrapperService getApplicationManagementWrapperService () {
      return applicationManagementWrapperService;
   }

   public void setApplicationManagementWrapperService (
            IApplicationManagementWrapperService applicationManagementWrapperService) {
      this.applicationManagementWrapperService = applicationManagementWrapperService;
   }

   public IApplicationDeletionJobService getApplicationDeletionJobService () {
      return applicationDeletionJobService;
   }

   public void setApplicationDeletionJobService (IApplicationDeletionJobService applicationDeletionJobService) {
      this.applicationDeletionJobService = applicationDeletionJobService;
   }

}
