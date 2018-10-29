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


package com.execue.querycache.configuration.impl;

import javax.sql.DataSource;

import com.execue.core.configuration.ExecueDBConfiguration;
import com.execue.core.configuration.IConfigurable;
import com.execue.core.configuration.IConfiguration;
import com.execue.core.exception.ConfigurationException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.querycache.configuration.IQueryCacheConfiguration;

public class QueryCacheConfigurationImpl implements IQueryCacheConfiguration, IConfigurable {

   public static String          NAME = "QueryDataConfigurationService";
   private DataSource            dataSource;
   private String                configurationName;
   private ExecueDBConfiguration configuration;

   public void setDataSource (DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public void setConfigurationName (String configurationName) {
      this.configurationName = configurationName;
   }

   public IConfiguration getConfiguration () {
      return configuration;
   }

   public void doConfigure () throws ConfigurationException {
      try {
         configuration = new ExecueDBConfiguration(dataSource, configurationName);
      } catch (Exception e) {
         throw new ConfigurationException(ExeCueExceptionCodes.CONFIGURATION_LOAD_ERROR, e);
      }
   }

   public void reConfigure () throws ConfigurationException {
      configuration = new ExecueDBConfiguration(dataSource, configurationName);
   }

   public String getServiceName () {
      return NAME;
   }
}
