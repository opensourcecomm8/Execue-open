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


package com.execue.core.common.bean.nlp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nitesh
 */
public class LocationNormalizedDataEntity extends NormalizedDataEntity {

   private Map<Long, Long> instanceByConceptBedId;

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder();
      sb.append(super.toString());
      return sb.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      LocationNormalizedDataEntity clonedNormalizedDataEntity = (LocationNormalizedDataEntity) super.clone();
      Map<Long, Long> clonedInstanceByConceptBedId = new HashMap<Long, Long>();
      clonedInstanceByConceptBedId.putAll(getInstanceByConceptBedId());
      clonedNormalizedDataEntity.setInstanceByConceptBedId(clonedInstanceByConceptBedId);
      return clonedNormalizedDataEntity;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof LocationNormalizedDataEntity || obj instanceof String)
               && this.toString().equals(obj.toString());
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
    * @return the instanceByConceptBedId
    */
   public Map<Long, Long> getInstanceByConceptBedId () {
      if (instanceByConceptBedId == null) {
         instanceByConceptBedId = new HashMap<Long, Long>();
      }
      return instanceByConceptBedId;
   }

   /**
    * @param instanceByConceptBedId the instanceByConceptBedId to set
    */
   public void setInstanceByConceptBedId (Map<Long, Long> instanceByConceptBedId) {
      this.instanceByConceptBedId = instanceByConceptBedId;
   }
}