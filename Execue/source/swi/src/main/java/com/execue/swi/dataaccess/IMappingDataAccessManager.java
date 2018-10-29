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


package com.execue.swi.dataaccess;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetGrainInfo;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.governor.AssetEntityTerm;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.governor.LightAssetEntityDefinitionInfo;
import com.execue.core.common.type.AssetType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.MappingException;

public interface IMappingDataAccessManager {

   public List<Mapping> getAssetGrain (Long assetId) throws MappingException;

   public List<Mapping> getAssetDefaultMetrics (Long assetId, Integer maxMetricsPerQuery) throws MappingException;

   public <BusinessObject extends Serializable> BusinessObject getById (Long id, Class<BusinessObject> clazz)
            throws MappingException;

   public void createMappings (List<Mapping> mappings) throws MappingException;

   public List<Mapping> getAssetEntities (BusinessEntityTerm businessEntityTerm, Long modelId, Long assetId)
            throws MappingException;

   public List<LightAssetEntityDefinitionInfo> getAssetEntities (Long businessEntityDefinitionId, Long modelId)
            throws MappingException;

   public List<LightAssetEntityDefinitionInfo> getPrimaryAssetEntities (Long businessEntityDefinitionId, Long modelId,
            List<Long> assetIds) throws MappingException;

   public void deleteBusinessEntityMappings (BusinessEntityTerm businessEntityTerm, Long modelId, Long assetId)
            throws MappingException;

   public void deleteAssetEntityMappings (AssetEntityTerm assetEntityTerm) throws MappingException;

   /**
    * Gets all the mappings b/w Concepts,Instances and Columns,Members
    * 
    * @param AssetId
    * @param modelId
    * @return
    * @throws MappingException
    */
   public List<Mapping> getMappings (Long AssetId, Long modelId) throws MappingException;

   /**
    * Deletes the mapping; Its enough to provide the mapping Id for each mapping
    * 
    * @param mappings
    * @throws MappingException
    */
   public void deleteMappings (List<Mapping> mappings) throws MappingException;

   /**
    * Only the mapping will be persisted, but none of the associated model or asset entities Its good enough to just set
    * the Ids of the related entities in the input data structures; Inside the save step, we populate all the entities
    * and the persist them to take care of save cascades that demand the state of the object to be available
    * 
    * @param mappings
    * @throws MappingException
    */
   public void persistMappings (List<Mapping> mappings) throws MappingException;

   public List<Mapping> getMappingsForAED (Long assetEntityDefinitionId) throws MappingException;

   public List<Mapping> getExistingMappingsForColumns (Long assetId) throws MappingException;

   public Mapping getMapping (Long assetEntityDefinitionId, Long businessEntityDefinitionId) throws MappingException;

   public List<Mapping> getMappingsForBED (Long businessEntityDefinitionId) throws MappingException;

   public void updateMapping (Mapping mapping) throws MappingException;

   public void updateMappings (List<Mapping> mappings) throws MappingException;

   public List<Mapping> getExistingMappingsForMembers (Long assetId) throws MappingException;

   public List<Concept> getMappedGrainConcepts (Long assetId) throws MappingException;

   public List<AssetGrainInfo> getGrainConceptsByColumnGranularity (Long assetId) throws MappingException;

   public List<Concept> getMappedDistributionConceptsByEntityBehavior (Long assetId) throws MappingException;

   public List<AssetGrainInfo> getMappedGrainConceptsIncludingDistributionByEntityBehavior (Long assetId)
            throws MappingException;

   public List<Instance> getMappedInstances (Long modelId, Long conceptId, Long assetId) throws DataAccessException;

   public List<AssetGrainInfo> getEligibleDefaultMetrics (Long assetId) throws MappingException;

   public List<AssetGrainInfo> getAssetDefaultMetricsForUI (Long assetId) throws MappingException;

   public List<Mapping> getMappingsForAsset (Long assetId) throws MappingException;

   public List<LightAssetEntityDefinitionInfo> getAssetEntitiesForAsset (Long businessEntityDefinitionId, Long modelId,
            Long assetId) throws MappingException;

   /**
    * Update the Primary Flag to 'Y' for uniquely mapped Business Entities on any given asset
    * 
    * @param assetId
    * @return
    * @throws MappingException
    */
   public Long updatePrimaryForUniqueMappingsByAsset (Long assetId) throws MappingException;

   public Long updateNonPrimaryForNonUniqueMappingsByBED (Long assetId, Long businessEntityDefinitionId)
            throws MappingException;

   /**
    * Get the list of mappings for any given asset which are mapped more than once on the asset
    * 
    * @param assetId
    * @return
    * @throws MappingException
    */
   public List<Long> getNonUniquelyMappedBEDIDsForAsset (Long assetId) throws MappingException;

   /**
    * @param modelId
    * @throws MappingException
    */
   public void deleteBusinessEntityMappingForModel (Long modelId) throws MappingException;

   public void saveOrUpdateMappings (List<Mapping> mappings) throws MappingException;

   public List<Mapping> getMappingsOfConceptsMappedWithDiffInNature (Long assetId) throws MappingException;

   public Long getUnmappedMemberCount (Long columnAEDId) throws MappingException;

   public BusinessEntityDefinition getInstanceMatchedByMemberLookupDescription (Long memberAEDId, Long conceptId)
            throws MappingException;

   public List<Mapping> getMemberMappingsByPage (Long columnAEDId, int pageNumber, int pageSize)
            throws MappingException;

   public List<Mapping> getMemberMappingsOfPageByStartIndex (Long columnAEDId, int startIndex, int pageSize)
            throws MappingException;

