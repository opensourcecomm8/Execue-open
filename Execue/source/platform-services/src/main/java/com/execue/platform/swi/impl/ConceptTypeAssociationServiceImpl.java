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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.AttributeInfo;
import com.execue.core.common.bean.ConceptTypeAssociationContext;
import com.execue.core.common.bean.ConceptTypeAssociationInfo;
import com.execue.core.common.bean.ConceptTypeInfo;
import com.execue.core.common.bean.TypeConceptAssociationInfo;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Conversion;
import com.execue.core.common.bean.entity.EntityDetailType;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.PossibleAttribute;
import com.execue.core.common.bean.entity.Rule;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.type.OriginType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.swi.IConceptTypeAssociationService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IConversionService;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXDeletionService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathDefinitionDeletionService;
import com.execue.swi.service.IPathDefinitionManagementService;
import com.execue.swi.service.IPathDefinitionRetrievalService;
import com.thoughtworks.xstream.XStream;

/**
 * @author John Mallavalli
 */
public class ConceptTypeAssociationServiceImpl implements IConceptTypeAssociationService {

   private IKDXRetrievalService              kdxRetrievalService;
   private IPathDefinitionRetrievalService   pathDefinitionRetrievalService;
   private IPathDefinitionManagementService  pathDefinitionManagementService;
   private IPathDefinitionDeletionService    pathDefinitionDeletionService;
   private IKDXManagementService             kdxManagementService;
   private IKDXCloudRetrievalService         kdxCloudRetrievalService;
   private IBusinessEntityMaintenanceService businessEntityMaintenanceService;
   private IBaseKDXRetrievalService          baseKDXRetrievalService;
   private IJobDataService                   jobDataService;
   private IConversionService                conversionService;
   private IKDXDeletionService               kdxDeletionService;
   private static final Logger               logger = Logger.getLogger(ConceptTypeAssociationServiceImpl.class);

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

