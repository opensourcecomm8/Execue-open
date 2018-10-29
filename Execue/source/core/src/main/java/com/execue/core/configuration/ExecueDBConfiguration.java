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


package com.execue.core.configuration;

import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.configuration.DatabaseConfiguration;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.execue.core.exception.ConfigurationException;
import com.execue.core.exception.ExeCueExceptionCodes;

/**
 * @author kaliki
 * @since 4.0
 */
public class ExecueDBConfiguration implements IConfiguration {

   private Logger                logger                          = Logger.getLogger(ExecueDBConfiguration.class);

   private static final String   CONFIGURATION_NAME_COLUMN_NAME  = "CONFIG_NAME";
   private static final String   CONFIGURATION_KEY_COLUMN_NAME   = "CONFIG_KEY";
   private static final String   CONFIGURATION_VALUE_COLUMN_NAME = "CONFIG_VALUE";
   private static final String   CONFIGURATION_TABLE_NAME        = "CONFIGURATION";

   private DatabaseConfiguration databaseConfiguration;

   public ExecueDBConfiguration (DataSource dataSource, String name) throws ConfigurationException {
      try {
         if (logger.isDebugEnabled()) {
            logger.debug("root configuration name : " + name);
         }
         databaseConfiguration = new DatabaseConfiguration(dataSource, CONFIGURATION_TABLE_NAME,
                  CONFIGURATION_NAME_COLUMN_NAME, CONFIGURATION_KEY_COLUMN_NAME, CONFIGURATION_VALUE_COLUMN_NAME, name);
      } catch (Exception e) {
         e.printStackTrace();
         throw new ConfigurationException(ExeCueExceptionCodes.CONFIGURATION_LOAD_ERROR, e);
      }
   }

   public ExecueDBConfiguration (DataSource dataSource) throws ConfigurationException {
      try {
         databaseConfiguration = new DatabaseConfiguration(dataSource, CONFIGURATION_TABLE_NAME,
                  CONFIGURATION_KEY_COLUMN_NAME, CONFIGURATION_VALUE_COLUMN_NAME);
      } catch (Exception e) {
         e.printStackTrace();
         throw new ConfigurationException(ExeCueExceptionCodes.CONFIGURATION_LOAD_ERROR, e);
      }
   }

   @Override
   public void saveConfigurationProperty (String key, Object value) {
      this.databaseConfiguration.setProperty(key, value);
   }

   public String getProperty (String key) {
      return databaseConfiguration.getString(key);
   }

   public Object getPropertyObject (String key) {
      return databaseConfiguration.getProperty(key);
   }

   public boolean getBoolean (String key) {
      return databaseConfiguration.getBoolean(key);
   }

   public double getDouble (String key) {
      return databaseConfiguration.getDouble(key);
   }

   public int getInt (String key) {
      return databaseConfiguration.getInt(key);
   }

   public long getLong (String key) {
      return databaseConfiguration.getLong(key);
   }

   @SuppressWarnings ("unchecked")
   public List<String> getList (String key) {
      return databaseConfiguration.getList(key);
   }

   public String[] getArray (String key) {
      return databaseConfiguration.getStringArray(key);
   }

   /**
    * Document does not make any sense here so returning null
    */
   public Document getDocument () {
      return null;
   }

   @SuppressWarnings ("unchecked")
   public Iterator getKeys () {
      return databaseConfiguration.getKeys();
   }

}
