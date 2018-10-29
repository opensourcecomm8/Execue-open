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
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.governor.LightAssetEntityDefinitionInfo;
import com.execue.core.common.type.AssetType;
import com.execue.swi.exception.MappingException;

public interface IMappingRetrievalService {

   // ******************************** Methods related to Asset*******************************************

   public List<Asset> getAssetsByConceptBedIdAndAssetType (Long conceptBedId, AssetType assetType, Long sourceAssetId)
            throws MappingException;

   // *********************** Methods related to Mappings starts********************************************

   public Mapping getMapping (Long mappingId) throws MappingException;

   public List<Mapping> getUIMappings (Long assetId, Long modelId) throws MappingException;

   public List<Mapping> getMappingsForAED (Long assetEntityDefinitionId) throws MappingException;

   public List<Mapping> getExistingMappingsForColumns (Long assetId) throws MappingException;

   public Mapping getMapping (Long assetEntityDefinitionId, Long businessEntityDefinitionId) throws MappingException;

   public List<Mapping> getMappingsForBED (Long businessEntityDefinitionId) throws MappingException;

   public List<Mapping> getMappingsForAsset (Long assetId) throws MappingException;

   public List<Mapping> getExistingMappingsForMembers (Long assetId) throws MappingException;

   public List<Mapping> getAssetDefaultMetrics (Long assetId, Integer maxMetricsPerQuery) throws MappingException;

   public List<Long> getNonUniquelyMappedBEDIDsForAsset (Long assetId) throws MappingException;

   public List<Mapping> getMappingsOfConceptsMappedWithDiffInNature (Long assetId) throws MappingException;

   public List<Mapping> getMemberMappingsByPage (Long columnAEDId, int pageNumber, int pageSize)
            throws MappingException;

   public List<Mapping> getMemberMappingsOfPageByStartIndex (Long columnAEDId, int startIndex, int pageSize)
            throws MappingException;

   public List<Mapping> getMappingsByInstanceDisplayName (Long columId, Long membrId, Long conceptId,
            String instanceDsiplayName) throws MappingException;

   public List<Mapping> getExistingDefaultMetrics (List<Long> tableIds, Integer maxMetricsPerQuery)
            throws MappingException;

   public List<Mapping> getExistingMappingsByLimit (Long assetId, int limit) throws MappingException;

   public List<Mapping> getMappedConceptsOfParticularType (Long assetId, Long modelId, String typeName)
            throws MappingException;

   public List<Mapping> getPrimaryMapping (List<Long> conceptBedIds, Long assetId) throws MappingException;

   public Mapping getPrimaryMapping (Long conceptId, Long modelId, Long assetId) throws MappingException;

   public Mapping getNonPrimaryMapping (Long conceptId, Long modelId, Long assetId) throws MappingException;

   public Mapping getPrimaryMapping (Long modelId, Long assetId, Long tableId, Long columnId) throws MappingException;

   public Mapping getPrimaryMapping (Long modelId, Long assetId, Long tableId, Long columnId, Long memberId)
            throws MappingException;

   public List<Mapping> getDistributionConcepts (Long modelId, Long assetId) throws MappingException;

   public List<Mapping> getAssetGrainByColumnGranularity (Long assetId) throws MappingException;

   public List<Mapping> getAssetGrain (Long assetId) throws MappingException;

   public List<Mapping> getAssetEntities (BusinessEntityDefinition businessEntityDefinition, Long assetId)
            throws MappingException;

   // ************************** Methods related to BED'S Starts***************************************************

   public BusinessEntityDefinition getMappedConceptBEDForColumn (Long assetId, Long tableId, Long columnId)
            throws MappingException;

   public BusinessEntityDefinition getMappedInstanceForMember (Long assetId, Long tableId, Long memberId)
            throws MappingException;

   public BusinessEntityDefinition getInstanceMatchedByMemberLookupDescription (Long memberAEDId, Long conceptId)
            throws MappingException;

   public List<Long> getDistinctFactTablesForBEDIdsByNonPrimaryMappings (List<Long> bedIds, Long assetId)
            throws MappingException;

   // ************************** Methods related to AssetGrainInfo starts *********************************************

   public List<AssetGrainInfo> getGrainConcepts (Long modelId, Long assetId) throws MappingException;

   public List<AssetGrainInfo> getEligibleDefaultMetrics (Long assetId) throws MappingException;

   public List<AssetGrainInfo> getAssetDefaultMetricsForUI (Long assetId) throws MappingException;

   // ************************** Methods related to Concept starts *********************************************

   public Concept getMappedConceptForColumn (Long assetId, Long tableId, Long columnId) throws MappingException;

   public List<String> getMappedConceptDisplayNames (Long modelId, Long assetId, Long tableId) throws MappingException;

   public boolean isModelConceptNameChangeUniqueOnMappedAssetTable (Long modelId, Long conceptId, Long assetId,
            Long tableId, String conceptDisplayName) throws MappingException;

   // *************************Methods related to Instance ******************************************************

   public List<Instance> getMappedInstances (Long modelId, Long conceptId, Long assetId) throws MappingException;

