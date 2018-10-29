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


package com.execue.core.configuration.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import com.execue.core.configuration.IConfigurable;
import com.execue.core.exception.ConfigurationException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.system.ExecueSystem;
import com.execue.core.type.SystemType;

public class CoreConfigurableService implements IConfigurable {

   private CoreConfigurationServiceImpl coreConfigurationService;
   private Context                      initialContext;
   private boolean                      log = false;
   private ServletContext               servletContext;

   @Override
   public void doConfigure () throws ConfigurationException {
      this.log = getCoreConfigurationService().isSystemLogEnabled();

      if (getCoreConfigurationService().isForcePooledDataSourcesViaJNDI()) {
         loadInitialContextInfo();
      }
      populateApplicationName();
   }

   private void populateApplicationName () {
      String appName = "batch";
      if (getServletContext() != null && SystemType.WEBAPP == ExecueSystem.getSystemType()) {
         String contextPath = getServletContext().getContextPath();
         if (contextPath != null && contextPath.indexOf("/") > -1) {
            contextPath = contextPath.substring(contextPath.lastIndexOf("/"));
            appName = contextPath.replaceAll("/", "");
         }
      }
      coreConfigurationService.setApplicationName(appName);
   }

   /**
    * Load the Initial Context Information to Configuration service
    *   This information is needed when ever the Data Source pointer needs to be passed to other
    *     execution modules like ETL process where in JNDI reference can not be accessed via Pooled Data Source Manager
    * 
    * @throws ConfigurationException
    */
   private void loadInitialContextInfo () throws ConfigurationException {
      try {

         List<String> neededInitialContextEnviromentPropertyKeys = new ArrayList<String>();
         neededInitialContextEnviromentPropertyKeys.add(Context.INITIAL_CONTEXT_FACTORY);
         neededInitialContextEnviromentPropertyKeys.add(Context.PROVIDER_URL);
         neededInitialContextEnviromentPropertyKeys.add(Context.URL_PKG_PREFIXES);

         Hashtable<?, ?> initialContextEnvironmentAsIs = getInitialContext().getEnvironment();
         Hashtable<String, String> initialContextEnvironment = new Hashtable<String, String>();

         Object tempObject = null;
         for (String key : neededInitialContextEnviromentPropertyKeys) {
            tempObject = initialContextEnvironmentAsIs.get(key);
            if (tempObject != null) {
               initialContextEnvironment.put(key, (String) tempObject);
            }
         }

         getCoreConfigurationService().setInitialContextEnvironment(initialContextEnvironment);
      } catch (NamingException namingException) {
         throw new ConfigurationException(ExeCueExceptionCodes.INITIAL_CONTEXT_INSTANTIASION_FAILED, namingException);
      }
   }

   @Override
   public void reConfigure () throws ConfigurationException {
      doConfigure();
   }

   /**
    * @return the coreConfigurationServiceImpl
    */
   public CoreConfigurationServiceImpl getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationServiceImpl the coreConfigurationServiceImpl to set
    */
   public void setCoreConfigurationService (CoreConfigurationServiceImpl coreConfigurationServiceImpl) {
      this.coreConfigurationService = coreConfigurationServiceImpl;
   }

   /**
    * @return the log
    */
   public boolean isLog () {
      return log;
   }

   /**
    * @param log the log to set
    */
   public void setLog (boolean log) {
      this.log = log;
   }

   public Context getInitialContext () {
      return initialContext;
   }

   public void setInitialContext (Context initialContext) {
      this.initialContext = initialContext;
   }

   public ServletContext getServletContext () {
      return servletContext;
   }

   public void setServletContext (ServletContext servletContext) {
      this.servletContext = servletContext;
   }

}
