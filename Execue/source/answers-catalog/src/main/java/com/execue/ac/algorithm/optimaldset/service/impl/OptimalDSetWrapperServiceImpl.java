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


/**
 * 
 */
package com.execue.ac.algorithm.optimaldset.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCubeOutputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetMartOutputInfo;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetConsumptionService;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetInvocationService;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetWrapperService;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.helper.CubeCreationServiceHelper;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.optimaldset.OptimalDSetContext;
import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.OperationRequestLevel;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.service.IAnswersCatalogManagementQueueService;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * @author Nitesh
 */
public class OptimalDSetWrapperServiceImpl implements IOptimalDSetWrapperService {

   private static final Logger                   logger = Logger.getLogger(OptimalDSetWrapperServiceImpl.class);

   private IOptimalDSetInvocationService         optimalDSetInvocationService;
   private IOptimalDSetConsumptionService        optimalDSetConsumptionService;
   private ISDXRetrievalService                  sdxRetrievalService;
   private IApplicationRetrievalService          applicationRetrievalService;
   private IKDXRetrievalService                  kdxRetrievalService;
   private IAnswersCatalogManagementQueueService answersCatalogManagementQueueService;
   private IJobDataService                       jobDataService;
   private CubeCreationServiceHelper             cubeCreationServiceHelper;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ac.algorithm.optimaldset.service.IOptimalDSetWrapperService#executeOptimalDSet(com.execue.core.common.bean.optimaldset.OptimalDSetContext)
    */
   @Override
   public void executeOptimalDSet (OptimalDSetContext optimalDSetContext) throws AnswersCatalogException {
      try {
         executeOptimalDSetForCubes(optimalDSetContext);
         executeOptimalDSetForMart(optimalDSetContext);
      } catch (SDXException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      } catch (KDXException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      } catch (AnswersCatalogManagementQueueException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      }
   }

   private void executeOptimalDSetForCubes (OptimalDSetContext optimalDSetContext) throws AnswersCatalogException,
            KDXException, SDXException, AnswersCatalogManagementQueueException {
      OperationRequestLevel operationRequestLevel = optimalDSetContext.getOperationRequestLevel();
      JobRequest jobRequest = optimalDSetContext.getJobRequest();
      JobOperationalStatus topLevelJobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(
               jobRequest, "Initiating " + operationRequestLevel + " level Optmal D-set request for Cubes.");

      if (operationRequestLevel == OperationRequestLevel.SYSTEM) {
         if (logger.isDebugEnabled()) {
            logger.debug("Initiating SYSTEM level Optmal D-set request.");
         }
         List<Application> applications = getApplicationRetrievalService().getAllApplications();
         for (Application application : applications) {
            //create JobOperationalStatus
            JobOperationalStatus jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(
                     jobRequest, "Processing Optmal D-set for Application " + application.getName());
            if (logger.isDebugEnabled()) {
               logger.debug("Processing Optmal D-set for Application " + application.getName());
            }
            Long applicationId = application.getId();
            List<Model> modles = getKdxRetrievalService().getModelsByApplicationId(applicationId);
            Model model = modles.get(0);
            List<Asset> assets = getSdxRetrievalService().getAllAssets(applicationId);

            for (Asset asset : assets) {
               if (logger.isDebugEnabled()) {
                  logger.debug("Processing Optmal D-set for Asset ");
               }
               OptimalDSetContext cloneOptimalDSetContext = ExecueBeanCloneUtil
                        .cloneOptimalDSetContext(optimalDSetContext);
               cloneOptimalDSetContext.setModelId(model.getId());
               cloneOptimalDSetContext.setId(asset.getId());
               cloneOptimalDSetContext.setOperationRequestLevel(OperationRequestLevel.ASSET);
               executeOptimalDSetAtAssetLevelForCubes(cloneOptimalDSetContext);
            }
            getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
         }
      } else if (operationRequestLevel == OperationRequestLevel.APPLICATION) {
         if (logger.isDebugEnabled()) {
            logger.debug("Initiating Application level Optmal D-set request.");
         }
         Long applicationId = optimalDSetContext.getId();
         Application application = getApplicationRetrievalService().getApplicationById(applicationId);
         JobOperationalStatus jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(
                  jobRequest, "Processing Optmal D-set for Application" + application.getName());
         List<Asset> assets = getSdxRetrievalService().getAllAssets(applicationId);
         for (Asset asset : assets) {
            OptimalDSetContext cloneOptimalDSetContext = ExecueBeanCloneUtil
                     .cloneOptimalDSetContext(optimalDSetContext);
            cloneOptimalDSetContext.setId(asset.getId());
            cloneOptimalDSetContext.setOperationRequestLevel(OperationRequestLevel.ASSET);
            executeOptimalDSetAtAssetLevelForCubes(cloneOptimalDSetContext);
         }
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
      } else if (operationRequestLevel == OperationRequestLevel.ASSET) {
         if (logger.isDebugEnabled()) {
            logger.debug("Initiating Asset level Optmal D-set request.");
         }
         executeOptimalDSetAtAssetLevelForCubes(optimalDSetContext);
      }
      getCubeCreationServiceHelper().updateJobOperationalStatus(topLevelJobOperationalStatus, jobRequest);

   }

