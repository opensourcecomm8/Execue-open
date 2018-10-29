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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.das.dataaccess.impl.SystemDataAccessServiceImpl;
import com.execue.platform.exception.PlatformException;
import com.execue.util.querygen.IQueryGenerationUtilService;

public class PublishedFileJDBCHelper extends SystemDataAccessServiceImpl {

   public String createSelectStatementWithLimit (DataSource targetDataSource, String tableName,
            List<String> columnNames, LimitEntity limitEntity) throws PlatformException {
      IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(targetDataSource
               .getProviderType());
      List<QueryColumn> queryColumns = ExecueBeanManagementUtil.prepareQueryColumnsFromColumnNames(columnNames);
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName, null, targetDataSource.getOwner());
      return queryGenerationUtilService.createSelectStatement(queryTable, queryColumns, limitEntity);
   }

   public void fillDataRows (List<List<String>> dataRows, DataSource dataSource, QueryTable table,
            List<String> columnNames, LimitEntity limitEntity, Page requestedPage) throws Exception {
      if (dataRows == null) {
         dataRows = new ArrayList<List<String>>();
      }
      String tableName = table.getTableName();
      String selectStatementWithLimit = createSelectStatementWithLimit(dataSource, tableName, columnNames, limitEntity);
      // execute query
      ExeCueResultSet resultSet = executeSQLQuery(dataSource.getName(), new SelectQueryInfo(selectStatementWithLimit));

      while (resultSet.next()) {
         List<String> dataRow = new ArrayList<String>();
         for (int index = 0; index < columnNames.size(); index++) {
            dataRow.add(resultSet.getString(index));
         }
         dataRows.add(dataRow);
      }
      requestedPage.setRecordCount(getStatBasedColumnValue(dataSource, table, ExecueBeanManagementUtil
               .prepareQueryColumnFromColumnName(columnNames.get(0)), StatType.COUNT));

      // TODO -RG- Proper exception handling needed here
   }

}
