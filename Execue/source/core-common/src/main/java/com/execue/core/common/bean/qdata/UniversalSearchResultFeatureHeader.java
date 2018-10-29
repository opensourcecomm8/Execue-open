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


package com.execue.core.common.bean.qdata;

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.DisplayableFeatureAlignmentType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.OrderEntityType;

public class UniversalSearchResultFeatureHeader {

   private String                          id;
   private String                          name;
   private String                          columnName;
   private CheckType                       sortable;
   private String                          format;
   private DisplayableFeatureAlignmentType diplayableFeatureAlignmentType;
   private OrderEntityType                 defaultSortOrder = OrderEntityType.ASCENDING;
   private FeatureValueType                featureValueType;
   private CheckType                       dataHeader       = CheckType.NO;

   /**
    * @return the id
    */
   public String getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (String id) {
      this.id = id;
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
    * @return the columnName
    */
   public String getColumnName () {
      return columnName;
   }

   /**
    * @param columnName the columnName to set
    */
   public void setColumnName (String columnName) {
      this.columnName = columnName;
   }

   /**
    * @return the sortable
    */
   public CheckType getSortable () {
      return sortable;
   }

   /**
    * @param sortable the sortable to set
    */
   public void setSortable (CheckType sortable) {
      this.sortable = sortable;
   }

   /**
    * @return the defaultSortOrder
    */
   public OrderEntityType getDefaultSortOrder () {
      return defaultSortOrder;
   }

   /**
    * @param defaultSortOrder the defaultSortOrder to set
    */
   public void setDefaultSortOrder (OrderEntityType defaultSortOrder) {
      this.defaultSortOrder = defaultSortOrder;
   }

   /**
    * @return the featureValueType
    */
   public FeatureValueType getFeatureValueType () {
      return featureValueType;
   }

   /**
    * @param featureValueType the featureValueType to set
    */
   public void setFeatureValueType (FeatureValueType featureValueType) {
      this.featureValueType = featureValueType;
   }

   /**
    * @return the diplayableFeatureAlignmentType
    */
   public DisplayableFeatureAlignmentType getDiplayableFeatureAlignmentType () {
      return diplayableFeatureAlignmentType;
   }

   /**
    * @param diplayableFeatureAlignmentType the diplayableFeatureAlignmentType to set
    */
   public void setDiplayableFeatureAlignmentType (DisplayableFeatureAlignmentType diplayableFeatureAlignmentType) {
      this.diplayableFeatureAlignmentType = diplayableFeatureAlignmentType;
   }

   /**
    * @return the format
    */
   public String getFormat () {
      return format;
   }

   /**
    * @param format the format to set
    */
   public void setFormat (String format) {
      this.format = format;
   }

   /**
    * @return the dataHeader
    */
   public CheckType getDataHeader () {
      return dataHeader;
   }

   /**
    * @param dataHeader the dataHeader to set
    */
   public void setDataHeader (CheckType dataHeader) {
      this.dataHeader = dataHeader;
   }

}
