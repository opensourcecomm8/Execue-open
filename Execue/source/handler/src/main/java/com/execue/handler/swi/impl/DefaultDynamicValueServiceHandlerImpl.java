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


package com.execue.handler.swi.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.DefaultDynamicValue;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.type.DynamicValueQualifierType;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.swi.IDefaultDynamicValueServiceHandler;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IDefaultDynamicValueService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class DefaultDynamicValueServiceHandlerImpl implements IDefaultDynamicValueServiceHandler {

   private IDefaultDynamicValueService defaultDynamicValueService;
   private IMappingRetrievalService    mappingRetrievalService;
   private ISDXRetrievalService        sdxRetrievalService;

   public List<UIConcept> getConceptsByType (Long assetId, Long modelId, String typeName) throws HandlerException {
      try {
         List<Mapping> mappedConceptsOfParticularType = getMappingRetrievalService().getMappedConceptsOfParticularType(
                  assetId, modelId, ExecueConstants.TIME_FRAME_TYPE);
         List<BusinessEntityDefinition> businessEntites = new ArrayList<BusinessEntityDefinition>();
         for (Mapping map : mappedConceptsOfParticularType) {
            businessEntites.add(map.getBusinessEntityDefinition());
         }
         return transformIntoUIConcepts(businessEntites);
      } catch (MappingException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<DefaultDynamicValue> getDefaultDynamicValues (Long assetId, Long bedId) throws HandlerException {
      List<DefaultDynamicValue> defaultDynamicList = new ArrayList<DefaultDynamicValue>();
      try {
         defaultDynamicList = getDefaultDynamicValueService().getDefaultDynamicValues(assetId, bedId);
         if (ExecueCoreUtil.isCollectionEmpty(defaultDynamicList)) {
            // this way we will get a default populated dynamic value set with the 1st 2 as its qualifier from the
            // Qualifier enum.
            for (int i = 0; i < 2; i++) {
               DefaultDynamicValue defaultDynamicValue = new DefaultDynamicValue();
               defaultDynamicValue.setAssetId(assetId);
               defaultDynamicValue.setLhsDEDId(bedId);
               List<DynamicValueQualifierType> qualifier = Arrays.asList(DynamicValueQualifierType.values());
               defaultDynamicValue.setQualifier(qualifier.get(i));
               defaultDynamicList.add(defaultDynamicValue);
            }
         }
         return defaultDynamicList;
      } catch (MappingException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
   }

   public void createDefaultDynamicValues (List<DefaultDynamicValue> defaultDynamicValues) throws HandlerException {
      try {
         getDefaultDynamicValueService().createDefaultDynamicValues(defaultDynamicValues);
      } catch (MappingException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<Asset> getAllAssets (Long applicationId) throws HandlerException {
      try {
         return getSdxRetrievalService().getAllAssets(applicationId);
      } catch (SDXException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
   }

   private List<UIConcept> transformIntoUIConcepts (List<BusinessEntityDefinition> concepts) {
      List<UIConcept> uiConcepts = new ArrayList<UIConcept>();
      if (ExecueCoreUtil.isCollectionNotEmpty(concepts)) {
         for (BusinessEntityDefinition bed : concepts) {
            UIConcept uiConcept = new UIConcept();
            uiConcept.setName(bed.getConcept().getName());
            uiConcept.setDisplayName(bed.getConcept().getDisplayName());
            uiConcept.setBedId(bed.getId());
            uiConcept.setId(bed.getConcept().getId());
            uiConcepts.add(uiConcept);
         }
      }
      return uiConcepts;
   }

   public IDefaultDynamicValueService getDefaultDynamicValueService () {
      return defaultDynamicValueService;
   }

   public void setDefaultDynamicValueService (IDefaultDynamicValueService defaultDynamicValueService) {
      this.defaultDynamicValueService = defaultDynamicValueService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

}
