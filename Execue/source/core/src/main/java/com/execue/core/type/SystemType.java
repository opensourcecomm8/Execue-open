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


package com.execue.core.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;

/**
 * 
 * Indicated which type of system that is being initiated
 * WEBAPP indicates a J2EE based web application
 * BATCH indicates a command line invocation
 * 
 * @author gopal
 *
 */
public enum SystemType implements IBaseEnumType {

   WEBAPP (1), BATCH(0);
   
   private Integer                                     value;
   private static final Map<Integer, SystemType> reverseLookupMap = new HashMap<Integer, SystemType>();
   private static String                               name             = SystemType.class.getSimpleName();

   static {
      for (SystemType systemType : EnumSet.allOf(SystemType.class)) {
         reverseLookupMap.put(systemType.value, systemType);
      }
   }

   SystemType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static SystemType getType (Integer value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }

}
