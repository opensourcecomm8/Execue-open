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
package com.execue.core.common.bean.nlp;

import com.execue.core.common.bean.WeightInformation;

/**
 * @author Nihar Agrawal
 */
public class InstanceInformation implements Cloneable {

   private static final long serialVersionUID = 1L;
   private Long              instanceBedId;
   private Long              knowledgeId;
   private String            instanceValue;
   private String            instanceDisplayName;
   private String            displaySymbol;
   private WeightInformation weightInformation;

   @Override
   public String toString () {
      return getInstanceBedId() + " : " + getInstanceValue();
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      InstanceInformation instanceInformation = (InstanceInformation) super.clone();
      instanceInformation.setInstanceBedId(this.getInstanceBedId());
      instanceInformation.setKnowledgeId(this.getKnowledgeId());
      instanceInformation.setInstanceValue(this.getInstanceValue());
      instanceInformation.setInstanceDisplayName(this.getInstanceDisplayName());
      instanceInformation.setDisplaySymbol(this.getDisplaySymbol());
      instanceInformation.setWeightInformation(this.getWeightInformation());
      return instanceInformation;
   }

   @Override
   public boolean equals (Object obj) {
      return (obj instanceof InstanceInformation || obj instanceof String)
               && this.toString().equalsIgnoreCase(obj.toString());
   }

   @Override
   public int hashCode () {
      return this.toString().hashCode();
   }

   /**
    * @return the instanceValue
    */
   public String getInstanceValue () {
      return instanceValue;
   }

   /**
    * @param instanceValue
    *           the instanceValue to set
    */
   public void setInstanceValue (String instanceValue) {
      this.instanceValue = instanceValue;
   }

   /**
    * @return the instanceBedId
    */
   public Long getInstanceBedId () {
      return instanceBedId;
   }

   /**
    * @param instanceBedId
    *           the instanceBedId to set
    */
   public void setInstanceBedId (Long instanceBedId) {
      this.instanceBedId = instanceBedId;
   }

   /**
    * @return the instanceDisplayName
    */
   public String getInstanceDisplayName () {
      return instanceDisplayName;
   }

   /**
    * @param instanceDisplayName
    *           the instanceDisplayName to set
    */
   public void setInstanceDisplayName (String instanceDisplayName) {
      this.instanceDisplayName = instanceDisplayName;
   }

   /**
    * @return the displaySymbol
    */
   public String getDisplaySymbol () {
      return displaySymbol;
   }

   /**
    * @param displaySymbol
    *           the displaySymbol to set
    */
   public void setDisplaySymbol (String displaySymbol) {
      this.displaySymbol = displaySymbol;
   }

   /**
    * @return the serialVersionUID
    */
   public static long getSerialVersionUID () {
      return serialVersionUID;
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

   public Long getId () {
      return instanceBedId;
   }

   /**
    * @return the weightInformation
    */
   public WeightInformation getWeightInformation () {
      return weightInformation;
   }

   /**
    * @param weightInformation the weightInformation to set
    */
   public void setWeightInformation (WeightInformation weightInformation) {
      this.weightInformation = weightInformation;
   }
}