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

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.core.common.bean.ConceptTypeAssociationContext;
import com.execue.core.common.bean.ontology.OntologyAbsorptionContext;
import com.execue.core.common.bean.optimaldset.OptimalDSetContext;
import com.execue.core.common.bean.swi.CorrectMappingsMaintenanceContext;
import com.execue.core.common.bean.swi.IndexFormManagementContext;
import com.execue.core.common.bean.swi.PopularityHitMaintenanceContext;
import com.execue.core.common.bean.swi.PopularityRestorationContext;
import com.execue.core.common.bean.swi.SDXSynchronizationContext;
import com.execue.core.common.bean.swi.SFLTermTokenWeightContext;
import com.execue.core.common.type.OperationRequestLevel;
import com.execue.core.common.type.SyncRequestLevel;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.handler.swi.IJobMaintenanceServiceHandler;
import com.execue.ontology.exceptions.OntologyException;
import com.execue.publisher.configuration.IPublisherConfigurationService;
import com.execue.scheduler.service.IBusinessModelPreparationJobService;
import com.execue.scheduler.service.IConceptTypeAssociationJobService;
import com.execue.scheduler.service.ICorrectMappingsMaintenanceJobService;
import com.execue.scheduler.service.IFileOntologyDataAbsorptionJobService;
import com.execue.scheduler.service.IIndexFormManagementJobService;
import com.execue.scheduler.service.IOptimalDSetJobService;
import com.execue.scheduler.service.IPopularityCollectionJobService;
import com.execue.scheduler.service.IPopularityDispersionJobService;
import com.execue.scheduler.service.IPopularityHitMaintainenceJobService;
import com.execue.scheduler.service.IPublishAssetJobService;
import com.execue.scheduler.service.IRIOntotermPopularityHitMaintainenceJobService;
import com.execue.scheduler.service.ISDXSynchronizationJobService;
import com.execue.scheduler.service.IUpdateSFLTermTokenWeightJobService;
import com.execue.scheduler.service.IUpdateSFLWeightBasedOnSecondaryWordJobService;
import com.execue.security.UserContextService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;

public class JobMaintenanceServiceHandlerImpl extends UserContextService implements IJobMaintenanceServiceHandler {

   private static Logger                                  logger = Logger
                                                                          .getLogger(JobMaintenanceServiceHandlerImpl.class);
   private OntologyAbsorptionContext                      ontologyAbsorptionContext;
   private IPublisherConfigurationService                 publisherConfigurationService;
   private IFileOntologyDataAbsorptionJobService          fileOntologyDataAbsorptionJobService;
   private ICorrectMappingsMaintenanceJobService          correctMappingsMaintenanceJobService;
   private IPopularityHitMaintainenceJobService           popularityHitMaintainenceJobService;
   private IPopularityCollectionJobService                popularityCollectionJobService;
   private IPopularityDispersionJobService                popularityDispersionJobService;
   private IUpdateSFLTermTokenWeightJobService            updateSFLTermTokenWeightJobService;
   private IUpdateSFLWeightBasedOnSecondaryWordJobService updateSFLWeightBasedOnSecondaryWordJobService;
   private IRIOntotermPopularityHitMaintainenceJobService riOntotermPopularityHitMaintainenceJobService;
   private IBusinessModelPreparationJobService            businessModelPreparationJobService;
   private IPublishAssetJobService                        publishAssetJobService;
   private IIndexFormManagementJobService                 indexFormManagementJobService;
   private IConceptTypeAssociationJobService              conceptTypeAssociationJobService;
   private ISDXSynchronizationJobService                  sdxSynchronizationJobService;
   private IOptimalDSetJobService                         optimalDSetJobService;

   public IBusinessModelPreparationJobService getBusinessModelPreparationJobService () {
      return businessModelPreparationJobService;
   }

   public void setBusinessModelPreparationJobService (
            IBusinessModelPreparationJobService businessModelPreparationJobService) {
      this.businessModelPreparationJobService = businessModelPreparationJobService;
   }

