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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.PageSearch;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.wrapper.AppDashBoardInfo;
import com.execue.core.common.type.AppOperationType;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIPublishedFileInfo;
import com.execue.handler.bean.grid.IGridBean;
import com.execue.handler.bean.grid.UIAppGrid;
import com.execue.handler.swi.IDashBoardServiceHandler;
import com.execue.qdata.service.IJobDataService;
import com.execue.security.UserContextService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPublishedFileRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class DashBoardServiceHandlerImpl extends UserContextService implements IDashBoardServiceHandler {

   private static final Logger            log = Logger.getLogger(DashBoardServiceHandlerImpl.class);

   private IPublishedFileRetrievalService publishedFileRetrievalService;
   private IJobDataService                jobDataService;
   private ISDXRetrievalService           sdxRetrievalService;
   private IApplicationRetrievalService   applicationRetrievalService;
   private IKDXRetrievalService           kdxRetrievalService;

   public List<UIPublishedFileInfo> getPublishedFileInfo (Long applicationId) throws ExeCueException {
      List<PublishedFileInfo> publishedFileInfoList = new ArrayList<PublishedFileInfo>();
      List<UIPublishedFileInfo> uiPublishedFileInfoList = new ArrayList<UIPublishedFileInfo>();
      try {
         publishedFileInfoList = getPublishedFileRetrievalService().getPublishedFileInfoByApplicationId(applicationId);
         if (ExecueCoreUtil.isCollectionNotEmpty(publishedFileInfoList)) {
            for (PublishedFileInfo publishedFileInfo : publishedFileInfoList) {
               Long jobRequestId = publishedFileInfo.getEvaluationJobRequestId();
               if (jobRequestId == null) {
                  jobRequestId = publishedFileInfo.getAbsorbtionJobRequestId();
                  if (jobRequestId != null) {
                     UIPublishedFileInfo uiPublishedFileInfo = new UIPublishedFileInfo();
                     uiPublishedFileInfo.setFileName(publishedFileInfo.getOriginalFileName());
                     JobRequest jobRequest = getJobDataService().getJobRequestById(jobRequestId);
                     uiPublishedFileInfo.setCurrentOperationStatus(jobRequest.getJobStatus());
                     uiPublishedFileInfoList.add(uiPublishedFileInfo);
                  }
               }
            }

         }
      } catch (PublishedFileException publishedFileException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, publishedFileException);
      }
      return uiPublishedFileInfoList;
   }

   public List<DataSource> getDataSources () throws ExeCueException {
      List<DataSource> dataSources = null;
      try {
         if ("ROLE_ADMIN".equalsIgnoreCase(getUserContext().getRole())) {
            dataSources = getSdxRetrievalService().getAllPublicDataSources();
         } else {
            dataSources = getSdxRetrievalService().getDisplayableDataSources(false, getUserId());
         }
      } catch (SDXException sdxException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, sdxException);
      }
      return dataSources;
   }

   public List<Application> getApplications () throws ExeCueException {
      try {
         List<Application> applications = null;
         if ("ROLE_ADMIN".equalsIgnoreCase(getUserContext().getRole())) {
            applications = getApplicationRetrievalService().getAllApplications();
         } else {
            applications = getApplicationRetrievalService().getApplications(getUserId());
         }
         return applications;
      } catch (KDXException kdxException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      }
   }

   public List<Asset> getAssetsByApplicationId (Long applicationId) throws ExeCueException {
      try {

         return getSdxRetrievalService().getAllAssets(applicationId);
      } catch (SDXException sdxException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, sdxException);
      }
   }

   public List<Model> getModelsByApplicationId (Long applicationId) throws ExeCueException {
      try {

         return getKdxRetrievalService().getModelsByApplicationId(applicationId);
      } catch (KDXException kdxException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      }
   }

   public List<Concept> getConceptsByPopularity (Long modelId) throws ExeCueException {
      try {
         return getKdxRetrievalService().getConceptsByPopularity(modelId, 10L);
      } catch (KDXException kdxException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      }
   }

   public List<IGridBean> getApplicationsByPage (Page pageDetails) throws ExeCueException {
      try {
         adjustPageDetail(pageDetails);
         List<AppDashBoardInfo> appDashBoardInfos = getApplicationRetrievalService().getAppDashBoardInfosByPage(
                  pageDetails);
         return transformToUIAppGrid(appDashBoardInfos);
      } catch (KDXException kdxException) {
         throw new ExeCueException(kdxException.getCode(), kdxException);
      }
   }

   private void adjustPageDetail (Page pageDetails) throws ExeCueException {
      try {
         if (!"ROLE_ADMIN".equalsIgnoreCase(getUserContext().getRole())) {
            List<PageSearch> searchList = pageDetails.getSearchList();
            if (ExecueCoreUtil.isCollectionEmpty(searchList)) {
               searchList = new ArrayList<PageSearch>();
               pageDetails.setSearchList(searchList);
            }
            PageSearch pageSearch = new PageSearch();
            pageSearch.setField("userId");
            pageSearch.setType(PageSearchType.EQUALS);
            pageSearch.setString(String.valueOf(getUserId()));
            searchList.add(pageSearch);
         }
      } catch (Exception exception) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
   }

   public List<IGridBean> getApplicationsByPage (Page pageDetails, boolean advancedMenu) throws ExeCueException {
      try {
         adjustPageDetail(pageDetails);
         List<AppDashBoardInfo> appDashBoardInfos = getApplicationRetrievalService().getAppDashBoardInfosByPage(
                  pageDetails, advancedMenu);
         return transformToUIAppGrid(appDashBoardInfos);
      } catch (KDXException kdxException) {
         throw new ExeCueException(kdxException.getCode(), kdxException);
      }
   }

   private List<IGridBean> transformToUIAppGrid (List<AppDashBoardInfo> appDashBoardInfos) throws KDXException {
      List<IGridBean> gridBeans = new ArrayList<IGridBean>();
      if (ExecueCoreUtil.isCollectionEmpty(appDashBoardInfos)) {
         return gridBeans;
      }
      for (AppDashBoardInfo appDashBoardInfo : appDashBoardInfos) {
         UIAppGrid gridBean = new UIAppGrid();

         gridBean.setId(appDashBoardInfo.getAppId());
         gridBean.setName(appDashBoardInfo.getAppName());
         gridBean.setDesc(appDashBoardInfo.getAppDescription());
         gridBean.setMode(appDashBoardInfo.getPublishMode().getDescription());
         gridBean.setStatus(appDashBoardInfo.getAppStatus().getDescription());
         gridBean.setCreationType(appDashBoardInfo.getCreationType().getValue());
         gridBean.setAppSourceType(appDashBoardInfo.getAppSourceType().getValue());
         gridBean.setModelId(appDashBoardInfo.getModelId());

         int assetCount = appDashBoardInfo.getAssetCount();

         gridBean.setOperationType(appDashBoardInfo.getOperationType().getDescription());
         gridBean.setJobRequestId(appDashBoardInfo.getJobRequestId());

         if (AppOperationType.DELETING.getDescription().equals(gridBean.getOperationType())
                  || AppOperationType.PUBLISHING.getDescription().equals(gridBean.getOperationType())) {
            gridBean.setInProgress(true);
            gridBean.setStatusLink(true);
            gridBean.setMetadataLink(false);
            gridBean.setStatus(gridBean.getOperationType());
         } else if (assetCount <= 1) {
            if (AppOperationType.ANALYZING.getDescription().equals(gridBean.getOperationType())
                     || AppOperationType.FULFILLING.getDescription().equals(gridBean.getOperationType())) {
               gridBean.setInProgress(true);
               gridBean.setStatusLink(true);
               gridBean.setMetadataLink(false);
               gridBean.setStatus(gridBean.getOperationType());
            } else if (AppOperationType.ANALYZED.getDescription().equals(gridBean.getOperationType())) {
               gridBean.setStatus(gridBean.getOperationType());
            }
         }
         gridBeans.add(gridBean);
      }
      return gridBeans;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
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

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

   private Long getUserId () {
      return getUserContext().getUser().getId();
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }
}