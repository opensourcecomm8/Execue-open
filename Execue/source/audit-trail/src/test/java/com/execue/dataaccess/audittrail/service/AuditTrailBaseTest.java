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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.audittrail.service.IAuditTrailService;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.audittrail.AnonymousUser;
import com.execue.core.common.bean.entity.audittrail.UserAccessAudit;
import com.execue.core.common.type.AuditLogType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ExeCueSystemType;
import com.execue.core.system.ExeCueSystemConfigurationUtility;
import com.execue.dataaccess.audittrail.dao.IUserAccessAuditDAO;
import com.execue.dataaccess.exception.DataAccessException;

public class AuditTrailBaseTest {

   private static ApplicationContext audittrailContext;

   public static void baseTearDown () {

   }

   public static void baseSetup () {
      audittrailContext = new ClassPathXmlApplicationContext(new String[] {
               "/platform/bean-config/execue-logging-configuration.xml",
               "/platform/bean-config/execue-query-builder.xml", "/platform/bean-config/execue-configuration.xml",
               "/ext/bean-config/execue-configuration-ext.xml", "spring-hibernate.xml", "spring-hibernate-qdata.xml",
               "/platform/bean-config/execue-dataaccess.xml", "spring-hibernate-audittrail.xml",
               "/platform/bean-config/execue-qdata-dataaccess.xml", "/platform/bean-config/execue-core.xml",
               "/platform/bean-config/execue-util-services.xml",
               "/platform/bean-config/execue-audit-trail-dataaccess.xml",
               "/platform/bean-config/execue-audit-trail.xml" });
      initialize();
   }

   public static void initialize () {
      try {
         ExeCueSystemConfigurationUtility.loadConfigurationServices(audittrailContext);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public UserAccessAudit createUserAccessAudit () {
      UserAccessAudit userAccessAudit = new UserAccessAudit();
      userAccessAudit.setAccessedSystemType(ExeCueSystemType.SEARCH_APP);
      userAccessAudit.setAccessedTime(new java.util.Date());
      userAccessAudit.setAnonymousUser(CheckType.YES);
      userAccessAudit.setAuditLogType(AuditLogType.LOGIN);
      userAccessAudit.setComments("************************");
      userAccessAudit.setIpLocation("10.10.56.221");
      userAccessAudit.setUserId(900000001L);
      return userAccessAudit;
   }

   public AnonymousUser createAnonymousUser () {
      AnonymousUser user = new AnonymousUser();
      user.setCityName("City");
      user.setCountryName("Country");
      user.setCountryCode("Con");
      user.setIpLocation("10.10.56.221");
      user.setLatitude("101");
      user.setLongitude("202");
      user.setStateCode("St");
      user.setStateName("State");
      user.setZipCode("500034");
      return user;
   }

   public List<Long> getUserIds () {
      List<Long> userIds = new ArrayList<Long>();
      userIds.add(900000001L);
      userIds.add(900000002L);
      return userIds;
   }

   public List<AuditLogType> getAuditLogTypes () {
      List<AuditLogType> auditLogTypes = new ArrayList<AuditLogType>();
      auditLogTypes.add(AuditLogType.LOGIN);
      auditLogTypes.add(AuditLogType.LOGOUT);
      return auditLogTypes;
   }

   public List<Date> getAccessedTime () throws DataAccessException {
      List<Date> dates = new ArrayList<Date>();
      // UserAccessAudit audit1 = getUserAccessAuditDAO().getById(22L,
      // UserAccessAudit.class);
      UserAccessAudit audit2 = getUserAccessAuditDAO().getById(34L, UserAccessAudit.class);
      // dates.add(audit1.getAccessedTime());
      dates.add(audit2.getAccessedTime());
      return dates;
   }

   public Page getPage () {
      Page page = new Page(1L, 10L);
      return page;
   }

   public IAuditTrailService getAuditTrailService () {
      return (IAuditTrailService) audittrailContext.getBean("audittrailService");
   }

   public IUserAccessAuditDAO getUserAccessAuditDAO () {
      return (IUserAccessAuditDAO) audittrailContext.getBean("userAccessAuditDAO");
   }

}
