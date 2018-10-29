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


package com.execue.web.core.action.account;

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UINotificationInfo;
import com.execue.handler.qdata.IUserNotificationHandler;
import com.execue.web.core.action.swi.SWIPaginationAction;

/**
 * @author jitendra
 */
public class UserNotificationAction extends SWIPaginationAction {

   private static final long        serialVersionUID = 1L;
   private List<UINotificationInfo> notificationInfo;
   private UINotificationInfo       selectedNotification;
   private String                   notificationBody;
   private IUserNotificationHandler userNotificationHandler;
   private static final int         PAGE_SIZE        = 5;
   private static final int         NUMBER_OF_LINKS  = 15;
   public static final Logger       logger           = Logger.getLogger(UserNotificationAction.class);

   public String getUserNotifications () {
      try {
         setNotificationInfo(getUserNotificationHandler().getUserNotifications());
         return SUCCESS;
      } catch (HandlerException handlerException) {
         return ERROR;
      }
   }

   public String getNotificationBodyContent () {
      try {
         setNotificationBody(getUserNotificationHandler().getUserNotificationDetail(selectedNotification.getId(),
                  selectedNotification.getNotificationType()));
         return SUCCESS;
      } catch (HandlerException handlerException) {
         return ERROR;
      }
   }

   public List<UINotificationInfo> getNotificationInfo () {
      return notificationInfo;
   }

   public void setNotificationInfo (List<UINotificationInfo> notificationInfo) {
      this.notificationInfo = notificationInfo;
   }

   public UINotificationInfo getSelectedNotification () {
      return selectedNotification;
   }

   public void setSelectedNotification (UINotificationInfo selectedNotification) {
      this.selectedNotification = selectedNotification;
   }

   public IUserNotificationHandler getUserNotificationHandler () {
      return userNotificationHandler;
   }

   public void setUserNotificationHandler (IUserNotificationHandler userNotificationHandler) {
      this.userNotificationHandler = userNotificationHandler;
   }

   public void setNotificationBody (String notificationBody) {
      this.notificationBody = notificationBody;
   }

   public String getNotificationBody () {
      return notificationBody;
   }

   @Override
   public String processPage () throws ExeCueException {
      try {
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
         getPageDetail().setRecordCount(getUserNotificationHandler().getUserNotificationsCount());
         setNotificationInfo(getUserNotificationHandler().getUserNotificationsByPage(
                  getPageDetail().getRequestedPage(), Long.valueOf(PAGE_SIZE)));
         if (logger.isDebugEnabled()) {
            logger.debug(getPageDetail().toString());
         }
         getHttpRequest().put(PAGINATION, getPageDetail());
         return SUCCESS;

      } catch (Exception exception) {
         logger.error(exception, exception);
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
   }

}
