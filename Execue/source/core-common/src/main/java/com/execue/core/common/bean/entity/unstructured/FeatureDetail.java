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
import com.execue.core.common.type.FeatureDetailType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.OrderEntityType;

/**
 * This object represent mapping between feature and column.
 * 
 * @author Jitendra
 * @version 1.0
 * @since 25/10/11
 */

public class FeatureDetail implements Serializable {

   private static final long               serialVersionUID = 1L;

   private Long                            id;
   private Long                            contextId;
   private Long                            featureId;
   private String                          columnName;
   private int                             displayOrder     = 1;
   private CheckType                       sortable         = CheckType.NO;
   private OrderEntityType                 defaultSortOrder = OrderEntityType.ASCENDING;
   private FeatureDetailType               featureDetailType;
   private DisplayableFeatureAlignmentType diplayableFeatureAlignmentType;
   private CheckType                       dataHeader       = CheckType.NO;
   private transient String                featureName;
   private transient String                featureDisplayName;
   private transient String                featureSymbol;
   private transient FeatureValueType      featureValueType;
   private String                          facetDependency;

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
    * @return the featureId
    */
   public Long getFeatureId () {
      return featureId;
   }

   /**
    * @param featureId
    *           the featureId to set
    */
   public void setFeatureId (Long featureId) {
      this.featureId = featureId;
   }

   /**
    * @return the columnName
    */
   public String getColumnName () {
      return columnName;
   }

   /**
    * @param columnName
    *           the columnName to set
    */
   public void setColumnName (String columnName) {
      this.columnName = columnName;
   }

   /**
    * @return the displayOrder
    */
   public int getDisplayOrder () {
      return displayOrder;
   }

   /**
    * @param displayOrder
    *           the displayOrder to set
    */
   public void setDisplayOrder (int displayOrder) {
      this.displayOrder = displayOrder;
   }

   /**
    * @return the sortable
    */
   public CheckType getSortable () {
      return sortable;
   }

   /**
    * @param sortable
    *           the sortable to set
    */
   public void setSortable (CheckType sortable) {
      this.sortable = sortable;
   }

   /**
    * @return the featureName
    */
   public String getFeatureName () {
      return featureName;
   }

   /**
    * @param featureName
    *           the featureName to set
    */
   public void setFeatureName (String featureName) {
      this.featureName = featureName;
   }

   /**
    * @return the featureDisplayName
    */
   public String getFeatureDisplayName () {
      return featureDisplayName;
   }

   /**
    * @param featureDisplayName
    *           the featureDisplayName to set
    */
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
    * @return the featureValueType
    */
   public FeatureValueType getFeatureValueType () {
      return featureValueType;
   }

   /**
    * @param featureValueType
    *           the featureValueType to set
    */
   public void setFeatureValueType (FeatureValueType featureValueType) {
      this.featureValueType = featureValueType;
   }

   /**
    * @return the defaultSortOrder
    */
   public OrderEntityType getDefaultSortOrder () {
      return defaultSortOrder;
   }

   /**
    * @param defaultSortOrder
    *           the defaultSortOrder to set
    */
   public void setDefaultSortOrder (OrderEntityType defaultSortOrder) {
      this.defaultSortOrder = defaultSortOrder;
   }

   /**
    * @return the diplayableFeatureAlignmentType
    */
   public DisplayableFeatureAlignmentType getDiplayableFeatureAlignmentType () {
      return diplayableFeatureAlignmentType;
   }

   /**
    * @param diplayableFeatureAlignmentType
    *           the diplayableFeatureAlignmentType to set
    */
   public void setDiplayableFeatureAlignmentType (DisplayableFeatureAlignmentType diplayableFeatureAlignmentType) {
      this.diplayableFeatureAlignmentType = diplayableFeatureAlignmentType;
   }

   public CheckType getDataHeader () {
      return dataHeader;
   }

   public void setDataHeader (CheckType dataHeader) {
      this.dataHeader = dataHeader;
   }

   /**
    * @return the facetDependency
    */
   public String getFacetDependency () {
      return facetDependency;
   }

   /**
    * @param facetDependency
    *           the facetDependency to set
    */
   public void setFacetDependency (String facetDependency) {
      this.facetDependency = facetDependency;
   }

   public FeatureDetailType getFeatureDetailType () {
      return featureDetailType;
   }

   public void setFeatureDetailType (FeatureDetailType featureDetailType) {
      this.featureDetailType = featureDetailType;
   }

}
