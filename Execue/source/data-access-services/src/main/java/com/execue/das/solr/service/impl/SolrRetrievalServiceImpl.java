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


package com.execue.das.solr.service.impl;

import java.util.List;

import com.execue.das.solr.bean.SolrFacetQuery;
import com.execue.das.solr.bean.SolrFacetQueryInput;
import com.execue.das.solr.bean.SolrFacetResponse;
import com.execue.das.solr.dataaccess.ISolrDataAccessManager;
import com.execue.das.solr.exception.SolrException;
import com.execue.das.solr.service.ISolrQueryGenerationService;
import com.execue.das.solr.service.ISolrRetrievalService;

/**
 * @author Vishay
 */
public class SolrRetrievalServiceImpl implements ISolrRetrievalService {

   private ISolrDataAccessManager      solrDataAccessManager;
   private ISolrQueryGenerationService solrQueryGenerationService;

   @Override
   public List<SolrFacetResponse> retrieveFacets (Long contextId, SolrFacetQueryInput queryInput) throws SolrException {
      SolrFacetQuery solrFacetQuery = getSolrQueryGenerationService().generateSolrFacetQuery(queryInput);
      List<SolrFacetResponse> solrFacetResponseList = getSolrDataAccessManager().executeSolrFacetQuery(contextId,
               solrFacetQuery);
      return solrFacetResponseList;
   }

   public ISolrDataAccessManager getSolrDataAccessManager () {
      return solrDataAccessManager;
   }

   public void setSolrDataAccessManager (ISolrDataAccessManager solrDataAccessManager) {
      this.solrDataAccessManager = solrDataAccessManager;
   }

   public ISolrQueryGenerationService getSolrQueryGenerationService () {
      return solrQueryGenerationService;
   }

   public void setSolrQueryGenerationService (ISolrQueryGenerationService solrQueryGenerationService) {
      this.solrQueryGenerationService = solrQueryGenerationService;
   }

}
