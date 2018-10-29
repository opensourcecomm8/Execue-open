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

public class WeeklyTriggerUtils {

   /**
    * Make a trigger that will fire every week at the given day and time. The generated trigger will not have its name,
    * group, or end-time set. The Start time defaults to 'now'. Parameters: dayOfWeek - (1-7) the day of week upon which
    * to fire hour - the hour (0-23) upon which to fire minute - the minute (0-59) upon which to fire Returns: the new
    * trigger See Also: SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    * 
    * @param dayOfWeek
    * @param hour
    * @param minute
    * @return
    */
   public static Trigger makeWeeklyTrigger (int dayOfWeek, int hour, int minute) {
      return TriggerUtils.makeWeeklyTrigger(dayOfWeek, hour, minute);
   }

   /**
    * Make a trigger that will fire every week at the given day and time. The generated trigger will not have its group,
    * or end-time set. The Start time defaults to 'now'. Parameters: trigName - the trigger's name dayOfWeek - (1-7) the
    * day of week upon which to fire hour - the hour (0-23) upon which to fire minute - the minute (0-59) upon which to
    * fire Returns: the newly created trigger See Also: SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    * 
    * @param trigName
    * @param dayOfWeek
    * @param hour
    * @param minute
    * @return
    */
   public static Trigger makeWeeklyTrigger (String trigName, int dayOfWeek, int hour, int minute) {
      return TriggerUtils.makeWeeklyTrigger(trigName, dayOfWeek, hour, minute);
   }
}
