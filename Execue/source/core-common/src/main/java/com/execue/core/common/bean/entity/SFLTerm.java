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


package com.execue.core.common.bean.entity;

import java.util.Set;

public class SFLTerm implements java.io.Serializable {

   private static final long serialVersionUID   = 1L;
   private Long              id;
   private String            businessTerm;
   private Set<SFLTermToken> sflTermTokens;
   private Long              contextId;
   private Integer           requiredTokenCount = 0;

   public Integer getRequiredTokenCount () {
      return requiredTokenCount;
   }

   public void setRequiredTokenCount (Integer requiredTokenCount) {
      this.requiredTokenCount = requiredTokenCount;
   }

   public static long getSerialVersionUID () {
      return serialVersionUID;
   }

   /**
    * @return the contextId
    */
   public Long getContextId () {
      return contextId;
   }

   /**
    * @param contextId
    *           the contextId to set
    */
   public void setContextId (Long contextId) {
      this.contextId = contextId;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getBusinessTerm () {
      return businessTerm;
   }

   public void setBusinessTerm (String businessTerm) {
      this.businessTerm = businessTerm;
   }

   public Set<SFLTermToken> getSflTermTokens () {
      return sflTermTokens;
   }

   public void setSflTermTokens (Set<SFLTermToken> sflTermTokens) {
      this.sflTermTokens = sflTermTokens;
   }

}
