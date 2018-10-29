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

import com.execue.core.ISystem;
import com.execue.core.type.SystemType;
import com.execue.reporting.aggregation.AggregationCommonBaseTest;
import com.execue.repoting.aggregation.bean.ReportSelection;
import com.execue.rules.bean.RuleParameters;
import com.execue.rules.service.IRuleService;

public class ReportRuleServiceTest extends AggregationCommonBaseTest {

   private static final Logger logger = Logger.getLogger(ReportRuleServiceTest.class);

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
   public void executeRuleTest () throws Exception {
      IRuleService ruleService = getRuleService();
      ISystem system = getExecueSystem();
      if (system.initialize(SystemType.WEBAPP)) {
         ReportSelection reportSelection = getReportSelection();
         populateReportSelection(reportSelection);
         RuleParameters parameters = new RuleParameters();
         parameters.addInput(reportSelection);
         ruleService.executeRule("reportSelection", parameters);
      }
   }
}