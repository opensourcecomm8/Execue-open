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


package com.execue.uswhda.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.system.ExeCueSystemConfigurationUtility;
import com.execue.uswhda.configuration.IUnstructuredWHDataAccessConfigurationService;
import com.execue.uswhda.configuration.impl.UnstructuredWHDataAccessConfigurableService;
import com.execue.uswhda.dataaccess.dao.IFeatureDAO;
import com.execue.uswhda.dataaccess.dao.IFeatureDetailDAO;
import com.execue.uswhda.dataaccess.dao.IFeatureRangeDAO;
import com.execue.uswhda.dataaccess.dao.IFeatureValueDAO;
import com.execue.uswhda.dataaccess.dao.ISemantifiedContentDAO;
import com.execue.uswhda.dataaccess.dao.IUnstructuredRIFeatureContentDAO;

public class UnstructuredWarehouseBaseTest {

   private static ApplicationContext unstructuredWHContext;

   public ApplicationContext getdataAccessContext () {
      return unstructuredWHContext;
   }

   public static void baseSetup () {
      unstructuredWHContext = new ClassPathXmlApplicationContext(new String[] {
               "/platform/bean-config/execue-logging-configuration.xml",
               "/platform/bean-config/execue-configuration.xml", "/ext/bean-config/execue-configuration-ext.xml",
               "platform/bean-config/execue-core.xml", "spring-hibernate.xml", "spring-hibernate-qdata.xml",
               "spring-hibernate-sdata.xml", "platform/bean-config/execue-util.xml",
               "/platform/bean-config/execue-dataaccess.xml", "/platform/bean-config/execue-qdata-dataaccess.xml",
               "/platform/bean-config/execue-sdata-dataaccess.xml",
               "platform/bean-config/execue-unstructured-wh-dataaccess.xml",
               "platform/bean-config/execue-query-builder.xml" });
      initialize();
   }

   public static void initialize () {
      try {
         ExeCueSystemConfigurationUtility.loadConfigurationServices(unstructuredWHContext);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void baseTeardown () {

   }

   public static UnstructuredWHDataAccessConfigurableService getUnstructuredWHDataAccessConfigurableService () {
      return (UnstructuredWHDataAccessConfigurableService) unstructuredWHContext
               .getBean("unstructuredWHDataAccessConfigurableService");
   }

   public static IUnstructuredWHDataAccessConfigurationService getUnstructuredWHDataAccessConfigurationService () {
      return (IUnstructuredWHDataAccessConfigurationService) unstructuredWHContext
               .getBean("unstructuredWHDataAccessConfigurationService");
   }

   public static IUnstructuredRIFeatureContentDAO getUnstructuredRIFeatureContentDAO () {
      return (IUnstructuredRIFeatureContentDAO) unstructuredWHContext.getBean("unstructuredRIFeatureContentDAO");
   }

   public IFeatureDAO getFeatureDAO () {
      return (IFeatureDAO) unstructuredWHContext.getBean("featureDAO");
   }

   public IFeatureRangeDAO getFeatureRangeDAO () {
      return (IFeatureRangeDAO) unstructuredWHContext.getBean("featureRangeDAO");
   }

   public IFeatureValueDAO getFeatureValueDAO () {
      return (IFeatureValueDAO) unstructuredWHContext.getBean("featureValueDAO");
   }

   public ISemantifiedContentDAO getSemantifiedContentDAO () {
      return (ISemantifiedContentDAO) unstructuredWHContext.getBean("semantifiedContentDAO");
   }

   public IFeatureDetailDAO getFeatureDetailDAO () {
      return (IFeatureDetailDAO) unstructuredWHContext.getBean("featureDetailDAO");
   }

}
