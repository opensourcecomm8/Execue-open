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

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
public abstract class OptimalDSetBusinessEntityInfo {

   public OptimalDSetBusinessEntityInfo () {
      super();
   }

   @Override
   public String toString () {
      return name;
   }

   private Long   bedId;
   private String name;
   private Double usagePercentage;

   public OptimalDSetBusinessEntityInfo (Long bedId, String name) {
      super();
      this.bedId = bedId;
      this.name = name;
   }

   public Long getBedId () {
      return bedId;
   }

   public OptimalDSetBusinessEntityInfo (Long bedId, String name, Double usagePercentage) {
      super();
      this.bedId = bedId;
      this.name = name;
      this.usagePercentage = usagePercentage;
   }

   public void setBedId (Long bedId) {
      this.bedId = bedId;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public Double getUsagePercentage () {
      return usagePercentage;
   }

   public void setUsagePercentage (Double usagePercentage) {
      this.usagePercentage = usagePercentage;
   }
}
