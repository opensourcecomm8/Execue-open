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


package com.execue.ac.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.service.IAnswersCatalogContextBuilderService;
import com.execue.ac.service.IAnswersCatalogDefaultsService;
import com.execue.core.common.bean.ac.AnswersCatalogMaintenanceRequestInfo;
import com.execue.core.common.bean.ac.AnswersCatalogUpdationContext;
import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.ac.CubeRangeDimensionInfo;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.type.AssetGrainType;
import com.execue.core.common.util.AssetGrainUtils;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class AnswersCatalogContextBuilderServiceImpl implements IAnswersCatalogContextBuilderService {

   private ICoreConfigurationService      coreConfigurationService;
   private ISDXRetrievalService           sdxRetrievalService;
   private IAnswersCatalogDefaultsService answersCatalogDefaultsService;
   private IKDXRetrievalService           kdxRetrievalService;
   private IMappingRetrievalService       mappingRetrievalService;

   @Override
   public CubeCreationContext buildCubeCreationContext (
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo) throws AnswersCatalogException {
      CubeCreationContext cubeCreationContext = new CubeCreationContext();
      try {
         cubeCreationContext.setApplicationId(answersCatalogMaintenanceRequestInfo.getApplicationId());
         cubeCreationContext.setModelId(answersCatalogMaintenanceRequestInfo.getModelId());
         cubeCreationContext.setUserId(answersCatalogMaintenanceRequestInfo.getUserId());
         // build the source asset
         Long sourceAssetId = answersCatalogMaintenanceRequestInfo.getParentAssetId();
         Asset sourceAsset = getSdxRetrievalService().getAssetById(sourceAssetId);
         DataSource sourceDataSource = getSdxRetrievalService().getDataSourceByAssetId(sourceAssetId);
         sourceAsset.setDataSource(sourceDataSource);

         // build the target asset
         Asset targetAsset = answersCatalogMaintenanceRequestInfo.getTargetAsset();
         boolean targetDataSourceSameAsSourceDataSource = getAnswersCatalogDefaultsService()
                  .isCubeTargetDataSourceSameAsSourceDataSource();
         if (targetDataSourceSameAsSourceDataSource) {
            targetAsset.setDataSource(sourceDataSource);
         } else {
            targetAsset.setDataSource(getAnswersCatalogDefaultsService().getDefaultTargetDataSource());
         }
         cubeCreationContext.setSourceAsset(sourceAsset);
         cubeCreationContext.setTargetAsset(targetAsset);

         // get the grain of the asset
         List<Mapping> sourceAssetGrain = getMappingRetrievalService().getAssetGrain(sourceAssetId);
         // update simple lookup dimensions with Distribution concept
         updateSimpleLookupDimensionsWithDefaultDistributionConcept(answersCatalogMaintenanceRequestInfo,
                  sourceAssetGrain);
         cubeCreationContext.setSimpleLookupDimensions(answersCatalogMaintenanceRequestInfo.getSelectedSimpleLookups());
         cubeCreationContext
                  .setRangeLookupDimensions(populateCubeRangeLookupDimensions(answersCatalogMaintenanceRequestInfo));
         cubeCreationContext.setFrequencyMeasures(populateFrequencyMeasures(sourceAssetId, sourceAssetGrain));
         cubeCreationContext.setMeasures(populateCubeRequestedMeasures(sourceAssetId));
         cubeCreationContext.setTargetDataSourceSameAsSourceDataSource(targetDataSourceSameAsSourceDataSource);
         cubeCreationContext.setSimpleLookupDimensionBEDIDs(getSimpleLookupDimensionBEDIDs(
                  answersCatalogMaintenanceRequestInfo.getModelId(), answersCatalogMaintenanceRequestInfo
                           .getSelectedSimpleLookups()));
         cubeCreationContext
                  .setRangeLookupDimensionBEDIDs(getRangeLookupDimensionBEDIDs(answersCatalogMaintenanceRequestInfo
                           .getSelectedRangeLookups()));
      } catch (SDXException sdxException) {
         throw new AnswersCatalogException(sdxException.getCode(), sdxException);
      } catch (KDXException kdxException) {
         throw new AnswersCatalogException(kdxException.getCode(), kdxException);
      } catch (MappingException mappingException) {
         throw new AnswersCatalogException(mappingException.getCode(), mappingException);
      }
      return cubeCreationContext;
   }

   private Map<Long, List<Long>> getRangeLookupDimensionBEDIDs (List<Range> selectedRangeLookups) {
      Map<Long, List<Long>> rangeLookupDimensionBEDIDsMap = new HashMap<Long, List<Long>>();
      List<Long> rangeIds = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(selectedRangeLookups)) {
         for (Range range : selectedRangeLookups) {
            Long conceptBedId = range.getConceptBedId();
            // TODO: NK: Later can remove null check when we are sure that range id will always come, currently it is not
            // coming for dynamic ranges
            Long rangeId = range.getId() == null ? getCoreConfigurationService().getDynamicRangeId() : range.getId();
            if (rangeLookupDimensionBEDIDsMap.get(conceptBedId) != null) {
               rangeLookupDimensionBEDIDsMap.get(conceptBedId).add(rangeId);
            } else {
               rangeIds = new ArrayList<Long>();
               rangeIds.add(rangeId);
               rangeLookupDimensionBEDIDsMap.put(conceptBedId, rangeIds);
            }
         }
      }
      return rangeLookupDimensionBEDIDsMap;
   }

   private List<Long> getSimpleLookupDimensionBEDIDs (Long modelId, List<String> selectedSimpleLookups)
            throws KDXException {
      List<Long> simpleLookupDimensionBEDIDs = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(selectedSimpleLookups)) {
         simpleLookupDimensionBEDIDs = getBEDIDsByConceptNames(modelId, selectedSimpleLookups);
      }
      return simpleLookupDimensionBEDIDs;
   }

   private List<Long> getBEDIDsByConceptNames (Long modelId, List<String> conceptNames) throws KDXException {
      List<Long> conceptBEDIDs = new ArrayList<Long>();
      Concept concept = null;
      for (String conceptName : conceptNames) {
         concept = getKdxRetrievalService().getConceptByName(modelId, conceptName);
         conceptBEDIDs.add(getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId, concept.getId(), null)
                  .getId());
      }
      return conceptBEDIDs;
   }

   @Override
   public AnswersCatalogUpdationContext buildCubeRefreshContext (
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo) throws AnswersCatalogException {
      return buildCubeUpdationContext(answersCatalogMaintenanceRequestInfo);
   }

   @Override
   public AnswersCatalogUpdationContext buildCubeUpdationContext (
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo) throws AnswersCatalogException {
      AnswersCatalogUpdationContext answersCatalogUpdationContext = new AnswersCatalogUpdationContext();
      answersCatalogUpdationContext.setExistingAssetId(answersCatalogMaintenanceRequestInfo.getExistingAssetId());
      answersCatalogUpdationContext.setParentAssetId(answersCatalogMaintenanceRequestInfo.getParentAssetId());
      answersCatalogUpdationContext.setApplicationId(answersCatalogMaintenanceRequestInfo.getApplicationId());
      answersCatalogUpdationContext.setModelId(answersCatalogMaintenanceRequestInfo.getModelId());
      answersCatalogUpdationContext.setUserId(answersCatalogMaintenanceRequestInfo.getUserId());
      return answersCatalogUpdationContext;
   }

   @Override
   public MartCreationContext buildMartCreationContext (
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo) throws AnswersCatalogException {

      MartCreationContext martCreationContext = new MartCreationContext();
      try {
         martCreationContext.setModelId(answersCatalogMaintenanceRequestInfo.getModelId());
         martCreationContext.setApplicationId(answersCatalogMaintenanceRequestInfo.getApplicationId());
         martCreationContext.setUserId(answersCatalogMaintenanceRequestInfo.getUserId());

         // build source asset
         Long sourceAssetId = answersCatalogMaintenanceRequestInfo.getParentAssetId();
         Asset sourceAsset = getSdxRetrievalService().getAssetById(sourceAssetId);
         DataSource sourceDataSource = getSdxRetrievalService().getDataSourceByAssetId(sourceAssetId);
         sourceAsset.setDataSource(sourceDataSource);

         // build target asset
         Asset targetAsset = answersCatalogMaintenanceRequestInfo.getTargetAsset();
         boolean targetDataSourceSameAsSourceDataSource = getAnswersCatalogDefaultsService()
                  .isMartTargetDataSourceSameAsSourceDataSource();
         if (targetDataSourceSameAsSourceDataSource) {
            targetAsset.setDataSource(sourceDataSource);
         } else {
            targetAsset.setDataSource(getAnswersCatalogDefaultsService().getDefaultTargetDataSource());
         }
         martCreationContext.setTargetDataSourceSameAsSourceDataSource(targetDataSourceSameAsSourceDataSource);
         martCreationContext.setSourceAsset(sourceAsset);
         martCreationContext.setTargetAsset(targetAsset);

         List<Mapping> mappings = getMappingRetrievalService().getAssetGrain(sourceAssetId);
         String population = getPopulation(mappings);
         martCreationContext.setPopulation(population);
         List<String> distributions = getDefaultDistributions(mappings);
         martCreationContext.setDistributions(distributions);
         List<String> prominentDimensionNames = answersCatalogMaintenanceRequestInfo.getProminentDimensions();
         if (ExecueCoreUtil.isCollectionNotEmpty(distributions)) {
            prominentDimensionNames.removeAll(distributions);
         }
         if (population != null) {
            prominentDimensionNames.remove(population);
         }
         martCreationContext.setProminentDimensions(prominentDimensionNames);
         martCreationContext.setProminentMeasures(answersCatalogMaintenanceRequestInfo.getProminentMeasures());
         martCreationContext.setProminentDimensionBEDIDs(getProminentDimensionBEDIDs(
                  answersCatalogMaintenanceRequestInfo.getModelId(), prominentDimensionNames));
         martCreationContext.setProminentMeasureBEDIDs(getProminentMeasureBEDIDs(answersCatalogMaintenanceRequestInfo
                  .getModelId(), answersCatalogMaintenanceRequestInfo.getProminentMeasures()));
      } catch (MappingException mappingException) {
         throw new AnswersCatalogException(mappingException.getCode(), mappingException);
      } catch (SDXException sdxException) {
         throw new AnswersCatalogException(sdxException.getCode(), sdxException);
      } catch (KDXException kdxException) {
         throw new AnswersCatalogException(kdxException.getCode(), kdxException);
      }
      return martCreationContext;
   }

   private List<Long> getProminentMeasureBEDIDs (Long modelId, List<String> prominentMeasures) throws KDXException {
      if (ExecueCoreUtil.isCollectionNotEmpty(prominentMeasures)) {
         return getBEDIDsByConceptNames(modelId, prominentMeasures);
      }
      return new ArrayList<Long>();
   }

   private List<Long> getProminentDimensionBEDIDs (Long modelId, List<String> prominentDimensionNames)
            throws KDXException {
      if (ExecueCoreUtil.isCollectionNotEmpty(prominentDimensionNames)) {
         return getBEDIDsByConceptNames(modelId, prominentDimensionNames);
      }
      return new ArrayList<Long>();
   }

   @Override
   public AnswersCatalogUpdationContext buildMartRefreshContext (
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo) throws AnswersCatalogException {
      return buildMartUpdationContext(answersCatalogMaintenanceRequestInfo);
   }

   @Override
   public AnswersCatalogUpdationContext buildMartUpdationContext (
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo) throws AnswersCatalogException {
      AnswersCatalogUpdationContext answersCatalogUpdationContext = new AnswersCatalogUpdationContext();
      answersCatalogUpdationContext.setExistingAssetId(answersCatalogMaintenanceRequestInfo.getExistingAssetId());
      answersCatalogUpdationContext.setParentAssetId(answersCatalogMaintenanceRequestInfo.getParentAssetId());
      answersCatalogUpdationContext.setApplicationId(answersCatalogMaintenanceRequestInfo.getApplicationId());
      answersCatalogUpdationContext.setModelId(answersCatalogMaintenanceRequestInfo.getModelId());
      answersCatalogUpdationContext.setUserId(answersCatalogMaintenanceRequestInfo.getUserId());
      return answersCatalogUpdationContext;
   }

   /**
    * @return the sdxRetrievalService
    */
   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   /**
    * @param sdxRetrievalService
    *           the sdxRetrievalService to set
    */
   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   /**
    * @return the answersCatalogDefaultsService
    */
   public IAnswersCatalogDefaultsService getAnswersCatalogDefaultsService () {
      return answersCatalogDefaultsService;
   }

   /**
    * @param answersCatalogDefaultsService
    *           the answersCatalogDefaultsService to set
    */
   public void setAnswersCatalogDefaultsService (IAnswersCatalogDefaultsService answersCatalogDefaultsService) {
      this.answersCatalogDefaultsService = answersCatalogDefaultsService;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService
    *           the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the mappingRetrievalService
    */
   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   /**
    * @param mappingRetrievalService
    *           the mappingRetrievalService to set
    */
   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   private List<String> populateFrequencyMeasures (Long sourceAssetId, List<Mapping> sourceAssetGrain)
            throws KDXException {
      List<String> frequencyMeasures = new ArrayList<String>();
      // get the default population concept name.
      String defaultPopulationConceptName = null;
      Mapping defaultPopulationConcept = AssetGrainUtils.getDefaultPopulationConcept(sourceAssetGrain);
      if (defaultPopulationConcept != null) {
         defaultPopulationConceptName = defaultPopulationConcept.getBusinessEntityDefinition().getConcept().getName();
      }
      if (defaultPopulationConceptName != null) {
         frequencyMeasures.add(defaultPopulationConceptName);
      }

      return frequencyMeasures;
   }

   private void updateSimpleLookupDimensionsWithDefaultDistributionConcept (
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo, List<Mapping> sourceAssetGrain) {
      List<String> requestedSimpleLookupDimensions = answersCatalogMaintenanceRequestInfo.getSelectedSimpleLookups();
      // get the default distribution concept name.
      Concept defaultDistributionConcept = null;
      Mapping defaultDistributionConceptMapping = AssetGrainUtils.getDefaultDistributionConcept(sourceAssetGrain);
      if (defaultDistributionConceptMapping != null) {
         defaultDistributionConcept = defaultDistributionConceptMapping.getBusinessEntityDefinition().getConcept();
      }

      // add the default distribution concept to simple lookup dimensions if it doesn't exists.
      if (defaultDistributionConcept != null) {
         if (!requestedSimpleLookupDimensions.contains(defaultDistributionConcept.getName())) {
            requestedSimpleLookupDimensions.add(defaultDistributionConcept.getName());
         }
      }
   }

   private List<CubeRangeDimensionInfo> populateCubeRangeLookupDimensions (
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo) throws KDXException {
      List<CubeRangeDimensionInfo> rangeLookupDimensions = new ArrayList<CubeRangeDimensionInfo>();
      if (ExecueCoreUtil.isCollectionNotEmpty(answersCatalogMaintenanceRequestInfo.getSelectedRangeLookups())) {
         for (Range range : answersCatalogMaintenanceRequestInfo.getSelectedRangeLookups()) {
            BusinessEntityDefinition populatedBED = getKdxRetrievalService().getBusinessEntityDefinitionById(
                     range.getConceptBedId());
            rangeLookupDimensions.add(new CubeRangeDimensionInfo(populatedBED.getConcept().getName(), range));
         }
      }
      return rangeLookupDimensions;
   }

   private List<String> populateCubeRequestedMeasures (Long sourceAssetId) throws KDXException {
      // populate the measures.
      List<Concept> measures = getKdxRetrievalService().getMeasureConceptsForAsset(sourceAssetId);
      List<String> cubeRequestedMeasures = new ArrayList<String>();
      for (Concept concept : measures) {
         cubeRequestedMeasures.add(concept.getName());
      }
      return cubeRequestedMeasures;
   }

   private List<String> getDefaultDistributions (List<Mapping> mappings) {
      List<String> distributionConcepts = new ArrayList<String>();
      List<Mapping> distributionConceptMappings = AssetGrainUtils.getAllDistributionConcepts(mappings);
      if (ExecueCoreUtil.isCollectionNotEmpty(distributionConceptMappings)) {
         for (Mapping mapping : distributionConceptMappings) {
            if (AssetGrainType.DEFAULT_DISTRIBUTION_CONCEPT.equals(mapping.getAssetGrainType())) {
               distributionConcepts.add(mapping.getBusinessEntityDefinition().getConcept().getName());
            }// NOTE: -RG- We need not add other distributions, if requested by user they will come via prominent dimension(s)
         }
      }
      return distributionConcepts;
   }

   private String getPopulation (List<Mapping> mappings) {
      String populationConceptName = null;
      Mapping defaultPopulationConceptMapping = AssetGrainUtils.getDefaultPopulationConcept(mappings);
      if (defaultPopulationConceptMapping != null) {
         populationConceptName = defaultPopulationConceptMapping.getBusinessEntityDefinition().getConcept().getName();
      } else {
         List<Mapping> populationConcepts = AssetGrainUtils.getPopulationConcepts(mappings);
         if (ExecueCoreUtil.isCollectionNotEmpty(populationConcepts)) {
            populationConceptName = populationConcepts.get(0).getBusinessEntityDefinition().getConcept().getName();
         }
      }
      return populationConceptName;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}
