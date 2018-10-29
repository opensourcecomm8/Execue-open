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


package com.execue.ac.algorithm.optimaldsetoldversion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.service.IAnswersCatalogContextBuilderService;
import com.execue.ac.service.IAnswersCatalogDefaultsService;
import com.execue.core.common.bean.ac.AnswersCatalogMaintenanceRequestInfo;
import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.optimaldset.OptimalDSet;
import com.execue.core.common.bean.optimaldset.OptimalDSetAlgorithmInput;
import com.execue.core.common.bean.optimaldset.OptimalDSetDimension;
import com.execue.core.common.bean.qdata.QDataDimensionCombinationInfo;
import com.execue.core.common.bean.qdata.QDataDimensionInfo;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.exception.RangeSuggestionException;
import com.execue.platform.helper.RangeSuggestionServiceHelper;
import com.execue.platform.swi.IRangeSuggestionService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * This helper class contains the static helper methods
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class OptimalDSetHelper {

   private static final Logger                  log                               = Logger
                                                                                           .getLogger(OptimalDSetHelper.class);

   private IKDXRetrievalService                 kdxRetrievalService;
   private ISDXRetrievalService                 sdxRetrievalService;
   private IMappingRetrievalService             mappingRetrievalService;
   private IRangeSuggestionService              rangeSuggestionService;
   private IAnswersCatalogDefaultsService       answersCatalogDefaultsService;
   private IAnswersCatalogContextBuilderService answersCatalogContextBuilderService;
   private ICoreConfigurationService            coreConfigurationService;

   private static final String                  DIMENSION_COMBINATION_NAME_PREFIX = "dimensionCombination";

   public static OptimalDSetAlgorithmInput populateOptimalDSetAlgorithmInput (List<QDataDimensionInfo> qDataDimensions,
            List<QDataDimensionCombinationInfo> qDataDimensionCombinations) {
      displayQDataInput(qDataDimensions, qDataDimensionCombinations);
      OptimalDSetAlgorithmInput optimalDSetAlgorithmInput = new OptimalDSetAlgorithmInput();
      List<OptimalDSet> optimalDSets = new ArrayList<OptimalDSet>();
      int counter = 1;
      for (QDataDimensionCombinationInfo qDataDimensionCombinationInfo : qDataDimensionCombinations) {
         OptimalDSet optimalDSet = new OptimalDSet();
         List<QDataDimensionInfo> qDataDimensionCombination = populateQDataDimensionCombination(
                  qDataDimensionCombinationInfo.getCombination(), qDataDimensions);
         List<OptimalDSetDimension> optimalDSetDimensionCombinations = populateOptimalDSetDimensionCombination(qDataDimensionCombination);
         List<String> dimensionNames = new ArrayList<String>();
         for (OptimalDSetDimension optimalDSetDimension : optimalDSetDimensionCombinations) {
            dimensionNames.add(optimalDSetDimension.getName());
         }
         optimalDSet.setDimensions(optimalDSetDimensionCombinations);
         optimalDSet.setDimensionNames(ExecueCoreUtil.joinCollection(dimensionNames));
         optimalDSet.setUsagePercentage(qDataDimensionCombinationInfo.getUsagePercentage());
         optimalDSet.setName(DIMENSION_COMBINATION_NAME_PREFIX + counter++);
         optimalDSets.add(optimalDSet);
      }
      List<OptimalDSetDimension> optimalDSetDimensions = populateOptimalDSetDimensionCombination(qDataDimensions);
      optimalDSetAlgorithmInput.setDimensions(optimalDSetDimensions);
      optimalDSetAlgorithmInput.setOptimalDSets(optimalDSets);
      return optimalDSetAlgorithmInput;
   }

   public static void displayQDataInput (List<QDataDimensionInfo> dataDimensions,
            List<QDataDimensionCombinationInfo> dataDimensionCombinations) {
      if (log.isDebugEnabled()) {
         log.debug("List of Dimensions ");
         for (QDataDimensionInfo dataDimension : dataDimensions) {
            log.debug("Dimension name :: " + dataDimension.getName());
            log.debug("Dimension Members :: " + dataDimension.getNoOfMembers());
            log.debug("Dimension Occurences :: " + dataDimension.getOccurences());
         }
         log.debug("List of Dimension Combinations");
         for (QDataDimensionCombinationInfo qDataDimensionCombinationInfo : dataDimensionCombinations) {
            log.debug("Dimension Combinations size " + qDataDimensionCombinationInfo.getCombination().size());
            for (String str : qDataDimensionCombinationInfo.getCombination()) {
               log.debug(str + "\t");
            }
            log.debug("Usage Percent " + qDataDimensionCombinationInfo.getUsagePercentage());
         }
      }
   }

   public static void displayOptimalDSet (List<OptimalDSet> optimalDSets) {
      if (CollectionUtils.isEmpty(optimalDSets)) {
         return;
      }
      if (log.isDebugEnabled()) {
         log.debug("\nTotal Optimal DSets : " + optimalDSets.size());
         for (OptimalDSet optimalDSet : optimalDSets) {
            log.debug("\nOptimal DSet Name(Usage): " + optimalDSet.getName() + "(" + optimalDSet.getUsagePercentage()
                     + ")");
            Set<OptimalDSetDimension> dimensions = optimalDSet.getDimensions();
            for (OptimalDSetDimension optimalDSetDimension : dimensions) {
               log.debug("\nDimension Name(NoOfMembers): " + optimalDSetDimension.getName() + "("
                        + optimalDSetDimension.getNoOfMembers() + ")");
            }
         }
      }
   }

   private static List<OptimalDSetDimension> populateOptimalDSetDimensionCombination (
            List<QDataDimensionInfo> qDataDimensions) {
      List<OptimalDSetDimension> optimalDSetDimensionCombination = new ArrayList<OptimalDSetDimension>();
      for (QDataDimensionInfo qDataDimensionInfo : qDataDimensions) {
         OptimalDSetDimension optimalDSetDimension = new OptimalDSetDimension();
         optimalDSetDimension.setName(qDataDimensionInfo.getName());
         optimalDSetDimension.setNoOfMembers(qDataDimensionInfo.getNoOfMembers());
         optimalDSetDimensionCombination.add(optimalDSetDimension);
      }
      return optimalDSetDimensionCombination;
   }

   private static List<QDataDimensionInfo> populateQDataDimensionCombination (Set<String> dimensionNames,
            List<QDataDimensionInfo> qDataDimensions) {
      List<QDataDimensionInfo> qDataDimensionCombination = new ArrayList<QDataDimensionInfo>();
      for (String dimensionName : dimensionNames) {
         for (QDataDimensionInfo qDataDimensionInfo : qDataDimensions) {
            if (dimensionName.equalsIgnoreCase(qDataDimensionInfo.getName())) {
               qDataDimensionCombination.add(qDataDimensionInfo);
               break;
            }
         }
      }
      return qDataDimensionCombination;
   }

   public CubeCreationContext populateCubeCreationContext (OptimalDSet finalOptimalDSet, Asset asset,
            String targetAssetName, Long modelId, Long applicationId) throws AnswersCatalogException {
      CubeCreationContext cubeCreationContext = new CubeCreationContext();
      try {

         AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo = new AnswersCatalogMaintenanceRequestInfo();
         answersCatalogMaintenanceRequestInfo.setApplicationId(applicationId);
         answersCatalogMaintenanceRequestInfo.setModelId(modelId);
         answersCatalogMaintenanceRequestInfo.setUserId(getCoreConfigurationService().getAdminUserId());
         answersCatalogMaintenanceRequestInfo.setParentAssetId(asset.getId());
         Asset targetAsset = new Asset();
         targetAsset.setName(targetAssetName);
         targetAsset.setDisplayName(targetAssetName);
         targetAsset.setDescription(targetAssetName);
         answersCatalogMaintenanceRequestInfo.setTargetAsset(targetAsset);

         // get the dimensions list
         List<String> dimensions = new ArrayList<String>();
         for (OptimalDSetDimension optimalDSetDimension : finalOptimalDSet.getDimensions()) {
            dimensions.add(optimalDSetDimension.getName());
         }

         // prepare the simple lookup dimensions and range lookup dimensions
         Set<String> requestedSimpleLookupDimensions = new HashSet<String>();
         List<Range> ranges = new ArrayList<Range>();
         for (String dimensionName : dimensions) {
            Concept concept = kdxRetrievalService.getConceptByName(modelId, dimensionName);
            BusinessEntityDefinition conceptBED = kdxRetrievalService.getConceptBEDByName(modelId, concept.getName());
            List<Instance> instances = kdxRetrievalService.getInstances(modelId, concept.getId());
            if (ExecueCoreUtil.isCollectionEmpty(instances)) {
               // get the range and add to ranges list
               ranges.add(getRangeSuggestionService().deduceRange(
                        RangeSuggestionServiceHelper.populateDynamicRangeInput(modelId, asset.getId(), conceptBED
                                 .getId())));
            } else {
               // add to simple lookup list
               requestedSimpleLookupDimensions.add(dimensionName);
            }
         }
         answersCatalogMaintenanceRequestInfo.setSelectedSimpleLookups(new ArrayList<String>(
                  requestedSimpleLookupDimensions));
         answersCatalogMaintenanceRequestInfo.setSelectedRangeLookups(ranges);

         return getAnswersCatalogContextBuilderService().buildCubeCreationContext(answersCatalogMaintenanceRequestInfo);

      } catch (SWIException e) {
         e.printStackTrace();
         throw new AnswersCatalogException(e.getCode(), e);
      } catch (RangeSuggestionException e) {
         e.printStackTrace();
         throw new AnswersCatalogException(e.getCode(), e);
      }
   }

   public IAnswersCatalogDefaultsService getAnswersCatalogDefaultsService () {
      return answersCatalogDefaultsService;
   }

   public void setAnswersCatalogDefaultsService (IAnswersCatalogDefaultsService answersCatalogDefaultsService) {
      this.answersCatalogDefaultsService = answersCatalogDefaultsService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IRangeSuggestionService getRangeSuggestionService () {
      return rangeSuggestionService;
   }

   public void setRangeSuggestionService (IRangeSuggestionService rangeSuggestionService) {
      this.rangeSuggestionService = rangeSuggestionService;
   }

   /**
    * @return the answersCatalogContextBuilderService
    */
   public IAnswersCatalogContextBuilderService getAnswersCatalogContextBuilderService () {
      return answersCatalogContextBuilderService;
   }

   /**
    * @param answersCatalogContextBuilderService the answersCatalogContextBuilderService to set
    */
   public void setAnswersCatalogContextBuilderService (
            IAnswersCatalogContextBuilderService answersCatalogContextBuilderService) {
      this.answersCatalogContextBuilderService = answersCatalogContextBuilderService;
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
