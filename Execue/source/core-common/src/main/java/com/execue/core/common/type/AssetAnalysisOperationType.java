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

public enum AssetAnalysisOperationType implements IBaseEnumType{
   LOOKUP_TABLE_WITHOUT_MEMBERS ("LookupTableWithoutMembers"), TABLES_WITHOUT_JOINS ("TablesWithoutJoins"), COLUMNS_WITH_MISSING_COLUMTYPE (
            "ColumnsWithMissingColumnType"), UNMAPPED_COLUMNS ("UnmappedColumns"), UNMAPPED_MEMBERS ("UnmappedMembers"), ASSET_WITHOUT_GRAIN (
            "AssetWithoutGrain"), TABLE_WITHOUT_DEFAULT_METRICS ("TableWithoutDefaultMetrics");

   private String                                               value;
   private static final Map<String, AssetAnalysisOperationType> reverseLookupMap = new HashMap<String, AssetAnalysisOperationType>();
   private static String                                        name             = AssetAnalysisOperationType.class
                                                                                          .getSimpleName();

   static {
      for (AssetAnalysisOperationType assetEntityType : EnumSet.allOf(AssetAnalysisOperationType.class)) {
         reverseLookupMap.put(assetEntityType.value, assetEntityType);
      }
   }

   AssetAnalysisOperationType (String value) {
      this.value = value;
   }

   public String getValue () {
      return this.value;
   }

   public static AssetAnalysisOperationType getType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
