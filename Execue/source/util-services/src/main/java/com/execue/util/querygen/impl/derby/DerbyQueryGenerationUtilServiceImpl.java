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


package com.execue.util.querygen.impl.derby;

import java.util.List;

import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.util.querygen.impl.SQLQueryGenerationUtilServiceImpl;

/**
 * @author Prasanna
 */
public class DerbyQueryGenerationUtilServiceImpl extends SQLQueryGenerationUtilServiceImpl implements
         ISQLQueryConstants {

   @Override
   protected Object createColumnsCommaSeperatedRepresentationWithDateHandling (List<QueryColumn> queryColumns) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public SelectQueryInfo createCountNotNullStringToDateStatement (QueryTable queryTable, QueryColumn queryColumn,
            String dateFormat) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String createIndexPingStatement (String indexName, QueryTable tableName) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public SelectQueryInfo createPingSelectStatement (QueryTable queryTable) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   protected Object createColumnsCommaSeperatedRepresentation (List<QueryColumn> sourceQueryColumns,
            List<QueryColumn> destinationQueryColumns) {
      // TODO Auto-generated method stub
      return null;
   }

}
