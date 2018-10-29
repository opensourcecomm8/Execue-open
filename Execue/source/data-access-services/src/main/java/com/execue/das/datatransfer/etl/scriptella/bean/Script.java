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


package com.execue.das.datatransfer.etl.scriptella.bean;

/**
 * @author Murthy Used by XSteam API
 */
public class Script {

   private String connection_id;
   private String insertQuery;

   public Script (String connection_id, String insertQuery) {
      super();
      this.connection_id = connection_id;
      this.insertQuery = insertQuery;
   }

   public String getConnection_id () {
      return connection_id;
   }

   public void setConnection_id (String connection_id) {
      this.connection_id = connection_id;
   }

   public String getInsertQuery () {
      return insertQuery;
   }

   public void setInsertQuery (String insertQuery) {
      this.insertQuery = insertQuery;
   }

}
