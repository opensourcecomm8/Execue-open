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


package com.execue.ac.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.ac.bean.CubeCreationOutputInfo;
import com.execue.ac.bean.CubeRefreshOutputInfo;
import com.execue.ac.bean.CubeUpdationContext;
import com.execue.ac.bean.CubeUpdationDimensionInfo;
import com.execue.ac.bean.CubeUpdationOutputInfo;
import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.bean.MartRefreshOutputInfo;
import com.execue.ac.bean.MartUpdationContext;
import com.execue.ac.bean.MartUpdationOutputInfo;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.helper.CubeCreationServiceHelper;
import com.execue.ac.service.IAnswersCatalogUpdationService;
import com.execue.ac.service.ICubeCreationService;
import com.execue.ac.service.ICubeOperationalConstants;
import com.execue.ac.service.ICubeUpdationService;
import com.execue.ac.service.IDataMartCreationService;
import com.execue.ac.service.IDataMartUpdationService;
import com.execue.core.common.bean.ac.AnswersCatalogUpdationContext;
import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.swi.AssetDeletionContext;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.exception.AssetSynchronizationException;
import com.execue.platform.swi.IAssetDeletionWrapperService;
import com.execue.platform.swi.operation.synchronization.IAssetSyncPopulateService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * @author Vishay
 */
public class AnswersCatalogUpdationServiceImpl implements IAnswersCatalogUpdationService {

   private IAssetDeletionWrapperService assetDeletionWrapperService;
   private IAssetSyncPopulateService    assetSyncPopulateService;
   private ISDXRetrievalService         sdxRetrievalService;
   private ISDXManagementService        sdxManagementService;
   private IKDXRetrievalService         kdxRetrievalService;
   private ICubeCreationService         cubeCreationService;
   private ICubeUpdationService         cubeUpdationService;
   private IDataMartCreationService     dataMartCreationService;
   private IDataMartUpdationService     dataMartUpdationService;
   private IJobDataService              jobDataService;
   private CubeCreationServiceHelper    cubeCreationServiceHelper;
   private static final Logger          logger = Logger.getLogger(AnswersCatalogUpdationServiceImpl.class);

