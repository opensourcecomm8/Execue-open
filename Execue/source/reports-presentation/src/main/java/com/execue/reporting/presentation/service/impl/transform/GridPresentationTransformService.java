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


package com.execue.reporting.presentation.service.impl.transform;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.reporting.presentation.bean.AggregationReport;
import com.execue.reporting.presentation.bean.AggregationReportData;
import com.execue.reporting.presentation.bean.AggregationValue;
import com.execue.reporting.presentation.bean.PresentationTransformData;
import com.execue.reporting.presentation.helper.AggregationReportParser;
import com.execue.reporting.presentation.helper.PresentationTransformHelper;

/**
 * Transform Class specific to Grid
 * 
 * @author kaliki
 * @since 4.0
 */
public class GridPresentationTransformService extends AbstractPresentationTransformService {

   public static String name = "grid";

   public PresentationTransformData getReport (long queryId, long assetId, long businessQueryId, AggregateQueryType type) {
      PresentationTransformData transformedData = new PresentationTransformData();
      try {
         String xmlData = queryDataService.getReportXMLData(queryId, assetId, businessQueryId, type);

         AggregationReport report = PresentationTransformHelper.applyTransformationOnUniversalXml(xmlData, true);

         // generate the transformed xml
         xmlData = AggregationReportParser.generateXML(report);

         // de-normalize the transformed xml
         xmlData = PresentationTransformHelper.denormalizeDataSection(xmlData);
         transformedData.setXmlData(xmlData);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return transformedData;
   }

   @SuppressWarnings ("unchecked")
   @Deprecated
   public PresentationTransformData getReport (int pageNum, int pageSize, long queryId, long assetId,
            long businessQueryId, AggregateQueryType type) {
      int startIndex = (pageNum - 1) * pageSize + 1;
      int endIndex = pageNum * pageSize;
      PresentationTransformData transformedData = new PresentationTransformData();
      AggregationReport pageReport = new AggregationReport();
      AggregationReportData pageData = new AggregationReportData();
      List<AggregationValue> pageValues = new ArrayList<AggregationValue>();
      try {
         String xmlData = queryDataService.getReportXMLData(queryId, assetId, businessQueryId, type);

         // generate the AggregationReport object out of the modified xml
         AggregationReport report = PresentationTransformHelper.applyTransformationOnUniversalXml(xmlData, false);

         // process only the page values
         List<AggregationValue> values = report.getData().getValues();
         int resultsCount = values.size();

         // calculate the page count
         int totalPages = resultsCount / pageSize;
         int remainder = resultsCount % pageSize;
         if (remainder > 0) {
            totalPages++;
         }
         transformedData.setPageCount(resultsCount); // we are sending total count.
         transformedData.setRequestedPage(pageNum);
         if (endIndex > resultsCount) {
            endIndex = resultsCount;
         }

         pageValues = getSubList((startIndex - 1), endIndex, values);

         pageData.setValues(pageValues);
         pageReport.setHeader(report.getHeader());
         pageReport.setData(pageData);

         // apply the numerical transformation
         AggregationReport transformedPageReport = PresentationTransformHelper.applyNumericTransformation(pageReport);

         // generate the transformed xml
         String transformedPageXMLData = AggregationReportParser.generateXML(transformedPageReport);

         // de-normalize the transformed xml
         String denormXMLData = PresentationTransformHelper.denormalizeDataSection(transformedPageXMLData);
         transformedData.setXmlData(denormXMLData);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return transformedData;
   }

   @SuppressWarnings ("unchecked")
   public PresentationTransformData getReport (int pageNum, int pageSize, long aggregatedQueryId) {
      int startIndex = (pageNum - 1) * pageSize + 1;
      int endIndex = pageNum * pageSize;
      PresentationTransformData transformedData = new PresentationTransformData();
      AggregationReport pageReport = new AggregationReport();
      AggregationReportData pageData = new AggregationReportData();
      List<AggregationValue> pageValues = new ArrayList<AggregationValue>();
      try {
         String xmlData = queryDataService.getReportXMLData(aggregatedQueryId);

         // generate the AggregationReport object out of the modified xml
         AggregationReport report = PresentationTransformHelper.applyTransformationOnUniversalXml(xmlData, false);

         List<AggregationValue> values = report.getData().getValues();
         int resultsCount = values.size();

         // calculate the page count
         int totalPages = resultsCount / pageSize;
         int remainder = resultsCount % pageSize;
         if (remainder > 0) {
            totalPages++;
         }
         transformedData.setPageCount(resultsCount); // we are sending total count.
         transformedData.setRequestedPage(pageNum);
         if (endIndex > resultsCount) {
            endIndex = resultsCount;
         }

         pageValues = getSubList((startIndex - 1), endIndex, values);

         pageData.setValues(pageValues);
         pageReport.setHeader(report.getHeader());
         pageReport.setData(pageData);
         // apply the numerical transformation
         AggregationReport transformedPageReport = PresentationTransformHelper.applyNumericTransformation(pageReport);
         // generate the transformed xml
         String transformedPageXMLData = AggregationReportParser.generateXML(transformedPageReport);
         // de-normalize the transformed xml
         String denormXMLData = PresentationTransformHelper.denormalizeDataSection(transformedPageXMLData);
         transformedData.setXmlData(denormXMLData);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return transformedData;
   }

   @SuppressWarnings ("unchecked")
   public static List getSubList (int startIndex, int endIndex, List list) {
      List subList = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(list)) {
         if (startIndex < 0) {
            return null;
         }
         if (endIndex > list.size()) {
            endIndex = list.size();
         }
         subList = new ArrayList();
         for (int i = startIndex; i < endIndex; i++) {
            subList.add(list.get(i));
         }
      }
      return subList;
   }

   public PresentationTransformData getHeader (long queryId, long assetId, long businessQueryId) {
      return null;
   }

   public PresentationTransformData getData (long queryId, long assetId, long businessQueryId) {
      return null;
   }

}
