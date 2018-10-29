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

import com.execue.core.common.bean.reports.prsntion.UniversalColumn;
import com.execue.core.common.bean.reports.prsntion.UniversalColumnData;
import com.execue.core.common.bean.reports.prsntion.UniversalHierarchy;
import com.execue.core.common.bean.reports.prsntion.UniversalHierarchyEntity;
import com.execue.core.common.bean.reports.prsntion.UniversalMember;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.bean.reports.prsntion.UniversalReportData;
import com.execue.core.common.bean.reports.prsntion.UniversalReportHeader;
import com.execue.core.common.bean.reports.prsntion.UniversalValue;
import com.thoughtworks.xstream.XStream;

public class UniversalReportParser {

   /**
    * Parse XML string and generate the UniversalReport object
    * 
    * @param xmlData
    * @return universalReport
    */
   public static UniversalReport parseReport (String xmlData) {
      return (UniversalReport) getXStreamInstance().fromXML(xmlData);
   }

   /**
    * Generates the XML out of the UniversalReport object
    * 
    * @param universalReport
    * @return xmlData
    */
   public static String generateXML (UniversalReport universalReport) {
      return ExeCueXMLUtils.stripWhiteSpacesBetweenTags(getXStreamInstance().toXML(universalReport));
   }

   /**
    * This method prepares the XStream instance by setting the proper aliases, collections and attributes
    */
   private static XStream getXStreamInstance () {
      XStream xstream = new XStream();
      xstream.alias("XMLDocument", UniversalReport.class);
      xstream.alias("COLUMN", UniversalColumn.class);
      xstream.alias("MEMBER", UniversalMember.class);
      xstream.alias("COLUMNDATA", UniversalColumnData.class);
      xstream.alias("VALUES", UniversalValue.class);
      xstream.alias("HIERARCHY", UniversalHierarchy.class);
      xstream.alias("HIERARCHYENTITY", UniversalHierarchyEntity.class);

      xstream.useAttributeFor(UniversalColumnData.class, "NAME");
      xstream.useAttributeFor(UniversalColumnData.class, "VALUE");
      xstream.useAttributeFor(UniversalColumnData.class, "CTYPE");

      xstream.addImplicitCollection(UniversalReportHeader.class, "columns");
      xstream.addImplicitCollection(UniversalReportData.class, "values");
      xstream.addImplicitCollection(UniversalValue.class, "columndata");

      return xstream;
   }
}
