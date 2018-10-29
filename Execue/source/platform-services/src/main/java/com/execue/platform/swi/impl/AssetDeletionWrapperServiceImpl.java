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
import java.util.List;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.swi.AssetDeletionContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.das.dataaccess.ISystemDataAccessService;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.platform.security.ISecurityDefinitionWrapperService;
import com.execue.platform.swi.IAssetDeletionWrapperService;
import com.execue.platform.swi.ISDXDeletionWrapperService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IAssetDetailService;
import com.execue.swi.service.IDefaultDynamicValueService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXRetrievalService;

public class AssetDeletionWrapperServiceImpl implements IAssetDeletionWrapperService {

   private ISDXRetrievalService              sdxRetrievalService;
   private IJobDataService                   jobDataService;
   private ISystemDataAccessService          systemDataAccessService;
   private ISDXDeletionService               sdxDeletionService;
   private IAssetDetailService               assetDetailService;
   private IDefaultDynamicValueService       defaultDynamicValueService;
   private ISecurityDefinitionWrapperService securityDefinitionWrapperService;
   private ISDXDeletionWrapperService        sdxDeletionWrapperService;

   public void deleteAsset (AssetDeletionContext assetDeletionContext) throws SDXException {
      JobRequest jobRequest = assetDeletionContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      try {
         deleteAsset(jobRequest, jobOperationalStatus, getSdxRetrievalService().getAsset(
                  assetDeletionContext.getAssetId()));
      } catch (Exception exception) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                     .getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException queryDataException) {
               throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, queryDataException);
            }
         }
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
   }

   private void deleteAsset (JobRequest jobRequest, JobOperationalStatus jobOperationalStatus, Asset asset)
            throws SDXException {
      try {
         cleanChildAssets(jobRequest, jobOperationalStatus, asset);
         cleanSourceTablesForExecueAssets(asset);

         List<Tabl> tables = getSdxRetrievalService().getAllTables(asset);
         if (ExecueCoreUtil.isCollectionNotEmpty(tables)) {
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "deleting Asset Hierarchy", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);
            getSdxDeletionWrapperService().deleteAssetTables(asset, tables);
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         }
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "deleting AssetDetail Heirarchy", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         getAssetDetailService().deleteAssetDetail(asset.getId());
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "deleting Default Dynamic Values", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         getDefaultDynamicValueService().deleteDefaultDynamicValues(asset.getId());
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         // If the asset is owned by ExeCue and then is of type Mart or Cube, then delete the Context pertaining to the asset
         if (ExecueBeanUtil.isExecueOwnedCube(asset) || ExecueBeanUtil.isExecueOwnedMart(asset)) {
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "deleting the Answers Catalog Context", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);

            getJobDataService().deleteAnswersCatalogContextByAssetId(asset.getId());

            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         }

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest, "deleting the Asset",
                  JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         getSecurityDefinitionWrapperService().deleteSecurityDefinitions(asset);
         getSdxDeletionService().deleteAsset(asset);
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
      } catch (Exception exception) {
         ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                  .getMessage(), new Date());
         try {
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         } catch (QueryDataException queryDataException) {
            throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, queryDataException);
         }
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
   }

   private void cleanSourceTablesForExecueAssets (Asset asset) throws SDXException, DataAccessException {
      if (asset.getBaseAssetId() != null
               && (ExecueBeanUtil.isExecueOwnedMart(asset) || ExecueBeanUtil.isExecueOwnedCube(asset))) {
         List<Tabl> sourceTables = getSdxRetrievalService().getAllTablesExcludingVirtual(asset.getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(sourceTables)) {
            List<String> factTablesList = new ArrayList<String>();
            List<String> lookupTablesList = new ArrayList<String>();
            for (Tabl tabl : sourceTables) {
               if (tabl.getLookupType().equals(LookupType.None)) {
                  factTablesList.add(tabl.getName());
               } else {
                  lookupTablesList.add(tabl.getName());
               }
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(factTablesList)) {
               getSystemDataAccessService().dropTables(asset.getDataSource(), factTablesList);
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(lookupTablesList)) {
               getSystemDataAccessService().dropTables(asset.getDataSource(), lookupTablesList);
            }
         }
      }
   }

   private void cleanChildAssets (JobRequest jobRequest, JobOperationalStatus jobOperationalStatus, Asset asset)
            throws SDXException {
      List<Asset> childAssets = getSdxRetrievalService().getAllChildAssets(asset.getId());
      if (ExecueCoreUtil.isCollectionNotEmpty(childAssets)) {
         for (Asset childAsset : childAssets) {
            deleteAsset(jobRequest, jobOperationalStatus, childAsset);
         }
      }
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public ISystemDataAccessService getSystemDataAccessService () {
      return systemDataAccessService;
   }

   public void setSystemDataAccessService (ISystemDataAccessService systemDataAccessService) {
      this.systemDataAccessService = systemDataAccessService;
   }

   public ISDXDeletionService getSdxDeletionService () {
      return sdxDeletionService;
   }

   public void setSdxDeletionService (ISDXDeletionService sdxDeletionService) {
      this.sdxDeletionService = sdxDeletionService;
   }

   public IAssetDetailService getAssetDetailService () {
      return assetDetailService;
   }

   public void setAssetDetailService (IAssetDetailService assetDetailService) {
      this.assetDetailService = assetDetailService;
   }

   public IDefaultDynamicValueService getDefaultDynamicValueService () {
      return defaultDynamicValueService;
   }

   public void setDefaultDynamicValueService (IDefaultDynamicValueService defaultDynamicValueService) {
      this.defaultDynamicValueService = defaultDynamicValueService;
   }

   public ISecurityDefinitionWrapperService getSecurityDefinitionWrapperService () {
      return securityDefinitionWrapperService;
   }

   public void setSecurityDefinitionWrapperService (ISecurityDefinitionWrapperService securityDefinitionWrapperService) {
      this.securityDefinitionWrapperService = securityDefinitionWrapperService;
   }

   public ISDXDeletionWrapperService getSdxDeletionWrapperService () {
      return sdxDeletionWrapperService;
   }

   public void setSdxDeletionWrapperService (ISDXDeletionWrapperService sdxDeletionWrapperService) {
      this.sdxDeletionWrapperService = sdxDeletionWrapperService;
   }
}
