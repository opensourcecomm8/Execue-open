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


package com.execue.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.system.ExeCueSystemConfigurationUtility;
import com.execue.util.conversion.IDynamicDateFormulaEvaluationService;

/**
 * This class represents the base test for utility services module
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/08/09
 */
public class UtilBaseTest {

   private static ApplicationContext context;

   public static void baseSetup () {
      context = new ClassPathXmlApplicationContext(new String[] { "platform/bean-config/execue-util.xml" });
      initialize();
   }

   public static void initialize () {
      try {
         ExeCueSystemConfigurationUtility.loadConfigurationServices(context);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void baseTeardown () {

   }

   public IDynamicDateFormulaEvaluationService getDynamicDateFormulaEvaluationService () {
      return (IDynamicDateFormulaEvaluationService) context.getBean("dynamicDateFormulaEvaluationService");
   }

}
