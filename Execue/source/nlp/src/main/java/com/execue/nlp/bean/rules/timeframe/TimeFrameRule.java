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


package com.execue.nlp.bean.rules.timeframe;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import com.execue.core.common.bean.algorithm.BaseBean;
import com.execue.nlp.bean.rules.RulePart;
import com.execue.nlp.bean.rules.RuleRecognitionInfo;

public class TimeFrameRule extends BaseBean {

   /**
    *
    */
   private static final long          serialVersionUID = 1315731529365196603L;

   private Long                       conceptID; 
   private String                     conceptName;
   private String                     name;
   private String                     rule;
   private String                     expandedRule;
   private List<RulePart>             ruleParts;
   // private List<RulePlaceHolder> placeHolders;
   private List<TimeFrameRuleFormula> formulas;
   private List<RuleRecognitionInfo>  ruleRecognitionInfos;

   private Pattern                    compiledRegexPattern;

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
    * @return Returns the placeHolders.
    */
   /*
    * public List<RulePlaceHolder> getPlaceHolders() { return placeHolders; }
    */

   /**
    * @param placeHolders
    *           The placeHolders to set.
    */
   /*
    * public void setPlaceHolders(List<RulePlaceHolder> placeHolders) { this.placeHolders = placeHolders; }
    */

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

   /**
    * @return Returns the formulas.
    */
   public List<TimeFrameRuleFormula> getFormulas () {
      return formulas;
   }

   /**
    * @param formulas
    *           The formulas to set.
    */
   public void setFormulas (List<TimeFrameRuleFormula> formulas) {
      this.formulas = formulas;
   }

   /**
    * @return Returns the conceptName.
    */
   public String getConceptName () {
      return conceptName;
   }

   /**
    * @param conceptName
    *           The conceptName to set.
    */
   public void setConceptName (String conceptName) {
      this.conceptName = conceptName;
   }

   public Long getConceptID() {
      return conceptID;
   }

   public void setConceptID(Long conceptID) {
      this.conceptID = conceptID;
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

   public List<RuleRecognitionInfo> getRuleRecognitionInfos () {
      return ruleRecognitionInfos;
   }

   public void setRuleRecognitionInfos (List<RuleRecognitionInfo> ruleRecognitionInfos) {
      this.ruleRecognitionInfos = ruleRecognitionInfos;
   }

}
