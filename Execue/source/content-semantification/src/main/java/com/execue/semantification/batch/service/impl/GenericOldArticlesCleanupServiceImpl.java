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


package com.execue.semantification.batch.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.exception.SystemException;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.unstructured.IUnstructuredFacetManagementService;
import com.execue.semantification.batch.service.IGenericOldArticlesCleanupService;
import com.execue.semantification.configuration.impl.SemantificationConfigurationImpl;
import com.execue.usca.exception.UnstructuredCAException;
import com.execue.usca.service.IUnstructuredCAManagementService;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.service.IUnstructuredWarehouseManagementService;

/**
 * @author Nitesh
 */
public class GenericOldArticlesCleanupServiceImpl implements IGenericOldArticlesCleanupService {

   private static final Logger                     log = Logger.getLogger(GenericOldArticlesCleanupServiceImpl.class);

   private SemantificationConfigurationImpl        semantificationConfiguration;
   private IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService;
   private IUnstructuredFacetManagementService     unstructuredFacetManagementService;
   private IUnstructuredCAManagementService        unstructuredCAManagementService;

   @Override
   public void cleanupOldArticlesTrace (List<Long> contextIds) {
      int oldArticleCleanupDays = getSemantificationConfiguration().getSemantificationArticleCleanupDays();
      Date contentDate = getCleanupDate(oldArticleCleanupDays);
      for (Long contextId : contextIds) {
         try {
            getUnstructuredWarehouseManagementService().deleteSourceContentByContentDate(contextId, contentDate);
            getUnstructuredWarehouseManagementService().deleteSemantifiedContentByContentDate(contextId, contentDate);
            getUnstructuredWarehouseManagementService().deleteSemantifiedContentFeatureInfoByContentDate(contextId,
                     contentDate);
            getUnstructuredWarehouseManagementService().deleteSemantifiedContentKeywordByContentDate(contextId,
                     contentDate);
            getUnstructuredFacetManagementService().cleanUpOldFacets(contextId, contentDate);
            getUnstructuredCAManagementService().deleteLinksPriorToPublishedDate(contextId, contentDate);
         } catch (UnstructuredWarehouseException unstructuredWarehouseException) {
            throw new SystemException(unstructuredWarehouseException.getCode(), unstructuredWarehouseException);
         } catch (PlatformException e) {
            throw new SystemException(e.getCode(), e);
         } catch (UnstructuredCAException uscaException) {
            throw new SystemException(uscaException.getCode(), uscaException);
         }
      }
   }

   private Date getCleanupDate (int newsItemCleanupDays) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -newsItemCleanupDays);
      return cal.getTime();
   }

   /**
    * @return the semantificationConfiguration
    */
   public SemantificationConfigurationImpl getSemantificationConfiguration () {
      return semantificationConfiguration;
   }

   /**
    * @param semantificationConfiguration the semantificationConfiguration to set
    */
   public void setSemantificationConfiguration (SemantificationConfigurationImpl semantificationConfiguration) {
      this.semantificationConfiguration = semantificationConfiguration;
   }

   /**
    * @return the unstructuredWarehouseManagementService
    */
   public IUnstructuredWarehouseManagementService getUnstructuredWarehouseManagementService () {
      return unstructuredWarehouseManagementService;
   }

   /**
    * @param unstructuredWarehouseManagementService the unstructuredWarehouseManagementService to set
    */
   public void setUnstructuredWarehouseManagementService (
            IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService) {
      this.unstructuredWarehouseManagementService = unstructuredWarehouseManagementService;
   }

   /**
    * @return the unstructuredFacetManagementService
    */
   public IUnstructuredFacetManagementService getUnstructuredFacetManagementService () {
      return unstructuredFacetManagementService;
   }

   /**
    * @param unstructuredFacetManagementService the unstructuredFacetManagementService to set
    */
   public void setUnstructuredFacetManagementService (
            IUnstructuredFacetManagementService unstructuredFacetManagementService) {
      this.unstructuredFacetManagementService = unstructuredFacetManagementService;
   }

   public IUnstructuredCAManagementService getUnstructuredCAManagementService () {
      return unstructuredCAManagementService;
   }

   public void setUnstructuredCAManagementService (IUnstructuredCAManagementService unstructuredCAManagementService) {
      this.unstructuredCAManagementService = unstructuredCAManagementService;
   }

}