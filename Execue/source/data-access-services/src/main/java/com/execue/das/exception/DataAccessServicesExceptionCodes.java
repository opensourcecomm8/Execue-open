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


package com.execue.das.exception;

import com.execue.core.exception.ExeCueExceptionCodes;

public interface DataAccessServicesExceptionCodes extends ExeCueExceptionCodes {

   int REMOTE_USER_LOCATION_DATA_RETRIEVAL_FAILED         = 900066;
   int CITY_CENTER_ZIPCODE_LOOKUP_FAILED                  = 900067;
   int DEFAULT_REMOTE_DATA_TRANSFER_EXCEPTION_CODE        = 19000;
   int DEFAULT_REMOTE_DATA_TRANSFER_SYSTEM_EXCEPTION_CODE = 99000;
   int DEFAULT_ETL_SCRIPTELLA_EXCEPTION_CODE              = 20000;
   int DEFAULT_ETL_KETTLE_EXCEPTION_CODE                  = 21000;
   int DEFAULT_DATA_TRANSFER_ETL_PROCESS_EXCEPTION_CODE   = 34000;
}
