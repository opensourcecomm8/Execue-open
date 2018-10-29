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

public class MinutelyTriggerUtils {

   /**
    * Make a trigger that will fire every minute, indefinitely. The generated trigger will not have its name, group, or
    * end-time set. The Start time defaults to 'now'. Returns: the new trigger
    * 
    * @return
    */
   public static Trigger makeMinutelyTrigger () {
      return TriggerUtils.makeMinutelyTrigger();
   }

   /**
    * Make a trigger that will fire every minute, indefinitely. The generated trigger will not have its group, or
    * end-time set. The Start time defaults to 'now'. Parameters: trigName - the trigger's name Returns: the new trigger
    * 
    * @param trigName
    * @return
    */
   public static Trigger makeMinutelyTrigger (String trigName) {
      return TriggerUtils.makeMinutelyTrigger(trigName);
   }

   /**
    * Make a trigger that will fire every N minutes, indefinitely. The generated trigger will not have its name, group,
    * or end-time set. The Start time defaults to 'now'. Parameters: intervalInMinutes - the number of minutes between
    * firings Returns: the new trigger
    * 
    * @param intervalInMinutes
    * @return
    */
   public static Trigger makeMinutelyTrigger (int intervalInMinutes) {
      return TriggerUtils.makeMinutelyTrigger(intervalInMinutes);
   }

   /**
    * Make a trigger that will fire every N minutes, with the given number of repeats. The generated trigger will not
    * have its name, group, or end-time set. The Start time defaults to 'now'. Parameters: intervalInMinutes - the
    * number of minutes between firings repeatCount - the number of times to repeat the firing Returns: the new trigger
    * 
    * @param intervalInMinutes
    * @param repeatCount
    * @return
    */
   public static Trigger makeMinutelyTrigger (int intervalInMinutes, int repeatCount) {
      return TriggerUtils.makeMinutelyTrigger(intervalInMinutes, repeatCount);
   }

   /**
    * Make a trigger that will fire every N minutes, with the given number of repeats. The generated trigger will not
    * have its group, or end-time set. The Start time defaults to 'now'. Parameters: trigName - the trigger's name
    * intervalInMinutes - the number of minutes between firings repeatCount - the number of times to repeat the firing
    * Returns: the new trigger
    * 
    * @param trigName
    * @param intervalInMinutes
    * @param repeatCount
    * @return
    */
   public static Trigger makeMinutelyTrigger (String trigName, int intervalInMinutes, int repeatCount) {
      return TriggerUtils.makeMinutelyTrigger(trigName, intervalInMinutes, repeatCount);
   }

}
