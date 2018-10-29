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

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.type.ValidationRuleType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.engine.barcode.token.TokenUtility;
import com.execue.nlp.rule.IValidationRule;
import com.execue.nlp.rule.IValidationRuleInput;
import com.execue.nlp.rule.ValidationRuleInput;
import com.execue.nlp.rule.service.IRuleProcessingService;

/**
 * @author Nitesh
 */
public class CloudRuleExecutor {

   private IRuleProcessingService ruleProcessingService;

   /**
    * This method does the validation for the input string of recognition entities and the set of validation rules
    * present on the recognized cloud entity
    * 
    * @param recognizedCloudEntity
    * @param validationRules
    * @return boolean true/false
    */
   public boolean processValidationRule (RecognizedCloudEntity recognizedCloudEntity,
            List<IValidationRule> validationRules) {

      // Return true by default if no validation rules exist for the cloud
      if (CollectionUtils.isEmpty(validationRules)) {
         return true;
      }

      Map<ValidationRuleType, List<IValidationRule>> rulesByRuleType = getValiationRulesByRuleType(validationRules);

      List<IValidationRule> logicalRules = rulesByRuleType.get(ValidationRuleType.LOGICAL_RULE);
      boolean isValid = processLogicalRules(recognizedCloudEntity, logicalRules);

      // Return if it is not valid
      if (!isValid) {
         return isValid;
      }

      List<IValidationRule> regexRules = rulesByRuleType.get(ValidationRuleType.REGEX_RULE);
      // If it is valid and regex rules are not defined then return true  
      if (isValid && CollectionUtils.isEmpty(regexRules)) {
         return isValid;
      }

      // Process regex rules
      isValid = processRegexRules(recognizedCloudEntity, regexRules);

      return isValid;
   }

   /**
    * @param recognizedCloudEntity
    * @param regexRules
    * @return the boolean true/false
    */
   private boolean processRegexRules (RecognizedCloudEntity recognizedCloudEntity, List<IValidationRule> regexRules) {
      if (CollectionUtils.isEmpty(regexRules)) {
         return true;
      }
      boolean isValid = false;
      List<IWeightedEntity> ruleRecognitionEntities = new ArrayList<IWeightedEntity>(1);
      ruleRecognitionEntities.addAll(recognizedCloudEntity.getRecognitionEntities());
      List<String> recEntityStrings = TokenUtility.getRecognitionEntityAsString(ruleRecognitionEntities);
      String userQueryInStrignForm = ExecueCoreUtil.convertAsString(recEntityStrings);
      for (IValidationRule validationRule : regexRules) {
         IValidationRuleInput validationRuleInput = new ValidationRuleInput(userQueryInStrignForm);
         isValid = getRuleProcessingService().isValid(validationRuleInput, validationRule);
         if (isValid) {
            break;
         }
      }
      return isValid;
   }

   /**
    * @param recognizedCloudEntity
    * @param logicalRules
    * @return boolean true/false
    */
   private boolean processLogicalRules (RecognizedCloudEntity recognizedCloudEntity, List<IValidationRule> logicalRules) {
      if (CollectionUtils.isEmpty(logicalRules)) {
         return true;
      }
      boolean logicallyValid = false;
      for (IValidationRule validationRule : logicalRules) {
         String ruleInput = getRuleInput(recognizedCloudEntity, validationRule.getRule());
         IValidationRuleInput validationRuleInput = new ValidationRuleInput(ruleInput);
         logicallyValid = getRuleProcessingService().isValid(validationRuleInput, validationRule);
         if (logicallyValid) {
            break;
         }
      }
      return logicallyValid;
   }

   /**
    * @param recognizedCloudEntity
    * @param expression
    * @return the String expanded expression
    */
   private String getRuleInput (RecognizedCloudEntity recognizedCloudEntity, String expression) {
      Map<Long, CloudComponent> cloudComponentInfoMap = recognizedCloudEntity.getCloudComponentInfoMap();
      for (CloudComponent cloudComponent : cloudComponentInfoMap.values()) {
         Integer partId = cloudComponent.getCloudPart();
         expression = expression.replaceAll("" + partId, "true");
      }
      List<String> asList = ExecueStringUtil.getAsList(expression);
      for (String part : asList) {
         if (ExecueCoreUtil.isNumber(part)) {
            expression = expression.replaceAll("" + part, "false");
         }
      }
      return expression;
   }

   private Map<ValidationRuleType, List<IValidationRule>> getValiationRulesByRuleType (
            List<IValidationRule> validationRules) {
      Map<ValidationRuleType, List<IValidationRule>> validationRulesByRuleType = new HashMap<ValidationRuleType, List<IValidationRule>>();
      if (CollectionUtils.isEmpty(validationRules)) {
         return validationRulesByRuleType;
      }
      for (IValidationRule validationRule : validationRules) {
         ValidationRuleType ruleType = validationRule.getRuleType();
         List<IValidationRule> rulesByType = validationRulesByRuleType.get(ruleType);
         if (rulesByType == null) {
            rulesByType = new ArrayList<IValidationRule>();
            validationRulesByRuleType.put(ruleType, rulesByType);
         }
         rulesByType.add(validationRule);
      }
      return validationRulesByRuleType;
   }

   public static Map<Integer, IWeightedEntity> getRecEntitesByPosMap (List<IWeightedEntity> recEntities) {
      Map<Integer, IWeightedEntity> recEntitiesByPosMap = new HashMap<Integer, IWeightedEntity>();
      for (IWeightedEntity weightedEntity : recEntities) {
         recEntitiesByPosMap.put(((RecognitionEntity) weightedEntity).getPosition(), weightedEntity);
      }
      return recEntitiesByPosMap;
   }

   /**
    * @return the ruleProcessingService
    */
   public IRuleProcessingService getRuleProcessingService () {
      return ruleProcessingService;
   }

   /**
    * @param ruleProcessingService
    *           the ruleProcessingService to set
    */
   public void setRuleProcessingService (IRuleProcessingService ruleProcessingService) {
      this.ruleProcessingService = ruleProcessingService;
   }
}