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


package com.execue.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.QueryValidationResult;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.nlp.NLPInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.nlp.UserQuery;
import com.execue.core.common.bean.nlp.WordRecognitionState;
import com.execue.core.common.bean.qi.IQIComponent;
import com.execue.core.common.bean.qi.QIBusinessTerm;
import com.execue.core.common.bean.qi.QIPossibility;
import com.execue.core.common.bean.qi.QISelect;
import com.execue.core.common.bean.qi.QuerySuggestion;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.SearchFilterType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.engine.INLPEngine;
import com.execue.nlp.exception.NLPException;
import com.execue.nlp.generator.IReducedFormBusinessQueryGenerator;

/**
 * @author John Mallavalli
 */
public class QueryValidationHelper {

   private INLPEngine                         nlpEngine;
   private IReducedFormBusinessQueryGenerator reducedFormBusinessQueryGenerator;

   public QueryValidationResult processRequest (String request, String context) {
      QueryValidationResult queryValidationResult = null;
      try {
         queryValidationResult = new QueryValidationResult();
         QuerySuggestion querySuggestion = new QuerySuggestion();
         querySuggestion.setType(context);

         // Create the general search filter
         SearchFilter searchFilter = new SearchFilter();
         searchFilter.setSearchFilterType(SearchFilterType.GENERAL);

         // Create the user query with the search filter and user query sentence
         UserQuery userQuery = new UserQuery();
         userQuery.setSearchFilter(searchFilter);
         userQuery.setOriginalQuery(request);

         // call NLP to get NLPInformation object
         NLPInformation nlpInformation = nlpEngine.processQuery(userQuery, context);
         Map<SemanticPossibility, Integer> reducedForms = nlpInformation.getReducedForms();
         if (reducedForms.size() == 1) {
            SemanticPossibility semanticPossibility = reducedForms.keySet().iterator().next();
            // validate the possibility
            if (validatePossibility(semanticPossibility)) {
               List<IQIComponent> result = generateQIComponents(semanticPossibility, context);
               queryValidationResult.setResult(result);
               queryValidationResult.setSuccess(true);
            } else {
               // TODO: set the error message
               queryValidationResult.setSuccess(false);
            }
         } else if (reducedForms.size() > 1) {
            List<SemanticPossibility> possibilities = new ArrayList<SemanticPossibility>(reducedForms.keySet());
            List<QIPossibility> qiPossibilities = new ArrayList<QIPossibility>();
            // TODO: JVK Create an ENUM for the context values
            if ("select".equalsIgnoreCase(context)) {
               for (SemanticPossibility possibility : possibilities) {
                  // i. Validate WordReconginstionState of each possibility and filter possibilities
                  if (validatePossibility(possibility)) {
                     // ii. Show possibilities for user selection via QuerySuggestion object
                     List<IQIComponent> component = generateQIComponents(possibility, context);
                     // generate the QIPossibility object for each SemanticPossibility
                     QIPossibility qiPossibility = new QIPossibility();
                     qiPossibility.setComponent(component);
                     qiPossibility.setValue(request); // TODO: JVK verify if this correct?
                     qiPossibilities.add(qiPossibility);
                  }
               }
               querySuggestion.setType(context);
               querySuggestion.setPossibilites(qiPossibilities);
               queryValidationResult.setSuggestion(querySuggestion);
               queryValidationResult.setSuccess(false);
            } else if ("where".equalsIgnoreCase(context)) {
               // TODO: where condition implementation comes here
            } else if ("population".equalsIgnoreCase(context)) {
               // TODO: population implementation comes here
            }
         } else {
            // There are no reduced forms
            // set message as "no data found"
            querySuggestion.setErrMsg("qi.validation.noMatch");
            queryValidationResult.setSuggestion(querySuggestion);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return queryValidationResult;
   }

   private List<IQIComponent> generateQIComponents (SemanticPossibility semanticPossibility, String context) {
      List<IQIComponent> result = new ArrayList<IQIComponent>();
      try {
         BusinessQuery businessQuery = getReducedFormBusinessQueryGenerator().generateMetrics(semanticPossibility);
         if ("select".equalsIgnoreCase(context)) {
            List<BusinessTerm> metrics = businessQuery.getMetrics();
            for (BusinessTerm metric : metrics) {
               result.add(generateQISelect(metric));
            }
         } else if ("where".equalsIgnoreCase(context)) {
            // TODO: where condition implementation comes here
         } else if ("population".equalsIgnoreCase(context)) {
            // TODO: population implementation comes here
         }
      } catch (NLPException kdxe) {
         kdxe.printStackTrace();
      }
      return result;
   }

   private QISelect generateQISelect (BusinessTerm metric) {
      QISelect qiSelect = new QISelect();
      QIBusinessTerm qiBusinessTerm = new QIBusinessTerm();
      String name = "";
      String dispName = "";
      BusinessEntityTerm businessEntityTerm = metric.getBusinessEntityTerm();
      if (BusinessEntityType.CONCEPT.equals(businessEntityTerm.getBusinessEntityType())) {
         Concept concept = (Concept) businessEntityTerm.getBusinessEntity();
         name = concept.getName();
         dispName = concept.getDisplayName();
      } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(businessEntityTerm.getBusinessEntityType())) {
         Instance instance = (Instance) businessEntityTerm.getBusinessEntity();
         name = instance.getName();
         dispName = instance.getDisplayName();
      }
      qiBusinessTerm.setName(name);
      qiBusinessTerm.setDisplayName(dispName);
      qiBusinessTerm.setType(businessEntityTerm.getBusinessEntityType());
      qiSelect.setTerm(qiBusinessTerm);
      BusinessStat bStat = metric.getBusinessStat();
      if (bStat != null) {
         qiSelect.setStat(bStat.getStat().getStatType().getValue());
         qiSelect.setStatDisplayName(bStat.getStat().getDisplayName());
      }
      return qiSelect;
   }

   private boolean validatePossibility (SemanticPossibility semanticPossibility) {
      // check the validity of the WRS of each of the NLP tokens in the possibility using validateWordRecognitionState
      validateWordRecognitionState(null);
      return true;
   }

   private boolean validateWordRecognitionState (WordRecognitionState wordRecognitionState) {
      // validate the WRS
      // a. All tokens should be recognized
      // b. If statistics are found, is it associated with at least BT.
      boolean isValid = false;
      return isValid;
   }

   public INLPEngine getNlpEngine () {
      return nlpEngine;
   }

   public void setNlpEngine (INLPEngine nlpEngine) {
      this.nlpEngine = nlpEngine;
   }

   /**
    * This method processes the query form to check for the free form text for various sections of the query and updates
    * the corresponding sections of the query form with the results or if multiple possibilities result, then the
    * QuerySuggestion object gets populated
    */
   public List<QuerySuggestion> processQueryForm (QueryForm queryForm) {
      List<QuerySuggestion> querySuggestions = null;
      // Check for the select section
      String context = "Select";
      String selectText = queryForm.getSelectText();
      if (ExecueCoreUtil.isNotEmpty(selectText)) {
         QueryValidationResult queryValidationResult = processRequest(queryForm.getSelectText(), context);
         if (queryValidationResult.getSuggestion() != null) {
            // multiple possibilities resulted - set them into the QuerySuggestion object
            querySuggestions = new ArrayList<QuerySuggestion>();
            querySuggestions.add(queryValidationResult.getSuggestion());
         } else {
            // get the components and set it into the query form
            List<IQIComponent> qiComponents = queryValidationResult.getResult();
            List<QISelect> qiSelectList = new ArrayList<QISelect>();
            for (IQIComponent qiComponent : qiComponents) {
               qiSelectList.add((QISelect) qiComponent);
            }
            queryForm.setSelects(qiSelectList);
            queryForm.setSelectTextProcessed(true);
         }
      }
      return querySuggestions;
   }

   public IReducedFormBusinessQueryGenerator getReducedFormBusinessQueryGenerator () {
      return reducedFormBusinessQueryGenerator;
   }

   public void setReducedFormBusinessQueryGenerator (
            IReducedFormBusinessQueryGenerator reducedFormBusinessQueryGenerator) {
      this.reducedFormBusinessQueryGenerator = reducedFormBusinessQueryGenerator;
   }
}
