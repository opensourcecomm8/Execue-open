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

import com.execue.core.common.type.AnswersCatalogOperationType;

public class AnswersCatalogContext implements Serializable {

   private static final long           serialVersionUID = 1L;
   private Long                        id;
   private Long                        assetId;
   private Long                        parentAssetId;
   private String                      contextData;
   private AnswersCatalogOperationType latestOperation;
   private long                        userId;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getContextData () {
      return contextData;
   }

   public void setContextData (String contextData) {
      this.contextData = contextData;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public long getUserId () {
      return userId;
   }

   public void setUserId (long userId) {
      this.userId = userId;
   }

   public Long getParentAssetId () {
      return parentAssetId;
   }

   public void setParentAssetId (Long parentAssetId) {
      this.parentAssetId = parentAssetId;
   }

   public AnswersCatalogOperationType getLatestOperation () {
      return latestOperation;
   }

   public void setLatestOperation (AnswersCatalogOperationType latestOperation) {
      this.latestOperation = latestOperation;
   }

   public static long getSerialversionuid () {
      return serialVersionUID;
   }

}
