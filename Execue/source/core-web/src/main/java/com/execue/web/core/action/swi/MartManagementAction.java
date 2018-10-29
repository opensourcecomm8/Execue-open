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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.ac.configuration.IAnswersCatalogConfigurationService;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.handler.bean.UIAsset;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIStatus;
import com.execue.handler.swi.IMartManagementServiceHandler;

public class MartManagementAction extends SWIAction {

   private static final long                   serialVersionUID = 1L;
   private static final Logger                 log              = Logger.getLogger(MartManagementAction.class);

   private Long                                existingAssetId;
   private Asset                               targetAsset;
   private List<Asset>                         assets;
   private Asset                               asset;
   private List<UIConcept>                     eligibleProminentMeasures;
   private List<UIConcept>                     eligibleProminentDimensions;
   private List<UIConcept>                     selectedProminentMeasures;
   private List<UIConcept>                     selectedProminentDimensions;
   private IMartManagementServiceHandler       martManagementServiceHandler;
   private IAnswersCatalogConfigurationService answersCatalogConfigurationService;
   private Long                                jobRequestId;
   protected Integer                           maxDimensions;
   protected Integer                           maxMeasures;
   private List<UIAsset>                       marts;
   private MartCreationContext                 martCreationContext;
   private UIStatus                            uiStatus;

   // Methods

   public String input () {
      try {
         setAssets(getMartManagementServiceHandler().getAllParentAssets(getApplicationContext().getAppId()));
         Long sessionAssetId = getApplicationContext().getAssetId();
         if (sessionAssetId != null) {
            for (Asset localAsset : getAssets()) {
               if (localAsset.getId().longValue() == sessionAssetId.longValue()) {
                  asset = localAsset;
                  break;
               }
            }
         }
         setEligibleProminentDimensions(getMartManagementServiceHandler().getEligibleProminentDimensions(
                  getApplicationContext().getModelId(), getApplicationContext().getAssetId()));
         setEligibleProminentMeasures(getMartManagementServiceHandler().getEligibleProminentMeasures(
                  getApplicationContext().getModelId(), getApplicationContext().getAssetId()));
         setMaxDimensions(getAnswersCatalogConfigurationService().getMartMaxDimensions());
         setMaxMeasures(getAnswersCatalogConfigurationService().getMartMaxMeasures());
      } catch (HandlerException e) {
         e.printStackTrace();
         return ERROR;
      }

      return SUCCESS;
   }

   public String showMarts () {
      try {
         setMarts(getMartManagementServiceHandler().getMartsForApplication(getApplicationContext().getAppId()));
      } catch (ExeCueException e) {
         setMarts(new ArrayList<UIAsset>());
         e.printStackTrace();
         return ERROR;
      }

      return SUCCESS;
   }

   public String editMart () {
      try {
         martCreationContext = getMartManagementServiceHandler().getMartCreationContextByAssetId(existingAssetId);
      } catch (Exception e) {
         log.error(e);
         addActionError(getText("execue.global.error", new String[] { getText("execue.errors.general") }));
      }

      return SUCCESS;
   }

   public String createMart () {
      try {
         if (log.isDebugEnabled()) {
            log.debug("Create mart method called");
            log.debug("Name::" + targetAsset.getName());
            log.debug("Description::" + targetAsset.getDescription());
            log.debug("model id::" + getApplicationContext().getModelId());
            log.debug("soruce Asset id::" + getApplicationContext().getAssetId());
         }
         targetAsset.setName(ExecueStringUtil.getNormalizedName(ExecueCoreUtil.generateAlphanumericString(targetAsset
                  .getDisplayName())));
         if (getSdxServiceHandler().isAssetExists(getApplicationContext().getAppId(), targetAsset.getName())) {
            uiStatus = new UIStatus();
            uiStatus.addErrorMessage(getText("execue.global.exist.message", new String[] { targetAsset.getName() }));
            uiStatus.setStatus(false);
            return ERROR;
         }
         uiStatus = getMartManagementServiceHandler().createMart(getApplicationContext(), targetAsset,
                  selectedProminentMeasures, selectedProminentDimensions);
      } catch (ExeCueException e) {
         log.error(e);
         if (uiStatus == null) {
            uiStatus = new UIStatus();
         }
         uiStatus.addErrorMessage(getText("execue.global.error", new String[] { getText("execue.errors.general") }));
         uiStatus.setStatus(false);
         return ERROR;
      }
      return SUCCESS;
   }

