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


package com.execue.repoting.aggregation.exception;

import com.execue.core.exception.ExeCueExceptionCodes;

/**
 * @author John Mallavalli
 */
public interface AggregationExceptionCodes extends ExeCueExceptionCodes {

   int AGG_REPORT_SELECTION_EXCEPTION_CODE  = 60001;
   int AGG_DYNAMIC_RANGES_EXCEPTION_CODE    = 60002;
   int AGG_REPORT_QUERY_DATA_EXCEPTION_CODE = 60003;
   int AGG_DATA_EXTRACTION_EXCEPTION_CODE   = 60004;
   int COUNTS_RETRIEVAL_EXCEPTION_CODE      = 60005;
}
