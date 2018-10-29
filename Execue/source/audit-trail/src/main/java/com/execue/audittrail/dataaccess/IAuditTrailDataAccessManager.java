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


package com.execue.audittrail.dataaccess;

import java.util.List;

import com.execue.audittrail.exception.AuditTrailException;
import com.execue.core.common.bean.audittrail.UserAccessAuditInput;
import com.execue.core.common.bean.entity.audittrail.AnonymousUser;
import com.execue.core.common.bean.entity.audittrail.UserAccessAudit;

public interface IAuditTrailDataAccessManager {

   public void createAnonymousUser (AnonymousUser user) throws AuditTrailException;

   public AnonymousUser getAnonymousUserByIPLocation (String ipLocation) throws AuditTrailException;

   public void createUserAccessAudit (UserAccessAudit userAccessAudit) throws AuditTrailException;

   public List<UserAccessAudit> populateUserAccessAudit (UserAccessAuditInput input) throws AuditTrailException;

   public AnonymousUser getAnonymousUserById (Long anonymousUserId) throws AuditTrailException;

}
