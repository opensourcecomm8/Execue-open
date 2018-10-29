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


/*
 * Created on March 17, 2010
 */
package com.execue.nlp.bean.rules.weight.assignment;

import java.util.ArrayList;
import java.util.List;

import com.execue.nlp.bean.rules.AssociationRuleType;
import com.execue.nlp.bean.rules.association.CAAssociationWord;
import com.execue.nlp.type.AssociationDirectionType;

/**
 * Holds the DefaultWeightAssignmentRule
 * 
 * @author Nitesh
 */
public class DefaultWeightAssignmentRuleContent implements Cloneable {

   private String                   associationID;
   private AssociationDirectionType associationDirection;
   private double                   proximityReduction   = 0;
   private double                   defaultWeight        = 0;
   private List<CAAssociationWord>  associationWordsInfo = new ArrayList<CAAssociationWord>();
   private AssociationRuleType      associationRuleType;

   /**
    * @return the associationRuleType
    */
   public AssociationRuleType getAssociationRuleType () {
      return associationRuleType;
   }

   /**
    * @param associationRuleType
    *           the associationRuleType to set
    */
   public void setAssociationRuleType (AssociationRuleType associationRuleType) {
      this.associationRuleType = associationRuleType;
   }

   /**
    * @return the associationID
    */
   public String getAssociationID () {
      return associationID;
   }

   /**
    * @param associationID
    *           the associationID to set
    */
   public void setAssociationID (String associationID) {
      this.associationID = associationID;
   }

   /**
    * @return the associationDirection
    */
   public AssociationDirectionType getAssociationDirection () {
      return associationDirection;
   }

   /**
    * @param associationDirection
    *           the associationDirection to set
    */
   public void setAssociationDirection (AssociationDirectionType associationDirection) {
      this.associationDirection = associationDirection;
   }

   /**
    * @return the proximityReduction
    */
   public double getProximityReduction () {
      return proximityReduction;
   }

   /**
    * @param proximityReduction
    *           the proximityReduction to set
    */
   public void setProximityReduction (double proximityReduction) {
      this.proximityReduction = proximityReduction;
   }

   /**
    * @return the defaultWeight
    */
   public double getDefaultWeight () {
      return defaultWeight;
   }

   /**
    * @param defaultWeight
    *           the defaultWeight to set
    */
   public void setDefaultWeight (double defaultWeight) {
      this.defaultWeight = defaultWeight;
   }

   /**
    * @return the associationWordsInfo
    */
   public List<CAAssociationWord> getAssociationWordsInfo () {
      return associationWordsInfo;
   }

   /**
    * @param associationWordsInfo
    *           the associationWordsInfo to set
    */
   public void setAssociationWordsInfo (List<CAAssociationWord> associationWordsInfo) {
      this.associationWordsInfo = associationWordsInfo;
   }

   @Override
   public DefaultWeightAssignmentRuleContent clone () throws CloneNotSupportedException {
      DefaultWeightAssignmentRuleContent defaultWeightAssignmentRuleContent = (DefaultWeightAssignmentRuleContent) super
               .clone();
      defaultWeightAssignmentRuleContent.setAssociationDirection(this.getAssociationDirection());
      defaultWeightAssignmentRuleContent.setDefaultWeight(this.getDefaultWeight());
      defaultWeightAssignmentRuleContent.setAssociationID(getAssociationID());
      defaultWeightAssignmentRuleContent.setAssociationWordsInfo(getAssociationWordsInfo());
      defaultWeightAssignmentRuleContent.setProximityReduction(getProximityReduction());
      return defaultWeightAssignmentRuleContent;
   }
}