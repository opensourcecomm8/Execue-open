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

/**
 * @author Nitesh
 *
 */
public class ValidationRuleInput implements IValidationRuleInput {

   private String ruleInput;

   /**
    * Default constructor
    */
   public ValidationRuleInput () {

   }

   /**
    * Constructor which takes ruleInput
    * 
    * @param ruleInput
    */
   public ValidationRuleInput (String ruleInput) {
      this.ruleInput = ruleInput;
   }

   /**
    * @return the ruleInput
    */
   public String getRuleInput () {
      return ruleInput;
   }

   /**
    * @param ruleInput the ruleInput to set
    */
   public void setRuleInput (String ruleInput) {
      this.ruleInput = ruleInput;
   }
}