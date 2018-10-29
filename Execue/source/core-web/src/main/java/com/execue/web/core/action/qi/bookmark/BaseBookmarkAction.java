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

import com.execue.handler.swi.IBookmarkHandler;
import com.opensymphony.xwork2.ActionSupport;

public class BaseBookmarkAction extends ActionSupport {

   private IBookmarkHandler bookmarkHandler;
   /**
    * show error message
    */
   private String           errorMessage;
   /**
    * show message
    */
   private String           message;

   public IBookmarkHandler getBookmarkHandler () {
      return bookmarkHandler;
   }

   public void setBookmarkHandler (IBookmarkHandler bookmarkHandler) {
      this.bookmarkHandler = bookmarkHandler;
   }

   public String getMessage () {
      return message;
   }

   public void setMessage (String message) {
      this.message = message;
   }

   public String getErrorMessage () {
      return errorMessage;
   }

   public void setErrorMessage (String errorMessage) {
      this.errorMessage = errorMessage;
   }

}
