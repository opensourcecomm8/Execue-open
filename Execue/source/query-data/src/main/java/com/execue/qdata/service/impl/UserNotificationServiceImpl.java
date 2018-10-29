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


package com.execue.qdata.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.qdata.Notification;
import com.execue.core.common.bean.qdata.NotificationDetail;
import com.execue.core.common.bean.qdata.NotificationTemplate;
import com.execue.core.common.type.NotificationCategory;
import com.execue.core.common.type.NotificationParamName;
import com.execue.core.common.type.NotificationType;
import com.execue.core.common.type.TemplateType;
import com.execue.qdata.exception.UserNotificationException;
import com.execue.qdata.dataaccess.IUserNotificationDataAccessManager;
import com.execue.qdata.service.IUserNotificationService;

public class UserNotificationServiceImpl implements IUserNotificationService {

   private IUserNotificationDataAccessManager userNotificationDataAccessManager;

   public IUserNotificationDataAccessManager getUserNotificationDataAccessManager () {
      return userNotificationDataAccessManager;
   }

   public void setUserNotificationDataAccessManager (
            IUserNotificationDataAccessManager userNotificationDataAccessManager) {
      this.userNotificationDataAccessManager = userNotificationDataAccessManager;
   }

   public List<NotificationDetail> getUserNotificationDetails (Long notificationId) throws UserNotificationException {
      return getUserNotificationDataAccessManager().getUserNotificationDetails(notificationId);
   }

   public NotificationTemplate getUserNotificationTemplate (NotificationType notificationType, TemplateType templateType)
            throws UserNotificationException {
      return getUserNotificationDataAccessManager().getUserNotificationTemplate(notificationType, templateType);
   }

   public List<Notification> getUserNotifications (Long userId) throws UserNotificationException {
      return getUserNotificationDataAccessManager().getUserNotifications(userId);
   }

   public String getUserNotificationBody (Long notificationId, NotificationType notificationType)
            throws UserNotificationException {
      List<NotificationDetail> userNotificationDetails = getUserNotificationDetails(notificationId);
      Map<NotificationParamName, String> params = new HashMap<NotificationParamName, String>();
      for (NotificationDetail notificationDetail : userNotificationDetails) {
         params.put(notificationDetail.getParamName(), notificationDetail.getParamValue());
      }
      return buildNotificationTemplate(params, notificationType, TemplateType.BODY_CONTENT);
   }

   private String buildNotificationTemplate (Map<NotificationParamName, String> params,
            NotificationType notificationType, TemplateType templateType) throws UserNotificationException {
      NotificationTemplate userNotificationTemplate = getUserNotificationTemplate(notificationType, templateType);
      String template = userNotificationTemplate.getTemplate();
      for (NotificationParamName paramName : params.keySet()) {
         template = template.replaceAll("&" + paramName.getValue(), params.get(paramName));
      }
      return template;
   }

   public Notification createUserNotification (NotificationType type, NotificationCategory category, Long userId,
            Map<NotificationParamName, String> subjectParams, Map<NotificationParamName, String> bodyParams)
            throws UserNotificationException {
      Notification notification = new Notification();
      notification.setUserId(userId);
      notification.setType(type);
      notification.setCategory(category);
      notification.setCreatedDate(new Date());
      notification.setSubject(buildNotificationTemplate(subjectParams, type, TemplateType.SUBJECT));
      Set<NotificationDetail> notificationDetails = new HashSet<NotificationDetail>();
      for (NotificationParamName notificationParamName : bodyParams.keySet()) {
         NotificationDetail notificationDetail = new NotificationDetail();
         notificationDetail.setParamName(notificationParamName);
         notificationDetail.setParamValue(bodyParams.get(notificationParamName));
         notificationDetail.setNotification(notification);
         notificationDetails.add(notificationDetail);
      }
      notification.setNotificationDetails(notificationDetails);
      getUserNotificationDataAccessManager().createUserNotification(notification);
      return notification;
   }

   public Map<TemplateType, String> getUserNotificationTemplateParams (NotificationType notificationType)
            throws UserNotificationException {
      return getUserNotificationDataAccessManager().getUserNotificationTemplateParams(notificationType);
   }

   public List<Notification> getUserNotificationsByPage (Long userId, Long pageNumber, Long pageSize)
            throws UserNotificationException {
      return getUserNotificationDataAccessManager().getUserNotificationsByPage(userId, pageNumber, pageSize);
   }

   public Long getUserNotificationsCount (Long userId) throws UserNotificationException {
      return getUserNotificationDataAccessManager().getUserNotificationsCount(userId);
   }

   public void saveUpdateNotificationTemplate (NotificationTemplate notificationTemplate)
            throws UserNotificationException {
      userNotificationDataAccessManager.saveUpdateNotificationTemplate(notificationTemplate);

   }
}
