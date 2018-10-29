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

import java.util.List;

public class AssetColumnMember {

   private MappingHeader     header;
   private long              id;
   private long              aedId;
   private String            tblDispName;
   private String            colDispName;
   private List<AssetMember> members;

   public long getId () {
      return id;
   }

   public void setId (long id) {
      this.id = id;
   }

   public long getAedId () {
      return aedId;
   }

   public void setAedId (long aedId) {
      this.aedId = aedId;
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

   public List<AssetMember> getMembers () {
      return members;
   }

   public void setMembers (List<AssetMember> members) {
      this.members = members;
   }

   public MappingHeader getHeader () {
      return header;
   }

   public void setHeader (MappingHeader header) {
      this.header = header;
   }

}
