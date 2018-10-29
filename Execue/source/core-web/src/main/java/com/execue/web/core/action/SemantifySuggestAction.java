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


package com.execue.web.core.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.SemanticSuggestTerm;
import com.execue.core.common.type.SearchFilterType;
import com.execue.handler.qi.ISemanticSuggestHandler;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author John Mallavalli
 */
public class SemantifySuggestAction extends ActionSupport {

   private ISemanticSuggestHandler   semanticSuggestHandler;
   private String                    search;
   private List<SemanticSuggestTerm> terms;
   private Long                      verticalId;
   private Long                      applicationId;
   private static final Logger       log = Logger.getLogger(SemantifySuggestAction.class);

   public Long getVerticalId () {
      return verticalId;
   }

   public void setVerticalId (Long verticalId) {
      this.verticalId = verticalId;
   }

   public String suggestSelect () {
      try {
         SearchFilter searchFilter = new SearchFilter();
         searchFilter.setSearchFilterType(SearchFilterType.GENERAL);
         if (verticalId != null && verticalId != -1) {
            searchFilter.setId(verticalId);
            searchFilter.setSearchFilterType(SearchFilterType.VERTICAL);
         } else if (applicationId != null && applicationId != -1) {
            searchFilter.setId(applicationId);
            searchFilter.setSearchFilterType(SearchFilterType.APP);
         }
         terms = semanticSuggestHandler.getSuggestTerms(search, searchFilter);

      } catch (Exception exception) {
         log.error(exception);
         terms = new ArrayList<SemanticSuggestTerm>();
      }
      return SUCCESS;
   }

   public List<SemanticSuggestTerm> getTerms () {
      return terms;
   }

   public void setTerms (List<SemanticSuggestTerm> terms) {
      this.terms = terms;
   }

   public String getSearch () {
      return search;
   }

   public void setSearch (String search) {
      this.search = search;
   }

   public ISemanticSuggestHandler getSemanticSuggestHandler () {
      return semanticSuggestHandler;
   }

   public void setSemanticSuggestHandler (ISemanticSuggestHandler semanticSuggestHandler) {
      this.semanticSuggestHandler = semanticSuggestHandler;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

}
