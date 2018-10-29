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


package com.execue.swi.dataaccess;

import java.util.List;

import com.execue.core.common.bean.entity.BatchProcess;
import com.execue.core.common.bean.entity.BatchProcessDetail;
import com.execue.core.common.type.BatchProcessDetailType;
import com.execue.core.common.type.BatchType;
import com.execue.swi.exception.BatchMaintenanceException;

public interface IBatchMaintenanceDataAccessManager {

   public BatchProcess getBatchProcessById (Long batchProcessId) throws BatchMaintenanceException;

   public BatchProcessDetail getBatchProcessDetailById (Long batchProcessDetailId) throws BatchMaintenanceException;

   public void createBatchProcess (BatchProcess batchProcess) throws BatchMaintenanceException;

   public void updateBatchProcess (BatchProcess batchProcess) throws BatchMaintenanceException;

   public void deleteBatchProcess (BatchProcess batchProcess) throws BatchMaintenanceException;

   public void createBathProcessDetail (BatchProcessDetail batchProcessDetail) throws BatchMaintenanceException;

   public void updateBatchProcessDetail (BatchProcessDetail batchProcessDetail) throws BatchMaintenanceException;

   public void deleteBatchProcessDetail (BatchProcessDetail batchProcessDetail) throws BatchMaintenanceException;

   public boolean isApplicationUnderBatchProcess (Long applicationId) throws BatchMaintenanceException;

   public boolean isModelUnderBatchProcess (Long modelId) throws BatchMaintenanceException;

   public boolean isAssetUnderBatchProcess (Long assetId) throws BatchMaintenanceException;

   public boolean isTableUnderBatchProcess (Long tableId) throws BatchMaintenanceException;

   public boolean isColumnUnderBatchProcess (Long columnId) throws BatchMaintenanceException;

   public List<Long> getTablesOfApplicationUnderBatchProcess (Long applicationId) throws BatchMaintenanceException;

   public List<Long> getTablesOfAssetUnderBatchProcess (Long assetId) throws BatchMaintenanceException;

   public BatchProcess getBatchProcessByIds (Long applicationId, Long assetId, Long modelId, BatchType batchType,
            BatchProcessDetailType batchProcessDetailType, String batchProcessDetailParamValue)
            throws BatchMaintenanceException;

   public BatchProcessDetail getBatchProcessDetailByValues (String batchProcessDetailParamName,
            String batchProcessDetailParamValue) throws BatchMaintenanceException;

}
