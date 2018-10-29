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


package com.execue.uss.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.bean.qdata.UniversalSearchResult;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.uss.exception.USSException;

/**
 * @author Abhijit
 * @since Jul 22, 2009 : 2:39:23 AM
 */
public interface IEntitySearchEngine {

   public Set<UnStructuredIndex> search (Set<Long> dedIDs) throws USSException;

   public UniversalUnstructuredSearchResult getUniversalSearchResultsByUserQueryId (Long userQueryId, int pageNumber)
            throws USSException;

   public UniversalUnstructuredSearchResult universalSearch (SemanticPossibility possibility,
            Map<Long, Set<Long>> bedsFromKnowledgeSearchByAppId, int position) throws USSException;

   public UniversalUnstructuredSearchResult universalSearch (Map<Long, Set<Long>> knowledgeSearchBeds,
            Long userQueryId, int pageNumber) throws USSException;

   public List<UniversalSearchResult> filterUniversalSearchResultByRFXValueMatch (
            Set<SemanticPossibility> possibilities, List<UniversalSearchResult> results, Long queryId)
            throws USSException;
}
