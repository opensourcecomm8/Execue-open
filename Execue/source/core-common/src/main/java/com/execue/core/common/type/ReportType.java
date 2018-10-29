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
 * @author Vishy
 */
public enum ReportType implements IBaseEnumType {
   Grid (1), BarChart (2), LineChart (3), PivotTable (4), GroupTable (5), CrossTable (6), CrossBarChart (7), CrossLineChart (
            8), HierarchyChart (9), BarLineChart (10), MultiBarChart (11), ClusterBarChart (12), CMultiBarChart (13), MultiLineChart (
            14), ClusterLineChart (15), CMultiLineChart (16), MultiLineClusterChart (17), CMMultiBarChart (18), CMMultiLineChart (
            19), DetailGrid (20), DetailGroupTable (21), StockChart (22), PortraitTable (23), ClusterColumnGrid (24), CountryMapChart (
            80), CrossStateMapChart (81), DetailCsvFile (98), CsvFile (99), ClusterPieChart (50), PieChart (25), HierarchicalGrid (
            60), ScatterChart (26);

   private Integer                               value;
   private static final Map<Integer, ReportType> reverseLookupMap = new HashMap<Integer, ReportType>();
   private static String                         name             = ReportType.class.getSimpleName();

   static {
      for (ReportType type : EnumSet.allOf(ReportType.class)) {
         reverseLookupMap.put(type.value, type);
      }
   }

   ReportType (Integer value) {
      this.value = value;
   }

   public Integer getValue () {
      return this.value;
   }

   public static ReportType getType (Integer value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
