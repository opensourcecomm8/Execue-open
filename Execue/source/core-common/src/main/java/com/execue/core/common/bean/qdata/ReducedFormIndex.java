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

import java.io.Serializable;

import com.execue.core.common.type.RFXEntityType;
import com.execue.core.common.type.RFXVariationSubType;

public abstract class ReducedFormIndex implements Serializable {

   private static final long   serialVersionUID  = 1L;
   private Long                id;
   private Long                applicationId;
   private Long                rfId;
   private RFXEntityType       rfxEntityType;
   private RFXVariationSubType rfxVariationSubType;
   private long                order;
   private Long                srcInstanceBEId;
   private Long                srcConceptBEId;
   private Long                destInstanceBEId;
   private Long                destConceptBEId;
   private Long                relationBEId;
   private String              value;
   private transient double    srcRecWeight;
   private transient double    destRecWeight;
   private transient double    relRecWeight;
   private transient double    maxWeight;
   private transient boolean   mutuallyDependant = false;

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder(1);
      if (srcInstanceBEId != null) {
         sb.append(srcInstanceBEId);
      }
      if (srcConceptBEId != null) {
         sb.append(srcConceptBEId);
      }
      if (relationBEId != null) {
         sb.append(relationBEId);
      }
      if (destInstanceBEId != null) {
         sb.append(destInstanceBEId);
      }
      if (destConceptBEId != null) {
         sb.append(destConceptBEId);
      }
      if (value != null) {
         sb.append(value);
      }
      sb.append(rfxVariationSubType.getValue());
      return sb.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof ReducedFormIndex || obj instanceof String)
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

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public long getOrder () {
      return order;
   }

   public void setOrder (long order) {
      this.order = order;
   }

   public RFXEntityType getRfxEntityType () {
      return rfxEntityType;
   }

   public void setRfxEntityType (RFXEntityType rfxEntityType) {
      this.rfxEntityType = rfxEntityType;
   }

   public Long getRfId () {
      return rfId;
   }

   public void setRfId (Long rfId) {
      this.rfId = rfId;
   }

   /**
    * @return the srcInstanceBEId
    */
   public Long getSrcInstanceBEId () {
      return srcInstanceBEId;
   }

   /**
    * @param srcInstanceBEId
    *           the srcInstanceBEId to set
    */
   public void setSrcInstanceBEId (Long srcInstanceBEId) {
      this.srcInstanceBEId = srcInstanceBEId;
   }

   /**
    * @return the srcConceptBEId
    */
   public Long getSrcConceptBEId () {
      return srcConceptBEId;
   }

   /**
    * @param srcConceptBEId
    *           the srcConceptBEId to set
    */
   public void setSrcConceptBEId (Long srcConceptBEId) {
      this.srcConceptBEId = srcConceptBEId;
   }

   /**
    * @return the destInstanceBEId
    */
   public Long getDestInstanceBEId () {
      return destInstanceBEId;
   }

   /**
    * @param destInstanceBEId
    *           the destInstanceBEId to set
    */
   public void setDestInstanceBEId (Long destInstanceBEId) {
      this.destInstanceBEId = destInstanceBEId;
   }

   /**
    * @return the destConceptBEId
    */
   public Long getDestConceptBEId () {
      return destConceptBEId;
   }

   /**
    * @param destConceptBEId
    *           the destConceptBEId to set
    */
   public void setDestConceptBEId (Long destConceptBEId) {
      this.destConceptBEId = destConceptBEId;
   }

   /**
    * @return the relationBEId
    */
   public Long getRelationBEId () {
      return relationBEId;
   }

   /**
    * @param relationBEId
    *           the relationBEId to set
    */
   public void setRelationBEId (Long relationBEId) {
      this.relationBEId = relationBEId;
   }

   /**
    * @return the value
    */
   public String getValue () {
      return value;
   }

   /**
    * @param value
    *           the value to set
    */
   public void setValue (String value) {
      this.value = value;
   }

   /**
    * @return the rfxVariationSubType
    */
   public RFXVariationSubType getRfxVariationSubType () {
      return rfxVariationSubType;
   }

   /**
    * @param rfxVariationSubType
    *           the rfxVariationSubType to set
    */
   public void setRfxVariationSubType (RFXVariationSubType rfxVariationSubType) {
      this.rfxVariationSubType = rfxVariationSubType;
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
    * @return the srcRecWeight
    */
   public double getSrcRecWeight () {
      return srcRecWeight;
   }

   /**
    * @param srcRecWeight
    *           the srcRecWeight to set
    */
   public void setSrcRecWeight (double srcRecWeight) {
      this.srcRecWeight = srcRecWeight;
   }

   /**
    * @return the destRecWeight
    */
   public double getDestRecWeight () {
      return destRecWeight;
   }

   /**
    * @param destRecWeight
    *           the destRecWeight to set
    */
   public void setDestRecWeight (double destRecWeight) {
      this.destRecWeight = destRecWeight;
   }

   /**
    * @return the relRecWeight
    */
   public double getRelRecWeight () {
      return relRecWeight;
   }

   /**
    * @param relRecWeight
    *           the relRecWeight to set
    */
   public void setRelRecWeight (double relRecWeight) {
      this.relRecWeight = relRecWeight;
   }

   /**
    * @return the maxWeight
    */
   public double getMaxWeight () {
      return maxWeight;
   }

   /**
    * @param maxWeight
    *           the maxWeight to set
    */
   public void setMaxWeight (double maxWeight) {
      this.maxWeight = maxWeight;
   }

   /**
    * @return the mutuallyDependant
    */
   public boolean isMutuallyDependant () {
      return mutuallyDependant;
   }

   /**
    * @param mutuallyDependant the mutuallyDependant to set
    */
   public void setMutuallyDependant (boolean mutuallyDependant) {
      this.mutuallyDependant = mutuallyDependant;
   }
}