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


package com.execue.core.common.bean.qdata;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.execue.core.common.type.NotificationCategory;
import com.execue.core.common.type.NotificationType;

public class Notification implements Serializable {

   private Long                    id;
   private Date                    createdDate;
   private NotificationCategory    category;
   private NotificationType        type;
   private String                  subject;
   private Long                    userId;
   private Set<NotificationDetail> notificationDetails;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Date getCreatedDate () {
      return createdDate;
   }

   public void setCreatedDate (Date createdDate) {
      this.createdDate = createdDate;
   }

   public NotificationCategory getCategory () {
      return category;
   }

   public void setCategory (NotificationCategory category) {
      this.category = category;
   }

   public NotificationType getType () {
      return type;
   }

   public void setType (NotificationType type) {
      this.type = type;
   }

   public String getSubject () {
      return subject;
   }

   public void setSubject (String subject) {
      this.subject = subject;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

   public Set<NotificationDetail> getNotificationDetails () {
      return notificationDetails;
   }

   public void setNotificationDetails (Set<NotificationDetail> notificationDetails) {
      this.notificationDetails = notificationDetails;
   }

}
