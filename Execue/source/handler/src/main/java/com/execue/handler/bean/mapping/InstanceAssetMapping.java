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


package com.execue.handler.bean.mapping;

/**
 * JSON Object representation of Instance to Table.column.member Mapping
 * 
 * @author kaliki
 * @since 4.0
 */
public class InstanceAssetMapping extends AbstractAssetMapping {

   private String  memDispName;
   private String  instanceDispName;
   private String  instanceType;    // Suggested or Existing
   private String  conDispName;
   private String  tblDispName;
   private String  colDispName;
   // TODO: -JVK- check if instanceId is required, if not remove it
   private Long    insId;
   private boolean deleteMapping;

   public boolean isDeleteMapping () {
      return deleteMapping;
   }

   public void setDeleteMapping (boolean deleteMapping) {
      this.deleteMapping = deleteMapping;
   }

   public String getTblDispName () {
      return tblDispName;
   }

   public void setTblDispName (String tblDispName) {
      this.tblDispName = tblDispName;
   }

   public String getColDispName () {
      return colDispName;
   }

   public void setColDispName (String colDispName) {
      this.colDispName = colDispName;
   }

   public String getMemDispName () {
      return memDispName;
   }

   public void setMemDispName (String memDispName) {
      this.memDispName = memDispName;
   }

   public String getInstanceDispName () {
      return instanceDispName;
   }

   public void setInstanceDispName (String instanceDispName) {
      this.instanceDispName = instanceDispName;
   }

   public String getInstanceType () {
      return instanceType;
   }

   public void setInstanceType (String instanceType) {
      this.instanceType = instanceType;
   }

   public String getConDispName () {
      return conDispName;
   }

   public void setConDispName (String conDispName) {
      this.conDispName = conDispName;
   }

   public Long getInsId () {
      return insId;
   }

   public void setInsId (Long insId) {
      this.insId = insId;
   }
}