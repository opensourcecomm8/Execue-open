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

import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.governor.AssetEntityTerm;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.swi.exception.MappingException;

public interface IMappingDeletionService {

   public void deleteBusinessEntityMappings (BusinessEntityTerm businessEntityTerm, Long modelId, Long assetId)
            throws MappingException;

   public void deleteAssetEntityMappings (AssetEntityTerm assetEntityTerm) throws MappingException;

   public void deleteUIMappings (List<Mapping> mappings) throws MappingException;

   public void deleteBusinessEntityMappingForModel (Long modelId) throws MappingException;

   public void deleteDefaultMetrics (Long tableId) throws MappingException;

   public void deleteDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException;

   public void deleteMappingForBusinessEntity (Long entityBedId) throws MappingException;

   public void deleteInstanceMappingsForConcept (Long modelId, Long conceptId) throws MappingException;
}
