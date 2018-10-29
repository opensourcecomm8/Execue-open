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


package com.execue.nlp.generator.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.BusinessCondition;
import com.execue.core.common.bean.BusinessLimitClause;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.IBusinessEntity;
import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.Conversion;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.graph.Graph;
import com.execue.core.common.bean.graph.GraphComponentType;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.graph.IGraphPath;
import com.execue.core.common.bean.nlp.ComparativeInfoNormalizedData;
import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.ListNormalizedData;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.bean.nlp.RangeNormalizedData;
import com.execue.core.common.bean.nlp.RelativeTimeNormalizedData;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.bean.nlp.UnitNormalizedData;
import com.execue.core.common.bean.nlp.ValueRealizationNormalizedData;
import com.execue.core.common.bean.nlp.WeekDayNormalizedDataComponent;
import com.execue.core.common.bean.nlp.WordRecognitionState;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.bean.util.TimeInformation;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.DynamicValueQualifierType;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.OrderLimitEntityType;
import com.execue.core.common.type.ProfileType;
import com.execue.core.common.type.QueryValueType;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.nlp.exception.NLPException;
import com.execue.nlp.exception.NLPExceptionCodes;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.generator.IReducedFormBusinessQueryGenerator;
import com.execue.ontology.configuration.OntologyConstants;
import com.execue.ontology.service.IOntologyService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IConversionService;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPreferencesRetrievalService;
import com.execue.util.conversion.timeframe.util.TimeFrameUtility;

/**
 * Class with methods to generate a BusinessQuery object from a ReducedForm object
 * 
 * @author Laura Zavala <laura@execue.com>
 * @version 1.0; June 4th, 2009
 */
public class ReducedFormBusinessQueryGenerator implements IReducedFormBusinessQueryGenerator {

   public static final int              EXECUE_VERTEX      = 1;
   public static final int              EXECUE_EDGE        = 2;
   public static final String           EXECUE_LIST_FLAG   = "List";

   private static final Logger          log                = Logger.getLogger(ReducedFormBusinessQueryGenerator.class);

   private IKDXRetrievalService         kdxRetrievalService;
   private IKDXModelService             kdxModelService;
   private IBaseKDXRetrievalService     baseKDXRetrievalService;
   private IConversionService           conversionService;
   private IPreferencesRetrievalService preferencesRetrievalService;
   private IOntologyService             ontologyService;
   private boolean                      generateMutipleBqs = false;

   public IOntologyService getOntologyService () {
      return ontologyService;
   }

   public void setOntologyService (IOntologyService ontologyService) {
      this.ontologyService = ontologyService;
   }

   /**
    * Method to validate the generated ReduceForm. 1. Get all the Original Positions for each Domain Recognition and if
    * it is missing any position from the user query, then something is not recognized 2. If all tokens are recognized
    * then all need to be associated. This can be done by making sure isAssociated flag for all Recognition is TRUE.
    * 
    * @param reducedForm
    *           Semantic possibility
    * @return a boolean to indicate if the possibility is valid.
    */
   public boolean validateReducedForm (SemanticPossibility reducedForm) {
      boolean isAssociatedFully = true;
      boolean isRecognizedFully = true;
      Map<Integer, WordRecognitionState> wordRecognitionStates = reducedForm.getWordRecognitionStates();
      for (WordRecognitionState wordRec : wordRecognitionStates.values()) {
         isAssociatedFully &= wordRec.isAssociated();
         isRecognizedFully = isRecognizedFully && wordRec.getRecognitionType() != null
                  && wordRec.getRecognitionType().equals(NLPConstants.NLP_TAG_ONTO_CONCEPT)
                  || wordRec.getRecognitionType().equals(NLPConstants.NLP_TAG_ONTO_CONCEPT_PROFILE)
                  || wordRec.getRecognitionType().equals(NLPConstants.NLP_TAG_ONTO_INSTANCE_STAT);
      }
      return isAssociatedFully && isRecognizedFully;
   }

   /**
    * Method to generate the Complete Business Query from reduced Form passed Reduced Form. Will iterate over the graph
    * in depth First Manner and will apply rules on different components to add These components on diffrent parts of
    * BQ.
    * 
    * @param reducedForm
    * @return
    * @throws KDXException
    */
   public List<BusinessQuery> generateBQs (SemanticPossibility reducedForm) throws KDXException {
      List<BusinessQuery> bqList = new ArrayList<BusinessQuery>(1);
      Graph reducedFormGraph = reducedForm.getReducedForm();
      Collection<IGraphComponent> rootVertices = reducedForm.getRootVertices();
      BusinessQuery bq = new BusinessQuery();
      for (IGraphComponent rv : rootVertices) {
         bq = generateBQForRootVertex(reducedFormGraph, rv, reducedForm.getModel());
         bqList.add(bq);
      }

      if (!generateMutipleBqs) {
         BusinessQuery finalBQ = mergeBQs(bqList);
         finalBQ.setModelId(reducedForm.getModel().getId());
         adjustConditionsOperators(finalBQ);
         updateTopBottom(finalBQ, reducedForm);
         bqList.clear();
         bqList.add(finalBQ);
         return bqList;
      }
      return bqList;
   }

   /**
    * Method to update the TOP/BOTTOM in the business Query. will go through the recognitions and will get the falgs for
    * these recognitions.Bsead on the flags BusinessLimitClause will be constructed.
    * 
    * @param finalBQ
    * @param reducedForm
    */
   private void updateTopBottom (BusinessQuery finalBQ, SemanticPossibility reducedForm) {
      List<IGraphComponent> components = reducedForm.getAllGraphComponents();
      String limit = null;
      DomainRecognition termRecognition = null;
      OrderLimitEntityType entityType = OrderLimitEntityType.TOP;
      boolean queryType = false;
      for (IGraphComponent component : components) {
         DomainRecognition recognition = (DomainRecognition) component;
         if (recognition.getFlag(ExecueConstants.QUERY_TYPE)) {
            ComparativeInfoNormalizedData comparativeInfoNormalizedData = (ComparativeInfoNormalizedData) recognition
                     .getNormalizedData();
            if (comparativeInfoNormalizedData.getStatistics().getValue().equals("ComparativeStatistics2")) {
               entityType = OrderLimitEntityType.BOTTOM;
            }
            if (comparativeInfoNormalizedData.getLimit() == null) {
               limit = "1";
            } else {
               limit = comparativeInfoNormalizedData.getLimit().getValue();
            }
            queryType = true;

         }
         if (recognition.getFlag(ExecueConstants.GROUP_VARIABLE)) {
            termRecognition = recognition;
         }
      }

      BusinessLimitClause businessLimitClause = null;
      if (termRecognition != null && limit != null && queryType) {
         businessLimitClause = new BusinessLimitClause();
         BusinessTerm businessTerm = getBusinessTerm(termRecognition, reducedForm.getModel());
         if (businessTerm != null) {
            businessTerm.setRequestedByUser(true);
            businessLimitClause.setBusinessTerm(businessTerm);
            businessLimitClause.setLimitType(entityType);
            businessLimitClause.setLimitValue(limit);
         }
      }
      finalBQ.setTopBottom(businessLimitClause);

   }

   /**
    * Method to adjust Operators in the condition. If the RHS has a single Term than operator must be Equals instead of
    * IN
    * 
    * @param finalBQ
    */
   private void adjustConditionsOperators (BusinessQuery finalBQ) {
      List<BusinessCondition> conditions = new ArrayList<BusinessCondition>();
      if (!CollectionUtils.isEmpty(finalBQ.getConditions())) {
         conditions = finalBQ.getConditions();
         for (BusinessCondition condition : conditions) {
            if (!CollectionUtils.isEmpty(condition.getRhsBusinessTerms())
                     && CollectionUtils.size(condition.getRhsBusinessTerms()) == 1
                     && condition.getOperator() == OperatorType.IN) {
               condition.setOperator(OperatorType.EQUALS);
            } else if (!CollectionUtils.isEmpty(condition.getRhsValues())
                     && CollectionUtils.size(condition.getRhsValues()) == 1
                     && condition.getOperator() == OperatorType.IN) {
               condition.setOperator(OperatorType.EQUALS);
            } else if (!CollectionUtils.isEmpty(condition.getRhsValues())
                     && CollectionUtils.size(condition.getRhsValues()) > 1
                     && condition.getOperator() == OperatorType.EQUALS) {
               condition.setOperator(OperatorType.IN);
            }
         }
      }

   }

   /**
    * Method
    * 
    * @param bqList
    * @return
    * @throws KDXException
    */
   private BusinessQuery mergeBQs (List<BusinessQuery> bqList) throws KDXException {
      BusinessQuery finalBQ = new BusinessQuery();
      List<BusinessTerm> metricList = new ArrayList<BusinessTerm>();
      List<BusinessCondition> conditions = new ArrayList<BusinessCondition>();
      List<BusinessTerm> populations = new ArrayList<BusinessTerm>();
      List<BusinessTerm> summarization = new ArrayList<BusinessTerm>();
      /*
       * if (checkIfCohortQuery(bqList)) { finalBQ = null; for (BusinessQuery currentBQ : bqList) { if (finalBQ == null &&
       * !CollectionUtils.isEmpty(currentBQ.getConditions())) { finalBQ = currentBQ; } if (finalBQ != null &&
       * !currentBQ.equals(finalBQ)) { finalBQ.setCohort(currentBQ); } } return finalBQ; }
       */

      for (BusinessQuery currentBQ : bqList) {

         for (BusinessTerm bt : currentBQ.getMetrics()) {
            addBusinessTermToList(metricList, bt);
         }
         if (currentBQ.getPopulations() != null) {
            for (BusinessTerm bt : currentBQ.getPopulations()) {
               addBusinessTermToList(populations, bt);
            }
         }
         if (currentBQ.getSummarizations() != null) {
            for (BusinessTerm bt : currentBQ.getSummarizations()) {
               addBusinessTermToList(summarization, bt);
            }
         }
         if (currentBQ.getConditions() != null) {
            mergeConditions(conditions, currentBQ);
         }
         if (currentBQ.getCohort() != null) {
            finalBQ.setCohort(currentBQ.getCohort());
            if (currentBQ.getCohort().getConditions() != null) {
               List<BusinessCondition> mergedConditions = new ArrayList<BusinessCondition>();
               mergeConditions(mergedConditions, currentBQ.getCohort());
               currentBQ.getCohort().setConditions(mergedConditions);
            }
         }
      }
      finalBQ.setMetrics(metricList);
      finalBQ.setPopulations(populations);
      finalBQ.setSummarizations(summarization);
      finalBQ.setConditions(conditions);
      return finalBQ;

   }

