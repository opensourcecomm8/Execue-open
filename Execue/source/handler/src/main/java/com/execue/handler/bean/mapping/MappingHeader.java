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


package com.execue.handler.bean.mapping;

/**
 * @author kaliki
 * @since 4.0
 * JSON Object used for setting mapping headers
 */
public class MappingHeader {

   private int    currentPage;
   private int    totalPages;
   private String filterType;

   public int getCurrentPage () {
      return currentPage;
   }

   public void setCurrentPage (int currentPage) {
      this.currentPage = currentPage;
   }

   public int getTotalPages () {
      return totalPages;
   }

   public void setTotalPages (int totalPages) {
      this.totalPages = totalPages;
   }

   public String getFilterType () {
      return filterType;
   }

   public void setFilterType (String filterType) {
      this.filterType = filterType;
   }

}
