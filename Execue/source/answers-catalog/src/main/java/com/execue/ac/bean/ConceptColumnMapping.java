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

import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;

/**
 * This class contains mapping between concept and column.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class ConceptColumnMapping {

   private Mapping                  mapping;
   private AssetEntityDefinition    assetEntityDefinition;
   private BusinessEntityDefinition businessEntityDefinition;
   private Concept                  concept;
   private Colum                    column;
   private Tabl                     tabl;
   private QueryColumn              queryColumn;
   private QueryTable               queryTable;
   private QueryTableColumn         queryTableColumn;
   private Range                    rangeDefinition;

   public QueryTableColumn getQueryTableColumn () {
      return queryTableColumn;
   }

   public void setQueryTableColumn (QueryTableColumn queryTableColumn) {
      this.queryTableColumn = queryTableColumn;
   }

   public Mapping getMapping () {
      return mapping;
   }

   public void setMapping (Mapping mapping) {
      this.mapping = mapping;
   }

   public Tabl getTabl () {
      return tabl;
   }

   public void setTabl (Tabl tabl) {
      this.tabl = tabl;
   }

   public QueryColumn getQueryColumn () {
      return queryColumn;
   }

   public void setQueryColumn (QueryColumn queryColumn) {
      this.queryColumn = queryColumn;
   }

   public QueryTable getQueryTable () {
      return queryTable;
   }

   public void setQueryTable (QueryTable queryTable) {
      this.queryTable = queryTable;
   }

   public AssetEntityDefinition getAssetEntityDefinition () {
      return assetEntityDefinition;
   }

   public void setAssetEntityDefinition (AssetEntityDefinition assetEntityDefinition) {
      this.assetEntityDefinition = assetEntityDefinition;
   }

   public BusinessEntityDefinition getBusinessEntityDefinition () {
      return businessEntityDefinition;
   }

   public void setBusinessEntityDefinition (BusinessEntityDefinition businessEntityDefinition) {
      this.businessEntityDefinition = businessEntityDefinition;
   }

   public Concept getConcept () {
      return concept;
   }

   public void setConcept (Concept concept) {
      this.concept = concept;
   }

   public Colum getColumn () {
      return column;
   }

   public void setColumn (Colum column) {
      this.column = column;
   }

   public Range getRangeDefinition () {
      return rangeDefinition;
   }

   public void setRangeDefinition (Range rangeDefinition) {
      this.rangeDefinition = rangeDefinition;
   }
}
