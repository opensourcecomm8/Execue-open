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


package com.execue.handler.qdata;

import java.util.List;

import com.execue.core.common.bean.qdata.NotificationTemplate;
import com.execue.core.common.type.NotificationType;
import com.execue.core.common.type.TemplateType;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UINotificationInfo;

public interface IUserNotificationHandler {

   public List<UINotificationInfo> getUserNotifications () throws HandlerException;

   public String getUserNotificationDetail (Long notificationId, NotificationType notificationType)
            throws HandlerException;

   public Long getUserNotificationsCount () throws HandlerException;

   public List<UINotificationInfo> getUserNotificationsByPage (Long pageNumber, Long pageSize) throws HandlerException;

   public NotificationTemplate getUserNotificationTemplate (NotificationType notificationType, TemplateType templateType)
            throws HandlerException;

   public void saveUpdateNotificationTemplate (NotificationTemplate notificationTemplate) throws HandlerException;

}
