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


package com.execue.handler.bean;

import java.util.List;

import com.execue.core.common.type.CheckType;

public class UIAsset {

   private String        name;
   private Long          id;
   private Long          parentAssetId;
   private String        parentAssetName;
   private String        description;
   private List<UITable> tables;
   private CheckType     hasAclPermission = CheckType.NO;
   private String        lastModifiedDate;

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public List<UITable> getTables () {
      return tables;
   }

   public void setTables (List<UITable> tables) {
      this.tables = tables;
   }

   /**
    * @return the parentAssetId
    */
   public Long getParentAssetId () {
      return parentAssetId;
   }

   /**
    * @param parentAssetId the parentAssetId to set
    */
   public void setParentAssetId (Long parentAssetId) {
      this.parentAssetId = parentAssetId;
   }

   /**
    * @return the parentAssetName
    */
   public String getParentAssetName () {
      return parentAssetName;
   }

   /**
    * @param parentAssetName the parentAssetName to set
    */
   public void setParentAssetName (String parentAssetName) {
      this.parentAssetName = parentAssetName;
   }

   /**
    * @return the hasAclPermission
    */
   public CheckType getHasAclPermission () {
      return hasAclPermission;
   }

   /**
    * @param hasAclPermission the hasAclPermission to set
    */
   public void setHasAclPermission (CheckType hasAclPermission) {
      this.hasAclPermission = hasAclPermission;
   }

   
   public String getLastModifiedDate () {
      return lastModifiedDate;
   }

   
   public void setLastModifiedDate (String lastModifiedDate) {
      this.lastModifiedDate = lastModifiedDate;
   }

}
