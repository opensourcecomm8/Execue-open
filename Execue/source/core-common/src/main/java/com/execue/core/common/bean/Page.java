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


package com.execue.core.common.bean;

import java.util.List;

import com.execue.core.util.ExecueCoreUtil;

/**
 * @author John Mallavalli
 */
public class Page {

   private Long     pageSize;
   private Long     requestedPage;
   private Long     recordCount;
   private Long     pageCount;
   private Long     numberOfLinks;
   List<PageSort>   sortList;
   List<PageSearch> searchList;

   public String toString () {
      StringBuilder sbPage = new StringBuilder();
      sbPage.append("Requested Page : " + getRequestedPage());
      sbPage.append("\nRecord Count : " + getRecordCount());
      sbPage.append("\nPage Size : " + getPageSize());
      sbPage.append("\nNumber of Links : " + getNumberOfLinks());
      sbPage.append("\nPage Count : " + getPageCount());
      if (ExecueCoreUtil.isCollectionNotEmpty(getSortList())) {
         for (PageSort sort : getSortList()) {
            sbPage.append("\nSort Field : " + sort.getField());
            sbPage.append("\nSort Order : " + sort.getOrder());
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(getSearchList())) {
         for (PageSearch search : getSearchList()) {
            sbPage.append("\nSearch Field : " + search.getField());
            sbPage.append("\nSearch String : " + search.getString());
            sbPage.append("\nSearch Type : " + search.getType());
         }
      }
      return sbPage.toString();
   }

   public Page () {
   }

   public Page (Long requestedPage, Long pageSize) {
      this.requestedPage = requestedPage;
      this.pageSize = pageSize;
   }

   public Page (Long requestedPage, Long pageSize, Long recordCount) {
      this.requestedPage = requestedPage;
      this.pageSize = pageSize;
      this.recordCount = recordCount;
   }

   public Long getPageSize () {
      return pageSize;
   }

   public void setPageSize (Long pageSize) {
      this.pageSize = pageSize;
   }

   public Long getRecordCount () {
      return recordCount;
   }

   public void setRecordCount (Long recordCount) {
      this.recordCount = recordCount;

      // calculate and set the page count
      if (this.pageSize == null) {
         this.pageSize = 10L;
      }
      this.pageCount = (recordCount / this.pageSize);
      if ((recordCount % this.pageSize) > 0) {
         this.pageCount++;
      }
   }

   public Long getPageCount () {
      return pageCount;
   }

   public void setPageCount (Long pageCount) {
      this.pageCount = pageCount;
   }

   /**
    * @return the requestedPage
    */
   public Long getRequestedPage () {
      return requestedPage;
   }

   /**
    * @param requestedPage
    *           the requestedPage to set
    */
   public void setRequestedPage (Long requestedPage) {
      this.requestedPage = requestedPage;
   }

   /**
    * @return the numberOfLinks
    */
   public Long getNumberOfLinks () {
      return numberOfLinks;
   }

   /**
    * @param numberOfLinks
    *           the numberOfLinks to set
    */
   public void setNumberOfLinks (Long numberOfLinks) {
      this.numberOfLinks = numberOfLinks;
   }

   public List<PageSort> getSortList () {
      return sortList;
   }

   public void setSortList (List<PageSort> sortList) {
      this.sortList = sortList;
   }

   public List<PageSearch> getSearchList () {
      return searchList;
   }

   public void setSearchList (List<PageSearch> searchList) {
      this.searchList = searchList;
   }
}
