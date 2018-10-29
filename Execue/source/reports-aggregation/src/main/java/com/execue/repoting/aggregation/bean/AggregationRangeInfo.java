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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.execue.repoting.aggregation.bean;

import java.util.Set;

/**
 * @author Nitesh
 */
public class AggregationRangeInfo {

   private Long                        rangeId;
   private String                      name;
   private String                      description;
   private Long                        conceptBedId;
   private boolean                     enabled;
   private Set<AggregationRangeDetail> aggregationRangeDetails;
   private Long                        userId;
   private String                      conceptDisplayName;

   /**
    * @return the rangeId
    */
   public Long getRangeId () {
      return rangeId;
   }

   /**
    * @param rangeId the rangeId to set
    */
   public void setRangeId (Long rangeId) {
      this.rangeId = rangeId;
   }

   /**
    * @return the name
    */
   public String getName () {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName (String name) {
      this.name = name;
   }

   /**
    * @return the description
    */
   public String getDescription () {
      return description;
   }

   /**
    * @param description the description to set
    */
   public void setDescription (String description) {
      this.description = description;
   }

   /**
    * @return the conceptBedId
    */
   public Long getConceptBedId () {
      return conceptBedId;
   }

   /**
    * @param conceptBedId the conceptBedId to set
    */
   public void setConceptBedId (Long conceptBedId) {
      this.conceptBedId = conceptBedId;
   }

   /**
    * @return the enabled
    */
   public boolean isEnabled () {
      return enabled;
   }

   /**
    * @param enabled the enabled to set
    */
   public void setEnabled (boolean enabled) {
      this.enabled = enabled;
   }

   /**
    * @return the aggregationRangeDetails
    */
   public Set<AggregationRangeDetail> getAggregationRangeDetails () {
      return aggregationRangeDetails;
   }

   /**
    * @param aggregationRangeDetails the aggregationRangeDetails to set
    */
   public void setAggregationRangeDetails (Set<AggregationRangeDetail> aggregationRangeDetails) {
      this.aggregationRangeDetails = aggregationRangeDetails;
   }

   /**
    * @return the userId
    */
   public Long getUserId () {
      return userId;
   }

   /**
    * @param userId the userId to set
    */
   public void setUserId (Long userId) {
      this.userId = userId;
   }

   /**
    * @return the conceptDisplayName
    */
   public String getConceptDisplayName () {
      return conceptDisplayName;
   }

   /**
    * @param conceptDisplayName the conceptDisplayName to set
    */
   public void setConceptDisplayName (String conceptDisplayName) {
      this.conceptDisplayName = conceptDisplayName;
   }

}