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

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.type.ProfileType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.swi.IBusinessEntityDeletionWrapperService;
import com.execue.platform.swi.IIndexFormManagementService;
import com.execue.platform.unstructured.IUnstructuredWarehouseManagementWrapperService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PreferencesException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IDefaultDynamicValueService;
import com.execue.swi.service.IKDXCloudDeletionService;
import com.execue.swi.service.IKDXCloudManagementService;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXDeletionService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingDeletionService;
import com.execue.swi.service.IPathDefinitionDeletionService;
import com.execue.swi.service.IPreferencesDeletionService;
import com.execue.swi.service.IPreferencesManagementService;
import com.execue.swi.service.IPreferencesRetrievalService;
import com.execue.swi.service.IUserQueryPossibilityService;

public class BusinessEntityDeletionWrapperServiceImpl implements IBusinessEntityDeletionWrapperService {

   private IPreferencesDeletionService                    preferencesDeletionService;
   private IPreferencesRetrievalService                   preferencesRetrievalService;
   private IIndexFormManagementService                    indexFormManagementService;
   private IKDXRetrievalService                           kdxRetrievalService;
   private IKDXDeletionService                            kdxDeletionService;
   private IKDXManagementService                          kdxManagementService;
   private IMappingDeletionService                        mappingDeletionService;
   private IDefaultDynamicValueService                    defaultDynamicValueService;
   private IUserQueryPossibilityService                   userQueryPossibilityService;
   private IPathDefinitionDeletionService                 pathDefinitionDeletionService;
   private IKDXCloudDeletionService                       kdxCloudDeletionService;
   private IBusinessEntityMaintenanceService              businessEntityMaintenanceService;
   private IPreferencesManagementService                  preferencesManagementService;
   private IKDXCloudRetrievalService                      kdxCloudRetrievalService;
   private IKDXCloudManagementService                     kdxCloudManagementService;
   private IKDXModelService                               kdxModelService;
   private IUnstructuredWarehouseManagementWrapperService unstructuredWarehouseManagementWrapperService;
   private IApplicationRetrievalService                   applicationRetrievalService;

