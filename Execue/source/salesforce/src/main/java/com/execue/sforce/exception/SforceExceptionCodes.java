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


package com.execue.sforce.exception;

import com.execue.core.exception.ExeCueExceptionCodes;
/**
 * This class creates a sforce module related exception codes
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public interface SforceExceptionCodes extends ExeCueExceptionCodes {

   int SFORCE_DEFAULT_EXCEPTION_CODE                    = 35000;
   int SFORCE_SOAP_REQUEST_FAILED_EXCEPTION_CODE        = 35001;
   int SFORCE_PARSE_SOAP_RESPONSE_FAILED_EXCEPTION_CODE = 35002;
   int SFORCE_SOBJECT_ABSORPTION_FAILED_EXCEPTION_CODE  = 35003;
   int SFORCE_SOAP_MESSAGE_FAILED_EXCEPTION_CODE  = 35004;
   int SFORCE_JOB_SETUP_EXCEPTION_CODE  = 35005;
   int SFORCE_NORMALIZATION_DATA_EXCEPTION_CODE  = 35006;
}
