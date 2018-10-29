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


package com.execue.nlp.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.execue.core.ISystem;
import com.execue.core.system.ExeCueSystemConfigurationUtility;
import com.execue.nlp.configuration.INLPConfigurationService;
import com.execue.nlp.engine.INLPEngine;
import com.execue.nlp.engine.impl.SFLTermHitsUpdateHelper;
import com.execue.nlp.generator.impl.ReducedFormBusinessQueryGenerator;
import com.execue.platform.IBusinessQueryOrganizationService;

public abstract class NLPBaseTest {

   static ApplicationContext nlpContext;

   public static ApplicationContext getContext () {
      return nlpContext;
   }

   public static void baseSetup () {
      nlpContext = new ClassPathXmlApplicationContext(new String[] {
               "/platform/bean-config/execue-logging-configuration.xml",
               "/platform/bean-config/execue-configuration.xml", "/ext/bean-config/execue-configuration-ext.xml",
               "spring-hibernate.xml", "spring-hibernate-qdata.xml", "spring-hibernate-sdata.xml",
               "platform/bean-config/execue-core.xml", "platform/bean-config/execue-swi.xml",
               "ext/bean-config/execue-swi-ext.xml", "/platform/bean-config/execue-dataaccess.xml",
               "platform/bean-config/execue-unstructured-wh-dataaccess.xml",
               "platform/bean-config/execue-unstructured-wh.xml", "platform/bean-config/execue-security.xml",
               "platform/bean-config/execue-qdata.xml", "platform/bean-config/execue-dataaccess-services.xml",
               "platform/bean-config/execue-util-services.xml", "platform/bean-config/execue-platform-services.xml",
               "platform/bean-config/execue-sdata.xml", "platform/bean-config/execue-query-generation.xml",
               "platform/bean-config/execue-util.xml", "platform/bean-config/execue-qdata-dataaccess.xml",
               "platform/bean-config/execue-sdata-dataaccess.xml",
               "platform/bean-config/execue-semantic-util-services.xml", "platform/bean-config/execue-nlp.xml",
               "ext/bean-config/execue-nlp-ext.xml", "platform/bean-config/execue-ontology.xml",
               "platform/bean-config/execue-query-builder.xml",
               "platform/bean-config/execue-unstructured-ca-dataaccess.xml",
               "platform/bean-config/execue-unstructured-ca.xml" });
      initialize();
   }

   public static void baseTeardown () {
   }

   public static void initialize () {
      try {
         ExeCueSystemConfigurationUtility.loadConfigurationServices(nlpContext);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public ISystem getExecueSystem () {
      return (ISystem) nlpContext.getBean("execueSystem");
   }

   public INLPConfigurationService getConfigurationService () {
      return (INLPConfigurationService) nlpContext.getBean("nlpConfigurationService");
   }

   public INLPEngine getNLPEngine () {
      return (INLPEngine) nlpContext.getBean("nlpEngine");
   }

   public SFLTermHitsUpdateHelper getSFLTermsHitsUpdateHelper () {
      return (SFLTermHitsUpdateHelper) nlpContext.getBean("sfTermHitsUpdateHelper");
   }

   public ReducedFormBusinessQueryGenerator getReducedFormBusinessQueryGenerator () {
      return (ReducedFormBusinessQueryGenerator) nlpContext.getBean("reducedFormBusinessQueryGenerator");
   }

   public IBusinessQueryOrganizationService getBusinessQueryOrganizationService () {
      return (IBusinessQueryOrganizationService) nlpContext.getBean("businessQueryOrganizationService");
   }

   public HibernateTemplate getHibernateTemplate () {
      return (HibernateTemplate) nlpContext.getBean("hibernateTemplate");
   }
}