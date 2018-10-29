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


package com.execue.core.common.bean.querygen;

import java.util.List;

import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.QueryConditionOperandType;
import com.execue.core.common.type.StatType;

public class ConditionEntity {

   private StatType                  lhsFunctionName;
   private QueryTableColumn          lhsTableColumn;
   private OperatorType              operator;
   private QueryConditionOperandType operandType;
   private List<QueryTableColumn>    rhsTableColumns;
   private List<QueryValue>          rhsValues;
   private Query                     rhsSubQuery;
   private boolean                   subCondition;
   private List<ConditionEntity>     subConditionEntities;

   public StatType getLhsFunctionName () {
      return lhsFunctionName;
   }

   public void setLhsFunctionName (StatType lhsFunctionName) {
      this.lhsFunctionName = lhsFunctionName;
   }

   public QueryTableColumn getLhsTableColumn () {
      return lhsTableColumn;
   }

   public void setLhsTableColumn (QueryTableColumn lhsTableColumn) {
      this.lhsTableColumn = lhsTableColumn;
   }

   public OperatorType getOperator () {
      return operator;
   }

   public void setOperator (OperatorType operator) {
      this.operator = operator;
   }

   public QueryConditionOperandType getOperandType () {
      return operandType;
   }

   public void setOperandType (QueryConditionOperandType operandType) {
      this.operandType = operandType;
   }

   public List<QueryValue> getRhsValues () {
      return rhsValues;
   }

   public void setRhsValues (List<QueryValue> rhsValues) {
      this.rhsValues = rhsValues;
   }

   public Query getRhsSubQuery () {
      return rhsSubQuery;
   }

   public void setRhsSubQuery (Query rhsSubQuery) {
      this.rhsSubQuery = rhsSubQuery;
   }

   public List<QueryTableColumn> getRhsTableColumns () {
      return rhsTableColumns;
   }

   public void setRhsTableColumns (List<QueryTableColumn> rhsTableColumns) {
      this.rhsTableColumns = rhsTableColumns;
   }

   public List<ConditionEntity> getSubConditionEntities () {
      return subConditionEntities;
   }

   public void setSubConditionEntities (List<ConditionEntity> subConditionEntities) {
      this.subConditionEntities = subConditionEntities;
   }

   public boolean isSubCondition () {
      return subCondition;
   }

   public void setSubCondition (boolean subCondition) {
      this.subCondition = subCondition;
   }

}