   public void assignConceptType (TypeConceptAssociationInfo typeConceptAssociationInfo) throws KDXException {
      try {
         // get the modelId
         Model model = getKdxRetrievalService().getModelById(typeConceptAssociationInfo.getModelId());
         getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
                  ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(typeConceptAssociationInfo
                           .getSourceBed().getId(), ExecueBeanUtil
                           .getCorrespondingEntityType(typeConceptAssociationInfo.getSourceBed().getEntityType()),
                           model.getId(), OperationType.UPDATE, null));
         // sequence here is very imp. as unit and format method use detail type and attribute path info
         // so if it is running from publisher flow which means user has not added them we have to set them back to bean
         // so that this method can use it.
         deleteExistingBehaviorAndPathDefinitions(model, typeConceptAssociationInfo);
         createBehaviorAndPathDefinitions(typeConceptAssociationInfo);
         if (!typeConceptAssociationInfo.isAdvanceSave()) {
            updateDetailTypeForEntity(typeConceptAssociationInfo);
            if (BusinessEntityType.CONCEPT.equals(typeConceptAssociationInfo.getSourceBed().getEntityType())) {
               updateUnitFormatConceptInfo(model, typeConceptAssociationInfo);
            }
            updateTypeInBEDAndCloudComponent(model, typeConceptAssociationInfo);
         }
         if (typeConceptAssociationInfo.isTypeChangedFromLocationToNonLocation()) {
            // delete shared mappings for this concept.
         }
      } catch (SWIException swiException) {
         swiException.printStackTrace();
      }
   }

   public void assignCRC (Long modelId, List<Long> previousCRCPathDefIds, List<EntityTripleDefinition> crcETDs)
            throws KDXException {
      try {
         Cloud cloud = getKdxCloudRetrievalService().getDefaultAppCloud(modelId);

         // Delete the existing CRC Paths
         if (ExecueCoreUtil.isCollectionNotEmpty(previousCRCPathDefIds)) {
            for (Long pathDefinitionId : previousCRCPathDefIds) {
               getPathDefinitionDeletionService().deleteDirectPath(
                        getPathDefinitionRetrievalService().getPathDefinitionById(pathDefinitionId));
            }
         }

         // Create the CRC Paths
         for (EntityTripleDefinition etd : crcETDs) {
            Set<Rule> attributeRules = new HashSet<Rule>();
            getPathDefinitionManagementService().createPathDefinition(cloud.getId(), etd, attributeRules);
         }
      } catch (SWIException swiException) {
         swiException.printStackTrace();
      }
   }

   private void updateUnitFormatConceptInfo (Model model, TypeConceptAssociationInfo typeConceptAssociationInfo)
            throws SWIException {
      Type type = typeConceptAssociationInfo.getBedType().getType();
      Type srcType = typeConceptAssociationInfo.getSourceBed().getType();
      Concept concept = typeConceptAssociationInfo.getSourceBed().getConcept();
      if (!type.getId().equals(srcType.getId())) {
         concept.setDefaultDataFormat(null);
         concept.setDefaultUnit(null);
      }
      if (ExecueConstants.TIME_FRAME_TYPE.equals(type.getName())) {
         concept.setDefaultConversionType(ConversionType.DATE);
         if (CheckType.YES.equals(typeConceptAssociationInfo.getBedType().getType().getAbstrat())) {
            concept.setDefaultUnit(typeConceptAssociationInfo.getDetailTypeBed().getType().getName());
         }
      } else if (ExecueConstants.MEASURABLE_ENTITY_TYPE.equals(type.getName())) {
         Map<Long, List<EntityTripleDefinition>> attributePaths = typeConceptAssociationInfo.getAttributePaths();
         List<EntityTripleDefinition> attributePathlist = attributePaths.get(ExecueConstants.VALUE_TYPE_BED_ID);
         if (ExecueCoreUtil.isCollectionNotEmpty(attributePathlist)) {
            String conceptName = attributePathlist.get(0).getDestinationBusinessEntityDefinition().getConcept()
                     .getName().toUpperCase();
            ConversionType conversionType = ConversionType.getType(conceptName);
            concept.setDefaultConversionType(conversionType);
            if (concept.getDefaultDataFormat() == null) {
               Conversion numberConversion = getConversionService().getBaseConversion(ConversionType.NUMBER);
               concept.setDefaultDataFormat(numberConversion.getFormat());
            }
            if (concept.getDefaultUnit() == null) {
               Conversion unitConversion = getConversionService().getBaseConversion(conversionType);
               concept.setDefaultUnit(unitConversion.getUnit());
            }
         }
      } else {
         concept.setDefaultConversionType(ConversionType.DEFAULT);
      }
      getKdxManagementService().updateConcept(model.getId(), concept);

   }

   public void updateDetailTypeForEntity (TypeConceptAssociationInfo typeConceptAssociationInfo) throws KDXException {
      getKdxDeletionService().deleteEntityDetailTypeByConcept(typeConceptAssociationInfo.getSourceBed().getId());
      if (CheckType.YES.equals(typeConceptAssociationInfo.getBedType().getType().getAbstrat())) {
         BusinessEntityDefinition detailTypeBed = null;
         if (typeConceptAssociationInfo.isDetailTypeProvided()) {
            detailTypeBed = typeConceptAssociationInfo.getDetailTypeBed();
         } else {
            detailTypeBed = getKdxRetrievalService().getDefaultPopulatedDetailType(
                     typeConceptAssociationInfo.getBedType().getId());
            typeConceptAssociationInfo.setDetailTypeBed(detailTypeBed);
         }
         EntityDetailType entityDetailType = new EntityDetailType(typeConceptAssociationInfo.getSourceBed(),
                  detailTypeBed);
         getKdxManagementService().createEntityDetailType(entityDetailType);
      }
   }

   private void updateTypeInBEDAndCloudComponent (Model model, TypeConceptAssociationInfo typeConceptAssociationInfo)
            throws KDXException {
      BusinessEntityDefinition sourceBED = typeConceptAssociationInfo.getSourceBed();
      BusinessEntityDefinition typeBED = typeConceptAssociationInfo.getBedType();
      List<BehaviorType> behaviorTypes = new ArrayList<BehaviorType>();
      if (!CollectionUtils.isEmpty(typeConceptAssociationInfo.getBehaviorTypes())) {
         behaviorTypes.addAll(typeConceptAssociationInfo.getBehaviorTypes());
      }

      // change the business entity type for the concept & its instances
      if (!typeBED.getType().getId().equals(sourceBED.getType().getId())) {
         getKdxManagementService().updateConceptAndInstancesType(model.getId(), typeBED, sourceBED);
         // cloud component type change
         getKdxManagementService().updateCloudComponentsType(typeConceptAssociationInfo.getCloud().getId(), typeBED,
                  sourceBED, behaviorTypes.contains(BehaviorType.ATTRIBUTE));
      }
   }

   private void createBehaviorAndPathDefinitions (TypeConceptAssociationInfo typeConceptAssociationInfo)
            throws KDXException {
      // if behavior is not specified, then get the type behavior
      List<BehaviorType> behaviorTypes = typeConceptAssociationInfo.getBehaviorTypes();
      if (!typeConceptAssociationInfo.isBehaviorProvided()) {
         behaviorTypes = getKdxRetrievalService().getAllPossibleBehavior(
                  typeConceptAssociationInfo.getBedType().getId());
      }
      // create behavior
      getKdxManagementService().createEntityBehaviors(typeConceptAssociationInfo.getSourceBed().getId(), behaviorTypes);

      Map<Long, List<EntityTripleDefinition>> attributePathMap = typeConceptAssociationInfo.getAttributePaths();
      if (ExecueCoreUtil.isMapEmpty(attributePathMap)) {
         attributePathMap = new HashMap<Long, List<EntityTripleDefinition>>();
      }
      // if attributes are not specified, then get the type attributes
      if (!typeConceptAssociationInfo.isAttributesProvided()) {
         attributePathMap.putAll(prepareTypeRealizationsMap(typeConceptAssociationInfo.getModelId(),
                  typeConceptAssociationInfo.getBedType().getId(), typeConceptAssociationInfo.getSourceBed()));
         typeConceptAssociationInfo.setAttributePaths(attributePathMap);
      }
      // create paths
      createPathDefinitions(typeConceptAssociationInfo.getCloud().getId(), typeConceptAssociationInfo.getBedType(),
               typeConceptAssociationInfo.getSourceBed(), attributePathMap);
   }

   private void deleteExistingBehaviorAndPathDefinitions (Model model,
            TypeConceptAssociationInfo typeConceptAssociationInfo) throws SWIException {
      if (typeConceptAssociationInfo.isPreviousBehaviorTypesProvided()) {
         if (ExecueCoreUtil.isCollectionNotEmpty(typeConceptAssociationInfo.getPreviousBehaviorTypes())) {
            getKdxDeletionService().deleteEntityBehaviors(typeConceptAssociationInfo.getSourceBed().getId(),
                     typeConceptAssociationInfo.getPreviousBehaviorTypes());
         }
      } else {
         getKdxDeletionService().deleteEntityBehavior(typeConceptAssociationInfo.getSourceBed().getId());
      }

      if (typeConceptAssociationInfo.isPreviousAttributePathDefinitionIdsProvided()) {
         if (ExecueCoreUtil.isCollectionNotEmpty(typeConceptAssociationInfo.getPreviousAttributePathDefinitionIds())) {
            for (Long pathDefinitionId : typeConceptAssociationInfo.getPreviousAttributePathDefinitionIds()) {
               getPathDefinitionDeletionService().deleteDirectPath(
                        getPathDefinitionRetrievalService().getPathDefinitionById(pathDefinitionId));
            }
         }
      } else {
         List<EntityTripleDefinition> triplesToBeDeleted = getPathDefinitionRetrievalService()
                  .getAllTypeOriginPathsBySourceId(typeConceptAssociationInfo.getSourceBed().getId(), model.getId());
         for (EntityTripleDefinition tripleDefinition : triplesToBeDeleted) {
            getPathDefinitionDeletionService().deleteEntityTripleDefinition(tripleDefinition);
         }
      }

   }

   private Map<Long, List<EntityTripleDefinition>> prepareTypeRealizationsMap (Long modelId, Long typeBedId,
            BusinessEntityDefinition conceptBED) throws KDXException {
      Map<Long, List<EntityTripleDefinition>> attributePathMap = new HashMap<Long, List<EntityTripleDefinition>>();
      // Get All possible attributes for a TYPE like MeasurableEntity will have timeframe, statistics and value
      List<PossibleAttribute> possibleAttributes = getKdxRetrievalService().getAllPossibleAttributes(typeBedId);
      List<EntityTripleDefinition> attributePaths = new ArrayList<EntityTripleDefinition>();
      // Iterate over possible attributes
      for (PossibleAttribute possibleAttribute : possibleAttributes) {
         // Get destination type be id
         Long typeId = possibleAttribute.getComponentTypeBed().getType().getId();
         attributePaths = new ArrayList<EntityTripleDefinition>();
         // If the attribute destination is Realized-Type its used as it is
         // Otherwise default realization of the type is picked
         if (possibleAttribute.getComponentTypeBed().getEntityType() == BusinessEntityType.REALIZED_TYPE) {
            EntityTripleDefinition etd = ExecueBeanManagementUtil.prepareEntityTripleDefinition(conceptBED,
                     possibleAttribute.getRelationBed(), possibleAttribute.getComponentTypeBed(),
                     EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
            // Set the origin as TYPE
            // TODO --AP-- Why is this variable required ?
            etd.setOrigin(OriginType.TYPE);
            // Add attribute path to be added
            attributePaths.add(etd);
         } else {
            // Get Default realization for the type
            List<BusinessEntityDefinition> realizations = new ArrayList<BusinessEntityDefinition>();
            // Get Default realization and if its null then get all realizations
            if (possibleAttribute.getDefaultRealizationBedId() != null) {
               realizations.add(getKdxRetrievalService().getBusinessEntityDefinitionById(
                        possibleAttribute.getDefaultRealizationBedId()));
            } else {
               realizations = getKdxRetrievalService().getRealizationsForTypeInModelIncludingBaseGroup(typeId, modelId);
            }
            for (BusinessEntityDefinition destination : realizations) {
               EntityTripleDefinition etd = ExecueBeanManagementUtil.prepareEntityTripleDefinition(conceptBED,
                        possibleAttribute.getRelationBed(), destination,
                        EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
               attributePaths.add(etd);
               etd.setOrigin(OriginType.TYPE);
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(attributePaths)) {
            attributePathMap.put(possibleAttribute.getComponentTypeBed().getId(), attributePaths);
         }
      }
      return attributePathMap;
   }

   private void createPathDefinitions (Long cloudId, BusinessEntityDefinition typeBED,
            BusinessEntityDefinition conceptBED, Map<Long, List<EntityTripleDefinition>> attributePathMap)
            throws KDXException {
      if (ExecueCoreUtil.isMapNotEmpty(attributePathMap)) {
         for (Long attributeId : attributePathMap.keySet()) {
            for (EntityTripleDefinition etd : attributePathMap.get(attributeId)) {
               PossibleAttribute possibleAttribute = getKdxRetrievalService().getPossibleAttribute(typeBED.getId(),
                        attributeId, etd.getRelation().getId());
               Set<Rule> attributeRules = new HashSet<Rule>();
               if (possibleAttribute != null) {
                  attributeRules.addAll(possibleAttribute.getRules());
               }
               getPathDefinitionManagementService().createPathDefinition(cloudId, etd, attributeRules);
            }
         }
      }
   }

   public void absorbConceptTypeAssociations (ConceptTypeAssociationContext conceptTypeAssociationContext)
            throws KDXException {
      JobOperationalStatus jobOperationalStatus = null;
      try {
         ConceptTypeAssociationInfo conceptTypeAssociationInfo = parseConceptAssociationXMLFile(conceptTypeAssociationContext
                  .getFilePath());
         Long modelId = getKdxRetrievalService().getModelByName(conceptTypeAssociationInfo.getModelName()).getId();

         for (ConceptTypeInfo conceptTypeInfo : conceptTypeAssociationInfo.getConcepts()) {

            BusinessEntityDefinition conceptBED = getKdxRetrievalService().getBusinessEntityDefinitionByNames(
                     conceptTypeAssociationInfo.getModelName(), conceptTypeInfo.getName(), null);
            BusinessEntityDefinition typeBED = getKdxRetrievalService().getTypeBedByName(conceptTypeInfo.getType());

            logger.debug("Concept Name to Process :" + conceptTypeInfo.getName());
            logger.debug("Type Name to process :" + conceptTypeInfo.getType());
            if (conceptTypeAssociationContext.getJobRequest() != null) {
               jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(
                        conceptTypeAssociationContext.getJobRequest(), "Associating concept : "
                                 + conceptTypeInfo.getName() + " with type : " + conceptTypeInfo.getType(),
                        JobStatus.INPROGRESS, null, new Date());
               jobDataService.createJobOperationStatus(jobOperationalStatus);
            }
            try {

               // realizations map.
               Map<Long, List<EntityTripleDefinition>> attributePathMap = new HashMap<Long, List<EntityTripleDefinition>>();

               // if attributes are not empty.
               if (ExecueCoreUtil.isCollectionNotEmpty(conceptTypeInfo.getAttributes())) {
                  // for each attribute, get the typeBEDId, check if it needs to be realized then realizations must be
                  // given
                  for (AttributeInfo attribute : conceptTypeInfo.getAttributes()) {
                     List<EntityTripleDefinition> attributePaths = new ArrayList<EntityTripleDefinition>();
                     BusinessEntityDefinition attributeTypeBed = null;
                     if (ExecueCoreUtil.isCollectionNotEmpty(attribute.getRealizations())
                              || attribute.getRealizations() != null) {
                        attributeTypeBed = getKdxRetrievalService().getTypeBedByName(attribute.getName());
                     } else {
                        attributeTypeBed = getKdxRetrievalService().getTypeBedByName(attribute.getName(), true);
                     }

                     PossibleAttribute possibleAttribute = getKdxRetrievalService().getPossibleAttribute(
                              typeBED.getId(), attributeTypeBed.getId());
                     if (ExecueCoreUtil.isCollectionEmpty(attribute.getRealizations())) {
                        if (attributeTypeBed.getEntityType() == BusinessEntityType.REALIZED_TYPE) {
                           EntityTripleDefinition etd = ExecueBeanManagementUtil.prepareEntityTripleDefinition(
                                    conceptBED, possibleAttribute.getRelationBed(), attributeTypeBed,
                                    EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
                           etd.setOrigin(OriginType.TYPE);
                           attributePaths.add(etd);
                        } else {
                           logger.debug("Realizations needs to be given for Non Realized Type ");
                        }
                     } else {
                        for (String realization : attribute.getRealizations()) {
                           BusinessEntityDefinition realizationBED = getKdxRetrievalService().getBEDByRealizationName(
                                    modelId, realization);
                           EntityTripleDefinition etd = ExecueBeanManagementUtil.prepareEntityTripleDefinition(
                                    conceptBED, possibleAttribute.getRelationBed(), realizationBED,
                                    EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
                           etd.setOrigin(OriginType.TYPE);
                           attributePaths.add(etd);
                        }
                     }
                     if (ExecueCoreUtil.isCollectionNotEmpty(attributePaths)) {
                        attributePathMap.put(possibleAttribute.getComponentTypeBed().getId(), attributePaths);
                     }
                  }
               }
               List<BehaviorType> behaviors = new ArrayList<BehaviorType>();
               if (ExecueCoreUtil.isCollectionNotEmpty(conceptTypeInfo.getBehavior())) {
                  for (String behaviorName : conceptTypeInfo.getBehavior()) {
                     BusinessEntityDefinition businessEntityDefinition = getKdxRetrievalService().getBEDByBehaviorName(
                              behaviorName);
                     BehaviorType behaviorType = BehaviorType.getType(businessEntityDefinition.getId().intValue());
                     behaviors.add(behaviorType);
                  }
               }

               Cloud cloud = getKdxCloudRetrievalService().getDefaultAppCloud(modelId);

               BusinessEntityDefinition detailTypeBed = null;
               boolean detailTypeProvided = false;
               if (conceptTypeInfo.getDetailTypeName() != null) {
                  detailTypeBed = getKdxRetrievalService().getTypeBedByName(conceptTypeInfo.getDetailTypeName());
                  detailTypeProvided = true;
               }

               TypeConceptAssociationInfo typeConceptInfo = ExecueBeanManagementUtil.populateTypeConceptInfo(
                        conceptBED, typeBED, cloud, conceptTypeAssociationContext.getModelId(), behaviors,
                        attributePathMap, detailTypeBed, true, true, detailTypeProvided, null, null, false, false,
                        false);
               assignConceptType(typeConceptInfo);
               if (conceptTypeAssociationContext.getJobRequest() != null) {
                  jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                           JobStatus.SUCCESS, null, new Date());
                  getJobDataService().updateJobOperationStatus(jobOperationalStatus);
               }

            } catch (ExeCueException exception) {
               if (conceptTypeAssociationContext.getJobRequest() != null) {
                  jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                           JobStatus.FAILURE, null, new Date());
                  getJobDataService().updateJobOperationStatus(jobOperationalStatus);
               }
            }
         }
      } catch (ExeCueException exception) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                     .getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
            }
         }
         exception.printStackTrace();
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
   }

   private ConceptTypeAssociationInfo parseConceptAssociationXMLFile (String filePath) throws ExeCueException {
      try {
         File file = new File(filePath);
         InputStream inputStream = new FileInputStream(file);
         XStream xstream = new XStream();
         xstream.alias("ConceptTypeAssociation", ConceptTypeAssociationInfo.class);
         xstream.alias("concept", ConceptTypeInfo.class);
         xstream.alias("attribute", AttributeInfo.class);
         xstream.alias("behavior", ConceptTypeInfo.class);
         ConceptTypeAssociationInfo conceptTypeAssociationInfo = (ConceptTypeAssociationInfo) xstream
                  .fromXML(ExecueCoreUtil.readFileAsString(inputStream));
         return conceptTypeAssociationInfo;

      } catch (FileNotFoundException fileNotFoundException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, fileNotFoundException);
      } catch (IOException ioException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, ioException);
      }

   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
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

   public IConversionService getConversionService () {
      return conversionService;
   }

   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }

   public IKDXDeletionService getKdxDeletionService () {
      return kdxDeletionService;
   }

   public void setKdxDeletionService (IKDXDeletionService kdxDeletionService) {
      this.kdxDeletionService = kdxDeletionService;
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

   public IPathDefinitionDeletionService getPathDefinitionDeletionService () {
      return pathDefinitionDeletionService;
   }

   public void setPathDefinitionDeletionService (IPathDefinitionDeletionService pathDefinitionDeletionService) {
      this.pathDefinitionDeletionService = pathDefinitionDeletionService;
   }

}
