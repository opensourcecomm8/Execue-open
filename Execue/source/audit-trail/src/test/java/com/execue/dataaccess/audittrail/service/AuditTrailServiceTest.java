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


package com.execue.dataaccess.audittrail.service;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.audittrail.exception.AuditTrailException;
import com.execue.core.common.bean.audittrail.UserAccessAuditInput;
import com.execue.core.common.bean.entity.audittrail.AnonymousUser;
import com.execue.core.common.bean.entity.audittrail.UserAccessAudit;
import com.execue.core.common.type.OperatorType;
import com.execue.dataaccess.exception.DataAccessException;

public class AuditTrailServiceTest extends AuditTrailBaseTest {

   @BeforeClass
   public static void setup () {
      baseSetup();
   }

   @AfterClass
   public static void teardown () {
      baseTearDown();
   }

   // @Test
   public void testCreateAnonymousUser () throws AuditTrailException {
      getAuditTrailService().createAnonymousUser(createAnonymousUser());
   }

   // @Test
   public void testGetAnonymousUserByIPLocation () throws AuditTrailException {
      String ipLocation = "10.10.56.221";
      AnonymousUser anonymousUserByIPLocation = getAuditTrailService().getAnonymousUserByIPLocation(ipLocation);
      System.out.println(anonymousUserByIPLocation.getCityName());
   }

   // @Test
   public void testCreateUserAccessAudit () throws AuditTrailException {
      getAuditTrailService().createUserAccessAudit(createUserAccessAudit());
   }

   @Test
   public void testPopulateUserAccessAudit () throws DataAccessException, AuditTrailException {
      UserAccessAuditInput input = new UserAccessAuditInput();
      input.setPage(getPage());
      input.setUserIds(getUserIds());
      input.setAuditLogTypes(getAuditLogTypes());
      input.setOperator(OperatorType.GREATER_THAN);
      input.setSearchDates(getAccessedTime());
      List<UserAccessAudit> populateUserAccessAudit = getAuditTrailService().populateUserAccessAudit(input);
      System.out.println(populateUserAccessAudit.size());

   }
}
