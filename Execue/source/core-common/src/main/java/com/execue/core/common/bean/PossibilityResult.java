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

import java.util.List;

import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.CheckType;

/**
 * @author kaliki
 * @since 4.0
 */
public class PossibilityResult {

   private int               id;
   private String            colorCodedRequest;
   private List<AssetResult> assets;
   private String            appName;
   private String            appUrl;
   private Long              appId;
   private Long              appImageId;
   private boolean           scoped   = false;
   private AppSourceType     applicationType;
   private CheckType         showMore = CheckType.NO;
   private int               totalCount;
   private int               perfectMatchCount;
   private int               mightMatchCount;
   private int               partialMatchCount;

   public Long getAppImageId () {
      return appImageId;
   }

   public void setAppImageId (Long appImageId) {
      this.appImageId = appImageId;
   }

   public Long getAppId () {
      return appId;
   }

   public void setAppId (Long appId) {
      this.appId = appId;
   }

   public int getId () {
      return id;
   }

   public void setId (int id) {
      this.id = id;
   }

   public List<AssetResult> getAssets () {
      return assets;
   }

   public void setAssets (List<AssetResult> assets) {
      this.assets = assets;
   }

   public String getColorCodedRequest () {
      return colorCodedRequest;
   }

   public void setColorCodedRequest (String colorCodedRequest) {
      this.colorCodedRequest = colorCodedRequest;
   }

   public String getAppName () {
      return appName;
   }

   public void setAppName (String appName) {
      this.appName = appName;
   }

   public String getAppUrl () {
      return appUrl;
   }

   public void setAppUrl (String appUrl) {
      this.appUrl = appUrl;
   }

   public boolean isScoped () {
      return scoped;
   }

   public void setScoped (boolean scoped) {
      this.scoped = scoped;
   }

   /**
    * @return the applicationType
    */
   public AppSourceType getApplicationType () {
      return applicationType;
   }

   /**
    * @param applicationType
    *           the applicationType to set
    */
   public void setApplicationType (AppSourceType applicationType) {
      this.applicationType = applicationType;
   }

   /**
    * @return the showMore
    */
   public CheckType getShowMore () {
      return showMore;
   }

   /**
    * @param showMore the showMore to set
    */
   public void setShowMore (CheckType showMore) {
      this.showMore = showMore;
   }

   /**
    * @return the totalCount
    */
   public int getTotalCount () {
      return totalCount;
   }

   /**
    * @param totalCount the totalCount to set
    */
   public void setTotalCount (int totalCount) {
      this.totalCount = totalCount;
   }

   /**
    * @return the perfectMatchCount
    */
   public int getPerfectMatchCount () {
      return perfectMatchCount;
   }

   /**
    * @param perfectMatchCount the perfectMatchCount to set
    */
   public void setPerfectMatchCount (int perfectMatchCount) {
      this.perfectMatchCount = perfectMatchCount;
   }

   /**
    * @return the mightMatchCount
    */
   public int getMightMatchCount () {
      return mightMatchCount;
   }

   /**
    * @param mightMatchCount the mightMatchCount to set
    */
   public void setMightMatchCount (int mightMatchCount) {
      this.mightMatchCount = mightMatchCount;
   }

   /**
    * @return the partialMatchCount
    */
   public int getPartialMatchCount () {
      return partialMatchCount;
   }

   /**
    * @param partialMatchCount the partialMatchCount to set
    */
   public void setPartialMatchCount (int partialMatchCount) {
      this.partialMatchCount = partialMatchCount;
   }

}
