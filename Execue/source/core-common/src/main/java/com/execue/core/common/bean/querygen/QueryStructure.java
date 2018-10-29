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
import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.type.QueryCombiningType;

/**
 * This bean represents the query structure representing different clauses.
 * 
 * @author Vishay
 * @version 1.0
 */
public class QueryStructure implements Serializable {

   private static final long        serialVersionUID = 1L;
   private List<QueryClauseElement> selectElements;
   private List<QueryClauseElement> fromElements;
   private List<QueryClauseElement> joinElements;
   private List<QueryClauseElement> whereElements;
   private List<QueryClauseElement> groupElements;
   private List<QueryClauseElement> orderElements;
   private List<QueryClauseElement> havingElements;
   private QueryClauseElement       limitElement;
   private boolean                  rollupQuery;
   private List<QueryStructure>     combiningQueryStructures;
   private QueryCombiningType       combiningType;

   public List<QueryClauseElement> getSelectElements () {
      if (selectElements == null) {
         selectElements = new ArrayList<QueryClauseElement>();
      }
      return selectElements;
   }

   public List<QueryClauseElement> getFromElements () {
      if (fromElements == null) {
         fromElements = new ArrayList<QueryClauseElement>();
      }
      return fromElements;
   }

   public List<QueryClauseElement> getJoinElements () {
      if (joinElements == null) {
         joinElements = new ArrayList<QueryClauseElement>();
      }
      return joinElements;
   }

   public List<QueryClauseElement> getWhereElements () {
      if (whereElements == null) {
         whereElements = new ArrayList<QueryClauseElement>();
      }
      return whereElements;
   }

   public List<QueryClauseElement> getGroupElements () {
      if (groupElements == null) {
         groupElements = new ArrayList<QueryClauseElement>();
      }
      return groupElements;
   }

   public List<QueryClauseElement> getOrderElements () {
      if (orderElements == null) {
         orderElements = new ArrayList<QueryClauseElement>();
      }
      return orderElements;
   }

   public QueryClauseElement getLimitElement () {
      return limitElement;
   }

   public void setSelectElements (List<QueryClauseElement> selectElements) {
      this.selectElements = selectElements;
   }

   public void setFromElements (List<QueryClauseElement> fromElements) {
      this.fromElements = fromElements;
   }

   public void setJoinElements (List<QueryClauseElement> joinElements) {
      this.joinElements = joinElements;
   }

   public void setWhereElements (List<QueryClauseElement> whereElements) {
      this.whereElements = whereElements;
   }

   public void setGroupElements (List<QueryClauseElement> groupElements) {
      this.groupElements = groupElements;
   }

   public void setOrderElements (List<QueryClauseElement> orderElements) {
      this.orderElements = orderElements;
   }

   public void setLimitElement (QueryClauseElement limitElement) {
      this.limitElement = limitElement;
   }

   public List<QueryClauseElement> getHavingElements () {
      return havingElements;
   }

   public void setHavingElements (List<QueryClauseElement> havingElements) {
      this.havingElements = havingElements;
   }

   /**
    * @return the rollupQuery
    */
   public boolean isRollupQuery () {
      return rollupQuery;
   }

   /**
    * @param rollupQuery the rollupQuery to set
    */
   public void setRollupQuery (boolean rollupQuery) {
      this.rollupQuery = rollupQuery;
   }

   public List<QueryStructure> getCombiningQueryStructures () {
      return combiningQueryStructures;
   }

   public void setCombiningQueryStructures (List<QueryStructure> combiningQueryStructures) {
      this.combiningQueryStructures = combiningQueryStructures;
   }

   public QueryCombiningType getCombiningType () {
      return combiningType;
   }

   public void setCombiningType (QueryCombiningType combiningType) {
      this.combiningType = combiningType;
   }

}