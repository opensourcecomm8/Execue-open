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


package com.execue.platform.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.swi.PopularityInfo;
import com.execue.core.common.type.AssetEntityType;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.PopularityTermType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.swi.exception.SWIException;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

public class PopularityServiceHelper {

   private QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory;

   public Connection getConnection (String url, String driverClass, String userName, String password)
            throws SWIException {
      Connection connection = null;
      try {
         Class.forName(driverClass);
         connection = DriverManager.getConnection(url, userName, password);
      } catch (ClassNotFoundException classNotFoundException) {

      } catch (SQLException e) {

      }
      return connection;
   }

   public List<Integer> populateSQLTypesForPopularityInfo () {
      List<Integer> sqlTypes = new ArrayList<Integer>();
      sqlTypes.add(ExecueBeanUtil.getSQLDataType(DataType.STRING));
      sqlTypes.add(ExecueBeanUtil.getSQLDataType(DataType.INT));
      sqlTypes.add(ExecueBeanUtil.getSQLDataType(DataType.INT));
      sqlTypes.add(ExecueBeanUtil.getSQLDataType(DataType.NUMBER));
      return sqlTypes;
   }

   public List<String> tokenizeFullyQualifiedName (String fullyQualifiedName, String separator) {
      StringTokenizer tokenizer = new StringTokenizer(fullyQualifiedName, separator);
      List<String> tokens = new ArrayList<String>();
      while (tokenizer.hasMoreTokens()) {
         tokens.add(tokenizer.nextToken());
      }
      return tokens;
   }

   public PopularityTermType getAssetPopularityTermType (AssetEntityType assetEntityType) {
      PopularityTermType popularityTermType = null;
      switch (assetEntityType) {
         case ASSET:
            popularityTermType = PopularityTermType.ASSET;
            break;
         case TABLE:
            popularityTermType = PopularityTermType.TABL;
            break;
         case COLUMN:
            popularityTermType = PopularityTermType.COLUM;
            break;
         case MEMBER:
            popularityTermType = PopularityTermType.MEMBR;
      }
      return popularityTermType;
   }

   public PopularityTermType getBusinessPopularityTermType (BusinessEntityType businessEntityType) {
      PopularityTermType popularityTermType = null;
      switch (businessEntityType) {
         case CONCEPT:
            popularityTermType = PopularityTermType.CONCEPT;
            break;
         case CONCEPT_LOOKUP_INSTANCE:
            popularityTermType = PopularityTermType.INSTANCE;
      }
      return popularityTermType;
   }

   public List<List<Object>> getPopularityObjectList (List<PopularityInfo> popularityInfoList) {
      List<List<Object>> popularityObjectList = new ArrayList<List<Object>>();
      for (PopularityInfo popularityInfo : popularityInfoList) {
         List<Object> popularityObject = new ArrayList<Object>();
         popularityObject.add(popularityInfo.getFullyQualifiedName());
         popularityObject.add(popularityInfo.getPopularityTermType().getValue());
         popularityObject.add(popularityInfo.getHits());
         popularityObject.add(popularityInfo.getWeight());
         popularityObjectList.add(popularityObject);
      }
      return popularityObjectList;
   }

   public String preparePopularityInfoCreateStatement (AssetProviderType assetProviderType) {
      List<QueryColumn> queryColums = new ArrayList<QueryColumn>();
      queryColums.add(prepareQueryColumn("FULLY_QUALIFIED_NAME", DataType.STRING, 1024, 0, false));
      queryColums.add(prepareQueryColumn("POPULARITY_TERM_TYPE", DataType.INT, 1, 0, false));
      queryColums.add(prepareQueryColumn("HITS", DataType.INT, 10, 0, true));
      queryColums.add(prepareQueryColumn("WEIGHT", DataType.NUMBER, 22, 6, true));
      QueryTable queryTable = new QueryTable();
      queryTable.setTableName("POPULARITY_INFO");
      // TODO:: NK: Should pass and set the owner when we use this method for different provider types like MSSQL, SAS, Teradata, etc 
      // where owner reference is mandatory
      // Here owner should come from configuration as SWI/QDATA doesn't exist in the datasource table
      String createTableQuery = getQueryGenerationUtilService(assetProviderType).createTableCreateStatement(queryTable,
               queryColums);
      return createTableQuery;
   }

   public String preparePopularityInfoDropStatement (AssetProviderType assetProviderType) {
      QueryTable queryTable = new QueryTable();
      queryTable.setTableName("POPULARITY_INFO");
      // TODO:: NK: Should pass and set the owner when we use this method for different provider types like MSSQL, SAS, Teradata, etc 
      // where owner reference is mandatory
      // Here owner should come from configuration as SWI/QDATA doesn't exist in the datasource table
      String dropTableQuery = getQueryGenerationUtilService(assetProviderType).createTableDropStatement(queryTable);
      return dropTableQuery;
   }

   private QueryColumn prepareQueryColumn (String columnName, DataType dataType, int precision, int scale,
            boolean nullFlag) {
      QueryColumn queryColumn = new QueryColumn();
      queryColumn = new QueryColumn();
      queryColumn.setColumnName(columnName);
      queryColumn.setDataType(dataType);
      queryColumn.setPrecision(precision);
      queryColumn.setScale(scale);
      queryColumn.setNullable(nullFlag);
      return queryColumn;
   }

   public AssetProviderType getAssetProviderType (int providerType) {
      AssetProviderType assetProviderType = null;
      if (AssetProviderType.MySql.getValue() == providerType) {
         assetProviderType = AssetProviderType.MySql;
      } else if (AssetProviderType.Oracle.getValue() == providerType) {
         assetProviderType = AssetProviderType.Oracle;
      } else if (AssetProviderType.MSSql.getValue() == providerType) {
         assetProviderType = AssetProviderType.MSSql;
      } else if (AssetProviderType.Teradata.getValue() == providerType) {
         assetProviderType = AssetProviderType.Teradata;
      } else if (AssetProviderType.SAS_SHARENET.getValue() == providerType) {
         assetProviderType = AssetProviderType.SAS_SHARENET;
      } else if (AssetProviderType.SAS_WORKSPACE.getValue() == providerType) {
         assetProviderType = AssetProviderType.SAS_WORKSPACE;
      }
      return assetProviderType;
   }

   public QueryGenerationUtilServiceFactory getQueryGenerationUtilServiceFactory () {
      return queryGenerationUtilServiceFactory;
   }

   public void setQueryGenerationUtilServiceFactory (QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory) {
      this.queryGenerationUtilServiceFactory = queryGenerationUtilServiceFactory;
   }

   private IQueryGenerationUtilService getQueryGenerationUtilService (AssetProviderType assetProviderType) {
      return getQueryGenerationUtilServiceFactory().getQueryGenerationUtilService(assetProviderType);
   }
}
