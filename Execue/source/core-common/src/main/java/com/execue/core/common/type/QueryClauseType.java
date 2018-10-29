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
public enum QueryClauseType implements IBaseEnumType {

   SELECT ("select"), FROM ("from"), GROUPBY ("groupBy"), ORDERBY ("orderBy"), WHERE ("where"), HAVING ("having"), JOIN (
            "join"), LIMIT ("limit");

   private String                                    value;
   private static final Map<String, QueryClauseType> reverseLookupMap = new HashMap<String, QueryClauseType>();
   private static String                             name             = QueryClauseType.class.getSimpleName();

   static {
      for (QueryClauseType elementType : EnumSet.allOf(QueryClauseType.class)) {
         reverseLookupMap.put(elementType.value, elementType);
      }
   }

   QueryClauseType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static QueryClauseType getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
