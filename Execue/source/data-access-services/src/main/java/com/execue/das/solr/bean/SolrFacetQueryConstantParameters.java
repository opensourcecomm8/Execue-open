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

/**
 * @author Vishay
 */
public class SolrFacetQueryConstantParameters {

   private Integer totalRows;
   private boolean facet;
   private String  facetMethodParamValue;
   private String  facetMethodParamName;
   private boolean facetSortByCount;
   private Integer fetchFacetsWithMinCount;
   private Integer facetLimit;

   public boolean isFacetSortByCount () {
      return facetSortByCount;
   }

   public void setFacetSortByCount (boolean facetSortByCount) {
      this.facetSortByCount = facetSortByCount;
   }

   public Integer getTotalRows () {
      return totalRows;
   }

   public void setTotalRows (Integer totalRows) {
      this.totalRows = totalRows;
   }

   public boolean isFacet () {
      return facet;
   }

   public void setFacet (boolean facet) {
      this.facet = facet;
   }

   public Integer getFetchFacetsWithMinCount () {
      return fetchFacetsWithMinCount;
   }

   public void setFetchFacetsWithMinCount (Integer fetchFacetsWithMinCount) {
      this.fetchFacetsWithMinCount = fetchFacetsWithMinCount;
   }

   public Integer getFacetLimit () {
      return facetLimit;
   }

   public void setFacetLimit (Integer facetLimit) {
      this.facetLimit = facetLimit;
   }

   public String getFacetMethodParamValue () {
      return facetMethodParamValue;
   }

   public void setFacetMethodParamValue (String facetMethodParamValue) {
      this.facetMethodParamValue = facetMethodParamValue;
   }

   public String getFacetMethodParamName () {
      return facetMethodParamName;
   }

   public void setFacetMethodParamName (String facetMethodParamName) {
      this.facetMethodParamName = facetMethodParamName;
   }

}
