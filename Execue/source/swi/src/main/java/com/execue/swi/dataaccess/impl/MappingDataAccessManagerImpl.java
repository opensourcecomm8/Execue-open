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


package com.execue.swi.dataaccess.impl;

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
import com.execue.swi.dataaccess.IMappingDataAccessManager;
import com.execue.swi.dataaccess.MappingDAOComponents;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SWIExceptionCodes;

public class MappingDataAccessManagerImpl extends MappingDAOComponents implements IMappingDataAccessManager {

   public <BusinessObject extends Serializable> BusinessObject getById (Long id, Class<BusinessObject> clazz)
            throws MappingException {
      try {
         return getMappingDAO().getById(id, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void createMappings (List<Mapping> mappings) throws MappingException {
      try {
         getMappingDAO().createMappings(mappings);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void saveOrUpdateMappings (List<Mapping> mappings) throws MappingException {
      try {
         getMappingDAO().saveOrUpdateAll(mappings);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteAssetEntityMappings (AssetEntityTerm assetEntityTerm) throws MappingException {
      try {
         getMappingDAO().deleteAssetEntityMappings(assetEntityTerm);
      } catch (DataAccessException dae) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   public void deleteBusinessEntityMappings (BusinessEntityTerm businessEntityTerm, Long modelId, Long assetId)
            throws MappingException {
      try {
         getMappingDAO().deleteBusinessEntityMappings(businessEntityTerm, modelId, assetId);
      } catch (DataAccessException dae) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   public List<Mapping> getAssetEntities (BusinessEntityTerm businessEntityTerm, Long modelId, Long assetId)
            throws MappingException {
      try {
         return getMappingDAO().getAssetEntities(businessEntityTerm, modelId, assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Mapping> getAssetEntities (BusinessEntityDefinition businessEntityDefinition, Long assetId)
            throws MappingException {
      try {
         return getMappingDAO().getAssetEntities(businessEntityDefinition, assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<LightAssetEntityDefinitionInfo> getAssetEntitiesInBaseGroup (Long businessEntityDefinitionId,
            Long modelId) throws MappingException {
      try {
         return getMappingDAO().getAssetEntitiesForAllAssetsInBaseGroup(businessEntityDefinitionId, modelId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteMappings (List<Mapping> mappings) throws MappingException {
      try {
         getMappingDAO().deleteMappings(mappings);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Mapping> getMappings (Long assetId, Long modelId) throws MappingException {
      try {
         return getMappingDAO().getMappings(assetId, modelId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void persistMappings (List<Mapping> mappings) throws MappingException {
      try {
         getMappingDAO().persistMappings(mappings);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Mapping> getMappingsForAED (Long assetEntityDefinitionId) throws MappingException {
      try {
         return getMappingDAO().getMappingsForAED(assetEntityDefinitionId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Mapping> getExistingMappingsForColumns (Long assetId) throws MappingException {
      try {
         return getMappingDAO().getExistingMappingsForColumns(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public Mapping getMapping (Long assetEntityDefinitionId, Long businessEntityDefinitionId) throws MappingException {
      try {
         return getMappingDAO().getMapping(assetEntityDefinitionId, businessEntityDefinitionId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Mapping> getMappingsForBED (Long businessEntityDefinitionId) throws MappingException {
      try {
         return getMappingDAO().getMappingsForBED(businessEntityDefinitionId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void updateMapping (Mapping mapping) throws MappingException {
      try {
         getMappingDAO().update(mapping);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void updateMappings (List<Mapping> mappings) throws MappingException {
      try {
         getMappingDAO().saveOrUpdateAll(mappings);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Mapping> getExistingMappingsForMembers (Long assetId) throws MappingException {
      try {
         return getMappingDAO().getExistingMappingsForMembers(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Mapping> getAssetGrain (Long assetId) throws MappingException {
      try {
         return getMappingDAO().getAssetGrain(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Mapping> getAssetGrainByColumnGranularity (Long assetId) throws MappingException {
      try {
         return getMappingDAO().getAssetGrainByColumnGranularity(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Mapping> getAssetDefaultMetrics (Long assetId, Integer maxMetricsPerQuery) throws MappingException {
      try {
         return getMappingDAO().getAssetDefaultMetrics(assetId, maxMetricsPerQuery);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Concept> getMappedGrainConcepts (Long assetId) throws MappingException {
      try {
         return getMappingDAO().getMappedGrainConcepts(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<AssetGrainInfo> getGrainConceptsByColumnGranularity (Long assetId) throws MappingException {
      try {
         return getMappingDAO().getGrainConceptsByColumnGranularity(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<AssetGrainInfo> getMappedGrainConceptsIncludingDistributionByEntityBehavior (Long assetId)
            throws MappingException {
      try {
         return getMappingDAO().getMappedGrainConceptsIncludingDistributionByEntityBehavior(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Concept> getMappedDistributionConceptsByEntityBehavior (Long assetId) throws MappingException {
      try {
         return getMappingDAO().getMappedDistributionConceptsByEntityBehavior(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<LightAssetEntityDefinitionInfo> getAssetEntities (Long businessEntityDefinitionId, Long modelId)
            throws MappingException {
      try {
         return getMappingDAO().getAssetEntitiesForAllAssets(businessEntityDefinitionId, modelId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<LightAssetEntityDefinitionInfo> getAssetEntitiesForAsset (Long businessEntityDefinitionId, Long modelId,
            Long assetId) throws MappingException {
      try {
         return getMappingDAO().getAssetEntitiesForAsset(businessEntityDefinitionId, modelId, assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<LightAssetEntityDefinitionInfo> getPrimaryAssetEntities (Long businessEntityDefinitionId, Long modelId,
            List<Long> assetIds) throws MappingException {
      try {
         return getMappingDAO().getPrimaryAssetEntitiesForAssets(businessEntityDefinitionId, modelId, assetIds);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<LightAssetEntityDefinitionInfo> getPrimaryAssetEntitiesForAssetsInBaseGroup (
            Long businessEntityDefinitionId, Long modelId, List<Long> assetIds) throws MappingException {
      try {
         return getMappingDAO().getPrimaryAssetEntitiesForAssetsInBaseGroup(businessEntityDefinitionId, modelId,
                  assetIds);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Instance> getMappedInstances (Long modelId, Long conceptId, Long assetId) throws DataAccessException {
      return getMappingDAO().getMappedInstances(modelId, conceptId, assetId);
   }

   public List<AssetGrainInfo> getEligibleDefaultMetrics (Long assetId) throws MappingException {
      try {
         return getDefaultMetricDAO().getEligibleDefaultMetrics(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);

      }
   }

   public List<AssetGrainInfo> getAssetDefaultMetricsForUI (Long assetId) throws MappingException {
      try {
         return getDefaultMetricDAO().getAssetDefaultMetricsForUI(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Mapping> getMappingsForAsset (Long assetId) throws MappingException {
      try {
         return getMappingDAO().getMappingsForAsset(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IMappingDataAccessManager#updatePrimaryForUniqueMappingsByAsset(java.lang.Long)
    */
   public Long updatePrimaryForUniqueMappingsByAsset (Long assetId) throws MappingException {
      try {
         return getMappingDAO().updatePrimaryForUniqueMappingsByAsset(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.UPDATE_PRIMARY_FOR_UNIQUELY_MAPPED_BEDS_FOR_ASSET_FAILED,
                  dataAccessException);
      }
   }

   public Long updateNonPrimaryForNonUniqueMappingsByBED (Long assetId, Long businessEntityDefinitionId)
            throws MappingException {
      try {
         return getMappingDAO().updateNonPrimaryForNonUniqueMappingsByBED(assetId, businessEntityDefinitionId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.UPDATE_NON_PRIMARY_FOR_NON_UNIQUE_MAPPINGS_FOR_BED_FAILED,
                  dataAccessException);
      }
   }

   public List<Long> getNonUniquelyMappedBEDIDsForAsset (Long assetId) throws MappingException {
      try {
         return getMappingDAO().getNonUniquelyMappedBEDIDsForAsset(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.QUERY_NON_UNIQUE_MAPPING_FOR_ASSET_FAILED, dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IMappingDataAccessManager#deleteBusinessEntityMappingForModel(java.lang.Long)
    */
   public void deleteBusinessEntityMappingForModel (Long modelId) throws MappingException {
      try {
         getMappingDAO().deleteBusinessEntityMappingForModel(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DELETE_MAPPINGS_FOR_MODEL_FAILED, dataAccessException);
      }

   }

   public List<Mapping> getMappingsOfConceptsMappedWithDiffInNature (Long assetId) throws MappingException {
      try {
         return getMappingDAO().getMappingsOfConceptsMappedWithDiffInNature(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Long getUnmappedMemberCount (Long columnAEDId) throws MappingException {
      try {
         return getMappingDAO().getUnmappedMemberCount(columnAEDId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getInstanceMatchedByMemberLookupDescription (Long memberAEDId, Long conceptId)
            throws MappingException {
      try {
         return getMappingDAO().getInstanceMatchedByMemberLookupDescription(memberAEDId, conceptId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Membr> getUnmappedMembersByBatchAndSize (Long columnId, Long conceptId, Long batchNum, Long batchSize)
            throws MappingException {
      try {
         return getMappingDAO().getUnmappedMembersByBatchAndSize(columnId, conceptId, batchNum, batchSize);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Mapping> getMemberMappingsByPage (Long columnAEDId, int pageNumber, int pageSize)
            throws MappingException {
      try {
         return getMappingDAO().getMemberMappingsByPage(columnAEDId, pageNumber, pageSize);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Mapping> getMemberMappingsOfPageByStartIndex (Long columnAEDId, int startIndex, int pageSize)
            throws MappingException {
      try {
         return getMappingDAO().getMemberMappingsOfPageByStartIndex(columnAEDId, startIndex, pageSize);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Long getMappedMemberCount (Long columnAEDId) throws MappingException {
      try {
         return getMappingDAO().getMappedMemberCount(columnAEDId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Long getMappedMemberCount (Long assetId, Long businessEntityId) throws MappingException {
      try {
         return getMappingDAO().getMappedMemberCount(assetId, businessEntityId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Mapping> getMappingsByInstanceDisplayName (Long columId, Long membrId, Long conceptId,
            String instanceDsiplayName) throws MappingException {
      try {
         return getMappingDAO().getMappingsByInstanceDisplayName(columId, membrId, conceptId, instanceDsiplayName);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void saveUpdateDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException {
      try {
         getMappingDAO().saveOrUpdateAll(defaultMetrics);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteDefaultMetrics (Long tableId) throws MappingException {
      try {
         getDefaultMetricDAO().deleteDefaultMetrics(tableId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Mapping> getExistingDefaultMetrics (List<Long> tableIds, Integer maxMetricsPerQuery)
            throws MappingException {
      try {
         return getMappingDAO().getExistingDefaultMetrics(tableIds, maxMetricsPerQuery);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<DefaultMetric> getPossibleDefaultMetrics (Long assetId, Long tableId, Integer maxMetricsPerTable)
            throws MappingException {
      try {
         return getDefaultMetricDAO().getPossibleDefaultMetrics(assetId, tableId, maxMetricsPerTable);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getDistinctFactTablesForBEDIdsByNonPrimaryMappings (List<Long> bedIds, Long assetId)
            throws MappingException {
      try {
         return getMappingDAO().getDistinctFactTablesForBEDIdsByNonPrimaryMappings(bedIds, assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }

   }

   public List<DefaultMetric> getInvalidDefaultMetrics (List<Long> tableIds) throws MappingException {
      try {
         return getDefaultMetricDAO().getInvalidDefaultMetrics(tableIds);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void updateDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException {
      try {
         getDefaultMetricDAO().updateAll(defaultMetrics);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<DefaultMetric> getAllPossibleDefaultMetrics (Long assetId, Long tableId) throws MappingException {
      try {
         return getDefaultMetricDAO().getAllPossibleDefaultMetrics(assetId, tableId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException {
      try {
         getDefaultMetricDAO().deleteAll(defaultMetrics);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteDefaultMetricsByAedIds (List<Long> aedIds) throws MappingException {
      try {
         getDefaultMetricDAO().deleteDefaultMetricsByAedIds(aedIds);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<DefaultMetric> getInValidExistingDefaultMetrics (Long tableId) throws MappingException {
      try {
         return getDefaultMetricDAO().getInValidExistingDefaultMetrics(tableId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<DefaultMetric> getValidExistingDefaultMetrics (Long tableId) throws MappingException {
      try {
         return getDefaultMetricDAO().getValidExistingDefaultMetrics(tableId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getMappedColumnIds (Long assetId, Long tableId) throws MappingException {
      try {
         return getMappingDAO().getMappedColumnIds(assetId, tableId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getMappedMemberIds (Long assetId, Long tableId) throws MappingException {
      try {
         return getMappingDAO().getMappedMemberIds(assetId, tableId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Mapping> getExistingMappingsByLimit (Long assetId, int limit) throws MappingException {
      try {
         return getMappingDAO().getExistingMappingsByLimit(assetId, limit);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }

   }

   public List<Mapping> getMappedConceptsOfParticularType (Long assetId, Long modelId, String typeName)
            throws MappingException {
      try {
         return getMappingDAO().getMappedConceptsOfParticularType(assetId, modelId, typeName);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Mapping getMappedConceptForColumn (Long assetId, Long tableId, Long columnId) throws MappingException {
      try {
         return getMappingDAO().getMappedConceptForColumn(assetId, tableId, columnId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public Mapping getMappedInstanceForMember (Long assetId, Long tableId, Long memberId) throws MappingException {
      try {
         return getMappingDAO().getMappedInstanceForMember(assetId, tableId, memberId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Long> getOtherModelConceptIdsByDisplayNameMappedOnAssetTable (Long modelId, Long conceptId,
            Long assetId, Long tableId, String conceptDisplayName) throws MappingException {
      try {
         return getMappingDAO().getOtherModelConceptIdsByDisplayNameMappedOnAssetTable(modelId, conceptId, assetId,
                  tableId, conceptDisplayName);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<String> getMappedConceptDisplayNames (Long modelId, Long assetId, Long tableId) throws MappingException {
      try {
         return getMappingDAO().getMappedConceptDisplayNames(modelId, assetId, tableId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Asset> getAssetsByConceptBedIdAndAssetType (Long conceptBedId, AssetType assetType, Long sourceAssetId)
            throws MappingException {
      try {
         return getMappingDAO().getAssetsByConceptBedIdAndAssetType(conceptBedId, assetType, sourceAssetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public List<Mapping> getPrimaryMapping (List<Long> conceptBedIds, Long assetId) throws MappingException {
      try {
         return getMappingDAO().getPrimaryMapping(conceptBedIds, assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public List<Mapping> getPrimaryMappingForConcepts (Long modelId, Long assetId) throws MappingException {
      try {
         return getMappingDAO().getPrimaryMappingForConcepts(modelId, assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Mapping getPrimaryMapping (Long conceptId, Long modelId, Long assetId) throws MappingException {
      try {
         return getMappingDAO().getPrimaryMapping(conceptId, modelId, assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public Mapping getNonPrimaryMapping (Long conceptId, Long modelId, Long assetId) throws MappingException {
      try {
         return getMappingDAO().getNonPrimaryMapping(conceptId, modelId, assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Mapping getPrimaryMapping (Long modelId, Long assetId, Long tableId, Long columnId, Long memberId)
            throws MappingException {
      try {
         return getMappingDAO().getPrimaryMapping(modelId, assetId, tableId, columnId, memberId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Mapping getPrimaryMapping (Long modelId, Long assetId, Long tableId, Long columnId) throws MappingException {
      try {
         return getMappingDAO().getPrimaryMapping(modelId, assetId, tableId, columnId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getMappedInstanceBEDIdsByConceptAndModelId (Long modelId, Long conceptId) throws MappingException {
      try {
         return getMappingDAO().getMappedInstanceBEDIdsByConceptAndModelId(modelId, conceptId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteInstanceMappings (Long conceptId, List<Long> modelGroupIds) throws MappingException {
      try {
         getMappingDAO().deleteInstanceMappings(conceptId, modelGroupIds);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }

   }

   public void deleteMappingForBusinessEntity (Long entityBedId) throws MappingException {
      try {
         getMappingDAO().deleteMappingForBusinessEntity(entityBedId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public List<DefaultMetric> getAllDefaultMetricsForAsset (Long assetId) throws MappingException {
      try {
         return getDefaultMetricDAO().getAllDefaultMetricsForAsset(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public List<DefaultMetric> getValidDefaultMetricsForAsset (Long assetId, Integer maxMetrics) throws MappingException {
      try {
         return getDefaultMetricDAO().getValidDefaultMetricsForAsset(assetId, maxMetrics);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public void createDefaultMetrics (List<DefaultMetric> defaultMetrics) throws MappingException {
      try {
         getDefaultMetricDAO().createAll(defaultMetrics);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }

   }

   @Override
   public void deleteInstanceMappingsForConcept (Long modelId, Long conceptId) throws MappingException {
      try {
         getMappingDAO().deleteInstanceMappingsForConcept(modelId, conceptId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }

   }

   @Override
   public List<Colum> getJoinedColumnsByMappedConcept (Long columnId) throws MappingException {
      try {
         return getMappingDAO().getJoinedColumnsByMappedConcept(columnId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public Map<Long, List<Long>> getMappedColumnIDsByConceptBEDID (Long assetId) throws MappingException {
      try {
         return getMappingDAO().getMappedColumnIDsByConceptBEDID(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public List<Long> getMappedColumnIDsForConceptBEDID (Long conceptBEDID, Long assetId) throws MappingException {
      try {
         return getMappingDAO().getMappedColumnIDsForConceptBEDID(conceptBEDID, assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public Map<Long, Long> getMappedMemberIDByInstanceBEDID (Long conceptBEDID, Long assetId, Long pageNumber,
            Long pageSize) throws MappingException {
      try {
         return getMappingDAO().getMappedMemberIDByInstanceBEDID(conceptBEDID, assetId, pageNumber, pageSize);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public Long getMappedInstanceCount (Long conceptBEDID, Long assetId) throws MappingException {
      try {
         return getMappingDAO().getMappedInstanceCount(conceptBEDID, assetId);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }
   
   @Override
   public Tabl getStatisticsMappedLookupTableOnCube (Long cubeAssetId, String statisticsConceptName) throws MappingException {
      try {
         return getMappingDAO().getStatisticsMappedLookupTableOnCube(cubeAssetId, statisticsConceptName);
      } catch (DataAccessException dataAccessException) {
         throw new MappingException(dataAccessException.getCode(), dataAccessException);
      }
   }

}