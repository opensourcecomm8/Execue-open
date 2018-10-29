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

import java.io.Serializable;

public class Scale implements Serializable {

   private int    scaleTobeApplied;
   private int    precision;
   private String suffix;
   private double avgLog;

   public int getPrecision () {
      return precision;
   }

   public void setPrecision (int precision) {
      this.precision = precision;
   }

   public String getSuffix () {
      return suffix;
   }

   public void setSuffix (String suffix) {
      this.suffix = suffix;
   }

   public int getScaleTobeApplied () {
      return scaleTobeApplied;
   }

   public void setScaleTobeApplied (int scaleTobeApplied) {
      this.scaleTobeApplied = scaleTobeApplied;
   }

   /**
    * @return the avgLog
    */
   public double getAvgLog () {
      return avgLog;
   }

   /**
    * @param avgLog
    *           the avgLog to set
    */
   public void setAvgLog (double avgLog) {
      this.avgLog = avgLog;
   }
}