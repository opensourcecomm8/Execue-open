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


package com.execue.handler.bean;

import com.execue.core.common.type.CheckType;

/**
 * @author JTiwari
 * @since 09/01/2010
 */
public class UIAssetDetail {

   private Long      assetDetailId;
   private Long      extendedAssetDetailId;
   private String    shortDisclaimer;
   private String    shortNote;
   private String    extendedDisclaimer;
   private String    extendedNote;
   private Long      applicationId;
   private CheckType isAssetDisclaimerApplicable = CheckType.NO;

   /**
    * @return the extendedNote
    */
   public String getExtendedNote () {
      return extendedNote;
   }

   /**
    * @param extendedNote
    *           the extendedNote to set
    */
   public void setExtendedNote (String extendedNote) {
      this.extendedNote = extendedNote;
   }

   /**
    * @return the shortDisclaimer
    */
   public String getShortDisclaimer () {
      return shortDisclaimer;
   }

   /**
    * @param shortDisclaimer
    *           the shortDisclaimer to set
    */
   public void setShortDisclaimer (String shortDisclaimer) {
      this.shortDisclaimer = shortDisclaimer;
   }

   /**
    * @return the extendedDisclaimer
    */
   public String getExtendedDisclaimer () {
      return extendedDisclaimer;
   }

   /**
    * @param extendedDisclaimer
    *           the extendedDisclaimer to set
    */
   public void setExtendedDisclaimer (String extendedDisclaimer) {
      this.extendedDisclaimer = extendedDisclaimer;
   }

   public Long getAssetDetailId () {
      return assetDetailId;
   }

   public void setAssetDetailId (Long assetDetailId) {
      this.assetDetailId = assetDetailId;
   }

   /**
    * @return the extendedAssetDetailId
    */
   public Long getExtendedAssetDetailId () {
      return extendedAssetDetailId;
   }

   /**
    * @param extendedAssetDetailId
    *           the extendedAssetDetailId to set
    */
   public void setExtendedAssetDetailId (Long extendedAssetDetailId) {
      this.extendedAssetDetailId = extendedAssetDetailId;
   }

   /**
    * @return the shortNote
    */
   public String getShortNote () {
      return shortNote;
   }

   /**
    * @param shortNote
    *           the shortNote to set
    */
   public void setShortNote (String shortNote) {
      this.shortNote = shortNote;
   }

   public CheckType getIsAssetDisclaimerApplicable () {
      return isAssetDisclaimerApplicable;
   }

   public void setIsAssetDisclaimerApplicable (CheckType isAssetDisclaimerApplicable) {
      this.isAssetDisclaimerApplicable = isAssetDisclaimerApplicable;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

}
