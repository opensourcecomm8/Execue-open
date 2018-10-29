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

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

public enum ConstraintSubType implements IBaseEnumType {
   ENTIRE ('E'), PART ('P');

   private Character                                      value;
   private static final Map<Character, ConstraintSubType> reverseLookupMap = new HashMap<Character, ConstraintSubType>();
   private static String                                  name             = ConstraintSubType.class.getSimpleName();

   static {
      for (ConstraintSubType constraintSubType : EnumSet.allOf(ConstraintSubType.class)) {
         reverseLookupMap.put(constraintSubType.value, constraintSubType);
      }
   }

   ConstraintSubType (Character value) {
      this.value = value;
   }

   public Character getValue () {
      return this.value;
   }

   public static ConstraintSubType getType (Character value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
