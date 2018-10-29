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


package com.execue.offline.batch.shareddata.location.helper.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.offline.batch.configuration.IOfflineBatchConfigurationService;
import com.execue.offline.batch.exception.OfflineBatchException;
import com.execue.offline.batch.exception.OfflineBatchExceptionCodes;
import com.execue.offline.batch.shareddata.location.helper.ILocationDataInputSourceWrapper;
import com.execue.platform.shareddata.location.bean.LocationDataInputInfo;

public class LocationDataInputSourceZipCodeDBWrapperImpl implements ILocationDataInputSourceWrapper {

   private static final Logger               log = Logger.getLogger(LocationDataInputSourceZipCodeDBWrapperImpl.class);

   private IOfflineBatchConfigurationService offlineBatchConfigurationService;

   public List<LocationDataInputInfo> getLocationDataInputInfo (int batchNumber) throws OfflineBatchException {
      List<LocationDataInputInfo> locationDataInputInfosFromZipCodeDB = new ArrayList<LocationDataInputInfo>();
      Connection connection = null;
      Statement statement = null;
      ResultSet rs = null;
      try {
         int batchStartRecord = ((batchNumber - 1) * getOfflineBatchConfigurationService()
                  .getLocationInputDataSourceBatchSize());
         int batchEndRecord = getOfflineBatchConfigurationService().getLocationInputDataSourceBatchSize();

         String sourceDataQuery = getOfflineBatchConfigurationService().getLocationInputDataSourceZipCodeDBSelectQuery()
                  + " " + batchStartRecord + "," + batchEndRecord;
         if (log.isInfoEnabled()) {
            log.info("Select Query : "+sourceDataQuery);
         } else {
            System.out.println("[LocationDataInputSourceZipCodeDBWrapperImpl::getLocationDataInputInfo] Select Query : "+sourceDataQuery);
         }
         connection = getLocationDataInputSourceConnection();

         statement = connection.createStatement();
         rs = statement.executeQuery(sourceDataQuery);
         while (rs.next()) {
            LocationDataInputInfo locationDataInputInfo = new LocationDataInputInfo();
            locationDataInputInfo.setZipCode(rs.getString("zip_code"));
            locationDataInputInfo.setLatitude(rs.getDouble("latitude"));
            locationDataInputInfo.setLongitude(rs.getDouble("longitude"));
            locationDataInputInfo.setCityName(rs.getString("city_name"));
            locationDataInputInfo.setStateName(rs.getString("state_code"));
            locationDataInputInfo.setCountryName(getOfflineBatchConfigurationService()
                     .getLocationInputDataSourceDefaultCountryName());
            locationDataInputInfosFromZipCodeDB.add(locationDataInputInfo);
         }
      } catch (SQLException sqle) {
         throw new OfflineBatchException(OfflineBatchExceptionCodes.LOCATION_SOURCE_DATA_RETRIEVAL_FAILED, sqle);
      } finally {
         try {
            if (rs != null) {
               rs.close();
            }
            if (statement != null) {
               statement.close();
            }
            if (connection != null) {
               connection.close();
            }
         } catch (SQLException sqle) {
            log.error("Exception from finally, IGNORED : " + sqle.getMessage());
            log.error(sqle, sqle);
         }
      }
      return locationDataInputInfosFromZipCodeDB;
   }

   /**
    * @return
    * @throws ClassNotFoundException
    * @throws SQLException
    */
   private Connection getLocationDataInputSourceConnection () throws OfflineBatchException {
      Connection connection = null;
      try {
         Class.forName(getOfflineBatchConfigurationService()
                  .getLocationInputDataSourceZipCodeDBConnectionDriverClassName());
         connection = DriverManager.getConnection(getOfflineBatchConfigurationService()
                  .getLocationInputDataSourceZipCodeDBConnectionURL(), getOfflineBatchConfigurationService()
                  .getLocationInputDataSourceZipCodeDBConnectionUsername(), getOfflineBatchConfigurationService()
                  .getLocationInputDataSourceZipCodeDBConnectionPassword());
      } catch (ClassNotFoundException cnfe) {
         throw new OfflineBatchException(
                  OfflineBatchExceptionCodes.FAILED_TO_GET_LOCATION_DATA_INPUT_SOURCE_CONNECTION, cnfe);
      } catch (SQLException sqle) {
         throw new OfflineBatchException(
                  OfflineBatchExceptionCodes.FAILED_TO_GET_LOCATION_DATA_INPUT_SOURCE_CONNECTION, sqle);
      }
      return connection;
   }

   public IOfflineBatchConfigurationService getOfflineBatchConfigurationService () {
      return offlineBatchConfigurationService;
   }

   public void setOfflineBatchConfigurationService (IOfflineBatchConfigurationService offlineBatchConfigurationService) {
      this.offlineBatchConfigurationService = offlineBatchConfigurationService;
   }

}
