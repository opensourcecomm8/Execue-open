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

public class HourlyTriggerUtils {

   /**
    * Make a trigger that will fire every hour, indefinitely. The generated trigger will not have its group, or end-time
    * set. The Start time defaults to 'now'. Parameters: trigName - the trigger's name Returns: the new trigger
    * 
    * @param trigName
    * @return
    */
   public static Trigger makeHourlyTrigger (String trigName) {
      return TriggerUtils.makeHourlyTrigger(trigName);
   }

   /**
    * Make a trigger that will fire every N hours, indefinitely. The generated trigger will not have its name, group, or
    * end-time set. The Start time defaults to 'now'. The generated trigger can be assigned an identity using the
    * TriggerUtils setTriggerIdentity functions Parameters: intervalInHours - the number of hours between firings
    * Returns: the new trigger
    * 
    * @param intervalInHours
    * @return
    */
   public static Trigger makeHourlyTrigger (int intervalInHours) {
      return TriggerUtils.makeHourlyTrigger(intervalInHours);
   }

   /**
    * Make a trigger that will fire every N hours, with the given number of repeats. The generated trigger will not have
    * its name, group, or end-time set. The Start time defaults to 'now'. Parameters: intervalInHours - the number of
    * hours between firings repeatCount - the number of times to repeat the firing Returns: the new trigger
    * 
    * @param intervalInHours
    * @param repeatCount
    * @return
    */
   public static Trigger makeHourlyTrigger (int intervalInHours, int repeatCount) {
      return TriggerUtils.makeHourlyTrigger(intervalInHours, repeatCount);
   }

   /**
    * Make a trigger that will fire every N hours, with the given number of repeats. The generated trigger will not have
    * its group, or end-time set. The Start time defaults to 'now'. Parameters: trigName - the trigger's name
    * intervalInHours - the number of hours between firings repeatCount - the number of times to repeat the firing
    * Returns: the new trigger
    * 
    * @param trigName
    * @param intervalInHours
    * @param repeatCount
    * @return
    */
   public static Trigger makeHourlyTrigger (String trigName, int intervalInHours, int repeatCount) {
      return TriggerUtils.makeHourlyTrigger(trigName, intervalInHours, repeatCount);
   }

}
