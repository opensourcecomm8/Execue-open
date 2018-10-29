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


package com.execue.das;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.system.ExeCueSystemConfigurationUtility;
import com.execue.das.dataaccess.ISystemDataAccessService;
import com.execue.das.solr.dataaccess.ISolrDataAccessManager;
import com.execue.das.solr.service.ISolrManagementService;
import com.execue.das.solr.service.ISolrQueryGenerationService;
import com.execue.das.solr.service.ISolrRetrievalService;
import com.execue.dataaccess.swi.dao.IDataSourceDAO;

public class DataAccessServicesBaseTest {

   private static ApplicationContext context;

   public ApplicationContext getdataAccessContext () {
      return context;
   }

   public static void baseSetup () {
      context = new ClassPathXmlApplicationContext(new String[] {
               "/platform/bean-config/execue-logging-configuration.xml",
               "/platform/bean-config/execue-configuration.xml", "/ext/bean-config/execue-configuration-ext.xml",
               "platform/bean-config/execue-core.xml", "spring-hibernate.xml", "spring-hibernate-qdata.xml",
               "spring-hibernate-sdata.xml", 
               "/platform/bean-config/execue-query-builder.xml",
               "/platform/bean-config/execue-dataaccess.xml",
               "/platform/bean-config/execue-qdata-dataaccess.xml",
               "/platform/bean-config/execue-sdata-dataaccess.xml", 
               "platform/bean-config/execue-util-services.xml",
               "platform/bean-config/execue-dataaccess-services.xml" });
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

   public ISystemDataAccessService getSystemDataAccessService () {
      return (ISystemDataAccessService) context.getBean("systemDataAccessService");
   }

   public ISolrDataAccessManager getSolrDataAccessManager () {
      return (ISolrDataAccessManager) context.getBean("solrDataAccessManager");
   }

   public ISolrQueryGenerationService getSolrQueryGenerationService () {
      return (ISolrQueryGenerationService) context.getBean("solrQueryGenerationService");
   }

   public ISolrRetrievalService getSolrRetrievalService () {
      return (ISolrRetrievalService) context.getBean("solrRetrievalService");
   }

   public ISolrManagementService getSolrManagementService () {
      return (ISolrManagementService) context.getBean("solrManagementService");
   }
   
   public IDataSourceDAO getDataSourceDAO() {
      return (IDataSourceDAO) context.getBean("dataSourceDAO");
   }
}
