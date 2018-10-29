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


package com.execue.handler.bean;

import java.util.Date;

import com.execue.core.common.type.NotificationCategory;
import com.execue.core.common.type.NotificationType;

public class UINotificationInfo {

   private Long                 id;
   private Date                 createdDate;
   private String               subject;
   private NotificationType     notificationType;
   private NotificationCategory category;
   
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
   
   public String getSubject () {
      return subject;
   }
   
   public void setSubject (String subject) {
      this.subject = subject;
   }
   
   public NotificationType getNotificationType () {
      return notificationType;
   }
   
   public void setNotificationType (NotificationType notificationType) {
      this.notificationType = notificationType;
   }
   
   public NotificationCategory getCategory () {
      return category;
   }
   
   public void setCategory (NotificationCategory category) {
      this.category = category;
   }
}
