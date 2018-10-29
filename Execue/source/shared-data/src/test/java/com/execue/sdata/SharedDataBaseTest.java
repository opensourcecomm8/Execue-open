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


package com.execue.sdata;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.system.ExeCueSystemConfigurationUtility;
import com.execue.sdata.service.ILocationRetrievalService;

/**
 * This is the base class for Junit tests related to Shared Data module.
 * 
 * @author MurthySN
 * @version 1.0
 * @since 27/09/11
 */
public abstract class SharedDataBaseTest {

   private static ApplicationContext context;

   public ApplicationContext getdataAccessContext () {
      return context;
   }

   public static void baseSetup () {

      context = new ClassPathXmlApplicationContext(new String[] {
               "/platform/bean-config/execue-logging-configuration.xml",
               "/platform/bean-config/execue-configuration.xml", "/ext/bean-config/execue-configuration-ext.xml",
               "spring-hibernate.xml", "spring-hibernate-qdata.xml", "spring-hibernate-sdata.xml",
               "/platform/bean-config/execue-dataaccess.xml", "/platform/bean-config/execue-qdata-dataaccess.xml",
               "/platform/bean-config/execue-sdata-dataaccess.xml", "/platform/bean-config/execue-sdata.xml",
               "/platform/bean-config/execue-query-builder.xml" });
      initialize();
   }

   public static void baseTeardown () {

   }

   public ILocationRetrievalService getLocationRetrievalService () {
      return (ILocationRetrievalService) context.getBean("locationRetrievalService");
   }

   public static void initialize () {
      try {
         ExeCueSystemConfigurationUtility.loadConfigurationServices(context);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
