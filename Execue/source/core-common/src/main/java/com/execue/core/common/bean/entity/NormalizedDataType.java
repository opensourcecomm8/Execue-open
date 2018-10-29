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


/**
 * 
 */
package com.execue.core.common.bean.entity;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.type.IBaseEnumType;

/**
 * @author Nitesh
 */
public enum NormalizedDataType implements IBaseEnumType {

   TIME_FRAME_NORMALIZED_DATA ("com.execue.core.common.bean.nlp.TimeFrameNormalizedData"), VALUE_NORMALIZED_DATA (
            "com.execue.core.common.bean.nlp.ValueRealizationNormalizedData"), UNIT_NORMALIZED_DATA (
            "com.execue.core.common.bean.nlp.UnitNormalizedData"), RANGE_NORMALIZED_DATA (
            "com.execue.core.common.bean.nlp.RangeNormalizedData"), RELATIVE_TIME_NORMALIZED_DATA (
            "com.execue.core.common.bean.nlp.RelativeTimeNormalizedData"), LIST_NORMALIZED_DATA (
            "com.execue.core.common.bean.nlp.ListNormalizedData"), DEFAULT_NORMALIZED_DATA (
            "com.execue.core.common.bean.nlp.DefaultNormalizedData"), COMPARATIVE_INFO_NORMALIZED_DATA (
            "com.execue.core.common.bean.nlp.ComparativeInfoNormalizedData"), RELATIVE_NORMALIZED_DATA (
            "com.execue.core.common.bean.nlp.RelativeNormalizedData"), WEEK_NORMALIZED_DATA (
            "com.execue.core.common.bean.nlp.WeekNormalizedData"), WEEK_DAY_NORMALIZED_DATA (
            "com.execue.core.common.bean.nlp.WeekDayNormalizedDataComponent"), LOCATION_NORMALIZED_DATA (
            "com.execue.core.common.bean.nlp.LocationNormalizedData");

   private String                                       value;
   private static final Map<String, NormalizedDataType> reverseLookupMap = new HashMap<String, NormalizedDataType>();

   static {
      for (NormalizedDataType cloudType : EnumSet.allOf(NormalizedDataType.class)) {
         reverseLookupMap.put(cloudType.value, cloudType);
      }
   }

   NormalizedDataType (String className) {
      this.value = className;
   }

   public String getValue () {
      return this.value;
   }

   public static NormalizedDataType getCloudType (String value) {
      return reverseLookupMap.get(value);
   }
}
