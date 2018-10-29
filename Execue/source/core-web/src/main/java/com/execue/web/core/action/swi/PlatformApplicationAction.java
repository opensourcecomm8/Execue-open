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
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.ApplicationContext;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.PageSearch;
import com.execue.core.common.bean.PageSort;
import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.ApplicationDetail;
import com.execue.core.common.bean.entity.ApplicationExample;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetExtendedDetail;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.UnstructuredApplicationDetail;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.FacetNatureType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIAppStatus;
import com.execue.handler.bean.UIApplicationExample;
import com.execue.handler.bean.UIApplicationInfo;
import com.execue.handler.bean.UIApplicationModelInfo;
import com.execue.handler.bean.UIAssetDetail;
import com.execue.handler.bean.UIJobRequestResult;
import com.execue.handler.bean.UIJobRequestStatus;
import com.execue.handler.swi.IApplicationServiceHandler;
import com.execue.handler.swi.IAssetDetailServiceHandler;
import com.execue.publisher.configuration.IPublisherConfigurationService;
import com.execue.swi.exception.KDXException;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * This action is specific to platform and here we will avoid any vertical/portal stuff
 * 
 * @author jitendra
 */
public class PlatformApplicationAction extends SWIPaginationAction {

   private static final long              serialVersionUID = 2416048769090984311L;
   private static final Logger            log              = Logger.getLogger(ApplicationAction.class);
   private IApplicationServiceHandler     applicationServiceHandler;
   private List<UIApplicationModelInfo>   applications;
   private Application                    application;
   private InputStream                    imageStream;
   private String                         imageType;
   private Long                           applicationId;
   private Long                           appImageId;
   private List<UIApplicationInfo>        applicationsList;
   private InputStream                    inputStream;
   private UIAppStatus                    appStatus;
   private File                           sourceData;
   private String                         sourceDataContentType;
   private String                         sourceDataFileName;
   private IPublisherConfigurationService publisherConfigurationService;
   private static final int               PAGE_SIZE        = 5;
   private static final int               NUMBER_OF_LINKS  = 5;
   private static final String            WIKI_URL         = "WIKI_URL";
   private ApplicationExample             applicationExample;
   private List<UIApplicationExample>     applicationExamples;
   private UIJobRequestResult             jobRequestResult;
   private UIJobRequestStatus             jobRequest;
   private UIAssetDetail                  uiAssetDetail;
   private IAssetDetailServiceHandler     assetDetailServiceHandler;
   private String                         searchStringType;
   private String                         searchStr;

   // action methods
   @Override
   public String input () {
      if (log.isDebugEnabled()) {
         log.info("Came to input flow");
      }
      if (application != null && application.getId() != null) {
         if (log.isDebugEnabled()) {
            log.info("application Name : " + application.getName());
         }
      }
      return SUCCESS;
   }

   public String createApplication () {
      String message = null;
      UIAppStatus appStatus = new UIAppStatus();
      try {
         validateInput(appStatus);
         if (ExecueCoreUtil.isCollectionNotEmpty(appStatus.getErrorMessages())) {
            inputStream = new ByteArrayInputStream(JSONUtil.serialize(appStatus).getBytes());
            return SUCCESS;
         }
         if (application.getId() != null) {
            prepareApplicationForUpdate();
            applicationServiceHandler.updateApplication(application);
            message = "Application modified successfully";
         } else {
            application.setStatus(StatusEnum.ACTIVE);
            application.setPopularity(0L);
            applicationServiceHandler.createApplication(application);
            message = "Application created successfully";
         }
         setAppContext();
         absorbApplicationImage();
         appStatus.setStatus(true);
         appStatus.setApplicationId(application.getId());
         appStatus.setApplicationImageId(application.getImageId());
         appStatus.setApplicationURL(application.getApplicationURL());
         appStatus.setMessage(message);
         inputStream = new ByteArrayInputStream(JSONUtil.serialize(appStatus).getBytes());
      } catch (HandlerException handlerException) {
         appStatus.setStatus(false);
         appStatus.addErrorMessage("Application with name [" + application.getName() + "] already exists.");
      } catch (JSONException e) {
         inputStream = new ByteArrayInputStream(
                  new String(
                           "{\"errorMessages\":[\"An error occurred while importing source data. Please contact customer support\"],\"status\":false}")
                           .getBytes());
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         appStatus.setStatus(false);
         appStatus.addErrorMessage("An error occurred while importing source data. Please contact customer support.");
      } catch (Exception exception) {
         appStatus.setStatus(false);
         appStatus.addErrorMessage("An error occurred while importing source data. Please contact customer support.");
      }

      return SUCCESS;
   }

