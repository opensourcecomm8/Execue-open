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


package com.execue.swi.service;

import java.util.List;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetOperationInfo;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.HistoryAssetOperationInfo;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.swi.exception.SDXException;

/**
 * This service acts as a wrapper for the deletion services
 * 
 * @author MurthySN
 * @version 1.0
 * @since 29/01/10
 */

public interface ISDXDeletionService extends ISDXCommonService {

   // Methods for deletion on the SDX side

   public void deleteAsset (Asset asset) throws SDXException;

   public void deleteDataSource (DataSource dataSource) throws SDXException;

   public void deleteMembers (List<Membr> members) throws SDXException;
   
   public void deleteMembersInBatches (Colum column, int batchSize) throws SDXException;

   public void deleteConstraints (List<Constraint> constraints) throws SDXException;

   public void deleteConstraints (Long assetId) throws SDXException;

   public void deleteAssetOperation (AssetOperationInfo assetOperationInfo) throws SDXException;

   public void deleteHistoryAssetOperation (HistoryAssetOperationInfo historyAssetOperationInfo) throws SDXException;

   public void deleteAppDataSourceMappings (Long appId) throws SDXException;
   
   /**
    * Columns are deleted with out considering any other dependencies and child objects
    * 
    * @param columns
    * @throws SDXException
    */
   public void deleteColumnsWithOutConsideringChildEntities (List<Colum> columns) throws SDXException;
   
   /**
    * Tables are deleted with out considering any other dependencies and child objects
    * 
    * @param columns
    * @throws SDXException
    */
   public void deleteTablesWithOutConsideringChildEntities (List<Tabl> tables) throws SDXException;
}
