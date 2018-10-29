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

import com.execue.core.common.type.BatchProcessDetailType;

/**
 * This class represents the BatchProcess object.
 * 
 * @author MurthySN
 * @version 1.0
 * @since 24/02/10
 */
public class BatchProcessDetail implements java.io.Serializable {

   private static final long      serialVersionUID = 1L;
   private Long                   id;
   private BatchProcessDetailType paramName;
   private String                 paramValue;
   private BatchProcess           batchProcess;

   public BatchProcess getBatchProcess () {
      return batchProcess;
   }

   public void setBatchProcess (BatchProcess batchProcess) {
      this.batchProcess = batchProcess;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public BatchProcessDetailType getParamName () {
      return paramName;
   }

   public void setParamName (BatchProcessDetailType paramName) {
      this.paramName = paramName;
   }

   public String getParamValue () {
      return paramValue;
   }

   public void setParamValue (String paramValue) {
      this.paramValue = paramValue;
   }
}
