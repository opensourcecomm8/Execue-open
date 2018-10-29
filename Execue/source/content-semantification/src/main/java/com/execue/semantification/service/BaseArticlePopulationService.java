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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.graph.Graph;
import com.execue.core.common.bean.graph.GraphPath;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.graph.IGraphPath;
import com.execue.core.common.bean.kb.POSContext;
import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.NLPInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.nlp.UserQuery;
import com.execue.core.common.bean.qdata.AppNewsPopularity;
import com.execue.core.common.bean.qdata.NewsItem;
import com.execue.core.common.bean.qdata.RFXValue;
import com.execue.core.common.bean.qdata.RIUniversalSearch;
import com.execue.core.common.bean.qdata.ReducedFormIndex;
import com.execue.core.common.bean.qdata.UDXKeyword;
import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.type.NewsCategory;
import com.execue.core.common.type.RFXVariationSubType;
import com.execue.core.common.type.SearchFilterType;
import com.execue.core.common.type.SearchType;
import com.execue.core.common.type.UniversalSearchType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.UidException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.nlp.engine.INLPEngine;
import com.execue.nlp.exception.NLPException;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.platform.IUidService;
import com.execue.qdata.exception.RFXException;
import com.execue.qdata.exception.RFXExceptionCodes;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.service.IRFXService;
import com.execue.qdata.service.IUDXService;
import com.execue.semantification.configuration.ISemantificationConfiguration;
import com.execue.semantification.exception.InvalidNewsItemException;
import com.execue.semantification.exception.SemantificationException;
import com.execue.semantification.unstructured.service.IArticlePopulationService;
import com.execue.sus.helper.NewsUtil;
import com.execue.sus.helper.RFXServiceHelper;
import com.execue.sus.helper.ReducedFormHelper;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathDefinitionRetrievalService;

/**
 * @author Nihar
 */
public abstract class BaseArticlePopulationService implements IArticlePopulationService {

   private static Logger                   logger = Logger.getLogger(BaseArticlePopulationService.class);
   private INLPEngine                      nlpEngine;
   private IRFXService                     rfxService;
   private IUDXService                     udxService;
   private RFXServiceHelper                rfxServiceHelper;
   private ReducedFormHelper               reducedFormHelper;
   private IUidService                     rfIdGenerationService;
   private IKDXRetrievalService            kdxRetrievalService;
   private IPathDefinitionRetrievalService pathDefinitionRetrievalService;
   private ISWIConfigurationService        swiConfigurationService;
   private ISemantificationConfiguration   semantificationConfiguration;
   private IApplicationRetrievalService    applicationRetrievalService;

   public List<String> getValidImageUrls (UnStructuredIndex unStructuredIndex) throws SemantificationException {
      return new ArrayList<String>(1);
   }

   public void semantifiNewsArticle (NewsItem newsItem) throws SemantificationException {
      try {
         Long verticalId = getSemantificationConfiguration().getVerticalCategoryMap().get(newsItem.getCategory());
         Long appId = getSemantificationConfiguration().getApplicationCategoryMap().get(newsItem.getCategory());
         if (verticalId == null && appId == null) {
            return;
         }
         if (logger.isDebugEnabled()) {
            logger.debug("Processing Article with Id : " + newsItem.getId());
         }
         List<String> newsItemLinesToSemantify = getLinesForNewsItem(newsItem);

         newsItemLinesToSemantify = updateCategorySpecificPatterns(newsItemLinesToSemantify);

         List<SemanticPossibility> semanticPossibilities = semantifiNewsItem(newsItem, newsItemLinesToSemantify);
         if (CollectionUtils.isEmpty(semanticPossibilities)) {
            populateDummyUdxAndKeyWordInformation(newsItem, newsItemLinesToSemantify, semanticPossibilities);
            return;
         }
         // Assign application id to the SemanticPossibility
         assignAppId(semanticPossibilities);

         semanticPossibilities = updatePossibilities(semanticPossibilities);

         try {
            populateMissingDependentInstance(semanticPossibilities, newsItem);
            invalidateNewsItem(semanticPossibilities, newsItem);
         } catch (InvalidNewsItemException e) {
            // Now in case of Invalid news item we are also populating the udx and key word information
            // with dummy information whereever applicable before throwing back the error.
            populateDummyUdxAndKeyWordInformation(newsItem, newsItemLinesToSemantify, semanticPossibilities);
            throw e;
         }

         List<ReducedFormIndex> rfxList = new ArrayList<ReducedFormIndex>();
         List<RFXValue> rfxValueList = new ArrayList<RFXValue>();

         // Generate RFX and RFXValue
         Long reducedFormId = populateRfxAndRfxValue(newsItem, semanticPossibilities, rfxList, rfxValueList);

         // Generate UDX
         Double entityCount = getEntityCount(semanticPossibilities);
         Double maxMatchWeight = RFXServiceHelper.getMaxMatchWeight(rfxList, rfxValueList);
         UnStructuredIndex udx = RFXServiceHelper.populateUDXForNewsItem(newsItem, reducedFormId, entityCount,
                  maxMatchWeight);
         udx = getUdxService().storeUDX(udx);

         // Generate RI UDX
         populateRIUdx(rfxList, entityCount, udx);

         // Populate the udx key word table
         if (checkIfPopulateUdxKeyWord()) {
            populateUdxKeyWord(semanticPossibilities, newsItemLinesToSemantify, udx);
         }

         // Populate the article category specific tables if any
         populateCategorySpecificTables(semanticPossibilities, newsItemLinesToSemantify, udx);

         try {
            // clear old List of Possibilities and add new Possibility
            updatePopularityHits(semanticPossibilities, udx);
         } catch (SWIException e) {
            logger.error("Problem in storing Popularity Hits.");
         }
      } catch (UidException e) {
         throw new SemantificationException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
      } catch (UDXException e) {
         throw new SemantificationException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
      } catch (KDXException e) {
         throw new SemantificationException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
      } catch (RFXException e) {
         throw new SemantificationException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
      }
   }

