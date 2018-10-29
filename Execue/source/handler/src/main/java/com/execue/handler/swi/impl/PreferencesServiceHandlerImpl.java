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


package com.execue.handler.swi.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ParallelWord;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.bean.entity.comparator.BusinessEntityInfoDisplayNameComparator;
import com.execue.core.common.bean.entity.comparator.ConceptDisplaNameComparator;
import com.execue.core.common.bean.entity.comparator.InstanceDisplaNameComparator;
import com.execue.core.common.bean.governor.BusinessEntityInfo;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.BusinessEntityTermType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.type.ParallelWordType;
import com.execue.core.common.type.ProfileType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.handler.bean.UIInvalidParallelWord;
import com.execue.handler.bean.UIParallelWord;
import com.execue.handler.bean.UIParallelWordValidationResult;
import com.execue.handler.bean.UIRangeDetail;
import com.execue.handler.swi.IPreferencesServiceHandler;
import com.execue.handler.transformer.RangeDetailTransformer;
import com.execue.platform.helper.RangeSuggestionServiceHelper;
import com.execue.platform.swi.IBusinessEntityDeletionWrapperService;
import com.execue.platform.swi.IKDXMaintenanceService;
import com.execue.platform.swi.IRangeSuggestionService;
import com.execue.platform.swi.ISWIPlatformManagementService;
import com.execue.security.UserContextService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PreferencesException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPreferencesDeletionService;
import com.execue.swi.service.IPreferencesManagementService;
import com.execue.swi.service.IPreferencesRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.swi.service.IUserManagementService;

public class PreferencesServiceHandlerImpl extends UserContextService implements IPreferencesServiceHandler {

   private IUserManagementService                userManagementService;
   private IKDXMaintenanceService                kdxMaintenanceService;
   private IKDXRetrievalService                  kdxRetrievalService;
   private IBaseKDXRetrievalService              baseKDXRetrievalService;
   private IBusinessEntityMaintenanceService     businessEntityMaintenanceService;
   private IPreferencesRetrievalService          preferencesRetrievalService;
   private IPreferencesManagementService         preferencesManagementService;
   private IPreferencesDeletionService           preferencesDeletionService;
   private IBusinessEntityDeletionWrapperService businessEntityDeletionWrapperService;
   private ISDXRetrievalService                  sdxRetrievalService;
   private ISWIPlatformManagementService         swiPlatformManagementService;
   private IRangeSuggestionService               rangeSuggestionService;

   private static final Logger                   log = Logger.getLogger(PreferencesServiceHandlerImpl.class);

   // TODO: -JVK- verify the display name of the concept
   public List<Concept> getBaseProfiles (Long modelId) throws ExeCueException {
      Concept concept = new Concept();
      concept.setDisplayName("Measure Profile");
      Concept hybridConcept = new Concept();
      hybridConcept.setDisplayName("Hybrid Profile");
      List<Concept> instanceProfiles = getPreferencesRetrievalService().getConceptsForInstanceProfiles(modelId);
      instanceProfiles.add(0, concept);
      instanceProfiles.add(1, hybridConcept);
      return instanceProfiles;
   }

   public List<ConceptProfile> getConceptProfiles (Long modelId) throws ExeCueException {
      return getPreferencesRetrievalService().getConceptProfiles(modelId, getUserId());
   }

   public List<Concept> getConceptsForConceptProfiles (Long modelId, CheckType hybridProfile) throws ExeCueException {
      List<Concept> concepts = null;
      if (CheckType.YES.equals(hybridProfile)) {
         concepts = getPreferencesRetrievalService().getConceptsForHybridProfiles(modelId);
      } else {
         concepts = getPreferencesRetrievalService().getConceptsForConceptProfiles(modelId);
      }
      if (ExecueCoreUtil.isCollectionEmpty(concepts)) {
         concepts = new ArrayList<Concept>();
      }
      Collections.sort(concepts, new ConceptDisplaNameComparator());
      return concepts;
   }

