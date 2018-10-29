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


package com.execue.platform.swi.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.TypeConceptAssociationInfo;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ComponentCategory;
import com.execue.core.common.type.OriginType;
import com.execue.core.common.type.ProfileType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.UidException;
import com.execue.platform.IUidService;
import com.execue.platform.swi.IConceptTypeAssociationService;
import com.execue.platform.swi.ISWIPlatformManagementService;
import com.execue.swi.exception.PreferencesException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXCloudManagementService;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathDefinitionManagementService;
import com.execue.swi.service.IPathDefinitionRetrievalService;
import com.execue.swi.service.IPreferencesManagementService;

public class SWIPlatformManagementServiceImpl implements ISWIPlatformManagementService {

   private IPreferencesManagementService    preferencesManagementService;
   private IKDXCloudRetrievalService        kdxCloudRetrievalService;
   private IKDXCloudManagementService       kdxCloudManagementService;
   private IConceptTypeAssociationService   conceptTypeAssociationService;
   private IKDXRetrievalService             kdxRetrievalService;
   private IPathDefinitionRetrievalService  pathDefinitionRetrievalService;
   private IPathDefinitionManagementService pathDefinitionManagementService;
   private IUidService                      knowledgeIdGenerationService;
   private static final Logger              logger = Logger.getLogger(SWIPlatformManagementServiceImpl.class);

   public BusinessEntityDefinition createProfile (Profile profile, Type type, Long modelId, Long userId,
            CheckType hybridProfile) throws PreferencesException {
      BusinessEntityDefinition profileBed = null;
      try {
         profileBed = getPreferencesManagementService().createProfile(profile, type, modelId, userId, hybridProfile,
                  getKnowledgeIdGenerationService().getNextId());
         // For concept profile, also create the cloud component
         if (ProfileType.CONCEPT.equals(profile.getType())) {

            // TODO: NK: Getting the default cloud for the model, should be change once we have different clouds per
            // model
            Cloud defaultAppCloud = getKdxCloudRetrievalService().getDefaultAppCloud(modelId);
            // BusinessEntityDefinition typeBusinessEntityDefinition = getKdxRetrievalService()
            // .getTypeBusinessEntityDefinition(type.getId());
            // -JT-Fixed issue in profile creation. We are using type info later on during concept type assignment when
            // updating detail entity type , so we need to
            // have BusinessEntityDefinition with populated type
            BusinessEntityDefinition typeBusinessEntityDefinition = getKdxRetrievalService()
                     .getPopulatedTypeBusinessEntityDefinition(type.getId());
            List<CloudComponent> componentsToBeAdded = new ArrayList<CloudComponent>(1);
            componentsToBeAdded.add(ExecueBeanManagementUtil.prepareCloudComponent(defaultAppCloud, profileBed,
                     typeBusinessEntityDefinition, ComponentCategory.REALIZATION));
            getKdxCloudManagementService().addComponentsToCloud(componentsToBeAdded);

            // TODO: NK: Added for assigning the default paths.
            // Should remove it once we have the separate assignType call for concept profiles too from UI
            TypeConceptAssociationInfo typeConceptInfo = ExecueBeanManagementUtil.populateTypeConceptInfo(profileBed,
                     typeBusinessEntityDefinition, defaultAppCloud, modelId, null, null, null, false, false, false,
                     null, null, false, false, false);
            getConceptTypeAssociationService().assignConceptType(typeConceptInfo);

            // Create the CRC Paths by taking one of the concept in the profile first as source then as destination
            // this condition will not be applied for HYBRID profile
            if (CheckType.NO.equals(hybridProfile)) {
               createCRCTriples((ConceptProfile) profile, profileBed, modelId);
            }
         }
      } catch (PreferencesException e) {
         throw new PreferencesException(e.getCode(), e.getMessage());
      } catch (SWIException e) {
         throw new PreferencesException(e.getCode(), e.getMessage());
      } catch (UidException e) {
         throw new PreferencesException(e.getCode(), e.getMessage());
      }
      return profileBed;
   }

   private Concept getConcept (Set<Concept> concepts) {
      return new ArrayList<Concept>(concepts).get(0);
   }

