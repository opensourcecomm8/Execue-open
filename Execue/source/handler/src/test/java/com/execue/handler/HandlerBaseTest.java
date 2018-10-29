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


package com.execue.handler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.system.ExeCueSystemConfigurationUtility;
import com.execue.handler.presentation.IPresentationHandler;
import com.execue.handler.swi.IApplicationServiceHandler;
import com.execue.handler.swi.mappings.IMappingServiceHandler;

public abstract class HandlerBaseTest {

   private static ApplicationContext handlerContext;

   public ApplicationContext getHandlerContext () {
      return handlerContext;
   }

   public static void baseSetup () {
      handlerContext = new ClassPathXmlApplicationContext(new String[] { "/spring-hibernate.xml",
               "/spring-hibernate-qdata.xml", "/spring-hibernate-sdata.xml",
               "platform/bean-config/execue-job-scheduler.xml", "platform/bean-config/execue-configuration.xml",
               "platform/bean-config/execue-core.xml", "platform/bean-config/execue-logging-configuration.xml",
               "ext/bean-config/execue-configuration-ext.xml", "platform/bean-config/execue-security.xml",
               "ext/bean-config/execue-job-scheduler-ext.xml", "platform/bean-config/execue-platform-services.xml",
               "platform/bean-config/execue-publisher.xml", "platform/bean-config/execue-ontology.xml",
               "platform/bean-config/execue-answers-catalog.xml", "platform/bean-config/execue-swi.xml",
               "ext/bean-config/execue-swi-ext.xml", "platform/bean-config/execue-util.xml",
               "platform/bean-config/execue-util-services.xml", "platform/bean-config/execue-dataaccess.xml",
               "platform/bean-config/execue-dataaccess-services.xml", "platform/bean-config/execue-handler.xml",
               "platform/bean-config/execue-unstructured-wh-dataaccess.xml",
               "platform/bean-config/execue-unstructured-ca-dataaccess.xml",
               "platform/bean-config/execue-qdata-dataaccess.xml", "platform/bean-config/execue-sdata-dataaccess.xml",
               "platform/bean-config/execue-qdata.xml", "platform/bean-config/execue-query-generation.xml",
               "platform/bean-config/execue-unstructured-wh.xml", "platform/bean-config/execue-unstructured-ca.xml",
               "platform/bean-config/execue-sdata.xml", "platform/bean-config/execue-report-presentation.xml",
               "platform/bean-config/execue-query-builder.xml", "platform/bean-config/execue-unstructured-search.xml",
               "platform/bean-config/execue-acmq-controller.xml", "platform/bean-config/execue-ac-query-history.xml",
               "platform/bean-config/execue-semantic-driver.xml",
               "platform/bean-config/execue-semantic-util-services.xml",
               "platform/bean-config/execue-content-postprocessor.xml",
               "platform/bean-config/execue-content-preprocessor.xml",
               "platform/bean-config/execue-content-semantification.xml", "platform/bean-config/execue-nlp.xml",
               "ext/bean-config/execue-nlp-ext.xml", "platform/bean-config/execue-swi-handler.xml",
               "platform/bean-config/execue-query-cache.xml", "ext/bean-config/execue-report-presentation-ext.xml",
               "platform/bean-config/execue-pseudolang.xml", "ext/bean-config/execue-governor-ext.xml",
               "platform/bean-config/execue-governor.xml", "platform/bean-config/execue-driver.xml",
               "ext/bean-config/execue-driver-ext.xml", "platform/bean-config/execue-aggregation.xml",
               "ext/bean-config/execue-aggregation-ext.xml", "platform/bean-config/execue-rules.xml" });
      initialize();
   }

   public static void initialize () {
      try {
         ExeCueSystemConfigurationUtility.loadConfigurationServices(handlerContext);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public IMappingServiceHandler getMappingServiceHandler () {
      return (IMappingServiceHandler) handlerContext.getBean("mappingServiceHandler");
   }

   public IApplicationServiceHandler getApplicationServiceHandler () {
      return (IApplicationServiceHandler) handlerContext.getBean("applicationServiceHandler");
   }

   public IPresentationHandler getPresentationHandler () {
      return (IPresentationHandler) handlerContext.getBean("presentationHandler");
   }

   public static void baseTeardown () {

   }
}