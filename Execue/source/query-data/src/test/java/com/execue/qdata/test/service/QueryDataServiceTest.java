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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.audittrail.UserSearchAuditInput;
import com.execue.core.common.bean.audittrail.UserSearchAuditOutput;
import com.execue.core.common.bean.qdata.QDataUserQuery;
import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.test.QueryDataServiceBaseTest;

public class QueryDataServiceTest extends QueryDataServiceBaseTest {

   private static final Logger logger = Logger.getLogger(QueryDataServiceTest.class);

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
   public void testACMQService () {
      Long assetId = 1L;
      Long dependentManagementId = 22L;
      ACManagementOperationStatusType opeartionStatus = ACManagementOperationStatusType.INACTIVATED;
      Long acmqId = 22L;
      try {
         // boolean answersCatalogManagementQueueUnderProgress = getAnswersCatalogManagementQueueService()
         // .isAnswersCatalogManagementQueueUnderProgress(assetId);
         // System.out.println(answersCatalogManagementQueueUnderProgress);
         //
         // boolean dependantAnswersCatalogManagementQueueUnderProgress = getAnswersCatalogManagementQueueService()
         // .isDependantAnswersCatalogManagementQueueUnderProgress(dependentManagementId);
         // System.out.println(dependantAnswersCatalogManagementQueueUnderProgress);
         //
         // getAnswersCatalogManagementQueueService().updateOperationStatus(acmqId, opeartionStatus);
         //
         // int acmqCount = getAnswersCatalogManagementQueueService().getAnswersCatalogManagementQueueInProgressCount();
         // System.out.println(acmqCount);

         ACManagementOperationSourceType operationSourceType = ACManagementOperationSourceType.OPTIMAL_DSET;
         Long parentAssetId = 11L;

         boolean answersCatalogManagementQueueUnderProgressForSourceType = getAnswersCatalogManagementQueueService()
                  .isAnswersCatalogManagementQueueUnderProgressForSourceType(parentAssetId, operationSourceType);
         System.out.println(answersCatalogManagementQueueUnderProgressForSourceType);

      } catch (AnswersCatalogManagementQueueException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testQDataServiceStoreUserQuery () {
      try {
         QueryForm qForm = getQueryFormFromXML("/xmls/qf_metrics.xml");
         long queryid = getQueryId();
         logger.debug("Query ID : " + queryid);
         getQueryDataService().storeUserQuery(queryid / 1000, qForm, "Testing String", 101L, CheckType.YES);
      } catch (Exception e) {
         Assert.fail("Unable to Store User Query " + e.getMessage());
      }
   }

   @Test
   public void testGetPastUsagePatternInfos () {
      try {
         // Give the appropriate asset id
         Long assetId = 1001L;
         Map<String, Double> patternInfos = getQueryDataService().getPastUsagePatternInfoMap(assetId,
                  getQueryExecutionDate());
         System.out.println("Size: " + patternInfos.size() + "\nData: " + patternInfos);
      } catch (QueryDataException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testRearrangePatternInfos () {
      Map<String, Double> patternInfos = new HashMap<String, Double>();
      populateDummyPatternInfos(patternInfos);
      System.out.println("Size: " + patternInfos.size() + "\nData: " + patternInfos);
      rearrangePatternInfos(patternInfos);
      System.out.println("Size: " + patternInfos.size() + "\nData: " + patternInfos);
   }

   private Date getQueryExecutionDate () {
      int days = 600;
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -days);
      return cal.getTime();
   }

   /**
    * @param patternInfos
    */
   private void populateDummyPatternInfos (Map<String, Double> patternInfos) {
      Object[] array = new Object[16];
      array[0] = "101";
      array[1] = new Double(1);
      array[2] = "101,103";
      array[3] = new Double(2);
      array[4] = "101,103,203~-1";
      array[5] = new Double(2);
      array[6] = "101,201~-1";
      array[7] = new Double(1);
      array[8] = "103,101";
      array[9] = new Double(1);
      array[10] = "103,101,201~501";
      array[11] = new Double(1);
      array[12] = "103,203~-1,102";
      array[13] = new Double(1);
      array[14] = "103,201~501,101";
      array[15] = new Double(1);

      MapUtils.putAll(patternInfos, array);
   }

   private void rearrangePatternInfos (Map<String, Double> patternInfos) {
      if (MapUtils.isEmpty(patternInfos)) {
         return;
      }
      Set<Entry<String, Double>> entrySet = new HashSet<Entry<String, Double>>(patternInfos.entrySet());
      for (Entry<String, Double> entry : entrySet) {
         String key = entry.getKey();
         String[] elements = key.split(",");
         Collection<String> inputStrings = new TreeSet<String>();
         CollectionUtils.addAll(inputStrings, elements);
         String sortedKey = ExecueCoreUtil.joinCollection(inputStrings);
         if (!key.equalsIgnoreCase(sortedKey)) {
            Double removedValue = patternInfos.remove(key);
            Double value = patternInfos.get(sortedKey);
            if (value == null) {
               // Put the removed value back with the sorted key
               patternInfos.put(sortedKey, removedValue);
            } else {
               // Sum the removed value to the sorted key value and put it back
               value += removedValue;
               patternInfos.put(sortedKey, value);
            }
         }
      }
   }

   private UserSearchAuditInput createUserSearchAuditInput () throws QueryDataException {
      UserSearchAuditInput input = new UserSearchAuditInput();
      List<Long> userIds = new ArrayList<Long>();
      userIds.add(1L);
      userIds.add(2L);
      input.setUserIds(userIds);

      Page page = new Page(0l, 20l);
      input.setPage(page);

      input.setAnonymousUser(CheckType.NO);

      input.setOperator(OperatorType.BETWEEN);
      List<Date> searchDates = new ArrayList<Date>();
      QDataUserQuery query = getQueryDataService().getUserQuery(10000214063L);
      QDataUserQuery query2 = getQueryDataService().getUserQuery(10000001009L);
      searchDates.add(query2.getExecutionDate());
      searchDates.add(query.getExecutionDate());
      input.setSearchDates(searchDates);
      return input;
   }

   // @Test
   public void testPopulateUserSearchAudit () {
      try {
         List<UserSearchAuditOutput> userSearchAudit = getQueryDataService().populateUserSearchAudit(
                  createUserSearchAuditInput());
         Assert.assertTrue(userSearchAudit.size() > 0);
      } catch (QueryDataException e) {
         Assert.fail(e.getMessage());
      }
   }

}