   private void executeOptimalDSetForMart (OptimalDSetContext optimalDSetContext) throws AnswersCatalogException,
            SDXException, KDXException, AnswersCatalogManagementQueueException {
      OperationRequestLevel operationRequestLevel = optimalDSetContext.getOperationRequestLevel();
      JobRequest jobRequest = optimalDSetContext.getJobRequest();
      JobOperationalStatus topLevelJobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(
               jobRequest, "Initiating " + operationRequestLevel + " level Optmal D-set request for Mart.");
      if (operationRequestLevel == OperationRequestLevel.SYSTEM) {
         if (logger.isDebugEnabled()) {
            logger.debug("Initiating SYSTEM level Optmal D-set request.");
         }
         List<Application> applications = getApplicationRetrievalService().getAllApplications();
         for (Application application : applications) {
            JobOperationalStatus jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(
                     jobRequest, "Processing Optmal D-set for Application " + application.getName());
            if (logger.isDebugEnabled()) {
               logger.debug("Processing Optmal D-set for Application " + application.getName());
            }
            Long applicationId = application.getId();
            List<Model> modles = getKdxRetrievalService().getModelsByApplicationId(applicationId);
            Model model = modles.get(0);
            List<Asset> assets = getSdxRetrievalService().getAllAssets(applicationId);
            for (Asset asset : assets) {
               OptimalDSetContext cloneOptimalDSetContext = ExecueBeanCloneUtil
                        .cloneOptimalDSetContext(optimalDSetContext);
               cloneOptimalDSetContext.setModelId(model.getId());
               cloneOptimalDSetContext.setId(asset.getId());
               cloneOptimalDSetContext.setOperationRequestLevel(OperationRequestLevel.ASSET);
               executeOptimalDSetAtAssetLevelForMarts(cloneOptimalDSetContext);
            }
            getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
         }
      } else if (operationRequestLevel == OperationRequestLevel.APPLICATION) {
         if (logger.isDebugEnabled()) {
            logger.debug("Initiating Application level Optmal D-set request.");
         }
         Long applicationId = optimalDSetContext.getId();
         Application application = getApplicationRetrievalService().getApplicationById(applicationId);
         JobOperationalStatus jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(
                  jobRequest, "Processing Optmal D-set for Application" + application.getName());
         List<Asset> assets = getSdxRetrievalService().getAllAssets(applicationId);
         for (Asset asset : assets) {
            OptimalDSetContext cloneOptimalDSetContext = ExecueBeanCloneUtil
                     .cloneOptimalDSetContext(optimalDSetContext);
            cloneOptimalDSetContext.setId(asset.getId());
            cloneOptimalDSetContext.setOperationRequestLevel(OperationRequestLevel.ASSET);
            executeOptimalDSetAtAssetLevelForMarts(cloneOptimalDSetContext);
         }
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
      } else if (operationRequestLevel == OperationRequestLevel.ASSET) {
         if (logger.isDebugEnabled()) {
            logger.debug("Initiating Asset level Optmal D-set request.");
         }
         executeOptimalDSetAtAssetLevelForMarts(optimalDSetContext);
      }
      getCubeCreationServiceHelper().updateJobOperationalStatus(topLevelJobOperationalStatus, jobRequest);
   }

