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
package com.execue.core.common.bean.uss;

import java.util.Date;

import com.execue.core.common.bean.swi.PossibleAssetInfo;

/**
 * @author Nitesh
 *
 */
public class UnstructuredPossibleAssetInfo extends PossibleAssetInfo {

   private String                       url;
   private String                       imageUrl;
   private String                       shortDescription;
   private Date                         contentDate;
   private UniversalSearchResultItemType resultItemType;

   /**
    * @return the url
    */
   public String getUrl () {
      return url;
   }

   /**
    * @param url the url to set
    */
   public void setUrl (String url) {
      this.url = url;
   }

   /**
    * @return the imageUrl
    */
   public String getImageUrl () {
      return imageUrl;
   }

   /**
    * @param imageUrl the imageUrl to set
    */
   public void setImageUrl (String imageUrl) {
      this.imageUrl = imageUrl;
   }

   /**
    * @return the shortDescription
    */
   public String getShortDescription () {
      return shortDescription;
   }

   /**
    * @param shortDescription the shortDescription to set
    */
   public void setShortDescription (String shortDescription) {
      this.shortDescription = shortDescription;
   }

   /**
    * @return the contentDate
    */
   public Date getContentDate () {
      return contentDate;
   }

   /**
    * @param contentDate the contentDate to set
    */
   public void setContentDate (Date contentDate) {
      this.contentDate = contentDate;
   }

   /**
    * @return the resultItemType
    */
   public UniversalSearchResultItemType getResultItemType () {
      return resultItemType;
   }

   /**
    * @param resultItemType the resultItemType to set
    */
   public void setResultItemType (UniversalSearchResultItemType resultItemType) {
      this.resultItemType = resultItemType;
   }
}
