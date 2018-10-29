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

import org.apache.log4j.Logger;

import com.execue.core.common.bean.QueryValidationResult;
import com.execue.core.exception.HandlerException;
import com.execue.handler.qi.IQueryInterfaceSuggestHandler;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author kaliki
 * @since 4.0
 */
public class QueryInterfaceValidationAction extends ActionSupport {

   private static final long             serialVersionUID = 1L;
   // XML request data
   private String                        request;
   private String                        context;
   private IQueryInterfaceSuggestHandler queryInterfaceSuggestHandler;
   private QueryValidationResult         result;
   private Logger                        logger           = Logger.getLogger(QueryInterfaceValidationAction.class);

   public String validateRequest () {

      try {
         if (logger.isDebugEnabled()) {
            logger.debug("request:" + request);
         }
         result = queryInterfaceSuggestHandler.validateRequest(request, context);
         if (result != null && result.getSuggestion() != null && result.getSuggestion().getErrMsg() != null) {
            String msgDesc = getText(result.getSuggestion().getErrMsg());
            if (msgDesc != null) {
               result.getSuggestion().setErrMsg(msgDesc);
            }
         }
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String getRequest () {
      return request;
   }

   public void setRequest (String request) {
      this.request = request;
   }

   public String getContext () {
      return context;
   }

   public void setContext (String context) {
      this.context = context;
   }

   public QueryValidationResult getResult () {
      return result;
   }

   public void setResult (QueryValidationResult result) {
      this.result = result;
   }

   public IQueryInterfaceSuggestHandler getQueryInterfaceSuggestHandler () {
      return queryInterfaceSuggestHandler;
   }

   public void setQueryInterfaceSuggestHandler (IQueryInterfaceSuggestHandler queryInterfaceSuggestHandler) {
      this.queryInterfaceSuggestHandler = queryInterfaceSuggestHandler;
   }
}
