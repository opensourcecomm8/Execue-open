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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.execue.core.util.ExecueCoreUtil;

/**
 * @author Vishay
 */
public class SolrFacetQuery {

   private String                                query;
   private List<String>                          filterQueries;
   private List<String>                          facetFields;
   private Map<String, List<SolrFacetQueryInfo>> facetQueries;
   private SolrFacetQueryConstantParameters      solrFacetQueryConstantParameters;

   public String getQuery () {
      return query;
   }

   public void addFilterQuery (String filterQuery) {
      if (ExecueCoreUtil.isCollectionEmpty(filterQueries)) {
         filterQueries = new ArrayList<String>();
      }
      filterQueries.add(filterQuery);
   }

   public void setQuery (String query) {
      this.query = query;
   }

   public List<String> getFilterQueries () {
      return filterQueries;
   }

   public void setFilterQueries (List<String> filterQueries) {
      this.filterQueries = filterQueries;
   }

   public List<String> getFacetFields () {
      return facetFields;
   }

   public void setFacetFields (List<String> facetFields) {
      this.facetFields = facetFields;
   }

   public SolrFacetQueryConstantParameters getSolrFacetQueryConstantParameters () {
      return solrFacetQueryConstantParameters;
   }

   public void setSolrFacetQueryConstantParameters (SolrFacetQueryConstantParameters solrFacetQueryConstantParameters) {
      this.solrFacetQueryConstantParameters = solrFacetQueryConstantParameters;
   }

   public void setFacetQueries (Map<String, List<SolrFacetQueryInfo>> facetQueries) {
      this.facetQueries = facetQueries;
   }

   public Map<String, List<SolrFacetQueryInfo>> getFacetQueries () {
      return facetQueries;
   }

}
