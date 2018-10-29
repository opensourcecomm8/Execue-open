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
import com.execue.platform.unstructured.content.transporter.bean.UnstructuredContentTransporterContext;

public interface IUnstructuredAppBasedContentTransporter {

   /**
    * For the application id provided, transfer the content 
    *   from all sources to target warehouse (temp table only)
    * 
    * Get the application by id provided
    * on each entry of contextList,
    *   invoke the source level content transporter
    * 
    * @param applicationId
    * @param unstructuredContentTransporterContexts
    * @throws UnstructuredContentTransporterException
    */
   public void transportContent (Long applicationId,
            List<UnstructuredContentTransporterContext> unstructuredContentTransporterContexts)
            throws UnstructuredContentTransporterException;
}
