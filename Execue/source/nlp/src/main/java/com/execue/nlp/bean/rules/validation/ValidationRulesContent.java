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


package com.execue.nlp.bean.rules.validation;

import java.util.Map;

import com.execue.core.common.bean.algorithm.BaseBean;
import com.execue.nlp.rule.IValidationRule;

/**
 * @author Nitesh
 */
public class ValidationRulesContent extends BaseBean {

   private static final long serialVersionUID = -6450822769395296961L;
   private Map<Long, IValidationRule> validationRules;

   /**
    * @return the validationRules
    */
   public Map<Long, IValidationRule> getValidationRules () {
      return validationRules;
   }

   /**
    * @param validationRules
    *           the validationRules to set
    */
   public void setValidationRules (Map<Long, IValidationRule> validationRules) {
      this.validationRules = validationRules;
   }

}
