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


package com.execue.audittrail.configuration.impl;

import com.execue.audittrail.configuration.IAuditTrailConfigurationService;
import com.execue.core.configuration.IConfiguration;

public class AuditTrailConfigurationServiceImpl implements IAuditTrailConfigurationService {

   private IConfiguration      auditTrailConfiguration;

   /**
    * This key will retrieve the path of the audittrail configuration properties file
    */
   private static final String AUDIT_TRAIL_HISTORY_ACCESS_TIME_LIMIT_KEY = "audit.static-values.execue-audit-trail-history-access-time-limit-key";

   @Override
   public Integer getAuditTrailHistoryAccessTimeLimit () {
      return getAuditTrailConfiguration().getInt(AUDIT_TRAIL_HISTORY_ACCESS_TIME_LIMIT_KEY);
   }

   public IConfiguration getAuditTrailConfiguration () {
      return auditTrailConfiguration;
   }

   public void setAuditTrailConfiguration (IConfiguration auditTrailConfiguration) {
      this.auditTrailConfiguration = auditTrailConfiguration;
   }

}
