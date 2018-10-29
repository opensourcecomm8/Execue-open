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


package com.execue.web.core.action.audittrail;

import java.util.List;

import com.execue.core.exception.HandlerException;
import com.execue.handler.audittrail.IAuditTrailServiceHandler;
import com.execue.handler.bean.UIUserAccessAuditInput;
import com.execue.handler.bean.grid.IGridBean;
import com.execue.handler.bean.grid.UIUserAccessAudit;
import com.execue.web.core.action.swi.PaginationGridAction;

/**
 * Action class to perform search operation for User Access Audit log
 * @author Jitendra
 *
 */
public class SearchUserAccessAuditAction extends PaginationGridAction {

   // Data coming from UI
   private UIUserAccessAuditInput    userAccessAuditInput;
   private IAuditTrailServiceHandler auditTrailServiceHandler;

   @Override
   protected List<? extends IGridBean> processPageGrid () {
      List<UIUserAccessAudit> userAccessAudit = null;
      try {
         userAccessAudit = getAuditTrailServiceHandler().getUserAccessAuditLog(userAccessAuditInput, getPageDetail());
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return userAccessAudit;

   }

   /**
    * @return the userAccessAuditInput
    */
   public UIUserAccessAuditInput getUserAccessAuditInput () {
      return userAccessAuditInput;
   }

   /**
    * @param userAccessAuditInput the userAccessAuditInput to set
    */
   public void setUserAccessAuditInput (UIUserAccessAuditInput userAccessAuditInput) {
      this.userAccessAuditInput = userAccessAuditInput;
   }

   /**
    * @return the auditTrailServiceHandler
    */
   public IAuditTrailServiceHandler getAuditTrailServiceHandler () {
      return auditTrailServiceHandler;
   }

   /**
    * @param auditTrailServiceHandler the auditTrailServiceHandler to set
    */
   public void setAuditTrailServiceHandler (IAuditTrailServiceHandler auditTrailServiceHandler) {
      this.auditTrailServiceHandler = auditTrailServiceHandler;
   }

}
