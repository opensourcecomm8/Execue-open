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
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.helper.PublishedFileJDBCHelper;
import com.execue.platform.swi.ISWIPlatformRetrievalService;
import com.execue.swi.configuration.ILocationConfigurationService;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPreferencesRetrievalService;
import com.execue.swi.service.IPublishedFileRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class SWIPlatformRetrievalServiceImpl implements ISWIPlatformRetrievalService {

   private PublishedFileJDBCHelper        publishedFileJDBCHelper;
   private IPublishedFileRetrievalService publishedFileRetrievalService;
   private ISDXRetrievalService           sdxRetrievalService;
   private IPreferencesRetrievalService   preferencesRetrievalService;
   private IKDXRetrievalService           kdxRetrievalService;
   private IBaseKDXRetrievalService       baseKDXRetrievalService;
   private ILocationConfigurationService  locationConfigurationService;

   public List<List<String>> getUploadedFileDataFromSource (Long fileId, Long tableId, List<String> columnNames,
            LimitEntity limitEntity, Page page) throws SWIException {
      List<List<String>> dataRows = new ArrayList<List<String>>();
      try {
         // get table by fileTableId
         PublishedFileTableInfo publishedFileTableInfo = getPublishedFileRetrievalService()
                  .getPublishedFileTableInfoById(tableId);
         // get datasource
         DataSource dataSource = getSdxRetrievalService().getDataSourceById(
                  getPublishedFileRetrievalService().getPublishedFileInfoById(fileId).getDatasourceId());
         String tempTableName = publishedFileTableInfo.getTempTableName();
         QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tempTableName);
         getPublishedFileJDBCHelper().fillDataRows(dataRows, dataSource, queryTable, columnNames, limitEntity, page);

      } catch (SDXException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
      return dataRows;
   }

   public List<List<String>> getAssetTableDataFromSourceByPage (Long assetId, Long tableId,
            List<String> requestedColumnNames, LimitEntity limitEntity, Page page) throws SWIException {
      List<List<String>> dataRows = new ArrayList<List<String>>();
      Tabl table = getSdxRetrievalService().getTableById(tableId);
      DataSource dataSource = getSdxRetrievalService().getDataSourceByAssetId(assetId);
      String tableName = table.getActualName();
      if (StringUtils.isEmpty(tableName)) {
         tableName = table.getName();
      }
      try {
         QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(table);
         getPublishedFileJDBCHelper().fillDataRows(dataRows, dataSource, queryTable, requestedColumnNames, limitEntity,
                  page);
         // TODO : -RG- need proper error handling
      } catch (Exception e) {
         e.printStackTrace();
      }
      return dataRows;
   }

   @Override
   public boolean hasLocationBasedEntities (Long applicationId) throws SWIException {
      Long entityCount = null;
      BusinessEntityDefinition locationTypeBed = getBaseKDXRetrievalService().getTypeBEDByName(
               ExecueConstants.LOCATION_TYPE);
      List<Long> locationChildrenBedIds = getLocationConfigurationService().getChildBedIdsByParentBedId(
               locationTypeBed.getId());
      List<Model> models = getKdxRetrievalService().getModelsByApplicationId(applicationId);
      if (ExecueCoreUtil.isCollectionNotEmpty(models)) {
         Long modelId = models.get(0).getId();
         entityCount = getKdxRetrievalService().getEntityCountWithTypeBedIds(modelId, locationChildrenBedIds);
      }
      if (entityCount > 0) {
         return true;
      }
      return false;
   }

   public PublishedFileJDBCHelper getPublishedFileJDBCHelper () {
      return publishedFileJDBCHelper;
   }

   public void setPublishedFileJDBCHelper (PublishedFileJDBCHelper publishedFileJDBCHelper) {
      this.publishedFileJDBCHelper = publishedFileJDBCHelper;
   }

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return preferencesRetrievalService;
   }

   public void setPreferencesRetrievalService (IPreferencesRetrievalService preferencesRetrievalService) {
      this.preferencesRetrievalService = preferencesRetrievalService;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the baseKDXRetrievalService
    */
   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   /**
    * @param baseKDXRetrievalService the baseKDXRetrievalService to set
    */
   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   /**
    * @return the locationConfigurationService
    */
   public ILocationConfigurationService getLocationConfigurationService () {
      return locationConfigurationService;
   }

   /**
    * @param locationConfigurationService the locationConfigurationService to set
    */
   public void setLocationConfigurationService (ILocationConfigurationService locationConfigurationService) {
      this.locationConfigurationService = locationConfigurationService;
   }

}
