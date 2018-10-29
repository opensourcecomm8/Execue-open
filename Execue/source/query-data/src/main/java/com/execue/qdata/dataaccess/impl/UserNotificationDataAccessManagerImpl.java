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


package com.execue.qdata.dataaccess.impl;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.qdata.Notification;
import com.execue.core.common.bean.qdata.NotificationDetail;
import com.execue.core.common.bean.qdata.NotificationTemplate;
import com.execue.core.common.type.NotificationType;
import com.execue.core.common.type.TemplateType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.qdata.dataaccess.IUserNotificationDataAccessManager;
import com.execue.qdata.dataaccess.QDataDAOComponents;
import com.execue.qdata.exception.UserNotificationException;
import com.execue.qdata.exception.UserNotificationExceptionCodes;

public class UserNotificationDataAccessManagerImpl extends QDataDAOComponents implements
         IUserNotificationDataAccessManager {

   public NotificationTemplate getUserNotificationTemplate (NotificationType notificationType, TemplateType templateType)
            throws UserNotificationException {
      try {
         return getUserNotificationDAO().getUserNotificationTemplate(notificationType, templateType);
      } catch (DataAccessException e) {
         throw new UserNotificationException(UserNotificationExceptionCodes.NOTIFICATION_TEMPLATE_RETRIEVAL_FAILED, e);
      }
   }

   public List<Notification> getUserNotifications (Long userId) throws UserNotificationException {
      try {
         return getUserNotificationDAO().getUserNotifications(userId);
      } catch (DataAccessException e) {
         throw new UserNotificationException(UserNotificationExceptionCodes.NOTIFICATION_RETRIEVAL_FAILED, e);
      }
   }

   public void createUserNotification (Notification notification) throws UserNotificationException {
      try {
         getUserNotificationDAO().create(notification);
      } catch (DataAccessException e) {
         throw new UserNotificationException(UserNotificationExceptionCodes.NOTIFICATION_CREATION_FAILED, e);
      }

   }

   public void createUserNotificationDetails (List<NotificationDetail> notificationDetails)
            throws UserNotificationException {
      try {
         getUserNotificationDAO().createAll(notificationDetails);
      } catch (DataAccessException e) {
         throw new UserNotificationException(UserNotificationExceptionCodes.NOTIFICATION_DETAIL_CREATION_FAILED, e);
      }

   }

   public List<NotificationDetail> getUserNotificationDetails (Long notificationId) throws UserNotificationException {
      try {
         return getUserNotificationDAO().getUserNotificationDetails(notificationId);
      } catch (DataAccessException e) {
         throw new UserNotificationException(UserNotificationExceptionCodes.NOTIFICATION_DETAIL_RETRIEVAL_FAILED, e);
      }
   }

   public Map<TemplateType, String> getUserNotificationTemplateParams (NotificationType notificationType)
            throws UserNotificationException {
      try {
         return getUserNotificationDAO().getUserNotificationTemplateParams(notificationType);
      } catch (DataAccessException e) {
         throw new UserNotificationException(UserNotificationExceptionCodes.NOTIFICATION_DETAIL_RETRIEVAL_FAILED, e);
      }
   }

   public List<Notification> getUserNotificationsByPage (Long userId, Long pageNumber, Long pageSize)
            throws UserNotificationException {
      try {
         return getUserNotificationDAO().getUserNotificationsByPage(userId, pageNumber, pageSize);
      } catch (DataAccessException e) {
         throw new UserNotificationException(UserNotificationExceptionCodes.NOTIFICATION_RETRIEVAL_FAILED, e);
      }
   }

   public Long getUserNotificationsCount (Long userId) throws UserNotificationException {
      try {
         return getUserNotificationDAO().getUserNotificationsCount(userId);
      } catch (DataAccessException e) {
         throw new UserNotificationException(UserNotificationExceptionCodes.NOTIFICATION_RETRIEVAL_FAILED, e);
      }
   }

   public void saveUpdateNotificationTemplate (NotificationTemplate notificationTemplate)
            throws UserNotificationException {
      try {
         getUserNotificationDAO().saveOrUpdate(notificationTemplate);
      } catch (DataAccessException e) {
         throw new UserNotificationException(UserNotificationExceptionCodes.NOTIFICATION_RETRIEVAL_FAILED, e);
      }
   }

}
