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

/**
 * This bean contains the relationship between column of cube table and corresponding column from parent asset table.
 * 
 * @author Vishay
 * @version 1.0
 * @since 10/03/2011
 */
public class CubeSourceColumnMapping {

   private QueryColumn          queryColumn;
   private ConceptColumnMapping sourceConceptColumnMapping;

   public QueryColumn getQueryColumn () {
      return queryColumn;
   }

   public void setQueryColumn (QueryColumn queryColumn) {
      this.queryColumn = queryColumn;
   }

   public ConceptColumnMapping getSourceConceptColumnMapping () {
      return sourceConceptColumnMapping;
   }

   public void setSourceConceptColumnMapping (ConceptColumnMapping sourceConceptColumnMapping) {
      this.sourceConceptColumnMapping = sourceConceptColumnMapping;
   }

}
