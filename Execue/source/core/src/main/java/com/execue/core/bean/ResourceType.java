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


package com.execue.core.bean;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

public enum ResourceType implements IBaseEnumType {
   ACTION ('A'), LINK_RESOURCE ('L');

   private Character                                 value;
   private static final Map<Character, ResourceType> reverseLookupMap = new HashMap<Character, ResourceType>();
   private static String                             name             = ResourceType.class.getSimpleName();

   static {
      for (ResourceType resource : EnumSet.allOf(ResourceType.class)) {
         reverseLookupMap.put(resource.value, resource);
      }
   }

   ResourceType (Character value) {
      this.value = value;
   }

   public Character getValue () {
      return this.value;
   }

   public static ResourceType getType (Character value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
