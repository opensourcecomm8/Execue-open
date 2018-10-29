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

public enum UdxCarsInfoSortType implements IBaseEnumType{
   MAKE_ASC ("MAKE_NAME##ASC##N"), MODEL_ASC ("MODEL_NAME##ASC##N"), MODEL_YEAR_ASC ("MODEL_YEAR##ASC##Y"), MODEL_YEAR_DESC (
            "MODEL_YEAR##DESC##Y"), PRICE_ASC ("PRICE##ASC##Y"), PRICE_DESC ("PRICE##DESC##Y"), MILEAGE_ASC (
            "MILEAGE##ASC##Y"), MILEAGE_DESC ("MILEAGE##DESC##Y"), DISTANCE_ASC ("DISTANCE##ASC##N"), DISTANCE_DESC (
            "DISTANCE##DESC##N"), UDX_CONTENT_DATE_DESC ("UDX_CONTENT_DATE##DESC##N");

   private String                                        value;
   private static final Map<String, UdxCarsInfoSortType> reverseLookupMap = new HashMap<String, UdxCarsInfoSortType>();
   private static String                                 name             = UdxCarsInfoSortType.class.getSimpleName();

   static {
      for (UdxCarsInfoSortType udxCarsInfoSortType : EnumSet.allOf(UdxCarsInfoSortType.class)) {
         reverseLookupMap.put(udxCarsInfoSortType.value, udxCarsInfoSortType);
      }
   }

   UdxCarsInfoSortType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static UdxCarsInfoSortType getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
