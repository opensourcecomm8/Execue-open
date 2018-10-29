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
import java.util.Date;

import com.execue.core.common.type.RFXVariationSubType;

/**
 * @author Nihar
 */
public class RIUserQuery implements Serializable {

   private static final long   serialVersionUID = 1L;
   private Long                id;
   private Long                queryId;
   private Long                beId1            = 0L;
   private Long                beId2            = 0L;
   private Long                beId3            = 0L;
   private Integer             variationType;
   private RFXVariationSubType variationSubType;
   private RFXVariationSubType originalSubType;
   private Integer             derived;
   private Double              variationWeight;
   private Double              recWeight;
   private Double              entityCount;
   private Long                applicationId;
   private Date                executionDate;
   private Integer             tripleIdentifier;

   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder(1);
      sb.append(queryId).append(" ").append(beId1).append(" ").append(beId2).append(" ").append(beId3).append(" ")
               .append(variationType).append(" ").append(variationSubType).append(" ").append(originalSubType).append(
                        " ").append(derived).append(" ").append(variationWeight).append(" ").append(recWeight).append(
                        " ");
      return sb.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof RIUserQuery || obj instanceof String) && this.toString().equalsIgnoreCase(obj.toString());
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
    * @return the queryId
    */
   public Long getQueryId () {
      return queryId;
   }

   /**
    * @param queryId
    *           the queryId to set
    */
   public void setQueryId (Long queryId) {
      this.queryId = queryId;
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

   public RFXVariationSubType getVariationSubType () {
      return variationSubType;
   }

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

   public Date getExecutionDate () {
      return executionDate;
   }

   public void setExecutionDate (Date executionDate) {
      this.executionDate = executionDate;
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

   /**
    * @return the tripleIdentifier
    */
   public Integer getTripleIdentifier () {
      return tripleIdentifier;
   }

   /**
    * @param tripleIdentifier
    *           the tripleIdentifier to set
    */
   public void setTripleIdentifier (Integer tripleIdentifier) {
      this.tripleIdentifier = tripleIdentifier;
   }
}