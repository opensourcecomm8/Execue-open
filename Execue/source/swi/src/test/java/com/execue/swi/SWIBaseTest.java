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


package com.execue.swi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.type.LookupType;
import com.execue.core.system.ExeCueSystemConfigurationUtility;
import com.execue.dataaccess.swi.dao.IAssetEntityDefinitionDAO;
import com.execue.dataaccess.swi.dao.IBusinessEntityDefinitionDAO;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.dataaccess.IMappingDataAccessManager;
import com.execue.swi.dataaccess.IPreferencesDataAccessManager;
import com.execue.swi.dataaccess.ISDXDataAccessManager;
import com.execue.swi.service.IApplicationManagementService;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IAssetOperationDetailService;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IConversionService;
import com.execue.swi.service.IEASIndexService;
import com.execue.swi.service.IJoinService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ILookupService;
import com.execue.swi.service.IMappingManagementService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.IPopularityService;
import com.execue.swi.service.IPreferencesManagementService;
import com.execue.swi.service.IPreferencesRetrievalService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.swi.validation.impl.SWIValidatorImpl;

public class SWIBaseTest {

   private static ApplicationContext swiContext;

   public static void baseTearDown () {

   }

   public static void baseSetup () {
      swiContext = new ClassPathXmlApplicationContext(new String[] {
               "/platform/bean-config/execue-logging-configuration.xml",
               "/platform/bean-config/execue-query-builder.xml", "/platform/bean-config/execue-configuration.xml",
               "/ext/bean-config/execue-configuration-ext.xml", "spring-hibernate.xml", "spring-hibernate-qdata.xml",
               "spring-hibernate-sdata.xml", "/platform/bean-config/execue-dataaccess.xml",
               "/platform/bean-config/execue-qdata-dataaccess.xml",
               "/platform/bean-config/execue-sdata-dataaccess.xml", "/platform/bean-config/execue-swi.xml",
               "/platform/bean-config/execue-core.xml", "ext/bean-config/execue-swi-ext.xml",
               "/platform/bean-config/execue-util-services.xml", });
      initialize();
   }

   public static void initialize () {
      try {
         ExeCueSystemConfigurationUtility.loadConfigurationServices(swiContext);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return (IBaseKDXRetrievalService) swiContext.getBean("baseKDXRetrievalService");
   }

   public IKDXRetrievalService getKDXRetrievalService () {
      return (IKDXRetrievalService) swiContext.getBean("kdxRetrievalService");
   }

   public IKDXManagementService getKDXManagementService () {
      return (IKDXManagementService) swiContext.getBean("kdxManagementService");
   }

   public IKDXModelService getKDXModelService () {
      return (IKDXModelService) swiContext.getBean("kdxModelService");
   }

   public IConversionService getConversionService () {
      return (IConversionService) swiContext.getBean("conversionService");
   }

   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return (IPreferencesRetrievalService) swiContext.getBean("preferencesRetrievalService");
   }

   public IPreferencesManagementService getPreferencesManagementService () {
      return (IPreferencesManagementService) swiContext.getBean("preferencesManagementService");
   }

   public ILookupService getLookupService () {
      return (ILookupService) swiContext.getBean("lookupService");
   }

   public ISDXDataAccessManager getSDXDataAccessManager () {
      return (ISDXDataAccessManager) swiContext.getBean("sdxDataAccessManager");
   }

   public ISDXDeletionService getSDXDeletionService () {
      return (ISDXDeletionService) swiContext.getBean("sdxDeletionService");
   }

   public IPreferencesDataAccessManager getPreferencesDataAccessManager () {
      return (IPreferencesDataAccessManager) swiContext.getBean("preferencesDataAccessManager");
   }

   public IKDXDataAccessManager getKDXDataAccessManager () {
      return (IKDXDataAccessManager) swiContext.getBean("kdxDataAccessManager");
   }

   public IMappingDataAccessManager getMappingDataAccessManager () {
      return (IMappingDataAccessManager) swiContext.getBean("mappingDataAccessManager");
   }

   public IAssetEntityDefinitionDAO getAssetEntityDefinitionDAO () {
      return (IAssetEntityDefinitionDAO) swiContext.getBean("assetEntityDefinitionDAO");
   }

   public IBusinessEntityDefinitionDAO getDomainEntityDefinitionDAO () {
      return (IBusinessEntityDefinitionDAO) swiContext.getBean("domainEntityDefinitionDAO");
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return (IMappingRetrievalService) swiContext.getBean("mappingRetrievalService");
   }

   public IMappingManagementService getMappingManagementService () {
      return (IMappingManagementService) swiContext.getBean("mappingManagementService");
   }

   public IJoinService getJoinService () {
      return (IJoinService) swiContext.getBean("joinService");
   }

   public IApplicationManagementService getApplicationManagementService () {
      return (IApplicationManagementService) swiContext.getBean("applicationManagementService");
   }

   public IApplicationRetrievalService getApplicationRetrievalService () {
      return (IApplicationRetrievalService) swiContext.getBean("applicationRetrievalService");
   }

   public SWIValidatorImpl getSwIValidationImpl () {
      return (SWIValidatorImpl) swiContext.getBean("swiValidation");
   }

   public ISDXManagementService getSdxManagementService () {
      return (ISDXManagementService) swiContext.getBean("sdxManagementService");
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return (ISDXRetrievalService) swiContext.getBean("sdxRetrievalService");
   }

   public IPopularityService getPopularityService () {
      return (IPopularityService) swiContext.getBean("popularityService");
   }

   public IEASIndexService getEASIndexService () {
      return (IEASIndexService) swiContext.getBean("easIndexService");
   }

   public IAssetOperationDetailService getAssetOperationDetailService () {
      return (IAssetOperationDetailService) swiContext.getBean("assetOperationDetailService");
   }

   public Asset getSampleAsset (final String name, final long port) {
      final Asset asset = new Asset();
      asset.setName(name);
      return asset;
   }

   public Tabl getSampleTabl (String sampleData, LookupType lookupType) {
      Tabl table = new Tabl();
      table.setName(sampleData);
      table.setLookupType(lookupType);
      return table;
   }

   public Colum getSampleColum (final String sampleColumnName) {
      final Colum colum = new Colum();
      colum.setName(sampleColumnName);
      return colum;
   }

   public Membr getSampleMembr (final String sampleLongDesc) {
      Membr member = new Membr();
      member.setLongDescription(sampleLongDesc);
      return member;
   }

   public Instance getSampleInstance (final String name) {
      final Instance instance = new Instance();
      instance.setName(name);
      instance.setDisplayName(name);
      return instance;
   }

   public Model getSampleModel (final String name) {
      final Model model = new Model();
      model.setName(name);
      return model;
   }

   public Concept getSampleConcept (final String name) {
      final Concept concept = new Concept();
      concept.setName(name);
      concept.setDisplayName(name);
      return concept;
   }

}
