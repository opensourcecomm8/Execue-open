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

import com.execue.ac.bean.CubeFactTableStructure;
import com.execue.ac.bean.CubeUpdatePreFactTableStructure;
import com.execue.ac.bean.CubeUpdationContext;
import com.execue.ac.bean.CubeUpdationInputInfo;
import com.execue.ac.bean.CubeUpdationOutputInfo;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.CubeAssetExtractionException;
import com.execue.ac.exception.CubeContextPopulationException;
import com.execue.ac.exception.CubeFactPopulationException;
import com.execue.ac.exception.CubeInputValidatorException;
import com.execue.ac.exception.CubeLookupPopulationException;
import com.execue.ac.exception.CubePreFactPopulationException;
import com.execue.ac.exception.CubeResourceCleanupException;
import com.execue.ac.exception.CubeUpdationException;
import com.execue.ac.service.ICubeAssetExtractionService;
import com.execue.ac.service.ICubeContextPopulationService;
import com.execue.ac.service.ICubeFactPopulationService;
import com.execue.ac.service.ICubeInputValidatorService;
import com.execue.ac.service.ICubeLookupPopulationService;
import com.execue.ac.service.ICubePreFactPopulationService;
import com.execue.ac.service.ICubeResourceCleanupService;
import com.execue.ac.service.ICubeUpdationService;

/**
 * @author Vishay
 */
public class CubeUpdationServiceImpl implements ICubeUpdationService {

   private ICubeInputValidatorService    cubeInputValidatorService;

   private ICubeContextPopulationService cubeContextPopulationService;

   private ICubePreFactPopulationService cubePreFactPopulationService;

   private ICubeFactPopulationService    cubeFactPopulationService;

   private ICubeLookupPopulationService  cubeLookupPopulationService;

   private ICubeResourceCleanupService   cubeResourceCleanupService;

   private ICubeAssetExtractionService   cubeAssetExtractionService;

   // step 1 - validate the input for cube updation

   // step 2 - pre fact table creation

   // step 3 - temp fact table creation & manage the existing fact table

   // step 4 - lookup table management

   // step 5 - cleanup resources.

   // step 6 - asset updation

   @Override
   public CubeUpdationOutputInfo updateCube (CubeUpdationContext cubeUpdationContext) throws CubeUpdationException {
      CubeUpdationOutputInfo cubeUpdationOutputInfo = null;
      try {
         boolean isCubeUpdationContextValid = cubeInputValidatorService
                  .validateCubeUpdationContext(cubeUpdationContext);
         if (isCubeUpdationContextValid) {
            CubeUpdationInputInfo cubeUpdationInputInfo = cubeContextPopulationService
                     .populateCubeUpdationInputInfo(cubeUpdationContext);
            cubeUpdationOutputInfo = new CubeUpdationOutputInfo();
            cubeUpdationOutputInfo.setCubeUpdationInputInfo(cubeUpdationInputInfo);

            // populating the prefact table
            CubeUpdatePreFactTableStructure cubeUpdatePreFactTableStructure = getCubePreFactPopulationService()
                     .createCubePreFactTable(cubeUpdationOutputInfo);
            cubeUpdationOutputInfo.setCubeUpdatePreFactTableStructure(cubeUpdatePreFactTableStructure);

            // managing the existing fact table
            CubeFactTableStructure cubeFactTableStructure = getCubeFactPopulationService().manageCubeFactTable(
                     cubeUpdationOutputInfo);
            cubeUpdationOutputInfo.setCubeFactTableStructure(cubeFactTableStructure);

            // managing the lookup table
            getCubeLookupPopulationService().manageCubeLookupTable(cubeUpdationOutputInfo);

            // cleaning the resources
            if (cubeUpdationInputInfo.getCubeConfigurationContext().isCleanTemporaryResources()) {
               getCubeResourceCleanupService().cleanupResources(cubeUpdationOutputInfo);
            }

            // update the asset for updations in all dimensions
            boolean updateAssetSuccess = getCubeAssetExtractionService().updateAsset(cubeUpdationOutputInfo);
            cubeUpdationOutputInfo.setUpdationSuccessful(updateAssetSuccess);
         }
      } catch (CubeInputValidatorException e) {
         throw new CubeUpdationException(AnswersCatalogExceptionCodes.DEFAULT_CUBE_INPUT_VALIDATION_EXCEPTION_CODE, e);
      } catch (CubeContextPopulationException e) {
         throw new CubeUpdationException(AnswersCatalogExceptionCodes.DEFAULT_CUBE_CONEXT_POPULATION_EXCEPTION_CODE, e);
      } catch (CubeFactPopulationException e) {
         throw new CubeUpdationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_FACT_TABLE_POPULATION_EXCEPTION_CODE, e);
      } catch (CubePreFactPopulationException e) {
         throw new CubeUpdationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_PREFACT_TABLE_POPULATION_EXCEPTION_CODE, e);
      } catch (CubeResourceCleanupException e) {
         throw new CubeUpdationException(AnswersCatalogExceptionCodes.DEFAULT_CUBE_RESOURCE_CLEANUP_EXCEPTION_CODE, e);
      } catch (CubeLookupPopulationException e) {
         throw new CubeUpdationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_LOOKUP_TABLE_POPULATION_EXCEPTION_CODE, e);
      } catch (CubeAssetExtractionException e) {
         throw new CubeUpdationException(AnswersCatalogExceptionCodes.DEFAULT_CUBE_ASSET_EXTRACTION_EXCEPTION_CODE, e);
      }
      return cubeUpdationOutputInfo;
   }

   public ICubeInputValidatorService getCubeInputValidatorService () {
      return cubeInputValidatorService;
   }

   public void setCubeInputValidatorService (ICubeInputValidatorService cubeInputValidatorService) {
      this.cubeInputValidatorService = cubeInputValidatorService;
   }

   public ICubeContextPopulationService getCubeContextPopulationService () {
      return cubeContextPopulationService;
   }

   public void setCubeContextPopulationService (ICubeContextPopulationService cubeContextPopulationService) {
      this.cubeContextPopulationService = cubeContextPopulationService;
   }

   public ICubePreFactPopulationService getCubePreFactPopulationService () {
      return cubePreFactPopulationService;
   }

   public void setCubePreFactPopulationService (ICubePreFactPopulationService cubePreFactPopulationService) {
      this.cubePreFactPopulationService = cubePreFactPopulationService;
   }

   public ICubeFactPopulationService getCubeFactPopulationService () {
      return cubeFactPopulationService;
   }

   public void setCubeFactPopulationService (ICubeFactPopulationService cubeFactPopulationService) {
      this.cubeFactPopulationService = cubeFactPopulationService;
   }

   public ICubeLookupPopulationService getCubeLookupPopulationService () {
      return cubeLookupPopulationService;
   }

   public void setCubeLookupPopulationService (ICubeLookupPopulationService cubeLookupPopulationService) {
      this.cubeLookupPopulationService = cubeLookupPopulationService;
   }

   public ICubeResourceCleanupService getCubeResourceCleanupService () {
      return cubeResourceCleanupService;
   }

   public void setCubeResourceCleanupService (ICubeResourceCleanupService cubeResourceCleanupService) {
      this.cubeResourceCleanupService = cubeResourceCleanupService;
   }

   public ICubeAssetExtractionService getCubeAssetExtractionService () {
      return cubeAssetExtractionService;
   }

   public void setCubeAssetExtractionService (ICubeAssetExtractionService cubeAssetExtractionService) {
      this.cubeAssetExtractionService = cubeAssetExtractionService;
   }

}
