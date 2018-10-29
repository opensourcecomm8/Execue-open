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


package com.execue.handler.swi;

import java.util.List;

import com.execue.core.common.bean.ApplicationContext;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIAsset;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIStatus;

public interface IMartManagementServiceHandler {

   public List<UIConcept> getEligibleProminentMeasures (Long modelId, Long assetId) throws HandlerException;

   public List<UIConcept> getEligibleProminentDimensions (Long modelId, Long assetId) throws HandlerException;

   public UIStatus createMart (ApplicationContext applicationContext, Asset targetAsset,
            List<UIConcept> prominentMeasures, List<UIConcept> prominentDimensions) throws HandlerException;

   public UIStatus updateMart (ApplicationContext applicationContext, Long existingAssetId) throws HandlerException;

   public UIStatus refreshMart (ApplicationContext applicationContext, Long existingAssetId) throws HandlerException;

   public List<Asset> getAllParentAssets (Long applicationId) throws HandlerException;

   public List<UIAsset> getMartsForApplication (Long appId) throws HandlerException;

   public MartCreationContext getMartCreationContextByAssetId (Long existingAssetId) throws HandlerException;

}
