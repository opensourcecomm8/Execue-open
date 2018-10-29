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


package com.execue.handler.audittrail.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.audittrail.configuration.IAuditTrailConfigurationService;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.audittrail.UserAccessAuditInput;
import com.execue.core.common.bean.audittrail.UserSearchAuditInput;
import com.execue.core.common.bean.audittrail.UserSearchAuditOutput;
import com.execue.core.common.bean.entity.audittrail.UserAccessAudit;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.AuditLogType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.audittrail.IAuditTrailServiceHandler;
import com.execue.handler.bean.UIUser;
import com.execue.handler.bean.UIUserAccessAuditInput;
import com.execue.handler.bean.grid.UIUserAccessAudit;
import com.execue.handler.bean.grid.UIUserSearchAudit;
import com.execue.platform.audittrail.IAuditTrailWrapperService;
import com.execue.platform.exception.PlatformException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IUserManagementService;

public class AuditTrailServiceHandlerImpl implements IAuditTrailServiceHandler {

   private static final Logger             logger           = Logger.getLogger(AuditTrailServiceHandlerImpl.class);

   private IUserManagementService          userManagementService;
   private IAuditTrailWrapperService       auditTrailWrapperService;
   private IAuditTrailConfigurationService auditTrailConfigurationService;

   private static Map<String, Integer>     calendarMonthMap = new HashMap<String, Integer>();
   static {
      populateCalendarMonthMap(calendarMonthMap);
   }

