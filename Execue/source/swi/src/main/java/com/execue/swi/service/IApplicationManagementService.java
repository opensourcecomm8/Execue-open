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

import java.io.File;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.ApplicationDetail;
import com.execue.core.common.bean.entity.ApplicationExample;
import com.execue.core.common.bean.entity.UnstructuredApplicationDetail;
import com.execue.core.common.type.AppOperationType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.exception.ExeCueException;
import com.execue.swi.exception.KDXException;

public interface IApplicationManagementService {

   public void updateApplicationPublishAssetMode (Long applicationId, PublishAssetMode publishMode) throws KDXException;

   public void updateApplicationOperationDetails (Long applicationId, AppOperationType operationType,
            Long jobRequestId, JobStatus operationStatus) throws KDXException;

   public void absorbApplicationImage (Long applicationId, File imageFile, String sourceDataFileName, String imageType)
            throws ExeCueException;

   public ApplicationExample createApplicationExample (ApplicationExample applicationExample) throws KDXException;

   public void updateApplicationExample (ApplicationExample applicationExample) throws KDXException;

   public void createApplication (Application application) throws KDXException;

   public void createApplicationImage (ApplicationDetail applicationImage) throws KDXException;

   public void updateApplicationImage (ApplicationDetail applicationImage) throws KDXException;

   public void updateApplication (Application application) throws KDXException;

   public void createUnstructuredApplicationDetail (UnstructuredApplicationDetail unstructuredApplicationDetail)
            throws KDXException;

   public void updateUnstructuredApplicationDetail (UnstructuredApplicationDetail unstructuredApplicationDetail)
            throws KDXException;

}
