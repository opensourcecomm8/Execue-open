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


package com.execue.web.core.action.qi.bookmark;

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.UserInput;
import com.execue.core.common.type.BookmarkType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIBookmarkSearch;
import com.execue.handler.bean.UIUserInput;
import com.execue.handler.qi.QueryInterfaceHandlerReqeust;

public class SearchBookmarkAction extends BaseBookmarkAction {

   private static final Logger          log = Logger.getLogger(SearchBookmarkAction.class);

   /**
    * used for search String
    */
   private String                       search;
   private List<UIBookmarkSearch>       uiBookmarkSearch;
   private BookmarkType                 bookmarkType;
   private String                       bookmarkValue;
   private QueryForm                    queryForm;
   private QueryInterfaceHandlerReqeust queryFormRequestTransform;

   // Action Method
   public String searchBookmarks () {
      try {
         if (log.isDebugEnabled()) {
            log.debug("searchBookmarkName:" + search);
            log.debug("bookmarkType: " + bookmarkType);
         }
         uiBookmarkSearch = getBookmarkHandler().searchBookmarks(search, bookmarkType);
         if (uiBookmarkSearch.size() == 0) {
            setErrorMessage(getText("execue.global.search.error"));
            return ERROR;
         }
      } catch (HandlerException e) {
         setErrorMessage(getText("execue.global.error"));
         return ERROR;
      }
      return SUCCESS;
   }

   public String retrieveBookmark () {
      if (log.isDebugEnabled()) {
         log.debug("bookmarkValue :" + bookmarkValue);
      }
      try {
         if (ExecueCoreUtil.isNotEmpty(bookmarkValue)) {
            UIUserInput userRequest = new UIUserInput();
            userRequest.setRequest(bookmarkValue);
            UserInput userInput = (UserInput) queryFormRequestTransform.transformRequest(userRequest);
            queryForm = userInput.getQueryForm();
         }
      } catch (ExeCueException exeCueException) {
         setErrorMessage(getText("execue.global.error"));
         return ERROR;
      }
      return SUCCESS;
   }

   /**
    * @return the bookmarkType
    */
   public BookmarkType getBookmarkType () {
      return bookmarkType;
   }

   /**
    * @param bookmarkType
    *           the bookmarkType to set
    */
   public void setBookmarkType (BookmarkType bookmarkType) {
      this.bookmarkType = bookmarkType;
   }

   /**
    * @return the bookmarkValue
    */
   public String getBookmarkValue () {
      return bookmarkValue;
   }

   /**
    * @param bookmarkValue
    *           the bookmarkValue to set
    */
   public void setBookmarkValue (String bookmarkValue) {
      this.bookmarkValue = bookmarkValue;
   }

   public QueryForm getQueryForm () {
      return queryForm;
   }

   public void setQueryForm (QueryForm queryForm) {
      this.queryForm = queryForm;
   }

   public String getSearch () {
      return search;
   }

   public void setSearch (String search) {
      this.search = search;
   }

   public List<UIBookmarkSearch> getUiBookmarkSearch () {
      return uiBookmarkSearch;
   }

   public void setUiBookmarkSearch (List<UIBookmarkSearch> uiBookmarkSearch) {
      this.uiBookmarkSearch = uiBookmarkSearch;
   }

   /**
    * @return the queryFormRequestTransform
    */
   public QueryInterfaceHandlerReqeust getQueryFormRequestTransform () {
      return queryFormRequestTransform;
   }

   /**
    * @param queryFormRequestTransform
    *           the queryFormRequestTransform to set
    */
   public void setQueryFormRequestTransform (QueryInterfaceHandlerReqeust queryFormRequestTransform) {
      this.queryFormRequestTransform = queryFormRequestTransform;
   }

}
