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
import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.type.AssetOwnerType;
import com.execue.core.common.type.AssetSubType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.common.type.PublishedFileType;

/**
 * This class represents the Asset object.
 * 
 * @author Vishay
 * @version 1.0
 * @since 09/01/09
 */
public class Asset implements java.io.Serializable, IAssetEntity, ISecurityBean {

   private static final long          serialVersionUID      = 1L;
   private Long                       id;
   private Long                       baseAssetId;
   private String                     name;
   private String                     description;
   private String                     displayName;
   private Double                     priority;
   private AssetOwnerType             ownerType             = AssetOwnerType.Client;
   private AssetType                  type                  = AssetType.Relational;
   private AssetSubType               subType;
   private DataSource                 dataSource;
   private StatusEnum                 status                = StatusEnum.INACTIVE;
   private Set<AssetEntityDefinition> assetEntityDefinitions;
   private Set<Join>                  joins;
   private Set<JoinDefinition>        joinDefinitions;
   private Application                application;
   private PublishedFileType          originType            = PublishedFileType.RDBMS;
   private PublishAssetMode           publishMode           = PublishAssetMode.NONE;
   private CheckType                  queryExecutionAllowed = CheckType.YES;

   public PublishAssetMode getPublishMode () {
      return publishMode;
   }

   public void setPublishMode (PublishAssetMode publishMode) {
      this.publishMode = publishMode;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getBaseAssetId () {
      return baseAssetId;
   }

   public void setBaseAssetId (Long baseAssetId) {
      this.baseAssetId = baseAssetId;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public String getDisplayName () {
      return displayName;
   }

   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

   public DataSource getDataSource () {
      return dataSource;
   }

   public void setDataSource (DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public Set<AssetEntityDefinition> getAssetEntityDefinitions () {
      return assetEntityDefinitions;
   }

   public void setAssetEntityDefinitions (Set<AssetEntityDefinition> assetEntityDefinitions) {
      this.assetEntityDefinitions = assetEntityDefinitions;
   }

   public Set<Join> getJoins () {
      return joins;
   }

   public void setJoins (Set<Join> joins) {
      this.joins = joins;
   }

   public Set<JoinDefinition> getJoinDefinitions () {
      return joinDefinitions;
   }

   public void setJoinDefinitions (Set<JoinDefinition> joinDefinitions) {
      this.joinDefinitions = joinDefinitions;
   }

   @Override
   public boolean equals (Object obj) {
      Asset asset = (Asset) obj;
      return this.id.longValue() == asset.getId().longValue();
   }

   @Override
   public int hashCode () {
      return 100;
   }

   public StatusEnum getStatus () {
      return status;
   }

   public void setStatus (StatusEnum status) {
      this.status = status;
   }

   public AssetOwnerType getOwnerType () {
      return ownerType;
   }

   public void setOwnerType (AssetOwnerType ownerType) {
      this.ownerType = ownerType;
   }

   public AssetType getType () {
      return type;
   }

   public void setType (AssetType type) {
      this.type = type;
   }

   public AssetSubType getSubType () {
      return subType;
   }

   public void setSubType (AssetSubType subType) {
      this.subType = subType;
   }

   public Double getPriority () {
      return priority;
   }

   public void setPriority (Double priority) {
      this.priority = priority;
   }

   public Application getApplication () {
      return application;
   }

   public void setApplication (Application application) {
      this.application = application;
   }

   public PublishedFileType getOriginType () {
      return originType;
   }

   public void setOriginType (PublishedFileType originType) {
      this.originType = originType;
   }

   public CheckType getQueryExecutionAllowed () {
      return queryExecutionAllowed;
   }

   public void setQueryExecutionAllowed (CheckType queryExecutionAllowed) {
      this.queryExecutionAllowed = queryExecutionAllowed;
   }

}
