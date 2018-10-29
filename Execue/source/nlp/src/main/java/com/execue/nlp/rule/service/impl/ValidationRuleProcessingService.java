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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.execue.core.common.type.ValidationRuleType;
import com.execue.nlp.bean.rules.validation.ValidationRule;
import com.execue.nlp.rule.IValidationRule;
import com.execue.nlp.rule.IValidationRuleInput;
import com.execue.util.ExpressionEvaluator;

/**
 * @author Nitesh
 */
public class ValidationRuleProcessingService extends AbstractRuleProcessingService {

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.rule.service.IRuleProcessingService#isValid(com.execue.nlp.rule.IValidationRuleInput,
    *      com.execue.nlp.rule.IValidationRule)
    */
   @Override
   public boolean isValid (IValidationRuleInput validationRuleInput, IValidationRule validationRule) {
      String expression = validationRuleInput.getRuleInput();
      if (validationRule.getRuleType() == ValidationRuleType.REGEX_RULE) {
         Pattern pattern = Pattern.compile(((ValidationRule) validationRule).getExpandedRule(),
                  Pattern.CASE_INSENSITIVE);
         Matcher matcher = pattern.matcher(expression);
         return matcher.matches();
      } else if (validationRule.getRuleType() == ValidationRuleType.LOGICAL_RULE) {
         return ExpressionEvaluator.evaluate(expression);
      }
      return false;
   }
}