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


/**
 *
 */
package com.execue.core.common.bean.qdata;

import java.io.Serializable;

import com.execue.core.common.type.RFXVariationSubType;

/**
 * @author Nihar
 */
public abstract class RIUniversalSearch implements Serializable {

   private static final long   serialVersionUID = 1L;
   private Long                id;
   private Long                applicationId;
   private Long                beId1            = 0L;
   private Long                beId2            = 0L;
   private Long                beId3            = 0L;

   private String              beType;
   private Integer             searchType;
   private Integer             variationType;

   private RFXVariationSubType variationSubType;
   private RFXVariationSubType originalSubType;
   private Integer             derived;
   private Double              variationWeight;
   private Double              maxWeight;
   private Double              recWeight;
   private Double              entityCount;
   private String              tripleString;

   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder(1);
      sb.append(getContextId()).append(" ").append(beId1).append(" ").append(beId2).append(" ").append(beId3).append(
               " ").append(variationType).append(" ").append(variationSubType).append(" ").append(originalSubType)
               .append(" ").append(derived).append(" ").append(variationWeight).append(" ").append(maxWeight).append(
                        " ").append(recWeight).append(" ").append(entityCount);
      return sb.toString();
   }

   protected abstract Long getContextId ();

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof RIUniversalSearch || obj instanceof String)
               && this.toString().equalsIgnoreCase(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   public String getTripleString () {
      return tripleString;
   }

   public void setTripleString (String tripleString) {
      this.tripleString = tripleString;
   }

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the beId1
    */
   public Long getBeId1 () {
      return beId1;
   }

   /**
    * @param beId1
    *           the beId1 to set
    */
   public void setBeId1 (Long beId1) {
      this.beId1 = beId1;
   }

   /**
    * @return the beId2
    */
   public Long getBeId2 () {
      return beId2;
   }

   /**
    * @param beId2
    *           the beId2 to set
    */
   public void setBeId2 (Long beId2) {
      this.beId2 = beId2;
   }

   /**
    * @return the beId3
    */
   public Long getBeId3 () {
      return beId3;
   }

   /**
    * @param beId3
    *           the beId3 to set
    */
   public void setBeId3 (Long beId3) {
      this.beId3 = beId3;
   }

   /**
    * @return the beType
    */
   public String getBeType () {
      return beType;
   }

   /**
    * @param beType
    *           the beType to set
    */
   public void setBeType (String beType) {
      this.beType = beType;
   }

   /**
    * @return the searchType
    */
   public Integer getSearchType () {
      return searchType;
   }

   /**
    * @param searchType
    *           the searchType to set
    */
   public void setSearchType (Integer searchType) {
      this.searchType = searchType;
   }

   /**
    * @return the variationType
    */
   public Integer getVariationType () {
      return variationType;
   }

   /**
    * @param variationType
    *           the variationType to set
    */
   public void setVariationType (Integer variationType) {
      this.variationType = variationType;
   }

   /**
    * @return the variationSubType
    */
   public RFXVariationSubType getVariationSubType () {
      return variationSubType;
   }

   /**
    * @param variationSubType
    *           the variationSubType to set
    */
   public void setVariationSubType (RFXVariationSubType variationSubType) {
      this.variationSubType = variationSubType;
   }

   /**
    * @return the variationWeight
    */
   public Double getVariationWeight () {
      return variationWeight;
   }

   /**
    * @param variationWeight
    *           the variationWeight to set
    */
   public void setVariationWeight (Double variationWeight) {
      this.variationWeight = variationWeight;
   }

   /**
    * @return the recWeight
    */
   public Double getRecWeight () {
      return recWeight;
   }

   /**
    * @param recWeight
    *           the recWeight to set
    */
   public void setRecWeight (Double recWeight) {
      this.recWeight = recWeight;
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId
    *           the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   /**
    * @return the originalSubType
    */
   public RFXVariationSubType getOriginalSubType () {
      return originalSubType;
   }

   /**
    * @param originalSubType
    *           the originalSubType to set
    */
   public void setOriginalSubType (RFXVariationSubType originalSubType) {
      this.originalSubType = originalSubType;
   }

   /**
    * @return the derived
    */
   public Integer getDerived () {
      return derived;
   }

   /**
    * @param derived
    *           the derived to set
    */
   public void setDerived (Integer derived) {
      this.derived = derived;
   }

   /**
    * @return the maxWeight
    */
   public Double getMaxWeight () {
      return maxWeight;
   }

   /**
    * @param maxWeight
    *           the maxWeight to set
    */
   public void setMaxWeight (Double maxWeight) {
      this.maxWeight = maxWeight;
   }

   /**
    * @return the entityCount
    */
   public Double getEntityCount () {
      return entityCount;
   }

   /**
    * @param entityCount
    *           the entityCount to set
    */
   public void setEntityCount (Double entityCount) {
      this.entityCount = entityCount;
   }
}