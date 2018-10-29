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


package com.execue.platform.configuration.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.configuration.IConfiguration;
import com.execue.platform.configuration.IPlatformServicesConfigurationService;

public class PlatformServicesConfigurationServiceImpl implements IPlatformServicesConfigurationService {

   private static final String RANGE_LOWEST_LOWER_LIMIT                                                  = "platform-services.static-values.range-lower-limit";

   private static final String RANGE_HIGHEST_UPPER_LIMIT                                                 = "platform-services.static-values.range-upper-limit";

   private static final String RANGE_DESCRIPTION_DELIMITER                                               = "platform-services.static-values.range-description-delimiter";

   private static final String APP_SCOPING_REFRESH_INDEX_ENABLED                                         = "platform-services.static-values.app-scoping-refresh-index-enabled";

   private static final String SAMPLING_STRATEGY_ALLOWED_PROPORTION_VALUES                               = "platform-services.sampling-strategy.allowed-proportion-values";

   private static final String SAMPLING_STRATEGY_THEORTICAL_MEAN_AND_STDDEV_VARIATION_ALLOWED_PERCENTAGE = "platform-services.sampling-strategy.theortical-mean-and-stddev-variation-allowed-percentage";

   private static final String NEEDED_POPULATION_COVERAGE_PERCENTAGE_WHILE_CHECKING_FOR_BELL_CURVE       = "platform-services.sampling-strategy.needed-population-coverage-percentage-while-checking-for-bell-curve";

   private static final String MEMBER_LEVEL_ACCESS_RIGHTS_PROPAGATION_NEEDED                            = "platform-services.security.member-level-access-rights-propagation-needed";

   private IConfiguration      platformServicesConfiguration;
   private Set<Long>           locationApplicationIds                                                    = new HashSet<Long>(
                                                                                                                  1);

   @Override
   public String getRangeDescriptionDelimiter () {
      return getPlatformServicesConfiguration().getProperty(RANGE_DESCRIPTION_DELIMITER);
   }

   @Override
   public String getRangeLowerLimit () {
      return getPlatformServicesConfiguration().getProperty(RANGE_LOWEST_LOWER_LIMIT);
   }

   @Override
   public String getRangeUpperLimit () {
      return getPlatformServicesConfiguration().getProperty(RANGE_HIGHEST_UPPER_LIMIT);
   }

   /**
    * @return the platformServicesConfiguration
    */
   public IConfiguration getPlatformServicesConfiguration () {
      return platformServicesConfiguration;
   }

   @Override
   public boolean isAppScopeRefreshIndexEnabled () {
      return getPlatformServicesConfiguration().getBoolean(APP_SCOPING_REFRESH_INDEX_ENABLED);
   }

   @Override
   public boolean isApplicationHasLocationRealization (Long applicationId) {
      return locationApplicationIds.contains(applicationId);
   }

   @Override
   public List<String> getSamplingStrategyAllowedProportionValues () {
      return getPlatformServicesConfiguration().getList(SAMPLING_STRATEGY_ALLOWED_PROPORTION_VALUES);
   }

   public Integer getSamplingStrategyTheorticalMeanAndStddevVariationAllowedPercentage () {
      return getPlatformServicesConfiguration().getInt(
               SAMPLING_STRATEGY_THEORTICAL_MEAN_AND_STDDEV_VARIATION_ALLOWED_PERCENTAGE);
   }

   public Double getNeededPopulationCoveragePercentageWhileCheckingForBellCurve () {
      return getPlatformServicesConfiguration().getDouble(
               NEEDED_POPULATION_COVERAGE_PERCENTAGE_WHILE_CHECKING_FOR_BELL_CURVE);
   }

   public boolean isMemberLevelAccessRightsPropagationNeeded () {
      return getPlatformServicesConfiguration().getBoolean(
               MEMBER_LEVEL_ACCESS_RIGHTS_PROPAGATION_NEEDED);
   }
   
   /**
    * @param platformServicesConfiguration
    *           the platformServicesConfiguration to set
    */
   public void setPlatformServicesConfiguration (IConfiguration platformServicesConfiguration) {
      this.platformServicesConfiguration = platformServicesConfiguration;
   }

   public void loadLocationApplicationIds (Set<Long> locationApplicationIds) {
      this.locationApplicationIds = locationApplicationIds;
   }

}
