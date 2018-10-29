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
 * JSONObject representing concept Mapping list
 * 
 * @author kaliki
 * @since 4.0
 */
public class ConceptMapping {

   private MappingHeader             header;
   private List<ConceptAssetMapping> mappings;
   private List<TermInfo>         newConcepts;
   private String                    msg;
   private String                    msgType;

   public String getMsg () {
      return msg;
   }

   public MappingHeader getHeader () {
      return header;
   }

   public void setHeader (MappingHeader header) {
      this.header = header;
   }

   public List<ConceptAssetMapping> getMappings () {
      return mappings;
   }

   public void setMappings (List<ConceptAssetMapping> mappings) {
      this.mappings = mappings;
   }

   public List<TermInfo> getNewConcepts () {
      return newConcepts;
   }

   public void setNewConcepts (List<TermInfo> newConcepts) {
      this.newConcepts = newConcepts;
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
}
