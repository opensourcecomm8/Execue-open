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

import com.execue.core.common.bean.entity.AppDataSource;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.DataSourceType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.swi.IUnstructuredAppDataSourceServiceHandler;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.unstructured.IUnstructuredApplicationConnectionManagementService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

public class UnstructuredAppDataSourceServiceHandlerImpl implements IUnstructuredAppDataSourceServiceHandler {

   private ISDXRetrievalService                                sdxRetrievalService;
   private ISDXDeletionService                                 sdxDeletionService;
   private ISDXManagementService                               sdxManagementService;
   private IApplicationRetrievalService                        applicationRetrievalService;
   private IUnstructuredApplicationConnectionManagementService unstructuredApplicationConnectionManagementService;

   // Methods

   public List<Application> getUnstructuredApplications () throws HandlerException {
      List<Application> applications = null;
      try {
         applications = getApplicationRetrievalService().getApplicationsByType(AppSourceType.UNSTRUCTURED);
         if (ExecueCoreUtil.isCollectionEmpty(applications)) {
            applications = new ArrayList<Application>();
         }
      } catch (KDXException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
      return applications;
   }

   public DataSource UnstructuredDataSourceByAppId (Long applicationId) throws HandlerException {
      DataSource dataSource = null;
      try {
         dataSource = getSdxRetrievalService().getUnstructuredWHDataSourceByAppId(applicationId);
         if (dataSource == null) {
            dataSource = new DataSource();
         }
      } catch (SDXException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
      return dataSource;

   }

   public List<DataSource> getContentAggregatorDataSources () throws HandlerException {
      List<DataSource> dataSources = null;
      try {
         dataSources = getSdxRetrievalService().getDataSourcesByType(
                  DataSourceType.SYSTEM_UNSTRUCTURED_CONTENT_AGGREGATOR);
         if (ExecueCoreUtil.isCollectionEmpty(dataSources)) {
            dataSources = new ArrayList<DataSource>();
         }
      } catch (SDXException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
      return dataSources;
   }

   public List<DataSource> getUnstructureDataSources () throws HandlerException {
      List<DataSource> dataSources = null;
      try {
         dataSources = getSdxRetrievalService().getDataSourcesByType(DataSourceType.SYSTEM_UNSTRUCTURED_WAREHOUSE);
         if (ExecueCoreUtil.isCollectionEmpty(dataSources)) {
            dataSources = new ArrayList<DataSource>();
         }
      } catch (SDXException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
      return dataSources;
   }

   public List<DataSource> getUnstructureSOLRDataSources () throws HandlerException {
      List<DataSource> dataSources = null;
      try {
         dataSources = getSdxRetrievalService().getDataSourcesByType(DataSourceType.SYSTEM_SOLR);
         if (ExecueCoreUtil.isCollectionEmpty(dataSources)) {
            dataSources = new ArrayList<DataSource>();
         }
      } catch (SDXException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
      return dataSources;
   }

   public List<DataSource> getContentAggregatorDataSourcesByAppId (Long applicationId) throws HandlerException {
      List<DataSource> dataSources = null;
      try {
         dataSources = getSdxRetrievalService().getContentAggregatorDataSourcesByAppId(applicationId);
         if (ExecueCoreUtil.isCollectionEmpty(dataSources)) {
            dataSources = new ArrayList<DataSource>();
         }
      } catch (SDXException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
      return dataSources;

   }

   public void saveUnstructuredContentAggregators (Long applicationId, Long selectedUnstructuredDataSourceForApp,
            Long selectedUnstructuredSOLRDataSourceForApp, List<Long> selectedUnstructuredContentAggregatorsForApp)
            throws HandlerException {
      try {
         List<AppDataSource> appDataSources = new ArrayList<AppDataSource>();
         appDataSources.add(ExecueBeanManagementUtil.buildAppDataSource(applicationId,
                  selectedUnstructuredDataSourceForApp));
         appDataSources.add(ExecueBeanManagementUtil.buildAppDataSource(applicationId,
                  selectedUnstructuredSOLRDataSourceForApp));
         for (Long selectedUnstructuredContentAggregatorForApp : selectedUnstructuredContentAggregatorsForApp) {
            appDataSources.add(ExecueBeanManagementUtil.buildAppDataSource(applicationId,
                     selectedUnstructuredContentAggregatorForApp));
         }
         //update datasource pool
         getUnstructuredApplicationConnectionManagementService().updatePool(applicationId,
                  selectedUnstructuredDataSourceForApp, selectedUnstructuredSOLRDataSourceForApp,
                  selectedUnstructuredContentAggregatorsForApp);
         // delete and create App data source mapping
         getSdxDeletionService().deleteAppDataSourceMappings(applicationId);
         getSdxManagementService().createAppDataSourceMappings(appDataSources);
      } catch (SDXException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   @Override
   public DataSource getUnstructuredWHSolrDataSourceByAppId (Long appId) throws HandlerException {
      try {
         return getSdxRetrievalService().getSolrDataSourceByAppId(appId);
      } catch (SDXException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
   }

   /**
    * @return the sdxRetrievalService
    */
   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   /**
    * @param sdxRetrievalService
    *           the sdxRetrievalService to set
    */
   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public ISDXDeletionService getSdxDeletionService () {
      return sdxDeletionService;
   }

   public void setSdxDeletionService (ISDXDeletionService sdxDeletionService) {
      this.sdxDeletionService = sdxDeletionService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
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
