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


package com.execue.core.common.bean.reports.prsntion;

import java.util.List;

/**
 * Object representation for header section of report aggregation/Business Summary xml.
 * 
 * @author kaliki
 * @since 4.0
 */
public class UniversalReportHeader {

   private String                TITLE;
   private String                NUMCOLUMNS;
   private String                REPORTTYPES;
   private String                source;
   private List<UniversalColumn> columns;
   private UniversalHierarchy    HIERARCHY;

   public String getNumcolumns () {
      return NUMCOLUMNS;
   }

   public void setNumcolumns (String numcolumns) {
      this.NUMCOLUMNS = numcolumns;
   }

   public String getReporttypes () {
      return REPORTTYPES;
   }

   public void setReporttypes (String reporttypes) {
      this.REPORTTYPES = reporttypes;
   }

   public List<UniversalColumn> getColumns () {
      return columns;
   }

   public void setColumns (List<UniversalColumn> columns) {
      this.columns = columns;
   }

   public String getTITLE () {
      return TITLE;
   }

   public void setTITLE (String title) {
      TITLE = title;
   }

   public String getSource () {
      return source;
   }

   public void setSource (String source) {
      this.source = source;
   }

   public UniversalHierarchy getHIERARCHY () {
      return HIERARCHY;
   }

   public void setHIERARCHY (UniversalHierarchy hIERARCHY) {
      HIERARCHY = hIERARCHY;
   }

}
