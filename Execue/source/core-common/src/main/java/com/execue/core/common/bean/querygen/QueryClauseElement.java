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

import java.io.Serializable;
import java.util.List;

import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.type.QueryElementType;

/**
 * This represents the single Element in query clause element. 1. In select clause of query, simple string can come,
 * case statement can come and even subQuery can come. 2. In From clause, simple string can come and even sub query can
 * come. 3. In Joins clause, only simple string can come. 4. In where clause, simple string can come if right side is
 * value, otherwise a combination of simple string and subQuery will come for right side. 5. In GroupBy clause simple
 * string and case statement is a possibility. 6. In OrderBy, simple string and case is possibility. 7. Limit clause
 * will always be a string. 7. When ever the element is of type case or sub query then the alias should be filled in. 8.
 * In orderBy clause orderEntityType will be there for that clause
 * 
 * @author Vishay
 * @version 1.0
 */
public class QueryClauseElement implements Serializable {

   private static final long serialVersionUID = 1L;
   private String            simpleString;
   private List<String>      caseStatement;
   private QueryStructure    subQuery;
   private String            alias;
   private OrderEntityType   orderEntityType;
   private QueryElementType  queryElementType;

   public String getSimpleString () {
      return simpleString;
   }

   public void setSimpleString (String simpleString) {
      this.simpleString = simpleString;
   }

   public List<String> getCaseStatement () {
      return caseStatement;
   }

   public void setCaseStatement (List<String> caseStatement) {
      this.caseStatement = caseStatement;
   }

   public QueryStructure getSubQuery () {
      return subQuery;
   }

   public void setSubQuery (QueryStructure subQuery) {
      this.subQuery = subQuery;
   }

   public String getAlias () {
      return alias;
   }

   public void setAlias (String alias) {
      this.alias = alias;
   }

   public QueryElementType getQueryElementType () {
      return queryElementType;
   }

   public void setQueryElementType (QueryElementType queryElementType) {
      this.queryElementType = queryElementType;
   }

   
   public OrderEntityType getOrderEntityType () {
      return orderEntityType;
   }

   
   public void setOrderEntityType (OrderEntityType orderEntityType) {
      this.orderEntityType = orderEntityType;
   }
}
