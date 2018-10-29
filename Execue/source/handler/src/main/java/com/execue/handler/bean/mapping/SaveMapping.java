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

public class SaveMapping {

   private Long   id;
   private Long   aedId;
   private Long   bedId;
   private String bedType; // Suggested, Existing
   private String delMap;
   private String mapType; // New, Suggested, Existing
   private String dispName;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getAedId () {
      return aedId;
   }

   public void setAedId (Long aedId) {
      this.aedId = aedId;
   }

   public Long getBedId () {
      return bedId;
   }

   public void setBedId (Long dedId) {
      this.bedId = dedId;
   }

   public String getBedType () {
      return bedType;
   }

   public void setBedType (String bedType) {
      this.bedType = bedType;
   }

   public String getDelMap () {
      return delMap;
   }

   public void setDelMap (String delMap) {
      this.delMap = delMap;
   }

   public String getMapType () {
      return mapType;
   }

   public void setMapType (String mapType) {
      this.mapType = mapType;
   }

   public String getDispName () {
      return dispName;
   }

   public void setDispName (String dispName) {
      this.dispName = dispName;
   }
}