   /*
    * private boolean checkIfCohortQuery (List<BusinessQuery> bqList) throws KDXException { int timeFrameCountQueries =
    * 0; List<BusinessTerm> businessTerm = null; for (BusinessQuery currentBQ : bqList) { if
    * (CollectionUtils.isEmpty(currentBQ.getConditions())) { continue; } for (BusinessCondition condition :
    * currentBQ.getConditions()) { Long bedId =
    * condition.getLhsBusinessTerm().getBusinessEntityTerm().getBusinessEntityDefinitionId(); BusinessEntityDefinition
    * businessEntityDefinition = getKdxRetrievalService() .getBusinessEntityDefinitionById(bedId); if
    * (businessEntityDefinition.getType().getName().equalsIgnoreCase("TimeFrame")) { if (businessTerm == null) {
    * timeFrameCountQueries++; businessTerm = condition.getRhsBusinessTerms(); } else if
    * (!businessTerm.containsAll(condition.getRhsBusinessTerms())) { timeFrameCountQueries++; } } } } return
    * timeFrameCountQueries > 1; }
    */
   private void mergeConditions (List<BusinessCondition> conditions, BusinessQuery currentBQ) {
      for (BusinessCondition condition : currentBQ.getConditions()) {
         boolean conditionRefered = false;
         for (BusinessCondition addedCondition : conditions) {
            if (addedCondition == null || condition == null) {
               continue;
            }
            if (addedCondition.getLhsBusinessTerm().getBusinessEntityTerm().equals(
                     condition.getLhsBusinessTerm().getBusinessEntityTerm())) {
               if (!addedCondition.getOperandType().equals(condition.getOperandType())) {
                  continue;
               }
               // TODO -NA- need to handle this properly based on proximity of the condition to the measure.
               if (condition.getNormalizedDataType() != addedCondition.getNormalizedDataType()) {
                  conditionRefered = true;
                  break;
               }
               if (condition.getOperandType().equals(OperandType.BUSINESS_TERM)) {
                  for (BusinessTerm rhsTerm : condition.getRhsBusinessTerms()) {
                     addBusinessTermToList(addedCondition.getRhsBusinessTerms(), rhsTerm);
                  }
                  // addedCondition.getRhsBusinessTerms().addAll(condition.getRhsBusinessTerms());
               } else if (condition.getOperandType().equals(OperandType.VALUE)) {
                  List<QueryValue> valuesToAdd = new ArrayList<QueryValue>(1);
                  for (QueryValue queryValue : condition.getRhsValues()) {
                     boolean addValue = true;
                     for (QueryValue addedQueryValue : addedCondition.getRhsValues()) {
                        if (addedQueryValue.getValueType() == queryValue.getValueType()) {
                           if (addedQueryValue.getNormalizedData() != null && queryValue.getNormalizedData() != null
                                    && addedQueryValue.getNormalizedData().equals(queryValue.getNormalizedData())) {
                              addValue = false;
                           } else if (addedQueryValue.getDataType() == queryValue.getDataType()
                                    && addedQueryValue.getValue() != null
                                    && addedQueryValue.getValue().equalsIgnoreCase(queryValue.getValue())) {
                              // If the values are same, then also we have to skip the merging of values
                              addValue = false;
                           }
                        }
                     }
                     if (addValue) {
                        valuesToAdd.add(queryValue);
                     }
                  }
                  addedCondition.getRhsValues().addAll(valuesToAdd);
               }
               conditionRefered = true;
               break;
            }
         }
         if (!conditionRefered) {
            conditions.add(condition);
         }
      }

   }

   /**
    * @param metricList
    * @param bt
    */
   private void addBusinessTermToList (List<BusinessTerm> metricList, BusinessTerm bt) {
      boolean alreadyAdded = false;
      for (BusinessTerm addedBT : metricList) {
         if (addedBT.getBusinessEntityTerm().equals(bt.getBusinessEntityTerm())) {
            alreadyAdded = true;
            break;
         }
      }
      if (!alreadyAdded) {
         metricList.add(bt);
      }
   }

