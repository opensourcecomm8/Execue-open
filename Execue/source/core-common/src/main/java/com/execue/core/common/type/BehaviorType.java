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


/**
 * 
 */
package com.execue.core.common.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

/**
 * @author Nitesh
 */
public enum BehaviorType implements IBaseEnumType {
   ABSTRACT (9001), ATTRIBUTE (9002), QUANTITATIVE (9003), ENUMERATION (9004), POPULATION (9005), COMPARATIVE (9006), GRAIN (
            9007), DISTRIBUTION (9008), MUTUALYEXCLUSIVE (9009), INDICATOR (9010), MULTIVALUED (9011), MULTI_VALUED_GLOBAL_PENALTY (
            9012), DEPENDENT_VARIABLE (9013);

   private Integer                                 value;
   private static final Map<Integer, BehaviorType> reverseLookupMap = new HashMap<Integer, BehaviorType>();
   private static String                           name             = BehaviorType.class.getSimpleName();

   static {
      for (BehaviorType behaviorType : EnumSet.allOf(BehaviorType.class)) {
         reverseLookupMap.put(behaviorType.value, behaviorType);
      }
   }

   BehaviorType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static BehaviorType getType (Integer value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}