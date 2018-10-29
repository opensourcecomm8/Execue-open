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

public class ETLInput {

   private Map<String, Object> sourceConnectionPropsMap;
   private Map<String, Object> targetConnectionPropsMap;
   private String              sourceQuery;
   private String              targetInsert;

   public Map<String, Object> getSourceConnectionPropsMap () {
      return sourceConnectionPropsMap;
   }

   public void setSourceConnectionPropsMap (Map<String, Object> sourceConnectionPropsMap) {
      this.sourceConnectionPropsMap = sourceConnectionPropsMap;
   }

   public Map<String, Object> getTargetConnectionPropsMap () {
      return targetConnectionPropsMap;
   }

   public void setTargetConnectionPropsMap (Map<String, Object> targetConnectionPropsMap) {
      this.targetConnectionPropsMap = targetConnectionPropsMap;
   }

   public String getSourceQuery () {
      return sourceQuery;
   }

   public void setSourceQuery (String sourceQuery) {
      this.sourceQuery = sourceQuery;
   }

   public String getTargetInsert () {
      return targetInsert;
   }

   public void setTargetInsert (String targetInsert) {
      this.targetInsert = targetInsert;
   }

}
