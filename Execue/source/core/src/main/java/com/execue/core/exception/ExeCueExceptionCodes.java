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


package com.execue.core.exception;

/** */
public interface ExeCueExceptionCodes {

   int DEFAULT_EXCEPTION_CODE                                                     = 10000;
   int MAX_USER_QUERY_LENGTH_EXCEPTION_CODE                                       = 10001;
   int DEFAULT_SYSTEM_EXCEPTION_CODE                                              = 90000;
   int ASSET_CREATION_FAILED                                                      = 10100;
   int CONFIGURATION_LOAD_FAILED_FOR_DATA_ACCESS                                  = 10007;
   int CONFIGURATION_LOAD_ERROR                                                   = 10001;
   int ENTITY_ALREADY_EXISTS                                                      = 10002;
   int ARITHMETIC_EXPRESSION_EVALUATION_FAILED                                    = 10901;
   int ENCRPTION_PROCESS_FAILED                                                   = 10902;
   int DECYPTION_PROCESS_FAILED                                                   = 10903;
   int SECRET_KEY_GENERATION_FAILED                                               = 10904;
   int MAIL_SENDING_FAILED                                                        = 10905;
   int USER_ACTIVATION_FAILED                                                     = 10906;
   int ENTITY_RETRIEVAL_FAILED                                                    = 10907;
   int RESULT_SET_ERROR                                                           = 10908;
   int INVALID_PASSWORD                                                           = 10909;
   int SECRET_KEY_GENERATION_TRIPLE_DES_LENGTH_CANNOT_BE_LESSTHAN_24_BYTES_FAILED = 10910;
   int REPORT_COMMENT_RETRIEVAL_FAILED                                            = 109010;
   int REPORT_COMMENT_CREATION_FAILED                                             = 109011;
   int USER_UPDATION_FAILED                                                       = 109012;
   int QUERY_INSTANCE_BED_LIST_IN_RI_ONTO_TERMBY_CONCPET_FAILED                   = 109013;
   int QUERY_GET_INSTANCE_BEDIDS_FOR_CONCEPT_FAILED                               = 109014;
   int DRIVER_PROCESS_POSSIBILITIES_PRIOR_GOVERNOR_UNHANDLED_EXCPETION            = 109015;
   int DRIVER_PROCESS_POSSIBILITIES_GOVERNOR_HANDLED_EXCPETION                    = 109015;
   int RESERVE_WORD_MATCH                                                         = 900065;
   int ENTITY_DELETION_FAILED                                                     = 109016;
   int INITIAL_CONTEXT_INSTANTIASION_FAILED                                       = 109017;
   int INVALID_CONFIGURATION_MODIFICATIONEXCEPTION                                = 109018;
}
