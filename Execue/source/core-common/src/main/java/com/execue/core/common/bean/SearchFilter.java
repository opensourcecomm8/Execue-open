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


/**
 * 
 */
package com.execue.core.common.bean;

import java.util.List;

import com.execue.core.common.type.SearchFilterType;

/**
 * @author Nihar Agrawal
 */
public class SearchFilter {

   private SearchFilterType searchFilterType         = SearchFilterType.GENERAL;
   private SearchFilterType originalSearchFilterType = SearchFilterType.GENERAL;
   private Long             id;
   private boolean          appScopingEnabled;
   private List<Long>       appIds;

   public List<Long> getAppIds () {
      return appIds;
   }

   public void setAppIds (List<Long> appIds) {
      this.appIds = appIds;
   }

   /**
    * @return the searchType
    */
   public SearchFilterType getSearchFilterType () {
      return searchFilterType;
   }

   /**
    * @param searchType
    *           the searchType to set
    */
   public void setSearchFilterType (SearchFilterType searchType) {
      this.searchFilterType = searchType;
   }

   /**
    * @return the Id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param name
    *           the name to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   public boolean isAppScopingEnabled () {
      return appScopingEnabled;
   }

   public void setAppScopingEnabled (boolean appScopingEnabled) {
      this.appScopingEnabled = appScopingEnabled;
   }

   public SearchFilterType getOriginalSearchFilterType () {
      return originalSearchFilterType;
   }

   public void setOriginalSearchFilterType (SearchFilterType originalSearchFilterType) {
      this.originalSearchFilterType = originalSearchFilterType;
   }
}
