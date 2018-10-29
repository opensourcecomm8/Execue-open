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


package com.execue.swi.service.impl;

import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.ParallelWord;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.ProfileType;
import com.execue.swi.dataaccess.IPreferencesDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PreferencesException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPreferencesRetrievalService;

public class PreferencesRetrievalServiceImpl implements IPreferencesRetrievalService {

   private IPreferencesDataAccessManager preferencesDataAccessManager;
   private IKDXRetrievalService          kdxRetrievalService;
   private IApplicationRetrievalService  applicationRetrievalService;

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   // *************************** Methods related to Range ************************************************

   public Range getRange (Long rangeId) throws PreferencesException {
      Range range = getPreferencesDataAccessManager().getRange(rangeId);
      try {
         for (RangeDetail eachRangeDetail : range.getRangeDetails()) {
            eachRangeDetail.getDescription();
         }
         Concept concept = getKdxRetrievalService().getConceptByBEDId(range.getConceptBedId());
         range.setConceptDisplayName(concept.getDisplayName());
      } catch (KDXException kdxException) {
         throw new PreferencesException(kdxException.getCode(), kdxException);
      }
      return range;
   }

   public List<Range> getExistingRangesForConcept (Long conceptBedId, Long userId) throws PreferencesException {
      List<Range> existingRanges = getPreferencesDataAccessManager().getExistingRangesForConcept(conceptBedId, userId);
      for (Range eachRange : existingRanges) {
         for (RangeDetail eachRangeDetail : eachRange.getRangeDetails()) {
            eachRangeDetail.getDescription();
         }
         eachRange.getUser().getUsername();
      }
      return existingRanges;
   }

   public List<Range> getUserDefinedRangeForConceptForDynamicRangeEvaluation (Long conceptBedId)
            throws PreferencesException {
      Long userId = 1L;
      List<Range> existingRanges = getPreferencesDataAccessManager()
               .getUserDefinedRangeForConceptForDynamicRangeEvaluation(conceptBedId, userId);
      for (Range eachRange : existingRanges) {
         for (RangeDetail eachRangeDetail : eachRange.getRangeDetails()) {
            eachRangeDetail.getDescription();
         }
      }
      return existingRanges;
   }

   public List<Range> getRanges (Long modelId) throws PreferencesException {
      return getPreferencesDataAccessManager().getRanges(modelId);
   }

   // *************************** Methods related to Profiles ************************************************

   public List<ConceptProfile> getConceptProfiles (Long modelId, Long userId) throws PreferencesException {
      return getPreferencesDataAccessManager().getConceptProfiles(modelId, userId);
   }

   public List<InstanceProfile> getInstanceProfiles (Long modelId, Long userId) throws PreferencesException {
      return getPreferencesDataAccessManager().getInstanceProfiles(modelId, userId);
   }

   public List<InstanceProfile> getInstanceProfilesForConcept (Long modelId, Long conceptId)
            throws PreferencesException {
      return getPreferencesDataAccessManager().getInstanceProfilesForConcept(modelId, conceptId);
   }

   public Profile getProfile (Long profileId, ProfileType profileType) throws PreferencesException {
      Profile profile = getPreferencesDataAccessManager().getProfile(profileId, profileType);
      ConceptProfile conceptProfile = null;
      InstanceProfile instanceProfile = null;
      if (ProfileType.CONCEPT.equals(profile.getType())) {
         conceptProfile = (ConceptProfile) profile;
         Set<Concept> concepts = conceptProfile.getConcepts();
         for (Concept concept : concepts) {
            concept.getDisplayName();
         }
         conceptProfile.getUser().getFirstName();
      } else {
         instanceProfile = (InstanceProfile) profile;
         Concept concept = instanceProfile.getConcept();
         concept.getName();
         Set<Instance> instances = instanceProfile.getInstances();
         for (Instance instance : instances) {
            instance.getDisplayName();
            instance.setParentConcept(concept);
         }
         instanceProfile.getUser().getFirstName();
      }
      return profile;
   }

