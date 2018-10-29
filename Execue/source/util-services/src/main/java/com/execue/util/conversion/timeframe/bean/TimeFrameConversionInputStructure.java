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


package com.execue.util.conversion.timeframe.bean;

public class TimeFrameConversionInputStructure implements Cloneable {

   private TimeFrameConversionInputSourceInfo timeFrameConversionInputSourceInfo;
   private TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo;

   public TimeFrameConversionInputStructure (TimeFrameConversionInputSourceInfo timeFrameConversionInputSourceInfo,
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo) {
      super();
      this.timeFrameConversionInputSourceInfo = timeFrameConversionInputSourceInfo;
      this.timeFrameConversionInputTargetInfo = timeFrameConversionInputTargetInfo;
   }

   public TimeFrameConversionInputSourceInfo getTimeFrameConversionInputSourceInfo () {
      return timeFrameConversionInputSourceInfo;
   }

   public void setTimeFrameConversionInputSourceInfo (
            TimeFrameConversionInputSourceInfo timeFrameConversionInputSourceInfo) {
      this.timeFrameConversionInputSourceInfo = timeFrameConversionInputSourceInfo;
   }

   public TimeFrameConversionInputTargetInfo getTimeFrameConversionInputTargetInfo () {
      return timeFrameConversionInputTargetInfo;
   }

   public void setTimeFrameConversionInputTargetInfo (
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo) {
      this.timeFrameConversionInputTargetInfo = timeFrameConversionInputTargetInfo;
   }

   @Override
   protected Object clone () throws CloneNotSupportedException {
      TimeFrameConversionInputStructure clonedTimeFrameConversionInputStructure = (TimeFrameConversionInputStructure) super
               .clone();
      clonedTimeFrameConversionInputStructure
               .setTimeFrameConversionInputSourceInfo((TimeFrameConversionInputSourceInfo) timeFrameConversionInputSourceInfo
                        .clone());
      clonedTimeFrameConversionInputStructure
               .setTimeFrameConversionInputTargetInfo((TimeFrameConversionInputTargetInfo) timeFrameConversionInputTargetInfo
                        .clone());
      return clonedTimeFrameConversionInputStructure;
   }
}
