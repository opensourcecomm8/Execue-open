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


package com.execue.core.common.bean.qdata;

import java.io.Serializable;

public class AdditionalInfo implements Serializable {

   private Long   id;
   private String wikiURLId;
   private Long beId;
   private String instanceName;
   private String netflixURLId;
   private String imdbURLId;
   
   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }
   
   /**
    * @param id the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }
   
   /**
    * @return the instanceName
    */
   public String getInstanceName () {
      return instanceName;
   }
   
   /**
    * @param instanceName the instanceName to set
    */
   public void setInstanceName (String instanceName) {
      this.instanceName = instanceName;
   }

   
   /**
    * @return the wikiURLId
    */
   public String getWikiURLId () {
      return wikiURLId;
   }

   
   /**
    * @param wikiURLId the wikiURLId to set
    */
   public void setWikiURLId (String wikiURLId) {
      this.wikiURLId = wikiURLId;
   }

   
   /**
    * @return the beId
    */
   public Long getBeId () {
      return beId;
   }
   
   /**
    * @param beId the beId to set
    */
   public void setBeId (Long beId) {
      this.beId = beId;
   }
   
   /**
    * @return the netflixURLId
    */
   public String getNetflixURLId () {
      return netflixURLId;
   }

   
   /**
    * @param netflixURLId the netflixURLId to set
    */
   public void setNetflixURLId (String netflixURLId) {
      this.netflixURLId = netflixURLId;
   }

   
   /**
    * @return the imdbURLId
    */
   public String getImdbURLId () {
      return imdbURLId;
   }

   
   /**
    * @param imdbURLId the imdbURLId to set
    */
   public void setImdbURLId (String imdbURLId) {
      this.imdbURLId = imdbURLId;
   }
}
