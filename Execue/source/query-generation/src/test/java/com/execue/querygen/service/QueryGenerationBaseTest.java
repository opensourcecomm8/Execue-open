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


package com.execue.querygen.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.system.ExeCueSystemConfigurationUtility;

public class QueryGenerationBaseTest {

   private static ApplicationContext queryGenerationContext;

   public ApplicationContext getdataAccessContext () {
      return queryGenerationContext;
   }

   public static void queryGenerationBaseSetUp () {
      queryGenerationContext = new ClassPathXmlApplicationContext(new String[] {
               "/platform/bean-config/execue-logging-configuration.xml",
               "/platform/bean-config/execue-configuration.xml", "/ext/bean-config/execue-configuration-ext.xml",
               "spring-hibernate.xml", "spring-hibernate-qdata.xml", "spring-hibernate-sdata.xml",
               "/platform/bean-config/execue-dataaccess.xml", "/platform/bean-config/execue-qdata-dataaccess.xml",
               "/platform/bean-config/execue-sdata-dataaccess.xml", "platform/bean-config/execue-swi.xml",
               "ext/bean-config/execue-swi-ext.xml", "platform/bean-config/execue-query-generation.xml",
               "platform/bean-config/execue-core.xml" });
      initialize();
   }

   public static void queryGenerationBaseTearDown () {

   }

   public static void initialize () {
      try {
         ExeCueSystemConfigurationUtility.loadConfigurationServices(queryGenerationContext);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public QueryGenerationServiceFactory getQueryGenerationServiceFactory () {
      return (QueryGenerationServiceFactory) queryGenerationContext.getBean("queryGenerationServiceFactory");
   }

   public IQueryGenerationService getQueryGenerationService () {
      return (IQueryGenerationService) queryGenerationContext.getBean("mySqlQueryGenerationService");
   }
}