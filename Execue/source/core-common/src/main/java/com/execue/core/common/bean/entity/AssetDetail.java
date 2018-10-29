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


package com.execue.core.common.bean.entity;

/**
 * This class represents the AssetDetail object.
 * 
 * @author MurthySN
 * @version 1.0
 * @since 09/01/10
 */
public class AssetDetail implements java.io.Serializable {

   private static final long   serialVersionUID = 1L;
   private Long                id;
   private Long                assetId;
   private String              shortNote;
   private String              shortDisclaimer;
   private String              creationInfo;
   private AssetExtendedDetail assetExtendedDetail;

   public AssetExtendedDetail getAssetExtendedDetail () {
      return assetExtendedDetail;
   }

   public void setAssetExtendedDetail (AssetExtendedDetail assetExtendedDetail) {
      this.assetExtendedDetail = assetExtendedDetail;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public String getShortNote () {
      return shortNote;
   }

   public void setShortNote (String shortNote) {
      this.shortNote = shortNote;
   }

   public String getShortDisclaimer () {
      return shortDisclaimer;
   }

   public void setShortDisclaimer (String shortDisclaimer) {
      this.shortDisclaimer = shortDisclaimer;
   }

   /**
    * @return the creationInfo
    */
   public String getCreationInfo () {
      return creationInfo;
   }

   /**
    * @param creationInfo the creationInfo to set
    */
   public void setCreationInfo (String creationInfo) {
      this.creationInfo = creationInfo;
   }

}