   protected boolean checkIfPopulateUdxKeyWord () {
      // By default return true
      return true;
   }

   public void updateCategorySpecificImageInformation (List<Long> imagePresentUdxIds) throws SemantificationException {
      // Do nothing
   }

   /**
    * @param semanticPossibilities
    * @param newsItemLinesToSemantify
    * @param udx
    */
   protected void populateUdxKeyWord (List<SemanticPossibility> semanticPossibilities,
            List<String> newsItemLinesToSemantify, UnStructuredIndex udx) {

      // Get the key word text
      StringBuilder reducedFormString = new StringBuilder(1);
      if (!CollectionUtils.isEmpty(semanticPossibilities)) {
         for (SemanticPossibility semanticPossibility : semanticPossibilities) {
            reducedFormString.append(getReducedFormHelper().getKeyWordMatchText(semanticPossibility,
                     getConceptPriorityMap()));
         }
      }

      String articleKeyWords = getOptimizedArticleText(semanticPossibilities, reducedFormString,
               newsItemLinesToSemantify);

      // Merge the reduced form text and article key word text
      String keywordText = reducedFormString.toString().toLowerCase() + " " + articleKeyWords;
      keywordText = ExecueStringUtil.removeDuplicates(keywordText, true);

      Long appId = semanticPossibilities.get(0).getApplication().getId();
      UDXKeyword udxKeyWord = new UDXKeyword();
      udxKeyWord.setUdxId(udx.getId());
      udxKeyWord.setKeywordText(keywordText);
      udxKeyWord.setContentDate(udx.getContentDate());

      createUdxKeyWord(appId, udxKeyWord);
   }

   protected Map<Long, Integer> getConceptPriorityMap () {
      return new HashMap<Long, Integer>(1);
   }

   protected abstract void createUdxKeyWord (Long applicationId, UDXKeyword udxKeyWord);