   @Override
   public CubeUpdationOutputInfo cubeupdate (AnswersCatalogUpdationContext answersCatalogUpdationContext)
            throws AnswersCatalogException {
      CubeUpdationOutputInfo cubeUpdationOutputInfo = null;
      JobOperationalStatus jobOperationalStatus = null;
      try {
         Long existingAssetId = answersCatalogUpdationContext.getExistingAssetId();
         CubeCreationContext existingCubeCreationContext = getJobDataService().getCubeCreationContextByAssetId(
                  existingAssetId);
         CubeUpdationContext cubeUpdationContext = buildCubeUpdationContext(existingCubeCreationContext,
                  answersCatalogUpdationContext);
         if (cubeUpdationContext == null) {
            JobRequest jobRequest = answersCatalogUpdationContext.getJobRequest();
            jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                     ICubeOperationalConstants.VALIDATE_CHANGES_IN_CUBE_SWI_TO_SOURCE_SWI);
            if (logger.isInfoEnabled()) {
               logger.info("There is no change in cube swi to source swi");
            }
            cubeUpdationOutputInfo = new CubeUpdationOutputInfo();
            cubeUpdationOutputInfo.setUpdationSuccessful(false);
            cubeUpdationOutputInfo.setFailureReason(ICubeOperationalConstants.NO_CHANGES_IN_CUBE_SWI_TO_SOURCE_SWI);
            getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                     ICubeOperationalConstants.NO_CHANGES_IN_CUBE_SWI_TO_SOURCE_SWI);

         } else {
            Asset targetAsset = cubeUpdationContext.getTargetAsset();
            getCubeCreationServiceHelper().inActivateAsset(targetAsset);
            cubeUpdationOutputInfo = getCubeUpdationService().updateCube(cubeUpdationContext);
            getCubeCreationServiceHelper().activateAsset(targetAsset);
         }
      } catch (QueryDataException e) {
         throw new AnswersCatalogException(e.getCode(), "Failed to Update the Cube");
      } catch (KDXException e) {
         throw new AnswersCatalogException(e.getCode(), "Failed to Update the Cube");
      } catch (AssetSynchronizationException e) {
         throw new AnswersCatalogException(e.getCode(), "Failed to Update the Cube");
      }
      return cubeUpdationOutputInfo;
   }

   private CubeUpdationContext buildCubeUpdationContext (CubeCreationContext existingCubeCreationContext,
            AnswersCatalogUpdationContext answersCatalogUpdationContext) throws KDXException,
            AssetSynchronizationException {
      CubeUpdationContext cubeUpdationContext = null;
      List<CubeUpdationDimensionInfo> cubeUpdationDimensionInfoList = new ArrayList<CubeUpdationDimensionInfo>();
      Long parentAssetId = existingCubeCreationContext.getSourceAsset().getId();
      Long modelId = answersCatalogUpdationContext.getModelId();
      Long existingAssetId = answersCatalogUpdationContext.getExistingAssetId();
      List<String> simpleLookupDimensions = existingCubeCreationContext.getSimpleLookupDimensions();
      if (ExecueCoreUtil.isCollectionNotEmpty(simpleLookupDimensions)) {
         for (String dimensionName : simpleLookupDimensions) {
            Concept concept = getKdxRetrievalService().getConceptByName(modelId, dimensionName);
            List<Membr> addedMembers = getAssetSyncPopulateService().getAddedMembers(parentAssetId, existingAssetId,
                     modelId, concept.getId());
            List<Membr> deletedMembers = getAssetSyncPopulateService().getDeletedMembers(parentAssetId,
                     existingAssetId, modelId, concept.getId());
            if (ExecueCoreUtil.isCollectionNotEmpty(addedMembers)
                     || ExecueCoreUtil.isCollectionNotEmpty(deletedMembers)) {
               CubeUpdationDimensionInfo cubeUpdationDimensionInfo = new CubeUpdationDimensionInfo();
               cubeUpdationDimensionInfo.setDimensionName(dimensionName);
               cubeUpdationDimensionInfo.setAddedMembers(addedMembers);
               cubeUpdationDimensionInfo.setDeletedMembers(deletedMembers);
               cubeUpdationDimensionInfoList.add(cubeUpdationDimensionInfo);
            }
         }

         // if atleast one dimension is changed
         if (cubeUpdationDimensionInfoList.size() > 0) {
            JobRequest jobRequest = answersCatalogUpdationContext.getJobRequest();
            cubeUpdationContext = new CubeUpdationContext();
            cubeUpdationContext.setApplicationId(existingCubeCreationContext.getApplicationId());
            cubeUpdationContext.setFrequencyMeasures(existingCubeCreationContext.getFrequencyMeasures());
            cubeUpdationContext.setMeasures(existingCubeCreationContext.getMeasures());
            cubeUpdationContext.setModelId(existingCubeCreationContext.getModelId());
            cubeUpdationContext.setRangeLookupDimensions(existingCubeCreationContext.getRangeLookupDimensions());
            cubeUpdationContext.setSimpleLookupDimensions(existingCubeCreationContext.getSimpleLookupDimensions());
            cubeUpdationContext.setSourceAsset(existingCubeCreationContext.getSourceAsset());
            cubeUpdationContext.setTargetAsset(existingCubeCreationContext.getTargetAsset());
            cubeUpdationContext.setTargetDataSourceSameAsSourceDataSource(existingCubeCreationContext
                     .isTargetDataSourceSameAsSourceDataSource());
            cubeUpdationContext.setUserId(existingCubeCreationContext.getUserId());
            cubeUpdationContext.setJobRequest(jobRequest);
            cubeUpdationContext.setCubeUpdationDimensionInfoList(cubeUpdationDimensionInfoList);
            cubeUpdationContext.setExistingAssetId(existingAssetId);
            // update the target asset with asset id
            cubeUpdationContext.getTargetAsset().setId(existingAssetId);
         }
      }
      return cubeUpdationContext;
   }

   @Override
   public MartUpdationOutputInfo martUpdate (AnswersCatalogUpdationContext answersCatalogUpdationContext)
            throws AnswersCatalogException {
      MartUpdationOutputInfo martUpdationOutputInfo = null;
      try {
         Long existingAssetId = answersCatalogUpdationContext.getExistingAssetId();
         MartCreationContext existingMartCreationContext = getJobDataService().getMartCreationContextByAssetId(
                  existingAssetId);

         MartUpdationContext martUpdationContext = buildMartUpdationContext(existingMartCreationContext,
                  answersCatalogUpdationContext);
         if (martUpdationContext == null) {
            if (logger.isInfoEnabled()) {
               logger.info("There is no change in cube swi to source swi");
            }
            martUpdationOutputInfo = new MartUpdationOutputInfo();
            martUpdationOutputInfo.setUpdationSuccessful(false);
         } else {
            Asset targetAsset = martUpdationContext.getTargetAsset();
            getCubeCreationServiceHelper().inActivateAsset(targetAsset);
            martUpdationOutputInfo = getDataMartUpdationService().updateDataMart(martUpdationContext);
            getCubeCreationServiceHelper().activateAsset(targetAsset);
         }

      } catch (QueryDataException e) {
         throw new AnswersCatalogException(e.getCode(), "Failed to Update the Mart");
      }
      return martUpdationOutputInfo;
   }

   private MartUpdationContext buildMartUpdationContext (MartCreationContext existingMartCreationContext,
            AnswersCatalogUpdationContext answersCatalogUpdationContext) {
      MartUpdationContext martUpdationContext = new MartUpdationContext();
      martUpdationContext.setJobRequest(answersCatalogUpdationContext.getJobRequest());
      martUpdationContext.setApplicationId(existingMartCreationContext.getApplicationId());
      martUpdationContext.setDistributions(existingMartCreationContext.getDistributions());
      martUpdationContext.setModelId(existingMartCreationContext.getModelId());
      martUpdationContext.setPopulation(existingMartCreationContext.getPopulation());
      martUpdationContext.setProminentDimensions(existingMartCreationContext.getProminentDimensions());
      martUpdationContext.setProminentMeasures(existingMartCreationContext.getProminentMeasures());
      martUpdationContext.setSourceAsset(existingMartCreationContext.getSourceAsset());
      martUpdationContext.setTargetAsset(existingMartCreationContext.getTargetAsset());
      martUpdationContext.setTargetDataSourceSameAsSourceDataSource(existingMartCreationContext
               .isTargetDataSourceSameAsSourceDataSource());
      martUpdationContext.getTargetAsset().setId(answersCatalogUpdationContext.getExistingAssetId());
      martUpdationContext.setUserId(existingMartCreationContext.getUserId());
      return martUpdationContext;

   }

   @Override
   public MartRefreshOutputInfo martRefresh (AnswersCatalogUpdationContext answersCatalogUpdationContext)
            throws AnswersCatalogException {
      MartRefreshOutputInfo martRefreshOutputInfo = null;
      try {
         Long existingAssetId = answersCatalogUpdationContext.getExistingAssetId();
         JobRequest jobRequest = answersCatalogUpdationContext.getJobRequest();
         MartCreationContext existingMartCreationContext = getJobDataService().getMartCreationContextByAssetId(
                  existingAssetId);
         existingMartCreationContext.setJobRequest(jobRequest);
         // give new name to asset
         String originalAssetName = existingMartCreationContext.getTargetAsset().getName();
         existingMartCreationContext.getTargetAsset().setName(
                  getUniqueNonExistentAssetName(answersCatalogUpdationContext.getApplicationId(), originalAssetName));
         MartCreationOutputInfo martCreationOutputInfo = getDataMartCreationService().dataMartCreation(
                  existingMartCreationContext);
         if (martCreationOutputInfo != null) {
            Asset existingAsset = getSdxRetrievalService().getAsset(existingAssetId);
            AssetDeletionContext assetDeletionContext = new AssetDeletionContext();
            assetDeletionContext.setAssetId(existingAsset.getId());
            assetDeletionContext.setJobRequest(jobRequest);
            assetDeletionContext.setUserId(existingMartCreationContext.getUserId());
            getAssetDeletionWrapperService().deleteAsset(assetDeletionContext);

            martRefreshOutputInfo = new MartRefreshOutputInfo();
            martRefreshOutputInfo.setRefreshSuccessful(true);
            martRefreshOutputInfo.setMartCreationOutputInfo(martCreationOutputInfo);
            Asset targetAsset = martCreationOutputInfo.getMartCreationInputInfo().getMartCreationPopulatedContext()
                     .getTargetAsset();
            targetAsset = getSdxRetrievalService().getAssetById(targetAsset.getId());
            // update the asset to original name
            targetAsset.setName(originalAssetName);
            getSdxManagementService().updateAsset(targetAsset);
         }
      } catch (SDXException sdxException) {
         throw new AnswersCatalogException(sdxException.getCode(), "Failed to Refresh the Mart");
      } catch (QueryDataException e) {
         throw new AnswersCatalogException(e.getCode(), "Failed to Refresh the Mart");
      }
      return martRefreshOutputInfo;
   }

   private String getUniqueNonExistentAssetName (Long applicationId, String assetName) throws SDXException {
      boolean uniqueNonExistentTableFound = false;
      String normalizedTableName = assetName;
      do {
         uniqueNonExistentTableFound = !getSdxRetrievalService().isAssetExists(applicationId, normalizedTableName);
         if (uniqueNonExistentTableFound) {
            break;
         } else {
            normalizedTableName = assetName + "_" + ExecueCoreUtil.getRandomNumberLessThanHundred();
         }
      } while (!uniqueNonExistentTableFound);
      return normalizedTableName;
   }

   @Override
   public CubeRefreshOutputInfo cubeRefresh (AnswersCatalogUpdationContext answersCatalogUpdationContext)
            throws AnswersCatalogException {
      CubeRefreshOutputInfo cubeRefreshOutputInfo = null;
      try {
         Long existingAssetId = answersCatalogUpdationContext.getExistingAssetId();
         JobRequest jobRequest = answersCatalogUpdationContext.getJobRequest();
         CubeCreationContext existingCubeCreationContext = getJobDataService().getCubeCreationContextByAssetId(
                  existingAssetId);
         existingCubeCreationContext.setJobRequest(jobRequest);
         // give new name to asset
         String originalAssetName = existingCubeCreationContext.getTargetAsset().getName();
         existingCubeCreationContext.getTargetAsset().setName(
                  getUniqueNonExistentAssetName(answersCatalogUpdationContext.getApplicationId(), originalAssetName));

         CubeCreationOutputInfo cubeCreationOutputInfo = getCubeCreationService().cubeCreation(
                  existingCubeCreationContext);
         if (cubeCreationOutputInfo != null) {
            Asset existingAsset = getSdxRetrievalService().getAsset(existingAssetId);
            AssetDeletionContext assetDeletionContext = new AssetDeletionContext();
            assetDeletionContext.setAssetId(existingAsset.getId());
            assetDeletionContext.setJobRequest(jobRequest);
            assetDeletionContext.setUserId(existingCubeCreationContext.getUserId());
            getAssetDeletionWrapperService().deleteAsset(assetDeletionContext);

            cubeRefreshOutputInfo = new CubeRefreshOutputInfo();
            cubeRefreshOutputInfo.setRefreshSuccessful(true);
            cubeRefreshOutputInfo.setCubeCreationOutputInfo(cubeCreationOutputInfo);

            Asset targetAsset = cubeCreationOutputInfo.getCubeCreationInputInfo().getCubeCreationPopulatedContext()
                     .getTargetAsset();
            targetAsset = getSdxRetrievalService().getAssetById(targetAsset.getId());
            // update the asset to original name
            targetAsset.setName(originalAssetName);
            getSdxManagementService().updateAsset(targetAsset);

         }
      } catch (SDXException sdxException) {
         throw new AnswersCatalogException(sdxException.getCode(), "Failed to Refresh the Cube");
      } catch (QueryDataException e) {
         throw new AnswersCatalogException(e.getCode(), "Failed to Refresh the Cube");
      }
      return cubeRefreshOutputInfo;
   }

   public ICubeCreationService getCubeCreationService () {
      return cubeCreationService;
   }

   public void setCubeCreationService (ICubeCreationService cubeCreationService) {
      this.cubeCreationService = cubeCreationService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IDataMartCreationService getDataMartCreationService () {
      return dataMartCreationService;
   }

   public void setDataMartCreationService (IDataMartCreationService dataMartCreationService) {
      this.dataMartCreationService = dataMartCreationService;
   }

   public IAssetDeletionWrapperService getAssetDeletionWrapperService () {
      return assetDeletionWrapperService;
   }

   public void setAssetDeletionWrapperService (IAssetDeletionWrapperService assetDeletionWrapperService) {
      this.assetDeletionWrapperService = assetDeletionWrapperService;
   }

   public ICubeUpdationService getCubeUpdationService () {
      return cubeUpdationService;
   }

   public void setCubeUpdationService (ICubeUpdationService cubeUpdationService) {
      this.cubeUpdationService = cubeUpdationService;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IAssetSyncPopulateService getAssetSyncPopulateService () {
      return assetSyncPopulateService;
   }

   public void setAssetSyncPopulateService (IAssetSyncPopulateService assetSyncPopulateService) {
      this.assetSyncPopulateService = assetSyncPopulateService;
   }

   public IDataMartUpdationService getDataMartUpdationService () {
      return dataMartUpdationService;
   }

   public void setDataMartUpdationService (IDataMartUpdationService dataMartUpdationService) {
      this.dataMartUpdationService = dataMartUpdationService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

   public CubeCreationServiceHelper getCubeCreationServiceHelper () {
      return cubeCreationServiceHelper;
   }

   public void setCubeCreationServiceHelper (CubeCreationServiceHelper cubeCreationServiceHelper) {
      this.cubeCreationServiceHelper = cubeCreationServiceHelper;
   }

}
