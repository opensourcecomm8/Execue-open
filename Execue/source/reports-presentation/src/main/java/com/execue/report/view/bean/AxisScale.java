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

/**
 * @author Owner
 */
public class AxisScale {

   private double min;
   private double max;
   private double step;
   private double maxSizePercent;
   private double minorStep;
   private int    depth;

   public double getMin () {
      return min;
   }

   public void setMin (double min) {
      this.min = min;
   }

   public double getMax () {
      return max;
   }

   public void setMax (double max) {
      this.max = max;
   }

   public double getStep () {
      return step;
   }

   public void setStep (double step) {
      this.step = step;
   }

   public double getMaxSizePercent () {
      return maxSizePercent;
   }

   public void setMaxSizePercent (double maxSizePercent) {
      this.maxSizePercent = maxSizePercent;
   }

   public double getMinorStep () {
      return minorStep;
   }

   public void setMinorStep (double minorStep) {
      this.minorStep = minorStep;
   }

   public int getDepth () {
      return depth;
   }

   public void setDepth (int depth) {
      this.depth = depth;
   }

}
