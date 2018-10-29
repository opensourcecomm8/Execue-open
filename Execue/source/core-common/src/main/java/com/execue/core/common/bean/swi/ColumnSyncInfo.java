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


package com.execue.core.common.bean.swi;

import java.io.Serializable;
import java.util.List;

/**
 * This bean represents the colum level sync information
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */
public class ColumnSyncInfo implements Serializable {

   private AssetOperationTable        tabl;
   private List<AssetOperationColumn> addedColumns;
   private List<AssetOperationColumn> deletedColumns;
   private List<ColumnUpdationInfo>   updatedColumns;

   public List<ColumnUpdationInfo> getUpdatedColumns () {
      return updatedColumns;
   }

   public void setUpdatedColumns (List<ColumnUpdationInfo> updatedColumns) {
      this.updatedColumns = updatedColumns;
   }

   
   public AssetOperationTable getTabl () {
      return tabl;
   }

   
   public void setTabl (AssetOperationTable tabl) {
      this.tabl = tabl;
   }

   
   public List<AssetOperationColumn> getAddedColumns () {
      return addedColumns;
   }

   
   public void setAddedColumns (List<AssetOperationColumn> addedColumns) {
      this.addedColumns = addedColumns;
   }

   
   public List<AssetOperationColumn> getDeletedColumns () {
      return deletedColumns;
   }

   
   public void setDeletedColumns (List<AssetOperationColumn> deletedColumns) {
      this.deletedColumns = deletedColumns;
   }
}
