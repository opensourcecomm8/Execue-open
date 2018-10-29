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


package com.execue.platform.swi.impl;

import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.swi.DefaultMetricsMaintenanceContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.swi.IDefaultMetricPopulationService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IDefaultMetricService;
import com.execue.swi.service.ISDXRetrievalService;

public class DefaultMetricPopulationServiceImpl implements IDefaultMetricPopulationService {

   private IJobDataService              jobDataService;
   private IApplicationRetrievalService applicationRetrievalService;
   private ISDXRetrievalService         sdxRetrievalService;
   private IDefaultMetricService        defaultMetricService;

   public void populateDefaultMetrics (DefaultMetricsMaintenanceContext defaultMetricsMaintenanceContext)
            throws SWIException {
      JobOperationalStatus jobOperationalStatus = null;
      try {
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(defaultMetricsMaintenanceContext
                  .getJobRequest(), "Processing Default Metrics for asset", JobStatus.INPROGRESS, null, new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);
         List<Application> allApplications = getApplicationRetrievalService().getAllApplications();
         if (ExecueCoreUtil.isCollectionNotEmpty(allApplications)) {
            for (Application application : allApplications) {
               List<Long> assetIds = getSdxRetrievalService().getAllAssetIds(application.getId());
               if (ExecueCoreUtil.isCollectionNotEmpty(assetIds)) {
                  for (Long assetId : assetIds) {
                     getDefaultMetricService().populateDefaultMetricsAsset(assetId);
                  }
               }
            }
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
      } catch (ExeCueException exception) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                     .getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new MappingException(SWIExceptionCodes.DEFAULT_METRICS_POPULATION_FAILURE_CODE, exception);
            }
         }
         throw new MappingException(SWIExceptionCodes.DEFAULT_METRICS_POPULATION_FAILURE_CODE, exception);
      }
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IDefaultMetricService getDefaultMetricService () {
      return defaultMetricService;
   }

   public void setDefaultMetricService (IDefaultMetricService defaultMetricService) {
      this.defaultMetricService = defaultMetricService;
   }
}
