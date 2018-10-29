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

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.execue.core.exception.ConfigurationException;
import com.execue.core.exception.ExeCueExceptionCodes;

/**
 * @author kaliki
 */
public class ExecueConfiguration implements IConfiguration {

   private XMLConfiguration xMLConfiguration;

   private Logger           logger = Logger.getLogger(ExecueConfiguration.class);

   public ExecueConfiguration (List<String> fileNames) throws ConfigurationException {
      try {
         if (fileNames.size() > 1) {
            xMLConfiguration = new XMLConfiguration();
            for (String fileName : fileNames) {
               logger.debug("File Name " + fileName);
               xMLConfiguration.append(new XMLConfiguration(getClass().getResource(fileName)));
            }
         } else {
            logger.debug("File Name " + fileNames.get(0));
            xMLConfiguration = new XMLConfiguration(getClass().getResource(fileNames.get(0)));
         }
      } catch (Exception e) {
         e.printStackTrace();
         throw new ConfigurationException(10, e);
      }
   }

   public String getProperty (String key) {
      return xMLConfiguration.getString(key);
   }

   public Object getPropertyObject (String key) {
      return xMLConfiguration.getProperty(key);
   }

   public boolean getBoolean (String key) {
      return xMLConfiguration.getBoolean(key);
   }

   public double getDouble (String key) {
      return xMLConfiguration.getDouble(key);
   }

   public int getInt (String key) {
      return xMLConfiguration.getInt(key);
   }

   public long getLong (String key) {
      return xMLConfiguration.getLong(key);
   }

   @SuppressWarnings ("unchecked")
   public List<String> getList (String key) {
      return xMLConfiguration.getList(key);
   }

   public String[] getArray (String key) {
      return xMLConfiguration.getStringArray(key);
   }

   public Document getDocument () {
      return xMLConfiguration.getDocument();
   }

   @SuppressWarnings ("unchecked")
   public Iterator getKeys () {
      return xMLConfiguration.getKeys();
   }

   @Override
   public void saveConfigurationProperty (String key, Object value) {
      throw new RuntimeException(new ConfigurationException(
               ExeCueExceptionCodes.INVALID_CONFIGURATION_MODIFICATIONEXCEPTION,
               "Configuration can not be modified for [" + key + "]"));
   }
}
