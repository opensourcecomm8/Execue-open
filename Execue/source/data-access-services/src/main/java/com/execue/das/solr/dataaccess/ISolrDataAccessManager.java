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


package com.execue.das.solr.dataaccess;

import java.util.List;

import com.execue.das.solr.bean.SolrDocument;
import com.execue.das.solr.bean.SolrFacetQuery;
import com.execue.das.solr.bean.SolrFacetResponse;
import com.execue.das.solr.exception.SolrException;

/**
 * @author Vishay
 */
public interface ISolrDataAccessManager {

   public Integer createSolrDocument (Long contextId, SolrDocument solrDocument) throws SolrException;

   public Integer createSolrDocuments (Long contextId, List<SolrDocument> solrDocuments) throws SolrException;

   public Integer updateSolrDocument (Long contextId, SolrDocument solrDocument) throws SolrException;

   public Integer deleteSolrDocumentById (Long contextId, String documentId) throws SolrException;

   public Integer deleteSolrDocuments (Long contextId, String deleteQuery) throws SolrException;

   public List<SolrFacetResponse> executeSolrFacetQuery (Long contextId, SolrFacetQuery solrFacetQuery)
            throws SolrException;

}
