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
import java.util.Collections;
import java.util.List;

import com.execue.core.util.ExecueCoreUtil;

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
public class OptimalDSetCandidateDimensionPattern extends OptimalDSetDimensionPattern {

   private List<OptimalDSetPastUsageDimensionPattern> subSets;
   private Double                                     space;

   public OptimalDSetCandidateDimensionPattern () {
      super();
   }

   public OptimalDSetCandidateDimensionPattern (List<OptimalDSetDimensionInfo> dimensions,
            List<OptimalDSetPastUsageDimensionPattern> subSets) {
      super(dimensions);
      this.subSets = subSets;
   }

   public OptimalDSetCandidateDimensionPattern (List<OptimalDSetDimensionInfo> dimensions,
            List<OptimalDSetPastUsageDimensionPattern> subSets, Double space) {
      super(dimensions);
      this.subSets = subSets;
      this.space = space;
   }

   @Override
   public String toString () {
      StringBuilder toStringRepresenation = new StringBuilder();
      toStringRepresenation.append(super.toString());
      if (space != null) {
         toStringRepresenation.append(space);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(subSets)) {
         StringBuilder subSetStringRepresenation = new StringBuilder();
         List<String> subSetStrings = new ArrayList<String>();
         for (OptimalDSetPastUsageDimensionPattern pastUsagePattern : subSets) {
            subSetStrings.add(pastUsagePattern.toString());
         }
         Collections.sort(subSetStrings);
         subSetStringRepresenation.append(ExecueCoreUtil.joinCollection(subSetStrings));
         toStringRepresenation.append(subSetStringRepresenation.toString());
      }
      return toStringRepresenation.toString();
   }

   @Override
   public boolean equals (Object obj) {
      return ((OptimalDSetCandidateDimensionPattern) obj).toString().equalsIgnoreCase(toString());
   }

   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   public String getDisplayString () {
      StringBuilder toStringRepresenation = new StringBuilder();
      toStringRepresenation.append(super.toString());
      if (space != null) {
         toStringRepresenation.append("\n\tSpace : " + space);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(subSets)) {
         StringBuilder subSetStringRepresenation = new StringBuilder();
         List<String> subSetStrings = new ArrayList<String>();
         for (OptimalDSetPastUsageDimensionPattern pastUsagePattern : subSets) {
            subSetStrings.add(pastUsagePattern.getDisplayString());
         }
         Collections.sort(subSetStrings);
         subSetStringRepresenation.append(ExecueCoreUtil.joinCollection(subSetStrings, "\n\t\t"));
         toStringRepresenation.append("\n\tSubSet Representation : \n\t\t" + subSetStringRepresenation.toString());
      }
      return toStringRepresenation.toString();
   }

   public Double getSpace () {
      return space;
   }

   public void setSpace (Double space) {
      this.space = space;
   }

   public List<OptimalDSetPastUsageDimensionPattern> getSubSets () {
      return subSets;
   }

   public void setSubSets (List<OptimalDSetPastUsageDimensionPattern> subSets) {
      this.subSets = subSets;
   }
}