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
 * This enum represents the LocationType
 * 
 * @author MurthySN
 * @version 1.0
 * @since 27/09/11
 */
public enum LocationType implements IBaseEnumType{
   LAT_LONG (1), ZIPCODE (2), CITY (3), STATE (4), COUNTRY (5), COUNTY (6);

   private Integer                                 value;
   private static final Map<Integer, LocationType> reverseLookupMap = new HashMap<Integer, LocationType>();

   static {
      for (LocationType locationType : EnumSet.allOf(LocationType.class)) {
         reverseLookupMap.put(locationType.value, locationType);
      }
   }

   LocationType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static LocationType getType (Integer value) {
      return reverseLookupMap.get(value);
   }
}
