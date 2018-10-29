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
 * @author kaliki
 * @since 4.0
 */
public enum ColumnType implements IBaseEnumType {
   NULL ("NULL"), ID ("ID"), MEASURE ("MEASURE"), DIMENSION ("DIMENSION"), SIMPLE_LOOKUP ("SL"), RANGE_LOOKUP ("RL"), DEDUCED_DIMENSION (
            "DD"), NON_DEDUCABLE ("ND"), SIMPLE_HIERARCHY_LOOKUP ("SHL");

   private String                               value;
   private static final Map<String, ColumnType> reverseLookupMap = new HashMap<String, ColumnType>();
   private static String                        name             = ColumnType.class.getSimpleName();

   static {
      for (ColumnType columnType : EnumSet.allOf(ColumnType.class)) {
         reverseLookupMap.put(columnType.value, columnType);
      }
   }

   ColumnType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static ColumnType getColumnType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }

}
