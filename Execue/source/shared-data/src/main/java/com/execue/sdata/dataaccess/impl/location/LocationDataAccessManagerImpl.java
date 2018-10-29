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


package com.execue.sdata.dataaccess.impl.location;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.sdata.LocationInfo;
import com.execue.core.common.bean.sdata.location.CountryCity;
import com.execue.core.common.bean.sdata.location.CountryState;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.bean.sdata.location.StateCity;
import com.execue.core.common.type.LocationConversionType;
import com.execue.core.common.type.NormalizedLocationType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.sdata.dao.ILocationDAO;
import com.execue.sdata.dataaccess.location.ILocationDataAccessManager;
import com.execue.sdata.exception.LocationException;

/**
 * @author john
 */
public class LocationDataAccessManagerImpl implements ILocationDataAccessManager {

   private ILocationDAO locationDAO;

   public List<Long> getConvertedLocationInfo (LocationConversionType locationConversionType, Long sourceId)
            throws LocationException {
      try {
         return getLocationDAO().getConvertedLocationInfo(locationConversionType, sourceId);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }

   public List<Long> getConvertedLocationInfo (LocationConversionType locationConversionType, List<Long> sourceIds)
            throws LocationException {
      try {
         return getLocationDAO().getConvertedLocationInfo(locationConversionType, sourceIds);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }

   public Map<Long, List<Long>> getConvertedLocationInfoAsMap (LocationConversionType locationConversionType,
            List<Long> sourceIds) throws LocationException {
      try {
         return getLocationDAO().getConvertedLocationInfoAsMap(locationConversionType, sourceIds);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }

   public void createLocationInfo (LocationInfo locationInfo) throws LocationException {
      Long cityId = locationInfo.getCityId();
      Long stateId = locationInfo.getStateId();
      Long countryId = locationInfo.getCountryId();

      StateCity stateCity = new StateCity();
      stateCity.setCityId(cityId);
      stateCity.setStateId(stateId);

      CountryCity countryCity = new CountryCity();
      countryCity.setCountryId(countryId);
      countryCity.setCityId(cityId);

      CountryState countryState = new CountryState();
      countryState.setCountryId(countryId);
      countryState.setStateId(stateId);

      try {
         getLocationDAO().create(countryState);
      } catch (DataAccessException dataAccessException) {
         throw new LocationException(dataAccessException.getCode(), "Error in saving CountryState", dataAccessException
                  .getCause());
      }
      try {
         getLocationDAO().create(stateCity);
      } catch (DataAccessException dataAccessException) {
         throw new LocationException(dataAccessException.getCode(), "Error in saving StateCity", dataAccessException
                  .getCause());
      }
      try {
         getLocationDAO().create(countryCity);
      } catch (DataAccessException dataAccessException) {
         throw new LocationException(dataAccessException.getCode(), "Error in saving CountryCity", dataAccessException
                  .getCause());
      }
   }

   @Override
   public List<LocationPointInfo> getLocationPointsByBedIds (List<Long> locationBedIds,
            NormalizedLocationType locationType) throws LocationException {
      try {
         return getLocationDAO().getLocationPointsByBedIds(locationBedIds, locationType);
      } catch (DataAccessException dataAccessException) {
         throw new LocationException(dataAccessException.getCode(), "Error in retrieving locationPointInfo",
                  dataAccessException.getCause());
      }
   }

   @Override
   public List<LocationPointInfo> getLocationPointsByZipCodes (List<String> zipCodes,
            NormalizedLocationType locationType) throws LocationException {
      try {
         return getLocationDAO().getLocationPointsByZipCodes(zipCodes, locationType);
      } catch (DataAccessException dataAccessException) {
         throw new LocationException(dataAccessException.getCode(), "Error in retrieving locationPointInfo",
                  dataAccessException.getCause());
      }
   }

   /**
    * @return the locationDAO
    */
   public ILocationDAO getLocationDAO () {
      return locationDAO;
   }

   /**
    * @param locationDAO
    *           the locationDAO to set
    */
   public void setLocationDAO (ILocationDAO locationDAO) {
      this.locationDAO = locationDAO;
   }

   @Override
   public CountryCity getValidCountryCity (Long countryId, Long cityId) throws LocationException {
      try {
         return locationDAO.getValidCountryCity(countryId, cityId);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }

   @Override
   public CountryState getValidCountryState (Long countryId, Long stateId) throws LocationException {
      try {
         return locationDAO.getValidCountryState(countryId, stateId);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }

   @Override
   public StateCity getValidStateCity (Long stateId, Long cityId) throws LocationException {
      try {
         return locationDAO.getValidStateCity(stateId, cityId);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }

   @Override
   public CountryCity findValidCountryCityCombination (Long countryId, List<Long> cityIds) throws LocationException {
      try {
         return locationDAO.findValidCountryCityCombination(countryId, cityIds);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }

   @Override
   public CountryState findValidCountryStateCombination (Long countryId, List<Long> stateIds) throws LocationException {
      try {
         return locationDAO.findValidCountryStateCombination(countryId, stateIds);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }

   @Override
   public StateCity findValidStateCityCombination (Long stateId, List<Long> cityIds) throws LocationException {
      try {
         return locationDAO.findValidStateCityCombination(stateId, cityIds);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }

   @Override
   public void createCountryCity (CountryCity countryCity) throws LocationException {
      try {
         locationDAO.create(countryCity);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }

   @Override
   public void createCountryState (CountryState countryState) throws LocationException {
      try {
         locationDAO.create(countryState);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }

   @Override
   public void createStateCity (StateCity stateCity) throws LocationException {
      try {
         locationDAO.create(stateCity);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }

   @Override
   public void createLocationPointInfo (LocationPointInfo locationPointInfo) throws LocationException {
      try {
         locationDAO.create(locationPointInfo);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }

   @Override
   public LocationPointInfo getMatchingLocationPointInfo (LocationPointInfo locationPointInfo) throws LocationException {
      LocationPointInfo matchedLocationPointInfo = null;
      try {
         // TODO : -VG- need to consider lat and long in query
         if (ExecueCoreUtil.isNotEmpty(locationPointInfo.getZipCode())) {
            matchedLocationPointInfo = getLocationDAO().getMatchingLocationPointInfoByZipCode(locationPointInfo);
         } else if (locationPointInfo.getLocationBedId() != null) {
            matchedLocationPointInfo = getLocationDAO().getMatchingLocationPointInfoByLocationBedId(locationPointInfo);
         }
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
      return matchedLocationPointInfo;
   }

   @Override
   public LocationPointInfo getLocationPointInfoByZipCode (String zipCode) throws LocationException {
      try {
         return getLocationDAO().getLocationPointInfoByZipCode(zipCode);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }

   }

   @Override
   public LocationPointInfo getLocationPointInfoByLocationBedId (Long locationEntityBedId) throws LocationException {
      try {
         return getLocationDAO().getLocationPointInfoByLocationBedId(locationEntityBedId);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }

   }

   @Override
   public List<StateCity> getValidStateCityCombinations (List<Long> stateIds, List<Long> cityIds)
            throws LocationException {
      List<StateCity> validStateCityCombinations = null;
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(stateIds) && ExecueCoreUtil.isCollectionNotEmpty(cityIds)) {
            validStateCityCombinations = getLocationDAO().getValidStateCityCombinations(stateIds, cityIds);
         }
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
      return validStateCityCombinations;
   }

   @Override
   public List<LocationPointInfo> suggestLocationBySearchString (String search, Integer size) throws LocationException {
      try {
         return getLocationDAO().suggestLocationBySearchString(search, size);
      } catch (DataAccessException e) {
         throw new LocationException(e.getCode(), e);
      }
   }
}
