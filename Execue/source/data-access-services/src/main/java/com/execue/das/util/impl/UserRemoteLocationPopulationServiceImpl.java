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


package com.execue.das.util.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.bean.security.UserRemoteLocationInfo;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.das.configuration.IDataAccessServicesConfigurationService;
import com.execue.das.exception.DataAccessServicesExceptionCodes;
import com.execue.das.jdbc.service.IGenericJDBCService;
import com.execue.das.util.ICityCenterZipCodeLookupService;
import com.execue.das.util.IUserRemoteLocationPopulationService;
import com.execue.dataaccess.exception.DataAccessException;

/**
 * @author Vishay
 */
public class UserRemoteLocationPopulationServiceImpl implements IUserRemoteLocationPopulationService {

   private IGenericJDBCService                     genericJDBCService;
   private IDataAccessServicesConfigurationService dataAccessServicesConfigurationService;
   private ICityCenterZipCodeLookupService         cityCenterZipCodeLookupService;
   private static final String                     remoteLocationLookupQuery = "SELECT iplocationdb_location.country AS country_code, iplocationdb_country.name AS country_name, iplocationdb_location.region AS state_code, iplocationdb_region.name AS state_name, iplocationdb_location.city AS city_name, iplocationdb_location.postalcode AS zip_code, iplocationdb_location.latitude, iplocationdb_location.longitude FROM iplocationdb_ip LEFT JOIN iplocationdb_location ON iplocationdb_location.id = iplocationdb_ip.location_id LEFT JOIN iplocationdb_country ON iplocationdb_country.code = iplocationdb_location.country LEFT JOIN iplocationdb_region ON iplocationdb_region.country_code = iplocationdb_location.country AND iplocationdb_region.region_code = iplocationdb_location.region WHERE iplocationdb_ip.prefix=? AND ? BETWEEN iplocationdb_ip.start_ip AND iplocationdb_ip.end_ip";

   public UserRemoteLocationInfo populateUserRemoteLocationInfo (String ipAddress) throws DataAccessException {
      UserRemoteLocationInfo userRemoteLocationInfo = null;
      try {
         String remoteIPLocationDataSourceName = getDataAccessServicesConfigurationService()
                  .getRemoteIPLocationDataSource();
         if (remoteIPLocationDataSourceName != null && isIPAddressValid(ipAddress)) {
            List<Integer> parameterTypes = new ArrayList<Integer>();
            List<Object> parameterValues = new ArrayList<Object>();
            populateParamTypesAndValues(ipAddress, parameterTypes, parameterValues);
            ExeCueResultSet ipLocationQueryResultSet = getGenericJDBCService().executeQuery(
                     remoteIPLocationDataSourceName, new SelectQueryInfo(remoteLocationLookupQuery), parameterValues,
                     parameterTypes);
            userRemoteLocationInfo = populateRemoteLocationInfo(ipAddress, ipLocationQueryResultSet);
         }
      } catch (Exception e) {
         throw new DataAccessException(DataAccessServicesExceptionCodes.REMOTE_USER_LOCATION_DATA_RETRIEVAL_FAILED, e);
      }
      return userRemoteLocationInfo;
   }

   private void populateParamTypesAndValues (String ipAddress, List<Integer> parameterTypes,
            List<Object> parameterValues) {
      Integer ipValueAsInteger = getIPValueAsInteger(ipAddress);
      Integer ipValuePrefix = getIPValuePrefix(ipValueAsInteger);
      parameterTypes.add(Types.INTEGER);
      parameterTypes.add(Types.INTEGER);
      parameterValues.add(ipValuePrefix);
      parameterValues.add(ipValueAsInteger);
   }

