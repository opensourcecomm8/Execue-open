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


package com.execue.uss.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.RFXValue;
import com.execue.core.common.bean.qdata.RIUserQuery;
import com.execue.core.common.bean.qdata.ReducedFormIndex;
import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.bean.qdata.UniversalSearchResult;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.core.common.type.RFXType;
import com.execue.core.common.type.RFXValueType;
import com.execue.core.common.type.RFXVariationSubType;
import com.execue.core.common.type.SearchType;
import com.execue.core.exception.UidException;
import com.execue.platform.IUidService;
import com.execue.qdata.exception.RFXException;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.service.IRFXService;
import com.execue.qdata.service.IUDXService;
import com.execue.sus.helper.RFXServiceHelper;
import com.execue.sus.helper.ReducedFormHelper;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.uss.exception.USSException;
import com.execue.uss.service.IEntitySearchEngine;

/**
 * @author Abhijit
 * @since Jul 22, 2009 : 2:40:37 AM
 */
public class EntitySearchEngineImpl implements IEntitySearchEngine {

   private static final Logger  log = Logger.getLogger(EntitySearchEngineImpl.class);
   private IUDXService          udxService;
   private IKDXRetrievalService kdxRetrievalService;
   private IUidService          rfIdGenerationService;
   private IRFXService          rfxService;
   private RFXServiceHelper     rfxServiceHelper;

   // Getter Setter

   public IUDXService getUdxService () {
      return udxService;
   }

   public void setUdxService (IUDXService udxService) {
      this.udxService = udxService;
   }

   // Logic

   public Set<UnStructuredIndex> search (Set<Long> dedIDs) throws USSException {
      List<Long> rfIds = new ArrayList<Long>();
      Set<UnStructuredIndex> result = new HashSet<UnStructuredIndex>();
      Map<Long, List<Long>> rfIDMap = null;
      try {
         // Method call getUdxService().getRFIdsMapByDEDIds does not exist
         // because bean and table structure has been changed.
         rfIDMap = null;
         for (Map.Entry<Long, List<Long>> entry : rfIDMap.entrySet()) {
            rfIds.addAll(entry.getValue());
         }
         Map<Long, List<UnStructuredIndex>> udxIDMap = getUdxService().getUDXMapByRFIds(rfIds);
         for (Map.Entry<Long, List<UnStructuredIndex>> entry : udxIDMap.entrySet()) {
            result.addAll(entry.getValue());
         }
      } catch (UDXException e) {
         throw new USSException(e.getCode(), e);
      }
      return result;
   }

