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


package com.execue.ac.service;

import java.util.List;
import java.util.Map;

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.type.AssetProviderType;

/**
 * This service contains methods used to prepare sql query using SQLQueryGenerationService
 * 
 * @author Vishay
 * @version 1.0
 * @since 13/01/2011
 */

public interface IAnswersCatalogSQLQueryGenerationService {

   public String prepareSelectQuery (Asset asset, Query query) throws AnswersCatalogException;

   public String prepareETLInsertQuery (AssetProviderType assetProviderType, QueryTable queryTable,
            List<QueryColumn> columns, Map<String, String> selectQueryAliases) throws AnswersCatalogException;

   public String prepareCreateTableQuery (AssetProviderType assetProviderType, QueryTable queryTable,
            List<QueryColumn> columns) throws AnswersCatalogException;

   public String prepareInsertQuery (AssetProviderType assetProviderType, QueryTable queryTable,
            List<QueryColumn> queryColumns) throws AnswersCatalogException;

   public String prepareInsertQueryWithValuesSection (AssetProviderType assetProviderType, QueryTable queryTable,
            List<QueryColumn> queryColumns) throws AnswersCatalogException;

   public String prepareAlterTableQuery (AssetProviderType assetProviderType, QueryTable queryTable,
            QueryColumn queryColumn) throws AnswersCatalogException;

}
