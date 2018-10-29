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


package com.execue.core.common.bean.qdata;

import java.util.Set;

/**
 * This bean represents the DimensionCombination in the system. It contains unique dimension combination name asked in
 * the group by section of query and their percentage.
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class QDataDimensionCombinationInfo {

   private Set<String> combination;
   private double      usagePercentage;

   public Set<String> getCombination () {
      return combination;
   }

   public void setCombination (Set<String> combination) {
      this.combination = combination;
   }

   public double getUsagePercentage () {
      return usagePercentage;
   }

   public void setUsagePercentage (double usagePercentage) {
      this.usagePercentage = usagePercentage;
   }

}
