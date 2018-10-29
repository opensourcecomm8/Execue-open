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


package com.execue.nlp.engine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.NLPInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.nlp.UserQuery;
import com.execue.core.common.bean.swi.ApplicationScopeInfo;
import com.execue.core.common.type.SearchFilterType;
import com.execue.core.common.type.SearchType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.bean.NLPToken;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.nlp.bean.matrix.RootMatrix;
import com.execue.nlp.configuration.INLPConfigurationService;
import com.execue.nlp.engine.INLPEngine;
import com.execue.nlp.engine.barcode.BarcodeScannerFactory;
import com.execue.nlp.engine.barcode.RMManager;
import com.execue.nlp.engine.barcode.matrix.MatrixUtility;
import com.execue.nlp.engine.barcode.possibility.PossibilityController;
import com.execue.nlp.engine.barcode.possibility.SemanticScopingHandler;
import com.execue.nlp.exception.NLPException;
import com.execue.nlp.exception.NLPExceptionCodes;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.generator.ReducedFormGenerator;
import com.execue.nlp.helper.NLPServiceHelper;
import com.execue.nlp.preprocessor.IPreProcessor;
import com.execue.nlp.processor.ISummarizationService;
import com.execue.nlp.processor.impl.SemanticScopingOntoRecognitionProcessor;
import com.execue.nlp.util.NLPUtilities;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IEASIndexService;
import com.execue.util.CollectionUtil;
import com.execue.util.MathUtil;

/**
 * @author Kaliki
 */
public class NLPEngineImpl implements INLPEngine {

   private final Logger                            logger = Logger.getLogger(NLPEngineImpl.class);

