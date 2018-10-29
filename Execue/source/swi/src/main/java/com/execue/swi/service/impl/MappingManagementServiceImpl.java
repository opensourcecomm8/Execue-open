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
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.dataaccess.IMappingDataAccessManager;
import com.execue.swi.service.IMappingManagementService;

public class MappingManagementServiceImpl implements IMappingManagementService {

   private IMappingDataAccessManager mappingDataAccessManager;

   public IMappingDataAccessManager getMappingDataAccessManager () {
      return mappingDataAccessManager;
   }

   public void setMappingDataAccessManager (IMappingDataAccessManager mappingDataAccessManager) {
      this.mappingDataAccessManager = mappingDataAccessManager;
   }

   // ********************** Methods related to Mappings starts **************************

   public void createMappings (List<Mapping> mappings) throws MappingException {
      mappingDataAccessManager.createMappings(mappings);
   }

   public void persistUIMappings (List<Mapping> mappings) throws MappingException {
      mappingDataAccessManager.persistMappings(mappings);
   }

   public void updateMapping (Mapping mapping) throws MappingException {
      mappingDataAccessManager.updateMapping(mapping);
   }

   public void updateMappings (List<Mapping> mappings) throws MappingException {
      mappingDataAccessManager.updateMappings(mappings);
   }

   public Long updatePrimaryForUniqueMappingsByAsset (Long assetId) throws MappingException {
      return getMappingDataAccessManager().updatePrimaryForUniqueMappingsByAsset(assetId);
   }

   public Long updateNonPrimaryForNonUniqueMappingsByBED (Long assetId, Long businessEntityDefinitionId)
            throws MappingException {
      return getMappingDataAccessManager().updateNonPrimaryForNonUniqueMappingsByBED(assetId,
               businessEntityDefinitionId);

   }

   public void saveOrUpdateMappings (List<Mapping> mappings) throws MappingException {
      mappingDataAccessManager.saveOrUpdateMappings(mappings);
   }

   // ********************** Methods related to Default Metric starts **************************

   public void saveUpdateDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException {
      mappingDataAccessManager.saveUpdateDefaultMetrics(defaultMetrics);
   }

   public void updateDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException {
      mappingDataAccessManager.updateDefaultMetrics(defaultMetrics);
   }

   public void deleteDefaultMetricsByAedIds (List<Long> aedIds) throws MappingException {
      mappingDataAccessManager.deleteDefaultMetricsByAedIds(aedIds);
   }

   public void deleteInstanceMappings (Long conceptId, List<Long> modelGroupIds) throws MappingException {
      mappingDataAccessManager.deleteInstanceMappings(conceptId, modelGroupIds);
   }

   public void createDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException {
      mappingDataAccessManager.createDefaultMetrics(defaultMetrics);
   }

}
