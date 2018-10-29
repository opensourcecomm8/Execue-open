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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.TypeConceptAssociationInfo;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.PossibleAttribute;
import com.execue.core.common.bean.entity.PossibleDetailType;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.OriginType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIAttribute;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIPathDefinition;
import com.execue.handler.bean.UIRealization;
import com.execue.handler.swi.ITypeConceptAssociationServiceHandler;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.swi.IBusinessEntityManagementWrapperService;
import com.execue.platform.swi.IConceptTypeAssociationService;
import com.execue.swi.configuration.ILocationConfigurationService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathDefinitionRetrievalService;

public class TypeConceptAssociationServiceHandlerImpl implements ITypeConceptAssociationServiceHandler {

   private IKDXRetrievalService                    kdxRetrievalService;
   private IBusinessEntityManagementWrapperService businessEntityManagementWrapperService;
   private IKDXCloudRetrievalService               kdxCloudRetrievalService;
   private IConceptTypeAssociationService          conceptTypeAssociationService;
   private IPathDefinitionRetrievalService         pathDefinitionRetrievalService;
   private IBaseKDXRetrievalService                baseKDXRetrievalService;
   private ISWIConfigurationService                swiConfigurationService;
   private ILocationConfigurationService           locationConfigurationService;

   public ILocationConfigurationService getLocationConfigurationService () {
      return locationConfigurationService;
   }

   public void setLocationConfigurationService (ILocationConfigurationService locationConfigurationService) {
      this.locationConfigurationService = locationConfigurationService;
   }

