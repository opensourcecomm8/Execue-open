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


package com.execue.swi.dataaccess.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.Rule;
import com.execue.core.common.bean.entity.SystemVariable;
import com.execue.core.common.type.AssociationPositionType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.dataaccess.IBaseKDXDataAccessManager;
import com.execue.swi.dataaccess.KDXDAOComponents;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;

public class BaseKDXDataAccessManagerImpl extends KDXDAOComponents implements IBaseKDXDataAccessManager {

   public BusinessEntityDefinition getBusinessEntityDefinitionByNames (String baseGroupName, String conceptName,
            String instanceName) throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getBusinessEntityDefinitionByNames(baseGroupName, conceptName,
                  instanceName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public <BusinessObject extends Serializable> BusinessObject getByField (String fieldValue, String fieldName,
            Class<BusinessObject> clazz) throws SWIException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getByField(fieldValue, fieldName, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public <BusinessObject extends Serializable> BusinessObject getById (Long id, Class<BusinessObject> clazz)
            throws SWIException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getById(id, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<? extends Serializable> getByIds (List<Long> ids, Class<?> clazz) throws SWIException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getByIds(ids, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Model getBaseModel () throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getBaseModel();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getConceptBEDByName (Long baseGroupId, String conceptName) throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getConceptBEDByName(baseGroupId, conceptName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getTypeBEDByName (Long baseGroupId, String typeName) throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getTypeBEDByName(baseGroupId, typeName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getRelationBEDByName (Long baseGroupId, String relationName) throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getRelationBEDByName(baseGroupId, relationName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public ModelGroup getBaseGroup () throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getBaseGroup();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Concept getConceptByName (Long modelId, String conceptName) throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getConceptByName(modelId, conceptName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<RIOntoTerm> getInstanceTermsForConceptBedId (Long modeGrouplId, Long conceptBedId) throws KDXException {
      try {
         return getOntoReverseIndexDAO().getInstanceTermsForConceptBedId(modeGrouplId, conceptBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Instance> getInstancesForTypeBedId (Long modeGrouplId, Long typeBedId) throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getInstancesForTypeBedId(modeGrouplId, typeBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityDefinition> getBEDInBaseModel (BusinessEntityType businessEntityType,
            ModelGroup baseModelGroup) throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getBEDInBaseModel(businessEntityType, baseModelGroup);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Map<String, String> getOperatorMapByConceptName (String conceptName) throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getOperatorMapByConceptName(conceptName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Map<String, String> getUnitMapByConceptName (String conceptName) throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getUnitMapByConceptName(conceptName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Map<String, String> getOperatorNameToExprMap () throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getOperatorNameToExprMap();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Rule> getRulesForBehaviorsIdsAndPosType (List<Long> behaviorIds, AssociationPositionType assocPosType)
            throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getRulesForBehaviorsIdsAndPosType(behaviorIds, assocPosType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Set<SystemVariable> getSystemVariables () throws KDXException {
      Set<SystemVariable> systemVariables = new HashSet<SystemVariable>();
      try {
         systemVariables = getBaseBusinessEntityDefinitionDAO().getSystemVariables();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
      return systemVariables;
   }

   public BusinessEntityDefinition getRealizedTypeBEDByName (Long baseGroupId, String realizationName)
            throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getRealizedTypeBEDByName(baseGroupId, realizationName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public RIOntoTerm getOntotermByWordAndTypeName (String word, String typeName) throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getOntotermByWordAndTypeName(word, typeName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Relation> getBaseRelations () throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getBaseRelations();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<ModelGroup> getBaseAndSystemModelGroups () throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getBaseAndSystemModelGroups();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Set<Long> getNonRealizableTypeBedIds () throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getNonRealizableTypeBedIds();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<String> getSecondrayWordsByBaseModel () throws KDXException {
      try {
         return getBaseBusinessEntityDefinitionDAO().getSecondrayWordsByBaseModel();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }
}
