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


package com.execue.core.common.bean.uss;

import java.util.List;

import com.execue.core.common.bean.qdata.UniversalSearchResultFeature;

/**
 * 
 * @author jitendra
 *
 */
public class UnstructuredSearchResultDataHeader {

   private String                             shortDescription;
   private String                             longDescription;
   private String                             imageUrl;
   private String                             contentSource;
   private String                             url;
   private String                             location;
   private List<UniversalSearchResultFeature> unstructuredSearchResultData;

   /**
    * @return the location
    */
   public String getLocation () {
      return location;
   }

   /**
    * @param location the location to set
    */
   public void setLocation (String location) {
      this.location = location;
   }

   /**
    * @return the unstructuredSearchResultData
    */
   public List<UniversalSearchResultFeature> getUnstructuredSearchResultData () {
      return unstructuredSearchResultData;
   }

   /**
    * @param unstructuredSearchResultData the unstructuredSearchResultData to set
    */
   public void setUnstructuredSearchResultData (List<UniversalSearchResultFeature> unstructuredSearchResultData) {
      this.unstructuredSearchResultData = unstructuredSearchResultData;
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
    * @return the longDescription
    */
   public String getLongDescription () {
      return longDescription;
   }

   /**
    * @param longDescription the longDescription to set
    */
   public void setLongDescription (String longDescription) {
      this.longDescription = longDescription;
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
    * @return the contentSource
    */
   public String getContentSource () {
      return contentSource;
   }

   /**
    * @param contentSource the contentSource to set
    */
   public void setContentSource (String contentSource) {
      this.contentSource = contentSource;
   }

}
