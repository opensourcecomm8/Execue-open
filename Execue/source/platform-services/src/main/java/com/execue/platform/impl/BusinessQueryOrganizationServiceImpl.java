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


package com.execue.platform.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.BusinessCondition;
import com.execue.core.common.bean.BusinessOrderClause;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.HierarchyTerm;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.common.bean.entity.HierarchyDetail;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.QuerySectionType;
import com.execue.core.common.type.UserInterfaceType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.IBusinessQueryOrganizationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathDefinitionRetrievalService;

public class BusinessQueryOrganizationServiceImpl implements IBusinessQueryOrganizationService {

   private IKDXRetrievalService            kdxRetrievalService;
   private IKDXModelService                kdxModelService;
   private IPathDefinitionRetrievalService pathDefinitionRetrievalService;

   private boolean                         handleTimeFrameConditions = false;
   public static final Logger              logger                    = Logger
                                                                              .getLogger(BusinessQueryOrganizationServiceImpl.class);

   public void organizeBusinessQuery (BusinessQuery query) {
      try {
         disintergrateProfiles(query);
         organizeQuery(query);
         organizeCohortQuery(query);
         if (handleTimeFrameConditions) {
            handleTimeFrameConditions(query);
         }
         fillAlternateTimeFrameConcepts(query);
         organizeBusinessQueryForHierachies(query);
      } catch (KDXException e) {
         e.printStackTrace();
      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   private void fillAlternateTimeFrameConcepts (BusinessQuery query) throws SWIException {
      if (UserInterfaceType.QUERY_INTERFACE.equals(query.getUserInterfaceType())) {
         fillAlternateTimeFrameConcepts(query.getConditions());
         if (query.getCohort() != null) {
            fillAlternateTimeFrameConcepts(query.getCohort().getConditions());
         }
      }
   }

   private void fillAlternateTimeFrameConcepts (List<BusinessCondition> conditions) throws SWIException {
      if (ExecueCoreUtil.isCollectionNotEmpty(conditions)) {
         for (BusinessCondition businessCondition : conditions) {
            if (isTimeFrameCondition(businessCondition)) {
               Set<Long> convertableBedIds = getPathDefinitionRetrievalService().getConvertableTypePaths(
                        businessCondition.getLhsBusinessTerm().getBusinessEntityTerm().getBusinessEntityDefinitionId());
               if (ExecueCoreUtil.isCollectionNotEmpty(convertableBedIds)) {
                  List<BusinessTerm> convertableBusinessTerms = populateBusinessTerms(convertableBedIds);
                  businessCondition.setLhsBusinessTermVariations(convertableBusinessTerms);
               }
            }
         }
      }
   }

   private List<BusinessTerm> populateBusinessTerms (Set<Long> bedIds) throws KDXException {
      List<BusinessTerm> businessTerms = new ArrayList<BusinessTerm>();
      for (Long bedId : bedIds) {
         BusinessEntityDefinition businessEntityDefinition = getKdxRetrievalService().getBusinessEntityDefinitionById(
                  bedId);

         BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
         businessEntityTerm.setBusinessEntity(businessEntityDefinition.getConcept());
         businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
         businessEntityTerm.setBusinessEntityDefinitionId(businessEntityDefinition.getId());

         BusinessTerm businessTerm = new BusinessTerm();
         businessTerm.setBusinessEntityTerm(businessEntityTerm);
         businessTerm.setRequestedByUser(false);
         businessTerms.add(businessTerm);
      }
      return businessTerms;
   }

   private boolean isTimeFrameCondition (BusinessCondition businessCondition) {
      boolean isTimeFrameCondition = false;
      if (NormalizedDataType.TIME_FRAME_NORMALIZED_DATA.equals(businessCondition.getNormalizedDataType())
               || NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA.equals(businessCondition.getNormalizedDataType())) {
         isTimeFrameCondition = true;
      }
      return isTimeFrameCondition;
   }

   private void handleTimeFrameConditions (BusinessQuery query) {
      if (ExecueCoreUtil.isCollectionNotEmpty(query.getConditions())) {
         List<BusinessCondition> validBusinessConditions = new ArrayList<BusinessCondition>();
         for (BusinessCondition businessCondition : query.getConditions()) {
            if (isTFConditionValid(businessCondition)) {
               validBusinessConditions.add(businessCondition);
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(validBusinessConditions)) {
            query.setConditions(validBusinessConditions);
         }
      }
   }

   private boolean isTFConditionValid (BusinessCondition businessCondition) {
      boolean isTFConditionValid = true;
      try {
         if (NormalizedDataType.TIME_FRAME_NORMALIZED_DATA.equals(businessCondition.getNormalizedDataType())) {
            for (QueryValue queryValue : businessCondition.getRhsValues()) {
               if (!NormalizedDataType.TIME_FRAME_NORMALIZED_DATA.equals(queryValue.getNormalizedData()
                        .getNormalizedDataType())) {
                  isTFConditionValid = false;
                  break;
               }
            }
         } else if (NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA.equals(businessCondition.getNormalizedDataType())) {
            for (QueryValue queryValue : businessCondition.getRhsValues()) {
               if (!NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA.equals(queryValue.getNormalizedData()
                        .getNormalizedDataType())) {
                  isTFConditionValid = false;
                  break;
               }
            }
         }
      } catch (ClassCastException classCastException) {
         logger.error("Class cast exception in TF condition");
         isTFConditionValid = false;
      }
      return isTFConditionValid;
   }

   private void disintergrateProfiles (BusinessQuery query) throws KDXException {
      disintegrateConceptProfiles(query.getMetrics(), query.getModelId());
      disintegrateConceptProfiles(query.getSummarizations(), query.getModelId());
      disintegrateInstanceProfiles(query.getConditions(), query.getModelId());
   }

   private void disintegrateInstanceProfiles (List<BusinessCondition> businessConditions, Long modelId)
            throws KDXException {
      if (ExecueCoreUtil.isCollectionNotEmpty(businessConditions)) {
         for (BusinessCondition businessCondition : businessConditions) {
            BusinessTerm lhsBusinessTerm = businessCondition.getLhsBusinessTerm();
            if (OperandType.BUSINESS_TERM.equals(businessCondition.getOperandType())) {
               List<BusinessTerm> rhsBusinessTerms = businessCondition.getRhsBusinessTerms();
               List<BusinessTerm> instanceProfileBusinessTerms = new ArrayList<BusinessTerm>();
               List<BusinessTerm> instanceProfileDisintegratedBusinessTerms = new ArrayList<BusinessTerm>();
               for (BusinessTerm businessTerm : rhsBusinessTerms) {
                  if (BusinessEntityType.INSTANCE_PROFILE.equals(businessTerm.getBusinessEntityTerm()
                           .getBusinessEntityType())) {
                     Concept parentConcept = (Concept) lhsBusinessTerm.getBusinessEntityTerm().getBusinessEntity();
                     instanceProfileBusinessTerms.add(businessTerm);
                     instanceProfileDisintegratedBusinessTerms.addAll(getInstanceProfileBusinessTerms(businessTerm,
                              parentConcept, modelId));
                  }
               }
               if (ExecueCoreUtil.isCollectionNotEmpty(instanceProfileBusinessTerms)) {
                  rhsBusinessTerms.removeAll(instanceProfileBusinessTerms);
                  if (ExecueCoreUtil.isCollectionNotEmpty(instanceProfileDisintegratedBusinessTerms)) {
                     for (BusinessTerm businessTerm : instanceProfileDisintegratedBusinessTerms) {
                        addUpdateMatchedTermToList(rhsBusinessTerms, businessTerm, QuerySectionType.CONDITION, false,
                                 false, true);
                     }
                  }
                  adjustOperatorForInstanceProfiles(businessCondition);
               }
            }
         }
      }
   }

   private void adjustOperatorForInstanceProfiles (BusinessCondition businessCondition) {
      switch (businessCondition.getOperator()) {
         case EQUALS:
            businessCondition.setOperator(OperatorType.IN);
            break;
         case GREATER_THAN:
            businessCondition.setOperator(OperatorType.IN);
            break;
         case GREATER_THAN_EQUALS:
            businessCondition.setOperator(OperatorType.IN);
            break;
         case LESS_THAN:
            businessCondition.setOperator(OperatorType.IN);
            break;
         case LESS_THAN_EQUALS:
            businessCondition.setOperator(OperatorType.IN);
            break;
         case BETWEEN:
            businessCondition.setOperator(OperatorType.IN);
            break;
         case NOT_EQUALS:
            businessCondition.setOperator(OperatorType.NOT_IN);
            break;
      }
   }

   private void disintegrateConceptProfiles (List<BusinessTerm> businessTerms, Long modelId) throws KDXException {
      if (ExecueCoreUtil.isCollectionNotEmpty(businessTerms)) {
         List<BusinessTerm> conceptProfileBusinessTerms = new ArrayList<BusinessTerm>();
         List<BusinessTerm> conceptProfileDisintegratedBusinessTerms = new ArrayList<BusinessTerm>();
         for (BusinessTerm businessTerm : businessTerms) {
            if (BusinessEntityType.CONCEPT_PROFILE.equals(businessTerm.getBusinessEntityTerm().getBusinessEntityType())) {
               conceptProfileBusinessTerms.add(businessTerm);
               conceptProfileDisintegratedBusinessTerms.addAll(getConceptProfileBusinessTerms(businessTerm, modelId));
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(conceptProfileBusinessTerms)) {
            businessTerms.removeAll(conceptProfileBusinessTerms);
            if (ExecueCoreUtil.isCollectionNotEmpty(conceptProfileDisintegratedBusinessTerms)) {
               for (BusinessTerm businessTerm : conceptProfileDisintegratedBusinessTerms) {
                  addUpdateMatchedTermToList(businessTerms, businessTerm, QuerySectionType.SELECT, false, false, true);
               }
            }
         }
      }
   }

   private List<BusinessTerm> getConceptProfileBusinessTerms (BusinessTerm profileBusinessTerm, Long modelId)
            throws KDXException {
      List<BusinessTerm> conceptProfileBusinessTerms = new ArrayList<BusinessTerm>();
      BusinessEntityTerm conceptProfileBusinessEntityTerm = profileBusinessTerm.getBusinessEntityTerm();
      if (BusinessEntityType.CONCEPT_PROFILE.equals(conceptProfileBusinessEntityTerm.getBusinessEntityType())) {
         ConceptProfile conceptProfile = (ConceptProfile) conceptProfileBusinessEntityTerm.getBusinessEntity();
         Set<Concept> concepts = conceptProfile.getConcepts();
         if (ExecueCoreUtil.isCollectionNotEmpty(concepts)) {
            for (Concept concept : concepts) {
               BusinessEntityDefinition businessEntityDefinition = kdxRetrievalService
                        .getBusinessEntityDefinitionByIds(modelId, concept.getId(), null);

               BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
               businessEntityTerm.setBusinessEntity(concept);
               businessEntityTerm.setBusinessEntityDefinitionId(businessEntityDefinition.getId().longValue());
               businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
               businessEntityTerm.setDependantMeasure(getKdxModelService().checkEntityHasBehavior(
                        businessEntityDefinition.getId(), BehaviorType.DEPENDENT_VARIABLE));

               BusinessTerm businessTerm = new BusinessTerm();
               businessTerm.setBusinessStat(profileBusinessTerm.getBusinessStat());
               businessTerm.setRequestedByUser(profileBusinessTerm.isRequestedByUser());
               businessTerm.setBusinessEntityTerm(businessEntityTerm);
               businessTerm.setProfileBusinessEntityDefinitionId(conceptProfileBusinessEntityTerm
                        .getBusinessEntityDefinitionId());
               String profileName = ((ConceptProfile) conceptProfileBusinessEntityTerm.getBusinessEntity())
                        .getDisplayName();
               businessTerm.setProfileName(profileName);

               conceptProfileBusinessTerms.add(businessTerm);
            }
         }
      }
      return conceptProfileBusinessTerms;
   }

   private List<BusinessTerm> getInstanceProfileBusinessTerms (BusinessTerm profileBusinessTerm,
            Concept parentConceptForInstanceProfile, Long modelId) throws KDXException {
      List<BusinessTerm> instanceProfileBusinessTerms = new ArrayList<BusinessTerm>();
      BusinessEntityTerm instanceProfileBusinessEntityTerm = profileBusinessTerm.getBusinessEntityTerm();
      if (BusinessEntityType.INSTANCE_PROFILE.equals(instanceProfileBusinessEntityTerm.getBusinessEntityType())) {
         InstanceProfile instanceProfile = (InstanceProfile) instanceProfileBusinessEntityTerm.getBusinessEntity();
         Set<Instance> instances = instanceProfile.getInstances();
         if (ExecueCoreUtil.isCollectionNotEmpty(instances)) {
            for (Instance instance : instances) {
               BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
               BusinessEntityDefinition businessEntityDefinition = kdxRetrievalService
                        .getBusinessEntityDefinitionByIds(modelId, parentConceptForInstanceProfile.getId(), instance
                                 .getId());
               instance.setParentConcept(parentConceptForInstanceProfile);
               businessEntityTerm.setBusinessEntity(instance);
               businessEntityTerm.setBusinessEntityDefinitionId(businessEntityDefinition.getId().longValue());
               businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
               BusinessTerm businessTerm = new BusinessTerm();
               businessTerm.setRequestedByUser(profileBusinessTerm.isRequestedByUser());
               businessTerm.setBusinessEntityTerm(businessEntityTerm);
               businessTerm.setProfileBusinessEntityDefinitionId(instanceProfileBusinessEntityTerm
                        .getBusinessEntityDefinitionId());
               String profileName = ((InstanceProfile) instanceProfileBusinessEntityTerm.getBusinessEntity())
                        .getDisplayName();
               businessTerm.setProfileName(profileName);
               instanceProfileBusinessTerms.add(businessTerm);
            }
         }
      }
      return instanceProfileBusinessTerms;
   }

   private void organizeQuery (BusinessQuery query) {
      List<BusinessTerm> selectList = query.getMetrics();
      for (BusinessTerm businessTerm : selectList) {
         businessTerm.getQuerySectionTypes().add(QuerySectionType.SELECT);
      }

      if (ExecueCoreUtil.isCollectionNotEmpty(query.getSummarizations())) {
         for (BusinessTerm businessTerm : query.getSummarizations()) {
            addUpdateMatchedTermToList(selectList, businessTerm, QuerySectionType.GROUP, false, false, false);
         }
      }

      if (query.getTopBottom() != null) {
         addUpdateMatchedTermToList(selectList, query.getTopBottom().getBusinessTerm(), QuerySectionType.TOP_BOTTOM,
                  false, false, false);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(query.getOrderClauses())) {
         for (BusinessOrderClause businessOrderClause : query.getOrderClauses()) {
            addUpdateMatchedTermToList(selectList, businessOrderClause.getBusinessTerm(), QuerySectionType.ORDER,
                     false, false, false);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(query.getConditions())) {
         List<BusinessCondition> conditions = query.getConditions();
         for (BusinessCondition businessCondition : conditions) {
            addUpdateMatchedTermToList(selectList, businessCondition.getLhsBusinessTerm(), QuerySectionType.CONDITION,
                     false, false, false);
            if (ExecueCoreUtil.isCollectionNotEmpty(businessCondition.getLhsBusinessTermVariations())) {
               for (BusinessTerm businessTerm : businessCondition.getLhsBusinessTermVariations()) {
                  businessTerm.setAlternateEntity(true);
                  if (businessTerm.getBusinessEntityTerm() != null) {
                     businessTerm.getBusinessEntityTerm().setAlternateEntity(true);
                  }
               }
            }
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(query.getPopulations())) {
         List<BusinessTerm> populations = query.getPopulations();
         for (BusinessTerm businessTerm : populations) {
            addUpdateMatchedTermToList(selectList, businessTerm, QuerySectionType.POPULATION, true, false, false);
         }
      }

   }

   private void organizeCohortQuery (BusinessQuery query) throws KDXException {
      BusinessQuery cohortQuery = query.getCohort();
      if (cohortQuery != null) {
         List<BusinessTerm> cohortMetrics = new ArrayList<BusinessTerm>();
         List<BusinessTerm> metrics = query.getMetrics();
         List<BusinessTerm> summarizations = query.getSummarizations();
         if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getSummarizations())) {
            for (BusinessTerm businessTerm : cohortQuery.getSummarizations()) {
               // move the groupby of cohort to cohort metrics
               addUpdateMatchedTermToList(cohortMetrics, businessTerm, QuerySectionType.COHORT_GROUP, false, false,
                        false);
               // move the groupby of cohort to main metrics
               addUpdateMatchedTermToList(metrics, businessTerm, QuerySectionType.COHORT_GROUP, false, true, false);
               // move the groupby of cohort to main groupby
               addUpdateMatchedTermToList(summarizations, businessTerm, QuerySectionType.COHORT_GROUP, false, true,
                        false);
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getConditions())) {
            List<BusinessCondition> conditions = cohortQuery.getConditions();
            for (BusinessCondition businessCondition : conditions) {
               // move only if the condition is on measure
               BusinessTerm lhsBusinessTerm = businessCondition.getLhsBusinessTerm();
               if (getKdxRetrievalService().isMatchedBusinessEntityType(
                        lhsBusinessTerm.getBusinessEntityTerm().getBusinessEntityDefinitionId(),
                        ExecueConstants.MEASURABLE_ENTITY_TYPE)) {
                  // move the conditions of cohort to cohort metrics
                  addUpdateMatchedTermToList(cohortMetrics, lhsBusinessTerm, QuerySectionType.COHORT_CONDITION, false,
                           false, false);
                  // move the conditions of cohort to main metrics as main metric
                  addUpdateMatchedTermToList(metrics, lhsBusinessTerm, QuerySectionType.COHORT_CONDITION, false, false,
                           false);
                  // move the conditions of cohort to main metrics as cohort metric
                  BusinessTerm clonedBusinessTerm = ExecueBeanCloneUtil.cloneBusinessTerm(lhsBusinessTerm);
                  clonedBusinessTerm.setFromCohort(true);
                  clonedBusinessTerm.getQuerySectionTypes().add(QuerySectionType.COHORT_CONDITION);
                  metrics.add(clonedBusinessTerm);
               }
            }
         }
         cohortQuery.setMetrics(cohortMetrics);
      }
   }

   private void addUpdateMatchedTermToList (List<BusinessTerm> businessTermsList, BusinessTerm toBeAddedBusinessTerm,
            QuerySectionType querySectionType, boolean fromPopulation, boolean fromCohort, boolean isRequestedByUser) {
      BusinessTerm matchedBusinessTerm = getMatchedBusinessTerm(toBeAddedBusinessTerm, businessTermsList);
      if (matchedBusinessTerm == null) {
         BusinessTerm clonedBusinessTerm = ExecueBeanCloneUtil.cloneBusinessTerm(toBeAddedBusinessTerm);
         clonedBusinessTerm.setFromCohort(fromCohort);
         clonedBusinessTerm.setFromPopulation(fromPopulation);
         clonedBusinessTerm.setRequestedByUser(isRequestedByUser);
         clonedBusinessTerm.getQuerySectionTypes().add(querySectionType);
         businessTermsList.add(clonedBusinessTerm);
      } else {
         if (toBeAddedBusinessTerm.getBusinessStat() != null) {
            int currIndex = businessTermsList.indexOf(matchedBusinessTerm);
            businessTermsList.remove(matchedBusinessTerm);
            BusinessTerm clonedBusinessTerm = ExecueBeanCloneUtil.cloneBusinessTerm(toBeAddedBusinessTerm);
            clonedBusinessTerm.setFromCohort(fromCohort);
            clonedBusinessTerm.setFromPopulation(fromPopulation);
            clonedBusinessTerm.getQuerySectionTypes().add(querySectionType);
            businessTermsList.add(currIndex, clonedBusinessTerm);
         } else {
            matchedBusinessTerm.setFromCohort(fromCohort);
            matchedBusinessTerm.setFromPopulation(fromPopulation);
            matchedBusinessTerm.getQuerySectionTypes().add(querySectionType);
         }
      }
   }

   /**
    * This method returns the matched business term among the list
    * 
    * @param currBusinessTerm
    * @param selectList
    * @return matchedTerm
    */
   private BusinessTerm getMatchedBusinessTerm (BusinessTerm currBusinessTerm, List<BusinessTerm> selectList) {
      BusinessTerm matchedTerm = null;
      for (BusinessTerm businessTerm : selectList) {
         if (businessTerm.getBusinessEntityTerm().equals(currBusinessTerm.getBusinessEntityTerm())) {
            if (businessTerm.getBusinessStat() != null && currBusinessTerm.getBusinessStat() != null) {
               if (businessTerm.getBusinessStat().getStat().getStatType().equals(
                        currBusinessTerm.getBusinessStat().getStat().getStatType())) {
                  matchedTerm = businessTerm;
               }
            } else {
               matchedTerm = businessTerm;
            }
         }
      }
      return matchedTerm;
   }

   /**
    * This method collects BusinessEntityDefinitionIds from all the sections of the BusinessQuery and populates list of
    * HierarchyTerms and sets in to the Hierarchies section of the BusuinessQuery.
    * 
    * @param businessQuery
    * @throws KDXException
    */

   private void organizeBusinessQueryForHierachies (BusinessQuery businessQuery) throws KDXException {
      Set<Long> bedIdsForHirarchy = collectBusinessEntityIdsForHierarchy(businessQuery);
      if (ExecueCoreUtil.isCollectionNotEmpty(bedIdsForHirarchy)) {
         List<HierarchyTerm> hierarchyTerms = createHierarchyTerms(new ArrayList<Long>(bedIdsForHirarchy));
         businessQuery.setHierarchies(hierarchyTerms);
      }
   }

   private List<HierarchyTerm> createHierarchyTerms (List<Long> businessEntityIds) throws KDXException {
      List<HierarchyTerm> hierarchyTerms = new ArrayList<HierarchyTerm>();
      List<Hierarchy> hierarchies = getKdxRetrievalService().getHierarchiesByConceptBEDIds(businessEntityIds);
      if (ExecueCoreUtil.isCollectionNotEmpty(hierarchies)) {
         for (Hierarchy hierarchy : hierarchies) {
            HierarchyTerm hierarchyTerm = new HierarchyTerm();
            hierarchyTerm.setHierarchyId(hierarchy.getId());
            hierarchyTerm.setHierarchyName(hierarchy.getName());
            hierarchyTerm.setHierarchyBusinessDefinition(createBusinessTermsFromHierarchy(hierarchy
                     .getHierarchyDetails()));
            hierarchyTerm
                     .setParticipatingQueryEntityIDs(createParticipatingQueryEntityIDs(hierarchy, businessEntityIds));
            hierarchyTerms.add(hierarchyTerm);
         }
      }
      return hierarchyTerms;
   }

   private List<Long> createParticipatingQueryEntityIDs (Hierarchy hierarchy, List<Long> businessEntityIds) {
      List<Long> participatingQueryEntityIDs = new ArrayList<Long>();
      List<Long> bedIdsFromHierarchies = new ArrayList<Long>();
      for (HierarchyDetail hierarchyDetail : hierarchy.getHierarchyDetails()) {
         bedIdsFromHierarchies.add(hierarchyDetail.getConceptBedId());
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(bedIdsFromHierarchies)) {
         for (Long bedId : bedIdsFromHierarchies) {
            if (businessEntityIds.contains(bedId)) {
               participatingQueryEntityIDs.add(bedId);
            }
         }
      }
      return participatingQueryEntityIDs;
   }

   private List<BusinessTerm> createBusinessTermsFromHierarchy (Set<HierarchyDetail> hierarchyDetails)
            throws KDXException {
      List<BusinessTerm> businessTerms = new ArrayList<BusinessTerm>();
      for (HierarchyDetail hierarchyDetail : hierarchyDetails) {
         BusinessEntityDefinition businessEntityDefinition = getKdxRetrievalService().getBusinessEntityDefinitionById(
                  hierarchyDetail.getConceptBedId());
         BusinessTerm businessTerm = new BusinessTerm();
         BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
         businessEntityTerm.setBusinessEntity(businessEntityDefinition.getConcept());
         businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
         businessEntityTerm.setBusinessEntityDefinitionId(businessEntityDefinition.getId());
         businessTerm.setBusinessEntityTerm(businessEntityTerm);
         businessTerm.setRequestedByUser(false);
         businessTerms.add(businessTerm);
      }
      return businessTerms;
   }

   // Get all the BusinessEntityDefinitionIds
   private Set<Long> collectBusinessEntityIdsForHierarchy (BusinessQuery businessQuery) {
      Set<Long> businessEntityIds = new HashSet<Long>();
      populateBusinessEntityIds(businessQuery.getMetrics(), businessEntityIds);
      populateBusinessEntityIds(businessQuery.getSummarizations(), businessEntityIds);
      populateBusinessEntityIds(businessQuery.getPopulations(), businessEntityIds);
      populateBusinessEntityIdsForConditionClauses(businessQuery.getConditions(), businessEntityIds);
      populateBusinessEntityIdsForConditionClauses(businessQuery.getHavingClauses(), businessEntityIds);
      populateBusinessEntityIdsForOrderClauses(businessQuery.getOrderClauses(), businessEntityIds);
      if (businessQuery.getCohort() != null) {
         businessEntityIds.addAll(collectBusinessEntityIdsForHierarchy(businessQuery.getCohort()));
      }
      if (businessQuery.getTopBottom() != null) {
         populateBusinessEntityIds(businessQuery.getTopBottom().getBusinessTerm(), businessEntityIds);
      }
      return businessEntityIds;
   }

   private void populateBusinessEntityIds (List<BusinessTerm> businessTerms, Set<Long> businessEntityIds) {
      if (ExecueCoreUtil.isCollectionNotEmpty(businessTerms)) {
         for (BusinessTerm businessTerm : businessTerms) {
            BusinessEntityType businessEntityType = businessTerm.getBusinessEntityTerm().getBusinessEntityType();
            if (BusinessEntityType.CONCEPT == businessEntityType && businessTerm.isRequestedByUser()) {
               businessEntityIds.add(businessTerm.getBusinessEntityTerm().getBusinessEntityDefinitionId());
            }
         }
      }
   }

   private void populateBusinessEntityIds (BusinessTerm businessTerm, Set<Long> businessEntityIds) {
      BusinessEntityType businessEntityType = businessTerm.getBusinessEntityTerm().getBusinessEntityType();
      if (BusinessEntityType.CONCEPT == businessEntityType && businessTerm.isRequestedByUser()) {
         businessEntityIds.add(businessTerm.getBusinessEntityTerm().getBusinessEntityDefinitionId());
      }
   }

   private void populateBusinessEntityIdsForConditionClauses (List<BusinessCondition> businessConditions,
            Set<Long> businessEntityIds) {
      if (ExecueCoreUtil.isCollectionNotEmpty(businessConditions)) {
         for (BusinessCondition businessCondition : businessConditions) {
            // Collect LHS part BED id and then collect for variations as well.
            businessEntityIds.add(businessCondition.getLhsBusinessTerm().getBusinessEntityTerm()
                     .getBusinessEntityDefinitionId());
            if (ExecueCoreUtil.isCollectionNotEmpty(businessCondition.getLhsBusinessTermVariations())) {
               populateBusinessEntityIds(businessCondition.getLhsBusinessTermVariations(), businessEntityIds);
            }
            // On the basis of operand type collect the BED id's .
            switch (businessCondition.getOperandType()) {
               case BUSINESS_TERM:
                  populateBusinessEntityIds(businessCondition.getRhsBusinessTerms(), businessEntityIds);
                  break;
               case BUSINESS_QUERY:
                  Set<Long> subQueryBEDIds = collectBusinessEntityIdsForHierarchy(businessCondition
                           .getRhsBusinessQuery());
                  if (ExecueCoreUtil.isCollectionNotEmpty(subQueryBEDIds)) {
                     businessEntityIds.addAll(subQueryBEDIds);
                  }
                  break;
            }
         }
      }
   }

   private void populateBusinessEntityIdsForOrderClauses (List<BusinessOrderClause> businessOrderClauses,
            Set<Long> businessEntityIds) {
      if (ExecueCoreUtil.isCollectionNotEmpty(businessOrderClauses)) {
         for (BusinessOrderClause businessOrderClause : businessOrderClauses) {
            populateBusinessEntityIds(businessOrderClause.getBusinessTerm(), businessEntityIds);
         }
      }
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IPathDefinitionRetrievalService getPathDefinitionRetrievalService () {
      return pathDefinitionRetrievalService;
   }

   public void setPathDefinitionRetrievalService (IPathDefinitionRetrievalService pathDefinitionRetrievalService) {
      this.pathDefinitionRetrievalService = pathDefinitionRetrievalService;
   }

   /**
    * @return the kdxModelService
    */
   public IKDXModelService getKdxModelService () {
      return kdxModelService;
   }

   /**
    * @param kdxModelService the kdxModelService to set
    */
   public void setKdxModelService (IKDXModelService kdxModelService) {
      this.kdxModelService = kdxModelService;
   }
}
