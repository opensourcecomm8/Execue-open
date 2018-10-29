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


package com.execue.core.common.bean.aggregation;

import java.util.ArrayList;
import java.util.List;

/**
 * This bean class corresponds to the final XML data file that contains the data which has to be displayed as a report. <BR>
 * It contains the place holders for the header containing the meta information and the body which contains the actual data.
 * 
 * @author John Mallavalli
 */

public class QueryData {

   private QueryDataHeader queryDataHeader;
   private QueryDataRows   queryDataRows;

   public QueryDataRows getQueryDataRows () {
      return queryDataRows;
   }

   public void setQueryDataRows (QueryDataRows queryDataRows) {
      this.queryDataRows = queryDataRows;
   }

   public QueryDataHeader getQueryDataHeader () {
      return queryDataHeader;
   }

   public void setQueryDataHeader (QueryDataHeader queryDataHeader) {
      this.queryDataHeader = queryDataHeader;
   }

   public QueryData getSampleXML () {
      QueryData queryData = new QueryData();
      // prepare header
      QueryDataHeader header = new QueryDataHeader();
      List<QueryDataHeaderColumnMeta> columns = new ArrayList<QueryDataHeaderColumnMeta>();
      QueryDataHeaderColumnMeta column = new QueryDataHeaderColumnMeta();
      column.setId("c1");
      column.setDescription("BILL_MONTH");
      column.setCtype("dimension");
      column.setDtype("string");
      columns.add(column);
      column = new QueryDataHeaderColumnMeta();
      column.setId("c2");
      column.setDescription("MERCHANT_AMT_AVG");
      column.setCtype("measure");
      column.setDtype("number");
      columns.add(column);
      column = new QueryDataHeaderColumnMeta();
      column.setId("c3");
      column.setDescription("ACCOUNT_CNT");
      column.setCtype("idcolumn");
      column.setDtype("number");
      columns.add(column);

      header.setColumnCount(3);
      header.setQueryDataHeaderColumns(columns);
      queryData.setQueryDataHeader(header);

      // prepare data
      QueryDataRows data = new QueryDataRows();
      int rowCount = 5;
      List<QueryDataRowData> rows = new ArrayList<QueryDataRowData>();
      QueryDataRowData row = new QueryDataRowData();
      List<QueryDataColumnData> values = new ArrayList<QueryDataColumnData>();
      for (int i = 1; i <= rowCount; i++) {
         row = new QueryDataRowData();
         values = new ArrayList<QueryDataColumnData>();
         for (int j = 0; j < header.getColumnCount(); j++) {
            QueryDataColumnData value = new QueryDataColumnData();
            value.setColumnName(columns.get(j).getDescription());
            value.setColumnValue(columns.get(j).getDescription() + i);
            values.add(value);
            row.setQueryDataColumns(values);
         }
         rows.add(row);
      }
      data.setQueryDataRowsList(rows);
      queryData.setQueryDataRows(data);
      return queryData;
   }
}