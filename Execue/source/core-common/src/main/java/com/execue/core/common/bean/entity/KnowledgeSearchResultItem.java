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


package com.execue.core.common.bean.entity;

/**
 * @author anujit Abhijit
 * @since Jan 26, 2010 Time: 11:17:18 AM
 */

public class KnowledgeSearchResultItem extends InstanceTripleData {

   private double matchWeight;
   private String contentSource;
   private int    matchedVariationSubType;
   private int    searchType;

   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder(1);
      sb.append(sourceInstanceName).append(" ").append(relationName).append(" ").append(destinationInstanceName)
               .append(" ").append(matchWeight).append(" ").append(contentSource).append(" ").append(
                        matchedVariationSubType).append(" ").append(searchType);
      return sb.toString();
   }

   public String getDisplayString () {
      StringBuilder sb = new StringBuilder(1);
      sb.append(sourceInstanceName).append("-").append(relationName).append("-").append(destinationInstanceName)
               .append("\tMatch Weight: ").append(matchWeight);
      return sb.toString();

   }

   public double getMatchWeight () {
      return matchWeight;
   }

   public void setMatchWeight (double matchWeight) {
      this.matchWeight = matchWeight;
   }

   public String getContentSource () {
      return contentSource;
   }

   public void setContentSource (String contentSource) {
      this.contentSource = contentSource;
   }

   public int getMatchedVariationSubType () {
      return matchedVariationSubType;
   }

   public void setMatchedVariationSubType (int matchedVariationSubType) {
      this.matchedVariationSubType = matchedVariationSubType;
   }

   /**
    * @return the searchType
    */
   public int getSearchType () {
      return searchType;
   }

   /**
    * @param searchType
    *           the searchType to set
    */
   public void setSearchType (int searchType) {
      this.searchType = searchType;
   }
}
