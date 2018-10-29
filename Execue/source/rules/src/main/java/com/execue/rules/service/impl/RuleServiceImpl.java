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


package com.execue.rules.service.impl;

import org.drools.RuleBase;
import org.drools.StatefulSession;

import com.execue.rules.bean.RuleParameters;
import com.execue.rules.configuration.IRuleConfigurationService;
import com.execue.rules.exception.RuleException;
import com.execue.rules.exception.RuleFlowNotFoundException;
import com.execue.rules.service.IRuleService;

/**
 * @author kaliki
 * @since 4.0
 */

public class RuleServiceImpl implements IRuleService {

   private IRuleConfigurationService ruleConfigurationService;

   public boolean executeRule (String ruleName, RuleParameters params) throws RuleException {
      RuleBase ruleBase = getRuleConfigurationService().getRuleBase(ruleName);
      if (ruleBase == null) {
         throw new RuleFlowNotFoundException(10, "not found " + ruleName);
      }

      StatefulSession ruleSession = ruleBase.newStatefulSession();

      for (Object obj : params.getInput()) {
         ruleSession.insert(obj);
      }

      /*
       * if (debug) { workingMemory .addEventListener(new DebugWorkingMemoryEventListener()); }
       */
      ruleSession.fireAllRules();
      return true;
   }

   /**
    * @return the ruleConfigurationService
    */
   public IRuleConfigurationService getRuleConfigurationService () {
      return ruleConfigurationService;
   }

   /**
    * @param ruleConfigurationService the ruleConfigurationService to set
    */
   public void setRuleConfigurationService (IRuleConfigurationService ruleConfigurationService) {
      this.ruleConfigurationService = ruleConfigurationService;
   }

}
