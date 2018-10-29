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


package com.execue.driver.configuration.impl;

import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.VerticalEntityBasedRedirection;
import com.execue.core.configuration.IConfigurable;
import com.execue.core.exception.ConfigurationException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ILookupService;

public class DriverConfigurableService implements IConfigurable {

   private ILookupService                 lookupService;
   private IKDXRetrievalService           kdxRetrievalService;
   private DriverConfigurationServiceImpl driverConfigurationService;
   private Logger                         log = Logger.getLogger(DriverConfigurableService.class);

   @Override
   public void doConfigure () throws ConfigurationException {
      try {

         Map<Long, VerticalEntityBasedRedirection> verticalBasedRedirectionMap = getLookupService()
                  .getVerticalEntityBasedRedirectionMap();
         //load vertical based redirection map
         getDriverConfigurationService().loadVerticalBasedRedirectionMap(verticalBasedRedirectionMap);
         for (VerticalEntityBasedRedirection verticalEntityBasedRedirection : verticalBasedRedirectionMap.values()) {
            Long bedId = verticalEntityBasedRedirection.getBusinessEntityId();
            try {
               verticalEntityBasedRedirection.setEntityId(getKdxRetrievalService().getBusinessEntityDefinitionById(
                        bedId).getConcept().getId());
            } catch (KDXException kdxException) {
               if (DataAccessExceptionCodes.ENTITY_NOT_FOUND_CODE == kdxException.getCode()) {
                  log.warn("VerticalEntityBasedRedirection configuration is invalid for BED ID [" + bedId + "]");
               } else {
                  throw kdxException;
               }
            }
         }
         // load App business entity map
         getDriverConfigurationService().loadAppBusinessEntityIdMap(getLookupService().getAppBusinessEntityMap());
         // load App business entity map
         getDriverConfigurationService().loadUniqueAppPossiblilityRedirectionMap();

      } catch (SWIException swiException) {
         throw new ConfigurationException(swiException.getCode(), swiException);
      }
   }

   @Override
   public void reConfigure () throws ConfigurationException {
      // TODO Auto-generated method stub

   }

   /**
    * @return the lookupService
    */
   public ILookupService getLookupService () {
      return lookupService;
   }

   /**
    * @param lookupService the lookupService to set
    */
   public void setLookupService (ILookupService lookupService) {
      this.lookupService = lookupService;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the driverConfigurationService
    */
   public DriverConfigurationServiceImpl getDriverConfigurationService () {
      return driverConfigurationService;
   }

   /**
    * @param driverConfigurationService the driverConfigurationService to set
    */
   public void setDriverConfigurationService (DriverConfigurationServiceImpl driverConfigurationService) {
      this.driverConfigurationService = driverConfigurationService;
   }

}
