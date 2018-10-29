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
 * JSONObject representing status after add/edit of concept or instance
 * 
 * @author kaliki
 * @since 4.0
 */
public class EntityStatus {

   private Long   id;
   private Long   bedId;
   private String type;
   private String dispName;
   private String msg;
   private String msgType;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getType () {
      return type;
   }

   public void setType (String type) {
      this.type = type;
   }

   public String getDispName () {
      return dispName;
   }

   public void setDispName (String dispName) {
      this.dispName = dispName;
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

   public Long getBedId () {
      return bedId;
   }

   public void setBedId (Long bedId) {
      this.bedId = bedId;
   }
}
