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


package com.execue.swi.dataaccess.impl;

import java.util.List;

import org.apache.log4j.Logger;

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
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.ProfileType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.dataaccess.IPreferencesDataAccessManager;
import com.execue.swi.dataaccess.PreferencesDAOComponents;
import com.execue.swi.exception.PreferencesException;
import com.execue.swi.exception.SWIExceptionCodes;

// TODO : -VG- In the class, we need to check whether the user is admin, if yes call the method of DAO which are
// independent of userId
public class PreferencesDataAccessManagerImpl extends PreferencesDAOComponents implements IPreferencesDataAccessManager {

   private static final Logger log = Logger.getLogger(PreferencesDataAccessManagerImpl.class);

   public BusinessEntityDefinition createProfile (Profile profile, Type type, ModelGroup primaryModelGroup,
            Long userId, Long knowledgeId) throws PreferencesException {
      try {
         User user = getProfileDAO().getById(userId, User.class);
         profile.setModelGroup(primaryModelGroup);
         profile.setUser(user);
         BusinessEntityDefinition businessEntityDefinition = new BusinessEntityDefinition();
         businessEntityDefinition.setType(type);
         businessEntityDefinition.setModelGroup(primaryModelGroup);
         if (ProfileType.CONCEPT.equals(profile.getType())) {
            ConceptProfile conceptProfile = (ConceptProfile) profile;
            businessEntityDefinition.setConceptProfile(conceptProfile);
            businessEntityDefinition.setEntityType(BusinessEntityType.CONCEPT_PROFILE);
         } else if (ProfileType.CONCEPT_LOOKUP_INSTANCE.equals(profile.getType())) {
            InstanceProfile instanceProfile = (InstanceProfile) profile;
            businessEntityDefinition.setInstanceProfile(instanceProfile);
            businessEntityDefinition.setEntityType(BusinessEntityType.INSTANCE_PROFILE);
         }
         assignKnowledgeId(businessEntityDefinition, knowledgeId);
         BusinessEntityDefinition profileBed = getProfileDAO().create(businessEntityDefinition);
         return profileBed;
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, dataAccessException);
      }
   }

   public BusinessEntityDefinition updateProfile (Profile profile, ModelGroup primaryModelGroup)
            throws PreferencesException {
      BusinessEntityDefinition businessEntityDefinition = null;
      try {
         profile.setModelGroup(primaryModelGroup);
         if (ProfileType.CONCEPT.equals(profile.getType())) {
            ConceptProfile conceptProfile = (ConceptProfile) profile;
            getProfileDAO().update(conceptProfile);
            businessEntityDefinition = getBusinessEntityDefinitionForConceptProfile(conceptProfile);
         } else if (ProfileType.CONCEPT_LOOKUP_INSTANCE.equals(profile.getType())) {
            InstanceProfile instanceProfile = (InstanceProfile) profile;
            getProfileDAO().update(instanceProfile);
            businessEntityDefinition = getBusinessEntityDefinitionForInstanceProfile(instanceProfile);
         }
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, dataAccessException);
      }
      return businessEntityDefinition;
   }

   public void deleteProfile (Profile profile, Long modelId, BusinessEntityDefinition businessEntityDefinition,
            ProfileType profileType) throws PreferencesException {
      try {
         if (businessEntityDefinition != null) {
            getProfileDAO().delete(businessEntityDefinition);
            if (ProfileType.CONCEPT.equals(profile.getType())) {
               getProfileDAO().delete((ConceptProfile) profile);
            } else if (ProfileType.CONCEPT_LOOKUP_INSTANCE.equals(profile.getType())) {
               getProfileDAO().delete((InstanceProfile) profile);
            }
         }
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, dataAccessException);
      }
   }

   public void cleanProfile (Profile profile) throws PreferencesException {
      try {
         if (ProfileType.CONCEPT.equals(profile.getType())) {
            ConceptProfile conceptProfile = (ConceptProfile) profile;
            BusinessEntityDefinition businessEntityDefinitionForConceptProfile = getBusinessEntityDefinitionForConceptProfile(conceptProfile);
            if (businessEntityDefinitionForConceptProfile != null) {
               getProfileDAO().delete(businessEntityDefinitionForConceptProfile);
               getProfileDAO().delete(conceptProfile);
            }
         } else if (ProfileType.CONCEPT_LOOKUP_INSTANCE.equals(profile.getType())) {
            InstanceProfile instanceProfile = (InstanceProfile) profile;
            BusinessEntityDefinition businessEntityDefinition = getBusinessEntityDefinitionForInstanceProfile(instanceProfile);
            if (businessEntityDefinition != null) {
               getProfileDAO().delete(businessEntityDefinition);
               getProfileDAO().delete(instanceProfile);
            }
         }
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, dataAccessException);
      }
   }

   public void createRange (Range range, Long userId) throws PreferencesException {
      try {
         User user = getProfileDAO().getById(userId, User.class);
         range.setUser(user);
         addRangeToRangeDetails(range);
         getProfileDAO().create(range);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, dataAccessException);
      }
   }

   public void updateRange (Range range, Long userId) throws PreferencesException {
      try {
         User user = getProfileDAO().getById(userId, User.class);
         range.setUser(user);
         addRangeToRangeDetails(range);
         getProfileDAO().merge(range);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, dataAccessException);
      }
   }

   public List<ConceptProfile> getConceptProfiles (Long modelId, Long userId) throws PreferencesException {
      try {
         return getProfileDAO().getConceptProfiles(modelId, userId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Concept> getMappedConceptsForConceptProfiles (Long modelId) throws PreferencesException {
      try {
         return getProfileDAO().getMappedConceptsForConceptProfiles(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Concept> getConceptsForConceptProfiles (Long modelId) throws PreferencesException {
      try {
         return getProfileDAO().getConceptsForConceptProfiles(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Concept> getMappedConceptsForInstanceProfiles (Long modelId) throws PreferencesException {
      try {
         return getProfileDAO().getMappedConceptsForInstanceProfiles(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Concept> getConceptsForInstanceProfiles (Long modelId) throws PreferencesException {
      try {
         return getProfileDAO().getConceptsForInstanceProfiles(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Concept> getConceptsForRanges (Long modelId) throws PreferencesException {
      try {
         return getProfileDAO().getMappedConceptsForConceptProfiles(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Range> getRanges (Long modelId) throws PreferencesException {
      try {
         return getRangeDAO().getRanges(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Range> getExistingRangesForConcept (Long conceptBedId, Long userId) throws PreferencesException {
      try {
         return getRangeDAO().getExistingRangesForConcept(conceptBedId, userId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<InstanceProfile> getInstanceProfiles (Long modelId, Long userId) throws PreferencesException {
      try {
         return getProfileDAO().getInstanceProfiles(modelId, userId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public Profile getProfile (Long profileId, ProfileType profileType) throws PreferencesException {
      Profile profile = null;
      try {
         if (ProfileType.CONCEPT.equals(profileType)) {
            profile = getProfileDAO().getById(profileId, ConceptProfile.class);
         } else if (ProfileType.CONCEPT_LOOKUP_INSTANCE.equals(profileType)) {
            profile = getProfileDAO().getById(profileId, InstanceProfile.class);
         }
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
      return profile;
   }

   public Range getRange (Long rangeId) throws PreferencesException {
      try {
         return getProfileDAO().getById(rangeId, Range.class);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   private void addRangeToRangeDetails (Range range) {
      for (RangeDetail rangeDetail : range.getRangeDetails()) {
         rangeDetail.setRange(range);
      }
   }

   public Profile getProfile (Long modelId, String profileName, ProfileType profileType) throws PreferencesException {
      Profile profile = null;
      try {
         if (ProfileType.CONCEPT.equals(profileType)) {
            profile = getProfileDAO().getConceptProfile(modelId, profileName);
         } else if (ProfileType.CONCEPT_LOOKUP_INSTANCE.equals(profileType)) {
            profile = getProfileDAO().getInstanceProfile(modelId, profileName);
         }
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
      return profile;
   }

   public List<ParallelWord> getParallelWordsForKeyWord (Long keyWordId) throws PreferencesException {
      try {
         return getParallelWordDAO().getParallelWordsForKeyWord(keyWordId);
      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public KeyWord getKeyWord (Long keyWordId) throws PreferencesException {
      try {
         return getParallelWordDAO().getById(keyWordId, KeyWord.class);
      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public ParallelWord getParallelWord (Long parallelWordId) throws PreferencesException {
      try {
         return getParallelWordDAO().getById(parallelWordId, ParallelWord.class);
      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public void createKeyWord (KeyWord keyWord) throws PreferencesException {
      try {
         getParallelWordDAO().create(keyWord);
      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public ParallelWord createParallelWord (ParallelWord parallelWord) throws PreferencesException {
      try {
         log.debug("creating parallel word " + parallelWord.getParallelWord());
         log.debug("parallelWord user" + parallelWord.getUser());
         log.debug("parallelWord Keyword" + parallelWord.getKeyWord().getWord());
         parallelWord.setPrefixSpace(true);
         parallelWord.setSuffixSpace(true);
         getParallelWordDAO().create(parallelWord);
         log.debug("created parallel word " + parallelWord.getParallelWord());
         return parallelWord;
      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public void createRIParallelWords (List<RIParallelWord> riParallelWords) throws PreferencesException {
      try {
         getParallelWordDAO().createAll(riParallelWords);
      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public void deleteRIParallelWords (List<RIParallelWord> riParallelWords) throws PreferencesException {
      try {
         for (RIParallelWord riParallelWord : riParallelWords) {
            getParallelWordDAO().delete(riParallelWord);
         }
      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public void createParallelWords (List<ParallelWord> parallelWords) throws PreferencesException {
      try {
         getParallelWordDAO().createAll(parallelWords);
      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public void deleteParallelWord (ParallelWord parallelWord) throws PreferencesException {
      try {
         getParallelWordDAO().delete(parallelWord);
      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public void deleteKeyWord (KeyWord keyWord) throws PreferencesException {
      try {
         getParallelWordDAO().delete(keyWord);
      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public ParallelWord updateParallelWord (ParallelWord parallelWord) throws PreferencesException {
      try {
         getParallelWordDAO().update(parallelWord);
         return parallelWord;

      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public void updateParallelWords (List<ParallelWord> parallelWords) throws PreferencesException {
      try {
         getParallelWordDAO().updateAll(parallelWords);
      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public BusinessEntityDefinition getBusinessEntityDefinitionForConceptProfile (ConceptProfile conceptProfile)
            throws PreferencesException {
      try {
         return getProfileDAO().getBusinessEntityDefinitionForConceptProfile(conceptProfile);
      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public BusinessEntityDefinition getBusinessEntityDefinitionForInstanceProfile (InstanceProfile instanceProfile)
            throws PreferencesException {
      try {
         return getProfileDAO().getBusinessEntityDefinitionForInstanceProfile(instanceProfile);
      } catch (DataAccessException e) {
         throw new PreferencesException(e.code, e.getMessage(), e);
      }
   }

   public boolean doesKeyWordExists (Long modelId, String keyWordName) throws PreferencesException {
      try {
         return getKeywordDAO().doesKeyWordExist(modelId, keyWordName);
      } catch (DataAccessException e) {
         throw new PreferencesException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<KeyWord> getAllKeyWords (Long modelId) throws PreferencesException {
      try {
         return getKeywordDAO().getAllKeyWords(modelId);
      } catch (DataAccessException e) {
         throw new PreferencesException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public KeyWord getKeyWord (String wordName, Long modelId) throws PreferencesException {
      try {
         return getKeywordDAO().getKeyWord(wordName, modelId);
      } catch (DataAccessException e) {
         throw new PreferencesException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<ParallelWord> getParallelWordsForUser (Long keyWordId, Long userId) throws PreferencesException {
      try {
         getParallelWordDAO().getParallelWordsForUser(keyWordId, userId);
      } catch (DataAccessException e) {
         throw new PreferencesException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return null;
   }

   public List<RIParallelWord> getRIParallelWordsForKeyWord (Long keyWordId) throws PreferencesException {
      try {
         return getParallelWordDAO().getRIParallelWordsForKeyWord(keyWordId);
      } catch (DataAccessException e) {
         throw new PreferencesException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public ParallelWord getParallelWordByName (String wordName, Long keyWordId, Long userId) throws PreferencesException {
      try {
         return getParallelWordDAO().getParallelWordByName(wordName, keyWordId, userId);
      } catch (DataAccessException e) {
         throw new PreferencesException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<RIOntoTerm> getRIontoTermsForKeyWord (String keyWord) throws PreferencesException {
      try {
         return getKeywordDAO().getRIontoTermsForKeyWord(keyWord);
      } catch (DataAccessException e) {
         throw new PreferencesException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Profile> getConceptProfiles (List<ModelGroup> userModelGroups) throws PreferencesException {
      try {
         return getProfileDAO().getConceptProfiles(userModelGroups);
      } catch (DataAccessException dae) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
   }

   public List<Profile> getInstanceProfiles (List<ModelGroup> userModelGroups) throws PreferencesException {
      try {
         return getProfileDAO().getInstanceProfiles(userModelGroups);
      } catch (DataAccessException dae) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
   }

   private void assignKnowledgeId (BusinessEntityDefinition businessEntityDefinition, Long knowledgeId)
            throws PreferencesException {
      if (businessEntityDefinition.getKnowledgeId() == null) {
         businessEntityDefinition.setKnowledgeId(knowledgeId);
      }
   }

   public List<InstanceProfile> getInstanceProfilesForConcept (Long modelId, Long conceptId)
            throws PreferencesException {
      try {
         return getProfileDAO().getInstanceProfilesForConcept(modelId, conceptId);
      } catch (DataAccessException dae) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
   }

   public List<Concept> getMappedConceptsForHybridProfiles (long modelId) throws PreferencesException {
      try {
         return getProfileDAO().getMappedConceptsForHybridProfiles(modelId);
      } catch (DataAccessException dae) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
   }

   public List<Concept> getConceptsForHybridProfiles (long modelId) throws PreferencesException {
      try {
         return getProfileDAO().getConceptsForHybridProfiles(modelId);
      } catch (DataAccessException dae) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
   }

   public List<Range> getUserDefinedRangeForConceptForDynamicRangeEvaluation (Long conceptBedId, long userId)
            throws PreferencesException {
      try {
         return getRangeDAO().getUserDefinedRangeForConceptForDynamicRangeEvaluation(conceptBedId, userId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Concept> getConceptsForConceptRanges (Long modelId) throws PreferencesException {
      try {
         return getRangeDAO().getConceptsForConceptRanges(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public void deleteConceptProfile (ConceptProfile conceptProfile) throws PreferencesException {
      try {
         getProfileDAO().delete(conceptProfile);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_DELETE_FAILED, dataAccessException);
      }
   }

   public void deleteInstanceProfile (InstanceProfile instanceProfile) throws PreferencesException {
      try {
         getProfileDAO().delete(instanceProfile);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_DELETE_FAILED, dataAccessException);
      }
   }

   public void deleteRange (Long rangeId) throws PreferencesException {
      try {
         Range range = getProfileDAO().getById(rangeId, Range.class);
         if (range != null) {
            getProfileDAO().delete(range);
         }
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_DELETE_FAILED, dataAccessException);
      }
   }

   public List<KeyWord> getKeyWordsByBedId (Long bedId) throws PreferencesException {
      try {
         return getKeywordDAO().getKeyWordsByBedId(bedId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_DELETE_FAILED, dataAccessException);
      }
   }

   public boolean isParallelWordExist (Long modelId, Long parallelWordId, String parallelWord)
            throws PreferencesException {
      boolean isParallelWordExist = false;
      try {
         ParallelWord pWord = getParallelWordDAO().getParallelWordByNameForModel(modelId, parallelWord);
         if (pWord != null) {
            if (parallelWordId != null && pWord.getId().longValue() == parallelWordId.longValue()) {
               isParallelWordExist = false;
            } else {
               isParallelWordExist = true;
            }
         }
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
      return isParallelWordExist;
   }

   public List<Long> getInstanceProfileIdsForInstance (Long instanceId) throws PreferencesException {
      try {
         return getProfileDAO().getInstanceProfileIdsForInstance(instanceId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Long> getConceptProfileIdsForConcept (Long conceptId) throws PreferencesException {
      try {
         return getProfileDAO().getConceptProfileIdsForConcept(conceptId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<KeyWord> getKeyWordsForInstances (Long modelId, Long conceptId) throws PreferencesException {
      try {
         return getKeywordDAO().getKeyWordsForInstances(modelId, conceptId);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   @Override
   public List<ConceptProfile> getConceptProfilesByIds (List<Long> conceptProfileIds) throws PreferencesException {
      try {
         return getProfileDAO().getConceptProfilesByIds(conceptProfileIds);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   @Override
   public List<InstanceProfile> getInstanceProfilesByIds (List<Long> instanceProfileIds) throws PreferencesException {
      try {
         return getProfileDAO().getInstanceProfilesByIds(instanceProfileIds);
      } catch (DataAccessException dataAccessException) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

}
