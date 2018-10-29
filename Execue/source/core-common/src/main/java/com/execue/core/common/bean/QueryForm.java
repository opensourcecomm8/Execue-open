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


package com.execue.core.common.bean;

import java.util.List;

import com.execue.core.common.bean.qi.QIBusinessTerm;
import com.execue.core.common.bean.qi.QICondition;
import com.execue.core.common.bean.qi.QIOrderBy;
import com.execue.core.common.bean.qi.QISelect;
import com.execue.core.common.bean.qi.QISubQuery;
import com.execue.core.common.bean.qi.QITopBottom;

/**
 * @author kaliki
 * @since 4.0
 */
public class QueryForm {

   private Long                 modelId;
   private Long                 applicationId;
   private String               selectText;
   private boolean              selectTextProcessed;
   private List<QISelect>       selects;
   private List<QIBusinessTerm> summarizations;
   private List<QIBusinessTerm> populations;
   private List<QICondition>    conditions;
   private QISubQuery           cohort;
   private List<QIOrderBy>      orderBys;
   private QITopBottom          topBottom;

   public List<QISelect> getSelects () {
      return selects;
   }

   public void setSelects (List<QISelect> selects) {
      this.selects = selects;
   }

   public List<QIBusinessTerm> getSummarizations () {
      return summarizations;
   }

   public void setSummarizations (List<QIBusinessTerm> summarizations) {
      this.summarizations = summarizations;
   }

   public List<QIBusinessTerm> getPopulations () {
      return populations;
   }

   public void setPopulations (List<QIBusinessTerm> population) {
      this.populations = population;
   }

   public List<QICondition> getConditions () {
      return conditions;
   }

   public void setConditions (List<QICondition> conditions) {
      this.conditions = conditions;
   }

   public QISubQuery getCohort () {
      return cohort;
   }

   public void setCohort (QISubQuery cohort) {
      this.cohort = cohort;
   }

   public List<QIOrderBy> getOrderBys () {
      return orderBys;
   }

   public void setOrderBys (List<QIOrderBy> orderBys) {
      this.orderBys = orderBys;
   }

   public QITopBottom getTopBottom () {
      return topBottom;
   }

   public void setTopBottom (QITopBottom topBottom) {
      this.topBottom = topBottom;
   }

   public String getSelectText () {
      return selectText;
   }

   public void setSelectText (String selectText) {
      this.selectText = selectText;
   }

   public boolean isSelectTextProcessed () {
      return selectTextProcessed;
   }

   public void setSelectTextProcessed (boolean selectTextProcessed) {
      this.selectTextProcessed = selectTextProcessed;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId
    *           the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

}
