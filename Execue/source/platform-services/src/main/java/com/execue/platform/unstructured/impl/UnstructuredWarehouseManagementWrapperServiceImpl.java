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


package com.execue.platform.unstructured.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.unstructured.Feature;
import com.execue.core.common.bean.entity.unstructured.FeatureValue;
import com.execue.core.common.bean.entity.unstructured.SemantifiedContentFeatureInformation;
import com.execue.core.common.bean.swi.UnstructuredWarehouseContext;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.unstructured.IUnstructuredFacetManagementService;
import com.execue.platform.unstructured.IUnstructuredWarehouseManagementWrapperService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.service.IUnstructuredWarehouseManagementService;
import com.execue.uswh.service.IUnstructuredWarehouseRetrievalService;

/**
 * @author Vishay
 */
public class UnstructuredWarehouseManagementWrapperServiceImpl implements
         IUnstructuredWarehouseManagementWrapperService {

   private IBusinessEntityMaintenanceService       businessEntityMaintenanceService;
   private IKDXRetrievalService                    kdxRetrievalService;
   private IApplicationRetrievalService            applicationRetrievalService;
   private IUnstructuredWarehouseRetrievalService  unstructuredWarehouseRetrievalService;
   private IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService;
   private IUnstructuredFacetManagementService     unstructuredFacetManagementService;

   public void manageUnstructuredWarehouse (UnstructuredWarehouseContext unstructuredWarehouseContext)
            throws PlatformException {
      try {
         Long modelId = unstructuredWarehouseContext.getModelId();
         Long appId = unstructuredWarehouseContext.getApplicationId();

         List<Long> addedConceptBEDIds = getBusinessEntityMaintenanceService().getBusinessEntityMaintenanceDetails(
                  modelId, OperationType.ADD, EntityType.CONCEPT);
         List<Long> updatedConceptBEDIds = getBusinessEntityMaintenanceService()
                  .getDistinctUpdatedEntityMaintenanceDetails(modelId, EntityType.CONCEPT);
         updatedConceptBEDIds.removeAll(addedConceptBEDIds);
         createFeatures(appId, addedConceptBEDIds);
         updateFeatures(appId, updatedConceptBEDIds);
         List<Long> conceptsHavingInstances = getBusinessEntityMaintenanceService()
                  .getBusinessEntityMaintenanceParentDetails(modelId, EntityType.CONCEPT_LOOKUP_INSTANCE);
         for (Long conceptBEDId : conceptsHavingInstances) {
            List<Long> addedInstanceBEDIds = getBusinessEntityMaintenanceService()
                     .getBusinessEntityMaintenanceDetailsByParentId(modelId, OperationType.ADD,
                              EntityType.CONCEPT_LOOKUP_INSTANCE, conceptBEDId);
            List<Long> updatedInstanceBEDIds = getBusinessEntityMaintenanceService()
                     .getBusinessEntityMaintenanceDetailsByParentId(modelId, OperationType.UPDATE,
                              EntityType.CONCEPT_LOOKUP_INSTANCE, conceptBEDId);

            // if concept has been modified and created, we can consider it as only create.
            updatedInstanceBEDIds.removeAll(addedInstanceBEDIds);
            createFeatureValues(appId, conceptBEDId, addedInstanceBEDIds);
            updateFeatureValues(appId, updatedInstanceBEDIds);
         }
      } catch (UnstructuredWarehouseException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }

   }

   private Feature populateFeature (Long appId, Long conceptBedId, FeatureValueType featureValueType, Concept concept)
            throws KDXException {
      boolean hasConceptMultiValued = getKdxRetrievalService().isConceptMatchedBehavior(conceptBedId,
               BehaviorType.MULTIVALUED);
      Feature feature = new Feature();
      feature.setContextId(appId);
      feature.setFeatureName(concept.getName());
      feature.setFeatureDisplayName(concept.getDisplayName());
      feature.setFeatureValueType(featureValueType);
      feature.setFeatureBedId(conceptBedId);
      feature.setMultiValued(ExecueBeanUtil.getCorrespondingCheckTypeValue(hasConceptMultiValued));
      return feature;
   }

   private boolean isTimeFrameType (Long bedId) throws KDXException {
      return getKdxRetrievalService().isMatchedBusinessEntityType(bedId, ExecueConstants.TIME_FRAME_TYPE);
   }

   private boolean isMeasurableEntityType (Long bedId) throws KDXException {
      return getKdxRetrievalService().isMatchedBusinessEntityType(bedId, ExecueConstants.MEASURABLE_ENTITY_TYPE);
   }

   private boolean isEnumBehaviorType (Long bedId) throws KDXException {
      boolean hasEnumBehaviorType = false;
      List<BehaviorType> behaviorTypes = getKdxRetrievalService().getAllBehaviorForEntity(bedId);
      for (BehaviorType behaviorType : behaviorTypes) {
         if (BehaviorType.ENUMERATION == behaviorType) {
            hasEnumBehaviorType = true;
            break;
         }
      }
      return hasEnumBehaviorType;
   }

   private FeatureValueType getFeatureValueType (boolean isTimeFrameType, boolean isEnumBehaviorType,
            boolean isMeasurableEntityType) {
      FeatureValueType featureValueType = FeatureValueType.VALUE_STRING;
      if (isMeasurableEntityType || isTimeFrameType) {
         featureValueType = FeatureValueType.VALUE_NUMBER;
      }
      return featureValueType;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IUnstructuredWarehouseManagementService#deleteApplicationInfo(java.lang.Long)
    */
   public void deleteApplicationInfo (Long applicationId) throws PlatformException {
      try {
         List<Feature> features = getUnstructuredWarehouseRetrievalService().getFeaturesByContextId(applicationId);
         if (ExecueCoreUtil.isCollectionNotEmpty(features)) {
            getUnstructuredWarehouseManagementService().deleteFeatures(applicationId, features);
         }
      } catch (UnstructuredWarehouseException e) {
         throw new PlatformException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateImagePresentForFacets (Long appId, Long semantifiedContentId, CheckType imagePresent) throws PlatformException {
      try {
         List<SemantifiedContentFeatureInformation> featureInfoList = getUnstructuredWarehouseRetrievalService()
                  .getSemantifiedContentFeatureInfoBySemantifiedContentId(appId, semantifiedContentId);

         for (SemantifiedContentFeatureInformation semantifiedContentFeatureInformation : featureInfoList) {
            semantifiedContentFeatureInformation.setImagePresent(imagePresent);
         }

         getUnstructuredFacetManagementService().updateFacetInfo(appId, semantifiedContentId, featureInfoList);
      } catch (UnstructuredWarehouseException uswhException) {
         throw new PlatformException(uswhException.getCode(), uswhException);
      }
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

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService
    *           the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   @Override
   public void manageUnstructuredWarehouse (Long appId, Long bedId, Long parentBedId, OperationType operationType)
            throws PlatformException {
      try {
         if (getApplicationRetrievalService().isUnstructuredWHManagementRequiredByAppSourceType(appId)) {
            if (parentBedId == null) {
               handleFeatures(appId, bedId, operationType);
            } else {
               handleFeatureValues(appId, bedId, parentBedId, operationType);
            }
         }
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (UnstructuredWarehouseException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   private void handleFeatures (Long appId, Long bedId, OperationType operationType) throws SWIException,
            UnstructuredWarehouseException {
      if (OperationType.ADD.equals(operationType)) {
         createFeature(appId, bedId);
      } else if (OperationType.UPDATE.equals(operationType)) {
         updateFeature(appId, bedId);
      } else if (OperationType.DELETE.equals(operationType)) {
         deleteFeature(appId, bedId);
      }
   }

   private void updateFeature (Long appId, Long conceptBedId) throws SWIException, UnstructuredWarehouseException {
      List<Long> conceptBedIds = new ArrayList<Long>();
      conceptBedIds.add(conceptBedId);
      updateFeatures(appId, conceptBedIds);
   }

   private void updateFeatures (Long appId, List<Long> conceptBEDIds) throws SWIException,
            UnstructuredWarehouseException {
      boolean isTypeChange;
      boolean isNameChange;
      List<Feature> featuresToDeleted = new ArrayList<Feature>();
      List<Feature> featuresToAdded = new ArrayList<Feature>();
      List<Feature> featuresToUpdated = new ArrayList<Feature>();
      for (Long conceptBedId : conceptBEDIds) {
         Feature existingFeature = getUnstructuredWarehouseRetrievalService().getFeatureByFeatureBEDID(appId,
                  conceptBedId);
         Concept concept = getKdxRetrievalService().getBusinessEntityDefinitionById(conceptBedId).getConcept();
         boolean isTimeFrameType = isTimeFrameType(conceptBedId);
         boolean isMeasurableEntityType = isMeasurableEntityType(conceptBedId);
         boolean isEnumBehaviorType = isEnumBehaviorType(conceptBedId);

         // If any of the below behavior is not present, then add to delete list
         if (!(isTimeFrameType || isMeasurableEntityType || isEnumBehaviorType)) {
            if (existingFeature != null) {
               featuresToDeleted.add(existingFeature);
            }
            continue;
         }

         FeatureValueType featureValueType = getFeatureValueType(isTimeFrameType, isEnumBehaviorType,
                  isMeasurableEntityType);

         // If existing feature is null, then first create it
         if (existingFeature == null) {
            Feature feature = populateFeature(appId, conceptBedId, featureValueType, concept);
            featuresToAdded.add(feature);
            continue;
         }

         isTypeChange = featureValueType == existingFeature.getFeatureValueType() ? false : true;
         isNameChange = concept.getName().equals(existingFeature.getFeatureName()) ? false : true;
         if (isTypeChange) {
            List<Feature> features = new ArrayList<Feature>();
            features.add(existingFeature);
            featuresToDeleted.add(existingFeature);
            Feature feature = new Feature();
            feature.setContextId(appId);
            feature.setFeatureName(concept.getName());
            feature.setFeatureDisplayName(concept.getDisplayName());
            feature.setFeatureValueType(featureValueType);
            feature.setFeatureBedId(conceptBedId);
            featuresToAdded.add(feature);
         }
         if (isNameChange && !isTypeChange) {
            existingFeature.setFeatureName(concept.getName());
            existingFeature.setFeatureDisplayName(concept.getDisplayName());
            featuresToUpdated.add(existingFeature);
         }

      }
      // In future once we have dynamic range, then should also delete the feature range entries in dao
      getUnstructuredWarehouseManagementService().deleteFeatures(appId, featuresToDeleted);
      getUnstructuredWarehouseManagementService().createFeatures(appId, featuresToAdded);
      getUnstructuredWarehouseManagementService().updateFeatures(appId, featuresToUpdated);

   }

   // TODO - VG- need to clean up feature udx info table also.
   private void deleteFeature (Long appId, Long conceptBedId) throws UnstructuredWarehouseException {
      Feature existingFeature = getUnstructuredWarehouseRetrievalService()
               .getFeatureByFeatureBEDID(appId, conceptBedId);
      if (existingFeature != null) {
         getUnstructuredWarehouseManagementService().deleteFeature(appId, existingFeature);
      }
   }

   private void createFeature (Long appId, Long conceptBedId) throws SWIException, UnstructuredWarehouseException {
      Feature feature = prepareFeature(appId, conceptBedId);
      if (feature != null) {
         getUnstructuredWarehouseManagementService().createFeature(appId, feature);
      }
   }

   private void createFeatures (Long appId, List<Long> conceptBedIds) throws SWIException,
            UnstructuredWarehouseException {
      List<Feature> features = new ArrayList<Feature>();
      for (Long conceptBedId : conceptBedIds) {
         Feature feature = prepareFeature(appId, conceptBedId);
         if (feature != null) {
            features.add(feature);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(features)) {
         getUnstructuredWarehouseManagementService().createFeatures(appId, features);
      }
   }

   private Feature prepareFeature (Long appId, Long conceptBedId) throws SWIException {
      boolean isTimeFrameType = isTimeFrameType(conceptBedId);
      boolean isMeasurableEntityType = isMeasurableEntityType(conceptBedId);
      boolean isEnumBehaviorType = isEnumBehaviorType(conceptBedId);
      // If any of the below behavior is not present, then continue, skip creation
      if (!(isTimeFrameType || isMeasurableEntityType || isEnumBehaviorType)) {
         return null;
      }
      Concept concept = getKdxRetrievalService().getBusinessEntityDefinitionById(conceptBedId).getConcept();
      Feature feature = populateFeature(appId, conceptBedId, getFeatureValueType(isTimeFrameType, isEnumBehaviorType,
               isMeasurableEntityType), concept);
      return feature;
   }

   private void handleFeatureValues (Long appId, Long bedId, Long parentBedId, OperationType operationType)
            throws SWIException, UnstructuredWarehouseException {
      if (OperationType.ADD.equals(operationType)) {
         createFeatureValue(appId, parentBedId, bedId);
      } else if (OperationType.UPDATE.equals(operationType)) {
         updateFeatureValue(appId, bedId);
      } else if (OperationType.DELETE.equals(operationType)) {
         deleteFeatureValue(appId, bedId);
      }
   }

   private void deleteFeatureValue (Long appId, Long instanceBedId) throws UnstructuredWarehouseException {
      FeatureValue featureValue = getUnstructuredWarehouseRetrievalService().getFeatureValueByFeatureValueBEDID(appId,
               instanceBedId);
      getUnstructuredWarehouseManagementService().deleteFeatureValue(appId, featureValue);
   }

   private void updateFeatureValue (Long appId, Long instanceBedId) throws SWIException, UnstructuredWarehouseException {
      FeatureValue updatedFeatureValue = prepareUpdatedFeature(appId, instanceBedId);
      if (updatedFeatureValue != null) {
         getUnstructuredWarehouseManagementService().updateFeatureValue(appId, updatedFeatureValue);
      }
   }

   private void updateFeatureValues (Long appId, List<Long> updatedInstanceBEDIds) throws SWIException,
            UnstructuredWarehouseException {
      List<FeatureValue> updatedFeatureValues = new ArrayList<FeatureValue>();
      for (Long instanceBEDId : updatedInstanceBEDIds) {
         FeatureValue updatedFeature = prepareUpdatedFeature(appId, instanceBEDId);
         if (updatedFeature != null) {
            updatedFeatureValues.add(updatedFeature);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(updatedFeatureValues)) {
         getUnstructuredWarehouseManagementService().updateFeatureValues(appId, updatedFeatureValues);
      }
   }

   private FeatureValue prepareUpdatedFeature (Long appId, Long instanceBedId) throws KDXException,
            UnstructuredWarehouseException {
      FeatureValue updatedFeatureValue = null;
      Instance instance = getKdxRetrievalService().getBusinessEntityDefinitionById(instanceBedId).getInstance();
      FeatureValue featureValue = getUnstructuredWarehouseRetrievalService().getFeatureValueByFeatureValueBEDID(appId,
               instanceBedId);
      if (!featureValue.getFeatureValue().equals(instance.getName())) {
         featureValue.setFeatureValue(instance.getDisplayName());
         updatedFeatureValue = featureValue;
      }
      return updatedFeatureValue;
   }

   private void createFeatureValue (Long appId, Long conceptBedId, Long instanceBedId) throws SWIException,
            UnstructuredWarehouseException {
      FeatureValue featureValue = prepareFeatureValue(appId, conceptBedId, instanceBedId);
      if (featureValue != null) {
         getUnstructuredWarehouseManagementService().createFeatureValue(appId, featureValue);
      }
   }

   private void createFeatureValues (Long appId, Long conceptBEDID, List<Long> addedInstanceBEDIds)
            throws SWIException, UnstructuredWarehouseException {
      List<FeatureValue> featureValues = new ArrayList<FeatureValue>();
      for (Long instanceBedId : addedInstanceBEDIds) {
         FeatureValue featureValue = prepareFeatureValue(appId, conceptBEDID, instanceBedId);
         if (featureValue != null) {
            featureValues.add(featureValue);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(featureValues)) {
         getUnstructuredWarehouseManagementService().createFeatureValues(appId, featureValues);
      }
   }

   private FeatureValue prepareFeatureValue (Long appId, Long conceptBEDID, Long instanceBEDId) throws SWIException,
            UnstructuredWarehouseException {
      FeatureValue featureValue = null;
      if (isTimeFrameType(conceptBEDID) || isMeasurableEntityType(conceptBEDID) || isEnumBehaviorType(conceptBEDID)) {
         Feature feature = getUnstructuredWarehouseRetrievalService().getFeatureByFeatureBEDID(appId, conceptBEDID);
         Instance instance = getKdxRetrievalService().getBusinessEntityDefinitionById(instanceBEDId).getInstance();
         featureValue = new FeatureValue();
         featureValue.setFeatureId(feature.getFeatureId());
         featureValue.setFeatureValue(instance.getDisplayName());
         featureValue.setFeatureValueBeId(instanceBEDId);

      }
      return featureValue;
   }

   @Override
   public void manageUnstructuredWarehouse (Long appId, List<Long> bedIds, Long parentBedId, OperationType operationType)
            throws PlatformException {
      try {
         if (getApplicationRetrievalService().isUnstructuredWHManagementRequiredByAppSourceType(appId)) {
            for (Long bedId : bedIds) {
               manageUnstructuredWarehouse(appId, bedId, parentBedId, operationType);
            }
         }
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

   /**
    * @return the unstructuredWarehouseRetrievalService
    */
   public IUnstructuredWarehouseRetrievalService getUnstructuredWarehouseRetrievalService () {
      return unstructuredWarehouseRetrievalService;
   }

   /**
    * @param unstructuredWarehouseRetrievalService the unstructuredWarehouseRetrievalService to set
    */
   public void setUnstructuredWarehouseRetrievalService (
            IUnstructuredWarehouseRetrievalService unstructuredWarehouseRetrievalService) {
      this.unstructuredWarehouseRetrievalService = unstructuredWarehouseRetrievalService;
   }

   /**
    * @return the unstructuredWarehouseManagementService
    */
   public IUnstructuredWarehouseManagementService getUnstructuredWarehouseManagementService () {
      return unstructuredWarehouseManagementService;
   }

   /**
    * @param unstructuredWarehouseManagementService the unstructuredWarehouseManagementService to set
    */
   public void setUnstructuredWarehouseManagementService (
            IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService) {
      this.unstructuredWarehouseManagementService = unstructuredWarehouseManagementService;
   }

   public IUnstructuredFacetManagementService getUnstructuredFacetManagementService () {
      return unstructuredFacetManagementService;
   }

   public void setUnstructuredFacetManagementService (
            IUnstructuredFacetManagementService unstructuredFacetManagementService) {
      this.unstructuredFacetManagementService = unstructuredFacetManagementService;
   }

}
