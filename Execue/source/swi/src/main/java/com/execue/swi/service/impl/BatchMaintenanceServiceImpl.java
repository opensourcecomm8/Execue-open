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

import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.BatchProcess;
import com.execue.core.common.bean.entity.BatchProcessDetail;
import com.execue.core.common.type.BatchProcessDetailType;
import com.execue.core.common.type.BatchType;
import com.execue.swi.exception.BatchMaintenanceException;
import com.execue.swi.dataaccess.IBatchMaintenanceDataAccessManager;
import com.execue.swi.service.IBatchMaintenanceService;

/**
 * @author MurthySN
 */
public class BatchMaintenanceServiceImpl implements IBatchMaintenanceService {

   private IBatchMaintenanceDataAccessManager batchMaintenanceDataAccessManager;

   public void createBatchProcess (BatchProcess batchProcess) throws BatchMaintenanceException {
      getBatchMaintenanceDataAccessManager().createBatchProcess(batchProcess);

   }

   public void deleteBatchProcess (BatchProcess batchProcess) throws BatchMaintenanceException {
      getBatchMaintenanceDataAccessManager().deleteBatchProcess(batchProcess);

   }

   public List<Long> getTablesOfApplicationUnderBatchProcess (Long applicationId) throws BatchMaintenanceException {
      return getBatchMaintenanceDataAccessManager().getTablesOfApplicationUnderBatchProcess(applicationId);
   }

   public List<Long> getTablesOfAssetUnderBatchProcess (Long assetId) throws BatchMaintenanceException {
      return getBatchMaintenanceDataAccessManager().getTablesOfAssetUnderBatchProcess(assetId);
   }

   public boolean isApplicationUnderBatchProcess (Long applicationId) throws BatchMaintenanceException {
      return getBatchMaintenanceDataAccessManager().isApplicationUnderBatchProcess(applicationId);
   }

   public boolean isAssetUnderBatchProcess (Long assetId) throws BatchMaintenanceException {
      return getBatchMaintenanceDataAccessManager().isAssetUnderBatchProcess(assetId);
   }

   public boolean isColumnUnderBatchProcess (Long columnId) throws BatchMaintenanceException {
      return getBatchMaintenanceDataAccessManager().isColumnUnderBatchProcess(columnId);
   }

   public boolean isModelUnderBatchProcess (Long modelId) throws BatchMaintenanceException {
      return getBatchMaintenanceDataAccessManager().isModelUnderBatchProcess(modelId);
   }

   public boolean isTableUnderBatchProcess (Long tableId) throws BatchMaintenanceException {
      return getBatchMaintenanceDataAccessManager().isTableUnderBatchProcess(tableId);
   }

   public void updateBatchProcess (BatchProcess batchProcess) throws BatchMaintenanceException {
      getBatchMaintenanceDataAccessManager().updateBatchProcess(batchProcess);
   }

   public IBatchMaintenanceDataAccessManager getBatchMaintenanceDataAccessManager () {
      return batchMaintenanceDataAccessManager;
   }

   public void setBatchMaintenanceDataAccessManager (
            IBatchMaintenanceDataAccessManager batchMaintenanceDataAccessManager) {
      this.batchMaintenanceDataAccessManager = batchMaintenanceDataAccessManager;
   }

   public BatchProcess getBatchProcessById (Long batchProcessId) throws BatchMaintenanceException {
      BatchProcess batchProcess = getBatchMaintenanceDataAccessManager().getBatchProcessById(batchProcessId);
      Set<BatchProcessDetail> batchProcessDetails = batchProcess.getBatchProcessDetails();
      for (BatchProcessDetail batchProcessDetail : batchProcessDetails) {
         batchProcessDetail.getId();
      }
      return batchProcess;
   }

   public void createBatchProcessDetail (BatchProcessDetail batchProcessDetail) throws BatchMaintenanceException {
      getBatchMaintenanceDataAccessManager().createBathProcessDetail(batchProcessDetail);

   }

   public void deleteBatchProcessDetail (BatchProcessDetail batchProcessDetail) throws BatchMaintenanceException {
      getBatchMaintenanceDataAccessManager().deleteBatchProcessDetail(batchProcessDetail);

   }

   public void updateBatchProcessDetail (BatchProcessDetail batchProcessDetail) throws BatchMaintenanceException {
      getBatchMaintenanceDataAccessManager().updateBatchProcessDetail(batchProcessDetail);

   }

   public BatchProcessDetail getBatchProcessDetailById (Long batchProcessDetailId) throws BatchMaintenanceException {
      return getBatchMaintenanceDataAccessManager().getBatchProcessDetailById(batchProcessDetailId);
   }

   public BatchProcess getBatchProcessByIds (Long applicationId, Long assetId, Long modelId, BatchType batchType,
            BatchProcessDetailType batchProcessDetailType, String batchProcessDetailParamValue)
            throws BatchMaintenanceException {
      BatchProcess batchProcess = getBatchMaintenanceDataAccessManager().getBatchProcessByIds(applicationId, assetId,
               modelId, batchType, batchProcessDetailType, batchProcessDetailParamValue);
      Set<BatchProcessDetail> batchProcessDetails = batchProcess.getBatchProcessDetails();
      for (BatchProcessDetail batchProcessDetail : batchProcessDetails) {
         batchProcessDetail.getId();
      }
      return batchProcess;
   }

   public BatchProcessDetail getBatchProcessDetailByValues (String batchProcessDetailParamName,
            String batchProcessDetailParamValue) throws BatchMaintenanceException {
      return getBatchMaintenanceDataAccessManager().getBatchProcessDetailByValues(batchProcessDetailParamName,
               batchProcessDetailParamValue);
   }
}