   /**
    * Method to generate BQ for a rootVertex.
    * 
    * @param reducedFormGraph
    * @param rv
    * @param modelId
    * @return
    * @throws KDXException
    */
   private BusinessQuery generateBQForRootVertex (Graph reducedFormGraph, IGraphComponent rv, Model model)
            throws KDXException {
      List<IGraphComponent> components = (List<IGraphComponent>) reducedFormGraph.getDepthFirstRoute(rv);
      Map<DomainRecognition, BusinessTerm> componentTermMap = new HashMap<DomainRecognition, BusinessTerm>();
      Map<IGraphComponent, List<BusinessTerm>> businessTermMap = new HashMap<IGraphComponent, List<BusinessTerm>>();
      Map<IGraphComponent, List<BusinessCondition>> timeRecognitionConditions = new HashMap<IGraphComponent, List<BusinessCondition>>();
      Map<IGraphComponent, BusinessTerm> cohortBusinessTremMap = new HashMap<IGraphComponent, BusinessTerm>();
      Map<IGraphComponent, BusinessTerm> businessTermPopulationsMap = new HashMap<IGraphComponent, BusinessTerm>();
      Map<IGraphComponent, List<BusinessCondition>> recognitionConditions = new HashMap<IGraphComponent, List<BusinessCondition>>();
      List<BusinessTerm> profileConceptsList = new ArrayList<BusinessTerm>();
      int index = 0;
      List<DomainRecognition> addedTimeFrame = new ArrayList<DomainRecognition>();

      // Prepare the business query
      BusinessQuery bq = new BusinessQuery();
      bq.setModelId(model.getId());

      for (IGraphComponent component : components) {

         if (component.getType() == GraphComponentType.Vertex) {

            DomainRecognition recognition = (DomainRecognition) component;
            if (recognition.getNlpTag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_CONCEPT)) {

               BusinessTerm bTerm = getBusinessTerm(recognition, model);
               if (!componentTermMap.containsKey(recognition)) {

                  Long dedId = bTerm.getBusinessEntityTerm().getBusinessEntityDefinitionId();
                  componentTermMap.put(recognition, bTerm);
                  if (recognition.getFlag(ExecueConstants.BY_VARIABLE)
                           || recognition.getFlag(ExecueConstants.GROUP_VARIABLE)) {

                     if (bTerm.getBusinessEntityTerm().isMeasurableEntity()) {
                        bTerm.getBusinessEntityTerm().setMeasureGroupBy(CheckType.YES);
                     }

                     List<BusinessTerm> summarizations = bq.getSummarizations();
                     if (summarizations == null) {

                        summarizations = new ArrayList<BusinessTerm>(1);
                        bq.setSummarizations(summarizations);
                     }

                     summarizations.add(bTerm);
                     //} else if (kdxModelService.checkEntityHasBehavior(bedId, BehaviorType.POPULATION)) {
                  } else if (recognition.getBehaviors().contains(BehaviorType.POPULATION)) {
                     businessTermPopulationsMap.put(recognition, bTerm);

                  } else { // Removed isRequestedByUser check so that Default path concepts can be added to Metrics

                     List<BusinessTerm> bTermList = new ArrayList<BusinessTerm>(1);
                     bTermList.add(bTerm);
                     businessTermMap.put(recognition, bTermList);
                  }
               }
            } else if (recognition.getNlpTag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_INSTANCE)
                     || recognition.getNlpTag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_INSTANCE_PROFILE)
                     || recognition.getNlpTag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_TYPE_INSTANCE)
                     || recognition.getNlpTag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_REALIZED_TYPE_INSTANCE)) {
               /*
                * if (index == 0) { return bq; }
                */
               if (isInstanceToBeProcessed(recognition)) {
                  processInstanceRecognition(components, businessTermMap, timeRecognitionConditions, index, bq,
                           recognition, profileConceptsList, addedTimeFrame, cohortBusinessTremMap,
                           recognitionConditions, model, componentTermMap);
               }
            } else if (recognition.getNlpTag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_CONCEPT_PROFILE)) {
               BusinessTerm bTerm = getBusinessTerm(recognition, model);
               if (!componentTermMap.containsKey(recognition)) {
                  componentTermMap.put(recognition, bTerm);
                  List<BusinessTerm> bTermList = new ArrayList<BusinessTerm>(1);
                  bTermList.add(bTerm);
                  businessTermMap.put(recognition, bTermList);
               }
            }
         }
         index = index + 1;
      }
      bq.setMetrics(profileConceptsList);
      List<BusinessTerm> metrics = bq.getMetrics();
      if (metrics == null) {
         metrics = new ArrayList<BusinessTerm>();
      }
      for (List<BusinessTerm> termsList : businessTermMap.values()) {
         metrics.addAll(termsList);
      }
      bq.setMetrics(metrics);
      List<BusinessTerm> populations = bq.getPopulations();
      if (populations == null) {
         populations = new ArrayList<BusinessTerm>();
      }
      populations.addAll(businessTermPopulationsMap.values());
      bq.setPopulations(populations);
      for (Entry<IGraphComponent, List<BusinessCondition>> entry : recognitionConditions.entrySet()) {
         DomainRecognition recognition = (DomainRecognition) entry.getKey();
         List<BusinessCondition> conditions = entry.getValue();
         if (cohortBusinessTremMap.containsKey(recognition) && bq.getCohort() != null) {
            List<BusinessCondition> cohortConditions = bq.getCohort().getConditions();
            if (cohortConditions == null) {
               cohortConditions = new ArrayList<BusinessCondition>();
            }
            CollectionUtils.addAll(cohortConditions, conditions.iterator());
            bq.getCohort().setConditions(cohortConditions);
         } else {
            List<BusinessCondition> queryConditions = bq.getConditions();
            if (queryConditions == null) {
               queryConditions = new ArrayList<BusinessCondition>();
            }
            CollectionUtils.addAll(queryConditions, conditions.iterator());
            bq.setConditions(queryConditions);
         }
      }
      return bq;
   }

   private boolean isInstanceToBeProcessed (DomainRecognition recognition) {
      return !recognition.getTypeName().equals(ExecueConstants.CONJUNCTION_TYPE)
               && !recognition.getTypeName().equals(ExecueConstants.COORDINATING_CONCJUNCTION_TYPE)
               && !recognition.getTypeName().equals(ExecueConstants.BY_CONJUNCTION_TYPE)
               && !recognition.getTypeName().equals(ExecueConstants.ADJECTIVE_TYPE)
               && !recognition.getTypeName().equals(ExecueConstants.PREPOSITION_TYPE)
               && !recognition.getTypeName().equals(ExecueConstants.COMPARATIVE_STATISTICS_TYPE)
               && !recognition.getTypeName().equals(ExecueConstants.UNIT_SYMBOL_TYPE)
               && !recognition.getTypeName().equals(ExecueConstants.UNIT_SCALE_TYPE)
               && !recognition.getTypeName().equals(ExecueConstants.OPERATOR_TYPE)
               && !recognition.getTypeName().equals(ExecueConstants.VALUE_PREPOSITION_TYPE)
               && !recognition.getTypeName().equals(ExecueConstants.TIME_QUALIFIER)
               && !recognition.getTypeName().equals(ExecueConstants.TIME_PREPOSITION)
               && !recognition.getTypeName().equals(ExecueConstants.PUNCTUATION_TYPE);
   }

   /**
    * @param components
    * @param businessTremMap
    * @param timeRecognitionConditions 
    * @param index
    * @param bq
    * @param recognition
    * @param profileConceptsList
    * @param addedTimeFrame
    * @param cohortBusinessTremMap
    * @param recognitionConditions
    * @param componentTermMap 
    * @throws KDXException
    */
   private void processInstanceRecognition (List<IGraphComponent> components,
            Map<IGraphComponent, List<BusinessTerm>> businessTremMap,
            Map<IGraphComponent, List<BusinessCondition>> timeRecognitionConditions, int index, BusinessQuery bq,
            DomainRecognition recognition, List<BusinessTerm> profileConceptsList,
            List<DomainRecognition> addedTimeFrame, Map<IGraphComponent, BusinessTerm> cohortBusinessTremMap,
            Map<IGraphComponent, List<BusinessCondition>> recognitionConditions, Model model,
            Map<DomainRecognition, BusinessTerm> componentTermMap) throws KDXException {
      String businessEntityTypeName = recognition.getTypeName();

      /*
       * BusinessEntityDefinition timeConceptDed = baseKDXRetrievalService.getBusinessEntityDefinitionByNames(
       * OntologyConstants.TIME_CONCEPT, null);
       */

      if (businessEntityTypeName.equalsIgnoreCase(OntologyConstants.STATISTICS_CONCEPT)) {
         processStatInstance(components, businessTremMap, index, recognition, profileConceptsList, model);
      } else if (businessEntityTypeName.equalsIgnoreCase(ExecueConstants.VALUE_TYPE)) {
         processValueInstance(components, businessTremMap, index, bq, recognition, model, cohortBusinessTremMap,
                  recognitionConditions);
      } else if (businessEntityTypeName.equals(ExecueConstants.TIME_FRAME_TYPE)
               || businessEntityTypeName.equals("AbstractTime")) {
         processTimeInstance(businessTremMap, timeRecognitionConditions, bq, recognition, addedTimeFrame, index,
                  components, cohortBusinessTremMap, recognitionConditions, model);
      } else {
         List<BusinessTerm> btList = new ArrayList<BusinessTerm>(1);
         if (recognition.getNlpTag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_INSTANCE_PROFILE)) {
            processInstanceProfile(bq, recognition, btList, recognitionConditions, model);
         } else {
            processInstance(bq, recognition, btList, businessTremMap, recognitionConditions, model);

         }

      }
   }

   private void processTimeInstance (Map<IGraphComponent, List<BusinessTerm>> businessTremMap,
            Map<IGraphComponent, List<BusinessCondition>> timeRecognitionConditions, BusinessQuery bq,
            DomainRecognition recognition, List<DomainRecognition> addedTimeFrame, int index,
            List<IGraphComponent> components, Map<IGraphComponent, BusinessTerm> cohortBusinessTremMap,
            Map<IGraphComponent, List<BusinessCondition>> recognitionConditions, Model model) throws KDXException {
      // TODO need to imrove this section for cohort Queries.
      // TODO for relative Time Frame check if some Type can be set on NLP side.
      if (recognition.getNormalizedData() != null
               && recognition.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RANGE_NORMALIZED_DATA) {
         processRangeTimeFrame(recognition, recognitionConditions, addedTimeFrame, index, components, businessTremMap,
                  cohortBusinessTremMap, bq, model);
         return;
      }
      if (recognition.getNormalizedData() != null
               && (recognition.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA || recognition
                        .getNormalizedData().getNormalizedDataType() == NormalizedDataType.RELATIVE_NORMALIZED_DATA)) {
         processRelativeTimeFrame(recognition, recognitionConditions, addedTimeFrame, index, components,
                  cohortBusinessTremMap, cohortBusinessTremMap, bq, model);
         return;
      }
      List<BusinessTerm> rhsterm = getBusinessTermForInstance(recognition, model, true);
      List<BusinessTerm> btList = new ArrayList<BusinessTerm>();
      for (BusinessTerm bt : rhsterm) {
         if (bt.getBusinessEntityTerm().getBusinessEntity() != null) {
            btList.add(bt);
         }
      }

      DomainRecognition conceptRecognition = null;
      boolean isCohort = false;
      if (!CollectionUtils.isEmpty(addedTimeFrame)) {
         DomainRecognition addedTFRecognition = addedTimeFrame.get(0);
         isCohort = checkForCohort(addedTFRecognition, recognition);
      }
      if (!isCohort) {
         // addedTimeFrame.add(btList.get(0));
         populateConditionForTimeRecognition(bq, recognition, timeRecognitionConditions, model, addedTimeFrame);
         // processInstance(bq, recognition, btList, businessTremMap, timeRecognitionConditions, model);

      } else {

         if (index - 2 < 0) {
            return;
         }
         conceptRecognition = (DomainRecognition) CollectionUtils.get(components, index - 2);
         if (conceptRecognition == null) {
            return;
         }
         BusinessQuery cohortQuery = bq.getCohort();
         if (cohortQuery == null) {
            cohortQuery = new BusinessQuery();
         }
         populateConditionForTimeRecognition(cohortQuery, recognition, timeRecognitionConditions, model, addedTimeFrame);
         /*
          * processInstance(cohortQuery, recognition, new ArrayList<BusinessTerm>(), new HashMap<IGraphComponent, List<BusinessTerm>>(),
          * timeRecognitionConditions, model);
          */
         List<BusinessTerm> businessTerms = businessTremMap.remove(components.get(index - 2));
         if (CollectionUtils.isEmpty(businessTerms)) {
            return;
         }
         BusinessTerm businessTerm = businessTerms.get(0);
         if (businessTerm == null) {
            businessTerm = getBusinessTerm((DomainRecognition) components.get(index - 2), model);
            if (businessTerm == null) {
               return;
            }
         }
         cohortBusinessTremMap.put(conceptRecognition, businessTerm);

         List<BusinessTerm> cohortmetrics = cohortQuery.getMetrics();
         if (cohortmetrics == null) {
            cohortmetrics = new ArrayList<BusinessTerm>();
         }
         cohortmetrics.add(businessTerm);
         cohortQuery.setMetrics(cohortmetrics);
         cohortQuery.setModelId(bq.getModelId());
         bq.setCohort(cohortQuery);

      }
      for (Entry<IGraphComponent, List<BusinessCondition>> entry : timeRecognitionConditions.entrySet()) {
         List<BusinessCondition> conditions = entry.getValue();
         if (conceptRecognition != null) {
            List<BusinessCondition> addedConditions = recognitionConditions.get(conceptRecognition);
            if (conditions == null) {
               conditions = new ArrayList<BusinessCondition>();
            }
            if (addedConditions == null) {
               addedConditions = new ArrayList<BusinessCondition>();
            }
            conditions.addAll(addedConditions);
            recognitionConditions.put(conceptRecognition, conditions);
         } else {
            recognitionConditions.put(recognition, conditions);
         }
      }
      // }
      // }
      /*
       * else { if(rhsterm.getBusinessEntityTerm().equals(addedTimeFrame.get(0).getBusinessEntityTerm())){
       * processInstance(bq, recognition, new ArrayList<BusinessTerm>()); }else{ BusinessQuery cohortQuery =
       * bq.getCohort(); if(cohortQuery == null ){ cohortQuery = new BusinessQuery(); } processInstance(cohortQuery,
       * recognition, new ArrayList<BusinessTerm>()); BusinessTerm businessTerm =
       * businessTremMap.get(components.get(index - 2)); businessTremMap.remove(components.get(index - 2)); List<
       * BusinessTerm> cohortmetrics = new ArrayList<BusinessTerm>(); cohortmetrics.add(businessTerm);
       * cohortQuery.setMetrics(cohortmetrics); bq.setCohort(cohortQuery); } }
       */
   }

   /**
    * Method to check if two TFs are eligible to make a cohort query.
    * 
    * @param addedTFRecognition
    * @param recognition
    * @return
    */
   private boolean checkForCohort (DomainRecognition addedTFRecognition, DomainRecognition recognition) {
      Long addedConceptBedId = addedTFRecognition.getConceptBEDId();
      Long currentConceptBedId = recognition.getConceptBEDId();
      List<Long> alternateBedIds = recognition.getAlternateBedIds();
      // rule1 if the conceptid are same and the time mentioned is different.
      if (addedConceptBedId.equals(currentConceptBedId)) {
         INormalizedData addedTFNormalizedData = addedTFRecognition.getNormalizedData();
         INormalizedData normalizedData = recognition.getNormalizedData();
         if (!normalizedData.equals(addedTFNormalizedData)) {
            return true;
         }
      }
      // rule 2 if the two TFs are convertible
      if (alternateBedIds.contains(addedConceptBedId)) {
         return true;
      }
      return false;
   }

   private void populateConditionForTimeRecognition (BusinessQuery bq, DomainRecognition recognition,
            Map<IGraphComponent, List<BusinessCondition>> timeRecognitionConditions, Model model,
            List<DomainRecognition> addedTimeFrame) throws KDXException {
      NormalizedDataType normalizedDataType = NormalizedDataType.TIME_FRAME_NORMALIZED_DATA;
      BusinessEntityDefinition conceptDed = kdxRetrievalService.getBusinessEntityDefinitionById(recognition
               .getConceptBEDId());
      List<BusinessTerm> alternateBTList = retriveAlternateBusinessTerms(recognition.getAlternateBedIds());
      Concept concept = conceptDed.getConcept();
      INormalizedData normalizedData = recognition.getNormalizedData();
      if (normalizedData != null
               && normalizedData.getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA) {
         INormalizedData defaultNormalizedDataIfOnlyWeekDayisPresent = populateDefaultNormalizedDataIfOnlyWeekDayisPresent(normalizedData);
         if (defaultNormalizedDataIfOnlyWeekDayisPresent != null) {
            normalizedData = defaultNormalizedDataIfOnlyWeekDayisPresent;
            normalizedDataType = NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA;
         }
      }
      BusinessCondition condition = new BusinessCondition();
      BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
      businessEntityTerm.setBusinessEntity(concept);
      businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
      businessEntityTerm.setBusinessEntityDefinitionId(conceptDed.getId());
      businessEntityTerm.setDependantMeasure(recognition.getBehaviors().contains(BehaviorType.DEPENDENT_VARIABLE));

      BusinessTerm lhsBusinessTerm = new BusinessTerm();
      lhsBusinessTerm.setRequestedByUser(true);
      lhsBusinessTerm.setBusinessEntityTerm(businessEntityTerm);
      condition.setLhsBusinessTerm(lhsBusinessTerm);
      List<QueryValue> valueList = new ArrayList<QueryValue>(1);
      condition.setNormalizedDataType(normalizedDataType);
      if (normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
         ListNormalizedData listNormalizedData = (ListNormalizedData) normalizedData;
         for (NormalizedDataEntity normalizedDataEntity : listNormalizedData.getNormalizedDataEntities()) {
            QueryValue queryValue = new QueryValue();
            queryValue.setValueType(QueryValueType.NORMALIZED_OBJECT);
            queryValue.setNormalizedData(normalizedDataEntity.getNormalizedData());
            condition.setOperator(OperatorType.IN);
            valueList.add(queryValue);
         }
      } else {
         QueryValue queryValue = new QueryValue();
         queryValue.setValueType(QueryValueType.NORMALIZED_OBJECT);
         queryValue.setNormalizedData(normalizedData);
         condition.setOperator(OperatorType.EQUALS);
         valueList.add(queryValue);
      }
      condition.setRhsValues(valueList);
      condition.setOperandType(OperandType.VALUE);
      condition.setLhsBusinessTermVariations(alternateBTList);
      if (recognition.getNormalizedData() != null
               && recognition.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RANGE_NORMALIZED_DATA) {
         condition.setOperator(OperatorType.BETWEEN);
      }
      if (normalizedData != null && normalizedData instanceof TimeFrameNormalizedData) {
         TimeFrameNormalizedData tfNormalizedData = (TimeFrameNormalizedData) normalizedData;
         NormalizedDataEntity valuePreposition = tfNormalizedData.getValuePreposition();
         WeekDayNormalizedDataComponent weekDayNormalizedDataComponent = tfNormalizedData
                  .getWeekDayNormalizedDataComponent();
         if (weekDayNormalizedDataComponent != null) {
            populateMissingComponentsInWeekDayComponent(weekDayNormalizedDataComponent);
            TimeFrameUtility.populateTimeInformationInWeekDayComponent(tfNormalizedData);
         }
         mapValuePrepositionToOperator(condition, valuePreposition);
      }
      List<BusinessCondition> conditions = timeRecognitionConditions.get(recognition);
      if (conditions == null) {
         conditions = new ArrayList<BusinessCondition>();
      }

      for (BusinessCondition alreadyAddedCond : conditions) {
         if (alreadyAddedCond.getLhsBusinessTerm().getBusinessEntityTerm().equals(
                  condition.getLhsBusinessTerm().getBusinessEntityTerm())
                  && condition.getOperandType().equals(OperandType.BUSINESS_TERM)
                  && alreadyAddedCond.getOperandType().equals(OperandType.BUSINESS_TERM)) {
            for (BusinessTerm rhsTerm : condition.getRhsBusinessTerms()) {
               addBusinessTermToList(alreadyAddedCond.getRhsBusinessTerms(), rhsTerm);
            }
            condition = null;
            break;
         }
      }

      if (condition != null) {
         conditions.add(condition);
      }
      timeRecognitionConditions.put(recognition, conditions);
      addedTimeFrame.add(recognition);
   }

   private INormalizedData populateDefaultNormalizedDataIfOnlyWeekDayisPresent (INormalizedData normalizedData)
            throws KDXException {
      TimeFrameNormalizedData tfNormalizedData = (TimeFrameNormalizedData) normalizedData;
      if (tfNormalizedData.getWeekDayNormalizedDataComponent() != null) {
         NormalizedDataEntity week = tfNormalizedData.getWeek();
         NormalizedDataEntity month = tfNormalizedData.getMonth();
         NormalizedDataEntity quarter = tfNormalizedData.getQuarter();
         NormalizedDataEntity year = tfNormalizedData.getYear();
         if (week == null && month == null && quarter == null && year == null) {
            RelativeTimeNormalizedData defaultRelativeTimeNormalizedData = TimeFrameUtility
                     .buildDefaultRelativeTimeNormalizedData();
            WeekDayNormalizedDataComponent weekDayNormalizedDataComponent = tfNormalizedData
                     .getWeekDayNormalizedDataComponent();
            if (weekDayNormalizedDataComponent != null) {
               populateMissingComponentsInWeekDayComponent(weekDayNormalizedDataComponent);
            }
            defaultRelativeTimeNormalizedData.setWeekDayNormalizedDataComponent(weekDayNormalizedDataComponent);
            defaultRelativeTimeNormalizedData.setHour(tfNormalizedData.getHour());
            defaultRelativeTimeNormalizedData.setMinute(tfNormalizedData.getMinute());
            defaultRelativeTimeNormalizedData.setSecond(tfNormalizedData.getSecond());
            defaultRelativeTimeNormalizedData.setTimeQualifier(tfNormalizedData.getTimeQualifier());
            return defaultRelativeTimeNormalizedData;
         }
      }
      return null;
   }

   /**
    * @param weekDayNormalizedDataComponent
    * @throws KDXException
    */
   private void populateMissingComponentsInWeekDayComponent (
            WeekDayNormalizedDataComponent weekDayNormalizedDataComponent) throws KDXException {
      if (weekDayNormalizedDataComponent.getNumber() == null) {
         NormalizedDataEntity normalizedDataEntity = new NormalizedDataEntity();
         normalizedDataEntity.setValue("1");
         normalizedDataEntity.setDisplayValue("1");
         weekDayNormalizedDataComponent.setNumber(normalizedDataEntity);
      }
      if (weekDayNormalizedDataComponent.getAdjective() != null) {
         weekDayNormalizedDataComponent
                  .setAdjectiveQualifierType(getDynamicQualifierType(weekDayNormalizedDataComponent.getAdjective()
                           .getValue()));

      } else {
         if (weekDayNormalizedDataComponent.isTimeProvided()) {
            weekDayNormalizedDataComponent.setAdjectiveQualifierType(DynamicValueQualifierType.NEXT);
         } else {
            weekDayNormalizedDataComponent.setAdjectiveQualifierType(DynamicValueQualifierType.ALL);
         }
      }
   }

   private List<BusinessTerm> retriveAlternateBusinessTerms (List<Long> alternateBedIds) throws KDXException {
      if (!CollectionUtils.isEmpty(alternateBedIds)) {
         List<BusinessTerm> btList = new ArrayList<BusinessTerm>(1);
         for (Long bedId : alternateBedIds) {
            BusinessEntityDefinition conceptBed = kdxRetrievalService.getBusinessEntityDefinitionById(bedId);
            Concept concept = conceptBed.getConcept();
            BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
            businessEntityTerm.setBusinessEntity(concept);
            businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
            businessEntityTerm.setBusinessEntityDefinitionId(conceptBed.getId());
            BusinessTerm businessTerm = new BusinessTerm();
            // TODO marking it as false to avoid Governor weight logic need to change it to true it again.
            businessTerm.setRequestedByUser(false);
            businessTerm.setBusinessEntityTerm(businessEntityTerm);
            btList.add(businessTerm);
         }
         return btList;
      }
      return new ArrayList<BusinessTerm>(1);
   }

   private void mapValuePrepositionToOperator (BusinessCondition condition, NormalizedDataEntity valuePreposition) {
      if (valuePreposition != null) {
         OperatorType opType = null;
         if (valuePreposition.getDisplaySymbol().equals(OperatorType.GREATER_THAN.getValue())) {
            opType = OperatorType.GREATER_THAN;
            condition.setOperator(opType);
         } else if (valuePreposition.getDisplaySymbol().equals(OperatorType.LESS_THAN.getValue())) {
            opType = OperatorType.LESS_THAN;
            condition.setOperator(opType);
         } else if (valuePreposition.getDisplaySymbol().equals(OperatorType.GREATER_THAN_EQUALS.getValue())) {
            opType = OperatorType.GREATER_THAN_EQUALS;
            condition.setOperator(opType);
         } else if (valuePreposition.getDisplaySymbol().equals(OperatorType.LESS_THAN_EQUALS.getValue())) {
            opType = OperatorType.LESS_THAN_EQUALS;
            condition.setOperator(opType);
         }
      }
   }

   private void processRangeTimeFrame (DomainRecognition recognition,
            Map<IGraphComponent, List<BusinessCondition>> recognitionConditions,
            List<DomainRecognition> addedTimeFrame, int index, List<IGraphComponent> components,
            Map<IGraphComponent, List<BusinessTerm>> businessTremMap,
            Map<IGraphComponent, BusinessTerm> cohortBusinessTremMap, BusinessQuery businessQuery, Model model)
            throws KDXException {
      if (log.isDebugEnabled()) {
         log.debug("Recognition conceptBEDID : " + recognition.getConceptBEDId() + "; concept name : "
                  + recognition.getConceptName());
      }
      DomainRecognition conceptRecognition = null;
      if (index - 2 >= 0) {
         conceptRecognition = (DomainRecognition) CollectionUtils.get(components, index - 2);
      }
      if (conceptRecognition == null) {
         conceptRecognition = recognition;
      }
      List<BusinessTerm> alternateBTList = retriveAlternateBusinessTerms(recognition.getAlternateBedIds());

      RangeNormalizedData rangeNormalizedData = (RangeNormalizedData) recognition.getNormalizedData();
      NormalizedDataEntity startEntity = rangeNormalizedData.getStart();
      NormalizedDataEntity endEntity = rangeNormalizedData.getEnd();
      boolean checkIfNormalizedDataEqalsTillWeekdayComponents = TimeFrameUtility
               .checkIfNormalizedDataEqalsTillWeekdayComponents((TimeFrameNormalizedData) startEntity
                        .getNormalizedData(), (TimeFrameNormalizedData) endEntity.getNormalizedData());

      if (checkIfNormalizedDataEqalsTillWeekdayComponents) {
         INormalizedData tfNormalizedData = mergeTFNormalizedDataForTimeInformation(
                  (TimeFrameNormalizedData) startEntity.getNormalizedData(), (TimeFrameNormalizedData) endEntity
                           .getNormalizedData());
         recognition.setNormalizedData(tfNormalizedData);
         recognition.removeFlag(ExecueConstants.RANGE);
         populateConditionForTimeRecognition(businessQuery, recognition, recognitionConditions, model, addedTimeFrame);
      } else {
         BusinessEntityDefinition conceptDed = kdxRetrievalService.getBusinessEntityDefinitionById(recognition
                  .getConceptBEDId());
         Concept concept = conceptDed.getConcept();

         BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
         businessEntityTerm.setBusinessEntity(concept);
         businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
         businessEntityTerm.setBusinessEntityDefinitionId(conceptDed.getId());

         BusinessTerm lhsBusinessTerm = new BusinessTerm();
         lhsBusinessTerm.setRequestedByUser(true);
         lhsBusinessTerm.setBusinessEntityTerm(businessEntityTerm);

         BusinessCondition condition = new BusinessCondition();
         condition.setLhsBusinessTerm(lhsBusinessTerm);
         condition.setOperator(OperatorType.BETWEEN);
         condition.setNormalizedDataType(NormalizedDataType.TIME_FRAME_NORMALIZED_DATA);

         List<QueryValue> valueList = new ArrayList<QueryValue>(1);
         QueryValue startQueryValue = new QueryValue();
         startQueryValue.setValueType(QueryValueType.NORMALIZED_OBJECT);
         INormalizedData startNormalizedData = startEntity.getNormalizedData();
         if (startNormalizedData != null
                  && startNormalizedData.getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA) {
            INormalizedData defaultNormalizedDataIfOnlyWeekDayisPresent = populateDefaultNormalizedDataIfOnlyWeekDayisPresent(startNormalizedData);
            if (defaultNormalizedDataIfOnlyWeekDayisPresent != null) {
               startNormalizedData = defaultNormalizedDataIfOnlyWeekDayisPresent;
               condition.setNormalizedDataType(NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA);
            }
         }
         startQueryValue.setNormalizedData(startNormalizedData);
         valueList.add(startQueryValue);
         QueryValue endQueryValue = new QueryValue();
         endQueryValue.setValueType(QueryValueType.NORMALIZED_OBJECT);
         INormalizedData endNormalizedData = endEntity.getNormalizedData();
         if (endNormalizedData != null
                  && endNormalizedData.getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA) {
            INormalizedData defaultNormalizedDataIfOnlyWeekDayisPresent = populateDefaultNormalizedDataIfOnlyWeekDayisPresent(endNormalizedData);
            if (defaultNormalizedDataIfOnlyWeekDayisPresent != null) {
               endNormalizedData = defaultNormalizedDataIfOnlyWeekDayisPresent;

            }
         }
         endQueryValue.setNormalizedData(endNormalizedData);
         valueList.add(endQueryValue);
         condition.setRhsValues(valueList);
         condition.setOperandType(OperandType.VALUE);
         condition.setLhsBusinessTermVariations(alternateBTList);

         List<BusinessCondition> conditionList = recognitionConditions.get(conceptRecognition);
         if (CollectionUtils.isEmpty(conditionList)) {
            conditionList = new ArrayList<BusinessCondition>();
         }
         conditionList.add(condition);
         recognitionConditions.put(conceptRecognition, conditionList);
      }

      boolean isCohort = false;
      if (!CollectionUtils.isEmpty(addedTimeFrame)) {
         DomainRecognition addedTFRecognition = addedTimeFrame.get(0);
         isCohort = checkForCohort(addedTFRecognition, recognition);
      }
      if (!isCohort) {
         addedTimeFrame.add(recognition);

      } else {
         if (index - 2 < 0) {
            return;
         }
         BusinessQuery cohortQuery = businessQuery.getCohort();
         if (cohortQuery == null) {
            cohortQuery = new BusinessQuery();
         }
         BusinessTerm businessTerm = cohortBusinessTremMap.remove(components.get(index - 2));
         if (businessTerm == null) {
            businessTerm = getBusinessTerm((DomainRecognition) components.get(index - 2), model);
         }
         cohortBusinessTremMap.put(conceptRecognition, businessTerm);
         List<BusinessTerm> cohortmetrics = new ArrayList<BusinessTerm>();
         cohortmetrics.add(businessTerm);
         cohortQuery.setMetrics(cohortmetrics);
         cohortQuery.setModelId(businessQuery.getModelId());
         businessQuery.setCohort(cohortQuery);

      }

   }

   private void processInstance (BusinessQuery businessQuery, DomainRecognition recognition, List<BusinessTerm> btList,
            Map<IGraphComponent, List<BusinessTerm>> businessTremMap,
            Map<IGraphComponent, List<BusinessCondition>> recognitionConditions, Model model) throws KDXException {

      // TODO For List of instances DomainInstance ID comes As Null and the Name are seperated by ","
      // So for Now Call a diffrent method to get BusinessTerms For Instances.

      if (CollectionUtils.isEmpty(btList)) {
         List<BusinessTerm> rhsterm = getBusinessTermForInstance(recognition, model, false);
         for (BusinessTerm bt : rhsterm) {
            if (bt.getBusinessEntityTerm().getBusinessEntity() != null) {
               btList.add(bt);
            }
         }
         // if Still btList is Empty return
         if (CollectionUtils.isEmpty(btList)) {
            return;
         }
      }
      BusinessEntityDefinition conceptBed = kdxRetrievalService.getBusinessEntityDefinitionById(recognition
               .getConceptBEDId());

      // DomainEntityDefinition conceoptDed = getKdxService().getDomainEntityDefinitionByNames(
      // getKdxService().getDefaultDomain().getName(), recognition.getBusinessEntity(), null);
      Concept cp = conceptBed.getConcept();
      if (cp.getName().equals(OntologyConstants.COORDINATING_CONCJUNCTION)) {
         return;
      }
      boolean isParentConceptFromBase = false;
      if (conceptBed.getModelGroup().getId().equals(getBaseKDXRetrievalService().getBaseGroup().getId())) {
         isParentConceptFromBase = true;
      }
      BusinessCondition condition = generateCondition(btList, cp, businessQuery, model, isParentConceptFromBase);
      if (recognition.getNormalizedData() != null
               && recognition.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RANGE_NORMALIZED_DATA) {
         condition.setOperator(OperatorType.BETWEEN);
      }
      INormalizedData normalizedData = recognition.getNormalizedData();
      if (normalizedData != null
               && normalizedData.getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA) {
         TimeFrameNormalizedData tfNormalizedData = (TimeFrameNormalizedData) normalizedData;
         NormalizedDataEntity valuePreposition = tfNormalizedData.getValuePreposition();
         mapValuePrepositionToOperator(condition, valuePreposition);
      }

      List<BusinessCondition> conditions = recognitionConditions.get(recognition);

      // Check if no condition exist, then simply add it to the list and map
      if (ExecueCoreUtil.isCollectionEmpty(conditions)) {
         conditions = new ArrayList<BusinessCondition>();
         conditions.add(condition);
         recognitionConditions.put(recognition, conditions);
      } else {
         // Check if it matches the already added conditions
         boolean isExistingConditionFound = false;
         for (BusinessCondition alreadyAddedCond : conditions) {
            if (alreadyAddedCond.getLhsBusinessTerm().getBusinessEntityTerm().equals(
                     condition.getLhsBusinessTerm().getBusinessEntityTerm())
                     && condition.getOperandType().equals(OperandType.BUSINESS_TERM)
                     && alreadyAddedCond.getOperandType().equals(OperandType.BUSINESS_TERM)) {
               for (BusinessTerm rhsTerm : condition.getRhsBusinessTerms()) {
                  addBusinessTermToList(alreadyAddedCond.getRhsBusinessTerms(), rhsTerm);
               }
               isExistingConditionFound = true;
               break;
            }
         }

         if (!isExistingConditionFound) {
            conditions.add(condition);
         }
      }

   }

   /**
    * Method to process the relative time frame provided by the User. It created Condition with a Dynamic value which
    * gets populated at later flow in Governer based on Assets.
    * 
    * @param businessQuery
    *           BusinessQuery
    * @param recognition
    *           Recognition Object
    * @param recognitionConditions
    * @param addedTimeFrame
    * @param index
    * @param components
    * @param businessTremMap
    * @param cohortBusinessTremMap
    * @param businessQuery
    * @param model
    * @throws KDXException
    */
   private void processRelativeTimeFrame (DomainRecognition recognition,
            Map<IGraphComponent, List<BusinessCondition>> recognitionConditions,
            List<DomainRecognition> addedTimeFrame, int index, List<IGraphComponent> components,
            Map<IGraphComponent, BusinessTerm> businessTremMap,
            Map<IGraphComponent, BusinessTerm> cohortBusinessTremMap, BusinessQuery businessQuery, Model model)
            throws KDXException {
      if (log.isDebugEnabled()) {
         log.debug("Recognition conceptBEDID : " + recognition.getConceptBEDId() + "; concept name : "
                  + recognition.getConceptName());
      }
      // TODO -NA- Commented the code for relative Normalized data Need to discuss on this.
      if (recognition.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RELATIVE_NORMALIZED_DATA) {
         return;
      }
      WeekDayNormalizedDataComponent weekDayNormalizedDataComponent = ((TimeFrameNormalizedData) recognition
               .getNormalizedData()).getWeekDayNormalizedDataComponent();
      if (weekDayNormalizedDataComponent != null) {
         populateMissingComponentsInWeekDayComponent(weekDayNormalizedDataComponent);
      }
      BusinessCondition dynamicCondition = new BusinessCondition();
      QueryValue queryValue = new QueryValue();
      queryValue.setNormalizedData(recognition.getNormalizedData());
      queryValue.setValueType(QueryValueType.NORMALIZED_OBJECT);
      dynamicCondition.setOperandType(OperandType.VALUE);
      List<BusinessTerm> alternateBTList = retriveAlternateBusinessTerms(recognition.getAlternateBedIds());
      dynamicCondition.setLhsBusinessTermVariations(alternateBTList);
      RelativeTimeNormalizedData normalizedData = (RelativeTimeNormalizedData) recognition.getNormalizedData();
      /*
       * OperatorType operatorType = null; if (normalizedData.getOperator() != null) { operatorType =
       * getOperatorForRelativeNormalizedData(normalizedData.getOperator()); } else { operatorType =
       * getOperatorBasedOnAdjective(normalizedData.getAdjective()); }
       */
      // TODO: -RG- for relative TF, only EQUALS to be passed in as operator to get correct conversion representation
      dynamicCondition.setOperator(OperatorType.EQUALS);

      List<QueryValue> queryvList = new ArrayList<QueryValue>(1);
      queryvList.add(queryValue);
      dynamicCondition.setRhsValues(queryvList);

      if (normalizedData.getNumber() == null) {
         NormalizedDataEntity normalizedDataEntity = new NormalizedDataEntity();
         normalizedDataEntity.setValue("1");
         normalizedData.setNumber(normalizedDataEntity);
      }
      normalizedData.setDynamicValueQualifierType(getDynamicQualifierType(normalizedData.getAdjective().getValue()));

      // TODO: KA - Getting TF concept from normalizedString . Check logic if this can be handled at
      // TimeFrameRulesProcessor
      BusinessEntityDefinition conceptDed = getKdxRetrievalService().getBusinessEntityDefinitionById(
               recognition.getConceptBEDId());
      BusinessEntityTerm businessEntityTerm = prepareBusinessEntity(conceptDed.getConcept(), conceptDed);
      BusinessTerm lhsTerm = new BusinessTerm();
      lhsTerm.setBusinessEntityTerm(businessEntityTerm);
      lhsTerm.setBusinessTermWeight(recognition.getWeight());
      lhsTerm.setRequestedByUser(true);
      dynamicCondition.setLhsBusinessTerm(lhsTerm);
      dynamicCondition.setNormalizedDataType(normalizedData.getNormalizedDataType());
      DomainRecognition conceptRecognition = null;
      boolean isCohort = false;
      if (!CollectionUtils.isEmpty(addedTimeFrame)) {
         DomainRecognition addedTFRecognition = addedTimeFrame.get(0);
         isCohort = checkForCohort(addedTFRecognition, recognition);
      }
      if (!isCohort) {
         addedTimeFrame.add(recognition);

      } else {
         if (index - 2 < 0) {
            return;
         }
         conceptRecognition = (DomainRecognition) CollectionUtils.get(components, index - 2);
         if (conceptRecognition == null) {
            return;
         }
         BusinessQuery cohortQuery = businessQuery.getCohort();
         if (cohortQuery == null) {
            cohortQuery = new BusinessQuery();
         }
         BusinessTerm businessTerm = businessTremMap.remove(components.get(index - 2));
         if (businessTerm == null) {
            businessTerm = getBusinessTerm((DomainRecognition) components.get(index - 2), model);
         }
         cohortBusinessTremMap.put(conceptRecognition, businessTerm);

         List<BusinessTerm> cohortmetrics = new ArrayList<BusinessTerm>();
         cohortmetrics.add(businessTerm);
         cohortQuery.setMetrics(cohortmetrics);
         cohortQuery.setModelId(businessQuery.getModelId());
         businessQuery.setCohort(cohortQuery);

      }
      if (conceptRecognition == null) {
         conceptRecognition = recognition;
      }
      List<BusinessCondition> conditionList = recognitionConditions.get(conceptRecognition);
      if (CollectionUtils.isEmpty(conditionList)) {
         conditionList = new ArrayList<BusinessCondition>();
      }
      conditionList.add(dynamicCondition);
      recognitionConditions.put(conceptRecognition, conditionList);

   }

   private OperatorType getOperatorBasedOnAdjective (NormalizedDataEntity adjective) {
      String qualifierVal = adjective.getDisplayValue();
      if (qualifierVal.equalsIgnoreCase("last")) {
         return OperatorType.GREATER_THAN;
      } else if (qualifierVal.equalsIgnoreCase("first")) {
         return OperatorType.LESS_THAN;
      } else if (qualifierVal.equalsIgnoreCase("next")) {
         return OperatorType.GREATER_THAN;
      }
      return null;
   }

   private OperatorType getOperatorForRelativeNormalizedData (NormalizedDataEntity operator) {
      String qualifierVal = operator.getDisplayValue();
      if (qualifierVal.equals(OperatorType.LESS_THAN.getValue())) {
         return OperatorType.GREATER_THAN_EQUALS;
      } else if (qualifierVal.equals(OperatorType.LESS_THAN_EQUALS.getValue())) {
         return OperatorType.GREATER_THAN_EQUALS;
      } else if (qualifierVal.equals(OperatorType.GREATER_THAN.getValue())) {
         return OperatorType.LESS_THAN;
      } else if (qualifierVal.equals(OperatorType.GREATER_THAN_EQUALS.getValue())) {
         return OperatorType.LESS_THAN_EQUALS;
      } else {
         return OperatorType.EQUALS;
      }

   }

   /**
    * Method to get the appropiate Qualifier Type based on passed String.
    * 
    * @param qualifierVal
    * @return
    * @throws KDXException
    */
   private DynamicValueQualifierType getDynamicQualifierType (String qualifierVal) throws KDXException {
      BusinessEntityDefinition instanceBed = getBaseKDXRetrievalService().getBusinessEntityDefinitionByNames(
               OntologyConstants.ADJECTIVE_CONCEPT, qualifierVal);
      qualifierVal = instanceBed.getInstance().getDisplayName();
      DynamicValueQualifierType dynamicValueQualifierType = null;
      if (qualifierVal.equalsIgnoreCase("last")) {
         dynamicValueQualifierType = DynamicValueQualifierType.LAST;
      } else if (qualifierVal.equalsIgnoreCase("first")) {
         dynamicValueQualifierType = DynamicValueQualifierType.FIRST;
      } else if (qualifierVal.equalsIgnoreCase("since")) {
         dynamicValueQualifierType = DynamicValueQualifierType.SINCE;
      } else if (qualifierVal.equalsIgnoreCase("before")) {
         dynamicValueQualifierType = DynamicValueQualifierType.BEFORE;
      } else if (qualifierVal.equalsIgnoreCase("after")) {
         dynamicValueQualifierType = DynamicValueQualifierType.AFTER;
      } else if (qualifierVal.equalsIgnoreCase("next")) {
         dynamicValueQualifierType = DynamicValueQualifierType.NEXT;
      }

      return dynamicValueQualifierType;
   }

   /**
    * @param term
    * @param model
    * @return
    * @throws KDXException
    */
   private List<BusinessTerm> getBusinessTermForInstance (DomainRecognition term, Model model, boolean isTimeInstance)
            throws KDXException {
      Instance instance = null;
      List<Long> instanceBedIdList = term.getInstanceBedIds();
      BusinessEntityDefinition instanceBed = null;
      List<BusinessEntityTerm> businessEntityTerms = new ArrayList<BusinessEntityTerm>();
      List<BusinessTerm> businessTerms = new ArrayList<BusinessTerm>();
      // TODO --NA-- Code below was for testing as was getting BEID as NULL.
      if (instanceBedIdList != null) {
         for (Long instanceBedId : instanceBedIdList) {
            instanceBed = kdxRetrievalService.getBusinessEntityDefinitionById(instanceBedId);
            instance = instanceBed.getInstance();
            BusinessEntityTerm businessEntityTerm = prepareBusinessEntity(instance, instanceBed);
            businessEntityTerms.add(businessEntityTerm);
         }
      } else {
         if (term.getNormalizedData() != null
                  && term.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
            List<Long> instanceBedIds = new ArrayList<Long>(1);
            ListNormalizedData normalizedData = (ListNormalizedData) term.getNormalizedData();
            for (NormalizedDataEntity normalizedDataEntity : normalizedData.getNormalizedDataEntities()) {
               if (normalizedDataEntity.getValueBedId() != null) {
                  instanceBedIds.add(normalizedDataEntity.getValueBedId());
               }
            }
            for (Long instanceBedId : instanceBedIds) {
               instanceBed = kdxRetrievalService.getBusinessEntityDefinitionById(instanceBedId);
               if (instanceBed != null) {
                  instance = instanceBed.getInstance();
                  BusinessEntityTerm businessEntityTerm = prepareBusinessEntity(instance, instanceBed);
                  businessEntityTerms.add(businessEntityTerm);
               }
            }
         } else if (term.getDefaultInstanceValue().contains(ExecueConstants.RANGE_DENOTER)) {
            List<String> instanceNames = ExecueStringUtil.getAsList(term.getDefaultInstanceValue(),
                     ExecueConstants.RANGE_DENOTER);
            for (String instanceName : instanceNames) {
               instanceBed = kdxRetrievalService.getBusinessEntityDefinitionByNames(model.getName(), term
                        .getConceptName(), instanceName);

               if (instanceBed != null) {
                  instance = instanceBed.getInstance();
                  BusinessEntityTerm businessEntityTerm = prepareBusinessEntity(instance, instanceBed);
                  businessEntityTerms.add(businessEntityTerm);
               }
            }
         }

         // else {
         // instanceBed = getBedByInstanceName(model.getName(), term);
         // BusinessEntityTerm businessEntityTerm = null;
         // if (instanceBed == null) {
         // if (isTimeInstance && term.getConceptBEDId() != null
         // && term.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_CONCEPT)) {
         // BusinessEntityDefinition conceptBed = kdxRetrievalService.getBusinessEntityDefinitionById(term
         // .getConceptBEDId());
         // Concept concept = conceptBed.getConcept();
         // businessEntityTerm = prepareBusinessEntity(concept, conceptBed);
         // businessEntityTerms.add(businessEntityTerm);
         // }
         // } else {
         // instance = kdxRetrievalService.getBusinessEntityDefinitionById(instanceBed.getId()).getInstance();
         // businessEntityTerm = prepareBusinessEntity(instance, instanceBed);
         // businessEntityTerms.add(businessEntityTerm);
         // }
         // }
      }
      for (BusinessEntityTerm businessEntityTerm : businessEntityTerms) {
         BusinessTerm businessTerm = new BusinessTerm();
         businessTerm.setBusinessEntityTerm(businessEntityTerm);
         if (term.getPosition() >= 0) {
            businessTerm.setRequestedByUser(true);
         }
         businessTerm.setBusinessTermWeight(term.getWeight());
         businessTerm.setNormalizedData(term.getNormalizedData());
         businessTerms.add(businessTerm);
      }
      return businessTerms;

   }

   /**
    * @param domainEntity
    * @param entityDed
    * @return
    */
   private BusinessEntityTerm prepareBusinessEntity (IBusinessEntity domainEntity, BusinessEntityDefinition entityDed) {
      BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
      businessEntityTerm.setBusinessEntity(domainEntity);
      businessEntityTerm.setBusinessEntityDefinitionId(entityDed.getId());
      if (entityDed.getEntityType().equals(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE)) {
         businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
      } else if (entityDed.getEntityType().equals(BusinessEntityType.CONCEPT)) {
         businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
      } else if (entityDed.getEntityType().equals(BusinessEntityType.CONCEPT_PROFILE)) {
         businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT_PROFILE);
      } else if (entityDed.getEntityType().equals(BusinessEntityType.INSTANCE_PROFILE)) {
         businessEntityTerm.setBusinessEntityType(BusinessEntityType.INSTANCE_PROFILE);
      }
      return businessEntityTerm;
   }

   private void processInstanceProfile (BusinessQuery businessQuery, DomainRecognition recognition,
            List<BusinessTerm> btList, Map<IGraphComponent, List<BusinessCondition>> recognitionConditions, Model model)
            throws KDXException {
      BusinessTerm rhsterm = getBusinessTerm(recognition, model);
      InstanceProfile instanceProfile = (InstanceProfile) rhsterm.getBusinessEntityTerm().getBusinessEntity();
      btList.add(rhsterm);
      List<BusinessCondition> conditions = recognitionConditions.get(recognition);
      if (conditions == null) {
         conditions = new ArrayList<BusinessCondition>();
      }
      // TODO: -JVK- revisit the logic to send false as the parameter
      BusinessCondition condition = generateCondition(btList, instanceProfile.getConcept(), businessQuery, model, false);
      for (BusinessCondition alreadyAddedCond : conditions) {
         if (condition != null
                  && alreadyAddedCond.getLhsBusinessTerm().getBusinessEntityTerm().equals(
                           condition.getLhsBusinessTerm().getBusinessEntityTerm())) {
            alreadyAddedCond.getRhsBusinessTerms().addAll(condition.getRhsBusinessTerms());
            condition = null;
            break;
         }
      }
      if (condition != null) {
         conditions.add(condition);
      }
      recognitionConditions.put(recognition, conditions);
   }

   private void processValueInstance (List<IGraphComponent> components,
            Map<IGraphComponent, List<BusinessTerm>> businessTremMap, int index, BusinessQuery businessQuery,
            DomainRecognition recognition, Model model, Map<IGraphComponent, BusinessTerm> cohortBusinessTremMap,
            Map<IGraphComponent, List<BusinessCondition>> recognitionConditions) throws KDXException {
      BusinessCondition cond = new BusinessCondition();
      if (index == 0) {
         return;
      }
      DomainRecognition lhsRecognition = (DomainRecognition) CollectionUtils.get(components, index - 2);
      List<BusinessTerm> lhsBusinessTerms = businessTremMap.get(lhsRecognition);
      BusinessTerm lhsBusinessTerm = null;
      if (CollectionUtils.isEmpty(lhsBusinessTerms) && businessQuery.getCohort() != null) {
         lhsBusinessTerm = cohortBusinessTremMap.get(lhsRecognition);
         businessQuery = businessQuery.getCohort();
      } else if (CollectionUtils.isEmpty(lhsBusinessTerms)) {
         return;
      } else {
         lhsBusinessTerm = lhsBusinessTerms.get(0);
      }
      if (lhsBusinessTerm == null) {
         log.error(" LHS Business Term is Null, This Should not have Happened");
         return;
      }

      if (ExecueConstants.MEASURABLE_ENTITY_TYPE.equalsIgnoreCase(lhsRecognition.getTypeName())
               && lhsBusinessTerm.getBusinessStat() == null) {
         lhsBusinessTerm.getBusinessEntityTerm().setMeasureConditionWithoutStat(CheckType.YES);
      }
      cond.setLhsBusinessTerm(lhsBusinessTerm);

      if (recognition.getNormalizedData() != null
               && recognition.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RANGE_NORMALIZED_DATA) {
         cond.setOperandType(OperandType.VALUE);
         List<QueryValue> qvs = new ArrayList<QueryValue>();
         RangeNormalizedData rangeNormalizedData = (RangeNormalizedData) recognition.getNormalizedData();
         NormalizedDataEntity startEntity = rangeNormalizedData.getStart();
         NormalizedDataEntity endEntity = rangeNormalizedData.getEnd();
         QueryValue startQV = null;
         QueryValue endQV = null;
         if (startEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.UNIT_NORMALIZED_DATA) {
            UnitNormalizedData startNormalizedData = (UnitNormalizedData) startEntity.getNormalizedData();
            UnitNormalizedData endNormalizedData = (UnitNormalizedData) endEntity.getNormalizedData();
            startQV = populateQVForValueNormalizedData(cond, startNormalizedData.getNumber(), startNormalizedData
                     .getUnitScale(), startNormalizedData.getUnitSymbol(), recognition.getConceptBEDId());
            endQV = populateQVForValueNormalizedData(cond, endNormalizedData.getNumber(), endNormalizedData
                     .getUnitScale(), endNormalizedData.getUnitSymbol(), recognition.getConceptBEDId());
         } else {
            ValueRealizationNormalizedData startNormalizedData = (ValueRealizationNormalizedData) startEntity
                     .getNormalizedData();
            ValueRealizationNormalizedData endNormalizedData = (ValueRealizationNormalizedData) endEntity
                     .getNormalizedData();
            startQV = populateQVForValueNormalizedData(cond, startNormalizedData.getNumber(), startNormalizedData
                     .getUnitScale(), startNormalizedData.getUnitSymbol(), recognition.getConceptBEDId());
            endQV = populateQVForValueNormalizedData(cond, endNormalizedData.getNumber(), endNormalizedData
                     .getUnitScale(), endNormalizedData.getUnitSymbol(), recognition.getConceptBEDId());
         }
         qvs.add(startQV);
         qvs.add(endQV);
         cond.setRhsValues(qvs);
         cond.setOperator(OperatorType.BETWEEN);

      } else if (recognition.getNormalizedData() != null
               && recognition.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
         cond.setOperandType(OperandType.VALUE);
         List<QueryValue> qvs = new ArrayList<QueryValue>();
         for (NormalizedDataEntity normalizedDataEntity : ((ListNormalizedData) recognition.getNormalizedData())
                  .getNormalizedDataEntities()) {
            ValueRealizationNormalizedData normalizedData = (ValueRealizationNormalizedData) normalizedDataEntity
                     .getNormalizedData();
            QueryValue qv = populateQVForValueNormalizedData(cond, normalizedData.getNumber(), normalizedData
                     .getUnitScale(), normalizedData.getUnitSymbol(), recognition.getConceptBEDId());
            qvs.add(qv);
         }

         // for (String value : values) {
         // QueryValue rhsValue = prepareQueryValue(value);
         // qvs.add(rhsValue);
         // }
         cond.setRhsValues(qvs);
         cond.setOperator(OperatorType.IN);
      } else {
         String values[] = recognition.toString().split(" ");
         List<QueryValue> qvs = new ArrayList<QueryValue>();
         cond.setOperandType(OperandType.VALUE);
         // /rhsValue.setNumberConversionId(numberConversionId)

         // if (values.length == 1) {
         // QueryValue rhsValue = prepareQueryValue(values[0]);
         // qvs.add(rhsValue);
         // cond.setRhsValues(qvs);
         // cond.setOperator(OperatorType.EQUALS);
         // } else {
         ValueRealizationNormalizedData normalizedData = (ValueRealizationNormalizedData) recognition
                  .getNormalizedData();
         QueryValue rhsValue = new QueryValue();
         NormalizedDataEntity number = normalizedData.getNumber();
         rhsValue.setValue(number.getValue());
         rhsValue.setDataType(DataType.NUMBER);
         NormalizedDataEntity unitScale = normalizedData.getUnitScale();
         if (unitScale != null) {
            processUnits(rhsValue, unitScale.getTypeBedId(), unitScale.getValueBedId());
         }
         NormalizedDataEntity unitSymbol = normalizedData.getUnitSymbol();
         if (unitSymbol != null) {
            processUnitSymbol(cond, recognition.getConceptBEDId(), unitSymbol.getValueBedId());
         }

         qvs.add(rhsValue);
         cond.setRhsValues(qvs);
         // Concept operatorConcept = baseKDXRetrievalService.getConceptByName(OntologyConstants.OPERATOR_CONCEPT);
         OperatorType opType = null;
         if (values[0].indexOf(",") > -1) {
            opType = processOperatorList(values[0]);
         } else {
            NormalizedDataEntity operator = normalizedData.getOperator();
            if (operator != null) {
               String displaySymbol = operator.getDisplaySymbol();
               opType = OperatorType.getOperatorType(displaySymbol);
               // opType = OperatorType.getOperatorType(normalizedData.getOperator().getDisplayValue());
            } else if (normalizedData.getValuePreposition() != null) {
               NormalizedDataEntity valuePreposition = normalizedData.getValuePreposition();
               String displaySymbol = valuePreposition.getDisplaySymbol();
               opType = OperatorType.getOperatorType(displaySymbol);
            } else {
               opType = OperatorType.EQUALS;
            }
         }
         cond.setOperator(opType);
         // }
      }
      List<BusinessCondition> conditions = recognitionConditions.get(lhsRecognition);
      if (conditions == null) {
         conditions = new ArrayList<BusinessCondition>();
         recognitionConditions.put(lhsRecognition, conditions);
      }
      conditions.add(cond);

   }

   /**
    * @param recognition
    * @param cond
    * @param conceptBedId
    * @param startNormalizedData
    * @return
    * @throws KDXException
    */
   private QueryValue populateQVForValueNormalizedData (BusinessCondition cond, NormalizedDataEntity number,
            NormalizedDataEntity unitScale, NormalizedDataEntity unitSymbol, Long conceptBedId) throws KDXException {
      QueryValue startQV = prepareQueryValue(number.getValue());
      if (unitScale != null) {
         processUnits(startQV, unitScale.getTypeBedId(), unitScale.getValueBedId());
      }
      if (unitSymbol != null) {
         processUnitSymbol(cond, conceptBedId, unitSymbol.getValueBedId());
      }
      return startQV;
   }

   /**
    * Operator may get recognized as a list if there is space between entered tokens. This method parse the string to
    * get the appropriate operator.
    * 
    * @param opString
    * @return
    * @throws KDXException
    */
   private OperatorType processOperatorList (String opString) throws KDXException {
      List<String> opList = ExecueStringUtil.getAsList(opString, ",");
      if (!CollectionUtils.isEmpty(opList)) {
         String operator1 = opList.get(0);
         String operator2 = opList.get(1);
         Instance opInst1 = baseKDXRetrievalService.getBusinessEntityDefinitionByNames(
                  OntologyConstants.OPERATOR_CONCEPT, operator1).getInstance();
         Instance opInst2 = baseKDXRetrievalService.getBusinessEntityDefinitionByNames(
                  OntologyConstants.OPERATOR_CONCEPT, operator2).getInstance();
         OperatorType firstOperator = OperatorType.getOperatorType(opInst1.getDisplayName());
         OperatorType secondOperator = OperatorType.getOperatorType(opInst2.getDisplayName());
         if (firstOperator.equals(secondOperator)) {
            return firstOperator;
         }
         if (firstOperator.equals(OperatorType.EQUALS) || secondOperator.equals(OperatorType.EQUALS)) {
            if (firstOperator.equals(OperatorType.GREATER_THAN) || secondOperator.equals(OperatorType.GREATER_THAN)) {
               return OperatorType.GREATER_THAN_EQUALS;
            }
            if (firstOperator.equals(OperatorType.LESS_THAN) || secondOperator.equals(OperatorType.LESS_THAN)) {
               return OperatorType.LESS_THAN_EQUALS;
            }
         }

      }
      return OperatorType.NOT_EQUALS;
   }

   /**
    * @param cond
    * @param values
    * @throws KDXException
    */
   private void processUnitSymbol (BusinessCondition cond, Long conceptBedId, Long symbolBedId) throws KDXException {

      // s List<Conversion> conversions = null;
      try {
         // conversions = getConversionService().getConversionsByType(ConversionType.CURRENCY);
         Conversion conversion = getConversionService().getConversionByConceptAndInstanceBedId(conceptBedId,
                  symbolBedId);
         cond.setConversionId(conversion.getId());
      } catch (SWIException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }

   }

   /**
    * @param values
    * @param rhsValue
    * @param b
    * @throws KDXException
    */
   private void processUnits (QueryValue rhsValue, Long typeBedId, Long scaleBedId) throws KDXException {
      try {
         // TODO -NA- HACK Hardcoding the TypeBeId of Number As Value Realization.
         typeBedId = 157L;
         Conversion conversion = getConversionService().getConversionByConceptAndInstanceBedId(typeBedId, scaleBedId);
         rhsValue.setNumberConversionId(conversion.getId());
      } catch (SWIException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }

   }

   /**
    * @param value
    * @return
    */
   private QueryValue prepareQueryValue (String value) {
      QueryValue rhsValue = new QueryValue();
      try {
         Integer.parseInt(value.trim());
         rhsValue.setDataType(DataType.NUMBER);
      } catch (Exception e) {
         rhsValue.setDataType(DataType.STRING);
      }
      rhsValue.setValue(value);
      return rhsValue;
   }

   private void processStatInstance (List<IGraphComponent> components,
            Map<IGraphComponent, List<BusinessTerm>> businessTremMap, int index, DomainRecognition recognition,
            List<BusinessTerm> profileConceptsList, Model model) throws KDXException {
      // TODO was getting DE ID null, below code was for testing
      Stat stat = null;
      List<Stat> stats = new ArrayList<Stat>(1);
      if (recognition.getDefaultInstanceBedId() == null) {
         if (recognition.getNormalizedData() != null
                  && recognition.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
            List<Long> instanceBedIds = new ArrayList<Long>(1);
            ListNormalizedData normalizedData = (ListNormalizedData) recognition.getNormalizedData();
            for (NormalizedDataEntity normalizedDataEntity : normalizedData.getNormalizedDataEntities()) {
               if (normalizedDataEntity.getValueBedId() != null) {
                  instanceBedIds.add(normalizedDataEntity.getValueBedId());
               }
            }
            for (Long instanceBedId : instanceBedIds) {
               BusinessEntityDefinition instanceBed = kdxRetrievalService
                        .getBusinessEntityDefinitionById(instanceBedId);
               if (instanceBed != null) {
                  Instance instance = instanceBed.getInstance();
                  List<Stat> statList = kdxRetrievalService.getAllStats();
                  for (Stat tmpStat : statList) {
                     if (tmpStat.getDisplayName().equalsIgnoreCase(instance.getDescription())) {
                        stat = tmpStat;
                        stats.add(tmpStat);
                        break;
                     }
                  }
               }
            }
         }

      } else {
         stat = kdxRetrievalService.getStatByBusinessEntityId(recognition.getDefaultInstanceBedId());
         stats.add(stat);
      }
      BusinessStat bStat = new BusinessStat();
      bStat.setStat(stat);
      bStat.setRequestedByUser(true);
      if (index - 2 < 0) {
         return;
      }
      List<BusinessStat> bstats = new ArrayList<BusinessStat>();
      for (Stat stat1 : stats) {
         BusinessStat bStat1 = new BusinessStat();
         bStat1.setStat(stat1);
         bStat1.setRequestedByUser(true);
         bstats.add(bStat1);
      }
      List<BusinessTerm> businessTerms = businessTremMap.get(components.get(index - 2));
      if (businessTerms == null) {
         return;
      }
      if (businessTerms.get(0).getBusinessEntityTerm().getBusinessEntityType().equals(
               BusinessEntityType.CONCEPT_PROFILE)) {
         // NK: Now we just need to set the business stat to the Concept Profile business term, as we handle them in
         // BusinessQueryOrganizationServiceImpl
         businessTerms.get(0).setBusinessStat(bStat);
      } else {
         BusinessTerm bTerm = businessTerms.get(0);
         businessTerms.clear();
         for (BusinessStat businessStat : bstats) {
            BusinessTerm businessTerm = new BusinessTerm();
            businessTerm.setBusinessEntityTerm(bTerm.getBusinessEntityTerm());
            businessTerm.setRequestedByUser(bTerm.isRequestedByUser());
            businessTerm.setBusinessTermWeight(bTerm.getBusinessTermWeight());
            businessTerm.setBusinessStat(businessStat);
            businessTerms.add(businessTerm);
         }
      }
   }

   /**
    * @param entity
    * @return
    */
   private BusinessTerm buildBusinessTerm (IBusinessEntity entity) {
      BusinessEntityTerm bet = new BusinessEntityTerm();
      bet.setBusinessEntity(entity);
      if (entity instanceof Instance) {
         bet.setBusinessEntityType(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
      } else if (entity instanceof Concept) {
         bet.setBusinessEntityType(BusinessEntityType.CONCEPT);
      }

      BusinessTerm bt = new BusinessTerm();
      bt.setBusinessEntityTerm(bet);
      return bt;
   }

   /**
    * @param rhsTerms
    * @param parentConcept
    * @param businessQuery
    * @param model
    * @return
    * @throws KDXException
    */
   private BusinessCondition generateCondition (List<BusinessTerm> rhsTerms, Concept parentConcept,
            BusinessQuery businessQuery, Model model, boolean isParentConceptFromBase) throws KDXException {

      BusinessEntityDefinition conceptBed = null;
      if (isParentConceptFromBase) {
         conceptBed = baseKDXRetrievalService.getBusinessEntityDefinitionByNames(parentConcept.getName(), null);
      } else {
         conceptBed = kdxRetrievalService.getBusinessEntityDefinitionByNames(model.getName(), parentConcept.getName(),
                  null);
      }
      boolean isInstanceFound = false;
      for (BusinessTerm businessTerm : rhsTerms) {
         BusinessEntityTerm entityTerm = businessTerm.getBusinessEntityTerm();
         if (entityTerm.getBusinessEntityType() == BusinessEntityType.INSTANCE_PROFILE) {
            isInstanceFound = true;
         }
         if (entityTerm.getBusinessEntityType().equals(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE)) {
            Instance instance = (Instance) entityTerm.getBusinessEntity();
            if (instance == null) {
               return null;
            }
            isInstanceFound = true;
            instance.setParentConcept(parentConcept);
         }
      }

      BusinessEntityTerm bet = new BusinessEntityTerm();
      bet.setBusinessEntity(parentConcept);
      bet.setBusinessEntityType(BusinessEntityType.CONCEPT);
      bet.setBusinessEntityDefinitionId(conceptBed.getId());
      bet.setDependantMeasure(getKdxModelService().checkEntityHasBehavior(conceptBed.getId(),
               BehaviorType.DEPENDENT_VARIABLE));

      BusinessTerm lhsBusinessTerm = new BusinessTerm();
      lhsBusinessTerm.setRequestedByUser(true);
      lhsBusinessTerm.setBusinessEntityTerm(bet);
      /*
       * try { Long centralConceptDedID =
       * baseKDXRetrievalService.getConceptBEDByName(OntologyConstants.POPULATION_CONCEPT) .getId(); if
       * (getOntologyService().hasParent(conceptDed.getId(), centralConceptDedID)) { List<BusinessTerm> populations =
       * businessQuery.getPopulations(); if (populations == null) { populations = new ArrayList<BusinessTerm>(); }
       * populations.add(lhsBusinessTerm); businessQuery.setPopulations(populations); } } catch (OntologyException e) {
       * throw new KDXException(e.getCode(), e.getMessage(), e); }
       */
      BusinessCondition cond = new BusinessCondition();
      cond.setLhsBusinessTerm(lhsBusinessTerm);
      cond.setRhsBusinessTerms(rhsTerms);
      if (isInstanceFound) {
         cond.setOperandType(OperandType.BUSINESS_TERM);
      } else {
         cond.setOperandType(OperandType.VALUE);
         cond.setRhsValues(getQueryValues(rhsTerms));
      }

      cond.setOperator(OperatorType.IN);
      return cond;
   }

   private List<QueryValue> getQueryValues (List<BusinessTerm> rhsTerms) {

      List<QueryValue> queryValues = new ArrayList<QueryValue>();
      for (BusinessTerm businessTerm : rhsTerms) {
         BusinessEntityTerm entityTerm = businessTerm.getBusinessEntityTerm();
         Concept concept = (Concept) entityTerm.getBusinessEntity();
         QueryValue queryValue = new QueryValue();
         queryValue.setDataType(DataType.STRING);
         // queryValue.setValue(concept.getSampleValue());
         queryValues.add(queryValue);
      }
      return queryValues;
   }

   /**
    * Populate Metrics for a business Query from the reduced forms.
    * 
    * @param reducedForm
    * @return business Query Object populated with metrics.
    * @throws SWIException
    */
   public BusinessQuery generateMetrics (SemanticPossibility reducedForm) throws NLPException {
      Graph reducedFormGraph = reducedForm.getReducedForm();
      // businessTerm Map will contain the business term as value and Graph vertex(Domain Recognition) as
      // key. Need to do this because while iteration through paths a end vertex of a path could be start vertex of
      // another path. and we don't want to add the same business entitiy.
      Map<IGraphComponent, BusinessTerm> businessTremMap = new HashMap<IGraphComponent, BusinessTerm>();
      Collection<IGraphComponent> rootVertices = reducedForm.getRootVertices();
      if (reducedFormGraph.getPaths() != null) {
         for (IGraphPath path : reducedFormGraph.getPaths()) {
            DomainRecognition startVertex = (DomainRecognition) path.getStartVertex();
            if (startVertex.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_CONCEPT)) {
               if (!businessTremMap.containsKey(startVertex)) {
                  BusinessTerm bstartTerm = getBusinessTerm(startVertex, reducedForm.getModel());
                  if (startVertex.getPosition() >= 0) {
                     bstartTerm.setRequestedByUser(true);
                  }
                  bstartTerm.setBusinessTermWeight(startVertex.getWeight());
                  businessTremMap.put(startVertex, bstartTerm);
               }
               DomainRecognition endVertex = (DomainRecognition) path.getEndVertex();
               if (endVertex.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_CONCEPT)) {
                  if (!businessTremMap.containsKey(endVertex)) {
                     BusinessTerm endTerm = getBusinessTerm(endVertex, reducedForm.getModel());
                     if (endVertex.getPosition() >= 0) {
                        endTerm.setRequestedByUser(true);
                     }
                     endTerm.setBusinessTermWeight(endVertex.getWeight());
                     businessTremMap.put(endVertex, endTerm);
                  }
               } else if (endVertex.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_INSTANCE)) {
                  if (endVertex.getConceptName().equalsIgnoreCase(OntologyConstants.STATISTICS_CONCEPT)) {
                     try {
                        Stat stat = kdxRetrievalService.getStatByBusinessEntityId(endVertex.getConceptBEDId());
                        BusinessStat bStat = new BusinessStat();
                        bStat.setStat(stat);
                        bStat.setRequestedByUser(true);
                        businessTremMap.get(startVertex).setBusinessStat(bStat);
                     } catch (Exception e) {
                        // TODO: -NA- handle exception
                     }

                  }
               }
            }
         }
      }
      for (IGraphComponent rv : rootVertices) {
         DomainRecognition vertex = (DomainRecognition) rv;
         if (!businessTremMap.containsKey(vertex) && vertex.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_CONCEPT)) {
            BusinessTerm bTerm = getBusinessTerm(vertex, reducedForm.getModel());
            if (vertex.getPosition() >= 0) {
               bTerm.setRequestedByUser(true);
            }
            bTerm.setBusinessTermWeight(vertex.getWeight());
            businessTremMap.put(vertex, bTerm);
         }
      }
      BusinessQuery businessQuery = new BusinessQuery();
      List<BusinessTerm> selects = new ArrayList<BusinessTerm>();
      Iterator<BusinessTerm> itr = businessTremMap.values().iterator();
      while (itr.hasNext()) {
         selects.add(itr.next());
      }
      businessQuery.setMetrics(selects);
      return businessQuery;
   }

   private BusinessTerm getBusinessTerm (DomainRecognition term, Model model) {
      BusinessEntityTerm entityTerm = createBusinessEntityTerm(term, model);
      BusinessTerm businessTerm = new BusinessTerm();
      businessTerm.setBusinessEntityTerm(entityTerm);
      if (term.getPosition() >= 0) {
         businessTerm.setRequestedByUser(true);
      }
      businessTerm.setBusinessTermWeight(term.getWeight());
      return businessTerm;
   }

   public static INormalizedData mergeTFNormalizedDataForTimeInformation (TimeFrameNormalizedData normalizedData,
            TimeFrameNormalizedData normalizedData2) {
      try {
         TimeFrameNormalizedData tfNormalizedData = (TimeFrameNormalizedData) normalizedData.clone();
         TimeInformation timeInformation1 = TimeFrameUtility.getTimeInformation(normalizedData);
         TimeInformation timeInformation2 = TimeFrameUtility.getTimeInformation(normalizedData2);
         WeekDayNormalizedDataComponent weekDayNormalizedDataComponent = tfNormalizedData
                  .getWeekDayNormalizedDataComponent();
         weekDayNormalizedDataComponent.addTimeInformation(timeInformation1);
         weekDayNormalizedDataComponent.addTimeInformation(timeInformation2);
         weekDayNormalizedDataComponent.setOperator(OperatorType.BETWEEN);
         return tfNormalizedData;
      } catch (CloneNotSupportedException e) {
         throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
      }
   }

   /**
    * @param modelName
    * @param term
    * @return
    * @throws KDXException
    */
   // private BusinessEntityDefinition getBedByInstanceName (String modelName, DomainRecognition term) throws
   // KDXException {
   // return kdxRetrievalService.getBusinessEntityDefinitionByNames(modelName, term.getConceptName(), term
   // .getInstanceName());
   // // String instanceName = term.getInstanceName().indexOf(",") > 0 ? term.getInstanceName().substring(0,
   // // term.getInstanceName().length() - 1) : term.getInstanceName();
   // // return kdxRetrievalService.getBED getBedByInstanceName(instanceName);
   // }
   /**
    * @param term
    * @param model
    * @return
    */
   private BusinessEntityTerm createBusinessEntityTerm (DomainRecognition term, Model model) {
      try {
         BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
         businessEntityTerm.setDependantMeasure(term.getBehaviors().contains(BehaviorType.DEPENDENT_VARIABLE));
         if (ExecueConstants.MEASURABLE_ENTITY_TYPE.equalsIgnoreCase(term.getTypeName())) {
            businessEntityTerm.setMeasurableEntity(true);
         }
         if (term.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_CONCEPT)) {
            Concept concept = kdxRetrievalService.getBusinessEntityDefinitionById(term.getConceptBEDId()).getConcept();
            businessEntityTerm.setBusinessEntity(concept);
            businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
            businessEntityTerm.setBusinessEntityDefinitionId(term.getConceptBEDId());
         } else if (term.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_CONCEPT_PROFILE)) {

            ConceptProfile conceptProfile = (ConceptProfile) getPreferencesRetrievalService().getProfile(model.getId(),
                     term.getProfileName(), ProfileType.CONCEPT);
            businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT_PROFILE);
            businessEntityTerm.setBusinessEntity(conceptProfile);
            BusinessEntityDefinition domainEntityDefinition = getPreferencesRetrievalService()
                     .getBusinessEntityDefinitionForConceptProfile(conceptProfile);
            if (domainEntityDefinition != null) {
               businessEntityTerm.setBusinessEntityDefinitionId(domainEntityDefinition.getId());
            }

         } else if (term.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_INSTANCE_PROFILE)) {
            InstanceProfile ip = (InstanceProfile) getPreferencesRetrievalService().getProfile(model.getId(),
                     term.getProfileName(), ProfileType.CONCEPT_LOOKUP_INSTANCE);
            businessEntityTerm.setBusinessEntity(ip);
            BusinessEntityDefinition domainEntityDefinition = getPreferencesRetrievalService()
                     .getBusinessEntityDefinitionForInstanceProfile(ip);
            if (domainEntityDefinition != null) {
               businessEntityTerm.setBusinessEntityDefinitionId(domainEntityDefinition.getId());
            }
            businessEntityTerm.setBusinessEntityType(BusinessEntityType.INSTANCE_PROFILE);
         }
         return businessEntityTerm;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   /*
    * private Concept castConcept(Concept c){ }
    */

   private BusinessEntityTerm getBusinessEntityTerm (String termID, String nlpTag,
            Map<String, BusinessEntityTerm> termMap, Concept concept, Model model) {
      if (termMap.containsKey(termID)) {
         if (log.isTraceEnabled()) {
            log.trace("returning BusinessEntityTerm from cache Map");
         }
         return termMap.get(termID);
      } else {
         BusinessEntityTerm businessEntityTerm = createBusinessEntityTerm(termID, nlpTag, concept, model);
         termMap.put(termID, businessEntityTerm);
         return businessEntityTerm;
      }
   }

   private BusinessEntityTerm createBusinessEntityTerm (String termID, String nlpTag, Concept concept, Model model) {
      if (log.isTraceEnabled()) {
         log.trace("creating BusinessEntityTerm for " + termID);
      }
      try {
         BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
         if (nlpTag.equals(NLPConstants.NLP_TAG_ONTO_CONCEPT)) {
            concept = kdxRetrievalService.getPopulatedConceptWithStats(model.getId(), termID);
            businessEntityTerm.setBusinessEntity(concept);
            businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
         } else if (nlpTag.equals(NLPConstants.NLP_TAG_ONTO_INSTANCE)) {
            BusinessEntityDefinition businessEntityDefinition = kdxRetrievalService.getBusinessEntityDefinitionByNames(
                     model.getName(), concept.getName(), termID);
            businessEntityTerm.setBusinessEntity(businessEntityDefinition.getInstance());
            businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
         }
         return businessEntityTerm;

      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public static List<ArrayList<IGraphComponent>> depthFirstPaths (Graph g, IGraphComponent gc) {
      List<ArrayList<IGraphComponent>> sb = new ArrayList<ArrayList<IGraphComponent>>();
      List<IGraphPath> paths = g.getPaths();
      if (gc.getType().equals(GraphComponentType.Vertex)) {
         if (paths != null) {
            for (IGraphPath path : paths) {
               if (path.getStartVertex().equals(gc)) {
                  List<ArrayList<IGraphComponent>> tempPath = depthFirstPaths(g, path.getEndVertex());
                  if (tempPath != null && tempPath.size() > 0) {
                     for (int i = 0; i < tempPath.size(); i++) {
                        ArrayList<IGraphComponent> p = new ArrayList<IGraphComponent>();
                        p.add(path.getConnectors().get(0));
                        p.add(path.getEndVertex());
                        p.addAll(tempPath.get(i));
                        sb.add(p);
                     }
                  } else {
                     ArrayList<IGraphComponent> p = new ArrayList<IGraphComponent>();
                     p.add(path.getConnectors().get(0));
                     p.add(path.getEndVertex());
                     sb.add(p);
                  }
               }
            }
         }
      } else {
         // TODO Throw Exception
         log.error("You can not start at a Non-vertex Component.");
      }
      return sb;
   }

   /**
    * @return the kdxModelService
    */
   public IKDXModelService getKdxModelService () {
      return kdxModelService;
   }

   /**
    * @param kdxModelService
    *           the kdxModelService to set
    */
   public void setKdxModelService (IKDXModelService kdxModelService) {
      this.kdxModelService = kdxModelService;
   }

   /**
    * @return the conversionService
    */
   public IConversionService getConversionService () {
      return conversionService;
   }

   /**
    * @param conversionService
    *           the conversionService to set
    */
   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return preferencesRetrievalService;
   }

   public void setPreferencesRetrievalService (IPreferencesRetrievalService preferencesRetrievalService) {
      this.preferencesRetrievalService = preferencesRetrievalService;
   }

}