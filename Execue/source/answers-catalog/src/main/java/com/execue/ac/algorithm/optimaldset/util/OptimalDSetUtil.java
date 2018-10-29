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


package com.execue.ac.algorithm.optimaldset.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCandidateDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCubeOutputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetDimensionInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelInputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetLevelOutputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetPastUsageDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetSpaceCalculationInputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetStaticLevelInputInfo;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.util.ExecueCoreUtil;


/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
public class OptimalDSetUtil {

   private static final Logger logger = Logger.getLogger(OptimalDSetUtil.class);

   public static Integer getMinDimensionPatternSize (
            List<OptimalDSetPastUsageDimensionPattern> pastUsageDimensionPatterns) {
      Set<Integer> uniquePatternSize = new HashSet<Integer>();
      for (OptimalDSetPastUsageDimensionPattern pastUsageDimensionPattern : pastUsageDimensionPatterns) {
         uniquePatternSize.add(pastUsageDimensionPattern.getDimensions().size());
      }
      return (Integer) Collections.min(uniquePatternSize);
   }

   public static OptimalDSetLevelInputInfo buildLevelInputInfo (OptimalDSetLevelOutputInfo previousLevelOutputInfo,
            Integer currentLevelNum, OptimalDSetStaticLevelInputInfo staticLevelInputInfo,
            OptimalDSetSpaceCalculationInputInfo spaceCalculationInputInfo) {
      return new OptimalDSetLevelInputInfo(previousLevelOutputInfo, currentLevelNum, staticLevelInputInfo,
               spaceCalculationInputInfo);
   }

   /**
    * This method finds the patterns which are subsets of the dimension pattern passed. Example ABC is the dimension
    * patterns, then ABC, AB, B,A,AC all are subsets patterns of ABC.
    * 
    * @param dimensionPattern
    * @param allPastUsagePatterns
    * @return subSetPatterns
    */
   public static List<OptimalDSetPastUsageDimensionPattern> findSubSetPastUsagePatterns (
            OptimalDSetDimensionPattern dimensionPattern,
            List<OptimalDSetPastUsageDimensionPattern> allPastUsagePatterns) {
      List<OptimalDSetPastUsageDimensionPattern> subSetPatterns = new ArrayList<OptimalDSetPastUsageDimensionPattern>();
      for (OptimalDSetPastUsageDimensionPattern pastUsagePattern : allPastUsagePatterns) {
         if (dimensionPattern.getDimensions().containsAll(pastUsagePattern.getDimensions())) {
            subSetPatterns.add(pastUsagePattern);
         }
      }
      return subSetPatterns;
   }

   /**
    * This method checks if pattern passed is subset of any of the past usage patterns.
    * 
    * @param dimensionPattern
    * @param allPastUsagePatterns
    * @return boolean
    */
   public static boolean isPatternSubSetOfPastUsagePatterns (OptimalDSetDimensionPattern dimensionPattern,
            List<OptimalDSetPastUsageDimensionPattern> allPastUsagePatterns) {
      boolean patternSubSetOfPastUsagePattern = false;
      for (OptimalDSetPastUsageDimensionPattern pastUsagePattern : allPastUsagePatterns) {
         if (pastUsagePattern.getDimensions().containsAll(dimensionPattern.getDimensions())) {
            patternSubSetOfPastUsagePattern = true;
            break;
         }
      }
      return patternSubSetOfPastUsagePattern;
   }

   public static Set<OptimalDSetPastUsageDimensionPattern> populateUniquePastUsagePatternSubSets (
            List<OptimalDSetCandidateDimensionPattern> candidatePatterns) {
      Set<OptimalDSetPastUsageDimensionPattern> uniquePastUsagePatterns = new HashSet<OptimalDSetPastUsageDimensionPattern>();
      for (OptimalDSetCandidateDimensionPattern candidatePattern : candidatePatterns) {
         uniquePastUsagePatterns.addAll(candidatePattern.getSubSets());
      }
      return uniquePastUsagePatterns;
   }

