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


package com.execue.ac.algorithm.optimaldset.bean;

import java.util.ArrayList;
import java.util.List;

import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.util.ExecueCoreUtil;

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
public class OptimalDSetCubeOutputInfo {

   private List<OptimalDSetCandidateDimensionPattern> levelPatterns;
   private Double                                     totalSpace;
   private Double                                     totalUsage;
   private Double                                     totalSpaceInPercentageOfParentAsset;

   public String getDisplayString () {
      StringBuilder toStringRep = new StringBuilder();
      toStringRep.append("Patterns., \n");
      List<String> levelPatternsAsString = new ArrayList<String>();
      for (OptimalDSetCandidateDimensionPattern levelCandidatePattern : levelPatterns) {
         levelPatternsAsString.add(levelCandidatePattern.getDisplayString());
      }
      toStringRep.append(ExecueCoreUtil.joinCollection(levelPatternsAsString, "\n") + "\n");
      toStringRep.append("\nNumber of Patterns : " + getTotalNumberOfDSets() + "\n");
      toStringRep.append("Total Usage        : " + AnswersCatalogUtil.getRoundedValue(totalUsage,2) + "\n");
      toStringRep.append("Total Space        : " + AnswersCatalogUtil.getRoundedValue(totalSpace,2) + "\n");
      toStringRep.append("% Space            : " + totalSpaceInPercentageOfParentAsset + "\n");
      return toStringRep.toString();
   }

   public List<OptimalDSetCandidateDimensionPattern> getLevelPatterns () {
      return levelPatterns;
   }

   public void setLevelPatterns (List<OptimalDSetCandidateDimensionPattern> levelPatterns) {
      this.levelPatterns = levelPatterns;
   }

   public Double getTotalSpace () {
      return totalSpace;
   }

   public void setTotalSpace (Double totalSpace) {
      this.totalSpace = totalSpace;
   }

   public Double getTotalUsage () {
      return totalUsage;
   }

   public void setTotalUsage (Double totalUsage) {
      this.totalUsage = totalUsage;
   }

   
   public Double getTotalSpaceInPercentageOfParentAsset () {
      return totalSpaceInPercentageOfParentAsset;
   }

   
   public void setTotalSpaceInPercentageOfParentAsset (Double totalSpaceInPercentageOfParentAsset) {
      this.totalSpaceInPercentageOfParentAsset = totalSpaceInPercentageOfParentAsset;
   }
   
   public int getTotalNumberOfDSets () {
      if (ExecueCoreUtil.isCollectionNotEmpty(getLevelPatterns())) {
         return getLevelPatterns().size();
      }
      return 0;
   }
}
