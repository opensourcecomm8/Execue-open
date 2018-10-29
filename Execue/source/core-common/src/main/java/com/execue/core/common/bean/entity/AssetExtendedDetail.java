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
 * This class represents the AssetExtendedDetail object.
 * 
 * @author MurthySN
 * @version 1.0
 * @since 09/01/10
 */
public class AssetExtendedDetail implements java.io.Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private String            extendedNote;
   private String            extendedDisclaimer;
   private String            creationInfo;
   private AssetDetail       assetDetail;

   public AssetDetail getAssetDetail () {
      return assetDetail;
   }

   public void setAssetDetail (AssetDetail assetDetail) {
      this.assetDetail = assetDetail;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getExtendedNote () {
      return extendedNote;
   }

   public void setExtendedNote (String extendedNote) {
      this.extendedNote = extendedNote;
   }

   public String getExtendedDisclaimer () {
      return extendedDisclaimer;
   }

   public void setExtendedDisclaimer (String extendedDisclaimer) {
      this.extendedDisclaimer = extendedDisclaimer;
   }

   
   public String getCreationInfo () {
      return creationInfo;
   }

   
   public void setCreationInfo (String creationInfo) {
      this.creationInfo = creationInfo;
   }

}
