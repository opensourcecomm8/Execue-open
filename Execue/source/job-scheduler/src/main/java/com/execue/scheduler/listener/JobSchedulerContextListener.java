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


package com.execue.scheduler.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.service.IExecueJobSchedulerService;

public class JobSchedulerContextListener implements ServletContextListener {

   private Logger             logger = Logger.getLogger(JobSchedulerContextListener.class);

   public void contextDestroyed (ServletContextEvent event) {
      try {
         
         WebApplicationContext springWebContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
         IExecueJobSchedulerService schedulerService = (IExecueJobSchedulerService) springWebContext.getBean("execueJobSchedulerService");
         
         schedulerService.stopScheduler();
         logger.debug("Job Scheduler SUCCESSFULLY stopped");
      } catch (ExecueJobSchedulerException e) {
         logger.error(e.getMessage());
         logger.error("FATAL -- Scheduler stop failed");
      }

   }

   public void contextInitialized (ServletContextEvent event) {
      try {
         
         WebApplicationContext springWebContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
         IExecueJobSchedulerService schedulerService = (IExecueJobSchedulerService) springWebContext.getBean("execueJobSchedulerService");
         
         // Store the context for the Job Executioner could get the wired beans from Spring Context at later point of time
         SpringContextHolder.setSpringWebContext(springWebContext);
         
         schedulerService.startScheduler();
         logger.debug("Job Scheduler SUCCESSFULLY started");
      } catch (ExecueJobSchedulerException e) {
         logger.error(e.getMessage());
         logger.error("FATAL -- Scheduler startup failed");
      }

   }

}
