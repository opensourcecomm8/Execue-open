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


package com.execue.swi.service;

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

public interface IKDXDeletionService extends IKDXCommonService {

   public void deleteInstances (List<Instance> instances) throws KDXException;

   public void deleteConcepts (List<Concept> concepts) throws KDXException;

   public void deleteRelations (List<Relation> relations) throws KDXException;

   public void deleteSFLTerm (SFLTerm sflTerm) throws KDXException;

   public void deleteEntityBehavior (Long conceptBedId) throws KDXException;

   public void deleteEntityBehaviors (Long conceptBedId, List<BehaviorType> behaviorTypes) throws KDXException;

   public void deleteSecondaryWords (List<SecondaryWord> secondaryWords) throws KDXException;

   public void deleteVerticalAppWeight (VerticalAppWeight verticalAppWeight) throws KDXException;

   public void deleteVerticalRedirectionEntities (
            List<VerticalEntityBasedRedirection> verticalEntityBasedRedirectionList) throws KDXException;

   public void deleteVerticalAppExamples (List<VerticalAppExample> verticalAppExamples) throws KDXException;

   public void deleteEntityDetailTypeByConcept (Long conceptBedId) throws KDXException;

   public void deleteRIOntoTerms (List<ModelGroup> userModelGroups) throws KDXException;

   public void deleteSFLTerms (List<Long> sflTermIds) throws KDXException;

   public void deleteDefaultDynamicValues (List<ModelGroup> userModelGroups) throws KDXException;

   public void deleteModelGroupMappings (List<ModelGroupMapping> modelGroupMappings) throws KDXException;

   public void deleteModelGroups (List<ModelGroup> userModelGroups) throws KDXException;

   public void deleteModels (List<Model> models) throws KDXException;

   public void deleteBusinessEntityVariation (BusinessEntityVariation businessEntityVariation) throws KDXException;

   public void deleteBusinessEntityVariations (List<Long> modelGroupIds) throws KDXException;

   public void deleteRISharedUserModelMappings (List<Long> userModelGroupIds) throws KDXException;

   public void deleteBusinessEntityDefinitionById (Long bedId) throws KDXException;

   public void deleteVerticalEntityRedirectionEntitiesByEntityBedId (Long entityBedId, BusinessEntityType entityType)
            throws KDXException;

   public void deleteRelation (Relation relation) throws KDXException;

   public void deleteConcept (Concept concept) throws KDXException;

   public void deleteInstance (Instance instance) throws KDXException;

   public void deleteInstanceBedsForConcept (Long modelId, Long conceptId) throws KDXException;

   public void deleteInstanceByIds (List<Long> instanceIds) throws KDXException;
}
