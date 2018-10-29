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


package com.execue.util.querygen;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SQLIndex;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.ConstraintType;
import com.execue.core.common.type.StatType;

/**
 * Interface for published query generation utilities
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/08/09
 */
public interface IQueryGenerationUtilService {

   public String createTableCreateStatement (QueryTable queryTable);

   public String createTableCreateStatement (QueryTable queryTable, List<QueryColumn> columns);

   public String createTableUsingExistingTableWithoutData (QueryTable newQueryTable, QueryTable existingQueryTable);

   public String createTableAlterStatement (QueryTable queryTable, QueryColumn queryColumn);

   public String createColumnAlterStatement (QueryTable queryTable, QueryColumn queryColumn);

   public String createTableDropStatement (QueryTable queryTable);

   public String createTableDeleteStatement (ConditionEntity conditionEntity);

   public String createTableTruncateStatement (QueryTable queryTable);

   public String createTableRenameStatement (QueryTable fromTable, QueryTable toTable);

   public String createIndexStatement (SQLIndex sqlIndex, boolean isUnique);

   public String createIndexPingStatement (String indexName, QueryTable queryTable);

   public List<String> createMultipleIndexStatement (List<SQLIndex> indexes);

   public String createMultipleIndexsWithSingleStatement (List<SQLIndex> indexes);

   public String createConstraintStatement (ConstraintType constraintType, String constraintName,
            QueryTable childTable, List<QueryColumn> childColumns, QueryTable parentTable,
            List<QueryColumn> parentColumns);

   public String createUpdateStatement (QueryTable targetTable, List<QueryTable> sourceTables,
            List<ConditionEntity> setConditions, List<ConditionEntity> whereConditions);

   public String createUpdateStatement (QueryTable targetTable, List<ConditionEntity> setConditions);

   public String createUpdateStatement (QueryTable targetTable, List<ConditionEntity> setConditions,
            List<ConditionEntity> whereConditions);

   public String createInsertStatement (QueryTable existingCubeFactTable);

   public String createInsertStatement (QueryTable queryTable, List<QueryColumn> queryColumns, boolean isValuesSection);

   public String createETLInsertStatement (QueryTable queryTable, List<QueryColumn> queryColumns,
            Map<String, String> selectQueryColumnAliases);

   public String createETLInsertStatement (QueryTable queryTable, List<QueryColumn> queryColumns,
            Map<String, String> selectQueryColumnAliases, Map<String, String> dataColumnAliases);

   public String createSelectStatement (QueryTable queryTable, List<QueryColumn> queryColumns);

   public String createSelectStatement (QueryTable queryTable, List<QueryColumn> queryColumns, boolean distinct);

   public String createSelectStatement (QueryTable sqlTable, List<QueryColumn> queryColumns, LimitEntity limitEntity);

   public String createSelectStatement (QueryTable queryTable, boolean distinct);

   public SelectQueryInfo createPingSelectStatement (QueryTable queryTable);

   public String createSelectStatementWithDateHandling (QueryTable queryTable, List<QueryColumn> sourceQueryColumns,
            List<QueryColumn> destinationQueryColumns);

   public SelectQueryInfo createStatBasedSelectStatement (QueryTable queryTable, QueryColumn queryColumn,
            StatType statType);

   public SelectQueryInfo createDistinctCountSelectStatementIncludingNullRecords (QueryTable queryTable,
            QueryColumn queryColumn);

   public String createMaximumDataLengthSelectStatement (QueryTable queryTable, QueryColumn queryColumn);

   public SelectQueryInfo createCountNotNullStringToDateStatement (QueryTable queryTable, QueryColumn queryColumn,
            String dateFormat);

   public SelectQueryInfo createCountNotNullRecordsLengthStatement (QueryTable queryTable, QueryColumn column,
            int recordLength);

   public SelectQueryInfo createCountNotNullRegexStatement (QueryTable queryTable, QueryColumn queryColumn, String regex);

   public SelectQueryInfo createDateRegexSizeNotNullStatement (QueryTable queryTable, QueryColumn queryColumn,
            String regex, int length);

   public String createStatementForRandomRecords (QueryTable queryTable, QueryColumn queryColumn, int numRecords);

   public String createSQLStatementForColumnScale (QueryTable queryTable, QueryColumn queryColumn);

   public String createSQLStatementForNonDecimalColumnPrecision (QueryTable queryTable, QueryColumn queryColumn);

   public String createSQLStatementForDecimalColumnDecimalDataPrecision (QueryTable queryTable, QueryColumn queryColumn);

   public String createSQLStatementForDecimalColumnIntegerDataPrecision (QueryTable queryTable, QueryColumn queryColumn);

   public boolean isAutoIncrementClauseSupported ();

   public boolean isMultipleIndexesWithSingleStatementSupported ();
}