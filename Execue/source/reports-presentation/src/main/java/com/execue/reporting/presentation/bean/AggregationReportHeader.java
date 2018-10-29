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


package com.execue.reporting.presentation.bean;

import java.util.List;

/**
 * Object representation for header section of report aggregation/Business Summary xml.
 * 
 * @author kaliki
 * @since 4.0
 */
public class AggregationReportHeader {

   private String                  TITLE;
   private String                  NUMCOLUMNS;
   private String                  REPORTTYPES;
   private List<AggregationColumn> columns;

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

   public List<AggregationColumn> getColumns () {
      return columns;
   }

   public void setColumns (List<AggregationColumn> columns) {
      this.columns = columns;
   }

   public String getTitle () {
      return TITLE;
   }

   public void setTitle (String title) {
      this.TITLE = title;
   }
}