   private void executeOptimalDSetAtAssetLevelForCubes (OptimalDSetContext optimalDSetContext)
            throws AnswersCatalogException, AnswersCatalogManagementQueueException, SDXException {
      Long parentAssetId = optimalDSetContext.getId();
      Asset asset = getSdxRetrievalService().getAssetById(parentAssetId);
      JobRequest jobRequest = optimalDSetContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
               "Processing Optmal D-set for Cube " + asset.getDisplayName());
      if (getAnswersCatalogManagementQueueService().isAnswersCatalogManagementQueueUnderProgressForSourceType(
               parentAssetId, ACManagementOperationSourceType.OPTIMAL_DSET)) {
         getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                  "Optmal D-set is already in process for Cube " + asset.getDisplayName());
         return;
      }
      Long modelId = optimalDSetContext.getModelId();
      OptimalDSetCubeOutputInfo cubeOptimalDsetOutput = getOptimalDSetInvocationService().generateCubeOptimalDset(
               parentAssetId, modelId);

      // TODO:NK: Need to create the input bean
      if (cubeOptimalDsetOutput != null) {
         getOptimalDSetConsumptionService().processOptimalDSetCubeConsumption(cubeOptimalDsetOutput, parentAssetId,
                  modelId);
      }

      getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

   }

   private void executeOptimalDSetAtAssetLevelForMarts (OptimalDSetContext optimalDSetContext)
            throws AnswersCatalogException, AnswersCatalogManagementQueueException, SDXException {
      Long parentAssetId = optimalDSetContext.getId();
      Asset asset = getSdxRetrievalService().getAssetById(parentAssetId);
      JobRequest jobRequest = optimalDSetContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
               "Processing Optmal D-set for Mart " + asset.getDisplayName());
      if (getAnswersCatalogManagementQueueService().isAnswersCatalogManagementQueueUnderProgressForSourceType(
               parentAssetId, ACManagementOperationSourceType.OPTIMAL_DSET)) {
         getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                  "Optmal D-set is already in process for Mart " + asset.getDisplayName());
         return;
      }
      Long modelId = optimalDSetContext.getModelId();
      OptimalDSetMartOutputInfo martOptimalDsetOutput = getOptimalDSetInvocationService().generateMartOptimalDset(
               parentAssetId, modelId);

      getOptimalDSetConsumptionService().processOptimalDSetMartConsumption(martOptimalDsetOutput, parentAssetId,
               modelId);
      getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
   }

   /**
    * @return the optimalDSetInvocationService
    */
   public IOptimalDSetInvocationService getOptimalDSetInvocationService () {
      return optimalDSetInvocationService;
   }

   /**
    * @param optimalDSetInvocationService
    *           the optimalDSetInvocationService to set
    */
   public void setOptimalDSetInvocationService (IOptimalDSetInvocationService optimalDSetInvocationService) {
      this.optimalDSetInvocationService = optimalDSetInvocationService;
   }

   /**
    * @return the optimalDSetConsumptionService
    */
   public IOptimalDSetConsumptionService getOptimalDSetConsumptionService () {
      return optimalDSetConsumptionService;
   }

   /**
    * @param optimalDSetConsumptionService
    *           the optimalDSetConsumptionService to set
    */
   public void setOptimalDSetConsumptionService (IOptimalDSetConsumptionService optimalDSetConsumptionService) {
      this.optimalDSetConsumptionService = optimalDSetConsumptionService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the answersCatalogManagementQueueService
    */
   public IAnswersCatalogManagementQueueService getAnswersCatalogManagementQueueService () {
      return answersCatalogManagementQueueService;
   }

   /**
    * @param answersCatalogManagementQueueService the answersCatalogManagementQueueService to set
    */
   public void setAnswersCatalogManagementQueueService (
            IAnswersCatalogManagementQueueService answersCatalogManagementQueueService) {
      this.answersCatalogManagementQueueService = answersCatalogManagementQueueService;
   }

   /**
    * @return the jobDataService
    */
   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   /**
    * @param jobDataService the jobDataService to set
    */
   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   /**
    * @return the cubeCreationServiceHelper
    */
   public CubeCreationServiceHelper getCubeCreationServiceHelper () {
      return cubeCreationServiceHelper;
   }

   /**
    * @param cubeCreationServiceHelper the cubeCreationServiceHelper to set
    */
   public void setCubeCreationServiceHelper (CubeCreationServiceHelper cubeCreationServiceHelper) {
      this.cubeCreationServiceHelper = cubeCreationServiceHelper;
   }
}
