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


package com.execue.handler.qi.impl;

import java.util.List;

import com.execue.core.common.bean.QueryValidationResult;
import com.execue.core.common.bean.qi.suggest.SuggestConditionTerm;
import com.execue.core.common.bean.qi.suggest.SuggestTerm;
import com.execue.core.exception.HandlerException;
import com.execue.handler.QueryValidationHelper;
import com.execue.handler.qi.IQueryInterfaceSuggestHandler;
import com.execue.platform.IQueryInterfaceSuggestService;
import com.execue.platform.exception.PlatformException;
import com.execue.security.UserContextService;

/**
 * @author kaliki
 * @since 4.0
 */
public class QueryInterfaceSuggestHandlerImpl extends UserContextService implements IQueryInterfaceSuggestHandler {

   private IQueryInterfaceSuggestService queryInterfaceSuggestService;
   private QueryValidationHelper         queryValidationHelper;

   public void setQueryInterfaceSuggestService (IQueryInterfaceSuggestService queryInterfaceSuggestService) {
      this.queryInterfaceSuggestService = queryInterfaceSuggestService;
   }

   public List<SuggestTerm> suggestBTsAndStatsForSelect (Long modelId, String searchString) throws HandlerException {
      try {
         return queryInterfaceSuggestService.suggestBTsAndStatsForSelect(modelId, searchString, getUserId());
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<SuggestTerm> suggestBTAndValuesForWhereRHS (Long modelId, String businessTerm, String searchString)
            throws HandlerException {
      try {
         return queryInterfaceSuggestService.suggestBTAndValuesForWhereRHS(modelId, businessTerm, searchString,
                  getUserId());
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<SuggestConditionTerm> suggestBTsAndStatsForWhereLHS (Long modelId, String searchString)
            throws HandlerException {
      try {
         return queryInterfaceSuggestService.suggestBTsAndStatsForWhereLHS(modelId, searchString, getUserId());
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<SuggestConditionTerm> suggestBTsForWhereLHS (Long modelId, String searchString, String statName)
            throws HandlerException {
      try {
         return queryInterfaceSuggestService.suggestBTsForWhereLHS(modelId, searchString, getUserId(), statName);
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<SuggestTerm> suggestBTsForOrderBy (Long modelId, String searchString) throws HandlerException {
      try {
         return queryInterfaceSuggestService.suggestBTsForOrderBy(modelId, searchString, getUserId());
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<SuggestTerm> suggestBTsForPopulation (Long modelId, String searchString) throws HandlerException {
      try {
         return queryInterfaceSuggestService.suggestBTsForPopulation(modelId, searchString, getUserId());
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<SuggestTerm> suggestBTsForSelect (Long modelId, String searchString, String statName)
            throws HandlerException {
      try {
         return queryInterfaceSuggestService.suggestBTsForSelect(modelId, searchString, statName, getUserId());
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<SuggestTerm> suggestBTsForSummarize (Long modelId, String searchString) throws HandlerException {
      try {
         return queryInterfaceSuggestService.suggestBTsForSummarize(modelId, searchString, getUserId());
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public QueryValidationResult validateRequest (String request, String context) throws HandlerException {
      return getQueryValidationHelper().processRequest(request, context);
   }

   private Long getUserId () {
      return getUserContext().getUser().getId();
   }

   public QueryValidationHelper getQueryValidationHelper () {
      return queryValidationHelper;
   }

   public void setQueryValidationHelper (QueryValidationHelper queryValidationHelper) {
      this.queryValidationHelper = queryValidationHelper;
   }
}
