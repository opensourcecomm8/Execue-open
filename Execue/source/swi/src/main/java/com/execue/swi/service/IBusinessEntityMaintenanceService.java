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

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityMaintenanceInfo;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.OperationType;
import com.execue.swi.exception.KDXException;

public interface IBusinessEntityMaintenanceService {

   public void createBusinessEntityMaintenanceInfo (BusinessEntityMaintenanceInfo businessEntityMaintenanceInfo)
            throws KDXException;

   public void deleteBusinessEntityMaintenanceDetailsByEntityBedId (Long entityBedId) throws KDXException;

   public void deleteBusinessEntityMaintenanceInstancesByConceptId (Long modelId, Long conceptId) throws KDXException;

   public void deleteBusinessEntityMaintenanceDetails (Long modelId) throws KDXException;

   public void deleteBusinessEntityMaintenanceDetails (Long modelId, EntityType entityType) throws KDXException;

   public void deleteBusinessEntityMaintenanceDetails (Long modelId, List<Long> entityBedIds, EntityType entityType)
            throws KDXException;

   public List<Long> getBusinessEntityMaintenanceDetails (Long modelId, OperationType operationType,
            EntityType entityType) throws KDXException;

   public List<Long> getBusinessEntityMaintenanceDetailsByParentId (Long modelId, OperationType operationType,
            EntityType entityType, Long parentId) throws KDXException;

   public List<Long> getDistinctUpdatedEntityMaintenanceDetails (Long modelId, EntityType entityType)
            throws KDXException;

   public List<Long> getBusinessEntityMaintenanceParentDetails (Long modelId, EntityType entityType)
            throws KDXException;

   public List<BusinessEntityDefinition> getDistinctUpdatedMisMatchedEnumerationConceptBEDsFromBEMDetails (Long modelId)
            throws KDXException;

   public List<Long> getNonSharedBusinessEntityMaintenanceDetailsByParentId (Long modelId, OperationType operationType,
            EntityType entityType, Long parentId) throws KDXException;

}
