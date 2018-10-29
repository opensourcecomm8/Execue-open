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


/**
 *
 */
package com.execue.driver.uss.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import com.execue.core.common.bean.QueryResult;
import com.execue.core.common.bean.RelatedUserQuery;
import com.execue.core.common.bean.UnstructuredQueryResult;
import com.execue.core.common.bean.UserInput;
import com.execue.core.common.bean.nlp.NLPInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.nlp.UserQuery;
import com.execue.core.common.bean.qdata.QDataReducedQuery;
import com.execue.core.common.bean.qdata.RFXValue;
import com.execue.core.common.bean.qdata.ReducedFormIndex;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.driver.semantic.helper.SemanticDriverHelper;
import com.execue.driver.uss.IUnstructuredSearchDriver;
import com.execue.nlp.engine.INLPEngine;
import com.execue.nlp.exception.NLPException;
import com.execue.platform.IUidService;
import com.execue.pseudolang.service.IPseudoLanguageService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.exception.RFXException;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.service.IRFXService;
import com.execue.querycache.configuration.IQueryCacheService;
import com.execue.querycache.exception.QueryCacheException;
import com.execue.security.IUserContext;
import com.execue.uss.configuration.impl.UnstructuredSearchConfigurationServiceImpl;
import com.execue.uss.exception.USSException;

/**
 * @author Nitesh
 */
public class UnstructuredSearchDriver implements IUnstructuredSearchDriver {

   private Logger                                     logger = Logger.getLogger(UnstructuredSearchDriver.class);
   private ICoreConfigurationService                  coreConfigurationService;
   private UnstructuredSearchConfigurationServiceImpl unstructuredSearchConfigurationService;
   private INLPEngine                                 nlpEngine;
   private IQueryCacheService                         queryCacheService;
   private IRFXService                                rfxService;
   private IUidService                                transactionIdGenerationService;
   private IUserContext                               userContext;
   private IPseudoLanguageService                     pseudoLanguageService;
   private SemanticDriverHelper                       semanticDriverHelper;

   protected long getQueryId () {
      try {
         return getTransactionIdGenerationService().getNextId();
      } catch (Exception e) {
         return -1;
      }
   }

   public QueryResult process (Object userInputObject) throws USSException {
      UnstructuredQueryResult queryResult = new UnstructuredQueryResult();
      try {
         UserInput userInput = (UserInput) userInputObject;
         Long userQueryId = getQueryId();
         String request = userInput.getRequest();
         Set<SemanticPossibility> semanticPossibilities = new HashSet<SemanticPossibility>(1);
         List<RelatedUserQuery> relatedUserQueries = new ArrayList<RelatedUserQuery>(1);

         // Validate the length of the user request
         if (request.length() >= getCoreConfigurationService().getMaxUserQueryLength()) {
            logger.debug("MAX LIMIT EXCEEDED....throwing exception");
            throw new USSException(ExeCueExceptionCodes.MAX_USER_QUERY_LENGTH_EXCEPTION_CODE,
                     "User Query has exceeded maximum limit");
         }

         // Prepare the user query
         UserQuery userQuery = new UserQuery();
         userQuery.setUserQueryId(userQueryId);
         userQuery.setOriginalQuery(request);
         userQuery.setSearchFilter(userInput.getSearchFilter());

         // Obtain possibilities for the user query
         if (!userInput.isRequestSemantified()) {
            NLPInformation nlpInformation = getNlpEngine().processQuery(userQuery, null);
            semanticPossibilities = nlpInformation.getReducedForms().keySet();
         } else {
            semanticPossibilities = userInput.getUserPossibilities();
         }

         if (ExecueCoreUtil.isCollectionNotEmpty(semanticPossibilities)) {
            for (SemanticPossibility possibility : semanticPossibilities) {
               if (logger.isInfoEnabled()) {
                  logger.info("\nReduced Form (" + possibility.getId() + ")  ["
                           + possibility.getWeightInformationForExplicitEntities().getFinalWeight() + "],\n"
                           + possibility.getDisplayString());
               }
            }
         }

         // Reset the search filter
         userQuery.setSearchFilter(userInput.getSearchFilter());

         // Get the related user queries
         relatedUserQueries = getRelatedUserQueries(semanticPossibilities, userQuery);

         // Populate the query result
         queryResult.setId(userQueryId);
         queryResult.setQueryName(request);
         queryResult.setSemanticPossibilities(semanticPossibilities);
         queryResult.setRelatedUserQueries(relatedUserQueries);

         // NK:: Assumption here is unstructured search driver is app scoped and we are getting only one possibility out
         List<SemanticPossibility> possibilities = new ArrayList<SemanticPossibility>(semanticPossibilities);
         if (!CollectionUtils.isEmpty(possibilities)) {
            SemanticPossibility possibility = possibilities.get(0);
            String colorCodedRequest = pseudoLanguageService.getColorCodedPseudoLanguageStatement(possibility
                     .getQueryWords(), possibility.getWordRecognitionStates());
            queryResult.setColorCodedRequest(colorCodedRequest);
         }

      } catch (NLPException nlpException) {
         logger.error("NLPException in UnstructuredSearchDriver");
         logger.error("Actual Error : " + nlpException.getMessage());
         logger.error("Cause : " + nlpException.getCause());
         logger.error(nlpException, nlpException);
      } catch (QueryDataException queryDataException) {
         logger.error("QueryDataException in UnstructuredSearchDriver");
         logger.error("Actual Error : " + queryDataException.getMessage());
         logger.error("Cause : " + queryDataException.getCause());
         logger.error(queryDataException, queryDataException);
      } catch (UDXException udxException) {
         logger.error("UDXException in UnstructuredSearchDriver");
         logger.error("Actual Error : " + udxException.getMessage());
         logger.error("Cause : " + udxException.getCause());
         logger.error(udxException, udxException);
      } catch (RFXException rfxException) {
         logger.error("RFXException in UnstructuredSearchDriver");
         logger.error("Actual Error : " + rfxException.getMessage());
         logger.error("Cause : " + rfxException.getCause());
         logger.error(rfxException, rfxException);
      } catch (QueryCacheException queryCacheException) {
         logger.error("QueryCacheException in UnstructuredSearchDriver");
         logger.error("Actual Error : " + queryCacheException.getMessage());
         logger.error("Cause : " + queryCacheException.getCause());
         logger.error(queryCacheException, queryCacheException);
      } finally {
         MDC.remove("txnId");
      }
      return queryResult;
   }

