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
package com.execue.uss.configuration;

import java.util.List;

import com.execue.core.common.bean.qdata.UniversalSearchResultFeatureHeader;

/**
 * @author Nitesh
 */
public interface IUnstructuredSearchConfigurationService {

   public List<String> getSearchResultOrder (String userRequestedSearchResultOrder);

   public List<UniversalSearchResultFeatureHeader> getSearchResultFeatureHeaders ();

   public Integer getDefaultVicinityDistanceLimit ();

   public boolean getDisplayClosedMatchCount ();

   public String getUnwantedCharRegex ();

   public boolean getApplyKeyWordSearchFilter ();

   public boolean getApplyPartialMatchFilter ();

   public Long getUnstructuredResultNumberOfLinks ();

   public Long getUnstructuredResultPageSize ();

   public List<String> getDefaultVicinityDistanceLimits ();

   public String getDefaultUserPreferredZipCode ();

   public Integer getDefaultSemantifiedContentKeywordMatchMaxRecordCount ();

   public Integer getResemantificationSemantifiedContentKeywordMatchMaxRecordCount ();

   public Boolean isResemantificationCheckBoxVisible ();

   public List<UniversalSearchResultFeatureHeader> getUnstructuredSearchResultsHeaders ();

   public Integer getSuggestionRetrivalLimit ();

   public boolean getUseDbFunctionForMultipleLocationQuery ();

   public String getDbFunctionNameForMultipleLocationQuery ();
}
