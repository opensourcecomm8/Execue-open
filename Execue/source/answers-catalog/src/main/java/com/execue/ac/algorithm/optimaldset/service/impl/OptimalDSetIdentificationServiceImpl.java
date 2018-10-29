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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCandidateDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelInputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelOutputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetPastUsageDimensionPattern;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetIdentificationService;
import com.execue.ac.algorithm.optimaldset.util.OptimalDSetUtil;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.core.util.ExecueCoreUtil;

public class OptimalDSetIdentificationServiceImpl implements IOptimalDSetIdentificationService {

   // three ways to identify patterns
   // 1st from past patterns
   // second from merging previous level patterns
   // 3rd what is left at previous level unmerged ones
   @Override
   public List<OptimalDSetCandidateDimensionPattern> identifyPatterns (OptimalDSetLevelInputInfo levelInputInfo)
            throws AnswersCatalogException {
      List<OptimalDSetCandidateDimensionPattern> levelPatterns = new ArrayList<OptimalDSetCandidateDimensionPattern>();

      // step 1 - identifying patterns from past history
      List<OptimalDSetCandidateDimensionPattern> patternsFromPastHistory = identifyPatternsFromPastHistory(levelInputInfo);
      if (ExecueCoreUtil.isCollectionNotEmpty(patternsFromPastHistory)) {
         levelPatterns.addAll(patternsFromPastHistory);
      }
      // step 2 and 3 - identifying patterns from previous level
      List<OptimalDSetCandidateDimensionPattern> patternsFromPreviousLevel = identifyPatternsFromPreviousLevel(levelInputInfo);
      if (ExecueCoreUtil.isCollectionNotEmpty(patternsFromPreviousLevel)) {
         levelPatterns.addAll(patternsFromPreviousLevel);
      }
      return levelPatterns;
   }

   private List<OptimalDSetCandidateDimensionPattern> identifyPatternsFromPreviousLevel (
            OptimalDSetLevelInputInfo levelInputInfo) {
      List<OptimalDSetCandidateDimensionPattern> candidatePatterns = new ArrayList<OptimalDSetCandidateDimensionPattern>();
      Integer currentLevelNum = levelInputInfo.getCurrentLevelNum();
      OptimalDSetLevelOutputInfo previousLevelOutputInfo = levelInputInfo.getPreviousLevelOutputInfo();
      if (previousLevelOutputInfo != null) {
         List<OptimalDSetCandidateDimensionPattern> previousLevelPatterns = previousLevelOutputInfo.getLevelPatterns();
         if (ExecueCoreUtil.isCollectionNotEmpty(previousLevelPatterns)) {
            // try to merge all previous level patterns to generate new patterns of length equivalent to current level
            // maintain all patterns from previous level which are being participated in merging
            // whatever didnot participate in merging need to bring them up to current level
            Set<OptimalDSetCandidateDimensionPattern> mergeProcessParticipatedPatterns = new HashSet<OptimalDSetCandidateDimensionPattern>();
            // this is looping to match each with all the others
            // step 2 - merging process
            for (int outer = 0; outer < previousLevelPatterns.size() - 1; outer++) {
               OptimalDSetCandidateDimensionPattern firstCandidatePattern = previousLevelPatterns.get(outer);
               for (int inner = outer + 1; inner < previousLevelPatterns.size(); inner++) {
                  OptimalDSetCandidateDimensionPattern secondCandidatePattern = previousLevelPatterns.get(inner);
                  if (OptimalDSetUtil.isUnionPossibleOfLength(firstCandidatePattern, secondCandidatePattern,
                           currentLevelNum)) {
                     List<OptimalDSetCandidateDimensionPattern> mergedCandidatePatterns = new ArrayList<OptimalDSetCandidateDimensionPattern>();
                     mergedCandidatePatterns.add(firstCandidatePattern);
                     mergedCandidatePatterns.add(secondCandidatePattern);
                     // update the set
                     mergeProcessParticipatedPatterns.addAll(mergedCandidatePatterns);
                     candidatePatterns.add(OptimalDSetUtil.mergeCandidatePatterns(mergedCandidatePatterns));
                  }
               }
            }
            // step 3 - unmerged ones need to be considered
            List<OptimalDSetCandidateDimensionPattern> unMergedCandidatePatterns = OptimalDSetUtil
                     .getUnMergedCandidatePatterns(previousLevelPatterns, mergeProcessParticipatedPatterns);
            if (ExecueCoreUtil.isCollectionNotEmpty(unMergedCandidatePatterns)) {
               candidatePatterns.addAll(OptimalDSetUtil.cloneCandidatePatterns(unMergedCandidatePatterns));
            }
         }
      }
      return candidatePatterns;
   }

   private List<OptimalDSetCandidateDimensionPattern> identifyPatternsFromPastHistory (
            OptimalDSetLevelInputInfo levelInputInfo) {
      List<OptimalDSetCandidateDimensionPattern> candidatePatterns = new ArrayList<OptimalDSetCandidateDimensionPattern>();
      List<OptimalDSetPastUsageDimensionPattern> allPastUsagePatterns = levelInputInfo
               .getOptimalDSetStaticLevelInputInfo().getAllPastUsagePatterns();
      Integer currentLevelNum = levelInputInfo.getCurrentLevelNum();
      List<OptimalDSetPastUsageDimensionPattern> patternsOfDesiredLength = OptimalDSetUtil.findPatternsOfDesiredLength(
               allPastUsagePatterns, currentLevelNum);
      if (ExecueCoreUtil.isCollectionNotEmpty(patternsOfDesiredLength)) {
         for (OptimalDSetPastUsageDimensionPattern pastUsagePattern : patternsOfDesiredLength) {
            List<OptimalDSetPastUsageDimensionPattern> subSetPastUsagePatterns = OptimalDSetUtil
                     .findSubSetPastUsagePatterns(pastUsagePattern, allPastUsagePatterns);
            candidatePatterns.add(OptimalDSetUtil.buildCandidatePattern(subSetPastUsagePatterns));
         }
      }
      return candidatePatterns;

   }

}