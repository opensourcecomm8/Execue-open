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


package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StateAbbreviationUtility {

   private String     tableName;

   private String     columnName;

   private Connection connection;

   private void initialize (String url, String userName, String password, String tableName, String columnName)
            throws ClassNotFoundException, SQLException {
      this.tableName = tableName;
      this.columnName = columnName;
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection(url, userName, password);

   }

   private Map<String, String> populateStateAbbreviationMap () {
      Map<String, String> stateAbbreviationMap = new HashMap<String, String>();
      stateAbbreviationMap.put("AL", "ALABAMA");
      stateAbbreviationMap.put("AK", "ALASKA ");
      stateAbbreviationMap.put("AS", "AMERICAN SAMOA");
      stateAbbreviationMap.put("AZ", "ARIZONA");
      stateAbbreviationMap.put("AR", "ARKANSAS");
      stateAbbreviationMap.put("CA", "CALIFORNIA");
      stateAbbreviationMap.put("CO", "COLORADO");
      stateAbbreviationMap.put("CT", "CONNECTICUT");
      stateAbbreviationMap.put("DE", "DELAWARE");
      stateAbbreviationMap.put("DC", "DISTRICT OF COLUMBIA");
      stateAbbreviationMap.put("FM", "FEDERATED STATES OF MICRONESIA");
      stateAbbreviationMap.put("FL", "FLORIDA");
      stateAbbreviationMap.put("GA", "GEORGIA");
      stateAbbreviationMap.put("GU", "GUAM");
      stateAbbreviationMap.put("HI", "HAWAII");
      stateAbbreviationMap.put("ID", "IDAHO");
      stateAbbreviationMap.put("IL", "ILLINOIS");
      stateAbbreviationMap.put("IN", "INDIANA");
      stateAbbreviationMap.put("IA", "IOWA");
      stateAbbreviationMap.put("KS", "KANSAS");
      stateAbbreviationMap.put("KY", "KENTUCKY");
      stateAbbreviationMap.put("LA", "LOUISIANA");
      stateAbbreviationMap.put("ME", "MAINE");
      stateAbbreviationMap.put("MH", "MARSHALL ISLANDS");
      stateAbbreviationMap.put("MD", "MARYLAND");
      stateAbbreviationMap.put("MA", "MASSACHUSETTS");
      stateAbbreviationMap.put("MI", "MICHIGAN");
      stateAbbreviationMap.put("MN", "MINNESOTA");
      stateAbbreviationMap.put("MS", "MISSISSIPPI");
      stateAbbreviationMap.put("MO", "MISSOURI");
      stateAbbreviationMap.put("MT", "MONTANA");
      stateAbbreviationMap.put("NE", "NEBRASKA");
      stateAbbreviationMap.put("NV", "NEVADA");
      stateAbbreviationMap.put("NH", "NEW HAMPSHIRE");
      stateAbbreviationMap.put("NJ", "NEW JERSEY");
      stateAbbreviationMap.put("NM", "NEW MEXICO");
      stateAbbreviationMap.put("NY", "NEW YORK");
      stateAbbreviationMap.put("NC", "NORTH CAROLINA");
      stateAbbreviationMap.put("ND", "NORTH DAKOTA");
      stateAbbreviationMap.put("MP", "NORTHERN MARIANA ISLANDS");
      stateAbbreviationMap.put("OH", "OHIO");
      stateAbbreviationMap.put("OK", "OKLAHOMA");
      stateAbbreviationMap.put("OR", "OREGON");
      stateAbbreviationMap.put("PW", "PALAU");
      stateAbbreviationMap.put("PA", "PENNSYLVANIA");
      stateAbbreviationMap.put("PR", "PUERTO RICO");
      stateAbbreviationMap.put("RI", "RHODE ISLAND");
      stateAbbreviationMap.put("SC", "SOUTH CAROLINA");
      stateAbbreviationMap.put("SD", "SOUTH DAKOTA");
      stateAbbreviationMap.put("TN", "TENNESSEE");
      stateAbbreviationMap.put("TX", "TEXAS");
      stateAbbreviationMap.put("UT", "UTAH");
      stateAbbreviationMap.put("VT", "VERMONT");
      stateAbbreviationMap.put("VI", "VIRGIN ISLANDS");
      stateAbbreviationMap.put("VA", "VIRGINIA");
      stateAbbreviationMap.put("WA", "WASHINGTON");
      stateAbbreviationMap.put("WI", "WISCONSIN");
      stateAbbreviationMap.put("WY", "WYOMING");
      return stateAbbreviationMap;
   }

   private void modifyStateAbbreviations (Map<String, String> stateAbbreviationMap) throws SQLException {
      String updateSQL = "update " + tableName + " set " + columnName + " = ? where " + columnName + " = ?";
      PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
      for (String stateAbbreviation : stateAbbreviationMap.keySet()) {
         preparedStatement.setString(2, stateAbbreviation);
         preparedStatement.setString(1, stateAbbreviationMap.get(stateAbbreviation));
         preparedStatement.executeUpdate();
      }
   }

   public void updateStateAbbreviations (String url, String userName, String password, String tableName,
            String columnName) throws SQLException, ClassNotFoundException {
      initialize(url, userName, password, tableName, columnName);
      Map<String, String> stateAbbreviationMap = populateStateAbbreviationMap();
      modifyStateAbbreviations(stateAbbreviationMap);
   }

}