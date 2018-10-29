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


package com.execue.swi.dataaccess;

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
import com.execue.swi.exception.KDXException;

public interface IBaseKDXDataAccessManager extends ISWIDataAccessManager {

   public BusinessEntityDefinition getBusinessEntityDefinitionByNames (String baseGroupName, String conceptName,
            String instanceName) throws KDXException;

   public Model getBaseModel () throws KDXException;

   public BusinessEntityDefinition getConceptBEDByName (Long baseGroupId, String conceptName) throws KDXException;

   public BusinessEntityDefinition getRelationBEDByName (Long baseGroupId, String relationName) throws KDXException;

   public ModelGroup getBaseGroup () throws KDXException;

   public Concept getConceptByName (Long modelId, String conceptName) throws KDXException;

   public List<RIOntoTerm> getInstanceTermsForConceptBedId (Long modelId, Long conceptBedId) throws KDXException;

   public List<Instance> getInstancesForTypeBedId (Long baseGroupId, Long typeBedId) throws KDXException;

   public BusinessEntityDefinition getTypeBEDByName (Long id, String typeName) throws KDXException;

   public List<BusinessEntityDefinition> getBEDInBaseModel (BusinessEntityType businessEntityType,
            ModelGroup baseModelGroup) throws KDXException;

   public Map<String, String> getOperatorMapByConceptName (String conceptName) throws KDXException;

   public Map<String, String> getUnitMapByConceptName (String conceptName) throws KDXException;

   public Map<String, String> getOperatorNameToExprMap () throws KDXException;

   public List<Rule> getRulesForBehaviorsIdsAndPosType (List<Long> behaviorIds, AssociationPositionType assocPosType)
            throws KDXException;

   public Set<SystemVariable> getSystemVariables () throws KDXException;

   public BusinessEntityDefinition getRealizedTypeBEDByName (Long baseGroupId, String realizationName)
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
   public List<String> getSecondrayWordsByBaseModel () throws KDXException;;

}
