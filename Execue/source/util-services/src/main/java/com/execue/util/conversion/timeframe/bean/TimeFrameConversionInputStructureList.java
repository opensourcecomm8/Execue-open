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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.type.OperatorType;

public class TimeFrameConversionInputStructureList implements Cloneable {

   private List<TimeFrameConversionInputSourceInfo> timeFrameConversionInputSourceInfoList;
   private TimeFrameConversionInputTargetInfo       timeFrameConversionInputTargetInfo;
   private OperatorType                             operatorType;

   public List<TimeFrameConversionInputSourceInfo> getTimeFrameConversionInputSourceInfoList () {
      return timeFrameConversionInputSourceInfoList;
   }

   public void setTimeFrameConversionInputSourceInfoList (
            List<TimeFrameConversionInputSourceInfo> timeFrameConversionInputSourceInfoList) {
      this.timeFrameConversionInputSourceInfoList = timeFrameConversionInputSourceInfoList;
   }

   public TimeFrameConversionInputTargetInfo getTimeFrameConversionInputTargetInfo () {
      return timeFrameConversionInputTargetInfo;
   }

   public void setTimeFrameConversionInputTargetInfo (
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo) {
      this.timeFrameConversionInputTargetInfo = timeFrameConversionInputTargetInfo;
   }

   public TimeFrameConversionInputStructureList (
            List<TimeFrameConversionInputSourceInfo> timeFrameConversionInputSourceInfoList,
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo, OperatorType operatorType) {
      super();
      this.timeFrameConversionInputSourceInfoList = timeFrameConversionInputSourceInfoList;
      this.timeFrameConversionInputTargetInfo = timeFrameConversionInputTargetInfo;
      this.operatorType = operatorType;
   }

   public OperatorType getOperatorType () {
      return operatorType;
   }

   public void setOperatorType (OperatorType operatorType) {
      this.operatorType = operatorType;
   }

   @Override
   protected Object clone () throws CloneNotSupportedException {
      TimeFrameConversionInputStructureList clonedTimeFrameConversionInputStructureList = (TimeFrameConversionInputStructureList) super
               .clone();
      List<TimeFrameConversionInputSourceInfo> clonedTimeFrameConversionInputSourceInfoList = new ArrayList<TimeFrameConversionInputSourceInfo>();
      for (TimeFrameConversionInputSourceInfo timeFrameConversionInputSourceInfo : timeFrameConversionInputSourceInfoList) {
         timeFrameConversionInputSourceInfoList
                  .add((TimeFrameConversionInputSourceInfo) timeFrameConversionInputSourceInfo.clone());
      }
      clonedTimeFrameConversionInputStructureList
               .setTimeFrameConversionInputSourceInfoList(clonedTimeFrameConversionInputSourceInfoList);
      clonedTimeFrameConversionInputStructureList
               .setTimeFrameConversionInputTargetInfo((TimeFrameConversionInputTargetInfo) timeFrameConversionInputTargetInfo
                        .clone());
      clonedTimeFrameConversionInputStructureList.setOperatorType(operatorType);
      return clonedTimeFrameConversionInputStructureList;
   }
}
