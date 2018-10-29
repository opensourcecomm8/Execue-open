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

/**
 * @author John Mallavalli
 */
public enum RFXObjectType implements IBaseEnumType{
   DOMAIN_ENTITY_DEFINITION ("D"), VALUE ("V"), LIST ("L"), RANGE ("R");

   private String                                  value;
   private static final Map<String, RFXObjectType> reverseLookupMap = new HashMap<String, RFXObjectType>();
   private static String                           name             = RFXObjectType.class.getSimpleName();

   static {
      for (RFXObjectType rfxObjectType : EnumSet.allOf(RFXObjectType.class)) {
         reverseLookupMap.put(rfxObjectType.value, rfxObjectType);
      }
   }

   RFXObjectType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static RFXObjectType getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
