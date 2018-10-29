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


package com.execue.platform.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.execue.core.common.bean.TypeConceptEvaluationInfo;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.swi.BusinessModelPreparationContext;
import com.execue.core.common.bean.swi.EASIndexPeriodicContext;
import com.execue.core.common.bean.swi.IndexFormManagementContext;
import com.execue.core.common.bean.swi.UnstructuredWarehouseContext;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.IndicatorBehaviorType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.type.PathSelectionType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.IBusinessModelPreparationService;
import com.execue.platform.IModelAbsorbtionService;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.swi.IAssetActivationService;
import com.execue.platform.swi.IBusinessEntityDeletionWrapperService;
import com.execue.platform.swi.IConceptTypeEvaluationService;
import com.execue.platform.swi.IDefaultDynamicValuePopulationService;
import com.execue.platform.swi.IEASIndexPopulationService;
import com.execue.platform.swi.IIndexFormManagementService;
import com.execue.platform.swi.IKDXMaintenanceService;
import com.execue.platform.swi.ISDX2KDXMappingService;
import com.execue.platform.unstructured.IUnstructuredWarehouseManagementWrapperService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.JoinException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDX2KDXMappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IJoinService;
import com.execue.swi.service.IKDXCloudManagementService;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.IPathAbsorptionService;
import com.execue.swi.service.IPathDefinitionManagementService;
import com.execue.swi.service.IPathDefinitionRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.swi.service.ISFLService;
import com.hp.hpl.jena.ontology.OntologyException;

/**
 * This service prepares the business model
 * 
 * @author Vishay
 * @version 1.0
 * @since 10/09/09
 */
public class BusinessModelPreparationServiceImpl implements IBusinessModelPreparationService {

   private IJobDataService                                jobDataService;
   private IModelAbsorbtionService                        modelAbsorbtionService;
   private IConceptTypeEvaluationService                  conceptTypeEvaluationService;
   private IKDXRetrievalService                           kdxRetrievalService;
   private IAssetActivationService                        assetActivationService;
   private IIndexFormManagementService                    indexFormManagementService;
   private IPathAbsorptionService                         pathAbsorptionService;
   private IKDXCloudRetrievalService                      kdxCloudRetrievalService;
   private IKDXCloudManagementService                     kdxCloudManagementService;
   private IKDXManagementService                          kdxManagementService;
   private IKDXMaintenanceService                         kdxMaintenanceService;
   private ISDX2KDXMappingService                         sdx2kdxMappingService;
   private IMappingRetrievalService                       mappingRetrievalService;
   private ISDXRetrievalService                           sdxRetrievalService;
   private IKDXModelService                               kdxModelService;
   private ISFLService                                    sflService;
   private IBusinessEntityMaintenanceService              businessEntityMaintenanceService;
   private IEASIndexPopulationService                     easIndexPopulationService;
   private IJoinService                                   joinService;
   private IApplicationRetrievalService                   applicationRetrievalService;
   private IPathDefinitionRetrievalService                pathDefinitionRetrievalService;
   private IPathDefinitionManagementService               pathDefinitionManagementService;
   private IUnstructuredWarehouseManagementWrapperService unstructuredWarehouseManagementWrapperService;
   private IDefaultDynamicValuePopulationService          defaultDynamicValuePopulationService;
   private IBusinessEntityDeletionWrapperService          businessEntityDeletionWrapperService;

   /**
    * @return the pathDefinitionRetrievalService
    */
   public IPathDefinitionRetrievalService getPathDefinitionRetrievalService () {
      return pathDefinitionRetrievalService;
   }

   /**
    * @param pathDefinitionRetrievalService
    *           the pathDefinitionRetrievalService to set
    */
   public void setPathDefinitionRetrievalService (IPathDefinitionRetrievalService pathDefinitionRetrievalService) {
      this.pathDefinitionRetrievalService = pathDefinitionRetrievalService;
   }

   /**
    * @return the pathDefinitionManagementService
    */
   public IPathDefinitionManagementService getPathDefinitionManagementService () {
      return pathDefinitionManagementService;
   }

