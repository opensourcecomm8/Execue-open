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
 * This enum represents the query section types
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public enum QuerySectionType implements IBaseEnumType {
   SELECT ("SELECT"), GROUP ("GROUP"), CONDITION ("CONDITION"), ORDER ("ORDER"), HAVING ("HAVING"), POPULATION (
            "POPULATION"), TOP_BOTTOM ("TOP_BOTTOM"), COHORT_CONDITION ("COHORT_CONDITION"), COHORT_GROUP (
            "COHORT_GROUP");

   private String                                     value;
   private static final Map<String, QuerySectionType> reverseLookupMap = new HashMap<String, QuerySectionType>();
   private static String                              name             = QuerySectionType.class.getSimpleName();

   static {
      for (QuerySectionType querySectionType : EnumSet.allOf(QuerySectionType.class)) {
         reverseLookupMap.put(querySectionType.value, querySectionType);
      }
   }

   QuerySectionType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static QuerySectionType getQuerySectionType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
