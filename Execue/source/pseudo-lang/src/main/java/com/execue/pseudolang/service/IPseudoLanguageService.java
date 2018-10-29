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


package com.execue.pseudolang.service;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.nlp.WordRecognitionState;
import com.execue.core.common.bean.pseudolang.NormalizedPseudoQuery;

/**
 * Service to get the Language Statement out of normalized pseudo query, and getting normalized pseudo query from
 * business query, structured query and query form
 * 
 * @author execue
 * @since 4.0
 * @version 1.0
 */
public interface IPseudoLanguageService {

   /**
    * Normalizes the Business Query into pseudo normalized query
    * 
    * @param businessQuery
    * @return
    */
   public NormalizedPseudoQuery getPseudoQuery (BusinessQuery businessQuery);

   /**
    * Normalizes the Query Form into pseudo normalized query
    * 
    * @param queryForm
    * @return
    */
   public NormalizedPseudoQuery getPseudoQuery (QueryForm queryForm);

   /**
    * Normalizes the Structured Query into pseudo normalized query
    * 
    * @param structuredQuery
    * @return
    */
   public NormalizedPseudoQuery getPseudoQuery (StructuredQuery structuredQuery);

   /**
    * Generate pseudo query into language form
    * 
    * @param pseudoQuery
    * @return
    */
   public String getPseudoLanguageStatement (NormalizedPseudoQuery pseudoQuery);

   /**
    * Generate pseudo query into language form with color codings for the missing terms at structured pseudo query from
    * business pseudo query
    * 
    * @param businessPseudoQuery
    * @param structuredPseudoQuery
    * @return
    */
   public String getColorCodedPseudoLanguageStatement (List<String> queryWords,
            Map<Integer, WordRecognitionState> wordRecognitionStates);

   /**
    * Generate the title from the NormalizedPseudoQuery, to be displayed in the Header of the Results page
    * 
    * @param pseudoQuery
    * @return
    */
   public String generateTitle (NormalizedPseudoQuery pseudoQuery);
}