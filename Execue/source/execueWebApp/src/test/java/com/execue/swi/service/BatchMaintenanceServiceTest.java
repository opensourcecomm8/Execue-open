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


package com.execue.swi.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.BatchProcess;
import com.execue.core.common.bean.entity.BatchProcessDetail;
import com.execue.core.common.type.BatchProcessDetailType;
import com.execue.core.common.type.BatchType;
import com.execue.core.exception.batchmaintenance.BatchMaintenanceException;

public class BatchMaintenanceServiceTest extends ExeCueBaseTest {

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   @Test
   public void testCreateBatchProcess () {
      BatchProcess batchProcess = new BatchProcess();
      batchProcess.setAssetId(1002L);
      batchProcess.setBatchType(BatchType.MEMBER_ABSORPTION);
      BatchProcessDetail batchProcessDetail = new BatchProcessDetail();
      batchProcessDetail.setParamName(BatchProcessDetailType.TABLE);
      batchProcessDetail.setParamValue("1012");
      batchProcess.setJobRequestId(1L);
      Set<BatchProcessDetail> batchProcessDetails = new HashSet<BatchProcessDetail>();
      batchProcessDetails.add(batchProcessDetail);
      batchProcess.setBatchProcessDetails(batchProcessDetails);
      batchProcessDetail.setBatchProcess(batchProcess);
      try {
         getBatchMaintenanceService().createBatchProcess(batchProcess);
      } catch (BatchMaintenanceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testUpdateBatchProcess () {
      try {
         BatchProcess batchProcess = getBatchMaintenanceService().getBatchProcessById(2011L);
         batchProcess.setJobRequestId(110L);
         System.out.println("batchProcessDetailSet ::" + batchProcess.getBatchProcessDetails());
         Set<BatchProcessDetail> batchProcessDetailSet = batchProcess.getBatchProcessDetails();
         for (BatchProcessDetail batchProcessDetail : batchProcessDetailSet) {
            batchProcessDetail.setParamValue("111");
         }
         batchProcess.setBatchProcessDetails(batchProcessDetailSet);
         getBatchMaintenanceService().updateBatchProcess(batchProcess);
      } catch (BatchMaintenanceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testDeleteBatchProcess () {
      try {
         BatchProcess batchProcess = getBatchMaintenanceService().getBatchProcessById(1001L);
         Set<BatchProcessDetail> batchProcessDetails = batchProcess.getBatchProcessDetails();
         for (BatchProcessDetail batchProcessDetail : batchProcessDetails) {
            getBatchMaintenanceService().deleteBatchProcessDetail(batchProcessDetail);
         }
         getBatchMaintenanceService().deleteBatchProcess(batchProcess);
      } catch (BatchMaintenanceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testIsApplicationUnderBatchProcess () {
      try {
         Long applicationId = 110L;

         boolean status = getBatchMaintenanceService().isApplicationUnderBatchProcess(applicationId);
         System.out.println("Application status ::" + status);
      } catch (BatchMaintenanceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testCreateBatchProcessDetail () {
      try {
         BatchProcess batchProcess = getBatchMaintenanceService().getBatchProcessById(2004L);
         BatchProcessDetail batchProcessDetail = new BatchProcessDetail();
         batchProcessDetail.setBatchProcess(batchProcess);
         batchProcessDetail.setParamName(BatchProcessDetailType.TABLE);
         batchProcessDetail.setParamValue("220");
         getBatchMaintenanceService().createBatchProcessDetail(batchProcessDetail);
      } catch (BatchMaintenanceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testUpdateBatchProcessDetail () {
      try {
         BatchProcessDetail batchProcessDetail = getBatchMaintenanceService().getBatchProcessDetailById(3001L);
         batchProcessDetail.setParamValue("222");
         getBatchMaintenanceService().updateBatchProcessDetail(batchProcessDetail);
      } catch (BatchMaintenanceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testDeleteBatchProcessDetail () {
      try {
         BatchProcessDetail batchProcessDetail = getBatchMaintenanceService().getBatchProcessDetailById(3001L);
         getBatchMaintenanceService().deleteBatchProcessDetail(batchProcessDetail);
      } catch (BatchMaintenanceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testIsTableUnderBatchProcess () {
      try {
         Long tableId = 221L;
         boolean status = getBatchMaintenanceService().isTableUnderBatchProcess(tableId);
         System.out.println("Table status ::" + status);
      } catch (BatchMaintenanceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testIsColumnUnderBatchProcess () {
      try {
         Long columnId = 220L;
         boolean status = getBatchMaintenanceService().isColumnUnderBatchProcess(columnId);
         System.out.println("Column status ::" + status);
      } catch (BatchMaintenanceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetTablesOfAssetUnderBatchProcess () {
      try {
         Long assetId = 1002L;
         List<Long> tableIds = getBatchMaintenanceService().getTablesOfAssetUnderBatchProcess(assetId);
         for (Long tableId : tableIds) {
            System.out.println("TableId ::" + tableId);
         }
      } catch (BatchMaintenanceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetTablesOfApplicationUnderBatchProcess () {
      try {
         Long applicationId = 104L;
         List<Long> tableIds = getBatchMaintenanceService().getTablesOfApplicationUnderBatchProcess(applicationId);
         for (Long tableId : tableIds) {
            System.out.println("TableId ::" + tableId);
         }
      } catch (BatchMaintenanceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @Test
   public void testgetBatchProcessByIds () {
      try {
         Long applicationId = 110L;
         // Long assetId = 1001L;
         Long modelId = null;
         BatchType batchType = BatchType.MEMBER_ABSORPTION;
         BatchProcessDetailType batchProcessDetailType = BatchProcessDetailType.TABLE;
         String batchProcessDetailParamValue = "1001";

         BatchProcess batchProcess = getBatchMaintenanceService().getBatchProcessByIds(applicationId, null, modelId,
                  batchType, batchProcessDetailType, batchProcessDetailParamValue);

         System.out.println("Batch Process Id ::" + batchProcess.getId());
      } catch (BatchMaintenanceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetBatchProcessDetailByValues () {
      try {
         String batchProcessDetailParamType = "COLUMN_ID";
         String batchProcessDetailParamValue = "1001";

         BatchProcessDetail batchProcessDetail = getBatchMaintenanceService().getBatchProcessDetailByValues(
                  batchProcessDetailParamType, batchProcessDetailParamValue);

         System.out.println("Batch Process Detail Id ::" + batchProcessDetail.getId());
      } catch (BatchMaintenanceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
