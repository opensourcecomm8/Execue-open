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


package com.execue.handler.bean.grid;

import java.util.Date;

import com.execue.core.common.bean.entity.audittrail.AnonymousUser;
import com.execue.core.common.type.AuditLogType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ExeCueSystemType;
import com.execue.handler.bean.UIUser;

public class UIUserAccessAudit implements IGridBean {

   private Long             id;
   private Long             userId;
   private Date             accessedTime;
   private String           ipLocation;
   private AuditLogType     auditLogType;
   private ExeCueSystemType accessedSystemType;
   private CheckType        anonymousUser = CheckType.NO;
   private String           comments;
   private UIUser           user;
   private AnonymousUser    anonymUser;

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the userId
    */
   public Long getUserId () {
      return userId;
   }

   /**
    * @param userId the userId to set
    */
   public void setUserId (Long userId) {
      this.userId = userId;
   }

   /**
    * @return the accessedTime
    */
   public Date getAccessedTime () {
      return accessedTime;
   }

   /**
    * @param accessedTime the accessedTime to set
    */
   public void setAccessedTime (Date accessedTime) {
      this.accessedTime = accessedTime;
   }

   /**
    * @return the ipLocation
    */
   public String getIpLocation () {
      return ipLocation;
   }

   /**
    * @param ipLocation the ipLocation to set
    */
   public void setIpLocation (String ipLocation) {
      this.ipLocation = ipLocation;
   }

   /**
    * @return the auditLogType
    */
   public AuditLogType getAuditLogType () {
      return auditLogType;
   }

   /**
    * @param auditLogType the auditLogType to set
    */
   public void setAuditLogType (AuditLogType auditLogType) {
      this.auditLogType = auditLogType;
   }

   /**
    * @return the accessedSystemType
    */
   public ExeCueSystemType getAccessedSystemType () {
      return accessedSystemType;
   }

   /**
    * @param accessedSystemType the accessedSystemType to set
    */
   public void setAccessedSystemType (ExeCueSystemType accessedSystemType) {
      this.accessedSystemType = accessedSystemType;
   }

   /**
    * @return the anonymousUser
    */
   public CheckType getAnonymousUser () {
      return anonymousUser;
   }

   /**
    * @param anonymousUser the anonymousUser to set
    */
   public void setAnonymousUser (CheckType anonymousUser) {
      this.anonymousUser = anonymousUser;
   }

   /**
    * @return the comments
    */
   public String getComments () {
      return comments;
   }

   /**
    * @param comments the comments to set
    */
   public void setComments (String comments) {
      this.comments = comments;
   }

   /**
    * @return the anonymUser
    */
   public AnonymousUser getAnonymUser () {
      return anonymUser;
   }

   /**
    * @param anonymUser the anonymUser to set
    */
   public void setAnonymUser (AnonymousUser anonymUser) {
      this.anonymUser = anonymUser;
   }

   /**
    * @return the user
    */
   public UIUser getUser () {
      return user;
   }

   /**
    * @param user the user to set
    */
   public void setUser (UIUser user) {
      this.user = user;
   }
}
