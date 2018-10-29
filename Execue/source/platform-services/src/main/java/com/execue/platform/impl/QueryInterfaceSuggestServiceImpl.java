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
import java.util.List;

import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.qi.suggest.SuggestConditionTerm;
import com.execue.core.common.bean.qi.suggest.SuggestTerm;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.SuggestTermType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.IQueryInterfaceSuggestService;
import com.execue.platform.exception.PlatformException;
import com.execue.swi.dataaccess.IQueryInterfaceSuggestDataAccessManager;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXRetrievalService;

/**
 * @author kaliki
 * @since 4.0
 */
public class QueryInterfaceSuggestServiceImpl implements IQueryInterfaceSuggestService {

   private IQueryInterfaceSuggestDataAccessManager queryInterfaceSuggestDataAccessManager;
   private IKDXRetrievalService                    kdxRetrievalService;

   // This method is not being implemented currently and is ON HOLD
   public List<SuggestTerm> suggestBTsAndStatsForSelect (Long modelId, String searchString, Long userId)
            throws PlatformException {
      List<SuggestTerm> terms = new ArrayList<SuggestTerm>();
      try {
         terms.addAll(prepareSuggestTermList(queryInterfaceSuggestDataAccessManager.suggestConcepts(searchString,
                  modelId, userId)));
         terms.addAll(prepareConceptProfileBasedSuggestTermList(queryInterfaceSuggestDataAccessManager
                  .suggestConceptProfiles(searchString, userId, modelId)));
         terms
                  .addAll(prepareStatBasedSuggestTermList(queryInterfaceSuggestDataAccessManager
                           .suggestStats(searchString)));
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
      // TODO: get profiles
      // TODO: Sort based on searachString
      return getOrderedSuggestedTerms(searchString, terms);
   }

   public List<SuggestTerm> suggestBTsForSelect (Long modelId, String searchString, String statName, Long userId)
            throws PlatformException {
      List<SuggestTerm> terms = new ArrayList<SuggestTerm>();
      try {
         terms.addAll(prepareSuggestTermList(queryInterfaceSuggestDataAccessManager.suggestBTsForSelect(searchString,
                  statName, userId, modelId)));
         terms.addAll(prepareConceptProfileBasedSuggestTermList(queryInterfaceSuggestDataAccessManager
                  .suggestConceptProfiles(searchString, modelId, userId)));
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
      return terms;
   }

   public List<SuggestTerm> suggestBTsForPopulation (Long modelId, String searchString, Long userId)
            throws PlatformException {
      List<SuggestTerm> terms = new ArrayList<SuggestTerm>();
      try {
         terms.addAll(prepareSuggestTermList(queryInterfaceSuggestDataAccessManager.suggestBTsForPopulation(
                  searchString, userId, modelId)));
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
      return terms;
   }

   public List<SuggestConditionTerm> suggestBTsAndStatsForWhereLHS (Long modelId, String searchString, Long userId)
            throws PlatformException {

      List<SuggestConditionTerm> terms = new ArrayList<SuggestConditionTerm>();
      try {
         terms.addAll(prepareSuggestConditionTermFromConcepts(queryInterfaceSuggestDataAccessManager
                  .suggestBTsForWhereLHS(searchString, modelId, userId), modelId, true));
         terms.addAll(prepareSuggestConditionTermFromStats(queryInterfaceSuggestDataAccessManager
                  .suggestStats(searchString)));
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
      return getOrderedSuggestedConditionTerms(searchString, terms);
   }

   public List<SuggestConditionTerm> suggestBTsForWhereLHS (Long modelId, String searchString, Long userId,
            String statName) throws PlatformException {
      List<SuggestConditionTerm> terms = new ArrayList<SuggestConditionTerm>();
      try {
         terms.addAll(prepareSuggestConditionTermFromConcepts(queryInterfaceSuggestDataAccessManager
                  .suggestBTsForWhereLHS(searchString, statName, userId, modelId), modelId, false));
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
      return terms;
   }

   public List<SuggestTerm> suggestBTAndValuesForWhereRHS (Long modelId, String businessTerm, String searchString,
            Long userId) throws PlatformException {
      List<SuggestTerm> terms = new ArrayList<SuggestTerm>();
      try {
         terms.addAll(prepareSuggestTermListForInstances(queryInterfaceSuggestDataAccessManager
                  .suggestBTAndValuesForWhereRHS(businessTerm, searchString, userId, modelId)));
         terms.addAll(prepareInstanceProfileBasedSuggestTermList(queryInterfaceSuggestDataAccessManager
                  .suggestInstanceProfilesForWhereRHS(businessTerm, searchString, userId, modelId)));

      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
      return terms;
   }

   public List<SuggestTerm> suggestBTsForOrderBy (Long modelId, String searchString, Long userId)
            throws PlatformException {
      List<SuggestTerm> terms = new ArrayList<SuggestTerm>();
      try {
         terms.addAll(prepareSuggestTermList(queryInterfaceSuggestDataAccessManager.suggestConcepts(searchString,
                  modelId, userId)));
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
      return terms;
   }

   public List<SuggestTerm> suggestBTsForSummarize (Long modelId, String searchString, Long userId)
            throws PlatformException {
      List<SuggestTerm> terms = new ArrayList<SuggestTerm>();
      try {
         terms.addAll(prepareSuggestTermList(queryInterfaceSuggestDataAccessManager.suggestConcepts(searchString,
                  modelId, userId)));
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
      return terms;
   }

   private static List<SuggestTerm> getOrderedSuggestedTerms (String searchString, List<SuggestTerm> suggestTerms) {
      List<SuggestTerm> startingSuggestTerms = new ArrayList<SuggestTerm>();
      for (SuggestTerm suggestTerm : suggestTerms) {
         String suggestTermName = suggestTerm.getDisplayName().toLowerCase();
         String lowerCaseSearchString = searchString.toLowerCase();
         if (suggestTermName.startsWith(lowerCaseSearchString)) {
            startingSuggestTerms.add(suggestTerm);
         }
      }
      suggestTerms.removeAll(startingSuggestTerms);
      suggestTerms.addAll(0, startingSuggestTerms);
      return suggestTerms;
   }

   public static List<SuggestConditionTerm> getOrderedSuggestedConditionTerms (String searchString,
            List<SuggestConditionTerm> suggestConditionTerms) {
      List<SuggestConditionTerm> startingSuggestTerms = new ArrayList<SuggestConditionTerm>();
      for (SuggestConditionTerm suggestConditionTerm : suggestConditionTerms) {
         String suggestTermName = suggestConditionTerm.getDisplayName().toLowerCase();
         String lowerCaseSearchString = searchString.toLowerCase();
         if (suggestTermName.startsWith(lowerCaseSearchString)) {
            startingSuggestTerms.add(suggestConditionTerm);
         }
      }
      suggestConditionTerms.removeAll(startingSuggestTerms);
      suggestConditionTerms.addAll(0, startingSuggestTerms);
      return suggestConditionTerms;
   }

   private List<SuggestTerm> prepareSuggestTermList (List<Concept> concepts) {
      List<SuggestTerm> suggestTerms = new ArrayList<SuggestTerm>();
      for (Concept concept : concepts) {
         SuggestTerm suggestTerm = new SuggestTerm();
         suggestTerm.setId(concept.getId());
         suggestTerm.setName(concept.getName());
         suggestTerm.setDisplayName(concept.getDisplayName());
         suggestTerm.setType(SuggestTermType.CONCEPT);
         suggestTerms.add(suggestTerm);
      }
      return suggestTerms;
   }

   private List<SuggestTerm> prepareSuggestTermListForInstances (List<Instance> instances) {
      List<SuggestTerm> suggestTerms = new ArrayList<SuggestTerm>();
      for (Instance instance : instances) {
         SuggestTerm suggestTerm = new SuggestTerm();
         suggestTerm.setId(instance.getId());
         suggestTerm.setName(instance.getName());
         String displayName = instance.getDisplayName();
         if (ExecueCoreUtil.isNotEmpty(instance.getAbbreviatedName())) {
            displayName += " (" + instance.getAbbreviatedName() + ")";
         }
         suggestTerm.setDisplayName(displayName);
         suggestTerm.setType(SuggestTermType.CONCEPT_LOOKUP_INSTANCE);
         suggestTerms.add(suggestTerm);
      }
      return suggestTerms;
   }

   private List<SuggestTerm> prepareConceptProfileBasedSuggestTermList (List<ConceptProfile> conceptProfiles) {
      List<SuggestTerm> suggestTerms = new ArrayList<SuggestTerm>();
      for (ConceptProfile conceptProfile : conceptProfiles) {
         SuggestTerm suggestTerm = new SuggestTerm();
         suggestTerm.setId(conceptProfile.getId());
         suggestTerm.setName(conceptProfile.getName());
         suggestTerm.setDisplayName(conceptProfile.getDisplayName());
         suggestTerm.setType(SuggestTermType.CONCEPT_PROFILE);
         suggestTerms.add(suggestTerm);
      }
      return suggestTerms;
   }

   private List<SuggestTerm> prepareStatBasedSuggestTermList (List<Stat> stats) {
      List<SuggestTerm> suggestTerms = new ArrayList<SuggestTerm>();
      for (Stat stat : stats) {
         SuggestTerm suggestTerm = new SuggestTerm();
         suggestTerm.setId(stat.getId());
         suggestTerm.setName(stat.getStatType().getValue());
         suggestTerm.setDisplayName(stat.getDisplayName());
         suggestTerm.setType(SuggestTermType.STAT);
         suggestTerms.add(suggestTerm);
      }
      return suggestTerms;
   }

   private List<SuggestTerm> prepareInstanceProfileBasedSuggestTermList (List<InstanceProfile> instanceProfiles) {
      List<SuggestTerm> suggestTerms = new ArrayList<SuggestTerm>();
      for (InstanceProfile instanceProfile : instanceProfiles) {
         SuggestTerm suggestTerm = new SuggestTerm();
         suggestTerm.setId(instanceProfile.getId());
         suggestTerm.setName(instanceProfile.getName());
         suggestTerm.setDisplayName(instanceProfile.getDisplayName());
         suggestTerm.setType(SuggestTermType.CONCEPT_LOOKUP_INSTANCE_PROFILE);
         suggestTerms.add(suggestTerm);
      }
      return suggestTerms;
   }

   private List<SuggestConditionTerm> prepareSuggestConditionTermFromStats (List<Stat> stats) {
      List<SuggestConditionTerm> suggestTerms = new ArrayList<SuggestConditionTerm>();
      for (Stat stat : stats) {
         SuggestConditionTerm suggestConditionTerm = new SuggestConditionTerm();
         suggestConditionTerm.setId(stat.getId());
         suggestConditionTerm.setName(stat.getStatType().getValue());
         suggestConditionTerm.setDisplayName(stat.getDisplayName());
         suggestConditionTerm.setType(SuggestTermType.STAT);
         suggestTerms.add(suggestConditionTerm);
      }
      return suggestTerms;
   }

   private List<SuggestConditionTerm> prepareSuggestConditionTermFromConcepts (List<Concept> concepts, Long modelId,
            boolean checkHasInstances) throws SWIException {
      List<SuggestConditionTerm> suggestConditionTerms = new ArrayList<SuggestConditionTerm>();
      for (Concept concept : concepts) {
         SuggestConditionTerm suggestConditionTerm = new SuggestConditionTerm();
         suggestConditionTerm.setId(concept.getId());
         suggestConditionTerm.setName(concept.getName());
         suggestConditionTerm.setDisplayName(concept.getDisplayName());
         suggestConditionTerm.setType(SuggestTermType.CONCEPT);
         suggestConditionTerm.setUnitType(concept.getDefaultUnit());
         suggestConditionTerm.setDataFromat(concept.getDefaultDataFormat());
         String conceptDataType = ConversionType.DEFAULT.getValue();
         if (concept.getDefaultConversionType() != null) {
            conceptDataType = concept.getDefaultConversionType().getValue();
         }
         suggestConditionTerm.setDatatype(conceptDataType);
         // TODO: -JVK- change the way the domainId is passed
         if (checkHasInstances) {
            suggestConditionTerm.setHasInstances(getKdxRetrievalService().hasInstances(modelId, concept.getId()));
         }
         suggestConditionTerms.add(suggestConditionTerm);
      }
      return suggestConditionTerms;
   }

   public IQueryInterfaceSuggestDataAccessManager getQueryInterfaceSuggestDataAccessManager () {
      return queryInterfaceSuggestDataAccessManager;
   }

   public void setQueryInterfaceSuggestDataAccessManager (
            IQueryInterfaceSuggestDataAccessManager queryInterfaceSuggestDataAccessManager) {
      this.queryInterfaceSuggestDataAccessManager = queryInterfaceSuggestDataAccessManager;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

}