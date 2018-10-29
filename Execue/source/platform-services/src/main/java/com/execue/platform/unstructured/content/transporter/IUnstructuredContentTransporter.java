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


package com.execue.platform.unstructured.content.transporter;

import com.execue.core.common.bean.entity.Application;
import com.execue.platform.exception.UnstructuredContentTransporterException;
import com.execue.platform.unstructured.content.transporter.bean.UnstructuredContentTransporterContext;

public interface IUnstructuredContentTransporter {

   /**
    * For the context and application provided transfers the latest content 
    *   to target warehouse (temp table only) 
    *   from the source content by the application id as category id
    * 
    * Check if the temp table exists in target ware house
    *   if doesn't exist create the temp table
    *   if already present then truncate the temp table
    * From the target warehouse source content, get the latest content id for 
    *   the application id and the source node id (source data source id)
    * Prepare the query to pull the data from source with conditions 
    *   on category id (application id)
    *   on content entry id greater than the latest content entry id pulled earlier
    * Prepare the insert statement to the temp source content table
    * Prepare the ETL context with
    *   target warehouse data source details
    *   source content data source details
    *   select query (prepared above)
    *   insert statement (prepared above)
    *  invoke the ETL process
    *  capture the number of records transferred
    *  Log the information with 
    *    target warehouse data source id, 
    *    source content data source id,
    *    application id,
    *    number of records transferred
    *
    * @param application
    * @param unstructuredContentTransporterContext
    * @throws UnstructuredContentTransporterException
    */
   public void transportContent (Application application,
            UnstructuredContentTransporterContext unstructuredContentTransporterContext)
            throws UnstructuredContentTransporterException;
}
