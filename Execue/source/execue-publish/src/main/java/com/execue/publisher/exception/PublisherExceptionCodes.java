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


package com.execue.publisher.exception;

import com.execue.core.exception.ExeCueExceptionCodes;

/**
 * This class creates a publisher module related exception codes
 * 
 * @author Vishay
 * @version 1.0
 * @since 08/10/09
 */
public interface PublisherExceptionCodes extends ExeCueExceptionCodes {

   int PUBLISHER_DEFAULT_EXCEPTION_CODE                = 45000;
   int PUBLISHER_DATA_UPLOAD_FAILED_EXCEPTION_CODE     = 45001;
   int PUBLISHER_META_DATA_ABSORBTION_EXCEPTION_CODE   = 45002;
   int PUBLISHER_DATA_ABSORBTION_EXCEPTION_CODE        = 45003;
   int PUBLISHER_DATA_ABSORPTION_FAILED_EXCEPTION_CODE = 45004;
   int PUBLISHER_DATA_EVALUATION_FAILED_EXCEPTION_CODE = 45005;
}
