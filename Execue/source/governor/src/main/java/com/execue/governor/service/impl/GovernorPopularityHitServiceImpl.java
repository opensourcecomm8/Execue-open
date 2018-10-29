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


package com.execue.governor.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.swi.PopularityHit;
import com.execue.core.common.type.TermType;
import com.execue.governor.exception.GovernorException;
import com.execue.governor.exception.GovernorExceptionCodes;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.governor.helper.GovernorServiceHelper;
import com.execue.governor.service.IGovernorPopularityHitService;
import com.execue.swi.service.IPopularityService;

/**
 * This service is for updating the popularity hit for all the entities(business + asset)
 * 
 * @author Vishay
 * @version 1.0
 * @since 24/08/2010
 */
public class GovernorPopularityHitServiceImpl implements IGovernorPopularityHitService {

   private static final Logger logger = Logger.getLogger(GovernorPopularityHitServiceImpl.class);

   private IPopularityService  popularityService;

   public void updatePopularityCounts (BusinessQuery businessQuery, List<StructuredQuery> structuredQueries)
            throws GovernorException {
      try {
         Set<Long> totalBusinessEntityIds = new HashSet<Long>();
         Set<Long> totalAssetEntityIds = new HashSet<Long>();
         Set<Long> applicationIds = new HashSet<Long>();
         totalBusinessEntityIds.addAll(GovernorServiceHelper.populateBusinessEntityIds(businessQuery));
         for (StructuredQuery structuredQuery : structuredQueries) {
            applicationIds.add(structuredQuery.getAsset().getApplication().getId());
            totalBusinessEntityIds.addAll(GovernorServiceHelper.populateBusinessEntityIds(structuredQuery));
            totalAssetEntityIds.addAll(GovernorServiceHelper.populateAssetEntityIds(structuredQuery));
            long startTime = System.currentTimeMillis();
            long endTime = System.currentTimeMillis();
            logger.debug("time taken to getSdxService().getAssetEntityDefinitionByIds " + (endTime - startTime)
                     / 1000.0 + " seconds");
            totalAssetEntityIds.add(structuredQuery.getAssetAEDId());
         }

         // Popularity Service invocations for updating the HITS for the application,model,asset entity term,business
         // entity term
         long startTime = System.currentTimeMillis();
         List<PopularityHit> hits = getHitEntriesForAssetEntityTerms(totalAssetEntityIds);
         hits.addAll(getHitEntriesForBusinessEntityTerms(totalBusinessEntityIds));
         hits.addAll(getHitEntriesForApplication(applicationIds));
         hits.add(getPopularityHitForModel(businessQuery.getModelId()));
         getPopularityService().saveAll(hits);
         long endTime = System.currentTimeMillis();
         logger.debug("time taken to add HitEntryFor Applications,Models,AssetEntityTerms,BusinessEntityTerms "
                  + (endTime - startTime) / 1000.0 + " seconds");
      } catch (SDXException sException) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_SYSTEM_EXCEPTION_CODE, sException);
      } catch (KDXException kException) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_SYSTEM_EXCEPTION_CODE, kException);
      } catch (SWIException swiException) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_SYSTEM_EXCEPTION_CODE, swiException);
      }
   }

   private List<PopularityHit> getHitEntriesForAssetEntityTerms (Set<Long> assetEntityIds) throws SWIException {
      List<PopularityHit> hits = new ArrayList<PopularityHit>();
      for (Long termId : assetEntityIds) {
         PopularityHit hit = new PopularityHit();
         hit.setType(TermType.ASSET_ENTITY);
         hit.setCreatedDate(new Date());
         hit.setHits(1L);
         hit.setProcessingState("N");
         hit.setTermId(termId);
         hits.add(hit);
      }
      return hits;
   }

   private List<PopularityHit> getHitEntriesForBusinessEntityTerms (Set<Long> businessEntityIds) throws SWIException {
      List<PopularityHit> hits = new ArrayList<PopularityHit>();
      for (Long termId : businessEntityIds) {
         PopularityHit hit = new PopularityHit();
         hit.setType(TermType.BUSINESS_ENTITY);
         hit.setCreatedDate(new Date());
         hit.setHits(1L);
         hit.setProcessingState("N");
         hit.setTermId(termId);
         hits.add(hit);
      }
      return hits;
   }

   private List<PopularityHit> getHitEntriesForApplication (Set<Long> applicationIds) throws SWIException {
      List<PopularityHit> hits = new ArrayList<PopularityHit>();
      for (Long applicationId : applicationIds) {
         PopularityHit hit = new PopularityHit();
         hit.setType(TermType.APPLICATION);
         hit.setCreatedDate(new Date());
         hit.setHits(1L);
         hit.setProcessingState("N");
         hit.setTermId(applicationId);
         hits.add(hit);
      }
      return hits;
   }

   private PopularityHit getPopularityHitForModel (Long modelId) throws SWIException {

      PopularityHit hit = new PopularityHit();
      hit.setType(TermType.MODEL);
      hit.setCreatedDate(new Date());
      hit.setHits(1L);
      hit.setProcessingState("N");
      hit.setTermId(modelId);
      return hit;
   }

   public IPopularityService getPopularityService () {
      return popularityService;
   }

   public void setPopularityService (IPopularityService popularityService) {
      this.popularityService = popularityService;
   }

}
