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

import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCandidateDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelInputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelOutputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetStaticLevelInputInfo;
import com.execue.ac.algorithm.optimaldset.comparator.OptimalDSetCandidateDimensionPatternSpaceAscComparator;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetConstraintValidationService;
import com.execue.ac.algorithm.optimaldset.util.OptimalDSetUtil;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.util.AnswersCatalogUtil;

/*
 * This service takes the current level patterns and tries to validate them for the constraints defined. It builds the
 * output info for the level based on constraints or passed or failed. The output level bean has all the information
 * required.
 */
public class OptimalDSetConstraintValidationServiceImpl implements IOptimalDSetConstraintValidationService {

   @Override
   public OptimalDSetLevelOutputInfo validateConstraints (
            List<OptimalDSetCandidateDimensionPattern> currentLevelPatterns, OptimalDSetLevelInputInfo levelInputInfo)
            throws AnswersCatalogException {
      OptimalDSetStaticLevelInputInfo staticLevelInputInfo = levelInputInfo.getOptimalDSetStaticLevelInputInfo();
      Double minUsagePercentage = staticLevelInputInfo.getMinUsagePercentage();
      Double maxAllowedSpace = (staticLevelInputInfo.getMaxSpacePercentage() / 100)
               * staticLevelInputInfo.getParentAssetSpace();

      // sort the current level patterns by space.
      Collections.sort(currentLevelPatterns, new OptimalDSetCandidateDimensionPatternSpaceAscComparator());

      List<OptimalDSetCandidateDimensionPattern> constrainedLevelPatterns = new ArrayList<OptimalDSetCandidateDimensionPattern>();
      Double totalSpace = 0d;
      Double totalUsage = 0d;
      boolean spaceConstraintMet = false;
      boolean usageCriteriaMet = false;

      for (OptimalDSetCandidateDimensionPattern currentLevelPattern : currentLevelPatterns) {
         Double tempTotalSpace = totalSpace;
         Double tempTotalUsage = totalUsage;
         List<OptimalDSetCandidateDimensionPattern> tempConstrainedPatterns = new ArrayList<OptimalDSetCandidateDimensionPattern>(
                  constrainedLevelPatterns);
         tempConstrainedPatterns.add(currentLevelPattern);
         tempTotalSpace = OptimalDSetUtil.calculateSpace(tempConstrainedPatterns);
         tempTotalUsage = OptimalDSetUtil.calculateUsage(tempConstrainedPatterns);
         if (tempTotalSpace >= maxAllowedSpace
                  && levelInputInfo.getOptimalDSetStaticLevelInputInfo().isApplyConstraints()) {
            spaceConstraintMet = true;
            break;
         } else {
            if (!usageCriteriaMet && tempTotalUsage >= minUsagePercentage) {
               usageCriteriaMet = true;
            }
            // copy the temp to actual list, if space is not met
            constrainedLevelPatterns = tempConstrainedPatterns;
            totalUsage = tempTotalUsage;
            totalSpace = tempTotalSpace;
         }
      }

      OptimalDSetLevelOutputInfo levelOutputInfo = new OptimalDSetLevelOutputInfo();
      levelOutputInfo.setLevelPatterns(currentLevelPatterns);
      levelOutputInfo.setConstrainedLevelPatterns(constrainedLevelPatterns);
      levelOutputInfo.setLevelNum(levelInputInfo.getCurrentLevelNum());
      levelOutputInfo.setSpaceConstraintMet(spaceConstraintMet);
      levelOutputInfo.setTotalSpace(totalSpace);
      levelOutputInfo.setTotalUsage(totalUsage);
      levelOutputInfo.setTotalSpaceInPercentageOfParentAsset(AnswersCatalogUtil.getRoundedValue(
               (totalSpace / staticLevelInputInfo.getParentAssetSpace()) * 100, 2));
      levelOutputInfo.setUsageCriteriaMet(usageCriteriaMet);
      return levelOutputInfo;
   }
}
