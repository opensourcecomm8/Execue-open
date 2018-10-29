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


package com.execue.swi.service;

import java.util.List;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.ParallelWord;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.ProfileType;
import com.execue.swi.exception.PreferencesException;

public interface IPreferencesRetrievalService {

   // *************************** Methods related to Range ************************************************

   public Range getRange (Long rangeId) throws PreferencesException;

   public List<Range> getExistingRangesForConcept (Long conceptBedId, Long userId) throws PreferencesException;

   public List<Long> getInstanceProfileIdsForInstance (Long instanceId) throws PreferencesException;

   public List<Range> getRanges (Long modelId) throws PreferencesException;

   public List<Range> getUserDefinedRangeForConceptForDynamicRangeEvaluation (Long conceptBedId)
            throws PreferencesException;

   // *************************** Methods related to Profiles ************************************************

   public List<ConceptProfile> getConceptProfiles (Long modelId, Long userId) throws PreferencesException;

   public List<InstanceProfile> getInstanceProfiles (Long modelId, Long userId) throws PreferencesException;

   public List<InstanceProfile> getInstanceProfilesForConcept (Long modelId, Long conceptId)
            throws PreferencesException;

   public Profile getProfile (Long profileId, ProfileType profileType) throws PreferencesException;

   public Profile getProfile (Long modelId, String profileName, ProfileType profileType) throws PreferencesException;

   public List<Profile> getConceptProfiles (List<ModelGroup> userModelGroups) throws PreferencesException;

   public List<Profile> getInstanceProfiles (List<ModelGroup> userModelGroups) throws PreferencesException;

   // *************************** Methods related to Concept ************************************************

   public List<Concept> getConceptsForConceptProfiles (Long modelId) throws PreferencesException;

   public List<Concept> getConceptsForInstanceProfiles (Long modelId) throws PreferencesException;

   public List<Concept> getConceptsForRanges (Long modelId) throws PreferencesException;

   /**
    * Get the concepts eligible for Hybrid Profile, i.e. all the concepts of the model which are at the least mapped
    * once if the application is of any kind of structured source, else get all irrespective of mapping state.
    * 
    * @param modelId
    * @return
    * @throws PreferencesException
    */
   public List<Concept> getConceptsForHybridProfiles (long modelId) throws PreferencesException;

   // *************************** Methods related to BED'S ************************************************

   public BusinessEntityDefinition getBusinessEntityDefinitionForConceptProfile (ConceptProfile conceptProfile)
            throws PreferencesException;

   public BusinessEntityDefinition getBusinessEntityDefinitionForInstanceProfile (InstanceProfile instanceProfile)
            throws PreferencesException;

   // *************************** Methods related to ParallelWord's,Keyword's,RIOntoterm **************************

   public List<RIParallelWord> getRIParallelWordsForKeyWord (Long keyWordId) throws PreferencesException;

   public List<ParallelWord> getParallelWordsForKeyWord (Long keyWordId) throws PreferencesException;

   public ParallelWord getParallelWord (Long parallelWordId) throws PreferencesException;

   public KeyWord getKeyWord (Long keyWordId) throws PreferencesException;

   public KeyWord getKeyWord (String wordName, Long userId, Long modelId) throws PreferencesException;

   public KeyWord getKeyWord (String wordName, Long modelId) throws PreferencesException;

   public List<KeyWord> getAllKeyWords (Long modelId) throws PreferencesException;

   public List<ParallelWord> getParallelWordsForUser (User user, KeyWord keyword) throws PreferencesException;

   public ParallelWord getParallelWordByName (String wordName, Long keyWordId, Long userId) throws PreferencesException;

   public List<RIOntoTerm> getRIontoTermsForKeyWord (String keyWord) throws PreferencesException;

   public boolean doesKeyWordExist (Long modelId, String keyWordname) throws PreferencesException;

   public boolean isParallelWordExist (Long modelId, Long keyWordId, String parallelWord) throws PreferencesException;

   // *************************** Methods related to User and Roles **************************

   public List<KeyWord> getKeyWordsByBedId (Long bedId) throws PreferencesException;

   public List<Long> getConceptProfileIdsForConcept (Long conceptId) throws PreferencesException;

   public List<KeyWord> getKeyWordsForInstances (Long modelId, Long conceptId) throws PreferencesException;

   public List<ConceptProfile> getConceptProfilesByIds (List<Long> conceptProfileIds) throws PreferencesException;

   public List<InstanceProfile> getInstanceProfilesByIds (List<Long> instanceProfileIds) throws PreferencesException;
}
