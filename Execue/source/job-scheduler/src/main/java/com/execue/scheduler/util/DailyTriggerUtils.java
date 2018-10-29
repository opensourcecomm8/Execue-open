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

public class DailyTriggerUtils {

   /**
    * Make a trigger that will fire every day at the given time. The generated trigger will not have its name, group, or
    * end-time set. The Start time defaults to 'now'. Parameters: hour - the hour (0-23) upon which to fire minute - the
    * minute (0-59) upon which to fire Returns: the new trigger
    * 
    * @param hour
    * @param minute
    * @return
    */
   public static Trigger makeDailyTrigger (int hour, int minute) {
      return TriggerUtils.makeDailyTrigger(hour, minute);
   }

   /**
    * Make a trigger that will fire every day at the given time. The generated trigger will not have its group or
    * end-time set. The Start time defaults to 'now'. Parameters: trigName - the trigger's name hour - the hour (0-23)
    * upon which to fire minute - the minute (0-59) upon which to fire Returns: the newly created trigger
    * 
    * @param trigName
    * @param hour
    * @param minute
    * @return
    */
   public static Trigger makeDailyTrigger (String trigName, int hour, int minute) {
      return TriggerUtils.makeDailyTrigger(trigName, hour, minute);
   }

}
