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
package com.execue.semantification.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.content.postprocessor.bean.PostProcessorInput;
import com.execue.content.postprocessor.bean.PostProcessorOutput;
import com.execue.content.postprocessor.exception.ContentPostProcessorException;
import com.execue.content.postprocessor.service.IContentPostProcessingService;
import com.execue.content.preprocessor.bean.PreProcessorInput;
import com.execue.content.preprocessor.exception.ContentPreProcessorException;
import com.execue.content.preprocessor.service.IContentPreProcessingService;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.entity.unstructured.SemantifiedContent;
import com.execue.core.common.bean.entity.unstructured.SemantifiedContentFeatureInformation;
import com.execue.core.common.bean.entity.unstructured.SemantifiedContentKeyword;
import com.execue.core.common.bean.graph.GraphPath;
import com.execue.core.common.bean.graph.IGraphPath;
import com.execue.core.common.bean.kb.POSContext;
import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.NLPInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.nlp.UserQuery;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.SearchFilterType;
import com.execue.core.common.type.SearchType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.nlp.engine.INLPEngine;
import com.execue.nlp.exception.NLPException;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.platform.configuration.IPlatformServicesConfigurationService;
import com.execue.platform.unstructured.IUnstructuredFacetManagementService;
import com.execue.qdata.exception.RFXExceptionCodes;
import com.execue.semantification.bean.SemantificationInput;
import com.execue.semantification.configuration.ISemantificationConfiguration;
import com.execue.semantification.exception.SemantificationException;
import com.execue.semantification.unstructured.service.IGenericArticlePopulationService;
import com.execue.sus.helper.NewsUtil;
import com.execue.sus.helper.ReducedFormHelper;
import com.execue.sus.helper.SemantificationHelper;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IPathDefinitionRetrievalService;
import com.execue.uswh.configuration.IUnstructuredWHConfigurationService;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.helper.UnstructuredWarehouseHelper;
import com.execue.uswh.service.IUnstructuredWHFeatureContentLookupService;
import com.execue.uswh.service.IUnstructuredWarehouseManagementService;

/**
 * @author Nitesh
 */
public abstract class GenericArticlePopulationService implements IGenericArticlePopulationService {

   private static Logger                              logger = Logger.getLogger(GenericArticlePopulationService.class);
   private ISemantificationConfiguration              semantificationConfiguration;
   private INLPEngine                                 nlpEngine;
   private IPathDefinitionRetrievalService            pathDefinitionRetrievalService;
   private ISWIConfigurationService                   swiConfigurationService;
   private IUnstructuredWarehouseManagementService    unstructuredWarehouseManagementService;
   private SemantificationHelper                      semantificationHelper;
   private ReducedFormHelper                          reducedFormHelper;
   private IUnstructuredWHFeatureContentLookupService unstructuredWHFeatureContentLookupService;
   private IUnstructuredFacetManagementService        unstructuredFacetManagementService;
   private IContentPostProcessingService              contentPostProcessingService;
   private IContentPreProcessingService               contentPreProcessingService;
   private IPlatformServicesConfigurationService      platformServicesConfigurationService;
   private UnstructuredWarehouseHelper                unstructuredWarehouseHelper;
   private IUnstructuredWHConfigurationService        unstructuredWHConfigurationService;

