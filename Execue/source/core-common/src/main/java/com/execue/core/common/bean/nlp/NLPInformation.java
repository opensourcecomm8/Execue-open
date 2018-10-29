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


package com.execue.core.common.bean.nlp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Abhijit
 * @since Sep 21, 2008 - 5:27:06 PM
 */
public class NLPInformation {

   protected String                            userQuery;
   protected Map<SemanticPossibility, Integer> reducedForms;
   protected List<TokenCandidate>              tokenCandidates;

   public Map<SemanticPossibility, Integer> getReducedForms () {
      if (reducedForms == null) {
         reducedForms = new LinkedHashMap<SemanticPossibility, Integer>();
      }
      return reducedForms;
   }

   public void setReducedForms (Map<SemanticPossibility, Integer> reducedForms) {
      this.reducedForms = reducedForms;
   }

   public String getUserQuery () {
      return userQuery;
   }

   public void setUserQuery (String userQuery) {
      this.userQuery = userQuery;
   }

   /**
    * @return the tokenCandidates
    */
   public List<TokenCandidate> getTokenCandidates () {
      if (tokenCandidates == null) {
         tokenCandidates = new ArrayList<TokenCandidate>();
      }
      return tokenCandidates;
   }

   /**
    * @return the tokenCandidates
    */
   public void addTokenCandidate (TokenCandidate tokenCandidate) {
      if (tokenCandidates == null) {
         tokenCandidates = new ArrayList<TokenCandidate>();
      }
      tokenCandidates.add(tokenCandidate);
   }

   /**
    * @param tokenCandidates
    *           the tokenCandidates to set
    */
   public void setTokenCandidates (List<TokenCandidate> tokenCandidates) {
      this.tokenCandidates = tokenCandidates;
   }
}
