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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.LookupType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IDefaultMetricService;
import com.execue.swi.service.IMappingDeletionService;
import com.execue.swi.service.IMappingManagementService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class DefaultMetricServiceImpl implements IDefaultMetricService {

   private ISDXRetrievalService      sdxRetrievalService;
   private IMappingRetrievalService  mappingRetrievalService;
   private IMappingManagementService mappingManagementService;
   private IMappingDeletionService   mappingDeletionService;
   private ISWIConfigurationService  swiConfigurationService;

   private static final Logger       log = Logger.getLogger(DefaultMetricServiceImpl.class);

   private void correctInvalidDefaultMetrics (Long assetId) throws SWIException {
      List<Tabl> factTables = getSdxRetrievalService().getFactTables(assetId);
      List<Long> factTableIds = new ArrayList<Long>();
      for (Tabl tabl : factTables) {
         factTableIds.add(tabl.getId());
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(factTableIds)) {
         List<DefaultMetric> invalidDefaultMetrics = getMappingRetrievalService()
                  .getInvalidDefaultMetrics(factTableIds);
         if (ExecueCoreUtil.isCollectionNotEmpty(invalidDefaultMetrics)) {
            List<DefaultMetric> validDefaultMetrics = new ArrayList<DefaultMetric>();
            List<DefaultMetric> invalidDefaultMetricsAfterCorrection = new ArrayList<DefaultMetric>();
            for (DefaultMetric invalidDefaultMetric : invalidDefaultMetrics) {
               List<Mapping> mappingsForAED = getMappingRetrievalService().getMappingsForAED(
                        invalidDefaultMetric.getAedId());
               if (ExecueCoreUtil.isCollectionNotEmpty(mappingsForAED)) {
                  Mapping measureMapping = mappingsForAED.get(0);
                  invalidDefaultMetric.setMappingId(measureMapping.getId());
                  invalidDefaultMetric.setPopularity(measureMapping.getBusinessEntityDefinition().getPopularity());
                  invalidDefaultMetric.setValid(CheckType.YES);
                  validDefaultMetrics.add(invalidDefaultMetric);
               } else {
                  invalidDefaultMetricsAfterCorrection.add(invalidDefaultMetric);
               }
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(validDefaultMetrics)) {
               getMappingManagementService().updateDefaultMetrics(validDefaultMetrics);
            }
            // TODO : -VG- to delete the existing invalid default metrics we need to take a call???
            // if (ExecueCoreUtil.isCollectionNotEmpty(invalidDefaultMetrics)) {
            // getMappingService().deleteDefaultMetrics(invalidDefaultMetricsAfterCorrection);
            // }
         }
      }
   }

   public void populateDefaultMetricsAsset (Long assetId) throws SWIException {
      correctInvalidDefaultMetrics(assetId);
      Integer maxDefaultMetricsPerTable = getSwiConfigurationService().getMaxDefaultMetricsPerTable();
      List<Tabl> factTables = getSdxRetrievalService().getFactTablesEligibleForSystemDefaultMetrics(assetId);
      if (ExecueCoreUtil.isCollectionNotEmpty(factTables)) {
         // for each table, delete the existing entries and add new entries to default metric table.
         for (Tabl tabl : factTables) {
            List<DefaultMetric> possibleDefaultMetrics = getMappingRetrievalService().getPossibleDefaultMetrics(
                     assetId, tabl.getId(), maxDefaultMetricsPerTable);
            if (ExecueCoreUtil.isCollectionNotEmpty(possibleDefaultMetrics)) {
               getMappingDeletionService().deleteDefaultMetrics(tabl.getId());
               getMappingManagementService().saveUpdateDefaultMetrics(possibleDefaultMetrics);
            } else {
               log.info("No Default Metrics available for Table [" + tabl.getId() + "] : " + tabl.getName()
                        + " for asset [" + assetId + "]");
            }
         }
      } else {
         log.info("No Facts available for Asset : " + assetId);
      }
   }

   public void handleAssetDefaultMetrics (Asset asset, Tabl tabl, List<Colum> columns, boolean update)
            throws SWIException {
      if (LookupType.None.equals(tabl.getLookupType())) {
         List<DefaultMetric> defaultMetrics = new ArrayList<DefaultMetric>();
         List<Long> aedIds = new ArrayList<Long>();
         for (Colum colum : columns) {
            AssetEntityDefinition assetEntityDefinition = getSdxRetrievalService().getAssetEntityDefinitionByIds(asset,
                     tabl, colum, null);
            aedIds.add(assetEntityDefinition.getId());
            if (CheckType.YES.equals(colum.getDefaultMetric())) {
               DefaultMetric defaultMetric = new DefaultMetric();
               defaultMetric.setTableId(tabl.getId());
               defaultMetric.setAedId(assetEntityDefinition.getId());
               defaultMetric.setValid(CheckType.NO);
               List<Mapping> mappingsForAED = getMappingRetrievalService().getMappingsForAED(
                        assetEntityDefinition.getId());
               if (ExecueCoreUtil.isCollectionNotEmpty(mappingsForAED)) {
                  Mapping measureMapping = mappingsForAED.get(0);
                  defaultMetric.setMappingId(measureMapping.getId());
                  defaultMetric.setPopularity(measureMapping.getBusinessEntityDefinition().getPopularity());
                  defaultMetric.setValid(CheckType.YES);
               }
               defaultMetrics.add(defaultMetric);
            }
         }
         if (update) {
            getMappingManagementService().deleteDefaultMetricsByAedIds(aedIds);
         } else {
            getMappingDeletionService().deleteDefaultMetrics(tabl.getId());
         }
         getMappingManagementService().saveUpdateDefaultMetrics(defaultMetrics);
      }
   }

   public List<DefaultMetric> getValidDefaultMetricsForAsset (Long assetId, Integer maxMetrics) throws SWIException {
      return getMappingRetrievalService().getValidDefaultMetricsForAsset(assetId, maxMetrics);
   }

   public List<DefaultMetric> getAllDefaultMetricsForAsset (Long assetId) throws SWIException {
      return getMappingRetrievalService().getAllDefaultMetricsForAsset(assetId);
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public IMappingManagementService getMappingManagementService () {
      return mappingManagementService;
   }

   public void setMappingManagementService (IMappingManagementService mappingManagementService) {
      this.mappingManagementService = mappingManagementService;
   }

   public IMappingDeletionService getMappingDeletionService () {
      return mappingDeletionService;
   }

   public void setMappingDeletionService (IMappingDeletionService mappingDeletionService) {
      this.mappingDeletionService = mappingDeletionService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

}
