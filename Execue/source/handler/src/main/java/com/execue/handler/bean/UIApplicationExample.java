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

import com.execue.core.common.type.UserInterfaceType;

public class UIApplicationExample {

   private Long              id;
   private String            queryName;
   private String            truncatedQueryName;
   private String            queryValue;
   private UserInterfaceType type = UserInterfaceType.SIMPLE_SEARCH;
   private Long              applicationId;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getQueryName () {
      return queryName;
   }

   public void setQueryName (String queryName) {
      this.queryName = queryName;
   }

   public String getQueryValue () {
      return queryValue;
   }

   public void setQueryValue (String queryValue) {
      this.queryValue = queryValue;
   }

   public UserInterfaceType getType () {
      return type;
   }

   public void setType (UserInterfaceType type) {
      this.type = type;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public String getTruncatedQueryName () {
      return truncatedQueryName;
   }

   public void setTruncatedQueryName (String truncatedQueryName) {
      this.truncatedQueryName = truncatedQueryName;
   }

}
