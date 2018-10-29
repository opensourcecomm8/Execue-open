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
 * @since 14/01/2011
 */
public interface ICubeOperationalConstants {

   // cube creation constants
   public String VALIDATE_CUBE_CREATION_CONTEXT                                                            = "1. Validating the Cube Creation Context.";
   public String VALIDATE_CUBE_CONTEXT_FAILED                                                              = "Cube context validation failed.";

   public String POPULATE_GENERIC_ANSWERS_CATALOG_CONFIGURATION_CONTEXT                                    = "2.1 Populating the Generic Answers Catalog Context from configuration.";
   public String POPULATE_SPECIFIC_CUBE_CONFIGURATION_CONTEXT                                              = "2.2 Populating the Specific Cube Creation Context from Configuration.";
   public String POPULATE_CUBE_CREATION_CONTEXT_FROM_SWI                                                   = "2.3 Populating the Cube Creation Context from SWI.";

   public String GENERATE_PRE_FACT_TEMP_TABLE_STRUCTURE                                                    = "3.1 Creating the Pre Fact Temp Table Structure.";
   public String PRE_FACT_TEMP_TABLE_DATA_POPULATION                                                       = "3.2 Populating the data into pre fact temp table";

   public String GENERATE_FACT_TABLE_STRUCTURE                                                             = "4.1 Creating the Fact Table Structure.";
   public String CREATING_INDEXES_ON_FACT_TABLE                                                            = "4.2 Creating the Indexes On Fact Table.";

   public String GENERATING_SIMPLE_LOOKUP_TABLES                                                           = "5.1 Creating Simple Lookup Tables.";
   public String GENERATING_RANGE_LOOKUP_TABLES                                                            = "5.2 Creating Range Lookup Tables.";
   public String GENERATING_STAT_LOOKUP_TABLE                                                              = "5.3 Creating Stat Lookup Table.";
   public String CREATING_INDEXES_ON_LOOKUP_TABLES                                                         = "5.4 Creating the Indexes On Lookup Tables.";

   public String RESOURCE_CLEANUP                                                                          = "6. Cleaning up the resources";

   public String CREATE_ASSET_TABLE_COLUMN_MEMBER                                                          = "7.1 Asset Entities Creation in SWI.";
   public String COPY_ASSET_TABLE_COLUMN_MEMBER_INFO                                                       = "7.2 Copy Asset Entities Information from Source Asset.";
   public String CREATE_JOINS                                                                              = "7.3 Create the Joins from Source Asset.";
   public String CREATE_MAPPINGS_AND_GRAIN                                                                 = "7.4 Creating the Mappings and Grain from Source Asset.";
   public String ASSET_READINESS_AND_ACTIVATION                                                            = "7.5 Asset Readiness and Activation.";

   // cube updation constants
   public String VALIDATE_CHANGES_IN_CUBE_SWI_TO_SOURCE_SWI                                                = "1. Validating changes in cube SWI to source SWI.";
   public String NO_CHANGES_IN_CUBE_SWI_TO_SOURCE_SWI                                                      = "There is no change in cube SWI to source SWI";
   public String VALIDATE_CUBE_UPDATION_CONTEXT                                                            = "1. Validating the Cube Updation Context.";

   public String POPULATE_GENERIC_ANSWERS_CATALOG_CONFIGURATION_CONTEXT_FOR_CUBE_UPDATE                    = "2.1 Populating the Generic Answers Catalog Context from configuration.";
   public String POPULATE_SPECIFIC_CUBE_CONFIGURATION_CONTEXT_FOR_CUBE_UPDATE                              = "2.2 Populating the Specific Cube Creation Context from Configuration.";
   public String POPULATE_CUBE_UPDATION_CONTEXT_FROM_SWI                                                   = "2.3 Populating the Cube Updation Context from SWI.";

   public String GENERATE_PRE_FACT_TEMP_TABLE_STRUCTURE_FOR_CUBE_UPDATE                                    = "3.1 Creating the Pre Fact Temp Table Structure.";
   public String PRE_FACT_TEMP_TABLE_DATA_POPULATION_PER_DIMENSION_FOR_CUBE_UPDATE                         = "3.2 Populating the data into pre fact temp table for each updated dimension";
   public String DE_DUP_PRE_FACT_TEMP_TABLE_DATA_AND_POPULATE_FINAL_TEMP_TABLE                             = "3.3 De-dup the data for each dimension and populate the final temp table";

   public String GENERATE_FACT_TABLE_STRUCTURE_FOR_CUBE_UPDATE                                             = "4.1 Creating the Fact Table Structure.";
   public String DELETEING_DELETED_MEMBERS_FROM_EXISTING_FACT_TABLE_PER_DIMENSION                          = "4.2 Deleting the deleted members from existing fact table per dimension.";
   public String TRANSFER_UPDATED_FACT_DATA_TO_EXISTING_FACT_TABLE                                         = "4.3 Transferring the udpated temp fact table data to existing fact table.";

   public String DELETEING_DELETED_MEMBERS_AND_ADDING_ADDED_MEMBERS_TO_EXISTING_LOOKUP_TABLE_PER_DIMENSION = "5 Deleting the deleted members and Adding the added members to existing lookup table per dimension.";

   public String RESOURCE_CLEANUP_CUBE_UPDATE                                                              = "6. Cleaning up the resources";

   public String DELETE_DELETED_MEMBERS_FROM_SWI                                                           = "7.1 Delete the Deleted Members from SWI.";
   public String ADD_ADDED_MEMBERS_TO_SWI                                                                  = "7.2 Add the Added Members to SWI.";
   public String DEFAULT_DYNAMIC_VALUES_UPDATION                                                           = "7.3 Default Dynamic values updation in SWI.";

   // Added cube validation message, will see if need to move somewhere else

   public String ASSET_ALREADY_UNDER_PROCESS_MESSAGE                                                       = "Asset is already under some process. Please wait.";
   public String ASSET_OR_PARENT_ASSET_ALREADY_UNDER_PROCESS_MESSAGE                                       = "Asset or its parent asset is already under some process. Please wait.";

}
