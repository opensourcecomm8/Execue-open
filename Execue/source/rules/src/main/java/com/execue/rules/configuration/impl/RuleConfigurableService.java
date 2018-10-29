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

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.compiler.PackageBuilder;

import com.execue.core.configuration.IConfigurable;
import com.execue.core.exception.ConfigurationException;

/**
 * This class define rule configurable service, primary objective of this class is to <BR>
 * 1. Have doConfigure method <BR>
 * 2. Populate rule configuration service's map other properties by hitting database and from configuration if required.
 * 
 * @author Jitendra
 * @version 1.0
 * @since 20/10/11
 */
public class RuleConfigurableService implements IConfigurable {

   private static final Logger          logger = Logger.getLogger(RuleConfigurableService.class);
   private RuleConfigurationServiceImpl ruleConfigurationService;

   @Override
   public void doConfigure () throws ConfigurationException {
      long startTime = System.currentTimeMillis();
      List<String> ruleNames = getRuleConfigurationService().getRuleNames();
      List<String> filePath = getRuleConfigurationService().getFilePaths();
      Map<String, RuleBase> rulesMap = new HashMap<String, RuleBase>();
      for (int index = 0; index < ruleNames.size(); index++) {
         try {
            // Read in the rules source file
            Reader source = new InputStreamReader(this.getClass().getResourceAsStream(filePath.get(index)));
            // Use package builder to build up a rule package
            PackageBuilder builder = new PackageBuilder();
            // This parses and compiles in one step
            builder.addPackageFromDrl(source);
            // Get the compiled package
            org.drools.rule.Package pkg = builder.getPackage();
            // Add the package to a rule-base (deploy the rule package).
            RuleBase ruleBase = RuleBaseFactory.newRuleBase();
            ruleBase.addPackage(pkg);
            rulesMap.put(ruleNames.get(index), ruleBase);
         } catch (Error e) {
            logger.error("\nError Occured while loading the rule file: " + filePath.get(index));
            e.printStackTrace();
            // throw new ConfigurationException(10, e);
         } catch (Exception e) {
            logger.error("\nException Occured while loading the rule file: " + filePath.get(index));
            e.printStackTrace();
         }
      }
      // load rules map
      getRuleConfigurationService().loadRulesMap(rulesMap);

      long endTime = System.currentTimeMillis();
      logger.debug("Time taken to load " + ruleNames.size() + " rules : " + (endTime - startTime));
   }

   @Override
   public void reConfigure () throws ConfigurationException {
      doConfigure();
   }

   /**
    * @return the ruleConfigurationService
    */
   public RuleConfigurationServiceImpl getRuleConfigurationService () {
      return ruleConfigurationService;
   }

   /**
    * @param ruleConfigurationService
    *           the ruleConfigurationService to set
    */
   public void setRuleConfigurationService (RuleConfigurationServiceImpl ruleConfigurationService) {
      this.ruleConfigurationService = ruleConfigurationService;
   }
}
