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


package com.execue.ac.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.service.IAnswersCatalogSQLQueryGenerationService;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryGenerationInput;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.type.AssetProviderType;
import com.execue.querygen.service.IQueryGenerationService;
import com.execue.querygen.service.QueryGenerationServiceFactory;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

/**
 * This service contains methods used to prepare sql query using SQLQueryGenerationService
 * 
 * @author Vishay
 * @version 1.0
 * @since 13/01/2011
 */
public class AnswersCatalogSQLQueryGenerationServiceImpl implements IAnswersCatalogSQLQueryGenerationService {

   private QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory;
   private QueryGenerationServiceFactory     queryGenerationServiceFactory;

   public String prepareSelectQuery (Asset asset, Query query) throws AnswersCatalogException {
      AssetProviderType providerType = asset.getDataSource().getProviderType();
      List<Query> queries = new ArrayList<Query>();
      queries.add(query);
      QueryGenerationInput queryGenerationInput = new QueryGenerationInput();
      queryGenerationInput.setInputQueries(queries);
      queryGenerationInput.setTargetAsset(asset);
      QueryGenerationOutput queryGenerationOutput = getQueryGenerationService(providerType).generateQuery(
               queryGenerationInput);
      String selectStatement = getQueryGenerationService(providerType).extractQueryRepresentation(asset,
               queryGenerationOutput.getResultQuery()).getQueryString();
      return selectStatement;
   }

   public String prepareAlterTableQuery (AssetProviderType assetProviderType, QueryTable queryTable,
            QueryColumn queryColumn) throws AnswersCatalogException {
      return getQueryGenerationUtilService(assetProviderType).createTableAlterStatement(queryTable, queryColumn);
   }

   public String prepareETLInsertQuery (AssetProviderType assetProviderType, QueryTable queryTable,
            List<QueryColumn> queryColumns, Map<String, String> selectQueryAliases) throws AnswersCatalogException {
      return getQueryGenerationUtilService(assetProviderType).createETLInsertStatement(queryTable, queryColumns,
               selectQueryAliases);
   }

   public String prepareInsertQuery (AssetProviderType assetProviderType, QueryTable queryTable,
            List<QueryColumn> queryColumns) throws AnswersCatalogException {
      return getQueryGenerationUtilService(assetProviderType).createInsertStatement(queryTable, queryColumns, false);
   }

   public String prepareInsertQueryWithValuesSection (AssetProviderType assetProviderType, QueryTable queryTable,
            List<QueryColumn> queryColumns) throws AnswersCatalogException {
      return getQueryGenerationUtilService(assetProviderType).createInsertStatement(queryTable, queryColumns, true);
   }

   public String prepareCreateTableQuery (AssetProviderType assetProviderType, QueryTable queryTable,
            List<QueryColumn> columns) throws AnswersCatalogException {
      IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(assetProviderType);
      return queryGenerationUtilService.createTableCreateStatement(queryTable, columns);
   }

   private IQueryGenerationUtilService getQueryGenerationUtilService (AssetProviderType assetProviderType) {
      return queryGenerationUtilServiceFactory.getQueryGenerationUtilService(assetProviderType);
   }

   private IQueryGenerationService getQueryGenerationService (AssetProviderType assetProviderType) {
      return queryGenerationServiceFactory.getQueryGenerationService(assetProviderType);
   }

   public QueryGenerationUtilServiceFactory getQueryGenerationUtilServiceFactory () {
      return queryGenerationUtilServiceFactory;
   }

   public void setQueryGenerationUtilServiceFactory (QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory) {
      this.queryGenerationUtilServiceFactory = queryGenerationUtilServiceFactory;
   }

   public QueryGenerationServiceFactory getQueryGenerationServiceFactory () {
      return queryGenerationServiceFactory;
   }

   public void setQueryGenerationServiceFactory (QueryGenerationServiceFactory queryGenerationServiceFactory) {
      this.queryGenerationServiceFactory = queryGenerationServiceFactory;
   }

}
