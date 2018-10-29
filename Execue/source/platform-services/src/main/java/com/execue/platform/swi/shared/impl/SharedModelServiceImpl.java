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


package com.execue.platform.swi.shared.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.platform.IRealTimeBusinessEntityWrapperService;
import com.execue.platform.swi.shared.ISharedModelService;
import com.execue.sdata.exception.SharedModelException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingManagementService;

public abstract class SharedModelServiceImpl implements ISharedModelService {

   private IKDXRetrievalService                  kdxRetrievalService;

   private IMappingManagementService             mappingManagementService;

   private IRealTimeBusinessEntityWrapperService realTimeBusinessEntityWrapperService;

   private IBaseKDXRetrievalService              baseKDXRetrievalService;

   protected abstract ModelGroup getModelGroup () throws SharedModelException;

   protected abstract Model getModel () throws SharedModelException;

   private List<Long> getModelGroupIds (ModelGroup modelGroup) {
      List<Long> modelGroupIds = new ArrayList<Long>();
      modelGroupIds.add(modelGroup.getId());
      return modelGroupIds;
   }

   public void deleteInstanceMappings (Long conceptId) throws SharedModelException {
      List<Long> modelGroupIds = getModelGroupIds(getModelGroup());
      try {
         getMappingManagementService().deleteInstanceMappings(conceptId, modelGroupIds);
      } catch (MappingException e) {
         throw new SharedModelException(e.getCode(), e);
      }
   }

   public List<BusinessEntityDefinition> getInstanceBedsByConceptId (Long conceptId) throws SharedModelException {
      List<Long> modelGroupIds = getModelGroupIds(getModelGroup());
      try {
         return getKdxRetrievalService().getBedByModelGroupsAndConceptId(modelGroupIds, conceptId);
      } catch (KDXException e) {
         throw new SharedModelException(e.getCode(), e);
      }
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IMappingManagementService getMappingManagementService () {
      return mappingManagementService;
   }

   public void setMappingManagementService (IMappingManagementService mappingManagementService) {
      this.mappingManagementService = mappingManagementService;
   }

   public IRealTimeBusinessEntityWrapperService getRealTimeBusinessEntityWrapperService () {
      return realTimeBusinessEntityWrapperService;
   }

   public void setRealTimeBusinessEntityWrapperService (
            IRealTimeBusinessEntityWrapperService realTimeBusinessEntityWrapperService) {
      this.realTimeBusinessEntityWrapperService = realTimeBusinessEntityWrapperService;
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

}