   // we are not using this method anymore to create application
   public String saveUpdateAppExample () {
      try {
         if (applicationExample != null) {
            // For user interface SI, query name and query value will be same.
            // TODO:- need to handle user interface QI
            applicationExample.setQueryValue(applicationExample.getQueryName());
            if (applicationExample.getId() == null) {
               getApplicationServiceHandler().createApplicationExample(applicationExample);
            } else {
               getApplicationServiceHandler().updateApplicationExample(applicationExample);

            }
            setApplicationExamples(getApplicationServiceHandler().getAllAppExampleForApplication(
                     applicationExample.getApplication().getId()));
         }

      } catch (ExeCueException exeCueException) {
         appStatus.addErrorMessage("Operation failed");
         return ERROR;
      }
      return SUCCESS;
   }

   public String deleteAppExample () {
      try {
         if (applicationExample != null) {
            getApplicationServiceHandler().deleteApplicationExample(applicationExample);
            setApplicationExamples(getApplicationServiceHandler().getAllAppExampleForApplication(
                     applicationExample.getApplication().getId()));
         }

      } catch (ExeCueException exeCueException) {
         return ERROR;
      }
      return SUCCESS;
   }

   public String deleteApplication () {
      try {
         applicationServiceHandler.deleteApplication(application.getId());
         addActionMessage(getText("Application deletion request was successfully scheduled "));
      } catch (KDXException kdException) {
         addActionError(getText("execue.global.error", new String[] { kdException.getMessage() }));
         return ERROR;
      }
      return SUCCESS;
   }

   public String deleteApplicationJob () {
      try {
         jobRequestResult = new UIJobRequestResult();
         Long applicationId = getApplicationContext().getAppId();
         Long jobRequestId = applicationServiceHandler.deleteApplication(applicationId);
         // resetting the app context once the job has been initiated for application deletion
         setApplicationContext(null);
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         getJobRequestResult().setStatus(getJobRequest().getJobStatus());
         getJobRequestResult().setJobId(getJobRequest().getId());
      } catch (ExeCueException e) {
         e.printStackTrace();
         getJobRequestResult().setErrMsg("An error occurred while scheduling job. Please contact customer support");
         getJobRequestResult().setStatus(JobStatus.FAILURE);
         return ERROR;
      }
      return SUCCESS;
   }

