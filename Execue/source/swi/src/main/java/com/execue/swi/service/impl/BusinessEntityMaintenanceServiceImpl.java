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

import java.util.List;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityMaintenanceInfo;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.OperationType;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IBusinessEntityMaintenanceService;

public class BusinessEntityMaintenanceServiceImpl implements IBusinessEntityMaintenanceService {

   private IKDXDataAccessManager kdxDataAccessManager;

   public IKDXDataAccessManager getKdxDataAccessManager () {
      return kdxDataAccessManager;
   }

   public void setKdxDataAccessManager (IKDXDataAccessManager kdxDataAccessManager) {
      this.kdxDataAccessManager = kdxDataAccessManager;
   }

   public void createBusinessEntityMaintenanceInfo (BusinessEntityMaintenanceInfo businessEntityMaintenanceInfo)
            throws KDXException {
      getKdxDataAccessManager().createBusinessEntityMaintenanceInfo(businessEntityMaintenanceInfo);
   }

   public void deleteBusinessEntityMaintenanceDetails (Long modelId) throws KDXException {
      getKdxDataAccessManager().deleteBusinessEntityMaintenanceDetails(modelId);
   }

   public void deleteBusinessEntityMaintenanceDetailsByEntityBedId (Long entityBedId) throws KDXException {
      getKdxDataAccessManager().deleteBusinessEntityMaintenanceDetailsByEntityBedId(entityBedId);

   }

   public void deleteBusinessEntityMaintenanceInstancesByConceptId (Long modelId, Long conceptId) throws KDXException {
      getKdxDataAccessManager().deleteBusinessEntityMaintenanceInstancesByConceptId(modelId, conceptId);
   }

   // TODO : -VG- need to think of deleting the concept.we have to consider other scenarios like parent.
   public void deleteBusinessEntityMaintenanceDetails (Long modelId, List<Long> entityBedIds, EntityType entityType)
            throws KDXException {
      getKdxDataAccessManager().deleteBusinessEntityMaintenanceDetails(modelId, entityBedIds, entityType);
   }

   public void deleteBusinessEntityMaintenanceDetails (Long modelId, EntityType entityType) throws KDXException {
      getKdxDataAccessManager().deleteBusinessEntityMaintenanceDetails(modelId, entityType);
   }

   public List<Long> getBusinessEntityMaintenanceDetails (Long modelId, OperationType operationType,
            EntityType entityType) throws KDXException {
      return getKdxDataAccessManager().getBusinessEntityMaintenanceDetails(modelId, operationType, entityType);
   }

   public List<Long> getBusinessEntityMaintenanceParentDetails (Long modelId, EntityType entityType)
            throws KDXException {
      return getKdxDataAccessManager().getBusinessEntityMaintenanceParentDetails(modelId, entityType);
   }

   public List<Long> getDistinctUpdatedEntityMaintenanceDetails (Long modelId, EntityType entityType)
            throws KDXException {
      return getKdxDataAccessManager().getDistinctUpdatedEntityMaintenanceDetails(modelId, entityType);
   }

   public List<Long> getBusinessEntityMaintenanceDetailsByParentId (Long modelId, OperationType operationType,
            EntityType entityType, Long parentId) throws KDXException {
      return getKdxDataAccessManager().getBusinessEntityMaintenanceDetailsByParentId(modelId, operationType,
               entityType, parentId);
   }

   public List<BusinessEntityDefinition> getDistinctUpdatedMisMatchedEnumerationConceptBEDsFromBEMDetails (Long modelId)
            throws KDXException {
      List<BusinessEntityDefinition> enumerationConceptBEDs = getKdxDataAccessManager()
               .getDistinctUpdatedMisMatchedEnumerationConceptBEDsFromBEMDetails(modelId);
      for (BusinessEntityDefinition bed : enumerationConceptBEDs) {
         bed.getConcept().getName();
         bed.getType().getName();
      }
      return enumerationConceptBEDs;
   }

   public List<Long> getNonSharedBusinessEntityMaintenanceDetailsByParentId (Long modelId, OperationType operationType,
            EntityType entityType, Long parentId) throws KDXException {
      return getKdxDataAccessManager().getNonSharedBusinessEntityMaintenanceDetailsByParentId(modelId, operationType,
               entityType, parentId);
   }

}
