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


package com.execue.platform.unstructured.content.transporter.impl;

import java.util.List;

import com.execue.core.common.bean.entity.Application;
import com.execue.platform.exception.UnstructuredContentTransporterException;
import com.execue.platform.unstructured.content.transporter.IUnstructuredAppBasedContentTransporter;
import com.execue.platform.unstructured.content.transporter.IUnstructuredContentTransporter;
import com.execue.platform.unstructured.content.transporter.bean.UnstructuredContentTransporterContext;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IApplicationRetrievalService;

public class UnstructuredAppBasedContentTransporterImpl implements IUnstructuredAppBasedContentTransporter {

   private IApplicationRetrievalService    applicationRetrievalService;
   private IUnstructuredContentTransporter unstructuredContentTransporter;

   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

   public IUnstructuredContentTransporter getUnstructuredContentTransporter () {
      return unstructuredContentTransporter;
   }

   public void setUnstructuredContentTransporter (IUnstructuredContentTransporter unstructuredContentTransporter) {
      this.unstructuredContentTransporter = unstructuredContentTransporter;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.transportation.service.IUnstructuredAppBasedContentTransporter#transportContent(java.lang.Long,
    *      java.util.List)
    */
   public void transportContent (Long applicationId,
            List<UnstructuredContentTransporterContext> unstructuredContentTransporterContexts)
            throws UnstructuredContentTransporterException {
      try {
         Application application = getApplicationRetrievalService().getApplicationById(applicationId);
         for (UnstructuredContentTransporterContext unstructuredContentTransporterContext : unstructuredContentTransporterContexts) {
            getUnstructuredContentTransporter().transportContent(application, unstructuredContentTransporterContext);
         }
      } catch (KDXException kdxException) {
         throw new UnstructuredContentTransporterException(kdxException.getCode(), kdxException);
      }
   }

}
