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


package com.execue.nlp.bean.entity;

import java.util.HashSet;
import java.util.Set;

import com.execue.core.common.type.BehaviorType;

/**
 * @author Kaliki
 */
public class OntoEntity extends RecognitionEntity implements Cloneable {

   private static final long   serialVersionUID = 1L;
   protected Long              modelGroupId;
   protected Long              modelId;
   protected RecognitionEntity recEntity;
   protected long              popularity;
   protected Set<BehaviorType> behaviors;
   protected Set<Long>         behaviorBedIds;
   protected Long              appCloudId;
   protected Long              knowledgeId;
   private String              detailTypeName;
   private Long                detailTypeBedId;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.RecognitionEntity#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      OntoEntity ontoEntity = (OntoEntity) super.clone();
      ontoEntity.setModelGroupId(this.getModelGroupId());
      ontoEntity.setModelId(this.getModelId());
      ontoEntity.setPopularity(this.getPopularity());
      ontoEntity.setRecEntity(this.getRecEntity());
      ontoEntity.setBehaviors(this.getBehaviors());
      ontoEntity.setKnowledgeId(this.getKnowledgeId());
      ontoEntity.setAppCloudId(this.getAppCloudId());
      ontoEntity.setDetailTypeName(this.getDetailTypeName());
      ontoEntity.setDetailTypeBedId(this.getDetailTypeBedId());
      return ontoEntity;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.RecognitionEntity#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return obj instanceof OntoEntity && this.toString().equalsIgnoreCase(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.RecognitionEntity#toString()
    */
   @Override
   public String toString () {
      String superString = super.toString();
      if (word != null) {
         return superString + " " + word + " " + (modelGroupId != null ? modelGroupId : "");
      } else {
         return superString;
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.RecognitionEntity#hashCode()
    */
   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   /**
    * Returns the Ontological Name for this Entity. This is same as Word value of that Entity
    * 
    * @return String representing Ontological Name
    */
   public String getOntoName () {
      return word;
   }

   /**
    * Sets the Ontological name i.e. Word value of this entity
    * 
    * @param ontoName
    *           Ontological name
    */
   public void setOntoName (String ontoName) {
      this.word = ontoName;
   }

   public Long getModelGroupId () {
      return modelGroupId;
   }

   public void setModelGroupId (Long modelGroupId) {
      this.modelGroupId = modelGroupId;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   /**
    * @return the recEntity
    */
   public RecognitionEntity getRecEntity () {
      return recEntity;
   }

   /**
    * @param recEntity
    *           the recEntity to set
    */
   public void setRecEntity (RecognitionEntity recEntity) {
      this.recEntity = recEntity;
   }

   /**
    * @return the popularity
    */
   public long getPopularity () {
      return popularity;
   }

   /**
    * @param popularity
    *           the popularity to set
    */
   public void setPopularity (long popularity) {
      this.popularity = popularity;
   }

   /**
    * @return the knowledgeId
    */
   public Long getKnowledgeId () {
      return knowledgeId;
   }

   /**
    * @param knowledgeId
    *           the knowledgeId to set
    */
   public void setKnowledgeId (Long knowledgeId) {
      this.knowledgeId = knowledgeId;
   }

   /**
    * Returns the display name of the inheriting entity
    * 
    * @return the displayName
    */
   public String getDisplayName () {
      return "";
   }

   /**
    * Returns the bed id of the inheriting entity
    * 
    * @return the Long bed id
    */
   public Long getId () {
      return null;
   }

   /**
    * @return the isAttributeType
    */
   public Boolean isAttributeType () {
      return getBehaviors().contains(BehaviorType.ATTRIBUTE);
   }

   public Set<BehaviorType> getBehaviors () {
      if (behaviors == null) {
         behaviors = new HashSet<BehaviorType>(1);
      }
      return behaviors;
   }

   public void setBehaviors (Set<BehaviorType> behaviors) {
      if (behaviors == null) {
         behaviors = new HashSet<BehaviorType>(1);
      }
      this.behaviors = behaviors;
   }

   public Set<Long> getBehaviorBedIds () {
      if (behaviorBedIds == null) {
         return new HashSet<Long>(1);
      }
      return behaviorBedIds;
   }

   public void setBehaviorBedIds (Set<Long> behaviorBedIds) {
      this.behaviorBedIds = behaviorBedIds;
   }

   public void addBehaviorBedId (Long behaviorBedId) {
      if (this.behaviorBedIds == null) {
         behaviorBedIds = new HashSet<Long>(1);
      }
      behaviorBedIds.add(behaviorBedId);
   }

   /**
    * Retrieves the id of the app cloud to which this entity is part of
    * 
    * @return
    */
   public Long getAppCloudId () {
      return appCloudId;
   }

   /**
    * Sets the id of the app cloud to which this entity is part of
    * 
    * @param appCloudId
    */
   public void setAppCloudId (Long appCloudId) {
      this.appCloudId = appCloudId;
   }

   /**
    * @return the detailTypeName
    */
   public String getDetailTypeName () {
      return detailTypeName;
   }

   /**
    * @param detailTypeName
    *           the detailTypeName to set
    */
   public void setDetailTypeName (String detailTypeName) {
      this.detailTypeName = detailTypeName;
   }

   /**
    * @return the detailTypeBedId
    */
   public Long getDetailTypeBedId () {
      return detailTypeBedId;
   }

   /**
    * @param detailTypeBedId
    *           the detailTypeBedId to set
    */
   public void setDetailTypeBedId (Long detailTypeBedId) {
      this.detailTypeBedId = detailTypeBedId;
   }

}