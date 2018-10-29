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

/**
 * This bean represents the table updation sync information
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */
public class TableUpdationInfo implements Serializable {

   private AssetOperationTable sourceTable;
   private AssetOperationTable swiTable;
   private boolean             isDescriptionChanged = false;

   public boolean isDescriptionChanged () {
      return isDescriptionChanged;
   }

   public void setDescriptionChanged (boolean isDescriptionChanged) {
      this.isDescriptionChanged = isDescriptionChanged;
   }

   
   public AssetOperationTable getSourceTable () {
      return sourceTable;
   }

   
   public void setSourceTable (AssetOperationTable sourceTable) {
      this.sourceTable = sourceTable;
   }

   
   public AssetOperationTable getSwiTable () {
      return swiTable;
   }

   
   public void setSwiTable (AssetOperationTable swiTable) {
      this.swiTable = swiTable;
   }

}