   public Long getMappedMemberCount (Long columnAEDId) throws MappingException;

   public Long getMappedMemberCount (Long assetId, Long businessEntityId) throws MappingException;

   public List<Mapping> getMappingsByInstanceDisplayName (Long columId, Long membrId, Long conceptId,
            String instanceDsiplayName) throws MappingException;

   public List<Membr> getUnmappedMembersByBatchAndSize (Long columnId, Long conceptId, Long batchNum, Long batchSize)
            throws MappingException;

   public void saveUpdateDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException;

   public void deleteDefaultMetrics (Long tableId) throws MappingException;

   public List<Mapping> getExistingDefaultMetrics (List<Long> tableIds, Integer maxMetricsPerQuery)
            throws MappingException;

   public List<DefaultMetric> getPossibleDefaultMetrics (Long assetId, Long tableId, Integer maxMetricsPerTable)
            throws MappingException;

   public List<Long> getDistinctFactTablesForBEDIdsByNonPrimaryMappings (List<Long> bedIds, Long assetId)
            throws MappingException;

   public List<DefaultMetric> getInvalidDefaultMetrics (List<Long> tableIds) throws MappingException;

   public void updateDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException;

   public List<DefaultMetric> getAllPossibleDefaultMetrics (Long assetId, Long tableId) throws MappingException;

   public List<DefaultMetric> getInValidExistingDefaultMetrics (Long tableId) throws MappingException;

   public List<DefaultMetric> getValidExistingDefaultMetrics (Long tableId) throws MappingException;

   public void deleteDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException;

   public void deleteDefaultMetricsByAedIds (List<Long> aedIds) throws MappingException;

   public List<Long> getMappedColumnIds (Long assetId, Long tableId) throws MappingException;

   public List<Long> getMappedMemberIds (Long assetId, Long tableId) throws MappingException;

   public List<Mapping> getExistingMappingsByLimit (Long assetId, int limit) throws MappingException;

   public List<Mapping> getAssetGrainByColumnGranularity (Long assetId) throws MappingException;

   public List<Mapping> getMappedConceptsOfParticularType (Long assetId, Long modelId, String typeName)
            throws MappingException;

   public Mapping getMappedConceptForColumn (Long assetId, Long tableId, Long columnId) throws MappingException;

   public Mapping getMappedInstanceForMember (Long assetId, Long tableId, Long memberId) throws MappingException;

   public List<Long> getOtherModelConceptIdsByDisplayNameMappedOnAssetTable (Long modelId, Long conceptId,
            Long assetId, Long tableId, String conceptDisplayName) throws MappingException;

   public List<String> getMappedConceptDisplayNames (Long modelId, Long assetId, Long tableId) throws MappingException;

   public List<Asset> getAssetsByConceptBedIdAndAssetType (Long conceptBedId, AssetType assetType, Long sourceAssetId)
            throws MappingException;

   public Mapping getPrimaryMapping (Long conceptId, Long modelId, Long assetId) throws MappingException;

   public Mapping getPrimaryMapping (Long modelId, Long assetId, Long tableId, Long columnId, Long memberId)
            throws MappingException;

   public Mapping getPrimaryMapping (Long modelId, Long assetId, Long tableId, Long columnId) throws MappingException;

   public List<Mapping> getAssetEntities (BusinessEntityDefinition businessEntityDefinition, Long assetId)
            throws MappingException;

   public List<LightAssetEntityDefinitionInfo> getAssetEntitiesInBaseGroup (Long businessEntityDefinitionId,
            Long modelId) throws MappingException;

   public List<LightAssetEntityDefinitionInfo> getPrimaryAssetEntitiesForAssetsInBaseGroup (
            Long businessEntityDefinitionId, Long modelId, List<Long> assetIds) throws MappingException;

   public List<Long> getMappedInstanceBEDIdsByConceptAndModelId (Long modelId, Long conceptId) throws MappingException;

   public void deleteInstanceMappings (Long conceptId, List<Long> modelGroupIds) throws MappingException;

   public void deleteMappingForBusinessEntity (Long entityBedId) throws MappingException;

   public List<DefaultMetric> getValidDefaultMetricsForAsset (Long assetId, Integer maxMetrics) throws MappingException;

   public List<DefaultMetric> getAllDefaultMetricsForAsset (Long assetId) throws MappingException;

   public Mapping getNonPrimaryMapping (Long conceptId, Long modelId, Long assetId) throws MappingException;

   public void createDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException;

   public List<Mapping> getPrimaryMapping (List<Long> conceptBedIds, Long assetId) throws MappingException;

   public void deleteInstanceMappingsForConcept (Long modelId, Long conceptId) throws MappingException;

   public List<Mapping> getPrimaryMappingForConcepts (Long modelId, Long assetId) throws MappingException;

   public List<Colum> getJoinedColumnsByMappedConcept (Long columnId) throws MappingException;

   public Map<Long, List<Long>> getMappedColumnIDsByConceptBEDID (Long assetId) throws MappingException;

   public List<Long> getMappedColumnIDsForConceptBEDID (Long conceptBEDID, Long assetId) throws MappingException;

   public Map<Long, Long> getMappedMemberIDByInstanceBEDID (Long conceptBEDID, Long assetId, Long pageNumber,
            Long pageSize) throws MappingException;

   public Long getMappedInstanceCount (Long conceptBEDID, Long assetId) throws MappingException;

   public Tabl getStatisticsMappedLookupTableOnCube (Long cubeAssetId, String statisticsConceptName) throws MappingException;

}