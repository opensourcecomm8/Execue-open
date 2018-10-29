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

public class UIStatus {

   private String       message;
   private List<String> errorMessages;
   private boolean      status = true;
   private Long         jobRequestId;

   /**
    * @return the message
    */
   public String getMessage () {
      return message;
   }

   /**
    * @param message the message to set
    */
   public void setMessage (String message) {
      this.message = message;
   }

   /**
    * @return the errorMessages
    */
   public List<String> getErrorMessages () {
      return errorMessages;
   }

   /**
    * @param errorMessages the errorMessages to set
    */
   public void setErrorMessages (List<String> errorMessages) {
      this.errorMessages = errorMessages;
   }

   /**
    * @return the status
    */
   public boolean isStatus () {
      return status;
   }

   /**
    * @param status the status to set
    */
   public void setStatus (boolean status) {
      this.status = status;
   }

   public void addErrorMessage (String errorMessage) {
      if (getErrorMessages() == null) {
         this.errorMessages = new ArrayList<String>();
      }
      getErrorMessages().add(errorMessage);
   }

   /**
    * @return the jobRequestId
    */
   public Long getJobRequestId () {
      return jobRequestId;
   }

   /**
    * @param jobRequestId the jobRequestId to set
    */
   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

}
