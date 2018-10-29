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


package com.execue.uswhda.configuration.impl;

import java.util.List;
import java.util.Properties;

import com.execue.core.common.bean.DataSourceConfigurationProperties;
import com.execue.core.configuration.IConfiguration;
import com.execue.uswhda.configuration.IUnstructuredWHDataAccessConfigurationService;

/**
 * @author Vishay
 */
public class UnstructuredWHDataAccessConfigurationServiceImpl implements IUnstructuredWHDataAccessConfigurationService {

   private IConfiguration      unstructuredWHDataAccessConfiguration;
   private Properties          databaseProperties;

   private static final String UNSTRUCTURED_WH_INITIAL_SIZE_KEY           = "unstructured-wh-dataaccess.static-values.initialSize";
   private static final String UNSTRUCTURED_WH_MAX_ACTIVE_KEY             = "unstructured-wh-dataaccess.static-values.maxActive";
   private static final String UNSTRUCTURED_WH_TEST_WHILE_IDLE_KEY        = "unstructured-wh-dataaccess.static-values.testWhileIdle";
   private static final String UNSTRUCTURED_WH_TEST_ON_BORROW_KEY         = "unstructured-wh-dataaccess.static-values.testOnBorrow";
   private static final String UNSTRUCTURED_WH_VALIDATION_QUERY_KEY       = "unstructured-wh-dataaccess.static-values.validationQuery";

   private static final String UNSTRUCTURED_WH_MAPPING_RESOURCES_KEY      = "unstructured-wh-dataaccess.static-values.mapping-resources.value";
   private static final String UNSTRUCTURED_WH_HIBERNATE_PROPERTIES_KEY   = "unstructured-wh-dataaccess.static-values.hibernate-properties.map.key";
   private static final String UNSTRUCTURED_WH_HIBERNATE_PROPERTIES_VALUE = "unstructured-wh-dataaccess.static-values.hibernate-properties.map.value";
   private static final String UNSTRUCTURED_WH_HIBERNATE_DIALECT          = "hibernate.dialect";

   @Override
   public Boolean getUnstructureWarehouseTestWhileIdleValue () {
      return getUnstructuredWHDataAccessConfiguration().getBoolean(UNSTRUCTURED_WH_TEST_WHILE_IDLE_KEY);
   }

   @Override
   public List<String> getUnstructuredWarehouseHibernatePropertiesKeys () {
      return getUnstructuredWHDataAccessConfiguration().getList(UNSTRUCTURED_WH_HIBERNATE_PROPERTIES_KEY);
   }

   @Override
   public List<String> getUnstructuredWarehouseHibernatePropertiesValues () {
      return getUnstructuredWHDataAccessConfiguration().getList(UNSTRUCTURED_WH_HIBERNATE_PROPERTIES_VALUE);
   }

   @Override
   public Integer getUnstructuredWarehouseInitialSize () {
      return getUnstructuredWHDataAccessConfiguration().getInt(UNSTRUCTURED_WH_INITIAL_SIZE_KEY);
   }

   @Override
   public List<String> getUnstructuredWarehouseMappingResources () {
      return getUnstructuredWHDataAccessConfiguration().getList(UNSTRUCTURED_WH_MAPPING_RESOURCES_KEY);
   }

   @Override
   public Integer getUnstructuredWarehouseMaxActiveValue () {
      return getUnstructuredWHDataAccessConfiguration().getInt(UNSTRUCTURED_WH_MAX_ACTIVE_KEY);
   }

   @Override
   public Boolean getUnstructuredWarehouseTestOnBorrowValue () {
      return getUnstructuredWHDataAccessConfiguration().getBoolean(UNSTRUCTURED_WH_TEST_ON_BORROW_KEY);
   }

   @Override
   public String getUnstructuredWarehouseValidationQuery () {
      return getUnstructuredWHDataAccessConfiguration().getProperty(UNSTRUCTURED_WH_VALIDATION_QUERY_KEY);
   }

   /**
    * @return the unstructuredWHDataAccessConfiguration
    */
   public IConfiguration getUnstructuredWHDataAccessConfiguration () {
      return unstructuredWHDataAccessConfiguration;
   }

   /**
    * @param unstructuredWHDataAccessConfiguration
    *           the unstructuredWHDataAccessConfiguration to set
    */
   public void setUnstructuredWHDataAccessConfiguration (IConfiguration unstructuredWHDataAccessConfiguration) {
      this.unstructuredWHDataAccessConfiguration = unstructuredWHDataAccessConfiguration;
   }

   public DataSourceConfigurationProperties getDataSourceConfigurationProperties () {
      DataSourceConfigurationProperties dataSourceConfigurationProperties = new DataSourceConfigurationProperties();
      dataSourceConfigurationProperties.setInitialSize(getUnstructuredWarehouseInitialSize());
      dataSourceConfigurationProperties.setMaxActive(getUnstructuredWarehouseMaxActiveValue());
      dataSourceConfigurationProperties.setTestWhileIdle(getUnstructureWarehouseTestWhileIdleValue());
      dataSourceConfigurationProperties.setTestOnBorrow(getUnstructuredWarehouseTestOnBorrowValue());
      dataSourceConfigurationProperties.setValidationQuery(getDatabaseProperties().getProperty(
               getUnstructuredWarehouseValidationQuery()));
      dataSourceConfigurationProperties.setMappingResources(buildMappingResources());
      dataSourceConfigurationProperties.setHibernateProperties(buildHibernateProperties());
      return dataSourceConfigurationProperties;
   }

   private String[] buildMappingResources () {
      List<String> mappingResourcesList = getUnstructuredWarehouseMappingResources();
      String[] mappingResources = new String[mappingResourcesList.size()];
      mappingResourcesList.toArray(mappingResources);
      return mappingResources;
   }

   private Properties buildHibernateProperties () {
      Properties properties = new Properties();
      List<String> keys = getUnstructuredWarehouseHibernatePropertiesKeys();
      List<String> values = getUnstructuredWarehouseHibernatePropertiesValues();
      String key = null;
      String value = null;
      for (int index = 0; index < keys.size(); index++) {
         key = keys.get(index);
         value = values.get(index);
         if (UNSTRUCTURED_WH_HIBERNATE_DIALECT.equalsIgnoreCase(key)) {
            value = getDatabaseProperties().getProperty(value);
         }
         properties.put(key, value);
      }
      return properties;
   }

   public Properties getDatabaseProperties () {
      return databaseProperties;
   }

   public void setDatabaseProperties (Properties databaseProperties) {
      this.databaseProperties = databaseProperties;
   }

}
