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


package com.execue.swi.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.exception.ExeCueException;
import com.execue.dataaccess.configuration.PooledDataSourceManager;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.service.IDataSourceConnectionManagementService;

public class DataSourceConnectionManagementServiceImpl implements IDataSourceConnectionManagementService {

   private PooledDataSourceManager pooledDataSourceManager;

   public boolean doesConnectionExist (DataSource execueDataSource) throws ExeCueException {
      return getPooledDataSourceManager().doesConnectionExist(execueDataSource);
   }

   public void removeDataSource (DataSource execueDataSource) throws DataAccessException {
      pooledDataSourceManager.removeDataSource(execueDataSource);
   }

   public void setupDataSource (DataSource execueDataSource) throws DataAccessException {
      List<DataSource> dataSources = new ArrayList<DataSource>();
      dataSources.add(execueDataSource);
      pooledDataSourceManager.setupDataSources(dataSources);
   }

   public PooledDataSourceManager getPooledDataSourceManager () {
      return pooledDataSourceManager;
   }

   public void setPooledDataSourceManager (PooledDataSourceManager pooledDataSourceManager) {
      this.pooledDataSourceManager = pooledDataSourceManager;
   }

}
