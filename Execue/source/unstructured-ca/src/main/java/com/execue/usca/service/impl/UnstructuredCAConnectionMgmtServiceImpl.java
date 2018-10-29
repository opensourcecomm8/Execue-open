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


package com.execue.usca.service.impl;

import java.util.List;

import com.execue.dataaccess.exception.DataAccessException;
import com.execue.usca.service.IUnstructuredCAConnectionMgmtService;
import com.execue.uscada.configuration.impl.UnstructuredCAPooledDataManager;

/**
 * @author Vishay
 */
public class UnstructuredCAConnectionMgmtServiceImpl implements IUnstructuredCAConnectionMgmtService {

   private UnstructuredCAPooledDataManager unstructuredCAPooledDataManager;

   public void updatePool (Long appId, List<String> updatedDataSourceNames) throws DataAccessException {
      unstructuredCAPooledDataManager.updatePool(appId, updatedDataSourceNames);
   }

   public void setupPool (Long appId, List<String> dataSourceNames) throws DataAccessException {
      unstructuredCAPooledDataManager.setupPool(appId, dataSourceNames);
   }

   public void deletePool (Long appId) throws DataAccessException {
      unstructuredCAPooledDataManager.deletePool(appId);
   }

   public UnstructuredCAPooledDataManager getUnstructuredCAPooledDataManager () {
      return unstructuredCAPooledDataManager;
   }

   public void setUnstructuredCAPooledDataManager (UnstructuredCAPooledDataManager unstructuredCAPooledDataManager) {
      this.unstructuredCAPooledDataManager = unstructuredCAPooledDataManager;
   }

}
