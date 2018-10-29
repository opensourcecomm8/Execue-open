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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityDetailType;
import com.execue.core.common.bean.entity.EntityNameVariation;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.ParallelWord;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.swi.IndexFormManagementContext;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.type.RecognitionType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.platform.helper.IndexFormManagementHelper;
import com.execue.platform.swi.IIndexFormManagementService;
import com.execue.platform.swi.IRICloudsAbsorptionWrapperService;
import com.execue.platform.swi.IRIOntoTermAbsorbtionService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.bean.BusinessEntityDefinitionMaintenanceInfo;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPreferencesDeletionService;
import com.execue.swi.service.IPreferencesManagementService;
import com.execue.swi.service.IPreferencesRetrievalService;
import com.execue.swi.service.ISFLService;
import com.execue.swi.service.IUserManagementService;
import com.hp.hpl.jena.ontology.OntologyException;

/**
 * @author John Mallavalli
 */
public class IndexFormManagementServiceImpl implements IIndexFormManagementService {

   private IndexFormManagementHelper         indexFormManagementHelper;
   private IRIOntoTermAbsorbtionService      riOntoTermAbsorbtionService;
   private ISFLService                       sflService;
   private IKDXRetrievalService              kdxRetrievalService;
   private IJobDataService                   jobDataService;
   private IRICloudsAbsorptionWrapperService riCloudsAbsorptionWrapperService;
   private IKDXCloudRetrievalService         kdxCloudRetrievalService;
   private IBusinessEntityMaintenanceService businessEntityMaintenanceService;
   private IUserManagementService            userManagementService;
   private IPreferencesRetrievalService      preferencesRetrievalService;
   private IPreferencesManagementService     preferencesManagementService;
   private IPreferencesDeletionService       preferencesDeletionService;
   private IKDXManagementService             kdxManagementService;
   private IBaseKDXRetrievalService          baseKDXRetrievalService;
   private ISWIConfigurationService          swiConfigurationService;

