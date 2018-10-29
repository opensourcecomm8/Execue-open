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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.execue.core.common.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

/**
 * Use the enum if your String variable is of boolean type
 * 
 * @author kaliki
 * @since 4.0
 */
public enum BooleanType implements IBaseEnumType {
   YES ('Y'), NO ('N');

   private Character                                value;
   private static final Map<Character, BooleanType> reverseLookupMap = new HashMap<Character, BooleanType>();
   private static String                            name             = BooleanType.class.getSimpleName();

   static {
      for (BooleanType checkType : EnumSet.allOf(BooleanType.class)) {
         reverseLookupMap.put(checkType.value, checkType);
      }
   }

   BooleanType (Character value) {
      this.value = value;
   }

   public Character getValue () {
      return this.value;
   }

   public static BooleanType getType (Character value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}