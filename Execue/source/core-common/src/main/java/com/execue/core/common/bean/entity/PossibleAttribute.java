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

import com.execue.core.common.type.CheckType;

/**
 * This class represents the PossibleAttribute object.
 * 
 * @author Nihar
 * @version 1.0
 * @since 11/05/10
 */
public class PossibleAttribute implements Serializable {

   private static final long        serialVersionUID     = 1L;
   private Long                     id;
   private BusinessEntityDefinition typeBed;
   private BusinessEntityDefinition componentTypeBed;
   private BusinessEntityDefinition relationBed;
   private CheckType                optional             = CheckType.YES;
   private CheckType                inherent             = CheckType.NO;
   private CheckType                multipleRealizations = CheckType.NO;
   private Long                     defaultRealizationBedId;
   private Set<Rule>                rules;

   /**
    * @return the rules
    */
   public Set<Rule> getRules () {
      return rules;
   }

   /**
    * @param rules
    *           the rules to set
    */
   public void setRules (Set<Rule> rules) {
      this.rules = rules;
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
    * @return the componentTypeBed
    */
   public BusinessEntityDefinition getComponentTypeBed () {
      return componentTypeBed;
   }

   /**
    * @param componentTypeBed
    *           the componentTypeBed to set
    */
   public void setComponentTypeBed (BusinessEntityDefinition componentTypeBed) {
      this.componentTypeBed = componentTypeBed;
   }

   /**
    * @return the relationBed
    */
   public BusinessEntityDefinition getRelationBed () {
      return relationBed;
   }

   /**
    * @param relationBed
    *           the relationBed to set
    */
   public void setRelationBed (BusinessEntityDefinition relationBed) {
      this.relationBed = relationBed;
   }

   /**
    * @return the typeBed
    */
   public BusinessEntityDefinition getTypeBed () {
      return typeBed;
   }

   /**
    * @param typeBed
    *           the typeBed to set
    */
   public void setTypeBed (BusinessEntityDefinition typeBed) {
      this.typeBed = typeBed;
   }

   /**
    * @return the optional
    */
   public CheckType getOptional () {
      return optional;
   }

   /**
    * @param optional
    *           the optional to set
    */
   public void setOptional (CheckType optional) {
      this.optional = optional;
   }

   public CheckType getInherent () {
      return inherent;
   }

   public void setInherent (CheckType inherent) {
      this.inherent = inherent;
   }

   public CheckType getMultipleRealizations () {
      return multipleRealizations;
   }

   public void setMultipleRealizations (CheckType multipleRealizations) {
      this.multipleRealizations = multipleRealizations;
   }

   public Long getDefaultRealizationBedId () {
      return defaultRealizationBedId;
   }

   public void setDefaultRealizationBedId (Long defaultRealizationBedId) {
      this.defaultRealizationBedId = defaultRealizationBedId;
   }
}
