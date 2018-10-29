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


package com.execue.sdata.service.impl.location;

import com.execue.core.common.bean.sdata.LocationInfo;
import com.execue.core.common.bean.sdata.location.CountryCity;
import com.execue.core.common.bean.sdata.location.CountryState;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.bean.sdata.location.StateCity;
import com.execue.sdata.dataaccess.location.ILocationDataAccessManager;
import com.execue.sdata.exception.LocationException;
import com.execue.sdata.service.ILocationManagementService;
import com.execue.sdata.service.ILocationRetrievalService;

/**
 * @author john
 */
public class LocationManagementServiceImpl implements ILocationManagementService {

   private ILocationDataAccessManager locationDataAccessManager;
   private ILocationRetrievalService  locationRetrievalService;

   public void createLocationInfo (LocationInfo locationInfo) throws LocationException {
      getLocationDataAccessManager().createLocationInfo(locationInfo);
   }

   public ILocationDataAccessManager getLocationDataAccessManager () {
      return locationDataAccessManager;
   }

   public void setLocationDataAccessManager (ILocationDataAccessManager locationDataAccessManager) {
      this.locationDataAccessManager = locationDataAccessManager;
   }

   @Override
   public void createCountryCity (Long countryId, Long cityId) throws LocationException {
      createCountryCity(new CountryCity(countryId, cityId));
   }

   @Override
   public void createCountryState (Long countryId, Long stateId) throws LocationException {
      createCountryState(new CountryState(countryId, stateId));
   }

   @Override
   public void createStateCity (Long stateId, Long cityId) throws LocationException {
      createStateCity(new StateCity(stateId, cityId));
   }

   @Override
   public void createCountryCity (CountryCity countryCity) throws LocationException {
      if (!getLocationRetrievalService().isCountryCityCombinationExists(countryCity.getCountryId(),
               countryCity.getCityId())) {
         getLocationDataAccessManager().createCountryCity(countryCity);
      }
   }

   @Override
   public void createCountryState (CountryState countryState) throws LocationException {
      if (!getLocationRetrievalService().isCountryStateCombinationExists(countryState.getCountryId(),
               countryState.getStateId())) {
         getLocationDataAccessManager().createCountryState(countryState);
      }
   }

   @Override
   public void createStateCity (StateCity stateCity) throws LocationException {
      if (!getLocationRetrievalService().isStateCityCombinationExists(stateCity.getStateId(), stateCity.getCityId())) {
         getLocationDataAccessManager().createStateCity(stateCity);
      }
   }

   public ILocationRetrievalService getLocationRetrievalService () {
      return locationRetrievalService;
   }

   public void setLocationRetrievalService (ILocationRetrievalService locationRetrievalService) {
      this.locationRetrievalService = locationRetrievalService;
   }

   @Override
   public void createLocationPointInfo (LocationPointInfo locationPointInfo) throws LocationException {
      if (!getLocationRetrievalService().isLocationPointInfoExists(locationPointInfo)) {
         getLocationDataAccessManager().createLocationPointInfo(locationPointInfo);
      }
   }

}
