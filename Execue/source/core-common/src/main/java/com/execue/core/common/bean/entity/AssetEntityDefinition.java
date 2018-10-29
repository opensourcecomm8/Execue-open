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

import com.execue.core.common.type.AssetEntityType;

/**
 * This class represents the AssetEntity object.
 * 
 * @author Vishay
 * @version 1.0
 * @since 09/01/09
 */
public class AssetEntityDefinition implements java.io.Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private Asset             asset;
   private Tabl              tabl;
   private Colum             colum;
   private Membr             membr;
   private Long              popularity;
   private Set<Mapping>      mappings;
   private AssetEntityType   entityType;

   public Set<Mapping> getMappings () {
      return mappings;
   }

   public void setMappings (Set<Mapping> mappings) {
      this.mappings = mappings;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

   public Tabl getTabl () {
      return tabl;
   }

   public void setTabl (Tabl tabl) {
      this.tabl = tabl;
   }

   public Colum getColum () {
      return colum;
   }

   public void setColum (Colum colum) {
      this.colum = colum;
   }

   public Membr getMembr () {
      return membr;
   }

   public void setMembr (Membr membr) {
      this.membr = membr;
   }

   public AssetEntityType getEntityType () {
      return entityType;
   }

   public void setEntityType (AssetEntityType entityType) {
      this.entityType = entityType;
   }

   
   public Long getPopularity () {
      return popularity;
   }

   
   public void setPopularity (Long popularity) {
      this.popularity = popularity;
   }

}
