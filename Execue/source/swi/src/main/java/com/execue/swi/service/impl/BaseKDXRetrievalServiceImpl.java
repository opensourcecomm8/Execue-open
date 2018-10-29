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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.IBusinessEntity;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.SystemVariable;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.util.ExecueStringUtil;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.dataaccess.IBaseKDXDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IBaseKDXRetrievalService;

/**
 * @author John Mallavalli
 */
public class BaseKDXRetrievalServiceImpl implements IBaseKDXRetrievalService {

   private ISWIConfigurationService  swiConfigurationService;
   private IBaseKDXDataAccessManager baseKDXDataAccessManager;

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public BusinessEntityDefinition getBusinessEntityDefinitionByNames (String conceptName, String instanceName)
            throws KDXException {
      try {
         ModelGroup baseModelGroup = getBaseGroup();
         BusinessEntityDefinition businessEntityDefinition = getBaseKDXDataAccessManager()
                  .getBusinessEntityDefinitionByNames(baseModelGroup.getName(), conceptName, instanceName);
         if (businessEntityDefinition != null) {
            ModelGroup modelGroup = businessEntityDefinition.getModelGroup();
            if (modelGroup != null) {
               modelGroup.getId();
               Concept concept = businessEntityDefinition.getConcept();
               if (concept != null) {
                  concept.getName();
                  Instance instance = businessEntityDefinition.getInstance();
                  if (instance != null) {
                     instance.getName();
                  }
               }
            }
         }
         return businessEntityDefinition;
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   public Model getBaseModel () throws KDXException {
      return getBaseKDXDataAccessManager().getBaseModel();
   }

   public BusinessEntityDefinition getConceptBEDByName (String conceptName) throws KDXException {
      Model baseModel = getBaseModel();
      return getBaseKDXDataAccessManager().getConceptBEDByName(baseModel.getId(), conceptName);
   }

   public BusinessEntityDefinition getTypeBEDByName (String typeName) throws KDXException {
      Model baseModel = getBaseModel();
      return getBaseKDXDataAccessManager().getTypeBEDByName(baseModel.getId(), typeName);
   }

   public Concept getConceptByName (String conceptName) throws KDXException {
      Model baseModel = getBaseModel();
      Concept concept = getBaseKDXDataAccessManager().getConceptByName(baseModel.getId(), conceptName);
      return concept;
   }

   public BusinessEntityDefinition getRelationBEDByName (String relationName) throws KDXException {
      Model baseModel = getBaseModel();
      return getBaseKDXDataAccessManager().getRelationBEDByName(baseModel.getId(), relationName);
   }

   public ModelGroup getBaseGroup () throws KDXException {
      return getBaseKDXDataAccessManager().getBaseGroup();
   }

   public List<ModelGroup> getBaseAndSystemModelGroups () throws KDXException {
      return getBaseKDXDataAccessManager().getBaseAndSystemModelGroups();
   }

   /**
    * @return
    * @throws KDXException
    */
   // TODO -NA- we can use instances instead of Ontoterm
   public Set<String> getInstanceTermsForConjuctionConcepts () throws KDXException {
      Set<String> termNames = new HashSet<String>();
      ModelGroup group = getBaseGroup();
      BusinessEntityDefinition ConjBED = getTypeBEDByName("Conjunction");
      BusinessEntityDefinition ByConjBED = getTypeBEDByName("ByConjunction");
      BusinessEntityDefinition cordConjBED = getTypeBEDByName("CoordinatingConjunction");
      // Preposition adjective rangePreposition SubordinatingConjunction
      BusinessEntityDefinition PrepositionBED = getTypeBEDByName("Preposition");
      // BusinessEntityDefinition adjectiveBED = getConceptBEDByName("Adjective");
      BusinessEntityDefinition rangePrepositionBED = getTypeBEDByName("RangePreposition");
      BusinessEntityDefinition punctuationBED = getTypeBEDByName("Punctuation");
      List<Instance> conjTerms = getBaseKDXDataAccessManager().getInstancesForTypeBedId(group.getId(), ConjBED.getId());
      for (Instance instance : conjTerms) {
         termNames.add(instance.getDisplayName().toLowerCase());
      }
      List<Instance> byConjInstances = getBaseKDXDataAccessManager().getInstancesForTypeBedId(group.getId(),
               ByConjBED.getId());
      for (Instance instance : byConjInstances) {
         termNames.add(instance.getDisplayName().toLowerCase());
      }
      List<Instance> cordConjInstances = getBaseKDXDataAccessManager().getInstancesForTypeBedId(group.getId(),
               cordConjBED.getId());
      for (Instance instance : cordConjInstances) {
         termNames.add(instance.getDisplayName().toLowerCase());
      }

      /*
       * List<RIOntoTerm> adjectiveTerms = getBaseKDXDataAccessManager().getInstanceTermsForConceptBedId(group.getId(),
       * adjectiveBED.getId()); for (RIOntoTerm term : adjectiveTerms) { termNames.add(term.getWord().toLowerCase()); }
       */
      List<Instance> preposInstances = getBaseKDXDataAccessManager().getInstancesForTypeBedId(group.getId(),
               PrepositionBED.getId());
      for (Instance instance : preposInstances) {
         termNames.add(instance.getDisplayName().toLowerCase());
      }

      List<Instance> rangePreposInstances = getBaseKDXDataAccessManager().getInstancesForTypeBedId(group.getId(),
               rangePrepositionBED.getId());
      for (Instance instance : rangePreposInstances) {
         termNames.add(instance.getDisplayName().toLowerCase());
      }

      List<Instance> punctuationInstances = getBaseKDXDataAccessManager().getInstancesForTypeBedId(group.getId(),
               punctuationBED.getId());
      for (Instance instance : punctuationInstances) {
         termNames.add(instance.getDisplayName().toLowerCase());
      }

      return termNames;
   }

   public List<BusinessEntityDefinition> getBEDInBaseModel (BusinessEntityType businessEntityType) throws KDXException {
      ModelGroup baseModelGroup = getBaseGroup();
      return getBaseKDXDataAccessManager().getBEDInBaseModel(businessEntityType, baseModelGroup);
   }

   public Map<String, String> getOperatorMapByConceptName (String conceptName) throws KDXException {
      return getBaseKDXDataAccessManager().getOperatorMapByConceptName(conceptName);
   }

   public Map<String, String> getUnitMapByConceptName (String conceptName) throws KDXException {
      return getBaseKDXDataAccessManager().getUnitMapByConceptName(conceptName);
   }

   public Map<String, String> getOperatorNameToExprMap () throws KDXException {
      return getBaseKDXDataAccessManager().getOperatorNameToExprMap();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IBaseKDXRetrievalService#getSystemVariables()
    */
   public Set<SystemVariable> getSystemVariables () throws KDXException {
      return getBaseKDXDataAccessManager().getSystemVariables();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IBaseKDXRetrievalService#isSystemVariableExits(java.lang.String)
    */
   public boolean isSystemVariableExist (String word) throws KDXException {
      boolean isSystemVariable = false;
      // / TODO : -VG- for testing purpose, took off the code as we want to see is there any impact if we allow concepts
      // to be system variables.
      // Set<String> systemVariableWords = getSwiConfigurationService().getSystemVariableWords();
      // if (systemVariableWords.contains(word.toUpperCase())) {
      // isSystemVariable = true;
      // break;
      // }
      return isSystemVariable;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IBaseKDXRetrievalService#isSystemVariableExits(java.lang.String,
    *      java.lang.String)
    */
   public boolean isSystemVariableExist (String word, String Etype) throws KDXException {
      boolean isSystemVariable = false;
      Set<SystemVariable> systemVariable = getSwiConfigurationService().getSystemVariables();
      for (SystemVariable sysVariable : systemVariable) {
         if (sysVariable.getWord().equalsIgnoreCase(word) && sysVariable.getEntityType().equalsIgnoreCase(Etype)) {
            isSystemVariable = true;
            break;
         }
      }
      return isSystemVariable;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IBaseKDXRetrievalService#validateEntityForReservedWord(com.execue.core.common.bean.IBusinessEntity,
    *      com.execue.core.common.type.BusinessEntityType)
    */
   public void validateEntityForReservedWord (IBusinessEntity businessEntity, BusinessEntityType entityType)
            throws KDXException {

      if (BusinessEntityType.CONCEPT.equals(entityType)) {
         Concept concept = (Concept) businessEntity;
         String adjustedEntityName = ExecueStringUtil.getNormalizedName(concept.getDisplayName());
         if (isSystemVariableExist(adjustedEntityName)) {
            throw new KDXException(SWIExceptionCodes.RESERVE_WORD_MATCH, "This concept name[" + adjustedEntityName
                     + "] is a reserve word in the system");
         } else if (!StringUtils.isBlank(concept.getDescription())) {
            // check for not null StringUtils.isBlank()
            if (isSystemVariableExist(concept.getDescription())) {
               throw new KDXException(SWIExceptionCodes.RESERVE_WORD_MATCH, "This concept Description["
                        + concept.getDescription() + "] is a reserve word in the system");
            }
         }
      } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(entityType)) {
         Instance instance = (Instance) businessEntity;
         if (isSystemVariableExist(instance.getDisplayName())) {
            throw new KDXException(SWIExceptionCodes.RESERVE_WORD_MATCH, "This instance name["
                     + instance.getDisplayName() + "] is a reserve word in the system");
         } else if (!StringUtils.isBlank(instance.getDescription())) {
            // check for not null StringUtils.isBlank()
            if (isSystemVariableExist(instance.getDescription())) {
               throw new KDXException(SWIExceptionCodes.RESERVE_WORD_MATCH, "This instance Description["
                        + instance.getDescription() + "] is a reserve word in the system");
            }
         }
      } else if (BusinessEntityType.PROFILE.equals(entityType)) {
         Profile Profile = (Profile) businessEntity;
         if (isSystemVariableExist(Profile.getDisplayName())) {
            throw new KDXException(SWIExceptionCodes.RESERVE_WORD_MATCH, "This profile [" + Profile.getDisplayName()
                     + "] is a reserve word in the system");
         } else if (!StringUtils.isBlank(Profile.getDescription())) {
            // check for not null StringUtils.isBlank()
            if (isSystemVariableExist(Profile.getDescription())) {
               throw new KDXException(SWIExceptionCodes.RESERVE_WORD_MATCH, "This Profile Description["
                        + Profile.getDescription() + "] is a reserve word in the system");
            }
         }
      }
   }

   public BusinessEntityDefinition getRealizedTypeBEDByName (String realizationName) throws KDXException {
      Model baseModel = getBaseModel();
      BusinessEntityDefinition businessEntityDefinition = getBaseKDXDataAccessManager().getRealizedTypeBEDByName(
               baseModel.getId(), realizationName);
      if (businessEntityDefinition != null) {
         ModelGroup modelGroup = businessEntityDefinition.getModelGroup();
         if (modelGroup != null) {
            modelGroup.getId();
            Concept concept = businessEntityDefinition.getConcept();
            if (concept != null) {
               concept.getName();
               Instance instance = businessEntityDefinition.getInstance();
               if (instance != null) {
                  instance.getName();
               }
            }
         }
      }
      return businessEntityDefinition;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IBaseKDXRetrievalService#getOntotermByWordAndTypeName(java.lang.String,
    *      java.lang.String)
    */
   public RIOntoTerm getOntotermByWordAndTypeName (String word, String typeName) throws KDXException {
      return getBaseKDXDataAccessManager().getOntotermByWordAndTypeName(word, typeName);
   }

   public List<Relation> getBaseRelations () throws KDXException {
      return getBaseKDXDataAccessManager().getBaseRelations();
   }

   public Set<Long> getNonRealizableTypeBedIds () throws KDXException {
      return getBaseKDXDataAccessManager().getNonRealizableTypeBedIds();
   }

   public List<String> getSecondrayWordsByBaseModel () throws KDXException {
      return getBaseKDXDataAccessManager().getSecondrayWordsByBaseModel();
   }

   public IBaseKDXDataAccessManager getBaseKDXDataAccessManager () {
      return baseKDXDataAccessManager;
   }

   public void setBaseKDXDataAccessManager (IBaseKDXDataAccessManager baseKDXDataAccessManager) {
      this.baseKDXDataAccessManager = baseKDXDataAccessManager;
   }
}
