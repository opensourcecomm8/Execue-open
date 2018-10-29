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
import com.execue.qdata.service.IQueryDataService;
import com.execue.reporting.presentation.bean.AggregationReport;
import com.execue.reporting.presentation.helper.PresentationTransformHelper;
import com.execue.reporting.presentation.service.IPresentationTransformService;

/**
 * Abstract Query Data server and provides common methods for transform servies
 * 
 * @author kaliki
 * @since 4.0
 */
public abstract class AbstractPresentationTransformService implements IPresentationTransformService {

   protected IQueryDataService queryDataService;
   protected Logger            logger = Logger.getLogger(this.getClass());

   /**
    * Get data from query data and parses the xml to get AggregationReport
    * 
    * @param queryId
    * @param assetId
    * @return
    */
   public AggregationReport getAggregationReport (long queryId, long assetId, long businessQueryId,
            AggregateQueryType type) {
      try {
         String xmlData = queryDataService.getReportXMLData(queryId, assetId, businessQueryId, type);
         // The Universal XML data has to be normalized since the column data tag is not in the format which XStream
         // cannot function.
         AggregationReport aggregationReport = PresentationTransformHelper.applyTransformationOnUniversalXml(xmlData,
                  true);
         return aggregationReport;
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }
}
