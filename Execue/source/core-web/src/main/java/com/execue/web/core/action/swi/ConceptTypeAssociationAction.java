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


package com.execue.web.core.action.swi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.PossibleDetailType;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.OriginType;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIAttribute;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIPathDefinition;
import com.execue.handler.bean.UIRealization;
import com.execue.handler.bean.UIStatus;
import com.execue.handler.swi.ITypeConceptAssociationServiceHandler;
import com.execue.swi.exception.SWIExceptionCodes;

/**
 * @author jitendra
 */
public class ConceptTypeAssociationAction extends KDXAction {

   private static final long                     serialVersionUID     = 1L;
   private static final Long                     MEASURABLE_ENTITY_ID = 101L;
   private static Logger                         log                  = Logger
                                                                               .getLogger(ConceptTypeAssociationAction.class);
   private ITypeConceptAssociationServiceHandler typeConceptAssociationServiceHandler;
   private List<Type>                            types;
   private List<BehaviorType>                    typeBehaviors;
   private List<BehaviorType>                    extendedBehaviors;
   private List<UIAttribute>                     typeAttributes;
   private List<UIRealization>                   extendedRealizations;
   private Long                                  selectedAttributeTypeId;
   private List<UIConcept>                       realizedConcepts;
   private List<UIPathDefinition>                selectedTypePathDefinitions;
   private List<UIPathDefinition>                selectedExtendedPathDefinitions;
   private List<BehaviorType>                    selectedTypeBehaviors;
   private List<BehaviorType>                    selectedExtendedBehaviors;
   private Long                                  selectedTypeId;
   private Long                                  selectedConceptId;
   private List<Type>                            detailTypes;
   private Long                                  selectedDetailTypeId;
   private List<UIPathDefinition>                savedTypePathDefinitions;
   private List<UIPathDefinition>                savedExtendedPathDefinitions;
   private List<BehaviorType>                    savedTypeBehaviors;
   private List<BehaviorType>                    savedExtendedBehaviors;
   private Concept                               concept;
   private List<UIRealization>                   nonAttributeConcepts;
   private List<UIPathDefinition>                selectedCRCPathDefinitions;
   private List<UIPathDefinition>                savedCRCPathDefinitions;
   private UIStatus                              uiStatus;

   // Will be used as default conversion type if Concept if ME

