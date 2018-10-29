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


package com.execue.core.common.bean.audittrail;

import java.util.List;

import com.execue.core.common.bean.entity.audittrail.AnonymousUser;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.CheckType;

public class UserSearchAuditOutput {

   private Long          userId;

   private Long          userQueryId;

   private Long          businessQueryId;

   private Long          assetId;

   private Long          appId;

   private String        appName;

   private String        queryString;

   private List<Long>    aggregatedQueryIds;

   private List<Long>    aggregatedQueryTypes;

   private CheckType     anonymousUser = CheckType.NO;

   // these three fields needs to be filled in wrapper service.
   private User          user;

   private AnonymousUser anonymUser;

   private String        assetName;

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

   public Long getUserQueryId () {
      return userQueryId;
   }

   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public String getAssetName () {
      return assetName;
   }

   public void setAssetName (String assetName) {
      this.assetName = assetName;
   }

   public Long getAppId () {
      return appId;
   }

   public void setAppId (Long appId) {
      this.appId = appId;
   }

   public String getAppName () {
      return appName;
   }

   public void setAppName (String appName) {
      this.appName = appName;
   }

   public String getQueryString () {
      return queryString;
   }

   public void setQueryString (String queryString) {
      this.queryString = queryString;
   }

   public List<Long> getAggregatedQueryIds () {
      return aggregatedQueryIds;
   }

   public void setAggregatedQueryIds (List<Long> aggregatedQueryIds) {
      this.aggregatedQueryIds = aggregatedQueryIds;
   }

   public CheckType getAnonymousUser () {
      return anonymousUser;
   }

   public void setAnonymousUser (CheckType anonymousUser) {
      this.anonymousUser = anonymousUser;
   }

   public Long getBusinessQueryId () {
      return businessQueryId;
   }

   public void setBusinessQueryId (Long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }

   public List<Long> getAggregatedQueryTypes () {
      return aggregatedQueryTypes;
   }

   public void setAggregatedQueryTypes (List<Long> aggregatedQueryTypes) {
      this.aggregatedQueryTypes = aggregatedQueryTypes;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

}
