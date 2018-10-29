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


package com.execue.handler.swi.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.swi.AssetAnalysisReport;
import com.execue.core.common.bean.swi.PublishAssetContext;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.swi.IAssetPublishServiceHandler;
import com.execue.platform.exception.AssetAnalysisException;
import com.execue.platform.swi.operation.analysis.IAssetAnalysisService;
import com.execue.scheduler.service.IPublishAssetJobService;
import com.execue.security.UserContextService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;

public class AssetPublishServiceHandlerImpl extends UserContextService implements IAssetPublishServiceHandler {

   private IPublishAssetJobService      publishAssetJobService;
   private IAssetAnalysisService        assetAnalysisService;
   private IMappingRetrievalService     mappingRetrievalService;
   private IApplicationRetrievalService applicationRetrievalService;

   public AssetAnalysisReport fetchAssetAnalysisReport (Long assetId) throws HandlerException {
      try {
         return assetAnalysisService.fetchAssetAnalysisReport(assetId);
      } catch (AssetAnalysisException e) {
         throw new HandlerException(e.getCode(), e.getMessage());
      }
   }

   public boolean populateAssetAnalysisReport (Long assetId, PublishAssetMode mode) throws HandlerException {
      try {
         return assetAnalysisService.populateAssetAnalysisReport(assetId, mode);
      } catch (AssetAnalysisException e) {
         throw new HandlerException(e.getCode(), e.getMessage());
      }
   }

   public Long schedulePublishAssetsJob (Long applicationId, Long modelId, Long assetId, PublishAssetMode publishMode)
            throws HandlerException {
      Long jobRequestId = null;
      try {
         List<Long> assetIds = new ArrayList<Long>();
         assetIds.add(assetId);
         PublishAssetContext publishAssetContext = new PublishAssetContext();
         publishAssetContext.setModelId(modelId);
         publishAssetContext.setAssetId(assetId);
         publishAssetContext.setApplicationId(applicationId);
         publishAssetContext.setPublishMode(publishMode);
         publishAssetContext.setUserId(getUserContext().getUser().getId());
         jobRequestId = publishAssetJobService.schedulePublishAssetJob(publishAssetContext);

      } catch (SWIException swiException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, swiException.getMessage());
      }
      return jobRequestId;
   }

   public boolean checkPublishEligibility (Long applicationId, Long assetId) throws HandlerException {
      try {
         Application application = getApplicationRetrievalService().getApplicationById(applicationId);

         boolean publishEligibility = true;
         if (application.getSourceType() == AppSourceType.STRUCTURED
                  && ExecueCoreUtil.isCollectionEmpty(getMappingRetrievalService().getExistingMappingsByLimit(assetId,
                           1))) {
            publishEligibility = false;
         }
         return publishEligibility;
      } catch (SWIException swiException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, swiException.getMessage());
      }
   }

   public IPublishAssetJobService getPublishAssetJobService () {
      return publishAssetJobService;
   }

   public void setPublishAssetJobService (IPublishAssetJobService publishAssetJobService) {
      this.publishAssetJobService = publishAssetJobService;
   }

   public IAssetAnalysisService getAssetAnalysisService () {
      return assetAnalysisService;
   }

   public void setAssetAnalysisService (IAssetAnalysisService assetAnalysisService) {
      this.assetAnalysisService = assetAnalysisService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

}
