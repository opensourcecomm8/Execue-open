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


package com.execue.audittrail.service.impl;

import java.util.List;

import com.execue.audittrail.dataaccess.IAuditTrailDataAccessManager;
import com.execue.audittrail.exception.AuditTrailException;
import com.execue.audittrail.service.IAuditTrailService;
import com.execue.core.common.bean.audittrail.UserAccessAuditInput;
import com.execue.core.common.bean.entity.audittrail.AnonymousUser;
import com.execue.core.common.bean.entity.audittrail.UserAccessAudit;

public class AuditTrailServiceImpl implements IAuditTrailService {

   private IAuditTrailDataAccessManager auditTrailDataAccessManager;

   @Override
   public void createAnonymousUser (AnonymousUser user) throws AuditTrailException {
      getAuditTrailDataAccessManager().createAnonymousUser(user);
   }

   @Override
   public void createUserAccessAudit (UserAccessAudit userAccessAudit) throws AuditTrailException {
      getAuditTrailDataAccessManager().createUserAccessAudit(userAccessAudit);
   }

   @Override
   public AnonymousUser getAnonymousUserByIPLocation (String ipLocation) throws AuditTrailException {
      return getAuditTrailDataAccessManager().getAnonymousUserByIPLocation(ipLocation);
   }

   @Override
   public List<UserAccessAudit> populateUserAccessAudit (UserAccessAuditInput input) throws AuditTrailException {
      return getAuditTrailDataAccessManager().populateUserAccessAudit(input);
   }

   @Override
   public AnonymousUser getAnonymousUserById (Long anonymousUserId) throws AuditTrailException {
      return getAuditTrailDataAccessManager().getAnonymousUserById(anonymousUserId);
   }

   /**
    * @return the auditTrailDataAccessManager
    */
   public IAuditTrailDataAccessManager getAuditTrailDataAccessManager () {
      return auditTrailDataAccessManager;
   }

   /**
    * @param auditTrailDataAccessManager the auditTrailDataAccessManager to set
    */
   public void setAuditTrailDataAccessManager (IAuditTrailDataAccessManager auditTrailDataAccessManager) {
      this.auditTrailDataAccessManager = auditTrailDataAccessManager;
   }

}
