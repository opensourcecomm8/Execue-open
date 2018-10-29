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


package com.execue.swi.service;

import com.execue.core.common.bean.entity.DataSource;
import com.execue.swi.exception.SWIException;

/**
 * Provides the Data Source that needs to be used either, while uploading a data-set or for creating catalog based on
 * the application for which the request is made Selection is based on the algorithm below., Algorithm : Prerequisites :
 * 1. Number of Data Sources are created up front in the system for both types (Uploaded and Catalog) Goal : To choose a
 * Data Source which is least loaded Flow : (Is good for both both the types Catalog and Uploaded) 1. If there are any
 * Data Sources with no data-set collections attached to them (ordered by it's id) then choose the first one else get
 * the Data Source that is attached to minimum number of data-set collections
 * 
 * @author execue
 */
public interface IDataSourceSelectionService {

   /**
    * Provides the least loaded Data Source of type UPLOADED
    * 
    * @param userId
    * @param applicationId
    * @return
    * @throws SWIException
    */
   public DataSource getDataSourceForUploadedDatasets (Long userId) throws SWIException;

   /**
    * Provides the least loaded Data Source of type CATALOG
    * 
    * @param applicationId
    * @return
    * @throws SWIException
    */
   public DataSource getDataSourceForCatalogDatasets () throws SWIException;

}
