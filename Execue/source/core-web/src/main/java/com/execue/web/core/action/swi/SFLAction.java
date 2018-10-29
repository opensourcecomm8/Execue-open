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


package com.execue.web.core.action.swi;

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.exception.ExeCueException;

public class SFLAction extends SWIAction {

   private static final Logger log = Logger.getLogger(SFLAction.class);

   private Long                modelId;

   private List<SFLTerm>       sflterms;
   private List<SFLTermToken>  sflTermsTokens;
   private String              sflTerm;

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public String input () {
      if (log.isDebugEnabled()) {
         log.info("Came to input flow");
      }
      return SUCCESS;
   }

   public String getAllSFLTerms () {
      try {
         sflterms = getKdxServiceHandler().getAllExistingSFLTerms();
      } catch (ExeCueException e) {
         addActionMessage(getText("execue.errors.general"));
         return ERROR;
      }
      return SUCCESS;
   }

   public String getSFLTermsTokens () {
      try {
         if (sflTerm != null) {
            sflTermsTokens = getKdxServiceHandler().getSFLTermTokensBySFLTerm(sflTerm);
         }
      } catch (ExeCueException e) {
         addActionMessage(getText("execue.errors.general"));
         return ERROR;
      }
      return SUCCESS;
   }

   public String updateSFLTermTokens () {
      try {
         getKdxServiceHandler().updateSFLTermTokens(getSflTermsTokens());
         addActionMessage(getText("execue.weights updated successfully"));
      } catch (Exception e) {
         addActionMessage(getText("execue.errors.general"));
         return ERROR;
      }
      return SUCCESS;
   }

   public String getSflTerm () {
      return sflTerm;
   }

   public void setSflTerm (String sflTerm) {
      this.sflTerm = sflTerm;
   }

   public List<SFLTerm> getSflterms () {
      return sflterms;
   }

   public void setSflterms (List<SFLTerm> sflterms) {
      this.sflterms = sflterms;
   }

   public List<SFLTermToken> getSflTermsTokens () {
      return sflTermsTokens;
   }

   public void setSflTermsTokens (List<SFLTermToken> sflTermsTokens) {
      this.sflTermsTokens = sflTermsTokens;
   }

}
