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


package com.execue.handler.bean.grid;

public class UIAppGrid implements IGridBean {

   private Long    id;
   private String  name;
   private String  desc;
   private String  status;
   private String  mode;
   private Long    modelId;

   private boolean metadataLink = true;

   private Long    jobRequestId;
   private boolean inProgress   = false;

   private boolean statusLink   = false;
   private String  operationType;
   private String  creationType;
   private String  appSourceType;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getDesc () {
      return desc;
   }

   public void setDesc (String desc) {
      this.desc = desc;
   }

   public String getStatus () {
      return status;
   }

   public void setStatus (String status) {
      this.status = status;
   }

   public String getMode () {
      return mode;
   }

   public void setMode (String mode) {
      this.mode = mode;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public boolean isStatusLink () {
      return statusLink;
   }

   public void setStatusLink (boolean statusLink) {
      this.statusLink = statusLink;
   }

   public Long getJobRequestId () {
      return jobRequestId;
   }

   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   public boolean isMetadataLink () {
      return metadataLink;
   }

   public void setMetadataLink (boolean metadataLink) {
      this.metadataLink = metadataLink;
   }

   public boolean isInProgress () {
      return inProgress;
   }

   public void setInProgress (boolean inProgress) {
      this.inProgress = inProgress;
   }

   public String getOperationType () {
      return operationType;
   }

   public void setOperationType (String operationType) {
      this.operationType = operationType;
   }

   public String getCreationType () {
      return creationType;
   }

   public void setCreationType (String creationType) {
      this.creationType = creationType;
   }

   /**
    * @return the appSourceType
    */
   public String getAppSourceType () {
      return appSourceType;
   }

   /**
    * @param appSourceType
    *           the appSourceType to set
    */
   public void setAppSourceType (String appSourceType) {
      this.appSourceType = appSourceType;
   }

}