   private INLPConfigurationService                nlpConfigurationService;
   private RMManager                               RMManager;
   private ReducedFormGenerator                    reducedFormGenerator;
   private SFLTermHitsUpdateHelper                 sfTermHitsUpdateHelper;
   private SemanticScopingHandler                  semanticScopingHandler;
   private NLPWorkflowManager                      nlpWorkflowManager;
   private CumulativeDecisionMaker                 cumulativeDecisionMaker;
   private SemanticScopingOntoRecognitionProcessor ontoRecognitionProcessor;
   private ISummarizationService                   summarizationService;
   private NLPServiceHelper                        nlpServiceHelper;
   private IEASIndexService                        easIndexService;
   private List<IPreProcessor>                     preProcessors;
   private IApplicationRetrievalService            applicationRetrievalService;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.engine.INLPEngine#processQuery(java.lang.String)
    */
   public NLPInformation processQuery (String userQuerySentence) throws NLPException {
      try {
         // Create the general search filter
         SearchFilter searchFilter = new SearchFilter();
         searchFilter.setSearchFilterType(SearchFilterType.GENERAL);

         // Create the user query with the search filter and user query sentence
         UserQuery userQuery = new UserQuery();
         userQuery.setSearchFilter(searchFilter);
         userQuery.setOriginalQuery(userQuerySentence);

         // Call process query
         return processQuery(userQuery, null);
      } catch (NLPException nlpException) {
         throw nlpException;
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.nlp.INLPEngine#processQuery(com.execue.core.common.bean.nlp.UserQuery,
    *      java.lang.String)
    */
   public NLPInformation processQuery (UserQuery userQuery, String contextName) throws NLPException {
      long startTime = System.currentTimeMillis();
      String userQuerySentence = userQuery.getOriginalQuery();

      if (StringUtils.isEmpty(userQuerySentence)) {
         throw new NLPSystemException(NLPExceptionCodes.DEFAULT_EXCEPTION_CODE, "User Query Cannot be empty");
      }

      NLPInformation nlpInfo = new NLPInformation();
      nlpInfo.setUserQuery(userQuerySentence);
      userQuery.setNlpInformation(nlpInfo);

      // Perform PreProcessing.
      String taggedUserQuerySentence = performPreProcessing(userQuerySentence);
      if (StringUtils.isEmpty(taggedUserQuerySentence)) {
         throw new NLPSystemException(NLPExceptionCodes.DEFAULT_EXCEPTION_CODE, "User Query Cannot be empty");
      }

      // Prepare the NLP Tokens from the tagged userQuerySentence
      List<NLPToken> nlpTokens = MatrixUtility.unmarshalNameValuePairs(taggedUserQuerySentence);
      List<String> queryWords = prepareQueryWords(nlpTokens);

      // Validate as per the configured allowed max tokens, skip in case of backend nlp processing like article
      // semantification
      if (!userQuery.isBackendSearch()) {
         validateMaxTokens(queryWords, userQuery.getOriginalQuery());
      }

      // We have list of tokens in hand and we need to handle search filter here
      // as well as app scoping also.
      try {
         userQuery.getSearchFilter().setOriginalSearchFilterType(userQuery.getSearchFilter().getSearchFilterType());
         if (userQuery.getSearchFilter().isAppScopingEnabled()) {
            userQuery.getSearchFilter().setAppIds(handleAppScoping(userQuery, nlpTokens));
            userQuery.getSearchFilter().setSearchFilterType(SearchFilterType.APP_SCOPED);
         }
      } catch (SWIException swiException) {
         throw new NLPException(swiException.getCode(), swiException.getMessage(), swiException.getCause());
      }

      // Prepare the root matrix
      RootMatrix rootMatrix = createRootMatrix(nlpTokens, userQuery, contextName);
      rootMatrix.setProcessStartTime(System.currentTimeMillis());

      // As English is ambiguous language, and user may or may not specify question or query
      // in perfect english, we have to assume multiple possibilities may exist for the question
      // We will start with one possibility and as we understand more and more about user query
      // we may have to add more possibilities
      // All this is handled by a single Possibility Controller
      PossibilityController pController = BarcodeScannerFactory.getInstance().createPossibilityController();
      pController.setRootMatrix(rootMatrix);

      if (logger.isDebugEnabled()) {
         logger.debug("Starting Semantic Possibility Generation For User Query: " + userQuerySentence);
      }

      // Based on the search type of user query and its related configuration setting will perform the respective NLP
      // steps
      SearchType searchType = userQuery.getSearchType();

      // STEP 1: Perform the semantic processing
      if (nlpConfigurationService.getFalgValueForSearchType(searchType, NLPConstants.NLP_RECOGNITION_SEMANTIC_SCOPING)) {
         pController.performSemanticScoping();
      }

      // Onto Recognition will be executed before doing any thing else.
      runOntoRecognition(rootMatrix, userQuery.getSearchFilter());

      for (Possibility currentPossibility : rootMatrix.getPossibilities()) {
         getSummarizationService().updatPossibilityWithSemanticScopingOutput(currentPossibility);
      }

      // Print the total quality and coverage of recognitions against each model
      if (logger.isDebugEnabled() && userQuery.getSearchType() == SearchType.ENTITY_SEARCH) {
         printTotalCoveragePerModel(rootMatrix.getPossibilities(), queryWords.size());
      }

      // STEP 2: Find the semantics
      if (nlpConfigurationService.getFalgValueForSearchType(searchType, NLPConstants.NLP_RECOGNITION_FIND_MEANING)) {
         rootMatrix.setFindSemanticsStartTime(System.currentTimeMillis());
         getNlpWorkflowManager().findSemantics(pController);
         if (logger.isDebugEnabled()) {
            logger.debug("End of Find Semantics For User Query: " + userQuerySentence);
         }
      }

      // STEP 3: Enhance the semantics
      if (nlpConfigurationService.getFalgValueForSearchType(searchType, NLPConstants.NLP_RECOGNITION_ENHANCE_MEANING)) {
         rootMatrix.setEnhanceSemanticsStartTime(System.currentTimeMillis());
         getNlpWorkflowManager().enhanceSemantics(pController);
         if (logger.isDebugEnabled()) {
            logger.debug("End of Enhance Semantics For User Query: " + userQuerySentence);
         }
      } else {
         try {
            getNlpServiceHelper().populateModelBasedPossibilities(rootMatrix);
         } catch (KDXException e) {
            throw new NLPException(e.code, e.getMessage(), e);
         }
      }

      // Populate the unambiguous possibilities
      getSummarizationService().populateUnambiguousPossibilities(rootMatrix, userQuery);

      // Return, if no possibilities got generated
      if (CollectionUtils.isEmpty(rootMatrix.getPossibilities())) {
         return nlpInfo;
      }

      // Assign the weight and standardized weight to the possibilities
      sortAndAssignWeight(rootMatrix.getPossibilities());

      // Choose the best possibility or filter the possibilities based on nlp configuration
      List<Possibility> possibilities = new ArrayList<Possibility>(1);
      if (getNlpConfigurationService().isChooseBestpossibility()) {
         possibilities = getCumulativeDecisionMaker().chooseBestPossibilityPerModel(rootMatrix.getPossibilities());
      } else {
         possibilities = getCumulativeDecisionMaker().filterByCluster(rootMatrix.getPossibilities());
      }

      // Generate the reduced forms
      List<Possibility> possibilitiesToIterate = new ArrayList<Possibility>(1);
      List<SemanticPossibility> reducedForms = generateReducedForms(possibilities, possibilitiesToIterate, queryWords,
               userQuery.getSentenceId());
      Map<SemanticPossibility, Integer> sortedReducedForms = getSortedReducedForms(userQuery, reducedForms);
      nlpInfo.setReducedForms(sortedReducedForms);

      // TODO: -JVK- Handle exceptions
      try {
         if (!CollectionUtils.isEmpty(possibilitiesToIterate)) {
            getSfTermHitsUpdateHelper().updatePopularityHitsForToken(possibilitiesToIterate);
            // NK: COMMENTED THE DO YOU MEAN POPULATION LOGIC, SHOULD UPDATE THE LOGIC PER NLP4 CHANGES IF NEEDED IN
            // FUTURE
            // if(possibilitiesToIterate.size() > 1) {
            // getSfTermHitsUpdateHelper().populateTokenCandidates(possibilitiesToIterate, nlpInfo, nlpTokens);
            // getSfTermHitsUpdateHelper().filterTokenCandidates(nlpInfo);
            // }
         }
      } catch (Exception e) {
         logger.error("Exception while updating tokenCandidates ", e);
      }
      userQuery.setNlpInformation(nlpInfo);
      long endTime = System.currentTimeMillis();
      if (logger.isInfoEnabled()) {
         logger.info("Time taken By NLP Engine " + (endTime - startTime) / 1000.0 + " seconds");
      }
      return nlpInfo;
   }

   protected Map<SemanticPossibility, Integer> getSortedReducedForms (UserQuery userQuery,
            List<SemanticPossibility> reducedForms) {
      // Sort the reduced forms by descending weight
      // be careful with this as Each JAVA implementation by default tends to set it as descending or ascending at
      // random
      Map<SemanticPossibility, Double> reducedFormMap = new HashMap<SemanticPossibility, Double>(1);
      int counter = 0;
      double totalAverageWeight = 0;
      if (ExecueCoreUtil.isCollectionEmpty(reducedForms)) {
         return null;
      }

      for (SemanticPossibility reducedForm : reducedForms) {
         totalAverageWeight = reducedForm.getWeightInformationForExplicitEntities().getFinalWeight();
         reducedFormMap.put(reducedForm, totalAverageWeight);
         ++counter;
         if (logger.isInfoEnabled()) {
            logger.info("\nReduced Form " + counter + "(" + reducedForm.getId() + ")  [" + totalAverageWeight
                     + " out of " + userQuery.getBaseWeightInformation().getFinalWeight() + "],\n"
                     + reducedForm.getDisplayString());
         }
      }

      Map<SemanticPossibility, Double> sortedReducedFormMap = CollectionUtil.sortMapOnValue(NLPUtilities
               .getRelativePercentageMap(reducedFormMap));
      Set<SemanticPossibility> possibilities = sortedReducedFormMap.keySet();

      //Reset the consecutive possibility ids
      assignIds(possibilities);

      return getReducedFormById(possibilities);
   }

   private Map<SemanticPossibility, Integer> getReducedFormById (Set<SemanticPossibility> possibilities) {
      Map<SemanticPossibility, Integer> reducedForms = new LinkedHashMap<SemanticPossibility, Integer>(1);
      for (SemanticPossibility semanticPossibility : possibilities) {
         reducedForms.put(semanticPossibility, semanticPossibility.getId().intValue());
      }
      return reducedForms;
   }

   private Set<SemanticPossibility> assignIds (Set<SemanticPossibility> possibilities) {
      long id = 1;
      for (Iterator<SemanticPossibility> iterator = possibilities.iterator(); iterator.hasNext();) {
         SemanticPossibility semanticPossibility = iterator.next();
         semanticPossibility.setId(id++);
      }
      return possibilities;
   }

   /**
    * Method to return the possibility with all the recognition entities present in input list of nlpTokens.
    * 
    * @param nlpTokens
    * @return the List<Possibility>
    */
   private List<Possibility> getPossibilitiesForRootMatrix (List<NLPToken> nlpTokens) {
      List<Possibility> possibilities = new ArrayList<Possibility>();
      if (CollectionUtils.isEmpty(nlpTokens)) {
         return possibilities;
      }
      List<IWeightedEntity> recEntities = new ArrayList<IWeightedEntity>();
      for (NLPToken token : nlpTokens) {
         recEntities.addAll(token.getRecognitionEntities());
      }
      Possibility possibility = new Possibility();
      possibility.setRecognitionEntities(recEntities);
      possibilities.add(possibility);
      return possibilities;
   }

   private List<Long> handleAppScoping (UserQuery userQuery, List<NLPToken> nlpTokens) throws SWIException {
      SearchFilter searchFilter = userQuery.getSearchFilter();
      List<Long> scopedApplicationIds = new ArrayList<Long>();
      if (searchFilter.getSearchFilterType() == SearchFilterType.APP) {
         scopedApplicationIds.add(searchFilter.getId());
      } else if (searchFilter.getSearchFilterType() == SearchFilterType.VERTICAL) {
         List<ApplicationScopeInfo> scopedApps = getEasIndexService().getScopedAppsByVertical(getTokens(nlpTokens),
                  searchFilter.getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(scopedApps)) {
            scopedApplicationIds.addAll(getAppIds(scopedApps));
         }
      } else if (searchFilter.getSearchFilterType() == SearchFilterType.GENERAL) {
         List<ApplicationScopeInfo> scopedApps = getEasIndexService().getScopedApps(getTokens(nlpTokens));
         if (ExecueCoreUtil.isCollectionNotEmpty(scopedApps)) {
            scopedApplicationIds.addAll(getAppIds(scopedApps));
         }
      }
      return scopedApplicationIds;
   }

   private List<Long> getAppIds (List<ApplicationScopeInfo> appScopeInfoList) {
      List<Long> appIds = new ArrayList<Long>();
      for (ApplicationScopeInfo applicationScopeInfo : appScopeInfoList) {
         appIds.add(applicationScopeInfo.getAppId());
      }
      return appIds;
   }

   private List<String> getTokens (List<NLPToken> nlpTokens) {
      List<String> tokens = new ArrayList<String>();
      for (NLPToken nToken : nlpTokens) {
         tokens.add(nToken.getWord());
      }
      return tokens;
   }

   /**
    * @param possibilities
    * @param possibilitiesToIterate
    * @param queryWords
    * @param sentenceId
    * @return the Map<SemanticPossibility, Integer>
    * @throws NLPException
    */
   private List<SemanticPossibility> generateReducedForms (List<Possibility> possibilities,
            List<Possibility> possibilitiesToIterate, List<String> queryWords, Integer sentenceId) throws NLPException {
      if (logger.isDebugEnabled()) {
         logger.debug("Starting Generation of Reduced Forms... ");
      }

      List<SemanticPossibility> reducedForms = new ArrayList<SemanticPossibility>(1);
      List<List<IGraphComponent>> reducedFormComponents = new ArrayList<List<IGraphComponent>>(1);
      for (Possibility poss : possibilities) {
         boolean found = false;
         SemanticPossibility rf = getReducedFormGenerator().generateReducedForm(poss, sentenceId);
         if (rf.getAllGraphComponents().size() == 0) {
            continue;
         }
         rf.setId((long) poss.getId());
         rf.setQueryWords(queryWords);
         rf.setModel(poss.getModel());
         // TODO: NK: Remove the below db call if in future we have the application coming from Possibility itself, then
         // can directly set it
         try {
            List<Application> applications = getApplicationRetrievalService().getApplicationsByModelId(
                     poss.getModel().getId());
            Application application = applications.get(0);
            rf.setApplication(application);
         } catch (KDXException e) {
            throw new NLPException(e.code, e.getMessage(), e);
         }

         // Set the possibility weight to SemanticPossibility
         rf.setWeightInformationForExplicitEntities(poss.getWeightInformationForExplicitEntities());
         rf.setWeightInformationForAssociations(poss.getWeightInfoForAssociation());
         rf.setWeightInformationForFrmeworks(poss.getFrameworkWeightInformation());
         rf.setAppWeight(poss.getAppWeight());
         rf.setStandarizedApplicationWeight(poss.getStandarizedAppWeight());
         rf.setPossiblityWeight(poss.getPossibilityWeight());
         rf.setStandarizedPossiblityWeight(poss.getStandarizedPossiblityWeight());
         List<IGraphComponent> curList = rf.getAllGraphComponents();
         for (List<IGraphComponent> tempList : reducedFormComponents) {
            if (tempList.size() == curList.size() && tempList.containsAll(curList)) {
               found = true;
               break;
            }
         }
         if (!found) {
            possibilitiesToIterate.add(poss);
            reducedFormComponents.add(curList);
            reducedForms.add(rf);
         }
      }
      if (logger.isDebugEnabled()) {
         logger.debug("End Generation of Reduced Forms ");
      }
      return reducedForms;
   }

   private void printTotalCoveragePerModel (List<Possibility> possibilities, int totalTokens) {
      if (CollectionUtils.isEmpty(possibilities)) {
         return;
      }
      for (Possibility possibility : possibilities) {
         Map<Long, List<IWeightedEntity>> recEntitiesMapByModelGroup = NLPUtilities
                  .getRecEntitiesMapByModelGroup(possibility.getRecognitionEntities());

         List<IWeightedEntity> baseModelRecEntities = recEntitiesMapByModelGroup.get(1L);
         if (CollectionUtils.isEmpty(baseModelRecEntities)) {
            baseModelRecEntities = new ArrayList<IWeightedEntity>(1);
         }

         // Prepare the positions set for base model rec entities
         Set<Integer> positionsForRecsFromBaseModel = NLPUtilities.getReferredTokenPositions(baseModelRecEntities);

         for (Entry<Long, List<IWeightedEntity>> entry : recEntitiesMapByModelGroup.entrySet()) {
            Set<Integer> finalPositionsForRecsFromBaseModel = new HashSet<Integer>(1);
            finalPositionsForRecsFromBaseModel.addAll(positionsForRecsFromBaseModel);
            Long modelId = entry.getKey();
            if (modelId.equals(1L)) {
               continue;
            }

            List<IWeightedEntity> recEntities = entry.getValue();
            recEntities.addAll(baseModelRecEntities);

            Double totalQuality = 0.0;
            Set<Integer> positionsCovered = new HashSet<Integer>(1);

            // Get the list of rec entities against position
            Map<Integer, List<IWeightedEntity>> recEntitiesByPosMap = NLPUtilities
                     .getRecognitionEntitiesByPositionMap(recEntities);
            for (Entry<Integer, List<IWeightedEntity>> recEntitiesByPosEntry : recEntitiesByPosMap.entrySet()) {
               Integer position = recEntitiesByPosEntry.getKey();
               List<IWeightedEntity> recEntitiesForCurrentPosition = recEntitiesByPosEntry.getValue();
               // Take the max quality rec entity at each position to find the average quality
               NLPUtilities.sortRecEntitiesByQuality(recEntitiesForCurrentPosition);
               positionsCovered.add(position);
               totalQuality += ((RecognitionEntity) recEntitiesForCurrentPosition.get(0)).getRecognitionQuality();
               finalPositionsForRecsFromBaseModel.remove(position);
            }

            Double averageQuality = totalQuality / positionsCovered.size();
            Double totalCoverageWithoutBaseModelRecognition = (double) positionsCovered.size()
                     / (totalTokens - finalPositionsForRecsFromBaseModel.size());
            Double totalCoverageWithBaseModelRecognition = (double) positionsCovered.size() / totalTokens;
            if (logger.isInfoEnabled()) {
               logger.info("\nModel Id: " + modelId + "\tAverage Quality: " + averageQuality + "\tCoverage: "
                        + totalCoverageWithoutBaseModelRecognition + "\tFinal Coverage: " + averageQuality
                        * totalCoverageWithoutBaseModelRecognition);
               logger.info("\nModel Id: " + modelId + "\tAverage Quality: " + averageQuality + "\tCoverage: "
                        + totalCoverageWithBaseModelRecognition + "\tFinal Coverage: " + averageQuality
                        * totalCoverageWithBaseModelRecognition);

            }
         }
      }
   }

   private void validateMaxTokens (List<String> queryWords, String userQuery) throws NLPException {
      // Validate the number of tokens
      if (queryWords.size() > getNlpConfigurationService().getMaxToken()) {
         throw new NLPException(NLPExceptionCodes.NLP_MAX_TOKENS_LIMIT_EXCEPTION_CODE,
                  "User tokens count has exceeded maximum limit for query: " + userQuery);
      }
   }

   /**
    * Run the OntoRecognitionProcessor and set the output to the recognitionEntities in the possibility
    * 
    * @param rootMatrix
    * @param searchFilter
    */
   private void runOntoRecognition (RootMatrix rootMatrix, SearchFilter searchFilter) {
      long start = System.currentTimeMillis();
      for (Possibility possibility : rootMatrix.getPossibilities()) {

         // Prepare the processor input
         ProcessorInput processorInput = new ProcessorInput();
         processorInput.setSearchFilter(searchFilter);
         processorInput.setSkipLocationTypeRecognition(rootMatrix.isSkipLocationTypeRecognition());
         processorInput.setRecognitionEntities(possibility.getRecognitionEntities());

         // Process the onto recognition
         getOntoRecognitionProcessor().process(processorInput);

         // Set the output recognition entities to the possibility
         possibility.setRecognitionEntities(processorInput.getOutputRecognitionEntities());
         possibility.setUnrecognizedEntities(processorInput.getUnrecognizedEntities());
      }
      long end = System.currentTimeMillis();
      if (logger.isInfoEnabled()) {
         logger.info("Finished Onto Recognition. Time Taken # " + (end - start));
      }
   }

   /**
    * @param nlpTokens
    * @return List<String>
    * @throws NLPException
    */
   private List<String> prepareQueryWords (List<NLPToken> nlpTokens) throws NLPException {
      List<String> queryWords = new ArrayList<String>();
      for (NLPToken tok : nlpTokens) {
         queryWords.add(tok.getWord());
      }

      return queryWords;
   }

   /**
    * @param userQuerySentence
    * @return the userQuerySentence
    */
   private String performPreProcessing (String userQuerySentence) {
      for (IPreProcessor preProcessor : preProcessors) {
         userQuerySentence = preProcessor.preProcess(userQuerySentence);
      }
      return userQuerySentence;
   }

   /**
    * Method to create the root matrix
    * 
    * @param nlpTokens
    * @param userQuery
    * @param contextName
    * @return the RootMatrix
    * @throws NLPException
    */
   private RootMatrix createRootMatrix (List<NLPToken> nlpTokens, UserQuery userQuery, String contextName)
            throws NLPException {
      RootMatrix rootMatrix = null;
      try {
         rootMatrix = getRMManager().createRootMatrix(nlpTokens);
      } catch (CloneNotSupportedException e) {
         logger.error(e.getMessage(), e);
         throw new NLPException(NLPExceptionCodes.CLONE_FAILED, e);
      }

      // Set the search filter
      rootMatrix.setSearchFilter(userQuery.getSearchFilter());
      rootMatrix.setFromArticle(userQuery.isFromArticle());
      rootMatrix.setSkipLocationTypeRecognition(userQuery.isSkipLocationTypeRecognition());

      // Set the possibilities
      List<Possibility> possibilities = getPossibilitiesForRootMatrix(nlpTokens);
      rootMatrix.setPossibilities(possibilities);

      // Set the user query token count
      rootMatrix.setUserQueryTokensCount(nlpTokens.size());

      // set the processor context for the root matrix the processing of the query will be based
      // on the context set to the root matrix. If context is null that means all the processors
      // should take part in processing.
      if (contextName != null) {
         rootMatrix.setProcessorContext(contextName);
      }

      // Get the default base weight information and set the weight as per the nlp tokens size
      WeightInformation baseWeightInformation = NLPUtilities.getDefaultWeightInformation();
      baseWeightInformation.setRecognitionWeight(NLPConstants.MAX_WEIGHT * nlpTokens.size());
      rootMatrix.setBaseWeightInformation(baseWeightInformation);

      // Also update the base weight information in the user query
      userQuery.setBaseWeightInformation(baseWeightInformation);

      // Set the configuration related parameters like max-allowed-semantic-scoping-time, etc in the Root Matrix
      rootMatrix.setTimeBasedCutoffEnabled(getNlpConfigurationService().isNlpTimeBasedCutOffEnabled());
      rootMatrix.setMaxAllowedTimeForSemanticScoping(getNlpConfigurationService().getMaxAllowledSemanticScopingTime());
      rootMatrix.setMaxAllowedTimeForFindingSemantics(getNlpConfigurationService()
               .getMaxAllowledTimeForFindingSemantics());
      rootMatrix.setMaxAllowedTimeForEnhancingSemantics(getNlpConfigurationService()
               .getMaxAllowledTimeForEnhancingSemantics());
      return rootMatrix;
   }

   /**
    * Method to sort and assign the weight to the possibility
    * 
    * @param possibilities
    */
   private void sortAndAssignWeight (List<Possibility> possibilities) {
      if (logger.isDebugEnabled()) {
         logger.debug("Assigning the weights to possiblities ");
      }
      List<Double> possibilityWeights = new ArrayList<Double>();
      for (Possibility possibility : possibilities) {
         NLPUtilities.sortRecognitionEntitiesByPosition(possibility.getRecognitionEntities());
         double possibilityWeight = possibility.getWeightInformationForExplicitEntities().getFinalWeight();
         possibilityWeights.add(possibilityWeight);
         possibility.setPossibilityWeight(possibilityWeight);

      }
      List<Double> standardizedPossibilityWeights = MathUtil.getStandardizedValues(possibilityWeights);
      int count = 0;
      for (Possibility possibility : possibilities) {
         possibility.setStandarizedPossiblityWeight(standardizedPossibilityWeights.get(count++));
      }
   }

   /**
    * @return the nlpConfigurationService
    */
   public INLPConfigurationService getNlpConfigurationService () {
      return nlpConfigurationService;
   }

   /**
    * @param nlpConfigurationService
    *           the nlpConfigurationService to set
    */
   public void setNlpConfigurationService (INLPConfigurationService nlpConfigurationService) {
      this.nlpConfigurationService = nlpConfigurationService;
   }

   /**
    * @return the rMManager
    */
   public RMManager getRMManager () {
      return RMManager;
   }

   /**
    * @param manager
    *           the rMManager to set
    */
   public void setRMManager (RMManager manager) {
      RMManager = manager;
   }

   /**
    * @return the reducedFormGenerator
    */
   public ReducedFormGenerator getReducedFormGenerator () {
      return reducedFormGenerator;
   }

   /**
    * @param reducedFormGenerator
    *           the reducedFormGenerator to set
    */
   public void setReducedFormGenerator (ReducedFormGenerator reducedFormGenerator) {
      this.reducedFormGenerator = reducedFormGenerator;
   }

   /**
    * @return the sfTermHitsUpdateHelper
    */
   public SFLTermHitsUpdateHelper getSfTermHitsUpdateHelper () {
      return sfTermHitsUpdateHelper;
   }

   /**
    * @param sfTermHitsUpdateHelper
    *           the sfTermHitsUpdateHelper to set
    */
   public void setSfTermHitsUpdateHelper (SFLTermHitsUpdateHelper sfTermHitsUpdateHelper) {
      this.sfTermHitsUpdateHelper = sfTermHitsUpdateHelper;
   }

   /**
    * @return the semanticScopingHandler
    */
   public SemanticScopingHandler getSemanticScopingHandler () {
      return semanticScopingHandler;
   }

   /**
    * @param semanticScopingHandler
    *           the semanticScopingHandler to set
    */
   public void setSemanticScopingHandler (SemanticScopingHandler semanticScopingHandler) {
      this.semanticScopingHandler = semanticScopingHandler;
   }

   /**
    * @return the nlpWorkflowManager
    */
   public NLPWorkflowManager getNlpWorkflowManager () {
      return nlpWorkflowManager;
   }

   /**
    * @param nlpWorkflowManager
    *           the nlpWorkflowManager to set
    */
   public void setNlpWorkflowManager (NLPWorkflowManager nlpWorkflowManager) {
      this.nlpWorkflowManager = nlpWorkflowManager;
   }

   /**
    * @return the cumulativeDecisionMaker
    */
   public CumulativeDecisionMaker getCumulativeDecisionMaker () {
      return cumulativeDecisionMaker;
   }

   /**
    * @param cumulativeDecisionMaker
    *           the cumulativeDecisionMaker to set
    */
   public void setCumulativeDecisionMaker (CumulativeDecisionMaker cumulativeDecisionMaker) {
      this.cumulativeDecisionMaker = cumulativeDecisionMaker;
   }

   /**
    * @return the ontoRecognitionProcessor
    */
   public SemanticScopingOntoRecognitionProcessor getOntoRecognitionProcessor () {
      return ontoRecognitionProcessor;
   }

   /**
    * @param ontoRecognitionProcessor
    *           the ontoRecognitionProcessor to set
    */
   public void setOntoRecognitionProcessor (SemanticScopingOntoRecognitionProcessor ontoRecognitionProcessor) {
      this.ontoRecognitionProcessor = ontoRecognitionProcessor;
   }

   /**
    * @return the nlpServiceHelper
    */
   public NLPServiceHelper getNlpServiceHelper () {
      return nlpServiceHelper;
   }

   /**
    * @param nlpServiceHelper
    *           the nlpServiceHelper to set
    */
   public void setNlpServiceHelper (NLPServiceHelper nlpServiceHelper) {
      this.nlpServiceHelper = nlpServiceHelper;
   }

   /**
    * @return the preProcessors
    */
   public List<IPreProcessor> getPreProcessors () {
      return preProcessors;
   }

   /**
    * @param preProcessors
    *           the preProcessors to set
    */
   public void setPreProcessors (List<IPreProcessor> preProcessors) {
      this.preProcessors = preProcessors;
   }

   public IEASIndexService getEasIndexService () {
      return easIndexService;
   }

   public void setEasIndexService (IEASIndexService easIndexService) {
      this.easIndexService = easIndexService;
   }

   /**
    * @return the summarizationService
    */
   public ISummarizationService getSummarizationService () {
      return summarizationService;
   }

   /**
    * @param summarizationService
    *           the summarizationService to set
    */
   public void setSummarizationService (ISummarizationService summarizationService) {
      this.summarizationService = summarizationService;
   }

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }
}