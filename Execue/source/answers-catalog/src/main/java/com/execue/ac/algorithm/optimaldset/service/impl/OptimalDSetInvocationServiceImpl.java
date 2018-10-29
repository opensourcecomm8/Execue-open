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


package com.execue.ac.algorithm.optimaldset.service.impl;

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCubeOutputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetDimensionInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelInputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelOutputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetMartOutputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetMartStaticLevelInputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetMeasureInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetSpaceCalculationInputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetStaticLevelInputInfo;
import com.execue.ac.algorithm.optimaldset.comparator.OptimalDSetBusinessEntityInfoUsageDescComparator;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetContextPopulationService;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetInvocationService;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetLevelProcessingService;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetOutputDeciderService;
import com.execue.ac.algorithm.optimaldset.util.OptimalDSetUtil;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.core.util.ExecueCoreUtil;

public class OptimalDSetInvocationServiceImpl implements IOptimalDSetInvocationService {

   private static final Logger                  logger = Logger.getLogger(OptimalDSetInvocationServiceImpl.class);

   private IOptimalDSetContextPopulationService optimalDSetContextPopulationService;
   private IOptimalDSetLevelProcessingService   optimalDSetLevelProcessingService;
   private IOptimalDSetOutputDeciderService     optimalDSetOutputDeciderService;

   /**
    * This method prepares the static information followed by processing 'n' levels where 'n' is the max number of
    * distinct dimension in all past patterns.It keeps on collecting each level output. It gives all the levels output
    * to best decider which chooses the best among them
    */
   @Override
   public OptimalDSetCubeOutputInfo generateCubeOptimalDset (Long parentAssetId, Long modelId)
            throws AnswersCatalogException {
      OptimalDSetStaticLevelInputInfo staticLevelInputInfo = getOptimalDSetContextPopulationService()
               .populateStaticLevelInputInfo(parentAssetId, modelId);
      if (ExecueCoreUtil.isCollectionEmpty(staticLevelInputInfo.getDistinctDimensions())) {
         logger.error("No dimension information found in the history");
         return null;
      }

      OptimalDSetSpaceCalculationInputInfo spaceCalculationInputInfo = getOptimalDSetContextPopulationService()
               .populateSpaceCalculationInputInfo(parentAssetId, staticLevelInputInfo);
      // This is to start with because at previous levels we will not get any cubes
      Integer minDimensionPatternSize = OptimalDSetUtil.getMinDimensionPatternSize(staticLevelInputInfo
               .getAllPastUsagePatterns());

      OptimalDSetUtil.displayStaticLevelInputInfo(staticLevelInputInfo);

      List<OptimalDSetLevelOutputInfo> levelOutputInfoList = new ArrayList<OptimalDSetLevelOutputInfo>();
      OptimalDSetLevelOutputInfo previousLevelOutputInfo = null;
      for (Integer currentLevelNum = minDimensionPatternSize; currentLevelNum <= staticLevelInputInfo
               .getDistinctDimensions().size(); currentLevelNum++) {
         OptimalDSetLevelInputInfo levelInputInfo = OptimalDSetUtil.buildLevelInputInfo(previousLevelOutputInfo,
                  currentLevelNum, staticLevelInputInfo, spaceCalculationInputInfo);
         OptimalDSetLevelOutputInfo levelOutputInfo = getOptimalDSetLevelProcessingService().processLevel(
                  levelInputInfo);
         // input for next level.
         previousLevelOutputInfo = levelOutputInfo;
         levelOutputInfoList.add(levelOutputInfo);
      }
      // display all the levels
      OptimalDSetUtil.displayLevelOutputInfoList(levelOutputInfoList);
      OptimalDSetCubeOutputInfo bestOptimalDSet = getOptimalDSetOutputDeciderService().chooseBestOptimalDSet(
               levelOutputInfoList);
      // display the final output
      OptimalDSetUtil.displayCubeOutputInfo(bestOptimalDSet);
      return bestOptimalDSet;
   }

   /**
    * This method populates the static information and then use them to get top 'n' measures and top 'm' dimensions from
    * past history.
    */
   @Override
   public OptimalDSetMartOutputInfo generateMartOptimalDset (Long parentAssetId, Long modelId)
            throws AnswersCatalogException {
      OptimalDSetMartStaticLevelInputInfo martStaticLevelInputInfo = getOptimalDSetContextPopulationService()
               .populateMartStaticLevelInputInfo(parentAssetId, modelId);
      Integer maxEligibleDimensions = martStaticLevelInputInfo.getMaxEligibleDimensions();
      Integer maxEligibleMeasures = martStaticLevelInputInfo.getMaxEligibleMeasures();
      List<OptimalDSetDimensionInfo> pastUsageDimensions = martStaticLevelInputInfo.getPastUsageDimensions();
      List<OptimalDSetMeasureInfo> pastUsageMeasures = martStaticLevelInputInfo.getPastUsageMeasures();
      if (pastUsageDimensions.size() > maxEligibleDimensions) {
         Collections.sort(pastUsageDimensions, new OptimalDSetBusinessEntityInfoUsageDescComparator());
      } else {
         maxEligibleDimensions = pastUsageDimensions.size();
      }
      if (pastUsageMeasures.size() > maxEligibleMeasures) {
         Collections.sort(pastUsageMeasures, new OptimalDSetBusinessEntityInfoUsageDescComparator());
      } else {
         maxEligibleMeasures = pastUsageMeasures.size();
      }
      List<OptimalDSetDimensionInfo> outputDimensions = pastUsageDimensions.subList(0, maxEligibleDimensions);
      List<OptimalDSetMeasureInfo> outputMeasures = pastUsageMeasures.subList(0, maxEligibleMeasures);
      return new OptimalDSetMartOutputInfo(outputDimensions, outputMeasures);
   }

   public IOptimalDSetLevelProcessingService getOptimalDSetLevelProcessingService () {
      return optimalDSetLevelProcessingService;
   }

   public void setOptimalDSetLevelProcessingService (
            IOptimalDSetLevelProcessingService optimalDSetLevelProcessingService) {
      this.optimalDSetLevelProcessingService = optimalDSetLevelProcessingService;
   }

   public IOptimalDSetOutputDeciderService getOptimalDSetOutputDeciderService () {
      return optimalDSetOutputDeciderService;
   }

   public void setOptimalDSetOutputDeciderService (IOptimalDSetOutputDeciderService optimalDSetOutputDeciderService) {
      this.optimalDSetOutputDeciderService = optimalDSetOutputDeciderService;
   }

   public IOptimalDSetContextPopulationService getOptimalDSetContextPopulationService () {
      return optimalDSetContextPopulationService;
   }

   public void setOptimalDSetContextPopulationService (
            IOptimalDSetContextPopulationService optimalDSetContextPopulationService) {
      this.optimalDSetContextPopulationService = optimalDSetContextPopulationService;
   }

}
