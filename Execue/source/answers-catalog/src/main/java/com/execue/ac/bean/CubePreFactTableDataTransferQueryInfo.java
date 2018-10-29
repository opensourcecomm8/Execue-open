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


package com.execue.ac.bean;

import com.execue.core.common.bean.querygen.Query;

/**
 * @author Vishay
 * @version 1.0
 * @since 22/01/2011
 */
public class CubePreFactTableDataTransferQueryInfo {

   private Query   selectQuery;
   private Integer queryId;

   public CubePreFactTableDataTransferQueryInfo (Query selectQuery, Integer queryId) {
      super();
      this.selectQuery = selectQuery;
      this.queryId = queryId;
   }

   public Query getSelectQuery () {
      return selectQuery;
   }

   public void setSelectQuery (Query selectQuery) {
      this.selectQuery = selectQuery;
   }

   public Integer getQueryId () {
      return queryId;
   }

   public void setQueryId (Integer queryId) {
      this.queryId = queryId;
   }
}
