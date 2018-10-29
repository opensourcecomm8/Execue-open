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

/**
 * @author Nitesh
 * @since Jan 26, 2010 - 11:11:26 AM
 */

public class InstanceTripleDefinition implements java.io.Serializable {

   private static final long serialVersionUID = -5303862802383732054L;
   private Long              id;
   private Long              srcInstanceBedId;
   private Long              destInstanceBedId;
   private Long              srcConceptBedId;
   private Long              destConceptBedId;
   private Long              relationBedId;

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
    * @return the srcInstanceBedId
    */
   public Long getSrcInstanceBedId () {
      return srcInstanceBedId;
   }

   /**
    * @param srcInstanceBedId
    *           the srcInstanceBedId to set
    */
   public void setSrcInstanceBedId (Long srcInstanceBedId) {
      this.srcInstanceBedId = srcInstanceBedId;
   }

   /**
    * @return the destInstanceBedId
    */
   public Long getDestInstanceBedId () {
      return destInstanceBedId;
   }

   /**
    * @param destInstanceBedId
    *           the destInstanceBedId to set
    */
   public void setDestInstanceBedId (Long destInstanceBedId) {
      this.destInstanceBedId = destInstanceBedId;
   }

   /**
    * @return the srcConceptBedId
    */
   public Long getSrcConceptBedId () {
      return srcConceptBedId;
   }

   /**
    * @param srcConceptBedId
    *           the srcConceptBedId to set
    */
   public void setSrcConceptBedId (Long srcConceptBedId) {
      this.srcConceptBedId = srcConceptBedId;
   }

   /**
    * @return the destConceptBedId
    */
   public Long getDestConceptBedId () {
      return destConceptBedId;
   }

   /**
    * @param destConceptBedId
    *           the destConceptBedId to set
    */
   public void setDestConceptBedId (Long destConceptBedId) {
      this.destConceptBedId = destConceptBedId;
   }

   /**
    * @return the relationBedId
    */
   public Long getRelationBedId () {
      return relationBedId;
   }

   /**
    * @param relationBedId
    *           the relationBedId to set
    */
   public void setRelationBedId (Long relationBedId) {
      this.relationBedId = relationBedId;
   }

}
