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


package com.execue.swi.dataaccess;

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
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.type.ProfileType;
import com.execue.swi.exception.PreferencesException;

public interface IPreferencesDataAccessManager {

   public List<Range> getExistingRangesForConcept (Long conceptBedId, Long userId) throws PreferencesException;

   public List<Range> getRanges (Long modelId) throws PreferencesException;

   public List<ConceptProfile> getConceptProfiles (Long modelId, Long userId) throws PreferencesException;

   public List<InstanceProfile> getInstanceProfiles (Long modelId, Long userId) throws PreferencesException;

   public List<Concept> getMappedConceptsForConceptProfiles (Long modelId) throws PreferencesException;

   public List<Concept> getConceptsForConceptProfiles (Long modelId) throws PreferencesException;

   public List<Concept> getMappedConceptsForInstanceProfiles (Long modelId) throws PreferencesException;

   public List<Concept> getConceptsForInstanceProfiles (Long modelId) throws PreferencesException;

   public List<Long> getInstanceProfileIdsForInstance (Long instanceId) throws PreferencesException;

   public List<Concept> getConceptsForRanges (Long modelId) throws PreferencesException;

   public BusinessEntityDefinition createProfile (Profile profile, Type type, ModelGroup primaryModelGroup,
            Long userId, Long knowledgeId) throws PreferencesException;

   public BusinessEntityDefinition updateProfile (Profile profile, ModelGroup primaryModelGroup)
            throws PreferencesException;

   public void deleteProfile (Profile profile, Long modelId, BusinessEntityDefinition businessEntityDefinition,
            ProfileType profileType) throws PreferencesException;

   public BusinessEntityDefinition getBusinessEntityDefinitionForConceptProfile (ConceptProfile conceptProfile)
            throws PreferencesException;

   public BusinessEntityDefinition getBusinessEntityDefinitionForInstanceProfile (InstanceProfile instanceProfile)
            throws PreferencesException;

   public void createRange (Range range, Long userId) throws PreferencesException;

   public void updateRange (Range range, Long userId) throws PreferencesException;

   public Profile getProfile (Long profileId, ProfileType profileType) throws PreferencesException;

   public Range getRange (Long rangeId) throws PreferencesException;

   public Profile getProfile (Long modelId, String profileName, ProfileType profileType) throws PreferencesException;

   public List<ParallelWord> getParallelWordsForKeyWord (Long keyWordId) throws PreferencesException;

   public List<ParallelWord> getParallelWordsForUser (Long keyWordId, Long userId) throws PreferencesException;

   public ParallelWord getParallelWordByName (String wordName, Long keyWordId, Long userId) throws PreferencesException;

   public void createParallelWords (List<ParallelWord> parallelWords) throws PreferencesException;

   public void createRIParallelWords (List<RIParallelWord> riParallelWords) throws PreferencesException;

   public void deleteRIParallelWords (List<RIParallelWord> riParallelWords) throws PreferencesException;

   public ParallelWord createParallelWord (ParallelWord parallelWord) throws PreferencesException;

   public void deleteParallelWord (ParallelWord parallelWord) throws PreferencesException;

   public void deleteKeyWord (KeyWord keyWord) throws PreferencesException;

   public void updateParallelWords (List<ParallelWord> parallelWords) throws PreferencesException;

   public ParallelWord updateParallelWord (ParallelWord parallelWord) throws PreferencesException;

   public void createKeyWord (KeyWord keyWord) throws PreferencesException;

   public List<KeyWord> getAllKeyWords (Long modelId) throws PreferencesException;

   public boolean doesKeyWordExists (Long modelId, String keyWordname) throws PreferencesException;

   public KeyWord getKeyWord (Long keyWordId) throws PreferencesException;

   public ParallelWord getParallelWord (Long parallelWordId) throws PreferencesException;

   public KeyWord getKeyWord (String wordName, Long modelId) throws PreferencesException;

   public List<RIParallelWord> getRIParallelWordsForKeyWord (Long keyWordId) throws PreferencesException;

   // TODO: singleSWI Merge : change domain to model Id

   public List<RIOntoTerm> getRIontoTermsForKeyWord (String keyWord) throws PreferencesException;

   public void cleanProfile (Profile profile) throws PreferencesException;

   public List<Profile> getConceptProfiles (List<ModelGroup> userModelGroups) throws PreferencesException;

   public List<Profile> getInstanceProfiles (List<ModelGroup> userModelGroups) throws PreferencesException;

   public List<InstanceProfile> getInstanceProfilesForConcept (Long modelId, Long conceptId)
            throws PreferencesException;

   public List<Concept> getMappedConceptsForHybridProfiles (long modelId) throws PreferencesException;

   public List<Concept> getConceptsForHybridProfiles (long modelId) throws PreferencesException;

   public List<Range> getUserDefinedRangeForConceptForDynamicRangeEvaluation (Long conceptBedId, long userId)
            throws PreferencesException;

   public List<Concept> getConceptsForConceptRanges (Long modelId) throws PreferencesException;

   public void deleteRange (Long rangeId) throws PreferencesException;

   public List<KeyWord> getKeyWordsByBedId (Long bedId) throws PreferencesException;

   public void deleteInstanceProfile (InstanceProfile instanceProfile) throws PreferencesException;

   public void deleteConceptProfile (ConceptProfile conceptProfile) throws PreferencesException;

   public boolean isParallelWordExist (Long modelId, Long keyWordId, String parallelWord) throws PreferencesException;

   public List<Long> getConceptProfileIdsForConcept (Long conceptId) throws PreferencesException;

   public List<KeyWord> getKeyWordsForInstances (Long modelId, Long conceptId) throws PreferencesException;

   public List<ConceptProfile> getConceptProfilesByIds (List<Long> conceptProfileIds) throws PreferencesException;

   public List<InstanceProfile> getInstanceProfilesByIds (List<Long> instanceProfileIds) throws PreferencesException;
}
