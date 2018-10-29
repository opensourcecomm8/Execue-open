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


package com.execue.driver.configuration;

import java.util.List;

import com.execue.core.common.bean.entity.VerticalEntityBasedRedirection;

public interface IDriverConfigurationService {

   /**
    * Key to get the flag to decide whether to populate the Query string into the AssetResult object
    */
   public static final String DISPLAY_QUERY_STRING_KEY = "driver.display-query-string";

   public VerticalEntityBasedRedirection getVerticalEntityBasedRedirection (Long verticalId);

   public List<VerticalEntityBasedRedirection> getAppBusinessEntityIdMap (Long appId);

   public String getUniqueAppPossiblilityRedirectionMap (Long appId);

   /** Constant property methods **/
   public boolean isGovernorQueryRepresentationRequired ();

   public Integer getAggregateQueryRetryInterval ();

   public Integer getAggregateQueryRetryCount ();

   public boolean isDisplayQueryString ();

   public String getCachedDateFormatPattern ();

   public String getContentDateFormatPattern ();

   public String getGenericUnstructuredAppRedirectUrl ();

   public String getNoSummaryTitle ();

}
