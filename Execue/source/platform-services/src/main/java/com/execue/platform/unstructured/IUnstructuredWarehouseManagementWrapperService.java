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


package com.execue.platform.unstructured;

import java.util.List;

import com.execue.core.common.bean.swi.UnstructuredWarehouseContext;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.OperationType;
import com.execue.platform.exception.PlatformException;

/**
 * @author Vishay
 */
public interface IUnstructuredWarehouseManagementWrapperService {

   public void manageUnstructuredWarehouse (UnstructuredWarehouseContext unstructuredWarehouseContext)
            throws PlatformException;

   public void deleteApplicationInfo (Long applicationId) throws PlatformException;

   public void manageUnstructuredWarehouse (Long appId, Long bedId, Long parentBedId, OperationType operationType)
            throws PlatformException;

   public void manageUnstructuredWarehouse (Long appId, List<Long> bedIds, Long parentBedId, OperationType operationType)
            throws PlatformException;
   
   public void updateImagePresentForFacets (Long appId, Long semantifiedContentId, CheckType imagePresent) throws PlatformException;

}
