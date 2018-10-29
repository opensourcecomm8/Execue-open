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


package com.execue.platform.shareddata.location.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.sdata.location.CountryState;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.bean.sdata.location.StateCity;
import com.execue.core.common.type.LocationType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.shareddata.location.ILocationDataPopulationService;
import com.execue.platform.shareddata.location.bean.LocationDataInputInfo;
import com.execue.platform.shareddata.location.bean.LocationDataOutputInfo;
import com.execue.platform.swi.shared.location.impl.LocationSharedModelServiceImpl;
import com.execue.sdata.exception.LocationException;
import com.execue.sdata.exception.SharedModelException;
import com.execue.sdata.service.ILocationManagementService;
import com.execue.sdata.service.ILocationRetrievalService;

/**
 * @author Vishay
 */
public class LocationDataPopulationServiceImpl implements ILocationDataPopulationService {

   private static final Logger            log = Logger.getLogger(LocationDataPopulationServiceImpl.class);
   private ILocationManagementService     locationManagementService;
   private ILocationRetrievalService      locationRetrievalService;
   private LocationSharedModelServiceImpl locationSharedModelService;

   @Override
   public boolean absorbLocationData (LocationDataInputInfo locationDataInputInfo) throws PlatformException {
      boolean absorbSuccessful = false;
      try {
         boolean isLocationDataInputInfoValid = validateLocationDataInputInfo(locationDataInputInfo);
         if (isLocationDataInputInfoValid) {
            LocationType locationType = LocationType.LAT_LONG;
            Long locationBedId = null;
            String zipCode = null;
            LocationDataOutputInfo locationDataOutputInfo = populateLocationDataOutputInfo(locationDataInputInfo);
            if (locationDataOutputInfo != null) {
               populateLocationSharedDataTables(locationDataOutputInfo);
               locationType = locationDataOutputInfo.getLocationType();
               locationBedId = getLocationBedId(locationDataOutputInfo);
            }
            // if zip is present, it means primary records is for zip hence we need to change locationtype to zip but
            // locationBedId can go as secondary information
            if (ExecueCoreUtil.isNotEmpty(locationDataInputInfo.getZipCode())) {
               locationType = LocationType.ZIPCODE;
               zipCode = locationDataInputInfo.getZipCode();
            }
            populateLocationPointInfo(locationType, locationBedId, zipCode, locationDataInputInfo);
            absorbSuccessful = true;
         }
      } catch (SharedModelException sharedModelException) {
         throw new PlatformException(sharedModelException.getCode(), sharedModelException);
      }
      return absorbSuccessful;
   }

   private void populateLocationPointInfo (LocationType locationType, Long locationBedId, String zipCode,
            LocationDataInputInfo locationDataInputInfo) throws LocationException {
      LocationPointInfo locationPointInfo = new LocationPointInfo();
      locationPointInfo.setLatitude(locationDataInputInfo.getLatitude());
      locationPointInfo.setLongitude(locationDataInputInfo.getLongitude());
      locationPointInfo.setLocationBedId(locationBedId);
      locationPointInfo.setNormalizedLocationType(ExecueBeanUtil.getNormalizedLocationType(locationType));
      locationPointInfo.setZipCode(zipCode);
      locationPointInfo.setLocation("Point(" + locationDataInputInfo.getLongitude() + ","
               + locationDataInputInfo.getLatitude() + ")");

      // TODO : -RG- When ever needed, other types also needs to be handled for display name
      if (LocationType.ZIPCODE == locationType) {
         locationPointInfo.setLocationDisplayName(locationPointInfo.getZipCode());
      } else if (LocationType.CITY == locationType) {
         locationPointInfo.setLocationDisplayName(locationDataInputInfo.getCityName() + ", "
                  + locationDataInputInfo.getStateName());
      }

      getLocationManagementService().createLocationPointInfo(locationPointInfo);
   }

   private Long getLocationBedId (LocationDataOutputInfo locationDataOutputInfo) {
      Long locationBedId = null;
      LocationType locationType = locationDataOutputInfo.getLocationType();
      if (LocationType.COUNTRY.equals(locationType)) {
         locationBedId = locationDataOutputInfo.getCountryBedId();
      } else if (LocationType.STATE.equals(locationType)) {
         locationBedId = locationDataOutputInfo.getStateBedId();
      } else if (LocationType.CITY.equals(locationType)) {
         locationBedId = locationDataOutputInfo.getCityBedId();
      }
      return locationBedId;
   }

   private void populateLocationSharedDataTables (LocationDataOutputInfo locationDataOutputInfo)
            throws LocationException {
      LocationType locationType = locationDataOutputInfo.getLocationType();
      if (LocationType.COUNTRY.equals(locationType)) {
         // do nothing as no conversion can be done just on country
      } else if (LocationType.STATE.equals(locationType)) {
         getLocationManagementService().createCountryState(locationDataOutputInfo.getCountryBedId(),
                  locationDataOutputInfo.getStateBedId());
      } else if (LocationType.CITY.equals(locationType)) {
         getLocationManagementService().createCountryState(locationDataOutputInfo.getCountryBedId(),
                  locationDataOutputInfo.getStateBedId());
         getLocationManagementService().createCountryCity(locationDataOutputInfo.getCountryBedId(),
                  locationDataOutputInfo.getCityBedId());
         getLocationManagementService().createStateCity(locationDataOutputInfo.getStateBedId(),
                  locationDataOutputInfo.getCityBedId());
      }
   }

