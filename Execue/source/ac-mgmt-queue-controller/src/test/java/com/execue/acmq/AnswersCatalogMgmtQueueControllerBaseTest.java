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


package com.execue.acmq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.acmq.controller.IAnswersCatalogMgmtQueueController;
import com.execue.core.system.ExeCueSystemConfigurationUtility;

/**
 * This is the base class for Junit tests related to answers catalog management queue controller module
 * 
 * @author Nitesh
 * @version 1.0
 * @since 05/04/12
 */
public abstract class AnswersCatalogMgmtQueueControllerBaseTest {

   private static ApplicationContext answersCatalogManagementQueueControllerContext;

   public static void answersCatalogManagementQueueControllerBaseSetUp () {

      answersCatalogManagementQueueControllerContext = new ClassPathXmlApplicationContext(new String[] {
               "/platform/bean-config/execue-logging-configuration.xml",
               "/platform/bean-config/execue-configuration.xml", "/ext/bean-config/execue-configuration-ext.xml",
               "spring-hibernate.xml", "spring-hibernate-qdata.xml", "platform/bean-config/execue-core.xml",
               "platform/bean-config/execue-swi.xml", "ext/bean-config/execue-swi-ext.xml",
               "platform/bean-config/execue-qdata.xml", "/platform/bean-config/execue-dataaccess.xml",
               "platform/bean-config/execue-qdata-dataaccess.xml", "platform/bean-config/execue-security.xml",
               "platform/bean-config/execue-dataaccess-services.xml", "platform/bean-config/execue-util-services.xml",
               "platform/bean-config/execue-query-generation.xml", "platform/bean-config/execue-query-builder.xml",
               "platform/bean-config/execue-job-scheduler.xml", "ext/bean-config/execue-job-scheduler-ext.xml",
               "platform/bean-config/execue-acmq-controller.xml" });
      initialize();

   }

   public IAnswersCatalogMgmtQueueController getAnswersCatalogMgmtQueueController () {
      return (IAnswersCatalogMgmtQueueController) answersCatalogManagementQueueControllerContext
               .getBean("answersCatalogMgmtQueueController");
   }

   public static void answersCatalogManagementQueueControllerBaseTearDown () {

   }

   public static void initialize () {
      try {
         ExeCueSystemConfigurationUtility.loadConfigurationServices(answersCatalogManagementQueueControllerContext);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}