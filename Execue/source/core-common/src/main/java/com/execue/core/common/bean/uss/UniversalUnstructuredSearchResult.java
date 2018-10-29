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
package com.execue.core.common.bean.uss;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.qdata.UniversalSearchResultFeatureHeader;

/**
 * @author Nihar
 */
public class UniversalUnstructuredSearchResult {

   private List<UnstructuredSearchResultItem>       unstructuredSearchResultItem;
   private int                                      totalCount;
   private int                                      perfectMatchCount;
   private int                                      mightMatchCount;
   private int                                      partialMatchCount;
   private Long                                     userQueryId;
   private Long                                     applicationId;
   private List<UniversalSearchResultFeatureHeader> universalSearchResultFeatureHeaders;

   /**
    * @return the dbPediaUnStructuedResults
    */
   public List<UnstructuredSearchResultItem> getUnstructuredSearchResultItem () {
      if (unstructuredSearchResultItem == null) {
         return new ArrayList<UnstructuredSearchResultItem>(1);
      }
      return unstructuredSearchResultItem;
   }

   /**
    * @param dbPediaUnStructuedResults
    *           the dbPediaUnStructuedResults to set
    */
   public void setUnstructuredSearchResultItem (List<UnstructuredSearchResultItem> unstructuredSearchResultItem) {
      this.unstructuredSearchResultItem = unstructuredSearchResultItem;
   }

   /**
    * @return the totalCount
    */
   public int getTotalCount () {
      return totalCount;
   }

   /**
    * @param totalCount
    *           the totalCount to set
    */
   public void setTotalCount (int totalCount) {
      this.totalCount = totalCount;
   }

   /**
    * @return the userQueryId
    */
   public Long getUserQueryId () {
      return userQueryId;
   }

   /**
    * @param userQueryId
    *           the userQueryId to set
    */
   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   /**
    * @return the perfectMatchCount
    */
   public int getPerfectMatchCount () {
      return perfectMatchCount;
   }

   /**
    * @param perfectMatchCount
    *           the perfectMatchCount to set
    */
   public void setPerfectMatchCount (int perfectMatchCount) {
      this.perfectMatchCount = perfectMatchCount;
   }

   /**
    * @return the mightMatchCount
    */
   public int getMightMatchCount () {
      return mightMatchCount;
   }

   /**
    * @param mightMatchCount
    *           the mightMatchCount to set
    */
   public void setMightMatchCount (int mightMatchCount) {
      this.mightMatchCount = mightMatchCount;
   }

   /**
    * @return the partialMatchCount
    */
   public int getPartialMatchCount () {
      return partialMatchCount;
   }

   /**
    * @param partialMatchCount
    *           the partialMatchCount to set
    */
   public void setPartialMatchCount (int partialMatchCount) {
      this.partialMatchCount = partialMatchCount;
   }

   /**
    * @return the universalSearchResultFeatureHeaders
    */
   public List<UniversalSearchResultFeatureHeader> getUniversalSearchResultFeatureHeaders () {
      return universalSearchResultFeatureHeaders;
   }

   /**
    * @param universalSearchResultFeatureHeaders
    *           the universalSearchResultFeatureHeaders to set
    */
   public void setUniversalSearchResultFeatureHeaders (
            List<UniversalSearchResultFeatureHeader> universalSearchResultFeatureHeaders) {
      this.universalSearchResultFeatureHeaders = universalSearchResultFeatureHeaders;
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }
}
