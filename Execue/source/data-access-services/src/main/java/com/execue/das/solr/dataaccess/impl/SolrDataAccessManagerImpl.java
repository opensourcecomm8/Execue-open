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


package com.execue.das.solr.dataaccess.impl;

import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;

import com.execue.das.solr.bean.SolrDocument;
import com.execue.das.solr.bean.SolrFacetQuery;
import com.execue.das.solr.bean.SolrFacetResponse;
import com.execue.das.solr.dataaccess.ISolrDataAccessManager;
import com.execue.das.solr.exception.SolrException;
import com.execue.das.solr.service.SolrUtility;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.http.solr.ISolrDAO;

/**
 * @author Vishay
 */
public class SolrDataAccessManagerImpl implements ISolrDataAccessManager {

   private ISolrDAO solrDAO;

   public ISolrDAO getSolrDAO () {
      return solrDAO;
   }

   public void setSolrDAO (ISolrDAO solrDAO) {
      this.solrDAO = solrDAO;
   }

   @Override
   public Integer createSolrDocument (Long contextId, SolrDocument solrDocument) throws SolrException {
      try {
         return getSolrDAO().createSolrDocument(contextId, SolrUtility.buildSolrInputDocument(solrDocument));
      } catch (DataAccessException e) {
         throw new SolrException(e.getCode(), e);
      }
   }

   @Override
   public Integer createSolrDocuments (Long contextId, List<SolrDocument> solrDocuments) throws SolrException {
      try {
         return getSolrDAO().createSolrDocuments(contextId, SolrUtility.buildSolrInputDocuments(solrDocuments));
      } catch (DataAccessException e) {
         throw new SolrException(e.getCode(), e);
      }
   }

   @Override
   public Integer deleteSolrDocumentById (Long contextId, String documentId) throws SolrException {
      try {
         return getSolrDAO().deleteSolrDocumentById(contextId, documentId);
      } catch (DataAccessException e) {
         throw new SolrException(e.getCode(), e);
      }
   }

   @Override
   public Integer deleteSolrDocuments (Long contextId, String deleteQuery) throws SolrException {
      try {
         return getSolrDAO().deleteSolrDocuments(contextId, deleteQuery);
      } catch (DataAccessException e) {
         throw new SolrException(e.getCode(), e);
      }
   }

   @Override
   public Integer updateSolrDocument (Long contextId, SolrDocument solrDocument) throws SolrException {
      try {
         return getSolrDAO().updateSolrDocument(contextId, solrDocument.getDocumentId(),
                  SolrUtility.buildSolrInputDocument(solrDocument));
      } catch (DataAccessException e) {
         throw new SolrException(e.getCode(), e);
      }
   }

   @Override
   public List<SolrFacetResponse> executeSolrFacetQuery (Long contextId, SolrFacetQuery solrFacetQuery)
            throws SolrException {
      try {
         QueryResponse queryResponse = getSolrDAO().executeSolrQuery(contextId,
                  SolrUtility.buildSolrQuery(solrFacetQuery));
         return SolrUtility.parserQueryResponse(queryResponse, solrFacetQuery);
      } catch (DataAccessException e) {
         throw new SolrException(e.getCode(), e);
      }
   }

}
