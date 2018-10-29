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


package com.execue.platform.querydata.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Seed;
import com.execue.core.common.bean.qdata.QDataAggregatedQuery;
import com.execue.core.common.bean.qdata.QDataAggregatedReportType;
import com.execue.core.common.bean.qdata.QDataBusinessQuery;
import com.execue.core.common.bean.qdata.QDataUserQuery;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.platform.querydata.IQueryDataPlatformRetrievalService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.service.IQueryDataService;
import com.execue.qdata.service.IUDXService;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.ISDXRetrievalService;

public class QueryDataPlatformRetrievalServiceImpl implements IQueryDataPlatformRetrievalService {

   private IQueryDataService    queryDataService;
   private ISDXRetrievalService sdxRetrievalService;
   private IUDXService          udxService;

   public QDataAggregatedQuery getLoadedAggregatedQueryById (Long aggregateQueryId) throws QueryDataException {
      try {
         QDataAggregatedQuery dataAggregatedQuery = getQueryDataService().getQdataAggregatedQueryById(aggregateQueryId);
         Set<QDataAggregatedReportType> reportTypeSet = dataAggregatedQuery.getReportTypes();
         if (reportTypeSet != null) {
            for (QDataAggregatedReportType dataAggregatedReportType : reportTypeSet) {
               dataAggregatedReportType.getId();
            }
         }
         QDataBusinessQuery businessQuery = dataAggregatedQuery.getBusinessQuery();
         businessQuery.getApplicationName();
         Asset asset = getSdxRetrievalService().getAssetById(dataAggregatedQuery.getAssetId());
         asset.getDataSource().getName();
         asset.getApplication().getName();
         dataAggregatedQuery.setAsset(asset);

         dataAggregatedQuery.getUserQuery().getNormalizedQueryString();

         return dataAggregatedQuery;
      } catch (SDXException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<QDataUserQuery> getMatchUseryQueries (String userQuery) throws QueryDataException {
      try {
         List<QDataUserQuery> userQueryList = null;
         try {
            userQueryList = getQueryDataService().getUserQuerysByName(userQuery);
         } catch (QueryDataException qde) {
            // return empty list
            userQueryList = new ArrayList<QDataUserQuery>();
         }
         for (QDataUserQuery dataUserQuery : userQueryList) {
            Set<QDataAggregatedQuery> aggQuerySet = dataUserQuery.getAggregatedQueries();
            if (aggQuerySet != null) {
               for (QDataAggregatedQuery dataAggregatedQuery : aggQuerySet) {
                  Asset asset = getSdxRetrievalService().getAssetById(dataAggregatedQuery.getAssetId());
                  asset.getDataSource().getName();
                  asset.getApplication().getName();
                  dataAggregatedQuery.setAsset(asset);
                  // populate report types
                  Set<QDataAggregatedReportType> reportTypeSet = dataAggregatedQuery.getReportTypes();
                  if (reportTypeSet != null) {
                     for (QDataAggregatedReportType dataAggregatedReportType : reportTypeSet) {
                        dataAggregatedReportType.getId();
                     }
                  }
               }
            }
         }
         return userQueryList;
      } catch (Exception e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   @Override
   public Integer getNextSeedValueByTypeAndNodeId (Long nodeId, String type) throws QueryDataException {
      try {
         Seed dbSeed = getUdxService().getSeedByNodeAndType(nodeId, type);
         int value = dbSeed.getNextValue();
         dbSeed.setNextValue(value + 1);
         getUdxService().updateSeed(dbSeed);
         return value;
      } catch (UDXException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public IUDXService getUdxService () {
      return udxService;
   }

   public void setUdxService (IUDXService udxService) {
      this.udxService = udxService;
   }
}
