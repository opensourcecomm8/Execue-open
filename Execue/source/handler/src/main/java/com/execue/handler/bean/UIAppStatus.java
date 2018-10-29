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


package com.execue.handler.bean;

import java.util.ArrayList;
import java.util.List;

public class UIAppStatus {

   private boolean      status = false;
   private String       message;
   private List<String> errorMessages;
   private Long         applicationId;
   private Long         applicationImageId;
   private Long         jobRequestId;
   private String       applicationURL;

   public boolean isStatus () {
      return status;
   }

   public void setStatus (boolean status) {
      this.status = status;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public List<String> getErrorMessages () {
      return errorMessages;
   }

   public void setErrorMessages (List<String> errorMessages) {
      this.errorMessages = errorMessages;
   }

   public Long getJobRequestId () {
      return jobRequestId;
   }

   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   public void addErrorMessage (String errorMessage) {
      if (getErrorMessages() == null) {
         this.errorMessages = new ArrayList<String>();
      }
      getErrorMessages().add(errorMessage);
   }

   public String getMessage () {
      return message;
   }

   public void setMessage (String message) {
      this.message = message;
   }

   public Long getApplicationImageId () {
      return applicationImageId;
   }

   public void setApplicationImageId (Long applicationImageId) {
      this.applicationImageId = applicationImageId;
   }

   public String getApplicationURL () {
      return applicationURL;
   }

   public void setApplicationURL (String applicationURL) {
      this.applicationURL = applicationURL;
   }

}