   private String getOptimizedArticleText (List<SemanticPossibility> semanticPossibilities,
            StringBuilder reducedFormString, List<String> newsItemLinesToSemantify) {

      if (CollectionUtils.isEmpty(newsItemLinesToSemantify)) {
         return reducedFormString.toString();
      }
      // Prepare the invalid tokens set
      Set<String> invalidKeyWords = new HashSet<String>(1);
      // Add the removed vertices to the list of invalid tokens
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
    * @param newsItem
    * @param newsItemLinesToSemantify
    * @param semanticPossibilities
    * @throws UDXException
    */
   private void populateDummyUdxAndKeyWordInformation (NewsItem newsItem, List<String> newsItemLinesToSemantify,
            List<SemanticPossibility> semanticPossibilities) throws UDXException {
      Long reducedFormId = -1L;
      // TODO: NK: Need to check the impact of 0 entity count and 0 max match weight
      UnStructuredIndex udx = RFXServiceHelper.populateUDXForNewsItem(newsItem, reducedFormId, 0d, 0d);
      udx = getUdxService().storeUDX(udx);

      // Populate the udx key word table
      if (checkIfPopulateUdxKeyWord()) {
         // TODO: NK: Need to pass the application id once we have it in news item
         populateUdxKeyWord(semanticPossibilities, newsItemLinesToSemantify, udx);
      }

      // Populate the article category specific tables if any
      // TODO: NK: Need to pass the application id once we have it in news item
      populateCategorySpecificTablesWithDummyData(semanticPossibilities, newsItemLinesToSemantify, udx);
   }

   protected abstract void populateCategorySpecificTablesWithDummyData (
            List<SemanticPossibility> semanticPossibilities, List<String> newsItemLinesToSemantify,
            UnStructuredIndex unstructuredIndex);

   // Generate RI UDX
   protected void populateRIUdx (List<ReducedFormIndex> rfxList, Double entityCount, UnStructuredIndex udx)
            throws RFXException, UDXException {
      Set<RIUniversalSearch> riUdxSet = new HashSet<RIUniversalSearch>(1);
      for (ReducedFormIndex reducedFormIndex : rfxList) {
         Map<RFXVariationSubType, Double> rankingWeightsMap = getRfxService()
                  .getRankingWeightsMapForContentVariationSubType(reducedFormIndex.getRfxVariationSubType());
         riUdxSet.addAll(getRfxServiceHelper().getRIUniversalSearchEntries(reducedFormIndex, rankingWeightsMap,
                  UniversalSearchType.UNSTRUCTURED_SEARCH, udx.getId(), entityCount, udx.getContentDate()));
      }
      getUdxService().storeRIUDXEntries(new ArrayList<RIUniversalSearch>(riUdxSet));
   }

   // Generate RFX and RFXValue
   protected Long populateRfxAndRfxValue (NewsItem newsItem, List<SemanticPossibility> semanticPossibilities,
            List<ReducedFormIndex> rfxList, List<RFXValue> rfxValueList) throws UidException, RFXException {
      Long reducedFormId = getRfIdGenerationService().getNextId();
      Set<RFXValue> rfxValueSet = new HashSet<RFXValue>(1);
      Set<ReducedFormIndex> rfxSet = new HashSet<ReducedFormIndex>(1);
      generateRFXAndRFXValueFromSemanticPossibilities(newsItem, semanticPossibilities, rfxSet, rfxValueSet,
               reducedFormId);

      rfxList.addAll(rfxSet);
      rfxValueList.addAll(rfxValueSet);

      getRfxService().storeRFX(rfxList);
      if (!CollectionUtils.isEmpty(rfxValueList)) {
         getRfxService().storeRFXValue(rfxValueList);
      }
      return reducedFormId;
   }

   private void assignAppId (List<SemanticPossibility> semanticPossibilities) throws KDXException {
      if (CollectionUtils.isEmpty(semanticPossibilities)) {
         return;
      }
      for (SemanticPossibility possibility : semanticPossibilities) {
         List<Application> applications = getApplicationRetrievalService().getApplicationsByModelId(
                  possibility.getModel().getId());
         Application application = applications.get(0);
         possibility.setApplication(application);
      }
   }

   private void updatePopularityHits (List<SemanticPossibility> semanticPossibilities, UnStructuredIndex udx)
            throws UDXException, KDXException {
      List<AppNewsPopularity> appNewsPopularityList = new ArrayList<AppNewsPopularity>(1);
      for (SemanticPossibility possibility : semanticPossibilities) {
         List<Application> applications = getApplicationRetrievalService().getApplicationsByModelId(
                  possibility.getModel().getId());
         Application application = applications.get(0);
         possibility.setApplication(application);
         Graph reducedFormGraph = possibility.getReducedForm();
         Collection<IGraphComponent> rootVertices = possibility.getRootVertices();
         for (IGraphComponent rv : rootVertices) {
            List<IGraphComponent> components = (List<IGraphComponent>) reducedFormGraph.getDepthFirstRoute(rv);
            for (IGraphComponent component : components) {
               AppNewsPopularity appNewsPopularity = getAppNewsPopularity(udx, possibility, component);
               appNewsPopularityList.add(appNewsPopularity);
            }
         }
      }
      getUdxService().createAppNewsPopularity(appNewsPopularityList);

   }

   private double getEntityCount (List<SemanticPossibility> semanticPossibilities) {
      double entityCount = 0d;
      if (CollectionUtils.isEmpty(semanticPossibilities)) {
         return entityCount;
      }
      for (SemanticPossibility semanticPossibility : semanticPossibilities) {
         entityCount += semanticPossibility.getAllGraphComponents().size();
      }
      return entityCount;
   }

   private AppNewsPopularity getAppNewsPopularity (UnStructuredIndex udx, SemanticPossibility possibility,
            IGraphComponent component) {
      AppNewsPopularity appNewsPopularity = new AppNewsPopularity();
      DomainRecognition domainRecognition = (DomainRecognition) component;
      Long entityBeId = null;
      // TODO need to check if we should get all the instance info here
      if (domainRecognition.getDefaultInstanceBedId() != null) {
         entityBeId = domainRecognition.getDefaultInstanceBedId();
      } else if (domainRecognition.getConceptBEDId() != null) {
         entityBeId = domainRecognition.getConceptBEDId();
      } else if (domainRecognition.getRelationBEDId() != null) {
         entityBeId = domainRecognition.getRelationBEDId();
      } else if (domainRecognition.getTypeBEDId() != null) {
         entityBeId = domainRecognition.getTypeBEDId();
      }
      appNewsPopularity.setEntityBeId(entityBeId);
      appNewsPopularity.setAppId(possibility.getApplication().getId());
      appNewsPopularity.setHits(1);
      appNewsPopularity.setUdxId(udx.getId());
      return appNewsPopularity;
   }

   /**
    * @param newsItem
    * @param newsItemLinesToSemantify
    * @return the List<SemanticPossibility>
    * @throws NLPException
    */
   private List<SemanticPossibility> semantifiNewsItem (NewsItem newsItem, List<String> newsItemLinesToSemantify)
            throws SemantificationException {
      List<SemanticPossibility> semanticPossibilities = new ArrayList<SemanticPossibility>(1);

      if (CollectionUtils.isEmpty(newsItemLinesToSemantify)) {
         return semanticPossibilities;
      }

      try {
         if (logger.isDebugEnabled()) {
            logger.debug("Start Semantifying News Article: " + newsItem);
         }
         int sentenceId = 1;
         List<String> validLines = new ArrayList<String>(1);
         for (String newsLine : newsItemLinesToSemantify) {
            if (!isValid(newsLine, newsItem.getCategory())) {
               // If any of the line is invalid, then for now we won't semantify any of the remaining lines further
               break;
            }
            if (StringUtils.isEmpty(newsLine)) {
               continue;
            }
            Long appId = getSemantificationConfiguration().getApplicationCategoryMap().get(newsItem.getCategory());
            SearchFilter searchFilter = new SearchFilter();
            if (appId != null) {
               searchFilter.setSearchFilterType(SearchFilterType.APP);
               searchFilter.setId(appId);
            } else {
               Long verticalId = getSemantificationConfiguration().getVerticalCategoryMap().get(newsItem.getCategory());
               if (verticalId != null) {
                  searchFilter.setSearchFilterType(SearchFilterType.VERTICAL);
                  searchFilter.setId(verticalId);
               }
            }

            UserQuery userQuery = new UserQuery();
            userQuery.setOriginalQuery(newsLine);
            userQuery.setSearchType(SearchType.ENTITY_SEARCH);
            userQuery.setBackendSearch(true); // set the backend search flag to true
            userQuery.setSentenceId(sentenceId);
            userQuery.setSearchFilter(searchFilter);
            userQuery.setFromArticle(true);
            NLPInformation nlpInformation = null;
            if (logger.isDebugEnabled()) {
               logger.debug("News Article Line: " + newsLine);
            }
            try {
               nlpInformation = getNlpEngine().processQuery(userQuery, null);
            } catch (NLPSystemException e) {
               e.printStackTrace();
               continue;
            }
            sentenceId++;
            validLines.add(newsLine);
            Map<SemanticPossibility, Integer> reducedForms = nlpInformation.getReducedForms();
            for (SemanticPossibility poss : reducedForms.keySet()) {
               List<IGraphPath> paths = poss.getReducedForm().getPaths();
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
                  poss.getReducedForm().removePath(path);
               }
               paths.removeAll(removePaths);
            }
            semanticPossibilities.addAll(reducedForms.keySet());
         }
         if (logger.isDebugEnabled()) {
            logger.debug("End Semantifying News Article: " + newsItem);
         }
         newsItemLinesToSemantify.clear();
         newsItemLinesToSemantify.addAll(validLines);
         return semanticPossibilities;
      } catch (NLPException nlpException) {
         throw new SemantificationException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, nlpException.getMessage());
      } catch (SWIException swiException) {
         throw new SemantificationException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, swiException.getMessage());
      }
   }

   /**
    * @param newsItem
    * @return
    */
   private List<String> getLinesForNewsItem (NewsItem newsItem) {
      List<String> newsItemLinesToSemantify = new ArrayList<String>(1);

      // Add the news item title to the news items lines list
      String newsItemTitle = NewsUtil.cleanupNewsData(newsItem.getTitle());

      // Update title for the specific category
      newsItemTitle = updateTitle(newsItem, newsItemTitle);

      String newsItemSource = newsItem.getSource();

      // Check if the news item source should be prepended to the title
      if (ExecueCoreUtil.isNotEmpty(newsItemSource) && newsItemSource.startsWith("~~") && newsItemSource.endsWith("~~")) {
         newsItemTitle = newsItemSource.replaceAll("~~", "").trim() + " " + newsItemTitle;
      }
      if (ExecueCoreUtil.isNotEmpty(newsItemTitle)) {
         newsItemLinesToSemantify.add(newsItemTitle);
      }

      int tokenLimit = getSemantificationConfiguration().getSemantificationArticleSentenceTokenLimit();

      // Add the news item summary to the news items lines list
      String newsItemSummary = NewsUtil.cleanupNewsData(newsItem.getSummary());
      if (ExecueCoreUtil.isNotEmpty(newsItemSummary)) {
         newsItemLinesToSemantify.addAll(NewsUtil.getLinesFromSummary(newsItemSummary, tokenLimit));
      }
      return newsItemLinesToSemantify;
   }

   public String updateTitle (NewsItem newsItem, String newsItemTitle) {
      return newsItemTitle;
   }

   public boolean isValid (String newsLine, NewsCategory category) {
      return true;
   }

   public abstract void generateRFXAndRFXValueFromSemanticPossibilities (NewsItem newsItem,
            List<SemanticPossibility> semanticPossibilities, Set<ReducedFormIndex> rfxSet, Set<RFXValue> rfxValueSet,
            Long reducedFormId);

   public abstract void populateCategorySpecificTables (List<SemanticPossibility> semanticPossibilities,
            List<String> newsItemLinesToSemantify, UnStructuredIndex udx);

   public abstract List<SemanticPossibility> updatePossibilities (List<SemanticPossibility> semanticPossibilities);

   public List<String> updateCategorySpecificPatterns (List<String> newsItemLinesToSemantify) {
      return newsItemLinesToSemantify;
   }

   public void invalidateNewsItem (List<SemanticPossibility> semanticPossibilities, NewsItem newsItem)
            throws InvalidNewsItemException {
      // TODO --AP-- We better return boolean value here instead of throwing exception
   }

   public void populateMissingDependentInstance (List<SemanticPossibility> semanticPossibilities, NewsItem newsItem)
            throws SemantificationException {
      // TODO --AP-- Here also we need to write some generic
   }

   public POSContext getPosContext () {
      return getSwiConfigurationService().getPosContext();
   }

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

   /**
    * @return the rfxService
    */
   public IRFXService getRfxService () {
      return rfxService;
   }

   /**
    * @param rfxService
    *           the rfxService to set
    */
   public void setRfxService (IRFXService rfxService) {
      this.rfxService = rfxService;
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
    * @return the udxService
    */
   public IUDXService getUdxService () {
      return udxService;
   }

   /**
    * @param udxService
    *           the udxService to set
    */
   public void setUdxService (IUDXService udxService) {
      this.udxService = udxService;
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

   public IUidService getRfIdGenerationService () {
      return rfIdGenerationService;
   }

   public void setRfIdGenerationService (IUidService rfIdGenerationService) {
      this.rfIdGenerationService = rfIdGenerationService;
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
    * @return the rfxServiceHelper
    */
   public RFXServiceHelper getRfxServiceHelper () {
      return rfxServiceHelper;
   }

   /**
    * @param rfxServiceHelper
    *           the rfxServiceHelper to set
    */
   public void setRfxServiceHelper (RFXServiceHelper rfxServiceHelper) {
      this.rfxServiceHelper = rfxServiceHelper;
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