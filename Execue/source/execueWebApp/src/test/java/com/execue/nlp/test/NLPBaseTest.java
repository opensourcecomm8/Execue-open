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
import com.execue.core.common.api.nlp.INLPEngine;
import com.execue.core.common.api.querygen.IBusinessQueryOrganizationService;
import com.execue.core.system.ExecueSystem;
import com.execue.nlp.configuration.INLPConfiguration;
import com.execue.nlp.engine.impl.SFLTermHitsUpdateHelper;
import com.execue.nlp.generator.ReducedFormBusinessQueryGenerator;

public abstract class NLPBaseTest {

   static ApplicationContext nlpContext;

   public static ApplicationContext getContext () {
      return nlpContext;
   }

   public static void baseSetup () {
      nlpContext = new ClassPathXmlApplicationContext(new String[] { "/bean-config/execue-application.xml" });

      try {
         ((ExecueSystem) getContext().getBean("execueSystem")).initialize();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void baseTeardown () {
   }

   public ISystem getExecueSystem () {
      return (ISystem) nlpContext.getBean("execueSystem");
   }

   public INLPConfiguration getConfigurationService () {
      return (INLPConfiguration) nlpContext.getBean("nlpConfigurationService");
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