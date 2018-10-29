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


package com.execue.swi.dataaccess.impl;

import java.util.List;

import com.execue.core.common.bean.entity.BatchProcess;
import com.execue.core.common.bean.entity.BatchProcessDetail;
import com.execue.core.common.type.BatchProcessDetailType;
import com.execue.core.common.type.BatchType;
import com.execue.swi.exception.BatchMaintenanceException;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.dataaccess.BatchMaintenanceDAOComponents;
import com.execue.swi.dataaccess.IBatchMaintenanceDataAccessManager;
import com.execue.swi.exception.SWIExceptionCodes;

public class BatchMaintenanceDataAccessManagerImpl extends BatchMaintenanceDAOComponents implements
         IBatchMaintenanceDataAccessManager {

   public void createBatchProcess (BatchProcess batchProcess) throws BatchMaintenanceException {
      try {
         getBatchMaintenanceDAO().create(batchProcess);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }

   }

   public void deleteBatchProcess (BatchProcess batchProcess) throws BatchMaintenanceException {
      try {
         getBatchMaintenanceDAO().delete(batchProcess);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }

   }

   public List<Long> getTablesOfApplicationUnderBatchProcess (Long applicationId) throws BatchMaintenanceException {
      List<Long> tableIds = null;
      try {
         tableIds = getBatchMaintenanceDAO().getTablesOfApplicationUnderBatchProcess(applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
      return tableIds;
   }

   public List<Long> getTablesOfAssetUnderBatchProcess (Long assetId) throws BatchMaintenanceException {
      List<Long> tableIds = null;
      try {
         tableIds = getBatchMaintenanceDAO().getTablesOfAssetUnderBatchProcess(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
      return tableIds;
   }

   public boolean isApplicationUnderBatchProcess (Long applicationId) throws BatchMaintenanceException {
      try {
         return getBatchMaintenanceDAO().isApplicationUnderBatchProcess(applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public boolean isAssetUnderBatchProcess (Long assetId) throws BatchMaintenanceException {
      try {
         return getBatchMaintenanceDAO().isAssetUnderBatchProcess(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public boolean isColumnUnderBatchProcess (Long columnId) throws BatchMaintenanceException {
      try {
         return getBatchMaintenanceDAO().isColumnUnderBatchProcess(columnId);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public boolean isModelUnderBatchProcess (Long modelId) throws BatchMaintenanceException {
      try {
         return getBatchMaintenanceDAO().isModelUnderBatchProcess(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public boolean isTableUnderBatchProcess (Long tableId) throws BatchMaintenanceException {
      try {
         return getBatchMaintenanceDAO().isTableUnderBatchProcess(tableId);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void updateBatchProcess (BatchProcess batchProcess) throws BatchMaintenanceException {
      try {
         getBatchMaintenanceDAO().update(batchProcess);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }

   }

   public BatchProcess getBatchProcessById (Long batchProcessId) throws BatchMaintenanceException {
      BatchProcess batchProcess = null;
      try {
         batchProcess = getBatchMaintenanceDAO().getById(batchProcessId, BatchProcess.class);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
      return batchProcess;
   }

   public void createBathProcessDetail (BatchProcessDetail batchProcessDetail) throws BatchMaintenanceException {
      try {
         getBatchMaintenanceDAO().create(batchProcessDetail);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteBatchProcessDetail (BatchProcessDetail batchProcessDetail) throws BatchMaintenanceException {
      try {
         getBatchMaintenanceDAO().delete(batchProcessDetail);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void updateBatchProcessDetail (BatchProcessDetail batchProcessDetail) throws BatchMaintenanceException {
      try {
         getBatchMaintenanceDAO().update(batchProcessDetail);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }

   }

   public BatchProcessDetail getBatchProcessDetailById (Long batchProcessDetailId) throws BatchMaintenanceException {
      BatchProcessDetail batchProcessDetail = null;
      try {
         batchProcessDetail = getBatchMaintenanceDAO().getById(batchProcessDetailId, BatchProcessDetail.class);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
      return batchProcessDetail;
   }

   public BatchProcess getBatchProcessByIds (Long applicationId, Long assetId, Long modelId, BatchType batchType,
            BatchProcessDetailType batchProcessDetailType, String batchProcessDetailParamValue)
            throws BatchMaintenanceException {
      BatchProcess batchProcess = null;
      try {
         batchProcess = getBatchMaintenanceDAO().getBatchProcessByIds(applicationId, assetId, modelId, batchType,
                  batchProcessDetailType, batchProcessDetailParamValue);
      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
      return batchProcess;
   }

   public BatchProcessDetail getBatchProcessDetailByValues (String batchProcessDetailParamName,
            String batchProcessDetailParamValue) throws BatchMaintenanceException {

      BatchProcessDetail batchProcessDetail = null;
      try {
         batchProcessDetail = getBatchMaintenanceDAO().getBatchProcessDetailByValues(batchProcessDetailParamName,
                  batchProcessDetailParamValue);

      } catch (DataAccessException dataAccessException) {
         throw new BatchMaintenanceException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
      return batchProcessDetail;
   }

}