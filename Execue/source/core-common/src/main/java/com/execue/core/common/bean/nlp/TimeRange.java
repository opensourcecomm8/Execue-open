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


package com.execue.core.common.bean.nlp;

import java.util.List;

import com.execue.core.util.ExecueStringUtil;

/**
 * Class to represent the TimeRange with start and end time mentioned . as of now used for common thing like
 * morning/evening.
 * 
 * @author Prasanna
 */
public class TimeRange {

   private String rangeName;
   private String startTime;
   private String endTime;
   private String startHour;
   private String startMinute;
   private String startSecond;
   private String startTimeQualifier;
   private String endHour;
   private String endMinute;
   private String endSecond;
   private String endTimeQualifier;

   public String getRangeName () {
      return rangeName;
   }

   public void setRangeName (String rangeName) {
      this.rangeName = rangeName;
   }

   public String getStartTime () {
      return startTime;
   }

   public void setStartTime (String startTime) {
      this.startTime = startTime;
   }

   public String getEndTime () {
      return endTime;
   }

   public void setEndTime (String endTime) {
      this.endTime = endTime;
   }

   public TimeRange () {

   }

   public TimeRange (String rangeName, String startTime, String endTime) {
      this.rangeName = rangeName;
      this.startTime = startTime;
      this.endTime = endTime;
      List<String> startTimeAndQualifier = ExecueStringUtil.getAsList(startTime, " ");
      String startTimeString = startTimeAndQualifier.get(0);
      startTimeQualifier = startTimeAndQualifier.get(1);
      List<String> startTimeList = ExecueStringUtil.getAsList(startTimeString, ":");
      startHour = startTimeList.get(0);
      if (startTimeList.size() > 1) {
         startMinute = startTimeList.get(1);
         if (startTimeList.size() > 2) {
            startSecond = startTimeList.get(2);
         }
      }
      List<String> endTimeAndQualifier = ExecueStringUtil.getAsList(endTime, " ");
      String endTimeString = endTimeAndQualifier.get(0);
      endTimeQualifier = endTimeAndQualifier.get(1);
      List<String> endTimeList = ExecueStringUtil.getAsList(endTimeString, ":");
      endHour = endTimeList.get(0);
      if (endTimeList.size() > 1) {
         endMinute = endTimeList.get(1);
         if (endTimeList.size() > 2) {
            endSecond = endTimeList.get(2);
         }
      }
   }

   public String getStartHour () {
      return startHour;
   }

   public void setStartHour (String startHour) {
      this.startHour = startHour;
   }

   public String getStartMinute () {
      return startMinute;
   }

   public void setStartMinute (String startMinute) {
      this.startMinute = startMinute;
   }

   public String getStartSecond () {
      return startSecond;
   }

   public void setStartSecond (String startSecond) {
      this.startSecond = startSecond;
   }

   public String getStartTimeQualifier () {
      return startTimeQualifier;
   }

   public void setStartTimeQualifier (String startTimeQualifier) {
      this.startTimeQualifier = startTimeQualifier;
   }

   public String getEndHour () {
      return endHour;
   }

   public void setEndHour (String endHour) {
      this.endHour = endHour;
   }

   public String getEndMinute () {
      return endMinute;
   }

   public void setEndMinute (String endMinute) {
      this.endMinute = endMinute;
   }

   public String getEndSecond () {
      return endSecond;
   }

   public void setEndSecond (String endSecond) {
      this.endSecond = endSecond;
   }

   public String getEndTimeQualifier () {
      return endTimeQualifier;
   }

   public void setEndTimeQualifier (String endTimeQualifier) {
      this.endTimeQualifier = endTimeQualifier;
   }

}
