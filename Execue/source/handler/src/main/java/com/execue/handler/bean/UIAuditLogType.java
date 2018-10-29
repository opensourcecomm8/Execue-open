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

/**
 * This object is a UI representation of enum AuditLogType for auto suggestion.
 * @author Jitendra
 *
 */

public class UIAuditLogType {

   private String id;
   private String displayName;

   /**
    * @return the id
    */
   public String getId () {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId (String id) {
      this.id = id;
   }

   /**
    * @return the displayName
    */
   public String getDisplayName () {
      return displayName;
   }

   /**
    * @param displayName the displayName to set
    */
   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

}
