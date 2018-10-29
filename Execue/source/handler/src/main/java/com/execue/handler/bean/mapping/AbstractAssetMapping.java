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


package com.execue.handler.bean.mapping;

/**
 * Carries common attributes used by concept Mapping and instance mapping
 * 
 * @author kaliki
 * @since 4.0
 */
public abstract class AbstractAssetMapping {

   private long   id = -1;
   private long   aedId;
   private long   bedId;
   /*
    * Suggested - Both mapping and concept are new Existing - Both mapping and concept exist (changing concept display
    * name, concept might have changed) New - New mapping on an existing concept
    */
   private String mappingType; // Suggested, Existing, New
   private String msg;
   private String msgType;    // Error, Warning message

   public long getId () {
      return id;
   }

   public void setId (long id) {
      this.id = id;
   }

   public long getAedId () {
      return aedId;
   }

   public void setAedId (long aedId) {
      this.aedId = aedId;
   }

   public long getBedId () {
      return bedId;
   }

   public void setBedId (long bedId) {
      this.bedId = bedId;
   }

   public String getMsg () {
      return msg;
   }

   public void setMsg (String msg) {
      this.msg = msg;
   }

   public String getMsgType () {
      return msgType;
   }

   public void setMsgType (String msgType) {
      this.msgType = msgType;
   }

   public String getMappingType () {
      return mappingType;
   }

   public void setMappingType (String mappingType) {
      this.mappingType = mappingType;
   }
}