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


package com.execue.das.datatransfer.etl.bean;

/**
 * Structure for the query that runs on the remote asset
 * 
 * @author Jayadev
 */
public class DataTransferQuery {

   private Long   id;
   private String targetTable;
   private String sourceSelectQuery;
   private String targetCreateStatement;
   private String targetInsertStatement;
   private String targetRollbackStatement;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getTargetTable () {
      return targetTable;
   }

   public void setTargetTable (String targetTable) {
      this.targetTable = targetTable;
   }

   public String getSourceSelectQuery () {
      return sourceSelectQuery;
   }

   public void setSourceSelectQuery (String sourceSelectQuery) {
      this.sourceSelectQuery = sourceSelectQuery;
   }

   public String getTargetCreateStatement () {
      return targetCreateStatement;
   }

   public void setTargetCreateStatement (String targetCreateStatement) {
      this.targetCreateStatement = targetCreateStatement;
   }

   public String getTargetInsertStatement () {
      return targetInsertStatement;
   }

   public void setTargetInsertStatement (String targetInsertStatement) {
      this.targetInsertStatement = targetInsertStatement;
   }

   public String getTargetRollbackStatement () {
      return targetRollbackStatement;
   }

   public void setTargetRollbackStatement (String targetRollbackStatement) {
      this.targetRollbackStatement = targetRollbackStatement;
   }

}
