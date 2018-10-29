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


package com.execue.ac.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * This bean represents the list of lookup tables created for cube
 * 
 * @author Vishay
 * @version 1.0
 * @since 22/03/2011
 */
public class CubeLookupTableStructure {

   private List<CubeLookupTableStructureInfo> simpleLookupTables;
   private List<CubeLookupTableStructureInfo> rangeLookupTables;
   private CubeLookupTableStructureInfo       statLookupTable;

   public List<CubeLookupTableStructureInfo> getSimpleLookupTables () {
      return simpleLookupTables;
   }

   public void setSimpleLookupTables (List<CubeLookupTableStructureInfo> simpleLookupTables) {
      this.simpleLookupTables = simpleLookupTables;
   }

   public List<CubeLookupTableStructureInfo> getRangeLookupTables () {
      return rangeLookupTables;
   }

   public void setRangeLookupTables (List<CubeLookupTableStructureInfo> rangeLookupTables) {
      this.rangeLookupTables = rangeLookupTables;
   }

   public CubeLookupTableStructureInfo getStatLookupTable () {
      return statLookupTable;
   }

   public void setStatLookupTable (CubeLookupTableStructureInfo statLookupTable) {
      this.statLookupTable = statLookupTable;
   }

   public List<CubeLookupTableStructureInfo> getCombinedTablesExceptStat () {
      List<CubeLookupTableStructureInfo> combinedTablesExceptStat = new ArrayList<CubeLookupTableStructureInfo>();
      combinedTablesExceptStat.addAll(getSimpleLookupTables());
      combinedTablesExceptStat.addAll(getRangeLookupTables());
      return combinedTablesExceptStat;
   }

   public List<CubeLookupTableStructureInfo> getCombinedTables () {
      List<CubeLookupTableStructureInfo> combinedTables = new ArrayList<CubeLookupTableStructureInfo>();
      combinedTables.addAll(getSimpleLookupTables());
      combinedTables.addAll(getRangeLookupTables());
      combinedTables.add(getStatLookupTable());
      return combinedTables;
   }

}
