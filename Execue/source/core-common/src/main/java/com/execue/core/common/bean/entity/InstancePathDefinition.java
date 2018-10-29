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

/**
 * Entity Object for Instance Path Definition.<br>
 * Defines the path definition between 2 instances<br>
 * 
 * @author Raju Gottumukkala
 * @since 4.0
 */
public class InstancePathDefinition implements Serializable {

   private Long                   id;
   private Long                   sourceDEDId;     // Points to Instance only
   private Long                   destinationDEDId; // Points to Instance only
   private Long                   pathDefinitionId; // Points to Path Definition of Entity Triple Definition of
                                                      // Concepts only

   private BusinessEntityDefinition sourceDED;
   private BusinessEntityDefinition destinationDED;
   private PathDefinition         pathDefinition;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getSourceDEDId () {
      return sourceDEDId;
   }

   public void setSourceDEDId (Long sourceDEDId) {
      this.sourceDEDId = sourceDEDId;
   }

   public Long getDestinationDEDId () {
      return destinationDEDId;
   }

   public void setDestinationDEDId (Long destinationDEDId) {
      this.destinationDEDId = destinationDEDId;
   }

   public Long getPathDefinitionId () {
      return pathDefinitionId;
   }

   public void setPathDefinitionId (Long pathDefinitionId) {
      this.pathDefinitionId = pathDefinitionId;
   }

   // Base methods

   @Override
   public boolean equals (Object obj) {
      boolean equal = true;
      if (obj instanceof InstancePathDefinition) {
         InstancePathDefinition temp = (InstancePathDefinition) obj;
         equal = this.sourceDEDId.equals(temp.getSourceDEDId());
         equal &= this.destinationDEDId.equals(temp.getDestinationDEDId());
         equal &= this.pathDefinitionId.equals(temp.getPathDefinitionId());
      }
      return equal;
   }

   
   /**
    * @return the sourceDED
    */
   public BusinessEntityDefinition getSourceDED () {
      return sourceDED;
   }

   
   /**
    * @param sourceDED the sourceDED to set
    */
   public void setSourceDED (BusinessEntityDefinition sourceDED) {
      this.sourceDED = sourceDED;
   }

   
   /**
    * @return the destinationDED
    */
   public BusinessEntityDefinition getDestinationDED () {
      return destinationDED;
   }

   
   /**
    * @param destinationDED the destinationDED to set
    */
   public void setDestinationDED (BusinessEntityDefinition destinationDED) {
      this.destinationDED = destinationDED;
   }

   
   /**
    * @return the pathDefinition
    */
   public PathDefinition getPathDefinition () {
      return pathDefinition;
   }

   
   /**
    * @param pathDefinition the pathDefinition to set
    */
   public void setPathDefinition (PathDefinition pathDefinition) {
      this.pathDefinition = pathDefinition;
   }
}