   public Type getTypeByName (String typeName) throws HandlerException {
      Type type = null;
      try {
         type = getKdxRetrievalService().getTypeByName(typeName);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
      return type;
   }

   public List<Type> getAllTypes () throws HandlerException {
      List<Type> types = new ArrayList<Type>();
      try {
         types = getKdxRetrievalService().getAllTypes();
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
      return types;
   }

   public List<UIConcept> getRealizedConceptsForType (Long typeId, Long modelId) throws HandlerException {
      List<UIConcept> uiConcepts = new ArrayList<UIConcept>();
      try {
         List<BusinessEntityDefinition> realizedConcepts = getKdxRetrievalService()
                  .getPopulatedRealizationsForTypeInModelIncludingBaseGroup(typeId, modelId);
         if (ExecueCoreUtil.isCollectionNotEmpty(realizedConcepts)) {
            uiConcepts = transformRealizedConcepts(realizedConcepts);
         }
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
      return uiConcepts;
   }

   public List<Type> getAttributesToBeRealizedForType (Long typeId) throws HandlerException {
      List<Type> attributesToBeRealized = new ArrayList<Type>();
      try {
         BusinessEntityDefinition typeBusinessEntityDefinition = getKdxRetrievalService()
                  .getTypeBusinessEntityDefinition(typeId);
         List<PossibleAttribute> possibleAttributes = getKdxRetrievalService().getAllPossibleAttributes(
                  typeBusinessEntityDefinition.getId());
         attributesToBeRealized = populateAttributesToBeRealized(possibleAttributes);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
      return attributesToBeRealized;
   }

   public void associateTypeConcepts (Long modelId, Type type, List<UIConcept> concepts, Type typeToBeRealized,
            List<UIConcept> realizedConcepts) throws HandlerException {
      try {
         Cloud cloud = getKdxCloudRetrievalService().getDefaultAppCloud(modelId);
         BusinessEntityDefinition bedType = getKdxRetrievalService().getPopulatedTypeBusinessEntityDefinition(
                  type.getId());
         for (UIConcept uiConcept : concepts) {
            BusinessEntityDefinition conceptBed = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                     uiConcept.getId(), null);
            Map<Long, List<EntityTripleDefinition>> attributePathMap = new HashMap<Long, List<EntityTripleDefinition>>();
            List<PossibleAttribute> possibleAttributes = getKdxRetrievalService().getAllPossibleAttributes(
                     bedType.getId());
            for (PossibleAttribute possibleAttribute : possibleAttributes) {
               List<EntityTripleDefinition> attributePaths = new ArrayList<EntityTripleDefinition>();
               if (possibleAttribute.getComponentTypeBed().getEntityType() == BusinessEntityType.REALIZED_TYPE) {
                  EntityTripleDefinition etd = ExecueBeanManagementUtil.prepareEntityTripleDefinition(conceptBed,
                           possibleAttribute.getRelationBed(), possibleAttribute.getComponentTypeBed(),
                           EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
                  etd.setOrigin(OriginType.TYPE);
                  attributePaths.add(etd);
               } else {
                  if (typeToBeRealized != null && typeToBeRealized.getId() != null
                           && ExecueCoreUtil.isCollectionNotEmpty(realizedConcepts)) {
                     List<BusinessEntityDefinition> realizations = populateBusinessEntityDefinitions(realizedConcepts,
                              modelId);
                     for (BusinessEntityDefinition destination : realizations) {
                        EntityTripleDefinition etd = ExecueBeanManagementUtil.prepareEntityTripleDefinition(conceptBed,
                                 possibleAttribute.getRelationBed(), destination,
                                 EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
                        etd.setOrigin(OriginType.TYPE);
                        attributePaths.add(etd);
                     }
                  }
               }
               if (ExecueCoreUtil.isCollectionNotEmpty(attributePaths)) {
                  attributePathMap.put(possibleAttribute.getComponentTypeBed().getId(), attributePaths);
               }
            }
            TypeConceptAssociationInfo typeConceptInfo = ExecueBeanManagementUtil.populateTypeConceptInfo(conceptBed,
                     bedType, cloud, modelId, null, attributePathMap, null, true, false, false, null, null, false,
                     false, false);
            getConceptTypeAssociationService().assignConceptType(typeConceptInfo);
         }
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public void associateConceptType (Long modelId, Long typeId, Long conceptBEDId, List<BehaviorType> behaviorTypes,
            List<UIPathDefinition> pathDefinitions, List<BehaviorType> savedTypeBehaviors,
            List<UIPathDefinition> savedPathDefinitions, boolean create, OriginType originType,
            Long selectedDetailTypeId, boolean isAdvanceSave) throws HandlerException {
      try {
         Cloud cloud = getKdxCloudRetrievalService().getDefaultAppCloud(modelId);
         BusinessEntityDefinition typeBED = getKdxRetrievalService().getPopulatedTypeBusinessEntityDefinition(typeId);
         BusinessEntityDefinition sourceBED = getKdxRetrievalService().getBusinessEntityDefinitionById(conceptBEDId);
         boolean isSameType = typeBED.getId().equals(sourceBED.getType().getId());
         BusinessEntityDefinition detailTypeBed = null;
         if (selectedDetailTypeId != null) {
            detailTypeBed = getTypeById(selectedDetailTypeId);
         }
         Map<Long, List<EntityTripleDefinition>> attributePathMap = populateAttributeMap(modelId, sourceBED,
                  pathDefinitions, originType);
         TypeConceptAssociationInfo typeConceptInfo = null;
         if (isSameType) {
            typeConceptInfo = ExecueBeanManagementUtil.populateTypeConceptInfo(sourceBED, typeBED, cloud, modelId,
                     behaviorTypes, attributePathMap, detailTypeBed, true, true, true, savedTypeBehaviors,
                     getPathDefinitionIdsList(savedPathDefinitions), true, true, false);
         } else {
            // re-assign type
            boolean typeChangedFromLocationToNonLocation = false;
            boolean originalTypeLocation = getLocationConfigurationService().isLocationType(sourceBED.getId());
            boolean newTypeLocation = getLocationConfigurationService().isLocationType(typeBED.getId());
            if (originalTypeLocation && !newTypeLocation) {
               typeChangedFromLocationToNonLocation = true;
            }
            typeConceptInfo = ExecueBeanManagementUtil.populateTypeConceptInfo(sourceBED, typeBED, cloud, modelId,
                     behaviorTypes, attributePathMap, detailTypeBed, true, true, true, null, null, false, false,
                     typeChangedFromLocationToNonLocation);
         }
         typeConceptInfo.setAdvanceSave(isAdvanceSave);
         getConceptTypeAssociationService().assignConceptType(typeConceptInfo);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e.getMessage());
      }
   }

   public void associateCRC (Long modelId, BusinessEntityDefinition sourceBED,
            List<UIPathDefinition> selectedCRCPathDefinitions, List<UIPathDefinition> savedCRCPathDefinitions)
            throws HandlerException {
      try {
         List<EntityTripleDefinition> etdList = populateETDs(modelId, sourceBED, selectedCRCPathDefinitions,
                  OriginType.USER);
         getConceptTypeAssociationService().assignCRC(modelId, getPathDefinitionIdsList(savedCRCPathDefinitions),
                  etdList);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e.getMessage());
      }
   }

   private List<Long> getPathDefinitionIdsList (List<UIPathDefinition> pathDefinitions) {
      List<Long> pathDefinitionIds = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(pathDefinitions)) {
         for (UIPathDefinition pathDefinition : pathDefinitions) {
            pathDefinitionIds.add(pathDefinition.getPathDefinitionId());
         }
      }
      return pathDefinitionIds;
   }

   private Map<Long, List<EntityTripleDefinition>> populateAttributeMap (Long modelId,
            BusinessEntityDefinition sourceBED, List<UIPathDefinition> pathDefinitions, OriginType originType)
            throws KDXException {
      Map<Long, List<EntityTripleDefinition>> attributePathMap = new HashMap<Long, List<EntityTripleDefinition>>();
      if (ExecueCoreUtil.isCollectionNotEmpty(pathDefinitions)) {
         for (UIPathDefinition pathDefinition : pathDefinitions) {
            EntityTripleDefinition currentETD = getETD(modelId, sourceBED, originType, pathDefinition,
                     EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
            if (attributePathMap.keySet().contains(pathDefinition.getAttributeBedId())) {
               List<EntityTripleDefinition> etds = attributePathMap.get(pathDefinition.getAttributeBedId());
               etds.add(currentETD);
            } else {
               List<EntityTripleDefinition> etds = new ArrayList<EntityTripleDefinition>();
               etds.add(currentETD);
               attributePathMap.put(pathDefinition.getAttributeBedId(), etds);
            }
         }
      }
      return attributePathMap;
   }

   private List<EntityTripleDefinition> populateETDs (Long modelId, BusinessEntityDefinition sourceBED,
            List<UIPathDefinition> pathDefinitions, OriginType originType) throws KDXException {
      List<EntityTripleDefinition> etdList = new ArrayList<EntityTripleDefinition>(1);
      EntityTripleDefinitionType etdType = EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT;
      if (ExecueCoreUtil.isCollectionNotEmpty(pathDefinitions)) {
         for (UIPathDefinition pathDefinition : pathDefinitions) {
            EntityTripleDefinition currentETD = getCrcETD(modelId, sourceBED, originType, pathDefinition, etdType);
            // TODO: JT: Validate ETD for cyclic paths
            etdList.add(currentETD);
         }
      }
      return etdList;
   }

   private EntityTripleDefinition getCrcETD (Long modelId, BusinessEntityDefinition sourceBED, OriginType originType,
            UIPathDefinition pathDefinition, EntityTripleDefinitionType etdType) throws KDXException {
      BusinessEntityDefinition destinationBED = getKdxRetrievalService().getBusinessEntityDefinitionById(
               pathDefinition.getDestinationConceptBedId());
      EntityTripleDefinition currentETD = prepareETD(modelId, sourceBED, originType, pathDefinition, etdType,
               destinationBED);
      return currentETD;
   }

   /**
    * @param modelId
    * @param sourceBED
    * @param originType
    * @param pathDefinition
    * @return
    * @throws KDXException
    */
   private EntityTripleDefinition getETD (Long modelId, BusinessEntityDefinition sourceBED, OriginType originType,
            UIPathDefinition pathDefinition, EntityTripleDefinitionType etdType) throws KDXException {
      BusinessEntityDefinition destinationBED = getKdxRetrievalService().getBusinessEntityDefinitionById(
               pathDefinition.getDestinationConceptBedId());
      EntityTripleDefinition currentETD = prepareETD(modelId, sourceBED, originType, pathDefinition, etdType,
               destinationBED);
      return currentETD;
   }

   /**
    * @param modelId
    * @param sourceBED
    * @param originType
    * @param pathDefinition
    * @param etdType
    * @param destinationBED
    * @return
    * @throws KDXException
    */
   private EntityTripleDefinition prepareETD (Long modelId, BusinessEntityDefinition sourceBED, OriginType originType,
            UIPathDefinition pathDefinition, EntityTripleDefinitionType etdType, BusinessEntityDefinition destinationBED)
            throws KDXException {
      EntityTripleDefinition currentETD = null;
      try {
         BusinessEntityDefinition relationBED = null;
         String relationName = pathDefinition.getRelationName();
         if (pathDefinition.getRelationBedId() != null) {
            relationBED = getKdxRetrievalService().getBusinessEntityDefinitionById(pathDefinition.getRelationBedId());
         } else {
            // TODO: -JM- first normalize the name which user enters and then check - revisit the relation logic
            // check if any relation with the same name exists or not before creating the new relation
            Relation swiRelation = getKdxRetrievalService().getRelationByName(modelId, relationName, true);
            if (swiRelation != null) {
               // TODO: -JM- refactor the code to get the BED in the first call itself
               // first check in base
               relationBED = getBaseKDXRetrievalService().getRelationBEDByName(relationName);
               if (relationBED == null) {
                  relationBED = getKdxRetrievalService().getRelationBEDByName(modelId, relationName);
               }
            } else {
               Relation relation = new Relation();
               relation.setName(relationName);
               relation.setDisplayName(relationName);
               relationBED = getBusinessEntityManagementWrapperService().createRelation(modelId, relation);
            }
         }
         currentETD = ExecueBeanManagementUtil.prepareEntityTripleDefinition(sourceBED, relationBED, destinationBED,
                  etdType);
         currentETD.setOrigin(originType);
      } catch (PlatformException e) {
         throw new KDXException(e.getCode(), e);
      }
      return currentETD;
   }

   public List<UIAttribute> getPossibleAttributes (Long typeId) throws HandlerException {
      try {
         BusinessEntityDefinition typeBED = getKdxRetrievalService().getTypeBusinessEntityDefinition(typeId);
         List<PossibleAttribute> possibleAttributes = getKdxRetrievalService()
                  .getAllPossibleAttributes(typeBED.getId());
         return transformUIAttribute(possibleAttributes);
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
   }

   public List<BehaviorType> getAllPossibleBehaviors (Long typeId) throws HandlerException {
      try {
         BusinessEntityDefinition typeBED = getKdxRetrievalService().getTypeBusinessEntityDefinition(typeId);
         return getKdxRetrievalService().getAllPossibleBehavior(typeBED.getId());
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
   }

   public List<BehaviorType> getAllBehaviors () {
      return Arrays.asList(BehaviorType.values());
   }

   public List<UIRealization> getAllAttributes (Long modelId, Long typeId) throws HandlerException {
      try {
         List<BusinessEntityDefinition> bealizationBeds = getKdxRetrievalService().getAllAttributes(modelId, typeId);
         return transformUIAttributeFromBeds(bealizationBeds);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }

   }

   public List<BehaviorType> getAllEntityBehaviors (Long entityBedId) throws HandlerException {
      try {
         return kdxRetrievalService.getAllBehaviorForEntity(entityBedId);
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
   }

   public List<UIPathDefinition> getAllEntityDirectPaths (Long modelId, BusinessEntityDefinition entityBed,
            EntityTripleDefinitionType entityTripleDefinitionType) throws HandlerException {
      try {
         List<PathDefinition> pathDefinitions = getPathDefinitionRetrievalService().getAllDirectPaths(modelId,
                  entityBed.getId(), entityTripleDefinitionType);
         return transformPathDefinitions(entityBed, pathDefinitions);
      } catch (SWIException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
   }

   private List<UIRealization> transformUIAttributeFromBeds (List<BusinessEntityDefinition> bealizationBeds)
            throws KDXException {
      List<UIRealization> uiRealizations = new ArrayList<UIRealization>();
      if (ExecueCoreUtil.isCollectionNotEmpty(bealizationBeds)) {
         for (BusinessEntityDefinition bed : bealizationBeds) {
            UIRealization uiRealization = new UIRealization();
            uiRealization.setRealizationBedId(bed.getId());
            uiRealization.setRealizationName(bed.getConcept().getDisplayName());
            uiRealization.setRealizationId(bed.getConcept().getId());
            uiRealization.setEntityType(bed.getEntityType());
            uiRealization.setAttributeName(bed.getType().getName());
            uiRealization.setAttributeBedId(getKdxRetrievalService().getTypeBusinessEntityDefinition(
                     bed.getType().getId()).getId());
            uiRealizations.add(uiRealization);
         }
      }
      return uiRealizations;
   }

   private List<UIAttribute> transformUIAttribute (List<PossibleAttribute> possibleAttributes) {
      List<UIAttribute> uiAttributes = new ArrayList<UIAttribute>();
      if (ExecueCoreUtil.isCollectionNotEmpty(possibleAttributes)) {
         for (PossibleAttribute attribute : possibleAttributes) {
            UIAttribute uiAttribute = new UIAttribute();
            uiAttribute.setAttributeName(attribute.getComponentTypeBed().getType().getDisplayName());
            uiAttribute.setAttributeTypeId(attribute.getComponentTypeBed().getType().getId());
            uiAttribute.setAttributeBedId(attribute.getComponentTypeBed().getId());
            uiAttribute.setRelationName(attribute.getRelationBed().getRelation().getName());
            uiAttribute.setRelationBedId(attribute.getRelationBed().getId());
            CheckType required = CheckType.YES;
            if (CheckType.YES.equals(attribute.getOptional())) {
               required = CheckType.NO;
            }
            uiAttribute.setIsRequired(required);
            uiAttribute.setMultipleRealizations(attribute.getMultipleRealizations());
            uiAttribute.setEntityType(attribute.getComponentTypeBed().getEntityType());
            uiAttributes.add(uiAttribute);
         }
      }
      return uiAttributes;
   }

   private List<UIPathDefinition> transformPathDefinitions (BusinessEntityDefinition entityBed,
            List<PathDefinition> pathDefinitions) throws KDXException {
      List<UIPathDefinition> uiPathDefinitions = new ArrayList<UIPathDefinition>();
      if (ExecueCoreUtil.isCollectionNotEmpty(pathDefinitions)) {
         for (PathDefinition pathDefinition : pathDefinitions) {
            List<Path> paths = new ArrayList<Path>(pathDefinition.getPaths());
            if (ExecueCoreUtil.isCollectionNotEmpty(paths)) {
               EntityTripleDefinition etd = paths.get(0).getEntityTripleDefinition();
               UIPathDefinition uiPathDefinition = new UIPathDefinition();
               uiPathDefinition.setPathDefinitionId(pathDefinition.getId());
               uiPathDefinition.setSourceConceptBedId(etd.getSourceBusinessEntityDefinition().getId());
               if (BusinessEntityType.CONCEPT.equals(etd.getSourceBusinessEntityDefinition().getEntityType())) {
                  uiPathDefinition.setSourceConceptName(etd.getSourceBusinessEntityDefinition().getConcept().getName());
               } else {
                  // TODO: -JM- handle the concept profile case
                  // ignore the current path definition and continue
                  continue;
               }
               uiPathDefinition.setDestinationConceptBedId(etd.getDestinationBusinessEntityDefinition().getId());
               uiPathDefinition.setDestinationConceptName(etd.getDestinationBusinessEntityDefinition().getConcept()
                        .getName());
               uiPathDefinition.setRelationName(etd.getRelation().getRelation().getName());
               uiPathDefinition.setRelationBedId(etd.getRelation().getId());
               // populate with the destination's type bed id
               Type type = etd.getDestinationBusinessEntityDefinition().getType();
               BusinessEntityDefinition typeBed = getKdxRetrievalService()
                        .getTypeBusinessEntityDefinition(type.getId());
               uiPathDefinition.setAttributeBedId(typeBed.getId());
               // populate the type name only for Type Entity
               if (BusinessEntityType.TYPE.equals(typeBed.getEntityType())) {
                  uiPathDefinition.setAttributeName(type.getName());
               }
               uiPathDefinitions.add(uiPathDefinition);
            }
         }
      }
      return uiPathDefinitions;
   }

   private List<BusinessEntityDefinition> populateBusinessEntityDefinitions (List<UIConcept> uiConcepts, Long modelId)
            throws KDXException {
      List<BusinessEntityDefinition> businessEntityDefinitions = new ArrayList<BusinessEntityDefinition>();
      for (UIConcept uiConcept : uiConcepts) {
         businessEntityDefinitions.add(getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                  uiConcept.getId(), null));
      }
      return businessEntityDefinitions;
   }

   private List<UIConcept> transformRealizedConcepts (List<BusinessEntityDefinition> businessEntityDefinitions) {
      List<UIConcept> uiConcepts = new ArrayList<UIConcept>();
      for (BusinessEntityDefinition businessEntityDefinition : businessEntityDefinitions) {
         UIConcept uiConcept = new UIConcept();
         uiConcept.setId(businessEntityDefinition.getConcept().getId());
         uiConcept.setBedId(businessEntityDefinition.getId());
         uiConcept.setDisplayName(businessEntityDefinition.getConcept().getDisplayName());
         uiConcepts.add(uiConcept);
      }
      return uiConcepts;
   }

   private List<Type> populateAttributesToBeRealized (List<PossibleAttribute> possibleAttributes) {
      List<Type> attributesToBeRealized = new ArrayList<Type>();
      for (PossibleAttribute possibleAttribute : possibleAttributes) {
         if (possibleAttribute.getComponentTypeBed().getEntityType() == BusinessEntityType.TYPE) {
            attributesToBeRealized.add(possibleAttribute.getComponentTypeBed().getType());
         }
      }
      return attributesToBeRealized;
   }

   public BusinessEntityDefinition getBusinessEntityDefinition (Long modelId, Long conceptId) throws HandlerException {
      try {
         return getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId, conceptId, null);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<Relation> getRelationSuggestions (Long modelId, String searchString) throws HandlerException {
      try {
         // TODO: -JM- get the max results from configuration
         int maxResults = 10;
         return kdxRetrievalService.suggestRelationsByName(modelId, searchString, maxResults);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<UIRealization> getAllNonAttributeConcepts (Long modelId) throws HandlerException {
      try {
         List<BusinessEntityDefinition> nonAttributeBeds = getKdxRetrievalService().getAllNonAttributeEntities(modelId);
         return transformUIAttributeFromBeds(nonAttributeBeds);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }

   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

   public IConceptTypeAssociationService getConceptTypeAssociationService () {
      return conceptTypeAssociationService;
   }

   public void setConceptTypeAssociationService (IConceptTypeAssociationService conceptTypeAssociationService) {
      this.conceptTypeAssociationService = conceptTypeAssociationService;
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   public BusinessEntityDefinition getTypeById (Long typeId) throws HandlerException {
      try {
         return getKdxRetrievalService().getPopulatedTypeBusinessEntityDefinition(typeId);
      } catch (KDXException e) {
         e.printStackTrace();
      }
      return null;
   }

   public BusinessEntityDefinition getDetailTypeForConcept (Long conceptBedId) throws HandlerException {
      try {
         return getKdxRetrievalService().getDetailTypeForConcept(conceptBedId).getDetailTypeBed();
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<PossibleDetailType> getPossibleDetailTypes (Long typeBedId) throws HandlerException {
      try {
         return getKdxRetrievalService().getPossibleDetailTypes(typeBedId);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<Type> getAllVisibleTypes () throws HandlerException {
      List<Type> types = null;
      try {
         types = getKdxRetrievalService().getAllVisibleTypes();
         if (CollectionUtils.isEmpty(types)) {
            types = new ArrayList<Type>();

         }
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
      return types;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public List<String> getBaseRelationsName () {
      return getSwiConfigurationService().getBaseRelations();
   }

   public IPathDefinitionRetrievalService getPathDefinitionRetrievalService () {
      return pathDefinitionRetrievalService;
   }

   public void setPathDefinitionRetrievalService (IPathDefinitionRetrievalService pathDefinitionRetrievalService) {
      this.pathDefinitionRetrievalService = pathDefinitionRetrievalService;
   }

   public IBusinessEntityManagementWrapperService getBusinessEntityManagementWrapperService () {
      return businessEntityManagementWrapperService;
   }

   public void setBusinessEntityManagementWrapperService (
            IBusinessEntityManagementWrapperService businessEntityManagementWrapperService) {
      this.businessEntityManagementWrapperService = businessEntityManagementWrapperService;
   }
}
