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
public class Query {

   private String connection_id;
   private String sourceQuery;
   private Script script;

   public Query (String connection_id, String sourceQuery, Script script) {
      super();
      this.connection_id = connection_id;
      this.sourceQuery = sourceQuery;
      this.script = script;
   }

   public Query (String connection_id, String sourceQuery) {
      super();
      this.connection_id = connection_id;
      this.sourceQuery = sourceQuery;
   }

   public String getConnection_id () {
      return connection_id;
   }

   public void setConnection_id (String connection_id) {
      this.connection_id = connection_id;
   }

   public String getSourceQuery () {
      return sourceQuery;
   }

   public void setSourceQuery (String sourceQuery) {
      this.sourceQuery = sourceQuery;
   }

   public Script getScript () {
      return script;
   }

   public void setScript (Script script) {
      this.script = script;
   }

}
