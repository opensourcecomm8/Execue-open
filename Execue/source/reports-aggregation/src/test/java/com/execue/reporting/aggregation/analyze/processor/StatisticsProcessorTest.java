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


package com.execue.reporting.aggregation.analyze.processor;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.BusinessStat;
import com.execue.reporting.aggregation.AggregationCommonBaseTest;
import com.execue.repoting.aggregation.analyze.processor.StatisticsProcessor;
import com.execue.repoting.aggregation.bean.ReportColumnInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.exception.ReportMetadataException;
import com.execue.repoting.aggregation.helper.ReportAggregationHelper;

public class StatisticsProcessorTest extends AggregationCommonBaseTest {

   private static final Logger logger = Logger.getLogger(StatisticsProcessorTest.class);

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
   public void analyzeMetadataTest () {
      StatisticsProcessor statisticsProcessor = getStatisticsProcessor();

      ReportMetaInfo reportMetaInfo = getReportMetaInfo(getAssetQueryFromXML("/xmls/aq_warehouse_stats.xml"));
      try {
         statisticsProcessor.analyzeMetadata(reportMetaInfo);
         for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
            logger.debug("Entity : " + ReportAggregationHelper.getEntityName(reportColumnInfo.getBizAssetTerm()));
            if (null != reportColumnInfo.getBusinessStats()) {
               for (BusinessStat businessStat : reportColumnInfo.getBusinessStats()) {
                  logger.debug("Stat : " + businessStat.getStat().getStatType() + "; "
                           + businessStat.isRequestedByUser());
               }
            } else {
               logger.debug("No Stat Applied");
            }
         }
      } catch (ReportMetadataException rme) {
         rme.printStackTrace();
      }
   }
}