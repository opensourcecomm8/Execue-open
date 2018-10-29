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


package com.execue.scheduler;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.swi.BusinessModelPreparationContext;
import com.execue.scheduler.service.IBusinessModelPreparationJobService;

/**
 * 
 * @author jitendra
 *
 */
public abstract class JobSchedulerCommonBaseTest extends JobSchedulerBaseTest {

   protected IBusinessModelPreparationJobService getBusinessModelPreparationJobService () {
      return (IBusinessModelPreparationJobService) getContext().getBean("businessModelPreparationJobService");
   }

   protected BusinessModelPreparationContext getBusinessModelPrepartionContext () {
      BusinessModelPreparationContext businessModelPreparationContext = new BusinessModelPreparationContext();
      businessModelPreparationContext.setApplicationId(101L);
      businessModelPreparationContext.setModelId(101L);
      businessModelPreparationContext.setUserId(1L);
      List<Long> assetIds = new ArrayList<Long>();
      assetIds.add(1L);
      businessModelPreparationContext.setSelectedAssetIds(assetIds);

      return businessModelPreparationContext;
   }

}
