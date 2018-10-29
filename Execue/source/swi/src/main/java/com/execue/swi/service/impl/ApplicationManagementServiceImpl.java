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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.io.FileUtils;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.ApplicationDetail;
import com.execue.core.common.bean.entity.ApplicationExample;
import com.execue.core.common.bean.entity.UnstructuredApplicationDetail;
import com.execue.core.common.type.AppOperationType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IApplicationManagementService;
import com.execue.swi.service.IApplicationRetrievalService;

public class ApplicationManagementServiceImpl implements IApplicationManagementService {

   private IKDXDataAccessManager        kdxDataAccessManager;
   private IApplicationRetrievalService applicationRetrievalService;
   private ICoreConfigurationService    coreConfigurationService;

   public void updateApplicationPublishAssetMode (Long applicationId, PublishAssetMode publishMode) throws KDXException {
      Application application = getApplicationRetrievalService().getApplicationById(applicationId);
      application.setPublishMode(publishMode);
      getKdxDataAccessManager().updateApplication(application);
   }

   public void absorbApplicationImage (Long applicationId, File imageFile, String sourceDataFileName, String imageType)
            throws ExeCueException {
      ApplicationDetail applicationImage = new ApplicationDetail();
      FileInputStream fileInputStream = null;
      Blob image = null;
      try {
         
         byte[] imageFileByteArray = FileUtils.readFileToByteArray(imageFile);
         image = new SerialBlob(imageFileByteArray);
         
         // fileInputStream = new FileInputStream(imageFile);
         // image = Hibernate.createBlob(fileInputStream);
         
         applicationImage.setApplicationId(applicationId);
         applicationImage.setImageData(image);
         applicationImage.setImageName(sourceDataFileName);
         applicationImage.setImageType(imageType);
         ApplicationDetail existingApplicationImage = getKdxDataAccessManager().getImageByApplicationId(applicationId);
         if (existingApplicationImage == null) {
            getKdxDataAccessManager().createApplicationImage(applicationImage);
         } else {
            applicationImage.setId(existingApplicationImage.getId());
            getKdxDataAccessManager().updateApplicationImage(applicationImage);
         }
      } catch (FileNotFoundException fileNotFoundException) {
         throw new KDXException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, fileNotFoundException.getMessage());
      } catch (IOException ioException) {
         throw new KDXException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, ioException.getMessage());
      } catch (SerialException serialException) {
         throw new KDXException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, serialException.getMessage());
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public ApplicationExample createApplicationExample (ApplicationExample applicationExample) throws KDXException {
      handleExampleQueryNameLength(applicationExample);
      return getKdxDataAccessManager().createApplicationExample(applicationExample);
   }

   public void updateApplicationExample (ApplicationExample applicationExample) throws KDXException {
      handleExampleQueryNameLength(applicationExample);
      getKdxDataAccessManager().updateApplicationExample(applicationExample);
   }

   private void handleExampleQueryNameLength (ApplicationExample applicationExample) {
      if (applicationExample.getQueryName().length() > 255) {
         applicationExample.setQueryName(applicationExample.getQueryName().substring(0, 250));
         applicationExample.setQueryName(applicationExample.getQueryName() + " ..");
      }
   }

   public void createApplication (Application application) throws KDXException {

      // NOTE: -RG- set the applicationKey attribute as the name of the application by default
      // This attribute should never be over-ridden
      String applicationKey = application.getName();
      applicationKey = applicationKey.replaceAll(" ", "_");
      application.setApplicationKey(applicationKey);
      
      application.setApplicationURL(getCoreConfigurationService().getApplicationSearchPageURL());

      getKdxDataAccessManager().createApplication(application);
      
      application.setApplicationURL(application.getApplicationURL() + application.getId());
      getKdxDataAccessManager().updateApplication(application);
   }

   public void createApplicationImage (ApplicationDetail applicationImage) throws KDXException {
      getKdxDataAccessManager().createApplicationImage(applicationImage);

   }

   public void updateApplicationImage (ApplicationDetail applicationImage) throws KDXException {
      getKdxDataAccessManager().updateApplicationImage(applicationImage);

   }

   public void updateApplication (Application application) throws KDXException {
      getKdxDataAccessManager().updateApplication(application);
   }

   public void updateApplicationOperationDetails (Long applicationId, AppOperationType operationType,
            Long jobRequestId, JobStatus operationStatus) throws KDXException {
      getKdxDataAccessManager().updateApplicationOperationDetails(applicationId, operationType, jobRequestId,
               operationStatus);
   }

   @Override
   public void createUnstructuredApplicationDetail (UnstructuredApplicationDetail unstructuredApplicationDetail)
            throws KDXException {
      getKdxDataAccessManager().createUnstructuredApplicationDetail(unstructuredApplicationDetail);
   }

   @Override
   public void updateUnstructuredApplicationDetail (UnstructuredApplicationDetail unstructuredApplicationDetail)
            throws KDXException {
      getKdxDataAccessManager().updateUnstructuredApplicationDetail(unstructuredApplicationDetail);

   }

   public IKDXDataAccessManager getKdxDataAccessManager () {
      return kdxDataAccessManager;
   }

   public void setKdxDataAccessManager (IKDXDataAccessManager kdxDataAccessManager) {
      this.kdxDataAccessManager = kdxDataAccessManager;
   }

   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}