   // ************************** Methods related to AED'S starts *********************************************

   public List<LightAssetEntityDefinitionInfo> getAssetEntitiesForAllAssets (Long businessEntityDefinitionId,
            Long modelId) throws MappingException;

   public List<LightAssetEntityDefinitionInfo> getAssetEntitiesForAllAssetsInBaseGroup (
            Long businessEntityDefinitionId, Long modelId) throws MappingException;

   public List<LightAssetEntityDefinitionInfo> getPrimaryAssetEntitiesForAssets (Long businessEntityDefinitionId,
            Long modelId, List<Long> assetIds) throws MappingException;

   public List<LightAssetEntityDefinitionInfo> getPrimaryAssetEntitiesForAssetsInBaseGroup (
            Long businessEntityDefinitionId, Long modelId, List<Long> assetIds) throws MappingException;

   public List<LightAssetEntityDefinitionInfo> getAssetEntitiesForAsset (Long id, Long modelId, Long assetId)
            throws MappingException;

   public List<Mapping> getAssetEntities (BusinessEntityTerm businessEntityTerm, Long modelId, Long assetId)
            throws MappingException;

   // ************************** Methods related to DefaultMetric starts *********************************************

   // ***********************************Methods related to DefaultMetric*********************************

   public List<DefaultMetric> getValidDefaultMetricsForAsset (Long assetId, Integer maxMetrics) throws MappingException;

   public List<DefaultMetric> getAllDefaultMetricsForAsset (Long assetId) throws MappingException;

   public List<DefaultMetric> getInValidExistingDefaultMetrics (Long tableId) throws MappingException;

   public List<DefaultMetric> getValidExistingDefaultMetrics (Long tableId) throws MappingException;

   public List<DefaultMetric> getAllExistingDefaultMetrics (Long tableId) throws MappingException;

   public List<DefaultMetric> getPossibleDefaultMetrics (Long assetId, Long tableId, Integer maxMetricsPerTable)
            throws MappingException;

   public List<DefaultMetric> getInvalidDefaultMetrics (List<Long> tableIds) throws MappingException;

   public List<DefaultMetric> getAllPossibleDefaultMetrics (Long assetId, Long tableId) throws MappingException;

   // ************************** Methods related to Member starts *********************************************

   public List<Long> getMappedColumnIds (Long assetId, Long tableId) throws MappingException;

   // ************************** Methods related to Member starts *********************************************

   public Long getUnmappedMemberCount (Long columnAEDId) throws MappingException;

   public Long getMappedMemberCount (Long columnAEDId) throws MappingException;

   public Long getMappedMemberCount (Long assetId, Long businessEntityId) throws MappingException;

   public List<Membr> getUnmappedMembersByBatchAndSize (Long columnId, Long conceptId, Long batchNum, Long batchSize)
            throws MappingException;

   public List<Long> getMappedMemberIds (Long assetId, Long tableId) throws MappingException;

   public List<Instance> getUnmappedInstances (Long modelId, Long conceptId) throws MappingException;

   public BusinessEntityDefinition getMatchedInstanceBedByMemberLookupDescription (Long modelId, Concept concept,
            String memberLookupDesc) throws MappingException;

   public List<Mapping> getPrimaryMappingsForConcepts (Long modelId, Long assetId) throws MappingException;

   /**
    * Get a List of Columns which a mapped to the Same Concept that is mapped to the given Column Id
    *  
    * @param columnId
    * @return
    * @throws MappingException
    */
   public List<Colum> getJoinedColumnsByMappedConcept (Long columnId) throws MappingException;

   /**
    * Get a Map of ConceptBEDID as Key and List of Mapped Column IDs of a given Asset ID
    * 
    * @param assetId
    * @return
    * @throws MappingException
    */
   public Map<Long, List<Long>> getMappedColumnIDsByConceptBEDID (Long assetId) throws MappingException;

   /**
    * Get all the Column IDs mapped to the ConceptBEDID passed in for the given asset
    * 
    * @param conceptBEDID
    * @param assetId
    * @return
    * @throws MappingException
    */
   public List<Long> getMappedColumnIDsForConceptBEDID (Long conceptBEDID, Long assetId) throws MappingException;

   /**
    * Get the Instance BED ID and Member ID map for a given concept BED ID against the asset ID by page
    * 
    * @param conceptBEDID
    * @param assetId
    * @param pageNumber
    * @param pageSize
    * @return
    * @throws MappingException
    */
   public Map<Long, Long> getMappedMemberIDByInstanceBEDID (Long conceptBEDID, Long assetId, Long pageNumber,
            Long pageSize) throws MappingException;
   
   /**
    * Get Mapped Instance Count for the given ConceptBEDID and Asset ID
    * 
    * @param conceptBEDID
    * @param assetId
    * @return
    * @throws MappingException
    */
   public Long getMappedInstanceCount (Long conceptBEDID, Long assetId) throws MappingException;
   
   public Tabl getStatisticsMappedLookupTableOnCube (Long cubeAssetId) throws MappingException;
}