   public Profile getProfile (Long modelId, String profileName, ProfileType profileType) throws PreferencesException {
      Profile profile = getPreferencesDataAccessManager().getProfile(modelId, profileName, profileType);
      ConceptProfile conceptProfile = null;
      InstanceProfile instanceProfile = null;
      if (ProfileType.CONCEPT.equals(profile.getType())) {
         conceptProfile = (ConceptProfile) profile;
         Set<Concept> concepts = conceptProfile.getConcepts();
         for (Concept concept : concepts) {
            concept.getDisplayName();
         }
         conceptProfile.getUser().getFirstName();
      } else {
         instanceProfile = (InstanceProfile) profile;
         Concept concept = instanceProfile.getConcept();
         concept.getName();
         Set<Instance> instances = instanceProfile.getInstances();
         for (Instance instance : instances) {
            instance.getDisplayName();
            instance.setParentConcept(concept);
         }
         instanceProfile.getUser().getFirstName();
      }
      return profile;
   }

   public List<Profile> getConceptProfiles (List<ModelGroup> userModelGroups) throws PreferencesException {
      return getPreferencesDataAccessManager().getConceptProfiles(userModelGroups);
   }

   public List<Profile> getInstanceProfiles (List<ModelGroup> userModelGroups) throws PreferencesException {
      return getPreferencesDataAccessManager().getInstanceProfiles(userModelGroups);
   }

   // *************************** Methods related to Concept ************************************************

   public List<Concept> getConceptsForConceptProfiles (Long modelId) throws PreferencesException {
      try {
         if (AppSourceType.STRUCTURED.equals(getApplicationRetrievalService().getApplicationByModelId(modelId)
                  .getSourceType())) {
            return getPreferencesDataAccessManager().getMappedConceptsForConceptProfiles(modelId);
         } else {
            return getPreferencesDataAccessManager().getConceptsForConceptProfiles(modelId);
         }
      } catch (KDXException kdxException) {
         throw new PreferencesException(kdxException.getCode(), kdxException);
      }
   }

   public List<Concept> getConceptsForInstanceProfiles (Long modelId) throws PreferencesException {
      try {
         if (AppSourceType.STRUCTURED.equals(getApplicationRetrievalService().getApplicationByModelId(modelId)
                  .getSourceType())) {
            return getPreferencesDataAccessManager().getMappedConceptsForInstanceProfiles(modelId);
         } else {
            return getPreferencesDataAccessManager().getConceptsForInstanceProfiles(modelId);
         }
      } catch (KDXException kdxException) {
         throw new PreferencesException(kdxException.getCode(), kdxException);
      }
   }

   public List<Concept> getConceptsForRanges (Long modelId) throws PreferencesException {
      return getPreferencesDataAccessManager().getConceptsForConceptRanges(modelId);
   }

   public List<Concept> getConceptsForHybridProfiles (long modelId) throws PreferencesException {
      try {
         if (AppSourceType.STRUCTURED.equals(getApplicationRetrievalService().getApplicationByModelId(modelId)
                  .getSourceType())) {
            return getPreferencesDataAccessManager().getMappedConceptsForHybridProfiles(modelId);
         } else {
            return getPreferencesDataAccessManager().getConceptsForHybridProfiles(modelId);
         }
      } catch (KDXException kdxException) {
         throw new PreferencesException(kdxException.getCode(), kdxException);
      }
   }

   // *************************** Methods related to BED'S ************************************************

   public BusinessEntityDefinition getBusinessEntityDefinitionForConceptProfile (ConceptProfile conceptProfile)
            throws PreferencesException {
      return getPreferencesDataAccessManager().getBusinessEntityDefinitionForConceptProfile(conceptProfile);
   }

   public BusinessEntityDefinition getBusinessEntityDefinitionForInstanceProfile (InstanceProfile instanceProfile)
            throws PreferencesException {
      return getPreferencesDataAccessManager().getBusinessEntityDefinitionForInstanceProfile(instanceProfile);
   }

   // *************************** Methods related to ParallelWord's,Keyword's,RIOntoterm **************************

