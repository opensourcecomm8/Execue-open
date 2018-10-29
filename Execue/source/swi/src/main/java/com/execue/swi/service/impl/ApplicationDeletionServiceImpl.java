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

import java.util.List;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.ApplicationDetail;
import com.execue.core.common.bean.entity.ApplicationExample;
import com.execue.core.common.bean.entity.ApplicationOperation;
import com.execue.swi.exception.SWIException;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.service.IApplicationDeletionService;

public class ApplicationDeletionServiceImpl implements IApplicationDeletionService {

   private IKDXDataAccessManager kdxDataAccessManager;

   public void deleteApplicationImage (ApplicationDetail applicationDetail) throws SWIException {
      getKdxDataAccessManager().deleteApplicationImage(applicationDetail);
   }

   public void deleteApplicationExample (ApplicationExample applicationExample) throws SWIException {
      getKdxDataAccessManager().deleteApplicationExample(applicationExample);

   }

   public void deleteApplicationExamples (List<ApplicationExample> applicationExamples) throws SWIException {
      getKdxDataAccessManager().deleteApplicationExamples(applicationExamples);

   }

   public void deleteApplicationOperationData () throws SWIException {
      getKdxDataAccessManager().cleanApplicationOperationData();
   }

   public void deleteApplicationOperations (List<ApplicationOperation> applicationOperations) throws SWIException {
      getKdxDataAccessManager().deleteApplicationOperations(applicationOperations);
   }

   public void deleteApplication (Application application) throws SWIException {
      getKdxDataAccessManager().cleanApplication(application);
   }

   public IKDXDataAccessManager getKdxDataAccessManager () {
      return kdxDataAccessManager;
   }

   public void setKdxDataAccessManager (IKDXDataAccessManager kdxDataAccessManager) {
      this.kdxDataAccessManager = kdxDataAccessManager;
   }

}
