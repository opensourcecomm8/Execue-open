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

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.sdata.location.CountryCity;
import com.execue.core.common.bean.sdata.location.CountryState;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.bean.sdata.location.LocationSuggestTerm;
import com.execue.core.common.bean.sdata.location.StateCity;
import com.execue.core.common.type.LocationConversionType;
import com.execue.core.common.type.NormalizedLocationType;
import com.execue.sdata.exception.LocationException;

/**
 * @author john
 */
public interface ILocationRetrievalService {

   public List<Long> getConvertedLocationInfo (LocationConversionType locationConversionType, Long sourceId)
            throws LocationException;

   public List<Long> getConvertedLocationInfo (LocationConversionType locationConversionType, List<Long> sourceIds)
            throws LocationException;

   public Map<Long, List<Long>> getConvertedLocationInfoAsMap (LocationConversionType locationConversionType,
            List<Long> sourceIds) throws LocationException;

   public List<LocationPointInfo> getLocationPointsByZipCodes (List<String> zipCodes,
            NormalizedLocationType locationType) throws LocationException;

   public List<LocationPointInfo> getLocationPointsByBedIds (List<Long> locationBedIds,
            NormalizedLocationType locationType) throws LocationException;

   public boolean isLocationPointInfoExists (LocationPointInfo locationPointInfo) throws LocationException;

   public LocationPointInfo getMatchingLocationPointInfo (LocationPointInfo locationPointInfo) throws LocationException;

   public CountryCity getValidCountryCity (Long countryId, Long cityId) throws LocationException;

   public CountryState getValidCountryState (Long countryId, Long stateId) throws LocationException;

   public StateCity getValidStateCity (Long stateId, Long cityId) throws LocationException;

   public CountryCity findValidCountryCityCombination (Long countryId, List<Long> cityIds) throws LocationException;

   public CountryState findValidCountryStateCombination (Long countryId, List<Long> stateIds) throws LocationException;

   public StateCity findValidStateCityCombination (Long stateId, List<Long> cityIds) throws LocationException;

   public boolean isCountryCityCombinationExists (Long countryId, Long cityId) throws LocationException;

   public boolean isCountryStateCombinationExists (Long countryId, Long stateId) throws LocationException;

   public boolean isStateCityCombinationExists (Long stateId, Long cityId) throws LocationException;

   /**
    * This method tries to find out the valid StateCity combinations from the given city and state bed id lists
    */
   public List<StateCity> getValidStateCityCombinations (List<Long> stateIds, List<Long> cityIds)
            throws LocationException;

   /**
    * This method return the  list of LocationSuggestTerm by search string and given size
    * @param search
    * @param size
    * @return List<LocationSuggestTerm>
    * @throws LocationException
    */
   public List<LocationSuggestTerm> suggestLocationBySearchString (String search, Integer size)
            throws LocationException;

   public LocationPointInfo getLocationPointInfoByZipCode (String zipCode) throws LocationException;

   public LocationPointInfo getLocationPointInfoByLocationBedId (Long locationEntityBedId) throws LocationException;
}
