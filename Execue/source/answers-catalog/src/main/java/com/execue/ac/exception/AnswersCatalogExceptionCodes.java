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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.execue.ac.exception;

import com.execue.core.exception.ExeCueExceptionCodes;

/**
 * @author venu
 */
public interface AnswersCatalogExceptionCodes extends ExeCueExceptionCodes {

   int DEFAULT_ASSET_CREATION_EXCEPTION_CODE                            = 11000;
   int DEFAULT_ASSET_CREATION_SYSTEM_EXCEPTION_CODE                     = 91000;

   int DEFAULT_ASSET_UPDATION_EXCEPTION_CODE                            = 12000;
   int DEFAULT_ASSET_UPDATION_SYSTEM_EXCEPTION_CODE                     = 92000;

   int DEFAULT_ASSET_DELETION_EXCEPTION_CODE                            = 13000;
   int DEFAULT_ASSET_DELETION_SYSTEM_EXCEPTION_CODE                     = 93000;

   int DEFAULT_ASSET_QUERY_GENERATION_EXCEPTION_CODE                    = 14000;
   int DEFAULT_ASSET_QUERY_GENERATION_SYSTEM_EXCEPTION_CODE             = 94000;

   int DEFAULT_ASSET_DATA_EXTRACTION_EXCEPTION_CODE                     = 15000;
   int DEFAULT_ASSET_DATA_EXTRACTION_SYSTEM_EXCEPTION_CODE              = 95000;

   int DEFAULT_ASSET_DATA_TRANSFORMATION_EXCEPTION_CODE                 = 16000;
   int DEFAULT_ASSET_DATA_TRANSFORMATION_SYSTEM_EXCEPTION_CODE          = 96000;

   int DEFAULT_ASSET_INDEX_CREATION_EXCEPTION_CODE                      = 17000;
   int DEFAULT_ASSET_INDEX_CREATION_SYSTEM_EXCEPTION_CODE               = 97000;

   int DEFAULT_ASSET_METADATA_EXTRACTION_EXCEPTION_CODE                 = 18000;
   int DEFAULT_ASSET_METADATA_EXTRACTION_SYSTEM_EXCEPTION_CODE          = 98000;

   int DEFAULT_ASSET_CONSTRAINT_CREATION_EXCEPTION_CODE                 = 22000;
   int DEFAULT_ASSET_CONSTRAINT_CREATION_SYSTEM_EXCEPTION_CODE          = 23000;

   int CUBE_JOB_SCHEDULING_FAILURE_CODE                                 = 24000;

   int DEFAULT_JDBC_EXCEPTION_CODE                                      = 25000;
   int DEFAULT_DATA_MART_CONEXT_POPULATION_EXCEPTION_CODE               = 26000;
   int DEFAULT_DATA_MART_INPUT_VALIDATION_EXCEPTION_CODE                = 27000;
   int DEFAULT_DATA_MART_SFACTOR_POPULATION_EXCEPTION_CODE              = 28000;
   int DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE            = 29000;
   int DEFAULT_DATA_MART_MERGE_POPULATION_EXCEPTION_CODE                = 30000;
   int DEFAULT_DATA_MART_FRACTIONAL_DATASETS_POPULATION_EXCEPTION_CODE  = 31000;
   int DEFAULT_DATA_MART_POPULATION_SLICING_EXCEPTION_CODE              = 32000;
   int DEFAULT_DATA_MART_RESOURCE_CLEANUP_EXCEPTION_CODE                = 33000;
   int MART_JOB_SCHEDULING_FAILURE_CODE                                 = 35000;
   int DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE                           = 36000;
   int DEFAULT_CUBE_INPUT_VALIDATION_EXCEPTION_CODE                     = 37000;
   int DEFAULT_CUBE_PREFACT_TABLE_POPULATION_EXCEPTION_CODE             = 38000;
   int DEFAULT_CUBE_FACT_TABLE_POPULATION_EXCEPTION_CODE                = 39000;
   int DEFAULT_CUBE_LOOKUP_TABLE_POPULATION_EXCEPTION_CODE              = 40000;
   int DEFAULT_CUBE_RESOURCE_CLEANUP_EXCEPTION_CODE                     = 41000;
   int DEFAULT_CUBE_CONEXT_POPULATION_EXCEPTION_CODE                    = 42000;
   int DEFAULT_DATA_MART_ASSET_EXTRACTION_EXCEPTION_CODE                = 43000;
   int DEFAULT_THREAD_POOL_RELATED_EXCEPTION_CODE             = 43001;
   int DEFAULT_CUBE_ASSET_EXTRACTION_EXCEPTION_CODE                     = 44000;
   int DEFAULT_CUBE_ASSET_UPDATION_EXCEPTION_CODE                       = 45000;
   int DEFAULT_PARENT_ASSET_SYNCHRONIZATION_JOB_SCHEDULING_FAILURE_CODE = 46000;

}
