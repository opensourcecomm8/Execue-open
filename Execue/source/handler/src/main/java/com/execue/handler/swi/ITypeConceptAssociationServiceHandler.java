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


package com.execue.handler.swi;

import java.util.List;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.PossibleDetailType;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.OriginType;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIAttribute;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIPathDefinition;
import com.execue.handler.bean.UIRealization;

public interface ITypeConceptAssociationServiceHandler {

   public Type getTypeByName (String typeName) throws HandlerException;

   public BusinessEntityDefinition getTypeById (Long typeId) throws HandlerException;

   public List<Type> getAllTypes () throws HandlerException;

   public List<UIConcept> getRealizedConceptsForType (Long typeId, Long modelId) throws HandlerException;

   public List<Type> getAttributesToBeRealizedForType (Long typeId) throws HandlerException;

   public void associateTypeConcepts (Long modelId, Type type, List<UIConcept> concepts, Type typeToBeRealized,
            List<UIConcept> realizedConcepts) throws HandlerException;

   public List<UIAttribute> getPossibleAttributes (Long typeId) throws HandlerException;

   public List<BehaviorType> getAllPossibleBehaviors (Long typeId) throws HandlerException;

   public List<BehaviorType> getAllBehaviors ();

   public void associateConceptType (Long modelId, Long typeId, Long conceptBEDId, List<BehaviorType> behaviorTypes,
            List<UIPathDefinition> pathDefinitions, List<BehaviorType> savedTypeBehaviors,
            List<UIPathDefinition> savedTypePathDefinitions, boolean create, OriginType originType,
            Long selectedDetailTypeId, boolean isAdvanceSave) throws HandlerException;

   public void associateCRC (Long modelId, BusinessEntityDefinition sourceBED,
            List<UIPathDefinition> selectedCRCPathDefinitions, List<UIPathDefinition> savedCRCPathDefinitions)
            throws HandlerException;

   public List<UIRealization> getAllAttributes (Long modelId, Long typeId) throws HandlerException;

   public List<BehaviorType> getAllEntityBehaviors (Long entityBedId) throws HandlerException;

   public List<UIPathDefinition> getAllEntityDirectPaths (Long modelId, BusinessEntityDefinition entityBed,
            EntityTripleDefinitionType entityTripleDefinitionType) throws HandlerException;

   public BusinessEntityDefinition getBusinessEntityDefinition (Long modelId, Long conceptId) throws HandlerException;

   public List<Relation> getRelationSuggestions (Long modelId, String searchString) throws HandlerException;

   public List<PossibleDetailType> getPossibleDetailTypes (Long typeBedId) throws HandlerException;

   public BusinessEntityDefinition getDetailTypeForConcept (Long conceptBedId) throws HandlerException;

   public List<Type> getAllVisibleTypes () throws HandlerException;

   public List<UIRealization> getAllNonAttributeConcepts (Long modelId) throws HandlerException;

   public List<String> getBaseRelationsName ();
}
