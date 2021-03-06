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
public class Chart {

   private List<Series> series;
   private boolean      hasLegendBox;
   private LegendBox    legendBox;
   private List<Axis>   axes;

   public List<Axis> getAxes () {
      return axes;
   }

   public void setAxes (List<Axis> axes) {
      this.axes = axes;
   }

   public boolean isHasLegendBox () {
      return hasLegendBox;
   }

   public void setHasLegendBox (boolean hasLegendBox) {
      this.hasLegendBox = hasLegendBox;
   }

   public LegendBox getLegendBox () {
      return legendBox;
   }

   public void setLegendBox (LegendBox legendBox) {
      this.legendBox = legendBox;
   }

   public List<Series> getSeries () {
      return series;
   }

   public void setSeries (List<Series> series) {
      this.series = series;
   }

}
