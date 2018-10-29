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


package com.execue.querybuilder.service.qdata.impl;

import java.util.List;

import com.execue.core.util.ExecueCoreUtil;

/**
 * Oracle specific query builder routines come here
 * 
 * @author Vishay
 * @version 1.0
 * @since 03/01/12
 */
public class QDataOracleQueryBuilderServiceImpl extends QDataQueryBuilderServiceImpl {

   public String prepareInsertQueryForDuplicateUDXIds (List<Long> duplicateArticleIds) {
      String insertQuery = "INSERT INTO temp_dup_udx_id  values " + getValues(duplicateArticleIds);
      return insertQuery;
   }

   private String getValues (List<Long> duplicateArticles) {
      StringBuilder valueString = new StringBuilder();
      if (ExecueCoreUtil.isListElementsEmpty(duplicateArticles)) {
         return valueString.toString();
      }
      for (Long duplicateArticleId : duplicateArticles) {
         valueString.append("(").append(duplicateArticleId).append("),");
      }
      return valueString.substring(0, valueString.length() - 1);
   }
}
