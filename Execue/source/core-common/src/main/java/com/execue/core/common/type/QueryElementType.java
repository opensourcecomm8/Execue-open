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
 * This enum type used to represent the element types present inside a query
 * 
 * @author Vishay
 * @version 1.0
 */
public enum QueryElementType implements IBaseEnumType{

   SIMPLE_STRING ("Simple String"), CASE_STATEMENT ("Case Statement"), SUB_QUERY ("Sub Query");

   private String                                     value;
   private static final Map<String, QueryElementType> reverseLookupMap = new HashMap<String, QueryElementType>();
   private static String                              name             = QueryElementType.class.getSimpleName();

   static {
      for (QueryElementType elementType : EnumSet.allOf(QueryElementType.class)) {
         reverseLookupMap.put(elementType.value, elementType);
      }
   }

   QueryElementType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static QueryElementType getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
