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


package com.execue.qdata.test;

import java.io.InputStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.common.bean.QueryForm;
import com.execue.core.system.ExeCueSystemConfigurationUtility;
import com.execue.qdata.news.service.IAppPopularityService;
import com.execue.qdata.service.IAnswersCatalogManagementQueueService;
import com.execue.qdata.service.IMessageService;
import com.execue.qdata.service.IQueryDataService;
import com.execue.qdata.service.IUDXService;
import com.thoughtworks.xstream.XStream;

/**
 * @author kaliki
 * @since 4.0
 */
public abstract class QueryDataServiceBaseTest {

   private static ApplicationContext context;

   public ApplicationContext getContext () {
      return context;
   }

   public static void baseSetup () {
      context = new ClassPathXmlApplicationContext(new String[] {
               "/platform/bean-config/execue-logging-configuration.xml",
               "/platform/bean-config/execue-configuration.xml", "/ext/bean-config/execue-configuration-ext.xml",
               "spring-hibernate.xml", "spring-hibernate-qdata.xml", "spring-hibernate-sdata.xml",
               "/platform/bean-config/execue-dataaccess.xml", "/platform/bean-config/execue-qdata-dataaccess.xml",
               "/platform/bean-config/execue-sdata-dataaccess.xml", "/platform/bean-config/execue-qdata.xml",
               "/platform/bean-config/execue-query-builder.xml", "/platform/bean-config/execue-core.xml",
               "/platform/bean-config/execue-util-services.xml" });
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

   public IQueryDataService getQueryDataService () {
      return (IQueryDataService) context.getBean("queryDataService");
   }

   public IUDXService getUDXService () {
      return (IUDXService) context.getBean("udxService");
   }

   public IMessageService getMessageService () {
      return (IMessageService) context.getBean("messageService");
   }

   public IAppPopularityService getAppPopularityService () {
      return (IAppPopularityService) context.getBean("appPopularityService");
   }

   public IAnswersCatalogManagementQueueService getAnswersCatalogManagementQueueService () {
      return (IAnswersCatalogManagementQueueService) context.getBean("answersCatalogManagementQueueService");
   }

   public QueryForm getQueryFormFromXML (String name) {
      InputStream inputStream = this.getClass().getResourceAsStream(name);
      XStream xstream = new XStream();
      return (QueryForm) xstream.fromXML(inputStream);
   }
}
