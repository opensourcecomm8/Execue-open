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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.AssetGrainInfo;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.governor.LightAssetEntityDefinitionInfo;
import com.execue.core.common.type.AssetGrainType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.GranularityType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.dataaccess.IMappingDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class MappingRetrievalServiceImpl implements IMappingRetrievalService {

   private IMappingDataAccessManager mappingDataAccessManager;
   private ISDXRetrievalService      sdxRetrievalService;
   private IKDXRetrievalService      kdxRetrievalService;

   public IMappingDataAccessManager getMappingDataAccessManager () {
      return mappingDataAccessManager;
   }

   public void setMappingDataAccessManager (IMappingDataAccessManager mappingDataAccessManager) {
      this.mappingDataAccessManager = mappingDataAccessManager;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   // ******************************** Methods related to Asset*******************************************

   public List<Asset> getAssetsByConceptBedIdAndAssetType (Long conceptBedId, AssetType assetType, Long sourceAssetId)
            throws MappingException {
      return getMappingDataAccessManager().getAssetsByConceptBedIdAndAssetType(conceptBedId, assetType, sourceAssetId);
   }

   // *********************** Methods related to Mappings starts********************************************

   public Mapping getMapping (Long mappingId) throws MappingException {
      Mapping mapping = mappingDataAccessManager.getById(mappingId, Mapping.class);
      AssetEntityDefinition assetEntityDefinition = mapping.getAssetEntityDefinition();
      Asset asset = assetEntityDefinition.getAsset();
      if (asset != null) {
         asset.getName();
         Tabl tabl = assetEntityDefinition.getTabl();
         if (tabl != null) {
            tabl.getName();
            Colum colum = assetEntityDefinition.getColum();
            if (colum != null) {
               colum.getName();
            }
         }
      }
      BusinessEntityDefinition businessEntityDefinition = mapping.getBusinessEntityDefinition();
      Concept concept = businessEntityDefinition.getConcept();
      if (concept != null) {
         concept.getName();
      }
      Instance instance = businessEntityDefinition.getInstance();
      if (instance != null) {
         instance.getDisplayName();
      }
      return mapping;
   }

   public List<Mapping> getUIMappings (Long assetId, Long modelId) throws MappingException {
      // process for loading all the internal references and values - lazy
      // loading
      List<Mapping> mappings = mappingDataAccessManager.getMappings(assetId, modelId);
      loadMappingTree(mappings, false);
      return mappings;
   }

   private void loadMappingTree (List<Mapping> mappings, boolean loadStats) {
      if (ExecueCoreUtil.isCollectionNotEmpty(mappings)) {
         for (Mapping mapping : mappings) {
            loadMapping(mapping, loadStats);
         }
      }
   }

   private void loadAED (AssetEntityDefinition assetEntityDefinition) {
      Asset asset = assetEntityDefinition.getAsset();
      if (asset != null) {
         asset.getName();
         Tabl tabl = assetEntityDefinition.getTabl();
         if (tabl != null) {
            tabl.getName();
            Colum colum = assetEntityDefinition.getColum();
            if (colum != null) {
               colum.getName();
               Membr membr = assetEntityDefinition.getMembr();
               if (membr != null) {
                  membr.getLookupColumn();
               }
            }
         }
      }
   }

   private void loadMapping (Mapping mapping, boolean loadStats) {
      AssetEntityDefinition assetEntityDefinition = mapping.getAssetEntityDefinition();
      loadAED(assetEntityDefinition);
      BusinessEntityDefinition businessEntityDefinition = mapping.getBusinessEntityDefinition();
      ModelGroup modelGroup = businessEntityDefinition.getModelGroup();
      if (modelGroup != null) {
         modelGroup.getId();
         Concept concept = businessEntityDefinition.getConcept();
         if (concept != null) {
            concept.getName();
            if (loadStats) {
               Set<Stat> statSet = concept.getStats();
               for (Stat stat : statSet) {
                  stat.getStatType();
               }
            }
            Instance instance = businessEntityDefinition.getInstance();
            if (instance != null) {
               instance.getName();
            }
         }
      }
   }

   public List<Mapping> getMappingsForAED (Long assetEntityDefinitionId) throws MappingException {
      List<Mapping> mappings = mappingDataAccessManager.getMappingsForAED(assetEntityDefinitionId);
      loadMappingTree(mappings, false);
      return mappings;
   }

   public List<Mapping> getExistingMappingsForColumns (Long assetId) throws MappingException {
      List<Mapping> mappings = mappingDataAccessManager.getExistingMappingsForColumns(assetId);
      for (Mapping mapping : mappings) {
         mapping.getAssetEntityDefinition().getId();
         mapping.getBusinessEntityDefinition().getId();
         mapping.getAssetEntityDefinition().getColum().getName();
         mapping.getAssetEntityDefinition().getTabl().getName();
         mapping.getBusinessEntityDefinition().getConcept().getDisplayName();
         mapping.getBusinessEntityDefinition().getConcept().getId();
      }
      return mappings;
   }

   public Mapping getMapping (Long assetEntityDefinitionId, Long businessEntityDefinitionId) throws MappingException {
      return mappingDataAccessManager.getMapping(assetEntityDefinitionId, businessEntityDefinitionId);
   }

   public List<Mapping> getMappingsForBED (Long businessEntityDefinitionId) throws MappingException {
      List<Mapping> mappings = mappingDataAccessManager.getMappingsForBED(businessEntityDefinitionId);
      for (Mapping mapping : mappings) {
         AssetEntityDefinition aed = mapping.getAssetEntityDefinition();
         Asset asset = aed.getAsset();
         if (asset != null) {
            asset.getDataSource().getProviderType();
            asset.getName();
            Tabl tabl = aed.getTabl();
            if (tabl != null) {
               tabl.getName();
               Colum colum = aed.getColum();
               if (colum != null) {
                  colum.getName();
                  Membr membr = aed.getMembr();
                  if (membr != null) {
                     membr.getLookupColumn();
                  }
               }
            }
         }
      }
      return mappings;
   }

   public List<Mapping> getMappingsForAsset (Long assetId) throws MappingException {
      return getMappingDataAccessManager().getMappingsForAsset(assetId);
   }

   public List<Mapping> getExistingMappingsForMembers (Long assetId) throws MappingException {
      List<Mapping> mappings = mappingDataAccessManager.getExistingMappingsForMembers(assetId);
      loadMappingTree(mappings, false);
      return mappings;
   }

   public List<Mapping> getAssetDefaultMetrics (Long assetId, Integer maxMetricsPerQuery) throws MappingException {
      List<Mapping> defaultMetrics = mappingDataAccessManager.getAssetDefaultMetrics(assetId, maxMetricsPerQuery);
      loadMappingTree(defaultMetrics, true);
      return defaultMetrics;
   }

   public List<Long> getNonUniquelyMappedBEDIDsForAsset (Long assetId) throws MappingException {
      return getMappingDataAccessManager().getNonUniquelyMappedBEDIDsForAsset(assetId);
   }

   public List<Mapping> getMappingsOfConceptsMappedWithDiffInNature (Long assetId) throws MappingException {
      List<Mapping> mappingsOfConceptsMappedWithDiffInNature = getMappingDataAccessManager()
               .getMappingsOfConceptsMappedWithDiffInNature(assetId);
      for (Mapping mapping : mappingsOfConceptsMappedWithDiffInNature) {
         mapping.getAssetEntityDefinition().getPopularity();
         mapping.getAssetEntityDefinition().getColum().getName();
      }
      return mappingsOfConceptsMappedWithDiffInNature;
   }

   public List<Mapping> getMemberMappingsByPage (Long columnAEDId, int pageNumber, int pageSize)
            throws MappingException {
      List<Mapping> memberMappingsByPage = mappingDataAccessManager.getMemberMappingsByPage(columnAEDId, pageNumber,
               pageSize);
      loadMappingTree(memberMappingsByPage, false);
      return memberMappingsByPage;
   }

   public List<Mapping> getMemberMappingsOfPageByStartIndex (Long columnAEDId, int startIndex, int pageSize)
            throws MappingException {
      List<Mapping> memberMappingsByPage = mappingDataAccessManager.getMemberMappingsOfPageByStartIndex(columnAEDId,
               startIndex, pageSize);
      loadMappingTree(memberMappingsByPage, false);
      return memberMappingsByPage;
   }

   public List<Mapping> getMappingsByInstanceDisplayName (Long columId, Long membrId, Long conceptId,
            String instanceDsiplayName) throws MappingException {
      List<Mapping> mappingsByInstanceDisplayName = mappingDataAccessManager.getMappingsByInstanceDisplayName(columId,
               membrId, conceptId, instanceDsiplayName);
      loadMappingTree(mappingsByInstanceDisplayName, false);
      return mappingsByInstanceDisplayName;
   }

   public List<Mapping> getExistingDefaultMetrics (List<Long> tableIds, Integer maxMetricsPerQuery)
            throws MappingException {
      List<Mapping> existingDefaultMetrics = mappingDataAccessManager.getExistingDefaultMetrics(tableIds,
               maxMetricsPerQuery);
      loadMappingTree(existingDefaultMetrics, true);
      return existingDefaultMetrics;
   }

   public List<Mapping> getExistingMappingsByLimit (Long assetId, int limit) throws MappingException {
      return mappingDataAccessManager.getExistingMappingsByLimit(assetId, limit);
   }

   public List<Mapping> getMappedConceptsOfParticularType (Long assetId, Long modelId, String typeName)
            throws MappingException {
      List<Mapping> mappedConceptsOfParticularType = mappingDataAccessManager.getMappedConceptsOfParticularType(
               assetId, modelId, typeName);
      loadMappingTree(mappedConceptsOfParticularType, false);
      loadAssetDatasource(mappedConceptsOfParticularType);
      return mappedConceptsOfParticularType;
   }

   private void loadAssetDatasource (List<Mapping> mappedConceptsOfParticularType) {
      for (Mapping mapping : mappedConceptsOfParticularType) {
         Asset asset = mapping.getAssetEntityDefinition().getAsset();
         DataSource dataSource = asset.getDataSource();
         dataSource.getName();
      }

   }

   @Override
   public List<Mapping> getPrimaryMappingsForConcepts (Long modelId, Long assetId) throws MappingException {
      List<Mapping> mappings = mappingDataAccessManager.getPrimaryMappingForConcepts(modelId, assetId);
      if (ExecueCoreUtil.isCollectionNotEmpty(mappings)) {
         loadMappingTree(mappings, false);
      }
      return mappings;
   }

   @Override
   public List<Mapping> getPrimaryMapping (List<Long> conceptBedIds, Long assetId) throws MappingException {
      List<Mapping> mappings = mappingDataAccessManager.getPrimaryMapping(conceptBedIds, assetId);
      if (ExecueCoreUtil.isCollectionNotEmpty(mappings)) {
         loadMappingTree(mappings, false);
      }
      return mappings;
   }

   public Mapping getPrimaryMapping (Long conceptId, Long modelId, Long assetId) throws MappingException {
      Mapping mapping = mappingDataAccessManager.getPrimaryMapping(conceptId, modelId, assetId);
      loadMapping(mapping, false);
      return mapping;
   }

   @Override
   public Mapping getNonPrimaryMapping (Long conceptId, Long modelId, Long assetId) throws MappingException {
      Mapping mapping = mappingDataAccessManager.getNonPrimaryMapping(conceptId, modelId, assetId);
      loadMapping(mapping, false);
      return mapping;
   }

   public Mapping getPrimaryMapping (Long modelId, Long assetId, Long tableId, Long columnId) throws MappingException {
      return mappingDataAccessManager.getPrimaryMapping(modelId, assetId, tableId, columnId);
   }

   public Mapping getPrimaryMapping (Long modelId, Long assetId, Long tableId, Long columnId, Long memberId)
            throws MappingException {
      return mappingDataAccessManager.getPrimaryMapping(modelId, assetId, tableId, columnId, memberId);
   }

   public List<Mapping> getDistributionConcepts (Long modelId, Long assetId) throws MappingException {
      List<Mapping> mappings = new ArrayList<Mapping>();
      try {
         // List<Concept> centralConcepts = mappingDataAccessManager.getMappedDistributionConcepts(assetId);

         // Get Concepts which are DISTRIBUTION by behavior
         List<Concept> distributionConceptsByEntityBehavior = getMappingDataAccessManager()
                  .getMappedDistributionConceptsByEntityBehavior(assetId);

         // List<Concept> consolidatedDistributionConcepts = mergeGrainConcepts(centralConcepts,
         // distributionConceptsByEntityBehavior);

         if (ExecueCoreUtil.isCollectionNotEmpty(distributionConceptsByEntityBehavior)) {
            for (Concept concept : distributionConceptsByEntityBehavior) {
               BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
               businessEntityTerm.setBusinessEntity(concept);
               businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
               businessEntityTerm.setBusinessEntityDefinitionId(kdxRetrievalService.getBusinessEntityDefinitionByIds(
                        modelId, concept.getId(), null).getId());
               List<Mapping> assetEntities = getAssetEntities(businessEntityTerm, modelId, assetId);
               List<AssetEntityDefinition> assetEntityDefinitions = ExecueBeanUtil
                        .getAssetEntityDefinitions(assetEntities);
               AssetEntityDefinition correctAssetEntityDefinition = getSdxRetrievalService()
                        .pickCorrectAssetEntityDefinition(assetEntityDefinitions);
               mappings.add(findMatchingMapping(assetEntities, correctAssetEntityDefinition));
            }
         }
      } catch (SDXException sdxException) {
         throw new MappingException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, sdxException);
      } catch (KDXException kdxException) {
         throw new MappingException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      }
      for (Mapping mapping : mappings) {
         BusinessEntityDefinition businessEntityDefinition = mapping.getBusinessEntityDefinition();
         ModelGroup modelGroup = businessEntityDefinition.getModelGroup();
         if (modelGroup != null) {
            modelGroup.getName();
            Concept concept = businessEntityDefinition.getConcept();
            if (concept != null) {
               concept.getName();
            }
         }
      }
      return mappings;
   }

   public List<Mapping> getAssetEntities (BusinessEntityTerm businessEntityTerm, Long modelId, Long assetId)
            throws MappingException {
      List<Mapping> mappings = mappingDataAccessManager.getAssetEntities(businessEntityTerm, modelId, assetId);
      loadMappingTree(mappings, false);
      return mappings;
   }

   private Mapping findMatchingMapping (List<Mapping> mappings, AssetEntityDefinition assetEntityDefinition) {
      Mapping matchedMapping = null;
      for (Mapping mapping : mappings) {
         if (assetEntityDefinition.equals(mapping.getAssetEntityDefinition())) {
            matchedMapping = mapping;
            break;
         }
      }
      return matchedMapping;
   }

   public List<Mapping> getAssetGrainByColumnGranularity (Long assetId) throws MappingException {
      List<Mapping> assetGrainByColumnGranularity = mappingDataAccessManager.getAssetGrainByColumnGranularity(assetId);
      loadAssetGrainMappings(assetGrainByColumnGranularity);
      return assetGrainByColumnGranularity;
   }

   private void loadAssetGrainMappings (List<Mapping> mappings) {
      if (ExecueCoreUtil.isCollectionNotEmpty(mappings)) {
         for (Mapping mapping : mappings) {
            AssetEntityDefinition assetEntityDefinition = mapping.getAssetEntityDefinition();
            BusinessEntityDefinition businessEntityDefinition = mapping.getBusinessEntityDefinition();
            Asset asset = assetEntityDefinition.getAsset();
            if (asset != null) {
               asset.getName();
               Tabl tabl = assetEntityDefinition.getTabl();
               if (tabl != null) {
                  tabl.getName();
                  Colum colum = assetEntityDefinition.getColum();
                  if (colum != null) {
                     colum.getName();
                  }
               }
            }
            ModelGroup modelGroup = businessEntityDefinition.getModelGroup();
            if (modelGroup != null) {
               modelGroup.getName();
               Concept concept = businessEntityDefinition.getConcept();
               if (concept != null) {
                  concept.getName();
               }
            }
         }
      }
   }

   public List<Mapping> getAssetGrain (Long assetId) throws MappingException {
      List<Mapping> assetGrain = mappingDataAccessManager.getAssetGrain(assetId);
      loadAssetGrainMappings(assetGrain);
      return assetGrain;
   }

   public List<Mapping> getAssetEntities (BusinessEntityDefinition businessEntityDefinition, Long assetId)
            throws MappingException {
      List<Mapping> mappings = mappingDataAccessManager.getAssetEntities(businessEntityDefinition, assetId);
      loadMappingTree(mappings, false);
      return mappings;
   }

   // ************************** Methods related to BED'S Starts***************************************************

   public BusinessEntityDefinition getMappedConceptBEDForColumn (Long assetId, Long tableId, Long columnId)
            throws MappingException {
      BusinessEntityDefinition mappedConceptBed = null;
      Mapping mapping = mappingDataAccessManager.getMappedConceptForColumn(assetId, tableId, columnId);
      if (mapping != null) {
         mappedConceptBed = mapping.getBusinessEntityDefinition();
         mappedConceptBed.getConcept().getName();
      }
      return mappedConceptBed;
   }

   public BusinessEntityDefinition getMappedInstanceForMember (Long assetId, Long tableId, Long memberId)
            throws MappingException {
      BusinessEntityDefinition instanceBed = null;
      Mapping mapping = mappingDataAccessManager.getMappedInstanceForMember(assetId, tableId, memberId);
      if (mapping != null) {
         instanceBed = mapping.getBusinessEntityDefinition();
         Instance mappedInstance = instanceBed.getInstance();
         mappedInstance.getName();
         Concept concept = instanceBed.getConcept();
         concept.getName();
      }
      return instanceBed;
   }

   public BusinessEntityDefinition getInstanceMatchedByMemberLookupDescription (Long memberAEDId, Long conceptId)
            throws MappingException {

      BusinessEntityDefinition bed = mappingDataAccessManager.getInstanceMatchedByMemberLookupDescription(memberAEDId,
               conceptId);
      if (bed != null) {
         Concept concept = bed.getConcept();
         concept.getDisplayName();
         Instance instance = bed.getInstance();
         instance.getDisplayName();
      }
      return bed;
   }

   public List<Long> getDistinctFactTablesForBEDIdsByNonPrimaryMappings (List<Long> bedIds, Long assetId)
            throws MappingException {
      return mappingDataAccessManager.getDistinctFactTablesForBEDIdsByNonPrimaryMappings(bedIds, assetId);
   }

   // ************************** Methods related to AssetGrainInfo starts *********************************************

   public List<AssetGrainInfo> getGrainConcepts (Long modelId, Long assetId) throws MappingException {
      List<AssetGrainInfo> grainByColumnGranularity = getMappingDataAccessManager()
               .getGrainConceptsByColumnGranularity(assetId);
      List<AssetGrainInfo> grainByEntityBehavior = getMappingDataAccessManager()
               .getMappedGrainConceptsIncludingDistributionByEntityBehavior(assetId);
      List<AssetGrainInfo> grains = mergeGrainConcepts(grainByColumnGranularity, grainByEntityBehavior);
      if (ExecueCoreUtil.isCollectionNotEmpty(grains)) {
         for (AssetGrainInfo assetGrainInformation : grains) {
            if (ExecueCoreUtil.isCollectionNotEmpty(assetGrainInformation.getBehaviorTypes())) {
               assetGrainInformation.setGrainType(getGrainTypeForCorrespondingBehaviorType(assetGrainInformation
                        .getBehaviorTypes()));
            }
            if (assetGrainInformation.getGrainType() == null && assetGrainInformation.getGrain() != null) {
               assetGrainInformation.setGrainType(getGrainTypeForCorrespondingGranularityType(assetGrainInformation
                        .getGrain()));
            }
         }
      }
      return grains;
   }

   private List<AssetGrainInfo> mergeGrainConcepts (List<AssetGrainInfo> grainByColumnGranularity,
            List<AssetGrainInfo> grainsByEntityBehavior) {
      List<AssetGrainInfo> grains = new ArrayList<AssetGrainInfo>();
      grains.addAll(grainsByEntityBehavior);
      boolean contains = false;
      for (AssetGrainInfo grainByColumn : grainByColumnGranularity) {
         contains = false;
         for (AssetGrainInfo grainByEntityBehavior : grainsByEntityBehavior) {
            if (grainByColumn.getMappingId().equals(grainByEntityBehavior.getMappingId())) {
               contains = true;
               break;
            }
         }
         if (!contains) {
            grains.add(grainByColumn);
         }
      }
      return grains;
   }

   private AssetGrainType getGrainTypeForCorrespondingBehaviorType (List<BehaviorType> behaviorTypes) {
      AssetGrainType grainType = null;
      BehaviorType behaviorType = getAppropriateBehaviorType(behaviorTypes);
      if (BehaviorType.POPULATION.equals(behaviorType)) {
         grainType = AssetGrainType.POPULATION_CONCEPT;
      } else if (BehaviorType.DISTRIBUTION.equals(behaviorType)) {
         grainType = AssetGrainType.DISTRIBUTION_CONCEPT;
      } else if (BehaviorType.GRAIN.equals(behaviorType)) {
         grainType = AssetGrainType.GRAIN_CONCEPT;
      }
      return grainType;
   }

   private AssetGrainType getGrainTypeForCorrespondingGranularityType (GranularityType grain) {
      AssetGrainType grainType = null;
      if (GranularityType.GRAIN.equals(grain)) {
         grainType = AssetGrainType.GRAIN_CONCEPT;
      }
      return grainType;
   }

   private BehaviorType getAppropriateBehaviorType (List<BehaviorType> behaviorTypes) {
      BehaviorType correctBehaviorType = behaviorTypes.get(0);
      if (behaviorTypes.size() > 1) {
         correctBehaviorType = getMatchedBehaviorType(behaviorTypes, BehaviorType.POPULATION);
         if (correctBehaviorType == null) {
            correctBehaviorType = getMatchedBehaviorType(behaviorTypes, BehaviorType.DISTRIBUTION);
         }
      }
      return correctBehaviorType;
   }

   private BehaviorType getMatchedBehaviorType (List<BehaviorType> behaviorTypes, BehaviorType toBeMatchedBehaviorType) {
      BehaviorType matchedBehaviorType = null;
      for (BehaviorType behaviorType : behaviorTypes) {
         if (behaviorType.equals(toBeMatchedBehaviorType)) {
            matchedBehaviorType = behaviorType;
            break;
         }
      }
      return matchedBehaviorType;
   }

   public List<AssetGrainInfo> getEligibleDefaultMetrics (Long assetId) throws MappingException {
      return getMappingDataAccessManager().getEligibleDefaultMetrics(assetId);
   }

   public List<AssetGrainInfo> getAssetDefaultMetricsForUI (Long assetId) throws MappingException {
      return getMappingDataAccessManager().getAssetDefaultMetricsForUI(assetId);
   }

   // ************************** Methods related to Concept starts *********************************************

   public Concept getMappedConceptForColumn (Long assetId, Long tableId, Long columnId) throws MappingException {
      Concept mappedConcept = null;
      Mapping mapping = mappingDataAccessManager.getMappedConceptForColumn(assetId, tableId, columnId);
      if (mapping != null) {
         BusinessEntityDefinition conceptBed = mapping.getBusinessEntityDefinition();
         mappedConcept = conceptBed.getConcept();
         mappedConcept.getName();
      }
      return mappedConcept;
   }

   public List<String> getMappedConceptDisplayNames (Long modelId, Long assetId, Long tableId) throws MappingException {
      return getMappingDataAccessManager().getMappedConceptDisplayNames(modelId, assetId, tableId);
   }

   public boolean isModelConceptNameChangeUniqueOnMappedAssetTable (Long modelId, Long conceptId, Long assetId,
            Long tableId, String conceptDisplayName) throws MappingException {
      boolean modelConceptNameChangeUniqueOnMappedAssetTable = true;
      if (ExecueCoreUtil.isCollectionNotEmpty(getMappingDataAccessManager()
               .getOtherModelConceptIdsByDisplayNameMappedOnAssetTable(modelId, conceptId, assetId, tableId,
                        conceptDisplayName))) {
         modelConceptNameChangeUniqueOnMappedAssetTable = false;
      }
      return modelConceptNameChangeUniqueOnMappedAssetTable;
   }

   // *************************Methods related to Instance ******************************************************

   public List<Instance> getMappedInstances (Long modelId, Long conceptId, Long assetId) throws MappingException {
      try {
         return getMappingDataAccessManager().getMappedInstances(modelId, conceptId, assetId);
      } catch (DataAccessException dae) {
         throw new MappingException(dae.getCode(), dae);
      }
   }

   // ************************** Methods related to AED'S starts *********************************************

   public List<LightAssetEntityDefinitionInfo> getAssetEntitiesForAllAssets (Long businessEntityDefinitionId,
            Long modelId) throws MappingException {
      return mappingDataAccessManager.getAssetEntities(businessEntityDefinitionId, modelId);
   }

   public List<LightAssetEntityDefinitionInfo> getAssetEntitiesForAllAssetsInBaseGroup (
            Long businessEntityDefinitionId, Long modelId) throws MappingException {
      return mappingDataAccessManager.getAssetEntitiesInBaseGroup(businessEntityDefinitionId, modelId);
   }

   public List<LightAssetEntityDefinitionInfo> getPrimaryAssetEntitiesForAssets (Long businessEntityDefinitionId,
            Long modelId, List<Long> assetIds) throws MappingException {
      return mappingDataAccessManager.getPrimaryAssetEntities(businessEntityDefinitionId, modelId, assetIds);
   }

   public List<LightAssetEntityDefinitionInfo> getPrimaryAssetEntitiesForAssetsInBaseGroup (
            Long businessEntityDefinitionId, Long modelId, List<Long> assetIds) throws MappingException {
      return mappingDataAccessManager.getPrimaryAssetEntitiesForAssetsInBaseGroup(businessEntityDefinitionId, modelId,
               assetIds);
   }

   public List<LightAssetEntityDefinitionInfo> getAssetEntitiesForAsset (Long businessEntityDefinitionId, Long modelId,
            Long assetId) throws MappingException {
      return mappingDataAccessManager.getAssetEntitiesForAsset(businessEntityDefinitionId, modelId, assetId);
   }

   // ************************** Methods related to DefaultMetric starts *********************************************

   public List<DefaultMetric> getValidDefaultMetricsForAsset (Long assetId, Integer maxMetrics) throws MappingException {
      return getMappingDataAccessManager().getValidDefaultMetricsForAsset(assetId, maxMetrics);
   }

   public List<DefaultMetric> getAllDefaultMetricsForAsset (Long assetId) throws MappingException {
      return getMappingDataAccessManager().getAllDefaultMetricsForAsset(assetId);
   }

   public List<DefaultMetric> getInValidExistingDefaultMetrics (Long tableId) throws MappingException {
      return getMappingDataAccessManager().getInValidExistingDefaultMetrics(tableId);
   }

   public List<DefaultMetric> getValidExistingDefaultMetrics (Long tableId) throws MappingException {
      return getMappingDataAccessManager().getValidExistingDefaultMetrics(tableId);
   }

   public List<DefaultMetric> getAllExistingDefaultMetrics (Long tableId) throws MappingException {
      List<DefaultMetric> allDefaultMetrics = new ArrayList<DefaultMetric>();
      List<DefaultMetric> validExistingDefaultMetrics = getMappingDataAccessManager().getValidExistingDefaultMetrics(
               tableId);
      List<DefaultMetric> inValidExistingDefaultMetrics = getMappingDataAccessManager()
               .getInValidExistingDefaultMetrics(tableId);
      if (ExecueCoreUtil.isCollectionNotEmpty(validExistingDefaultMetrics)) {
         allDefaultMetrics.addAll(validExistingDefaultMetrics);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(inValidExistingDefaultMetrics)) {
         allDefaultMetrics.addAll(inValidExistingDefaultMetrics);
      }
      return allDefaultMetrics;
   }

   public List<DefaultMetric> getPossibleDefaultMetrics (Long assetId, Long tableId, Integer maxMetricsPerTable)
            throws MappingException {
      return mappingDataAccessManager.getPossibleDefaultMetrics(assetId, tableId, maxMetricsPerTable);
   }

   public List<DefaultMetric> getInvalidDefaultMetrics (List<Long> tableIds) throws MappingException {
      return mappingDataAccessManager.getInvalidDefaultMetrics(tableIds);
   }

   public List<DefaultMetric> getAllPossibleDefaultMetrics (Long assetId, Long tableId) throws MappingException {
      return mappingDataAccessManager.getAllPossibleDefaultMetrics(assetId, tableId);
   }

   // ************************** Methods related to Member starts *********************************************

   public List<Long> getMappedColumnIds (Long assetId, Long tableId) throws MappingException {
      return mappingDataAccessManager.getMappedColumnIds(assetId, tableId);
   }

   // ************************** Methods related to Member starts *********************************************

   public Long getUnmappedMemberCount (Long columnAEDId) throws MappingException {
      return mappingDataAccessManager.getUnmappedMemberCount(columnAEDId);
   }

   public Long getMappedMemberCount (Long columnAEDId) throws MappingException {
      return mappingDataAccessManager.getMappedMemberCount(columnAEDId);
   }

   public Long getMappedMemberCount (Long assetId, Long businessEntityId) throws MappingException {
      return mappingDataAccessManager.getMappedMemberCount(assetId, businessEntityId);
   }

   public List<Membr> getUnmappedMembersByBatchAndSize (Long columnId, Long conceptId, Long batchNum, Long batchSize)
            throws MappingException {
      return getMappingDataAccessManager().getUnmappedMembersByBatchAndSize(columnId, conceptId, batchNum, batchSize);
   }

   public List<Long> getMappedMemberIds (Long assetId, Long tableId) throws MappingException {
      return mappingDataAccessManager.getMappedMemberIds(assetId, tableId);
   }

   public List<Long> getMappedInstanceBEDIdsByConceptAndModelId (Long modelId, Long conceptId) throws MappingException {
      return getMappingDataAccessManager().getMappedInstanceBEDIdsByConceptAndModelId(modelId, conceptId);
   }

   public List<Instance> getUnmappedInstances (Long modelId, Long conceptId) throws MappingException {
      List<Instance> unMappedInstances = new ArrayList<Instance>();
      try {
         // Get all the instances BEDIds for concept and model id.
         List<Long> allInstancesBEDIds = getKdxRetrievalService().getInstanceBEDIdsByConceptId(modelId, conceptId);
         if (ExecueCoreUtil.isCollectionNotEmpty(allInstancesBEDIds)) {
            // Get all the mapped instances BEDIds for concept and model id.
            List<Long> mappedInstancesBEDIds = getMappedInstanceBEDIdsByConceptAndModelId(modelId, conceptId);
            if (ExecueCoreUtil.isCollectionNotEmpty(mappedInstancesBEDIds)) {
               // Get unmapped instances BEDIds for concept and model id.
               allInstancesBEDIds.removeAll(mappedInstancesBEDIds);
            }
         }
         // Prepare list of instances from instances BEDIds.
         if (ExecueCoreUtil.isCollectionNotEmpty(allInstancesBEDIds)) {
            unMappedInstances = getKdxRetrievalService().getInstancesByBEDIds(allInstancesBEDIds);
         }
      } catch (KDXException e) {
         throw new MappingException(e.getCode(), e);
      }
      return unMappedInstances;
   }

   public BusinessEntityDefinition getMatchedInstanceBedByMemberLookupDescription (Long modelId, Concept concept,
            String memberLookupDesc) throws MappingException {
      List<Instance> unmappedInstances = getUnmappedInstances(modelId, concept.getId());
      BusinessEntityDefinition matchedInstanceBed = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(unmappedInstances)) {
         Map<String, Instance> unmappedInstancesByDisplayName = getUnmappedInstancesByDisplayName(unmappedInstances);
         Instance matchedInstance = unmappedInstancesByDisplayName.get(memberLookupDesc);
         try {
            if (matchedInstance == null) {
               // If not found, then check all unmapped instances with other variations and entity variations
               for (Instance instance : unmappedInstances) {
                  try {
                     matchedInstanceBed = getMatchedInstanceBedByMemberLookupDesc(modelId, concept, instance,
                              memberLookupDesc);
                  } catch (KDXException e) {
                     throw new MappingException(e.getCode(), e);
                  }
                  if (matchedInstanceBed != null) {
                     break;
                  }
               }
            } else {
               matchedInstanceBed = getKdxRetrievalService().getInstanceBEDByName(modelId, concept.getName(),
                        matchedInstance.getName());
            }
         } catch (KDXException e) {
            throw new MappingException(e.getCode(), e);
         }
      }

      return matchedInstanceBed;
   }

   private Map<String, Instance> getUnmappedInstancesByDisplayName (List<Instance> unmappedInstances) {
      Map<String, Instance> instancesByDisplayName = new HashMap<String, Instance>();
      for (Instance instance : unmappedInstances) {
         instancesByDisplayName.put(instance.getDisplayName(), instance);
      }
      return instancesByDisplayName;
   }

   private BusinessEntityDefinition getMatchedInstanceBedByMemberLookupDesc (Long modelId, Concept concept,
            Instance instance, String memberLookupDesc) throws KDXException {
      // Get the variations of the instance and check if it matches with member lookup description,
      // then return the matching instance.
      Instance matchedInstance = null;
      BusinessEntityDefinition instanceBED = getKdxRetrievalService().getInstanceBEDByName(modelId, concept.getName(),
               instance.getName());
      List<String> instanceVariations = getKdxRetrievalService().getBusinessEntityVariationNames(instanceBED.getId());

      if (ExecueCoreUtil.isCollectionNotEmpty(instanceVariations)) {
         for (String instanceVariation : instanceVariations) {
            if (memberLookupDesc.equalsIgnoreCase(instanceVariation)) {
               matchedInstance = instance;
               break;
            }
         }
      }
      if (matchedInstance == null) {
         return null;
      }
      return instanceBED;
   }

   @Override
   public List<Colum> getJoinedColumnsByMappedConcept (Long columnId) throws MappingException {
      return getMappingDataAccessManager().getJoinedColumnsByMappedConcept(columnId);
   }

   @Override
   public Map<Long, List<Long>> getMappedColumnIDsByConceptBEDID (Long assetId) throws MappingException {
      return getMappingDataAccessManager().getMappedColumnIDsByConceptBEDID (assetId);
   }
   
   @Override
   public List<Long> getMappedColumnIDsForConceptBEDID (Long conceptBEDID, Long assetId) throws MappingException {
      return getMappingDataAccessManager().getMappedColumnIDsForConceptBEDID(conceptBEDID, assetId);
   }

   @Override
   public Map<Long, Long> getMappedMemberIDByInstanceBEDID (Long conceptBEDID, Long assetId, Long pageNumber,
            Long pageSize) throws MappingException {
      return getMappingDataAccessManager().getMappedMemberIDByInstanceBEDID (conceptBEDID, assetId, pageNumber, pageSize);
   }
   
   @Override
   public Long getMappedInstanceCount (Long conceptBEDID, Long assetId) throws MappingException {
      return getMappingDataAccessManager().getMappedInstanceCount (conceptBEDID, assetId);
   }
   
   @Override
   public Tabl getStatisticsMappedLookupTableOnCube (Long cubeAssetId) throws MappingException {
      return getMappingDataAccessManager().getStatisticsMappedLookupTableOnCube(cubeAssetId, ExecueConstants.STATISTICS);
   }
}
