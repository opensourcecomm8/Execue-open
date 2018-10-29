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


package com.execue.report.view.rule.service.drools;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.PackageBuilder;
import org.drools.rule.Package;

import com.execue.report.view.rule.service.IViewRuleService;
import com.execue.report.view.rule.service.IWorkingEnvironmentCallback;
import com.execue.rules.exception.RuleException;

public class DroolsViewRulesService implements IViewRuleService {

   private static final Logger log = Logger.getLogger(DroolsViewRulesService.class);

   public RuleBase initializeRuleBase (String ruleFilePath) {
      RuleBase ruleBase = null;
      long startTime = System.currentTimeMillis();
      try {
         // Read in the rules source file
         InputStreamReader inputStreamReader = new InputStreamReader(this.getClass().getResourceAsStream(ruleFilePath));
         BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

         // Use package builder to build up a rule package
         PackageBuilder builder = new PackageBuilder();

         // This will parse and compile in one step
         builder.addPackageFromDrl(bufferedReader);

         // Get the compiled package
         Package pkg = builder.getPackage();

         // Add the package to a rulebase (deploy the rule package).
         ruleBase = RuleBaseFactory.newRuleBase();
         ruleBase.addPackage(pkg);
      } catch (Exception e) {
         e.printStackTrace();
      }
      long endTime = System.currentTimeMillis();
      if (log.isInfoEnabled()) {
         log.info("execution time(MS) " + (long) (endTime - startTime));
      }
      return ruleBase;
   }

   public boolean executeRules (IWorkingEnvironmentCallback callBack, String ruleFilePath) throws RuleException {
      RuleBase ruleBase = initializeRuleBase(ruleFilePath);
      WorkingMemory workingMemory = ruleBase.newStatefulSession();
      callBack.initEnvironment(workingMemory);
      workingMemory.fireAllRules();
      return true;
   }

}
