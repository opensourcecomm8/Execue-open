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


package com.execue.platform.unstructured.content.transporter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.das.jdbc.service.IGenericJDBCService;
import com.execue.platform.exception.UnstructuredContentTransporterException;
import com.execue.platform.helper.UnstructuredContentTransporterHelper;
import com.execue.platform.unstructured.content.transporter.IUnstructuredAppBasedContentTransporter;
import com.execue.platform.unstructured.content.transporter.IUnstructuredTargetBasedContentTransporter;
import com.execue.platform.unstructured.content.transporter.bean.UnstructuredContentTransporterContext;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.service.IUnstructuredWarehouseManagementService;

public class UnstructuredTargetBasedContentTransporterImpl implements IUnstructuredTargetBasedContentTransporter {

   private IUnstructuredAppBasedContentTransporter unstructuredAppBasedContentTransporter;
   private UnstructuredContentTransporterHelper    unstructuredContentTransporterHelper;
   private ISDXRetrievalService                    sdxRetrievalService;
   private IGenericJDBCService                     genericJDBCService;
   private IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService;

   public IGenericJDBCService getGenericJDBCService () {
      return genericJDBCService;
   }

   public void setGenericJDBCService (IGenericJDBCService genericJDBCService) {
      this.genericJDBCService = genericJDBCService;
   }

   public IUnstructuredAppBasedContentTransporter getUnstructuredAppBasedContentTransporter () {
      return unstructuredAppBasedContentTransporter;
   }

   public void setUnstructuredAppBasedContentTransporter (
            IUnstructuredAppBasedContentTransporter unstructuredAppBasedContentTransporter) {
      this.unstructuredAppBasedContentTransporter = unstructuredAppBasedContentTransporter;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.transportation.service.IUnstructuredTargetBasedContentTransporter#transportContent(java.util.List)
    */
   public void transportContent (List<Long> targetWareHouseDataSourceIds)
            throws UnstructuredContentTransporterException {

      try {
         for (Long warehouseDataSourceId : targetWareHouseDataSourceIds) {

            DataSource targetWarehouseDataSource = getSdxRetrievalService().getDataSourceById(warehouseDataSourceId);
            List<Long> applicationIds = getSdxRetrievalService().getApplicationIdsConfiguredForUSWH(
                     targetWarehouseDataSource.getId());
            // build the map of application id and list of source content transporter contexts
            Map<Long, List<UnstructuredContentTransporterContext>> appContentTransporterContextsMap = new HashMap<Long, List<UnstructuredContentTransporterContext>>();
            List<DataSource> sourceContentDataSources = null;
            for (Long applicationId : applicationIds) {
               List<UnstructuredContentTransporterContext> contentTransporterContexts = new ArrayList<UnstructuredContentTransporterContext>();
               sourceContentDataSources = getSdxRetrievalService()
                        .getContentAggregatorDataSourcesByAppId(applicationId);
               for (DataSource sourceContentDataSource : sourceContentDataSources) {
                  UnstructuredContentTransporterContext unstructuredContentTransporterContext = new UnstructuredContentTransporterContext();

                  unstructuredContentTransporterContext.setTargetWareHouseDataSource(targetWarehouseDataSource);
                  unstructuredContentTransporterContext.setSourceContentDataSource(sourceContentDataSource);
                  unstructuredContentTransporterContext
                           .setContentTempTableName(getUnstructuredContentTransporterHelper()
                                    .constructSourceContentTempTableName(targetWarehouseDataSource.getId(),
                                             applicationId, sourceContentDataSource.getId()));
                  contentTransporterContexts.add(unstructuredContentTransporterContext);
               }
               appContentTransporterContextsMap.put(applicationId, contentTransporterContexts);
            }

            // perform transport per each app
            for (Entry<Long, List<UnstructuredContentTransporterContext>> appTransporterContexts : appContentTransporterContextsMap
                     .entrySet()) {
               getUnstructuredAppBasedContentTransporter().transportContent(appTransporterContexts.getKey(),
                        appTransporterContexts.getValue());
            }

            // Perform transport to main table from temp table and then drop the temp table
            for (Entry<Long, List<UnstructuredContentTransporterContext>> appTransporterContexts : appContentTransporterContextsMap
                     .entrySet()) {
               transportContentFromTempToSourceContentTable(appTransporterContexts.getKey(), appTransporterContexts
                        .getValue());
            }
         }
      } catch (SDXException sdxException) {
         throw new UnstructuredContentTransporterException(sdxException.getCode(), sdxException);
      } catch (UnstructuredWarehouseException e) {
         throw new UnstructuredContentTransporterException(e.getCode(), e);
      }
   }

   private void transportContentFromTempToSourceContentTable (Long contextId,
            List<UnstructuredContentTransporterContext> unstructuredContentTransporterContexts)
            throws UnstructuredWarehouseException, SDXException {
      DataSource dataSource = getSdxRetrievalService().getUnstructuredWHDataSourceByAppId(contextId);
      for (UnstructuredContentTransporterContext unstructuredContentTransporterContext : unstructuredContentTransporterContexts) {
         // Transport content to main table from temp table
         QueryTable sourceContentTempQueryTable = ExecueBeanManagementUtil.prepareQueryTable(
                  unstructuredContentTransporterContext.getContentTempTableName(), dataSource.getOwner());
         getUnstructuredWarehouseManagementService().populateSourceContentTable(contextId, sourceContentTempQueryTable);
         // Drop the temp table
         String ddlTempTableDropStatement = getUnstructuredContentTransporterHelper().getTempTableDropStatement(
                  dataSource.getProviderType(), sourceContentTempQueryTable);
         getUnstructuredWarehouseManagementService().dropSourceContentTempTable(contextId, ddlTempTableDropStatement);
      }
   }

   public IUnstructuredWarehouseManagementService getUnstructuredWarehouseManagementService () {
      return unstructuredWarehouseManagementService;
   }

   public void setUnstructuredWarehouseManagementService (
            IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService) {
      this.unstructuredWarehouseManagementService = unstructuredWarehouseManagementService;
   }

   public UnstructuredContentTransporterHelper getUnstructuredContentTransporterHelper () {
      return unstructuredContentTransporterHelper;
   }

   public void setUnstructuredContentTransporterHelper (
            UnstructuredContentTransporterHelper unstructuredContentTransporterHelper) {
      this.unstructuredContentTransporterHelper = unstructuredContentTransporterHelper;
   }
}
