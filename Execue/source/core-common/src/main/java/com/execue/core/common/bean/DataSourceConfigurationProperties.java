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


package com.execue.core.common.bean;

import java.util.Properties;

/**
 * @author Vishay
 */
public class DataSourceConfigurationProperties {

   private Integer    initialSize;
   private Integer    maxActive;
   private boolean    testWhileIdle;
   private boolean    testOnBorrow;
   private String     validationQuery;
   private String[]   mappingResources;
   private Properties hibernateProperties;

   public Integer getInitialSize () {
      return initialSize;
   }

   public void setInitialSize (Integer initialSize) {
      this.initialSize = initialSize;
   }

   public Integer getMaxActive () {
      return maxActive;
   }

   public void setMaxActive (Integer maxActive) {
      this.maxActive = maxActive;
   }

   public boolean isTestWhileIdle () {
      return testWhileIdle;
   }

   public void setTestWhileIdle (boolean testWhileIdle) {
      this.testWhileIdle = testWhileIdle;
   }

   public boolean isTestOnBorrow () {
      return testOnBorrow;
   }

   public void setTestOnBorrow (boolean testOnBorrow) {
      this.testOnBorrow = testOnBorrow;
   }

   public String getValidationQuery () {
      return validationQuery;
   }

   public void setValidationQuery (String validationQuery) {
      this.validationQuery = validationQuery;
   }

   public String[] getMappingResources () {
      return mappingResources;
   }

   public void setMappingResources (String[] mappingResources) {
      this.mappingResources = mappingResources;
   }

   public Properties getHibernateProperties () {
      return hibernateProperties;
   }

   public void setHibernateProperties (Properties hibernateProperties) {
      this.hibernateProperties = hibernateProperties;
   }
}
