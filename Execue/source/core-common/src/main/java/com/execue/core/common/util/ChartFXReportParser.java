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


package com.execue.core.common.util;

import com.execue.core.common.bean.reports.Row;
import com.execue.core.common.bean.reports.view.ChartFXReport;
import com.execue.core.common.bean.reports.view.Column;
import com.execue.core.common.bean.reports.view.ColumnData;
import com.execue.core.common.bean.reports.view.Columns;
import com.thoughtworks.xstream.XStream;

public class ChartFXReportParser {

   /**
    * Parse XML string and generate the ChartFXReport object
    * 
    * @param xmlData
    * @return chartFXReport
    */
   public static ChartFXReport parseReport (String xmlData) {
      return (ChartFXReport) getXStreamInstance().fromXML(xmlData);
   }

   /**
    * Generates the XML out of the ChartFXReport object
    * 
    * @param chartFXReport
    * @return xmlData
    */
   public static String generateXML (ChartFXReport chartFXReport) {
      return ExeCueXMLUtils.stripWhiteSpacesBetweenTags(getXStreamInstance().toXML(chartFXReport));
   }

   /**
    * This method prepares the XStream instance by setting the proper aliases, collections and attributes
    */
   private static XStream getXStreamInstance () {
      XStream xstream = new XStream();
      xstream.alias("CHARTFX", ChartFXReport.class);
      xstream.alias("COLUMN", Column.class);
      xstream.alias("COLUMNDATA", ColumnData.class);
      xstream.alias("ROW", Row.class);
      xstream.addImplicitCollection(Row.class, "COLUMNDATA");
      xstream.addImplicitCollection(Columns.class, "COLUMN");
      // xstream.addImplicitCollection(ChartFXReport.class, "ROW");
      xstream.useAttributeFor(Column.class, "DESCRIPTION");
      xstream.useAttributeFor(Column.class, "NAME");
      xstream.useAttributeFor(Column.class, "TYPE");
      xstream.useAttributeFor(Column.class, "AXIS");
      xstream.useAttributeFor(ColumnData.class, "NAME");
      xstream.useAttributeFor(ColumnData.class, "VALUE");
      return xstream;
   }

   public static ChartFXReport parseCannedReport (String xmlData) {
      XStream xstream = new XStream();
      xstream.alias("CHARTFX", ChartFXReport.class);
      xstream.addImplicitCollection(ChartFXReport.class, "ROW", Row.class);
      xstream.addImplicitCollection(Row.class, "COLUMNDATA", ColumnData.class);
      xstream.addImplicitCollection(Columns.class, "COLUMN", Column.class);
      xstream.useAttributeFor(Column.class, "DESCRIPTION");
      xstream.useAttributeFor(Column.class, "NAME");
      xstream.useAttributeFor(Column.class, "TYPE");
      xstream.useAttributeFor(Column.class, "AXIS");
      xstream.useAttributeFor(ColumnData.class, "NAME");
      xstream.useAttributeFor(ColumnData.class, "VALUE");
      return (ChartFXReport) xstream.fromXML(xmlData);
   }
}
