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


/**
 * 
 */
package com.execue.platform.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.TypeConceptAssociationInfo;
import com.execue.core.common.bean.TypeConceptEvaluationInfo;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.type.AppCreationType;
import com.execue.core.common.type.ConceptBaseType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.OriginType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.IModelAbsorbtionService;
import com.execue.platform.swi.IConceptTypeAssociationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathDefinitionDeletionService;
import com.execue.swi.service.IPathDefinitionManagementService;
import com.execue.swi.service.IPathDefinitionRetrievalService;

/**
 * This class provides method to absorb the domain ontology into NLP tables of SWI
 * 
 * @author Nitesh
 */
public class ModelAbsorbtionServiceImpl implements IModelAbsorbtionService {

   private IKDXRetrievalService             kdxRetrievalService;
   private IKDXCloudRetrievalService        kdxCloudRetrievalService;
   private IConceptTypeAssociationService   conceptTypeAssociationService;
   private IKDXManagementService            kdxManagementService;
   private IPathDefinitionRetrievalService  pathDefinitionRetrievalService;
   private IPathDefinitionManagementService pathDefinitionManagementService;
   private IPathDefinitionDeletionService   pathDefinitionDeletionService;
   private IApplicationRetrievalService     applicationRetrievalService;

   public IPathDefinitionDeletionService getPathDefinitionDeletionService () {
      return pathDefinitionDeletionService;
   }

   public void setPathDefinitionDeletionService (IPathDefinitionDeletionService pathDefinitionDeletionService) {
      this.pathDefinitionDeletionService = pathDefinitionDeletionService;
   }