   private LocationDataOutputInfo populateLocationDataOutputInfo (LocationDataInputInfo locationDataInputInfo)
            throws SharedModelException {
      LocationDataOutputInfo locationDataOutputInfo = null;
      String countryName = locationDataInputInfo.getCountryName();
      String stateName = locationDataInputInfo.getStateName();
      String cityName = locationDataInputInfo.getCityName();
      if (ExecueCoreUtil.isNotEmpty(countryName) || ExecueCoreUtil.isNotEmpty(stateName)
               || ExecueCoreUtil.isNotEmpty(cityName)) {
         LocationType locationType = null;
         Long countryBedId = null;
         Long stateBedId = null;
         Long cityBedId = null;
         if (ExecueCoreUtil.isNotEmpty(cityName)) {
            countryBedId = populateCountryBedId(countryName);
            stateBedId = populateStateBedId(stateName, countryBedId);
            cityBedId = populateCityBedId(cityName, stateBedId);
            locationType = LocationType.CITY;
         } else if (ExecueCoreUtil.isNotEmpty(stateName)) {
            countryBedId = populateCountryBedId(countryName);
            stateBedId = populateStateBedId(stateName, countryBedId);
            locationType = LocationType.STATE;
         } else if (ExecueCoreUtil.isNotEmpty(countryName)) {
            countryBedId = populateCountryBedId(countryName);
            locationType = LocationType.COUNTRY;
         }
         locationDataOutputInfo = new LocationDataOutputInfo(locationType, countryBedId, stateBedId, cityBedId);
      }
      return locationDataOutputInfo;
   }

   private Long populateCityBedId (String cityName, Long stateBedId) throws SharedModelException {
      Long cityBedId = null;
      List<Long> matchingCityInstances = getLocationSharedModelService().findMatchingCityInstance(cityName);
      if (ExecueCoreUtil.isCollectionEmpty(matchingCityInstances)) {
         cityBedId = getLocationSharedModelService().createRealTimeCityInstance(cityName);
      } else {
         StateCity validStateCityCombination = getLocationRetrievalService().findValidStateCityCombination(stateBedId,
                  matchingCityInstances);
         if (validStateCityCombination == null) {
            cityBedId = getLocationSharedModelService().createRealTimeCityInstance(cityName);
         } else {
            cityBedId = validStateCityCombination.getCityId();
         }
      }
      return cityBedId;
   }

   private Long populateStateBedId (String stateName, Long countryBedId) throws SharedModelException {
      Long stateBedId = null;
      List<Long> matchingStateInstances = getLocationSharedModelService().findMatchingStateInstance(stateName);
      if (ExecueCoreUtil.isCollectionEmpty(matchingStateInstances)) {
         stateBedId = getLocationSharedModelService().createRealTimeStateInstance(stateName);
      } else {
         CountryState validCountryStateCombination = getLocationRetrievalService().findValidCountryStateCombination(
                  countryBedId, matchingStateInstances);
         if (validCountryStateCombination == null) {
            stateBedId = getLocationSharedModelService().createRealTimeStateInstance(stateName);
         } else {
            stateBedId = validCountryStateCombination.getStateId();
         }
      }
      return stateBedId;
   }

   private Long populateCountryBedId (String countryName) throws SharedModelException {
      Long countryBedId = null;
      List<Long> matchingCountryInstances = getLocationSharedModelService().findMatchingCountryInstance(countryName);
      if (ExecueCoreUtil.isCollectionEmpty(matchingCountryInstances)) {
         countryBedId = getLocationSharedModelService().createRealTimeCountryInstance(countryName);
      } else {
         if (matchingCountryInstances.size() > 1) {
            log.error("More than one country exists with name " + countryName);
         }
         countryBedId = matchingCountryInstances.get(0);
      }
      return countryBedId;
   }

   private boolean validateLocationDataInputInfo (LocationDataInputInfo locationDataInputInfo) {
      boolean isValid = true;
      if (locationDataInputInfo.getLatitude() == null || locationDataInputInfo.getLongitude() == null) {
         log.error("Required Information Missing such as lat & long");
         isValid = false;
      }
      if (ExecueCoreUtil.isNotEmpty(locationDataInputInfo.getCountryName())
               || ExecueCoreUtil.isNotEmpty(locationDataInputInfo.getStateName())
               || ExecueCoreUtil.isNotEmpty(locationDataInputInfo.getCityName())) {
         if (ExecueCoreUtil.isNotEmpty(locationDataInputInfo.getCityName())) {
            if (ExecueCoreUtil.isEmpty(locationDataInputInfo.getStateName())
                     || ExecueCoreUtil.isEmpty(locationDataInputInfo.getCountryName())) {
               log.error("City Triple not present properly");
               isValid = false;
            }
         } else if (ExecueCoreUtil.isNotEmpty(locationDataInputInfo.getStateName())) {
            if (ExecueCoreUtil.isEmpty(locationDataInputInfo.getCountryName())) {
               log.error("State Triple not present properly");
               isValid = false;
            }
         }
      }
      return isValid;
   }

   public ILocationManagementService getLocationManagementService () {
      return locationManagementService;
   }

   public void setLocationManagementService (ILocationManagementService locationManagementService) {
      this.locationManagementService = locationManagementService;
   }

   public ILocationRetrievalService getLocationRetrievalService () {
      return locationRetrievalService;
   }

   public void setLocationRetrievalService (ILocationRetrievalService locationRetrievalService) {
      this.locationRetrievalService = locationRetrievalService;
   }

   public void setLocationSharedModelService (LocationSharedModelServiceImpl locationSharedModelService) {
      this.locationSharedModelService = locationSharedModelService;
   }

   public LocationSharedModelServiceImpl getLocationSharedModelService () {
      return locationSharedModelService;
   }

}
