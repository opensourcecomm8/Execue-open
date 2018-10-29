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


package com.execue.core.common.bean.entity;

import java.io.Serializable;
import java.util.Set;

import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.RecognitionType;

/**
 * This class represents the BusinessEntity object.
 * 
 * @author Vishay
 * @version 1.0
 * @since 12/01/09
 */
public class BusinessEntityDefinition implements Serializable {

   private static final long         serialVersionUID = 1L;

   private Long                      id;
   private ModelGroup                modelGroup;
   private Concept                   concept;
   private Type                      type;
   private Relation                  relation;
   private Instance                  instance;
   private ConceptProfile            conceptProfile;
   private InstanceProfile           instanceProfile;
   private Behavior                  behavior;
   private Long                      knowledgeId;
   private Long                      popularity       = 0L;
   private BusinessEntityType        entityType;
   private CheckType                 fromShared       = CheckType.NO;
   private Set<Mapping>              mappings;
   private Set<Cloud>                clouds;

   private transient RecognitionType recognitionType;

   public Set<Cloud> getClouds () {
      return clouds;
   }

   public void setClouds (Set<Cloud> clouds) {
      this.clouds = clouds;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Concept getConcept () {
      return concept;
   }

   public void setConcept (Concept concept) {
      this.concept = concept;
   }

   public Relation getRelation () {
      return relation;
   }

   public void setRelation (Relation relation) {
      this.relation = relation;
   }

   public Instance getInstance () {
      return instance;
   }

   public void setInstance (Instance instance) {
      this.instance = instance;
   }

   public ConceptProfile getConceptProfile () {
      return conceptProfile;
   }

   public void setConceptProfile (ConceptProfile conceptProfile) {
      this.conceptProfile = conceptProfile;
   }

   public InstanceProfile getInstanceProfile () {
      return instanceProfile;
   }

   public void setInstanceProfile (InstanceProfile instanceProfile) {
      this.instanceProfile = instanceProfile;
   }

   public Set<Mapping> getMappings () {
      return mappings;
   }

   public void setMappings (Set<Mapping> mappings) {
      this.mappings = mappings;
   }

   public Long getPopularity () {
      return popularity;
   }

   public void setPopularity (Long popularity) {
      this.popularity = popularity;
   }

   public BusinessEntityType getEntityType () {
      return entityType;
   }

   public void setEntityType (BusinessEntityType entityType) {
      this.entityType = entityType;
   }

   public RecognitionType getRecognitionType () {
      return recognitionType;
   }

   public void setRecognitionType (RecognitionType recognitionType) {
      this.recognitionType = recognitionType;
   }

   @Override
   public boolean equals (Object obj) {
      if (obj instanceof BusinessEntityDefinition) {
         return this.getId().equals(((BusinessEntityDefinition) obj).getId());
      }
      return false;
   }

   public ModelGroup getModelGroup () {
      return modelGroup;
   }

   public void setModelGroup (ModelGroup modelGroup) {
      this.modelGroup = modelGroup;
   }

   /**
    * @return
    */
   public Type getType () {
      return type;
   }

   /**
    * @param type
    */
   public void setType (Type type) {
      this.type = type;
   }

   public Behavior getBehavior () {
      return behavior;
   }

   public void setBehavior (Behavior behavior) {
      this.behavior = behavior;
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
    * @return the fromShared
    */
   public CheckType getFromShared () {
      return fromShared;
   }

   /**
    * @param fromShared the fromShared to set
    */
   public void setFromShared (CheckType fromShared) {
      this.fromShared = fromShared;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode () {
      return id.hashCode();
   }

}
