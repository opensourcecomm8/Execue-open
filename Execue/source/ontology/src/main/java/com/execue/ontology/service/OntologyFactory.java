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


package com.execue.ontology.service;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.execue.ontology.configuration.IOntologyConfiguration;
import com.execue.ontology.configuration.OntologyConstants;
import com.execue.ontology.service.ontoModel.IOntologyModelServices;

public class OntologyFactory implements BeanFactoryAware {

   private static final Logger    logger = Logger.getLogger(OntologyFactory.class);
   private static OntologyFactory _factory;

   private IOntologyConfiguration ontologyConfigurationService;
   private BeanFactory            beanFactory;

   /**
    * Restrict access to static
    */
   private OntologyFactory () {
   }

   public static OntologyFactory getInstance () {
      if (_factory == null) {
         _factory = new OntologyFactory();
      }
      return _factory;
   }

   public IOntologyService getOntologyService () {
      String beanName = getOntologyConfigurationService().getOntologyServiceClassName();
      try {
         return (IOntologyService) beanFactory.getBean(beanName);
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public IOntologyModelServices getOntologyModelServices () {
      String beanName = getOntologyConfigurationService().getOntologyModelServiceClassName();
      try {
         return (IOntologyModelServices) beanFactory.getBean(beanName);
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public void setBeanFactory (BeanFactory arg0) throws BeansException {
      beanFactory = arg0;
   }

   public IOntologyConfiguration getOntologyConfigurationService () {
      return ontologyConfigurationService;
   }

   public void setOntologyConfigurationService (IOntologyConfiguration ontologyConfigurationService) {
      this.ontologyConfigurationService = ontologyConfigurationService;
   }
}
