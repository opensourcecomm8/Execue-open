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
public class Series {

   public Axis getAxis () {
      return axis;
   }

   public void setAxis (Axis axis) {
      this.axis = axis;
   }

   public String getLabel () {
      return label;
   }

   public void setLabel (String label) {
      this.label = label;
   }

   public int getLabelSize () {
      return labelSize;
   }

   public void setLabelSize (int labelSize) {
      this.labelSize = labelSize;
   }

   public Legend getLegend () {
      return legend;
   }

   public void setLegend (Legend legend) {
      this.legend = legend;
   }

   public List<SeriesPoint> getSeriesPoints () {
      return seriesPoints;
   }

   public void setSeriesPoints (List<SeriesPoint> seriesPoints) {
      this.seriesPoints = seriesPoints;
   }

   private List<SeriesPoint> seriesPoints;
   private Legend            legend;
   private Axis              axis;
   private String            label;
   private int               labelSize;

}
