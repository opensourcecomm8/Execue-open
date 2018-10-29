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

public class SDXStatus {

   private String status;
   private Long   assetId;
   private String message;

   public String getStatus () {
      return status;
   }

   public void setStatus (String status) {
      this.status = status;
   }
   
   public Long getAssetId () {
      return assetId;
   }
   
   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }
   
   public String getMessage () {
      return message;
   }

   public void setMessage (String message) {
      this.message = message;
   }

}
