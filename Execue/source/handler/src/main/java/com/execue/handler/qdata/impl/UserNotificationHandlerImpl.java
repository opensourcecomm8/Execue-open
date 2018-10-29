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


package com.execue.handler.qdata.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.qdata.Notification;
import com.execue.core.common.bean.qdata.NotificationTemplate;
import com.execue.core.common.type.NotificationParamName;
import com.execue.core.common.type.NotificationType;
import com.execue.core.common.type.TemplateType;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UINotificationInfo;
import com.execue.handler.qdata.IUserNotificationHandler;
import com.execue.qdata.exception.UserNotificationException;
import com.execue.qdata.service.IUserNotificationService;
import com.execue.security.UserContextService;

public class UserNotificationHandlerImpl extends UserContextService implements IUserNotificationHandler {

   private IUserNotificationService userNotificationService;

   public String getUserNotificationDetail (Long notificationId, NotificationType notificationType)
            throws HandlerException {
      try {
         return getUserNotificationService().getUserNotificationBody(notificationId, notificationType);
      } catch (UserNotificationException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<UINotificationInfo> getUserNotifications () throws HandlerException {
      try {
         List<Notification> userNotifications = getUserNotificationService().getUserNotifications(
                  getUserContext().getUser().getId());
         return tranformToUINotifications(userNotifications);
      } catch (UserNotificationException e) {
         throw new HandlerException(e.getCode(), e);
      }

   }

   public List<UINotificationInfo> getUserNotificationsByPage (Long pageNumber, Long pageSize) throws HandlerException {
      try {
         List<Notification> userNotifications = getUserNotificationService().getUserNotificationsByPage(
                  getUserContext().getUser().getId(), pageNumber, pageSize);
         return tranformToUINotifications(userNotifications);
      } catch (UserNotificationException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public Long getUserNotificationsCount () throws HandlerException {
      try {
         return getUserNotificationService().getUserNotificationsCount(getUserContext().getUser().getId());
      } catch (UserNotificationException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   private List<UINotificationInfo> tranformToUINotifications (List<Notification> notifications) {
      List<UINotificationInfo> uiNotifications = new ArrayList<UINotificationInfo>();
      for (Notification notification : notifications) {
         UINotificationInfo uiNotification = new UINotificationInfo();
         uiNotification.setId(notification.getId());
         uiNotification.setCreatedDate(notification.getCreatedDate());
         uiNotification.setCategory(notification.getCategory());
         uiNotification.setNotificationType(notification.getType());
         uiNotification.setSubject(notification.getSubject());
         uiNotifications.add(uiNotification);
      }
      return uiNotifications;
   }

   public IUserNotificationService getUserNotificationService () {
      return userNotificationService;
   }

   public void setUserNotificationService (IUserNotificationService userNotificationService) {
      this.userNotificationService = userNotificationService;
   }

   public NotificationTemplate getUserNotificationTemplate (NotificationType notificationType, TemplateType templateType)
            throws HandlerException {
      try {
         return userNotificationService.getUserNotificationTemplate(notificationType, templateType);
      } catch (UserNotificationException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public void saveUpdateNotificationTemplate (NotificationTemplate notificationTemplate) throws HandlerException {
      StringBuilder notificationTemplateParams = new StringBuilder();
      String template = notificationTemplate.getTemplate();
      String[] tokens = template.split("\\s");
      for (String token : tokens) {
         if (token.startsWith("&")) {
            token = token.substring(1);
            if (NotificationParamName.getType(token) != null) {
               notificationTemplateParams.append(token);
               notificationTemplateParams.append("~");
            }
         }
      }
      if (notificationTemplateParams.length() > 0) {
         notificationTemplateParams.deleteCharAt(notificationTemplateParams.length() - 1);
      }
      notificationTemplate.setParamNames(notificationTemplateParams.toString());
      try {
         userNotificationService.saveUpdateNotificationTemplate(notificationTemplate);
      } catch (UserNotificationException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }
}