   private List<RelatedUserQuery> getRelatedUserQueries (Set<SemanticPossibility> semanticPossibilities,
            UserQuery userQuery) throws QueryCacheException, QueryDataException, RFXException, UDXException {
      List<RelatedUserQuery> relatedUserQueries = new ArrayList<RelatedUserQuery>(1);

      if (CollectionUtils.isEmpty(semanticPossibilities)) {
         return relatedUserQueries;
      }
      // Generate QDataReducedQuery entries used for related searches
      Map<Long, QDataReducedQuery> reducedQueryByAppId = new HashMap<Long, QDataReducedQuery>(1);

      //TODO:: NK: Need to see how we get the below map(s) information for generic unstructured apps
      Map<Long, Integer> conceptPriorityByBedId = new HashMap<Long, Integer>();
      Map<String, String> dependentConceptByRequiredConcept = new HashMap<String, String>();
      int counter = 0;
      double baseUserQueryWeight = userQuery.getBaseWeightInformation().getFinalWeight();
      Long userQueryId = userQuery.getUserQueryId();
      Set<ReducedFormIndex> rfxList = new HashSet<ReducedFormIndex>(1);
      Set<RFXValue> rfxValues = new HashSet<RFXValue>(1);
      Map<Long, Set<ReducedFormIndex>> rfxByAppId = new HashMap<Long, Set<ReducedFormIndex>>(1);

      getSemanticDriverHelper().generateRIUserQueries(semanticPossibilities, rfxList, rfxValues, reducedQueryByAppId,
               rfxByAppId, conceptPriorityByBedId, userQueryId, baseUserQueryWeight, dependentConceptByRequiredConcept);

      return getQueryCacheService().performUniversalSearchForRelatedQueries(userQueryId, reducedQueryByAppId,
               userQuery.getOriginalQuery());
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   /**
    * @return the unstructuredSearchConfigurationService
    */
   public UnstructuredSearchConfigurationServiceImpl getUnstructuredSearchConfigurationService () {
      return unstructuredSearchConfigurationService;
   }

   /**
    * @param unstructuredSearchConfigurationService the unstructuredSearchConfigurationService to set
    */
   public void setUnstructuredSearchConfigurationService (
            UnstructuredSearchConfigurationServiceImpl unstructuredSearchConfigurationService) {
      this.unstructuredSearchConfigurationService = unstructuredSearchConfigurationService;
   }

   /**
    * @return the nlpEngine
    */
   public INLPEngine getNlpEngine () {
      return nlpEngine;
   }

   /**
    * @param nlpEngine the nlpEngine to set
    */
   public void setNlpEngine (INLPEngine nlpEngine) {
      this.nlpEngine = nlpEngine;
   }

   /**
    * @return the queryCacheService
    */
   public IQueryCacheService getQueryCacheService () {
      return queryCacheService;
   }

   /**
    * @param queryCacheService the queryCacheService to set
    */
   public void setQueryCacheService (IQueryCacheService queryCacheService) {
      this.queryCacheService = queryCacheService;
   }

   /**
    * @return the rfxService
    */
   public IRFXService getRfxService () {
      return rfxService;
   }

   /**
    * @param rfxService the rfxService to set
    */
   public void setRfxService (IRFXService rfxService) {
      this.rfxService = rfxService;
   }

   /**
    * @return the transactionIdGenerationService
    */
   public IUidService getTransactionIdGenerationService () {
      return transactionIdGenerationService;
   }

   /**
    * @param transactionIdGenerationService the transactionIdGenerationService to set
    */
   public void setTransactionIdGenerationService (IUidService transactionIdGenerationService) {
      this.transactionIdGenerationService = transactionIdGenerationService;
   }

   /**
    * @return the userContext
    */
   public IUserContext getUserContext () {
      return userContext;
   }

   /**
    * @param userContext the userContext to set
    */
   public void setUserContext (IUserContext userContext) {
      this.userContext = userContext;
   }

   /**
    * @return the semanticDriverHelper
    */
   public SemanticDriverHelper getSemanticDriverHelper () {
      return semanticDriverHelper;
   }

   /**
    * @param semanticDriverHelper the semanticDriverHelper to set
    */
   public void setSemanticDriverHelper (SemanticDriverHelper semanticDriverHelper) {
      this.semanticDriverHelper = semanticDriverHelper;
   }

   /**
    * @return the pseudoLanguageService
    */
   public IPseudoLanguageService getPseudoLanguageService () {
      return pseudoLanguageService;
   }

   /**
    * @param pseudoLanguageService the pseudoLanguageService to set
    */
   public void setPseudoLanguageService (IPseudoLanguageService pseudoLanguageService) {
      this.pseudoLanguageService = pseudoLanguageService;
   }

}