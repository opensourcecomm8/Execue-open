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

/**
 * @author Jtiwari
 * @since 25/10/09
 */
public class ApplicationInfo implements java.io.Serializable {

   private Long   id;
   private String appName;
   private String serachUrl;
   private String suggestUrl;
   private String qiAdvanceSearchUrl;
   private String examplesUrl;

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
    * @return the appName
    */
   public String getAppName () {
      return appName;
   }

   /**
    * @param appName
    *           the appName to set
    */
   public void setAppName (String appName) {
      this.appName = appName;
   }

   /**
    * @return the serachUrl
    */
   public String getSerachUrl () {
      return serachUrl;
   }

   /**
    * @param serachUrl
    *           the serachUrl to set
    */
   public void setSerachUrl (String serachUrl) {
      this.serachUrl = serachUrl;
   }

   /**
    * @return the suggestUrl
    */
   public String getSuggestUrl () {
      return suggestUrl;
   }

   /**
    * @param suggestUrl
    *           the suggestUrl to set
    */
   public void setSuggestUrl (String suggestUrl) {
      this.suggestUrl = suggestUrl;
   }

   /**
    * @return the qiAdvanceSearchUrl
    */
   public String getQiAdvanceSearchUrl () {
      return qiAdvanceSearchUrl;
   }

   /**
    * @param qiAdvanceSearchUrl
    *           the qiAdvanceSearchUrl to set
    */
   public void setQiAdvanceSearchUrl (String qiAdvanceSearchUrl) {
      this.qiAdvanceSearchUrl = qiAdvanceSearchUrl;
   }

   public String getExamplesUrl () {
      return examplesUrl;
   }

   public void setExamplesUrl (String examplesUrl) {
      this.examplesUrl = examplesUrl;
   }

}
