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


package com.execue.core.common.bean.entity.unstructured;

import java.io.Serializable;
import java.util.List;

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.FeatureValueType;

public class Feature implements Serializable {

   private static final long            serialVersionUID         = 1L;
   private Long                         featureId;
   private String                       featureName;
   private String                       featureDisplayName;
   private String                       featureSymbol;
   private FeatureValueType             featureValueType;
   private Long                         featureBedId;
   private Long                         contextId;
   private transient List<FeatureRange> featureRanges;
   private transient List<FeatureValue> featureValues;
   private transient Long               featureCount;
   private transient CheckType          selected                 = CheckType.NO;
   private CheckType                    locationBased            = CheckType.NO;
   private CheckType                    multiValued              = CheckType.NO;
   private CheckType                    multiValuedGlobalPenalty = CheckType.NO;

   /**
    * @return the selected
    */
   public CheckType getSelected () {
      return selected;
   }

   /**
    * @param selected
    *           the selected to set
    */
   public void setSelected (CheckType selected) {
      this.selected = selected;
   }

   public Long getFeatureId () {
      return featureId;
   }

   public void setFeatureId (Long featureId) {
      this.featureId = featureId;
   }

   public String getFeatureName () {
      return featureName;
   }

   public void setFeatureName (String featureName) {
      this.featureName = featureName;
   }

   /**
    * @return the featureCount
    */
   public Long getFeatureCount () {
      return featureCount;
   }

   /**
    * @param featureCount
    *           the featureCount to set
    */
   public void setFeatureCount (Long featureCount) {
      this.featureCount = featureCount;
   }

   /**
    * @return the featureRanges
    */
   public List<FeatureRange> getFeatureRanges () {
      return featureRanges;
   }

   /**
    * @param featureRanges
    *           the featureRanges to set
    */
   public void setFeatureRanges (List<FeatureRange> featureRanges) {
      this.featureRanges = featureRanges;
   }

   public FeatureValueType getFeatureValueType () {
      return featureValueType;
   }

   public void setFeatureValueType (FeatureValueType featureValueType) {
      this.featureValueType = featureValueType;
   }

   public List<FeatureValue> getFeatureValues () {
      return featureValues;
   }

   public void setFeatureValues (List<FeatureValue> featureValues) {
      this.featureValues = featureValues;
   }

   public String getFeatureDisplayName () {
      return featureDisplayName;
   }

   public void setFeatureDisplayName (String featureDisplayName) {
      this.featureDisplayName = featureDisplayName;
   }

   /**
    * @return the featureSymbol
    */
   public String getFeatureSymbol () {
      return featureSymbol;
   }

   /**
    * @param featureSymbol
    *           the featureSymbol to set
    */
   public void setFeatureSymbol (String featureSymbol) {
      this.featureSymbol = featureSymbol;
   }

   /**
    * @return the featureBedId
    */
   public Long getFeatureBedId () {
      return featureBedId;
   }

   /**
    * @param featureBedId
    *           the featureBedId to set
    */
   public void setFeatureBedId (Long featureBedId) {
      this.featureBedId = featureBedId;
   }

   /**
    * @return the contextId
    */
   public Long getContextId () {
      return contextId;
   }

   /**
    * @param contextId
    *           the contextId to set
    */
   public void setContextId (Long contextId) {
      this.contextId = contextId;
   }

   /**
    * @return the locationBased
    */
   public CheckType getLocationBased () {
      return locationBased;
   }

   /**
    * @param locationBased the locationBased to set
    */
   public void setLocationBased (CheckType locationBased) {
      this.locationBased = locationBased;
   }

   /**
    * @return the multiValued
    */
   public CheckType getMultiValued () {
      return multiValued;
   }

   /**
    * @param multiValued the multiValued to set
    */
   public void setMultiValued (CheckType multiValued) {
      this.multiValued = multiValued;
   }

   public CheckType getMultiValuedGlobalPenalty () {
      return multiValuedGlobalPenalty;
   }

   public void setMultiValuedGlobalPenalty (CheckType multiValuedGlobalPenalty) {
      this.multiValuedGlobalPenalty = multiValuedGlobalPenalty;
   }

}
