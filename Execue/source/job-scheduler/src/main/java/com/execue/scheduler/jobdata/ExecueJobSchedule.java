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


package com.execue.scheduler.jobdata;

import java.io.Serializable;
import java.util.Date;

public class ExecueJobSchedule implements Serializable {

   /**
    * 
    */
   private static final long serialVersionUID = -2153899958939935288L;
   /*
    * ExecueJobSchedule has the trigger information
    */

   private int               periodicity;                             // hourly, daily, weekly, monthly
   private int               noOfRepeats;                             // no. of times this job should get triggered
   private boolean           isIndefinite;                            // is the job scheduled to execute indefinitely
   private int               intervalInMinutes;

   private Date              startTime;
   private Date              endTime;

   // HOURLY - every hour
   // DAILY - everyday at 9am
   // WEEKLY - every MONDAY at 9am
   // MONTHLY - MONDAY of every month at 9am

   private int               dayOfMonth;
   private int               dayOfWeek;
   private int               hourOfDay;
   private int               minuteOfHour;
   private String            am_pm;
   private int               everyNthHour;

   private String            seedOperation;

   public int getDayOfWeek () {
      return dayOfWeek;
   }

   public void setDayOfWeek (int dayOfWeek) {
      this.dayOfWeek = dayOfWeek;
   }

   public Date getEndTime () {
      return endTime;
   }

   public void setEndTime (Date endTime) {
      this.endTime = endTime;
   }

   public int getHourOfDay () {
      return hourOfDay;
   }

   public void setHourOfDay (int hourOfDay) {
      this.hourOfDay = hourOfDay;
   }

   public boolean isIndefinite () {
      return isIndefinite;
   }

   public void setIndefinite (boolean isIndefinite) {
      this.isIndefinite = isIndefinite;
   }

   public int getNoOfRepeats () {
      return noOfRepeats;
   }

   public void setNoOfRepeats (int noOfRepeats) {
      this.noOfRepeats = noOfRepeats;
   }

   public int getPeriodicity () {
      return periodicity;
   }

   public void setPeriodicity (int periodicity) {
      this.periodicity = periodicity;
   }

   public Date getStartTime () {
      return startTime;
   }

   public void setStartTime (Date startTime) {
      this.startTime = startTime;
   }

   public String getAm_pm () {
      return am_pm;
   }

   public void setAm_pm (String am_pm) {
      this.am_pm = am_pm;
   }

   public int getMinuteOfHour () {
      return minuteOfHour;
   }

   public void setMinuteOfHour (int minuteOfHour) {
      this.minuteOfHour = minuteOfHour;
   }

   public int getDayOfMonth () {
      return dayOfMonth;
   }

   public void setDayOfMonth (int dayOfMonth) {
      this.dayOfMonth = dayOfMonth;
   }

   public int getEveryNthHour () {
      return everyNthHour;
   }

   public void setEveryNthHour (int everyNthHour) {
      this.everyNthHour = everyNthHour;
   }

   public String getSeedOperation () {
      return seedOperation;
   }

   public void setSeedOperation (String seedOperation) {
      this.seedOperation = seedOperation;
   }

   /**
    * @return the intervalInMinutes
    */
   public int getIntervalInMinutes () {
      return intervalInMinutes;
   }

   /**
    * @param intervalInMinutes
    *           the intervalInMinutes to set
    */
   public void setIntervalInMinutes (int intervalInMinutes) {
      this.intervalInMinutes = intervalInMinutes;
   }

}
