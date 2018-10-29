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
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCandidateDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelInputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelOutputInfo;
import com.execue.ac.algorithm.optimaldset.helper.OptimalDSetHelper;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetConstraintValidationService;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetIdentificationService;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetLevelProcessingService;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetRestructuringService;
import com.execue.ac.algorithm.optimaldset.util.OptimalDSetUtil;
import com.execue.ac.exception.AnswersCatalogException;

public class OptimalDSetLevelProcessingServiceImpl implements IOptimalDSetLevelProcessingService {

   private IOptimalDSetIdentificationService       optimalDSetIdentificationService;
   private IOptimalDSetRestructuringService        optimalDSetRestructuringService;
   private IOptimalDSetConstraintValidationService optimalDSetConstraintValidationService;
   private OptimalDSetHelper                       optimalDSetHelper;
   private static final Logger                     logger = Logger
                                                                   .getLogger(OptimalDSetLevelProcessingServiceImpl.class);

   /**
    * This method process level input and returns level output. For any level, it tries to find patterns followed by
    * running validations rules to minimize the patterns.It then runs constraint validation to get the output of that
    * level. In case identified patterns are same as previous level, we dont need to run validation rules neither we
    * need to run constraint valdiation service. Therefore previous level output is the same output of this level also
    */
   @Override
   public OptimalDSetLevelOutputInfo processLevel (OptimalDSetLevelInputInfo levelInputInfo)
            throws AnswersCatalogException {
      OptimalDSetLevelOutputInfo levelOutputInfo = null;
      if (logger.isDebugEnabled()) {
         logger.debug("Level " + levelInputInfo.getCurrentLevelNum() + " Processing Starts");
      }
      List<OptimalDSetCandidateDimensionPattern> currentLevelPatterns = getOptimalDSetIdentificationService()
               .identifyPatterns(levelInputInfo);
      // display the identified patterns
      if (logger.isDebugEnabled()) {
         logger.debug("Identitified Patterns List : \n");
      }
      OptimalDSetUtil.displayCandidatePatterns(currentLevelPatterns);

      // check if the current level identified patterns are exactly same as previous level patterns, we don't need to run
      // restructuring and constraint validation, rather clone the previous level and that becomes the output of this
      // level.

      if (isCandidatePatternsSame(currentLevelPatterns, levelInputInfo.getPreviousLevelOutputInfo())) {
         levelOutputInfo = OptimalDSetUtil.cloneOptimalDSetLevelOutputInfo(levelInputInfo.getPreviousLevelOutputInfo());
         // set the current level num
         levelOutputInfo.setLevelNum(levelInputInfo.getCurrentLevelNum());
      } else {
         levelOutputInfo = performRestructuringAndConstraintValidation(currentLevelPatterns, levelInputInfo);
      }
      return levelOutputInfo;
   }

   private boolean isCandidatePatternsSame (List<OptimalDSetCandidateDimensionPattern> currentLevelPatterns,
            OptimalDSetLevelOutputInfo previousLevelOutputInfo) {
      boolean isCandidatePatternsSame = false;
      if (previousLevelOutputInfo != null) {
         List<OptimalDSetCandidateDimensionPattern> previousLevelPatterns = previousLevelOutputInfo.getLevelPatterns();
         if (currentLevelPatterns.size() == previousLevelPatterns.size()) {
            if (previousLevelPatterns.containsAll(previousLevelPatterns)) {
               isCandidatePatternsSame = true;
            }
         }
      }
      return isCandidatePatternsSame;
   }

   private OptimalDSetLevelOutputInfo performRestructuringAndConstraintValidation (
            List<OptimalDSetCandidateDimensionPattern> identifiedPatterns, OptimalDSetLevelInputInfo levelInputInfo)
            throws AnswersCatalogException {
      List<OptimalDSetCandidateDimensionPattern> restructuredPatterns = getOptimalDSetRestructuringService()
               .restructurePatterns(identifiedPatterns, levelInputInfo);
      // display the restructured patterns
      if (logger.isDebugEnabled()) {
         logger.debug("Restructured Patterns List : \n");
      }
      OptimalDSetUtil.displayCandidatePatterns(restructuredPatterns);

      // space calculation
      getOptimalDSetHelper().populateSpace(restructuredPatterns,
               levelInputInfo.getOptimalDSetSpaceCalculationInputInfo());

      // display the restructured patterns after space calculation
      if (logger.isDebugEnabled()) {
         logger.debug("After Space Calculation Patterns List : \n");
      }
      OptimalDSetUtil.displayCandidatePatterns(restructuredPatterns);

      // validate the constraints
      OptimalDSetLevelOutputInfo levelOutputInfo = getOptimalDSetConstraintValidationService().validateConstraints(
               restructuredPatterns, levelInputInfo);
      // display the constrainted patterns
      if (logger.isDebugEnabled()) {
         logger.debug("Final Constrained Patterns List : \n");
      }
      OptimalDSetUtil.displayCandidatePatterns(levelOutputInfo.getConstrainedLevelPatterns());
      if (logger.isDebugEnabled()) {
         logger.debug("Level " + levelInputInfo.getCurrentLevelNum() + " Processing Ends");
      }
      return levelOutputInfo;
   }

   public IOptimalDSetIdentificationService getOptimalDSetIdentificationService () {
      return optimalDSetIdentificationService;
   }

   public void setOptimalDSetIdentificationService (IOptimalDSetIdentificationService optimalDSetIdentificationService) {
      this.optimalDSetIdentificationService = optimalDSetIdentificationService;
   }

   public IOptimalDSetRestructuringService getOptimalDSetRestructuringService () {
      return optimalDSetRestructuringService;
   }

   public void setOptimalDSetRestructuringService (IOptimalDSetRestructuringService optimalDSetRestructuringService) {
      this.optimalDSetRestructuringService = optimalDSetRestructuringService;
   }

   public IOptimalDSetConstraintValidationService getOptimalDSetConstraintValidationService () {
      return optimalDSetConstraintValidationService;
   }

   public void setOptimalDSetConstraintValidationService (
            IOptimalDSetConstraintValidationService optimalDSetConstraintValidationService) {
      this.optimalDSetConstraintValidationService = optimalDSetConstraintValidationService;
   }

   public OptimalDSetHelper getOptimalDSetHelper () {
      return optimalDSetHelper;
   }

   public void setOptimalDSetHelper (OptimalDSetHelper optimalDSetHelper) {
      this.optimalDSetHelper = optimalDSetHelper;
   }

}
