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
package com.execue.nlp.rule;

import com.execue.core.common.bean.IWeightedEntity;

/**
 * @author Nitesh
 */
public class AssignmentRuleInput implements IAssignmentRuleInput {

   private IWeightedEntity source;
   private IWeightedEntity destination;
   private String          userQuery;
   private boolean         allowedInProximity;
   private boolean         validByAssociation;

   /**
    * @return the allowedInProximity
    */
   public boolean isAllowedInProximity () {
      return allowedInProximity;
   }

   /**
    * @param allowedInProximity
    *           the allowedInProximity to set
    */
   public void setAllowedInProximity (boolean allowedInProximity) {
      this.allowedInProximity = allowedInProximity;
   }

   /**
    * @return the userQuery
    */
   public String getUserQuery () {
      return userQuery;
   }

   /**
    * @param userQuery
    *           the userQuery to set
    */
   public void setUserQuery (String userQuery) {
      this.userQuery = userQuery;
   }

   /**
    * @return the source
    */
   public IWeightedEntity getSource () {
      return source;
   }

   /**
    * @param source
    *           the source to set
    */
   public void setSource (IWeightedEntity source) {
      this.source = source;
   }

   /**
    * @return the destination
    */
   public IWeightedEntity getDestination () {
      return destination;
   }

   /**
    * @param destination
    *           the destination to set
    */
   public void setDestination (IWeightedEntity destination) {
      this.destination = destination;
   }

   /**
    * Return true if Association is valid based on relation in User Query
    * @return True or False
    */
   public boolean isValidByAssociation () {
      return validByAssociation;
   }

   /**
    * Set if Association is valid by relation in user query
    * @param validByAssociation True or False
    */
   public void setValidByAssociation (boolean validByAssociation) {
      this.validByAssociation = validByAssociation;
   }
}