   public IUpdateSFLTermTokenWeightJobService getUpdateSFLTermTokenWeightJobService () {
      return updateSFLTermTokenWeightJobService;
   }

   public void setUpdateSFLTermTokenWeightJobService (
            IUpdateSFLTermTokenWeightJobService updateSFLTermTokenWeightJobService) {
      this.updateSFLTermTokenWeightJobService = updateSFLTermTokenWeightJobService;
   }

   public Long absorbFileOntology (String filePath, boolean generateRIOntoTerms, boolean generateSFLTerms,
            Long modelId, Long cloudId) throws HandlerException {
      OntologyAbsorptionContext ontologyAbsorptionContext = new OntologyAbsorptionContext();
      ontologyAbsorptionContext.setModelId(modelId);
      ontologyAbsorptionContext.setCloudId(cloudId);
      ontologyAbsorptionContext.setFilePath(filePath);
      ontologyAbsorptionContext.setGenerateRIOntoterms(generateRIOntoTerms);
      ontologyAbsorptionContext.setGenerateSFLTerms(generateSFLTerms);
      Long userId = getUserContext().getUser().getId();
      ontologyAbsorptionContext.setUserId(userId);
      Long jobRequestId = null;
      try {
         jobRequestId = fileOntologyDataAbsorptionJobService
                  .scheduleFileOntologyDataAbsorbtion(ontologyAbsorptionContext);

      } catch (OntologyException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return jobRequestId;
   }

   public Long associateConceptWithType (String filePath, Long modelId) throws HandlerException {
      ConceptTypeAssociationContext conceptTypeAssociationContext = new ConceptTypeAssociationContext();
      conceptTypeAssociationContext.setModelId(modelId);
      conceptTypeAssociationContext.setFilePath(filePath);
      Long userId = getUserContext().getUser().getId();
      conceptTypeAssociationContext.setUserId(userId);
      Long jobRequestId = null;
      try {
         jobRequestId = conceptTypeAssociationJobService
                  .scheduleConceptTypeAssociationJob(conceptTypeAssociationContext);
      } catch (KDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return jobRequestId;
   }

   public String getOWLFileStoragePath (String sourceType) throws HandlerException {
      return getPublisherConfigurationService().getOntologyFileStoragePath(sourceType);
   }

   public String getOtherFileStoragePath (String sourceType) throws HandlerException {
      return getPublisherConfigurationService().getPublisherFileStoragePath(sourceType);

   }

   public Long scheduleCorrectMappingMaintenanceJob (Long applicationId, List<Long> assetIds) throws HandlerException {
      CorrectMappingsMaintenanceContext correctMappingsMaintenanceContext = new CorrectMappingsMaintenanceContext();
      correctMappingsMaintenanceContext.setApplicationId(applicationId);
      correctMappingsMaintenanceContext.setAssetIds(assetIds);
      correctMappingsMaintenanceContext.setUserId(getUserContext().getUser().getId());

      Long jobRequestId = null;
      try {
         jobRequestId = getCorrectMappingsMaintenanceJobService().scheduleCorrectMappingsMaintenanceJob(
                  correctMappingsMaintenanceContext);
      } catch (MappingException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return jobRequestId;
   }

   public Long schedulePopularityHitMaintenanceJob () throws HandlerException {
      Long jobRequestId = null;
      try {
         PopularityHitMaintenanceContext popularityHitMaintenanceContext = new PopularityHitMaintenanceContext();
         popularityHitMaintenanceContext.setUserId(getUserContext().getUser().getId());
         jobRequestId = popularityHitMaintainenceJobService
                  .scheduleUpdateTermsBasedOnPopularityJob(popularityHitMaintenanceContext);
      } catch (KDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return jobRequestId;
   }

   @Override
   public Long scheduleSDXSynchronizationeJob (Long id, Long applicationId, Long modelId,
            SyncRequestLevel syncRequestLevel, OperationRequestLevel operationRequestLevel) throws HandlerException {
      Long jobRequestId = null;
      try {

         SDXSynchronizationContext sdxSynchronizationContext = new SDXSynchronizationContext();
         sdxSynchronizationContext.setUserId(getUserContext().getUser().getId());
         sdxSynchronizationContext.setApplicationId(applicationId);
         sdxSynchronizationContext.setModelId(modelId);
         sdxSynchronizationContext.setId(id);
         sdxSynchronizationContext.setSyncRequestLevel(syncRequestLevel);
         sdxSynchronizationContext.setRequestLevel(operationRequestLevel);

         jobRequestId = sdxSynchronizationJobService.scheduleSDXSynchronizationJob(sdxSynchronizationContext);
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return jobRequestId;
   }

   @Override
   public Long scheduleOptimalDSetJob (Long id, Long modelId, OperationRequestLevel operationRequestLevel)
            throws HandlerException {
      Long jobRequestId = null;
      try {
         OptimalDSetContext optimalDSetContext = new OptimalDSetContext();
         optimalDSetContext.setUserId(getUserContext().getUser().getId());
         optimalDSetContext.setModelId(modelId);
         optimalDSetContext.setId(id);
         optimalDSetContext.setOperationRequestLevel(operationRequestLevel);
         jobRequestId = getOptimalDSetJobService().scheduleOptimalDSetJob(optimalDSetContext);
      } catch (AnswersCatalogException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return jobRequestId;
   }

   public Long scheduleRIOntoTermPopularityHitMaintenanceJob (Long modelId) throws HandlerException {
      Long jobRequestId = null;
      try {
         PopularityHitMaintenanceContext popularityHitMaintenanceContext = new PopularityHitMaintenanceContext();
         popularityHitMaintenanceContext.setUserId(getUserContext().getUser().getId());
         popularityHitMaintenanceContext.setModelId(modelId);
         jobRequestId = getRiOntotermPopularityHitMaintainenceJobService()
                  .scheduleRIOntoTermPopularityHitMaintainenceJob(popularityHitMaintenanceContext);
      } catch (KDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return jobRequestId;
   }

   public Long schedulePopularityCollectionMaintenanceJob (Long applicationId, Long modelId) throws HandlerException {
      Long jobRequestId = null;
      try {
         PopularityRestorationContext popularityRestorationContext = new PopularityRestorationContext();
         popularityRestorationContext.setUserId(getUserContext().getUser().getId());
         popularityRestorationContext.setApplicationId(applicationId);
         popularityRestorationContext.setModelId(modelId);
         jobRequestId = getPopularityCollectionJobService().schedulePopularityCollectionJob(
                  popularityRestorationContext);
      } catch (SWIException swiException) {
         logger.error(swiException);
         throw new HandlerException(swiException.getCode(), swiException);
      }
      return jobRequestId;
   }

   public Long schedulePopularityDispersionMaintenanceJob (Long applicationId, Long modelId) throws HandlerException {
      Long jobRequestId = null;
      try {
         PopularityRestorationContext popularityRestorationContext = new PopularityRestorationContext();
         popularityRestorationContext.setUserId(getUserContext().getUser().getId());
         popularityRestorationContext.setApplicationId(applicationId);
         popularityRestorationContext.setModelId(modelId);
         jobRequestId = getPopularityDispersionJobService().schedulePopularityDispersionJob(
                  popularityRestorationContext);
      } catch (SWIException swiException) {
         logger.error(swiException);
         throw new HandlerException(swiException.getCode(), swiException);
      }
      return jobRequestId;
   }

   public Long scheduleParallelWordMaintenanceJob () throws HandlerException {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * @return the ontologyAbsorptionContext
    */
   public OntologyAbsorptionContext getOntologyAbsorptionContext () {
      return ontologyAbsorptionContext;
   }

   /**
    * @param ontologyAbsorptionContext
    *           the ontologyAbsorptionContext to set
    */
   public void setOntologyAbsorptionContext (OntologyAbsorptionContext ontologyAbsorptionContext) {
      this.ontologyAbsorptionContext = ontologyAbsorptionContext;
   }

   /**
    * @return the fileOntologyDataAbsorptionJobService
    */
   public IFileOntologyDataAbsorptionJobService getFileOntologyDataAbsorptionJobService () {
      return fileOntologyDataAbsorptionJobService;
   }

   /**
    * @param fileOntologyDataAbsorptionJobService
    *           the fileOntologyDataAbsorptionJobService to set
    */
   public void setFileOntologyDataAbsorptionJobService (
            IFileOntologyDataAbsorptionJobService fileOntologyDataAbsorptionJobService) {
      this.fileOntologyDataAbsorptionJobService = fileOntologyDataAbsorptionJobService;
   }

   /**
    * @return the publisherConfigurationService
    */
   public IPublisherConfigurationService getPublisherConfigurationService () {
      return publisherConfigurationService;
   }

   /**
    * @return the popularityHitMaintainenceJobService
    */
   public IPopularityHitMaintainenceJobService getPopularityHitMaintainenceJobService () {
      return popularityHitMaintainenceJobService;
   }

   /**
    * @param popularityHitMaintainenceJobService
    *           the popularityHitMaintainenceJobService to set
    */
   public void setPopularityHitMaintainenceJobService (
            IPopularityHitMaintainenceJobService popularityHitMaintainenceJobService) {
      this.popularityHitMaintainenceJobService = popularityHitMaintainenceJobService;
   }

   public void setPublisherConfigurationService (IPublisherConfigurationService publisherConfigurationService) {
      this.publisherConfigurationService = publisherConfigurationService;
   }

   /**
    * @return the publishAssetJobService
    */
   public IPublishAssetJobService getPublishAssetJobService () {
      return publishAssetJobService;
   }

   /**
    * @param publishAssetJobService
    *           the publishAssetJobService to set
    */
   public void setPublishAssetJobService (IPublishAssetJobService publishAssetJobService) {
      this.publishAssetJobService = publishAssetJobService;
   }

   public Long scheduleSflTermTokenWeightMaintenaceJob () throws HandlerException {
      Long jobRequestId = null;
      try {
         SFLTermTokenWeightContext sflTermTokenWeightContext = new SFLTermTokenWeightContext();
         // the SFL term token weight adjustment is model ignorant
         sflTermTokenWeightContext.setModelId(new Long(-1));
         sflTermTokenWeightContext.setUserId(getUserContext().getUser().getId());
         jobRequestId = updateSFLTermTokenWeightJobService
                  .scheduleUpdateSFLTermTokenWeightJob(sflTermTokenWeightContext);
      } catch (SWIException swiException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, swiException.getMessage());
      }
      return jobRequestId;
   }

   public Long scheduleIndexFormManagementJob (Long applicationId, Long modelId) throws HandlerException {
      Long jobRequestId = null;
      try {
         IndexFormManagementContext indexFormManagementContext = new IndexFormManagementContext();
         indexFormManagementContext.setApplicationId(applicationId);
         indexFormManagementContext.setModelId(modelId);
         indexFormManagementContext.setUserId(getUserContext().getUser().getId());
         jobRequestId = getIndexFormManagementJobService().scheduleIndexFormManagementJob(indexFormManagementContext);
      } catch (SWIException swiException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, swiException.getMessage());
      }
      return jobRequestId;
   }

   /**
    * @return the indexFormManagementJobService
    */
   public IIndexFormManagementJobService getIndexFormManagementJobService () {
      return indexFormManagementJobService;
   }

   /**
    * @param indexFormManagementJobService
    *           the indexFormManagementJobService to set
    */
   public void setIndexFormManagementJobService (IIndexFormManagementJobService indexFormManagementJobService) {
      this.indexFormManagementJobService = indexFormManagementJobService;
   }

   public Long scheduleSflWeightUpdationBySecondaryWordMaintenaceJob (Long modelId) throws HandlerException {
      Long jobRequestId = null;
      try {
         SFLTermTokenWeightContext sflTermTokenWeightContext = new SFLTermTokenWeightContext();
         sflTermTokenWeightContext.setModelId(modelId);
         sflTermTokenWeightContext.setUserId(getUserContext().getUser().getId());
         jobRequestId = updateSFLWeightBasedOnSecondaryWordJobService
                  .scheduleUpdateSFLTermTokenWeightBasedOnSecondaryWordJob(sflTermTokenWeightContext);

      } catch (SWIException swiException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, swiException.getMessage());
      }
      return jobRequestId;
   }

   public IUpdateSFLWeightBasedOnSecondaryWordJobService getUpdateSFLWeightBasedOnSecondaryWordJobService () {
      return updateSFLWeightBasedOnSecondaryWordJobService;
   }

   public void setUpdateSFLWeightBasedOnSecondaryWordJobService (
            IUpdateSFLWeightBasedOnSecondaryWordJobService updateSFLWeightBasedOnSecondaryWordJobService) {
      this.updateSFLWeightBasedOnSecondaryWordJobService = updateSFLWeightBasedOnSecondaryWordJobService;
   }

   public IConceptTypeAssociationJobService getConceptTypeAssociationJobService () {
      return conceptTypeAssociationJobService;
   }

   public void setConceptTypeAssociationJobService (IConceptTypeAssociationJobService conceptTypeAssociationJobService) {
      this.conceptTypeAssociationJobService = conceptTypeAssociationJobService;
   }

   public ICorrectMappingsMaintenanceJobService getCorrectMappingsMaintenanceJobService () {
      return correctMappingsMaintenanceJobService;
   }

   public void setCorrectMappingsMaintenanceJobService (
            ICorrectMappingsMaintenanceJobService correctMappingsMaintenanceJobService) {
      this.correctMappingsMaintenanceJobService = correctMappingsMaintenanceJobService;
   }

   public IPopularityCollectionJobService getPopularityCollectionJobService () {
      return popularityCollectionJobService;
   }

   public void setPopularityCollectionJobService (IPopularityCollectionJobService popularityCollectionJobService) {
      this.popularityCollectionJobService = popularityCollectionJobService;
   }

   public IPopularityDispersionJobService getPopularityDispersionJobService () {
      return popularityDispersionJobService;
   }

   public void setPopularityDispersionJobService (IPopularityDispersionJobService popularityDispersionJobService) {
      this.popularityDispersionJobService = popularityDispersionJobService;
   }

   public IRIOntotermPopularityHitMaintainenceJobService getRiOntotermPopularityHitMaintainenceJobService () {
      return riOntotermPopularityHitMaintainenceJobService;
   }

   public void setRiOntotermPopularityHitMaintainenceJobService (
            IRIOntotermPopularityHitMaintainenceJobService riOntotermPopularityHitMaintainenceJobService) {
      this.riOntotermPopularityHitMaintainenceJobService = riOntotermPopularityHitMaintainenceJobService;
   }

   public ISDXSynchronizationJobService getSdxSynchronizationJobService () {
      return sdxSynchronizationJobService;
   }

   public void setSdxSynchronizationJobService (ISDXSynchronizationJobService sdxSynchronizationJobService) {
      this.sdxSynchronizationJobService = sdxSynchronizationJobService;
   }

   /**
    * @return the optimalDSetJobService
    */
   public IOptimalDSetJobService getOptimalDSetJobService () {
      return optimalDSetJobService;
   }

   /**
    * @param optimalDSetJobService the optimalDSetJobService to set
    */
   public void setOptimalDSetJobService (IOptimalDSetJobService optimalDSetJobService) {
      this.optimalDSetJobService = optimalDSetJobService;
   }

}