   /**
    * @param pathDefinitionManagementService
    *           the pathDefinitionManagementService to set
    */
   public void setPathDefinitionManagementService (IPathDefinitionManagementService pathDefinitionManagementService) {
      this.pathDefinitionManagementService = pathDefinitionManagementService;
   }

   public boolean prepareBusinessModel (BusinessModelPreparationContext businessModelPreparationContext)
            throws KDXException {
      boolean status = true;
      String errorMessage = "";
      JobOperationalStatus jobOperationalStatus = null;
      JobRequest jobRequest = businessModelPreparationContext.getJobRequest();
      try {
         Long modelId = businessModelPreparationContext.getModelId();
         Long applicationId = businessModelPreparationContext.getApplicationId();
         Application application = getApplicationRetrievalService().getApplicationById(applicationId);

         // Check if the Model Evaluation is needed
         boolean isModelEvaluationRequired = getKdxModelService().isModelEvaluationRequired(modelId);
         boolean isModelEvaluationRequiredByAppSourceType = getApplicationRetrievalService()
                  .isModelEvaluationRequiredByAppSourceType(applicationId);

         status = manageBusinessModel(businessModelPreparationContext, status, jobOperationalStatus,
                  isModelEvaluationRequired, isModelEvaluationRequiredByAppSourceType);

         // Creating Warehouse entries if application is unstructured
         if (application.getSourceType() == AppSourceType.UNSTRUCTURED) {
            manageUnStructuredWarehouse(jobRequest, applicationId, modelId, jobOperationalStatus);
         }

         // deleting the records for this model from business entity maintenance table
         cleanupBusinessEntityMaintenanceDetails(jobRequest, modelId, jobOperationalStatus);
      } catch (QueryDataException queryDataException) {
         status = false;
         errorMessage = queryDataException.getMessage();
         throw new KDXException(queryDataException.getCode(), queryDataException);
      } catch (KDXException kdxException) {
         status = false;
         errorMessage = kdxException.getMessage();
         throw kdxException;
      } catch (SWIException swiException) {
         status = false;
         errorMessage = swiException.getMessage();
         throw new KDXException(swiException.getCode(), swiException);
      } catch (PlatformException e) {
         status = false;
         errorMessage = e.getMessage();
         throw new KDXException(e.getCode(), e);
      } finally {
         if (!status && jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, errorMessage,
                     new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException queryDataException) {
               throw new KDXException(queryDataException.getCode(), queryDataException);
            }
         }
      }
      return status;
   }

   /**
    * @param jobRequest
    * @param applicationId
    * @param modelId
    * @return
    * @throws QueryDataException
    * @throws SWIException
    * @throws PlatformException
    */
   private void manageUnStructuredWarehouse (JobRequest jobRequest, Long applicationId, Long modelId,
            JobOperationalStatus jobOperationalStatus) throws QueryDataException, SWIException, PlatformException {
      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               "Creating Unstructured Warehouse entries", JobStatus.INPROGRESS, null, new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);
      UnstructuredWarehouseContext unstructuredWarehouseContext = new UnstructuredWarehouseContext();
      unstructuredWarehouseContext.setModelId(modelId);
      unstructuredWarehouseContext.setApplicationId(applicationId);
      getUnstructuredWarehouseManagementWrapperService().manageUnstructuredWarehouse(unstructuredWarehouseContext);
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);
   }

   /**
    * @param businessModelPreparationContext
    * @param status
    * @param jobOperationalStatus
    * @param isModelEvaluationRequired
    * @param isModelEvaluationRequiredByAppSourceType
    * @return
    * @throws KDXException
    * @throws PlatformException 
    */
   protected boolean manageBusinessModel (BusinessModelPreparationContext businessModelPreparationContext,
            boolean status, JobOperationalStatus jobOperationalStatus, boolean isModelEvaluationRequired,
            boolean isModelEvaluationRequiredByAppSourceType) throws KDXException, PlatformException {
      if (ExecueCoreUtil.isCollectionNotEmpty(businessModelPreparationContext.getSelectedAssetIds())) {
         for (Long assetId : businessModelPreparationContext.getSelectedAssetIds()) {
            businessModelPreparationContext.setAssetId(assetId);
            if (status) {
               status = prepareBusinessModelForAsset(businessModelPreparationContext, jobOperationalStatus,
                        isModelEvaluationRequired, isModelEvaluationRequiredByAppSourceType);
            }
         }
      } else {
         businessModelPreparationContext.setAssetId(businessModelPreparationContext.getAssetId());
         if (status) {
            status = prepareBusinessModelForAsset(businessModelPreparationContext, jobOperationalStatus,
                     isModelEvaluationRequired, isModelEvaluationRequiredByAppSourceType);
         }
      }
      return status;
   }

   private boolean prepareBusinessModelForAsset (BusinessModelPreparationContext businessModelPreparationContext,
            JobOperationalStatus jobOperationalStatus, boolean isModelEvaluationRequired,
            boolean isModelEvaluationRequiredByAppSourceType) throws KDXException, PlatformException {
      boolean status = true;

      JobRequest jobRequest = businessModelPreparationContext.getJobRequest();

      try {

         Long applicationId = businessModelPreparationContext.getApplicationId();
         Long modelId = businessModelPreparationContext.getModelId();
         Long assetId = businessModelPreparationContext.getAssetId();
         Long userId = businessModelPreparationContext.getUserId();

         // Perform Model Evaluation
         if (isModelEvaluationRequired && isModelEvaluationRequiredByAppSourceType) {
            performModelEvaluation(jobRequest, modelId, assetId, jobOperationalStatus);
         }

         // Perform EAS index refresh
         performEASIndexRefresh(jobRequest, applicationId, jobOperationalStatus);

         // manage the base concept in the cloud . a concept from base will be added to cloud only if its been used in
         // this cloud.
         manageConceptsFromBaseInAppCloud(jobRequest, modelId, jobOperationalStatus);

         // handle the concepts behavior. mark the concepts with enumeration behavior which have instances and not
         // marked as enumeration
         manageConceptsForEnumerationBehavior(jobRequest, modelId, jobOperationalStatus);

         // Creating indices
         performIndexFormManagement(jobRequest, modelId, userId, jobOperationalStatus);

         // creating indirect paths
         manageIndirectPaths(jobRequest, modelId, jobOperationalStatus);

         // manage the default paths
         manageDefaultFlagForValuePaths(jobRequest, modelId, jobOperationalStatus);

         // deleting the orphan sfl terms.
         // retrieve the orphan SFLs based on the availability of the corresponding RIOTs
         cleanupOrphanSFLTerms(jobRequest, modelId, jobOperationalStatus);

         if (isModelEvaluationRequiredByAppSourceType) {
            manageIndirectJoins(jobRequest, assetId, jobOperationalStatus);

            activateDataset(businessModelPreparationContext, jobRequest, assetId, jobOperationalStatus);

            manageDefaultDynamicValues(jobRequest, applicationId, jobOperationalStatus);
         }

         // this has to be last step to delete the entities from the system which has been marked for
         // deletion
         cleanModelForBusinessEntities(jobRequest, modelId, jobOperationalStatus);
      } catch (SDXException sdxException) {
         status = false;
         throw new KDXException(sdxException.getCode(), sdxException);
      } catch (QueryDataException e) {
         status = false;
         throw new KDXException(e.getCode(), e);
      } catch (SWIException e) {
         status = false;
         throw new KDXException(e.getCode(), e);
      }
      return status;
   }

   /**
    * Method to mark a value realization path as default path in a model. If there is only one measure is associated
    * with that value realization that path gets marked as default .
    * 
    * @param jobRequest
    * @param modelId
    * @param jobOperationalStatus
    * @throws QueryDataException
    * @throws SWIException
    */
   private void manageDefaultFlagForValuePaths (JobRequest jobRequest, Long modelId,
            JobOperationalStatus jobOperationalStatus) throws QueryDataException, SWIException {
      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               "Marking the default value path", JobStatus.INPROGRESS, null, new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);
      Map<Long, List<PathDefinition>> pathDefByDestId = getPathDefinitionRetrievalService()
               .getDirectValuePathDefinitonByDestId(getKdxCloudRetrievalService().getDefaultAppCloud(modelId).getId(),
                        modelId);
      List<PathDefinition> pathsToSave = new ArrayList<PathDefinition>(1);
      for (Entry<Long, List<PathDefinition>> entry : pathDefByDestId.entrySet()) {
         if (entry.getValue().size() == 1) {
            PathDefinition pathDefinition = entry.getValue().get(0);
            if (pathDefinition.getPathSelectionType() != PathSelectionType.DEFAULT_VALUE_PATH) {
               pathDefinition.setPathSelectionType(PathSelectionType.DEFAULT_VALUE_PATH);
               pathsToSave.add(pathDefinition);
            }
         } else {
            boolean defaultPathAdded = false;
            for (PathDefinition pathDefinition : entry.getValue()) {
               if (pathDefinition.getPathSelectionType() == PathSelectionType.DEFAULT_VALUE_PATH) {
                  defaultPathAdded = true;
                  break;
               }
            }
            if (!defaultPathAdded) {
               // TODO sort the paths by id as of now. In future need to see if these can be sorted by source concept
               // popularity
               PathDefinition pathDefinition = entry.getValue().get(0);
               pathDefinition.setPathSelectionType(PathSelectionType.DEFAULT_VALUE_PATH);
               pathsToSave.add(pathDefinition);
            }
         }
      }
      getPathDefinitionManagementService().updatePathDefinitions(pathsToSave);
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);

   }

   private void cleanModelForBusinessEntities (JobRequest jobRequest, Long modelId,
            JobOperationalStatus jobOperationalStatus) {
      // read from business_entity_maintenance table
      // and call wrapper delete calls from respective services.
      // sequence is imp as instances should be processed before concept
      // always use null check conditions because deletion of concept can remove instances
      // hence instance might or might not exist
   }

   private void manageConceptsForEnumerationBehavior (JobRequest jobRequest, Long modelId,
            JobOperationalStatus jobOperationalStatus) throws KDXException {
      List<Long> conceptBedIdsHavingInstances = getKdxRetrievalService().getConceptBedIdsHavingInstances(modelId);
      if (ExecueCoreUtil.isCollectionNotEmpty(conceptBedIdsHavingInstances)) {
         List<Long> conceptBedsHavingEnumerationBehavior = getKdxRetrievalService().getConceptBedsHavingBehaviorType(
                  conceptBedIdsHavingInstances, BehaviorType.ENUMERATION);
         // if concepts having enumeration list is empty, then we need to mark the entire list as enumeration, else we
         // need to subtract and find the concepts which are instances and not marked as enumeration
         if (ExecueCoreUtil.isCollectionNotEmpty(conceptBedsHavingEnumerationBehavior)) {
            conceptBedIdsHavingInstances.removeAll(conceptBedsHavingEnumerationBehavior);
         }
         // check if there are some concepts which needs to be marked as enumeration
         if (ExecueCoreUtil.isCollectionNotEmpty(conceptBedIdsHavingInstances)) {
            List<BehaviorType> behaviorTypes = new ArrayList<BehaviorType>();
            behaviorTypes.add(BehaviorType.ENUMERATION);
            for (Long conceptBedIdHavingInstances : conceptBedIdsHavingInstances) {
               getKdxManagementService().createEntityBehaviors(conceptBedIdHavingInstances, behaviorTypes);
            }
         }
      }
   }

   /**
    * @param jobRequest
    * @param applicationId
    * @return
    * @throws QueryDataException
    * @throws MappingException
    */
   private void manageDefaultDynamicValues (JobRequest jobRequest, Long applicationId,
            JobOperationalStatus jobOperationalStatus) throws QueryDataException, MappingException {
      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               "Populating the Dynamic Default Values", JobStatus.INPROGRESS, null, new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);
      getDefaultDynamicValuePopulationService().populateDefaultDynamicValues(applicationId);
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);
   }

   /**
    * @param businessModelPreparationContext
    * @param jobRequest
    * @param assetId
    * @return
    * @throws QueryDataException
    * @throws SDXException
    */
   private void activateDataset (BusinessModelPreparationContext businessModelPreparationContext,
            JobRequest jobRequest, Long assetId, JobOperationalStatus jobOperationalStatus) throws QueryDataException,
            SDXException {
      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               "Activating the Dataset Collection", JobStatus.INPROGRESS, null, new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);
      getAssetActivationService().activateAsset(businessModelPreparationContext.getUserId(), assetId);
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);
   }

   /**
    * @param jobRequest
    * @param assetId
    * @return
    * @throws QueryDataException
    * @throws JoinException
    */
   private void manageIndirectJoins (JobRequest jobRequest, Long assetId, JobOperationalStatus jobOperationalStatus)
            throws QueryDataException, JoinException {
      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               "Rebuilding Joins for DataSet", JobStatus.INPROGRESS, null, new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);
      getJoinService().rebuildIndirectJoins(assetId);
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);
   }

   /**
    * @param jobRequest
    * @param modelId
    * @return
    * @throws QueryDataException
    * @throws KDXException
    */
   protected void cleanupBusinessEntityMaintenanceDetails (JobRequest jobRequest, Long modelId,
            JobOperationalStatus jobOperationalStatus) throws QueryDataException, KDXException {
      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               "Cleaning the Maintenance Records Processed", JobStatus.INPROGRESS, null, new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);
      businessEntityMaintenanceService.deleteBusinessEntityMaintenanceDetails(modelId);
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);
   }

   /**
    * @param jobRequest
    * @param modelId
    * @return
    * @throws QueryDataException
    * @throws KDXException
    */
   private void cleanupOrphanSFLTerms (JobRequest jobRequest, Long modelId, JobOperationalStatus jobOperationalStatus)
            throws QueryDataException, KDXException {
      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               "Deleting Orphan SFL terms", JobStatus.INPROGRESS, null, new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);
      List<ModelGroup> modelGroupIds = getKdxRetrievalService().getUserModelGroupsByModelId(modelId);
      Long modelGroupId = modelGroupIds.get(0).getId();
      List<SFLTerm> orphanSFLTerms = getKdxRetrievalService().getOrphanSFLTerms(modelGroupId);
      // delete the orphan SFLs
      if (ExecueCoreUtil.isCollectionNotEmpty(orphanSFLTerms)) {
         for (SFLTerm orphanSFLTerm : orphanSFLTerms) {
            getSflService().deleteSFLTerm(orphanSFLTerm);
         }
      }
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);
   }

   /**
    * @param jobRequest
    * @param modelId
    * @return
    * @throws QueryDataException
    * @throws SWIException
    * @throws KDXException
    */
   private void manageIndirectPaths (JobRequest jobRequest, Long modelId, JobOperationalStatus jobOperationalStatus)
            throws QueryDataException, SWIException, KDXException {
      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               "Creating Indirect Paths", JobStatus.INPROGRESS, null, new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);
      getPathAbsorptionService().absorbIndirectPaths(getKdxCloudRetrievalService().getDefaultAppCloud(modelId).getId(),
               modelId);
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);
   }

   /**
    * @param jobRequest
    * @param modelId
    * @param userId 
    * @return
    * @throws QueryDataException
    * @throws SWIException
    */
   private void performIndexFormManagement (JobRequest jobRequest, Long modelId, Long userId,
            JobOperationalStatus jobOperationalStatus) throws QueryDataException, SWIException {
      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest, "Absoring the Indexes",
               JobStatus.INPROGRESS, null, new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);
      IndexFormManagementContext indexFormManagementContext = new IndexFormManagementContext();
      indexFormManagementContext.setModelId(modelId);
      indexFormManagementContext.setJobRequest(jobRequest);
      indexFormManagementContext.setUserId(userId);
      getIndexFormManagementService().manageIndexForms(indexFormManagementContext);
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);
   }

   /**
    * @param jobRequest
    * @param modelId
    * @return
    * @throws QueryDataException
    * @throws SWIException
    * @throws KDXException
    */
   private void manageConceptsFromBaseInAppCloud (JobRequest jobRequest, Long modelId,
            JobOperationalStatus jobOperationalStatus) throws QueryDataException, SWIException, KDXException {
      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               "Managing the Base Concepts in Cloud", JobStatus.INPROGRESS, null, new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);
      getKdxCloudManagementService().manageBaseConceptsInCloud(
               getKdxCloudRetrievalService().getDefaultAppCloud(modelId), modelId);
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);
   }

   /**
    * @param jobRequest
    * @param applicationId
    * @return
    * @throws QueryDataException
    * @throws SWIException
    */
   private void performEASIndexRefresh (JobRequest jobRequest, Long applicationId,
            JobOperationalStatus jobOperationalStatus) throws QueryDataException, SWIException {
      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               "Refreshing the EAS Indexes for the application", JobStatus.INPROGRESS, null, new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);
      EASIndexPeriodicContext easIndexPeriodicContext = new EASIndexPeriodicContext();
      easIndexPeriodicContext.setJobRequest(jobRequest);
      easIndexPeriodicContext.setApplicationId(applicationId);
      getEasIndexPopulationService().populateEASIndex(easIndexPeriodicContext);
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);
   }

   /**
    * @param jobRequest
    * @param modelId
    * @param assetId
    * @return
    * @throws QueryDataException
    * @throws SWIException
    * @throws PlatformException 
    * @throws OntologyException
    */
   private void performModelEvaluation (JobRequest jobRequest, Long modelId, Long assetId,
            JobOperationalStatus jobOperationalStatus) throws QueryDataException, SWIException, PlatformException {
      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               "Absorbing the Terminology from the Dataset", JobStatus.INPROGRESS, null, new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);

      List<Long> addedConceptBEDIds = getBusinessEntityMaintenanceService().getBusinessEntityMaintenanceDetails(
               modelId, OperationType.ADD, EntityType.CONCEPT);
      List<Long> updatedConceptBEDIds = getBusinessEntityMaintenanceService()
               .getDistinctUpdatedEntityMaintenanceDetails(modelId, EntityType.CONCEPT);

      // if concept has been modified and created, we can consider it as only create.
      updatedConceptBEDIds.removeAll(addedConceptBEDIds);

      List<TypeConceptEvaluationInfo> evaluateAddedConcepts = getConceptTypeEvaluationService()
               .evaluateConceptsForBaseTypes(assetId, modelId, addedConceptBEDIds, OperationType.ADD);

      List<TypeConceptEvaluationInfo> evaluateUpdatedConcepts = getConceptTypeEvaluationService()
               .evaluateConceptsForBaseTypes(assetId, modelId, updatedConceptBEDIds, OperationType.UPDATE);

      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);

      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               "Absorbing the Business Model from terminology", JobStatus.INPROGRESS, null, new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);

      List<TypeConceptEvaluationInfo> allEvaluatedConcepts = new ArrayList<TypeConceptEvaluationInfo>();
      allEvaluatedConcepts.addAll(evaluateAddedConcepts);
      allEvaluatedConcepts.addAll(evaluateUpdatedConcepts);
      getModelAbsorbtionService().absorbModel(allEvaluatedConcepts, modelId);

      // TODO : -RG- Restrict the model from being evaluated again should only happen
      // only when a proper solution for analysis is done, till then this call should not happen
      // getKdxManagementService().restrictModelFromEvaluation(modelId);

      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);

      handleIndicatorConcepts(modelId, assetId, allEvaluatedConcepts);
   }

   private void handleIndicatorConcepts (Long modelId, Long assetId,
            List<TypeConceptEvaluationInfo> allEvaluatedConcepts) throws KDXException, SDX2KDXMappingException,
            MappingException, SDXException, PlatformException {
      for (TypeConceptEvaluationInfo typeConceptEvaluationInfo : allEvaluatedConcepts) {
         if (ExecueCoreUtil.isCollectionNotEmpty(typeConceptEvaluationInfo.getBehaviorTypes())
                  && typeConceptEvaluationInfo.getBehaviorTypes().contains(BehaviorType.INDICATOR)) {
            Model model = getKdxRetrievalService().getModelById(modelId);
            BusinessEntityDefinition conceptBed = typeConceptEvaluationInfo.getConceptBed();
            Concept concept = conceptBed.getConcept();
            getBusinessEntityDeletionWrapperService().deleteInstancesHierarchyForConcept(modelId, concept.getId());
            List<Mapping> mappings = getMappingRetrievalService().getMappingsForBED(conceptBed.getId());
            List<AssetEntityDefinition> assetEntityDefinitions = ExecueBeanUtil.getAssetEntityDefinitions(mappings);
            AssetEntityDefinition correctAssetEntityDefinition = getSdxRetrievalService()
                     .pickCorrectAssetEntityDefinition(assetEntityDefinitions);
            List<Membr> columnMembers = getSdxRetrievalService().getColumnMembers(
                     correctAssetEntityDefinition.getColum());

            // handling positive member
            Membr positiveMember = pickMemberByIndicatorBehavior(columnMembers, IndicatorBehaviorType.POSITIVE);
            List<Membr> positiveMemberList = new ArrayList<Membr>();
            positiveMemberList.add(positiveMember);
            List<Long> positiveInstanceBedList = getSdx2kdxMappingService().mapMembersForAssetSyncUpProcess(
                     correctAssetEntityDefinition.getAsset(), correctAssetEntityDefinition.getTabl(),
                     correctAssetEntityDefinition.getColum(), positiveMemberList, conceptBed, model);
            BusinessEntityDefinition positiveInstanceBED = getKdxRetrievalService().getBusinessEntityDefinitionById(
                     positiveInstanceBedList.get(0));
            Instance positiveInstance = positiveInstanceBED.getInstance();
            positiveInstance.setDisplayName(concept.getName());
            positiveInstance.setDescription(concept.getName());
            getKdxManagementService().updateInstance(modelId, concept.getId(), positiveInstance);

            // handle negative member
            Membr negativeMember = pickMemberByIndicatorBehavior(columnMembers, IndicatorBehaviorType.NEGATIVE);
            List<Membr> negativeMemberList = new ArrayList<Membr>();
            negativeMemberList.add(negativeMember);
            List<Long> negativeInstanceBedList = getSdx2kdxMappingService().mapMembersForAssetSyncUpProcess(
                     correctAssetEntityDefinition.getAsset(), correctAssetEntityDefinition.getTabl(),
                     correctAssetEntityDefinition.getColum(), negativeMemberList, conceptBed, model);
            BusinessEntityDefinition negativeInstanceBED = getKdxRetrievalService().getBusinessEntityDefinitionById(
                     negativeInstanceBedList.get(0));
            Instance negativeInstance = negativeInstanceBED.getInstance();
            negativeInstance.setDisplayName("Non" + concept.getName());
            negativeInstance.setDescription("Non" + concept.getName());
            getKdxManagementService().updateInstance(modelId, concept.getId(), negativeInstance);
         }
      }
   }

   private Membr pickMemberByIndicatorBehavior (List<Membr> columnMembers, IndicatorBehaviorType indicatorBehavior) {
      Membr indicatorMembr = null;
      for (Membr membr : columnMembers) {
         if (membr.getIndicatorBehavior().equals(indicatorBehavior)) {
            indicatorMembr = membr;
            break;
         }
      }
      return indicatorMembr;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IConceptTypeEvaluationService getConceptTypeEvaluationService () {
      return conceptTypeEvaluationService;
   }

   public void setConceptTypeEvaluationService (IConceptTypeEvaluationService conceptTypeEvaluationService) {
      this.conceptTypeEvaluationService = conceptTypeEvaluationService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IAssetActivationService getAssetActivationService () {
      return assetActivationService;
   }

   public void setAssetActivationService (IAssetActivationService assetActivationService) {
      this.assetActivationService = assetActivationService;
   }

   /**
    * @return the indexFormManagementService
    */
   public IIndexFormManagementService getIndexFormManagementService () {
      return indexFormManagementService;
   }

   /**
    * @param indexFormManagementService
    *           the indexFormManagementService to set
    */
   public void setIndexFormManagementService (IIndexFormManagementService indexFormManagementService) {
      this.indexFormManagementService = indexFormManagementService;
   }

   public IPathAbsorptionService getPathAbsorptionService () {
      return pathAbsorptionService;
   }

   public void setPathAbsorptionService (IPathAbsorptionService pathAbsorptionService) {
      this.pathAbsorptionService = pathAbsorptionService;
   }

   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

   /**
    * @return the kdxManagementService
    */
   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   /**
    * @param kdxManagementService
    *           the kdxManagementService to set
    */
   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
   }

   /**
    * @return the kdxModelService
    */
   public IKDXModelService getKdxModelService () {
      return kdxModelService;
   }

   /**
    * @param kdxModelService
    *           the kdxModelService to set
    */
   public void setKdxModelService (IKDXModelService kdxModelService) {
      this.kdxModelService = kdxModelService;
   }

   public ISFLService getSflService () {
      return sflService;
   }

   public void setSflService (ISFLService sflService) {
      this.sflService = sflService;
   }

   public IBusinessEntityMaintenanceService getBusinessEntityMaintenanceService () {
      return businessEntityMaintenanceService;
   }

   public void setBusinessEntityMaintenanceService (IBusinessEntityMaintenanceService businessEntityMaintenanceService) {
      this.businessEntityMaintenanceService = businessEntityMaintenanceService;
   }

   /**
    * @return the kdxCloudManagementService
    */
   public IKDXCloudManagementService getKdxCloudManagementService () {
      return kdxCloudManagementService;
   }

   /**
    * @param kdxCloudManagementService
    *           the kdxCloudManagementService to set
    */
   public void setKdxCloudManagementService (IKDXCloudManagementService kdxCloudManagementService) {
      this.kdxCloudManagementService = kdxCloudManagementService;
   }

   public IJoinService getJoinService () {
      return joinService;
   }

   public void setJoinService (IJoinService joinService) {
      this.joinService = joinService;
   }

   public IKDXMaintenanceService getKdxMaintenanceService () {
      return kdxMaintenanceService;
   }

   public void setKdxMaintenanceService (IKDXMaintenanceService kdxMaintenanceService) {
      this.kdxMaintenanceService = kdxMaintenanceService;
   }

   public ISDX2KDXMappingService getSdx2kdxMappingService () {
      return sdx2kdxMappingService;
   }

   public void setSdx2kdxMappingService (ISDX2KDXMappingService sdx2kdxMappingService) {
      this.sdx2kdxMappingService = sdx2kdxMappingService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
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
    * @param applicationRetrievalService
    *           the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

   public IDefaultDynamicValuePopulationService getDefaultDynamicValuePopulationService () {
      return defaultDynamicValuePopulationService;
   }

   public void setDefaultDynamicValuePopulationService (
            IDefaultDynamicValuePopulationService defaultDynamicValuePopulationService) {
      this.defaultDynamicValuePopulationService = defaultDynamicValuePopulationService;
   }

   public IBusinessEntityDeletionWrapperService getBusinessEntityDeletionWrapperService () {
      return businessEntityDeletionWrapperService;
   }

   public void setBusinessEntityDeletionWrapperService (
            IBusinessEntityDeletionWrapperService businessEntityDeletionWrapperService) {
      this.businessEntityDeletionWrapperService = businessEntityDeletionWrapperService;
   }

   public IEASIndexPopulationService getEasIndexPopulationService () {
      return easIndexPopulationService;
   }

   public void setEasIndexPopulationService (IEASIndexPopulationService easIndexPopulationService) {
      this.easIndexPopulationService = easIndexPopulationService;
   }

   public IModelAbsorbtionService getModelAbsorbtionService () {
      return modelAbsorbtionService;
   }

   public void setModelAbsorbtionService (IModelAbsorbtionService modelAbsorbtionService) {
      this.modelAbsorbtionService = modelAbsorbtionService;
   }

   /**
    * @return the unstructuredWarehouseManagementWrapperService
    */
   public IUnstructuredWarehouseManagementWrapperService getUnstructuredWarehouseManagementWrapperService () {
      return unstructuredWarehouseManagementWrapperService;
   }

   /**
    * @param unstructuredWarehouseManagementWrapperService the unstructuredWarehouseManagementWrapperService to set
    */
   public void setUnstructuredWarehouseManagementWrapperService (
            IUnstructuredWarehouseManagementWrapperService unstructuredWarehouseManagementWrapperService) {
      this.unstructuredWarehouseManagementWrapperService = unstructuredWarehouseManagementWrapperService;
   }

}
