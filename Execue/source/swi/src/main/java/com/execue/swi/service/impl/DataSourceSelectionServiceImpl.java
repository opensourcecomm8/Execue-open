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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.DataSourceType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.dataaccess.ISDXDataAccessManager;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IDataSourceSelectionService;
import com.execue.swi.service.IUserManagementService;

/**
 * Implementation for IDataSourceSelectionService
 * 
 * @author execue
 */
public class DataSourceSelectionServiceImpl implements IDataSourceSelectionService {

   private ISDXDataAccessManager     sdxDataAccessManager;
   private IUserManagementService    userManagementService;
   private ICoreConfigurationService coreConfigurationService;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.DataSourceSelectionService#getDataSourceForCatalog(java.lang.Long)
    */
   public DataSource getDataSourceForCatalogDatasets () throws SWIException {
      // TODO:-VG- need to get the user object as parameter
      Long userId = 1L;
      try {
         DataSource catalogDataSource = null;
         List<DataSource> dataSources = getSdxDataAccessManager().getDataSourcesAssociatedToUserByType(userId,
                  DataSourceType.CATALOG);
         if (ExecueCoreUtil.isCollectionEmpty(dataSources)) {
            catalogDataSource = getSdxDataAccessManager().getLeastLoadedPublicDataSource(DataSourceType.CATALOG);
            User user = userManagementService.getUserById(userId);
            Set<User> users = new HashSet<User>();
            users.add(user);
            catalogDataSource.setUsers(users);
            getSdxDataAccessManager().updateDataSource(catalogDataSource);
         } else {
            catalogDataSource = dataSources.get(0);
         }
         if (catalogDataSource == null) {
            catalogDataSource = getSdxDataAccessManager().getDataSource(
                     getCoreConfigurationService().getDefaultDataSourceNameForCatalogDatasets());
         }
         return catalogDataSource;
      } catch (SDXException e) {
         throw new SWIException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.DataSourceSelectionService#getDataSourceForUploadedDatasets(java.lang.Long)
    */
   public DataSource getDataSourceForUploadedDatasets (Long userId) throws SWIException {
      try {
         DataSource uploadedDataSource = null;
         List<DataSource> dataSources = getSdxDataAccessManager().getDataSourcesAssociatedToUserByType(userId,
                  DataSourceType.UPLOADED);
         if (ExecueCoreUtil.isCollectionEmpty(dataSources)) {
            uploadedDataSource = getSdxDataAccessManager().getLeastLoadedPublicDataSource(DataSourceType.UPLOADED);
            User user = userManagementService.getUserById(userId);
            Set<User> users = new HashSet<User>();
            users.add(user);
            uploadedDataSource.setUsers(users);
            getSdxDataAccessManager().updateDataSource(uploadedDataSource);
         } else {
            uploadedDataSource = dataSources.get(0);
         }
         if (uploadedDataSource == null) {
            uploadedDataSource = getSdxDataAccessManager().getDataSource(
                     getCoreConfigurationService().getDefaultDataSourceNameForUploadedDatasets());
         }
         return uploadedDataSource;
      } catch (SDXException e) {
         throw new SWIException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
   }

   public ISDXDataAccessManager getSdxDataAccessManager () {
      return sdxDataAccessManager;
   }

   public void setSdxDataAccessManager (ISDXDataAccessManager sdxDataAccessManager) {
      this.sdxDataAccessManager = sdxDataAccessManager;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   public IUserManagementService getUserManagementService () {
      return userManagementService;
   }

   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

}
