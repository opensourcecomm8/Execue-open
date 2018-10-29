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

import com.execue.ac.bean.CubeCreationInputInfo;
import com.execue.ac.bean.CubeCreationOutputInfo;
import com.execue.ac.bean.CubeFactTableStructure;
import com.execue.ac.bean.CubeLookupTableStructure;
import com.execue.ac.bean.CubePreFactTableStructure;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.CubeAssetExtractionException;
import com.execue.ac.exception.CubeContextPopulationException;
import com.execue.ac.exception.CubeCreationException;
import com.execue.ac.exception.CubeFactPopulationException;
import com.execue.ac.exception.CubeInputValidatorException;
import com.execue.ac.exception.CubeLookupPopulationException;
import com.execue.ac.exception.CubePreFactPopulationException;
import com.execue.ac.exception.CubeResourceCleanupException;
import com.execue.ac.service.ICubeAssetExtractionService;
import com.execue.ac.service.ICubeContextPopulationService;
import com.execue.ac.service.ICubeCreationService;
import com.execue.ac.service.ICubeFactPopulationService;
import com.execue.ac.service.ICubeInputValidatorService;
import com.execue.ac.service.ICubeLookupPopulationService;
import com.execue.ac.service.ICubeOperationalConstants;
import com.execue.ac.service.ICubePreFactPopulationService;
import com.execue.ac.service.ICubeResourceCleanupService;
import com.execue.core.common.bean.ac.CubeCreationContext;

/**
 * This service is the wrapper service for cube creation.
 * 
 * @author Vishay
 * @version 1.0
 * @since 22/02/2011
 */
public class CubeCreationServiceImpl implements ICubeCreationService, ICubeOperationalConstants {

   private ICubeInputValidatorService    cubeInputValidatorService;
   private ICubeContextPopulationService cubeContextPopulationService;
   private ICubePreFactPopulationService cubePreFactPopulationService;
   private ICubeFactPopulationService    cubeFactPopulationService;
   private ICubeLookupPopulationService  cubeLookupPopulationService;
   private ICubeResourceCleanupService   cubeResourceCleanupService;
   private ICubeAssetExtractionService   cubeAssetExtractionService;

   // step 1 - validate the input for cube creation

   // step 2 - pre fact table creation

   // step 3 - fact table creation

   // step 4 - lookup tables creation

   // step 5 - cleanup resources.

   // step 6 - asset extraction.

