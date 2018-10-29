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


package com.execue.scheduler.configuration.impl;

import java.util.Map;

import com.execue.core.common.type.JobType;
import com.execue.scheduler.configuration.IJobSchedulerConfigurationService;
import com.execue.scheduler.jobdata.ExecueJobData;

public class JobSchedulerConfigurationServiceImpl implements IJobSchedulerConfigurationService {

   private Map<JobType, ExecueJobData> jobDataByJobTypeMap;

   public ExecueJobData getExeCueJobDataByJobType (JobType jobType) {
      return jobDataByJobTypeMap.get(jobType);
   }

   public void loadJobDataByJobTypeMap (Map<JobType, ExecueJobData> jobDataByJobTypeMap) {
      this.jobDataByJobTypeMap = jobDataByJobTypeMap;
   }

}