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
 * Oracle specific query builder routines come here
 * 
 * @author Vishay
 * @version 1.0
 * @since 03/01/12
 */
public class SWIOracleQueryBuilderServiceImpl extends SWIQueryBuilderServiceImpl {

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
      query.append("ex_orc_grp_concat_hash2(business_term_token) as tokens,");
      query.append("ex_orc_grp_concat(weight) as weightlist,ex_orc_grp_concat(id) as tokenIDs,");
      query.append("ex_orc_grp_concat(token_order) as orderlist,sum(weight) totalweight,");
      query.append("context_id as contextId , ex_orc_grp_concat(REQUIRED) as tokenRequired,"
               + "REQUIRED_TOKEN_COUNT as requiredTokenCount,ex_orc_grp_concat(primary_word) as primaryTokens ");
      query.append("FROM sfl_term_token WHERE (LOWER(business_term_token) IN (");
      query.append(wordsAsString);
      query.append(")) ");
      if (ExecueCoreUtil.isNotEmpty(mGroupsIdsAsString.toString())) {
         query.append("AND context_id IN  (");
         query.append(mGroupsIdsAsString.toString());
         query.append(") ");
      }
      query.append("GROUP BY sfl_term_id,context_id,REQUIRED_TOKEN_COUNT,business_term HAVING SUM(primary_word) > 0 "
               + "AND SUM(REQUIRED) >= AVG(REQUIRED_TOKEN_COUNT) ORDER BY SUM(weight) DESC");
      return query.toString();
   }

}
