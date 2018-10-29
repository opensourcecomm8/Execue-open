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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.execue.core.system;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.execue.core.ISystem;
import com.execue.core.exception.InitializationException;
import com.execue.core.type.SystemType;

/**
 * @author kaliki
 */
public class ExecueSystem implements ISystem, ApplicationContextAware {

   private Logger             logger = Logger.getLogger(ExecueSystem.class);

   private ApplicationContext ctx;
   
   private static SystemType typeOfTheSystem;

   public boolean initialize (SystemType systemType) throws InitializationException {
      try {
         typeOfTheSystem = systemType;
         if (logger.isInfoEnabled()) {
            logger.info("Initializing ExeCue System Configuration");
         }
         ExeCueSystemConfigurationUtility.loadConfigurationServices(getApplicationContext());
         if (logger.isInfoEnabled()) {
            logger.info("ExeCue System Configuration Initialized");
         }
         return true;
      } catch (Exception e) {
         throw new InitializationException(10, e);
      }

   }

   public void destroy () {
      // TODO: Check if any thing to destroy
   }

   public boolean reConfigureService (String serviceName) throws InitializationException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public static SystemType getSystemType() {
      return typeOfTheSystem;
   }
   
   public void setApplicationContext (ApplicationContext applicationContext) throws BeansException {
      this.ctx = applicationContext;
   }

   /**
    * @return the applicationContext
    */
   public ApplicationContext getApplicationContext () {
      return ctx;
   }

}
