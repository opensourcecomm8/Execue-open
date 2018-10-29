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

import java.util.List;

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
public class OptimalDSetPastUsageDimensionPattern extends OptimalDSetDimensionPattern {

   private Double usagePercentage;

   public OptimalDSetPastUsageDimensionPattern () {
      super();
   }

   public OptimalDSetPastUsageDimensionPattern (List<OptimalDSetDimensionInfo> dimensions, Double usagePercentage) {
      super(dimensions);
      this.usagePercentage = usagePercentage;
   }

   @Override
   public String toString () {
      StringBuilder toStringRepresenation = new StringBuilder();
      toStringRepresenation.append(super.toString());
      if (usagePercentage != null) {
         toStringRepresenation.append(usagePercentage);
      }
      return toStringRepresenation.toString();
   }

   @Override
   public boolean equals (Object obj) {
      return ((OptimalDSetPastUsageDimensionPattern) obj).toString().equalsIgnoreCase(toString());
   }

   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   public String getDisplayString () {
      StringBuilder toStringRepresenation = new StringBuilder();
      toStringRepresenation.append(super.toString());
      if (usagePercentage != null) {
         toStringRepresenation.append("\n\t\t\tUsage Percentage : " + usagePercentage);
      }
      return toStringRepresenation.toString();
   }

   public Double getUsagePercentage () {
      return usagePercentage;
   }

   public void setUsagePercentage (Double usagePercentage) {
      this.usagePercentage = usagePercentage;
   }
}
