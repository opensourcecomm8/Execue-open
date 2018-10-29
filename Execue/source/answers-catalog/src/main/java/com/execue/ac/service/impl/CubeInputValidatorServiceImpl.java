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

import org.apache.log4j.Logger;

import com.execue.ac.bean.CubeUpdationContext;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.CubeInputValidatorException;
import com.execue.ac.helper.CubeCreationServiceHelper;
import com.execue.ac.service.ICubeInputValidatorService;
import com.execue.ac.service.ICubeOperationalConstants;
import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.util.ExecueCoreUtil;

/**
 * This service is used for validating the cube creation input.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class CubeInputValidatorServiceImpl implements ICubeInputValidatorService, ICubeOperationalConstants {

   private CubeCreationServiceHelper cubeCreationServiceHelper;
   private static final Logger       logger = Logger.getLogger(CubeInputValidatorServiceImpl.class);

   public boolean validateCubeCreationContext (CubeCreationContext cubeCreationContext)
            throws CubeInputValidatorException {
      boolean validationSuccessful = true;
      JobRequest jobRequest = cubeCreationContext.getJobRequest();
      Exception exception = null;
      JobOperationalStatus jobOperationalStatus = null;
      try {
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  VALIDATE_CUBE_CREATION_CONTEXT);
         validationSuccessful = isCubeCreationContextValid(cubeCreationContext);

         if (logger.isDebugEnabled()) {
            logger.debug("Result of Validation for cube creation input context " + validationSuccessful);
         }
         if (validationSuccessful) {
            getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
         } else {
            getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                     VALIDATE_CUBE_CONTEXT_FAILED);
         }

      } catch (Exception e) {
         exception = e;
         throw new CubeInputValidatorException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_INPUT_VALIDATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubeInputValidatorException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return validationSuccessful;
   }

   public CubeCreationServiceHelper getCubeCreationServiceHelper () {
      return cubeCreationServiceHelper;
   }

   public void setCubeCreationServiceHelper (CubeCreationServiceHelper cubeCreationServiceHelper) {
      this.cubeCreationServiceHelper = cubeCreationServiceHelper;
   }

   @Override
   public boolean validateCubeUpdationContext (CubeUpdationContext cubeUpdationContext)
            throws CubeInputValidatorException {
      boolean validationSuccessful = true;
      CubeCreationContext cubeCreationContext = (CubeCreationContext) cubeUpdationContext;
      JobRequest jobRequest = cubeUpdationContext.getJobRequest();
      Exception exception = null;
      JobOperationalStatus jobOperationalStatus = null;
      try {
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  VALIDATE_CUBE_UPDATION_CONTEXT);

         validationSuccessful = isCubeCreationContextValid(cubeCreationContext);
         // at least one dimension need to be updated
         validationSuccessful = validationSuccessful
                  && (cubeUpdationContext.getCubeUpdationDimensionInfoList().size() > 0);

         if (logger.isDebugEnabled()) {
            logger.debug("Result of Validation for cube updation input context " + validationSuccessful);
         }

         if (validationSuccessful) {
            getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
         } else {
            getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                     VALIDATE_CUBE_CONTEXT_FAILED);
         }

      } catch (Exception e) {
         exception = e;
         throw new CubeInputValidatorException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_INPUT_VALIDATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubeInputValidatorException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return validationSuccessful;
   }

   private boolean isCubeCreationContextValid (CubeCreationContext cubeCreationContext) {
      boolean validationSuccessful = true;
      validationSuccessful = validationSuccessful && cubeCreationContext.getModelId() != null;

      validationSuccessful = validationSuccessful && cubeCreationContext.getSourceAsset() != null;

      validationSuccessful = validationSuccessful && cubeCreationContext.getTargetAsset() != null;

      validationSuccessful = validationSuccessful && cubeCreationContext.getApplicationId() != null;

      validationSuccessful = validationSuccessful && cubeCreationContext.getUserId() != null;

      validationSuccessful = validationSuccessful
               && ExecueCoreUtil.isCollectionNotEmpty(cubeCreationContext.getMeasures());

      return validationSuccessful;
   }

}
