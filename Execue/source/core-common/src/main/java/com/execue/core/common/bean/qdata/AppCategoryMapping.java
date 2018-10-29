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


package com.execue.core.common.bean.qdata;

import java.io.Serializable;

import com.execue.core.common.type.SearchFilterType;

@SuppressWarnings ("serial")
public class AppCategoryMapping implements Serializable {

   private Long             id;
   private Long             contextId;
   private String           categoryName;
   private SearchFilterType searchFilterType;

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
    * @return the contextId
    */
   public Long getContextId () {
      return contextId;
   }

   /**
    * @param contextId the contextId to set
    */
   public void setContextId (Long contextId) {
      this.contextId = contextId;
   }

   /**
    * @return the categoryName
    */
   public String getCategoryName () {
      return categoryName;
   }

   /**
    * @param categoryName the categoryName to set
    */
   public void setCategoryName (String categoryName) {
      this.categoryName = categoryName;
   }

   /**
    * @return the searchFilterType
    */
   public SearchFilterType getSearchFilterType () {
      return searchFilterType;
   }

   /**
    * @param searchFilterType the searchFilterType to set
    */
   public void setSearchFilterType (SearchFilterType searchFilterType) {
      this.searchFilterType = searchFilterType;
   }

}