   /**
    * This method tells whether merging two patterns results in a pattern exactly of length "length" passed.
    * 
    * @param firstPattern
    * @param secondPattern
    * @param length
    * @return boolean
    */
   public static boolean isUnionPossibleOfLength (OptimalDSetDimensionPattern firstPattern,
            OptimalDSetDimensionPattern secondPattern, Integer length) {
      Set<OptimalDSetDimensionInfo> uniqueDimensions = new HashSet<OptimalDSetDimensionInfo>();
      uniqueDimensions.addAll(firstPattern.getDimensions());
      uniqueDimensions.addAll(secondPattern.getDimensions());
      return length.equals(uniqueDimensions.size());
   }

   public static OptimalDSetCandidateDimensionPattern mergeCandidatePatterns (
            List<OptimalDSetCandidateDimensionPattern> candidatePatterns) {
      Set<OptimalDSetDimensionInfo> uniqueDimensions = populateUniqueDimensions(candidatePatterns);
      Set<OptimalDSetPastUsageDimensionPattern> uniqueSubSetPatterns = populateUniquePastUsagePatterns(candidatePatterns);
      return new OptimalDSetCandidateDimensionPattern(new ArrayList<OptimalDSetDimensionInfo>(uniqueDimensions),
               new ArrayList<OptimalDSetPastUsageDimensionPattern>(uniqueSubSetPatterns));
   }

   private static Set<OptimalDSetDimensionInfo> populateUniqueDimensions (
            List<OptimalDSetCandidateDimensionPattern> candidatePatterns) {
      Set<OptimalDSetDimensionInfo> uniqueDimensions = new HashSet<OptimalDSetDimensionInfo>();
      for (OptimalDSetCandidateDimensionPattern candidatePattern : candidatePatterns) {
         uniqueDimensions.addAll(candidatePattern.getDimensions());
      }
      return uniqueDimensions;
   }

   private static Set<OptimalDSetPastUsageDimensionPattern> populateUniquePastUsagePatterns (
            List<OptimalDSetCandidateDimensionPattern> candidatePatterns) {
      Set<OptimalDSetPastUsageDimensionPattern> uniqueSubSetPatterns = new HashSet<OptimalDSetPastUsageDimensionPattern>();
      for (OptimalDSetCandidateDimensionPattern candidatePattern : candidatePatterns) {
         uniqueSubSetPatterns.addAll(candidatePattern.getSubSets());
      }
      return uniqueSubSetPatterns;
   }

   public static List<OptimalDSetCandidateDimensionPattern> getUnMergedCandidatePatterns (
            List<OptimalDSetCandidateDimensionPattern> previousLevelPatterns,
            Set<OptimalDSetCandidateDimensionPattern> mergeProcessParticipatedPatterns) {
      List<OptimalDSetCandidateDimensionPattern> unMergedCandidatePatterns = new ArrayList<OptimalDSetCandidateDimensionPattern>();
      for (OptimalDSetCandidateDimensionPattern previousLevelPattern : previousLevelPatterns) {
         if (!mergeProcessParticipatedPatterns.contains(previousLevelPattern)) {
            unMergedCandidatePatterns.add(previousLevelPattern);
         }
      }
      return unMergedCandidatePatterns;
   }

   public static List<OptimalDSetPastUsageDimensionPattern> findPatternsOfDesiredLength (
            List<OptimalDSetPastUsageDimensionPattern> allPastUsagePatterns, Integer length) {
      List<OptimalDSetPastUsageDimensionPattern> desiredLengthPatterns = new ArrayList<OptimalDSetPastUsageDimensionPattern>();
      for (OptimalDSetPastUsageDimensionPattern pastUsagePattern : allPastUsagePatterns) {
         if (length.equals(pastUsagePattern.getDimensions().size())) {
            desiredLengthPatterns.add(pastUsagePattern);
         }
      }
      return desiredLengthPatterns;
   }

