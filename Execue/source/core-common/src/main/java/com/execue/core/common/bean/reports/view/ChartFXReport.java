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


package com.execue.core.common.bean.reports.view;

import java.util.List;

import com.execue.core.common.bean.reports.Row;

public class ChartFXReport {

   private String    REPORTTYPES;
   private String    TITLE;
   private String    SUBTITLE;
   private String    SOURCE;
   private double    minXAxisValue;
   private double    minYAxisValue;
   private Columns   COLUMNS;
   private List<Row> ROW;

   public String getREPORTTYPES () {
      return REPORTTYPES;
   }

   public void setREPORTTYPES (String reporttypes) {
      REPORTTYPES = reporttypes;
   }

   public Columns getCOLUMNS () {
      return COLUMNS;
   }

   public void setCOLUMNS (Columns columns) {
      COLUMNS = columns;
   }

   public List<Row> getROW () {
      return ROW;
   }

   public void setROW (List<Row> row) {
      ROW = row;
   }

   public String getTITLE () {
      return TITLE;
   }

   public void setTITLE (String title) {
      TITLE = title;
   }

   public String getSOURCE () {
      return SOURCE;
   }

   public void setSOURCE (String source) {
      SOURCE = source;
   }

   public double getMinXAxisValue () {
      return minXAxisValue;
   }

   public void setMinXAxisValue (double minXAxisValue) {
      this.minXAxisValue = minXAxisValue;
   }

   public double getMinYAxisValue () {
      return minYAxisValue;
   }

   public void setMinYAxisValue (double minYAxisValue) {
      this.minYAxisValue = minYAxisValue;
   }

   public String getSUBTITLE () {
      return SUBTITLE;
   }

   public void setSUBTITLE (String subtitle) {
      SUBTITLE = subtitle;
   }

}