   public UniversalUnstructuredSearchResult universalSearch (SemanticPossibility reducedFormPossibility,
            Map<Long, Set<Long>> knowledgeSearchBedsByAppId, int position) throws USSException {
      UniversalUnstructuredSearchResult universalSearchResult = null;
      try {
         Long reducedFormId = getRfIdGenerationService().getNextId();

         // Get the rfx variation
         // TODO: NK: Should later check if we need to pass the correct user query id, currently this method itself if
         // not used
         Set<ReducedFormIndex> rfxEntries = new HashSet<ReducedFormIndex>(1);
         Set<RFXValue> rfxValueEntries = new HashSet<RFXValue>();
         getRfxServiceHelper().populateRFXAndRFXValueFromSemanticPossibility(reducedFormPossibility, rfxEntries,
                  rfxValueEntries, reducedFormId, -1L, false, RFXType.RFX_ENTITY_SEARCH,
                  RFXValueType.RFX_VALUE_USER_QUERY, new HashSet<String>(1), new HashSet<String>(1),
                  new HashMap<Long, Integer>(1), new HashMap<String, String>(1));
         if (!knowledgeSearchBedsByAppId.isEmpty()) {
            for (Entry<Long, Set<Long>> entry : knowledgeSearchBedsByAppId.entrySet()) {
               Long applicationId = entry.getKey();
               Set<Long> knowledgeSearchBeds = entry.getValue();
               rfxEntries.addAll(getRfxServiceHelper().generateRFXFromInstanceBeds(knowledgeSearchBeds,
                        RFXType.RFX_ENTITY_SEARCH, reducedFormId, applicationId));
            }
         }
         if (CollectionUtils.isEmpty(rfxEntries)) {
            return universalSearchResult;
         }
         List<ReducedFormIndex> rfxList = new ArrayList<ReducedFormIndex>(rfxEntries);
         getRfxService().storeRFX(rfxList);

         // Generate the ri_usery_query variation based on the rfx
         Set<RIUserQuery> riUserQueries = new HashSet<RIUserQuery>(1);
         Integer entityCount = ReducedFormHelper.getEntityCount(reducedFormPossibility);
         for (ReducedFormIndex reducedFormIndex : rfxList) {
            Map<RFXVariationSubType, Double> rankingWeightsMap = getRfxService()
                     .getRankingWeightsMapForContentVariationSubType(reducedFormIndex.getRfxVariationSubType());
            riUserQueries.addAll(getRfxServiceHelper().getRIUserQueryEntries(reducedFormIndex, rankingWeightsMap,
                     new HashMap<Long, Integer>(), SearchType.ENTITY_SEARCH, reducedFormId, entityCount, false));

         }
         getUdxService().saveUserQueryReverseIndex(new ArrayList<RIUserQuery>(riUserQueries));

         // Get the universal search results
         universalSearchResult = getUniversalSearchResultsByUserQueryId(reducedFormId, position);
      } catch (UidException e) {
         throw new USSException(e.getCode(), e);
      } catch (RFXException e) {
         throw new USSException(e.getCode(), e);
      } catch (UDXException e) {
         throw new USSException(e.getCode(), e);
      }
      return universalSearchResult;
   }

   public UniversalUnstructuredSearchResult universalSearch (Map<Long, Set<Long>> knowledgeSearchBedsByAppId,
            Long userQueryId, int pageNumber) throws USSException {
      UniversalUnstructuredSearchResult universalSearchResult = null;
      try {
         // Get the rfx variation
         Set<ReducedFormIndex> rfxEntries = new HashSet<ReducedFormIndex>(1);
         if (!knowledgeSearchBedsByAppId.isEmpty()) {
            for (Entry<Long, Set<Long>> entry : knowledgeSearchBedsByAppId.entrySet()) {
               Long applicationId = entry.getKey();
               Set<Long> knowledgeSearchBeds = entry.getValue();
               rfxEntries.addAll(getRfxServiceHelper().generateRFXFromInstanceBeds(knowledgeSearchBeds,
                        RFXType.RFX_ENTITY_SEARCH, userQueryId, applicationId));
            }
         }
         List<ReducedFormIndex> rfxList = new ArrayList<ReducedFormIndex>(rfxEntries);
         if (!CollectionUtils.isEmpty(rfxList)) {
            // getRfxService().storeRFX(rfxList);
            // Generate the ri_usery_query variation based on the rfx
            Set<RIUserQuery> riUserQueries = new HashSet<RIUserQuery>(1);
            for (ReducedFormIndex reducedFormIndex : rfxList) {
               Map<RFXVariationSubType, Double> rankingWeightsMap = getRfxService()
                        .getRankingWeightsMapForContentVariationSubType(reducedFormIndex.getRfxVariationSubType());
               riUserQueries.addAll(getRfxServiceHelper().getRIUserQueryEntries(reducedFormIndex, rankingWeightsMap,
                        new HashMap<Long, Integer>(), SearchType.ENTITY_SEARCH, userQueryId, 1, false));
            }
            getUdxService().saveUserQueryReverseIndex(new ArrayList<RIUserQuery>(riUserQueries));
         }

         // Get the universal search results
         universalSearchResult = getUniversalSearchResultsByUserQueryId(userQueryId, pageNumber);
      } catch (RFXException e) {
         throw new USSException(e.getCode(), e);
      } catch (UDXException e) {
         throw new USSException(e.getCode(), e);
      }
      return universalSearchResult;
   }

