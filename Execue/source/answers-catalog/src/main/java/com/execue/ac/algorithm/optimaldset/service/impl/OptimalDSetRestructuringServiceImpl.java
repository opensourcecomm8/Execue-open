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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCandidateDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelInputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetPastUsageDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetSpaceCalculationInputInfo;
import com.execue.ac.algorithm.optimaldset.comparator.OptimalDSetCandidateDimensionPatternSpaceDescComparator;
import com.execue.ac.algorithm.optimaldset.helper.OptimalDSetHelper;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetRestructuringService;
import com.execue.ac.algorithm.optimaldset.util.OptimalDSetUtil;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.core.util.ExecueCoreUtil;

public class OptimalDSetRestructuringServiceImpl implements IOptimalDSetRestructuringService {

   private OptimalDSetHelper optimalDSetHelper;

   /**
    * This method performs restructuring of the current patterns to optimize them, de-dup them etc.
    */
   @Override
   public List<OptimalDSetCandidateDimensionPattern> restructurePatterns (
            List<OptimalDSetCandidateDimensionPattern> currentLevelPatterns, OptimalDSetLevelInputInfo levelInputInfo)
            throws AnswersCatalogException {
      OptimalDSetSpaceCalculationInputInfo spaceCalculationInputInfo = levelInputInfo
               .getOptimalDSetSpaceCalculationInputInfo();
      List<OptimalDSetCandidateDimensionPattern> restructuredPatterns = new ArrayList<OptimalDSetCandidateDimensionPattern>();

      restructuredPatterns = deDupBasedOnSameDimensionPatterns(currentLevelPatterns);
      // calculate the space & sort by desc order to make sure bigger cubes go off.
      getOptimalDSetHelper().populateSpace(restructuredPatterns, spaceCalculationInputInfo);
      Collections.sort(restructuredPatterns, new OptimalDSetCandidateDimensionPatternSpaceDescComparator());

      restructuredPatterns = deDupBasedOnAllSubSetsAnswered(restructuredPatterns);

      // calculate the space & sort by desc order to make sure bigger cubes go off.
      getOptimalDSetHelper().populateSpace(restructuredPatterns, spaceCalculationInputInfo);
      Collections.sort(restructuredPatterns, new OptimalDSetCandidateDimensionPatternSpaceDescComparator());
      restructuredPatterns = restructureForSmallerCubesBasedOnSubSetsAnswered(restructuredPatterns);
      return restructuredPatterns;
   }

   /**
    * This method restructures the cubes to make them smaller in size. It removes the subsets which can be answered by
    * other patterns and makes a smaller pattern with pending subsets which cannot be answered.
    * 
    * @param restructuredPatterns
    * @return
    */
   private List<OptimalDSetCandidateDimensionPattern> restructureForSmallerCubesBasedOnSubSetsAnswered (
            List<OptimalDSetCandidateDimensionPattern> restructuredPatterns) {
      List<OptimalDSetCandidateDimensionPattern> deDupedCandidatePatterns = new ArrayList<OptimalDSetCandidateDimensionPattern>(
               restructuredPatterns);
      Set<OptimalDSetCandidateDimensionPattern> patternsMarkedForDeletion = new HashSet<OptimalDSetCandidateDimensionPattern>();
      Set<OptimalDSetCandidateDimensionPattern> patternsMarkedForAddition = new HashSet<OptimalDSetCandidateDimensionPattern>();
      for (OptimalDSetCandidateDimensionPattern candidatePattern : restructuredPatterns) {
         Set<OptimalDSetPastUsageDimensionPattern> setsAnsweredByOtherPatterns = getSubSetsAnsweredByOtherPatterns(
                  restructuredPatterns, candidatePattern, patternsMarkedForDeletion, patternsMarkedForAddition);
         if (ExecueCoreUtil.isCollectionNotEmpty(setsAnsweredByOtherPatterns)) {
            patternsMarkedForDeletion.add(candidatePattern);
            Set<OptimalDSetPastUsageDimensionPattern> totalSubSets = new HashSet<OptimalDSetPastUsageDimensionPattern>(
                     candidatePattern.getSubSets());
            totalSubSets.removeAll(setsAnsweredByOtherPatterns);
            patternsMarkedForAddition.add(OptimalDSetUtil.buildCandidatePattern(totalSubSets));
         }
      }
      // if patterns are marked for deletion, remove them from the actual list
      if (ExecueCoreUtil.isCollectionNotEmpty(patternsMarkedForDeletion)) {
         deDupedCandidatePatterns.removeAll(patternsMarkedForDeletion);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(patternsMarkedForAddition)) {
         deDupedCandidatePatterns.addAll(patternsMarkedForAddition);
      }
      return deDupedCandidatePatterns;
   }

