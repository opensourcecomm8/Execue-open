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


package com.execue.sdata.service;

import com.execue.core.common.bean.sdata.LocationInfo;
import com.execue.core.common.bean.sdata.location.CountryCity;
import com.execue.core.common.bean.sdata.location.CountryState;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.bean.sdata.location.StateCity;
import com.execue.sdata.exception.LocationException;

/**
 * @author john
 */
public interface ILocationManagementService {

   public void createLocationInfo (LocationInfo locationInfo) throws LocationException;

   public void createLocationPointInfo (LocationPointInfo locationPointInfo) throws LocationException;

   public void createCountryState (CountryState countryState) throws LocationException;

   public void createStateCity (StateCity stateCity) throws LocationException;

   public void createCountryCity (CountryCity countryCity) throws LocationException;

   public void createCountryState (Long countryId, Long stateId) throws LocationException;

   public void createStateCity (Long stateId, Long cityId) throws LocationException;

   public void createCountryCity (Long countryId, Long cityId) throws LocationException;

}