   public List<RIParallelWord> getRIParallelWordsForKeyWord (Long keyWordId) throws PreferencesException {
      return getPreferencesDataAccessManager().getRIParallelWordsForKeyWord(keyWordId);
   }

   public List<ParallelWord> getParallelWordsForKeyWord (Long keyWordId) throws PreferencesException {
      return getPreferencesDataAccessManager().getParallelWordsForKeyWord(keyWordId);
   }

   public ParallelWord getParallelWord (Long parallelWordId) throws PreferencesException {
      return getPreferencesDataAccessManager().getParallelWord(parallelWordId);
   }

   public KeyWord getKeyWord (Long keyWordId) throws PreferencesException {
      return getPreferencesDataAccessManager().getKeyWord(keyWordId);
   }

   public KeyWord getKeyWord (String wordName, Long modelId) throws PreferencesException {
      return getPreferencesDataAccessManager().getKeyWord(wordName, modelId);
   }

   public KeyWord getKeyWord (String wordName, Long userId, Long modelId) throws PreferencesException {
      return getPreferencesDataAccessManager().getKeyWord(wordName, modelId);
   }

   public List<KeyWord> getAllKeyWords (Long modelId) throws PreferencesException {
      return getPreferencesDataAccessManager().getAllKeyWords(modelId);

   }

   public List<ParallelWord> getParallelWordsForUser (User user, KeyWord keyword) throws PreferencesException {
      return getPreferencesDataAccessManager().getParallelWordsForUser(keyword.getId(), user.getId());
   }

   public ParallelWord getParallelWordByName (String wordName, Long keyWordId, Long userId) throws PreferencesException {
      return getPreferencesDataAccessManager().getParallelWordByName(wordName, keyWordId, userId);
   }

   public List<RIOntoTerm> getRIontoTermsForKeyWord (String keyWord) throws PreferencesException {
      return getPreferencesDataAccessManager().getRIontoTermsForKeyWord(keyWord);
   }

   public boolean doesKeyWordExist (Long modelId, String keyWordname) throws PreferencesException {
      return getPreferencesDataAccessManager().doesKeyWordExists(modelId, keyWordname);

   }

   public boolean isParallelWordExist (Long modelId, Long parallelWordId, String parallelWord)
            throws PreferencesException {
      return getPreferencesDataAccessManager().isParallelWordExist(modelId, parallelWordId, parallelWord);
   }

   public List<Long> getInstanceProfileIdsForInstance (Long instanceId) throws PreferencesException {
      return getPreferencesDataAccessManager().getInstanceProfileIdsForInstance(instanceId);
   }

   public List<KeyWord> getKeyWordsByBedId (Long bedId) throws PreferencesException {
      return getPreferencesDataAccessManager().getKeyWordsByBedId(bedId);
   }

   public List<Long> getConceptProfileIdsForConcept (Long conceptId) throws PreferencesException {
      return getPreferencesDataAccessManager().getConceptProfileIdsForConcept(conceptId);
   }

   public List<KeyWord> getKeyWordsForInstances (Long modelId, Long conceptId) throws PreferencesException {
      return getPreferencesDataAccessManager().getKeyWordsForInstances(modelId, conceptId);
   }

   @Override
   public List<ConceptProfile> getConceptProfilesByIds (List<Long> conceptProfileIds) throws PreferencesException {
      return getPreferencesDataAccessManager().getConceptProfilesByIds(conceptProfileIds);
   }

   @Override
   public List<InstanceProfile> getInstanceProfilesByIds (List<Long> instanceProfileIds) throws PreferencesException {
      return getPreferencesDataAccessManager().getInstanceProfilesByIds(instanceProfileIds);
   }

   public IPreferencesDataAccessManager getPreferencesDataAccessManager () {
      return preferencesDataAccessManager;
   }

   public void setPreferencesDataAccessManager (IPreferencesDataAccessManager preferencesDataAccessManager) {
      this.preferencesDataAccessManager = preferencesDataAccessManager;
   }

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

}
