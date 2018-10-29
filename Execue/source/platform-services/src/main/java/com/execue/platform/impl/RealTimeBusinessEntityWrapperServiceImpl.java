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


package com.execue.platform.impl;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.type.OperationType;
import com.execue.platform.IRealTimeBusinessEntityWrapperService;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.swi.IBusinessEntityManagementWrapperService;
import com.execue.platform.swi.IIndexFormManagementService;
import com.execue.platform.unstructured.IUnstructuredWarehouseManagementWrapperService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;

public class RealTimeBusinessEntityWrapperServiceImpl implements IRealTimeBusinessEntityWrapperService {

   private IKDXManagementService                          kdxManagementService;
   private IBusinessEntityManagementWrapperService        businessEntityManagementWrapperService;
   private IIndexFormManagementService                    indexFormManagementService;
   private IUnstructuredWarehouseManagementWrapperService unstructuredWarehouseManagementWrapperService;
   private IBusinessEntityMaintenanceService              businessEntityMaintenanceService;
   private IKDXRetrievalService                           kdxRetrievalService;
   private IApplicationRetrievalService                   applicationRetrievalService;

   @Override
   public void createInstance (Long modelId, Long conceptId, Instance instance) throws PlatformException {
      try {
         BusinessEntityDefinition instanceBed = getBusinessEntityManagementWrapperService().createInstance(modelId,
                  conceptId, instance);
         BusinessEntityDefinition conceptBed = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                  conceptId, null);
         getIndexFormManagementService().manageIndexForms(modelId, instanceBed.getId(), conceptBed.getId(),
                  OperationType.ADD);
         getUnstructuredWarehouseManagementWrapperService().manageUnstructuredWarehouse(
                  getApplicationIdByModelId(modelId), instanceBed.getId(), conceptBed.getId(), OperationType.ADD);
         getBusinessEntityMaintenanceService().deleteBusinessEntityMaintenanceDetailsByEntityBedId(instanceBed.getId());
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public Long createTypeInstance (Long modelId, Long typeModelGroupId, Long typeId, Instance instance)
            throws PlatformException {
      try {
         BusinessEntityDefinition instanceBed = getBusinessEntityManagementWrapperService().createTypeInstance(modelId,
                  typeModelGroupId, typeId, instance);
         BusinessEntityDefinition typeBed = getKdxRetrievalService().getBusinessEntityDefinitionByTypeIds(
                  typeModelGroupId, typeId, null);
         getIndexFormManagementService().manageIndexForms(modelId, instanceBed.getId(), typeBed.getId(),
                  OperationType.ADD);
         getBusinessEntityMaintenanceService().deleteBusinessEntityMaintenanceDetailsByEntityBedId(instanceBed.getId());
         return instanceBed.getId();
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   @Override
   public void updateInstance (Long modelId, Long conceptId, Instance instance) throws PlatformException {
      try {
         BusinessEntityDefinition instanceBed = getKdxManagementService().updateInstance(modelId, conceptId, instance);
         BusinessEntityDefinition conceptBed = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                  conceptId, null);
         getIndexFormManagementService().manageIndexForms(modelId, instanceBed.getId(), conceptBed.getId(),
                  OperationType.UPDATE);
         getUnstructuredWarehouseManagementWrapperService().manageUnstructuredWarehouse(
                  getApplicationIdByModelId(modelId), instanceBed.getId(), conceptBed.getId(), OperationType.UPDATE);
         getBusinessEntityMaintenanceService().deleteBusinessEntityMaintenanceDetailsByEntityBedId(instanceBed.getId());
      } catch (KDXException e) {
         throw new PlatformException(e.getCode(), e);
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
   }

   public IBusinessEntityManagementWrapperService getBusinessEntityManagementWrapperService () {
      return businessEntityManagementWrapperService;
   }

   public void setBusinessEntityManagementWrapperService (
            IBusinessEntityManagementWrapperService businessEntityManagementWrapperService) {
      this.businessEntityManagementWrapperService = businessEntityManagementWrapperService;
   }

   public IIndexFormManagementService getIndexFormManagementService () {
      return indexFormManagementService;
   }

   public void setIndexFormManagementService (IIndexFormManagementService indexFormManagementService) {
      this.indexFormManagementService = indexFormManagementService;
   }

   public IBusinessEntityMaintenanceService getBusinessEntityMaintenanceService () {
      return businessEntityMaintenanceService;
   }

   public void setBusinessEntityMaintenanceService (IBusinessEntityMaintenanceService businessEntityMaintenanceService) {
      this.businessEntityMaintenanceService = businessEntityMaintenanceService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   private Long getApplicationIdByModelId (Long modelId) throws KDXException {
      return getApplicationRetrievalService().getApplicationByModelId(modelId).getId();
   }

   /**
    * @return the unstructuredWarehouseManagementWrapperService
    */
   public IUnstructuredWarehouseManagementWrapperService getUnstructuredWarehouseManagementWrapperService () {
      return unstructuredWarehouseManagementWrapperService;
   }

   /**
    * @param unstructuredWarehouseManagementWrapperService the unstructuredWarehouseManagementWrapperService to set
    */
   public void setUnstructuredWarehouseManagementWrapperService (
            IUnstructuredWarehouseManagementWrapperService unstructuredWarehouseManagementWrapperService) {
      this.unstructuredWarehouseManagementWrapperService = unstructuredWarehouseManagementWrapperService;
   }

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }
}
