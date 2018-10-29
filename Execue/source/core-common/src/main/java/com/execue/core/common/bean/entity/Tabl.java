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

import java.util.Set;

import com.execue.core.common.bean.IAssetEntity;
import com.execue.core.common.bean.ISecurityBean;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.LookupType;

/**
 * This class represents the Tabl object.
 * 
 * @author Vishay
 * @version 1.0
 * @since 12/01/09
 */
public class Tabl implements java.io.Serializable, IAssetEntity, ISecurityBean {

   private static final long          serialVersionUID                     = 1L;
   private Long                       id;
   private String                     name;
   private Asset                      ownerAsset;
   private String                     actualName;
   private transient String           actualTableDisplayName;
   private String                     description;
   private String                     displayName;
   private String                     alias;
   private LookupType                 lookupType                           = LookupType.None;
   private String                     lookupValueColumn;                                     // need to change it
   // LookupColumnType
   private String                     lookupDescColumn;
   private String                     lowerLimitColumn;
   private String                     upperLimitColumn;
   private String                     parentTable;
   private String                     parentColumn;
   private CheckType                  aggregated                           = CheckType.NO;
   private CheckType                  virtual                              = CheckType.NO;
   private Set<AssetEntityDefinition> assetEntityDefinitions;
   private String                     owner;
   private Long                       parentTableId;
   private CheckType                  eligibleDefaultMetric                = CheckType.NO;
   private CheckType                  indicator                            = CheckType.NO;
   private CheckType                  virtualTableDescColumnExistsOnSource = CheckType.YES;

   public Long getParentTableId () {
      return parentTableId;
   }

   public void setParentTableId (Long parentTableId) {
      this.parentTableId = parentTableId;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getActualName () {
      return actualName;
   }

   public void setActualName (String actualName) {
      this.actualName = actualName;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public String getAlias () {
      return alias;
   }

   public void setAlias (String alias) {
      this.alias = alias;
   }

   public String getLookupValueColumn () {
      return lookupValueColumn;
   }

   public void setLookupValueColumn (String lookupValueColumn) {
      this.lookupValueColumn = lookupValueColumn;
   }

   public String getLookupDescColumn () {
      return lookupDescColumn;
   }

   public void setLookupDescColumn (String lookupDescColumn) {
      this.lookupDescColumn = lookupDescColumn;
   }

   public String getLowerLimitColumn () {
      return lowerLimitColumn;
   }

   public void setLowerLimitColumn (String lowerLimitColumn) {
      this.lowerLimitColumn = lowerLimitColumn;
   }

   public String getUpperLimitColumn () {
      return upperLimitColumn;
   }

   public void setUpperLimitColumn (String upperLimitColumn) {
      this.upperLimitColumn = upperLimitColumn;
   }

   public String getParentTable () {
      return parentTable;
   }

   public void setParentTable (String parentTable) {
      this.parentTable = parentTable;
   }

   public String getParentColumn () {
      return parentColumn;
   }

   public void setParentColumn (String parentColumn) {
      this.parentColumn = parentColumn;
   }

   public CheckType getAggregated () {
      return aggregated;
   }

   public void setAggregated (CheckType aggregated) {
      this.aggregated = aggregated;
   }

   public CheckType getVirtual () {
      return virtual;
   }

   public void setVirtual (CheckType virtual) {
      this.virtual = virtual;
   }

   public Set<AssetEntityDefinition> getAssetEntityDefinitions () {
      return assetEntityDefinitions;
   }

   public void setAssetEntityDefinitions (Set<AssetEntityDefinition> assetEntityDefinitions) {
      this.assetEntityDefinitions = assetEntityDefinitions;
   }

   public Asset getOwnerAsset () {
      return ownerAsset;
   }

   public void setOwnerAsset (Asset ownerAsset) {
      this.ownerAsset = ownerAsset;
   }

   public LookupType getLookupType () {
      return lookupType;
   }

   public void setLookupType (LookupType lookupType) {
      this.lookupType = lookupType;
   }

   public String getOwner () {
      return owner;
   }

   public void setOwner (String owner) {
      this.owner = owner;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals (Object obj) {
      if (obj instanceof Tabl) {
         return this.getId().equals(((Tabl) obj).getId());
      }
      return false;
   }

   /**
    * @return the displayName
    */
   public String getDisplayName () {
      return displayName;
   }

   /**
    * @param displayName
    *           the displayName to set
    */
   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

   public CheckType getEligibleDefaultMetric () {
      return eligibleDefaultMetric;
   }

   public void setEligibleDefaultMetric (CheckType eligibleDefaultMetric) {
      this.eligibleDefaultMetric = eligibleDefaultMetric;
   }

   public String getActualTableDisplayName () {
      return actualTableDisplayName;
   }

   public void setActualTableDisplayName (String actualTableDisplayName) {
      this.actualTableDisplayName = actualTableDisplayName;
   }

   public CheckType getIndicator () {
      return indicator;
   }

   public void setIndicator (CheckType indicator) {
      this.indicator = indicator;
   }

   public CheckType getVirtualTableDescColumnExistsOnSource () {
      return virtualTableDescColumnExistsOnSource;
   }

   public void setVirtualTableDescColumnExistsOnSource (CheckType virtualTableDescColumnExistsOnSource) {
      this.virtualTableDescColumnExistsOnSource = virtualTableDescColumnExistsOnSource;
   }

}
