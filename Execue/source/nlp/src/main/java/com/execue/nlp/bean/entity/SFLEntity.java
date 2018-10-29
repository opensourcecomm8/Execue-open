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


package com.execue.nlp.bean.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.nlp.CandidateEntity;

public class SFLEntity extends TagEntity implements Cloneable, Serializable, ICandidate {

   private List<CandidateEntity> candidates;
   private Long                  sflTermId;
   private Long                  contextId;
   /**
    * List to keep tract of which tokens are considered for this SFL entity
    */
   private List<Integer>         consideredParts;

   public String getSflName () {
      return word;
   }

   public void setSflName (String sflName) {
      this.word = sflName;
   }

   public List<CandidateEntity> getCandidates () {
      return candidates;
   }

   public void setCandidates (List<CandidateEntity> candidates) {
      this.candidates = candidates;
   }

   public void addCandidate (CandidateEntity candidateEntity) {
      if (candidates == null) {
         candidates = new ArrayList<CandidateEntity>();
      }
      candidates.add(candidateEntity);
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      SFLEntity sflEntity = (SFLEntity) super.clone();
      if (!CollectionUtils.isEmpty(consideredParts)) {
         sflEntity.setConsideredParts(new ArrayList<Integer>(consideredParts));
      }
      sflEntity.setContextId(contextId);
      sflEntity.setClusterInformation(clusterInformation);
      return sflEntity;
   }

   @Override
   public String toString () {
      String superString = super.toString();
      if (word != null) {
         superString = superString + " " + word;
      }
      if (sflTermId != null) {
         superString = superString + " " + sflTermId;
      }
      if (contextId != null) {
         superString = superString + " " + contextId;
      }
      return superString;

   }

   /**
    * @return the sflTermId
    */
   public Long getSflTermId () {
      return sflTermId;
   }

   /**
    * @param sflTermId
    *           the sflTermId to set
    */
   public void setSflTermId (Long sflTermId) {
      this.sflTermId = sflTermId;
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

   /**
    * @return the consideredParts
    */
   public List<Integer> getConsideredParts () {
      if (consideredParts == null) {
         consideredParts = new ArrayList<Integer>(1);
      }
      return consideredParts;
   }

   /**
    * @param consideredParts
    *           the consideredParts to set
    */
   public void setConsideredParts (List<Integer> consideredParts) {
      this.consideredParts = consideredParts;
   }

}
