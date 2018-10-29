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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.execue.report.view.bean;

import java.util.List;

/**
 * @author Owner
 */
public class Canvas {

   private String      title;
   private int         chartAlignment; // 0-horizontal, 1-vertical
   private List<Chart> charts;
   private LegendBox   legendBox;
   private boolean     hasLegendBox;
   private String      chartType;     // multiBar, crossLine, etc.

   public int getChartAlignment () {
      return chartAlignment;
   }

   public void setChartAlignment (int chartAlignment) {
      this.chartAlignment = chartAlignment;
   }

   public List<Chart> getCharts () {
      return charts;
   }

   public void setCharts (List<Chart> charts) {
      this.charts = charts;
   }

   public LegendBox getLegendBox () {
      return legendBox;
   }

   public void setLegendBox (LegendBox legendBox) {
      this.legendBox = legendBox;
   }

   public String getTitle () {
      return title;
   }

   public void setTitle (String title) {
      this.title = title;
   }

   public String getChartType () {
      return chartType;
   }

   public void setChartType (String chartType) {
      this.chartType = chartType;
   }

   public boolean isHasLegendBox () {
      return hasLegendBox;
   }

   public void setHasLegendBox (boolean hasLegendBox) {
      this.hasLegendBox = hasLegendBox;
   }
}
