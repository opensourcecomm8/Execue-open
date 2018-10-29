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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.nlp.InstanceInformation;

public class InstanceEntity extends ConceptEntity implements Cloneable, Serializable {

   private static final long         serialVersionUID = 1L;
   private List<InstanceInformation> instanceInformations;

   @Override
   public Object clone () throws CloneNotSupportedException {
      InstanceEntity instanceEntity = (InstanceEntity) super.clone();
      List<InstanceInformation> clonedInstanceInformations = new ArrayList<InstanceInformation>();
      for (InstanceInformation instanceInformation : instanceInformations) {
         clonedInstanceInformations.add((InstanceInformation) instanceInformation.clone());
      }
      instanceEntity.setInstanceInformations(clonedInstanceInformations);
      return instanceEntity;
   }

   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder();
      sb.append(super.toString());
      for (InstanceInformation instanceInformation : getInstanceInformations()) {
         if (instanceInformation.toString() != null) {
            sb.append(" ").append(instanceInformation.toString());
         }
      }
      return sb.toString();
   }

   @Override
   public boolean equals (Object obj) {
      return (obj instanceof InstanceEntity || obj instanceof String)
               && this.toString().equalsIgnoreCase(obj.toString());

   }

   @Override
   public int hashCode () {
      return this.toString().hashCode();
   }

   /**
    * @return the instanceInformations
    */
   public List<InstanceInformation> getInstanceInformations () {
      if (instanceInformations == null) {
         instanceInformations = new ArrayList<InstanceInformation>(1);
      }
      return instanceInformations;
   }

   /**
    * @param instanceInformations
    *           the instanceInformations to set
    */
   public void setInstanceInformations (List<InstanceInformation> instanceInformations) {
      this.instanceInformations = instanceInformations;
   }

   public void addInstanceInformation (InstanceInformation instanceInformation) {
      if (this.instanceInformations == null) {
         instanceInformations = new ArrayList<InstanceInformation>(1);
      }
      instanceInformations.add(instanceInformation);
   }

   public String getDefaultInstanceValue () {
      if (CollectionUtils.isEmpty(instanceInformations)) {
         return null;
      }
      return getInstanceInformations().get(0).getInstanceValue();
   }

   public String getDefaultInstanceDisplayName () {
      if (CollectionUtils.isEmpty(instanceInformations)) {
         return null;
      }
      return getInstanceInformations().get(0).getInstanceDisplayName();
   }

   public String getDefaultInstanceDisplaySymbol () {
      if (CollectionUtils.isEmpty(instanceInformations)) {
         return null;
      }
      return getInstanceInformations().get(0).getDisplaySymbol();
   }

   public Long getDefaultInstanceBedId () {
      if (CollectionUtils.isEmpty(instanceInformations)) {
         return null;
      }
      return getInstanceInformations().get(0).getInstanceBedId();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.ConceptEntity#getId()
    */
   @Override
   public Long getId () {
      return getDefaultInstanceBedId();
   }

   public String getInstanceDisplayString () {
      if (!CollectionUtils.isEmpty(getInstanceInformations()) && this.getDefaultInstanceDisplayName() != null) {
         StringBuilder sb = new StringBuilder();
         int i = 0;
         for (InstanceInformation instanceInformation : instanceInformations) {
            if (i > 0) {
               sb.append(",");
            }
            sb.append(instanceInformation.getInstanceDisplayName());
            i++;
         }
         if (super.getConceptDisplayName() != null) {
            return sb.toString() + " As " + super.getConceptDisplayName();
         } else {
            return sb.toString();
         }
      } else if (!CollectionUtils.isEmpty(getInstanceInformations()) && this.getDefaultInstanceValue() != null) {
         StringBuilder sb = new StringBuilder();
         int i = 0;
         for (InstanceInformation instanceInformation : instanceInformations) {
            if (i > 0) {
               sb.append(",");
            }
            sb.append(instanceInformation.getInstanceValue());
            i++;
         }
         if (super.getConceptDisplayName() != null) {
            return sb.toString() + " As " + super.getConceptDisplayName();
         } else {
            return sb.toString();
         }
      }
      return null;
   }
}
