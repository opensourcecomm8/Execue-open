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


package com.execue.core.common.bean.nlp;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.type.IBaseEnumType;

/**
 * @author Nitesh
 */
public enum RecognizedType implements IBaseEnumType {
   NUMBER_TYPE ("Digit"), DAY_TYPE ("Day"), MONTH_TYPE ("Month"), YEAR_TYPE ("Year"), QUARTER_TYPE ("Quarter"), VALUE_TYPE (
            "Value"), OPERATOR_TYPE ("Operator"), COMPARATIVE_STATISTICS_TYPE ("ComparativeStatistics"), UNIT_SCALE_TYPE (
            "UnitScale"), UNIT_SYMBOL_TYPE ("UnitSymbol"), UNIT_TYPE ("Unit"), TF_TYPE ("TimeFrame"), ADJECTIVE_TYPE (
            "Adjective"), VALUE_PREPOSITION ("ValuePreposition"), NAME_TYPE ("Name"), TIME_TYPE ("Time"), WEEK_TYPE (
            "Week"), WEEK_DAY_TYPE ("WeekDay"), TIME_QULAIFIER_TYPE ("TimeQualifier"), HOUR_TYPE ("Hour"), MINUTE_TYPE (
            "Minute"), SECOND_TYPE ("Second"), TIME_PREPOSITION ("TimePreposition"), CITY_TYPE ("City"), STATE_TYPE (
            "State"), COUNTRY_TYPE ("Country"), COUNTY_TYPE ("County");

   private String                                   value;
   private static final Map<String, RecognizedType> reverseLookupMap = new HashMap<String, RecognizedType>();

   static {
      for (RecognizedType cloudType : EnumSet.allOf(RecognizedType.class)) {
         reverseLookupMap.put(cloudType.value.toLowerCase(), cloudType);
      }
   }

   RecognizedType (String className) {
      this.value = className;
   }

   public String getValue () {
      return this.value;
   }

   public static RecognizedType getCloudType (String value) {
      return reverseLookupMap.get(value.toLowerCase());
   }
}