   /**
    * This method gets all subsets of the candidate pattern which can be answered by restructured patterns in terms of
    * subsets.It also merges the subsets to the patterns where they said can answer this subset.
    * 
    * @param restructuredPatterns
    * @param currentCandidatePattern
    * @param patternsMarkedForDeletion
    * @param patternsMarkedForAddition
    * @return
    */
   private Set<OptimalDSetPastUsageDimensionPattern> getSubSetsAnsweredByOtherPatterns (
            List<OptimalDSetCandidateDimensionPattern> restructuredPatterns,
            OptimalDSetCandidateDimensionPattern currentCandidatePattern,
            Set<OptimalDSetCandidateDimensionPattern> patternsMarkedForDeletion,
            Set<OptimalDSetCandidateDimensionPattern> patternsMarkedForAddition) {
      Set<OptimalDSetPastUsageDimensionPattern> subSetsAnsweredByOtherPatternSubSets = new HashSet<OptimalDSetPastUsageDimensionPattern>();
      // prepare list to check for duplicates
      Set<OptimalDSetCandidateDimensionPattern> setToCheckForDuplicates = new HashSet<OptimalDSetCandidateDimensionPattern>(
               restructuredPatterns);

      setToCheckForDuplicates.remove(currentCandidatePattern);
      if (ExecueCoreUtil.isCollectionNotEmpty(patternsMarkedForDeletion)) {
         setToCheckForDuplicates.removeAll(patternsMarkedForDeletion);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(patternsMarkedForAddition)) {
         setToCheckForDuplicates.addAll(patternsMarkedForAddition);
      }
      for (OptimalDSetPastUsageDimensionPattern pastUsageDimensionPattern : currentCandidatePattern.getSubSets()) {
         // get all the patterns which answers this past usage subset

         Set<OptimalDSetCandidateDimensionPattern> matchingCandidatePatternsBySubSet = findMatchingCandidatePatternsBySubSet(
                  pastUsageDimensionPattern, setToCheckForDuplicates);
         if (ExecueCoreUtil.isCollectionNotEmpty(matchingCandidatePatternsBySubSet)) {
            subSetsAnsweredByOtherPatternSubSets.add(pastUsageDimensionPattern);
            // adjust the candidate patterns for the subsets they can answer
            adjustCandidatePatternsSubSets(matchingCandidatePatternsBySubSet, pastUsageDimensionPattern);
         }

      }
      return subSetsAnsweredByOtherPatternSubSets;
   }

   private void adjustCandidatePatternsSubSets (Set<OptimalDSetCandidateDimensionPattern> candidatePatterns,
            OptimalDSetPastUsageDimensionPattern pastUsageDimensionPattern) {
      for (OptimalDSetCandidateDimensionPattern candidatePattern : candidatePatterns) {
         if (!candidatePattern.getSubSets().contains(pastUsageDimensionPattern)) {
            candidatePattern.getSubSets().add(pastUsageDimensionPattern);
         }
      }
   }

   private Set<OptimalDSetCandidateDimensionPattern> findMatchingCandidatePatternsBySubSet (
            OptimalDSetPastUsageDimensionPattern subSetPattern,
            Set<OptimalDSetCandidateDimensionPattern> candidatePatterns) {
      Set<OptimalDSetCandidateDimensionPattern> matchingCandidatePatterns = new HashSet<OptimalDSetCandidateDimensionPattern>();
      for (OptimalDSetCandidateDimensionPattern candidatePattern : candidatePatterns) {
         if (OptimalDSetUtil.isPatternSubSetOfPastUsagePatterns(subSetPattern, candidatePattern.getSubSets())) {
            matchingCandidatePatterns.add(candidatePattern);
         }
      }
      return matchingCandidatePatterns;
   }

   /**
    * This method checks if all of the subsets of pattern is being answered by other patterns(in terms of subsets), we
    * can take off this pattern and add the current level patterns to each of the patterns where they are saying that
    * they can answer these patterns
    * 
    * @param restructuredPatterns
    * @return
    */
   private List<OptimalDSetCandidateDimensionPattern> deDupBasedOnAllSubSetsAnswered (
            List<OptimalDSetCandidateDimensionPattern> restructuredPatterns) {
      List<OptimalDSetCandidateDimensionPattern> deDupedCandidatePatterns = new ArrayList<OptimalDSetCandidateDimensionPattern>(
               restructuredPatterns);
      Set<OptimalDSetCandidateDimensionPattern> patternsMarkedForDeletion = new HashSet<OptimalDSetCandidateDimensionPattern>();
      for (OptimalDSetCandidateDimensionPattern candidatePattern : restructuredPatterns) {
         if (isAllSubSetsAnsweredByOtherPatterns(restructuredPatterns, candidatePattern, patternsMarkedForDeletion)) {
            patternsMarkedForDeletion.add(candidatePattern);
         }
      }
      // if patterns are marked for deletion, remove them from the actual list
      if (ExecueCoreUtil.isCollectionNotEmpty(patternsMarkedForDeletion)) {
         deDupedCandidatePatterns.removeAll(patternsMarkedForDeletion);
      }
      return deDupedCandidatePatterns;
   }

