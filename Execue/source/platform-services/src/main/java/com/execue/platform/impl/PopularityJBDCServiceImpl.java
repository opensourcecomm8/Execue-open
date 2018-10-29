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


package com.execue.platform.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.swi.PopularityInfo;
import com.execue.core.common.type.PopularityTermType;
import com.execue.swi.exception.SWIException;
import com.execue.platform.IPopularityJBDCService;
import com.execue.swi.exception.SWIExceptionCodes;

public class PopularityJBDCServiceImpl implements IPopularityJBDCService {

   public void insertIntoPopularityInfo (Connection connection, List<List<Object>> popularityInfo,
            List<Integer> sqlTypes) throws SWIException {
      try {
         String QUERY_INSERT_POPULARITY_INFO = "insert into popularity_info values(?,?,?,?)";

         PreparedStatement preparedStatement = connection.prepareStatement(QUERY_INSERT_POPULARITY_INFO);
         if (popularityInfo != null && sqlTypes != null) {
            for (List<Object> parameterValues : popularityInfo) {
               addToBatchPreparedStatement(connection, preparedStatement, parameterValues, sqlTypes);
            }
            preparedStatement.executeBatch();
         }
      } catch (SQLException sException) {
      }
   }

   public void createPopularityInfo (Connection connection, String createStatement) throws SWIException {
      try {
         Statement statement = connection.createStatement();
         statement.executeUpdate(createStatement);
      } catch (SQLException sqlException) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, sqlException);
      }
   }

   public void dropPopularityInfo (Connection connection, String dropStatement) throws SWIException {
      try {
         Statement statement = connection.createStatement();
         statement.executeUpdate(dropStatement);
      } catch (SQLException sqlException) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, sqlException);
      }
   }

   private void addToBatchPreparedStatement (Connection connection, PreparedStatement preparedStatement,
            List<Object> parameterValues, List<Integer> parameterTypes) throws SQLException {
      if (parameterValues != null && parameterTypes != null) {
         int index = 0;
         for (Object paramValue : parameterValues) {
            Integer sqlType = parameterTypes.get(index).intValue();
            if (paramValue == null) {
               sqlType = Types.NULL;
            }
            preparedStatement.setObject((index + 1), paramValue, sqlType);
            index++;
         }
      }
      preparedStatement.addBatch();
   }

   public List<PopularityInfo> getPopularityInfo (Connection connection, PopularityTermType popularityTermType)
            throws SWIException {
      List<PopularityInfo> popularityInfos = new ArrayList<PopularityInfo>();
      try {
         String QUERY_POPULARITY_INFO = "select * from popularity_info where popularity_term_type = "
                  + popularityTermType.getValue();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(QUERY_POPULARITY_INFO);
         while (resultSet.next()) {
            PopularityInfo popularityInfo = new PopularityInfo();
            popularityInfo.setFullyQualifiedName(resultSet.getString(1));
            popularityInfo.setPopularityTermType(PopularityTermType.getType(resultSet.getInt(2)));
            popularityInfo.setHits(new Long(resultSet.getInt(3)));
            popularityInfo.setWeight(resultSet.getDouble(4));
            popularityInfos.add(popularityInfo);
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return popularityInfos;
   }

}
