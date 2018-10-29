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

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.das.configuration.IDataAccessServicesConfigurationService;
import com.execue.das.exception.DataAccessServicesExceptionCodes;
import com.execue.das.jdbc.service.IGenericJDBCService;
import com.execue.das.util.ICityCenterZipCodeLookupService;
import com.execue.dataaccess.exception.DataAccessException;

public class CityCenterZipCodeLookupServiceImpl implements ICityCenterZipCodeLookupService {

   private static final Logger                     log                              = Logger
                                                                                             .getLogger(CityCenterZipCodeLookupServiceImpl.class);

   private static final String                     CITY_CENTER_ZIPCODE_LOOKUP_QUERY = "SELECT zip_at_city_center FROM city_center_zip_code "
                                                                                             + "WHERE city_name = ? AND state_code = ?";

   private IGenericJDBCService                     genericJDBCService;
   private IDataAccessServicesConfigurationService dataAccessServicesConfigurationService;

   public IGenericJDBCService getGenericJDBCService () {
      return genericJDBCService;
   }

   public void setGenericJDBCService (IGenericJDBCService genericJDBCService) {
      this.genericJDBCService = genericJDBCService;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.util.ICityCenterZipCodeLookupService#getCityCenterZipCodeByCityNameStateCode(java.lang.String,
    *      java.lang.String)
    */
   @Override
   public String getCityCenterZipCodeByCityNameStateCode (String cityName, String stateCode) throws DataAccessException {
      String cityCenterZipCode = null;
      try {

         String cityCenterZipCodeDataSourceName = getDataAccessServicesConfigurationService()
                  .getCityCenterZipCodeDataSource();
         List<Integer> parameterTypes = new ArrayList<Integer>();
         List<Object> parameterValues = new ArrayList<Object>();
         populateParamTypesAndValues(cityName, stateCode, parameterTypes, parameterValues);

         ExeCueResultSet cityCenterZipCodeQueryResultSet = getGenericJDBCService().executeQuery(
                  cityCenterZipCodeDataSourceName, new SelectQueryInfo(CITY_CENTER_ZIPCODE_LOOKUP_QUERY),
                  parameterValues, parameterTypes);
         if (cityCenterZipCodeQueryResultSet.next()) {
            cityCenterZipCode = cityCenterZipCodeQueryResultSet.getString(0);
         }

      } catch (Exception e) {
         log.error("Error from getCityCenterZipCodeByCityNameStateCode(cityName, stateCode) : " + e.getMessage());
         throw new DataAccessException(DataAccessServicesExceptionCodes.CITY_CENTER_ZIPCODE_LOOKUP_FAILED, e);
      }
      return cityCenterZipCode;
   }

   private void populateParamTypesAndValues (String cityName, String stateCode, List<Integer> parameterTypes,
            List<Object> parameterValues) {

      parameterTypes.add(Types.VARCHAR);
      parameterTypes.add(Types.VARCHAR);
      parameterValues.add(cityName);
      parameterValues.add(stateCode);

   }

   public IDataAccessServicesConfigurationService getDataAccessServicesConfigurationService () {
      return dataAccessServicesConfigurationService;
   }

   public void setDataAccessServicesConfigurationService (
            IDataAccessServicesConfigurationService dataAccessServicesConfigurationService) {
      this.dataAccessServicesConfigurationService = dataAccessServicesConfigurationService;
   }

}
