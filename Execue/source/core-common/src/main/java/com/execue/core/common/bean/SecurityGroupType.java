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

package com.execue.core.common.bean;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

public enum SecurityGroupType implements IBaseEnumType {
   ALL (1), USER_GROUP (2), PUBLIHSER_GROUP (3), ADV_PUBLISHER_GROUP (4);

   private Integer                                      value;
   private static final Map<Integer, SecurityGroupType> reverseLookupMap = new HashMap<Integer, SecurityGroupType>();
   private static String                                name             = SecurityGroupType.class.getSimpleName();

   static {
      for (SecurityGroupType statusEnum : EnumSet.allOf(SecurityGroupType.class)) {
         reverseLookupMap.put(statusEnum.value, statusEnum);
      }
   }

   SecurityGroupType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static SecurityGroupType getType (Integer value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
