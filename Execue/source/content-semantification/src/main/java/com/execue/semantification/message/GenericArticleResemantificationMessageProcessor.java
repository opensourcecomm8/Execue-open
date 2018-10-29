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


package com.execue.semantification.message;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import com.execue.message.exception.MessageException;
import com.execue.semantification.batch.service.IGenericArticleResemantificationService;
import com.execue.semantification.bean.GenericArticleResemantificationMessage;

public class GenericArticleResemantificationMessageProcessor {

   private static final Logger                     log = Logger
                                                                .getLogger(GenericArticleResemantificationMessageProcessor.class);
   private IGenericArticleResemantificationService genericArticleResemantificationService;

   void processMessage (GenericArticleResemantificationMessage resemantificationMessage) throws MessageException {
      try {
         String tidValue = "txnId-" + resemantificationMessage.getUserQueryId();
         MDC.put("txnId", tidValue);
         long contextId = resemantificationMessage.getContextId();
         long userQueryId = resemantificationMessage.getUserQueryId();
         getGenericArticleResemantificationService().semantifiArticles(contextId, userQueryId);
      } finally {
         MDC.remove("txnId");
      }
   }

   /**
    * @return the genericArticleResemantificationService
    */
   public IGenericArticleResemantificationService getGenericArticleResemantificationService () {
      return genericArticleResemantificationService;
   }

   /**
    * @param genericArticleResemantificationService the genericArticleResemantificationService to set
    */
   public void setGenericArticleResemantificationService (
            IGenericArticleResemantificationService genericArticleResemantificationService) {
      this.genericArticleResemantificationService = genericArticleResemantificationService;
   }
}
