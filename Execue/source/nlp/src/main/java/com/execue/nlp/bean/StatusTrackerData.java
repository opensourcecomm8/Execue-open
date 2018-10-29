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


package com.execue.nlp.bean;

import java.util.HashMap;
import java.util.Map;

import com.execue.core.common.bean.algorithm.BaseBean;

/**
 * @author Abhijit
 * @since Oct 15, 2008 : 11:23:08 AM
 */
public class StatusTrackerData extends BaseBean {

   protected Map<String, Boolean> statusFlags = new HashMap<String, Boolean>();

   public Map<String, Boolean> getStatusFlags () {
      return statusFlags;
   }

   public void setStatusFlags (Map<String, Boolean> statusFlags) {
      this.statusFlags = statusFlags;
   }

   // Utility Methods

   /**
    * Get the value of flag specified by flag name
    * 
    * @param flagName
    *           Name of the flag for which true or false value is needed
    * @return TRUE or FALSE
    */
   public boolean getStatus (String flagName) {
      return this.statusFlags.containsKey(flagName) && this.statusFlags.get(flagName);
   }

   /**
    * Remove the flag from the Status
    * 
    * @param flagName
    *           Name of the flag to be removed
    */
   public void removeStatusFlag (String flagName) {
      this.statusFlags.remove(flagName);
   }

   /**
    * Set the flag named by flag name to TRUE
    * 
    * @param flagName
    *           name of the flag for which boolean value would be set
    */
   public void setStatus (String flagName) {
      this.statusFlags.put(flagName, true);
   }

   public void setStatus (String flagName, Boolean val) {
      this.statusFlags.put(flagName, val);
   }

   // Overridden methods

   @Override
   public Object clone () throws CloneNotSupportedException {
      StatusTrackerData data = new StatusTrackerData();
      data.setStatusFlags((Map<String, Boolean>) ((HashMap<String, Boolean>) this.statusFlags).clone());
      return super.clone();
   }
}