   // Action
   public String showConcept () {
      try {
         // TODO-JT- hack to fix bug 1718
         Long requestedConceptId = (Long) getHttpRequest().get("concept.id");
         // Check for new concept creation
         if (requestedConceptId != null) {
            concept.setId(requestedConceptId);
         }
         if (concept != null && concept.getId() != null) {
            Long modelId = getApplicationContext().getModelId();
            selectedConceptId = concept.getId();
            Concept populatedConcept = getKdxServiceHandler().getPopulatedConceptWithStats(selectedConceptId);
            if (populatedConcept != null) {
               populatedConcept.setHasInstance(getKdxServiceHandler().hasInstances(modelId, populatedConcept.getId()));
            }
            setConcept(populatedConcept);
         }
         setStats(getKdxServiceHandler().getAllStats());
         populateTypes();
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String showAdvanceConceptDetails () {
      try {
         if (concept != null && concept.getId() != null) {
            setConcept(getKdxServiceHandler().getPopulatedConceptWithStats(concept.getId()));
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String list () {
      try {
         BusinessEntityDefinition conceptBED = null;
         Long modelId = getApplicationContext().getModelId();
         if (selectedConceptId != null) {
            conceptBED = getTypeConceptAssociationServiceHandler().getBusinessEntityDefinition(modelId,
                     selectedConceptId);
         }
         prepareList(modelId, conceptBED);
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String showCRCAssociations () {
      try {
         Long modelId = getApplicationContext().getModelId();
         // TODO: JT: Need to check if we can avoid this action call in case of add new concept
         if (selectedConceptId != null) {
            BusinessEntityDefinition conceptBED = getTypeConceptAssociationServiceHandler()
                     .getBusinessEntityDefinition(modelId, selectedConceptId);
            prepareCRCList(modelId, conceptBED);
         }

      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String populateTypes () {
      try {
         types = getTypeConceptAssociationServiceHandler().getAllVisibleTypes();
         Type matchedType = null;

         if (selectedConceptId != null) {
            Long modelId = getApplicationContext().getModelId();
            BusinessEntityDefinition conceptBED = getTypeConceptAssociationServiceHandler()
                     .getBusinessEntityDefinition(modelId, selectedConceptId);
            Type selectedConceptType = conceptBED.getType();
            setSelectedTypeId(selectedConceptType.getId());
            matchedType = getMatchedType(types, selectedConceptType);
         } else {
            matchedType = getMatchedTypeByName(types, ExecueConstants.ONTO_ENTITY_TYPE);
         }
         types.remove(matchedType);
         types.add(0, matchedType);

      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String populateRealizedConcepts () {
      try {
         Long modelId = getApplicationContext().getModelId();
         realizedConcepts = getTypeConceptAssociationServiceHandler().getRealizedConceptsForType(
                  selectedAttributeTypeId, modelId);
      } catch (ExeCueException exeCueException) {
         exeCueException.printStackTrace();
      }
      return SUCCESS;
   }

   public String saveConceptTypeAssociation () {
      Long modelId = getApplicationContext().getModelId();
      try {
         setStats((getKdxServiceHandler().getAllStats()));
         if (concept.getId() != null) {
            Concept swiConcept = getKdxServiceHandler().getPopulatedConceptWithStats(concept.getId());
            populateStats(getStatList(), swiConcept);
            // populateConceptDetails(swiConcept);
            swiConcept.setDisplayName(concept.getDisplayName());
            swiConcept.setDescription(concept.getDescription());
            getKdxServiceHandler().updateConcept(modelId, swiConcept);
            selectedConceptId = swiConcept.getId();
            saveUpdateTypeAssociation(modelId, false);
            addActionMessage(getText("execue.global.update.success", new String[] { concept.getName() }));
         } else {
            populateStats(getStatList(), concept);
            // populateConceptDetails(concept);
            getKdxServiceHandler().createConcept(modelId, concept);
            selectedConceptId = concept.getId();
            saveUpdateTypeAssociation(modelId, true);
            addActionMessage(getText("execue.global.insert.success", new String[] { concept.getName() }));
            getHttpRequest().put("concept.id", concept.getId());
         }
         if (concept.getStats() == null) {
            concept.setStats(new HashSet<Stat>());
         }

      } catch (ExeCueException execueException) {
         if (log.isDebugEnabled()) {
            log.debug(execueException.getMessage());
         }
         if (execueException.getCode() == SWIExceptionCodes.ENTITY_ALREADY_EXISTS) {
            addActionError(getText("execue.global.exist.message", new String[] { concept.getDisplayName() }));
         } else if (execueException.getCode() == SWIExceptionCodes.RESERVE_WORD_MATCH) {
            addActionError(getText("execue.kdx.reservedword.message", new String[] { concept.getDisplayName() }));
         } else {
            addActionError(getText("execue.global.error", execueException.getMessage()));
         }
         populateTypes();
         return ERROR;
      }
      return SUCCESS;
   }

   public String saveAdvanceConceptTypeAssociation () {
      Long modelId = getApplicationContext().getModelId();
      BusinessEntityDefinition conceptBED = null;
      try {
         conceptBED = getTypeConceptAssociationServiceHandler().getBusinessEntityDefinition(modelId, selectedConceptId);
         // TODO :-VG- this call is required to track that concept is getting modifiied.
         // we need to formalize the entire process and then we will come back and clean the code.
         getKdxServiceHandler().updateConcept(modelId, conceptBED.getConcept());
         getTypeConceptAssociationServiceHandler().associateConceptType(modelId, selectedTypeId, conceptBED.getId(),
                  selectedExtendedBehaviors, selectedExtendedPathDefinitions, savedExtendedBehaviors,
                  savedExtendedPathDefinitions, false, OriginType.USER, null, true);
         addActionMessage(getText("execue.concept.AdvancePathDefintion.save.success"));
      } catch (ExeCueException execueException) {
         if (log.isDebugEnabled()) {
            log.debug(execueException.getMessage());
         }
         if (SWIExceptionCodes.RELATION_ALREADY_EXIST == 90018) {
            addActionError(execueException.getMessage());
         } else {
            addActionError(getText("execue.concept.AdvancePathDefintion.save.failure"));
         }

      } finally {
         try {
            prepareList(modelId, conceptBED);
         } catch (HandlerException e) {
            if (log.isDebugEnabled()) {
               log.debug(e);
            }
         }
      }
      return SUCCESS;
   }

   public String saveCRCAssociation () {
      Long modelId = getApplicationContext().getModelId();
      BusinessEntityDefinition conceptBED = null;
      try {
         conceptBED = getTypeConceptAssociationServiceHandler().getBusinessEntityDefinition(modelId, selectedConceptId);
         getTypeConceptAssociationServiceHandler().associateCRC(modelId, conceptBED, selectedCRCPathDefinitions,
                  savedCRCPathDefinitions);
         addActionMessage(getText("execue.concept.crc.save.success"));
      } catch (ExeCueException execueException) {
         addActionMessage(getText("execue.concept.crc.save.failure"));
         execueException.printStackTrace();
      } finally {
         try {
            prepareCRCList(modelId, conceptBED);
         } catch (HandlerException e) {
            if (log.isDebugEnabled()) {
               log.debug(e);
            }
         }
      }
      return SUCCESS;
   }

   public String saveAdvanceConceptDetails () {
      Long modelId = getApplicationContext().getModelId();
      try {
         if (concept.getId() != null) {
            Concept swiConcept = getKdxServiceHandler().getPopulatedConceptWithStats(concept.getId());
            // populateDisplayTypes(getDisplayTypeList(), swiConcept);
            swiConcept.setDefaultDataFormat(concept.getDefaultDataFormat());
            swiConcept.setDefaultUnit((concept.getDefaultUnit()));
            getKdxServiceHandler().updateConcept(modelId, swiConcept);
            setConcept(swiConcept);
            addActionMessage(getText("execue.global.update.success", new String[] { concept.getName() }));
         }

      } catch (ExeCueException execueException) {
         if (log.isDebugEnabled()) {
            log.debug(execueException.getMessage());
         }
      }
      return SUCCESS;
   }

   public String deleteConceptHeirarchy () {
      Long modelId = getApplicationContext().getModelId();
      uiStatus = new UIStatus();
      try {
         getKdxServiceHandler().deleteConceptHeirarchy(modelId, selectedConceptId);
         uiStatus.setMessage(getText("execue.concept.delete.success"));
      } catch (ExeCueException execueException) {
         uiStatus.setStatus(false);
         uiStatus.addErrorMessage("Error: " + execueException.getMessage());
         if (log.isDebugEnabled()) {
            log.debug(execueException.getMessage());
         }
      }
      return SUCCESS;
   }

   public String deleteInstancesHeirarchyForConcept () {
      Long modelId = getApplicationContext().getModelId();
      uiStatus = new UIStatus();
      try {
         getKdxServiceHandler().deleteInstancesHeirarchyForConcept(modelId, concept.getId());
         uiStatus.setMessage(getText("execue.concept.instances.delete.success"));
      } catch (ExeCueException execueException) {
         uiStatus.setStatus(false);
         uiStatus.addErrorMessage("Error: " + execueException.getMessage());
         if (log.isDebugEnabled()) {
            log.debug(execueException.getMessage());
         }
      }
      return SUCCESS;
   }

   private void prepareCRCList (Long modelId, BusinessEntityDefinition conceptBED) throws HandlerException {
      nonAttributeConcepts = getTypeConceptAssociationServiceHandler().getAllNonAttributeConcepts(modelId);
      removeSelectedConcept(nonAttributeConcepts);
      setSelectedCRCPathDefinitions(getTypeConceptAssociationServiceHandler().getAllEntityDirectPaths(modelId,
               conceptBED, EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT));
   }

   private void populateStats (List<Long> statList, Concept concept) {
      concept.setStats(new HashSet<Stat>());
      if (ExecueCoreUtil.isCollectionNotEmpty(getStatList())) {
         for (int i = 0; i < getStatList().size(); i++) {
            Stat stat = new Stat();
            stat.setId(getStatList().get(i));
            concept.getStats().add(stat);
         }
      }

   }

   private void saveUpdateTypeAssociation (Long modelId, boolean create) throws HandlerException {
      BusinessEntityDefinition conceptBED = getTypeConceptAssociationServiceHandler().getBusinessEntityDefinition(
               modelId, selectedConceptId);
      getTypeConceptAssociationServiceHandler().associateConceptType(modelId, selectedTypeId, conceptBED.getId(),
               selectedTypeBehaviors, selectedTypePathDefinitions, savedTypeBehaviors, savedTypePathDefinitions,
               create, OriginType.TYPE, selectedDetailTypeId, false);
      // update the concept bed for type
      conceptBED = getTypeConceptAssociationServiceHandler().getBusinessEntityDefinition(modelId, selectedConceptId);
      showBehaviors();
      showAttributes(modelId);
      showExistingBehaviors(conceptBED);
      showExistingAttributePaths(modelId, conceptBED);
      if (ExecueCoreUtil.isCollectionEmpty(getSelectedTypeBehaviors())) {
         setSelectedTypeBehaviors(typeBehaviors);
      }
      populateTypes();
   }

   private void prepareList (Long modelId, BusinessEntityDefinition conceptBED) throws HandlerException {
      showBehaviors();
      showAttributes(modelId);
      if (conceptBED != null && selectedTypeId.equals(conceptBED.getType().getId())) {
         showExistingBehaviors(conceptBED);
         showExistingAttributePaths(modelId, conceptBED);
      }
      if (ExecueCoreUtil.isCollectionEmpty(getSelectedTypeBehaviors())) {
         setSelectedTypeBehaviors(typeBehaviors);
      }
      BusinessEntityDefinition selectedTypeBed = getTypeConceptAssociationServiceHandler().getTypeById(selectedTypeId);
      if (CheckType.YES.equals(selectedTypeBed.getType().getAbstrat())) {
         List<PossibleDetailType> possibleDetailTypes = getTypeConceptAssociationServiceHandler()
                  .getPossibleDetailTypes(selectedTypeBed.getId());
         setDetailTypes(populateDetailTypes(possibleDetailTypes));
         if (conceptBED != null && selectedTypeId.equals(conceptBED.getType().getId())) {
            BusinessEntityDefinition detailTypeForConcept = getTypeConceptAssociationServiceHandler()
                     .getDetailTypeForConcept(conceptBED.getId());
            setSelectedDetailTypeId(detailTypeForConcept.getType().getId());
         }
      }
   }

   private List<Type> populateDetailTypes (List<PossibleDetailType> possibleDetailTypes) {
      List<Type> detailTypes = new ArrayList<Type>();
      Type defaultDetailType = possibleDetailTypes.get(0).getDetailTypeBed().getType();
      for (PossibleDetailType possibleDetailType : possibleDetailTypes) {
         detailTypes.add(possibleDetailType.getDetailTypeBed().getType());
         if (CheckType.YES.equals(possibleDetailType.getDfault())) {
            defaultDetailType = possibleDetailType.getDetailTypeBed().getType();
         }
      }
      detailTypes.remove(defaultDetailType);
      detailTypes.add(0, defaultDetailType);
      return detailTypes;
   }

   private Type getMatchedType (List<Type> types, Type toBeMatchedType) {
      Type matchedType = null;
      for (Type type : types) {
         if (type.getId().equals(toBeMatchedType.getId())) {
            matchedType = type;
            break;
         }
      }
      return matchedType;
   }

   private Type getMatchedTypeByName (List<Type> types, String ontoEntityTypeName) {
      Type matchedType = null;
      for (Type type : types) {
         if (type.getName().equals(ontoEntityTypeName)) {
            matchedType = type;
            break;
         }
      }
      return matchedType;
   }

   private void showExistingAttributePaths (Long modelId, BusinessEntityDefinition conceptBED) throws HandlerException {
      List<UIPathDefinition> entityDirectPaths = getTypeConceptAssociationServiceHandler().getAllEntityDirectPaths(
               modelId, conceptBED, EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
      setSelectedTypePathDefinitions(getMatchedUIPathDefinitions(entityDirectPaths, typeAttributes));
      setSelectedExtendedPathDefinitions(getMatchedExtendedUIPathDefinitions(entityDirectPaths, extendedRealizations));
   }

   private List<UIPathDefinition> getMatchedExtendedUIPathDefinitions (List<UIPathDefinition> entityPathDefinitions,
            List<UIRealization> extendedRealizations) {
      List<UIPathDefinition> matchedPathDefinitions = new ArrayList<UIPathDefinition>();
      for (UIPathDefinition uiPathDefinition : entityPathDefinitions) {
         if (isMatchingExatendedPathDefinition(uiPathDefinition, extendedRealizations)) {
            matchedPathDefinitions.add(uiPathDefinition);
         }
      }
      return matchedPathDefinitions;
   }

   private boolean isMatchingExatendedPathDefinition (UIPathDefinition uiPathDefinition,
            List<UIRealization> extendedRealizations) {
      boolean isMatchedFound = false;
      for (UIRealization uiRealization : extendedRealizations) {
         if (uiPathDefinition.getAttributeBedId().equals(uiRealization.getAttributeBedId())) {
            isMatchedFound = true;
            break;
         }
      }
      return isMatchedFound;
   }

   private List<UIPathDefinition> getMatchedUIPathDefinitions (List<UIPathDefinition> entityPathDefinitions,
            List<UIAttribute> uiAttributes) {
      List<UIPathDefinition> matchedPathDefinitions = new ArrayList<UIPathDefinition>();
      for (UIPathDefinition uiPathDefinition : entityPathDefinitions) {
         if (isMatchingPathDefinition(uiPathDefinition, uiAttributes)) {
            matchedPathDefinitions.add(uiPathDefinition);
         }
      }
      return matchedPathDefinitions;
   }

   private boolean isMatchingPathDefinition (UIPathDefinition uiPathDefinition, List<UIAttribute> uiAttributes) {
      boolean isMatchedFound = false;
      for (UIAttribute uiAttribute : uiAttributes) {
         if (uiPathDefinition.getAttributeBedId().equals(uiAttribute.getAttributeBedId())) {
            isMatchedFound = true;
            break;
         }
      }
      return isMatchedFound;
   }

   private void showExistingBehaviors (BusinessEntityDefinition conceptBED) throws HandlerException {
      List<BehaviorType> entityBehaviors = getTypeConceptAssociationServiceHandler().getAllEntityBehaviors(
               conceptBED.getId());
      setSelectedTypeBehaviors(getMatchedBehavior(entityBehaviors, typeBehaviors));
      setSelectedExtendedBehaviors(getMatchedBehavior(entityBehaviors, extendedBehaviors));
   }

   private List<BehaviorType> getMatchedBehavior (List<BehaviorType> existingBehaviors,
            List<BehaviorType> toBeMatchedBehaviors) {
      List<BehaviorType> matchedBehaviors = new ArrayList<BehaviorType>(existingBehaviors);
      matchedBehaviors.retainAll(toBeMatchedBehaviors);
      return matchedBehaviors;
   }

   private void showAttributes (Long modelId) throws HandlerException {
      typeAttributes = getTypeConceptAssociationServiceHandler().getPossibleAttributes(selectedTypeId);
      extendedRealizations = new ArrayList<UIRealization>();
      List<UIRealization> allAttributes = getTypeConceptAssociationServiceHandler().getAllAttributes(modelId,
               selectedTypeId);
      if (ExecueCoreUtil.isCollectionNotEmpty(typeAttributes)) {
         for (UIRealization uiRealization : allAttributes) {
            if (!isAttributeExists(typeAttributes, uiRealization)) {
               extendedRealizations.add(uiRealization);
            }
         }
      } else {
         extendedRealizations = allAttributes;
      }
   }

   private boolean isAttributeExists (List<UIAttribute> uiAttributes, UIAttribute toBeMatchedUIAttribute) {
      boolean isAttributeExists = false;
      for (UIAttribute uiAttribute : uiAttributes) {
         if (toBeMatchedUIAttribute.getAttributeBedId().equals(uiAttribute.getAttributeBedId())) {
            isAttributeExists = true;
            break;
         }
      }
      return isAttributeExists;
   }

   private void showBehaviors () throws HandlerException {
      typeBehaviors = getTypeConceptAssociationServiceHandler().getAllPossibleBehaviors(selectedTypeId);
      List<BehaviorType> totalBehaviors = getTypeConceptAssociationServiceHandler().getAllBehaviors();
      extendedBehaviors = new ArrayList<BehaviorType>();
      if (ExecueCoreUtil.isCollectionNotEmpty(typeBehaviors)) {
         for (BehaviorType behavior : totalBehaviors) {
            if (!typeBehaviors.contains(behavior)) {
               extendedBehaviors.add(behavior);
            }
         }
      } else {
         extendedBehaviors.addAll(totalBehaviors);
      }
      //101 means measurable entity
      if (ExecueCoreUtil.isCollectionNotEmpty(extendedBehaviors)
               && MEASURABLE_ENTITY_ID.longValue() != selectedTypeId.longValue()) {
         extendedBehaviors.remove(BehaviorType.DEPENDENT_VARIABLE);
      }
   }

   private void removeSelectedConcept (List<UIRealization> nonAttributeConcepts) {
      for (UIRealization realization : nonAttributeConcepts) {
         if (selectedConceptId.equals(realization.getRealizationId())) {
            nonAttributeConcepts.remove(realization);
            break;
         }
      }

   }

   public ITypeConceptAssociationServiceHandler getTypeConceptAssociationServiceHandler () {
      return typeConceptAssociationServiceHandler;
   }

   public void setTypeConceptAssociationServiceHandler (
            ITypeConceptAssociationServiceHandler typeConceptAssociationServiceHandler) {
      this.typeConceptAssociationServiceHandler = typeConceptAssociationServiceHandler;
   }

   public List<Type> getTypes () {
      return types;
   }

   public void setTypes (List<Type> types) {
      this.types = types;
   }

   public List<UIConcept> getRealizedConcepts () {
      return realizedConcepts;
   }

   public void setRealizedConcepts (List<UIConcept> realizedConcepts) {
      this.realizedConcepts = realizedConcepts;
   }

   public Long getSelectedAttributeTypeId () {
      return selectedAttributeTypeId;
   }

   public void setSelectedAttributeTypeId (Long selectedAttributeTypeId) {
      this.selectedAttributeTypeId = selectedAttributeTypeId;
   }

   public List<BehaviorType> getTypeBehaviors () {
      return typeBehaviors;
   }

   public void setTypeBehaviors (List<BehaviorType> typeBehaviors) {
      this.typeBehaviors = typeBehaviors;
   }

   public List<BehaviorType> getExtendedBehaviors () {
      return extendedBehaviors;
   }

   public void setExtendedBehaviors (List<BehaviorType> extendedBehaviors) {
      this.extendedBehaviors = extendedBehaviors;
   }

   public List<UIAttribute> getTypeAttributes () {
      return typeAttributes;
   }

   public void setTypeAttributes (List<UIAttribute> typeAttributes) {
      this.typeAttributes = typeAttributes;
   }

   public List<UIPathDefinition> getSelectedExtendedPathDefinitions () {
      return selectedExtendedPathDefinitions;
   }

   public void setSelectedExtendedPathDefinitions (List<UIPathDefinition> selectedExtendedPathDefinitions) {
      this.selectedExtendedPathDefinitions = selectedExtendedPathDefinitions;
   }

   public List<BehaviorType> getSelectedTypeBehaviors () {
      return selectedTypeBehaviors;
   }

   public void setSelectedTypeBehaviors (List<BehaviorType> selectedTypeBehaviors) {
      this.selectedTypeBehaviors = selectedTypeBehaviors;
   }

   public List<BehaviorType> getSelectedExtendedBehaviors () {
      return selectedExtendedBehaviors;
   }

   public void setSelectedExtendedBehaviors (List<BehaviorType> selectedExtendedBehaviors) {
      this.selectedExtendedBehaviors = selectedExtendedBehaviors;
   }

   public Long getSelectedTypeId () {
      return selectedTypeId;
   }

   public void setSelectedTypeId (Long selectedTypeId) {
      this.selectedTypeId = selectedTypeId;
   }

   public Long getSelectedConceptId () {
      return selectedConceptId;
   }

   public void setSelectedConceptId (Long selectedConceptId) {
      this.selectedConceptId = selectedConceptId;
   }

   public List<UIPathDefinition> getSelectedTypePathDefinitions () {
      return selectedTypePathDefinitions;
   }

   public void setSelectedTypePathDefinitions (List<UIPathDefinition> selectedTypePathDefinitions) {
      this.selectedTypePathDefinitions = selectedTypePathDefinitions;
   }

   public List<UIPathDefinition> getSavedTypePathDefinitions () {
      return savedTypePathDefinitions;
   }

   public void setSavedTypePathDefinitions (List<UIPathDefinition> savedTypePathDefinitions) {
      this.savedTypePathDefinitions = savedTypePathDefinitions;
   }

   public List<UIPathDefinition> getSavedExtendedPathDefinitions () {
      return savedExtendedPathDefinitions;
   }

   public void setSavedExtendedPathDefinitions (List<UIPathDefinition> savedExtendedPathDefinitions) {
      this.savedExtendedPathDefinitions = savedExtendedPathDefinitions;
   }

   public List<BehaviorType> getSavedTypeBehaviors () {
      return savedTypeBehaviors;
   }

   public void setSavedTypeBehaviors (List<BehaviorType> savedTypeBehaviors) {
      this.savedTypeBehaviors = savedTypeBehaviors;
   }

   public List<BehaviorType> getSavedExtendedBehaviors () {
      return savedExtendedBehaviors;
   }

   public void setSavedExtendedBehaviors (List<BehaviorType> savedExtendedBehaviors) {
      this.savedExtendedBehaviors = savedExtendedBehaviors;
   }

   @Override
   public Concept getConcept () {
      return concept;
   }

   @Override
   public void setConcept (Concept concept) {
      this.concept = concept;
   }

   public List<UIRealization> getExtendedRealizations () {
      return extendedRealizations;
   }

   public void setExtendedRealizations (List<UIRealization> extendedRealizations) {
      this.extendedRealizations = extendedRealizations;
   }

   public List<Type> getDetailTypes () {
      return detailTypes;
   }

   public void setDetailTypes (List<Type> detailTypes) {
      this.detailTypes = detailTypes;
   }

   public Long getSelectedDetailTypeId () {
      return selectedDetailTypeId;
   }

   public void setSelectedDetailTypeId (Long selectedDetailTypeId) {
      this.selectedDetailTypeId = selectedDetailTypeId;
   }

   public List<UIRealization> getNonAttributeConcepts () {
      return nonAttributeConcepts;
   }

   public void setNonAttributeConcepts (List<UIRealization> nonAttributeConcepts) {
      this.nonAttributeConcepts = nonAttributeConcepts;
   }

   public List<UIPathDefinition> getSelectedCRCPathDefinitions () {
      return selectedCRCPathDefinitions;
   }

   public void setSelectedCRCPathDefinitions (List<UIPathDefinition> selectedCRCPathDefinitions) {
      this.selectedCRCPathDefinitions = selectedCRCPathDefinitions;
   }

   public List<UIPathDefinition> getSavedCRCPathDefinitions () {
      return savedCRCPathDefinitions;
   }

   public void setSavedCRCPathDefinitions (List<UIPathDefinition> savedCRCPathDefinitions) {
      this.savedCRCPathDefinitions = savedCRCPathDefinitions;
   }

   public List<String> getBaseRelations () {
      return getTypeConceptAssociationServiceHandler().getBaseRelationsName();
   }

   /**
    * @return the uiStatus
    */
   public UIStatus getUiStatus () {
      return uiStatus;
   }

   /**
    * @param uiStatus the uiStatus to set
    */
   public void setUiStatus (UIStatus uiStatus) {
      this.uiStatus = uiStatus;
   }
}
