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


package com.execue.web.core.action.qi;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.qi.QIConversion;
import com.execue.core.common.bean.qi.QIConversionValueDetail;
import com.execue.core.common.bean.qi.suggest.SuggestConditionTerm;
import com.execue.core.common.bean.qi.suggest.SuggestTerm;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.qi.IQueryInterfaceSuggestHandler;
import com.execue.handler.swi.IConversionServiceHandler;
import com.execue.handler.swi.IKDXServiceHandler;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author kaliki
 * @since 4.0
 */
public class QueryInterfaceSuggestAction extends ActionSupport {

   private Long                          modelId;
   private List<SuggestConditionTerm>    conditionTerms;
   private List<SuggestTerm>             terms;
   private String                        search;
   private String                        businessTerm;
   private boolean                       stat;
   private IQueryInterfaceSuggestHandler suggestHandler;
   private IConversionServiceHandler     conversionServiceHandler;
   private String                        statName;
   private QIConversionValueDetail       qiConversionValueDetail;
   private String                        displayType;
   private IKDXServiceHandler            kdxServiceHandler;
   private static final Logger           logger = Logger.getLogger(QueryInterfaceSuggestAction.class);

   public String suggestSelect () {
      try {
         if (stat) {
            terms = suggestHandler.suggestBTsForSelect(modelId, search, statName);
         } else {
            terms = suggestHandler.suggestBTsAndStatsForSelect(modelId, search);
         }
      } catch (Exception e) {
         logger.error(e);
         terms = new ArrayList<SuggestTerm>();
      }
      return SUCCESS;
   }

   public String suggestBTsForPopulation () {
      try {
         terms = suggestHandler.suggestBTsForPopulation(modelId, search);
      } catch (Exception e) {
         logger.error(e);
         terms = new ArrayList<SuggestTerm>();
      }
      return SUCCESS;
   }

   public String suggestWhereLHS () {
      try {
         if (stat) {
            conditionTerms = suggestHandler.suggestBTsForWhereLHS(modelId, search, statName);
         } else {
            conditionTerms = suggestHandler.suggestBTsAndStatsForWhereLHS(modelId, search);
         }

      } catch (Exception e) {
         logger.error(e);
         conditionTerms = new ArrayList<SuggestConditionTerm>();
      }
      return SUCCESS;
   }

   public String suggestBTAndValuesForWhereRHS () {
      try {
         terms = suggestHandler.suggestBTAndValuesForWhereRHS(modelId, businessTerm, search);
      } catch (Exception e) {
         logger.error(e);
         terms = new ArrayList<SuggestTerm>();
      }
      return SUCCESS;
   }

   public String suggestBTsForSummarize () {
      try {
         terms = suggestHandler.suggestBTsForSummarize(modelId, search);
      } catch (Exception e) {
         logger.error(e);
         terms = new ArrayList<SuggestTerm>();
      }
      return SUCCESS;
   }

   public String suggestBTsForOrderBy () {
      try {
         terms = suggestHandler.suggestBTsForOrderBy(modelId, search);
      } catch (Exception e) {
         logger.error(e);
         terms = new ArrayList<SuggestTerm>();
      }
      return SUCCESS;
   }

   public String suggestConversion () {
      try {
         qiConversionValueDetail = new QIConversionValueDetail();
         QIConversion qiConversion = conversionServiceHandler.getQIConversion(displayType);
         Concept concept = kdxServiceHandler.getConceptByName(modelId, businessTerm);
         if (qiConversion != null) {
            qiConversionValueDetail.setQiConversion(qiConversion);
         }
         if (concept != null) {
            qiConversionValueDetail.setSampleValue(concept.getSampleValue());
         }
      } catch (ExeCueException e) {
         logger.error(e);
      }
      return SUCCESS;
   }

   public List<SuggestConditionTerm> getConditionTerms () {
      return conditionTerms;
   }

   public List<SuggestTerm> getTerms () {
      return terms;
   }

   public void setStat (boolean stat) {
      this.stat = stat;
   }

   public void setSearch (String search) {
      this.search = search;
   }

   public void setSuggestHandler (IQueryInterfaceSuggestHandler suggestHandler) {
      this.suggestHandler = suggestHandler;
   }

   public String getStatName () {
      return statName;
   }

   public void setStatName (String statName) {
      this.statName = statName;
   }

   public String getBusinessTerm () {
      return businessTerm;
   }

   public void setBusinessTerm (String businessTerm) {
      this.businessTerm = businessTerm;
   }

   public IConversionServiceHandler getConversionServiceHandler () {
      return conversionServiceHandler;
   }

   public void setConversionServiceHandler (IConversionServiceHandler conversionServiceHandler) {
      this.conversionServiceHandler = conversionServiceHandler;
   }

   public String getDisplayType () {
      return displayType;
   }

   public void setDisplayType (String displayType) {
      this.displayType = displayType;
   }

   public QIConversionValueDetail getQiConversionValueDetail () {
      return qiConversionValueDetail;
   }

   public void setQiConversionValueDetail (QIConversionValueDetail qiConversionValueDetail) {
      this.qiConversionValueDetail = qiConversionValueDetail;
   }

   public IKDXServiceHandler getKdxServiceHandler () {
      return kdxServiceHandler;
   }

   public void setKdxServiceHandler (IKDXServiceHandler kdxServiceHandler) {
      this.kdxServiceHandler = kdxServiceHandler;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }
}
