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


package com.execue.platform.swi;

import com.execue.core.common.bean.batchMaintenance.InstanceAbsorptionContext;
import com.execue.core.common.bean.batchMaintenance.MemberAbsorptionContext;
import com.execue.swi.exception.BatchMaintenanceException;

/**
 * Service interface for performing batch maintenance on SDX entities
 * 
 * @author Raju Gottumukkala
 */
public interface ISDXBatchMaintenanceService {

   /**
    * For the provided asset and table, absorb the members from source in batch. 1. Batch size is taken from
    * core-configuration 2. call absorbMembersByBatch on sourceMetaDataService 3. save the members through sdxService 4.
    * loop over steps 2 & 3 till all the members are absorbed
    * 
    * @param asset
    * @param table
    * @throws BatchMaintenanceException
    */
   public void absorbMembersInBatches (MemberAbsorptionContext memberAbsorptionContext)
            throws BatchMaintenanceException;

   public void absorbInstanceInBatches (InstanceAbsorptionContext instanceAbsorptionContext)
            throws BatchMaintenanceException;

   public void createBatchProcess (MemberAbsorptionContext memberAbsorptionContext) throws BatchMaintenanceException;

   public void deleteBatchProcess (MemberAbsorptionContext memberAbsorptionContext) throws BatchMaintenanceException;
}
