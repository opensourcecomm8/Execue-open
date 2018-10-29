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


package com.execue.audittrail.dataaccess.impl;

import java.util.List;

import com.execue.audittrail.dataaccess.IAuditTrailDataAccessManager;
import com.execue.audittrail.exception.AuditTrailException;
import com.execue.core.common.bean.audittrail.UserAccessAuditInput;
import com.execue.core.common.bean.entity.audittrail.AnonymousUser;
import com.execue.core.common.bean.entity.audittrail.UserAccessAudit;
import com.execue.dataaccess.audittrail.dao.IAnonymousUserDAO;
import com.execue.dataaccess.audittrail.dao.IUserAccessAuditDAO;
import com.execue.dataaccess.exception.DataAccessException;

public class AuditTrailDataAccessManagerImpl implements IAuditTrailDataAccessManager {

   private IAnonymousUserDAO   anonymousUserDAO;

   private IUserAccessAuditDAO userAccessAuditDAO;

   public IAnonymousUserDAO getAnonymousUserDAO () {
      return anonymousUserDAO;
   }

   public void setAnonymousUserDAO (IAnonymousUserDAO anonymousUserDAO) {
      this.anonymousUserDAO = anonymousUserDAO;
   }

   public IUserAccessAuditDAO getUserAccessAuditDAO () {
      return userAccessAuditDAO;
   }

   public void setUserAccessAuditDAO (IUserAccessAuditDAO userAccessAuditDAO) {
      this.userAccessAuditDAO = userAccessAuditDAO;
   }

   @Override
   public void createAnonymousUser (AnonymousUser user) throws AuditTrailException {
      try {
         getAnonymousUserDAO().create(user);
      } catch (DataAccessException e) {
         throw new AuditTrailException(e.getCode(), e);
      }
   }

   @Override
   public void createUserAccessAudit (UserAccessAudit userAccessAudit) throws AuditTrailException {
      try {
         getUserAccessAuditDAO().create(userAccessAudit);
      } catch (DataAccessException e) {
         throw new AuditTrailException(e.getCode(), e);
      }
   }

   @Override
   public AnonymousUser getAnonymousUserByIPLocation (String ipLocation) throws AuditTrailException {
      try {
         return getAnonymousUserDAO().getAnonymousUserByIPLocation(ipLocation);
      } catch (DataAccessException e) {
         throw new AuditTrailException(e.getCode(), e);
      }
   }

   @Override
   public List<UserAccessAudit> populateUserAccessAudit (UserAccessAuditInput input) throws AuditTrailException {
      try {
         return getUserAccessAuditDAO().populateUserAccessAudit(input);
      } catch (DataAccessException e) {
         throw new AuditTrailException(e.getCode(), e);
      }
   }

   @Override
   public AnonymousUser getAnonymousUserById (Long anonymousUserId) throws AuditTrailException {
      try {
         return getUserAccessAuditDAO().getById(anonymousUserId, AnonymousUser.class);
      } catch (DataAccessException e) {
         throw new AuditTrailException(e.getCode(), e);
      }
   }
}
