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


package com.execue.platform.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.system.ExeCueSystemConfigurationUtility;
import com.execue.platform.swi.IAssetActivationService;
import com.execue.platform.unstructured.IUnstructuredPlatformManagementService;

public class PlatformBaseTest {

   private static ApplicationContext context;

   public static void baseTearDown () {

   }

   public static void baseSetup () {
      context = new ClassPathXmlApplicationContext(new String[] {
               "/platform/bean-config/execue-logging-configuration.xml",
               "/platform/bean-config/execue-configuration.xml", "/ext/bean-config/execue-configuration-ext.xml",
               "spring-hibernate.xml", "spring-hibernate-qdata.xml", "spring-hibernate-sdata.xml", "spring-hibernate-audittrail.xml",
               "/platform/bean-config/execue-dataaccess.xml", "/platform/bean-config/execue-qdata-dataaccess.xml",
               "/platform/bean-config/execue-sdata-dataaccess.xml", "/platform/bean-config/execue-swi.xml",
               "platform/bean-config/execue-qdata.xml", "platform/bean-config/execue-sdata.xml",
               "/platform/bean-config/execue-core.xml", "ext/bean-config/execue-swi-ext.xml",
               "/platform/bean-config/execue-audit-trail.xml", "/platform/bean-config/execue-audit-trail-dataaccess.xml", 
               "platform/bean-config/execue-util.xml", "platform/bean-config/execue-util-services.xml",
               "platform/bean-config/execue-query-generation.xml", "platform/bean-config/execue-security.xml",
               "platform/bean-config/execue-platform-services.xml",
               "platform/bean-config/execue-dataaccess-services.xml",
               "platform/bean-config/execue-unstructured-wh-dataaccess.xml",
               "platform/bean-config/execue-unstructured-wh.xml",
               "platform/bean-config/execue-unstructured-ca-dataaccess.xml",
               "platform/bean-config/execue-unstructured-ca.xml", "platform/bean-config/execue-query-builder.xml" });
      initialize();
   }

   public static void baseTeardown () {

   }

   public static void initialize () {
      try {
         ExeCueSystemConfigurationUtility.loadConfigurationServices(context);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public IAssetActivationService getAssetActivationService () {
      return (IAssetActivationService) context.getBean("assetActivationServiceImpl");
   }

   public IUnstructuredPlatformManagementService getUnstructuredPlatformManagementService () {
      return (IUnstructuredPlatformManagementService) context.getBean("unstructuredPlatformManagementService");
   }

   public static ApplicationContext getContext () {
      return context;
   }

   public static void setContext (ApplicationContext context) {
      PlatformBaseTest.context = context;
   }

}
