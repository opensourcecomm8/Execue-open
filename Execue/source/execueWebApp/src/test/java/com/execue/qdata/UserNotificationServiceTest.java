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


package com.execue.qdata;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.qdata.Notification;
import com.execue.core.common.bean.qdata.NotificationDetail;
import com.execue.core.common.bean.qdata.NotificationTemplate;
import com.execue.core.common.type.NotificationCategory;
import com.execue.core.common.type.NotificationParamName;
import com.execue.core.common.type.NotificationType;
import com.execue.core.common.type.TemplateType;
import com.execue.core.exception.UserNotificationException;

public class UserNotificationServiceTest extends ExeCueBaseTest {

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTestTeardown();
   }

   @Test
   public void testGetUserNotifications () throws UserNotificationException {
      List<Notification> userNotifications = getUserNotificationService().getUserNotifications(1L);
      System.out.println(userNotifications.size());
   }

   @Test
   public void testGetUserNotificationDetails () throws UserNotificationException {
      List<NotificationDetail> userNotificationDetails = getUserNotificationService().getUserNotificationDetails(1004L);
      System.out.println(userNotificationDetails.size());
   }

   @Test
   public void testGetUserNotificationTemplate () {
      try {
         NotificationTemplate userNotificationTemplate = getUserNotificationService().getUserNotificationTemplate(
                  NotificationType.CUBE_CREATION, TemplateType.BODY_CONTENT);
         System.out.println("Template " + userNotificationTemplate.getTemplate());
      } catch (UserNotificationException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testGetUserNotificationBody () throws UserNotificationException {
      String userNotificationBody = getUserNotificationService().getUserNotificationBody(1035L,
               NotificationType.CUBE_CREATION);
      System.out.println(userNotificationBody);
   }

   @Test
   public void testCreateUserNotificationWithDetails () throws UserNotificationException {
      Map<NotificationParamName, String> subjectParams = new HashMap<NotificationParamName, String>();
      Map<NotificationParamName, String> bodyParams = new HashMap<NotificationParamName, String>();
      subjectParams.put(NotificationParamName.ASSET_NAME, "TestAsset");
      bodyParams.put(NotificationParamName.ASSET_NAME, "TestAsset");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
      getUserNotificationService().createUserNotification(NotificationType.CUBE_CREATION,
               NotificationCategory.OFFLINE_JOB, 1L, subjectParams, bodyParams);
   }
}
