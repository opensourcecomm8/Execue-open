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

/**
 * @author MurthySN
 */
public class ApplicationScopeIndex implements Serializable {

   private Long   id;
   private String token;
   private Double tokenWeight;
   private Long   appearances;
   private Long   modelGroupId;
   private Long   appId;

   public Long getAppId () {
      return appId;
   }

   public void setAppId (Long appId) {
      this.appId = appId;
   }

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
    * @return the token
    */
   public String getToken () {
      return token;
   }

   /**
    * @param token
    *           the token to set
    */
   public void setToken (String token) {
      this.token = token;
   }

   /**
    * @return the tokenWeight
    */
   public Double getTokenWeight () {
      return tokenWeight;
   }

   /**
    * @param tokenWeight
    *           the tokenWeight to set
    */
   public void setTokenWeight (Double tokenWeight) {
      this.tokenWeight = tokenWeight;
   }

   /**
    * @return the appearances
    */
   public Long getAppearances () {
      return appearances;
   }

   /**
    * @param appearances
    *           the appearances to set
    */
   public void setAppearances (Long appearances) {
      this.appearances = appearances;
   }

   /**
    * @return the modelGroupId
    */
   public Long getModelGroupId () {
      return modelGroupId;
   }

   /**
    * @param modelGroupId
    *           the modelGroupId to set
    */
   public void setModelGroupId (Long modelGroupId) {
      this.modelGroupId = modelGroupId;
   }

}
