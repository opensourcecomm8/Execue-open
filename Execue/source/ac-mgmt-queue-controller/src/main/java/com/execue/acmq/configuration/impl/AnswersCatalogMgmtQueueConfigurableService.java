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


/**
 * 
 */
package com.execue.acmq.configuration.impl;

import com.execue.acmq.configuration.IAnswersCatalogMgmtQueueControllerConfigurationService;
import com.execue.core.configuration.IConfigurable;
import com.execue.core.exception.ConfigurationException;

/**
 * @author Nitesh
 *
 */
public class AnswersCatalogMgmtQueueConfigurableService implements IConfigurable {

   private IAnswersCatalogMgmtQueueControllerConfigurationService answersCatalogMgmtQueueControllerConfigurationService;

   @Override
   public void doConfigure () throws ConfigurationException {

   }

   @Override
   public void reConfigure () throws ConfigurationException {
      doConfigure();
   }

   /**
    * @return the answersCatalogMgmtQueueControllerConfigurationService
    */
   public IAnswersCatalogMgmtQueueControllerConfigurationService getAnswersCatalogMgmtQueueControllerConfigurationService () {
      return answersCatalogMgmtQueueControllerConfigurationService;
   }

   /**
    * @param answersCatalogMgmtQueueControllerConfigurationService the answersCatalogMgmtQueueControllerConfigurationService to set
    */
   public void setAnswersCatalogMgmtQueueControllerConfigurationService (
            IAnswersCatalogMgmtQueueControllerConfigurationService answersCatalogMgmtQueueControllerConfigurationService) {
      this.answersCatalogMgmtQueueControllerConfigurationService = answersCatalogMgmtQueueControllerConfigurationService;
   }

}