   public static OptimalDSetCandidateDimensionPattern buildCandidatePattern (
            Collection<OptimalDSetPastUsageDimensionPattern> pastUsagePatterns) {
      Set<OptimalDSetDimensionInfo> uniqueDimensions = new HashSet<OptimalDSetDimensionInfo>();
      for (OptimalDSetPastUsageDimensionPattern pastUsagePattern : pastUsagePatterns) {
         uniqueDimensions.addAll(pastUsagePattern.getDimensions());
      }
      return new OptimalDSetCandidateDimensionPattern(new ArrayList<OptimalDSetDimensionInfo>(uniqueDimensions),
               new ArrayList<OptimalDSetPastUsageDimensionPattern>(pastUsagePatterns));
   }

   public static OptimalDSetDimensionInfo cloneDimensionInfo (OptimalDSetDimensionInfo toBeClonedDimensionInfo) {
      OptimalDSetDimensionInfo clonedDimensionInfo = new OptimalDSetDimensionInfo();
      clonedDimensionInfo.setBedId(toBeClonedDimensionInfo.getBedId());
      clonedDimensionInfo.setName(toBeClonedDimensionInfo.getName());
      clonedDimensionInfo.setNumMembers(toBeClonedDimensionInfo.getNumMembers());
      // cloning range not required as of now
      clonedDimensionInfo.setRange(toBeClonedDimensionInfo.getRange());
      clonedDimensionInfo.setUsagePercentage(toBeClonedDimensionInfo.getUsagePercentage());
      return clonedDimensionInfo;
   }

   public static OptimalDSetPastUsageDimensionPattern clonePastUsagePattern (
            OptimalDSetPastUsageDimensionPattern toBeClonedPastUsagePattern) {
      List<OptimalDSetDimensionInfo> clonedDimensions = new ArrayList<OptimalDSetDimensionInfo>();
      for (OptimalDSetDimensionInfo dimensionInfo : toBeClonedPastUsagePattern.getDimensions()) {
         clonedDimensions.add(cloneDimensionInfo(dimensionInfo));
      }
      OptimalDSetPastUsageDimensionPattern clonedPastUsageDimensionPattern = new OptimalDSetPastUsageDimensionPattern();
      clonedPastUsageDimensionPattern.setDimensions(clonedDimensions);
      clonedPastUsageDimensionPattern.setUsagePercentage(toBeClonedPastUsagePattern.getUsagePercentage());
      return clonedPastUsageDimensionPattern;
   }

   public static OptimalDSetLevelOutputInfo cloneOptimalDSetLevelOutputInfo (
            OptimalDSetLevelOutputInfo toBeClonedLevelOutputInfo) {
      OptimalDSetLevelOutputInfo clonedLevelOutputInfo = new OptimalDSetLevelOutputInfo();
      clonedLevelOutputInfo.setConstrainedLevelPatterns(cloneCandidatePatterns(toBeClonedLevelOutputInfo
               .getConstrainedLevelPatterns()));
      clonedLevelOutputInfo.setLevelNum(toBeClonedLevelOutputInfo.getLevelNum());
      clonedLevelOutputInfo.setLevelPatterns(cloneCandidatePatterns(toBeClonedLevelOutputInfo.getLevelPatterns()));
      clonedLevelOutputInfo.setSpaceConstraintMet(toBeClonedLevelOutputInfo.isSpaceConstraintMet());
      clonedLevelOutputInfo.setTotalSpace(toBeClonedLevelOutputInfo.getTotalSpace());
      clonedLevelOutputInfo.setTotalUsage(toBeClonedLevelOutputInfo.getTotalUsage());
      clonedLevelOutputInfo.setTotalSpaceInPercentageOfParentAsset(toBeClonedLevelOutputInfo
               .getTotalSpaceInPercentageOfParentAsset());
      clonedLevelOutputInfo.setUsageCriteriaMet(toBeClonedLevelOutputInfo.isUsageCriteriaMet());
      return clonedLevelOutputInfo;
   }

   public static List<OptimalDSetCandidateDimensionPattern> cloneCandidatePatterns (
            List<OptimalDSetCandidateDimensionPattern> toBeClonedCandidatePatterns) {
      List<OptimalDSetCandidateDimensionPattern> clonedCandidatePatterns = new ArrayList<OptimalDSetCandidateDimensionPattern>();
      if (ExecueCoreUtil.isCollectionNotEmpty(toBeClonedCandidatePatterns)) {
         for (OptimalDSetCandidateDimensionPattern toBeClonedCandidatePattern : toBeClonedCandidatePatterns) {
            clonedCandidatePatterns.add(cloneCandidatePattern(toBeClonedCandidatePattern));
         }
      }
      return clonedCandidatePatterns;
   }

