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

import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.bean.MartFractionalDataSetTempTableStructure;
import com.execue.ac.bean.MartFractionalDatasetTableStructure;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.MartResourceCleanupException;
import com.execue.ac.helper.MartCreationServiceHelper;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.ac.service.IMartOperationalConstants;
import com.execue.ac.service.IMartResourceCleanupService;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.dataaccess.exception.DataAccessException;

/**
 * This service cleans the resources not required after mart preparation.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/01/2011
 */
public class MartResourceCleanupServiceImpl implements IMartResourceCleanupService, IMartOperationalConstants {

   private IAnswersCatalogDataAccessService answersCatalogDataAccessService;
   private MartCreationServiceHelper        martCreationServiceHelper;

   /**
    * This method cleans the temporary resources built while mart creation process
    * 
    * @param martCreationOutputInfo
    * @throws MartResourceCleanupException
    */
   public void cleanupResources (MartCreationOutputInfo martCreationOutputInfo) throws MartResourceCleanupException {
      JobRequest jobRequest = martCreationOutputInfo.getMartCreationInputInfo().getMartCreationContext()
               .getJobRequest();
      Exception exception = null;
      JobOperationalStatus jobOperationalStatus = null;
      try {
         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest, RESOURCE_CLEANUP);
         DataSource targetDataSource = martCreationOutputInfo.getMartCreationInputInfo()
                  .getMartCreationPopulatedContext().getTargetAsset().getDataSource();

         // drop the population table.
         getAnswersCatalogDataAccessService().dropTable(targetDataSource,
                  martCreationOutputInfo.getPopulationTable().getTableName());

         // drop each fractional dataset temp table
         for (MartFractionalDataSetTempTableStructure fractionalDataSetTempTable : martCreationOutputInfo
                  .getFractionalDataSetTempTables()) {
            getAnswersCatalogDataAccessService().dropTable(targetDataSource, fractionalDataSetTempTable.getTableName());
         }

         // drop each fractional dataset table
         for (MartFractionalDatasetTableStructure fractionalDatasetTable : martCreationOutputInfo
                  .getFractionalDataSetTables()) {
            getAnswersCatalogDataAccessService().dropTable(targetDataSource, fractionalDatasetTable.getTableName());
         }

         // drop temp merged population table
         getAnswersCatalogDataAccessService().dropTable(targetDataSource,
                  martCreationOutputInfo.getFractionalPopulationTable().getMergedTempTableName());

         // drop final merged population table
         getAnswersCatalogDataAccessService().dropTable(targetDataSource,
                  martCreationOutputInfo.getFractionalPopulationTable().getMergedTableName());
         getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
      } catch (DataAccessException e) {
         exception = e;
         throw new MartResourceCleanupException(e.getCode(), e);
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new MartResourceCleanupException(e.getCode(), e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getMartCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new MartResourceCleanupException(
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

   public MartCreationServiceHelper getMartCreationServiceHelper () {
      return martCreationServiceHelper;
   }

   public void setMartCreationServiceHelper (MartCreationServiceHelper martCreationServiceHelper) {
      this.martCreationServiceHelper = martCreationServiceHelper;
   }

}
