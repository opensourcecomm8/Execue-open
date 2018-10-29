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


package com.execue.platform.swi.impl;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityDetailType;
import com.execue.core.common.bean.entity.EntityNameVariation;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.RecognitionType;
import com.execue.core.util.ExecueStringUtil;
import com.execue.platform.swi.IRIOntoTermAbsorbtionService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXManagementService;

public class RIOntoTermAbsorbtionServiceImpl implements IRIOntoTermAbsorbtionService {

   private IKDXManagementService kdxManagementService;

   public void generateRIOntoTerm (EntityNameVariation variation, BusinessEntityDefinition businessTerm,
            BusinessEntityDefinition parentBusinessTerm, BusinessEntityDefinition typeBusinessTerm,
            EntityDetailType entityDetailType) throws KDXException {
      RIOntoTerm riOntoTerm = new RIOntoTerm();
      riOntoTerm.setWord(variation.getName());
      riOntoTerm.setWordType(variation.getType());
      riOntoTerm.setEntityType(businessTerm.getEntityType());
      riOntoTerm.setModelGroupId(businessTerm.getModelGroup().getId());
      riOntoTerm.setPopularity(1L);
      riOntoTerm.setTypeBEDID(typeBusinessTerm.getId());
      riOntoTerm.setTypeName(typeBusinessTerm.getType().getName());
      if (BusinessEntityType.CONCEPT.equals(businessTerm.getEntityType())) {
         riOntoTerm.setConceptName(businessTerm.getConcept().getName());
         riOntoTerm.setConceptBEDID(businessTerm.getId());
         riOntoTerm.setEntityBEDID(businessTerm.getId());
         if (riOntoTerm.getWordType() == RecognitionType.DisplayName
                  && ExecueStringUtil.compactString(riOntoTerm.getWord()).equals(riOntoTerm.getConceptName())) {
            riOntoTerm.setWordType(RecognitionType.Exact);
         }
         if (entityDetailType != null) {
            riOntoTerm.setDetailTypeBedId(entityDetailType.getDetailTypeBed().getId());
            riOntoTerm.setDetailTypeName(entityDetailType.getDetailTypeBed().getType().getName());
         }
         riOntoTerm.setDefaultConversionType(businessTerm.getConcept().getDefaultConversionType());
         riOntoTerm.setDefaultUnit(businessTerm.getConcept().getDefaultUnit());
         riOntoTerm.setDefaultDataFormat(businessTerm.getConcept().getDefaultDataFormat());

      }
      if (BusinessEntityType.RELATION.equals(businessTerm.getEntityType())) {
         riOntoTerm.setRelationName(businessTerm.getRelation().getName());
         riOntoTerm.setRelationBEDID(businessTerm.getId());
         riOntoTerm.setEntityBEDID(businessTerm.getId());
      }
      if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(businessTerm.getEntityType())) {
         riOntoTerm.setConceptName(parentBusinessTerm.getConcept().getName());
         riOntoTerm.setInstanceName(businessTerm.getInstance().getName());
         riOntoTerm.setConceptBEDID(parentBusinessTerm.getId());
         riOntoTerm.setInstanceBEDID(businessTerm.getId());
         riOntoTerm.setEntityBEDID(businessTerm.getId());
         if (entityDetailType != null) {
            riOntoTerm.setDetailTypeBedId(entityDetailType.getDetailTypeBed().getId());
            riOntoTerm.setDetailTypeName(entityDetailType.getDetailTypeBed().getType().getName());
         }
      }
      if (BusinessEntityType.TYPE_LOOKUP_INSTANCE.equals(businessTerm.getEntityType())) {
         riOntoTerm.setInstanceName(businessTerm.getInstance().getName());
         riOntoTerm.setInstanceBEDID(businessTerm.getId());
         riOntoTerm.setEntityBEDID(businessTerm.getId());
      }
      if (BusinessEntityType.CONCEPT_PROFILE.equals(businessTerm.getEntityType())) {
         riOntoTerm.setProfileName(businessTerm.getConceptProfile().getName());
         riOntoTerm.setProfileBEDID(businessTerm.getId());
         riOntoTerm.setEntityBEDID(businessTerm.getId());
      }
      if (BusinessEntityType.INSTANCE_PROFILE.equals(businessTerm.getEntityType())) {
         riOntoTerm.setConceptName(parentBusinessTerm.getConcept().getName());
         riOntoTerm.setProfileName(businessTerm.getInstanceProfile().getName());
         riOntoTerm.setConceptBEDID(parentBusinessTerm.getId());
         riOntoTerm.setProfileBEDID(businessTerm.getId());
         riOntoTerm.setEntityBEDID(businessTerm.getId());
      }

      riOntoTerm.setKnowledgeId(businessTerm.getKnowledgeId());
      getKdxManagementService().createRIOntoTerm(riOntoTerm);
   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
   }
}
