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

import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.governor.AssetEntityTerm;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.swi.dataaccess.IMappingDataAccessManager;
import com.execue.swi.exception.MappingException;
import com.execue.swi.service.IMappingDeletionService;

public class MappingDeletionServiceImpl implements IMappingDeletionService {

   private IMappingDataAccessManager mappingDataAccessManager;

   public void deleteBusinessEntityMappings (BusinessEntityTerm businessEntityTerm, Long modelId, Long assetId)
            throws MappingException {
      mappingDataAccessManager.deleteBusinessEntityMappings(businessEntityTerm, modelId, assetId);
   }

   public void deleteAssetEntityMappings (AssetEntityTerm assetEntityTerm) throws MappingException {
      mappingDataAccessManager.deleteAssetEntityMappings(assetEntityTerm);
   }

   public void deleteUIMappings (List<Mapping> mappings) throws MappingException {
      mappingDataAccessManager.deleteMappings(mappings);
   }

   public void deleteBusinessEntityMappingForModel (Long modelId) throws MappingException {
      getMappingDataAccessManager().deleteBusinessEntityMappingForModel(modelId);
   }

   public IMappingDataAccessManager getMappingDataAccessManager () {
      return mappingDataAccessManager;
   }

   public void setMappingDataAccessManager (IMappingDataAccessManager mappingDataAccessManager) {
      this.mappingDataAccessManager = mappingDataAccessManager;
   }

   public void deleteDefaultMetrics (Long tableId) throws MappingException {
      mappingDataAccessManager.deleteDefaultMetrics(tableId);
   }

   public void deleteDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException {
      mappingDataAccessManager.deleteDefaultMetrics(defaultMetrics);
   }

   public void deleteMappingForBusinessEntity (Long entityBedId) throws MappingException {
      mappingDataAccessManager.deleteMappingForBusinessEntity(entityBedId);
   }

   @Override
   public void deleteInstanceMappingsForConcept (Long modelId, Long conceptId) throws MappingException {
      mappingDataAccessManager.deleteInstanceMappingsForConcept(modelId, conceptId);

   }

}
