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


package com.execue.reporting.aggregation.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.reporting.aggregation.AggregationCommonBaseTest;
import com.execue.repoting.aggregation.bean.AggregationBusinessAssetTerm;
import com.execue.repoting.aggregation.bean.AggregationMetaInfo;
import com.execue.repoting.aggregation.bean.AggregationRangeDetail;
import com.execue.repoting.aggregation.bean.AggregationRangeInfo;
import com.execue.repoting.aggregation.bean.AggregationStructuredQuery;

public class ReportAggregationHelperTest extends AggregationCommonBaseTest {

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   @Test
   public void test () {
      try {
         AggregationMetaInfo aggregationMetaInfo = prepareAggegateMetaInfo();
         String xml = getReportAggregationHelper().getXStreamForMetaInfoTransformation().toXML(aggregationMetaInfo);
         System.out.println("************************");
         System.out.println(xml);
         System.out.println("************************");
         aggregationMetaInfo = (AggregationMetaInfo) getReportAggregationHelper().getXStreamForMetaInfoTransformation()
                  .fromXML(xml);
         xml = getReportAggregationHelper().getXStreamForMetaInfoTransformation().toXML(aggregationMetaInfo);
         System.out.println("************************");
         System.out.println(xml);
         System.out.println("************************");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private AggregationMetaInfo prepareAggegateMetaInfo () {

      AggregationBusinessAssetTerm aggregationBusinessAssetTerm1 = new AggregationBusinessAssetTerm();
      aggregationBusinessAssetTerm1.setAggregationRangeInfo(prepareAggregationRangeInfo());

      AggregationBusinessAssetTerm aggregationBusinessAssetTerm2 = new AggregationBusinessAssetTerm();

      List<AggregationBusinessAssetTerm> aggregationBusinessAssetTerms = new ArrayList<AggregationBusinessAssetTerm>();
      aggregationBusinessAssetTerms.add(aggregationBusinessAssetTerm1);
      aggregationBusinessAssetTerms.add(aggregationBusinessAssetTerm2);

      AggregationStructuredQuery aggregationStructuredQuery = new AggregationStructuredQuery();
      aggregationStructuredQuery.setMetrics(aggregationBusinessAssetTerms);

      // Prepare the aggregationMetaInfo 
      AggregationMetaInfo aggregationMetaInfo = new AggregationMetaInfo();
      aggregationMetaInfo.setStructuredQuery(aggregationStructuredQuery);
      //      aggregationMetaInfo.setReportColumns(reportColumns);

      return aggregationMetaInfo;
   }

   private AggregationRangeInfo prepareAggregationRangeInfo () {

      AggregationRangeInfo aggregationRangeInfo = new AggregationRangeInfo();
      aggregationRangeInfo.setAggregationRangeDetails(prepareAggregationRangeDetails());
      aggregationRangeInfo.setName("Range");

      return aggregationRangeInfo;
   }

   private Set<AggregationRangeDetail> prepareAggregationRangeDetails () {
      Set<AggregationRangeDetail> aggregationRangeDetails = new HashSet<AggregationRangeDetail>();
      aggregationRangeDetails.add(prepareAggregationRangeDetail());
      aggregationRangeDetails.add(prepareAggregationRangeDetail());

      return aggregationRangeDetails;
   }

   private AggregationRangeDetail prepareAggregationRangeDetail () {
      AggregationRangeDetail aggregationRangeDetail = new AggregationRangeDetail();
      aggregationRangeDetail.setRangeDetailId(1L);
      aggregationRangeDetail.setDescription("Range Detail");
      aggregationRangeDetail.setLowerLimit(1d);
      aggregationRangeDetail.setUpperLimit(5d);
      aggregationRangeDetail.setOrder(1);
      return aggregationRangeDetail;
   }
}