   public CheckType isConceptProfileHybridProfileType (Long modelId, ConceptProfile conceptProfile)
            throws ExeCueException {
      CheckType hybridProfileType = CheckType.NO;
      BusinessEntityDefinition profileBED = getPreferencesRetrievalService()
               .getBusinessEntityDefinitionForConceptProfile(conceptProfile);
      Type type = getKdxRetrievalService().getTypeByName(ExecueConstants.ONTO_ENTITY_TYPE);
      if (type.getId().equals(profileBED.getType().getId())) {
         hybridProfileType = CheckType.YES;
      }
      return hybridProfileType;
   }

   public List<Concept> getConceptsForInstanceProfiles (Long modelId) throws ExeCueException {
      List<Concept> concepts = getPreferencesRetrievalService().getConceptsForInstanceProfiles(modelId);
      Collections.sort(concepts, new ConceptDisplaNameComparator());
      return concepts;
   }

   public List<InstanceProfile> getInstanceProfiles (Long modelId) throws ExeCueException {
      return getPreferencesRetrievalService().getInstanceProfiles(modelId, getUserId());
   }

   @Override
   public void createProfile (Long modelId, Profile profile, CheckType hybridProfile) throws ExeCueException {
      Type type = null;
      if (ProfileType.CONCEPT.equals(profile.getType())) {
         // For concept profiles, we will set the type to measurable entity
         if (CheckType.YES.equals(hybridProfile)) {
            type = getKdxRetrievalService().getTypeByName(ExecueConstants.ONTO_ENTITY_TYPE);
         } else {
            type = getKdxRetrievalService().getTypeByName(ExecueConstants.MEASURES_PROFILE_TYPE);
         }

      } else if (ProfileType.CONCEPT_LOOKUP_INSTANCE.equals(profile.getType())) {
         BusinessEntityDefinition conceptBed = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                  ((InstanceProfile) profile).getConcept().getId(), null);
         type = conceptBed.getType();
      }
      getSwiPlatformManagementService().createProfile(profile, type, modelId, getUserId(), hybridProfile);
   }

   @Override
   public void updateProfile (Long modelId, Profile profile) throws ExeCueException {
      User user = getUserContext().getUser();
      profile.setUser(user);
      getPreferencesManagementService().updateProfile(profile, modelId);
   }

   @Override
   public void deleteProfiles (Long modelId, List<Profile> profiles) throws ExeCueException {
      getBusinessEntityDeletionWrapperService().deleteProfiles(modelId, profiles);
   }

   public Profile getProfile (Long profileId, ProfileType profileType) throws ExeCueException {
      return getPreferencesRetrievalService().getProfile(profileId, profileType);
   }

   public List<Instance> getInstancesForConcept (Long modelId, Long conceptId) throws ExeCueException {
      List<Instance> instances = getKdxRetrievalService().getInstances(modelId, conceptId);
      Collections.sort(instances, new InstanceDisplaNameComparator());
      return instances;
   }

   public Concept getConceptById (Long conceptId) throws ExeCueException {
      return getKdxRetrievalService().getConceptById(conceptId);
   }

   public Model getModelById (Long modelId) throws ExeCueException {
      return getKdxRetrievalService().getModelById(modelId);
   }

   public void createRange (Long modelId, Range range) throws ExeCueException {
      getPreferencesManagementService().createRange(modelId, getUserId(), range);
   }

   public void updateRange (Long modelId, Range range) throws ExeCueException {
      getPreferencesManagementService().updateRange(modelId, getUserId(), range);
   }

   public void deleteRanges (List<Long> rangeIds) throws ExeCueException {
      for (Long rangeId : rangeIds) {
         getBusinessEntityDeletionWrapperService().deleteRangeHeirarchy(rangeId);
      }

   }

   public List<Concept> getConceptsForRanges (Long modelId) throws ExeCueException {
      return getPreferencesRetrievalService().getConceptsForRanges(modelId);
   }

   public Range getExistingRangeForConcept (Long modelId, Long conceptId) throws ExeCueException {
      List<Range> rangeList = getPreferencesRetrievalService().getExistingRangesForConcept(conceptId, getUserId());

      Range range = null;
      for (int i = 0; i < rangeList.size(); i++) {
         range = rangeList.get(i);
         if (range.isEnabled()) {
            return range;
         }
      }
      if (range != null) {
         return range;
      }
      // return getPreferencesService().getSuggestedRangeForConcept(conceptId);
      return null;
   }

   public Range getSuggestedRangesForConceptByApplication (Long applicationId, Long modelId, Long conceptBedId)
            throws ExeCueException {
      Range range = null;
      // get all the parent assets and take zeroth in the list
      List<Asset> parentAssets = getSdxRetrievalService().getAllParentAssets(applicationId);
      if (ExecueCoreUtil.isCollectionNotEmpty(parentAssets)) {
         range = getRangeSuggestionService().deduceRange(
                  RangeSuggestionServiceHelper.populateDynamicRangeInput(modelId, parentAssets.get(0).getId(),
                           conceptBedId));
      }
      return range;
   }

   public Range getRangeById (Long rangeId) throws ExeCueException {
      return getPreferencesRetrievalService().getRange(rangeId);
   }

   public List<Range> getRanges (Long modelId) throws ExeCueException {
      return getPreferencesRetrievalService().getRanges(modelId);
   }

   public List<UIRangeDetail> transformRangeDetails (Set<RangeDetail> domainRangeDetails) {
      // TODO: -RG- has to be taken as a wired bean
      RangeDetailTransformer rangeDetailTransformer = new RangeDetailTransformer();
      List<UIRangeDetail> uiRangeDetails = new ArrayList<UIRangeDetail>();
      UIRangeDetail uiRangeDetail = null;
      for (RangeDetail rangeDetail : domainRangeDetails) {
         uiRangeDetail = new UIRangeDetail();
         rangeDetailTransformer.transformToUIObject(rangeDetail, uiRangeDetail);
         uiRangeDetails.add(uiRangeDetail);
      }
      return uiRangeDetails;
   }

   public Set<RangeDetail> transformRangeDetails (List<UIRangeDetail> uiRangeDetails) {
      // TODO: -RG- has to be taken as a wired bean
      RangeDetailTransformer rangeDetailTransformer = new RangeDetailTransformer();
      Set<RangeDetail> domainRangeDetails = new HashSet<RangeDetail>();
      RangeDetail domainRangeDetail = null;
      for (UIRangeDetail rangeDetail : uiRangeDetails) {
         domainRangeDetail = new RangeDetail();
         rangeDetailTransformer.transformToDomainObject(rangeDetail, domainRangeDetail);
         domainRangeDetails.add(domainRangeDetail);
      }
      return domainRangeDetails;
   }

   public List<ParallelWord> transformParallelWords (List<UIParallelWord> uiParallelWords) {
      // TODO: -RG- has to be taken as a wired bean
      List<ParallelWord> parallelWords = new ArrayList<ParallelWord>();
      for (UIParallelWord word : uiParallelWords) {
         ParallelWord parallelWord = new ParallelWord();
         parallelWord.setParallelWord(word.getParallelWord());
         parallelWord.setKeyWord(word.getKeyword());
         parallelWord.setPrefixSpace(false);
         parallelWord.setSuffixSpace(false);
         parallelWords.add(parallelWord);
      }
      return parallelWords;
   }

   public List<UIParallelWord> transformUIParallelWords (List<ParallelWord> parallelWords) throws PreferencesException {
      // TODO: -RG- has to be taken as a wired bean
      List<UIParallelWord> uiParallelWords = new ArrayList<UIParallelWord>();
      for (ParallelWord word : parallelWords) {
         UIParallelWord uiParallelWord = new UIParallelWord();
         // check if parallel word belongs to the current user
         if (word.getUser().getId().equals(getUserId())) {
            uiParallelWord.setUsersParallelWord("YES");
         }
         uiParallelWord.setId(word.getId());
         uiParallelWord.setParallelWord(word.getParallelWord());
         uiParallelWord.setKeyword(word.getKeyWord());
         uiParallelWords.add(uiParallelWord);
      }
      return uiParallelWords;
   }

   // TODO: -JVK- check if the caller can provide the modelId or not
   public UIParallelWordValidationResult createUpdateKeywordParallelWords (Long modelId, KeyWord keyWord,
            List<UIParallelWord> parallelWords) throws HandlerException {
      // check if keyword is the exact match with the Business Terms
      log.debug("inside the createUpdateKeyWordParallelWords method");
      boolean isValidKeyWord = false;
      boolean isNewKeyWord = false;
      boolean hasValidParallelWords = true;
      UIParallelWordValidationResult validationResult = new UIParallelWordValidationResult();
      // List<String> invalidKeyWordParallelWordNames = new ArrayList<String>();
      try {
         if (keyWord != null && ExecueCoreUtil.isNotEmpty(keyWord.getWord())) {
            // keyWord.setDomain(getKdxService().getDefaultDomain());
            Long bedId = getKdxRetrievalService().isExactBusinessTerm(modelId, keyWord.getWord());
            if (bedId != null) {
               keyWord.setBusinessEntityDefinition(getKdxRetrievalService().getBusinessEntityDefinitionById(bedId));
               keyWord.setModelGroup(getKdxRetrievalService().getPrimaryGroup(modelId));
               isValidKeyWord = true;
            }
            log.debug("KeywordCheck for exact Business Term  ::" + isValidKeyWord);
            if (!isValidKeyWord) {
               if (getKdxRetrievalService().isPartOfBusinessTerm(modelId, keyWord.getWord())) {
                  keyWord.setModelGroup(getKdxRetrievalService().getPrimaryGroup(modelId));
                  isValidKeyWord = true;
               }
            }
            log.debug("KeywordCheck for Part of Business Term  ::" + isValidKeyWord);
            if (getPreferencesRetrievalService().getKeyWord(keyWord.getWord(), modelId) != null
                     && keyWord.getId() == null && ExecueCoreUtil.isCollectionEmpty(parallelWords)) {
               validationResult.setDuplicateKeyWord(keyWord.getWord());
               return validationResult;
            }
            log.debug("duplicate keyword check ::" + isValidKeyWord);

            if (getPreferencesRetrievalService().getKeyWord(keyWord.getWord(), modelId) != null) {
               keyWord = getPreferencesRetrievalService().getKeyWord(keyWord.getWord(), modelId);
            } else {
               isNewKeyWord = true;
               log.debug("New KeyWord Check ::" + isNewKeyWord);
            }

         }

         if (!isValidKeyWord) {
            // set the Invalid keyword and return
            validationResult.setKeyWord(keyWord.getWord());
            return validationResult;
         }
         if (isValidKeyWord) {
            List<UIInvalidParallelWord> invalidParallelWordsList = parallelWordsValidityCheck(modelId, keyWord.getId(),
                     parallelWords);
            if (ExecueCoreUtil.isCollectionNotEmpty(invalidParallelWordsList)) {
               validationResult.setParallelWords(invalidParallelWordsList);
               hasValidParallelWords = false;
               return validationResult;
            }
            log.debug("hasValid ParallelWords ::" + hasValidParallelWords);
         }
         if (isValidKeyWord || hasValidParallelWords) {
            if (isNewKeyWord) {
               getPreferencesManagementService().createKeyWord(keyWord);
               log.debug("keyWord created ");
            }
            if (!isNewKeyWord || isNewKeyWord && isValidKeyWord) {
               if (ExecueCoreUtil.isCollectionNotEmpty(parallelWords)) {
                  for (UIParallelWord word : parallelWords) {
                     ParallelWord parallelWord = null;
                     ParallelWord newParallelWord = null;
                     if (CheckType.YES.getValue().toString().equals(word.getCheckedState())) {
                        if (word.getId() == null) {
                           parallelWord = new ParallelWord();
                           KeyWord keyword = getPreferencesRetrievalService().getKeyWord(keyWord.getWord(), modelId);
                           parallelWord.setKeyWord(keyword);
                           parallelWord.setUser(getUserContext().getUser());
                           parallelWord.setParallelWord(word.getParallelWord());
                           parallelWord.setPwdType(ParallelWordType.RelatedWord);
                           parallelWord.setQuality(0.85);
                           if (ExecueStringUtil.compactString(keyWord.getWord().toLowerCase()).contains(
                                    parallelWord.getParallelWord().toLowerCase())) {
                              parallelWord.setIsDifferentWord(CheckType.NO);
                           }
                           newParallelWord = getPreferencesManagementService().createParallelWord(parallelWord);
                           log.debug("parallelword created");
                           getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
                                    ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(newParallelWord
                                             .getId(), EntityType.PARALLEL_WORD, modelId, OperationType.ADD, keyWord
                                             .getId()));

                        } else {
                           parallelWord = getPreferencesRetrievalService().getParallelWord(word.getId());
                           parallelWord.setParallelWord(word.getParallelWord());
                           if (ExecueStringUtil.compactString(keyWord.getWord().toLowerCase()).contains(
                                    parallelWord.getParallelWord().toLowerCase())) {
                              parallelWord.setIsDifferentWord(CheckType.NO);
                           }
                           parallelWord.setPwdType(ParallelWordType.RelatedWord);
                           newParallelWord = getPreferencesManagementService().updateParallelWord(parallelWord);
                           log.debug("parallelword updated");
                           getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
                                    ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(newParallelWord
                                             .getId(), EntityType.PARALLEL_WORD, modelId, OperationType.UPDATE, keyWord
                                             .getId()));
                        }
                     } else {
                        if (word.getId() != null) {
                           log.debug("deleting parallel word");
                           parallelWord = getPreferencesRetrievalService().getParallelWord(word.getId());
                           getPreferencesDeletionService().deleteParallelWordHeirarchy(parallelWord.getKeyWord(),
                                    parallelWord);

                        }
                     }

                  }
               }
            }
         }
      } catch (PreferencesException PreferencesException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, PreferencesException);
      } catch (KDXException kdxException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      }
      if (validationResult.getDuplicateKeyWord() == null && validationResult.getKeyWord() == null
               && validationResult.getParallelWords() == null) {
         validationResult = null;
      }
      return validationResult;
   }

   public IUserManagementService getUserManagementService () {
      return userManagementService;
   }

   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   public KeyWord getKeyWord (String wordName, Long modelId) throws HandlerException {
      KeyWord word;
      try {
         word = getPreferencesRetrievalService().getKeyWord(wordName, modelId);
      } catch (PreferencesException PreferencesException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, PreferencesException);
      }
      return word;
   }

   public List<UIParallelWord> getParallelWordsById (Long keyWordId) throws HandlerException {
      List<UIParallelWord> words = null;
      try {
         List<ParallelWord> pWords = getPreferencesRetrievalService().getParallelWordsForKeyWord(keyWordId);
         words = transformUIParallelWords(pWords);
      } catch (PreferencesException PreferencesException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, PreferencesException);
      }
      return words;
   }

   public void deleteKeyWords (List<KeyWord> keywords) throws HandlerException {
      log.debug("-- deleteKeywords of PreferenceServiceHandler --");
      for (KeyWord keyWord : keywords) {
         try {
            getPreferencesDeletionService().deleteKeywordHeirarchy(keyWord);
         } catch (PreferencesException preferencesException) {
            throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, preferencesException);
         }
      }
   }

   public List<BusinessEntityInfo> getAllBusinessTermEntities (Long modelId,
            BusinessEntityTermType businessEntityTermType, Page page) throws HandlerException {
      List<BusinessEntityInfo> businessTerms = new ArrayList<BusinessEntityInfo>();
      try {
         if (BusinessEntityTermType.CONCEPT_PROFILE.equals(businessEntityTermType)) {
            businessTerms.addAll(getKdxRetrievalService()
                     .getConceptProfileBusinessEntitiesByPageForModel(modelId, page));
         } else if (BusinessEntityTermType.INSTANCE_PROFILE.equals(businessEntityTermType)) {
            businessTerms.addAll(getKdxRetrievalService().getInstanceProfileBusinessEntitiesByPageForModel(modelId,
                     page));
         } else if (BusinessEntityTermType.INSTANCE.equals(businessEntityTermType)) {
            businessTerms.addAll(getKdxRetrievalService().getInstanceBusinessEntitiesByPageForModel(modelId, page));
         } else if (BusinessEntityTermType.RELATION.equals(businessEntityTermType)) {
            businessTerms.addAll(getKdxRetrievalService().getRelationBusinessEntitiesByPageForModel(modelId, page));
         } else {
            businessTerms.addAll(getKdxRetrievalService().getConceptBusinessEntitiesByPageForModel(modelId, page));
         }
         Collections.sort(businessTerms, new BusinessEntityInfoDisplayNameComparator());
      } catch (KDXException kdxException) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, kdxException);
      }
      return businessTerms;
   }

   public List<KeyWord> getAllExistingKeywords (Long modelId) throws HandlerException {
      List<KeyWord> keyWords = new ArrayList<KeyWord>();
      try {
         keyWords = getPreferencesRetrievalService().getAllKeyWords(modelId);
      } catch (PreferencesException PreferencesException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, PreferencesException);
      }
      return keyWords;
   }

   private List<UIInvalidParallelWord> parallelWordsValidityCheck (Long modelId, Long keyWordId,
            List<UIParallelWord> parallelWords) throws HandlerException {
      List<UIInvalidParallelWord> invalidParallelWordList = new ArrayList<UIInvalidParallelWord>();
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(parallelWords)) {
            for (UIParallelWord uiParallelWord : parallelWords) {
               if (CheckType.YES.getValue().toString().equals(uiParallelWord.getCheckedState())) {
                  log.debug("parallel word validity check");
                  // check if the parallelWord is valid.
                  if (getBaseKDXRetrievalService().isSystemVariableExist(uiParallelWord.getParallelWord())) {
                     uiParallelWord.setIsInvalidParallelWord("YES");
                     invalidParallelWordList.add(prepareInvalidParallelWord(uiParallelWord.getParallelWord(), true,
                              false, false));
                  } else if (getKdxRetrievalService().isExactBusinessTerm(modelId, uiParallelWord.getParallelWord()) != null) {
                     uiParallelWord.setIsInvalidParallelWord("YES");
                     invalidParallelWordList.add(prepareInvalidParallelWord(uiParallelWord.getParallelWord(), false,
                              true, false));
                  } else if (getPreferencesRetrievalService().isParallelWordExist(modelId, uiParallelWord.getId(),
                           uiParallelWord.getParallelWord())) {
                     uiParallelWord.setIsInvalidParallelWord("YES");
                     invalidParallelWordList.add(prepareInvalidParallelWord(uiParallelWord.getParallelWord(), false,
                              false, true));
                  }
               }
            }
         }

      } catch (KDXException kdxException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      } catch (PreferencesException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return invalidParallelWordList;
   }

   private UIInvalidParallelWord prepareInvalidParallelWord (String parallelWord, boolean isSystemVariable,
            boolean isExactBusinessTerm, boolean isDuplicate) {
      UIInvalidParallelWord invalidParallelWord = new UIInvalidParallelWord();
      invalidParallelWord.setName(parallelWord);
      invalidParallelWord.setSystemVariable(isSystemVariable);
      invalidParallelWord.setExactBusinessTerm(isExactBusinessTerm);
      invalidParallelWord.setDuplicate(isDuplicate);
      return invalidParallelWord;
   }

   public List<RIParallelWord> getRIParallelWordById (long keyWordId) throws HandlerException {
      List<RIParallelWord> riWords = new ArrayList<RIParallelWord>();
      try {
         riWords = getPreferencesRetrievalService().getRIParallelWordsForKeyWord(keyWordId);
      } catch (PreferencesException PreferencesException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, PreferencesException);
      }
      return riWords;

   }

   public List<RIOntoTerm> getRIOntoTermsByKeyWord (String keyWord) throws HandlerException {
      List<RIOntoTerm> riOntoTerms = new ArrayList<RIOntoTerm>();
      try {
         riOntoTerms = getPreferencesRetrievalService().getRIontoTermsForKeyWord(keyWord);
      } catch (PreferencesException PreferencesException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, PreferencesException);
      }
      return riOntoTerms;
   }

   /**
    * @return the kdxMaintenanceService
    */
   public IKDXMaintenanceService getKdxMaintenanceService () {
      return kdxMaintenanceService;
   }

   /**
    * @param kdxMaintenanceService
    *           the kdxMaintenanceService to set
    */
   public void setKdxMaintenanceService (IKDXMaintenanceService kdxMaintenanceService) {
      this.kdxMaintenanceService = kdxMaintenanceService;
   }

   /**
    * @return the businessEntityMaintenanceService
    */
   public IBusinessEntityMaintenanceService getBusinessEntityMaintenanceService () {
      return businessEntityMaintenanceService;
   }

   /**
    * @param businessEntityMaintenanceService
    *           the businessEntityMaintenanceService to set
    */
   public void setBusinessEntityMaintenanceService (IBusinessEntityMaintenanceService businessEntityMaintenanceService) {
      this.businessEntityMaintenanceService = businessEntityMaintenanceService;
   }

   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return preferencesRetrievalService;
   }

   public void setPreferencesRetrievalService (IPreferencesRetrievalService preferencesRetrievalService) {
      this.preferencesRetrievalService = preferencesRetrievalService;
   }

   public IPreferencesManagementService getPreferencesManagementService () {
      return preferencesManagementService;
   }

   public void setPreferencesManagementService (IPreferencesManagementService preferencesManagementService) {
      this.preferencesManagementService = preferencesManagementService;
   }

   public IPreferencesDeletionService getPreferencesDeletionService () {
      return preferencesDeletionService;
   }

   public void setPreferencesDeletionService (IPreferencesDeletionService preferencesDeletionService) {
      this.preferencesDeletionService = preferencesDeletionService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IBusinessEntityDeletionWrapperService getBusinessEntityDeletionWrapperService () {
      return businessEntityDeletionWrapperService;
   }

   public void setBusinessEntityDeletionWrapperService (
            IBusinessEntityDeletionWrapperService businessEntityDeletionWrapperService) {
      this.businessEntityDeletionWrapperService = businessEntityDeletionWrapperService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IRangeSuggestionService getRangeSuggestionService () {
      return rangeSuggestionService;
   }

   public void setRangeSuggestionService (IRangeSuggestionService rangeSuggestionService) {
      this.rangeSuggestionService = rangeSuggestionService;
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   private Long getUserId () {
      return getUserContext().getUser().getId();
   }

   public ISWIPlatformManagementService getSwiPlatformManagementService () {
      return swiPlatformManagementService;
   }

   public void setSwiPlatformManagementService (ISWIPlatformManagementService swiPlatformManagementService) {
      this.swiPlatformManagementService = swiPlatformManagementService;
   }

}
