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


package com.execue.ontology.configuration.impl;

import org.apache.log4j.Logger;

import com.execue.core.configuration.IConfigurable;
import com.execue.core.exception.ConfigurationException;
import com.execue.ontology.configuration.IOntologyConfiguration;

public class OntologyConfigurableService implements IConfigurable {

   private static Logger          logger = Logger.getLogger(OntologyConfigurableService.class);

   private IOntologyConfiguration ontologyConfigurationService;

   @Override
   public void doConfigure () throws ConfigurationException {
      if (logger.isDebugEnabled()) {
         logger.debug("OntologyContext loading begin");
      }
      try {

         // String ontologyFileURL = configuration.getProperty(OntologyConstants.ONTOLOGY_FILE_URL_KEY);
         //
         // ontology = loadOntology(ontologyFileURL);

         getOntologyConfigurationService().populateRegexInstances();

      } catch (Exception configurationException) {
         logger.error(configurationException);
         configurationException.printStackTrace();
      }
      if (logger.isDebugEnabled()) {
         logger.debug("OntologyContext loading end");
      }

   }

   @Override
   public void reConfigure () throws ConfigurationException {
      doConfigure();

   }

   public IOntologyConfiguration getOntologyConfigurationService () {
      return ontologyConfigurationService;
   }

   public void setOntologyConfigurationService (IOntologyConfiguration ontologyConfigurationService) {
      this.ontologyConfigurationService = ontologyConfigurationService;
   }

}
