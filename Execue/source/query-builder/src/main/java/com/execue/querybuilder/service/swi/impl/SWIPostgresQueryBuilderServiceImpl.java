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


package com.execue.querybuilder.service.swi.impl;

import java.util.List;
import java.util.Set;

import com.execue.core.util.ExecueCoreUtil;

/**
 * Postgressql specific query builder routines come here
 * 
 * @author Prasanna
 */
public class SWIPostgresQueryBuilderServiceImpl extends SWIQueryBuilderServiceImpl {

   @Override
   public String buildGroupBasedSFLQuery (List<String> words, Set<Long> modelGroupIds) {
      // populate modelgroupId
      StringBuilder mGroupsIdsAsString = new StringBuilder();
      if (ExecueCoreUtil.isCollectionNotEmpty(modelGroupIds)) {
         mGroupsIdsAsString.append(ExecueCoreUtil.joinLongCollection(modelGroupIds));
      }

      String wordsAsString = ExecueCoreUtil.joinCollectionWithSingleQuotesOnValues(words);
      wordsAsString = wordsAsString.replaceAll(":", " ");

      StringBuilder query = new StringBuilder();
      query.append("SELECT sfl_term_id as id,business_term as businessTerm,");
      query.append("STRING_AGG(business_term_token,'##') as tokens,");
      query
               .append("array_to_STRING(array_agg(weight),'##') as weightlist,array_to_string(array_agg(id) ,'##') as tokenIDs,");
      query.append("array_to_string(array_agg(token_order),'##') as orderlist,sum(weight) totalweight,");
      query
               .append("context_id as contextId , array_to_STRING(array_agg(REQUIRED),'##') as tokenRequired, REQUIRED_TOKEN_COUNT as requiredTokenCount, array_to_STRING(array_agg(primary_word),'##') as primaryTokens,");
      query
               .append("sum(primary_word) as sumPrimary,sum(required) as sumRequired,avg(required_token_count) as avgRequiredTokenCount ");
      query.append("FROM sfl_term_token WHERE business_term_token in (");
      query.append(wordsAsString);
      query.append(") ");
      if (ExecueCoreUtil.isNotEmpty(mGroupsIdsAsString.toString())) {
         query.append(" and context_id IN  ( ");
         query.append(mGroupsIdsAsString.toString());
         query.append(" ) ");
      }
      query
               .append(" GROUP BY sfl_term_id,context_id,business_term,REQUIRED_TOKEN_COUNT HAVING sum(primary_word) > 0 AND  sum(required) >= avg(required_token_count) ORDER BY totalweight DESC");
      return query.toString();
   }

}