   public CubeCreationOutputInfo cubeCreation (CubeCreationContext cubeCreationContext) throws CubeCreationException {
      CubeCreationOutputInfo cubeCreationOutputInfo = null;
      try {
         boolean isCubeCreationContextValid = cubeInputValidatorService
                  .validateCubeCreationContext(cubeCreationContext);
         if (isCubeCreationContextValid) {
            CubeCreationInputInfo cubeCreationInputInfo = cubeContextPopulationService
                     .populateCubeCreationInputInfo(cubeCreationContext);
            cubeCreationOutputInfo = new CubeCreationOutputInfo();
            cubeCreationOutputInfo.setCubeCreationInputInfo(cubeCreationInputInfo);

            // step 2. pre fact table creation
            CubePreFactTableStructure cubePreFactTableStructure = cubePreFactPopulationService
                     .createCubePreFactTable(cubeCreationOutputInfo);
            cubeCreationOutputInfo.setCubePreFactTableStructure(cubePreFactTableStructure);

            // step 3. fact table creation
            CubeFactTableStructure cubeFactTableStructure = cubeFactPopulationService
                     .createCubeFactTable(cubeCreationOutputInfo);
            cubeCreationOutputInfo.setCubeFactTableStructure(cubeFactTableStructure);

            // step 4. lookup tables creation
            CubeLookupTableStructure cubeLookupTableStructure = cubeLookupPopulationService
                     .createCubeLookupTables(cubeCreationOutputInfo);
            cubeCreationOutputInfo.setCubeLookupTableStructure(cubeLookupTableStructure);

            // step 6. Resource clean up.
            if (cubeCreationInputInfo.getCubeConfigurationContext().isCleanTemporaryResources()) {
               cubeResourceCleanupService.cleanupResources(cubeCreationOutputInfo);
            }

            // step 7. Asset extraction
            cubeCreationOutputInfo.setCreationSuccessful(cubeAssetExtractionService
                     .extractAsset(cubeCreationOutputInfo));
         }
      } catch (CubeInputValidatorException e) {
         throw new CubeCreationException(AnswersCatalogExceptionCodes.DEFAULT_CUBE_INPUT_VALIDATION_EXCEPTION_CODE, e);
      } catch (CubeContextPopulationException e) {
         throw new CubeCreationException(AnswersCatalogExceptionCodes.DEFAULT_CUBE_CONEXT_POPULATION_EXCEPTION_CODE, e);
      } catch (CubeAssetExtractionException e) {
         throw new CubeCreationException(AnswersCatalogExceptionCodes.DEFAULT_CUBE_ASSET_EXTRACTION_EXCEPTION_CODE, e);
      } catch (CubeResourceCleanupException e) {
         throw new CubeCreationException(AnswersCatalogExceptionCodes.DEFAULT_CUBE_RESOURCE_CLEANUP_EXCEPTION_CODE, e);
      } catch (CubeLookupPopulationException e) {
         throw new CubeCreationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_LOOKUP_TABLE_POPULATION_EXCEPTION_CODE, e);
      } catch (CubeFactPopulationException e) {
         throw new CubeCreationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_FACT_TABLE_POPULATION_EXCEPTION_CODE, e);
      } catch (CubePreFactPopulationException e) {
         throw new CubeCreationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_PREFACT_TABLE_POPULATION_EXCEPTION_CODE, e);
      }
      return cubeCreationOutputInfo;
   }

   /**
    * @return the cubeInputValidatorService
    */
   public ICubeInputValidatorService getCubeInputValidatorService () {
      return cubeInputValidatorService;
   }

   /**
    * @param cubeInputValidatorService
    *           the cubeInputValidatorService to set
    */
   public void setCubeInputValidatorService (ICubeInputValidatorService cubeInputValidatorService) {
      this.cubeInputValidatorService = cubeInputValidatorService;
   }

   /**
    * @return the cubeContextPopulationService
    */
   public ICubeContextPopulationService getCubeContextPopulationService () {
      return cubeContextPopulationService;
   }

   /**
    * @param cubeContextPopulationService
    *           the cubeContextPopulationService to set
    */
   public void setCubeContextPopulationService (ICubeContextPopulationService cubeContextPopulationService) {
      this.cubeContextPopulationService = cubeContextPopulationService;
   }

   /**
    * @return the cubePreFactPopulationService
    */
   public ICubePreFactPopulationService getCubePreFactPopulationService () {
      return cubePreFactPopulationService;
   }

   /**
    * @param cubePreFactPopulationService
    *           the cubePreFactPopulationService to set
    */
   public void setCubePreFactPopulationService (ICubePreFactPopulationService cubePreFactPopulationService) {
      this.cubePreFactPopulationService = cubePreFactPopulationService;
   }

   /**
    * @return the cubeFactPopulationService
    */
   public ICubeFactPopulationService getCubeFactPopulationService () {
      return cubeFactPopulationService;
   }

   /**
    * @param cubeFactPopulationService
    *           the cubeFactPopulationService to set
    */
   public void setCubeFactPopulationService (ICubeFactPopulationService cubeFactPopulationService) {
      this.cubeFactPopulationService = cubeFactPopulationService;
   }

   /**
    * @return the cubeLookupPopulationService
    */
   public ICubeLookupPopulationService getCubeLookupPopulationService () {
      return cubeLookupPopulationService;
   }

   /**
    * @param cubeLookupPopulationService
    *           the cubeLookupPopulationService to set
    */
   public void setCubeLookupPopulationService (ICubeLookupPopulationService cubeLookupPopulationService) {
      this.cubeLookupPopulationService = cubeLookupPopulationService;
   }

   /**
    * @return the cubeResourceCleanupService
    */
   public ICubeResourceCleanupService getCubeResourceCleanupService () {
      return cubeResourceCleanupService;
   }

   /**
    * @param cubeResourceCleanupService
    *           the cubeResourceCleanupService to set
    */
   public void setCubeResourceCleanupService (ICubeResourceCleanupService cubeResourceCleanupService) {
      this.cubeResourceCleanupService = cubeResourceCleanupService;
   }

   /**
    * @return the cubeAssetExtractionService
    */
   public ICubeAssetExtractionService getCubeAssetExtractionService () {
      return cubeAssetExtractionService;
   }

   /**
    * @param cubeAssetExtractionService
    *           the cubeAssetExtractionService to set
    */
   public void setCubeAssetExtractionService (ICubeAssetExtractionService cubeAssetExtractionService) {
      this.cubeAssetExtractionService = cubeAssetExtractionService;
   }

}