   private void manageIndexForms (BusinessEntityDefinition businessTerm, BusinessEntityDefinition parentBusinessTerm,
            BusinessEntityDefinition typeBusinessTerm, OperationType operationType,
            Map<String, Double> secondaryWordsWeightMap, EntityDetailType entityDetailType) throws SWIException {
      try {
         if (OperationType.ADD.equals(operationType)) {
            List<EntityNameVariation> variations = indexFormManagementHelper.generateVariations(businessTerm);
            for (EntityNameVariation variation : variations) {
               boolean isInstanceTimeFrame = false;
               if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(businessTerm.getEntityType())
                        && ExecueConstants.TIME_FRAME_TYPE.equals(typeBusinessTerm.getType().getName())) {
                  isInstanceTimeFrame = true;
               }
               if (!isInstanceTimeFrame) {
                  getRiOntoTermAbsorbtionService().generateRIOntoTerm(variation, businessTerm, parentBusinessTerm,
                           typeBusinessTerm, entityDetailType);
                  getSflService().generateSFLTerm(variation, businessTerm.getModelGroup().getId(),
                           secondaryWordsWeightMap);
               }
            }
            // if profile has disabled status need to remove entries from RIOTerms table
            handleDisabledProfileRiOntoTerms(businessTerm);
         } else if (OperationType.DELETE.equals(operationType)) {
            deleteOntoTermsForEntity(businessTerm);
         } else if (OperationType.UPDATE.equals(operationType)) {
            deleteOntoTermsForEntity(businessTerm);
            // generate the new index forms using the updated business entity
            manageIndexForms(businessTerm, parentBusinessTerm, typeBusinessTerm, OperationType.ADD,
                     secondaryWordsWeightMap, entityDetailType);

         }

      } catch (KDXException kdxException) {
         throw new SWIException(kdxException.getCode(), kdxException);
      }
   }

   /**
    * if profile has disabled status need to remove entries from RIOTerms table , SFL does not need to be taken care
    * because it is already taken care by orphan SFL deletion
    * 
    * @param businessTerm
    * @throws KDXException
    * @throws OntologyException
    */
   private void handleDisabledProfileRiOntoTerms (BusinessEntityDefinition businessTerm) throws KDXException {
      if (BusinessEntityType.CONCEPT_PROFILE.equals(businessTerm.getEntityType())) {
         if (!businessTerm.getConceptProfile().isEnabled()) {
            deleteOntoTermsForEntity(businessTerm);
         }
      }
      if (BusinessEntityType.INSTANCE_PROFILE.equals(businessTerm.getEntityType())) {
         if (!businessTerm.getInstanceProfile().isEnabled()) {
            deleteOntoTermsForEntity(businessTerm);
         }
      }

   }

   private void deleteOntoTermsForEntity (BusinessEntityDefinition businessTerm) throws KDXException {
      // get the RIOTs based on the BED Id
      List<RIOntoTerm> riOntoTerms = getKdxRetrievalService().getRIOntoTermsByBEDId(businessTerm.getId(),
               businessTerm.getEntityType());
      // delete the RIOTs retrieved
      if (ExecueCoreUtil.isCollectionNotEmpty(riOntoTerms)) {
         for (RIOntoTerm riOntoTerm : riOntoTerms) {
            getKdxManagementService().deleteRIOntoTerm(riOntoTerm);
         }
      }
   }

   public void manageIndexForms (Long modelId, List<Long> businessEntityDefinitionIds, Long parentBedId,
            OperationType operationType) throws SWIException {
      BusinessEntityDefinition parentBusinessEntityDefinition = null;
      BusinessEntityDefinition typeBusinessEntityDefinition = null;
      EntityDetailType entityDetailType = null;
      if (parentBedId != null) {
         parentBusinessEntityDefinition = kdxRetrievalService.getPopulatedBEDForIndexForms(parentBedId);
         typeBusinessEntityDefinition = kdxRetrievalService
                  .getPopulatedTypeBusinessEntityDefinition(parentBusinessEntityDefinition.getType().getId());
         entityDetailType = getKdxRetrievalService().getDetailTypeForConcept(parentBusinessEntityDefinition.getId());
      }
      Map<String, Double> secondaryWordsWeightMap = buildSecondayWordWeightMap(modelId);
      for (Long bedId : businessEntityDefinitionIds) {
         if (parentBedId == null) {
            typeBusinessEntityDefinition = null;
            entityDetailType = null;
         }
         BusinessEntityDefinition businessEntityDefinition = kdxRetrievalService.getPopulatedBEDForIndexForms(bedId);

         if (typeBusinessEntityDefinition == null) {
            typeBusinessEntityDefinition = kdxRetrievalService
                     .getPopulatedTypeBusinessEntityDefinition(businessEntityDefinition.getType().getId());
         }
         if (entityDetailType == null) {
            entityDetailType = getKdxRetrievalService().getDetailTypeForConcept(businessEntityDefinition.getId());
         }
         manageIndexForms(businessEntityDefinition, parentBusinessEntityDefinition, typeBusinessEntityDefinition,
                  operationType, secondaryWordsWeightMap, entityDetailType);
      }
   }

   private Map<String, Double> buildSecondayWordWeightMap (Long modelId) throws KDXException {
      Map<Long, Map<String, Double>> secondaryWeightMapByModelId = new HashMap<Long, Map<String, Double>>(1);
      Map<String, Double> secondaryWordsWeightMap = secondaryWeightMapByModelId.get(modelId);
      if (secondaryWordsWeightMap == null) {
         secondaryWordsWeightMap = getKdxRetrievalService().getAllSecondaryWordsWeightMapForModel(modelId);
         secondaryWeightMapByModelId.put(modelId, secondaryWordsWeightMap);
      }
      List<String> baseRIontoWords = getBaseKDXRetrievalService().getSecondrayWordsByBaseModel();
      for (String baseRIontoWord : baseRIontoWords) {
         Double defaultWeightForBaseRIontoTermWords = getSwiConfigurationService().getDefaultWeightForBaseRIontoTermWords();
         secondaryWordsWeightMap.put(baseRIontoWord, defaultWeightForBaseRIontoTermWords);
      }
      return secondaryWordsWeightMap;
   }

   public void manageIndexForms (IndexFormManagementContext indexFormManagementContext) throws SWIException {
      JobOperationalStatus jobOperationalStatus = null;
      try {
         Long modelId = indexFormManagementContext.getModelId();

         // manage concepts
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(indexFormManagementContext
                  .getJobRequest(), "Fetching concepts for model", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         List<Long> addedConceptBEDIds = getBusinessEntityMaintenanceService().getBusinessEntityMaintenanceDetails(
                  modelId, OperationType.ADD, EntityType.CONCEPT);
         List<Long> updatedConceptBEDIds = getBusinessEntityMaintenanceService()
                  .getDistinctUpdatedEntityMaintenanceDetails(modelId, EntityType.CONCEPT);

         // build the information about the updated concepts which will be used later
         List<BusinessEntityDefinitionMaintenanceInfo> businessEntityMaintenanceInfoDetails = new ArrayList<BusinessEntityDefinitionMaintenanceInfo>();
         for (Long updatedConceptBedId : updatedConceptBEDIds) {
            BusinessEntityDefinition conceptBED = getKdxRetrievalService().getBusinessEntityDefinitionById(
                     updatedConceptBedId);
            BusinessEntityDefinition typeBED = getKdxRetrievalService().getPopulatedTypeBusinessEntityDefinition(
                     conceptBED.getType().getId());
            Long oldTypeBEDId = conceptBED.getId();
            String oldTypeName = typeBED.getType().getName();
            List<RIOntoTerm> ontoTerms = getKdxRetrievalService().getRIOntoTermsByBEDId(conceptBED.getId(),
                     BusinessEntityType.CONCEPT);
            if (ExecueCoreUtil.isCollectionNotEmpty(ontoTerms)) {
               RIOntoTerm ontoTerm = ontoTerms.get(0);
               oldTypeBEDId = ontoTerm.getTypeBEDID();
               oldTypeName = ontoTerm.getTypeName();
            }
            BusinessEntityDefinitionMaintenanceInfo businessEntityDefinitionMaintenanceInfo = new BusinessEntityDefinitionMaintenanceInfo();
            businessEntityDefinitionMaintenanceInfo.setConceptBedId(conceptBED.getId());
            businessEntityDefinitionMaintenanceInfo.setConceptId(conceptBED.getConcept().getId());
            businessEntityDefinitionMaintenanceInfo.setOldTypeBedId(oldTypeBEDId);
            businessEntityDefinitionMaintenanceInfo.setOldTypeName(oldTypeName);

            businessEntityDefinitionMaintenanceInfo.setNewTypeBedId(typeBED.getId());
            businessEntityDefinitionMaintenanceInfo.setNewTypeName(typeBED.getType().getName());
            businessEntityDefinitionMaintenanceInfo.setNewConceptName(conceptBED.getConcept().getName());
            businessEntityMaintenanceInfoDetails.add(businessEntityDefinitionMaintenanceInfo);
         }

         // if concept has been modified and created, we can consider it as only create for index forms for concepts
         updatedConceptBEDIds.removeAll(addedConceptBEDIds);

         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(indexFormManagementContext
                  .getJobRequest(), "Creating index farm for concepts", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         manageIndexForms(modelId, addedConceptBEDIds, null, OperationType.ADD);
         manageIndexForms(modelId, updatedConceptBEDIds, null, OperationType.UPDATE);
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         // manage concept profiles
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(indexFormManagementContext
                  .getJobRequest(), "Fetching concept profiles for model", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         List<Long> addedConceptProfileBEDIds = getBusinessEntityMaintenanceService()
                  .getBusinessEntityMaintenanceDetails(modelId, OperationType.ADD, EntityType.CONCEPT_PROFILE);
         List<Long> updatedConceptProfileBEDIds = getBusinessEntityMaintenanceService()
                  .getDistinctUpdatedEntityMaintenanceDetails(modelId, EntityType.CONCEPT_PROFILE);

         // if concept has been modified and created, we can consider it as only create.
         updatedConceptProfileBEDIds.removeAll(addedConceptProfileBEDIds);

         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(indexFormManagementContext
                  .getJobRequest(), "Creating index farm for concept profiles", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         manageIndexForms(modelId, addedConceptProfileBEDIds, null, OperationType.ADD);
         manageIndexForms(modelId, updatedConceptProfileBEDIds, null, OperationType.UPDATE);
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         // manage concept instances
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(indexFormManagementContext
                  .getJobRequest(), "Creating index farm for instances of model", JobStatus.INPROGRESS, null,
                  new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         List<Long> conceptsHavingInstances = getBusinessEntityMaintenanceService()
                  .getBusinessEntityMaintenanceParentDetails(modelId, EntityType.CONCEPT_LOOKUP_INSTANCE);
         boolean isRelativeOfLocation = false;
         for (Long conceptBEDId : conceptsHavingInstances) {
            // TODO: -JM- check if the concept is of location type
            // isRelativeOfLocation = getKdxRetrievalService().isRelativeOfLocation(conceptBEDId);
            // List<Long> addedSharedModelInstanceBEDIds = null;
            // List<Long> updatedSharedModelInstanceBEDIds = null;

            List<Long> addedInstanceBEDIds = getBusinessEntityMaintenanceService()
                     .getBusinessEntityMaintenanceDetailsByParentId(modelId, OperationType.ADD,
                              EntityType.CONCEPT_LOOKUP_INSTANCE, conceptBEDId);
            List<Long> updatedInstanceBEDIds = getBusinessEntityMaintenanceService()
                     .getBusinessEntityMaintenanceDetailsByParentId(modelId, OperationType.UPDATE,
                              EntityType.CONCEPT_LOOKUP_INSTANCE, conceptBEDId);

            // separate out the shared model instances from those belonging to the user model
            if (isRelativeOfLocation) {
               // get shared model instances
               // get non-shared model instances
            }

            // if concept has been modified and created, we can consider it as only create.
            updatedInstanceBEDIds.removeAll(addedInstanceBEDIds);

            manageIndexForms(modelId, addedInstanceBEDIds, conceptBEDId, OperationType.ADD);
            manageIndexForms(modelId, updatedInstanceBEDIds, conceptBEDId, OperationType.UPDATE);

            // TODO: -JM- handle shared model instances
            // add an entry into the ri_shared_user_model_mapping table for each shared model instance
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         // manage type instances
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(indexFormManagementContext
                  .getJobRequest(), "Creating index farm for type instances of model", JobStatus.INPROGRESS, null,
                  new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         List<Long> typeBedIdsHavingInstances = getBusinessEntityMaintenanceService()
                  .getBusinessEntityMaintenanceParentDetails(modelId, EntityType.TYPE_LOOKUP_INSTANCE);
         for (Long typeBEDId : typeBedIdsHavingInstances) {
            List<Long> addedTypeInstanceBEDIds = getBusinessEntityMaintenanceService()
                     .getBusinessEntityMaintenanceDetailsByParentId(modelId, OperationType.ADD,
                              EntityType.TYPE_LOOKUP_INSTANCE, typeBEDId);
            List<Long> updatedTypeInstanceBEDIds = getBusinessEntityMaintenanceService()
                     .getBusinessEntityMaintenanceDetailsByParentId(modelId, OperationType.UPDATE,
                              EntityType.TYPE_LOOKUP_INSTANCE, typeBEDId);

            // TODO: -JM- handle the same way for location as handled in "concept instances" block

            updatedTypeInstanceBEDIds.removeAll(addedTypeInstanceBEDIds);
            manageIndexForms(modelId, addedTypeInstanceBEDIds, typeBEDId, OperationType.ADD);
            manageIndexForms(modelId, updatedTypeInstanceBEDIds, typeBEDId, OperationType.UPDATE);

         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         // manage parallel words
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(indexFormManagementContext
                  .getJobRequest(), "Creating RI ParallelWords and SFLTerms for the ParallelWord",
                  JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         List<Long> addedParallelWordIds = getBusinessEntityMaintenanceService().getBusinessEntityMaintenanceDetails(
                  modelId, OperationType.ADD, EntityType.PARALLEL_WORD);
         List<Long> updatedParallelWordIds = getBusinessEntityMaintenanceService()
                  .getDistinctUpdatedEntityMaintenanceDetails(modelId, EntityType.PARALLEL_WORD);
         List<Long> addedUpdatedParallelWordIds = new ArrayList<Long>();
         addedUpdatedParallelWordIds.addAll(addedParallelWordIds);
         addedUpdatedParallelWordIds.addAll(updatedParallelWordIds);
         Map<String, Double> secondayWordWeightMap = buildSecondayWordWeightMap(modelId);
         for (Long parallelWordId : addedUpdatedParallelWordIds) {
            try {
               ParallelWord parallelWord = getPreferencesRetrievalService().getParallelWord(parallelWordId);
               KeyWord keyword = getPreferencesRetrievalService().getKeyWord(parallelWord.getKeyWord().getId());
               updateRIParallelWordForParallelWord(indexFormManagementContext.getUserId(), keyword.getId());
               getSflService().generateSFLTerm(
                        new EntityNameVariation(parallelWord.getParallelWord(), RecognitionType.Synonym),
                        keyword.getModelGroup().getId(), secondayWordWeightMap);
            } catch (SWIException swiException) {
               if (DataAccessExceptionCodes.ENTITY_NOT_FOUND_CODE != swiException.getCode()) {
                  throw swiException;
               }
            }
         }

         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         // NOTES : -RG-
         // In current model, in RI Onto Term, if there are any Instances with type different from their
         // concept
         // Then
         // If the concept type is not Time Frame, then simply set the type bedId and name of the concept
         // to all the instance's of that concept respectively
         // If the concept type is Time Frame, then drop all the RI entries for all the instances
         // of that concept and then recreate them afresh

         // Implementation Notes : -RG-
         // Get the concepts which has instances and are marked for updated since last publish
         // Separate them by of Time Frame type and not Time Frame type
         // For each of the non Time Frame Type concepts,
         // Set Concept Name, Type Name and Type BED ID properly for the instances of the concept
         // For Time Frame Type concepts,
         // Get all the Instances of the each concept and mark them as if they got updated and
         // execute manage index forms for all of them

         // Implementation
         // jobOperationalStatus =
         // ExecueBeanCloneUtil.prepareJobOperationalStatus(indexFormManagementContext.getJobRequest(),
         // "Adjusting index farm for instances for which Concepts got updated", JobStatus.INPROGRESS, null, new
         // Date());
         // getJobDataService().createJobOperationStatus(jobOperationalStatus);
         //         
         // Long modelGroupId = getKdxRetrievalService().getPrimaryGroup(modelId).getId();
         //         
         // List<BusinessEntityDefinition> updatedEnumerationConceptBEDs = getBusinessEntityMaintenanceService()
         // .getDistinctUpdatedMisMatchedEnumerationConceptBEDsFromBEMDetails(modelId);
         // for (BusinessEntityDefinition updatedEnumerationConceptBED : updatedEnumerationConceptBEDs) {
         // if (ExecueConstants.TIME_FRAME_TYPE.equalsIgnoreCase(updatedEnumerationConceptBED.getType().getName())) {
         // List<Long> updatedInstanceBEDIds =
         // getRiOntoTermsAbsorptionService().getExistingInstanceBEDIdInRIOntoTerms(modelId,
         // updatedEnumerationConceptBED.getId());
         // manageIndexForms(modelId, updatedInstanceBEDIds, updatedEnumerationConceptBED.getId(),
         // OperationType.UPDATE);
         // } else {
         // Long conceptBEDId = updatedEnumerationConceptBED.getId();
         // String conceptName = updatedEnumerationConceptBED.getConcept().getName();
         // BusinessEntityDefinition typeBED =
         // getKdxRetrievalService().getPopulatedTypeBusinessEntityDefinition(updatedEnumerationConceptBED.getType().getId());
         // Long typeBEDId = typeBED.getId();
         // String typeName = typeBED.getType().getName();
         // getRiOntoTermsAbsorptionService().updateInstanceRIOntoTermsWithConceptInfo(modelGroupId, conceptBEDId,
         // conceptName, typeBEDId, typeName);
         // }
         // }
         // jobOperationalStatus = ExecueBeanCloneUtil.modifyJobOperationalStatus(jobOperationalStatus,
         // JobStatus.SUCCESS,
         // null, new Date());
         // getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         // NOTES : -RG- Adjusting the type info and instance entries at RI Onto Term based on Concept's type change
         // If Concept type is TF now,
         // then take off the instance entries from RI Onto
         // If Concept type is not TF but earlier it is TF and a dimension (should have instances)
         // then make RI Onto entries for these instances
         // If the Concept is not TF now and not a TF earlier then just adjust type info on existing RI Onto entries

         // List<BusinessEntityDefinition> updatedEnumerationConceptBEDs = getBusinessEntityMaintenanceService()
         // .getDistinctUpdatedMisMatchedEnumerationConceptBEDsFromBEMDetails(modelId);
         //
         // for (BusinessEntityDefinition updatedEnumerationConceptBED : updatedEnumerationConceptBEDs) {
         // if (ExecueConstants.TIME_FRAME_TYPE.equalsIgnoreCase(updatedEnumerationConceptBED.getType().getName())) {
         // getRiOntoTermsAbsorptionService().deleteInstanceRIOntoTermsByConceptBEDId(modelGroupId,
         // updatedEnumerationConceptBED.getId());
         // } else {
         // List<Long> existingInstanceBEDIdsFromRIOntoTerm = getRiOntoTermsAbsorptionService()
         // .getExistingInstanceBEDIdInRIOntoTerms(modelId, updatedEnumerationConceptBED.getId());
         // if (ExecueCoreUtil.isCollectionEmpty(existingInstanceBEDIdsFromRIOntoTerm)) { // earlier this concept was a
         // // TF
         // // TODO : -RG- is there any better way of figuring out whether this concept is earlier a TF ??
         // List<Long> instanceBEDIds = getKdxRetrievalService().getInstanceBEDIdsByConceptId(modelId,
         // updatedEnumerationConceptBED.getConcept().getId());
         // manageIndexForms(modelId, instanceBEDIds, updatedEnumerationConceptBED.getId(), OperationType.ADD);
         // } else {
         // Long conceptBEDId = updatedEnumerationConceptBED.getId();
         // String conceptName = updatedEnumerationConceptBED.getConcept().getName();
         // BusinessEntityDefinition typeBED = getKdxRetrievalService().getPopulatedTypeBusinessEntityDefinition(
         // updatedEnumerationConceptBED.getType().getId());
         // Long typeBEDId = typeBED.getId();
         // String typeName = typeBED.getType().getName();
         // getRiOntoTermsAbsorptionService().updateInstanceRIOntoTermsWithConceptInfo(modelGroupId,
         // conceptBEDId, conceptName, typeBEDId, typeName);
         // }
         // }
         // }
         // jobOperationalStatus = ExecueBeanCloneUtil.modifyJobOperationalStatus(jobOperationalStatus,
         // JobStatus.SUCCESS,
         // null, new Date());
         // getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(indexFormManagementContext
                  .getJobRequest(), "Adjusting index farm for instances for which Concepts got updated",
                  JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);

         Long modelGroupId = getKdxRetrievalService().getPrimaryGroup(modelId).getId();

         for (BusinessEntityDefinitionMaintenanceInfo businessEntityDefinitionMaintenanceInfo : businessEntityMaintenanceInfoDetails) {
            if (ExecueConstants.TIME_FRAME_TYPE.equals(businessEntityDefinitionMaintenanceInfo.getNewTypeName())
                     && !ExecueConstants.TIME_FRAME_PROPERTY.equals(businessEntityDefinitionMaintenanceInfo
                              .getOldTypeName())) {
               // delete the existing onto terms.
               getKdxManagementService().deleteInstanceRIOntoTermsByConceptBEDId(modelGroupId,
                        businessEntityDefinitionMaintenanceInfo.getConceptBedId());
            } else if (ExecueConstants.TIME_FRAME_TYPE.equals(businessEntityDefinitionMaintenanceInfo.getOldTypeName())
                     && !ExecueConstants.TIME_FRAME_PROPERTY.equals(businessEntityDefinitionMaintenanceInfo
                              .getNewTypeName())) {
               // delete the existing ontoterms if any.
               getKdxManagementService().deleteInstanceRIOntoTermsByConceptBEDId(modelGroupId,
                        businessEntityDefinitionMaintenanceInfo.getConceptBedId());
               // create the onto terms for all the instances.
               List<Long> instanceBEDIds = getKdxRetrievalService().getInstanceBEDIdsByConceptId(modelId,
                        businessEntityDefinitionMaintenanceInfo.getConceptId());
               manageIndexForms(modelId, instanceBEDIds, businessEntityDefinitionMaintenanceInfo.getConceptBedId(),
                        OperationType.ADD);
            } else if (!ExecueConstants.TIME_FRAME_TYPE
                     .equals(businessEntityDefinitionMaintenanceInfo.getOldTypeName())
                     && !ExecueConstants.TIME_FRAME_PROPERTY.equals(businessEntityDefinitionMaintenanceInfo
                              .getNewTypeName())) {
               getKdxManagementService().updateInstanceRIOntoTermsWithConceptInfo(modelGroupId,
                        businessEntityDefinitionMaintenanceInfo.getConceptBedId(),
                        businessEntityDefinitionMaintenanceInfo.getNewConceptName(),
                        businessEntityDefinitionMaintenanceInfo.getNewTypeBedId(),
                        businessEntityDefinitionMaintenanceInfo.getNewTypeName());
            }
         }

         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         // manage instance profiles
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(indexFormManagementContext
                  .getJobRequest(), "Creating index farm for instances profiles of model", JobStatus.INPROGRESS, null,
                  new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
         List<Long> conceptsHavingInstanceProfiles = getBusinessEntityMaintenanceService()
                  .getBusinessEntityMaintenanceParentDetails(modelId, EntityType.INSTANCE_PROFILE);
         for (Long conceptBEDId : conceptsHavingInstanceProfiles) {
            List<Long> addedInstanceProfileBEDIds = getBusinessEntityMaintenanceService()
                     .getBusinessEntityMaintenanceDetailsByParentId(modelId, OperationType.ADD,
                              EntityType.INSTANCE_PROFILE, conceptBEDId);
            List<Long> updatedInstanceProfileBEDIds = getBusinessEntityMaintenanceService()
                     .getBusinessEntityMaintenanceDetailsByParentId(modelId, OperationType.UPDATE,
                              EntityType.INSTANCE_PROFILE, conceptBEDId);

            // if concept has been modified and created, we can consider it as only create.
            updatedInstanceProfileBEDIds.removeAll(addedInstanceProfileBEDIds);
            manageIndexForms(modelId, addedInstanceProfileBEDIds, conceptBEDId, OperationType.ADD);
            manageIndexForms(modelId, updatedInstanceProfileBEDIds, conceptBEDId, OperationType.UPDATE);
         }

         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         // manage relations
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(indexFormManagementContext
                  .getJobRequest(), "Creating index farm for relations of model", JobStatus.INPROGRESS, null,
                  new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);

         List<Long> addedRelationBEDIds = getBusinessEntityMaintenanceService().getBusinessEntityMaintenanceDetails(
                  modelId, OperationType.ADD, EntityType.RELATION);
         List<Long> updatedRelationBEDIds = getBusinessEntityMaintenanceService().getBusinessEntityMaintenanceDetails(
                  modelId, OperationType.UPDATE, EntityType.RELATION);

         // if concept has been modified and created, we can consider it as only create.
         updatedRelationBEDIds.removeAll(addedRelationBEDIds);
         manageIndexForms(modelId, addedRelationBEDIds, null, OperationType.ADD);
         manageIndexForms(modelId, updatedRelationBEDIds, null, OperationType.UPDATE);

         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         // create RI Cloud entries
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(indexFormManagementContext
                  .getJobRequest(), "Creating RI Cloud indexes for the model", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);

         getRiCloudsAbsorptionWrapperService().regenerateRICloudFromCloud(
                  getKdxCloudRetrievalService().getDefaultAppCloud(indexFormManagementContext.getModelId()),
                  indexFormManagementContext.getModelId());

         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
      } catch (ExeCueException e) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE,
                     e.getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new SWIException(SWIExceptionCodes.INDEX_FORM_CREATION_FAILED, e);
            }
         }
         throw new SWIException(SWIExceptionCodes.INDEX_FORM_CREATION_FAILED, e);
      }
   }

   public void updateRIParallelWordForParallelWord (Long userId, Long keyWordId) throws KDXException {
      try {
         User user = getUserManagementService().getUserById(userId);
         KeyWord keyWord = getPreferencesRetrievalService().getKeyWord(keyWordId);

         List<RIParallelWord> existingRIParallelWords = getPreferencesRetrievalService().getRIParallelWordsForKeyWord(
                  keyWordId);
         List<ParallelWord> existingParallelWords = getPreferencesRetrievalService().getParallelWordsForKeyWord(
                  keyWordId);
         List<RIParallelWord> toBeAddedRIParallelWords = new ArrayList<RIParallelWord>();
         List<RIParallelWord> toBeDeletedRIParallelWords = new ArrayList<RIParallelWord>();
         getRIParallelWordUpdations(existingRIParallelWords, existingParallelWords, toBeAddedRIParallelWords,
                  toBeDeletedRIParallelWords, keyWord, user);
         if (ExecueCoreUtil.isCollectionNotEmpty(toBeAddedRIParallelWords)) {
            getPreferencesManagementService().createRIParallelWords(toBeAddedRIParallelWords);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(toBeDeletedRIParallelWords)) {
            getPreferencesDeletionService().deleteRIParallelWords(toBeDeletedRIParallelWords);
         }
      } catch (Exception e) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   private void getRIParallelWordUpdations (List<RIParallelWord> existingRIParallelWords,
            List<ParallelWord> existingParallelWords, List<RIParallelWord> toBeAddedRIParallelWords,
            List<RIParallelWord> toBeDeletedRIParallelWords, KeyWord keyWord, User user) {
      List<RIParallelWord> populatedRIParallelWords = prepareRIParallelWords(existingParallelWords, keyWord, user);
      // populate the additions
      for (RIParallelWord populatedParallelWord : populatedRIParallelWords) {
         if (!isRIParallelWordFound(populatedParallelWord, existingRIParallelWords)) {
            toBeAddedRIParallelWords.add(populatedParallelWord);
         }
      }
      // populate the deletions
      for (RIParallelWord existingParallelWord : existingRIParallelWords) {
         if (!isRIParallelWordFound(existingParallelWord, populatedRIParallelWords)) {
            toBeDeletedRIParallelWords.add(existingParallelWord);
         }
      }
   }

   private boolean isRIParallelWordFound (RIParallelWord toBeMatchedRIParallelWord, List<RIParallelWord> riParallelWords) {
      boolean isFound = false;
      for (RIParallelWord riParallelWord : riParallelWords) {
         if (riParallelWord.getWord().equalsIgnoreCase(toBeMatchedRIParallelWord.getWord())
                  && riParallelWord.getEquivalentWord().equalsIgnoreCase(toBeMatchedRIParallelWord.getEquivalentWord())) {
            isFound = true;
            break;
         }
      }
      return isFound;
   }

   private List<RIParallelWord> prepareRIParallelWords (List<ParallelWord> existingParallelWords, KeyWord keyWord,
            User user) {
      List<RIParallelWord> riParallelWords = new ArrayList<RIParallelWord>();
      // generate riParallelWords from keyword to parallelwords and each parallelWord to keyword
      for (ParallelWord parallelWord : existingParallelWords) {

         RIParallelWord keyWordToParallelWord = populateRIParallelWord(keyWord.getWord(), parallelWord
                  .getParallelWord(), parallelWord.isPrefixSpace(), parallelWord.isSuffixSpace(), false, keyWord, user,
                  parallelWord);

         riParallelWords.add(keyWordToParallelWord);
         RIParallelWord parallelWordToKeyWord = populateRIParallelWord(parallelWord.getParallelWord(), keyWord
                  .getWord(), true, true, true, keyWord, user, parallelWord);
         parallelWordToKeyWord.setPreferedSelect(1);
         riParallelWords.add(parallelWordToKeyWord);
         for (ParallelWord parallelWord2 : existingParallelWords) {
            if (!parallelWord.getParallelWord().equalsIgnoreCase(parallelWord2.getParallelWord())
                     && parallelWord.getPwdType() == parallelWord2.getPwdType()) {
               RIParallelWord parallelWordToParallelWord = populateRIParallelWord(parallelWord.getParallelWord(),
                        parallelWord2.getParallelWord(), parallelWord2.isPrefixSpace(), parallelWord2.isSuffixSpace(),
                        false, keyWord, user, parallelWord);
               riParallelWords.add(parallelWordToParallelWord);
            }
         }
      }
      return riParallelWords;
   }

   private RIParallelWord populateRIParallelWord (String word, String equivalentWord, boolean prefixSpace,
            boolean suffixSpace, boolean isKeyword, KeyWord keyWord, User user, ParallelWord parallelWord) {
      RIParallelWord riParallelWord = new RIParallelWord();
      riParallelWord.setWord(word);
      riParallelWord.setEquivalentWord(equivalentWord);
      riParallelWord.setPrefixSpace(prefixSpace);
      riParallelWord.setSuffixSpace(suffixSpace);
      riParallelWord.setKeyWord(isKeyword);
      riParallelWord.setKeyWordId(keyWord.getId());
      // TODO :-VG- need to have a look at table defaults
      riParallelWord.setHits(parallelWord.getPopularity());
      riParallelWord.setQuality(0.85); // NK: Default value for quality is now 0.85
      riParallelWord.setPosType(parallelWord.getPosType());
      riParallelWord.setPwdType(parallelWord.getPwdType());
      riParallelWord.setIsDifferentWord(parallelWord.getIsDifferentWord());
      riParallelWord.setUser(user);

      if (keyWord.getBusinessEntityDefinition() != null) {
         riParallelWord.setBedId(keyWord.getBusinessEntityDefinition().getId());
      }
      if (keyWord.getModelGroup() != null) {
         riParallelWord.setModelGroupId(keyWord.getModelGroup().getId());
      }
      return riParallelWord;
   }

   public IndexFormManagementHelper getIndexFormManagementHelper () {
      return indexFormManagementHelper;
   }

   public void setIndexFormManagementHelper (IndexFormManagementHelper indexFormManagementHelper) {
      this.indexFormManagementHelper = indexFormManagementHelper;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the jobDataService
    */
   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   /**
    * @param jobDataService
    *           the jobDataService to set
    */
   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public ISFLService getSflService () {
      return sflService;
   }

   public void setSflService (ISFLService sflService) {
      this.sflService = sflService;
   }

   /**
    * @return the kdxCloudRetrievalService
    */
   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   /**
    * @param kdxCloudRetrievalService
    *           the kdxCloudRetrievalService to set
    */
   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

   public IBusinessEntityMaintenanceService getBusinessEntityMaintenanceService () {
      return businessEntityMaintenanceService;
   }

   public void setBusinessEntityMaintenanceService (IBusinessEntityMaintenanceService businessEntityMaintenanceService) {
      this.businessEntityMaintenanceService = businessEntityMaintenanceService;
   }

   /**
    * @return the userManagementService
    */
   public IUserManagementService getUserManagementService () {
      return userManagementService;
   }

   /**
    * @param userManagementService
    *           the userManagementService to set
    */
   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   /**
    * @return the preferencesRetrievalService
    */
   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return preferencesRetrievalService;
   }

   /**
    * @param preferencesRetrievalService
    *           the preferencesRetrievalService to set
    */
   public void setPreferencesRetrievalService (IPreferencesRetrievalService preferencesRetrievalService) {
      this.preferencesRetrievalService = preferencesRetrievalService;
   }

   /**
    * @return the preferencesManagementService
    */
   public IPreferencesManagementService getPreferencesManagementService () {
      return preferencesManagementService;
   }

   /**
    * @param preferencesManagementService
    *           the preferencesManagementService to set
    */
   public void setPreferencesManagementService (IPreferencesManagementService preferencesManagementService) {
      this.preferencesManagementService = preferencesManagementService;
   }

   /**
    * @return the kdxManagementService
    */
   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   /**
    * @param kdxManagementService
    *           the kdxManagementService to set
    */
   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
   }

   public IPreferencesDeletionService getPreferencesDeletionService () {
      return preferencesDeletionService;
   }

   public void setPreferencesDeletionService (IPreferencesDeletionService preferencesDeletionService) {
      this.preferencesDeletionService = preferencesDeletionService;
   }

   public void manageIndexForms (Long modelId, Long businessEntityDefinitionId, Long parentBedId,
            OperationType operationType) throws SWIException {
      List<Long> businessEntityDefinitionIds = new ArrayList<Long>();
      businessEntityDefinitionIds.add(businessEntityDefinitionId);
      manageIndexForms(modelId, businessEntityDefinitionIds, parentBedId, operationType);
   }

   public IRIOntoTermAbsorbtionService getRiOntoTermAbsorbtionService () {
      return riOntoTermAbsorbtionService;
   }

   public void setRiOntoTermAbsorbtionService (IRIOntoTermAbsorbtionService riOntoTermAbsorbtionService) {
      this.riOntoTermAbsorbtionService = riOntoTermAbsorbtionService;
   }

   public IRICloudsAbsorptionWrapperService getRiCloudsAbsorptionWrapperService () {
      return riCloudsAbsorptionWrapperService;
   }

   public void setRiCloudsAbsorptionWrapperService (IRICloudsAbsorptionWrapperService riCloudsAbsorptionWrapperService) {
      this.riCloudsAbsorptionWrapperService = riCloudsAbsorptionWrapperService;
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }
}
