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


package com.execue.nlp.bean.rules.weight.assignment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.algorithm.BaseBean;
import com.execue.nlp.bean.rules.RuleRegexComponent;
import com.execue.nlp.rule.IWeightAssignmentRule;

/**
 * @author Nitesh
 */
public class WeightAssignmentRulesContent extends BaseBean {

   private static final long                serialVersionUID = -6450822769395296961L;

   private List<RuleRegexComponent>         regexComponents;
   private Map<Long, IWeightAssignmentRule> weightAssignmentRules;
   private IWeightAssignmentRule            defaultLeftWeightAssignmentRule;
   private IWeightAssignmentRule            defaultRightWeightAssignmentRule;

   /**
    * @return the regexComponents
    */
   public List<RuleRegexComponent> getRegexComponents () {
      return regexComponents;
   }

   /**
    * @param regexComponents
    *           the regexComponents to set
    */
   public void setRegexComponents (List<RuleRegexComponent> regexComponents) {
      this.regexComponents = regexComponents;
   }

   /**
    * @return the weightAssignmentRules
    */
   public Map<Long, IWeightAssignmentRule> getWeightAssignmentRules () {
      return weightAssignmentRules;
   }

   /**
    * @param weightAssignmentRules
    *           the weightAssignmentRules to set
    */
   public void setWeightAssignmentRules (Map<Long, IWeightAssignmentRule> weightAssignmentRules) {
      this.weightAssignmentRules = weightAssignmentRules;
   }

   /**
    * @return the defaultLeftWeightAssignmentRule
    */
   public IWeightAssignmentRule getDefaultLeftWeightAssignmentRule () {
      return defaultLeftWeightAssignmentRule;
   }

   /**
    * @param defaultLeftWeightAssignmentRule
    *           the defaultLeftWeightAssignmentRule to set
    */
   public void setDefaultLeftWeightAssignmentRule (WeightAssignmentRule defaultLeftWeightAssignmentRule) {
      this.defaultLeftWeightAssignmentRule = defaultLeftWeightAssignmentRule;
   }

   /**
    * @return the defaultRightWeightAssignmentRule
    */
   public IWeightAssignmentRule getDefaultRightWeightAssignmentRule () {
      return defaultRightWeightAssignmentRule;
   }

   /**
    * @param defaultRightWeightAssignmentRule
    *           the defaultRightWeightAssignmentRule to set
    */
   public void setDefaultRightWeightAssignmentRule (WeightAssignmentRule defaultRightWeightAssignmentRule) {
      this.defaultRightWeightAssignmentRule = defaultRightWeightAssignmentRule;
   }

   public void addWeightAssignmentRule (WeightAssignmentRule weightAssignmentRule) {
      if (weightAssignmentRules == null) {
         weightAssignmentRules = new HashMap<Long, IWeightAssignmentRule>(1);
      }
      weightAssignmentRules.put(weightAssignmentRule.getRuleId(), weightAssignmentRule);
   }
}
