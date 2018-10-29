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

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.semantification.batch.service.IGenericArticleRuntimeTablesCleanupService;
import com.execue.semantification.configuration.impl.SemantificationConfigurationImpl;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.service.IUnstructuredWarehouseManagementService;
import com.execue.uswh.service.IUnstructuredWarehouseRetrievalService;

/**
 * @author John Mallavalli
 */
public class GenericArticleRuntimeTablesCleanupServiceImpl implements IGenericArticleRuntimeTablesCleanupService {

   private static final Logger                     log = Logger
                                                                .getLogger(GenericArticleRuntimeTablesCleanupServiceImpl.class);

   private IUnstructuredWarehouseRetrievalService  unstructuredWarehouseRetrievalService;
   private IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService;
   private SemantificationConfigurationImpl        semantificationConfiguration;

   @Override
   public void cleanupRuntimeTables (List<Long> contextIds) {
      for (Long contextId : contextIds) {

         try {
            cleanSemantifiedContentKeywordMatch(contextId);
         } catch (UnstructuredWarehouseException e) {
            log.error("unable to clean up semantifiedContentKeywordMatch ["+contextId+"]", e);
         }
         try {
            cleanUserQueryFeatureInformation(contextId);
         } catch (UnstructuredWarehouseException e) {
            log.error("unable to clean up userQueryFeatureInformation ["+contextId+"]", e);
         }
         try {
            cleanUserQueryLocationInformation(contextId);
         } catch (UnstructuredWarehouseException e) {
            log.error("unable to clean up userQueryLocationInformation ["+contextId+"]", e);
         }
      }
   }

   private void cleanUserQueryLocationInformation (Long contextId) throws UnstructuredWarehouseException {
      Long userQueryLocationInfoMaxExecutionDate = getUnstructuredWarehouseRetrievalService()
               .getUserQueryLocationInfoMaxExecutionDate(contextId);
      if (userQueryLocationInfoMaxExecutionDate != null) {
         Long maintainUserQueryLocationInfoExecutionTime = getSemantificationConfiguration()
                  .getUserQueryLocationInfoExecutionTime();
         getUnstructuredWarehouseManagementService().deleteUserQuerLocationInfoByExecutionDate(contextId,
                  userQueryLocationInfoMaxExecutionDate - maintainUserQueryLocationInfoExecutionTime);
      }
   }

   private void cleanUserQueryFeatureInformation (Long contextId) throws UnstructuredWarehouseException {
      Long userQueryFeatureInfoMaxExecutionDate = getUnstructuredWarehouseRetrievalService()
               .getUserQueryFeatureInfoMaxExecutionDate(contextId);
      if (userQueryFeatureInfoMaxExecutionDate != null) {
         Long maintainUserQueryFeatureInfoExecutionTime = getSemantificationConfiguration()
                  .getUserQueryFeatureInfoExecutionTime();
         getUnstructuredWarehouseManagementService().deleteUserQuerFeatureInfoByExecutionDate(contextId,
                  userQueryFeatureInfoMaxExecutionDate - maintainUserQueryFeatureInfoExecutionTime);
      }
   }

   private void cleanSemantifiedContentKeywordMatch (Long contextId) throws UnstructuredWarehouseException {
      Long semantifiedContentKeywordMatchMaxExecutionDate = getUnstructuredWarehouseRetrievalService()
               .getSemantifiedConentKeywordMatchMaxExecutionDate(contextId);
      if (semantifiedContentKeywordMatchMaxExecutionDate != null) {
         Long maintainSemantifiedContentKeywordMatchExecutionTime = getSemantificationConfiguration()
                  .getSemantifiedContentKeywordMatchExecutionTime();
         getUnstructuredWarehouseManagementService().deleteSemantifiedContentKeyWordMatchByExecutionDate(contextId,
                  semantifiedContentKeywordMatchMaxExecutionDate - maintainSemantifiedContentKeywordMatchExecutionTime);
      }
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
    * @return the unstructuredWarehouseRetrievalService
    */
   public IUnstructuredWarehouseRetrievalService getUnstructuredWarehouseRetrievalService () {
      return unstructuredWarehouseRetrievalService;
   }

   /**
    * @param unstructuredWarehouseRetrievalService the unstructuredWarehouseRetrievalService to set
    */
   public void setUnstructuredWarehouseRetrievalService (
            IUnstructuredWarehouseRetrievalService unstructuredWarehouseRetrievalService) {
      this.unstructuredWarehouseRetrievalService = unstructuredWarehouseRetrievalService;
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

   public static void main (String[] args) {
      Date dt = GregorianCalendar.getInstance().getTime();
      dt = new Date(System.currentTimeMillis());
      Date dt2 = new Date(System.currentTimeMillis() - 90000);
      System.out.println(dt);
      System.out.println(dt2);
   }

}
