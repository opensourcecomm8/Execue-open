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


package com.execue.reporting.aggregation.service;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.reporting.aggregation.AggregationCommonBaseTest;
import com.execue.repoting.aggregation.bean.ReportSelection;
import com.execue.repoting.aggregation.service.impl.ReportSelectionServiceImpl;

public class ReportSelectionServiceTest extends AggregationCommonBaseTest {

   private static final Logger logger = Logger.getLogger(ReportSelectionServiceTest.class);
   ReportSelectionServiceImpl  reportSelectionService;
   ReportSelection             reportSelection;

   @BeforeClass
   public static void setUp () throws Exception {
      logger.debug("Inside the Setup Method");
      baseSetup();
   }

   @AfterClass
   public static void teardown () {
      logger.debug("Inside the TearDown Method");
      baseTeardown();
   }

   @Test
   public void getReportTypesTest () throws Exception {
      reportSelection = getReportSelection();
      reportSelectionService = getReportSelectionService();

      populateReportSelection(reportSelection);
      // List<ReportType> types = reportSelectionService.getReportTypes(reportSelection);
      // logger.info("The report types selected : " + types);
   }
}