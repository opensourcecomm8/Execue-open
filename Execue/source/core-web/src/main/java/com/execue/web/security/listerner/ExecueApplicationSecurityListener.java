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


package com.execue.web.security.listerner;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.execue.core.common.type.AuditLogType;
import com.execue.platform.audittrail.IAuditTrailWrapperService;
import com.execue.platform.exception.PlatformException;
import com.execue.security.bean.ExecueUserDetails;

public class ExecueApplicationSecurityListener implements ApplicationListener<ApplicationEvent> {

   private static final Logger       logger = Logger.getLogger(ExecueApplicationSecurityListener.class);

   private IAuditTrailWrapperService auditTrailWrapperService;

   @Override
   public void onApplicationEvent (ApplicationEvent event) {
      if (event instanceof SessionDestroyedEvent) {
         SessionDestroyedEvent sessinEvent = (SessionDestroyedEvent) event;
         if (sessinEvent.getSecurityContexts() != null && sessinEvent.getSecurityContexts().size() > 0) {
            Authentication authentication = sessinEvent.getSecurityContexts().get(0).getAuthentication();
            WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
            String remoteAddress = details.getRemoteAddress();
            // Call audit log service
            ExecueUserDetails execueUserDetail = (ExecueUserDetails) authentication.getPrincipal();
            try {
               getAuditTrailWrapperService().prepareAndPersistUserAccessAuditInfo(execueUserDetail.getUser(),
                        AuditLogType.LOGOUT, remoteAddress);
            } catch (PlatformException platformException) {
               platformException.printStackTrace();
               logger.error(platformException);
            }

         }
      }
   }

   /**
    * @return the auditTrailWrapperService
    */
   public IAuditTrailWrapperService getAuditTrailWrapperService () {
      return auditTrailWrapperService;
   }

   /**
    * @param auditTrailWrapperService the auditTrailWrapperService to set
    */
   public void setAuditTrailWrapperService (IAuditTrailWrapperService auditTrailWrapperService) {
      this.auditTrailWrapperService = auditTrailWrapperService;
   }
}