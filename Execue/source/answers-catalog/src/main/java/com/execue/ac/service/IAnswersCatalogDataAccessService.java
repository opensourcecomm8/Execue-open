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

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.das.dataaccess.ISystemDataAccessService;
import com.execue.dataaccess.exception.DataAccessException;

/**
 * This service contains methods for database access required for answers catalog.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public interface IAnswersCatalogDataAccessService extends ISystemDataAccessService {

   public Integer executeUpdateStatement (DataSource dataSource, QueryTable targetTable,
            List<ConditionEntity> setConditions) throws AnswersCatalogException;

   public Integer executeUpdateStatement (DataSource dataSource, QueryTable targetTable,
            List<ConditionEntity> setConditions, List<ConditionEntity> whereConditions) throws AnswersCatalogException;

   public Integer executeUpdateStatement (DataSource dataSource, QueryTable targetTable, List<QueryTable> sourceTables,
            List<ConditionEntity> setConditions, List<ConditionEntity> whereConditions) throws AnswersCatalogException;

   public Integer fetchMaxDataLength (DataSource dataSource, QueryTable queryTable, QueryColumn queryColumn)
            throws AnswersCatalogException;

   public ExeCueResultSet executeSQLQuery (DataSource dataSource, Query query, String queryString)
            throws DataAccessException;

}
