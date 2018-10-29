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


package com.execue.ac.algorithm.optimaldset.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;

import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetBusinessEntityInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCandidateDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetDimensionInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetMeasureInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetPastUsageDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetSpaceCalculationInputInfo;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetSpaceCalculationService;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.qdata.OptimalDSetSWIInfo;
import com.execue.core.common.bean.qdata.QueryHistoryBusinessEntityInfo;
import com.execue.core.common.bean.qdata.QueryHistoryDimensionPatternInfo;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.qdata.configuration.IQueryDataConfigurationService;

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
public class OptimalDSetHelper {

   private ICoreConfigurationService           coreConfigurationService;
   private IQueryDataConfigurationService      queryDataConfigurationService;
   private IOptimalDSetSpaceCalculationService optimalDSetSpaceCalculationService;

   public void populateSpace (List<OptimalDSetCandidateDimensionPattern> candidatePatterns,
            OptimalDSetSpaceCalculationInputInfo spaceCalculationInputInfo) throws AnswersCatalogException {
      for (OptimalDSetCandidateDimensionPattern candidatePattern : candidatePatterns) {
         candidatePattern.setSpace(getOptimalDSetSpaceCalculationService().computeProjectedSpaceForCube(
                  candidatePattern, spaceCalculationInputInfo));
      }
   }

   /**
    * This method returns the transformed list of OptimalDSetPastUsageDimensionPattern's for the input list of
    * QueryHistoryDimensionPatternInfo
    * 
    * @param queryHistoryDimensionPatternInfos
    * @return the List<OptimalDSetPastUsageDimensionPattern>
    */
   public List<OptimalDSetPastUsageDimensionPattern> transformQueryHistoryDimensionPatternInfo (
            List<QueryHistoryDimensionPatternInfo> queryHistoryDimensionPatternInfos) {
      List<OptimalDSetPastUsageDimensionPattern> optimalDSetPastUsageDimensionPatterns = new ArrayList<OptimalDSetPastUsageDimensionPattern>();
      if (ExecueCoreUtil.isCollectionEmpty(queryHistoryDimensionPatternInfos)) {
         return optimalDSetPastUsageDimensionPatterns;
      }
      for (QueryHistoryDimensionPatternInfo pastUsageDimensionPattern : queryHistoryDimensionPatternInfos) {
         Double usagePercentage = pastUsageDimensionPattern.getUsagePercentage();
         List<OptimalDSetSWIInfo> optimalDSetSwiInfos = pastUsageDimensionPattern.getOptimalDSetSwiInfos();
         List<OptimalDSetDimensionInfo> optimalDSetDimensionInfos = getOptimalDSetDimensionInfos(optimalDSetSwiInfos);

         OptimalDSetPastUsageDimensionPattern optimalDSetPastUsageDimensionPattern = new OptimalDSetPastUsageDimensionPattern();
         optimalDSetPastUsageDimensionPattern.setUsagePercentage(usagePercentage);
         optimalDSetPastUsageDimensionPattern.setDimensions(optimalDSetDimensionInfos);

         optimalDSetPastUsageDimensionPatterns.add(optimalDSetPastUsageDimensionPattern);
      }
      return optimalDSetPastUsageDimensionPatterns;
   }

   public List<OptimalDSetDimensionInfo> transformPastUsageDimensionInfo (
            List<QueryHistoryBusinessEntityInfo> pastUsageBusinessEntityInfos) {
      List<OptimalDSetDimensionInfo> opList = new ArrayList<OptimalDSetDimensionInfo>();
      if (CollectionUtils.isEmpty(pastUsageBusinessEntityInfos)) {
         return opList;
      }
      for (QueryHistoryBusinessEntityInfo pastUsageBusinessEntityInfo : pastUsageBusinessEntityInfos) {
         OptimalDSetDimensionInfo optimalDSetDimensionInfo = (OptimalDSetDimensionInfo) getOptimalDSetDimensionInfo(
                  pastUsageBusinessEntityInfo, true);
         opList.add(optimalDSetDimensionInfo);
      }
      return opList;
   }

   public List<OptimalDSetMeasureInfo> transformPastUsageMeasureInfo (
            List<QueryHistoryBusinessEntityInfo> pastUsageBusinessEntityInfos) {
      List<OptimalDSetMeasureInfo> opList = new ArrayList<OptimalDSetMeasureInfo>();
      if (CollectionUtils.isEmpty(pastUsageBusinessEntityInfos)) {
         return opList;
      }
      for (QueryHistoryBusinessEntityInfo pastUsageBusinessEntityInfo : pastUsageBusinessEntityInfos) {
         OptimalDSetMeasureInfo optimalDSetDimensionInfo = (OptimalDSetMeasureInfo) getOptimalDSetDimensionInfo(
                  pastUsageBusinessEntityInfo, false);
         opList.add(optimalDSetDimensionInfo);
      }
      return opList;
   }

   private OptimalDSetBusinessEntityInfo getOptimalDSetDimensionInfo (
            QueryHistoryBusinessEntityInfo pastUsageBusinessEntityInfo, boolean isDimension) {
      Long businessEntityId = pastUsageBusinessEntityInfo.getBusinessEntityId();
      String name = pastUsageBusinessEntityInfo.getName();
      double usagePercentage = pastUsageBusinessEntityInfo.getUsagePercentage();
      if (isDimension) {
         return new OptimalDSetDimensionInfo(businessEntityId, name, usagePercentage);
      } else {
         return new OptimalDSetMeasureInfo(businessEntityId, name, usagePercentage);
      }
   }

