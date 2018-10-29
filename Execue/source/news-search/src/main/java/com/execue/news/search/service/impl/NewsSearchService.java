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


/**
 * 
 */
package com.execue.news.search.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.ks.exception.KnowledgeSearchException;
import com.execue.ks.service.IKnowledgeBaseSearchEngine;
import com.execue.news.search.service.INewsSearchService;
import com.execue.uss.exception.USSException;
import com.execue.uss.service.IEntitySearchEngine;

/**
 * @author Nihar
 */
public class NewsSearchService implements INewsSearchService {

   private IKnowledgeBaseSearchEngine knowledgeSearchEngine;
   private IEntitySearchEngine        entitySearchEngine;
   private ICoreConfigurationService  coreConfigurationService;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.news.search.service.INewsSearchService#performNewsSearch(java.util.Set, java.lang.Long)
    */
   public List<UniversalUnstructuredSearchResult> performNewsSearch (Set<SemanticPossibility> possibilities)
            throws KnowledgeSearchException, USSException {
      // Sort the reduced forms by descending weight

      List<UniversalUnstructuredSearchResult> results = new ArrayList<UniversalUnstructuredSearchResult>(1);
      if (CollectionUtils.isEmpty(possibilities)) {
         return results;
      }
      for (SemanticPossibility possibility : possibilities) {
         Map<Long, Set<Long>> bedsFromKnowledgeSearchByAppId = getKnowledgeSearchEngine().searchRelatedInstances(
                  possibility);
         UniversalUnstructuredSearchResult universalSearchResult = getEntitySearchEngine().universalSearch(possibility,
                  bedsFromKnowledgeSearchByAppId, 1);

         results.add(universalSearchResult);
      }
      return results;
   }

   /**
    * Method to return the boolean value match filter true/false
    * 
    * @return boolean
    */
   public boolean isValueMatchFilterEnabled () {
      return getCoreConfigurationService().isFilterUniversalSearchResultByValueMatch();
   }

   /**
    * @return the knowledgeSearchEngine
    */
   public IKnowledgeBaseSearchEngine getKnowledgeSearchEngine () {
      return knowledgeSearchEngine;
   }

   /**
    * @param knowledgeSearchEngine
    *           the knowledgeSearchEngine to set
    */
   public void setKnowledgeSearchEngine (IKnowledgeBaseSearchEngine knowledgeSearchEngine) {
      this.knowledgeSearchEngine = knowledgeSearchEngine;
   }

   /**
    * @return the entitySearchEngine
    */
   public IEntitySearchEngine getEntitySearchEngine () {
      return entitySearchEngine;
   }

   /**
    * @param entitySearchEngine
    *           the entitySearchEngine to set
    */
   public void setEntitySearchEngine (IEntitySearchEngine entitySearchEngine) {
      this.entitySearchEngine = entitySearchEngine;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}