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

import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.BusinessStat;
import com.execue.reporting.aggregation.AggregationBaseTest;
import com.execue.repoting.aggregation.bean.ReportColumnInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.service.impl.ReportMetadataAnalyzerServiceImpl;

public class ReportMetadataAnalyzerServiceTest extends AggregationBaseTest {

   private static final Logger logger = Logger.getLogger(ReportMetadataAnalyzerServiceTest.class);

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
   public void analyzeTest () throws Exception {
      ReportMetadataAnalyzerServiceImpl reportMetadataAnalyzerService = getReportMetadataAnalyzerService();
      ReportMetaInfo reportMetaInfo = reportMetadataAnalyzerService.analyze(null);

      for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
         logger.debug("Column Type : " + reportColumnInfo.getColumnType());
         Set<BusinessStat> businessStats = reportColumnInfo.getBusinessStats();
         if (businessStats != null) {
            logger.debug("Statistics :");
            for (BusinessStat businessStat : businessStats) {
               if (businessStat.getStat() != null) {
                  logger.debug(businessStat.getStat().getStatType());
               }
            }
         }
      }
   }
}