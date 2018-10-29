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
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.DataType;

public class UIColumn {

   private String         name;
   private Long           id;
   private String         description;
   private ColumnType     columnType;
   private DataType       dataType;
   private List<UIMember> members;
   private CheckType      hasAclPermission = CheckType.NO;

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

   public ColumnType getColumnType () {
      return columnType;
   }

   public void setColumnType (ColumnType columnType) {
      this.columnType = columnType;
   }

   public DataType getDataType () {
      return dataType;
   }

   public void setDataType (DataType dataType) {
      this.dataType = dataType;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public List<UIMember> getMembers () {
      return members;
   }

   public void setMembers (List<UIMember> members) {
      this.members = members;
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
}
