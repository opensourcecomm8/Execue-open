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

import com.execue.core.common.type.OperationRequestLevel;
import com.execue.core.common.type.SyncRequestLevel;
import com.execue.core.exception.HandlerException;

public interface IJobMaintenanceServiceHandler {

   public Long absorbFileOntology (String filePath, boolean generateRIOntoTerms, boolean generateSFLTerms,
            Long modelId, Long cloudId) throws HandlerException;

   public String getOWLFileStoragePath (String sourceType) throws HandlerException;

   public String getOtherFileStoragePath (String sourceType) throws HandlerException;

   public Long scheduleCorrectMappingMaintenanceJob (Long applicationId, List<Long> assetIds) throws HandlerException;

   public Long schedulePopularityHitMaintenanceJob () throws HandlerException;

   public Long scheduleParallelWordMaintenanceJob () throws HandlerException;

   public Long schedulePopularityCollectionMaintenanceJob (Long applicationId, Long modelId) throws HandlerException;

   public Long schedulePopularityDispersionMaintenanceJob (Long applicationId, Long modelId) throws HandlerException;

   public Long scheduleSflTermTokenWeightMaintenaceJob () throws HandlerException;

   public Long scheduleRIOntoTermPopularityHitMaintenanceJob (Long modelId) throws HandlerException;

   public Long scheduleIndexFormManagementJob (Long applicationId, Long modelId) throws HandlerException;

   public Long scheduleSflWeightUpdationBySecondaryWordMaintenaceJob (Long modelId) throws HandlerException;

   public Long associateConceptWithType (String filePath, Long modelId) throws HandlerException;

   public Long scheduleSDXSynchronizationeJob (Long id, Long applicationId, Long modelId,
            SyncRequestLevel syncRequestLevel, OperationRequestLevel operationRequestLevel) throws HandlerException;

   public Long scheduleOptimalDSetJob (Long id, Long modelId, OperationRequestLevel operationRequestLevel)
            throws HandlerException;

}
