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


package com.execue.web.core.action.swi;

public class ProfileStatus {

   private String status;
   private Long   conceptProfileId;
   private Long   instanceProfileId;
   private String conceptProfileName;
   private String instanceProfileName;
   private String message;

   public String getStatus () {
      return status;
   }

   public void setStatus (String status) {
      this.status = status;
   }

   public Long getConceptProfileId () {
      return conceptProfileId;
   }

   public void setConceptProfileId (Long conceptProfileId) {
      this.conceptProfileId = conceptProfileId;
   }

   public Long getInstanceProfileId () {
      return instanceProfileId;
   }

   public void setInstanceProfileId (Long instanceProfileId) {
      this.instanceProfileId = instanceProfileId;
   }

   public String getConceptProfileName () {
      return conceptProfileName;
   }

   public void setConceptProfileName (String conceptProfileName) {
      this.conceptProfileName = conceptProfileName;
   }

   public String getInstanceProfileName () {
      return instanceProfileName;
   }

   public void setInstanceProfileName (String instanceProfileName) {
      this.instanceProfileName = instanceProfileName;
   }

   public String getMessage () {
      return message;
   }

   public void setMessage (String message) {
      this.message = message;
   }

}