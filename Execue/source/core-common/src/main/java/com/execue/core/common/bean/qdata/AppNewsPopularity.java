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
package com.execue.core.common.bean.qdata;

import java.io.Serializable;

/**
 * @author Nihar
 */
public class AppNewsPopularity implements Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private Long              udxId;
   private Long              entityBeId;
   private Integer           hits;
   private Long              appId;

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
    * @return the udxId
    */
   public Long getUdxId () {
      return udxId;
   }

   /**
    * @param udxId
    *           the udxId to set
    */
   public void setUdxId (Long udxId) {
      this.udxId = udxId;
   }

   /**
    * @return the entityBeId
    */
   public Long getEntityBeId () {
      return entityBeId;
   }

   /**
    * @param entityBeId
    *           the entityBeId to set
    */
   public void setEntityBeId (Long entityBeId) {
      this.entityBeId = entityBeId;
   }

   /**
    * @return the hits
    */
   public Integer getHits () {
      return hits;
   }

   /**
    * @param hits
    *           the hits to set
    */
   public void setHits (Integer hits) {
      this.hits = hits;
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
}