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


package com.execue.qdata.test.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;

public class AnswersCatalogManagementQueueServiceTest extends QueryDataServiceTest {

   private static final Logger logger = Logger.getLogger(MessageServiceTest.class);

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   public long getQueryId () {
      return System.currentTimeMillis();
   }

   // @Test
   public void testGetLatestPendingRecord () {
      List<Long> idsToIgnore = new ArrayList<Long>();
      idsToIgnore.add(30L);
      try {
         AnswersCatalogManagementQueue answersCatalogManagementQueue = getAnswersCatalogManagementQueueService()
                  .getLatestPendingRecord(null);
         System.out.println("id:" + answersCatalogManagementQueue.getId());
         System.out.println("assetid:" + answersCatalogManagementQueue.getAssetId());
         System.out.println("Operation type:" + answersCatalogManagementQueue.getOperationType());

      } catch (AnswersCatalogManagementQueueException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testAnyChildAssetACMQUnderProgress () {
      try {
         boolean isAnyChildAssetACMQUnderProgress = getAnswersCatalogManagementQueueService()
                  .isAnyChildAssetAnswersCatalogManagementQueueUnderProgress(11L);
         System.out.println("isAnyChildAssetACMQUnderProgress:::" + isAnyChildAssetACMQUnderProgress);
      } catch (AnswersCatalogManagementQueueException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testDependantACMQCompleted () {
      try {
         List<Long> dependentManagementIds = new ArrayList<Long>();
         dependentManagementIds.add(29L);
         boolean istestDependantACMQCompleted = getAnswersCatalogManagementQueueService()
                  .isDependantAnswersCatalogManagementQueueCompleted(dependentManagementIds);
         System.out.println("istestDependantACMQCompleted:::::" + istestDependantACMQCompleted);
      } catch (AnswersCatalogManagementQueueException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetAllACMQByOperationStatus () {
      try {
         List<AnswersCatalogManagementQueue> answersCatalogManagementQueueList = getAnswersCatalogManagementQueueService()
                  .getAllAnswersCatalogManagementQueueByOperationStatus(ACManagementOperationStatusType.PENDING);
         for (AnswersCatalogManagementQueue answersCatalogManagementQueue : answersCatalogManagementQueueList) {
            System.out.println("id:" + answersCatalogManagementQueue.getId());
            System.out.println("assetid:" + answersCatalogManagementQueue.getAssetId());
            System.out.println("Operation type:" + answersCatalogManagementQueue.getOperationType());
         }
      } catch (AnswersCatalogManagementQueueException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @Test
   public void testGetACMQInProgressCount () {
      try {
         int inProgressAcmqCount = getAnswersCatalogManagementQueueService()
                  .getACMQInProgressCountExcludingUserRequestOperationSrcType();
         System.out.println("inProgressAcmqCount::" + inProgressAcmqCount);
      } catch (AnswersCatalogManagementQueueException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testAnswersCatalogManagementQueueUnderProgress () {
      try {
         List<Long> assetIds = new ArrayList<Long>();
         assetIds.add(1015L);
         assetIds.add(1001L);
         boolean isAnswersCatalogManagementQueueUnderProgress = getAnswersCatalogManagementQueueService()
                  .isAnswersCatalogManagementQueueUnderProgress(assetIds);
         System.out.println("isAnswersCatalogManagementQueueUnderProgress:::"
                  + isAnswersCatalogManagementQueueUnderProgress);
      } catch (AnswersCatalogManagementQueueException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testACMQUnderProgressForSourceType () {
      try {
         boolean isACMQUnderProgressForSourceType = getAnswersCatalogManagementQueueService()
                  .isAnswersCatalogManagementQueueUnderProgressForSourceType(11L,
                           ACManagementOperationSourceType.ASSET_SYNC);
         System.out.println("isACMQUnderProgressForSourceType::" + isACMQUnderProgressForSourceType);
      } catch (AnswersCatalogManagementQueueException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testinvalidateAnswersCatalogManagementQueues () {
      List<Long> assetIds = new ArrayList<Long>();
      assetIds.add(1003L);
      assetIds.add(1002L);
      try {
         getAnswersCatalogManagementQueueService().invalidateAnswersCatalogManagementQueues(assetIds);
      } catch (AnswersCatalogManagementQueueException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
