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

import org.w3c.dom.Document;

/**
 * @author kaliki
 */
public interface IConfiguration {

   public String getProperty (String key);

   public Object getPropertyObject (String key);

   public boolean getBoolean (String key);

   public double getDouble (String key);

   public int getInt (String key);

   public long getLong (String key);

   public List<String> getList (String key);

   public String[] getArray (String key);

   public Document getDocument ();

   /**
    * Retrieves all the keys defined in the configuration file
    */
   @SuppressWarnings ("unchecked")
   public Iterator getKeys ();

   public void saveConfigurationProperty (String key, Object value);
}