   private UserRemoteLocationInfo populateRemoteLocationInfo (String ipAddress, ExeCueResultSet exeCueResultSet)
            throws Exception {
      UserRemoteLocationInfo userRemoteLocationInfo = null;
      boolean rowExists = exeCueResultSet.next();
      if (rowExists) {
         userRemoteLocationInfo = new UserRemoteLocationInfo();
         userRemoteLocationInfo.setIpAddress(ipAddress);
         userRemoteLocationInfo.setCountryCode(exeCueResultSet.getString(0));
         userRemoteLocationInfo.setCountryName(exeCueResultSet.getString(1));
         userRemoteLocationInfo.setStateCode(exeCueResultSet.getString(2));
         userRemoteLocationInfo.setStateName(exeCueResultSet.getString(3));
         userRemoteLocationInfo.setCityName(exeCueResultSet.getString(4));
         userRemoteLocationInfo.setZipCode(exeCueResultSet.getString(5));
         userRemoteLocationInfo.setLatitude(exeCueResultSet.getDouble(6).toString());
         userRemoteLocationInfo.setLongitude(exeCueResultSet.getDouble(7).toString());
      }
      return userRemoteLocationInfo;
   }

   private boolean isIPAddressValid (String ipAddress) {
      boolean ipAddressValid = false;
      String[] addressComponents = ipAddress.split("\\.");
      if (addressComponents.length == 4) {
         try {
            for (int index = 0; index < 4; index++) {
               Integer.parseInt(addressComponents[index]);
            }
            ipAddressValid = true;
         } catch (NumberFormatException numberFormatException) {
            ipAddressValid = false;
         }
      }
      return ipAddressValid;
   }

   private Integer getIPValueAsInteger (String ipAddress) {
      // TODO : -VG- we can directly use InetAddress.getByName(ipAddress).hashCode().
      String[] addressComponents = ipAddress.split("\\.");
      return (int) ((Integer.parseInt(addressComponents[0])) * Math.pow(256, 3) + (Integer
               .parseInt(addressComponents[1]))
               * Math.pow(256, 2))
               + (Integer.parseInt(addressComponents[2]) * 256 + (Integer.parseInt(addressComponents[3])));
   }

   private Integer getIPValuePrefix (Integer ipAddressAsInteger) {
      return ipAddressAsInteger >> 24;
   }

   public IGenericJDBCService getGenericJDBCService () {
      return genericJDBCService;
   }

   public void setGenericJDBCService (IGenericJDBCService genericJDBCService) {
      this.genericJDBCService = genericJDBCService;
   }

   public IDataAccessServicesConfigurationService getDataAccessServicesConfigurationService () {
      return dataAccessServicesConfigurationService;
   }

   public void setDataAccessServicesConfigurationService (
            IDataAccessServicesConfigurationService dataAccessServicesConfigurationService) {
      this.dataAccessServicesConfigurationService = dataAccessServicesConfigurationService;
   }

   @Override
   public UserRemoteLocationInfo populateUserRemoteLocationInfoWithCityCenterZipCode (String ipAddress)
            throws DataAccessException {
      UserRemoteLocationInfo userRemoteLocationInfo = populateUserRemoteLocationInfo(ipAddress);
      if (ExecueCoreUtil.isNotEmpty(userRemoteLocationInfo.getCityName())
               && ExecueCoreUtil.isNotEmpty(userRemoteLocationInfo.getStateCode())) {
         String cityCenterZipCode = getCityCenterZipCodeLookupService().getCityCenterZipCodeByCityNameStateCode(
                  userRemoteLocationInfo.getCityName(), userRemoteLocationInfo.getStateCode());
         if (ExecueCoreUtil.isNotEmpty(cityCenterZipCode)) {
            userRemoteLocationInfo.setCityCenterZipCode(cityCenterZipCode);
         }
      }
      return userRemoteLocationInfo;
   }

   public ICityCenterZipCodeLookupService getCityCenterZipCodeLookupService () {
      return cityCenterZipCodeLookupService;
   }

   public void setCityCenterZipCodeLookupService (ICityCenterZipCodeLookupService cityCenterZipCodeLookupService) {
      this.cityCenterZipCodeLookupService = cityCenterZipCodeLookupService;
   }

}
