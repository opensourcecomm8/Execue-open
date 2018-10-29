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


package com.execue.qdata.dataaccess;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.qdata.Notification;
import com.execue.core.common.bean.qdata.NotificationDetail;
import com.execue.core.common.bean.qdata.NotificationTemplate;
import com.execue.core.common.type.NotificationType;
import com.execue.core.common.type.TemplateType;
import com.execue.qdata.exception.UserNotificationException;

public interface IUserNotificationDataAccessManager {

   NotificationTemplate getUserNotificationTemplate (NotificationType notificationType, TemplateType templateType)
            throws UserNotificationException;

   List<Notification> getUserNotifications (Long userId) throws UserNotificationException;

   List<NotificationDetail> getUserNotificationDetails (Long notificationId) throws UserNotificationException;

   void createUserNotification (Notification notification) throws UserNotificationException;

   void createUserNotificationDetails (List<NotificationDetail> notificationDetails) throws UserNotificationException;

   Map<TemplateType, String> getUserNotificationTemplateParams (NotificationType notificationType)
            throws UserNotificationException;

   /**
    * This method return the notification count by user
    * 
    * @param userId
    * @return
    * @throws UserNotificationException
    */
   public Long getUserNotificationsCount (Long userId) throws UserNotificationException;

   /**
    * This method return the notifications by user and pagenumber
    * 
    * @param userId
    * @param pageNumber
    * @return
    * @throws UserNotificationException
    */
   public List<Notification> getUserNotificationsByPage (Long userId, Long pageNumber, Long pageSize)
            throws UserNotificationException;

   void saveUpdateNotificationTemplate (NotificationTemplate notificationTemplate) throws UserNotificationException;

}
