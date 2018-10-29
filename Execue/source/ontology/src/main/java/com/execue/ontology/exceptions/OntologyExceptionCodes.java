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


package com.execue.ontology.exceptions;

import com.execue.core.exception.ExeCueExceptionCodes;

public interface OntologyExceptionCodes extends ExeCueExceptionCodes {

   int ONTOLOGY_DEFAULT_EXCEPTION_CODE                = 6000;
   int ONTOLOGY_DATA_ABSORPTION_FAILED_EXCEPTION_CODE = 6001;
   int ONTOLOGY_RI_ONTO_TERMS_ABSORPTION_FAILED       = 6002;
   int ONTOLOGY_SNOW_FLAKES_TERMS_ABSORPTION_FAILED   = 6003;

}
