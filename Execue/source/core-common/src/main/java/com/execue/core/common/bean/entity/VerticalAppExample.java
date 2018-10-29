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

public class VerticalAppExample implements Serializable {

   private static final long serialVersionUID = 1L;

   private Long              id;
   private int               day;
   private Long              verticalId;
   private String            queryExample;
   private Long              applicationId;
   private transient String  truncatedQueryExample;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getQueryExample () {
      return queryExample;
   }

   public void setQueryExample (String queryExample) {
      this.queryExample = queryExample;
   }

   public Long getVerticalId () {
      return verticalId;
   }

   public void setVerticalId (Long verticalId) {
      this.verticalId = verticalId;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public int getDay () {
      return day;
   }

   public void setDay (int day) {
      this.day = day;
   }

   public String getTruncatedQueryExample () {
      return truncatedQueryExample;
   }

   public void setTruncatedQueryExample (String truncatedQueryExample) {
      this.truncatedQueryExample = truncatedQueryExample;
   }

}
