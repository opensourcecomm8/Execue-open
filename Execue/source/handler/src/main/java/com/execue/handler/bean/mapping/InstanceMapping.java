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

import java.util.List;

/**
 * JSONObject representing instance Mapping list
 * 
 * @author kaliki
 * @since 4.0
 */
public class InstanceMapping {

   private MappingHeader              header;
   private List<InstanceAssetMapping> mappings;
   private List<TermInfo>             newInstances;
   private String                     msg;
   private String                     msgType;
   private Long                       jobRequestId;

   public MappingHeader getHeader () {
      return header;
   }

   public void setHeader (MappingHeader header) {
      this.header = header;
   }

   public Long getJobRequestId () {
      return jobRequestId;
   }

   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   public List<InstanceAssetMapping> getMappings () {
      return mappings;
   }

   public void setMappings (List<InstanceAssetMapping> mappings) {
      this.mappings = mappings;
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

   public List<TermInfo> getNewInstances () {
      return newInstances;
   }

   public void setNewInstances (List<TermInfo> newInstances) {
      this.newInstances = newInstances;
   }
}
