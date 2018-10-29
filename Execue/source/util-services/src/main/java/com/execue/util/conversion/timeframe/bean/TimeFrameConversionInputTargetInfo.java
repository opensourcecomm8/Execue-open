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

import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.type.DateQualifier;

public class TimeFrameConversionInputTargetInfo implements Cloneable {

   private String                  targetFormat;
   private DateQualifier           targetDateQualifier;
   private TimeFrameNormalizedData defaultedTimeFrameNormalizedData;

   public String getTargetFormat () {
      return targetFormat;
   }

   public void setTargetFormat (String targetFormat) {
      this.targetFormat = targetFormat;
   }

   public DateQualifier getTargetDateQualifier () {
      return targetDateQualifier;
   }

   public void setTargetDateQualifier (DateQualifier targetDateQualifier) {
      this.targetDateQualifier = targetDateQualifier;
   }

   public TimeFrameConversionInputTargetInfo (String targetFormat, DateQualifier targetDateQualifier,
            TimeFrameNormalizedData defaultedTimeFrameNormalizedData) {
      super();
      this.targetFormat = targetFormat;
      this.targetDateQualifier = targetDateQualifier;
      this.defaultedTimeFrameNormalizedData = defaultedTimeFrameNormalizedData;
   }

   public TimeFrameNormalizedData getDefaultedTimeFrameNormalizedData () {
      return defaultedTimeFrameNormalizedData;
   }

   public void setDefaultedTimeFrameNormalizedData (TimeFrameNormalizedData defaultedTimeFrameNormalizedData) {
      this.defaultedTimeFrameNormalizedData = defaultedTimeFrameNormalizedData;
   }

   @Override
   protected Object clone () throws CloneNotSupportedException {
      TimeFrameConversionInputTargetInfo clonedTimeFrameConversionInputTargetInfo = (TimeFrameConversionInputTargetInfo) super
               .clone();
      clonedTimeFrameConversionInputTargetInfo.setTargetDateQualifier(targetDateQualifier);
      clonedTimeFrameConversionInputTargetInfo.setTargetFormat(targetFormat);
      clonedTimeFrameConversionInputTargetInfo
               .setDefaultedTimeFrameNormalizedData((TimeFrameNormalizedData) defaultedTimeFrameNormalizedData.clone());
      return clonedTimeFrameConversionInputTargetInfo;
   }

}