   public String getAllApplications () {
      try {
         applications = applicationServiceHandler.getAllApplications();
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   public String getApplicationInfo () {
      try {
         if (application != null && application.getId() != null) {
            application = applicationServiceHandler.getApplicationById(application.getId());
            if (application != null) {
               List<Model> models = applicationServiceHandler.getModelsByApplicationId(application.getId());
               ApplicationContext applicationContext = ExecueBeanManagementUtil.prepareApplicationContext(models.get(0)
                        .getId(), application.getId(), application.getName(), null, null, application.getSourceType());
               setApplicationContext(applicationContext);
            }
         }

      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   public String getApplicationWithExample () {
      try {
         if (application != null && application.getId() != null) {
            application = applicationServiceHandler.getApplicationWithExample(application.getId());
            if (application != null) {
               setAppContext(application);
               setAssetDisclaimer(application.getId());
            }
         }
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   public String getUploadedAppImage () {
      try {
         ApplicationDetail applicationDetail = applicationServiceHandler
                  .getApplicationDetail(applicationId, appImageId);
         if (applicationDetail != null) {
            Blob blob = applicationDetail.getImageData();
            imageStream = blob.getBinaryStream();
            imageType = applicationDetail.getImageType();
         }

      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
      } catch (SQLException sqlException) {
         log.error(sqlException, sqlException);
      }

      return SUCCESS;
   }

   // Action for displaying all the applications for browse all apps functionality;

   public String displayAllApplications () {
      try {
         applicationsList = applicationServiceHandler.getUIApplicationInfo();
      } catch (KDXException e) {
         log.error("Unable to get applications list ", e);
      }
      return SUCCESS;
   }

   public String getWikiUrl () {
      String wikiUrl = getConfiguration().getProperty(WIKI_URL);
      Application app = new Application();
      app.setApplicationURL(wikiUrl);
      setApplication(app);
      return SUCCESS;
   }

   public String getAppExamples () {
      try {
         setApplicationExamples(applicationServiceHandler.getAllAppExampleForApplication(getApplicationId()));
      } catch (HandlerException e) {
         log.error("Unable to get application examples ", e);
      }
      return SUCCESS;
   }

   @SuppressWarnings ("unchecked")
   @Override
   public String processPage () throws ExeCueException {
      getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
      getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
      // getPageDetail().setRecordCount(getApplicationServiceHandler().getAllExistingApplicationsCount());
      Page page = getPageDetail();
      // TODO -KA- Temp fix to avoid Craigslist app visibility to users.
      List<PageSearch> searchList = page.getSearchList();
      PageSearch pSearch = new PageSearch();
      if (ExecueCoreUtil.isListElementsEmpty(searchList)) {
         searchList = new ArrayList<PageSearch>();
         pSearch.setField("sourceType");
         pSearch.setType(PageSearchType.EQUALS);
         pSearch.setString(AppSourceType.STRUCTURED.getValue());
         searchList.add(pSearch);

      }
      page.setSearchList(searchList);

      if (ExecueCoreUtil.isCollectionNotEmpty(page.getSortList())) {
         // for application set DESC order by popularity
         for (PageSort pageSort : page.getSortList()) {
            pageSort.setOrder("DESC");
         }
      } else {
         page.setSortList(new ArrayList<PageSort>());
      }
      setApplicationsList(getApplicationServiceHandler().getApplicationsByPage(getPageDetail()));
      getHttpRequest().put(PAGINATION, getPageDetail());
      return SUCCESS;
   }

   public List<FacetNatureType> getFacetNatureTypes () {
      return Arrays.asList(FacetNatureType.values());
   }

   private void absorbApplicationImage () throws ExeCueException, HandlerException {

      if (getSourceData() != null) {
         String filePath = saveImageFile();
         File imageFile = new File(filePath);
         getApplicationServiceHandler().absorbApplicationImage(application.getId(), imageFile, sourceDataFileName,
                  sourceDataContentType);
         if (application.getImageId() != null && application.getImageId() > 0) {
            application.setImageId(new Long(application.getImageId() + 1));
         } else {
            application.setImageId(1L);
         }
         getApplicationServiceHandler().updateApplication(application);
      }
   }

   private void validateInput (UIAppStatus appStatus) {
      // App name is mandatory
      if (application == null || ExecueCoreUtil.isEmpty(application.getName())) {
         appStatus.addErrorMessage(getText("execue.app.main.name.required"));
         return;
      }
      if (application.getName().length() > 255 || application.getName().length() < 5) {
         appStatus.addErrorMessage(getText("execue.app.main.name.length.mismatch"));
      }
   }

   private void prepareApplicationForUpdate () throws HandlerException {
      String description = application.getDescription();
      String applicationURL = application.getApplicationURL();
      String name = application.getName();
      String applicationTag = application.getApplicationTag();
      PublishAssetMode publishMode = application.getPublishMode();
      UnstructuredApplicationDetail unstructuredApplicationDetailFromUI = application
               .getUnstructuredApplicationDetail();
      UnstructuredApplicationDetail unstructuredApplicationDetailFromDb = null;
      if (AppSourceType.UNSTRUCTURED.equals(application.getSourceType()) && unstructuredApplicationDetailFromUI != null) {
         unstructuredApplicationDetailFromDb = getApplicationServiceHandler()
                  .getUnstructuredApplicationDetailByApplicationId(application.getId());
         unstructuredApplicationDetailFromDb.setFacetNatureType(application.getUnstructuredApplicationDetail()
                  .getFacetNatureType());
         unstructuredApplicationDetailFromDb.setLocationFromContent(application.getUnstructuredApplicationDetail()
                  .getLocationFromContent());
      }

      application = applicationServiceHandler.getApplicationById(application.getId());
      application.setDescription(description);
      application.setApplicationURL(applicationURL);
      application.setName(name);
      application.setApplicationTag(applicationTag);
      application.setPublishMode(PublishAssetMode.LOCAL);
      if (PublishAssetMode.COMMUNITY == publishMode) {
         application.setPublishMode(publishMode);
      }
      application.setUnstructuredApplicationDetail(unstructuredApplicationDetailFromDb);
   }

   private void setAppContext () throws HandlerException {
      List<Model> models = applicationServiceHandler.getModelsByApplicationId(application.getId());
      if (ExecueCoreUtil.isCollectionNotEmpty(models)) {
         ApplicationContext applicationContext = ExecueBeanManagementUtil.prepareApplicationContext(models.get(0)
                  .getId(), application.getId(), application.getName(), null, null, application.getSourceType());
         setApplicationContext(applicationContext);
      }
   }

   private String saveImageFile () throws ExeCueException {
      String destinationImageFileName = getFileStoragePath("IMAGE");
      destinationImageFileName = destinationImageFileName + "/"
               + getFormattedFileNameForStorage(getSourceDataFileName(), getApplicationContext().getAppId());
      File destination = null;
      destination = new File(destinationImageFileName);
      if (destination.exists()) {
         destination.delete();
         destination = new File(destinationImageFileName);
      }

      try {
         ExecueCoreUtil.copyFile(getSourceData(), destination);
      } catch (Exception e) {
         log.error("Image File rename failed");
         throw new ExeCueException(1001, "Could not save the Image file");
      }

      return destinationImageFileName;
   }

   private String getFormattedFileNameForStorage (String originalFileName, Long applicationId) {
      String destinationFilename = ExecueCoreUtil.getFormattedSystemCurrentMillis() + "_"
               + RandomStringUtils.randomAlphabetic(3) + "_" + applicationId + "_" + originalFileName;
      return destinationFilename.toLowerCase();
   }

   private String getFileStoragePath (String sourceType) {
      return getPublisherConfigurationService().getPublisherFileStoragePath(sourceType);
   }

   private void setAppContext (Application application) {
      List<Model> models = new ArrayList<Model>(application.getModels());
      ApplicationContext applicationContext = ExecueBeanManagementUtil.prepareApplicationContext(models.get(0).getId(),
               application.getId(), application.getName(), null, null, application.getSourceType());
      setApplicationContext(applicationContext);
   }

   private void setAssetDisclaimer (Long id) throws ExeCueException {
      List<Asset> assets = getSdxServiceHandler().getAssetsByApplicationId(application.getId());
      uiAssetDetail = new UIAssetDetail();
      if (ExecueCoreUtil.isCollectionNotEmpty(assets)) {
         // TODO:-JT- for Simplified publisher process we have only one asset per application, need to revisit
         // this.
         uiAssetDetail.setIsAssetDisclaimerApplicable(CheckType.YES);
         AssetExtendedDetail assetExtendedDetail = getAssetDetailServiceHandler().getAssetExtendedDetailByAssetId(
                  assets.get(0).getId());
         uiAssetDetail.setExtendedNote(assetExtendedDetail.getExtendedNote());
         uiAssetDetail.setExtendedDisclaimer(assetExtendedDetail.getExtendedDisclaimer());
      }

   }

   public List<AppSourceType> getAppSourceTypes () {
      return Arrays.asList(AppSourceType.values());
   }

   public boolean isAdvancedPublisher () {
      return getApplicationServiceHandler().isAdvancedPublisher();
   }

   @Override
   public IAssetDetailServiceHandler getAssetDetailServiceHandler () {
      return assetDetailServiceHandler;
   }

   @Override
   public void setAssetDetailServiceHandler (IAssetDetailServiceHandler assetDetailServiceHandler) {
      this.assetDetailServiceHandler = assetDetailServiceHandler;
   }

   public Long getAppImageId () {
      return appImageId;
   }

   public void setAppImageId (Long appImageId) {
      this.appImageId = appImageId;
   }

   public ApplicationExample getApplicationExample () {
      return applicationExample;
   }

   public void setApplicationExample (ApplicationExample applicationExample) {
      this.applicationExample = applicationExample;
   }

   public List<UIApplicationExample> getApplicationExamples () {
      return applicationExamples;
   }

   public void setApplicationExamples (List<UIApplicationExample> applicationExamples) {
      this.applicationExamples = applicationExamples;
   }

   public UIJobRequestResult getJobRequestResult () {
      return jobRequestResult;
   }

   public void setJobRequestResult (UIJobRequestResult jobRequestResult) {
      this.jobRequestResult = jobRequestResult;
   }

   public UIJobRequestStatus getJobRequest () {
      return jobRequest;
   }

   public void setJobRequest (UIJobRequestStatus jobRequest) {
      this.jobRequest = jobRequest;
   }

   public List<UIApplicationModelInfo> getApplications () {
      return applications;
   }

   public void setApplications (List<UIApplicationModelInfo> applications) {
      this.applications = applications;
   }

   public IApplicationServiceHandler getApplicationServiceHandler () {
      return applicationServiceHandler;
   }

   public void setApplicationServiceHandler (IApplicationServiceHandler applicationServiceHandler) {
      this.applicationServiceHandler = applicationServiceHandler;
   }

   public Application getApplication () {
      return application;
   }

   public void setApplication (Application application) {
      this.application = application;
   }

   public InputStream getImageStream () {
      return imageStream;
   }

   public void setImageStream (InputStream imageStream) {
      this.imageStream = imageStream;
   }

   public String getImageType () {
      return imageType;
   }

   public void setImageType (String imageType) {
      this.imageType = imageType;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public InputStream getInputStream () {
      return inputStream;
   }

   public void setInputStream (InputStream inputStream) {
      this.inputStream = inputStream;
   }

   public UIAppStatus getAppStatus () {
      return appStatus;
   }

   public void setAppStatus (UIAppStatus appStatus) {
      this.appStatus = appStatus;
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

   public IPublisherConfigurationService getPublisherConfigurationService () {
      return publisherConfigurationService;
   }

   public void setPublisherConfigurationService (IPublisherConfigurationService publisherConfigurationService) {
      this.publisherConfigurationService = publisherConfigurationService;
   }

   public List<UIApplicationInfo> getApplicationsList () {
      return applicationsList;
   }

   public void setApplicationsList (List<UIApplicationInfo> applicationsList) {
      this.applicationsList = applicationsList;
   }

   public UIAssetDetail getUiAssetDetail () {
      return uiAssetDetail;
   }

   public void setUiAssetDetail (UIAssetDetail uiAssetDetail) {
      this.uiAssetDetail = uiAssetDetail;
   }

   /**
    * @return the searchStr
    */
   public String getSearchStr () {
      return searchStr;
   }

   /**
    * @param searchStr
    *           the searchStr to set
    */
   public void setSearchStr (String searchStr) {
      this.searchStr = searchStr;
   }

   /**
    * @return the searchStringType
    */
   public String getSearchStringType () {
      return searchStringType;
   }

   /**
    * @param searchStringType
    *           the searchStringType to set
    */
   public void setSearchStringType (String searchStringType) {
      this.searchStringType = searchStringType;
   }

}
