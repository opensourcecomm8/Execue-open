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


package com.execue.platform.configuration.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.configuration.IConfigurable;
import com.execue.core.exception.ConfigurationException;
import com.execue.platform.impl.RFIdGenerationServiceImpl;
import com.execue.platform.impl.TransactionIdGenerationServiceImpl;
import com.execue.platform.swi.ISWIPlatformRetrievalService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;

public class PlatformServicesConfigurableService implements IConfigurable {

   private TransactionIdGenerationServiceImpl       transactionIdGenerationService;
   private RFIdGenerationServiceImpl                rfIdGenerationService;
   private IApplicationRetrievalService             applicationRetrievalService;
   private ISWIPlatformRetrievalService             swiPlatformRetrievalService;
   private PlatformServicesConfigurationServiceImpl platformServicesConfigurationService;

   @Override
   public void doConfigure () throws ConfigurationException {
      try {
         transactionIdGenerationService.refreshBaseValue();
         rfIdGenerationService.refreshBaseValue();
         loadLocationApplicationIds();
      } catch (QueryDataException e) {
         throw new ConfigurationException(e.getCode(), e);
      } catch (SWIException e) {
         throw new ConfigurationException(e.getCode(), e);
      }
   }

   @Override
   public void reConfigure () throws ConfigurationException {
      doConfigure();
   }

   private void loadLocationApplicationIds () throws SWIException {
      List<Application> applications = getApplicationRetrievalService().getAllApplications();
      Set<Long> applicationIds = new HashSet<Long>();
      for (Application application : applications) {
         if (getSwiPlatformRetrievalService().hasLocationBasedEntities(application.getId())) {
            applicationIds.add(application.getId());
         }
      }
      getPlatformServicesConfigurationService().loadLocationApplicationIds(applicationIds);
   }

   /**
    * @return the transactionIdGenerationService
    */
   public TransactionIdGenerationServiceImpl getTransactionIdGenerationService () {
      return transactionIdGenerationService;
   }

   /**
    * @param transactionIdGenerationService the transactionIdGenerationService to set
    */
   public void setTransactionIdGenerationService (TransactionIdGenerationServiceImpl transactionIdGenerationService) {
      this.transactionIdGenerationService = transactionIdGenerationService;
   }

   /**
    * @return the rfIdGenerationService
    */
   public RFIdGenerationServiceImpl getRfIdGenerationService () {
      return rfIdGenerationService;
   }

   /**
    * @param rfIdGenerationService the rfIdGenerationService to set
    */
   public void setRfIdGenerationService (RFIdGenerationServiceImpl rfIdGenerationService) {
      this.rfIdGenerationService = rfIdGenerationService;
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

   /**
    * @return the swiPlatformRetrievalService
    */
   public ISWIPlatformRetrievalService getSwiPlatformRetrievalService () {
      return swiPlatformRetrievalService;
   }

   /**
    * @param swiPlatformRetrievalService the swiPlatformRetrievalService to set
    */
   public void setSwiPlatformRetrievalService (ISWIPlatformRetrievalService swiPlatformRetrievalService) {
      this.swiPlatformRetrievalService = swiPlatformRetrievalService;
   }

   /**
    * @return the platformServicesConfigurationService
    */
   public PlatformServicesConfigurationServiceImpl getPlatformServicesConfigurationService () {
      return platformServicesConfigurationService;
   }

   /**
    * @param platformServicesConfigurationService the platformServicesConfigurationService to set
    */
   public void setPlatformServicesConfigurationService (
            PlatformServicesConfigurationServiceImpl platformServicesConfigurationService) {
      this.platformServicesConfigurationService = platformServicesConfigurationService;
   }

}
