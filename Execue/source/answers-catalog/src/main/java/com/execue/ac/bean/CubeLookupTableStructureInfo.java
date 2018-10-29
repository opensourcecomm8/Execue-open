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

import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.type.LookupType;

/**
 * This bean represents the lookup table structure. Flag indicates whether parent table exists in swi as in case of
 * range it might or might not exist. In stat case, it never exists
 * 
 * @author Vishay
 * @version 1.0
 * @since 10/03/2011
 */
public class CubeLookupTableStructureInfo {

   private String               tableName;
   private QueryColumn          lookupValueColumn;
   private QueryColumn          lookupDescColumn;
   private QueryColumn          lowerLimitColumn;
   private QueryColumn          upperLimitColumn;
   private LookupType           lookupType;
   private ConceptColumnMapping sourceConceptColumnMapping;
   private boolean              parentTableExistsInSWI;

   public String getTableName () {
      return tableName;
   }

   public void setTableName (String tableName) {
      this.tableName = tableName;
   }

   public QueryColumn getLookupDescColumn () {
      return lookupDescColumn;
   }

   public void setLookupDescColumn (QueryColumn lookupDescColumn) {
      this.lookupDescColumn = lookupDescColumn;
   }

   public QueryColumn getLowerLimitColumn () {
      return lowerLimitColumn;
   }

   public void setLowerLimitColumn (QueryColumn lowerLimitColumn) {
      this.lowerLimitColumn = lowerLimitColumn;
   }

   public QueryColumn getUpperLimitColumn () {
      return upperLimitColumn;
   }

   public void setUpperLimitColumn (QueryColumn upperLimitColumn) {
      this.upperLimitColumn = upperLimitColumn;
   }

   public LookupType getLookupType () {
      return lookupType;
   }

   public void setLookupType (LookupType lookupType) {
      this.lookupType = lookupType;
   }

   public QueryColumn getLookupValueColumn () {
      return lookupValueColumn;
   }

   public void setLookupValueColumn (QueryColumn lookupValueColumn) {
      this.lookupValueColumn = lookupValueColumn;
   }

   public ConceptColumnMapping getSourceConceptColumnMapping () {
      return sourceConceptColumnMapping;
   }

   public void setSourceConceptColumnMapping (ConceptColumnMapping sourceConceptColumnMapping) {
      this.sourceConceptColumnMapping = sourceConceptColumnMapping;
   }

   public boolean isParentTableExistsInSWI () {
      return parentTableExistsInSWI;
   }

   public void setParentTableExistsInSWI (boolean parentTableExistsInSWI) {
      this.parentTableExistsInSWI = parentTableExistsInSWI;
   }

}
