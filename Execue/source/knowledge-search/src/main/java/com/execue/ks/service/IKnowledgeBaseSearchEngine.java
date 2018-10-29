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


package com.execue.ks.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.entity.InstancePathDefinition;
import com.execue.core.common.bean.entity.KnowledgeSearchResultItem;
import com.execue.core.common.bean.qdata.RIInstanceTripleDefinition;
import com.execue.core.common.type.SearchType;
import com.execue.ks.bean.KBSearchResult;
import com.execue.ks.exception.KnowledgeSearchException;

/**
 * @author Abhijit
 * @since Jul 7, 2009 : 8:48:45 AM
 */
public interface IKnowledgeBaseSearchEngine {

   public KBSearchResult searchAndOrganize (SemanticPossibility possibility) throws KnowledgeSearchException;

   public List<InstancePathDefinition> search (SemanticPossibility possibility) throws KnowledgeSearchException;

   public Map<SemanticPossibility, List<RIInstanceTripleDefinition>> knowledgeSearch (
            List<SemanticPossibility> reducedFormPossibilities, SearchType searchType) throws KnowledgeSearchException;

   public Map<Long, KnowledgeSearchResultItem> getKnowledgeSearchResultItemMap (
            SemanticPossibility reducedFormPossibility, List<RIInstanceTripleDefinition> riInstanceTriples);

   public KBSearchResult getKnowledgeSearchResult (Map<Long, KnowledgeSearchResultItem> instanceTriples)
            throws KnowledgeSearchException;

   public Map<Long, Set<Long>> searchRelatedInstances (SemanticPossibility semanticPossibility)
            throws KnowledgeSearchException;

   public Map<Long, Set<Long>> searchRelatedInstances (Collection<SemanticPossibility> semanticPossibilities)
            throws KnowledgeSearchException;

}