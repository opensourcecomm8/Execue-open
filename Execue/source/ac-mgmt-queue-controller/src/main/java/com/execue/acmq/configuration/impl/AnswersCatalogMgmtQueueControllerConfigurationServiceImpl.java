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
import com.execue.core.configuration.IConfiguration;

/**
 * @author Nitesh
 *
 */
public class AnswersCatalogMgmtQueueControllerConfigurationServiceImpl implements
         IAnswersCatalogMgmtQueueControllerConfigurationService {

   private IConfiguration      answersCatalogMgmtQueueControllerConfiguration;

   private static final String MAX_ACMQ_IN_PROGRESS_COUNT = "acmq.max-acmq-in-progress-count";

   public Integer getMaxACMQInProgressCount () {
      return getAnswersCatalogMgmtQueueControllerConfiguration().getInt(MAX_ACMQ_IN_PROGRESS_COUNT);
   }

   /**
    * @return the answersCatalogMgmtQueueControllerConfiguration
    */
   public IConfiguration getAnswersCatalogMgmtQueueControllerConfiguration () {
      return answersCatalogMgmtQueueControllerConfiguration;
   }

   /**
    * @param answersCatalogMgmtQueueControllerConfiguration the answersCatalogMgmtQueueControllerConfiguration to set
    */
   public void setAnswersCatalogMgmtQueueControllerConfiguration (
            IConfiguration answersCatalogMgmtQueueControllerConfiguration) {
      this.answersCatalogMgmtQueueControllerConfiguration = answersCatalogMgmtQueueControllerConfiguration;
   }

}