   public void deleteConceptHeirarchy (Long modelId, Long conceptBedId) throws PlatformException {
      try {
         getPreferencesDeletionService().deleteKeywordsForBusinessEntity(conceptBedId);
         getIndexFormManagementService().manageIndexForms(modelId, conceptBedId, null, OperationType.DELETE);
         Concept concept = getKdxRetrievalService().getBusinessEntityDefinitionById(conceptBedId).getConcept();
         getKdxDeletionService().deleteBusinessEntityVariations(conceptBedId);
         handleProfilesForConceptDeletion(modelId, concept);
         getMappingDeletionService().deleteMappingForBusinessEntity(conceptBedId);
         deleteConceptMiscTables(conceptBedId);
         Application application = getApplicationByModelId(modelId);
         // direct instance deletion, need to consider unstructured also, in order to remove feature values.
         if (AppSourceType.UNSTRUCTURED.equals(application.getSourceType())) {
            // TODO-JT-Commented this block for now as flow is not completed yet
            // getUnstructuredWarehouseManagementWrapperService().manageUnstructuredWarehouse(application.getId(),
            // conceptBedId, null, OperationType.DELETE);
         }
         deleteInstanceHeirarchyForConcept(modelId, concept.getId());
         getBusinessEntityMaintenanceService().deleteBusinessEntityMaintenanceDetailsByEntityBedId(conceptBedId);
         getKdxDeletionService().deleteBusinessEntityDefinitionById(conceptBedId);
         getKdxDeletionService().deleteConcept(concept);
      } catch (PreferencesException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public void deleteInstanceHeirarchyForConcept (Long modelId, Long parentConceptId) throws PlatformException {
      try {
         List<Long> instanceIds = getKdxRetrievalService().getInstanceIdsByConceptId(modelId, parentConceptId);
         if (ExecueCoreUtil.isCollectionNotEmpty(instanceIds)) {
            //delete instance keyword hierarchy
            deleteInstanceKeywordsForConcept(modelId, parentConceptId);
            // manage index form
            for (Long instanceId : instanceIds) {
               BusinessEntityDefinition instanceBed = getKdxRetrievalService().getBusinessEntityDefinitionByIds(
                        modelId, parentConceptId, instanceId);
               BusinessEntityDefinition conceptBed = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                        parentConceptId, null);
               getIndexFormManagementService().manageIndexForms(modelId, instanceBed.getId(), conceptBed.getId(),
                        OperationType.DELETE);
            }
            // delete Instance variation
            getKdxManagementService().deleteInstanceVariationsForConcept(modelId, parentConceptId);
            //delete instance profile
            handleInstanceProfilesDeletionForConcept(modelId, parentConceptId);
            //delete mappings
            getMappingDeletionService().deleteInstanceMappingsForConcept(modelId, parentConceptId);
            //handle unstructured app
            Application application = getApplicationByModelId(modelId);
            if (AppSourceType.UNSTRUCTURED.equals(application.getSourceType())) {
               // TODO-JT-Commented this block for now as flow is not completed yet
               //            getUnstructuredWarehouseManagementWrapperService().manageUnstructuredWarehouse(application.getId(),
               //                     instanceBedId, parentConceptBedId, OperationType.DELETE);
            }
            // delete instance beds
            getKdxDeletionService().deleteInstanceBedsForConcept(modelId, parentConceptId);
            //delete instances
            getKdxDeletionService().deleteInstanceByIds(instanceIds);
         }

      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (PreferencesException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   public void deleteInstanceHeirarchy (Long modelId, Long parentConceptBedId, Long instanceBedId)
            throws PlatformException {
      try {
         getPreferencesDeletionService().deleteKeywordsForBusinessEntity(instanceBedId);
         getIndexFormManagementService().manageIndexForms(modelId, instanceBedId, parentConceptBedId,
                  OperationType.DELETE);
         Instance instance = getKdxRetrievalService().getBusinessEntityDefinitionById(instanceBedId).getInstance();
         getKdxDeletionService().deleteBusinessEntityVariations(instanceBedId);
         handleProfilesForInstanceDeletion(modelId, instance.getId());
         getMappingDeletionService().deleteMappingForBusinessEntity(instanceBedId);

         Application application = getApplicationByModelId(modelId);
         if (AppSourceType.UNSTRUCTURED.equals(application.getSourceType())) {
            // TODO-JT-Commented this block for now as flow is not completed yet
            //            getUnstructuredWarehouseManagementWrapperService().manageUnstructuredWarehouse(application.getId(),
            //                     instanceBedId, parentConceptBedId, OperationType.DELETE);
         }
         getBusinessEntityMaintenanceService().deleteBusinessEntityMaintenanceDetailsByEntityBedId(instanceBedId);
         getKdxDeletionService().deleteBusinessEntityDefinitionById(instanceBedId);
         getKdxDeletionService().deleteInstance(instance);
      } catch (PreferencesException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   public void deleteRelationHeirarchy (Long modelId, Long relationBedId) throws PlatformException {
      try {
         getPreferencesDeletionService().deleteKeywordsForBusinessEntity(relationBedId);
         getPathDefinitionDeletionService().deletePathDefinitions(relationBedId);
         getKdxCloudDeletionService().removeComponentFromCloudByModelId(modelId, relationBedId);
         getIndexFormManagementService().manageIndexForms(modelId, relationBedId, null, OperationType.DELETE);
         getKdxDeletionService().deleteBusinessEntityVariations(relationBedId);
         Relation relation = getKdxRetrievalService().getBusinessEntityDefinitionById(relationBedId).getRelation();
         getBusinessEntityMaintenanceService().deleteBusinessEntityMaintenanceDetailsByEntityBedId(relationBedId);
         getKdxDeletionService().deleteBusinessEntityDefinitionById(relationBedId);
         getKdxDeletionService().deleteRelation(relation);
      } catch (PreferencesException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   public void deleteInstancesHierarchyForConcept (Long modelId, Long conceptId) throws PlatformException {
      // get the instance profiles for this conceptBedId
      // delete the rionto terms for these profiles
      // delete these profiles
      // delete the rionto terms for the instances of this concept
      // delete the instances
      try {
         BusinessEntityDefinition conceptBed = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                  conceptId, null);
         List<ModelGroup> modelGroups = getKdxRetrievalService().getUserModelGroupsByModelId(modelId);
         Long modelGroupId = modelGroups.get(0).getId();
         // cleaning the instance profiles for this concept.
         List<InstanceProfile> instanceProfilesForConcept = getPreferencesRetrievalService()
                  .getInstanceProfilesForConcept(modelId, conceptId);
         if (ExecueCoreUtil.isCollectionNotEmpty(instanceProfilesForConcept)) {
            // delete entries from business maintenance table
            List<BusinessEntityDefinition> instanceProfileBeds = new ArrayList<BusinessEntityDefinition>();
            for (InstanceProfile instanceProfile : instanceProfilesForConcept) {
               instanceProfileBeds.add(getPreferencesRetrievalService().getBusinessEntityDefinitionForInstanceProfile(
                        instanceProfile));
            }
            List<Long> instanceProfileBEDIdsListFromBED = getInstanceProfileIdsListFromBED(instanceProfileBeds);
            getBusinessEntityMaintenanceService().deleteBusinessEntityMaintenanceDetails(modelId,
                     instanceProfileBEDIdsListFromBED, EntityType.INSTANCE_PROFILE);

            // delete ri and sfl if any for instance profile.
            getIndexFormManagementService().manageIndexForms(modelId, instanceProfileBEDIdsListFromBED,
                     conceptBed.getId(), OperationType.DELETE);

            // delete the profiles.
            List<Profile> instanceProfilesAsProfilesList = getInstanceProfilesAsProfilesList(instanceProfilesForConcept);
            deleteProfiles(modelId, instanceProfilesAsProfilesList);
         }

         List<BusinessEntityDefinition> instancesBeds = getKdxRetrievalService().getInstanceBEDsByConceptId(modelId,
                  conceptId);
         if (ExecueCoreUtil.isCollectionNotEmpty(instancesBeds)) {
            getKdxManagementService().deleteInstanceRIOntoTermsByConceptBEDId(modelGroupId, conceptBed.getId());
            List<Long> instanceBedIdsList = getInstanceBedIdsList(instancesBeds);
            getBusinessEntityMaintenanceService().deleteBusinessEntityMaintenanceDetails(modelId, instanceBedIdsList,
                     EntityType.CONCEPT_LOOKUP_INSTANCE);
            List<Instance> instances = getInstances(instancesBeds);
            getKdxDeletionService().deleteInstances(instances);
         }
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   public void deleteInstanceProfileHeirarchy (Long modelId, Long instanceProfileBedId) throws PlatformException {
      try {
         getPreferencesDeletionService().deleteKeywordsForBusinessEntity(instanceProfileBedId);
         // TODO : -check whether parent concept is needed.
         getIndexFormManagementService().manageIndexForms(modelId, instanceProfileBedId, null, OperationType.DELETE);
         getKdxDeletionService().deleteBusinessEntityVariations(instanceProfileBedId);
         InstanceProfile instanceProfile = getKdxRetrievalService().getBusinessEntityDefinitionById(
                  instanceProfileBedId).getInstanceProfile();
         getBusinessEntityMaintenanceService()
                  .deleteBusinessEntityMaintenanceDetailsByEntityBedId(instanceProfileBedId);
         getKdxDeletionService().deleteBusinessEntityDefinitionById(instanceProfileBedId);
         getPreferencesDeletionService().deleteInstanceProfile(instanceProfile);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   public void deleteConceptProfileHeirarchy (Long modelId, Long conceptProfileBedId) throws PlatformException {
      try {
         getPreferencesDeletionService().deleteKeywordsForBusinessEntity(conceptProfileBedId);
         getPathDefinitionDeletionService().deletePathDefinitions(conceptProfileBedId);
         getKdxCloudDeletionService().removeComponentFromCloudByModelId(modelId, conceptProfileBedId);
         getIndexFormManagementService().manageIndexForms(modelId, conceptProfileBedId, null, OperationType.DELETE);
         getKdxDeletionService().deleteBusinessEntityVariations(conceptProfileBedId);
         ConceptProfile conceptProfile = getKdxRetrievalService().getBusinessEntityDefinitionById(conceptProfileBedId)
                  .getConceptProfile();
         getBusinessEntityMaintenanceService().deleteBusinessEntityMaintenanceDetailsByEntityBedId(conceptProfileBedId);
         getKdxDeletionService().deleteBusinessEntityDefinitionById(conceptProfileBedId);
         getPreferencesDeletionService().deleteConceptProfile(conceptProfile);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   /**
    * @param conceptProfile
    * @param profileBed
    * @param modelId
    * @throws SWIException
    */
   @Override
   public void deleteProfile (Long modelId, Profile profile) throws PlatformException {
      try {
         profile = getPreferencesRetrievalService().getProfile(profile.getId(), profile.getType());
         BusinessEntityDefinition businessEntityDefinition = null;
         ProfileType profileType = ProfileType.CONCEPT;
         if (ProfileType.CONCEPT.equals(profile.getType())) {
            ConceptProfile conceptProfile = (ConceptProfile) profile;
            businessEntityDefinition = getPreferencesRetrievalService().getBusinessEntityDefinitionForConceptProfile(
                     conceptProfile);
            profileType = ProfileType.CONCEPT;
         } else if (ProfileType.CONCEPT_LOOKUP_INSTANCE.equals(profile.getType())) {
            InstanceProfile instanceProfile = (InstanceProfile) profile;
            businessEntityDefinition = getPreferencesRetrievalService().getBusinessEntityDefinitionForInstanceProfile(
                     instanceProfile);
            profileType = ProfileType.CONCEPT_LOOKUP_INSTANCE;
         }

         if (businessEntityDefinition == null) {
            return;
         }

         Cloud defautlCloud = getKdxCloudRetrievalService().getDefaultAppCloud(modelId);

         // delete from cloud component
         if (ProfileType.CONCEPT.equals(profile.getType())) {
            getKdxCloudManagementService().removeComponentFromCloud(defautlCloud.getId(),
                     businessEntityDefinition.getId());
         }

         // Delete from ETD which will internally delete the path definitions as well
         List<EntityTripleDefinition> etds = getKdxModelService().getEntityTriplesForSource(
                  businessEntityDefinition.getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(etds)) {
            for (EntityTripleDefinition entityTripleDefinition : etds) {
               getPathDefinitionDeletionService().deleteEntityTripleDefinition(entityTripleDefinition);
            }
         }

         // delete from ri onto term
         ModelGroup defaultGroup = getKdxRetrievalService().getPrimaryGroup(modelId);
         getKdxManagementService().deleteRIOntoTermsByProfileBEDId(defaultGroup.getId(),
                  businessEntityDefinition.getId());

         // delete entries from business entity maintenance
         List<Long> bedIds = new ArrayList<Long>();
         bedIds.add(businessEntityDefinition.getId());
         getBusinessEntityMaintenanceService().deleteBusinessEntityMaintenanceDetails(modelId, bedIds,
                  ExecueBeanUtil.getCorrespondingEntityType(businessEntityDefinition.getEntityType()));

         // delete the profile
         getPreferencesManagementService().deleteProfile(profile, modelId, businessEntityDefinition, profileType);
      } catch (SWIException swiException) {
         throw new PlatformException(swiException.getCode(), swiException);
      }
   }

   @Override
   public void deleteProfiles (Long modelId, List<Profile> profiles) throws PlatformException {
      for (Profile profile : profiles) {
         deleteProfile(modelId, profile);
      }
   }

   @Override
   public void deleteRangeHeirarchy (Long rangeId) throws PlatformException {
      try {
         getPreferencesDeletionService().deleteRangeHeirarchy(rangeId);
      } catch (PreferencesException e) {
         throw new PlatformException(e.getCode(), e);
      }

   }

   private void handleInstanceProfilesDeletionForConcept (Long modelId, Long conceptId) throws PreferencesException,
            PlatformException {
      List<InstanceProfile> instanceProfiles = getPreferencesRetrievalService().getInstanceProfilesForConcept(modelId,
               conceptId);
      if (ExecueCoreUtil.isCollectionNotEmpty(instanceProfiles)) {
         for (InstanceProfile instanceProfile : instanceProfiles) {
            BusinessEntityDefinition instanceProfileBed = getPreferencesRetrievalService()
                     .getBusinessEntityDefinitionForInstanceProfile(instanceProfile);
            deleteInstanceProfileHeirarchy(modelId, instanceProfileBed.getId());
         }
      }

   }

   private void deleteInstanceKeywordsForConcept (Long modelId, Long conceptId) throws PreferencesException {
      List<KeyWord> keyWords = getPreferencesRetrievalService().getKeyWordsForInstances(modelId, conceptId);
      if (ExecueCoreUtil.isCollectionNotEmpty(keyWords)) {
         for (KeyWord keyWord : keyWords) {
            getPreferencesDeletionService().deleteKeywordHeirarchy(keyWord);
         }
      }
   }

   private void handleProfilesForInstanceDeletion (Long modelId, Long instanceId) throws PreferencesException,
            PlatformException {
      List<Long> instanceProfileIds = getPreferencesRetrievalService().getInstanceProfileIdsForInstance(instanceId);
      if (ExecueCoreUtil.isCollectionNotEmpty(instanceProfileIds)) {
         List<InstanceProfile> instanceProfiles = getPreferencesRetrievalService().getInstanceProfilesByIds(
                  instanceProfileIds);
         List<InstanceProfile> profilesToBeDeleted = new ArrayList<InstanceProfile>();
         for (InstanceProfile instanceProfile : instanceProfiles) {
            Set<Instance> instances = instanceProfile.getInstances();
            Set<Instance> profileInstancesToBeUpdated = new HashSet<Instance>();
            for (Instance profileInstance : instances) {
               if (!profileInstance.getId().equals(instanceId)) {
                  profileInstancesToBeUpdated.add(profileInstance);
               }
            }
            if (profileInstancesToBeUpdated.size() >= 2) {
               instanceProfile.setInstances(profileInstancesToBeUpdated);
               try {
                  getPreferencesManagementService().updateProfile(instanceProfile, modelId);
               } catch (PreferencesException preferencesException) {
                  profilesToBeDeleted.add(instanceProfile);
               }
            } else {
               profilesToBeDeleted.add(instanceProfile);
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(profilesToBeDeleted)) {
            for (InstanceProfile instanceProfile : profilesToBeDeleted) {
               BusinessEntityDefinition instanceProfileBed = getPreferencesRetrievalService()
                        .getBusinessEntityDefinitionForInstanceProfile(instanceProfile);
               deleteInstanceProfileHeirarchy(modelId, instanceProfileBed.getId());
            }
         }
      }
   }

   private void deleteConceptMiscTables (Long conceptBedId) throws SWIException {
      getUserQueryPossibilityService().deleteUserQueryPossibilitiesByEntityBedId(conceptBedId,
               BusinessEntityType.CONCEPT);
      getKdxDeletionService().deleteVerticalEntityRedirectionEntitiesByEntityBedId(conceptBedId,
               BusinessEntityType.CONCEPT);
      getDefaultDynamicValueService().deleteDefaultDynamicValueByBedId(conceptBedId);
      getKdxDeletionService().deleteEntityBehavior(conceptBedId);
      getKdxDeletionService().deleteEntityDetailTypeByConcept(conceptBedId);
   }

   private List<Instance> getInstances (List<BusinessEntityDefinition> instancesBeds) {
      List<Instance> instances = new ArrayList<Instance>();
      for (BusinessEntityDefinition businessEntityDefinition : instancesBeds) {
         instances.add(businessEntityDefinition.getInstance());
      }
      return instances;
   }

   private List<Long> getInstanceBedIdsList (List<BusinessEntityDefinition> instancesBeds) {
      List<Long> instanceBedIdsList = new ArrayList<Long>();
      for (BusinessEntityDefinition businessEntityDefinition : instancesBeds) {
         instanceBedIdsList.add(businessEntityDefinition.getId());
      }
      return instanceBedIdsList;
   }

   private List<Profile> getInstanceProfilesAsProfilesList (List<InstanceProfile> instanceProfilesForConcept) {
      List<Profile> instanceProfileAsProfiles = new ArrayList<Profile>();
      instanceProfileAsProfiles.addAll(instanceProfilesForConcept);
      return instanceProfileAsProfiles;
   }

   private List<Long> getInstanceProfileIdsListFromBED (List<BusinessEntityDefinition> instanceProfilesBeds) {
      List<Long> instanceProfileIdsList = new ArrayList<Long>();
      for (BusinessEntityDefinition instanceProfileBed : instanceProfilesBeds) {
         instanceProfileIdsList.add(instanceProfileBed.getId());
      }
      return instanceProfileIdsList;
   }

   private void handleProfilesForConceptDeletion (Long modelId, Concept concept) throws PreferencesException,
            PlatformException {

      // method to get concept profiles for concept
      List<Long> conceptProfileIds = getPreferencesRetrievalService().getConceptProfileIdsForConcept(concept.getId());
      if (ExecueCoreUtil.isCollectionNotEmpty(conceptProfileIds)) {
         List<ConceptProfile> conceptProfiles = getPreferencesRetrievalService().getConceptProfilesByIds(
                  conceptProfileIds);
         List<ConceptProfile> profilesToBeDeleted = new ArrayList<ConceptProfile>();

         for (ConceptProfile conceptProfile : conceptProfiles) {
            Set<Concept> concepts = conceptProfile.getConcepts();
            Set<Concept> profileConceptToBeUpdated = new HashSet<Concept>();
            for (Concept profileConcept : concepts) {

               if (!profileConcept.getId().equals(concept.getId())) {
                  profileConceptToBeUpdated.add(profileConcept);
               }

            }
            if (profileConceptToBeUpdated.size() >= 2) {
               conceptProfile.setConcepts(profileConceptToBeUpdated);
               try {
                  getPreferencesManagementService().updateProfile(conceptProfile, modelId);
               } catch (PreferencesException preferencesException) {
                  // TODO : -VG- need to check if same profile exists.
                  profilesToBeDeleted.add(conceptProfile);
               }
            } else {
               profilesToBeDeleted.add(conceptProfile);
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(profilesToBeDeleted)) {
            for (ConceptProfile conceptProfile : profilesToBeDeleted) {
               BusinessEntityDefinition conceptProfileBed = getPreferencesRetrievalService()
                        .getBusinessEntityDefinitionForConceptProfile(conceptProfile);
               deleteConceptProfileHeirarchy(modelId, conceptProfileBed.getId());
            }
         }
      }

   }

   public IPreferencesDeletionService getPreferencesDeletionService () {
      return preferencesDeletionService;
   }

   public void setPreferencesDeletionService (IPreferencesDeletionService preferencesDeletionService) {
      this.preferencesDeletionService = preferencesDeletionService;
   }

   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return preferencesRetrievalService;
   }

   public void setPreferencesRetrievalService (IPreferencesRetrievalService preferencesRetrievalService) {
      this.preferencesRetrievalService = preferencesRetrievalService;
   }

   public IIndexFormManagementService getIndexFormManagementService () {
      return indexFormManagementService;
   }

   public void setIndexFormManagementService (IIndexFormManagementService indexFormManagementService) {
      this.indexFormManagementService = indexFormManagementService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IKDXDeletionService getKdxDeletionService () {
      return kdxDeletionService;
   }

   public void setKdxDeletionService (IKDXDeletionService kdxDeletionService) {
      this.kdxDeletionService = kdxDeletionService;
   }

   public IMappingDeletionService getMappingDeletionService () {
      return mappingDeletionService;
   }

   public void setMappingDeletionService (IMappingDeletionService mappingDeletionService) {
      this.mappingDeletionService = mappingDeletionService;
   }

   public IDefaultDynamicValueService getDefaultDynamicValueService () {
      return defaultDynamicValueService;
   }

   public void setDefaultDynamicValueService (IDefaultDynamicValueService defaultDynamicValueService) {
      this.defaultDynamicValueService = defaultDynamicValueService;
   }

   public IUserQueryPossibilityService getUserQueryPossibilityService () {
      return userQueryPossibilityService;
   }

   public void setUserQueryPossibilityService (IUserQueryPossibilityService userQueryPossibilityService) {
      this.userQueryPossibilityService = userQueryPossibilityService;
   }

   public IPathDefinitionDeletionService getPathDefinitionDeletionService () {
      return pathDefinitionDeletionService;
   }

   public void setPathDefinitionDeletionService (IPathDefinitionDeletionService pathDefinitionDeletionService) {
      this.pathDefinitionDeletionService = pathDefinitionDeletionService;
   }

   public IKDXCloudDeletionService getKdxCloudDeletionService () {
      return kdxCloudDeletionService;
   }

   public void setKdxCloudDeletionService (IKDXCloudDeletionService kdxCloudDeletionService) {
      this.kdxCloudDeletionService = kdxCloudDeletionService;
   }

   public IBusinessEntityMaintenanceService getBusinessEntityMaintenanceService () {
      return businessEntityMaintenanceService;
   }

   public void setBusinessEntityMaintenanceService (IBusinessEntityMaintenanceService businessEntityMaintenanceService) {
      this.businessEntityMaintenanceService = businessEntityMaintenanceService;
   }

   public IPreferencesManagementService getPreferencesManagementService () {
      return preferencesManagementService;
   }

   public void setPreferencesManagementService (IPreferencesManagementService preferencesManagementService) {
      this.preferencesManagementService = preferencesManagementService;
   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
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

   public IKDXModelService getKdxModelService () {
      return kdxModelService;
   }

   public void setKdxModelService (IKDXModelService kdxModelService) {
      this.kdxModelService = kdxModelService;
   }

   private Application getApplicationByModelId (Long modelId) throws KDXException {
      return getApplicationRetrievalService().getApplicationByModelId(modelId);
   }

   /**
    * @return the unstructuredWarehouseManagementWrapperService
    */
   public IUnstructuredWarehouseManagementWrapperService getUnstructuredWarehouseManagementWrapperService () {
      return unstructuredWarehouseManagementWrapperService;
   }

   /**
    * @param unstructuredWarehouseManagementWrapperService the unstructuredWarehouseManagementWrapperService to set
    */
   public void setUnstructuredWarehouseManagementWrapperService (
            IUnstructuredWarehouseManagementWrapperService unstructuredWarehouseManagementWrapperService) {
      this.unstructuredWarehouseManagementWrapperService = unstructuredWarehouseManagementWrapperService;
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
