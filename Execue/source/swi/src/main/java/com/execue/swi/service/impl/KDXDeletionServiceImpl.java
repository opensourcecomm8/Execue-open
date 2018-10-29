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

import com.execue.core.common.bean.entity.BusinessEntityVariation;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.ModelGroupMapping;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SecondaryWord;
import com.execue.core.common.bean.entity.VerticalAppExample;
import com.execue.core.common.bean.entity.VerticalAppWeight;
import com.execue.core.common.bean.entity.VerticalEntityBasedRedirection;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IKDXDeletionService;

public class KDXDeletionServiceImpl extends KDXCommonServiceImpl implements IKDXDeletionService {

   private IBusinessEntityMaintenanceService businessEntityMaintenanceService;

   public void deleteInstances (List<Instance> instances) throws KDXException {
      getKdxDataAccessManager().deleteInstances(instances);
   }

   public void deleteRelation (Relation relation) throws KDXException {
      getKdxDataAccessManager().deleteRelation(relation);
   }

   public void deleteConcept (Concept concept) throws KDXException {
      getKdxDataAccessManager().deleteConcept(concept);
   }

   public void deleteInstance (Instance instance) throws KDXException {
      getKdxDataAccessManager().deleteInstance(instance);
   }

   public void deleteBusinessEntityDefinitionById (Long bedId) throws KDXException {
      getKdxDataAccessManager().deleteBusinessEntityDefinitionById(bedId);
   }

   public void deleteVerticalEntityRedirectionEntitiesByEntityBedId (Long entityBedId, BusinessEntityType entityType)
            throws KDXException {
      getKdxDataAccessManager().deleteVerticalEntityRedirectionEntitiesByEntityBedId(entityBedId, entityType);
   }

   public void deleteSFLTerm (SFLTerm sflTerm) throws KDXException {
      getKdxDataAccessManager().deleteSFLTerm(sflTerm);
   }

   public void deleteConcepts (List<Concept> concepts) throws KDXException {
      getKdxDataAccessManager().deleteConcepts(concepts);
   }

   public void deleteRelations (List<Relation> relations) throws KDXException {
      getKdxDataAccessManager().deleteRelations(relations);
   }

   public void deleteEntityDetailTypeByConcept (Long conceptBedId) throws KDXException {
      getKdxDataAccessManager().deleteEntityDetailTypeByConcept(conceptBedId);
   }

   public void deleteEntityBehavior (Long conceptBedId) throws KDXException {
      getKdxDataAccessManager().deleteEntityBehavior(conceptBedId);
   }

   public void deleteEntityBehaviors (Long conceptBedId, List<BehaviorType> behaviorTypes) throws KDXException {
      getKdxDataAccessManager().deleteEntityBehaviors(conceptBedId, behaviorTypes);
   }

   public void deleteSecondaryWords (List<SecondaryWord> secondaryWords) throws KDXException {
      getKdxDataAccessManager().deleteSecondaryWords(secondaryWords);
   }

   public void deleteVerticalAppWeight (VerticalAppWeight verticalAppWeight) throws KDXException {
      getKdxDataAccessManager().deleteVerticalAppWeight(verticalAppWeight);
   }

   public void deleteVerticalRedirectionEntities (
            List<VerticalEntityBasedRedirection> verticalEntityBasedRedirectionList) throws KDXException {
      getKdxDataAccessManager().deleteVerticalRedirectionEntities(verticalEntityBasedRedirectionList);
   }

   public void deleteVerticalAppExamples (List<VerticalAppExample> verticalAppExamples) throws KDXException {
      getKdxDataAccessManager().deleteVerticalAppExamples(verticalAppExamples);
   }

   public void deleteRIOntoTerms (List<ModelGroup> userModelGroups) throws KDXException {
      getKdxDataAccessManager().cleanRIOntoTerms(userModelGroups);
   }

   public void deleteSFLTerms (List<Long> sflTermIds) throws KDXException {
      getKdxDataAccessManager().cleanSFLTerms(sflTermIds);
   }

   public void deleteDefaultDynamicValues (List<ModelGroup> userModelGroups) throws KDXException {
      getKdxDataAccessManager().cleanDefaultDynamicValues(userModelGroups);
   }

   public void deleteModelGroupMappings (List<ModelGroupMapping> modelGroupMappings) throws KDXException {
      getKdxDataAccessManager().cleanModelGroupMappings(modelGroupMappings);
   }

   public void deleteModelGroups (List<ModelGroup> userModelGroups) throws KDXException {
      getKdxDataAccessManager().cleanModelGroups(userModelGroups);
   }

   public void deleteModels (List<Model> models) throws KDXException {
      getKdxDataAccessManager().cleanModels(models);
   }

   public void deleteBusinessEntityVariation (BusinessEntityVariation businessEntityVariation) throws KDXException {
      getKdxDataAccessManager().deleteBusinessEntityVariation(businessEntityVariation);
   }

   public void deleteBusinessEntityVariations (List<Long> modelGroupIds) throws KDXException {
      getKdxDataAccessManager().deleteBusinessEntityVariations(modelGroupIds);
   }

   public void deleteRISharedUserModelMappings (List<Long> userModelGroupIds) throws KDXException {
      getKdxDataAccessManager().deleteRISharedUserModelMappings(userModelGroupIds);
   }

   public void deleteIndexFormsByBedId (Long instanceProfileBedId) throws KDXException {
      getKdxDataAccessManager().deleteIndexFormsByBedId(instanceProfileBedId);
   }

   @Override
   public void deleteInstanceBedsForConcept (Long modelId, Long conceptId) throws KDXException {
      getKdxDataAccessManager().deleteInstanceBedsForConcept(modelId, conceptId);
   }

   @Override
   public void deleteInstanceByIds (List<Long> instanceIds) throws KDXException {
      getKdxDataAccessManager().deleteInstanceByIds(instanceIds);

   }

   public IBusinessEntityMaintenanceService getBusinessEntityMaintenanceService () {
      return businessEntityMaintenanceService;
   }

   public void setBusinessEntityMaintenanceService (IBusinessEntityMaintenanceService businessEntityMaintenanceService) {
      this.businessEntityMaintenanceService = businessEntityMaintenanceService;
   }

}
