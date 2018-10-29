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


package com.execue.uswhda.configuration.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.execue.core.common.bean.DataSourceConfigurationProperties;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.DataSourceType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.IHibernateTemplateBuilderService;
import com.execue.dataaccess.configuration.IDataAccessConfigurationService;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.dataaccess.swi.dao.IAppDataSourceDAO;
import com.execue.dataaccess.swi.dao.IApplicationDAO;
import com.execue.dataaccess.swi.dao.IAssetEntityDefinitionDAO;
import com.execue.dataaccess.swi.dao.IDataSourceDAO;
import com.execue.uswhda.configuration.IUnstructuredWHDataAccessConfigurationService;

/**
 * @author Vishay
 */
public class UnstructuredWHPooledDataManager {

   private IAppDataSourceDAO                             appDataSourceDAO;
   private IAssetEntityDefinitionDAO                     assetEntityDefinitionDAO;
   private IDataAccessConfigurationService               dataAccessConfigurationService;
   private IUnstructuredWHDataAccessConfigurationService unstructuredWHDataAccessConfigurationService;
   private IHibernateTemplateBuilderService              hibernateTemplateBuilderService;
   private IDataSourceDAO                                dataSourceDAO;
   private IApplicationDAO                               applicationDAO;
   // two way maps are maintained not to duplicate hibernate template for multiple apps sharing the same datasource.
   private Map<Long, String>                             unstructuredAppWHDataSourceNameMap           = new HashMap<Long, String>();
   private Map<String, HibernateTemplate>                unstructuredWHDataSourceHibernateTemplateMap = new HashMap<String, HibernateTemplate>();

   private static final Logger                           logger                                       = Logger
                                                                                                               .getLogger(UnstructuredWHPooledDataManager.class);

   public HibernateTemplate getHibernateTemplate (Long applicationId) throws DataAccessException {
      String dataSourceName = unstructuredAppWHDataSourceNameMap.get(applicationId);
      return unstructuredWHDataSourceHibernateTemplateMap.get(dataSourceName);
   }

   public void updatePool (Long appId, String updatedDataSourceName) throws DataAccessException {
      synchronized (unstructuredAppWHDataSourceNameMap) {
         unstructuredAppWHDataSourceNameMap.put(appId, updatedDataSourceName);
      }
   }

   public void setupPool (Long appId, String dataSourceName) throws DataAccessException {
      synchronized (unstructuredAppWHDataSourceNameMap) {
         unstructuredAppWHDataSourceNameMap.put(appId, dataSourceName);
      }
   }

   public void cleanUpPool (Long appId) throws DataAccessException {
      synchronized (unstructuredAppWHDataSourceNameMap) {
         unstructuredAppWHDataSourceNameMap.remove(appId);
      }
   }

   public synchronized void setupDataSources () throws DataAccessException {
      try {
         DataSourceConfigurationProperties dataSourceConfigurationProperties = getUnstructuredWHDataAccessConfigurationService()
                  .getDataSourceConfigurationProperties();
         List<Application> unstructuredApplications = getApplicationDAO().getApplicationsByType(
                  AppSourceType.UNSTRUCTURED);
         if (ExecueCoreUtil.isCollectionNotEmpty(unstructuredApplications)) {
            List<DataSource> unstructuredWHDataSources = getDataSourceDAO().getDataSourcesByType(
                     DataSourceType.SYSTEM_UNSTRUCTURED_WAREHOUSE);
            if (ExecueCoreUtil.isCollectionNotEmpty(unstructuredWHDataSources)) {
               for (DataSource dataSource : unstructuredWHDataSources) {
                  HibernateTemplate hibernateTemplate = getHibernateTemplateBuilderService().buildHibernateTemplate(
                           dataSource, dataSourceConfigurationProperties);
                  unstructuredWHDataSourceHibernateTemplateMap.put(dataSource.getName(), hibernateTemplate);
               }
            }
            for (Application application : unstructuredApplications) {
               DataSource unstructuredWHDataSource = getAppDataSourceDAO().getUnstructuredWHDataSourceByAppId(
                        application.getId());
               if (unstructuredWHDataSource != null) {
                  unstructuredAppWHDataSourceNameMap.put(application.getId(), unstructuredWHDataSource.getName());
               } else {
                  logger.error("For Unstructured App Id " + application.getId()
                           + " ,Warehouse Data Source is not defined");
               }
            }

         }
      } catch (Exception e) {
         throw new DataAccessException(
                  DataAccessExceptionCodes.UNSTRUCTURED_APPLICATIONS_WAREHOUSE_CONFIGURATION_FAILED, e);
      }
   }

   public IAppDataSourceDAO getAppDataSourceDAO () {
      return appDataSourceDAO;
   }

   public void setAppDataSourceDAO (IAppDataSourceDAO appDataSourceDAO) {
      this.appDataSourceDAO = appDataSourceDAO;
   }

   public IAssetEntityDefinitionDAO getAssetEntityDefinitionDAO () {
      return assetEntityDefinitionDAO;
   }

   public void setAssetEntityDefinitionDAO (IAssetEntityDefinitionDAO assetEntityDefinitionDAO) {
      this.assetEntityDefinitionDAO = assetEntityDefinitionDAO;
   }

   /**
    * @return the dataAccessConfigurationService
    */
   public IDataAccessConfigurationService getDataAccessConfigurationService () {
      return dataAccessConfigurationService;
   }

   /**
    * @param dataAccessConfigurationService
    *           the dataAccessConfigurationService to set
    */
   public void setDataAccessConfigurationService (IDataAccessConfigurationService dataAccessConfigurationService) {
      this.dataAccessConfigurationService = dataAccessConfigurationService;
   }

   /**
    * @param conextDataSourceMap
    *           the conextDataSourceMap to set
    */
   public void setUnstructuredAppWHDataSourceNameMap (Map<Long, String> unstructuredAppWHDataSourceNameMap) {
      this.unstructuredAppWHDataSourceNameMap = unstructuredAppWHDataSourceNameMap;
   }

   /**
    * @return the unstructuredWHDataAccessConfigurationService
    */
   public IUnstructuredWHDataAccessConfigurationService getUnstructuredWHDataAccessConfigurationService () {
      return unstructuredWHDataAccessConfigurationService;
   }

   /**
    * @param unstructuredWHDataAccessConfigurationService
    *           the unstructuredWHDataAccessConfigurationService to set
    */
   public void setUnstructuredWHDataAccessConfigurationService (
            IUnstructuredWHDataAccessConfigurationService unstructuredWHDataAccessConfigurationService) {
      this.unstructuredWHDataAccessConfigurationService = unstructuredWHDataAccessConfigurationService;
   }

   public IDataSourceDAO getDataSourceDAO () {
      return dataSourceDAO;
   }

   public void setDataSourceDAO (IDataSourceDAO dataSourceDAO) {
      this.dataSourceDAO = dataSourceDAO;
   }

   public IHibernateTemplateBuilderService getHibernateTemplateBuilderService () {
      return hibernateTemplateBuilderService;
   }

   public void setHibernateTemplateBuilderService (IHibernateTemplateBuilderService hibernateTemplateBuilderService) {
      this.hibernateTemplateBuilderService = hibernateTemplateBuilderService;
   }

   /**
    * @return the applicationDAO
    */
   public IApplicationDAO getApplicationDAO () {
      return applicationDAO;
   }

   /**
    * @param applicationDAO the applicationDAO to set
    */
   public void setApplicationDAO (IApplicationDAO applicationDAO) {
      this.applicationDAO = applicationDAO;
   }

}
