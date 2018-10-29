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


package com.execue.core.common.bean.entity;

import java.io.Serializable;

import com.execue.core.common.type.CheckType;

public class DefaultMetric implements Serializable {

   private Long             id;
   private Long             tableId;
   private Long             mappingId;
   private Long             popularity;
   private Long             aedId;
   private CheckType        valid = CheckType.NO;
   private transient String columnName;
   private transient String conceptName;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getTableId () {
      return tableId;
   }

   public void setTableId (Long tableId) {
      this.tableId = tableId;
   }

   public Long getPopularity () {
      return popularity;
   }

   public void setPopularity (Long popularity) {
      this.popularity = popularity;
   }

   public Long getMappingId () {
      return mappingId;
   }

   public void setMappingId (Long mappingId) {
      this.mappingId = mappingId;
   }

   public Long getAedId () {
      return aedId;
   }

   public void setAedId (Long aedId) {
      this.aedId = aedId;
   }

   public CheckType getValid () {
      return valid;
   }

   public void setValid (CheckType valid) {
      this.valid = valid;
   }

   public String getColumnName () {
      return columnName;
   }

   public void setColumnName (String columnName) {
      this.columnName = columnName;
   }

   public String getConceptName () {
      return conceptName;
   }

   public void setConceptName (String conceptName) {
      this.conceptName = conceptName;
   }
}
