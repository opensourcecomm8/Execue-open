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

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.ParallelWord;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.type.ProfileType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.swi.dataaccess.IPreferencesDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PreferencesException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPreferencesManagementService;
import com.execue.swi.validation.ISWIValidator;

public class PreferencesManagementServiceImpl implements IPreferencesManagementService {

   private IPreferencesDataAccessManager     preferencesDataAccessManager;

   private ISWIValidator                    swiValidator;

   private IKDXRetrievalService              kdxRetrievalService;

   private IBusinessEntityMaintenanceService businessEntityMaintenanceService;

   private IBaseKDXRetrievalService          baseKDXRetrievalService;

   // *********** Methods related to Profiles **************************************************************
   public BusinessEntityDefinition createProfile (Profile profile, Type type, Long modelId, Long userId,
            CheckType hybridProfile, Long knowledgeId) throws PreferencesException {
      BusinessEntityDefinition profileBed = null;
      try {
         String name = profile.getDisplayName();
         name = ExecueStringUtil.getNormalizedName(name);
         Long parentBedId = null;
         profile.setName(name);
         EntityType entityType = EntityType.CONCEPT_PROFILE;
         if (ProfileType.CONCEPT_LOOKUP_INSTANCE.equals(profile.getType())) {
            entityType = EntityType.INSTANCE_PROFILE;
            InstanceProfile instanceProfile = (InstanceProfile) profile;
            parentBedId = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                     instanceProfile.getConcept().getId(), null).getId();
         }
         validateProfiles(profile, modelId, parentBedId);
         ModelGroup primaryModelGroup = getKdxRetrievalService().getPrimaryGroup(modelId);
         profileBed = getPreferencesDataAccessManager().createProfile(profile, type, primaryModelGroup, userId,
                  knowledgeId);
         getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
                  ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(profileBed.getId(), entityType,
                           modelId, OperationType.ADD, parentBedId));
      } catch (PreferencesException e) {
         throw new PreferencesException(e.getCode(), e.getMessage());
      } catch (SWIException e) {
         throw new PreferencesException(e.getCode(), e.getMessage());
      }
      return profileBed;
   }

   private void validateProfiles (Profile profile, Long modelId, Long parentBedId) throws PreferencesException,
            PreferencesException {
      try {
         getBaseKDXRetrievalService().validateEntityForReservedWord(profile, BusinessEntityType.PROFILE);
         if (getSwiValidator().profileExists(modelId, profile, parentBedId)) {
            throw new PreferencesException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS,
                     "Profile with name or same set of members already exists");
         }
      } catch (KDXException e) {
         throw new PreferencesException(e.getCode(), e);
      }
   }

   public BusinessEntityDefinition updateProfile (Profile profile, Long modelId) throws PreferencesException {
      BusinessEntityDefinition updatedBed = null;
      try {
         String name = profile.getDisplayName();
         name = ExecueStringUtil.getNormalizedName(name);
         Long parentBedId = null;
         profile.setName(name);
         EntityType entityType = EntityType.CONCEPT_PROFILE;
         if (ProfileType.CONCEPT_LOOKUP_INSTANCE.equals(profile.getType())) {
            entityType = EntityType.INSTANCE_PROFILE;
            InstanceProfile instanceProfile = (InstanceProfile) profile;
            parentBedId = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                     instanceProfile.getConcept().getId(), null).getId();
         }
         validateProfiles(profile, modelId, parentBedId);
         ModelGroup primaryModelGroup = getKdxRetrievalService().getPrimaryGroup(modelId);
         updatedBed = getPreferencesDataAccessManager().updateProfile(profile, primaryModelGroup);
         getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
                  ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(updatedBed.getId(), entityType,
                           modelId, OperationType.UPDATE, parentBedId));
      } catch (KDXException e) {
         throw new PreferencesException(e.getCode(), e.getMessage());
      }
      return updatedBed;
   }

   public void cleanProfile (Profile profile) throws PreferencesException {
      getPreferencesDataAccessManager().cleanProfile(profile);
   }

   // *********** Methods related to Ranges **************************************************************

   public void createRange (Long modelId, Long userId, Range range) throws PreferencesException {
      range.setDescription(range.getName());
      Range rangeExists = getSwiValidator().rangeExists(modelId, range.getName());
      if (rangeExists != null) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Range with name [" + range.getName()
                  + "] already exists ");
      }
      getPreferencesDataAccessManager().createRange(range, userId);
   }

   public void updateRange (Long modelId, Long userId, Range range) throws PreferencesException {
      range.setDescription(range.getName());
      Range rangeExists = getSwiValidator().rangeExists(modelId, range.getName());
      if (rangeExists != null && rangeExists.getId().longValue() != range.getId().longValue()) {
         throw new PreferencesException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Range with name [" + range.getName()
                  + "] already exists ");
      }
      getPreferencesDataAccessManager().updateRange(range, userId);
   }

   // *********** Methods related to Riontoterm's,KeyWord's,Parallelword's ******************************************

   public void createRIParallelWords (List<RIParallelWord> riParallelWords) throws PreferencesException {
      for (RIParallelWord riParallelWord : riParallelWords) {
         riParallelWord.setWord(riParallelWord.getWord().toLowerCase());
         riParallelWord.setEquivalentWord(riParallelWord.getEquivalentWord().toLowerCase());
      }
      getPreferencesDataAccessManager().createRIParallelWords(riParallelWords);
   }

   public void createParallelWords (List<ParallelWord> parallelWords) throws PreferencesException {
      getPreferencesDataAccessManager().createParallelWords(parallelWords);
   }

   public void createKeyWord (KeyWord keyWord) throws PreferencesException {
      getPreferencesDataAccessManager().createKeyWord(keyWord);
   }

   public ParallelWord createParallelWord (ParallelWord parallelWord) throws PreferencesException {
      return getPreferencesDataAccessManager().createParallelWord(parallelWord);
   }

   public void updateParallelWords (List<ParallelWord> parallelWords) throws PreferencesException {
      getPreferencesDataAccessManager().updateParallelWords(parallelWords);
   }

   public ParallelWord updateParallelWord (ParallelWord parallelWord) throws PreferencesException {
      return getPreferencesDataAccessManager().updateParallelWord(parallelWord);
   }

   public void deleteProfile (Profile profile, Long modelId, BusinessEntityDefinition businessEntityDefinition,
            ProfileType profileType) throws PreferencesException {
      getPreferencesDataAccessManager().deleteProfile(profile, modelId, businessEntityDefinition, profileType);
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IBusinessEntityMaintenanceService getBusinessEntityMaintenanceService () {
      return businessEntityMaintenanceService;
   }

   public void setBusinessEntityMaintenanceService (IBusinessEntityMaintenanceService businessEntityMaintenanceService) {
      this.businessEntityMaintenanceService = businessEntityMaintenanceService;
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   public IPreferencesDataAccessManager getPreferencesDataAccessManager () {
      return preferencesDataAccessManager;
   }

   public void setPreferencesDataAccessManager (IPreferencesDataAccessManager preferencesDataAccessManager) {
      this.preferencesDataAccessManager = preferencesDataAccessManager;
   }

   public ISWIValidator getSwiValidator () {
      return swiValidator;
   }

   public void setSwiValidator (ISWIValidator swiValidator) {
      this.swiValidator = swiValidator;
   }

}
