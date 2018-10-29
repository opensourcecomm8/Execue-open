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


package com.execue.core.common.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.type.IBaseEnumType;

/**
 * This enum represents the cloud category type
 * 
 * @author Nitesh
 * @version 1.0
 * @since 15/04/10
 */
public enum ComponentCategory implements IBaseEnumType {
   TYPE (1), REALIZATION (2), BEHAVIOR_WITH_TYPE (3), ONLY_BEHAVIOR (4);

   private Integer                                      value;
   private static final Map<Integer, ComponentCategory> reverseLookupMap = new HashMap<Integer, ComponentCategory>();

   static {
      for (ComponentCategory jobType : EnumSet.allOf(ComponentCategory.class)) {
         reverseLookupMap.put(jobType.value, jobType);
      }
   }

   ComponentCategory (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static ComponentCategory getType (Integer value) {
      return reverseLookupMap.get(value);
   }
}
