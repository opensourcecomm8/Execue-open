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
package com.execue.nlp.rule.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.nlp.bean.rules.association.WeightReductionPart;
import com.execue.nlp.bean.rules.association.WeightReductionPartType;
import com.execue.nlp.bean.rules.weight.assignment.WeightAssignmentRule;
import com.execue.nlp.rule.AssignmentRuleInput;
import com.execue.nlp.rule.IAssignmentRuleInput;
import com.execue.nlp.rule.IWeightAssignmentRule;
import com.execue.nlp.util.RegexUtilities;

/**
 * @author Nihar
 */
public class WeightRuleProcessingService extends AbstractRuleProcessingService {

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.rule.service.impl.AbstractRuleProcessingService#getWeight(com.execue.nlp.rule.IAssignmentRuleInput,
    *      com.execue.nlp.rule.IWeightAssignmentRule)
    */
   @Override
   public double getWeight (IAssignmentRuleInput assignmentRuleInput, IWeightAssignmentRule weightAssignmentRule) {
      WeightAssignmentRule rule = (WeightAssignmentRule) weightAssignmentRule;
      AssignmentRuleInput ruleInput = (AssignmentRuleInput) assignmentRuleInput;

      double qualityReduction = NLPConstants.MIN_QUALITY;
      if (!CollectionUtils.isEmpty(rule.getPatternList())) {
         List<WeightReductionPart> reductionList = rule.getPatternList();
         for (WeightReductionPart wrPart : reductionList) {
            List<String> matchList = RegexUtilities.getMatchedPortions(wrPart.getExpandedExpressionPattern(), ruleInput
                     .getUserQuery());

            qualityReduction = qualityReduction + matchList.size() * wrPart.getWeightReductionValue();
         }
      }
      return qualityReduction;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.rule.service.impl.AbstractRuleProcessingService#getWeight(com.execue.nlp.rule.IAssignmentRuleInput,
    *      java.util.List)
    */
   @Override
   public double getWeight (IAssignmentRuleInput assignmentRuleInput, List<IWeightAssignmentRule> weightAssignmentRules) {
      double qualityReduction = NLPConstants.MIN_QUALITY;
      if (CollectionUtils.isEmpty(weightAssignmentRules)) {
         return qualityReduction;
      }

      AssignmentRuleInput ruleInput = (AssignmentRuleInput) assignmentRuleInput;

      // Match and apply weight reduction on All Allowed Rules
      qualityReduction = getQualityReductionForMatchedRules(weightAssignmentRules, ruleInput,
               WeightReductionPartType.ALLOWED);

      if (!ruleInput.isAllowedInProximity()) {
         // Match and apply weight reduction on All Not Allowed Rules
         qualityReduction = getQualityReductionForMatchedRules(weightAssignmentRules, ruleInput,
                  WeightReductionPartType.NOT_ALLOWED);
      }
      return qualityReduction;
   }

   /**
    * @param assignmentRuleInput
    * @param reductionValueByGroupNumber
    * @return the reduction quality value 
    */
   private double getQualityReductionForMatchedRules (List<IWeightAssignmentRule> weightAssignmentRules,
            AssignmentRuleInput assignmentRuleInput, WeightReductionPartType weightReductionPartType) {
      double qualityReduction = NLPConstants.MIN_QUALITY;

      Map<Integer, Double> reductionValueByGroupNumber = new HashMap<Integer, Double>();
      String expandedExpression = getMatchedExpressionByGroupNumberForPartType(weightAssignmentRules,
               weightReductionPartType, reductionValueByGroupNumber);

      if (StringUtils.isEmpty(expandedExpression)) {
         return qualityReduction;
      }

      Pattern pattern = Pattern.compile(expandedExpression, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(assignmentRuleInput.getUserQuery());
      if (matcher.find()) {
         if (WeightReductionPartType.ALLOWED == weightReductionPartType) {
            assignmentRuleInput.setAllowedInProximity(true);
         }
         for (int groupNumber = 1; groupNumber <= matcher.groupCount(); groupNumber++) {
            if (StringUtils.isNotEmpty(matcher.group(groupNumber))) {
               qualityReduction += reductionValueByGroupNumber.get(groupNumber);
            }
         }
      }
      return qualityReduction;
   }

   /**
    * This method returns the expanded regular expression (ORed) for the given list of weightAssignmentRules matching
    * part type weightReductionPartType
    * 
    * @param weightAssignmentRules
    * @param weightReductionPartType
    * @return the String form of expanded expression
    */
   private String getMatchedExpressionByGroupNumberForPartType (List<IWeightAssignmentRule> weightAssignmentRules,
            WeightReductionPartType weightReductionPartType, Map<Integer, Double> weightReductionValueByGroupNumber) {
      List<String> matchedExpressions = new ArrayList<String>();
      int groupNumber = 1;
      for (IWeightAssignmentRule weightAssignmentRule : weightAssignmentRules) {
         WeightAssignmentRule rule = (WeightAssignmentRule) weightAssignmentRule;
         if (!CollectionUtils.isEmpty(rule.getPatternList())) {
            List<WeightReductionPart> reductionList = rule.getPatternList();
            for (WeightReductionPart wrPart : reductionList) {
               if (weightReductionPartType == wrPart.getPartType()) {
                  matchedExpressions.add("(" + wrPart.getExpandedExpression() + ")");
                  weightReductionValueByGroupNumber.put(groupNumber, wrPart.getWeightReductionValue());
                  groupNumber++;
               }
            }
         }
      }
      return StringUtils.join(matchedExpressions, "|");
   }

   @Override
   public boolean validate (AssignmentRuleInput assignmentRuleInput, IWeightAssignmentRule weightAssignmentRule) {
      WeightAssignmentRule rule = (WeightAssignmentRule) weightAssignmentRule;
      List<IWeightAssignmentRule> rulesList = new ArrayList<IWeightAssignmentRule>(1);
      rulesList.add(rule);
      String expandedExpression = getExpandedExpressionForPartType(rulesList, WeightReductionPartType.ALLOWED);
      List<String> matchList = RegexUtilities
               .getMatchedPortions(expandedExpression, assignmentRuleInput.getUserQuery());
      return !CollectionUtils.isEmpty(matchList);
   }

   /**
    * This method returns the expanded regular expression (ORed) for the given list of weightAssignmentRules matching
    * part type weightReductionPartType
    * 
    * @param weightAssignmentRules
    * @param weightReductionPartType
    * @return the String form of expanded expression
    */
   private String getExpandedExpressionForPartType (List<IWeightAssignmentRule> weightAssignmentRules,
            WeightReductionPartType weightReductionPartType) {
      StringBuilder allExpandedExpression = new StringBuilder(1);
      for (IWeightAssignmentRule weightAssignmentRule : weightAssignmentRules) {
         WeightAssignmentRule rule = (WeightAssignmentRule) weightAssignmentRule;
         if (!CollectionUtils.isEmpty(rule.getPatternList())) {
            List<WeightReductionPart> reductionList = rule.getPatternList();
            for (WeightReductionPart wrPart : reductionList) {
               if (weightReductionPartType == wrPart.getPartType()) {
                  String expandedExpression = wrPart.getExpandedExpression();
                  double weightReductionValue = wrPart.getWeightReductionValue();
                  String expandedExpressionWithReductionValue = expandedExpression + "(#RV:" + weightReductionValue
                           + ")?";
                  allExpandedExpression.append("(").append(expandedExpressionWithReductionValue).append(")")
                           .append("|");
               }
            }
         }
      }
      if (allExpandedExpression.length() > 1) {
         allExpandedExpression.replace(allExpandedExpression.length() - 1, allExpandedExpression.length(), "");
      }
      return allExpandedExpression.toString();
   }

}