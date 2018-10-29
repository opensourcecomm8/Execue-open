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
import com.execue.core.common.bean.entity.DefaultDynamicValue;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.swi.IDefaultDynamicValueServiceHandler;

public class DefaultDynamicValueAction extends SWIAction {

   private static final long                  serialVersionUID = 1L;
   private static final Logger                logger           = Logger.getLogger(DefaultDynamicValueAction.class);
   private List<UIConcept>                    uiConcepts;
   private Long                               conceptAssetId;
   private Long                               conceptBedId;
   private List<Asset>                        assets;
   private List<DefaultDynamicValue>          uiDefaultDynamicValues;
   private IDefaultDynamicValueServiceHandler defaultDynamicValueServiceHandler;

   private void init () {
      try {
         setAssets(getDefaultDynamicValueServiceHandler().getAllAssets(getApplicationContext().getAppId()));
         setUiConcepts(getDefaultDynamicValueServiceHandler().getConceptsByType(getApplicationContext().getAssetId(),
                  getApplicationContext().getModelId(), ExecueConstants.TIME_FRAME_TYPE));

         if (logger.isDebugEnabled()) {
            logger.debug("Init Success with " + uiConcepts.size() + " uiConcepts");
         }
      } catch (HandlerException e) {
         e.printStackTrace();
         if (logger.isDebugEnabled()) {
            logger.error("Init failed to load uiConcepts for timeframe types for assetId "
                     + getApplicationContext().getAssetId() + " and modelId " + getApplicationContext().getModelId());
         }
      }
   }

   public String loadConcepts () {
      init();
      return SUCCESS;
   }

   public String getDefaultDynamicValue () {
      try {
         Long assetId = getApplicationContext().getAssetId();
         setUiDefaultDynamicValues(getDefaultDynamicValueServiceHandler().getDefaultDynamicValues(assetId,
                  getConceptBedId()));
      } catch (HandlerException e) {
         e.printStackTrace();
         return ERROR;
      }
      return SUCCESS;
   }

   public String createUpdateDynamicValues () {
      try {
         getDefaultDynamicValueServiceHandler().createDefaultDynamicValues(getUiDefaultDynamicValues());
      } catch (HandlerException e) {
         addActionError("Error: " + e.getMessage());
         return ERROR;
      }
      addActionMessage("Updated Successfully");
      return SUCCESS;
   }

   public IDefaultDynamicValueServiceHandler getDefaultDynamicValueServiceHandler () {
      return defaultDynamicValueServiceHandler;
   }

   public void setDefaultDynamicValueServiceHandler (
            IDefaultDynamicValueServiceHandler defaultDynamicValueServiceHandler) {
      this.defaultDynamicValueServiceHandler = defaultDynamicValueServiceHandler;
   }

   public List<UIConcept> getUiConcepts () {
      return uiConcepts;
   }

   public void setUiConcepts (List<UIConcept> uiConcepts) {
      this.uiConcepts = uiConcepts;
   }

   public Long getConceptBedId () {
      return conceptBedId;
   }

   public void setConceptBedId (Long conceptBedId) {
      this.conceptBedId = conceptBedId;
   }

   public Long getConceptAssetId () {
      return conceptAssetId;
   }

   public void setConceptAssetId (Long conceptAssetId) {
      this.conceptAssetId = conceptAssetId;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   public List<DefaultDynamicValue> getUiDefaultDynamicValues () {
      return uiDefaultDynamicValues;
   }

   public void setUiDefaultDynamicValues (List<DefaultDynamicValue> uiDefaultDynamicValues) {
      this.uiDefaultDynamicValues = uiDefaultDynamicValues;
   }
}
