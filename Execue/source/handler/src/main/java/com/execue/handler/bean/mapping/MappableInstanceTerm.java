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

public class MappableInstanceTerm {

   private MappingHeader  header;
   private String         conDispName;
   private long           bedId;
   private List<TermInfo> terms;
   private String         errorMsg;

   public String getConDispName () {
      return conDispName;
   }

   public void setConDispName (String conDispName) {
      this.conDispName = conDispName;
   }

   public long getBedId () {
      return bedId;
   }

   public void setBedId (long bedId) {
      this.bedId = bedId;
   }

   public List<TermInfo> getTerms () {
      return terms;
   }

   public void setTerms (List<TermInfo> terms) {
      this.terms = terms;
   }

   public String getErrorMsg () {
      return errorMsg;
   }

   public void setErrorMsg (String errorMsg) {
      this.errorMsg = errorMsg;
   }

   public MappingHeader getHeader () {
      return header;
   }

   public void setHeader (MappingHeader header) {
      this.header = header;
   }

}
