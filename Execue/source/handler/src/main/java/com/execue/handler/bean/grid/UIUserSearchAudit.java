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

import java.util.List;

import com.execue.core.common.bean.entity.audittrail.AnonymousUser;
import com.execue.core.common.type.CheckType;
import com.execue.handler.bean.UIUser;

/**
 * This object represents the user search audit result
 * @author Jitendra
 *
 */
public class UIUserSearchAudit implements IGridBean {

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
   private UIUser        user;

   private AnonymousUser anonymUser;

   private String        assetName;

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
    * @return the userQueryId
    */
   public Long getUserQueryId () {
      return userQueryId;
   }

   /**
    * @param userQueryId the userQueryId to set
    */
   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   /**
    * @return the businessQueryId
    */
   public Long getBusinessQueryId () {
      return businessQueryId;
   }

   /**
    * @param businessQueryId the businessQueryId to set
    */
   public void setBusinessQueryId (Long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }

   /**
    * @return the assetId
    */
   public Long getAssetId () {
      return assetId;
   }

   /**
    * @param assetId the assetId to set
    */
   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   /**
    * @return the appId
    */
   public Long getAppId () {
      return appId;
   }

   /**
    * @param appId the appId to set
    */
   public void setAppId (Long appId) {
      this.appId = appId;
   }

   /**
    * @return the appName
    */
   public String getAppName () {
      return appName;
   }

   /**
    * @param appName the appName to set
    */
   public void setAppName (String appName) {
      this.appName = appName;
   }

   /**
    * @return the queryString
    */
   public String getQueryString () {
      return queryString;
   }

   /**
    * @param queryString the queryString to set
    */
   public void setQueryString (String queryString) {
      this.queryString = queryString;
   }

   /**
    * @return the aggregatedQueryIds
    */
   public List<Long> getAggregatedQueryIds () {
      return aggregatedQueryIds;
   }

   /**
    * @param aggregatedQueryIds the aggregatedQueryIds to set
    */
   public void setAggregatedQueryIds (List<Long> aggregatedQueryIds) {
      this.aggregatedQueryIds = aggregatedQueryIds;
   }

   /**
    * @return the aggregatedQueryTypes
    */
   public List<Long> getAggregatedQueryTypes () {
      return aggregatedQueryTypes;
   }

   /**
    * @param aggregatedQueryTypes the aggregatedQueryTypes to set
    */
   public void setAggregatedQueryTypes (List<Long> aggregatedQueryTypes) {
      this.aggregatedQueryTypes = aggregatedQueryTypes;
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
    * @return the assetName
    */
   public String getAssetName () {
      return assetName;
   }

   /**
    * @param assetName the assetName to set
    */
   public void setAssetName (String assetName) {
      this.assetName = assetName;
   }

}
