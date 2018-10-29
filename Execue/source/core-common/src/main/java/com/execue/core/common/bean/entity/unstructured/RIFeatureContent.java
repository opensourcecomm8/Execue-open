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

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.DisplayableFeatureAlignmentType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.OrderEntityType;

/**
 * @author Vishay
 */
public class RIFeatureContent implements Serializable {

   private Long                            id;
   private Long                            contextId;
   private Long                            featureId;
   private String                          featureName;
   private String                          featureDisplayName;
   private String                          featureSymbol;
   private FeatureValueType                featureValueType;
   private Long                            featureBedId;

   private CheckType                       locationBased            = CheckType.NO;
   private CheckType                       multiValued              = CheckType.NO;
   private CheckType                       multiValuedGlobalPenalty = CheckType.NO;

   private String                          fieldName;

   private CheckType                       displayable              = CheckType.NO;
   private String                          displayableColumnName;
   private Integer                         displayableDisplayOrder  = 1;
   private CheckType                       sortable                 = CheckType.NO;
   private OrderEntityType                 defaultSortOrder         = OrderEntityType.ASCENDING;
   private DisplayableFeatureAlignmentType displayableFeatureAlignmentType;
   private CheckType                       dataHeader               = CheckType.NO;

   private CheckType                       facet                    = CheckType.NO;
   private Integer                         facetDisplayOrder        = 1;
   private String                          facetDependency;

   private Double                          rangeDefaultValue;
   private Double                          rangeMinimumValue;
   private Double                          rangeMaximumValue;
   private Double                          rangeMinimumImpactValue;

   public Long getContextId () {
      return contextId;
   }

   public void setContextId (Long contextId) {
      this.contextId = contextId;
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

   public String getFeatureDisplayName () {
      return featureDisplayName;
   }

   public void setFeatureDisplayName (String featureDisplayName) {
      this.featureDisplayName = featureDisplayName;
   }

   public String getFeatureSymbol () {
      return featureSymbol;
   }

   public void setFeatureSymbol (String featureSymbol) {
      this.featureSymbol = featureSymbol;
   }

   public FeatureValueType getFeatureValueType () {
      return featureValueType;
   }

   public void setFeatureValueType (FeatureValueType featureValueType) {
      this.featureValueType = featureValueType;
   }

   public Long getFeatureBedId () {
      return featureBedId;
   }

   public void setFeatureBedId (Long featureBedId) {
      this.featureBedId = featureBedId;
   }

   public CheckType getLocationBased () {
      return locationBased;
   }

   public void setLocationBased (CheckType locationBased) {
      this.locationBased = locationBased;
   }

   public CheckType getMultiValued () {
      return multiValued;
   }

   public void setMultiValued (CheckType multiValued) {
      this.multiValued = multiValued;
   }

   public CheckType getMultiValuedGlobalPenalty () {
      return multiValuedGlobalPenalty;
   }

   public void setMultiValuedGlobalPenalty (CheckType multiValuedGlobalPenalty) {
      this.multiValuedGlobalPenalty = multiValuedGlobalPenalty;
   }

   public CheckType getDisplayable () {
      return displayable;
   }

   public void setDisplayable (CheckType displayable) {
      this.displayable = displayable;
   }

   public String getDisplayableColumnName () {
      return displayableColumnName;
   }

   public void setDisplayableColumnName (String displayableColumnName) {
      this.displayableColumnName = displayableColumnName;
   }

   public Integer getDisplayableDisplayOrder () {
      return displayableDisplayOrder;
   }

   public void setDisplayableDisplayOrder (Integer displayableDisplayOrder) {
      this.displayableDisplayOrder = displayableDisplayOrder;
   }

   public CheckType getSortable () {
      return sortable;
   }

   public void setSortable (CheckType sortable) {
      this.sortable = sortable;
   }

   public OrderEntityType getDefaultSortOrder () {
      return defaultSortOrder;
   }

   public void setDefaultSortOrder (OrderEntityType defaultSortOrder) {
      this.defaultSortOrder = defaultSortOrder;
   }

   public CheckType getDataHeader () {
      return dataHeader;
   }

   public void setDataHeader (CheckType dataHeader) {
      this.dataHeader = dataHeader;
   }

   public CheckType getFacet () {
      return facet;
   }

   public void setFacet (CheckType facet) {
      this.facet = facet;
   }

   public Integer getFacetDisplayOrder () {
      return facetDisplayOrder;
   }

   public void setFacetDisplayOrder (Integer facetDisplayOrder) {
      this.facetDisplayOrder = facetDisplayOrder;
   }

   public String getFacetDependency () {
      return facetDependency;
   }

   public void setFacetDependency (String facetDependency) {
      this.facetDependency = facetDependency;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the displayableFeatureAlignmentType
    */
   public DisplayableFeatureAlignmentType getDisplayableFeatureAlignmentType () {
      return displayableFeatureAlignmentType;
   }

   /**
    * @param displayableFeatureAlignmentType
    *           the displayableFeatureAlignmentType to set
    */
   public void setDisplayableFeatureAlignmentType (DisplayableFeatureAlignmentType displayableFeatureAlignmentType) {
      this.displayableFeatureAlignmentType = displayableFeatureAlignmentType;
   }

   public String getFieldName () {
      return fieldName;
   }

   public void setFieldName (String fieldName) {
      this.fieldName = fieldName;
   }

   public Double getRangeDefaultValue () {
      return rangeDefaultValue;
   }

   public void setRangeDefaultValue (Double rangeDefaultValue) {
      this.rangeDefaultValue = rangeDefaultValue;
   }

   public Double getRangeMinimumValue () {
      return rangeMinimumValue;
   }

   public void setRangeMinimumValue (Double rangeMinimumValue) {
      this.rangeMinimumValue = rangeMinimumValue;
   }

   public Double getRangeMaximumValue () {
      return rangeMaximumValue;
   }

   public void setRangeMaximumValue (Double rangeMaximumValue) {
      this.rangeMaximumValue = rangeMaximumValue;
   }

   public Double getRangeMinimumImpactValue () {
      return rangeMinimumImpactValue;
   }

   public void setRangeMinimumImpactValue (Double rangeMinimumImpactValue) {
      this.rangeMinimumImpactValue = rangeMinimumImpactValue;
   }

}
