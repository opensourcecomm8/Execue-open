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
import com.execue.swi.exception.MappingException;

public interface IMappingManagementService {

   // ********************** Methods related to Mappings starts **************************

   public void createMappings (List<Mapping> mappings) throws MappingException;

   public void persistUIMappings (List<Mapping> mappings) throws MappingException;

   public void updateMapping (Mapping mapping) throws MappingException;

   public void updateMappings (List<Mapping> mappings) throws MappingException;

   public Long updatePrimaryForUniqueMappingsByAsset (Long assetId) throws MappingException;

   public Long updateNonPrimaryForNonUniqueMappingsByBED (Long assetId, Long businessEntityDefinitionId)
            throws MappingException;

   public void saveOrUpdateMappings (List<Mapping> mappings) throws MappingException;

   // ********************** Methods related to Default Metric starts **************************

   public void saveUpdateDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException;

   public void updateDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException;

   public void deleteDefaultMetricsByAedIds (List<Long> aedIds) throws MappingException;

   public void deleteInstanceMappings (Long conceptId, List<Long> modelGroupIds) throws MappingException;

   public void createDefaultMetrics (List<DefaultMetric> targetAssetDefaultMetrics) throws MappingException;
}
