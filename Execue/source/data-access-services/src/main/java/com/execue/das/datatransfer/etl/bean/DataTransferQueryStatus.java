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

import com.execue.core.common.type.SuccessFailureType;

public class DataTransferQueryStatus {

   private long               queryId;
   private SuccessFailureType queryExecutionStatus;
   private SuccessFailureType queryRollbackStatus;
   private long               errorCode;
   private String             errorMsg;
   private int                recordCount;
   private long               startTime;
   private long               endTime;

   public long getQueryId () {
      return queryId;
   }

   public void setQueryId (long queryId) {
      this.queryId = queryId;
   }

   public long getErrorCode () {
      return errorCode;
   }

   public void setErrorCode (long errorCode) {
      this.errorCode = errorCode;
   }

   public String getErrorMsg () {
      return errorMsg;
   }

   public void setErrorMsg (String errorMsg) {
      this.errorMsg = errorMsg;
   }

   public int getRecordCount () {
      return recordCount;
   }

   public void setRecordCount (int recordCount) {
      this.recordCount = recordCount;
   }

   public long getStartTime () {
      return startTime;
   }

   public void setStartTime (long startTime) {
      this.startTime = startTime;
   }

   public long getEndTime () {
      return endTime;
   }

   public void setEndTime (long endTime) {
      this.endTime = endTime;
   }

   public SuccessFailureType getQueryExecutionStatus () {
      return queryExecutionStatus;
   }

   public void setQueryExecutionStatus (SuccessFailureType queryExecutionStatus) {
      this.queryExecutionStatus = queryExecutionStatus;
   }

   public SuccessFailureType getQueryRollbackStatus () {
      return queryRollbackStatus;
   }

   public void setQueryRollbackStatus (SuccessFailureType queryRollbackStatus) {
      this.queryRollbackStatus = queryRollbackStatus;
   }

}
