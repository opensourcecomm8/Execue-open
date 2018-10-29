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


package com.execue.web.core.action.uss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.PageSearch;
import com.execue.core.common.bean.PageSort;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author jitendra
 *
 */

public abstract class UnstructuredSearchPaginationAction extends ActionSupport implements RequestAware, SessionAware {

   private String                searchField;
   private String                searchString;
   private String                searchType;
   private String                sortField;
   private String                sortOrder;
   private Page                  pageDetail;
   private String                requestedPage;
   private int                   pageSize;

   protected static final String DEFAULT_REQUESTED_PAGE  = "1";
   public static final String    SEARCH_TYPE_CONTAINS    = "contains";
   public static final String    SEARCH_TYPE_STARTWITH   = "startWith";
   public static final String    SEARCH_TYPE_BY_APP_NAME = "byAppName";
   protected static final String PAGINATION              = "PAGINATION";

   private Map                   httpSession;
   private Map                   httpRequest;

   public abstract String processPage () throws ExeCueException;

   public String getPageResults () throws Exception {
      try {
         prepareRequestedPageDetail();
         return processPage();
      } catch (ExeCueException execueException) {
         return ERROR;
      }
   }

   private void prepareRequestedPageDetail () {
      if (ExecueCoreUtil.isEmpty(requestedPage) || requestedPage.equals("0")) {
         setRequestedPage(DEFAULT_REQUESTED_PAGE);
      }
      pageDetail = new Page(Long.valueOf(getRequestedPage()), Long.valueOf(getPageSize()));
      // Add the sort list only if there is a valid sort field
      if (ExecueCoreUtil.isNotEmpty(sortField)) {
         PageSort pageSort = new PageSort();
         pageSort.setField(sortField);
         // if no sort order is specified, default it to ASCENDING order
         if (ExecueCoreUtil.isEmpty(sortOrder)) {
            sortOrder = "ASC";
         }
         pageSort.setOrder(sortOrder);
         List<PageSort> sortList = new ArrayList<PageSort>();
         sortList.add(pageSort);
         pageDetail.setSortList(sortList);
      }
      // Add the search list only if there is a valid search field, search string
      if (ExecueCoreUtil.isNotEmpty(searchString)) {
         PageSearch pageSearch = new PageSearch();
         if (ExecueCoreUtil.isEmpty(searchField)) {
            searchField = "displayName";
         }
         pageSearch.setField(searchField);
         if (SEARCH_TYPE_STARTWITH.equalsIgnoreCase(searchType)) {
            pageSearch.setType(PageSearchType.STARTS_WITH);
         } else if (SEARCH_TYPE_CONTAINS.equalsIgnoreCase(searchType)) {
            pageSearch.setType(PageSearchType.CONTAINS);
         } else if (SEARCH_TYPE_BY_APP_NAME.equalsIgnoreCase(searchType)) {
            pageSearch.setType(PageSearchType.BY_APP_NAME);
            pageSearch.setField("name");
         }
         pageSearch.setString(searchString);
         List<PageSearch> searchList = new ArrayList<PageSearch>();
         searchList.add(pageSearch);
         pageDetail.setSearchList(searchList);
      }
   }

   public Page getPageDetail () {
      return pageDetail;
   }

   public void setPageDetail (Page pageDetail) {
      this.pageDetail = pageDetail;
   }

   public String getRequestedPage () {
      return requestedPage;
   }

   public void setRequestedPage (String requestedPage) {
      this.requestedPage = requestedPage;
   }

   public int getPageSize () {
      return pageSize;
   }

   public void setPageSize (int pageSize) {
      this.pageSize = pageSize;
   }

   public String getSearchString () {
      return searchString;
   }

   public void setSearchString (String searchString) {
      this.searchString = searchString;
   }

   public String getSearchType () {
      return searchType;
   }

   public void setSearchType (String searchType) {
      this.searchType = searchType;
   }

   public String getSortField () {
      return sortField;
   }

   public void setSortField (String sortField) {
      this.sortField = sortField;
   }

   public String getSortOrder () {
      return sortOrder;
   }

   public void setSortOrder (String sortOrder) {
      this.sortOrder = sortOrder;
   }

   public String getSearchField () {
      return searchField;
   }

   public void setSearchField (String searchField) {
      this.searchField = searchField;
   }

   public void setSession (Map session) {
      this.httpSession = session;
   }

   public Map getHttpSession () {
      return this.httpSession;
   }

   public void setRequest (Map request) {
      this.httpRequest = request;
   }

   public Map getHttpRequest () {
      return httpRequest;
   }

}
