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


package com.execue.swi.exception;

import com.execue.core.exception.ExeCueExceptionCodes;

/**
 * @author venu
 */
public interface SWIExceptionCodes extends ExeCueExceptionCodes {

   int ASSET_CREATION_FAILED                                                   = 90001;
   int TABLE_CREATION_FAILED                                                   = 90002;
   int COLUMN_CREATION_FAILED                                                  = 90003;
   int MEMBER_CREATION_FAILED                                                  = 90004;
   int CONCEPT_CREATION_FAILED                                                 = 90005;
   int MODEL_CREATION_FAILED                                                   = 90006;
   int INSTANCE_CREATION_FAILED                                                = 90007;
   int ASSET_RETRIEVAL_FAILED                                                  = 90008;
   int ENTITY_RETRIEVAL_FAILED                                                 = 90009;
   int COLUMN_RETRIEVAL_FAILED                                                 = 90010;
   int TABLE_RETRIEVAL_FAILED                                                  = 90011;
   int MEMBER_RETRIEVAL_FAILED                                                 = 90012;
   int MODEL_RETRIEVAL_FAILED                                                  = 90013;
   int CONCEPT_RETRIEVAL_FAILED                                                = 90014;
   int INSTANCE_RETRIEVAL_FAILED                                               = 90015;

   int VALID_ASSET                                                             = 100;
   int INVALID_ASSET                                                           = 101;

   int MANDATORY_ASSET_DETAILS_MISSING                                         = 200;
   int ASSET_CONNECTION_REFUSED                                                = 201;
   int ASSET_CONNECTION_ESTABLISHED                                            = 202;
   int TABLE_INFO_RETRIEVAL_FAILED                                             = 203;
   int PROFILE_CREATION_FAILURE                                                = 90016;
   int RELATION_CREATION_FAILED                                                = 90017;
   int RELATION_ALREADY_EXIST                                                  = 90018;
   int RANGE_CREATION_FAILURE                                                  = 900017;
   int ENTITY_ALREADY_EXISTS                                                   = 900018;
   int DATASOURCE_CREATION_FAILED                                              = 900019;
   int DATASOURCE_UPDATE_FAILED                                                = 900020;
   int DATASOURCE_DELETE_FAILED                                                = 900021;

   int CONCEPT_UPDATE_FAILED                                                   = 900022;
   int INSTANCE_UPDATE_FAILED                                                  = 900023;

   int ENTITY_DELETE_FAILED                                                    = 900024;
   int MEMBERS_NOT_ALLOWED_ON_FACT_TABLE                                       = 900025;
   int MEMBER_DELETION_FAILED                                                  = 900024;
   int UNKNOWN_BUSINESS_ENTITY_TYPE                                            = 900025;

   int UPDATE_SFL_TERM_TOKEN_WEIGHT_JOB_SCHEDULING_FAILURE_CODE                = 900027;
   int UPDATE_RI_PARALLEL_WORD_JOB_SCHEDULING_FAILURE_CODE                     = 900028;
   int UPDATE_POPULARITY_HIT_UPDATION_JOB_SCHEDULING_FAILURE_CODE              = 900029;
   int UPDATE_PRIMARY_FOR_UNIQUELY_MAPPED_BEDS_FOR_ASSET_FAILED                = 900030;
   int QUERY_NON_UNIQUE_MAPPING_FOR_ASSET_FAILED                               = 900031;
   int CORRECT_MAPPINGS_JOB_SCHEDULING_FAILURE_CODE                            = 900032;
   int UPDATE_NON_PRIMARY_FOR_NON_UNIQUE_MAPPINGS_FOR_BED_FAILED               = 900033;
   int DELETE_MAPPINGS_FOR_MODEL_FAILED                                        = 900034;
   int DELETE_USER_QUERY_POSSIBILITY_JOB_FAILED                                = 900035;
   int SDX_DELETION_JOB_FAILED                                                 = 900036;
   int POPULARITY_COLLECTION_JOB_SCHEDULING_FAILURE_CODE                       = 900037;
   int POPULARITY_DISPERSION_JOB_SCHEDULING_FAILURE_CODE                       = 900038;
   int DELETE_POPULARITY_JOB_FAILED                                            = 900039;
   int UPDATE_RI_ONTO_TERM_POPULARITY_HIT_UPDATION_JOB_SCHEDULING_FAILURE_CODE = 900040;
   int BATCH_PROCESS_CREATION_FAILED                                           = 900041;
   int INSTANCE_MAPPING_SUGGESTION_CREATION_FAILED                             = 900042;
   int INSTANCE_MAPPING_SUGGESTION_DETAIL_CREATION_FAILED                      = 900043;
   int INSTANCE_MAPPING_SUGGESTION_DETAIL_RETRIEVAL_FAILED                     = 900044;
   int BUSINESS_MODEL_PREPARATION_JOB_SCHEDULING_FAILURE_CODE                  = 900045;
   int ENTITY_UPDATE_FAILED                                                    = 900046;
   int INSTANCE_MAPPING_SUGGESTION_DETAIL_COUNT_RETRIEVAL_FAILED               = 900047;
   int INSTANCE_MAPPING_SUGGESTION_RETRIEVAL_FAILED                            = 900048;
   int PUBLISH_ASSET_JOB_FAILURE_CODE                                          = 900049;
   int INDEX_FORM_MANAGEMENT_JOB_SCHEDULING_FAILURE_CODE                       = 900050;
   int INDEX_FORM_CREATION_FAILED                                              = 900051;
   int DEFAULT_METRICS_POPULATION_FAILURE_CODE                                 = 900052;
   int TYPE_CREATION_FAILED                                                    = 900053;
   int UPDATE_SFL_WEIGHT_BY_SECWORD_JOB_SCHEDULING_FAILURE_CODE                = 900054;
   int UPLOADED_FILE_QUERY_EXECUTION_FAILED                                    = 900055;
   int UPLOADED_FILE_DROP_TABLE_FAILED                                         = 900056;
   int UPLOADED_FILE_DELETE_FAILED                                             = 900057;
   int CTA_MAINTENANCE_JOB_FAILED                                              = 900058;
   int MEMBER_SYNCHRONIZATION_JOB_SCHEDULING_FAILURE_CODE                      = 900059;
   int RUNTIME_TABLES_CLEANUP_FAILURE_CODE                                     = 900060;
   int ENTITY_DETAIL_TYPE_CREATE_FAILURE_CODE                                  = 900061;
   int ENTITY_DETAIL_TYPE_DELETE_FAILURE_CODE                                  = 900062;
   int EAS_INDEX_REFRESH_FAILURE_CODE                                          = 900063;
   int VERTICAL_POPULARITY_JOB_SCHEDULING_FAILURE_CODE                         = 900064;
   int RESERVE_WORD_MATCH                                                      = 900065;
   int QI_AUTHORIZED_APPLICATIONS_RETRIEVAL_EXCEPTION_CODE                     = 20002;
}
