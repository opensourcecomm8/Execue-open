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
import java.util.List;

import com.execue.core.common.bean.entity.Type;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.swi.ITypeConceptAssociationServiceHandler;

public class TypeConceptAssociationAction extends SWIAction {

   private List<Type>                            types;
   private Type                                  selectedType;
   private List<UIConcept>                       eligibleConcepts;
   private List<Type>                            typesToBeRealized;
   private Type                                  selectedTypeToBeRealized;
   private List<UIConcept>                       realizedConcepts;
   private ITypeConceptAssociationServiceHandler typeConceptAssociationServiceHandler;

   public String list () {
      try {
         types = new ArrayList<Type>();
         Type type = new Type();
         type.setDisplayName("");
         type.setId(-1L);
         types.add(type);
         types.add(getTypeConceptAssociationServiceHandler().getTypeByName(ExecueConstants.TIME_FRAME_TYPE));
         types.add(getTypeConceptAssociationServiceHandler().getTypeByName(ExecueConstants.ONTO_ENTITY_TYPE));
         types.add(getTypeConceptAssociationServiceHandler().getTypeByName(ExecueConstants.MEASURABLE_ENTITY_TYPE));
      } catch (ExeCueException exeCueException) {
         exeCueException.printStackTrace();
      }
      return SUCCESS;
   }

   public String populateEligibleConcepts () {
      try {
         Long modelId = getApplicationContext().getModelId();
         eligibleConcepts = getTypeConceptAssociationServiceHandler().getRealizedConceptsForType(selectedType.getId(),
                  modelId);
         for (UIConcept uiConcept : eligibleConcepts) {
            uiConcept.setExisting(true);
         }
         if (!selectedType.getName().equalsIgnoreCase(ExecueConstants.ONTO_ENTITY_TYPE)) {
            Type ontoEntityType = getTypeConceptAssociationServiceHandler().getTypeByName(
                     ExecueConstants.ONTO_ENTITY_TYPE);
            List<UIConcept> realizedConceptsForType = getTypeConceptAssociationServiceHandler()
                     .getRealizedConceptsForType(ontoEntityType.getId(), modelId);
            if (ExecueCoreUtil.isCollectionNotEmpty(realizedConceptsForType)) {
               eligibleConcepts.addAll(realizedConceptsForType);
            }
         }

      } catch (ExeCueException exeCueException) {
         exeCueException.printStackTrace();
      }
      return SUCCESS;
   }

   public String populateTypesToBeRealized () {
      try {
         typesToBeRealized = getTypeConceptAssociationServiceHandler().getAttributesToBeRealizedForType(
                  selectedType.getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(typesToBeRealized)) {
            Type type = new Type();
            type.setDisplayName("");
            type.setId(-1L);
            typesToBeRealized.add(0, type);
         }
      } catch (ExeCueException exeCueException) {
         exeCueException.printStackTrace();
      }
      return SUCCESS;
   }

   public String populateRealizedConcepts () {
      try {
         Long modelId = getApplicationContext().getModelId();
         realizedConcepts = getTypeConceptAssociationServiceHandler().getRealizedConceptsForType(
                  selectedTypeToBeRealized.getId(), modelId);
      } catch (ExeCueException exeCueException) {
         exeCueException.printStackTrace();
      }
      return SUCCESS;
   }

   public String associateTypeConcepts () {
      try {
         Long modelId = getApplicationContext().getModelId();
         if (selectedType != null && selectedType.getId() != -1) {
            getTypeConceptAssociationServiceHandler().associateTypeConcepts(modelId, selectedType, eligibleConcepts,
                     selectedTypeToBeRealized, realizedConcepts);
            addActionMessage("Type to Concept Association Successfull");
         }
         list();
      } catch (ExeCueException exeCueException) {
         exeCueException.printStackTrace();
      }
      return SUCCESS;
   }

   public List<Type> getTypes () {
      return types;
   }

   public void setTypes (List<Type> types) {
      this.types = types;
   }

   public Type getSelectedType () {
      return selectedType;
   }

   public void setSelectedType (Type selectedType) {
      this.selectedType = selectedType;
   }

   public List<UIConcept> getEligibleConcepts () {
      return eligibleConcepts;
   }

   public void setEligibleConcepts (List<UIConcept> eligibleConcepts) {
      this.eligibleConcepts = eligibleConcepts;
   }

   public List<Type> getTypesToBeRealized () {
      return typesToBeRealized;
   }

   public void setTypesToBeRealized (List<Type> typesToBeRealized) {
      this.typesToBeRealized = typesToBeRealized;
   }

   public Type getSelectedTypeToBeRealized () {
      return selectedTypeToBeRealized;
   }

   public void setSelectedTypeToBeRealized (Type selectedTypeToBeRealized) {
      this.selectedTypeToBeRealized = selectedTypeToBeRealized;
   }

   public List<UIConcept> getRealizedConcepts () {
      return realizedConcepts;
   }

   public void setRealizedConcepts (List<UIConcept> realizedConcepts) {
      this.realizedConcepts = realizedConcepts;
   }

   public ITypeConceptAssociationServiceHandler getTypeConceptAssociationServiceHandler () {
      return typeConceptAssociationServiceHandler;
   }

   public void setTypeConceptAssociationServiceHandler (
            ITypeConceptAssociationServiceHandler typeConceptAssociationServiceHandler) {
      this.typeConceptAssociationServiceHandler = typeConceptAssociationServiceHandler;
   }

}