   public String updateMart () {
      try {
         uiStatus = getMartManagementServiceHandler().updateMart(getApplicationContext(), existingAssetId);
      } catch (ExeCueException e) {
         log.error(e);
         if (uiStatus == null) {
            uiStatus = new UIStatus();
         }
         uiStatus.setStatus(false);
         uiStatus.addErrorMessage(getText("execue.global.error", new String[] { getText("execue.errors.general") }));
         return ERROR;
      }
      return SUCCESS;
   }

   public String refreshMart () {
      try {
         uiStatus = getMartManagementServiceHandler().refreshMart(getApplicationContext(), existingAssetId);
      } catch (ExeCueException e) {
         log.error(e);
         if (uiStatus == null) {
            uiStatus = new UIStatus();
         }
         uiStatus.setStatus(false);
         uiStatus.addErrorMessage(getText("execue.global.error", new String[] { getText("execue.errors.general") }));
         return ERROR;
      }
      return SUCCESS;
   }

   public Asset getTargetAsset () {
      return targetAsset;
   }

   public void setTargetAsset (Asset targetAsset) {
      this.targetAsset = targetAsset;
   }

   public List<UIConcept> getSelectedProminentMeasures () {
      return selectedProminentMeasures;
   }

   public void setSelectedProminentMeasures (List<UIConcept> selectedProminentMeasures) {
      this.selectedProminentMeasures = selectedProminentMeasures;
   }

   public List<UIConcept> getSelectedProminentDimensions () {
      return selectedProminentDimensions;
   }

   public void setSelectedProminentDimensions (List<UIConcept> selectedProminentDimensions) {
      this.selectedProminentDimensions = selectedProminentDimensions;
   }

   public List<UIConcept> getEligibleProminentMeasures () {
      return eligibleProminentMeasures;
   }

   public void setEligibleProminentMeasures (List<UIConcept> eligibleProminentMeasures) {
      this.eligibleProminentMeasures = eligibleProminentMeasures;
   }

   public List<UIConcept> getEligibleProminentDimensions () {
      return eligibleProminentDimensions;
   }

   public void setEligibleProminentDimensions (List<UIConcept> eligibleProminentDimensions) {
      this.eligibleProminentDimensions = eligibleProminentDimensions;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   public Integer getMaxDimensions () {
      return maxDimensions;
   }

   public void setMaxDimensions (Integer maxDimensions) {
      this.maxDimensions = maxDimensions;
   }

   public Integer getMaxMeasures () {
      return maxMeasures;
   }

   public void setMaxMeasures (Integer maxMeasures) {
      this.maxMeasures = maxMeasures;
   }

   public Long getExistingAssetId () {
      return existingAssetId;
   }

   public void setExistingAssetId (Long existingAssetId) {
      this.existingAssetId = existingAssetId;
   }

   /**
    * @return the jobRequestId
    */
   public Long getJobRequestId () {
      return jobRequestId;
   }

   /**
    * @param jobRequestId
    *           the jobRequestId to set
    */
   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   /**
    * @return the answersCatalogConfigurationService
    */
   public IAnswersCatalogConfigurationService getAnswersCatalogConfigurationService () {
      return answersCatalogConfigurationService;
   }

   /**
    * @param answersCatalogConfigurationService
    *           the answersCatalogConfigurationService to set
    */
   public void setAnswersCatalogConfigurationService (
            IAnswersCatalogConfigurationService answersCatalogConfigurationService) {
      this.answersCatalogConfigurationService = answersCatalogConfigurationService;
   }

   public IMartManagementServiceHandler getMartManagementServiceHandler () {
      return martManagementServiceHandler;
   }

   public void setMartManagementServiceHandler (IMartManagementServiceHandler martManagementServiceHandler) {
      this.martManagementServiceHandler = martManagementServiceHandler;
   }

   /**
    * @return the marts
    */
   public List<UIAsset> getMarts () {
      return marts;
   }

   /**
    * @param marts the marts to set
    */
   public void setMarts (List<UIAsset> marts) {
      this.marts = marts;
   }

   /**
    * @return the martCreationContext
    */
   public MartCreationContext getMartCreationContext () {
      return martCreationContext;
   }

   /**
    * @param martCreationContext the martCreationContext to set
    */
   public void setMartCreationContext (MartCreationContext martCreationContext) {
      this.martCreationContext = martCreationContext;
   }

   /**
    * @return the uiStatus
    */
   public UIStatus getUiStatus () {
      return uiStatus;
   }

   /**
    * @param uiStatus the uiStatus to set
    */
   public void setUiStatus (UIStatus uiStatus) {
      this.uiStatus = uiStatus;
   }

   /**
    * @return the asset
    */
   public Asset getAsset () {
      return asset;
   }

   /**
    * @param asset the asset to set
    */
   public void setAsset (Asset asset) {
      this.asset = asset;
   }

}
