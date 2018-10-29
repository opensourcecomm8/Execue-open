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


package com.execue.swi.dataaccess;

import com.execue.dataaccess.swi.dao.IAppDataSourceDAO;
import com.execue.dataaccess.swi.dao.IAssetDAO;
import com.execue.dataaccess.swi.dao.IAssetDetailDAO;
import com.execue.dataaccess.swi.dao.IAssetEntityDefinitionDAO;
import com.execue.dataaccess.swi.dao.IAssetOperationDAO;
import com.execue.dataaccess.swi.dao.IAssetTableColumnConstraintDAO;
import com.execue.dataaccess.swi.dao.IAssetTableColumnDAO;
import com.execue.dataaccess.swi.dao.IAssetTableColumnMemberDAO;
import com.execue.dataaccess.swi.dao.IAssetTableDAO;
import com.execue.dataaccess.swi.dao.IDataSourceDAO;

public abstract class SDXDAOComponents {

   private IAssetDAO                      assetDAO;
   private IAssetEntityDefinitionDAO      assetEntityDefinitionDAO;
   private IAssetOperationDAO             assetOperationDAO;
   private IAssetDetailDAO                assetDetailDAO;
   private IAppDataSourceDAO              appDataSourceDAO;
   private IDataSourceDAO                 dataSourceDAO;
   private IAssetTableDAO                 assetTableDAO;
   private IAssetTableColumnDAO           assetTableColumnDAO;
   private IAssetTableColumnMemberDAO     assetTableColumnMemberDAO;
   private IAssetTableColumnConstraintDAO assetTableColumnConstraintDAO;

   public IAssetDAO getAssetDAO () {
      return assetDAO;
   }

   public void setAssetDAO (IAssetDAO assetDAO) {
      this.assetDAO = assetDAO;
   }

   public IAssetEntityDefinitionDAO getAssetEntityDefinitionDAO () {
      return assetEntityDefinitionDAO;
   }

   public void setAssetEntityDefinitionDAO (IAssetEntityDefinitionDAO assetEntityDefinitionDAO) {
      this.assetEntityDefinitionDAO = assetEntityDefinitionDAO;
   }

   public IAssetDetailDAO getAssetDetailDAO () {
      return assetDetailDAO;
   }

   public void setAssetDetailDAO (IAssetDetailDAO assetDetailDAO) {
      this.assetDetailDAO = assetDetailDAO;
   }

   public IAssetOperationDAO getAssetOperationDAO () {
      return assetOperationDAO;
   }

   public void setAssetOperationDAO (IAssetOperationDAO assetOperationDAO) {
      this.assetOperationDAO = assetOperationDAO;
   }

   public IAppDataSourceDAO getAppDataSourceDAO () {
      return appDataSourceDAO;
   }

   public void setAppDataSourceDAO (IAppDataSourceDAO appDataSourceDAO) {
      this.appDataSourceDAO = appDataSourceDAO;
   }

   public IDataSourceDAO getDataSourceDAO () {
      return dataSourceDAO;
   }

   public void setDataSourceDAO (IDataSourceDAO dataSourceDAO) {
      this.dataSourceDAO = dataSourceDAO;
   }

   public IAssetTableDAO getAssetTableDAO () {
      return assetTableDAO;
   }

   public void setAssetTableDAO (IAssetTableDAO assetTableDAO) {
      this.assetTableDAO = assetTableDAO;
   }

   public IAssetTableColumnDAO getAssetTableColumnDAO () {
      return assetTableColumnDAO;
   }

   public void setAssetTableColumnDAO (IAssetTableColumnDAO assetTableColumnDAO) {
      this.assetTableColumnDAO = assetTableColumnDAO;
   }

   public IAssetTableColumnConstraintDAO getAssetTableColumnConstraintDAO () {
      return assetTableColumnConstraintDAO;
   }

   public void setAssetTableColumnConstraintDAO (IAssetTableColumnConstraintDAO assetTableColumnConstraintDAO) {
      this.assetTableColumnConstraintDAO = assetTableColumnConstraintDAO;
   }

   public IAssetTableColumnMemberDAO getAssetTableColumnMemberDAO () {
      return assetTableColumnMemberDAO;
   }

   public void setAssetTableColumnMemberDAO (IAssetTableColumnMemberDAO assetTableColumnMemberDAO) {
      this.assetTableColumnMemberDAO = assetTableColumnMemberDAO;
   }

}
