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

import org.apache.log4j.Logger;

import com.execue.core.common.type.AggregateQueryType;
import com.execue.reporting.presentation.bean.PresentationTransformData;
import com.execue.reporting.presentation.helper.PresentationTransformHelper;

/**
 * Transform Class specific to Pivot
 * 
 * @author kaliki
 * @since 4.0
 */
public class PivotPresentationTransformService extends AbstractPresentationTransformService {

   private static final Logger log  = Logger.getLogger(PivotPresentationTransformService.class);

   public static String        name = "pivot";

   public PresentationTransformData getData (long queryId, long assetId, long businessQueryId) {
      // TODO Auto-generated method stub
      return null;
   }

   public PresentationTransformData getHeader (long queryId, long assetId, long businessQueryId) {
      // TODO Auto-generated method stub
      log.warn("In Pivot Presentation Transform Serivce");
      return null;
   }

   public PresentationTransformData getReport (long queryId, long assetId, long businessQueryId, AggregateQueryType type) {
      PresentationTransformData transformedData = new PresentationTransformData();
      try {
         if (log.isDebugEnabled()) {
            log.debug("In Pivot Presentation Transform Serivce");
         }
         String xmlData = queryDataService.getReportXMLData(queryId, assetId, businessQueryId, type);

         // normalize the xml dimension data values with the corresponding dimension column header member description
         xmlData = PresentationTransformHelper.transformXMLDataDescription(xmlData);

         transformedData.setXmlData(xmlData);
         return transformedData;
      } catch (Exception e) {
         // TODO: -JVK- error handling
         e.printStackTrace();
         return null;
      }
   }

   public PresentationTransformData getReport (int pageNum, int pageSize, long queryId, long assetId,
            long businessQueryId, AggregateQueryType type) {
      // TODO Auto-generated method stub
      return null;
   }

   public PresentationTransformData getReport (int pageNum, int pageSize, long aggregatedQueryId) {
      PresentationTransformData transformedData = new PresentationTransformData();
      try {
         if (log.isDebugEnabled()) {
            log.debug("In Pivot Presentation Transform Serivce");
         }
         String xmlData = queryDataService.getReportXMLData(aggregatedQueryId);

         // normalize the xml dimension data values with the corresponding dimension column header member description
         xmlData = PresentationTransformHelper.transformXMLDataDescription(xmlData);

         transformedData.setXmlData(xmlData);
         return transformedData;
      } catch (Exception e) {
         // TODO: -JVK- error handling
         e.printStackTrace();
         return null;
      }
   }
}
