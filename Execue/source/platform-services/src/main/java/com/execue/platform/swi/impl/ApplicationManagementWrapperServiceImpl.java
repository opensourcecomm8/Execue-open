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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.AppDataSource;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.UnstructuredApplicationDetail;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.common.bean.entity.VerticalAppWeight;
import com.execue.core.common.bean.swi.ApplicationScopeIndexDetail;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.CloudOutput;
import com.execue.core.common.type.DataSourceType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.swi.IApplicationManagementWrapperService;
import com.execue.platform.unstructured.IUnstructuredApplicationConnectionManagementService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationManagementService;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IEASIndexService;
import com.execue.swi.service.IKDXCloudManagementService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * @author Vishay
 */
public class ApplicationManagementWrapperServiceImpl implements IApplicationManagementWrapperService {

   private IKDXRetrievalService                                kdxRetrievalService;
   private IBaseKDXRetrievalService                            baseKDXRetrievalService;
   private IKDXManagementService                               kdxManagementService;
   private IApplicationManagementService                       applicationManagementService;
   private ISDXManagementService                               sdxManagementService;
   private IEASIndexService                                    easIndexService;
   private IKDXCloudManagementService                          kdxCloudManagementService;
   private ISDXRetrievalService                                sdxRetrievalService;
   private IUnstructuredApplicationConnectionManagementService unstructuredApplicationConnectionManagementService;
   private IApplicationRetrievalService                        applicationRetrievalService;

   public void createApplicationHierarchy (Application application) throws KDXException {
      // 1. Create App
      // 2. Create Model
      // 3. Create Group
      // 4. Map model to group
      // 5. Map model to base group
      // 6. Map App to model.
      // 7. Create App cloud (default cloud that get associated to the primary model group)
      if (getApplicationRetrievalService().isApplicationExist(application.getName())
               || getKdxRetrievalService().isModelExist(application.getName())
               || getKdxRetrievalService().isCloudExist(application.getName())) {
         throw new KDXException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, "Application with name ["
                  + application.getName() + "] already exists.");
      }
      getApplicationManagementService().createApplication(application);
      Model model = prepareModel(application);
      getKdxManagementService().createModel(model, application);
      ModelGroup modelGroup = prepareModelGroup(model);
      getKdxManagementService().createModelGroup(modelGroup);
      List<ModelGroup> baseAndSystemModelGroups = getBaseKDXRetrievalService().getBaseAndSystemModelGroups();
      for (ModelGroup baseAndSystemModelGroup : baseAndSystemModelGroups) {
         getKdxManagementService().createModelGroupMapping(model, baseAndSystemModelGroup, CheckType.NO, CheckType.NO);
      }
      getKdxManagementService().createModelGroupMapping(model, modelGroup, CheckType.YES, CheckType.YES);

      // create app cloud
      Cloud appCloud = new Cloud();
      appCloud.setName(application.getName());
      Set<Model> models = new HashSet<Model>();
      models.add(model);
      appCloud.setModels(models);
      appCloud.setCategory(CloudCategory.APP_CLOUD);
      appCloud.setCloudOutput(CloudOutput.ENHANCED);
      appCloud.setRequiredComponentCount(0);
      appCloud.setIsDefault(CheckType.YES);
      getKdxCloudManagementService().create(appCloud);

      // Create Vertical app weight
      // TODO:-JT- need to get vertical name from configuration.
      Vertical vertical = kdxRetrievalService.getVerticalByName("Others");
      VerticalAppWeight verticalAppWeight = new VerticalAppWeight();
      verticalAppWeight.setVerticalId(vertical.getId());
      verticalAppWeight.setApplicationId(application.getId());
      verticalAppWeight.setWeight(1.0);
      getKdxManagementService().createVerticalAppWeight(verticalAppWeight);

