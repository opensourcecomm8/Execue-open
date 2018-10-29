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
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.type.ColumnType;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIAsset;
import com.execue.handler.bean.UICubeCreation;
import com.execue.handler.bean.UIInstanceMember;
import com.execue.handler.bean.UIStatus;

public interface ICubeManagementServiceHandler {

   public List<Concept> getEligibleDimensions (Long assetId) throws HandlerException;

   public List<UIInstanceMember> getConceptDetails (Long modelId, Long conceptId, Long assetId) throws HandlerException;

   public UIStatus createCube (UICubeCreation cubeCreationContext, ApplicationContext applicationContext)
            throws HandlerException;

   public Asset getAssetById (Long assetId) throws HandlerException;

   public Concept getConceptById (Long conceptId) throws HandlerException;

   public Concept getConceptByBEDId (Long conceptBedId) throws HandlerException;

   public List<String> getDefaultStats () throws HandlerException;

   public List<String> getDefaultDimensions (Long assetId) throws HandlerException;

   public ColumnType getConceptKDXType (Long modelId, Long conceptId, Long assetId) throws HandlerException;

   public int getMappedInstanceCount (Long modelId, Long conceptId, Long assetId) throws HandlerException;

   public List<UIAsset> getAssetsByTypeForApplication (Long appId) throws HandlerException;

   public CubeCreationContext getCubeCreationContextByAssetId (Long assetId) throws HandlerException;

   public List<Concept> getEligibleConceptsOfAssetForCubeByPage (Long assetId, Page page) throws HandlerException;

   public UIStatus updateCube (Long existingAssetId, ApplicationContext applicationContext) throws HandlerException;

   public UIStatus refreshCube (Long existingAssetId, ApplicationContext applicationContext) throws HandlerException;

   public Range getSuggestedRangesForConcept (Long modelId, Long assetId, Long conceptBedId) throws HandlerException;
}
