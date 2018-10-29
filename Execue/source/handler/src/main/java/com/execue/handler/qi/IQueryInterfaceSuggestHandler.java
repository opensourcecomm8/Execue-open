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


package com.execue.handler.qi;

import java.util.List;

import com.execue.core.common.bean.QueryValidationResult;
import com.execue.core.common.bean.qi.suggest.SuggestConditionTerm;
import com.execue.core.common.bean.qi.suggest.SuggestTerm;
import com.execue.core.exception.HandlerException;
import com.execue.handler.IHandler;

/**
 * @author kaliki
 * @since 4.0
 */
public interface IQueryInterfaceSuggestHandler extends IHandler {

   /**
    * used for QueryInteface metrics suggestion. Valid return type are CONCEPT, PROFILE, FORMULA, STAT
    */
   public List<SuggestTerm> suggestBTsAndStatsForSelect (Long modelId, String searchString) throws HandlerException;

   /**
    * used for QueryInteface metrics suggestion. Valid return type are CONCEPT, PROFILE, FORMULA
    */
   public List<SuggestTerm> suggestBTsForSelect (Long modelId, String searchString, String statName)
            throws HandlerException;

   // Population
   /**
    * used for QueryInteface population suggestion. Valid return type are CONCEPT (Grain)
    */
   public List<SuggestTerm> suggestBTsForPopulation (Long modelId, String searchString) throws HandlerException;

   // where
   /**
    * used for QueryInteface where condition LHS suggestion. Valid return type are CONCEPT, FORMULA, STAT
    */
   public List<SuggestConditionTerm> suggestBTsAndStatsForWhereLHS (Long modelId, String searchString)
            throws HandlerException;

   /**
    * used for QueryInteface where condition LHS suggestion. Valid return type are CONCEPT, FORMULA
    */
   public List<SuggestConditionTerm> suggestBTsForWhereLHS (Long modelId, String searchString, String statName)
            throws HandlerException;

   // public List<QIOperator> suggestOperatorsForBT (String businessTerm)throws HandlerException;
   // public List<QIDataType> suggestDataTypeForBT (String businessTerm) throws HandlerException;

   /**
    * used for QueryInteface where condition RHS suggestion. Valid return type are CONCEPT_LOOKUP_INSTANCE, CONCEPT
    */
   public List<SuggestTerm> suggestBTAndValuesForWhereRHS (Long modelId, String businessTerm, String searchString)
            throws HandlerException;

   // Summarization/groupBy
   /**
    * used for QueryInteface summarization suggestion. Valid return type are CONCEPT
    */
   public List<SuggestTerm> suggestBTsForSummarize (Long modelId, String searchString) throws HandlerException;

   /**
    * used for QueryInteface orderBy/TopBottom suggestion. Valid return type are CONCEPT
    */
   public List<SuggestTerm> suggestBTsForOrderBy (Long modelId, String searchString) throws HandlerException;

   public QueryValidationResult validateRequest (String request, String context) throws HandlerException;
}
