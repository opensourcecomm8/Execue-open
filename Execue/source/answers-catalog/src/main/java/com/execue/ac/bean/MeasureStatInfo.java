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


package com.execue.ac.bean;

/**
 * This bean represents the stddev and mean value of measure
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MeasureStatInfo {

   private Double stddevValue;
   private Double meanValue;
   private Double minValue;
   private Double maxValue;

   public MeasureStatInfo (Double stddevValue, Double meanValue, Double minValue, Double maxValue) {
      super();
      this.stddevValue = stddevValue;
      this.maxValue = maxValue;
      this.meanValue = meanValue;
      this.minValue = minValue;
   }

   public Double getStddevValue () {
      return stddevValue;
   }

   public void setStddevValue (Double stddevValue) {
      this.stddevValue = stddevValue;
   }

   public Double getMeanValue () {
      return meanValue;
   }

   public void setMeanValue (Double meanValue) {
      this.meanValue = meanValue;
   }

   public Double getMinValue () {
      return minValue;
   }

   public void setMinValue (Double minValue) {
      this.minValue = minValue;
   }

   public Double getMaxValue () {
      return maxValue;
   }

   public void setMaxValue (Double maxValue) {
      this.maxValue = maxValue;
   }

}
