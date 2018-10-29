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


package com.execue.das.util;

import com.execue.core.exception.ExeCueException;
import com.execue.dataaccess.exception.DataAccessException;

public interface ICityCenterZipCodeLookupService {

   /**
    * Pull up the City Center Zip Code based on the city name and state code passed in.
    * 
    * @param cityName
    * @param stateCode
    * @return
    * @throws ExeCueException
    */
   public String getCityCenterZipCodeByCityNameStateCode (String cityName, String stateCode) throws DataAccessException;
}
