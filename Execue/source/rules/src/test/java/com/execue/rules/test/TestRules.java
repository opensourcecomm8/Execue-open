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


package com.execue.rules.test;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.ISystem;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.type.SystemType;
import com.execue.rules.bean.RuleParameters;
import com.execue.rules.service.IRuleService;

public class TestRules {

   static Logger      logger = Logger.getLogger(TestRules.class);
   ApplicationContext ctx    = null;

   public TestRules () {
   }

   @BeforeClass
   public static void setUpClass () throws Exception {
   }

   @AfterClass
   public static void tearDownClass () throws Exception {
   }

   @Before
   public void setUp () throws Exception {
      ctx = new ClassPathXmlApplicationContext("applicationContext-core.xml");
      logger.debug(ctx.getClassLoader().getClass());
      logger.debug(getClass().getClassLoader().getClass());
      ISystem system = (ISystem) ctx.getBean("execueSystem");
      system.initialize(SystemType.WEBAPP);
      ICoreConfigurationService coreConfigurationService = (ICoreConfigurationService) ctx
               .getBean("coreConfigurationService");
      logger.debug("Log enabled " + coreConfigurationService.isSystemLogEnabled());
   }

   @After
   public void tearDown () {
   }

   @Test
   public void sysConfigTest () throws Exception {
      long startTime = System.currentTimeMillis();
      ICoreConfigurationService coreConfigurationService = (ICoreConfigurationService) ctx
               .getBean("coreConfigurationService");
      long endTime = System.currentTimeMillis();
      logger.debug("sysConfigTest time " + (long) (endTime - startTime));

   }

   @Test
   public void ruleTest () throws Exception {
      long startTime = System.currentTimeMillis();
      IRuleService ruleService = (IRuleService) ctx.getBean("ruleService");
      ICoreConfigurationService coreConfigurationService = (ICoreConfigurationService) ctx
               .getBean("coreConfigurationService");
      RuleParameters parameters = new RuleParameters();
      parameters.addInput(coreConfigurationService);
      ruleService.executeRule("test", parameters);
      long endTime = System.currentTimeMillis();
      logger.debug("rulTest time " + (long) (endTime - startTime));
   }
}
