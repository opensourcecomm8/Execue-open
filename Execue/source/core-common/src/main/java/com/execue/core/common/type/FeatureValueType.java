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

import com.execue.core.type.IBaseEnumType;

/**
 * @author Nitesh
 */
public enum FeatureValueType implements IBaseEnumType{

   VALUE_STRING ("S"), VALUE_NUMBER ("N"), VALUE_DUMMY ("D"), VALUE_DATE ("DT");

   private String                                     value;
   private static final Map<String, FeatureValueType> reverseLookupMap = new HashMap<String, FeatureValueType>();
   private static String                              name             = FeatureValueType.class.getSimpleName();

   static {
      for (FeatureValueType featureValueType : EnumSet.allOf(FeatureValueType.class)) {
         reverseLookupMap.put(featureValueType.value, featureValueType);
      }
   }

   FeatureValueType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static FeatureValueType getType (String value) {
      return reverseLookupMap.get(value);
   }

   // public String getDescription () {
   // return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   // }
}
