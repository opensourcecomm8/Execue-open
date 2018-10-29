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

import com.execue.das.solr.bean.SolrConditionEntity;
import com.execue.das.solr.bean.SolrDocument;
import com.execue.das.solr.dataaccess.ISolrDataAccessManager;
import com.execue.das.solr.exception.SolrException;
import com.execue.das.solr.service.ISolrManagementService;
import com.execue.das.solr.service.ISolrQueryGenerationService;

/**
 * @author Vishay
 */
public class SolrManagementServiceImpl implements ISolrManagementService {

   private ISolrDataAccessManager      solrDataAccessManager;
   private ISolrQueryGenerationService solrQueryGenerationService;

   public ISolrDataAccessManager getSolrDataAccessManager () {
      return solrDataAccessManager;
   }

   public void setSolrDataAccessManager (ISolrDataAccessManager solrDataAccessManager) {
      this.solrDataAccessManager = solrDataAccessManager;
   }

   @Override
   public Integer createSolrDocument (Long contextId, SolrDocument solrDocument) throws SolrException {
      return getSolrDataAccessManager().createSolrDocument(contextId, solrDocument);
   }

   @Override
   public Integer createSolrDocuments (Long contextId, List<SolrDocument> solrDocuments) throws SolrException {
      return getSolrDataAccessManager().createSolrDocuments(contextId, solrDocuments);
   }

   @Override
   public Integer deleteSolrDocumentById (Long contextId, String documentId) throws SolrException {
      return getSolrDataAccessManager().deleteSolrDocumentById(contextId, documentId);
   }

   @Override
   public Integer deleteSolrDocuments (Long contextId, List<SolrConditionEntity> conditions) throws SolrException {
      String solrConditionQuery = getSolrQueryGenerationService().generateSolrConditionQuery(conditions);
      return getSolrDataAccessManager().deleteSolrDocuments(contextId, solrConditionQuery);
   }

   @Override
   public Integer updateSolrDocument (Long contextId, SolrDocument solrDocument) throws SolrException {
      return getSolrDataAccessManager().updateSolrDocument(contextId, solrDocument);
   }

   public ISolrQueryGenerationService getSolrQueryGenerationService () {
      return solrQueryGenerationService;
   }

   public void setSolrQueryGenerationService (ISolrQueryGenerationService solrQueryGenerationService) {
      this.solrQueryGenerationService = solrQueryGenerationService;
   }

}
