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


package com.execue.platform.unstructured.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.ExecueFacet;
import com.execue.core.common.bean.FacetQueryInput;
import com.execue.core.common.bean.LocationDetail;
import com.execue.core.common.bean.entity.unstructured.UserQueryFeatureInformation;
import com.execue.core.common.type.CheckType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.das.solr.bean.SolrFacetQueryInput;
import com.execue.das.solr.bean.SolrFacetResponse;
import com.execue.das.solr.exception.SolrException;
import com.execue.das.solr.service.ISolrRetrievalService;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.unstructured.IUnstructuredFacetRetrievalService;
import com.execue.platform.unstructured.UnstructuredFacetServiceHelper;

/**
 * @author Vishay
 */
public class UnstructuredFacetRetrievalServiceImpl implements IUnstructuredFacetRetrievalService {

   private ISolrRetrievalService          solrRetrievalService;
   private UnstructuredFacetServiceHelper unstructuredFacetServiceHelper;

   @Override
   public List<ExecueFacet> retrieveFacets (Long contextId, FacetQueryInput facetQueryInput) throws PlatformException {
      List<ExecueFacet> totalFacetsList = new ArrayList<ExecueFacet>();
      try {
         // TODO: Commented the test build facet query input, later can get rid off it as is coming as input parameter
         //         facetQueryInput = buildFacetQueryInput();
         // get the solr based queries populated using facet query input
         List<SolrFacetQueryInput> solrFacetQueryInputList = getUnstructuredFacetServiceHelper()
                  .buildSolrFacetQueryInput(contextId, facetQueryInput);
         if (ExecueCoreUtil.isCollectionNotEmpty(solrFacetQueryInputList)) {
            // for each solr query input, retrieve the facets response
            for (SolrFacetQueryInput solrFacetQueryInput : solrFacetQueryInputList) {
               List<SolrFacetResponse> solrFacetResponseList = getSolrRetrievalService().retrieveFacets(contextId,
                        solrFacetQueryInput);
               // parser solr facet response to execue response
               List<ExecueFacet> facetsList = getUnstructuredFacetServiceHelper().parseFacetQueryResponse(contextId,
                        solrFacetResponseList);
               if (ExecueCoreUtil.isCollectionNotEmpty(facetsList)) {
                  totalFacetsList.addAll(facetsList);
               }
            }
         }
      } catch (SolrException e) {
         throw new PlatformException(e.getCode(), e);
      }
      return totalFacetsList;
   }

   private FacetQueryInput buildFacetQueryInput () {
      FacetQueryInput facetQueryInput = new FacetQueryInput();
      facetQueryInput.setDistance(100d);
      facetQueryInput.setImagePresent(CheckType.NO);
      facetQueryInput.addLocationDetail(new LocationDetail(-118.24277496d, 35d));
      // facetQueryInput.addLocationDetail(new LocationDetail(20d, 35d));
      // facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(10L, "Single cyl", "Single cyl"));
      facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(10L, "UNKNOWN_VALUE", "UNKNOWN_VALUE"));
      // facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(13L, "Dodge", "Dodge"));
      facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(13L, "UNKNOWN_VALUE", "UNKNOWN_VALUE"));
      // facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(12L, "UNKNOWN_VALUE", "UNKNOWN_VALUE"));
      facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(18L, "-1", "-1"));
      facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(18L, "100", "100000"));
      return facetQueryInput;
   }

   private UserQueryFeatureInformation buildUserQueryFeatureInformation (Long featureId, String startValue,
            String endValue) {
      UserQueryFeatureInformation featureInformation = new UserQueryFeatureInformation();
      featureInformation.setFeatureId(featureId);
      featureInformation.setStartValue(startValue);
      featureInformation.setEndValue(endValue);
      return featureInformation;
   }

   public ISolrRetrievalService getSolrRetrievalService () {
      return solrRetrievalService;
   }

   public void setSolrRetrievalService (ISolrRetrievalService solrRetrievalService) {
      this.solrRetrievalService = solrRetrievalService;
   }

   public UnstructuredFacetServiceHelper getUnstructuredFacetServiceHelper () {
      return unstructuredFacetServiceHelper;
   }

   public void setUnstructuredFacetServiceHelper (UnstructuredFacetServiceHelper unstructuredFacetServiceHelper) {
      this.unstructuredFacetServiceHelper = unstructuredFacetServiceHelper;
   }
}
