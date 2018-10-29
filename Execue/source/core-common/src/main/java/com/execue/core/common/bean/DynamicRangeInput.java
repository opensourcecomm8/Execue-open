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
package com.execue.core.common.bean;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.SelectEntity;

/**
 * @author Nitesh
 *
 */
public class DynamicRangeInput {

   private Query        query;
   private Asset        targetAsset;
   private SelectEntity rangeSelectEntity;
   private Long         modelId;
   private Long         assetId;
   private Long         conceptBedId;
   private int          bandCount;
   private Long         sqlCountOfRangeColumn;

   /**
    * @return the query
    */
   public Query getQuery () {
      return query;
   }

   /**
    * @param query the query to set
    */
   public void setQuery (Query query) {
      this.query = query;
   }

   /**
    * @return the targetAsset
    */
   public Asset getTargetAsset () {
      return targetAsset;
   }

   /**
    * @param targetAsset the targetAsset to set
    */
   public void setTargetAsset (Asset targetAsset) {
      this.targetAsset = targetAsset;
   }

   /**
    * @return the rangeSelectEntity
    */
   public SelectEntity getRangeSelectEntity () {
      return rangeSelectEntity;
   }

   /**
    * @param rangeSelectEntity the rangeSelectEntity to set
    */
   public void setRangeSelectEntity (SelectEntity rangeSelectEntity) {
      this.rangeSelectEntity = rangeSelectEntity;
   }

   /**
    * @return the modelId
    */
   public Long getModelId () {
      return modelId;
   }

   /**
    * @param modelId the modelId to set
    */
   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   /**
    * @return the assetId
    */
   public Long getAssetId () {
      return assetId;
   }

   /**
    * @param assetId the assetId to set
    */
   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   /**
    * @return the conceptBedId
    */
   public Long getConceptBedId () {
      return conceptBedId;
   }

   /**
    * @param conceptBedId the conceptBedId to set
    */
   public void setConceptBedId (Long conceptBedId) {
      this.conceptBedId = conceptBedId;
   }

   /**
    * @return the bandCount
    */
   public int getBandCount () {
      return bandCount;
   }

   /**
    * @param bandCount2 the bandCount to set
    */
   public void setBandCount (int bandCount) {
      this.bandCount = bandCount;
   }

   /**
    * @return the sqlCountOfRangeColumn
    */
   public Long getSqlCountOfRangeColumn () {
      return sqlCountOfRangeColumn;
   }

   /**
    * @param sqlCountOfRangeColumn the sqlCountOfRangeColumn to set
    */
   public void setSqlCountOfRangeColumn (Long sqlCountOfRangeColumn) {
      this.sqlCountOfRangeColumn = sqlCountOfRangeColumn;
   }
}