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


package com.execue.governor.configuration.impl;

import com.execue.core.configuration.IConfiguration;
import com.execue.governor.configuration.IGovernorConfigurationService;

public class GovernorConfigurationServiceImpl implements IGovernorConfigurationService {

   // private String MIN_WEIGHT_QUERY_PERCENTAGE_KEY = "governor.static-values.min-weight-query-percentage";
   // private String MIN_PRIORITY_BASED_QUERY_PERCENTAGE_KEY =
   // "governor.static-values.min-priority-based-query-percentage";
   private String              FLAG_APPLY_DEFAULTS_KEY             = "governor.static-values.apply-defaults";
   private String              DEFAULTS_RANGE_VALUE_SEPERATOR      = "governor.static-values.defaults-range-value-seperator";
   private String              DEFAULTS_MULTIPLE_VALUES_SEPERATOR  = "governor.static-values.defaults-multiple-value-seperator";
   private String              SCALING_FACTOR_CONCEPT_NAME_KEY     = "governor.scaling-factor.concept-name";
   private String              MAX_DEFAULT_METRICS_PER_QUERY       = "governor.static-values.max-default-metrics-per-query";
   private String              APPLY_SECURITY_FILTERS              = "governor.static-values.apply-security-filters";
   private static final String APPLY_MEMBER_LEVEL_SECURITY_FILTERS = "governor.static-values.apply-member-level-security-filters";

   /* Business Query Term Weights */
   private String              BQ_TERM_SELECT_WEIGHT               = "governor.business-query-term-weights.select";
   private String              BQ_TERM_GROUP_WEIGHT                = "governor.business-query-term-weights.group";
   private String              BQ_TERM_CONDITION_WEIGHT            = "governor.business-query-term-weights.condition";
   private String              BQ_TERM_HAVING_WEIGHT               = "governor.business-query-term-weights.having";
   private String              BQ_TERM_ORDER_WEIGHT                = "governor.business-query-term-weights.order";
   // private String BQ_TERM_GRAIN_WEIGHT = "governor.business-query-term-weights.grain";

   private IConfiguration      governorConfiguration;

   /**
    * @return the governorConfiguration
    */
   public IConfiguration getGovernorConfiguration () {
      return governorConfiguration;
   }

   /**
    * @param governorConfiguration
    *           the governorConfiguration to set
    */
   public void setGovernorConfiguration (IConfiguration governorConfiguration) {
      this.governorConfiguration = governorConfiguration;
   }

   @Override
   public Double getBQTermConditionWeight () {
      return getGovernorConfiguration().getDouble(BQ_TERM_CONDITION_WEIGHT);
   }

   @Override
   public Double getBQTermGroupByWeight () {
      return getGovernorConfiguration().getDouble(BQ_TERM_GROUP_WEIGHT);
   }

   @Override
   public Double getBQTermHavingWeight () {
      return getGovernorConfiguration().getDouble(BQ_TERM_HAVING_WEIGHT);
   }

   @Override
   public Double getBQTermOrderByWeight () {
      return getGovernorConfiguration().getDouble(BQ_TERM_ORDER_WEIGHT);
   }

   @Override
   public Double getBQTermSelectWeight () {
      return getGovernorConfiguration().getDouble(BQ_TERM_SELECT_WEIGHT);
   }

   @Override
   public String getDefaultMultipleValueSeprator () {
      return getGovernorConfiguration().getProperty(DEFAULTS_MULTIPLE_VALUES_SEPERATOR);
   }

   @Override
   public String getDefaultRangeValueSeprator () {
      return getGovernorConfiguration().getProperty(DEFAULTS_RANGE_VALUE_SEPERATOR);
   }

   @Override
   public Integer getMaxDefaultMetricsPerQuery () {
      return getGovernorConfiguration().getInt(MAX_DEFAULT_METRICS_PER_QUERY);
   }

   @Override
   public String getScalingFactorConceptName () {
      return getGovernorConfiguration().getProperty(SCALING_FACTOR_CONCEPT_NAME_KEY);
   }

   @Override
   public boolean isApplyDefaults () {
      return getGovernorConfiguration().getBoolean(FLAG_APPLY_DEFAULTS_KEY);
   }

   @Override
   public boolean isApplySecurityFilters () {
      return getGovernorConfiguration().getBoolean(APPLY_SECURITY_FILTERS);
   }

   @Override
   public boolean isApplyMemberLevelSecurityFilters () {
      return getGovernorConfiguration().getBoolean(APPLY_MEMBER_LEVEL_SECURITY_FILTERS);
   }

}
