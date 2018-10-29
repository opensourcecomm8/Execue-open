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


package com.execue.report.view.rule.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.drools.WorkingMemory;

import com.execue.core.configuration.IConfiguration;
import com.execue.report.view.rule.config.RulesConfigurationConstants;
import com.execue.report.view.rule.service.IReportViewRuleService;
import com.execue.report.view.rule.service.IViewRuleService;
import com.execue.report.view.rule.service.IWorkingEnvironmentCallback;
import com.execue.rules.exception.RuleException;
import com.softwarefx.chartfx.server.ChartServer;

public class ReportViewRuleServiceImpl implements IReportViewRuleService {

   private static final Logger        log = Logger.getLogger(ReportViewRuleServiceImpl.class);

   private IViewRuleService           ruleService;
   private IConfiguration             ruleConfiguration;
   private static Map<String, String> ruleNameFileMap;

   public ReportViewRuleServiceImpl (IViewRuleService ruleService, IConfiguration ruleConfiguration) {
      super();
      this.ruleService = ruleService;
      this.ruleConfiguration = ruleConfiguration;
      initializeRuleMap();
   }

   public void initializeRuleMap () {
      ruleNameFileMap = new HashMap<String, String>();
      List<String> ruleNames = ruleConfiguration.getList(RulesConfigurationConstants.RULE_NAME_KEY);
      List<String> fileNames = ruleConfiguration.getList(RulesConfigurationConstants.RULE_FILE_NAME_KEY);

      if (log.isDebugEnabled()) {
         log.debug("Rule Names size " + ruleNames.size());
      }
      for (int index = 0; index < ruleNames.size(); index++) {
         ruleNameFileMap.put(ruleNames.get(index), fileNames.get(index));
         if (log.isDebugEnabled()) {
            log.debug(ruleNames.get(index) + "  " + fileNames.get(index));
         }
      }
   }

   public void applyViewRules (ChartServer chartServer, String chartType) {
      final ChartServer chartServerLocal = chartServer;
      try {

         String ruleFilePath = ruleNameFileMap.get(chartType);
         ruleService.executeRules(new IWorkingEnvironmentCallback() {

            public void initEnvironment (WorkingMemory workingMemory) {
               workingMemory.insert(chartServerLocal);
            };
         }, ruleFilePath);
      } catch (RuleException re) {
         re.printStackTrace();
      }
      chartServer = chartServerLocal;
   }

   public IViewRuleService getRuleService () {
      return ruleService;
   }

   // public void setRuleService (IViewRuleService ruleService) {
   // this.ruleService = ruleService;
   // }

   public IConfiguration getRuleConfiguration () {
      return ruleConfiguration;
   }

   public void setRuleConfiguration (IConfiguration ruleConfiguration) {
      this.ruleConfiguration = ruleConfiguration;
   }

}