   public void absorbModel (List<TypeConceptEvaluationInfo> evaluatedConcepts, Long modelId) throws SWIException {
      try {
         // prepare map of unique type names with type objects.
         Set<String> uniqueTypes = populateUniqueTypes(evaluatedConcepts);
         Map<String, BusinessEntityDefinition> typeNameObjectMap = new HashMap<String, BusinessEntityDefinition>();
         for (String typeName : uniqueTypes) {
            BusinessEntityDefinition typeBed = getKdxRetrievalService().getTypeBedByName(typeName);
            typeNameObjectMap.put(typeName, typeBed);
         }

         // get the default cloud
         Cloud defaultCloud = getKdxCloudRetrievalService().getDefaultAppCloud(modelId);

         // modify the list by putting the concepts with nonrealizedTypes first and then realizedOnes
         modifyEvaluatedConcepts(evaluatedConcepts);

         boolean isAppUnifiedProcessBased = isAppUnifiedProcessBased(modelId);

         // create paths for all concept bedids
         for (TypeConceptEvaluationInfo typeConceptEvaluationInfo : evaluatedConcepts) {
            ConceptBaseType conceptBaseType = typeConceptEvaluationInfo.getConceptBaseType();
            BusinessEntityDefinition typeBed = typeNameObjectMap.get(conceptBaseType.getValue());
            boolean detailTypeAlreadyConsidered = false;
            if (!typeConceptEvaluationInfo.getConceptBed().getType().getName().equalsIgnoreCase(
                     typeBed.getType().getName())) {
               // Either evaluated type is not an OntoEntity or application is created via unified process,
               // then only over ride what system has with evaluation
               if (!"OntoEntity".equalsIgnoreCase(typeBed.getType().getName()) || isAppUnifiedProcessBased) {
                  BusinessEntityDefinition detailedTypeBed = typeConceptEvaluationInfo.getDetailedTypeBed();
                  boolean detailTypeProvided = false;
                  if (detailedTypeBed != null) {
                     detailTypeProvided = true;
                  }
                  detailTypeAlreadyConsidered = true;
                  TypeConceptAssociationInfo typeConceptInfo = ExecueBeanManagementUtil.populateTypeConceptInfo(
                           typeConceptEvaluationInfo.getConceptBed(), typeBed, defaultCloud, modelId, null, null,
                           detailedTypeBed, false, false, detailTypeProvided, null, null, false, false, false);
                  getConceptTypeAssociationService().assignConceptType(typeConceptInfo);
               }
            }
            if (typeConceptEvaluationInfo.getDetailedTypeBed() != null && !detailTypeAlreadyConsidered) {
               TypeConceptAssociationInfo typeConceptInfo = ExecueBeanManagementUtil.populateTypeConceptInfo(
                        typeConceptEvaluationInfo.getConceptBed(), typeBed, defaultCloud, modelId, null, null,
                        typeConceptEvaluationInfo.getDetailedTypeBed(), false, false, true, null, null, false, false,
                        false);
               getConceptTypeAssociationService().updateDetailTypeForEntity(typeConceptInfo);
            }

            if (ExecueCoreUtil.isCollectionNotEmpty(typeConceptEvaluationInfo.getBehaviorTypes())) {
               getKdxManagementService().createEntityBehaviors(typeConceptEvaluationInfo.getConceptBed().getId(),
                        typeConceptEvaluationInfo.getBehaviorTypes());
            }
            if (typeConceptEvaluationInfo.getValueRealizationBed() != null) {
               BusinessEntityDefinition valueRelationBed = getKdxRetrievalService()
                        .getRelationBEDByNameIncludingBaseGroup(modelId, ExecueConstants.VALUE_PROPERTY);
               List<PathDefinition> valuePathDefinitions = getPathDefinitionRetrievalService()
                        .getPathDefinitionsBySrcBedAndRelation(typeConceptEvaluationInfo.getConceptBed().getId(),
                                 valueRelationBed.getId());
               EntityTripleDefinition etd = ExecueBeanManagementUtil.prepareEntityTripleDefinition(
                        typeConceptEvaluationInfo.getConceptBed(), valueRelationBed, typeConceptEvaluationInfo
                                 .getValueRealizationBed(), EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
               etd.setOrigin(OriginType.TYPE);
               PathDefinition valuePathDefinition = valuePathDefinitions.get(0);

               getPathDefinitionManagementService().createPathDefinition(defaultCloud.getId(), etd,
                        valuePathDefinition.getPathRules());
               getPathDefinitionDeletionService().deletePathDefinition(valuePathDefinition);
            }
         }

      } catch (KDXException e) {
         throw new SWIException(e.getCode(), e.getMessage(), e);
      } catch (SWIException e) {
         throw new SWIException(e.getCode(), e.getMessage(), e);
      }
   }

   private void modifyEvaluatedConcepts (List<TypeConceptEvaluationInfo> evaluatedConcepts) {
      List<TypeConceptEvaluationInfo> nonRealizedTypeConcepts = new ArrayList<TypeConceptEvaluationInfo>();
      for (TypeConceptEvaluationInfo typeConceptEvaluationInfo : evaluatedConcepts) {
         if (typeConceptEvaluationInfo.isNonRealizedType()) {
            nonRealizedTypeConcepts.add(typeConceptEvaluationInfo);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(nonRealizedTypeConcepts)) {
         evaluatedConcepts.removeAll(nonRealizedTypeConcepts);
         evaluatedConcepts.addAll(0, nonRealizedTypeConcepts);
      }
   }

   private Set<String> populateUniqueTypes (List<TypeConceptEvaluationInfo> evaluatedConcepts) {
      Set<String> uniqueTypes = new HashSet<String>();
      for (TypeConceptEvaluationInfo typeConceptEvaluationInfo : evaluatedConcepts) {
         uniqueTypes.add(typeConceptEvaluationInfo.getConceptBaseType().getValue());
      }
      return uniqueTypes;
   }

   private boolean isAppUnifiedProcessBased (Long modelId) throws KDXException {
      boolean appUnifiedProcessBased = false;
      List<Application> apps = getApplicationRetrievalService().getApplicationsByModelId(modelId);
      if (ExecueCoreUtil.isCollectionNotEmpty(apps)) {
         Application app = apps.get(0);
         if (AppCreationType.UNIFIED_PROCESS == app.getCreationType()) {
            appUnifiedProcessBased = true;
         }
      }
      return appUnifiedProcessBased;
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

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
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
