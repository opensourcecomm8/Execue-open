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
package com.execue.core.common.bean.qdata;

import java.io.Serializable;

/**
 * @author Nitesh
 */
public class RIInstanceTripleDefinition implements Serializable {

   private Long    id;
   private Long    beId1;
   private Long    beId2;
   private Long    beId3;
   private String  beId1Name;
   private String  beId2Name;
   private String  beId3Name;
   private Integer beType;
   private Integer variationType;
   private Integer variationSubType;
   private Double  matchWeight;
   private Long    instanceTripeId;
   private Long    sourceBEID;
   private Long    relationBEID;
   private Long    destinationBEID;
   private Integer searchType;
   private String  contentSource;

   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder(1);
      sb.append(id).append(" ").append(instanceTripeId).append(" ").append(matchWeight);
      return sb.toString();
   }

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the beId1
    */
   public Long getBeId1 () {
      return beId1;
   }

   /**
    * @param beId1
    *           the beId1 to set
    */
   public void setBeId1 (Long beId1) {
      this.beId1 = beId1;
   }

   /**
    * @return the beId2
    */
   public Long getBeId2 () {
      return beId2;
   }

   /**
    * @param beId2
    *           the beId2 to set
    */
   public void setBeId2 (Long beId2) {
      this.beId2 = beId2;
   }

   /**
    * @return the beId3
    */
   public Long getBeId3 () {
      return beId3;
   }

   /**
    * @param beId3
    *           the beId3 to set
    */
   public void setBeId3 (Long beId3) {
      this.beId3 = beId3;
   }

   /**
    * @return the variationType
    */
   public Integer getVariationType () {
      return variationType;
   }

   /**
    * @param variationType
    *           the variationType to set
    */
   public void setVariationType (Integer variationType) {
      this.variationType = variationType;
   }

   public Integer getVariationSubType () {
      return variationSubType;
   }

   public void setVariationSubType (Integer variationSubType) {
      this.variationSubType = variationSubType;
   }

   /**
    * @return the matchWeight
    */
   public Double getMatchWeight () {
      return matchWeight;
   }

   /**
    * @param matchWeight
    *           the matchWeight to set
    */
   public void setMatchWeight (Double matchWeight) {
      this.matchWeight = matchWeight;
   }

   /**
    * @return
    */
   public Long getInstanceTripeId () {
      return instanceTripeId;
   }

   /**
    * @param instanceTripeId
    */
   public void setInstanceTripeId (Long instanceTripeId) {
      this.instanceTripeId = instanceTripeId;
   }

   /**
    * @return
    */
   public Integer getBeType () {
      return beType;
   }

   /**
    * @param beType
    */
   public void setBeType (Integer beType) {
      this.beType = beType;
   }

   /**
    * @return the beId1Name
    */
   public String getBeId1Name () {
      return beId1Name;
   }

   /**
    * @param beId1Name
    *           the beId1Name to set
    */
   public void setBeId1Name (String beId1Name) {
      this.beId1Name = beId1Name;
   }

   /**
    * @return the beId2Name
    */
   public String getBeId2Name () {
      return beId2Name;
   }

   /**
    * @param beId2Name
    *           the beId2Name to set
    */
   public void setBeId2Name (String beId2Name) {
      this.beId2Name = beId2Name;
   }

   /**
    * @return the beId3Name
    */
   public String getBeId3Name () {
      return beId3Name;
   }

   /**
    * @param beId3Name
    *           the beId3Name to set
    */
   public void setBeId3Name (String beId3Name) {
      this.beId3Name = beId3Name;
   }

   public Long getSourceBEID () {
      return sourceBEID;
   }

   public void setSourceBEID (Long sourceBEID) {
      this.sourceBEID = sourceBEID;
   }

   public Long getRelationBEID () {
      return relationBEID;
   }

   public void setRelationBEID (Long relationBEID) {
      this.relationBEID = relationBEID;
   }

   public Long getDestinationBEID () {
      return destinationBEID;
   }

   public void setDestinationBEID (Long destinationBEID) {
      this.destinationBEID = destinationBEID;
   }

   // Utility Methods

   public boolean isOnlyRelationTriple () {
      return (beType == 2);
   }

   public boolean isBeId1SourceInstanceTriple () {
      return ((beType == 1) || (beType == 4) || (beType == 6) || (beType == 9) || (beType == 12) || (beType == 14));
   }

   public boolean isBeId2DestInstanceTriple () {
      return ((beId2 != null) && (beId2 != 0) && ((beType == 5) || (beType == 6) || (beType == 10)));
   }

   public boolean isBeId3DestInstanceTriple () {
      return ((beId3 != null) && (beId3 != 0) && ((beType == 12) || (beType == 13)));
   }

   public boolean isRelationExist () {
      return ((beType == 2) || (beType == 5) || (beType == 7) || (beType == 8) || (beType == 12) || (beType == 13)
               || (beType == 14) || (beType == 15));
   }

   public boolean isBeId1Relation () {
      return ((beType == 2) || (beType == 5) || (beType == 8));
   }

   public boolean isBeId2Relation () {
      return ((beType == 7) || (beType == 12) || (beType == 13) || (beType == 14) || (beType == 15));
   }

   /**
    * @return the searchType
    */
   public Integer getSearchType () {
      return searchType;
   }

   /**
    * @param searchType
    *           the searchType to set
    */
   public void setSearchType (Integer searchType) {
      this.searchType = searchType;
   }

   /**
    * @return the contentSource
    */
   public String getContentSource () {
      return contentSource;
   }

   /**
    * @param contentSource
    *           the contentSource to set
    */
   public void setContentSource (String contentSource) {
      this.contentSource = contentSource;
   }
}