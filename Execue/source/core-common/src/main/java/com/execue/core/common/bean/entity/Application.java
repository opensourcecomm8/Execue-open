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
 * This class represents the Application object.
 */

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.execue.core.common.bean.ISecurityBean;
import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.AppCreationType;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.PublishAssetMode;

public class Application implements Serializable, ISecurityBean {

   private static final long             serialVersionUID = 1L;
   private Long                          id;
   private String                        name;
   private String                        description;
   private AppSourceType                 sourceType       = AppSourceType.STRUCTURED;
   private User                          owner;
   private Date                          createdDate;
   private Date                          modifiedDate;
   private Set<Model>                    models;
   private Set<Asset>                    assets;
   private String                        applicationURL;
   private Long                          popularity;
   private Long                          imageId          = -1L;
   private StatusEnum                    status           = StatusEnum.INACTIVE;
   private PublishAssetMode              publishMode      = PublishAssetMode.NONE;
   private Set<ApplicationExample>       appQueryExamples;
   private Long                          rank;
   private Double                        constantRandomFactor;
   private AppCreationType               creationType     = AppCreationType.NORMAL_PROCESS;
   private CheckType                     associationExist = CheckType.NO;
   private String                        applicationTag;
   private String                        applicationTitle;
   private String                        applicationKey;
   private UnstructuredApplicationDetail unstructuredApplicationDetail;

   /**
    * @return the associationExist
    */
   public CheckType getAssociationExist () {
      return associationExist;
   }

   /**
    * @param associationExist
    *           the associationExist to set
    */
   public void setAssociationExist (CheckType associationExist) {
      this.associationExist = associationExist;
   }

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

   public User getOwner () {
      return owner;
   }

   public void setOwner (User owner) {
      this.owner = owner;
   }

   public Set<Model> getModels () {
      return models;
   }

   public void setModels (Set<Model> models) {
      this.models = models;
   }

   public Set<Asset> getAssets () {
      return assets;
   }

   public void setAssets (Set<Asset> assets) {
      this.assets = assets;
   }

   public Date getCreatedDate () {
      return createdDate;
   }

   public void setCreatedDate (Date createdDate) {
      this.createdDate = createdDate;
   }

   public Date getModifiedDate () {
      return modifiedDate;
   }

   public void setModifiedDate (Date modifiedDate) {
      this.modifiedDate = modifiedDate;
   }

   public String getApplicationURL () {
      return applicationURL;
   }

   public void setApplicationURL (String applicationURL) {
      this.applicationURL = applicationURL;
   }

   public Long getPopularity () {
      return popularity;
   }

   public void setPopularity (Long popularity) {
      this.popularity = popularity;
   }

   public StatusEnum getStatus () {
      return status;
   }

   public void setStatus (StatusEnum status) {
      this.status = status;
   }

   public Long getImageId () {
      return imageId;
   }

   public void setImageId (Long imageId) {
      this.imageId = imageId;
   }

   public Set<ApplicationExample> getAppQueryExamples () {
      return appQueryExamples;
   }

   public void setAppQueryExamples (Set<ApplicationExample> appQueryExamples) {
      this.appQueryExamples = appQueryExamples;
   }

   public Long getRank () {
      return rank;
   }

   public void setRank (Long rank) {
      this.rank = rank;
   }

   public Double getConstantRandomFactor () {
      return constantRandomFactor;
   }

   public void setConstantRandomFactor (Double constantRandomFactor) {
      this.constantRandomFactor = constantRandomFactor;
   }

   /**
    * @return the applicationType
    */
   public AppSourceType getSourceType () {
      return sourceType;
   }

   /**
    * @param applicationType
    *           the applicationType to set
    */
   public void setSourceType (AppSourceType sourceType) {
      this.sourceType = sourceType;
   }

   public AppCreationType getCreationType () {
      return creationType;
   }

   public void setCreationType (AppCreationType creationType) {
      this.creationType = creationType;
   }

   public String getApplicationTag () {
      return applicationTag;
   }

   public void setApplicationTag (String applicationTag) {
      this.applicationTag = applicationTag;
   }

   public String getApplicationTitle () {
      return applicationTitle;
   }

   public void setApplicationTitle (String applicationTitle) {
      this.applicationTitle = applicationTitle;
   }

   public String getApplicationKey () {
      return applicationKey;
   }

   public void setApplicationKey (String applicationKey) {
      this.applicationKey = applicationKey;
   }

   /**
    * @return the unstructuredApplicationDetail
    */
   public UnstructuredApplicationDetail getUnstructuredApplicationDetail () {
      return unstructuredApplicationDetail;
   }

   /**
    * @param unstructuredApplicationDetail the unstructuredApplicationDetail to set
    */
   public void setUnstructuredApplicationDetail (UnstructuredApplicationDetail unstructuredApplicationDetail) {
      this.unstructuredApplicationDetail = unstructuredApplicationDetail;
   }

}
