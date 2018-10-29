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


package com.execue.ac.service;

/**
 * This interface contains the operational constants related to cube.
 * 
 * @author Vishay
 * @version 1.0
 * @since 28/02/2011
 */
public interface IMartOperationalConstants {

   public String VALIDATE_MART_CREATION_CONTEXT                         = "1. Validating the Mart Creation Context.";
   public String VALIDATE_MART_CONTEXT_FAILED                           = "Mart context validation failed.";

   public String POPULATE_GENERIC_ANSWERS_CATALOG_CONFIGURATION_CONTEXT = "2.1 Populating the Generic Answers Catalog Context from configuration.";
   public String POPULATE_SPECIFIC_MART_CONFIGURATION_CONTEXT           = "2.2 Populating the Specific Mart Creation Context from Configuration.";
   public String POPULATE_MART_CREATION_CONTEXT_FROM_SWI                = "2.3 Populating the Mart Creation Context from SWI.";
   public String POPULATE_SLICING_ALGORITHM_STATIC_INPUT                = "2.4 Populating the Slicing Algorithm Static Input.";
   public String POPULATE_MART_INPUT_PARAMETERS_CONTEXT                 = "2.5 Populating the Mart Input Parameters Context.";
   public String POPULATE_SAMPLING_ALGORITHM_STATIC_INPUT               = "2.6 Populating the Sampling Algorithm Static Input.";
   public String POPULATE_BATCH_ALGORITHM_STATIC_INPUT                  = "2.7 Populating the Batch Algorithm Static Input.";

   public String GENERATE_POPULATION_TABLE_STRUCTURE                    = "3.1 Creating the Population Table Structure.";
   public String SLICE_NUMBER_COLUMN_POPULATION                         = "3.2 Populating the Slice Number Column Data.";
   public String INDEX_CREATION_ON_POPULATION_TABLE                           = "3.3 Creating indexes on population table.";

   public String GENERATE_FRACTIONAL_DATA_SET_TABLE_STRUCTURE           = "4.1 Creating the Fractional Dataset Temp Table Structure.";
   public String SLICE_COUNT_COLUMN_POPULATION                          = "4.2 Populating the Slice Count Column Data.";

   public String SFACTOR_POPULATION                                     = "5. Fractional Dataset table creation and Sfactor Population.";

   public String PREPARE_SAMPLED_POPULATION_TABLE                       = "6. Create Sampled Population Table";

   public String WAREHOUSE_EXTRACTION                                   = "7. Extraction of the warehouse";

   public String RESOURCE_CLEANUP                                       = "8. Cleaning up the resources";

   public String CREATE_ASSET_TABLE_COLUMN_MEMBER                       = "9.1 Asset Entities Creation in SWI.";
   public String CREATE_VIRTUAL_TABLES                                  = "9.2 Copy Virtual Lookup tables from Source Asset.";
   public String COPY_ASSET_TABLE_COLUMN_MEMBER_INFO                    = "9.3 Copy Asset Entities Information from Source Asset.";
   public String CREATE_JOINS                                           = "9.4 Copy the Joins from Source Asset.";
   public String CREATE_MAPPINGS_AND_GRAIN                              = "9.5 Creating the Mappings and Grain from Source Asset.";
   public String ASSET_READINESS_AND_ACTIVATION                         = "9.6 Asset Readiness and Activation.";

}
