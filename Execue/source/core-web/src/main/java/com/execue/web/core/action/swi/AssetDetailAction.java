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


package com.execue.web.core.action.swi;

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetDetail;
import com.execue.core.common.bean.entity.AssetExtendedDetail;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIAssetCreationInfo;
import com.execue.handler.bean.UIAssetDetail;

/**
 * @author JTiwari
 * @since 09/01/2010
 */
public class AssetDetailAction extends SWIAction {

   private static Logger       logger           = Logger.getLogger(AssetDetailAction.class);
   private static final long   serialVersionUID = 1L;
   private Long                assetId;
   private String              assetDisplayName;
   private String              assetDescription;
   private UIAssetDetail       uiAssetDetail;
   private List<Asset>         assets;
   private SDXStatus           operationStatus;
   private UIAssetCreationInfo assetCreationInfo;

   public String list () {
      try {
         assets = getSdxServiceHandler().getAllAssets(getApplicationContext().getAppId());
      } catch (ExeCueException e) {
         logger.error(e);
         return ERROR;
      }
      return SUCCESS;
   }

   public String showAssetDetailInfo () {
      uiAssetDetail = new UIAssetDetail();
      try {
         AssetDetail assetDetail = getAssetDetailServiceHandler().getAssetDetailInfo(assetId);
         if (assetDetail != null) {
            uiAssetDetail.setAssetDetailId(assetDetail.getId());
            uiAssetDetail.setShortNote(assetDetail.getShortNote());
            uiAssetDetail.setShortDisclaimer(assetDetail.getShortDisclaimer());
            AssetExtendedDetail assetExtendedDetail = getAssetDetailServiceHandler().getAssetExtendedDetailInfo(
                     assetDetail.getId());
            if (assetExtendedDetail != null) {
               uiAssetDetail.setExtendedAssetDetailId(assetExtendedDetail.getId());
               uiAssetDetail.setExtendedNote(assetExtendedDetail.getExtendedNote());
               uiAssetDetail.setExtendedDisclaimer(assetExtendedDetail.getExtendedDisclaimer());
            }
         }

      } catch (HandlerException e) {
         logger.error(e);
         return ERROR;
      }
      return SUCCESS;
   }

   public String updateAssetDetail () {
      try {
         getAssetDetailServiceHandler().createUpdateAssetDetail(assetId, uiAssetDetail);
         addActionMessage(getText("execue.assetdetail.update.success"));
      } catch (HandlerException handlerException) {
         addActionError(getText("execue.assetdetail.update.failure"));
         logger.error(handlerException);
         return ERROR;

      }
      return SUCCESS;
   }

   public String loadExtendedNote () {
      try {
         uiAssetDetail.setExtendedNote(getAssetDetailServiceHandler().getAssetExtendedDetailInfo(
                  uiAssetDetail.getAssetDetailId()).getExtendedNote());
      } catch (HandlerException e) {
         logger.error(e);
         return ERROR;
      }
      return SUCCESS;
   }

   public String showAssetCreationInfo () {
      try {
         assetCreationInfo = getAssetDetailServiceHandler().getAssetCreationInfo(assetId);
      } catch (HandlerException e) {
         logger.error(e);
         return ERROR;
      }
      return SUCCESS;
   }

   public String loadShortDisclaimer () {

      try {
         AssetDetail assetDetail = getAssetDetailServiceHandler().getAssetDetailInfo(assetId);
         uiAssetDetail = new UIAssetDetail();
         uiAssetDetail.setShortDisclaimer(assetDetail.getShortDisclaimer());
         uiAssetDetail.setAssetDetailId(assetDetail.getId());
      } catch (HandlerException e) {
         logger.error(e);
         return ERROR;
      }

      return SUCCESS;
   }

   public String loadExtendedDisclaimer () {
      try {
         uiAssetDetail.setExtendedDisclaimer(getAssetDetailServiceHandler().getAssetExtendedDetailInfo(
                  uiAssetDetail.getAssetDetailId()).getExtendedDisclaimer());
      } catch (HandlerException e) {
         logger.error(e);
         return ERROR;
      }
      return SUCCESS;
   }

   public String saveAjaxNotes () {
      operationStatus = new SDXStatus();
      try {
         getAssetDetailServiceHandler().createUpdateAssetDetailByApplication(uiAssetDetail, false);
         operationStatus.setMessage(getText("execue.assetdetail.note.update.success"));
      } catch (HandlerException e) {
         operationStatus.setMessage(getText("execue.assetdetail.note.update.failure"));
         e.printStackTrace();
      }

      return SUCCESS;
   }

   public String saveAjaxDisclaimer () {
      operationStatus = new SDXStatus();
      try {
         getAssetDetailServiceHandler().createUpdateAssetDetailByApplication(uiAssetDetail, true);
         operationStatus.setMessage(getText("execue.assetdetail.disclaimer.update.success"));
      } catch (HandlerException e) {
         operationStatus.setMessage("assetdetail.disclaimer.update.failure");
         e.printStackTrace();
      }
      return SUCCESS;
   }

   /**
    * @return the uiAssetDetail
    */
   public UIAssetDetail getUiAssetDetail () {
      return uiAssetDetail;
   }

   /**
    * @param uiAssetDetail
    *           the uiAssetDetail to set
    */
   public void setUiAssetDetail (UIAssetDetail uiAssetDetail) {
      this.uiAssetDetail = uiAssetDetail;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   /**
    * @return the assets
    */
   public List<Asset> getAssets () {
      return assets;
   }

   /**
    * @param assets
    *           the assets to set
    */
   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   /**
    * @return the assetDisplayName
    */
   public String getAssetDisplayName () {
      return assetDisplayName;
   }

   /**
    * @param assetDisplayName
    *           the assetDisplayName to set
    */
   public void setAssetDisplayName (String assetDisplayName) {
      this.assetDisplayName = assetDisplayName;
   }

   /**
    * @return the assetDescription
    */
   public String getAssetDescription () {
      return assetDescription;
   }

   /**
    * @param assetDescription
    *           the assetDescription to set
    */
   public void setAssetDescription (String assetDescription) {
      this.assetDescription = assetDescription;
   }

   public SDXStatus getOperationStatus () {
      return operationStatus;
   }

   public void setOperationStatus (SDXStatus operationStatus) {
      this.operationStatus = operationStatus;
   }

   /**
    * @return the assetCreationInfo
    */
   public UIAssetCreationInfo getAssetCreationInfo () {
      return assetCreationInfo;
   }

   /**
    * @param assetCreationInfo the assetCreationInfo to set
    */
   public void setAssetCreationInfo (UIAssetCreationInfo assetCreationInfo) {
      this.assetCreationInfo = assetCreationInfo;
   }

}
