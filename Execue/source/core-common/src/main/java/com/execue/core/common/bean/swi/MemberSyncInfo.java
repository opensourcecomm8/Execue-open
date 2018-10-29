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
 * This bean represents the member level sync information
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */
public class MemberSyncInfo implements Serializable {

   private AssetOperationTable        tabl;
   private AssetOperationColumn       colum;
   private List<AssetOperationMember> addedMembers;
   private List<AssetOperationMember> deletedMembers;
   private List<MemberUpdationInfo>   updatedMembers;

   public List<MemberUpdationInfo> getUpdatedMembers () {
      return updatedMembers;
   }

   public void setUpdatedMembers (List<MemberUpdationInfo> updatedMembers) {
      this.updatedMembers = updatedMembers;
   }

   
   public AssetOperationTable getTabl () {
      return tabl;
   }

   
   public void setTabl (AssetOperationTable tabl) {
      this.tabl = tabl;
   }

   
   public AssetOperationColumn getColum () {
      return colum;
   }

   
   public void setColum (AssetOperationColumn colum) {
      this.colum = colum;
   }

   
   public List<AssetOperationMember> getAddedMembers () {
      return addedMembers;
   }

   
   public void setAddedMembers (List<AssetOperationMember> addedMembers) {
      this.addedMembers = addedMembers;
   }

   
   public List<AssetOperationMember> getDeletedMembers () {
      return deletedMembers;
   }

   
   public void setDeletedMembers (List<AssetOperationMember> deletedMembers) {
      this.deletedMembers = deletedMembers;
   }

}
