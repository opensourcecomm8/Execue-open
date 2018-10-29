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


package com.execue.das.solr.bean;

import com.execue.das.solr.type.SolrFieldType;

/**
 * @author Vishay
 */
public class SolrFacetFieldEntity {

   private String        facetField;
   private SolrFieldType fieldType;

   public SolrFacetFieldEntity () {
      super();
      // TODO Auto-generated constructor stub
   }

   public SolrFacetFieldEntity (String facetField, SolrFieldType fieldType) {
      super();
      this.facetField = facetField;
      this.fieldType = fieldType;
   }

   public SolrFacetFieldEntity (String facetField) {
      super();
      this.facetField = facetField;
   }

   public String getFacetField () {
      return facetField;
   }

   public void setFacetField (String facetField) {
      this.facetField = facetField;
   }

   public SolrFieldType getFieldType () {
      return fieldType;
   }

   public void setFieldType (SolrFieldType fieldType) {
      this.fieldType = fieldType;
   }

}
