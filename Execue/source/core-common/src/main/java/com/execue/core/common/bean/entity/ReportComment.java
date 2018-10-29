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


package com.execue.core.common.bean.entity;

import java.io.Serializable;
import java.util.Date;

import com.execue.core.common.type.CheckType;

/**
 * @author JTiwari
 * @since 23/02/2010
 */
public class ReportComment implements Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private Long              queryId;
   private Long              userId;
   private Long              assetId;
   private String            queryHash;
   private String            comment;
   private Date              createdDate;
   private String            userName;
   private CheckType         isPublic;
   private CheckType         abuseReoprt;

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the queryId
    */
   public Long getQueryId () {
      return queryId;
   }

   /**
    * @param queryId
    *           the queryId to set
    */
   public void setQueryId (Long queryId) {
      this.queryId = queryId;
   }

   /**
    * @return the userId
    */
   public Long getUserId () {
      return userId;
   }

   /**
    * @param userId
    *           the userId to set
    */
   public void setUserId (Long userId) {
      this.userId = userId;
   }

   /**
    * @return the queryHash
    */
   public String getQueryHash () {
      return queryHash;
   }

   /**
    * @param queryHash
    *           the queryHash to set
    */
   public void setQueryHash (String queryHash) {
      this.queryHash = queryHash;
   }

   /**
    * @return the createdDate
    */
   public Date getCreatedDate () {
      return createdDate;
   }

   /**
    * @param createdDate
    *           the createdDate to set
    */
   public void setCreatedDate (Date createdDate) {
      this.createdDate = createdDate;
   }

   /**
    * @return the userName
    */
   public String getUserName () {
      return userName;
   }

   /**
    * @param userName
    *           the userName to set
    */
   public void setUserName (String userName) {
      this.userName = userName;
   }

   /**
    * @return the isPublic
    */
   public CheckType getIsPublic () {
      return isPublic;
   }

   /**
    * @param isPublic
    *           the isPublic to set
    */
   public void setIsPublic (CheckType isPublic) {
      this.isPublic = isPublic;
   }

   /**
    * @return the assetId
    */
   public Long getAssetId () {
      return assetId;
   }

   /**
    * @param assetId
    *           the assetId to set
    */
   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   /**
    * @return the abuseReoprt
    */
   public CheckType getAbuseReoprt () {
      return abuseReoprt;
   }

   /**
    * @param abuseReoprt
    *           the abuseReoprt to set
    */
   public void setAbuseReoprt (CheckType abuseReoprt) {
      this.abuseReoprt = abuseReoprt;
   }

   /**
    * @return the comment
    */
   public String getComment () {
      return comment;
   }

   /**
    * @param comment
    *           the comment to set
    */
   public void setComment (String comment) {
      this.comment = comment;
   }

}
