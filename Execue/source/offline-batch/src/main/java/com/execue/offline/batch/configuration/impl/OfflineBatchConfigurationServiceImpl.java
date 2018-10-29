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


package com.execue.offline.batch.configuration.impl;

import com.execue.core.configuration.IConfiguration;
import com.execue.offline.batch.configuration.IOfflineBatchConfigurationService;

public class OfflineBatchConfigurationServiceImpl implements IOfflineBatchConfigurationService {

   private static final String LOCATION_DATA_INPUT_SOURCE_CENSUS_DRIVER_CLASS_NAME_KEY    = "offline-batch.location-data-input-source.census.connection.driver-class-name";   //"com.mysql.jdbc.Driver";
   private static final String LOCATION_DATA_INPUT_SOURCE_CENSUS_URL_KEY                  = "offline-batch.location-data-input-source.census.connection.url";                 //"jdbc:mysql://localhost:3306/wh-census-csz";
   private static final String LOCATION_DATA_INPUT_SOURCE_CENSUS_USERNAME_KEY             = "offline-batch.location-data-input-source.census.connection.username";            //"root";
   private static final String LOCATION_DATA_INPUT_SOURCE_CENSUS_PASSWORD_KEY             = "offline-batch.location-data-input-source.census.connection.password";            //"power1";
   private static final String LOCATION_DATA_INPUT_SOURCE_CENSUS_QUERY_KEY                = "offline-batch.location-data-input-source.census.select-query";                          //"select * from zip_city_state order by zip asc limit ";

   private static final String LOCATION_DATA_INPUT_SOURCE_ZIPCODEDB_DRIVER_CLASS_NAME_KEY = "offline-batch.location-data-input-source.zipcodedb.connection.driver-class-name"; //"com.mysql.jdbc.Driver";
   private static final String LOCATION_DATA_INPUT_SOURCE_ZIPCODEDB_URL_KEY               = "offline-batch.location-data-input-source.zipcodedb.connection.url";              //"jdbc:mysql://localhost:3306/wh-census-csz";
   private static final String LOCATION_DATA_INPUT_SOURCE_ZIPCODEDB_USERNAME_KEY          = "offline-batch.location-data-input-source.zipcodedb.connection.username";         //"root";
   private static final String LOCATION_DATA_INPUT_SOURCE_ZIPCODEDB_PASSWORD_KEY          = "offline-batch.location-data-input-source.zipcodedb.connection.password";         //"power1";
   private static final String LOCATION_DATA_INPUT_SOURCE_ZIPCODEDB_QUERY_KEY             = "offline-batch.location-data-input-source.zipcodedb.select-query";                       //"select * from zip_city_state order by zip asc limit ";

   private static final String LOCATION_DATA_INPUT_SOURCE_DEFAULT_COUNTRY_NAME_KEY        = "offline-batch.location-data-input-source.default-country-name";                  //"USA";
   private static final String LOCATION_DATA_INPUT_SOURCE_BATCH_SIZE_KEY                  = "offline-batch.location-data-input-source.batch-size";                            //10000;

   private IConfiguration      offlineBatchConfiguration;

   public IConfiguration getOfflineBatchConfiguration () {
      return offlineBatchConfiguration;
   }

   public void setOfflineBatchConfiguration (IConfiguration offlineBatchConfiguration) {
      this.offlineBatchConfiguration = offlineBatchConfiguration;
   }

   public String getLocationInputDataSourceCensusConnectionDriverClassName () {
      return getOfflineBatchConfiguration().getProperty(LOCATION_DATA_INPUT_SOURCE_CENSUS_DRIVER_CLASS_NAME_KEY);
   }

   public String getLocationInputDataSourceCensusConnectionURL () {
      return getOfflineBatchConfiguration().getProperty(LOCATION_DATA_INPUT_SOURCE_CENSUS_URL_KEY);
   }

   public String getLocationInputDataSourceCensusConnectionUsername () {
      return getOfflineBatchConfiguration().getProperty(LOCATION_DATA_INPUT_SOURCE_CENSUS_USERNAME_KEY);
   }

   public String getLocationInputDataSourceCensusConnectionPassword () {
      return getOfflineBatchConfiguration().getProperty(LOCATION_DATA_INPUT_SOURCE_CENSUS_PASSWORD_KEY);
   }

   public String getLocationInputDataSourceCensusSelectQuery () {
      return getOfflineBatchConfiguration().getProperty(LOCATION_DATA_INPUT_SOURCE_CENSUS_QUERY_KEY);
   }

   public String getLocationInputDataSourceZipCodeDBConnectionDriverClassName () {
      return getOfflineBatchConfiguration().getProperty(LOCATION_DATA_INPUT_SOURCE_ZIPCODEDB_DRIVER_CLASS_NAME_KEY);
   }

   public String getLocationInputDataSourceZipCodeDBConnectionURL () {
      return getOfflineBatchConfiguration().getProperty(LOCATION_DATA_INPUT_SOURCE_ZIPCODEDB_URL_KEY);
   }

   public String getLocationInputDataSourceZipCodeDBConnectionUsername () {
      return getOfflineBatchConfiguration().getProperty(LOCATION_DATA_INPUT_SOURCE_ZIPCODEDB_USERNAME_KEY);
   }

   public String getLocationInputDataSourceZipCodeDBConnectionPassword () {
      return getOfflineBatchConfiguration().getProperty(LOCATION_DATA_INPUT_SOURCE_ZIPCODEDB_PASSWORD_KEY);
   }

   public String getLocationInputDataSourceZipCodeDBSelectQuery () {
      return getOfflineBatchConfiguration().getProperty(LOCATION_DATA_INPUT_SOURCE_ZIPCODEDB_QUERY_KEY);
   }

   public String getLocationInputDataSourceDefaultCountryName () {
      return getOfflineBatchConfiguration().getProperty(LOCATION_DATA_INPUT_SOURCE_DEFAULT_COUNTRY_NAME_KEY);
   }

   public int getLocationInputDataSourceBatchSize () {
      return getOfflineBatchConfiguration().getInt(LOCATION_DATA_INPUT_SOURCE_BATCH_SIZE_KEY);
   }

}