   private void createCRCTriples (ConceptProfile conceptProfile, BusinessEntityDefinition profileBed, Long modelId)
            throws PreferencesException, SWIException {
      Concept concept = getConcept(conceptProfile.getConcepts());

      // TODO: NK: Currently setting only for direct paths, need to check if we need to replicate this for
      // indirect
      // paths too??
      BusinessEntityDefinition conceptBed = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
               concept.getId(), null);
      List<PathDefinition> directPathsBySource = getPathDefinitionRetrievalService().getAllDirectPathsBySourceId(
               conceptBed.getId(), modelId);
      List<PathDefinition> directPathsByDestination = getPathDefinitionRetrievalService()
               .getAllDirectPathsByDestinationId(conceptBed.getId(), modelId);

      Set<String> existingTriples = new HashSet<String>(1);
      for (PathDefinition directPath : directPathsBySource) {
         BusinessEntityDefinition source = profileBed;
         BusinessEntityDefinition destination = directPath.getDestinationBusinessEntityDefinition();
         for (Path path : directPath.getPaths()) {
            BusinessEntityDefinition relation = path.getEntityTripleDefinition().getRelation();
            String key = source.getId() + "-" + relation.getId() + "-" + destination.getId() + "-1";
            if (existingTriples.contains(key)) {
               if (logger.isDebugEnabled()) {
                  logger.debug("Skipping duplicate triple in createCRCPathsByReplacingExistingBed: " + key);
               }
               continue;
            }
            // Create the path definition
            EntityTripleDefinition etd = ExecueBeanManagementUtil.prepareEntityTripleDefinition(source, relation,
                     destination, directPath.getType());
            etd.setOrigin(OriginType.USER);
            getPathDefinitionManagementService().createPathDefinition(directPath.getCloudId(), etd,
                     directPath.getPathRules());
            existingTriples.add(key);
         }
      }
      for (PathDefinition directPath : directPathsByDestination) {
         BusinessEntityDefinition source = directPath.getSourceBusinessEntityDefinition();
         BusinessEntityDefinition destination = profileBed;
         for (Path path : directPath.getPaths()) {
            BusinessEntityDefinition relation = path.getEntityTripleDefinition().getRelation();
            String key = source.getId() + "-" + relation.getId() + "-" + destination.getId() + "-1";
            if (existingTriples.contains(key)) {
               if (logger.isDebugEnabled()) {
                  logger.debug("Skipping duplicate triple in createCRCPathsByReplacingExistingBed: " + key);
               }
               continue;
            }
            // Create the path definition
            EntityTripleDefinition etd = ExecueBeanManagementUtil.prepareEntityTripleDefinition(source, relation,
                     destination, directPath.getType());
            etd.setOrigin(OriginType.USER);
            getPathDefinitionManagementService().createPathDefinition(directPath.getCloudId(), etd,
                     directPath.getPathRules());
            existingTriples.add(key);
         }
      }
   }

   public IPreferencesManagementService getPreferencesManagementService () {
      return preferencesManagementService;
   }

   public void setPreferencesManagementService (IPreferencesManagementService preferencesManagementService) {
      this.preferencesManagementService = preferencesManagementService;
   }

   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

   public IKDXCloudManagementService getKdxCloudManagementService () {
      return kdxCloudManagementService;
   }

   public void setKdxCloudManagementService (IKDXCloudManagementService kdxCloudManagementService) {
      this.kdxCloudManagementService = kdxCloudManagementService;
   }

   public IConceptTypeAssociationService getConceptTypeAssociationService () {
      return conceptTypeAssociationService;
   }

   public void setConceptTypeAssociationService (IConceptTypeAssociationService conceptTypeAssociationService) {
      this.conceptTypeAssociationService = conceptTypeAssociationService;
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

   public IPathDefinitionManagementService getPathDefinitionManagementService () {
      return pathDefinitionManagementService;
   }

   public void setPathDefinitionManagementService (IPathDefinitionManagementService pathDefinitionManagementService) {
      this.pathDefinitionManagementService = pathDefinitionManagementService;
   }

   public IUidService getKnowledgeIdGenerationService () {
      return knowledgeIdGenerationService;
   }

   public void setKnowledgeIdGenerationService (IUidService knowledgeIdGenerationService) {
      this.knowledgeIdGenerationService = knowledgeIdGenerationService;
   }

}
