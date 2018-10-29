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

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.MartInputValidatorException;
import com.execue.ac.helper.MartCreationServiceHelper;
import com.execue.ac.service.IMartInputValidatorService;
import com.execue.ac.service.IMartOperationalConstants;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.util.ExecueCoreUtil;

/**
 * This service will validate the mart context input.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartInputValidatorServiceImpl implements IMartInputValidatorService, IMartOperationalConstants {

   private MartCreationServiceHelper martCreationServiceHelper;
   private static final Logger       logger = Logger.getLogger(MartInputValidatorServiceImpl.class);

   public boolean validateMartCreationContext (MartCreationContext martCreationContext)
            throws MartInputValidatorException {
      JobRequest jobRequest = martCreationContext.getJobRequest();
      Exception exception = null;
      JobOperationalStatus jobOperationalStatus = null;
      boolean validationSuccessful = true;
      try {
         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  VALIDATE_MART_CREATION_CONTEXT);
         validationSuccessful = validationSuccessful && martCreationContext.getApplicationId() != null;

         validationSuccessful = validationSuccessful && martCreationContext.getModelId() != null;

         validationSuccessful = validationSuccessful && martCreationContext.getUserId() != null;

         validationSuccessful = validationSuccessful && martCreationContext.getSourceAsset() != null;

         validationSuccessful = validationSuccessful && martCreationContext.getTargetAsset() != null;

         validationSuccessful = validationSuccessful && ExecueCoreUtil.isNotEmpty(martCreationContext.getPopulation());

         validationSuccessful = validationSuccessful
                  && ExecueCoreUtil.isCollectionNotEmpty(martCreationContext.getDistributions());

         validationSuccessful = validationSuccessful
                  && ExecueCoreUtil.isCollectionNotEmpty(martCreationContext.getProminentMeasures());

         validationSuccessful = validationSuccessful
                  && ExecueCoreUtil.isCollectionNotEmpty(martCreationContext.getProminentDimensions());
         if (validationSuccessful) {
            getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
         } else {
            getMartCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                     VALIDATE_MART_CONTEXT_FAILED);
         }
         if (logger.isDebugEnabled()) {
            logger.debug("Result of Validation for mart creation input context " + validationSuccessful);
         }
      } catch (Exception e) {
         exception = e;
         throw new MartInputValidatorException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_INPUT_VALIDATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getMartCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new MartInputValidatorException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return validationSuccessful;
   }

   public MartCreationServiceHelper getMartCreationServiceHelper () {
      return martCreationServiceHelper;
   }

   public void setMartCreationServiceHelper (MartCreationServiceHelper martCreationServiceHelper) {
      this.martCreationServiceHelper = martCreationServiceHelper;
   }

}
