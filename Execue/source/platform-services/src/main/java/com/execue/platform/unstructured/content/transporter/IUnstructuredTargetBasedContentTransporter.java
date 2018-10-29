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

import java.util.List;

import com.execue.platform.exception.UnstructuredContentTransporterException;

/**
 * Transports the content from Content Aggregator to Unstructured Warehouse
 * 
 * @author Raju Gottumukkala
 *
 */
public interface IUnstructuredTargetBasedContentTransporter {

   /**
    * On each target warehouse, per each configured application for the warehouse and 
    * per each of the content sources (aggregator) configured for the application, 
    * transfers the content to the warehouse.
    * 
    * MAJOR ASSUMPTION : Target Warehouse configured in one node should 
    *   not be configured in any other node where in this transporter is invoked
    * 
    * On each of the warehouse,
    *   pull the data source for target ware house (USWH)
    *   collect all the applications configured for the warehouse from app data source mapping
    *   build the context UnstructuredTargetBasedContentTransporterContext
    *   on each application
    *     collect all the source data sources configured for the application from app data source mapping (USCA)
    *     for each of the content source data source
    *       build a temp table name as 
    *         <warehouse data source id>_<application id>_<content source data source id>_content_temp
    *       trim the temp table name to the max number of characters defined for table name
    *       build the context UnstructuredContentTransporterContext
    *       add this context to the list of contexts per application
    *     add the list of contexts to the app context map of application id against the list of context
    *   Once the app context map is ready,
    *   on each entry of the map 
    *     invoke the app based content transporter
    *       which in-turn calls the source content transporter and gets the data to it's temp source content table
    *   once all the map entries are done,
    *   loop over all the low level contexts and then transport the data from temp tables to main source content table
    *     on each context, drop the temp table
    *
    * @param targetWareHouseDataSourceIds
    * @throws UnstructuredContentTransporterException
    */
   public void transportContent (List<Long> targetWareHouseDataSourceIds)
            throws UnstructuredContentTransporterException;
}
