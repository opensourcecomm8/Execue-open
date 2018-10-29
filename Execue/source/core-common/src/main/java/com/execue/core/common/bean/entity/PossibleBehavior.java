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

import com.execue.core.common.type.BehaviorType;

/**
 * This class represents the PossibleBehavior object.
 * 
 * @author Nitesh
 * @version 1.0
 * @since 16/02/10
 */
public class PossibleBehavior implements Serializable {

   private static final long serialVersionUID = 1L;

   private Long              id;

   private Long              typeBeId;
   private BehaviorType      behaviorType;

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
    * @return the typeBeId
    */
   public Long getTypeBeId () {
      return typeBeId;
   }

   /**
    * @param typeBeId
    *           the typeBeId to set
    */
   public void setTypeBeId (Long typeBeId) {
      this.typeBeId = typeBeId;
   }

   /**
    * @return the behaviorType
    */
   public BehaviorType getBehaviorType () {
      return behaviorType;
   }

   /**
    * @param behaviorType
    *           the behaviorType to set
    */
   public void setBehaviorType (BehaviorType behaviorType) {
      this.behaviorType = behaviorType;
   }
}
