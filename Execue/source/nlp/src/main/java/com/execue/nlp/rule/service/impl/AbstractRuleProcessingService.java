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

import java.util.List;
import java.util.TreeSet;

import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.IWeightedEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.rules.weight.assignment.WeightAssignmentRule;
import com.execue.nlp.rule.AssignmentRuleInput;
import com.execue.nlp.rule.IAssignmentRuleInput;
import com.execue.nlp.rule.IRecognitionRule;
import com.execue.nlp.rule.IRecognitionRuleInput;
import com.execue.nlp.rule.IValidationRule;
import com.execue.nlp.rule.IValidationRuleInput;
import com.execue.nlp.rule.IWeightAssignmentRule;
import com.execue.nlp.rule.service.IRuleProcessingService;
import com.execue.nlp.util.NLPUtilities;

/**
 * @author Nitesh
 */
public abstract class AbstractRuleProcessingService implements IRuleProcessingService {

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.rule.service.IRuleProcessingService#validate(com.execue.nlp.rule.AssignmentRuleInput,
    *      com.execue.nlp.rule.IWeightAssignmentRule)
    */
   public boolean validate (AssignmentRuleInput assignmentRuleInput, IWeightAssignmentRule weightAssignmentRule) {
      return true;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.rule.service.IRuleProcessingService#getRecognition(com.execue.nlp.rule.IRecognitionRuleInput,
    *      com.execue.nlp.rule.IRecognitionRule)
    */
   public List<INormalizedData> getRecognition (IRecognitionRuleInput recognitionRuleInput,
            IRecognitionRule recognitionRule) {
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.rule.service.IRuleProcessingService#getWeight(com.execue.nlp.rule.IAssignmentRuleInput,
    *      com.execue.nlp.rule.IWeightAssignmentRule)
    */
   public double getWeight (IAssignmentRuleInput assignmentRuleInput, IWeightAssignmentRule weightAssignmentRule) {
      return 0;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.rule.service.IRuleProcessingService#getWeight(com.execue.nlp.rule.IAssignmentRuleInput,
    *      java.util.List)
    */
   public double getWeight (IAssignmentRuleInput assignmentRuleInput, List<IWeightAssignmentRule> weightAssignmentRule) {
      return 0;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.rule.service.IRuleProcessingService#isValid(com.execue.nlp.rule.IValidationRuleInput,
    *      com.execue.nlp.rule.IValidationRule)
    */
   public boolean isValid (IValidationRuleInput validationRuleInput, IValidationRule validationRule) {
      return false;
   }

   /**
    * This method returns the proximity reduction value for the source and destination
    * 
    * @param rule
    * @param source
    * @param destination
    * @return
    */
   public double getProximityReductionValue (WeightAssignmentRule rule, RecognitionEntity source,
            RecognitionEntity destination) {
      int positionDiff = NLPUtilities.getPositionDifference(source, destination);
      double proximityReduction = (positionDiff - 1) * rule.getProximityReduction();
      return proximityReduction;
   }

   /* (non-Javadoc)
    * @see com.execue.nlp.rule.service.IRuleProcessingService#getProximityReductionValue(com.execue.nlp.bean.rules.weight.assignment.WeightAssignmentRule, java.util.List)
    */
   public double getProximityReductionValue (WeightAssignmentRule rule, List<IWeightedEntity> entities) {
      TreeSet<Integer> referredTokenPositionsForProximityReduction = NLPUtilities
               .getReferredTokenPositions(entities);
      return referredTokenPositionsForProximityReduction.size() * rule.getProximityReduction();
   }
}
