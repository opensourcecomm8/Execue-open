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

import java.util.List;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.ApplicationDetail;
import com.execue.core.common.bean.entity.ApplicationExample;
import com.execue.core.common.bean.entity.ApplicationOperation;
import com.execue.swi.exception.SWIException;

public interface IApplicationDeletionService {

   public void deleteApplicationExample (ApplicationExample applicationExample) throws SWIException;

   public void deleteApplicationOperationData () throws SWIException;

   public void deleteApplicationImage (ApplicationDetail applicationDetail) throws SWIException;

   public void deleteApplicationExamples (List<ApplicationExample> applicationExamples) throws SWIException;

   public void deleteApplicationOperations (List<ApplicationOperation> applicationOperations) throws SWIException;

   public void deleteApplication (Application application) throws SWIException;

}
