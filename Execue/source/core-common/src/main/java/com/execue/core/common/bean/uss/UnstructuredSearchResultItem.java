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


package com.execue.core.common.bean.uss;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.bean.qdata.UniversalSearchResultFeature;

/**
 * @author Abhijit
 */
public class UnstructuredSearchResultItem extends UnStructuredIndex {

   private Long                                      resultItemId;
   private Double                                    weight;
   private String                                    searchType;
   private String                                    name;
   private Long                                      applicationId;
   // represent the string format of content date to display in UI
   private String                                    contentDateString;
   private UniversalSearchResultItemType             resultItemType;
   private Map<String, UniversalSearchResultFeature> resultFeatureMap = new LinkedHashMap<String, UniversalSearchResultFeature>();
   // unstructuredSearchResultData represents result data to display in UI
   private List<UniversalSearchResultFeature>        unstructuredSearchResultData;
   private UnstructuredSearchResultDataHeader        unstructuredSearchResultDataHeader;

   public Double getWeight () {
      return weight;
   }

   public void setWeight (Double rank) {
      this.weight = rank;
   }

   public String getSearchType () {
      return searchType;
   }

   public void setSearchType (String searchType) {
      this.searchType = searchType;
   }

   /**
    * @return the name
    */
   public String getName () {
      return name;
   }

   /**
    * @param name
    *           the name to set
    */
   public void setName (String name) {
      this.name = name;
   }

   /**
    * @return the applicationId
    */
   public final Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId
    *           the applicationId to set
    */
   public final void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public String getContentDateString () {
      return contentDateString;
   }

   public void setContentDateString (String contentDateString) {
      this.contentDateString = contentDateString;
   }

   /**
    * @return the resultItemType
    */
   public UniversalSearchResultItemType getResultItemType () {
      return resultItemType;
   }

   /**
    * @param resultItemType
    *           the resultItemType to set
    */
   public void setResultItemType (UniversalSearchResultItemType resultItemType) {
      this.resultItemType = resultItemType;
   }

   /**
    * @return the resultFeatureMap
    */
   public Map<String, UniversalSearchResultFeature> getResultFeatureMap () {
      return resultFeatureMap;
   }

   /**
    * @param resultFeatureMap
    *           the resultFeatureMap to set
    */
   public void setResultFeatureMap (Map<String, UniversalSearchResultFeature> resultFeatureMap) {
      this.resultFeatureMap = resultFeatureMap;
   }

   /**
    * @return the unstructuredSearchResultData
    */
   public List<UniversalSearchResultFeature> getUnstructuredSearchResultData () {
      if (unstructuredSearchResultData == null) {
         unstructuredSearchResultData = new ArrayList<UniversalSearchResultFeature>();
      }
      return unstructuredSearchResultData;
   }

   /**
    * @param unstructuredSearchResultData the unstructuredSearchResultData to set
    */
   public void setUnstructuredSearchResultData (List<UniversalSearchResultFeature> unstructuredSearchResultData) {
      this.unstructuredSearchResultData = unstructuredSearchResultData;
   }

   /**
    * @return the unstructuredSearchResultDataHeader
    */
   public UnstructuredSearchResultDataHeader getUnstructuredSearchResultDataHeader () {
      return unstructuredSearchResultDataHeader;
   }

   /**
    * @param unstructuredSearchResultDataHeader the unstructuredSearchResultDataHeader to set
    */
   public void setUnstructuredSearchResultDataHeader (
            UnstructuredSearchResultDataHeader unstructuredSearchResultDataHeader) {
      this.unstructuredSearchResultDataHeader = unstructuredSearchResultDataHeader;
   }

   /**
    * @return the resultItemId
    */
   public Long getResultItemId () {
      return resultItemId;
   }

   /**
    * @param resultItemId the resultItemId to set
    */
   public void setResultItemId (Long resultItemId) {
      this.resultItemId = resultItemId;
   }

}