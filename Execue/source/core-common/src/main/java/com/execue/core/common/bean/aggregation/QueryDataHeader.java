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

import java.util.List;

/**
 * This bean class holds the meta information of the report data. This contains the meta information of each column <BR>
 * which is being displayed in the data and also the count of the rows that form the data.
 * 
 * @author John Mallavalli
 */

public class QueryDataHeader {

   private int                             columnCount;
   private String                          title;
   private List<QueryDataHeaderColumnMeta> queryDataHeaderColumns;
   private QueryDataHeaderHierarchyMeta    queryDataHeaderHierarchyMeta;

   public int getColumnCount () {
      return columnCount;
   }

   public void setColumnCount (int ColumnCount) {
      this.columnCount = ColumnCount;
   }

   public List<QueryDataHeaderColumnMeta> getQueryDataHeaderColumns () {
      return queryDataHeaderColumns;
   }

   public void setQueryDataHeaderColumns (List<QueryDataHeaderColumnMeta> columns) {
      this.queryDataHeaderColumns = columns;
   }

   public String getTitle () {
      return title;
   }

   public void setTitle (String title) {
      this.title = title;
   }

   /**
    * @return the queryDataHeaderHierarchyMeta
    */
   public QueryDataHeaderHierarchyMeta getQueryDataHeaderHierarchyMeta () {
      return queryDataHeaderHierarchyMeta;
   }

   /**
    * @param queryDataHeaderHierarchyMeta the queryDataHeaderHierarchyMeta to set
    */
   public void setQueryDataHeaderHierarchyMeta (QueryDataHeaderHierarchyMeta queryDataHeaderHierarchyMeta) {
      this.queryDataHeaderHierarchyMeta = queryDataHeaderHierarchyMeta;
   }
}