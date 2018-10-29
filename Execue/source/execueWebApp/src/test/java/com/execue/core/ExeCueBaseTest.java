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


package com.execue.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.execue.ac.service.ICubeCreationService;
import com.execue.core.common.api.batchmaintenance.ISDXBatchMaintenanceService;
import com.execue.core.common.api.model.IKDXModelService;
import com.execue.core.common.api.nlp.INLPEngine;
import com.execue.core.common.api.qdata.IQueryDataService;
import com.execue.core.common.api.qdata.IUDXService;
import com.execue.core.common.api.qdata.IUserNotificationService;
import com.execue.core.common.api.querygen.IQueryGenerationService;
import com.execue.core.common.api.querygen.IQueryGenerationUtilService;
import com.execue.core.common.api.querygen.QueryGenerationServiceFactory;
import com.execue.core.common.api.querygen.QueryGenerationUtilServiceFactory;
import com.execue.core.common.api.semantic.ISemanticSuggestService;
import com.execue.core.common.api.swi.IApplicationService;
import com.execue.core.common.api.swi.IAssetAnalysisService;
import com.execue.core.common.api.swi.IAssetSyncAbsorptionService;
import com.execue.core.common.api.swi.IAssetSyncPopulateService;
import com.execue.core.common.api.swi.IAssetSynchronizationService;
import com.execue.core.common.api.swi.IBaseKDXRetrievalService;
import com.execue.core.common.api.swi.IBatchMaintenanceService;
import com.execue.core.common.api.swi.IConceptTypeAssociationService;
import com.execue.core.common.api.swi.ICorrectMappingService;
import com.execue.core.common.api.swi.ICorrectMappingsJobMaintenanceService;
import com.execue.core.common.api.swi.IDataSourceSelectionService;
import com.execue.core.common.api.swi.IDefaultMetricService;
import com.execue.core.common.api.swi.IGenericJDBCService;
import com.execue.core.common.api.swi.IKDXCloudManagementService;
import com.execue.core.common.api.swi.IKDXCloudRetrievalService;
import com.execue.core.common.api.swi.IKDXMaintenanceService;
import com.execue.core.common.api.swi.IKDXManagementService;
import com.execue.core.common.api.swi.IKDXRetrievalService;
import com.execue.core.common.api.swi.ILookupService;
import com.execue.core.common.api.swi.IMappingService;
import com.execue.core.common.api.swi.IPathAbsorptionService;
import com.execue.core.common.api.swi.IPathDefinitionService;
import com.execue.core.common.api.swi.IPopularityCollectionService;
import com.execue.core.common.api.swi.IPopularityService;
import com.execue.core.common.api.swi.IPreferencesService;
import com.execue.core.common.api.swi.IPublishedFileService;
import com.execue.core.common.api.swi.IRICloudsAbsorptionService;
import com.execue.core.common.api.swi.IRIOntermsPopularityHitMaintainenceJobService;
import com.execue.core.common.api.swi.IRIOntoTermsAbsorptionJobService;
import com.execue.core.common.api.swi.IRIOntoTermsAbsorptionService;
import com.execue.core.common.api.swi.ISDX2KDXMappingService;
import com.execue.core.common.api.swi.ISDXJobDeletionService;
import com.execue.core.common.api.swi.ISDXService;
import com.execue.core.common.api.swi.ISFLService;
import com.execue.core.common.api.swi.ISnowFlakesTermsAbsorptionJobService;
import com.execue.core.common.api.swi.ISnowFlakesTermsAbsorptionService;
import com.execue.core.common.api.swi.ISourceMetaDataService;
import com.execue.core.common.api.swi.IUpdateSFLTermTokenWeightJobService;
import com.execue.core.common.api.swi.IUserQueryPossibilityService;
import com.execue.core.common.api.swi.typeconversion.ITypeConvertor;
import com.execue.core.common.bean.TypeConceptAssociationInfo;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.ontology.absorption.IFileOntologyDataAbsorptionJobService;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.configuration.IConfigurable;
import com.execue.core.system.ExecueSystem;
import com.execue.dataaccess.qdata.dao.IQDataUserQueryDAO;
import com.execue.dataaccess.swi.dao.IAssetBusinessEntityMappingDAO;
import com.execue.dataaccess.swi.dao.IAssetDAO;
import com.execue.dataaccess.swi.dao.IAssetDetailDAO;
import com.execue.dataaccess.swi.dao.IAssetEntityDefinitionDAO;
import com.execue.dataaccess.swi.dao.IBusinessEntityDefinitionDAO;
import com.execue.dataaccess.swi.dao.IDateFormatDAO;
import com.execue.dataaccess.swi.dao.IJoinDAO;
import com.execue.dataaccess.swi.dao.IOntoReverseIndexDAO;
import com.execue.handler.bean.UICubeCreation;
import com.execue.handler.swi.ICubeCreationServiceHandler;
import com.execue.handler.swi.IPreferencesServiceHandler;
import com.execue.handler.swi.ITypeConceptAssociationServiceHandler;
import com.execue.handler.swi.mappings.IMappingServiceHandler;
import com.execue.ks.service.IKnowledgeBaseSearchEngine;
import com.execue.message.dao.IMessageDAO;
import com.execue.nlp.helper.NLPServiceHelper;
import com.execue.ontology.absorbtion.IFileOntologyDataAbsorptionService;
import com.execue.ontology.dataaccess.ISnowFlakesTermsDAO;
import com.execue.ontology.service.IRIOntoTermCreationJobService;
import com.execue.qdata.service.impl.TransactionIdGenerationService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.dataaccess.IPublishedFileDataAccessManager;
import com.execue.swi.dataaccess.ISDXDataAccessManager;
import com.execue.swi.helper.IndexFormManagementHelper;
import com.execue.swi.service.impl.BulkInstanceMappingCreation;
import com.execue.uss.service.IEntitySearchEngine;
import com.execue.uss.service.IUnstructuredSearchEngine;
import com.execue.util.conversion.IDateUnitConversion;
import com.execue.web.core.action.swi.JobRequestStatusAction;
import com.execue.web.core.action.swi.UploadAction;

