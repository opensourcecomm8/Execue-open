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


package com.execue.core.common.bean.swi;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MurthySN
 */
public class ApplicationScopeIndexDetail implements Serializable {

   private Long id;
   private Long appId;
   private Date lastRefreshDate;

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the appId
    */
   public Long getAppId () {
      return appId;
   }

   /**
    * @param appId
    *           the appId to set
    */
   public void setAppId (Long appId) {
      this.appId = appId;
   }

   /**
    * @return the lastRefreshDate
    */
   public Date getLastRefreshDate () {
      return lastRefreshDate;
   }

   /**
    * @param lastRefreshDate
    *           the lastRefreshDate to set
    */
   public void setLastRefreshDate (Date lastRefreshDate) {
      this.lastRefreshDate = lastRefreshDate;
   }

}