   public static OptimalDSetCandidateDimensionPattern cloneCandidatePattern (
            OptimalDSetCandidateDimensionPattern toBeClonedCandidatePattern) {
      List<OptimalDSetDimensionInfo> clonedDimensions = new ArrayList<OptimalDSetDimensionInfo>();
      for (OptimalDSetDimensionInfo dimensionInfo : toBeClonedCandidatePattern.getDimensions()) {
         clonedDimensions.add(cloneDimensionInfo(dimensionInfo));
      }
      List<OptimalDSetPastUsageDimensionPattern> clonedSubSets = new ArrayList<OptimalDSetPastUsageDimensionPattern>();
      if (ExecueCoreUtil.isCollectionNotEmpty(toBeClonedCandidatePattern.getSubSets())) {
         for (OptimalDSetPastUsageDimensionPattern pastUsageDimensionPattern : toBeClonedCandidatePattern.getSubSets()) {
            clonedSubSets.add(clonePastUsagePattern(pastUsageDimensionPattern));
         }
      }
      OptimalDSetCandidateDimensionPattern clonedCandidatePattern = new OptimalDSetCandidateDimensionPattern();
      clonedCandidatePattern.setDimensions(clonedDimensions);
      clonedCandidatePattern.setSpace(toBeClonedCandidatePattern.getSpace());
      clonedCandidatePattern.setSubSets(clonedSubSets);
      return clonedCandidatePattern;
   }

   public static Double convertToGigaBytes (Double sizeInCharacters) {
      // 1 character = 1byte
      // to convert to MB divide by 1024
      // to convert to GB divide by 1024 again
      return (sizeInCharacters / (1024 * 1024));
   }

   public static List<OptimalDSetDimensionInfo> getDistinctDimensions (
            List<OptimalDSetPastUsageDimensionPattern> pastUsagePatterns) {
      Set<OptimalDSetDimensionInfo> uniqueDimensions = new HashSet<OptimalDSetDimensionInfo>();
      for (OptimalDSetPastUsageDimensionPattern pastUsagePattern : pastUsagePatterns) {
         List<OptimalDSetDimensionInfo> dimensions = pastUsagePattern.getDimensions();
         for (OptimalDSetDimensionInfo dimensionInfo : dimensions) {
            uniqueDimensions.add(dimensionInfo);
         }
      }
      return new ArrayList<OptimalDSetDimensionInfo>(uniqueDimensions);
   }

   public static List<Long> getDistinctDimensionBedIds (List<OptimalDSetDimensionInfo> distinctDimensions) {
      Set<Long> uniqueDimensionBedIds = new HashSet<Long>();
      for (OptimalDSetDimensionInfo distinctDimension : distinctDimensions) {
         uniqueDimensionBedIds.add(distinctDimension.getBedId());
      }
      return new ArrayList<Long>(uniqueDimensionBedIds);
   }

   public static Double calculateLengthOfColumnForParentAsset (Colum colum) {
      Double length = 0d;
      length += colum.getPrecision();
      Integer scale = colum.getScale();
      if (scale > 0) {
         length += (scale + 1);
      }
      return length;
   }

   public static Double calculateLengthOfColumnForCube (Colum colum, Integer minDimensionLength) {
      Double originalColumnLength = calculateLengthOfColumnForParentAsset(colum);
      if (originalColumnLength < minDimensionLength) {
         originalColumnLength = minDimensionLength.doubleValue();
      }
      return originalColumnLength;
   }

   public static OptimalDSetDimensionInfo getMatchingDimension (List<OptimalDSetDimensionInfo> dimensions, Long bedId) {
      OptimalDSetDimensionInfo matchedDimension = null;
      for (OptimalDSetDimensionInfo dimensionInfo : dimensions) {
         if (dimensionInfo.getBedId().equals(bedId)) {
            matchedDimension = dimensionInfo;
            break;
         }
      }
      return matchedDimension;
   }

