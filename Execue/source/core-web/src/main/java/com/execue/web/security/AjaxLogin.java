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


package com.execue.web.security;

import com.execue.core.common.type.CheckType;

public class AjaxLogin {

   private String    name;
   private boolean   isAdmin;
   private String    url;
   private String    errMsg = "";
   private CheckType isPublisher;

   public String getErrMsg () {
      return errMsg;
   }

   public void setErrMsg (String errMsg) {
      this.errMsg = errMsg;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public boolean isAdmin () {
      return isAdmin;
   }

   public void setAdmin (boolean isAdmin) {
      this.isAdmin = isAdmin;
   }

   public String getUrl () {
      return url;
   }

   public void setUrl (String url) {
      this.url = url;
   }

   /**
    * @return the isPublisher
    */
   public CheckType getIsPublisher () {
      return isPublisher;
   }

   /**
    * @param isPublisher
    *           the isPublisher to set
    */
   public void setIsPublisher (CheckType isPublisher) {
      this.isPublisher = isPublisher;
   }
}
