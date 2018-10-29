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
package com.execue.nlp.bean.rules.validation;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import com.execue.core.common.type.ValidationRuleType;
import com.execue.nlp.bean.rules.RulePart;
import com.execue.nlp.rule.IValidationRule;

/**
 * @author Nihar
 */
public class ValidationRule implements IValidationRule {

   private static final long  serialVersionUID = 1315731529365196693L;

   private String             name;
   private Long               ruleId;
   private String             rule;
   private String             expandedRule;
   private List<RulePart>     ruleParts;
   private Pattern            compiledRegexPattern;
   private ValidationRuleType ruleType;

   /* (non-Javadoc)
    * @see com.execue.nlp.rule.IValidationRule#getRuleType()
    */
   public ValidationRuleType getRuleType () {
      return ruleType;
   }

   /**
    * @param ruleType
    *           the ruleType to set
    */
   public void setRuleType (ValidationRuleType ruleType) {
      this.ruleType = ruleType;
   }

   public Pattern getCompiledRegexPattern () {
      return compiledRegexPattern;
   }

   public void setCompiledRegexPattern (Pattern compiledRegexPattern) {
      this.compiledRegexPattern = compiledRegexPattern;
   }

   /**
    * @return Returns the expandedRule.
    */
   public String getExpandedRule () {
      return expandedRule;
   }

   /**
    * @param expandedRule
    *           The expandedRule to set.
    */
   public void setExpandedRule (String expandedRule) {
      this.expandedRule = expandedRule;
   }

   /**
    * @return Returns the name.
    */
   public String getName () {
      return name;
   }

   /**
    * @param name
    *           The name to set.
    */
   public void setName (String name) {
      this.name = name;
   }

   /**
    * @return Returns the rule.
    */
   public String getRule () {
      return rule;
   }

   /**
    * @param rule
    *           The rule to set.
    */
   public void setRule (String rule) {
      this.rule = rule;
   }

   /**
    * @return Returns the ruleParts.
    */
   public List<RulePart> getRuleParts () {
      return ruleParts;
   }

   /**
    * @param ruleParts
    *           The ruleParts to set.
    */
   public void setRuleParts (List<RulePart> ruleParts) {
      this.ruleParts = ruleParts;
   }

   public RulePart getRulePart (String rulePartId) {
      RulePart part = null;
      Iterator<RulePart> rulePartsIter = getRuleParts().iterator();
      while (rulePartsIter.hasNext()) {
         part = rulePartsIter.next();
         if (rulePartId.equalsIgnoreCase(part.getId())) {
            break;
         }
      }
      return part;
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

}