   @Override
   public List<UIUserAccessAudit> getUserAccessAuditLog (UIUserAccessAuditInput userAccessAuditInput, Page page)
            throws HandlerException {
      List<UIUserAccessAudit> uiUserAccessAuditList = new ArrayList<UIUserAccessAudit>();
      try {
         List<UserAccessAudit> userAccessAuditList = getAuditTrailWrapperService().populateUserAccessAudit(
                  (UserAccessAuditInput) prepareUserAccesAuditInput(userAccessAuditInput, page));
         if (ExecueCoreUtil.isCollectionNotEmpty(userAccessAuditList)) {
            for (UserAccessAudit userAccessAudit : userAccessAuditList) {
               uiUserAccessAuditList.add(transformIntoUIUserAccessAudit(userAccessAudit));
            }
         }
      } catch (PlatformException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
      return uiUserAccessAuditList;
   }

   @Override
   public List<UIUserSearchAudit> getUserSearchAuditLog (UIUserAccessAuditInput userAccessAuditInput, Page page)
            throws HandlerException {
      List<UIUserSearchAudit> uiUserSearchAuditList = new ArrayList<UIUserSearchAudit>();
      try {
         List<UserSearchAuditOutput> userSearchAuditList = getAuditTrailWrapperService().populateUserSearchAudit(
                  (UserSearchAuditInput) prepareUserSearchAuditInput(userAccessAuditInput, page));
         if (ExecueCoreUtil.isCollectionNotEmpty(userSearchAuditList)) {
            for (UserSearchAuditOutput userSearchAudit : userSearchAuditList) {
               uiUserSearchAuditList.add(transformIntoUIUserSearchAudit(userSearchAudit));
            }
         }

      } catch (PlatformException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
      return uiUserSearchAuditList;
   }

   @Override
   public List<UIUser> getUsers (String search) throws HandlerException {
      List<UIUser> uiUsers = new ArrayList<UIUser>();
      // TODO-JT- get limit from configuration
      int limit = 10;
      try {
         List<User> swiUsers = getUserManagementService().suggestUsers(search, limit);
         uiUsers.add(prepareStaticUser(-1l, "All"));
         uiUsers.add(prepareStaticUser(-2l, "All Registered User"));
         uiUsers.add(prepareStaticUser(-3l, "All Anonymous User"));
         if (ExecueCoreUtil.isCollectionNotEmpty(swiUsers)) {
            for (User swiUser : swiUsers) {
               uiUsers.add(transformIntoUIUser(swiUser));
            }

         }

      } catch (SWIException swiException) {
         logger.error(swiException);
         throw new HandlerException(swiException.getCode(), swiException);
      }
      return uiUsers;
   }

   private UIUser transformIntoUIUser (User swiUser) {
      UIUser uiUser = new UIUser();
      uiUser.setId(swiUser.getId());
      uiUser.setDisplayName(swiUser.getFirstName() + " " + swiUser.getLastName() + "(" + swiUser.getUsername() + ")");
      return uiUser;
   }

   private UIUser prepareStaticUser (Long id, String displayName) {
      UIUser uiUser = new UIUser();
      uiUser.setId(id);
      uiUser.setDisplayName(displayName);
      return uiUser;

   }

   private UserSearchAuditInput prepareUserSearchAuditInput (UIUserAccessAuditInput uiUserAccessAuditInput, Page page) {
      UserSearchAuditInput userSearchAuditInput = new UserSearchAuditInput();
      //Id -1 or empty userIds - All user
      //Id -2  - All registered user
      //Id -3 - All anonymous user
      //UserIds
      List<Long> userIds = uiUserAccessAuditInput.getUserIds();
      if (ExecueCoreUtil.isCollectionNotEmpty(userIds)) {
         if (userIds.get(0) == -2) {
            userSearchAuditInput.setAnonymousUser(CheckType.NO);
         } else if (userIds.get(0) == -3) {
            userSearchAuditInput.setAnonymousUser(CheckType.YES);
         } else if (userIds.get(0) == -1) {
            //do nothing
         } else {
            // set to maintain the uniqueness 
            Set<Long> userIdSet = new HashSet<Long>(uiUserAccessAuditInput.getUserIds());
            userSearchAuditInput.setUserIds(new ArrayList<Long>(userIdSet));
         }
      }
      // prepare operator and values
      String operator = uiUserAccessAuditInput.getOperator();
      List<Date> searchDateList = new ArrayList<Date>();
      if (operator != null) {
         userSearchAuditInput.setOperator(OperatorType.getOperatorType(operator));
         prepareSearchDatesForOperator(searchDateList, uiUserAccessAuditInput.getOperands());
      } else {
         userSearchAuditInput.setOperator(OperatorType.GREATER_THAN);
         // set default date time to get past 30 days records
         searchDateList.add(prepareDefaultAccessTime().getTime());
      }
      //Operands or Date time frame
      userSearchAuditInput.setSearchDates(searchDateList);

      //Page 
      userSearchAuditInput.setPage(page);
      return userSearchAuditInput;
   }

   private UserAccessAuditInput prepareUserAccesAuditInput (UIUserAccessAuditInput uiUserAccessAuditInput, Page page) {
      UserAccessAuditInput userAccessAuditInput = new UserAccessAuditInput();
      //Id -1 or empty userIds - All user
      //Id -2  - All registered user
      //Id -3 - All anonymous user
      //UserIds
      List<Long> userIds = uiUserAccessAuditInput.getUserIds();
      if (ExecueCoreUtil.isCollectionNotEmpty(userIds)) {
         if (userIds.get(0) == -2) {
            userAccessAuditInput.setAnonymousUser(CheckType.NO);
         } else if (userIds.get(0) == -3) {
            userAccessAuditInput.setAnonymousUser(CheckType.YES);
         } else if (userIds.get(0) == -1) {
            //do nothing
         } else {
            // set to maintain the uniqueness 
            Set<Long> userIdSet = new HashSet<Long>(uiUserAccessAuditInput.getUserIds());
            userAccessAuditInput.setUserIds(new ArrayList<Long>(userIdSet));
         }
      }
      // prepare operator and values
      String operator = uiUserAccessAuditInput.getOperator();
      List<Date> searchDateList = new ArrayList<Date>();
      if (operator != null) {
         userAccessAuditInput.setOperator(OperatorType.getOperatorType(operator));
         List<String> operands = uiUserAccessAuditInput.getOperands();
         prepareSearchDatesForOperator(searchDateList, operands);
      } else {
         userAccessAuditInput.setOperator(OperatorType.GREATER_THAN);
         // set default date time to get past 30 days records
         searchDateList.add(prepareDefaultAccessTime().getTime());
      }
      //Operands or Date time frame
      userAccessAuditInput.setSearchDates(searchDateList);

      //AuditlogType  
      if (ExecueCoreUtil.isCollectionNotEmpty(uiUserAccessAuditInput.getAuditLogTypeIds())) {
         // set to maintain the uniqueness 
         Set<String> auditLogTypeIds = new HashSet<String>(uiUserAccessAuditInput.getAuditLogTypeIds());
         userAccessAuditInput.setAuditLogTypes(prepareAuditLogTypes(new ArrayList<String>(auditLogTypeIds)));
      }
      //Page 
      userAccessAuditInput.setPage(page);
      return userAccessAuditInput;
   }

   private void prepareSearchDatesForOperator (List<Date> searchDateList, List<String> operands) {
      List<Date> tempAccessTimeList = getAccessedDateTime(operands);
      if (tempAccessTimeList.size() > 1) {
         Date fromDate = null;
         Date toDate = null;
         Date firstDate = tempAccessTimeList.get(0);
         Date secondDate = tempAccessTimeList.get(1);
         if (secondDate.compareTo(firstDate) > 0) {
            fromDate = getStartOfDay(firstDate);
            toDate = getEndOfDay(secondDate);
         } else {
            fromDate = getStartOfDay(secondDate);
            toDate = getEndOfDay(firstDate);
         }
         searchDateList.add(fromDate);
         searchDateList.add(toDate);
      } else {
         searchDateList.add(getStartOfDay(tempAccessTimeList.get(0)));
      }
   }

   private Calendar prepareDefaultAccessTime () {
      Calendar calendar = GregorianCalendar.getInstance();
      // Subtract 30 days from the current date       
      Integer auditTrailHistoryAccessTimeLimit = getAuditTrailConfigurationService()
               .getAuditTrailHistoryAccessTimeLimit();
      calendar.add(Calendar.DATE, -auditTrailHistoryAccessTimeLimit);
      calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
      calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
      calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
      return calendar;
   }

   private List<AuditLogType> prepareAuditLogTypes (List<String> uiAuditLogTypes) {
      List<AuditLogType> auditLogTypes = new ArrayList<AuditLogType>();
      if (ExecueCoreUtil.isCollectionNotEmpty(uiAuditLogTypes)) {
         for (String uiAuditLogType : uiAuditLogTypes) {
            auditLogTypes.add(AuditLogType.getType(uiAuditLogType));
         }

      }

      return auditLogTypes;
   }

   public Date getStartOfDay (Date day) {
      Calendar calendar = GregorianCalendar.getInstance();
      calendar.setTime(day);
      calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
      calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
      calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
      return calendar.getTime();
   }

   public Date getEndOfDay (Date day) {
      Calendar calendar = GregorianCalendar.getInstance();
      calendar.setTime(day);
      calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
      calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
      calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
      return calendar.getTime();
   }

   private List<Date> getAccessedDateTime (List<String> timeDuirations) {
      List<Date> accessedTimeList = new ArrayList<Date>();
      if (ExecueCoreUtil.isCollectionNotEmpty(timeDuirations)) {
         for (String accessDuiration : timeDuirations) {
            accessedTimeList.add(prepareAccessDate(accessDuiration));
         }
      }
      return accessedTimeList;
   }

   private Date prepareAccessDate (String accessDuiration) {
      String[] accessTimeArray = accessDuiration.split("-");
      String month = accessTimeArray[0];
      String day = accessTimeArray[1];
      String year = accessTimeArray[2];
      Calendar calendar = GregorianCalendar.getInstance();
      calendar.set(Calendar.YEAR, Integer.parseInt(year));
      calendar.set(Calendar.MONTH, calendarMonthMap.get(month));
      calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
      return calendar.getTime();
   }

   private UIUserSearchAudit transformIntoUIUserSearchAudit (UserSearchAuditOutput userSearchAudit) {
      UIUserSearchAudit uiUserSearchAudit = new UIUserSearchAudit();
      uiUserSearchAudit.setUserId(userSearchAudit.getUserId());
      uiUserSearchAudit.setUserQueryId(userSearchAudit.getUserQueryId());
      uiUserSearchAudit.setBusinessQueryId(userSearchAudit.getBusinessQueryId());
      uiUserSearchAudit.setAssetId(userSearchAudit.getAssetId());
      uiUserSearchAudit.setAppId(userSearchAudit.getAppId());
      uiUserSearchAudit.setAppName(userSearchAudit.getAppName());
      uiUserSearchAudit.setQueryString(userSearchAudit.getQueryString());
      uiUserSearchAudit.setAggregatedQueryIds(userSearchAudit.getAggregatedQueryIds());
      uiUserSearchAudit.setAggregatedQueryTypes(userSearchAudit.getAggregatedQueryTypes());
      uiUserSearchAudit.setAssetName(userSearchAudit.getAssetName());
      uiUserSearchAudit.setAnonymousUser(userSearchAudit.getAnonymousUser());
      if (CheckType.YES.equals(userSearchAudit.getAnonymousUser())) {
         uiUserSearchAudit.setAnonymUser(userSearchAudit.getAnonymUser());
      } else {
         uiUserSearchAudit.setUser(transformIntoUIUser(userSearchAudit.getUser()));
      }
      return uiUserSearchAudit;
   }

   private UIUserAccessAudit transformIntoUIUserAccessAudit (UserAccessAudit userAccessAudit) {
      UIUserAccessAudit uiUserAccessAudit = new UIUserAccessAudit();
      uiUserAccessAudit.setId(userAccessAudit.getId());
      uiUserAccessAudit.setUserId(userAccessAudit.getUserId());
      uiUserAccessAudit.setAccessedTime(userAccessAudit.getAccessedTime());
      uiUserAccessAudit.setAuditLogType(userAccessAudit.getAuditLogType());
      uiUserAccessAudit.setIpLocation(userAccessAudit.getIpLocation());
      uiUserAccessAudit.setAccessedSystemType(userAccessAudit.getAccessedSystemType());
      uiUserAccessAudit.setComments(userAccessAudit.getComments());
      uiUserAccessAudit.setAnonymousUser(userAccessAudit.getAnonymousUser());
      if (CheckType.YES.equals(userAccessAudit.getAnonymousUser())) {
         uiUserAccessAudit.setAnonymUser(userAccessAudit.getAnonymUser());
      } else {
         uiUserAccessAudit.setUser(transformIntoUIUser(userAccessAudit.getUser()));
      }
      return uiUserAccessAudit;
   }

   private static void populateCalendarMonthMap (Map<String, Integer> calendarMonthMap) {
      calendarMonthMap.put("Jan", 0);
      calendarMonthMap.put("Feb", 1);
      calendarMonthMap.put("Mar", 2);
      calendarMonthMap.put("Apr", 3);
      calendarMonthMap.put("May", 4);
      calendarMonthMap.put("Jun", 5);
      calendarMonthMap.put("Jul", 6);
      calendarMonthMap.put("Aug", 7);
      calendarMonthMap.put("Sep", 8);
      calendarMonthMap.put("Oct", 9);
      calendarMonthMap.put("Nov", 10);
      calendarMonthMap.put("Dec", 11);

   }

   /**
    * @return the userManagementService
    */
   public IUserManagementService getUserManagementService () {
      return userManagementService;
   }

   /**
    * @param userManagementService the userManagementService to set
    */
   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   /**
    * @return the auditTrailConfigurationService
    */
   public IAuditTrailConfigurationService getAuditTrailConfigurationService () {
      return auditTrailConfigurationService;
   }

   /**
    * @param auditTrailConfigurationService the auditTrailConfigurationService to set
    */
   public void setAuditTrailConfigurationService (IAuditTrailConfigurationService auditTrailConfigurationService) {
      this.auditTrailConfigurationService = auditTrailConfigurationService;
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