   public void semantifiContent (Long contextId, SemantificationInput semantificationInput)
            throws SemantificationException {

      try {
         if (logger.isDebugEnabled()) {
            logger.debug("Processing Article with Id : " + semantificationInput.getId());
         }
         List<SemanticPossibility> semanticPossibilities = new ArrayList<SemanticPossibility>();
         List<String> validLines = new ArrayList<String>(1);
         Long applicationId = semantificationInput.getContextId();
         try {
            // STEP 1: Start the semantification and get the possibilities

            // Prepare the search filter which is common
            SearchFilter searchFilter = new SearchFilter();
            searchFilter.setOriginalSearchFilterType(SearchFilterType.APP);
            searchFilter.setSearchFilterType(SearchFilterType.APP);
            searchFilter.setId(applicationId);

            // A. Semantify the article source, If the source is in between '~~' then it is having location
            int sentenceId = 1;
            String newsItemSource = semantificationInput.getSource();
            boolean skipLocationTypeRecognition = false;
            if (ExecueCoreUtil.isNotEmpty(newsItemSource)) {
               String articleLocation = newsItemSource.trim();
               UserQuery userQuery = prepareUserQuery(searchFilter, articleLocation, sentenceId,
                        skipLocationTypeRecognition);
               Set<SemanticPossibility> possibilities = semantifyContentLine(userQuery);
               if (!CollectionUtils.isEmpty(possibilities)) {
                  semanticPossibilities.addAll(possibilities);
                  validLines.add(articleLocation);
                  sentenceId++;
               }
            }

            // B. Semantify the article title
            // prepare preProcessor Input
            String articleTitle = null;
            PreProcessorInput preProcessorInput = new PreProcessorInput();
            preProcessorInput.setApplicationId(applicationId);
            preProcessorInput.setInput(semantificationInput.getTitle());
            preProcessorInput.setTitle(true);
            List<String> preProcessorOutputList = getContentPreProcessingService().preProcess(preProcessorInput);
            if (ExecueCoreUtil.isCollectionNotEmpty(preProcessorOutputList)) {
               articleTitle = preProcessorOutputList.get(0);
            }
            if (ExecueCoreUtil.isNotEmpty(articleTitle)) {
               UserQuery userQuery = prepareUserQuery(searchFilter, articleTitle, sentenceId,
                        skipLocationTypeRecognition);
               Set<SemanticPossibility> possibilities = semantifyContentLine(userQuery);
               if (!CollectionUtils.isEmpty(possibilities)) {
                  semanticPossibilities.addAll(possibilities);
                  validLines.add(articleTitle);
                  sentenceId++;
               }
            }

            // C. Semantify the article summary
            // List<String> summaryLines = getLinesFromSummary(semantificationInput.getSummary());
            preProcessorInput.setInput(semantificationInput.getSummary());
            preProcessorInput.setTitle(false);
            List<String> summaryLines = getContentPreProcessingService().preProcess(preProcessorInput);
            // SKIP the location type recognition in the description sentences
            skipLocationTypeRecognition = true;
            for (String summaryLine : summaryLines) {
               UserQuery userQuery = prepareUserQuery(searchFilter, summaryLine, sentenceId,
                        skipLocationTypeRecognition);
               Set<SemanticPossibility> possibilities = semantifyContentLine(userQuery);
               if (CollectionUtils.isEmpty(possibilities)) {
                  continue;
               }
               semanticPossibilities.addAll(possibilities);
               validLines.add(articleTitle);
               sentenceId++;
            }
         } catch (NLPException e) {
            throw new SemantificationException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
         } catch (SWIException e) {
            throw new SemantificationException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
         } catch (ContentPreProcessorException e) {
            // TODO: -JM- feed proper exception code and message
            throw new SemantificationException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
         }

         // STEP 2: Perform the post processing on the semantic possibilities including location validation
         List<SemantifiedContentFeatureInformation> semantifiedContentFeatureInformation = new ArrayList<SemantifiedContentFeatureInformation>();
         SemanticPossibility semanticPossibility = null;
         LocationPointInfo validLocationPointInfo = null;
         if (!CollectionUtils.isEmpty(semanticPossibilities)) {
            PostProcessorInput postProcessorInput = new PostProcessorInput();
            postProcessorInput.setApplicationId(applicationId);
            postProcessorInput.setSemanticPossibilities(semanticPossibilities);
            postProcessorInput.setResemantification(semantificationInput.isResemantification());

            PostProcessorOutput postProcessorOutput = getContentPostProcessingService().postProcess(postProcessorInput);
            semanticPossibility = postProcessorOutput.getSemanticPossibility();
            validLocationPointInfo = postProcessorOutput.getLocationPointInfo();
         }

         // STEP 3: Populate the semantified content 
         SemantifiedContent semantifiedContent = null;
         if (semantificationInput.isResemantification()) {
            semantifiedContent = semantificationInput.getExistingSemantifiedContent();
            long semantifiedContentId = semantifiedContent.getId();
            getUnstructuredWarehouseManagementService().deleteExistingSemantifiedContentFeatureInfo(contextId,
                     semantifiedContentId);
            getUnstructuredWarehouseManagementService().deleteExistingSemantifiedContentKeywordInfo(contextId,
                     semantifiedContentId);
         } else {
            // Populate the semantified content entry based on the category implementations like
            // classifieds, etc
            semantifiedContent = populateSemantifiedContent(contextId, semantificationInput, validLocationPointInfo);
         }

         // STEP 4: Populate the semantified content key word table 
         populateSemantifiedContentKeyWord(contextId, semanticPossibility, validLines, semantifiedContent);

         // STEP 5: Populate the semantified content feature information
         if (semantifiedContent == null) {
            semantifiedContentFeatureInformation = populateDummySemantifiedContentFeatureInformation(contextId,
                     semantifiedContent);
         } else {

            // Populate the semantified content feature information tables based on the category implementations like
            // classifieds, etc
            semantifiedContentFeatureInformation = populateSemantifiedContentFeatureInformation(contextId,
                     semanticPossibility, semantifiedContent);

         }

         // STEP 6 : Populate the facets information
         int status = populateFacetInformation(contextId, semantifiedContentFeatureInformation);
         logger.debug("Status for Facet Information " + status);

      } catch (UnstructuredWarehouseException e) {
         throw new SemantificationException(e.getCode(), e.getMessage());
      } catch (ContentPostProcessorException e) {
         throw new SemantificationException(e.getCode(), e.getMessage());
      }
   }

