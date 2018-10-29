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


package com.execue.core.common.bean.util;

import java.util.List;

/**
 * @author Murthy Used by XSteam API
 */
public class Connection {

   private String       id;
   private String       driver;
   private String       url;
   private String       user;
   private String       password;
   private List<String> initialContextEnvironmentProperties;
   private boolean      lazyInit = true;

   public Connection (String id, String driver) {
      super();
      this.id = id;
      this.driver = driver;
   }

   public Connection (String id, String driver, String url, String user, String password) {
      super();
      this.id = id;
      this.driver = driver;
      this.url = url;
      this.user = user;
      this.password = password;
   }

   public Connection (String id, String driver, String url) {
      super();
      this.id = id;
      this.driver = driver;
      this.url = url;
   }
   
   public Connection (String id, String driver, String url, List<String> initialContextEnvironmentProperties) {
      super();
      this.id = id;
      this.driver = driver;
      this.url = url;
      this.initialContextEnvironmentProperties = initialContextEnvironmentProperties;
   }

   public String getId () {
      return id;
   }

   public void setId (String id) {
      this.id = id;
   }

   public String getDriver () {
      return driver;
   }

   public void setDriver (String driver) {
      this.driver = driver;
   }

   public String getUrl () {
      return url;
   }

   public void setUrl (String url) {
      this.url = url;
   }

   public String getUser () {
      return user;
   }

   public void setUser (String user) {
      this.user = user;
   }

   public String getPassword () {
      return password;
   }

   public void setPassword (String password) {
      this.password = password;
   }

   public List<String> getInitialContextEnvironmentProperties () {
      return initialContextEnvironmentProperties;
   }

   public void setInitialContextEnvironmentProperties (List<String> initialContextEnvironmentProperties) {
      this.initialContextEnvironmentProperties = initialContextEnvironmentProperties;
   }

   
   public boolean isLazyInit () {
      return lazyInit;
   }

   
   public void setLazyInit (boolean lazyInit) {
      this.lazyInit = lazyInit;
   }

}
