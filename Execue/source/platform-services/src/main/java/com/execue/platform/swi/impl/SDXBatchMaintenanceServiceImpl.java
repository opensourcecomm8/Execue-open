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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.batchMaintenance.InstanceAbsorptionContext;
import com.execue.core.common.bean.batchMaintenance.MemberAbsorptionContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.BatchProcess;
import com.execue.core.common.bean.entity.BatchProcessDetail;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.type.BatchProcessDetailType;
import com.execue.core.common.type.BatchType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.swi.ISDXBatchMaintenanceService;
import com.execue.platform.swi.ISourceMetaDataService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.BatchMaintenanceException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IBatchMaintenanceService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

public class SDXBatchMaintenanceServiceImpl implements ISDXBatchMaintenanceService {

   private static final Logger       log = Logger.getLogger(SDXBatchMaintenanceServiceImpl.class);

   private ISDXRetrievalService      sdxRetrievalService;
   private ISDXManagementService     sdxManagementService;
   private ISourceMetaDataService    sourceMetaDataService;
   private IBatchMaintenanceService  batchMaintenanceService;
   private ICoreConfigurationService coreConfigurationService;
   private IJobDataService           jobDataService;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.batchmaintenance.ISDXBatchMaintenanceService
    *      #absorbMembersInBatches(com.execue.core.common.bean.entity.Asset, com.execue.core.common.bean.entity.Tabl)
    */
   public void absorbMembersInBatches (MemberAbsorptionContext memberAbsorptionContext)
            throws BatchMaintenanceException {
      JobOperationalStatus jobOperationalStatus = null;
      try {
         JobRequest jobRequest = memberAbsorptionContext.getJobRequest();
         int batchSize = getCoreConfigurationService().getSDXMemberAbsorptionBatchSize();
         log.debug("Batch Size for member absorption " + batchSize);

         LimitEntity limitEntity = new LimitEntity();
         Asset asset = getSdxRetrievalService().getAsset(memberAbsorptionContext.getAssetId());
         Tabl table = getSdxRetrievalService().getTableById(memberAbsorptionContext.getTableId());
         List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(table);
         for (int i = 1; i <= memberAbsorptionContext.getTotalMembers(); i = i + batchSize) {
            limitEntity.setStartingNumber(new Long(i));
            limitEntity.setEndingNumber(new Long(i + batchSize - 1));
            List<Membr> batchMembers = sourceMetaDataService.getMembersFromSource(asset, table, limitEntity);
            log.debug("Got batch of members of size " + batchMembers.size());
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest, "Absorbing ["
                     + limitEntity.getStartingNumber() + ".." + limitEntity.getEndingNumber() + "] members in swi",
                     JobStatus.INPROGRESS, null, new Date());
            jobDataService.createJobOperationStatus(jobOperationalStatus);
            log.debug("Absorbing Current batch of members in swi");
            if (ExecueCoreUtil.isCollectionNotEmpty(batchMembers)) {
               getSdxManagementService().createMembers(asset, table, columns, batchMembers);
            }
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            jobDataService.updateJobOperationStatus(jobOperationalStatus);
         }

      } catch (Exception e) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE,
                     e.getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new BatchMaintenanceException(SWIExceptionCodes.MEMBER_CREATION_FAILED, e1);
            }
         }
         e.printStackTrace();
         throw new BatchMaintenanceException(SWIExceptionCodes.MEMBER_CREATION_FAILED, e);
      }
   }

   public void absorbInstanceInBatches (InstanceAbsorptionContext instanceAbsorptionContext)
            throws BatchMaintenanceException {
      JobOperationalStatus jobOperationalStatus = null;
      try {
         JobRequest jobRequest = instanceAbsorptionContext.getJobRequest();
         int batchSize = getCoreConfigurationService().getKDXInstanceMappingBatchSize();
         log.debug("Batch Size for instance mapping absorption " + batchSize);

      } catch (Exception e) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE,
                     e.getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new BatchMaintenanceException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e1);
            }
         }
         e.printStackTrace();
         throw new BatchMaintenanceException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e);
      }
   }

   public void createBatchProcess (MemberAbsorptionContext memberAbsorptionContext) throws BatchMaintenanceException {
      JobOperationalStatus jobOperationalStatus = null;
      try {
         JobRequest jobRequest = memberAbsorptionContext.getJobRequest();

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Adding records to BatchProcess Tables", JobStatus.INPROGRESS, null, new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);
         Asset asset = getSdxRetrievalService().getAsset(memberAbsorptionContext.getAssetId());
         BatchProcess batchProcess = new BatchProcess();
         BatchProcessDetail batchProcessDetail = new BatchProcessDetail();
         Set<BatchProcessDetail> batchProcessDetails = new HashSet<BatchProcessDetail>();
         batchProcess.setApplicationId(asset.getApplication().getId());
         batchProcess.setAssetId(asset.getId());
         batchProcess.setBatchType(BatchType.MEMBER_ABSORPTION);
         batchProcess.setJobRequestId(memberAbsorptionContext.getJobRequest().getId());
         batchProcessDetail.setParamName(BatchProcessDetailType.TABLE);
         batchProcessDetail.setParamValue(memberAbsorptionContext.getTableId().toString());
         batchProcessDetails.add(batchProcessDetail);
         batchProcess.setBatchProcessDetails(batchProcessDetails);
         batchProcessDetail.setBatchProcess(batchProcess);
         batchMaintenanceService.createBatchProcess(batchProcess);

         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         jobDataService.updateJobOperationStatus(jobOperationalStatus);

      } catch (Exception e) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE,
                     e.getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new BatchMaintenanceException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e1);
            }
         }
         e.printStackTrace();
         throw new BatchMaintenanceException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e);
      }
   }

   public void deleteBatchProcess (MemberAbsorptionContext memberAbsorptionContext) throws BatchMaintenanceException {

      // TODO - PSNM- Should handle delete thru cascade
      JobOperationalStatus jobOperationalStatus = null;
      try {
         JobRequest jobRequest = memberAbsorptionContext.getJobRequest();

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Deleting records from BatchProcess Tables on Successfull Completion", JobStatus.INPROGRESS, null,
                  new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);
         String batchProcessDetailParamType = BatchProcessDetailType.TABLE.getValue();
         Asset asset = getSdxRetrievalService().getAsset(memberAbsorptionContext.getAssetId());
         BatchProcess batchProcess = batchMaintenanceService.getBatchProcessByIds(asset.getApplication().getId(),
                  memberAbsorptionContext.getAssetId(), null, BatchType.MEMBER_ABSORPTION,
                  BatchProcessDetailType.TABLE, memberAbsorptionContext.getTableId().toString());
         Set<BatchProcessDetail> batchProcessDetails = batchProcess.getBatchProcessDetails();
         for (BatchProcessDetail batchProcessDetail : batchProcessDetails) {
            getBatchMaintenanceService().deleteBatchProcessDetail(batchProcessDetail);
         }
         getBatchMaintenanceService().deleteBatchProcess(batchProcess);
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         jobDataService.updateJobOperationStatus(jobOperationalStatus);
      } catch (Exception e) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE,
                     e.getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new BatchMaintenanceException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e1);
            }
         }
         e.printStackTrace();
         throw new BatchMaintenanceException(SWIExceptionCodes.BATCH_PROCESS_CREATION_FAILED, e);
      }

   }

   /**
    * @return the sourceMetaDataService
    */
   public ISourceMetaDataService getSourceMetaDataService () {
      return sourceMetaDataService;
   }

   /**
    * @param sourceMetaDataService
    *           the sourceMetaDataService to set
    */
   public void setSourceMetaDataService (ISourceMetaDataService sourceMetaDataService) {
      this.sourceMetaDataService = sourceMetaDataService;
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

   public IBatchMaintenanceService getBatchMaintenanceService () {
      return batchMaintenanceService;
   }

   public void setBatchMaintenanceService (IBatchMaintenanceService batchMaintenanceService) {
      this.batchMaintenanceService = batchMaintenanceService;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

}