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


package com.execue.scheduler.util;

import org.quartz.Trigger;
import org.quartz.TriggerUtils;

public class MonthlyTriggerUtils {

   /**
    * Make a trigger that will fire every month at the given day and time. The generated trigger will not have its name,
    * group, or end-time set. The Start time defaults to 'now'. If the day of the month specified does not occur in a
    * given month, a firing will not occur that month. (i.e. if dayOfMonth is specified as 31, no firing will occur in
    * the months of the year with fewer than 31 days). Parameters: dayOfMonth - (1-31, or -1) the day of week upon which
    * to fire hour - the hour (0-23) upon which to fire minute - the minute (0-59) upon which to fire Returns: the newly
    * created trigger
    * 
    * @param dayOfMonth
    * @param hour
    * @param minute
    * @return
    */
   public static Trigger makeMonthlyTrigger (int dayOfMonth, int hour, int minute) {
      return TriggerUtils.makeMonthlyTrigger(dayOfMonth, hour, minute);
   }

   /**
    * Make a trigger that will fire every month at the given day and time. The generated trigger will not have its
    * group, or end-time set. The Start time defaults to 'now'. If the day of the month specified does not occur in a
    * given month, a firing will not occur that month. (i.e. if dayOfMonth is specified as 31, no firing will occur in
    * the months of the year with fewer than 31 days). Parameters: trigName - the trigger's name dayOfMonth - (1-31, or
    * -1) the day of week upon which to fire hour - the hour (0-23) upon which to fire minute - the minute (0-59) upon
    * which to fire Returns: the newly created trigger
    * 
    * @param trigName
    * @param dayOfMonth
    * @param hour
    * @param minute
    * @return
    */
   public static Trigger makeMonthlyTrigger (String trigName, int dayOfMonth, int hour, int minute) {
      return TriggerUtils.makeMonthlyTrigger(trigName, dayOfMonth, hour, minute);
   }

}