   public UniversalUnstructuredSearchResult getUniversalSearchResultsByUserQueryId (Long userQueryId, int pageNumber)
            throws USSException {
      try {
         UniversalUnstructuredSearchResult universalSearchResult = getUdxService().getRIUnstructuredResultsByUserQuery(
                  userQueryId, pageNumber, 10);
         return universalSearchResult;
      } catch (UDXException e) {
         throw new USSException(e.getCode(), e);
      }
   }

   public List<UniversalSearchResult> filterUniversalSearchResultByRFXValueMatch (
            Set<SemanticPossibility> possibilities, List<UniversalSearchResult> resultItems, Long queryId)
            throws USSException {
      if (CollectionUtils.isEmpty(resultItems)) {
         return resultItems;
      }

      // Prepare the rfx value for possibilities
      Map<Long, Set<RFXValue>> rfxValueMap = new HashMap<Long, Set<RFXValue>>(1);
      for (SemanticPossibility semanticPossibility : possibilities) {
         Set<RFXValue> rfxValueSet = getRfxServiceHelper().getRFXValueSet(semanticPossibility, queryId);
         if (!CollectionUtils.isEmpty(rfxValueSet)) {
            rfxValueMap.put(semanticPossibility.getId(), rfxValueSet);
         }
      }
      // If no rfx value found, then do not filter the result items
      if (rfxValueMap.isEmpty()) {
         return resultItems;
      }

      List<UniversalSearchResult> filteredResultItems = new ArrayList<UniversalSearchResult>(1);
      Map<Long, UniversalSearchResult> resultItemMap = getUniversalSearchResultMapByRfId(resultItems);
      Set<Long> validRFIds = new HashSet<Long>(1);
      for (SemanticPossibility semanticPossibility : possibilities) {
         Set<RFXValue> rfxValueSet = rfxValueMap.get(semanticPossibility.getId());
         if (CollectionUtils.isEmpty(rfxValueSet)) {
            continue;
         }
         try {
            // List<Long> validRfIdsForPossibility = getRfxService().matchAllRFXValue(rfxValueSet,
            // resultItemMap.keySet());
            List<Long> validRfIdsForPossibility = getRfxService().matchAnyRFXValue(rfxValueSet, resultItemMap.keySet());
            if (!CollectionUtils.isEmpty(validRfIdsForPossibility)) {
               validRFIds.addAll(validRfIdsForPossibility);
            }
         } catch (RFXException e) {
            throw new USSException(e.getCode(), e);
         }
      }

      // If no value match found, then return the empty list
      if (CollectionUtils.isEmpty(validRFIds)) {
         return filteredResultItems;
      }
      for (Long rfId : validRFIds) {
         filteredResultItems.add(resultItemMap.get(rfId));
      }

      // Sort the filtered result items by weight
      if (!CollectionUtils.isEmpty(filteredResultItems)) {
         Collections.sort(filteredResultItems, new Comparator<UniversalSearchResult>() {

            public int compare (UniversalSearchResult o1, UniversalSearchResult o2) {
               return (int) ((Double) o2.getMatchWeight() - (Double) o1.getMatchWeight());
            }
         });
      }

      return filteredResultItems;
   }

   private Map<Long, UniversalSearchResult> getUniversalSearchResultMapByRfId (List<UniversalSearchResult> resultItems) {
      Map<Long, UniversalSearchResult> resultItemMap = new HashMap<Long, UniversalSearchResult>(1);
      for (UniversalSearchResult universalSearchResult : resultItems) {
         resultItemMap.put(universalSearchResult.getRfId(), universalSearchResult);
      }
      return resultItemMap;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

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

   public IUidService getRfIdGenerationService () {
      return rfIdGenerationService;
   }

   public void setRfIdGenerationService (IUidService rfIdGenerationService) {
      this.rfIdGenerationService = rfIdGenerationService;
   }
}