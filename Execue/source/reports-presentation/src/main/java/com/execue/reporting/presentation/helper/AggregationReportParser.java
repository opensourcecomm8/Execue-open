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


package com.execue.reporting.presentation.helper;

import com.execue.reporting.presentation.bean.AggregationColumn;
import com.execue.reporting.presentation.bean.AggregationColumnData;
import com.execue.reporting.presentation.bean.AggregationMember;
import com.execue.reporting.presentation.bean.AggregationReport;
import com.execue.reporting.presentation.bean.AggregationReportData;
import com.execue.reporting.presentation.bean.AggregationReportHeader;
import com.execue.reporting.presentation.bean.AggregationValue;
import com.thoughtworks.xstream.XStream;

public class AggregationReportParser {

   /**
    * Parse XML string and generate the AggetationReport object
    * 
    * @param xmlData
    * @return aggregationReport
    */
   public static AggregationReport parseReport (String xmlData) {
      return (AggregationReport) getXStreamInstance().fromXML(xmlData);
   }

   /**
    * Generates the XML out of the AggregationReport object
    * 
    * @param aggregationReport
    * @return xmlData
    */
   public static String generateXML (AggregationReport aggregationReport) {
      return PresentationTransformHelper.stripWhiteSpacesBetweenTags(getXStreamInstance().toXML(aggregationReport));
   }

   /**
    * This method prepares the XStream instance by setting the proper aliases, collections and attributes
    */
   private static XStream getXStreamInstance () {
      XStream xstream = new XStream();
      xstream.alias("XMLDocument", AggregationReport.class);
      xstream.alias("COLUMN", AggregationColumn.class);
      xstream.alias("MEMBER", AggregationMember.class);
      xstream.alias("COLUMNDATA", AggregationColumnData.class);
      xstream.alias("VALUES", AggregationValue.class);
      xstream.useAttributeFor(AggregationColumnData.class, "NAME");
      xstream.useAttributeFor(AggregationColumnData.class, "VALUE");
      xstream.addImplicitCollection(AggregationReportHeader.class, "columns");
      xstream.addImplicitCollection(AggregationReportData.class, "values");
      xstream.addImplicitCollection(AggregationValue.class, "columndata");
      return xstream;
   }
}
