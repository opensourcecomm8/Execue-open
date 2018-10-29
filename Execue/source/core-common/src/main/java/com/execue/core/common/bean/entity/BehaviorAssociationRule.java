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

import com.execue.core.common.type.AssociationPositionType;

/**
 * This class represents the BehaviorAssociationRule object.
 * 
 * @author Nihar
 * @version 1.0
 * @since 20/01/2011
 */
public class BehaviorAssociationRule implements Serializable {

   private static final long       serialVersionUID = 1L;

   private Long                    id;

   private Long                    behaviorBeId;
   private AssociationPositionType behaviorAssociationPos;
   private Rule                    rule;

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
    * @return the behaviorBeId
    */
   public Long getBehaviorBeId () {
      return behaviorBeId;
   }

   /**
    * @param behaviorBeId
    *           the behaviorBeId to set
    */
   public void setBehaviorBeId (Long behaviorBeId) {
      this.behaviorBeId = behaviorBeId;
   }

   /**
    * @return the behaviorAssociationPos
    */
   public AssociationPositionType getBehaviorAssociationPos () {
      return behaviorAssociationPos;
   }

   /**
    * @param behaviorAssociationPos
    *           the behaviorAssociationPos to set
    */
   public void setBehaviorAssociationPos (AssociationPositionType behaviorAssociationPos) {
      this.behaviorAssociationPos = behaviorAssociationPos;
   }

   /**
    * @return the rule
    */
   public Rule getRule () {
      return rule;
   }

   /**
    * @param rule
    *           the rule to set
    */
   public void setRule (Rule rule) {
      this.rule = rule;
   }

}
