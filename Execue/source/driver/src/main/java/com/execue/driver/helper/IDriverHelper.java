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


package com.execue.driver.helper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.AssetResult;
import com.execue.core.common.bean.ReportGroupResult;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.QDataAggregatedQuery;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryRepresentation;
import com.execue.core.common.bean.swi.PossibleAssetInfo;
import com.execue.qdata.exception.QueryDataException;

public interface IDriverHelper {

   public AssetResult populateAssetResult (AssetResult assetResult, Long queryId, boolean isRelevancePopulated)
            throws QueryDataException;

   public List<ReportGroupResult> generateReportGroup (List<Integer> reportTypes);

   public void populateReportType (QDataAggregatedQuery dataAggregatedQuery, AssetResult assetResult);

   public QueryRepresentation getQueryRepresentation (Query query, Asset asset);

   public void populateAssetInfo (AssetResult assetResult, Asset asset);

   public String formatCachedDate (Date cachedDate);

   public String formatUnstructuredContentDate (Date cachedDate);

   public SearchFilter getGeneralSearchFilter ();

   public Set<SemanticPossibility> getScopedPossibilities (Set<SemanticPossibility> allPossibilities);

   public void correctSearchScoping (Set<SemanticPossibility> possibilities, List<String> appNames);

   public Map<Long, List<PossibleAssetInfo>> sortAssetInfoMapBasedOnSearchScope (
            Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap,
            Map<Long, SemanticPossibility> possibilityMap);
}