      ApplicationScopeIndexDetail applicationScopeIndexDetail = new ApplicationScopeIndexDetail();
      applicationScopeIndexDetail.setAppId(application.getId());
      applicationScopeIndexDetail.setLastRefreshDate(new Date());
      try {
         getEasIndexService().createApplicationScopeIndexDetail(applicationScopeIndexDetail);

         if (AppSourceType.UNSTRUCTURED.equals(application.getSourceType())) {
            // create unstructured app detail
            UnstructuredApplicationDetail unstructuredApplicationDetail = application
                     .getUnstructuredApplicationDetail();
            if (unstructuredApplicationDetail != null) {
               unstructuredApplicationDetail.setApplicationId(application.getId());
               getApplicationManagementService().createUnstructuredApplicationDetail(unstructuredApplicationDetail);
            }

            // assign data sources to unstructured application in swi and add to pool of hibernate templates

            Long applicationId = application.getId();
            DataSource whDataSource = getSdxRetrievalService().getLeastLoadedSystemDataSource(
                     DataSourceType.SYSTEM_UNSTRUCTURED_WAREHOUSE);
            DataSource solrDataSource = getSdxRetrievalService().getLeastLoadedSystemDataSource(
                     DataSourceType.SYSTEM_SOLR);
            DataSource cacDataSource = getSdxRetrievalService().getLeastLoadedSystemDataSource(
                     DataSourceType.SYSTEM_UNSTRUCTURED_CONTENT_AGGREGATOR);
            List<String> cacDataSourceNames = new ArrayList<String>();
            cacDataSourceNames.add(cacDataSource.getName());
            //prepare and create app datasource maaping
            List<AppDataSource> appDataSources = new ArrayList<AppDataSource>();
            appDataSources.add(ExecueBeanUtil.buildAppDataSource(applicationId, whDataSource.getId()));
            appDataSources.add(ExecueBeanUtil.buildAppDataSource(applicationId, solrDataSource.getId()));
            appDataSources.add(ExecueBeanUtil.buildAppDataSource(applicationId, cacDataSource.getId()));

            getSdxManagementService().createAppDataSourceMappings(appDataSources);

            //update DataSources pool 
            getUnstructuredApplicationConnectionManagementService().setupPool(applicationId, whDataSource.getName(),
                     solrDataSource.getName(), cacDataSourceNames);

         }
      } catch (SWIException sException) {
         throw new KDXException(sException.getCode(), sException);
      } catch (PlatformException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   @Override
   public void updateApplicationHierarchy (Application application) throws KDXException {
      // 1-update Application
      // 2- update UnstructuredAppDetail for Unstructured Application
      application.setModifiedDate(new Date());
      getApplicationManagementService().updateApplication(application);
      if (AppSourceType.UNSTRUCTURED.equals(application.getSourceType())
               && application.getUnstructuredApplicationDetail() != null) {
         UnstructuredApplicationDetail unstructuredApplicationDetail = application.getUnstructuredApplicationDetail();
         getApplicationManagementService().updateUnstructuredApplicationDetail(unstructuredApplicationDetail);
      }
   }

   private Model prepareModel (Application application) {
      Model model = new Model();
      model.setName(application.getName());
      model.setCreatedDate(new Date());
      model.setStatus(application.getStatus());
      model.setPopularity(application.getPopularity());
      model.setEvaluate(CheckType.YES);
      return model;
   }

   private ModelGroup prepareModelGroup (Model model) {
      ModelGroup modelGroup = new ModelGroup();
      modelGroup.setName(model.getName());
      modelGroup.setContextId(model.getId());
      return modelGroup;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
   }

   public IApplicationManagementService getApplicationManagementService () {
      return applicationManagementService;
   }

   public void setApplicationManagementService (IApplicationManagementService applicationManagementService) {
      this.applicationManagementService = applicationManagementService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

   public IEASIndexService getEasIndexService () {
      return easIndexService;
   }

   public void setEasIndexService (IEASIndexService easIndexService) {
      this.easIndexService = easIndexService;
   }

   public IKDXCloudManagementService getKdxCloudManagementService () {
      return kdxCloudManagementService;
   }

   public void setKdxCloudManagementService (IKDXCloudManagementService kdxCloudManagementService) {
      this.kdxCloudManagementService = kdxCloudManagementService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService
    *           the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
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
