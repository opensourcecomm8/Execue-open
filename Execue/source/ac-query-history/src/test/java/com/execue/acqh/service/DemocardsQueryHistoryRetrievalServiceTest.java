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


package com.execue.acqh.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.acqh.AnswersCatalogQueryHistoryBaseTest;
import com.execue.acqh.exception.AnswersCatalogQueryHistoryException;
import com.execue.core.common.bean.qdata.QueryHistoryDimensionPatternInfo;

/**
 * @author Nitesh
 *
 */
public class DemocardsQueryHistoryRetrievalServiceTest extends AnswersCatalogQueryHistoryBaseTest {

   @Before
   public void setUp () {
      answersCatalogQueryHistoryBaseSetUp();
   }

   @After
   public void tearDown () {
      answersCatalogQueryHistoryBaseTearDown();
   }

   @Test
   public void testBuildPastUsageDimensionPatternInfo () {

      List<QueryHistoryDimensionPatternInfo> optimalDSetPastUsageDimensionPatternList = new ArrayList<QueryHistoryDimensionPatternInfo>();
      try {
         optimalDSetPastUsageDimensionPatternList = getDemocardsQueryHistoryRetrievalService()
                  .buildQueryHistoryDimensionPatternInfo(11L, 110L, getQueryExecutionDate());
      } catch (AnswersCatalogQueryHistoryException e) {
         e.printStackTrace();
      }
      System.out.println("List of optimalDSetPastUsageDimensionPatternList");
      double totalUsage = 0d;
      for (QueryHistoryDimensionPatternInfo optimalDSetPastUsageDimensionPattern : optimalDSetPastUsageDimensionPatternList) {
         totalUsage += optimalDSetPastUsageDimensionPattern.getUsagePercentage();
         System.out.println(optimalDSetPastUsageDimensionPattern.getUsagePercentage());
         //         System.out.println(optimalDSetPastUsageDimensionPattern.getOptimalDSetSwiInfos().toString());
         System.out.println("-----------------------------------");
      }
      System.out.println("Total Past Patterns: " + optimalDSetPastUsageDimensionPatternList.size());
      System.out.println("Total Past Usage Covered: " + totalUsage);
   }

   private Date getQueryExecutionDate () {
      int days = 60;
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -days);
      return cal.getTime();
   }
}
