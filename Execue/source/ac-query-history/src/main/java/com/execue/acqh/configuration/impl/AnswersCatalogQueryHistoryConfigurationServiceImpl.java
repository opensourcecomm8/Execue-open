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


package com.execue.acqh.configuration.impl;

import com.execue.acqh.configuration.IAnswersCatalogQueryHistoryConfigurationService;
import com.execue.core.configuration.IConfiguration;

/**
 * @author Raju Gottumukkala
 */
public class AnswersCatalogQueryHistoryConfigurationServiceImpl implements
         IAnswersCatalogQueryHistoryConfigurationService {

   private IConfiguration      answersCatalogQueryHistoryConfiguration;
   private static final String QUERY_HISTORY_FILE_STORAGE_BED_DETAILS_PATH_KEY = "ac-query-history.static-values.query-history-file-storage-path.bed-details";
   private static final String QUERY_HISTORY_FILE_STORAGE_PAST_USAGE_PATH_KEY  = "ac-query-history.static-values.query-history-file-storage-path.past-usage";

   public IConfiguration getAnswersCatalogQueryHistoryConfiguration () {
      return answersCatalogQueryHistoryConfiguration;
   }

   public void setAnswersCatalogQueryHistoryConfiguration (IConfiguration answersCatalogQueryHistoryConfiguration) {
      this.answersCatalogQueryHistoryConfiguration = answersCatalogQueryHistoryConfiguration;
   }

   public String getQueryHistoryFileStorageBedDetailsPath () {
      return getAnswersCatalogQueryHistoryConfiguration().getProperty(QUERY_HISTORY_FILE_STORAGE_BED_DETAILS_PATH_KEY);
   }

   public String getQueryHistoryFileStoragePastUsagePath () {
      return getAnswersCatalogQueryHistoryConfiguration().getProperty(QUERY_HISTORY_FILE_STORAGE_PAST_USAGE_PATH_KEY);
   }

}
