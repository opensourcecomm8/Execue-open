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


package com.execue.driver.configuration.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.VerticalEntityBasedRedirection;
import com.execue.core.configuration.IConfiguration;
import com.execue.driver.configuration.IDriverConfigurationService;

public class DriverConfigurationServiceImpl implements IDriverConfigurationService {

   private static final String                             GOVERNOR_QUERY_REPRESENTATION_REQUIRED_KEY = "driver.flags.governor-query-representation-required";

   /**
    * Key to obtain the retry interval for retrieving the aggregate query by id
    */
   private static final String                             AGGREGATE_QUERY_RETRY_INTERVAL_KEY         = "driver.aggregate-query-retry-interval";

   /**
    * Key to obtain the format pattern for displaying the cached date
    */
   private static final String                             CACHED_DATE_FORMAT_PATTERN                 = "driver.cached-date-format-pattern";
   /**
    * Key to obtain the format pattern for displaying the content date
    */
   private static final String                             CONTENT_DATE_FORMAT_PATTERN                = "driver.content-date-format-pattern";

   private static final String                             AGGREGATE_QUERY_RETRY_COUNT_KEY            = "driver.aggregate-query-retry-count";
   private static final String                             UNIQUE_APP_POSSIBILITY_REDIRECTION         = "driver.unique-app-possibility-redirection";
   private static final String                             GENERIC_UNSTRUCTURED_APP_REDIRECTION_URL   = "driver.generic-unstructured-app-redirect-url";

   private static final String                             NO_SUMMARY_RESULT_TITLE                    = "driver.no-summary-result-title";

   private IConfiguration                                  driverConfiguration;
   private IConfiguration                                  driverDBConfiguration;
   private Map<Long, VerticalEntityBasedRedirection>       verticalBasedRedirectionMap                = new HashMap<Long, VerticalEntityBasedRedirection>();       ;
   private Map<Long, List<VerticalEntityBasedRedirection>> appBusinessEntityIdMap                     = new HashMap<Long, List<VerticalEntityBasedRedirection>>(); ;
   private Map<Long, String>                               uniqueAppPossiblilityRedirectionMap        = new HashMap<Long, String>();

   public void loadUniqueAppPossiblilityRedirectionMap () {
      List<String> appIdList = getDriverConfiguration().getList(UNIQUE_APP_POSSIBILITY_REDIRECTION + "." + "app.id");
      List<String> appRedirectUrls = getDriverConfiguration().getList(
               UNIQUE_APP_POSSIBILITY_REDIRECTION + "." + "app.redirect-url");
      int size = 0;
      size = appIdList.size();
      if (size > 0) {
         for (int i = 0; i < size; i++) {

            Long id = Long.valueOf(appIdList.get(i));
            String redirectUrl = appRedirectUrls.get(i);
            uniqueAppPossiblilityRedirectionMap.put(id, redirectUrl);

         }

      }
   }

   public void loadVerticalBasedRedirectionMap (Map<Long, VerticalEntityBasedRedirection> verticalBasedRedirectionMap) {
      this.verticalBasedRedirectionMap = verticalBasedRedirectionMap;
   }

   public void loadAppBusinessEntityIdMap (Map<Long, List<VerticalEntityBasedRedirection>> appBusinessEntityIdMap) {
      this.appBusinessEntityIdMap = appBusinessEntityIdMap;
   }

   @Override
   public List<VerticalEntityBasedRedirection> getAppBusinessEntityIdMap (Long appId) {
      return appBusinessEntityIdMap.get(appId);
   }

   @Override
   public String getUniqueAppPossiblilityRedirectionMap (Long appId) {
      return uniqueAppPossiblilityRedirectionMap.get(appId);
   }

   @Override
   public VerticalEntityBasedRedirection getVerticalEntityBasedRedirection (Long verticalId) {
      return verticalBasedRedirectionMap.get(verticalId);
   }

   @Override
   public Integer getAggregateQueryRetryCount () {
      return getDriverConfiguration().getInt(AGGREGATE_QUERY_RETRY_COUNT_KEY);
   }

   @Override
   public Integer getAggregateQueryRetryInterval () {
      return getDriverConfiguration().getInt(AGGREGATE_QUERY_RETRY_INTERVAL_KEY);
   }

   @Override
   public String getCachedDateFormatPattern () {
      return getDriverConfiguration().getProperty(CACHED_DATE_FORMAT_PATTERN);
   }

   @Override
   public String getContentDateFormatPattern () {
      return getDriverConfiguration().getProperty(CONTENT_DATE_FORMAT_PATTERN);
   }

   @Override
   public boolean isDisplayQueryString () {
      return getDriverDBConfiguration().getBoolean(DISPLAY_QUERY_STRING_KEY);
   }

   @Override
   public boolean isGovernorQueryRepresentationRequired () {
      return getDriverDBConfiguration().getBoolean(GOVERNOR_QUERY_REPRESENTATION_REQUIRED_KEY);
   }

   @Override
   public String getGenericUnstructuredAppRedirectUrl () {
      return getDriverConfiguration().getProperty(GENERIC_UNSTRUCTURED_APP_REDIRECTION_URL);
   }

   @Override
   public String getNoSummaryTitle () {
      return getDriverConfiguration().getProperty(NO_SUMMARY_RESULT_TITLE);
   }

   /**
    * @return the driverConfiguration
    */
   public IConfiguration getDriverConfiguration () {
      return driverConfiguration;
   }

   /**
    * @param driverConfiguration
    *           the driverConfiguration to set
    */
   public void setDriverConfiguration (IConfiguration driverConfiguration) {
      this.driverConfiguration = driverConfiguration;
   }

   public IConfiguration getDriverDBConfiguration () {
      return driverDBConfiguration;
   }

   public void setDriverDBConfiguration (IConfiguration driverDBConfiguration) {
      this.driverDBConfiguration = driverDBConfiguration;
   }

}