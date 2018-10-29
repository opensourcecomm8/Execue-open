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


package com.execue.util.queryadaptor;

import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.type.DataType;

/**
 * @author Nitesh
 */
public interface ISQLAdaptor {

   public QueryStructure handleLimitClause (QueryStructure queryStructure);

   public String createRandomNumberGeneratorFormula (Integer lowerBound, Integer upperBound);

   public String getAutoIncrementClause ();

   public String getNullRepresentationFunction (DataType dataType);

   public boolean isAutoIncrementClauseSupported ();
   
   public boolean isMultipleIndexesWithSingleStatementSupported ();

   /**
    * This method will create string representation for Column using QueryTableColum object.
    * 
    * @param queryTableColumn
    * @return
    */
   public String createColumRepresentationQueryTableColumn (QueryTableColumn queryTableColumn);

   /**
    * This method will create string representation for Table using QueryTableColum object. It will also append the
    * alias based on the input boolean
    * 
    * @param queryTable
    * @param appendAlias
    * @return
    */
   public String createTableRepresentationQueryTableColumn (QueryTable queryTable, boolean appendAlias);

}
