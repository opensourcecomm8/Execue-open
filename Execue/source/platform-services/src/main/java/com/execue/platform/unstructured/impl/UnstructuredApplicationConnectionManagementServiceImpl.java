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


package com.execue.platform.unstructured.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.DataSource;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.unstructured.IUnstructuredApplicationConnectionManagementService;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.usca.service.IUnstructuredCAConnectionMgmtService;
import com.execue.uswh.service.IUnstructuredWHConnectionMgmtService;
import com.execue.uswh.service.IUnstructuredWHSolrConnectionMgmtService;

public class UnstructuredApplicationConnectionManagementServiceImpl implements
         IUnstructuredApplicationConnectionManagementService {

   private IUnstructuredWHSolrConnectionMgmtService unstructuredWHSolrConnectionMgmtService;
   private IUnstructuredWHConnectionMgmtService     unstructuredWHConnectionMgmtService;
   private IUnstructuredCAConnectionMgmtService     unstructuredCAConnectionMgmtService;
   private ISDXRetrievalService                     sdxRetrievalService;

   @Override
   public void setupPool (Long applicationId, String whDataSourceName, String solrDataSourceName,
            List<String> cacDataSourceNames) throws PlatformException {
      try {
         //Add WH datasource into pool
         getUnstructuredWHConnectionMgmtService().setupPool(applicationId, whDataSourceName);
         //Add SOLR datasource into pool
         getUnstructuredWHSolrConnectionMgmtService().setupPool(applicationId, solrDataSourceName);
         //Add CAC datasource into pool
         getUnstructuredCAConnectionMgmtService().setupPool(applicationId, cacDataSourceNames);

      } catch (DataAccessException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public void updatePool (Long applicationId, Long updatedWHDataSourceId, Long updatedSolrDataSourceId,
            List<Long> updatedCACDataSourceIds) throws PlatformException {
      try {
         //update WH datasource pool
         DataSource existingWhDataSource = getSdxRetrievalService().getUnstructuredWHDataSourceByAppId(applicationId);
         if (!existingWhDataSource.getId().equals(updatedWHDataSourceId)) {
            DataSource updatedDataSource = getSdxRetrievalService().getDataSourceById(updatedWHDataSourceId);
            getUnstructuredWHConnectionMgmtService().updatePool(applicationId, updatedDataSource.getName());
         }
         //update SOLR datasource pool
         DataSource existingSolrDataSource = getSdxRetrievalService().getSolrDataSourceByAppId(applicationId);
         if (!existingSolrDataSource.getId().equals(updatedSolrDataSourceId)) {
            DataSource updatedSolrDataSource = getSdxRetrievalService().getDataSourceById(updatedSolrDataSourceId);
            getUnstructuredWHSolrConnectionMgmtService().updatePool(applicationId, updatedSolrDataSource.getName());
         }
         // we can refresh the pool every time for content aggregators
         List<String> updatedContentAggregatorDataSourceNames = new ArrayList<String>();
         for (Long unstructuredContentAggregatorId : updatedCACDataSourceIds) {
            updatedContentAggregatorDataSourceNames.add(getSdxRetrievalService().getDataSourceById(
                     unstructuredContentAggregatorId).getName());
         }
         getUnstructuredCAConnectionMgmtService().updatePool(applicationId, updatedContentAggregatorDataSourceNames);

      } catch (SDXException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (DataAccessException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public void deletePool (Long applicationId) throws PlatformException {
      try {
         //Clean WH datasource pool
         getUnstructuredWHConnectionMgmtService().deletePool(applicationId);
         //Clean SOLR datasource pool
         getUnstructuredWHSolrConnectionMgmtService().deletePool(applicationId);
         //Clean CAC datasource pool
         getUnstructuredCAConnectionMgmtService().deletePool(applicationId);
      } catch (DataAccessException e) {
         throw new PlatformException(e.getCode(), e);
      }

   }

   /**
    * @return the unstructuredWHSolrConnectionMgmtService
    */
   public IUnstructuredWHSolrConnectionMgmtService getUnstructuredWHSolrConnectionMgmtService () {
      return unstructuredWHSolrConnectionMgmtService;
   }

   /**
    * @param unstructuredWHSolrConnectionMgmtService the unstructuredWHSolrConnectionMgmtService to set
    */
   public void setUnstructuredWHSolrConnectionMgmtService (
            IUnstructuredWHSolrConnectionMgmtService unstructuredWHSolrConnectionMgmtService) {
      this.unstructuredWHSolrConnectionMgmtService = unstructuredWHSolrConnectionMgmtService;
   }

   /**
    * @return the unstructuredWHConnectionMgmtService
    */
   public IUnstructuredWHConnectionMgmtService getUnstructuredWHConnectionMgmtService () {
      return unstructuredWHConnectionMgmtService;
   }

   /**
    * @param unstructuredWHConnectionMgmtService the unstructuredWHConnectionMgmtService to set
    */
   public void setUnstructuredWHConnectionMgmtService (
            IUnstructuredWHConnectionMgmtService unstructuredWHConnectionMgmtService) {
      this.unstructuredWHConnectionMgmtService = unstructuredWHConnectionMgmtService;
   }

   /**
    * @return the unstructuredCAConnectionMgmtService
    */
   public IUnstructuredCAConnectionMgmtService getUnstructuredCAConnectionMgmtService () {
      return unstructuredCAConnectionMgmtService;
   }

   /**
    * @param unstructuredCAConnectionMgmtService the unstructuredCAConnectionMgmtService to set
    */
   public void setUnstructuredCAConnectionMgmtService (
            IUnstructuredCAConnectionMgmtService unstructuredCAConnectionMgmtService) {
      this.unstructuredCAConnectionMgmtService = unstructuredCAConnectionMgmtService;
   }

   /**
    * @return the sdxRetrievalService
    */
   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   /**
    * @param sdxRetrievalService the sdxRetrievalService to set
    */
   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

}
