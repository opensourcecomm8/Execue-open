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


package com.execue.core.configuration;

import java.util.Map;

public class EnumLookupHelper {

   /**
    * key - concatenation of enum type class name and value, ~#~ as the separator
    * value - description fetched from DB [ENUM_LOOKUP table]
    */
   private static Map<String, String> enumLookupDescriptionMap;
   private static String              enumTypeValueSeparator;

   private EnumLookupHelper () {

   }

   public static void setEnumLookupDescriptionMap (Map<String, String> enumLookupDescriptionMap) {
      EnumLookupHelper.enumLookupDescriptionMap = enumLookupDescriptionMap;
   }

   public static Map<String, String> getEnumLookupDescriptionMap () {
      return enumLookupDescriptionMap;
   }

   public static String getEnumLookupDescription (String type, String value) {
      return getEnumLookupDescriptionMap().get(type + enumTypeValueSeparator + value);
   }

   public static String getEnumTypeValueSeparator () {
      return enumTypeValueSeparator;
   }

   public static void setEnumTypeValueSeparator (String enumTypeValueSeparator) {
      EnumLookupHelper.enumTypeValueSeparator = enumTypeValueSeparator;
   }

}