   public static Double calculateSpace (List<OptimalDSetCandidateDimensionPattern> candidatePatterns) {
      Double totalSpace = 0d;
      for (OptimalDSetCandidateDimensionPattern candidatePattern : candidatePatterns) {
         totalSpace += candidatePattern.getSpace();
      }
      return totalSpace;
   }

   public static Double calculateUsage (List<OptimalDSetCandidateDimensionPattern> candidatePatterns) {
      Double totalUsage = 0d;
      Set<OptimalDSetPastUsageDimensionPattern> uniquePastUsagePatterns = populateUniquePastUsagePatterns(candidatePatterns);
      for (OptimalDSetPastUsageDimensionPattern pastUsagePattern : uniquePastUsagePatterns) {
         totalUsage += pastUsagePattern.getUsagePercentage();
      }
      return totalUsage;
   }

   public static void displayCandidatePatterns (List<OptimalDSetCandidateDimensionPattern> candidatePatterns) {
      StringBuilder stringBuilder = new StringBuilder();
      int index = 1;
      for (OptimalDSetCandidateDimensionPattern candidateDimensionPattern : candidatePatterns) {
         stringBuilder.append("Pattern " + index++ + ": \n" + candidateDimensionPattern.getDisplayString() + "\n");
      }
      if (logger.isDebugEnabled()) {
         logger.debug(stringBuilder.toString());
      }
   }

   /**
    * @param staticLevelInputInfo
    */
   public static void displayStaticLevelInputInfo (OptimalDSetStaticLevelInputInfo staticLevelInputInfo) {
      if (logger.isDebugEnabled()) {
         StringBuilder sb = new StringBuilder();
         sb.append("\n\nInput Information ..\n\n");
         sb.append("Apply Constraints set to       : " + staticLevelInputInfo.isApplyConstraints() + "\n");
         sb.append("Deduce Space at Runtime set to : " + staticLevelInputInfo.isDeduceSpaceAtRuntime() + "\n");
         sb.append("Parent Asset Space             : " + AnswersCatalogUtil.getRoundedValue(staticLevelInputInfo.getParentAssetSpace(),2) + "\n");
         sb.append("Number of Past Usage Patterns  : " + staticLevelInputInfo.getAllPastUsagePatterns().size() + "\n");
         List<String> pastUsagePatternsAsString = new ArrayList<String>();
         Double totalInputUsagePecentage = 0d;
         for (OptimalDSetPastUsageDimensionPattern pastUsagePattern : staticLevelInputInfo.getAllPastUsagePatterns()) {
            pastUsagePatternsAsString.add(pastUsagePattern.getDisplayString());
            totalInputUsagePecentage += pastUsagePattern.getUsagePercentage();
         }
         sb.append("Total Past Usage Percentage    : " + AnswersCatalogUtil.getRoundedValue(totalInputUsagePecentage,2) + "\n");
         sb.append("\nPast Usage Pattern Info., \n" + ExecueCoreUtil.joinCollection(pastUsagePatternsAsString, "\n"));
         sb.append("\n\n..  end of Input Information\n");
         logger.debug(sb.toString());
      }
   }

   public static void displayLevelOutputInfoList (List<OptimalDSetLevelOutputInfo> levelOutputInfoList) {
      if (ExecueCoreUtil.isCollectionNotEmpty(levelOutputInfoList)) {
         StringBuilder toStringRep = new StringBuilder();
         for (OptimalDSetLevelOutputInfo levelOutputInfo : levelOutputInfoList) {
            toStringRep.append(levelOutputInfo.getDisplayString() + "\n\n");
         }
         if (logger.isDebugEnabled()) {
            logger.debug(toStringRep.toString());
         }
      }
   }

   public static void displayCubeOutputInfo (OptimalDSetCubeOutputInfo cubeOutputInfo) {
      StringBuilder toStringRep = new StringBuilder();
      toStringRep.append("Final OptimalDset Cube Output  : \n" + cubeOutputInfo.getDisplayString());
      if (logger.isDebugEnabled()) {
         logger.debug(toStringRep.toString());
      }
   }

}
