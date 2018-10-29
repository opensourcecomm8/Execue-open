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


package com.execue.core.common.bean.pseudolang;

import java.util.List;
import java.util.Map;

/**
 * Pseudo query representing Business Query
 * 
 * @author execue
 * @since 4.0
 * @version 1.0
 */
public class NormalizedPseudoQuery {

   // private static final Logger log = Logger.getLogger(NormalizedPseudoQuery.class);
   private Map<Long, PseudoEntity>     pseudoEntities;
   private List<PseudoEntity>          metrics;
   private List<PseudoConditionEntity> conditions;
   private List<PseudoEntity>          population;
   private List<PseudoEntity>          summarizations;
   private List<PseudoEntity>          orderClauses;
   private List<PseudoConditionEntity> havingClauses;
   private NormalizedPseudoQuery       cohortClause;
   private PseudoLimitEntity           topBottom;

   public PseudoLimitEntity getTopBottom () {
      return topBottom;
   }

   public void setTopBottom (PseudoLimitEntity topBottom) {
      this.topBottom = topBottom;
   }

   public List<PseudoEntity> getMetrics () {
      return metrics;
   }

   public void setMetrics (List<PseudoEntity> metrics) {
      this.metrics = metrics;
   }

   public List<PseudoConditionEntity> getConditions () {
      return conditions;
   }

   public void setConditions (List<PseudoConditionEntity> conditions) {
      this.conditions = conditions;
   }

   public List<PseudoEntity> getPopulation () {
      return population;
   }

   public void setPopulation (List<PseudoEntity> population) {
      this.population = population;
   }

   public List<PseudoEntity> getSummarizations () {
      return summarizations;
   }

   public void setSummarizations (List<PseudoEntity> summarizations) {
      this.summarizations = summarizations;
   }

   public List<PseudoEntity> getOrderClauses () {
      return orderClauses;
   }

   public void setOrderClauses (List<PseudoEntity> orderClauses) {
      this.orderClauses = orderClauses;
   }

   public List<PseudoConditionEntity> getHavingClauses () {
      return havingClauses;
   }

   public void setHavingClauses (List<PseudoConditionEntity> havingClauses) {
      this.havingClauses = havingClauses;
   }

   public NormalizedPseudoQuery getCohortClause () {
      return cohortClause;
   }

   public void setCohortClause (NormalizedPseudoQuery cohortClause) {
      this.cohortClause = cohortClause;
   }

   public Map<Long, PseudoEntity> getPseudoEntities () {
      return pseudoEntities;
   }

   public void setPseudoEntities (Map<Long, PseudoEntity> pseudoEntities) {
      this.pseudoEntities = pseudoEntities;
   }
}