   private boolean isAllSubSetsAnsweredByOtherPatterns (
            List<OptimalDSetCandidateDimensionPattern> restructuredPatterns,
            OptimalDSetCandidateDimensionPattern currentCandidatePattern,
            Set<OptimalDSetCandidateDimensionPattern> patternsMarkedForDeletion) {
      boolean allSubSetsAnswered = false;

      Set<OptimalDSetPastUsageDimensionPattern> subSetsAnsweredByOtherPatterns = getSubSetsAnsweredByOtherPatterns(
               restructuredPatterns, currentCandidatePattern, patternsMarkedForDeletion,
               new HashSet<OptimalDSetCandidateDimensionPattern>());
      if (ExecueCoreUtil.isCollectionNotEmpty(subSetsAnsweredByOtherPatterns)) {
         if (subSetsAnsweredByOtherPatterns.size() == currentCandidatePattern.getSubSets().size()) {
            allSubSetsAnswered = true;
         }
      }
      return allSubSetsAnswered;
   }

   /**
    * This method de-dups the patterns which are exactly same but may be having different subsets to be answered like
    * ABC is answering AB and C, ABC is answering A and BC, but we can have only single patterns ABC with all 4 subsets
    * answering.
    * 
    * @param currentLevelPatterns
    * @return
    */
   private List<OptimalDSetCandidateDimensionPattern> deDupBasedOnSameDimensionPatterns (
            List<OptimalDSetCandidateDimensionPattern> currentLevelPatterns) {
      List<OptimalDSetCandidateDimensionPattern> deDupedCandidatePatterns = new ArrayList<OptimalDSetCandidateDimensionPattern>(
               currentLevelPatterns);
      Set<OptimalDSetCandidateDimensionPattern> duplicatePatternsFound = new HashSet<OptimalDSetCandidateDimensionPattern>();
      List<List<OptimalDSetCandidateDimensionPattern>> duplicatePatternSetsFound = new ArrayList<List<OptimalDSetCandidateDimensionPattern>>();
      for (int index = 0; index < currentLevelPatterns.size() - 1; index++) {
         OptimalDSetCandidateDimensionPattern candidatePattern = currentLevelPatterns.get(index);
         if (!duplicatePatternsFound.contains(candidatePattern)) {
            List<OptimalDSetCandidateDimensionPattern> listToCheckForDuplicates = currentLevelPatterns.subList(
                     index + 1, currentLevelPatterns.size());
            List<OptimalDSetCandidateDimensionPattern> duplicatesSetFound = findDuplicatesByPattern(candidatePattern,
                     listToCheckForDuplicates);
            if (ExecueCoreUtil.isCollectionNotEmpty(duplicatesSetFound)) {
               duplicatePatternsFound.addAll(duplicatesSetFound);
               duplicatePatternSetsFound.add(duplicatesSetFound);
            }
         }
      }
      // remove all duplicates from the actual list
      // if any duplicate sets found, need to merge each set and replace the patterns with one merged pattern
      if (ExecueCoreUtil.isCollectionNotEmpty(duplicatePatternsFound)) {
         deDupedCandidatePatterns.removeAll(duplicatePatternsFound);
         for (List<OptimalDSetCandidateDimensionPattern> duplicatePatternSet : duplicatePatternSetsFound) {
            deDupedCandidatePatterns.add(OptimalDSetUtil.mergeCandidatePatterns(duplicatePatternSet));
         }
      }
      return deDupedCandidatePatterns;
   }

   @SuppressWarnings ("unchecked")
   private List<OptimalDSetCandidateDimensionPattern> findDuplicatesByPattern (
            OptimalDSetCandidateDimensionPattern currentCandidatePattern,
            List<OptimalDSetCandidateDimensionPattern> listToCheckForDuplicates) {
      List<OptimalDSetCandidateDimensionPattern> duplicatePatternSet = new ArrayList<OptimalDSetCandidateDimensionPattern>();
      for (OptimalDSetCandidateDimensionPattern candidatePattern : listToCheckForDuplicates) {
         // check duplicate based on pattern and not on whole object
         if (new OptimalDSetDimensionPattern(currentCandidatePattern.getDimensions())
                  .equals(new OptimalDSetDimensionPattern(candidatePattern.getDimensions()))) {
            duplicatePatternSet.add(candidatePattern);
         }
      }
      // if found duplicates, add current to the list to make it full set
      if (ExecueCoreUtil.isCollectionNotEmpty(duplicatePatternSet)) {
         duplicatePatternSet.add(currentCandidatePattern);
      }
      return duplicatePatternSet;
   }

   public OptimalDSetHelper getOptimalDSetHelper () {
      return optimalDSetHelper;
   }

   public void setOptimalDSetHelper (OptimalDSetHelper optimalDSetHelper) {
      this.optimalDSetHelper = optimalDSetHelper;
      ;
   }

}
