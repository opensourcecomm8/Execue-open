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


package com.execue.platform.test;

import com.execue.platform.ISamplingStrategyIdentificationService;
import com.execue.platform.shareddata.location.ILocationDataPopulationService;
import com.execue.platform.shareddata.location.bean.LocationDataInputInfo;
import com.execue.platform.swi.IApplicationManagementWrapperService;
import com.execue.platform.swi.IAssetActivationService;
import com.execue.platform.swi.ISDX2KDXMappingService;
import com.execue.platform.swi.shared.SharedModelServiceFactory;
import com.execue.platform.unstructured.IUnstructuredFacetManagementService;
import com.execue.platform.unstructured.IUnstructuredFacetRetrievalService;
import com.execue.platform.unstructured.content.transporter.IUnstructuredTargetBasedContentTransporter;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public abstract class PlatformCommonBaseTest extends PlatformBaseTest {

   @Override
   public IAssetActivationService getAssetActivationService () {
      return (IAssetActivationService) getContext().getBean("assetActivationServiceImpl");
   }

   public SharedModelServiceFactory getSharedModelServiceFactory () {
      return (SharedModelServiceFactory) getContext().getBean("sharedModelServiceFactory");
   }

   public ILocationDataPopulationService getLocationDataPopulationService () {
      return (ILocationDataPopulationService) getContext().getBean("locationDataPopulationService");
   }

   public IUnstructuredFacetManagementService getUnstructuredFacetManagementService () {
      return (IUnstructuredFacetManagementService) getContext().getBean("unstructuredFacetManagementService");
   }

   public IUnstructuredFacetRetrievalService getUnstructuredFacetRetrievalService () {
      return (IUnstructuredFacetRetrievalService) getContext().getBean("unstructuredFacetRetrievalService");
   }

   public IApplicationManagementWrapperService getApplicationManagementWrapperService () {
      return (IApplicationManagementWrapperService) getContext().getBean("applicationManagementWrapperService");
   }

   public IUnstructuredTargetBasedContentTransporter getUnstructuredTargetBasedContentTransporter () {
      return (IUnstructuredTargetBasedContentTransporter) getContext().getBean(
               "unstructuredTargetBasedContentTransporter");
   }

   public ISamplingStrategyIdentificationService getSamplingStrategyIdentificationService () {
      return (ISamplingStrategyIdentificationService) getContext().getBean("samplingStrategyIdentificationService");
   }
   
   public ISDX2KDXMappingService getSDX2KDXMappingService () {
      return (ISDX2KDXMappingService) getContext().getBean("sdx2kdxMappingService");
   }
   
   public ISDXRetrievalService getSDXRetrievalService () {
      return (ISDXRetrievalService) getContext().getBean("sdxRetrievalService");
   }
   
   public IKDXRetrievalService getKDXRetrievalService () {
      return (IKDXRetrievalService) getContext().getBean("kdxRetrievalService");
   }

   public LocationDataInputInfo populateSampleLocationDataInputInfo () {
      LocationDataInputInfo locationDataInputInfo = new LocationDataInputInfo();
      locationDataInputInfo.setLatitude(1.5);
      locationDataInputInfo.setLongitude(4.5);
      // locationDataInputInfo.setZipCode("33065");
      locationDataInputInfo.setCountryName("USA");
      // locationDataInputInfo.setStateName("NY");
      locationDataInputInfo.setCityName("newyork");
      return locationDataInputInfo;
   }
}