public class ExeCueBaseTest {

   static ApplicationContext context;

   public static ApplicationContext getContext () {
      return context;
   }

   public static void setContext (ApplicationContext context) {
      ExeCueBaseTest.context = context;
   }

   public static void baseTestSetup () {
      context = new ClassPathXmlApplicationContext(new String[] { "/bean-config/execue-application.xml" });
      try {
         ((ExecueSystem) getContext().getBean("execueSystem")).initialize();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public ISFLService getSFLService () {
      return (ISFLService) context.getBean("sflService");
   }

   public IPathAbsorptionService getPathAbsorptionService () {
      return (IPathAbsorptionService) context.getBean("pathAbsorptionService");
   }

   public IPathDefinitionService getPathDefinitionService () {
      return (IPathDefinitionService) context.getBean("pathDefinitionService");
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return (IBaseKDXRetrievalService) context.getBean("baseKDXRetrievalService");
   }

   public IKDXCloudManagementService getCloudManagementService () {
      return (IKDXCloudManagementService) context.getBean("cloudManagementService");
   }

   public IPopularityService getPopularityService () {
      return (IPopularityService) context.getBean("popularityService");
   }

   public IPopularityCollectionService getPopularityCollectionService () {
      return (IPopularityCollectionService) context.getBean("popularityCollectionService");
   }

   public ISemanticSuggestService getSemanticSuggestService () {
      return (ISemanticSuggestService) context.getBean("semanticSuggestService");
   }

   public IMessageDAO getMessageDAO () {
      return (IMessageDAO) context.getBean("messageDAO");
   }

   public ISDX2KDXMappingService getSDX2KDXMappingService () {
      return (ISDX2KDXMappingService) context.getBean("sdx2kdxMappingService");
   }

   public BulkInstanceMappingCreation getBulkInstanceMappingCreation () {
      return (BulkInstanceMappingCreation) context.getBean("bulkInstanceMappingCreation");
   }

   public IGenericJDBCService getGenericJDBCService () {
      return (IGenericJDBCService) context.getBean("genericJDBCService");
   }

   public ISDXService getSDXService () {
      return (ISDXService) context.getBean("sdxService");
   }

   public IKDXRetrievalService getKDXRetrievalService () {
      return (IKDXRetrievalService) context.getBean("kdxRetrievalService");
   }

   public IPreferencesService getPreferenceService () {
      return (IPreferencesService) context.getBean("preferencesService");
   }

   public IKDXMaintenanceService getKDXMaintenanceService () {
      return (IKDXMaintenanceService) context.getBean("kdxMaintenanceService");
   }

   public IUpdateSFLTermTokenWeightJobService getUpdateSFLTermTokenWeightJobService () {
      return (IUpdateSFLTermTokenWeightJobService) context.getBean("sflTermTokenWeightJobService");
   }

   public IMappingService getMappingService () {
      return (IMappingService) context.getBean("mappingService");
   }

   public ILookupService getLookupService () {
      return (ILookupService) context.getBean("lookupService");
   }

   public ICubeCreationService getCubeCreationService () {
      return (ICubeCreationService) context.getBean("cubeCreationService");
   }

   public IMappingServiceHandler getMappingServiceHandler () {
      return (IMappingServiceHandler) context.getBean("mappingServiceHandler");
   }

   public IAssetSyncAbsorptionService getAssetSyncAbsorptionService () {
      return (IAssetSyncAbsorptionService) context.getBean("assetSyncAbsorptionService");
   }

   public ICubeCreationServiceHandler getCubeCreationServiceHandler () {
      return (ICubeCreationServiceHandler) context.getBean("cubeCreationServiceHandler");
   }

   public IAssetSyncPopulateService getAssetSyncPopulateService () {
      return (IAssetSyncPopulateService) context.getBean("assetSyncPopulateService");
   }

   public IAssetSynchronizationService getAssetSynchronizationService () {
      return (IAssetSynchronizationService) context.getBean("assetSynchronizationService");
   }

   public IQueryGenerationService getQueryGenerationService (Asset asset) {
      QueryGenerationServiceFactory queryGenerationServiceFactory = (QueryGenerationServiceFactory) context
               .getBean("queryGenerationServiceFactory");
      return queryGenerationServiceFactory.getQueryGenerationService(asset);
   }

   public IPreferencesServiceHandler getPreferencesServiceHandler () {
      return (IPreferencesServiceHandler) context.getBean("preferencesServiceHandler");
   }

   public IQueryGenerationUtilService getSQLGenerationUtil (AssetProviderType assetProviderType) {
      QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory = (QueryGenerationUtilServiceFactory) context
               .getBean("queryGenerationUtilServiceFactory");
      return queryGenerationUtilServiceFactory.getQueryGenerationUtilService(assetProviderType);
   }

   public IConfigurable getNLPConfiguration () {
      return (IConfigurable) getContext().getBean("nlpConfigurationService");
   }

   public ISWIConfigurationService getSWIConfiguration () {
      return (ISWIConfigurationService) getContext().getBean("swiConfigurationService");
   }

   public INLPEngine getNLPEngine () {
      return (INLPEngine) context.getBean("nlpEngine");
   }

   public HibernateTemplate getHibernateTemplate () {
      return (HibernateTemplate) context.getBean("hibernateTemplate");
   }

   public IFileOntologyDataAbsorptionService getFileOntologyDataAbsorptionService () {
      return (IFileOntologyDataAbsorptionService) context.getBean("fileOntologyDataAbsorptionService");

   }

   public IRIOntoTermsAbsorptionService getRiOntoTermsAbsorptionService () {
      return (IRIOntoTermsAbsorptionService) context.getBean("riOntoTermsAbsorptionService");
   }

   public IFileOntologyDataAbsorptionJobService getFileOntologyDataAbsorptionJobService () {
      return (IFileOntologyDataAbsorptionJobService) context.getBean("fileOntologyDataAbsorptionJobService");
   }

   public IRIOntoTermsAbsorptionJobService getRiOntoTermsAbsorptionJobService () {
      return (IRIOntoTermsAbsorptionJobService) context.getBean("riOntoTermsAbsorptionJobService");
   }

   public IRIOntoTermCreationJobService getRiOntoTermsCreationJobService () {
      return (IRIOntoTermCreationJobService) context.getBean("riOntoTermCreationJobService");
   }

   public ISnowFlakesTermsDAO getSnowFlakesTermsDAO () {
      return (ISnowFlakesTermsDAO) context.getBean("snowFlakesTermsDAO");
   }

   public ISnowFlakesTermsAbsorptionService getSnowFlakesTermsAbsorptionService () {
      return (ISnowFlakesTermsAbsorptionService) context.getBean("snowFlakesTermsAbsorptionService");
   }

   public ISnowFlakesTermsAbsorptionJobService getSnowFlakesTermsAbsorptionJobService () {
      return (ISnowFlakesTermsAbsorptionJobService) context.getBean("snowFlakesTermsAbsorptionJobService");
   }

   public IRIOntermsPopularityHitMaintainenceJobService getRIOntermsPopularityHitMaintainenceJobService () {
      return (IRIOntermsPopularityHitMaintainenceJobService) context
               .getBean("riOntermsPopularityHitMaintainenceJobService");
   }

   public ICorrectMappingsJobMaintenanceService getCorrectMappingsJobService () {
      return (ICorrectMappingsJobMaintenanceService) context.getBean("correctMappingsJobService");
   }

   public IUserQueryPossibilityService getUserQueryPossibilityService () {
      return (IUserQueryPossibilityService) context.getBean("userQueryPossibilityService");
   }

   public IAssetDetailDAO getAssetDetailDAO () {
      return (IAssetDetailDAO) context.getBean("assetDetailDAO");
   }

   public IPreferencesService getPreferencesService () {
      return (IPreferencesService) context.getBean("preferencesService");
   }

   public IKDXManagementService getKDXManagementService () {
      return (IKDXManagementService) context.getBean("kdxManagementService");
   }

   public IKDXModelService getKDXModelService () {
      return (IKDXModelService) context.getBean("kdxModelService");
   }

   public IBatchMaintenanceService getBatchMaintenanceService () {
      return (IBatchMaintenanceService) context.getBean("batchMaintenanceService");
   }

   public static void baseTestTeardown () {
      if (context == null) {
         return;
      }
      SessionFactory factory = (SessionFactory) context.getBean("swiSessionFactory");
      if (factory != null) {
         factory.close();
      }
   }

   public UICubeCreation prepareUICubeCreation () {
      UICubeCreation uCubeCreation = new UICubeCreation();
      Asset asset = new Asset();
      asset.setId(11L);
      uCubeCreation.setBaseAsset(asset);
      Concept concept = new Concept();
      concept.setName("Time");
      Concept concept2 = new Concept();
      concept2.setName("AccountStatus");
      List<Concept> coList = new ArrayList<Concept>();
      coList.add(concept);
      coList.add(concept2);
      uCubeCreation.setSelectedConcepts(coList);
      return uCubeCreation;
   }

   public TypeConceptAssociationInfo populateTypeConceptInfo (BusinessEntityDefinition conceptBed,
            BusinessEntityDefinition bedType, Cloud cloud, List<BehaviorType> possibleBehaviors,
            Map<Long, List<EntityTripleDefinition>> attributePaths, boolean attributesProvided, boolean behaviorProvided) {
      TypeConceptAssociationInfo typeConceptInfo = new TypeConceptAssociationInfo();
      typeConceptInfo.setConceptBed(conceptBed);
      typeConceptInfo.setBedType(bedType);
      typeConceptInfo.setCloud(cloud);
      typeConceptInfo.setBehaviorTypes(possibleBehaviors);
      typeConceptInfo.setAttributePaths(attributePaths);
      typeConceptInfo.setBehaviorProvided(behaviorProvided);
      typeConceptInfo.setAttributesProvided(attributesProvided);
      return typeConceptInfo;
   }

   public DefaultMetric getDefaultMetric (Long tableId, Long mappingId, Long popularity) {
      DefaultMetric defaultMetric = new DefaultMetric();
      defaultMetric.setTableId(tableId);
      defaultMetric.setMappingId(mappingId);
      defaultMetric.setPopularity(popularity);
      return defaultMetric;
   }

   public IBusinessEntityDefinitionDAO getdomainEntityDefinitionDAO () {
      return (IBusinessEntityDefinitionDAO) context.getBean("businessEntityDefinitionDAO");
   }

   public IPublishedFileDataAccessManager getPublishedFileDAO () {
      return (IPublishedFileDataAccessManager) context.getBean("publishedFileDataAccessManager");
   }

   public IPublishedFileService getPublishedFileService () {
      return (IPublishedFileService) context.getBean("publishedFileService");
   }

   public ITypeConvertor getDateTypeConvertor () {
      return (ITypeConvertor) context.getBean("dateTypeConvertor");
   }

   public IDateUnitConversion getDateUnitCoversion () {
      return (IDateUnitConversion) context.getBean("dateUnitConversion");
   }

   public IApplicationService getApplicationService () {
      return (IApplicationService) context.getBean("applicationService");
   }

   public IQDataUserQueryDAO getQDataUserQueryDAO () {
      return (IQDataUserQueryDAO) context.getBean("qDataUserQueryDAO");
   }

   public ICorrectMappingService getCorrectMappingService () {
      return (ICorrectMappingService) context.getBean("correctMappingService");
   }

   public IKDXCloudRetrievalService getKDXCloudRetrievalService () {
      return (IKDXCloudRetrievalService) context.getBean("kdxCloudRetrievalService");
   }

   public NLPServiceHelper getNLPServiceHelper () {
      return (NLPServiceHelper) context.getBean("nlpServiceHelper");
   }

   public IBusinessEntityDefinitionDAO getBusinessEntityDefinitionDAO () {
      return (IBusinessEntityDefinitionDAO) context.getBean("businessEntityDefinitionDAO");
   }

   public IAssetBusinessEntityMappingDAO getAssetBusinessEntityMappingDAO () {
      return (IAssetBusinessEntityMappingDAO) context.getBean("assetBusinessEntityMappingDAO");
   }

   public IUDXService getUDXService () {
      return (IUDXService) context.getBean("udxService");
   }

   /**
    * @return the transactionIdService
    */
   public TransactionIdGenerationService getTransactionIdService () {
      return (TransactionIdGenerationService) context.getBean("transactionIdService");
   }

   /**
    * @return the entitySearchEngine
    */
   public IEntitySearchEngine getEntitySearchEngine () {
      return (IEntitySearchEngine) context.getBean("entitySearchEngine");
   }

   public IUnstructuredSearchEngine getUnstructuredSearchEngine () {
      return (IUnstructuredSearchEngine) context.getBean("unstructuredSearchEngine");
   }

   /*
    * public IUserQueryPossibilityDeleteionJobService getUserQueryPossibilityDeleteionJobService () { return
    * (IUserQueryPossibilityDeleteionJobService) context.getBean("userQueryPossibilityDeleteionJobService"); }
    */

   public IKnowledgeBaseSearchEngine getKnowledgeSearchEngine () {
      return (IKnowledgeBaseSearchEngine) context.getBean("knowledgeSearchEngine");
   }

   /**
    * @return
    */
   public ISDXJobDeletionService getSDXJobDeletionService () {
      return (ISDXJobDeletionService) context.getBean("sdxJobDeletionService");
   }

   public IDateFormatDAO getDateFormatDAO () {
      return (IDateFormatDAO) context.getBean("dateFormatDAO");
   }

   public ISourceMetaDataService getJDBCSourceMetaDataService () {
      return (ISourceMetaDataService) context.getBean("sourceMetaDataService");
   }

   public ISDXBatchMaintenanceService getSDXBatchMaintenanceService () {
      return (ISDXBatchMaintenanceService) context.getBean("sdxBatchMaintenanceService");
   }

   public IOntoReverseIndexDAO getOntoReverseIndexDAO () {
      return (IOntoReverseIndexDAO) context.getBean("ontoReverseIndexDAO");
   }

   public IDataSourceSelectionService getDataSourceSelectionService () {
      return (IDataSourceSelectionService) context.getBean("dataSourceSelectionService");
   }

   public IAssetDAO getAssetDAO () {
      return (IAssetDAO) context.getBean("assetDAO");
   }

   public ISDXDataAccessManager getSDXDataAccessManager () {
      return (ISDXDataAccessManager) context.getBean("sdxDataAccessManager");
   }

   public IQueryDataService getQueryDataService () {
      return (IQueryDataService) context.getBean("qdataService");
   }

   public IKDXDataAccessManager getKDXDataAccessManager () {
      return (IKDXDataAccessManager) context.getBean("kdxDataAccessManager");
   }

   public UploadAction getUploadAction () {
      return (UploadAction) context.getBean("uploadAction");
   }

   public JobRequestStatusAction getJobRequestStatusAction () {
      return (JobRequestStatusAction) context.getBean("jobRequestStatusAction");
   }

   public IUserNotificationService getUserNotificationService () {
      return (IUserNotificationService) context.getBean("userNotificationService");
   }

   public IDefaultMetricService getDefaultMetricService () {
      return (IDefaultMetricService) context.getBean("defaultMetricService");
   }

   public IJoinDAO getJoinDAO () {
      return (IJoinDAO) context.getBean("joinDAO");
   }

   public IAssetEntityDefinitionDAO getAssetEntityDefinitionDAO () {
      return (IAssetEntityDefinitionDAO) context.getBean("assetEntityDefinitionDAO");
   }

   public IAssetAnalysisService getAssetAnalysisService () {
      return (IAssetAnalysisService) context.getBean("assetAnalysisService");
   }

   public IRICloudsAbsorptionService getRICloudsAbsorptionService () {
      return (IRICloudsAbsorptionService) context.getBean("riCloudsAbsorptionServiceImpl");
   }

   public IndexFormManagementHelper getIndexFormManagementHelper () {
      return (IndexFormManagementHelper) context.getBean("indexFormManagementHelper");
   }

   public ITypeConceptAssociationServiceHandler getTypeConceptAssociationServiceHandler () {
      return (ITypeConceptAssociationServiceHandler) context.getBean("typeConceptAssociationServiceHandler");
   }

   public IConceptTypeAssociationService getConceptTypeAssociationService () {
      return (IConceptTypeAssociationService) context.getBean("conceptTypeAssociationService");
   }
}
