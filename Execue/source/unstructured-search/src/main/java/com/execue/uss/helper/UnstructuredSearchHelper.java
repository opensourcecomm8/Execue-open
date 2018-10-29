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
package com.execue.uss.helper;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.entity.unstructured.SemantifiedContentFeatureInformation;
import com.execue.core.common.bean.qdata.UniversalSearchResultFeatureHeader;
import com.execue.core.common.bean.uss.UnstructuredKeywordSearchInput;
import com.execue.core.common.bean.uss.UnstructuredSearchInput;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.uswh.helper.UnstructuredWarehouseHelper;

/**
 * @author Nitesh
 *
 */
public class UnstructuredSearchHelper {

   public static Integer getUserQueryFeatureCount (List<SemantifiedContentFeatureInformation> featuresInformation) {
      int featureCount = 0;
      if (CollectionUtils.isEmpty(featuresInformation)) {
         return featureCount;
      }

      Map<Long, List<SemantifiedContentFeatureInformation>> featuresInformationByFeatureId = UnstructuredWarehouseHelper
               .getFeaturesInformationByFeatureId(featuresInformation);

      for (Entry<Long, List<SemantifiedContentFeatureInformation>> entry : featuresInformationByFeatureId.entrySet()) {
         List<SemantifiedContentFeatureInformation> featuresInformationValueEntries = entry.getValue();

         SemantifiedContentFeatureInformation semantifiedContentFeatureInformation = featuresInformationValueEntries
                  .get(0);

         if (semantifiedContentFeatureInformation.getFeatureValueType() == FeatureValueType.VALUE_DUMMY) {
            continue;
         }

         if (semantifiedContentFeatureInformation.getMultiValued() == CheckType.NO) {
            featureCount += 1;
            continue;
         } else if (semantifiedContentFeatureInformation.getMultiValuedGlobalPenalty() == CheckType.YES) {
            featureCount += featuresInformationValueEntries.size();
            continue;
         }
         featureCount += 1;
      }

      // If feature count is zero here, means its purely based on location, so will have to consider the feature count as 1 for dummy feature match
      if (featureCount == 0) {
         featureCount = 1;
      }
      return featureCount;
   }

   public static Integer getUserQueryRecordCount (List<SemantifiedContentFeatureInformation> featuresInformation) {
      int recordCount = 0;
      if (CollectionUtils.isEmpty(featuresInformation)) {
         return recordCount;
      }
      Map<Long, List<SemantifiedContentFeatureInformation>> featuresInformationByFeatureId = UnstructuredWarehouseHelper
               .getFeaturesInformationByFeatureId(featuresInformation);

      for (Entry<Long, List<SemantifiedContentFeatureInformation>> entry : featuresInformationByFeatureId.entrySet()) {
         List<SemantifiedContentFeatureInformation> featuresInformationValueEntries = entry.getValue();

         SemantifiedContentFeatureInformation semantifiedContentFeatureInformation = featuresInformationValueEntries
                  .get(0);

         if (semantifiedContentFeatureInformation.getFeatureValueType() == FeatureValueType.VALUE_DUMMY) {
            recordCount += 1;
            continue;
         }

         if (semantifiedContentFeatureInformation.getMultiValued() == CheckType.NO) {
            recordCount += 1;
            continue;
         } else {
            recordCount += featuresInformationValueEntries.size();
         }
      }
      return recordCount;
   }

   public static UnstructuredSearchInput prepareUnstructuredSearchInput (Long applicationId, Long userQueryId,
            Integer userQueryFeatureCount, Integer userQueryRecordCount, Integer distanceLimit,
            boolean isLocationBased, boolean imagePresent, boolean applyKeyWordSearchFilter,
            boolean applyPartialMatchFilter, boolean displayCloseMatchCount, List<String> searchResultOrder,
            List<String> userRequestedLocations, List<String> selectColumnNameList,
            List<UniversalSearchResultFeatureHeader> uiversalSearchResultFeatureHeaders, String userRequestedSortOrder,
            boolean useDbFunctionForMultipleLocationQuery, String dbFunctionNameForMultipleLocationQuery) {
      UnstructuredSearchInput unstructuredSearchInput = new UnstructuredSearchInput();
      unstructuredSearchInput.setApplicationId(applicationId);
      unstructuredSearchInput.setUserQueryId(userQueryId);
      unstructuredSearchInput.setUserQueryFeatureCount(userQueryFeatureCount);
      unstructuredSearchInput.setUserQueryRecordCount(userQueryRecordCount);
      unstructuredSearchInput.setDistanceLimit(distanceLimit);
      unstructuredSearchInput.setLocationBased(isLocationBased);
      unstructuredSearchInput.setImagePresent(imagePresent);
      unstructuredSearchInput.setApplyKeyWordSearchFilter(applyKeyWordSearchFilter);
      unstructuredSearchInput.setApplyPartialMatchFilter(applyPartialMatchFilter);
      unstructuredSearchInput.setDisplayCloseMatchCount(displayCloseMatchCount);
      unstructuredSearchInput.setSearchResultOrder(searchResultOrder);
      unstructuredSearchInput.setUserRequestedLocations(userRequestedLocations);
      unstructuredSearchInput.setSelectColumnNames(selectColumnNameList);
      unstructuredSearchInput.setUniversalSearchResultFeatureHeaders(uiversalSearchResultFeatureHeaders);
      unstructuredSearchInput.setUserRequestedSortOrder(userRequestedSortOrder);
      unstructuredSearchInput.setUseDbFunctionForMultipleLocationQuery(useDbFunctionForMultipleLocationQuery);
      unstructuredSearchInput.setDbFunctionNameForMultipleLocationQuery(dbFunctionNameForMultipleLocationQuery);
      return unstructuredSearchInput;
   }

   public static UnstructuredKeywordSearchInput prepareKeywordSearchInput (List<String> userRequestedLocations,
            String userQueryTokens, Long applicationId, Long userQueryId, Integer userQueryDistanceLimit,
            Integer maxRecordCount, boolean isLocationBased, boolean applyImagePresentFilter,
            boolean useDbFunctionForMultipleLocationQuery, String dbFunctionNameForMultipleLocationQuery) {
      UnstructuredKeywordSearchInput unstructuredKeywordSearchInput = new UnstructuredKeywordSearchInput();
      unstructuredKeywordSearchInput.setApplicationId(applicationId);
      unstructuredKeywordSearchInput.setApplyImagePresentFilter(applyImagePresentFilter);
      unstructuredKeywordSearchInput.setLocationBased(isLocationBased);
      unstructuredKeywordSearchInput.setMaxRecordCount(maxRecordCount);
      unstructuredKeywordSearchInput.setUserQueryDistanceLimit(userQueryDistanceLimit);
      unstructuredKeywordSearchInput.setUserQueryId(userQueryId);
      unstructuredKeywordSearchInput.setUserQueryTokens(userQueryTokens);
      unstructuredKeywordSearchInput.setUserRequestedLocations(userRequestedLocations);
      unstructuredKeywordSearchInput.setUseDbFunctionForMultipleLocationQuery(useDbFunctionForMultipleLocationQuery);
      unstructuredKeywordSearchInput.setDbFunctionNameForMultipleLocationQuery(dbFunctionNameForMultipleLocationQuery);
      return unstructuredKeywordSearchInput;
   }
}
