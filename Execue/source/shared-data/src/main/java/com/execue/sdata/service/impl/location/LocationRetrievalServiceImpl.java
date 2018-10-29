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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.sdata.location.CountryCity;
import com.execue.core.common.bean.sdata.location.CountryState;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.bean.sdata.location.LocationSuggestTerm;
import com.execue.core.common.bean.sdata.location.StateCity;
import com.execue.core.common.type.LocationConversionType;
import com.execue.core.common.type.NormalizedLocationType;
import com.execue.core.common.type.SuggestTermType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.sdata.dataaccess.location.ILocationDataAccessManager;
import com.execue.sdata.exception.LocationException;
import com.execue.sdata.service.ILocationRetrievalService;

/**
 * @author john
 */
public class LocationRetrievalServiceImpl implements ILocationRetrievalService {

   private ILocationDataAccessManager locationDataAccessManager;

   public List<Long> getConvertedLocationInfo (LocationConversionType locationConversionType, Long sourceId)
            throws LocationException {
      return getLocationDataAccessManager().getConvertedLocationInfo(locationConversionType, sourceId);
   }

   public List<Long> getConvertedLocationInfo (LocationConversionType locationConversionType, List<Long> sourceIds)
            throws LocationException {
      return getLocationDataAccessManager().getConvertedLocationInfo(locationConversionType, sourceIds);
   }

   public Map<Long, List<Long>> getConvertedLocationInfoAsMap (LocationConversionType locationConversionType,
            List<Long> sourceIds) throws LocationException {
      return getLocationDataAccessManager().getConvertedLocationInfoAsMap(locationConversionType, sourceIds);
   }

   @Override
   public List<LocationPointInfo> getLocationPointsByBedIds (List<Long> locationBedIds,
            NormalizedLocationType locationType) throws LocationException {
      return getLocationDataAccessManager().getLocationPointsByBedIds(locationBedIds, locationType);
   }

   @Override
   public List<LocationPointInfo> getLocationPointsByZipCodes (List<String> zipCodes,
            NormalizedLocationType locationType) throws LocationException {
      return getLocationDataAccessManager().getLocationPointsByZipCodes(zipCodes, locationType);
   }

   /**
    * @return the locationRetreivalDataAccessManager
    */
   public ILocationDataAccessManager getLocationDataAccessManager () {
      return locationDataAccessManager;
   }

   /**
    * @param locationRetreivalDataAccessManager
    *           the locationRetreivalDataAccessManager to set
    */
   public void setLocationDataAccessManager (ILocationDataAccessManager locationRetreivalDataAccessManager) {
      this.locationDataAccessManager = locationRetreivalDataAccessManager;
   }

   public CountryCity getValidCountryCity (Long countryId, Long cityId) throws LocationException {
      return getLocationDataAccessManager().getValidCountryCity(countryId, cityId);
   }

   public CountryState getValidCountryState (Long countryId, Long stateId) throws LocationException {
      return getLocationDataAccessManager().getValidCountryState(countryId, stateId);
   }

   public StateCity getValidStateCity (Long stateId, Long cityId) throws LocationException {
      return getLocationDataAccessManager().getValidStateCity(stateId, cityId);
   }

   public boolean isCountryCityCombinationExists (Long countryId, Long cityId) throws LocationException {
      boolean isCountryCityCombinationExists = false;
      CountryCity countryCity = getValidCountryCity(countryId, cityId);
      if (countryCity != null) {
         isCountryCityCombinationExists = true;
      }
      return isCountryCityCombinationExists;
   }

   public boolean isCountryStateCombinationExists (Long countryId, Long stateId) throws LocationException {
      boolean isCountryStateCombinationExists = false;
      CountryState countryState = getValidCountryState(countryId, stateId);
      if (countryState != null) {
         isCountryStateCombinationExists = true;
      }
      return isCountryStateCombinationExists;
   }

   public boolean isStateCityCombinationExists (Long stateId, Long cityId) throws LocationException {
      boolean isStateCityCombinationExists = false;
      StateCity stateCity = getValidStateCity(stateId, cityId);
      if (stateCity != null) {
         isStateCityCombinationExists = true;
      }
      return isStateCityCombinationExists;
   }

   @Override
   public CountryCity findValidCountryCityCombination (Long countryId, List<Long> cityIds) throws LocationException {
      return getLocationDataAccessManager().findValidCountryCityCombination(countryId, cityIds);
   }

   @Override
   public CountryState findValidCountryStateCombination (Long countryId, List<Long> stateIds) throws LocationException {
      return getLocationDataAccessManager().findValidCountryStateCombination(countryId, stateIds);
   }

   @Override
   public StateCity findValidStateCityCombination (Long stateId, List<Long> cityIds) throws LocationException {
      return getLocationDataAccessManager().findValidStateCityCombination(stateId, cityIds);
   }

   @Override
   public LocationPointInfo getMatchingLocationPointInfo (LocationPointInfo locationPointInfo) throws LocationException {
      return getLocationDataAccessManager().getMatchingLocationPointInfo(locationPointInfo);
   }

   @Override
   public boolean isLocationPointInfoExists (LocationPointInfo locationPointInfo) throws LocationException {
      boolean isLocationPointExists = false;
      LocationPointInfo matchingLocationPointInfo = getMatchingLocationPointInfo(locationPointInfo);
      if (matchingLocationPointInfo != null) {
         isLocationPointExists = true;
      }
      return isLocationPointExists;
   }

   @Override
   public List<StateCity> getValidStateCityCombinations (List<Long> stateIds, List<Long> cityIds)
            throws LocationException {
      return getLocationDataAccessManager().getValidStateCityCombinations(stateIds, cityIds);
   }

   @Override
   public List<LocationSuggestTerm> suggestLocationBySearchString (String search, Integer size)
            throws LocationException {
      List<LocationSuggestTerm> locationSuggestTerms = new ArrayList<LocationSuggestTerm>();
      List<LocationPointInfo> locationTerms = getLocationDataAccessManager()
               .suggestLocationBySearchString(search, size);
      if (ExecueCoreUtil.isCollectionNotEmpty(locationTerms)) {
         for (LocationPointInfo locationPointInfo : locationTerms) {
            LocationSuggestTerm locationSuggestTerm = new LocationSuggestTerm();
            locationSuggestTerm.setId(locationPointInfo.getId());
            locationSuggestTerm.setDisplayName(locationPointInfo.getLocationDisplayName());
            locationSuggestTerm.setType(SuggestTermType.LOCATION);
            locationSuggestTerm.setLatitude(locationPointInfo.getLatitude());
            locationSuggestTerm.setLongitude(locationPointInfo.getLongitude());
            locationSuggestTerm.setLocationBedId(locationPointInfo.getLocationBedId());
            locationSuggestTerms.add(locationSuggestTerm);
         }
      }
      return locationSuggestTerms;
   }

   @Override
   public LocationPointInfo getLocationPointInfoByLocationBedId (Long locationEntityBedId) throws LocationException {
      return getLocationDataAccessManager().getLocationPointInfoByLocationBedId(locationEntityBedId);
   }

   @Override
   public LocationPointInfo getLocationPointInfoByZipCode (String zipCode) throws LocationException {
      return getLocationDataAccessManager().getLocationPointInfoByZipCode(zipCode);
   }
}
