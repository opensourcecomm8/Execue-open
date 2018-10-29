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


package com.execue.core.common.bean.entity.audittrail;

import java.io.Serializable;
import java.util.Date;

import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.AuditLogType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ExeCueSystemType;

/**
 * @author Raju Gottumukkala
 */
public class UserAccessAudit implements Serializable {

   private Long                    id;
   private Long                    userId;
   private Date                    accessedTime;
   private String                  ipLocation;
   private AuditLogType            auditLogType;
   private ExeCueSystemType        accessedSystemType;
   private CheckType               anonymousUser = CheckType.NO;
   private String                  comments;
   private transient User          user;
   private transient AnonymousUser anonymUser;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

   public Date getAccessedTime () {
      return accessedTime;
   }

   public void setAccessedTime (Date accessedTime) {
      this.accessedTime = accessedTime;
   }

   public String getIpLocation () {
      return ipLocation;
   }

   public void setIpLocation (String ipLocation) {
      this.ipLocation = ipLocation;
   }

   public AuditLogType getAuditLogType () {
      return auditLogType;
   }

   public void setAuditLogType (AuditLogType auditLogType) {
      this.auditLogType = auditLogType;
   }

   public ExeCueSystemType getAccessedSystemType () {
      return accessedSystemType;
   }

   public void setAccessedSystemType (ExeCueSystemType accessedSystemType) {
      this.accessedSystemType = accessedSystemType;
   }

   public CheckType getAnonymousUser () {
      return anonymousUser;
   }

   public void setAnonymousUser (CheckType anonymousUser) {
      this.anonymousUser = anonymousUser;
   }

   public String getComments () {
      return comments;
   }

   public void setComments (String comments) {
      this.comments = comments;
   }

   public User getUser () {
      return user;
   }

   public void setUser (User user) {
      this.user = user;
   }

   public AnonymousUser getAnonymUser () {
      return anonymUser;
   }

   public void setAnonymUser (AnonymousUser anonymUser) {
      this.anonymUser = anonymUser;
   }

}
