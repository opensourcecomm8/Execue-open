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


package com.execue.ac.service.impl;

import com.execue.ac.bean.CubeCreationOutputInfo;
import com.execue.ac.bean.CubeUpdationInputInfo;
import com.execue.ac.bean.CubeUpdationOutputInfo;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.CubeResourceCleanupException;
import com.execue.ac.helper.CubeCreationServiceHelper;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.ac.service.ICubeOperationalConstants;
import com.execue.ac.service.ICubeResourceCleanupService;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.dataaccess.exception.DataAccessException;

/**
 * This service is used for resource clean up after cube process is successfull.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class CubeResourceCleanupServiceImpl implements ICubeResourceCleanupService, ICubeOperationalConstants {

   private IAnswersCatalogDataAccessService answersCatalogDataAccessService;
   private CubeCreationServiceHelper        cubeCreationServiceHelper;

   /**
    * This method cleans the temporary resources built while cube creation process
    * 
    * @param cubeCreationOutputInfo
    * @throws CubeResourceCleanupException
    */
   public void cleanupResources (CubeCreationOutputInfo cubeCreationOutputInfo) throws CubeResourceCleanupException {
      JobRequest jobRequest = cubeCreationOutputInfo.getCubeCreationInputInfo().getCubeCreationContext()
               .getJobRequest();
      Exception exception = null;
      JobOperationalStatus jobOperationalStatus = null;
      try {
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest, RESOURCE_CLEANUP);
         DataSource targetDataSource = cubeCreationOutputInfo.getCubeCreationInputInfo()
                  .getCubeCreationPopulatedContext().getTargetAsset().getDataSource();

         // drop the cube pre fact table.
         getAnswersCatalogDataAccessService().dropTable(targetDataSource,
                  cubeCreationOutputInfo.getCubePreFactTableStructure().getTableName());

         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
      } catch (DataAccessException e) {
         exception = e;
         throw new CubeResourceCleanupException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_RESOURCE_CLEANUP_EXCEPTION_CODE, e);
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new CubeResourceCleanupException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_RESOURCE_CLEANUP_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubeResourceCleanupException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
   }

   /**
    * This method cleans the temporary resources built while cube updation process
    * 
    * @param cubeUpdationOutputInfo
    * @throws CubeResourceCleanupException
    */
   public void cleanupResources (CubeUpdationOutputInfo cubeUpdationOutputInfo) throws CubeResourceCleanupException {
      CubeUpdationInputInfo cubeUpdationInputInfo = cubeUpdationOutputInfo.getCubeUpdationInputInfo();
      JobRequest jobRequest = cubeUpdationInputInfo.getCubeUpdationContext().getJobRequest();
      Exception exception = null;
      JobOperationalStatus jobOperationalStatus = null;
      try {
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  RESOURCE_CLEANUP_CUBE_UPDATE);
         DataSource targetDataSource = cubeUpdationInputInfo.getCubeUpdationPopulatedContext().getTargetAsset()
                  .getDataSource();

         // drop the cube pre fact table.
         getAnswersCatalogDataAccessService().dropTable(targetDataSource,
                  cubeUpdationOutputInfo.getCubeUpdatePreFactTableStructure().getTableName());

         // drop the cube pre fact temp table which has duplicate records
         getAnswersCatalogDataAccessService().dropTable(targetDataSource,
                  cubeUpdationOutputInfo.getCubeUpdatePreFactTableStructure().getTempFactTableName());

         // drop the cube fact table - which is temp
         getAnswersCatalogDataAccessService().dropTable(targetDataSource,
                  cubeUpdationOutputInfo.getCubeFactTableStructure().getTableName());

         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
      } catch (DataAccessException e) {
         exception = e;
         throw new CubeResourceCleanupException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_RESOURCE_CLEANUP_EXCEPTION_CODE, e);
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new CubeResourceCleanupException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_RESOURCE_CLEANUP_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubeResourceCleanupException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
   }

   public IAnswersCatalogDataAccessService getAnswersCatalogDataAccessService () {
      return answersCatalogDataAccessService;
   }

   public void setAnswersCatalogDataAccessService (IAnswersCatalogDataAccessService answersCatalogDataAccessService) {
      this.answersCatalogDataAccessService = answersCatalogDataAccessService;
   }

   public CubeCreationServiceHelper getCubeCreationServiceHelper () {
      return cubeCreationServiceHelper;
   }

   public void setCubeCreationServiceHelper (CubeCreationServiceHelper cubeCreationServiceHelper) {
      this.cubeCreationServiceHelper = cubeCreationServiceHelper;
   }

}
