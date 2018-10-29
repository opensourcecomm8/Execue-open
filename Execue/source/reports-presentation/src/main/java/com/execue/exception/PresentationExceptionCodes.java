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


package com.execue.exception;

public interface PresentationExceptionCodes {

   // The exception codes below ReportingConstants.PRESENTATION_EXCEPTION_RETURN_ERROR_LIMIT
   // would lead into an error page, being handled on report view action side
   int DEFAULT_EXCEPTION_CODE                    = 10000;
   int DEFAULT_EXCEPTION_METHOD_NOT_SUPPORTED    = 11111;
   int DEFAULT_PRESENTAION_REPORTS_FAILED        = 12000;
   int DATABASE_RETRIVAL_FAILED                  = 13000;

   int DEFAULT_DATA_BROWSER_FAILED               = 21000;
   // ReportingConstants.REVERT_TO_DEFAULT_GRID: update the code as well.
   int PRESENTATION_EXCEPTION_RETURN_ERROR_LIMIT = 20000;
   int REVERT_TO_DEFAULT_GRID                    = 25000;
   int DEFAULT_LOCALIZED_VIEWTX_EXCEPTION        = 26000;
   int CONFIGURATION_EXCEPTION                   = 27000;
   int HIERARCHY_REPORT_GRID_FAILED              = 27001;
}
