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


package com.execue.rules.configuration.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.RuleBase;

import com.execue.core.configuration.IConfiguration;
import com.execue.rules.configuration.IRuleConfigurationService;

/**
 * This class defines the rule configuration service, primary objective of this class is <BR>
 * 1. To provide rulesMap that will be further used by rule services <BR>
 * 2. To define configuration constant and configuration property methods
 * 
 * @author Jitendra
 * @version 1.0
 * @since 20/10/11
 */

public class RuleConfigurationServiceImpl implements IRuleConfigurationService {

   private String                RULE_NAME_KEY      = "rules.rule.rule-name";
   private String                RULE_FILE_NAME_KEY = "rules.rule.rule-file";

   private IConfiguration        ruleConfiguration;
   private Map<String, RuleBase> rulesMap           = new HashMap<String, RuleBase>();

   /**
    * @return the ruleConfiguration
    */
   public IConfiguration getRuleConfiguration () {
      return ruleConfiguration;
   }

   /**
    * @param ruleConfiguration
    *           the ruleConfiguration to set
    */
   public void setRuleConfiguration (IConfiguration ruleConfiguration) {
      this.ruleConfiguration = ruleConfiguration;
   }

   @Override
   public RuleBase getRuleBase (String ruleName) {
      return rulesMap.get(ruleName);
   }

   public List<String> getRuleNames () {
      return getRuleConfiguration().getList(RULE_NAME_KEY);
   }

   public List<String> getFilePaths () {
      return getRuleConfiguration().getList(RULE_FILE_NAME_KEY);
   }

   public void loadRulesMap (Map<String, RuleBase> rulesMap) {
      this.rulesMap = rulesMap;
   }
}
