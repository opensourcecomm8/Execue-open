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

import com.execue.core.common.bean.algorithm.BaseBean;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.nlp.bean.rules.AssociationRuleType;
import com.execue.nlp.bean.rules.association.WeightReductionPart;
import com.execue.nlp.rule.IWeightAssignmentRule;

/**
 * Holds the WeightAssignmentRule
 * 
 * @author Nitesh
 */
public class WeightAssignmentRule extends BaseBean implements IWeightAssignmentRule {

   private static final long                  serialVersionUID   = 1L;
   private String                             associationID;
   private Long                               ruleId;
   private List<EntityTripleDefinition>       triples;
   private String                             associationType;
   private String                             associationAllowed = "YES";
   private List<WeightReductionPart>          patternList;
   private DefaultWeightAssignmentRuleContent defaultWeightAssignmentRuleContent;
   private AssociationRuleType                associationRuleType;

   /**
    * @return the associationRuleType
    */
   public AssociationRuleType getAssociationRuleType () {
      if (associationRuleType == null) {
         associationRuleType = getDefaultWeightAssignmentRuleContent().getAssociationRuleType();
      }
      return associationRuleType;
   }

   /**
    * @param associationRuleType
    *           the associationRuleType to set
    */
   public void setAssociationRuleType (AssociationRuleType associationRuleType) {
      this.associationRuleType = associationRuleType;
   }

   public List<EntityTripleDefinition> getTriples () {
      return triples;
   }

   public void setTriples (List<EntityTripleDefinition> triple) {
      this.triples = triple;
   }

   public String getAssociationAllowed () {
      return associationAllowed;
   }

   public void setAssociationAllowed (String associationAllowed) {
      this.associationAllowed = associationAllowed;
   }

   public String getAssociationType () {
      return associationType;
   }

   public void setAssociationType (String associationType) {
      this.associationType = associationType;
   }

   public List<WeightReductionPart> getPatternList () {
      return patternList;
   }

   public void setPatternList (List<WeightReductionPart> patternList) {
      this.patternList = patternList;
   }

   // Utility Methods

   public void addTriple (EntityTripleDefinition trpl) {
      if (this.triples == null) {
         this.triples = new ArrayList<EntityTripleDefinition>(1);
      }
      this.triples.add(trpl);
   }

   public boolean isAssociationAllowed () {
      return this.associationAllowed.equalsIgnoreCase("YES");
   }

   /**
    * @return the ruleId
    */
   public Long getRuleId () {
      return ruleId;
   }

   /**
    * @param ruleId
    *           the ruleId to set
    */
   public void setRuleId (Long ruleId) {
      this.ruleId = ruleId;
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
    * @return the defaultWeightAssignmentRuleContent
    */
   public DefaultWeightAssignmentRuleContent getDefaultWeightAssignmentRuleContent () {
      return defaultWeightAssignmentRuleContent;
   }

   /**
    * @param defaultWeightAssignmentRuleContent
    *           the defaultWeightAssignmentRuleContent to set
    */
   public void setDefaultWeightAssignmentRuleContent (
            DefaultWeightAssignmentRuleContent defaultWeightAssignmentRuleContent) {
      this.defaultWeightAssignmentRuleContent = defaultWeightAssignmentRuleContent;
   }

   public double getProximityReduction () {
      return getDefaultWeightAssignmentRuleContent().getProximityReduction();
   }

   public double getWeight () {
      return getDefaultWeightAssignmentRuleContent().getDefaultWeight();
   }
}
