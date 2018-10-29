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


package com.execue.handler.bean;

import com.execue.core.common.type.CheckType;

public class UIPathDefinition {

   private Long      pathDefinitionId;
   private Long      entityTripleDefinitionId;
   private Long      sourceConceptBedId;
   private String    sourceConceptName;
   private Long      destinationConceptBedId;
   private String    destinationConceptName;
   private String    relationName;
   private Long      attributeBedId;
   // type name
   private String    attributeName;
   private Long      relationBedId;
   private CheckType source;
   private boolean   reverseAssociation;

   public String getRelationName () {
      return relationName;
   }

   public void setRelationName (String relationName) {
      this.relationName = relationName;
   }

   public Long getAttributeBedId () {
      return attributeBedId;
   }

   public void setAttributeBedId (Long attributeBedId) {
      this.attributeBedId = attributeBedId;
   }

   public Long getSourceConceptBedId () {
      return sourceConceptBedId;
   }

   public void setSourceConceptBedId (Long sourceConceptBedId) {
      this.sourceConceptBedId = sourceConceptBedId;
   }

   public Long getDestinationConceptBedId () {
      return destinationConceptBedId;
   }

   public void setDestinationConceptBedId (Long destinationConceptBedId) {
      this.destinationConceptBedId = destinationConceptBedId;
   }

   public String getSourceConceptName () {
      return sourceConceptName;
   }

   public void setSourceConceptName (String sourceConceptName) {
      this.sourceConceptName = sourceConceptName;
   }

   public String getDestinationConceptName () {
      return destinationConceptName;
   }

   public void setDestinationConceptName (String destinationConceptName) {
      this.destinationConceptName = destinationConceptName;
   }

   public Long getPathDefinitionId () {
      return pathDefinitionId;
   }

   public void setPathDefinitionId (Long pathDefinitionId) {
      this.pathDefinitionId = pathDefinitionId;
   }

   public String getAttributeName () {
      return attributeName;
   }

   public void setAttributeName (String attributeName) {
      this.attributeName = attributeName;
   }

   public Long getRelationBedId () {
      return relationBedId;
   }

   public void setRelationBedId (Long relationBedId) {
      this.relationBedId = relationBedId;
   }

   /**
    * @return the entityTripleDefinitionId
    */
   public Long getEntityTripleDefinitionId () {
      return entityTripleDefinitionId;
   }

   /**
    * @param entityTripleDefinitionId
    *           the entityTripleDefinitionId to set
    */
   public void setEntityTripleDefinitionId (Long entityTripleDefinitionId) {
      this.entityTripleDefinitionId = entityTripleDefinitionId;
   }

   /**
    * @return the source
    */
   public CheckType getSource () {
      return source;
   }

   /**
    * @param source
    *           the source to set
    */
   public void setSource (CheckType source) {
      this.source = source;
   }

   public boolean isReverseAssociation () {
      return reverseAssociation;
   }

   public void setReverseAssociation (boolean reverseAssociation) {
      this.reverseAssociation = reverseAssociation;
   }

}
