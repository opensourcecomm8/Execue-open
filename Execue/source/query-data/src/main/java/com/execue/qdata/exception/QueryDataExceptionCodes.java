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


package com.execue.qdata.exception;

import com.execue.core.exception.ExeCueExceptionCodes;

/**
 * @author John Mallavalli
 */
public interface QueryDataExceptionCodes extends ExeCueExceptionCodes {

   int QD_DEFAULT_EXCEPTION_CODE                                            = 40000;
   int QD_AGGREGATION_QUERY_EXCEPTION_CODE                                  = 40001;
   int QD_USER_QUERY_EXCEPTION_CODE                                         = 40002;

   int QD_REDUCED_QUERY_EXCEPTION_CODE                                      = 40006;
   int RUNTIME_TABLES_CLEANUP_FAILURE_CODE                                  = 40007;

   int QUERY_ANSWERS_CATALOG_MANAGEMENT_QUEUE_CREATION_FAILED               = 40008;

   int QUERY_ANSWERS_CATALOG_MANAGEMENT_QUEUE_DELETION_FAILED               = 40009;

   int QUERY_GET_ANSWERS_CATALOG_MANAGEMENT_QUEUE_BY_ID_FAILED              = 40010;

   int QUERY_DELETE_ANSWERS_CATALOG_MANAGEMENT_QUEUE_BY_REQUEST_DATE_FAILED = 40011;

   int QUERY_GET_PENDING_ANSWERS_CATALOG_MANAGEMENT_QUEUE_FAILED            = 40012;

   int POPULATION_OF_USER_SEARCH_AUDIT_FAILED                               = 40013;
}