   private List<OptimalDSetDimensionInfo> getOptimalDSetDimensionInfos (List<OptimalDSetSWIInfo> optimalDSetSwiInfos) {
      List<OptimalDSetDimensionInfo> optimalDSetDimensionInfos = new ArrayList<OptimalDSetDimensionInfo>();

      if (ExecueCoreUtil.isCollectionEmpty(optimalDSetSwiInfos)) {
         return optimalDSetDimensionInfos;
      }

      for (OptimalDSetSWIInfo optimalDSetSWIInfo : optimalDSetSwiInfos) {
         OptimalDSetDimensionInfo optimalDSetDimensionInfo = tranformOptimalDSetSwiInfo(optimalDSetSWIInfo);
         optimalDSetDimensionInfos.add(optimalDSetDimensionInfo);
      }
      return optimalDSetDimensionInfos;
   }

   private OptimalDSetDimensionInfo tranformOptimalDSetSwiInfo (OptimalDSetSWIInfo optimalDSetSWIInfo) {
      OptimalDSetDimensionInfo optimalDSetDimensionInfo = new OptimalDSetDimensionInfo();
      optimalDSetDimensionInfo.setBedId(optimalDSetSWIInfo.getBedId());
      optimalDSetDimensionInfo.setName(optimalDSetSWIInfo.getConceptName());
      optimalDSetDimensionInfo.setNumMembers(optimalDSetSWIInfo.getMemberCount());
      optimalDSetDimensionInfo.setRange(optimalDSetSWIInfo.getRange());
      // TODO: NK:: Need to check what percentage we need to set here
      optimalDSetDimensionInfo.setUsagePercentage(null);
      return optimalDSetDimensionInfo;
   }

   public List<String> getDimensionBEDIDsForContextComparision (List<OptimalDSetDimensionInfo> optimalDSetDimensionInfos) {
      List<String> dimensionBEDIDs = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionEmpty(optimalDSetDimensionInfos)) {
         return dimensionBEDIDs;
      }
      String bedIdRangeIdDelimiter = getQueryDataConfigurationService().getSqlConcatDelimeter();
      Long dynamicRangeId = getCoreConfigurationService().getDynamicRangeId();
      for (OptimalDSetDimensionInfo optimalDSetDimensionInfo : optimalDSetDimensionInfos) {
         String bedId = optimalDSetDimensionInfo.getBedId().toString();
         if (optimalDSetDimensionInfo.getRange() == null) {
            dimensionBEDIDs.add(bedId);
         } else {
            // TODO: NK: Later can remove null check when we are sure that range id will always come, currently it is
            // not
            // coming for dynamic ranges
            Long rangeId = optimalDSetDimensionInfo.getRange().getId() == null ? dynamicRangeId
                     : optimalDSetDimensionInfo.getRange().getId();
            dimensionBEDIDs.add(bedId + bedIdRangeIdDelimiter + rangeId);
         }
      }
      return dimensionBEDIDs;
   }

   public List<String> getDimensionBEDIDsForContextComparision (CubeCreationContext cubeCreationContext) {
      List<String> dimensionBEDIDs = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(cubeCreationContext.getSimpleLookupDimensionBEDIDs())) {
         for (Long bedId : cubeCreationContext.getSimpleLookupDimensionBEDIDs()) {
            dimensionBEDIDs.add(bedId.toString());
         }
      }
      if (cubeCreationContext.getRangeLookupDimensionBEDIDs() != null) {
         List<Long> rangeIds = null;
         String bedIdRangeIdDelimiter = getQueryDataConfigurationService().getSqlConcatDelimeter();
         for (Long bedId : cubeCreationContext.getRangeLookupDimensionBEDIDs().keySet()) {
            rangeIds = cubeCreationContext.getRangeLookupDimensionBEDIDs().get(bedId);
            for (Long rangeId : rangeIds) {
               dimensionBEDIDs.add(bedId + bedIdRangeIdDelimiter + rangeId);
            }
         }
      }
      return dimensionBEDIDs;
   }

   public TreeSet<String> getBusinessEntitiesForContextComparision (List<Long> businessEntityIds) {
      TreeSet<String> businessEntities = new TreeSet<String>();
      for (Long id : businessEntityIds) {
         businessEntities.add(String.valueOf(id));
      }
      return businessEntities;
   }

   public IOptimalDSetSpaceCalculationService getOptimalDSetSpaceCalculationService () {
      return optimalDSetSpaceCalculationService;
   }

   public void setOptimalDSetSpaceCalculationService (
            IOptimalDSetSpaceCalculationService optimalDSetSpaceCalculationService) {
      this.optimalDSetSpaceCalculationService = optimalDSetSpaceCalculationService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService
    *           the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   /**
    * @return the queryDataConfigurationService
    */
   public IQueryDataConfigurationService getQueryDataConfigurationService () {
      return queryDataConfigurationService;
   }

   /**
    * @param queryDataConfigurationService
    *           the queryDataConfigurationService to set
    */
   public void setQueryDataConfigurationService (IQueryDataConfigurationService queryDataConfigurationService) {
      this.queryDataConfigurationService = queryDataConfigurationService;
   }
}