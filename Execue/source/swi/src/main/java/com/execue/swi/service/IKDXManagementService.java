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

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityVariation;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.EntityDetailType;
import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.RISharedUserModelMapping;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.common.bean.entity.SecondaryWord;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.bean.entity.VerticalAppWeight;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.nlp.CandidateEntity;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.CheckType;
import com.execue.swi.exception.KDXException;

// need to create method for creation and updation on group itself rather than model
public interface IKDXManagementService extends ISWIService {

   public void createType (Long modelId, Type type, boolean isRealizedType, Long knowledgeId) throws KDXException;

   public BusinessEntityDefinition createConcept (Long modelId, Concept concept, Long knowledgeId) throws KDXException;

   public BusinessEntityDefinition createConcept (Long modelId, Type type, Concept concept, Long knowledgeId)
            throws KDXException;

   public BusinessEntityDefinition createConcept (Long modelId, Cloud cloud, Type type, Concept concept,
            Long knowledgeId) throws KDXException;

   public BusinessEntityDefinition createRelation (Long modelId, Relation relation, Long knowledgeId)
            throws KDXException;

   public BusinessEntityDefinition createInstance (Long modelId, Long conceptId, Instance instance, Long knowledgeId)
            throws KDXException;

   public BusinessEntityDefinition createTypeInstance (Long modelId, Long typeModelGroupId, Long typeId,
            Instance instance, Long knowledgeId) throws KDXException;

   public BusinessEntityDefinition createTypeInstance (Long modelId, Long typeId, Instance instance, Long knowledgeId)
            throws KDXException;

   public BusinessEntityDefinition updateConcept (Long modelId, Concept concept) throws KDXException;

   public BusinessEntityDefinition updateRelation (Long modelId, Relation relation) throws KDXException;

   public BusinessEntityDefinition saveOrUpdateConcept (Long modelId, Concept concept, Long knowledgeId)
            throws KDXException;

   public BusinessEntityDefinition saveOrUpdateConcept (Long modelId, Type type, Concept concept, Long knowledgeId)
            throws KDXException;

   public BusinessEntityDefinition saveOrUpdateConcept (Long modelId, Cloud cloud, Type type, Concept concept,
            Long knowledgeId) throws KDXException;

   public BusinessEntityDefinition updateInstance (Long modelId, Long conceptId, Instance instance) throws KDXException;

   // we will think on deletion strategy later

   public void createModelGroup (ModelGroup modelGroup) throws KDXException;

   public void createModelGroupMapping (Model model, ModelGroup modelGroup, CheckType owner, CheckType primary)
            throws KDXException;

   public void createModel (Model model, Application application) throws KDXException;

   public void createSFLTerm (SFLTerm sflTerm) throws KDXException;

   public void updateOrderForSFLTermTokens () throws KDXException;

   public void updateSFLTermToken (SFLTermToken termToken) throws KDXException;

   public void updateSFLTermTokens (List<SFLTermToken> termTokens) throws KDXException;

   public void updateTokenWeightsForSFLTerm (Long sflTermId) throws KDXException;

   public void updateWeightsForCandidateEntities (List<CandidateEntity> entities) throws KDXException;

   public Integer updateBusinessEntitiesPopularity (List<BusinessEntityTerm> businessEntityTerms) throws KDXException;

   public void updateBusinessEntityDefinitions (List<BusinessEntityDefinition> businessEntityDefinitions)
            throws KDXException;

   // Methods for deletion of application hierarchy
   public void updateBusinessEntityDefinition (BusinessEntityDefinition businessEntityDefinition) throws KDXException;

   public void updateModel (Model model) throws KDXException;

   public void restrictModelFromEvaluation (Long modelId) throws KDXException;

   /** **************NLP4 methods ********************** */

   /**
    * This method creates the relation and adds the relation as cloud component for the given cloud
    * 
    * @param modelId
    * @param cloud
    * @param type
    * @param relation
    * @throws KDXException
    */
   public BusinessEntityDefinition createRelation (Long modelId, Cloud cloud, Type type, Relation relation,
            Long knowledgeId) throws KDXException;

   public void createEntityBehaviors (Long conceptBedId, List<BehaviorType> behaviorTypes) throws KDXException;

   public void updateCloudComponentsType (Long cloudId, BusinessEntityDefinition typeBED,
            BusinessEntityDefinition conceptBED, boolean isAttribute) throws KDXException;

   public void updateConceptAndInstancesType (Long modelId, BusinessEntityDefinition typeBED,
            BusinessEntityDefinition conceptBED) throws KDXException;

   public void createSecondaryWords (List<SecondaryWord> secondaryWords) throws KDXException;

   public void createVerticalAppWeight (VerticalAppWeight verticalAppWeight) throws KDXException;

   public void createEntityDetailType (EntityDetailType entityDetailType) throws KDXException;

   public void createBusinessEntityVariation (BusinessEntityVariation businessEntityVariation) throws KDXException;

   public void updateBusinessEntityVariation (BusinessEntityVariation businessEntityVariation) throws KDXException;

   public void createRISharedUserModelMapping (RISharedUserModelMapping riSharedUserModelMapping) throws KDXException;

   public void deleteBusinessEntityVariationsByBedId (Long businessEntityId) throws KDXException;

   public void createBusinessEntityVariations (List<BusinessEntityVariation> businessEntityVariations)
            throws KDXException;

   public void createRIOntoTerm (RIOntoTerm riOnTOTerm) throws KDXException;

   public void deleteRIOntoTerm (RIOntoTerm riOntoTerm) throws KDXException;

   public void updateInstanceRIOntoTermsWithConceptInfo (Long modelGroupId, Long conceptBEDId, String conceptName,
            Long typeBEDId, String typeName) throws KDXException;

   public void deleteInstanceRIOntoTermsByConceptBEDId (Long modelGroupId, Long conceptBEDId) throws KDXException;

   public void deleteRIOntoTermsByProfileBEDId (Long modelId, Long profileBEDId) throws KDXException;

   public void deleteInstanceVariationsForConcept (Long modelId, Long conceptId) throws KDXException;

   public void createHierarchy (Long modelId, Hierarchy hierarchy) throws KDXException;

   public void updateHierarchy (Long modelId, Hierarchy hierarchy) throws KDXException;

   public void deleteHierarchy (Hierarchy hierarchy) throws KDXException;
}
