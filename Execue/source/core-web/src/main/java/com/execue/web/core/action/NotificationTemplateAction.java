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


package com.execue.web.core.action;

import java.util.Arrays;
import java.util.List;

import com.execue.core.common.bean.qdata.NotificationTemplate;
import com.execue.core.common.type.NotificationParamName;
import com.execue.core.common.type.NotificationType;
import com.execue.core.common.type.TemplateType;
import com.execue.core.exception.HandlerException;
import com.execue.handler.qdata.IUserNotificationHandler;
import com.execue.web.core.action.swi.SWIAction;

public class NotificationTemplateAction extends SWIAction {

   private static final long        serialVersionUID = 1L;
   private NotificationTemplate     notificationTemplate;
   private IUserNotificationHandler userNotificationHandler;

   public String populateNotificationTemplate () {
      try {
         NotificationTemplate userNotificationTemplate = getUserNotificationHandler().getUserNotificationTemplate(
                  notificationTemplate.getNotificationType(), notificationTemplate.getTemplateType());
         if (userNotificationTemplate != null) {
            setNotificationTemplate(userNotificationTemplate);
         }
         return SUCCESS;
      } catch (HandlerException e) {
         return ERROR;
      }
   }

   public String saveUpdateNotificationTemplate () {
      try {
         getUserNotificationHandler().saveUpdateNotificationTemplate(notificationTemplate);
         notificationTemplate.setMessage(getText("execue.notification.template.update.success"));
         setNotificationTemplate(notificationTemplate);
         return SUCCESS;
      } catch (HandlerException e) {
         notificationTemplate.setErrorMessage(getText("execue.notification.template.update.failed"));
         return SUCCESS;
      }
   }

   /**
    * @return the notificationTemplate
    */
   public NotificationTemplate getNotificationTemplate () {
      return notificationTemplate;
   }

   /**
    * @param notificationTemplate
    *           the notificationTemplate to set
    */
   public void setNotificationTemplate (NotificationTemplate notificationTemplate) {
      this.notificationTemplate = notificationTemplate;
   }

   /**
    * @return the userNotificationHandler
    */
   public IUserNotificationHandler getUserNotificationHandler () {
      return userNotificationHandler;
   }

   /**
    * @param userNotificationHandler
    *           the userNotificationHandler to set
    */
   public void setUserNotificationHandler (IUserNotificationHandler userNotificationHandler) {
      this.userNotificationHandler = userNotificationHandler;
   }

   public List<TemplateType> getTemplateTypes () {
      return Arrays.asList(TemplateType.values());
   }

   public List<NotificationType> getNotificationTypes () {
      return Arrays.asList(NotificationType.values());
   }

   public List<NotificationParamName> getNotificationParamNames () {
      return Arrays.asList(NotificationParamName.values());

   }
}
