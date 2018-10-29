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


package com.execue.ac.algorithm.optimaldsetoldversion.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.ac.algorithm.optimaldsetoldversion.IOptimalDSetEvaluationService;
import com.execue.ac.algorithm.optimaldsetoldversion.IOptimalDsetInvocationService;
import com.execue.ac.algorithm.optimaldsetoldversion.OptimalDSetGenerator;
import com.execue.ac.algorithm.optimaldsetoldversion.OptimalDSetHelper;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.service.ICubeCreationService;
import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.optimaldset.OptimalDSet;
import com.execue.core.common.bean.optimaldset.OptimalDSetAlgorithmInput;
import com.execue.core.common.bean.qdata.QDataDimensionCombinationInfo;
import com.execue.core.common.bean.qdata.QDataDimensionInfo;
import com.execue.core.common.bean.qdata.QDataDimensionInput;
import com.execue.core.common.type.JobType;
import com.execue.core.configuration.ExecueConfiguration;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IQueryDataService;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * This service is used to invoke the optimalDSet algorithm
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class OptimalDSetInvocationServiceImpl implements IOptimalDsetInvocationService {

   private static final Logger           log = Logger.getLogger(OptimalDSetInvocationServiceImpl.class);

   private ICoreConfigurationService     coreConfigurationService;
   private ISDXRetrievalService          sdxRetrievalService;
   private IMappingRetrievalService      mappingRetrievalService;
   private IQueryDataService             queryDataService;
   private OptimalDSetGenerator          optimalDSetGenerator;
   private IOptimalDSetEvaluationService optimalDSetEvaluationService;
   private ICubeCreationService          cubeCreationService;
   private OptimalDSetHelper             optimalDSetHelper;
   private ExecueConfiguration           aggregationConfiguration;

   public void invokeOptimalDSetAlgorithm (Long applicationId, Long modelId) throws AnswersCatalogException {
      try {
         List<Asset> parentAssets = getSdxRetrievalService().getAllParentAssets(applicationId);
         for (Asset asset : parentAssets) {
            if (log.isDebugEnabled()) {
               log.debug("\nPreparing Optimal DSets for the Asset: " + asset.getName());
            }
            QDataDimensionInput dataDimensionInput = getQueryDataService().getQDataDimensionInput(asset.getId());
            // update the qdata dimensions with the no of members information
            updateNoOfMembersInfo(dataDimensionInput, asset.getId());
            List<QDataDimensionInfo> qDataDimensions = dataDimensionInput.getQDataDimensions();
            List<QDataDimensionCombinationInfo> qDataDimensionCombinations = dataDimensionInput
                     .getQDataDimensionCombinations();
            OptimalDSetAlgorithmInput optimalDSetAlgorithmInput = OptimalDSetHelper.populateOptimalDSetAlgorithmInput(
                     qDataDimensions, qDataDimensionCombinations);
            List<OptimalDSet> outputOptimalDSets = (List<OptimalDSet>) getOptimalDSetGenerator().generateOptimalDSets(
                     optimalDSetAlgorithmInput);
            OptimalDSetHelper.displayOptimalDSet(outputOptimalDSets);

            List<OptimalDSet> analysedOptimalDSets = getOptimalDSetEvaluationService().analyseOptimalDSets(
                     outputOptimalDSets);

            for (OptimalDSet optimalDSet : analysedOptimalDSets) {
               String cubeName = optimalDSet.getDimensionNames();
               // // prepare the target asset name
               // String targetAssetName = "OptimalDSetCube";
               CubeCreationContext cubeCreationContext = getOptimalDSetHelper().populateCubeCreationContext(
                        optimalDSet, asset, cubeName, modelId, applicationId);
               JobRequest jobRequest = new JobRequest();
               jobRequest.setId(1L);
               jobRequest.setUserId(getCoreConfigurationService().getAdminUserId());
               jobRequest.setJobType(JobType.CUBE_CREATION);
               cubeCreationContext.setJobRequest(jobRequest);
               getCubeCreationService().cubeCreation(cubeCreationContext);
            }
         }
      } catch (SDXException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      } catch (QueryDataException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      } catch (MappingException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   private void updateNoOfMembersInfo (QDataDimensionInput dataDimensionInput, Long assetId) throws MappingException {
      if (CollectionUtils.isEmpty(dataDimensionInput.getQDataDimensions())) {
         return;
      }
      for (QDataDimensionInfo dataDimensionInfo : dataDimensionInput.getQDataDimensions()) {
         long businessEntityId = dataDimensionInfo.getBusinessEntityId();
         long noOfMembers = getMappingRetrievalService().getMappedMemberCount(assetId, businessEntityId);
         // NOTE: Assumption here is when we get the noOfMembers as zero, it is range dimensions(measures), so should
         // set it to the dynamic range band count plus one.
         // TODO: Later if we get noOfMembers as zero for non measure dimension then should handle it accordingly
         if (noOfMembers == 0) {
            // get the number of bands from config
            // TODO - VG- configuration
            int bandCount = 3;
            // int bandCount = getAggregationConfiguration().getInt(
            // AggregationConfigurationConstants.DYNAMIC_RANGES_BAND_COUNT_KEY);
            noOfMembers = bandCount + 1;
         }
         dataDimensionInfo.setNoOfMembers(noOfMembers);
      }
   }

   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

   public OptimalDSetGenerator getOptimalDSetGenerator () {
      return optimalDSetGenerator;
   }

   public void setOptimalDSetGenerator (OptimalDSetGenerator optimalDSetGenerator) {
      this.optimalDSetGenerator = optimalDSetGenerator;
   }

   public IOptimalDSetEvaluationService getOptimalDSetEvaluationService () {
      return optimalDSetEvaluationService;
   }

   public void setOptimalDSetEvaluationService (IOptimalDSetEvaluationService optimalDSetEvaluationService) {
      this.optimalDSetEvaluationService = optimalDSetEvaluationService;
   }

   public OptimalDSetHelper getOptimalDSetHelper () {
      return optimalDSetHelper;
   }

   public void setOptimalDSetHelper (OptimalDSetHelper optimalDSetHelper) {
      this.optimalDSetHelper = optimalDSetHelper;
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

   /**
    * @return the mappingRetrievalService
    */
   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   /**
    * @param mappingRetrievalService
    *           the mappingRetrievalService to set
    */
   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   /**
    * @return the aggregationConfiguration
    */
   public ExecueConfiguration getAggregationConfiguration () {
      return aggregationConfiguration;
   }

   /**
    * @param aggregationConfiguration
    *           the aggregationConfiguration to set
    */
   public void setAggregationConfiguration (ExecueConfiguration aggregationConfiguration) {
      this.aggregationConfiguration = aggregationConfiguration;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}
