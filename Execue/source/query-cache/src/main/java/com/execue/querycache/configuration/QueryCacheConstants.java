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


package com.execue.querycache.configuration;

public interface QueryCacheConstants {

   String CHECK_MATCH_FLAG                            = "qcache.flags.checkMatch";

   String QUERY_CACHE_MATCH_RESULT_WEIGHT             = "qcache.flags.matchResultWeight";

   String QUERY_CACHE_RELATED_QUERY_WEIGHT_THRESHOLD  = "qcache.flags.relatedQueryThresholdWeight";

   String QUERY_CACHE_RELATED_QUERY_MAX_LIMIT         = "qcache.flags.relatedQueryMaxLimit";

   String VERTICAL_BASED_SORTING_FLAG                 = "semantic.driver.flags.verticalBasedSorting";

   String HANDLE_PROCESS_POSSIBILITIES_ERRORS_FLAG    = "semantic.driver.flags.handle.propcessPossibilities";

   String HANDLE_TIME_FRAME_CLASS_CAST_EXCEPTION_FLAG = "querygen.flags.handleTimeFrameClassCastException";
}
