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

import org.apache.log4j.Logger;

import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.QueryResult;
import com.execue.core.common.bean.UserInput;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.driver.qi.IQueryInterfaceDriver;
import com.execue.handler.BaseHandler;
import com.execue.handler.QueryValidationHelper;
import com.execue.handler.bean.Response;
import com.execue.qi.exception.QIException;

/**
 * @author kaliki
 * @since 4.0
 */
public class QueryInterfaceHandler extends BaseHandler {

   private static final Logger   logger = Logger.getLogger(QueryInterfaceHandler.class);

   private IQueryInterfaceDriver driver;
   private QueryValidationHelper queryValidationHelper;

   public IQueryInterfaceDriver getDriver () {
      return driver;
   }

   public void setDriver (IQueryInterfaceDriver driver) {
      this.driver = driver;
   }

   public Response fetchResponse (Long userQueryId, Long businessQueryId, Long assetId) throws HandlerException {
      Response response = new Response();
      try {
         QueryResult queryResult = driver.getCachedQueryDataResult(userQueryId, businessQueryId, assetId);
         response.setObject(queryResult);
      } catch (ExeCueException exeCueException) {
         logger.error("QIException in QueryInterfaceHandler", exeCueException);
         logger.error("Actual Error : [" + exeCueException.getCode() + "] " + exeCueException.getMessage());
         logger.error("Cause : " + exeCueException.getCause());
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, "Error from Driver", exeCueException);
      }
      return response;
   }

   public Response fetchResponse (Long aggregateQueryId) throws HandlerException {
      Response response = new Response();
      try {
         QueryResult queryResult = driver.getCachedQueryDataResult(aggregateQueryId);
         response.setObject(queryResult);
      } catch (ExeCueException exeCueException) {
         throw new HandlerException(exeCueException.getCode(), "Error from Driver", exeCueException);
      }
      return response;
   }

   @Override
   public Object process (Object inputBean) throws HandlerException {
      QueryResult results = new QueryResult();
      UserInput userInput = (UserInput) inputBean;
      if (getUserContext().getUser() != null) {
         userInput.setUserId(getUserContext().getUser().getId());
      }
      QueryForm queryForm = userInput.getQueryForm();
      String selectText = queryForm.getSelectText();
      try {
         if (ExecueCoreUtil.isEmpty(selectText)) {
            results = driver.process(userInput);
         } else {
            if (logger.isDebugEnabled()) {
               logger.debug("selectText: " + selectText);
            }
            results.setQuerySuggestions(getQueryValidationHelper().processQueryForm(queryForm));
         }
      } catch (QIException qiException) {
         logger.error("QIException in QueryInterfaceHandler", qiException);
         logger.error("Actual Error : [" + qiException.getCode() + "] " + qiException.getMessage());
         logger.error("Cause : " + qiException.getCause());
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, "Error from QIService", qiException
                  .getCause());
      } catch (ExeCueException exeCueException) {
         logger.error("QIException in QueryInterfaceHandler", exeCueException);
         logger.error("Actual Error : [" + exeCueException.getCode() + "] " + exeCueException.getMessage());
         logger.error("Cause : " + exeCueException.getCause());
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, "Error from Driver", exeCueException);
      }
      return results;
   }

   public QueryValidationHelper getQueryValidationHelper () {
      return queryValidationHelper;
   }

   public void setQueryValidationHelper (QueryValidationHelper queryValidationHelper) {
      this.queryValidationHelper = queryValidationHelper;
   }

   // private List<String> getSuggestedTerm (String selectTerm, List<String> swiSelectTerms) {
   // List<String> suggestions = new ArrayList<String>();
   // List<String> suggestedSelTerms = new ArrayList<String>();
   // for (String selTerm : swiSelectTerms) {
   // suggestedSelTerms.add(selTerm.toLowerCase());
   // }
   // for (String swiSelectTerm : suggestedSelTerms) {
   // if (swiSelectTerm.contains(selectTerm.toLowerCase())) {
   // suggestions.add(swiSelectTerm);
   // }
   // }
   // return suggestions;
   // }

   // private List<String> querySuggestTerm () {
   // List<String> list = new ArrayList<String>();
   // list.add("Account");
   // list.add("Account Age");
   // list.add("Account Center Login");
   // list.add("Account Status");
   // list.add("Balance Transfer Amount");
   // list.add("Balance Transfer Balance");
   // list.add("Balance Transfer Balance Indicator");
   // list.add("Behavior Score");
   // list.add("Bill Month");
   // list.add("Sum");
   // list.add("Average");
   // list.add("Maximum");
   // list.add("Standard Deviation");
   // list.add("Summation");
   // list.add("Ficcoscore");
   // list.add("Utilization");
   // return list;
   // }

   /*
    * private List<SuggestTerm> querySuggestTerm () { List<SuggestTerm> list = new ArrayList<SuggestTerm>(); list =
    * new ArrayList<SuggestTerm>(); SuggestTerm bt = new SuggestTerm(); bt.setName("AccountStatus");
    * bt.setDisplayName("Account Status"); bt.setType(SuggestTermType.CONCEPT); list.add(bt); SuggestTerm bt1 = new
    * SuggestTerm(); bt1.setName("FicoScore"); bt1.setDisplayName("Fico Score"); bt1.setType(SuggestTermType.CONCEPT);
    * list.add(bt1); SuggestTerm bt2 = new SuggestTerm(); bt2.setName("MerchandiseAmount");
    * bt2.setDisplayName("Merchandise Sales Amount"); bt2.setType(SuggestTermType.CONCEPT); list.add(bt2); return list; }
    */

}
