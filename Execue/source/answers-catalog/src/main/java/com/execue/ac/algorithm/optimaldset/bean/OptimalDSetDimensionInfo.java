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

import com.execue.core.common.bean.entity.Range;

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
public class OptimalDSetDimensionInfo extends OptimalDSetBusinessEntityInfo {

   private Integer numMembers;
   private Range   range;

   public OptimalDSetDimensionInfo () {
      super();
   }

   @Override
   public String toString () {
      return super.toString();
   }

   @Override
   public boolean equals (Object obj) {
      return ((OptimalDSetDimensionInfo) obj).toString().equalsIgnoreCase(toString());
   }

   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   public OptimalDSetDimensionInfo (Long bedId, String name, Integer numMembers) {
      super(bedId, name);
      this.numMembers = numMembers;
   }

   public OptimalDSetDimensionInfo (Long bedId, String name, Range range) {
      super(bedId, name);
      this.range = range;
   }

   public OptimalDSetDimensionInfo (Long bedId, String name, Double usagePercentage, Integer numMembers) {
      super(bedId, name, usagePercentage);
      this.numMembers = numMembers;
   }

   public OptimalDSetDimensionInfo (Long bedId, String name, Double usagePercentage, Range range) {
      super(bedId, name, usagePercentage);
      this.range = range;
   }

   public OptimalDSetDimensionInfo (Long bedId, String name, Double usagePercentage) {
      super(bedId, name, usagePercentage);
   }

   public Integer getNumMembers () {
      if (range != null) {
         numMembers = range.getRangeDetails().size();
      }
      return numMembers;
   }

   public void setNumMembers (Integer numMembers) {
      this.numMembers = numMembers;
   }

   public Range getRange () {
      return range;
   }

   public void setRange (Range range) {
      this.range = range;
   }
}
