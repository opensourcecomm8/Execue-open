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


package com.execue.core.common.bean.entity.unstructured;

import java.io.Serializable;

/**
 * To contain the range information on a feature that is eligible for a Range Definition
 * defaultValue --> should always be either less than minimumValue or more than maximumValue
 *  
 * @author Raju Gottumukkala
 *
 */
public class FeatureRangeInfo implements Serializable {

   private Long   id;
   private Long   featureId;
   private Double defaultValue;
   private Double minimumValue;
   private Double maximumValue;
   private Double minimumImpactValue;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getFeatureId () {
      return featureId;
   }

   public void setFeatureId (Long featureId) {
      this.featureId = featureId;
   }

   public Double getDefaultValue () {
      return defaultValue;
   }

   public void setDefaultValue (Double defaultValue) {
      this.defaultValue = defaultValue;
   }

   public Double getMinimumValue () {
      return minimumValue;
   }

   public void setMinimumValue (Double minimumValue) {
      this.minimumValue = minimumValue;
   }

   public Double getMaximumValue () {
      return maximumValue;
   }

   public void setMaximumValue (Double maximumValue) {
      this.maximumValue = maximumValue;
   }

   public Double getMinimumImpactValue () {
      return minimumImpactValue;
   }

   public void setMinimumImpactValue (Double minimumImpactValue) {
      this.minimumImpactValue = minimumImpactValue;
   }

}
