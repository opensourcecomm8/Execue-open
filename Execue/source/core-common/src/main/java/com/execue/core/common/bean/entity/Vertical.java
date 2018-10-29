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

import com.execue.core.common.type.CheckType;

public class Vertical implements Serializable {

   private static final long serialVersionUID   = 1L;
   private Long              id;
   private String            name;
   private String            description;
   private Long              importance;
   private String            url;
   private Long              popularity;
   private CheckType         homepageVisibility = CheckType.NO;

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

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public Long getImportance () {
      return importance;
   }

   public void setImportance (Long importance) {
      this.importance = importance;
   }

   public Long getPopularity () {
      return popularity;
   }

   public void setPopularity (Long popularity) {
      this.popularity = popularity;
   }

   public String getUrl () {
      return url;
   }

   public void setUrl (String url) {
      this.url = url;
   }

   public CheckType getHomepageVisibility () {
      return homepageVisibility;
   }

   public void setHomepageVisibility (CheckType homepageVisibility) {
      this.homepageVisibility = homepageVisibility;
   }

}
