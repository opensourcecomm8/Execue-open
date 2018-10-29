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

import com.execue.core.common.type.AggregateQueryType;
import com.execue.reporting.presentation.bean.AggregationReport;
import com.execue.reporting.presentation.bean.PresentationTransformData;
import com.execue.reporting.presentation.helper.AggregationReportParser;
import com.execue.reporting.presentation.helper.PresentationTransformHelper;

/**
 * @author John Mallavalli
 */
public class CSVPresentationTransformService extends AbstractPresentationTransformService {

   public PresentationTransformData getReport (long queryId, long assetId, long businessQueryId, AggregateQueryType type) {
      try {
         String xmlData = queryDataService.getReportXMLData(queryId, assetId, businessQueryId, type);

         AggregationReport report = PresentationTransformHelper.applyTransformationOnUniversalXml(xmlData, true);

         // generate the transformed xml
         xmlData = AggregationReportParser.generateXML(report);

         // de-normalize the transformed xml
         xmlData = PresentationTransformHelper.denormalizeDataSection(xmlData);

         PresentationTransformData transformedData = new PresentationTransformData();
         transformedData.setXmlData(PresentationTransformHelper.transformXMLDataForCSVReport(xmlData));
         return transformedData;
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public PresentationTransformData getHeader (long queryId, long assetId, long businessQueryId) {
      return null;
   }

   public PresentationTransformData getData (long queryId, long assetId, long businessQueryId) {
      return null;
   }

   public PresentationTransformData getReport (int pageNum, int pageSize, long queryId, long assetId,
            long businessQueryId, AggregateQueryType type) {
      // TODO Auto-generated method stub
      return null;
   }

   public PresentationTransformData getReport (int pageNum, int pageSize, long aggregatedQueryId) {
      try {
         String xmlData = queryDataService.getReportXMLData(aggregatedQueryId);

         AggregationReport report = PresentationTransformHelper.applyTransformationOnUniversalXml(xmlData, true);

         // generate the transformed xml
         xmlData = AggregationReportParser.generateXML(report);

         // de-normalize the transformed xml
         xmlData = PresentationTransformHelper.denormalizeDataSection(xmlData);

         PresentationTransformData transformedData = new PresentationTransformData();
         transformedData.setXmlData(PresentationTransformHelper.transformXMLDataForCSVReport(xmlData));
         return transformedData;
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }
}