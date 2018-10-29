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

import com.execue.core.common.type.BookmarkType;

public class UIBookmark {

   private Long         id;
   private String       name;
   private String       value;
   private BookmarkType type;
   private Long         applicationId;
   private Long         modelId;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getValue () {
      return value;
   }

   public void setValue (String value) {
      this.value = value;
   }

   public BookmarkType getType () {
      return type;
   }

   public void setType (BookmarkType type) {
      this.type = type;
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId
    *           the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   /**
    * @return the modelId
    */
   public Long getModelId () {
      return modelId;
   }

   /**
    * @param modelId
    *           the modelId to set
    */
   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }
}
