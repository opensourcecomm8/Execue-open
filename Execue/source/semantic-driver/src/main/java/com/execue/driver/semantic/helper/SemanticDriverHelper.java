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


package com.execue.driver.semantic.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.QDataReducedQuery;
import com.execue.core.common.bean.qdata.RFXValue;
import com.execue.core.common.bean.qdata.RIUserQuery;
import com.execue.core.common.bean.qdata.ReducedFormIndex;
import com.execue.core.common.type.RFXType;
import com.execue.core.common.type.RFXValueType;
import com.execue.core.common.type.RFXVariationSubType;
import com.execue.core.common.type.SearchType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.exception.RFXException;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.helper.QueryDataServiceHelper;
import com.execue.qdata.service.IQueryDataService;
import com.execue.qdata.service.IRFXService;
import com.execue.qdata.service.IUDXService;
import com.execue.querycache.configuration.IQueryCacheConfiguration;
import com.execue.querycache.configuration.QueryCacheConstants;
import com.execue.sus.helper.RFXServiceHelper;
import com.execue.sus.helper.ReducedFormHelper;

public class SemanticDriverHelper {

   private static Logger             logger = Logger.getLogger(SemanticDriverHelper.class);

   private ICoreConfigurationService coreConfigurationService;
   private IQueryCacheConfiguration  queryCacheConfiguration;
   private RFXServiceHelper          rfxServiceHelper;
   private IUDXService               udxService;
   private IRFXService               rfxService;
   private IQueryDataService         queryDataService;

   public void generateRIUserQueries (Set<SemanticPossibility> semanticPossibilities, Set<ReducedFormIndex> rfxList,
            Set<RFXValue> rfxValues, Map<Long, QDataReducedQuery> reducedQueryByAppId,
            Map<Long, Set<ReducedFormIndex>> rfxByAppId, Map<Long, Integer> conceptPriorityMap, Long userQueryId,
            double baseUserQueryWeight, Map<String, String> dependentConceptByRequiredConcept)
            throws QueryDataException, RFXException, UDXException {
      if (CollectionUtils.isEmpty(semanticPossibilities)) {
         return;
      }
      Long riUserQueryGenerationStartTime = System.currentTimeMillis();
      Set<RIUserQuery> riUserQueries = new HashSet<RIUserQuery>(1);
      int queryCacheRelatedQueryThreshold = getQueryCacheConfiguration().getConfiguration().getInt(
               QueryCacheConstants.QUERY_CACHE_RELATED_QUERY_MAX_LIMIT);

      int counter = 0;
      // Limit the search to QueryCacheConstants.QUERY_CACHE_RELATED_QUERY_MAX_LIMIT i.e 10 possibilities
      for (SemanticPossibility reducedFormPossibility : semanticPossibilities) {
         if (counter >= queryCacheRelatedQueryThreshold) {
            break;
         }
         counter++;

         // NOTE: NK: RFX entries is not stored in db and rf id is never used, so assigning the temporary rf id
         Long reducedFormId = Long.valueOf(counter);

         // Get the rfx variation
         Set<RFXValue> rfxValueSet = new HashSet<RFXValue>(1);
         Set<ReducedFormIndex> rfxEntries = new HashSet<ReducedFormIndex>(1);

         getRfxServiceHelper().populateRFXAndRFXValueFromSemanticPossibility(reducedFormPossibility, rfxEntries,
                  rfxValueSet, reducedFormId, userQueryId, false, RFXType.RFX_QUERY_CACHE,
                  RFXValueType.RFX_VALUE_USER_QUERY, new HashSet<String>(1), new HashSet<String>(1),
                  conceptPriorityMap, dependentConceptByRequiredConcept);
         if (CollectionUtils.isEmpty(rfxEntries)) {
            continue;
         }

         String appName = reducedFormPossibility.getApplication().getName();
         // Populate the ri_usery_query variation based on the rfx
         Integer entityCount = ReducedFormHelper.getEntityCount(reducedFormPossibility);
         for (ReducedFormIndex reducedFormIndex : rfxEntries) {
            Map<RFXVariationSubType, Double> rankingWeightsMap = getRfxService()
                     .getRankingWeightsMapForContentVariationSubType(reducedFormIndex.getRfxVariationSubType());
            riUserQueries.addAll(getRfxServiceHelper().getRIUserQueryEntries(reducedFormIndex, rankingWeightsMap,
                     conceptPriorityMap, SearchType.QUERY_CACHE, userQueryId, entityCount,
                     checkIfSkipDerivedVariation(appName)));

         }

         Double reducedQueryWeight = reducedFormPossibility.getWeightInformationForExplicitEntities().getFinalWeight();
         Double reducedQueryMatchPercent = reducedQueryWeight / baseUserQueryWeight * 100;

         Long applicationId = reducedFormPossibility.getApplication().getId();
         // Store the reduced query entry
         Double queryMaxMatchWeight = RFXServiceHelper.getMaxMatchWeight(rfxEntries, rfxValueSet);
         QDataReducedQuery qDataReducedQuery = QueryDataServiceHelper.populateQDataReducedQuery(userQueryId,
                  applicationId, reducedFormPossibility.getDisplayString(), entityCount, queryMaxMatchWeight,
                  reducedQueryWeight, baseUserQueryWeight, reducedQueryMatchPercent);
         getQueryDataService().storeReducedQuery(qDataReducedQuery);

         // Store the rfx,ri user query entries in the list, map
         rfxList.addAll(rfxEntries);
         rfxValues.addAll(rfxValueSet);
         reducedQueryByAppId.put(applicationId, qDataReducedQuery);
         rfxByAppId.put(applicationId, rfxEntries);
      }

      // Store the ri user query entries
      if (CollectionUtils.isEmpty(riUserQueries)) {
         return;
      }
      getUdxService().saveUserQueryReverseIndex(new ArrayList<RIUserQuery>(riUserQueries));
      if (logger.isDebugEnabled()) {
         logger
                  .debug("\nRI User Query Creation Time: "
                           + (System.currentTimeMillis() - riUserQueryGenerationStartTime));
      }
   }

   private boolean checkIfSkipDerivedVariation (String appName) {
      List<String> appNames = getCoreConfigurationService().getSkipDerivedUserQueryVariationAppNames();
      return appNames.contains(appName);
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

   public IUDXService getUdxService () {
      return udxService;
   }

   public void setUdxService (IUDXService udxService) {
      this.udxService = udxService;
   }

   public IRFXService getRfxService () {
      return rfxService;
   }

   public void setRfxService (IRFXService rfxService) {
      this.rfxService = rfxService;
   }

   public IQueryCacheConfiguration getQueryCacheConfiguration () {
      return queryCacheConfiguration;
   }

   public void setQueryCacheConfiguration (IQueryCacheConfiguration queryCacheConfiguration) {
      this.queryCacheConfiguration = queryCacheConfiguration;
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
    * @return the queryDataService
    */
   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   /**
    * @param queryDataService the queryDataService to set
    */
   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

}
