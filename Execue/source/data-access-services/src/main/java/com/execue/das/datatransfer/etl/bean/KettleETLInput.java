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

import java.util.Map;

/**
 * Kettle specific ETL input data structure
 * 
 * @author Jayadev
 */
public class KettleETLInput {

   private Map<String, String> remoteConnectionPropsMap;
   private Map<String, String> localConnectionPropsMap;
   private String              remoteQuery;
   private String              localInsert;

   public Map<String, String> getRemoteConnectionPropsMap () {
      return remoteConnectionPropsMap;
   }

   public void setRemoteConnectionPropsMap (Map<String, String> remoteConnectionPropsMap) {
      this.remoteConnectionPropsMap = remoteConnectionPropsMap;
   }

   public Map<String, String> getLocalConnectionPropsMap () {
      return localConnectionPropsMap;
   }

   public void setLocalConnectionPropsMap (Map<String, String> localConnectionPropsMap) {
      this.localConnectionPropsMap = localConnectionPropsMap;
   }

   public String getRemoteQuery () {
      return remoteQuery;
   }

   public void setRemoteQuery (String remoteQuery) {
      this.remoteQuery = remoteQuery;
   }

   public String getLocalInsert () {
      return localInsert;
   }

   public void setLocalInsert (String localInsert) {
      this.localInsert = localInsert;
   }

}
