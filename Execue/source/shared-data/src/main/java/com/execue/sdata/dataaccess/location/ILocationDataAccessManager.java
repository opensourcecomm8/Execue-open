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


package com.execue.sdata.dataaccess.location;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.sdata.LocationInfo;
import com.execue.core.common.bean.sdata.location.CountryCity;
import com.execue.core.common.bean.sdata.location.CountryState;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.bean.sdata.location.StateCity;
import com.execue.core.common.type.LocationConversionType;
import com.execue.core.common.type.NormalizedLocationType;
import com.execue.sdata.exception.LocationException;

/**
 * @author john
 */
public interface ILocationDataAccessManager {

   public List<Long> getConvertedLocationInfo (LocationConversionType locationConversionType, Long sourceId)
            throws LocationException;

   public List<Long> getConvertedLocationInfo (LocationConversionType locationConversionType, List<Long> sourceIds)
            throws LocationException;

   public Map<Long, List<Long>> getConvertedLocationInfoAsMap (LocationConversionType locationConversionType,
            List<Long> sourceIds) throws LocationException;

   public void createLocationInfo (LocationInfo locationInfo) throws LocationException;

   public List<LocationPointInfo> getLocationPointsByZipCodes (List<String> zipCodes,
            NormalizedLocationType locationType) throws LocationException;

   public List<LocationPointInfo> getLocationPointsByBedIds (List<Long> locationBedIds,
            NormalizedLocationType locationType) throws LocationException;

   public void createCountryState (CountryState countryState) throws LocationException;

   public void createStateCity (StateCity stateCity) throws LocationException;

   public void createCountryCity (CountryCity countryCity) throws LocationException;

   public CountryCity getValidCountryCity (Long countryId, Long cityId) throws LocationException;

   public CountryState getValidCountryState (Long countryId, Long stateId) throws LocationException;

   public StateCity getValidStateCity (Long stateId, Long cityId) throws LocationException;

   public CountryCity findValidCountryCityCombination (Long countryId, List<Long> cityIds) throws LocationException;

   public CountryState findValidCountryStateCombination (Long countryId, List<Long> stateIds) throws LocationException;

   public StateCity findValidStateCityCombination (Long stateId, List<Long> cityIds) throws LocationException;

   public void createLocationPointInfo (LocationPointInfo locationPointInfo) throws LocationException;

   public LocationPointInfo getMatchingLocationPointInfo (LocationPointInfo locationPointInfo) throws LocationException;

   public LocationPointInfo getLocationPointInfoByZipCode (String zipCode) throws LocationException;

   public LocationPointInfo getLocationPointInfoByLocationBedId (Long locationEntityBedId) throws LocationException;

   public List<StateCity> getValidStateCityCombinations (List<Long> stateIds, List<Long> cityIds)
            throws LocationException;

   public List<LocationPointInfo> suggestLocationBySearchString (String search, Integer size) throws LocationException;
}
