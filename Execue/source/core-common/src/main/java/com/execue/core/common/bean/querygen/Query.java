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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.type.QueryCombiningType;
import com.execue.core.util.ExecueCoreUtil;

/**
 * Object holding the segments of a query
 * 
 * @author Jayadev
 */
public class Query {

   /**
    * Entities that the resulting grid (by executing the query) should contain
    */
   private List<SelectEntity>    selectEntities;
   /**
    * Be populated by the Query Generation Service based on all other segments of the query
    */
   private List<FromEntity>      fromEntities;
   /**
    * Be populated using Join Service based on the From Segment
    */
   private List<JoinEntity>      joinEntities;
   /**
    * Conditions that the query should applied with
    */
   private List<ConditionEntity> whereEntities;
   /**
    * Entities upon which the summarization should be done
    */
   private List<SelectEntity>    groupingEntities;
   /**
    * Results should be order by these entities in the order they appear in the list
    */
   private List<OrderEntity>     orderingEntities;
   /**
    * Conditions that might not be possible to be applied in a where clause of a sequel query
    */
   private List<ConditionEntity> havingEntities;

   /**
    * Condition to limit the number of rows in the result
    */
   private LimitEntity           limitingCondition;

   private QueryTableColumn      scalingFactorEntity;

   private List<SelectEntity>    populationEntities;

   /**
    * List of combining queries to perform SET operations on SQL. This operations are based on the combiningType set for this query object. 
    * So make sure once we set the combiningQueries then we do set the correct corresponding combiningType. EG: UNION, MINUS, UNION ALL
    * Then only the logic will work.
    */
   private List<Query>           combiningQueries;

   // NOTE: DO NOT SET THIS ENUM IF THE QUERY IS NOT OF ANY COMBINING TYPE. ONLY SET IT TO APPROPRIATE COMBINING TYPE 
   // ALONG WITH THE ABOVE LIST OF combiningQueries 
   private QueryCombiningType    combiningType;

   /**
    * alias if the query object needs to be used as an inner query
    */
   private String                alias;

   private boolean               rollupQuery = false;

   public List<SelectEntity> getSelectEntities () {
      return selectEntities;
   }

   public void setSelectEntities (List<SelectEntity> selectEntities) {
      this.selectEntities = selectEntities;
   }

   public List<FromEntity> getFromEntities () {
      return fromEntities;
   }

   public void setFromEntities (List<FromEntity> fromEntities) {
      this.fromEntities = fromEntities;
   }

   public List<ConditionEntity> getWhereEntities () {
      return whereEntities;
   }

   public void setWhereEntities (List<ConditionEntity> whereEntities) {
      this.whereEntities = whereEntities;
   }

   public List<SelectEntity> getGroupingEntities () {
      return groupingEntities;
   }

   public void setGroupingEntities (List<SelectEntity> groupingEntities) {
      this.groupingEntities = groupingEntities;
   }

   public List<OrderEntity> getOrderingEntities () {
      return orderingEntities;
   }

   public void setOrderingEntities (List<OrderEntity> orderingEntities) {
      this.orderingEntities = orderingEntities;
   }

   public List<ConditionEntity> getHavingEntities () {
      return havingEntities;
   }

   public void setHavingEntities (List<ConditionEntity> havingEntities) {
      this.havingEntities = havingEntities;
   }

   public LimitEntity getLimitingCondition () {
      return limitingCondition;
   }

   public void setLimitingCondition (LimitEntity limitingCondition) {
      this.limitingCondition = limitingCondition;
   }

   public String getAlias () {
      return alias;
   }

   public void setAlias (String alias) {
      this.alias = alias;
   }

   public List<JoinEntity> getJoinEntities () {
      return joinEntities;
   }

   public void setJoinEntities (List<JoinEntity> joinEntities) {
      this.joinEntities = joinEntities;
   }

   public QueryTableColumn getScalingFactorEntity () {
      return scalingFactorEntity;
   }

   public void setScalingFactorEntity (QueryTableColumn scalingFactorEntity) {
      this.scalingFactorEntity = scalingFactorEntity;
   }

   public List<SelectEntity> getPopulationEntities () {
      return populationEntities;
   }

   public void setPopulationEntities (List<SelectEntity> populationEntities) {
      this.populationEntities = populationEntities;
   }

   public void addConditionEntity (ConditionEntity conditionEntity) {
      if (ExecueCoreUtil.isCollectionEmpty(whereEntities)) {
         whereEntities = new ArrayList<ConditionEntity>();
      }
      whereEntities.add(conditionEntity);
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

   public List<Query> getCombiningQueries () {
      return combiningQueries;
   }

   public void setCombiningQueries (List<Query> combiningQueries) {
      this.combiningQueries = combiningQueries;
   }

   public QueryCombiningType getCombiningType () {
      return combiningType;
   }

   public void setCombiningType (QueryCombiningType combiningType) {
      this.combiningType = combiningType;
   }

}