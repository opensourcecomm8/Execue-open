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


package com.execue.semantification.bean;

public class GenericArticleResemantificationMessage {

   private Long userQueryId;
   private Long contextId;

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
    * @return the userQueryId
    */
   public Long getUserQueryId () {
      return userQueryId;
   }

   /**
    * @param userQueryId the userQueryId to set
    */
   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

}