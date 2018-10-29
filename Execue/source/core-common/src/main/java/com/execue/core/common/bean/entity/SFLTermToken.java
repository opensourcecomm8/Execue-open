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

public class SFLTermToken implements java.io.Serializable {

   private static final long serialVersionUID   = 1L;
   private Long              id;
   private String            businessTermToken;
   private Long              hits;
   private Double            weight;
   private int               group;
   private SFLTerm           sflTerm;
   private Long              sflTermId;
   private Integer           order;
   private Integer           primaryWord;            // takes a value 0 or 1; 0 for secondary word
   private Long              contextId;
   private Integer           required           = 0;
   private Integer           requiredTokenCount = 0;
   private String            businessTerm;

   public String getBusinessTerm () {
      return businessTerm;
   }

   public void setBusinessTerm (String businessTerm) {
      this.businessTerm = businessTerm;
   }

   public Integer getRequired () {
      return required;
   }

   public void setRequired (Integer required) {
      this.required = required;
   }

   public Integer getRequiredTokenCount () {
      return requiredTokenCount;
   }

   public void setRequiredTokenCount (Integer requiredTokenCount) {
      this.requiredTokenCount = requiredTokenCount;
   }

   public static long getSerialVersionUID () {
      return serialVersionUID;
   }

   public Integer getPrimaryWord () {
      return primaryWord;
   }

   public void setPrimaryWord (Integer primaryWord) {
      this.primaryWord = primaryWord;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public SFLTerm getSflTerm () {
      return sflTerm;
   }

   public void setSflTerm (SFLTerm sflTerm) {
      this.sflTerm = sflTerm;
   }

   public String getBusinessTermToken () {
      return businessTermToken;
   }

   public void setBusinessTermToken (String businessTermToken) {
      this.businessTermToken = businessTermToken;
   }

   public Long getHits () {
      return hits;
   }

   public void setHits (Long hits) {
      this.hits = hits;
   }

   public Double getWeight () {
      return weight;
   }

   public void setWeight (Double weight) {
      this.weight = weight;
   }

   public int getGroup () {
      return group;
   }

   public void setGroup (int group) {
      this.group = group;
   }

   public Long getSflTermId () {
      return sflTermId;
   }

   public void setSflTermId (Long sflTermId) {
      this.sflTermId = sflTermId;
   }

   /**
    * @return the order
    */
   public Integer getOrder () {
      return order;
   }

   /**
    * @param order
    *           the order to set
    */
   public void setOrder (Integer order) {
      this.order = order;
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

}
