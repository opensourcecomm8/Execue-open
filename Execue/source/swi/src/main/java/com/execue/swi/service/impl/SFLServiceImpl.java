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


package com.execue.swi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.execue.core.common.bean.entity.EntityNameVariation;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.dataaccess.ISFLDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ISFLService;

public class SFLServiceImpl implements ISFLService {

   private static final Logger      logger = Logger.getLogger(SFLServiceImpl.class);

   private ISFLDataAccessManager    sflDataAccessManager;
   private ISWIConfigurationService swiConfigurationService;
   private IKDXRetrievalService     kdxRetrievalService;

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public void generateSFLTerm (EntityNameVariation variation, Long modelGroupId,
            Map<String, Double> secondaryWordWeightMap) throws KDXException {
      // check if the SFLTerm already exists, if it does then abort the creation
      Long contextId = modelGroupId;
      if (getSflDataAccessManager().getSFLTermByWordAndContextId(variation.getName(), contextId) != null) {
         if (logger.isDebugEnabled()) {
            logger.debug("SFLTerm with business term [" + variation.getName() + "] already exists!");
         }
         return;
      }
      SFLTerm sflTerm = null;
      // check if the variation is eligible for SFLTerm generation
      String[] tokens = variation.getName().split(" ");
      if (tokens.length > 1) {
         Map<String, Double> secondaryTokensByWeight = new HashMap<String, Double>(1);
         double weightAccountedFor = 0;
         if (!variation.isSkipSecondaryWordCheck()) {
            for (int i = 0; i < tokens.length; i++) {
               String token = tokens[i].trim();
               if (token.length() == 0) {
                  continue;
               }
               Double secondaryWeight = getWeightIfSecondaryWord(secondaryWordWeightMap, token);
               if (secondaryWeight != null) {
                  secondaryTokensByWeight.put(token + i, secondaryWeight);
                  weightAccountedFor = weightAccountedFor + secondaryWeight;
               }
            }
         }
         double individualWeight = 0;
         boolean allSecondaryWords = false;
         if (secondaryTokensByWeight.size() < tokens.length) {
            individualWeight = (100 - weightAccountedFor) / (tokens.length - secondaryTokensByWeight.size());
         } else {
            allSecondaryWords = true;
            individualWeight = 100 / tokens.length;
         }
         weightAccountedFor = 0;
         List<SFLTermToken> sflTermTokens = new ArrayList<SFLTermToken>();
         List<SFLTermToken> primarySflTermTokens = new ArrayList<SFLTermToken>(1);
         for (int tokenOrder = 0; tokenOrder < tokens.length; tokenOrder++) {
            String token = tokens[tokenOrder].trim();
            if (token.length() == 0) {
               continue;
            }
            double weight = (int) individualWeight;
            Integer primary = secondaryTokensByWeight.containsKey(token + tokenOrder) ? 0 : 1;
            if (primary == 0 && !allSecondaryWords) {
               weight = secondaryTokensByWeight.get(token + tokenOrder);
            }

            SFLTermToken sflTermToken = populateSFLTermToken(tokens[tokenOrder], tokenOrder, weight, contextId, primary);
            sflTermToken.setBusinessTerm(variation.getName());
            if (primary == 1) {
               primarySflTermTokens.add(sflTermToken);
            }
            sflTermTokens.add(sflTermToken);
            weightAccountedFor += weight;
         }
         if (CollectionUtils.isEmpty(primarySflTermTokens)) {
            // Add the first token to set with the remaining weight
            // If no primary word found, then get the first SFL Term Token and mark it as primary and set the remaining
            // weight to it
            for (SFLTermToken termToken : sflTermTokens) {
               termToken.setRequired(1);
               termToken.setRequiredTokenCount(tokens.length);
            }
            SFLTermToken termToken = sflTermTokens.get(0);
            termToken.setPrimaryWord(1);
            termToken.setWeight(termToken.getWeight() + 100 - weightAccountedFor);
         } else if (weightAccountedFor < 100) {
            // Now set the remaining weight (most probably should be 1) to first primary token
            SFLTermToken termToken = primarySflTermTokens.get(0);
            termToken.setWeight(termToken.getWeight() + 100 - weightAccountedFor);
         }

         // Finally create the sfl term with the tokens
         sflTerm = new SFLTerm();
         sflTerm.setBusinessTerm(variation.getName());
         sflTerm.setContextId(contextId);
         sflTerm.setSflTermTokens(new HashSet<SFLTermToken>(sflTermTokens));
         createSFLTerm(sflTerm);
      }
   }

   private Double getWeightIfSecondaryWord (Map<String, Double> secondaryWordWeightMap, String token) {
      Double weight = null;
      if (secondaryWordWeightMap.containsKey(token.toLowerCase())) {
         weight = secondaryWordWeightMap.get(token.toLowerCase());
      } else if (getSwiConfigurationService().getPosContext().getConjAndByConjTermNames().contains(token.toLowerCase())) {
         weight = getDefaultWeightForConjunction();
      } else if (ExecueCoreUtil.isCardinalNumber(token)) {
         weight = getDefaultWeightForNumber();
      } else if (token.length() == 1) {
         weight = getDefaultWeightForSingleLetterToken();
      }
      return weight;
   }

   public void deleteSFLTerm (SFLTerm sflTerm) throws KDXException {
      getSflDataAccessManager().deleteSFLTerm(sflTerm);
   }

   public double getDefaultWeightForConjunction () {
      return Double.parseDouble(getSwiConfigurationService().getDefaultWeightForConjunctionSecondaryWords());
   }

   public double getDefaultWeightForSingleLetterToken () {
      return Double.parseDouble(getSwiConfigurationService().getDefaultWeightForSingleCharTokens());
   }

   public double getDefaultWeightForNumber () {
      return Double.parseDouble(getSwiConfigurationService().getDefaultWeightForNumberSecondaryWords());
   }

   /**
    * This method returns the SFLTermToken for the given inputs
    * 
    * @param businessTermToken
    * @param tokenOrder
    * @param weight
    * @param contextId
    * @return
    */
   private SFLTermToken populateSFLTermToken (String businessTermToken, int tokenOrder, double weight, Long contextId,
            Integer primaryWord) {
      SFLTermToken sflTermToken = new SFLTermToken();
      sflTermToken.setBusinessTermToken(businessTermToken);
      sflTermToken.setHits(0L);
      sflTermToken.setWeight(weight);
      sflTermToken.setGroup(1); // TODO: Why group is always set to 1
      sflTermToken.setPrimaryWord(primaryWord);
      sflTermToken.setOrder(tokenOrder);
      sflTermToken.setContextId(contextId);
      return sflTermToken;
   }

   public void createSFLTerm (SFLTerm sflTerm) throws KDXException {
      getSflDataAccessManager().createSFLTerm(sflTerm);
   }

   public ISFLDataAccessManager getSflDataAccessManager () {
      return sflDataAccessManager;
   }

   public void setSflDataAccessManager (ISFLDataAccessManager sflDataAccessManager) {
      this.sflDataAccessManager = sflDataAccessManager;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService
    *           the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

}