   public abstract int populateFacetInformation (Long contextId,
            List<SemantifiedContentFeatureInformation> semantificationInformation) throws SemantificationException;

   private UserQuery prepareUserQuery (SearchFilter searchFilter, String originalQuery, int sentenceId,
            boolean skipLocationTypeRecognition) {
      UserQuery userQuery = new UserQuery();
      userQuery.setSearchType(SearchType.ENTITY_SEARCH);
      userQuery.setBackendSearch(true); // set the backend search flag to true
      userQuery.setSearchFilter(searchFilter);
      userQuery.setFromArticle(true);
      userQuery.setSentenceId(sentenceId);
      userQuery.setOriginalQuery(originalQuery);
      userQuery.setSkipLocationTypeRecognition(skipLocationTypeRecognition);
      return userQuery;
   }

   private Set<SemanticPossibility> semantifyContentLine (UserQuery userQuery) throws NLPException, SWIException {
      Set<SemanticPossibility> semanticPossibilities = new HashSet<SemanticPossibility>();
      String contentLine = userQuery.getOriginalQuery();
      if (StringUtils.isEmpty(contentLine)) {
         return semanticPossibilities;
      }

      NLPInformation nlpInformation = null;
      if (logger.isDebugEnabled()) {
         logger.debug("Content Line: " + contentLine);
      }
      try {
         nlpInformation = getNlpEngine().processQuery(userQuery, null);
      } catch (NLPSystemException e) {
         e.printStackTrace();
         return semanticPossibilities;
      }
      Map<SemanticPossibility, Integer> reducedForms = nlpInformation.getReducedForms();
      Set<SemanticPossibility> possibilities = reducedForms.keySet();
      if (CollectionUtils.isEmpty(possibilities)) {
         return semanticPossibilities;
      }
      for (SemanticPossibility possibility : possibilities) {
         List<IGraphPath> paths = possibility.getReducedForm().getPaths();
         if (CollectionUtils.isEmpty(paths)) {
            continue;
         }
         List<IGraphPath> removePaths = new ArrayList<IGraphPath>();
         for (IGraphPath path : paths) {
            // Validate the Path Before Adding only if the path has the instance triples
            if (((GraphPath) path).isValidateInstanceTriple() && path.getPathLength() == 3) {
               // Instance Paths Should not be longer than 3 components i.e. Source-Relation-Destination
               DomainRecognition source = (DomainRecognition) path.getStartVertex();
               DomainRecognition dest = (DomainRecognition) path.getEndVertex();
               DomainRecognition rel = (DomainRecognition) path.getPathComponent(1);
               boolean instanceSrc = source.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_INSTANCE);
               boolean instanceDest = dest.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_INSTANCE);
               boolean relation = rel.getNlpTag().equals(NLPConstants.NLP_TAG_ONTO_PROPERTY);
               boolean validInstanceTriple = false;
               if (instanceSrc && instanceDest && relation) {
                  validInstanceTriple = getPathDefinitionRetrievalService().checkIfInstancePathExists(
                           source.getEntityBeId(), rel.getEntityBeId(), dest.getEntityBeId());
                  if (!validInstanceTriple) {
                     removePaths.add(path);
                  }
               }
            }
         }
         for (IGraphPath path : removePaths) {
            possibility.getReducedForm().removePath(path);
         }
         paths.removeAll(removePaths);
      }
      return possibilities;
   }

   /**
    * @param contextId
    * @param semanticPossibilities
    * @param contentLinesToSemantify
    * @param semantifiedContent
    * @throws UnstructuredWarehouseException
    */
   // TODO: NK: Should handle the empty possibility case separately
   protected void populateSemantifiedContentKeyWord (Long contextId, SemanticPossibility semanticPossibility,
            List<String> contentLinesToSemantify, SemantifiedContent semantifiedContent)
            throws UnstructuredWarehouseException {

      // Get the key word text
      StringBuilder reducedFormString = new StringBuilder(1);
      reducedFormString
               .append(getReducedFormHelper().getKeyWordMatchText(semanticPossibility, getConceptPriorityMap()));

      String articleKeyWords = getOptimizedArticleText(semanticPossibility, reducedFormString, contentLinesToSemantify);

      // Merge the reduced form text and article key word text
      String keywordText = reducedFormString.toString().toLowerCase() + " " + articleKeyWords;
      keywordText = ExecueStringUtil.removeDuplicates(keywordText, true);

      SemantifiedContentKeyword semantifiedContentKeyWord = new SemantifiedContentKeyword();
      semantifiedContentKeyWord.setContextId(contextId);
      semantifiedContentKeyWord.setSemantifiedContentId(semantifiedContent.getId());
      semantifiedContentKeyWord.setKeywordText(keywordText);
      semantifiedContentKeyWord.setContentDate(semantifiedContent.getContentDate());

      getUnstructuredWarehouseManagementService().createSemantifiedContentKeyword(contextId, semantifiedContentKeyWord);
   }

   protected Map<Long, Integer> getConceptPriorityMap () {
      return new HashMap<Long, Integer>(1);
   }

   private String getOptimizedArticleText (SemanticPossibility semanticPossibility, StringBuilder reducedFormString,
            List<String> newsItemLinesToSemantify) {

      if (CollectionUtils.isEmpty(newsItemLinesToSemantify)) {
         return reducedFormString.toString();
      }
      // Prepare the invalid tokens set
      Set<String> invalidKeyWords = new HashSet<String>(1);
      // Add the removed vertices to the list of invalid tokens
      List<SemanticPossibility> semanticPossibilities = new ArrayList<SemanticPossibility>();
      semanticPossibilities.add(semanticPossibility);
      invalidKeyWords.addAll(ReducedFormHelper.getRemovedVertices(semanticPossibilities));
      // Add the conjunctions and adjectives to the list of invalid tokens
      invalidKeyWords.addAll(getPosContext().getConjAndByConjTermNames());

      // Remove the invalid tokens from the article text
      String optimizedArticleText = ExecueCoreUtil.convertAsString(newsItemLinesToSemantify).toLowerCase();
      for (String invalidKeyWord : invalidKeyWords) {
         optimizedArticleText = optimizedArticleText.replaceAll("^|,|\\.|\\s+" + invalidKeyWord.toLowerCase().trim()
                  + "\\s+|,|\\.|$", " ");
      }

      // Remove the numbers from the article text
      optimizedArticleText = optimizedArticleText.replaceAll("(^|,|\\.|\\s+)((\\d*)?(\\.)?(\\d+)(%)?)(\\s+|,|\\.|$)",
               " ");
      return optimizedArticleText.trim();
   }

   /**
    * @param articleSummary
    * @return the List<String>
    */
   private List<String> getLinesFromSummary (String articleSummary) {
      List<String> newsItemLinesToSemantify = new ArrayList<String>(1);

      int tokenLimit = getSemantificationConfiguration().getSemantificationArticleSentenceTokenLimit();
      // Add the news item summary to the news items lines list
      String newsItemSummary = NewsUtil.cleanupNewsData(articleSummary);
      if (ExecueCoreUtil.isNotEmpty(newsItemSummary)) {
         newsItemLinesToSemantify.addAll(NewsUtil.getLinesFromSummary(newsItemSummary, tokenLimit));
      }
      return newsItemLinesToSemantify;
   }

   public List<SemantifiedContentFeatureInformation> populateDummySemantifiedContentFeatureInformation (Long contextId,
            SemantifiedContent semantifiedContent) throws UnstructuredWarehouseException {
      List<SemantifiedContentFeatureInformation> structuredContentList = new ArrayList<SemantifiedContentFeatureInformation>(
               1);
      SemantifiedContentFeatureInformation dummyContent = new SemantifiedContentFeatureInformation();
      dummyContent.setContextId(semantifiedContent.getContextId());
      dummyContent.setSemantifiedContentId(semantifiedContent.getId());
      dummyContent.setSemantifiedContentDate(semantifiedContent.getContentDate());
      dummyContent.setFeatureId(-1L);
      dummyContent.setFeatureValueType(FeatureValueType.VALUE_DUMMY);
      dummyContent.setFeatureWeight(10d);
      structuredContentList.add(dummyContent);
      getUnstructuredWarehouseManagementService().saveSemantifiedContentFeatureInformation(contextId,
               structuredContentList);
      return structuredContentList;
   }

   public abstract SemantifiedContent populateSemantifiedContent (Long contextId,
            SemantificationInput semantificationInput, LocationPointInfo locationPointInfo)
            throws SemantificationException;

   public abstract List<SemantifiedContentFeatureInformation> populateSemantifiedContentFeatureInformation (
            Long contextId, SemanticPossibility semanticPossibility, SemantifiedContent semantifiedContent)
            throws SemantificationException;

   public abstract List<String> getValidImageUrls (SemantifiedContent semantifiedContent)
            throws SemantificationException;

   public POSContext getPosContext () {
      return getSwiConfigurationService().getPosContext();
   }

   /**
    * @return the nlpEngine
    */
   public INLPEngine getNlpEngine () {
      return nlpEngine;
   }

   /**
    * @param nlpEngine
    *           the nlpEngine to set
    */
   public void setNlpEngine (INLPEngine nlpEngine) {
      this.nlpEngine = nlpEngine;
   }

   /**
    * @return the swiConfigurationService
    */
   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   /**
    * @param swiConfigurationService
    *           the swiConfigurationService to set
    */
   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   /**
    * @return the semantificationConfiguration
    */
   public ISemantificationConfiguration getSemantificationConfiguration () {
      return semantificationConfiguration;
   }

   /**
    * @param semantificationConfiguration
    *           the semantificationConfiguration to set
    */
   public void setSemantificationConfiguration (ISemantificationConfiguration semantificationConfiguration) {
      this.semantificationConfiguration = semantificationConfiguration;
   }

   public IPathDefinitionRetrievalService getPathDefinitionRetrievalService () {
      return pathDefinitionRetrievalService;
   }

   public void setPathDefinitionRetrievalService (IPathDefinitionRetrievalService pathDefinitionRetrievalService) {
      this.pathDefinitionRetrievalService = pathDefinitionRetrievalService;
   }

   /**
    * @return the semantificationHelper
    */
   public SemantificationHelper getSemantificationHelper () {
      return semantificationHelper;
   }

   /**
    * @param semantificationHelper
    *           the semantificationHelper to set
    */
   public void setSemantificationHelper (SemantificationHelper semantificationHelper) {
      this.semantificationHelper = semantificationHelper;
   }

   /**
    * @return the reducedFormHelper
    */
   public ReducedFormHelper getReducedFormHelper () {
      return reducedFormHelper;
   }

   /**
    * @param reducedFormHelper
    *           the reducedFormHelper to set
    */
   public void setReducedFormHelper (ReducedFormHelper reducedFormHelper) {
      this.reducedFormHelper = reducedFormHelper;
   }

   /**
    * @return the contentPostProcessingService
    */
   public IContentPostProcessingService getContentPostProcessingService () {
      return contentPostProcessingService;
   }

   /**
    * @param contentPostProcessingService
    *           the contentPostProcessingService to set
    */
   public void setContentPostProcessingService (IContentPostProcessingService contentPostProcessingService) {
      this.contentPostProcessingService = contentPostProcessingService;
   }

   public IUnstructuredWHFeatureContentLookupService getUnstructuredWHFeatureContentLookupService () {
      return unstructuredWHFeatureContentLookupService;
   }

   public void setUnstructuredWHFeatureContentLookupService (
            IUnstructuredWHFeatureContentLookupService unstructuredWHFeatureContentLookupService) {
      this.unstructuredWHFeatureContentLookupService = unstructuredWHFeatureContentLookupService;
   }

   public IUnstructuredFacetManagementService getUnstructuredFacetManagementService () {
      return unstructuredFacetManagementService;
   }

   public void setUnstructuredFacetManagementService (
            IUnstructuredFacetManagementService unstructuredFacetManagementService) {
      this.unstructuredFacetManagementService = unstructuredFacetManagementService;
   }

   public IContentPreProcessingService getContentPreProcessingService () {
      return contentPreProcessingService;
   }

   public void setContentPreProcessingService (IContentPreProcessingService contentPreProcessingService) {
      this.contentPreProcessingService = contentPreProcessingService;
   }

   /**
    * @return the unstructuredWarehouseManagementService
    */
   public IUnstructuredWarehouseManagementService getUnstructuredWarehouseManagementService () {
      return unstructuredWarehouseManagementService;
   }

   /**
    * @param unstructuredWarehouseManagementService the unstructuredWarehouseManagementService to set
    */
   public void setUnstructuredWarehouseManagementService (
            IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService) {
      this.unstructuredWarehouseManagementService = unstructuredWarehouseManagementService;
   }

   /**
    * @return the platformServicesConfigurationService
    */
   public IPlatformServicesConfigurationService getPlatformServicesConfigurationService () {
      return platformServicesConfigurationService;
   }

   /**
    * @param platformServicesConfigurationService the platformServicesConfigurationService to set
    */
   public void setPlatformServicesConfigurationService (
            IPlatformServicesConfigurationService platformServicesConfigurationService) {
      this.platformServicesConfigurationService = platformServicesConfigurationService;
   }

   public UnstructuredWarehouseHelper getUnstructuredWarehouseHelper () {
      return unstructuredWarehouseHelper;
   }

   public void setUnstructuredWarehouseHelper (UnstructuredWarehouseHelper unstructuredWarehouseHelper) {
      this.unstructuredWarehouseHelper = unstructuredWarehouseHelper;
   }

   public IUnstructuredWHConfigurationService getUnstructuredWHConfigurationService () {
      return unstructuredWHConfigurationService;
   }

   public void setUnstructuredWHConfigurationService (
            IUnstructuredWHConfigurationService unstructuredWHConfigurationService) {
      this.unstructuredWHConfigurationService = unstructuredWHConfigurationService;
   }
}