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
 * @author Raju Gottumukkala
 */
public enum BusinessEntityType implements IBaseEnumType {
   CONCEPT ("C"), CONCEPT_LOOKUP_INSTANCE ("CLI"), RELATION ("R"), CONCEPT_RELATION ("CR"), REGEX_INSTANCE ("RI"), PROFILE (
            "PRF"), CONCEPT_PROFILE ("CP"), INSTANCE_PROFILE ("IP"), FORMULA ("F"), TYPE ("T"), TYPE_LOOKUP_INSTANCE (
            "TLI"), REALIZED_TYPE ("RT"), REALIZED_TYPE_LOOKUP_INSTANCE ("RTLI"), BEHAVIOR ("B");

   private String                                       value;
   private static final Map<String, BusinessEntityType> reverseLookupMap = new HashMap<String, BusinessEntityType>();
   private static String                                name             = BusinessEntityType.class.getSimpleName();

   static {
      for (BusinessEntityType businessEntityType : EnumSet.allOf(BusinessEntityType.class)) {
         reverseLookupMap.put(businessEntityType.value, businessEntityType);
      }
   }

   BusinessEntityType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static BusinessEntityType getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
