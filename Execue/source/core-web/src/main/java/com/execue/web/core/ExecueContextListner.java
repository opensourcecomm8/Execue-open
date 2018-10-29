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


package com.execue.web.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.execue.core.ISystem;
import com.execue.core.bean.Menu;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.InitializationException;
import com.execue.core.type.SystemType;
import com.execue.core.util.MenuGeneration;
import com.execue.web.core.util.ExecueWebConstants;

public class ExecueContextListner implements ServletContextListener {

   public void contextDestroyed (ServletContextEvent event) {
      WebApplicationContext springWebContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event
               .getServletContext());
      ISystem execueSystem = (ISystem) springWebContext.getBean(ExecueWebConstants.EXECUE_SYSTEM);
      event.getServletContext().removeAttribute(ExecueWebConstants.ADMIN_MENU);
      execueSystem.destroy();
   }

   public void contextInitialized (ServletContextEvent event) {
      try {
         Menu menu = new MenuGeneration().generateMenu();
         event.getServletContext().setAttribute(ExecueWebConstants.ADMIN_MENU, menu);
         
         WebApplicationContext springWebContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event
                  .getServletContext());
         ISystem execueSystem = (ISystem) springWebContext.getBean(ExecueWebConstants.EXECUE_SYSTEM);
         
         execueSystem.initialize(SystemType.WEBAPP);
         
         // Loading main and admin context
         ICoreConfigurationService coreConfigurationService = (ICoreConfigurationService) springWebContext
                  .getBean("coreConfigurationService");
         event.getServletContext().setAttribute(ExecueWebConstants.MAIN_CONTEXT,
                  coreConfigurationService.getWebMainContext());
         event.getServletContext().setAttribute(ExecueWebConstants.ADMIN_CONTEXT,
                  coreConfigurationService.getWebAdminContext());

      } catch (InitializationException e) {
         e.printStackTrace();
      }
   }
}
