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
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.IBusinessEntity;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.SystemVariable;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.swi.exception.KDXException;

/**
 * @author John Mallavalli
 */
public interface IBaseKDXRetrievalService extends ISWIService {

   public BusinessEntityDefinition getBusinessEntityDefinitionByNames (String conceptName, String instanceName)
            throws KDXException;

   public BusinessEntityDefinition getConceptBEDByName (String conceptName) throws KDXException;

   public BusinessEntityDefinition getRealizedTypeBEDByName (String realizationName) throws KDXException;

   public Concept getConceptByName (String conceptName) throws KDXException;

   public Model getBaseModel () throws KDXException;

   public ModelGroup getBaseGroup () throws KDXException;

   public BusinessEntityDefinition getRelationBEDByName (String relationName) throws KDXException;

   public Set<String> getInstanceTermsForConjuctionConcepts () throws KDXException;

   public BusinessEntityDefinition getTypeBEDByName (String typeName) throws KDXException;

   public List<BusinessEntityDefinition> getBEDInBaseModel (BusinessEntityType businessEntityType) throws KDXException;

   public Map<String, String> getUnitMapByConceptName (String conceptName) throws KDXException;

   public Map<String, String> getOperatorMapByConceptName (String conceptName) throws KDXException;

   public Map<String, String> getOperatorNameToExprMap () throws KDXException;;

   public Set<SystemVariable> getSystemVariables () throws KDXException;

   public boolean isSystemVariableExist (String word, String entityType) throws KDXException;

   public boolean isSystemVariableExist (String word) throws KDXException;

   /**
    * This method is used to validate weather the concept, instance, profile name is a reserved word in the system or
    * not. It checks with both display Name as well as description of the entity.
    * 
    * @param businessEntity
    * @param entityType
    * @throws KDXException
    */
   public void validateEntityForReservedWord (IBusinessEntity businessEntity, BusinessEntityType entityType)
            throws KDXException;

   public RIOntoTerm getOntotermByWordAndTypeName (String word, String typeName) throws KDXException;

   public List<Relation> getBaseRelations () throws KDXException;

   public List<ModelGroup> getBaseAndSystemModelGroups () throws KDXException;

   public Set<Long> getNonRealizableTypeBedIds () throws KDXException;

   /**
    * This method is used to get all the single word words from rionto_term table excluding the regex instances from the
    * base.
    * 
    * @return
    * @throws KDXException
    */
   public List<String> getSecondrayWordsByBaseModel () throws KDXException;
}
