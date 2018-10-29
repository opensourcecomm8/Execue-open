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


/*
 * Created on Oct 8, 2008
 */
package com.execue.core.common.bean.nlp;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;

/**
 * @author kaliki
 */
public class CandidateEntity implements IWeightedEntity {

   private String       name;
   private String       originalPositionRange;
   private double       weight;
   private RecognitionEntityType   type;
   private Long         id;
   private List<String> words;

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public void setWeight (double weight) {
      this.weight = weight;
   }

   public double getWeight () {
      return weight;
   }

   public WeightInformation getWeightInformation() {
      return null;  
   }

   public String getOriginalPositionRange () {
      return originalPositionRange;
   }

   public void setOriginalPositionRange (String originalPositionRange) {
      this.originalPositionRange = originalPositionRange;
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      CandidateEntity toBeClonedCandidateEntity = this;
      CandidateEntity clonedCandidateEntity = new CandidateEntity();
      clonedCandidateEntity.setId(toBeClonedCandidateEntity.getId());
      clonedCandidateEntity.setName(toBeClonedCandidateEntity.getName());
      clonedCandidateEntity.setOriginalPositionRange(toBeClonedCandidateEntity.getOriginalPositionRange());
      clonedCandidateEntity.setType(toBeClonedCandidateEntity.getType());
      clonedCandidateEntity.setWeight(toBeClonedCandidateEntity.getWeight());
      List<String> toBeClonedWords = toBeClonedCandidateEntity.getWords();
      List<String> clonedWords = new ArrayList<String>();
      for (String str : toBeClonedWords) {
         clonedWords.add(new String(str));
      }
      clonedCandidateEntity.setWords(clonedWords);
      return clonedCandidateEntity;
   }

   /**
    * @return the type
    */
   public RecognitionEntityType getType () {
      return type;
   }

   /**
    * @param type
    *           the type to set
    */
   public void setType (RecognitionEntityType type) {
      this.type = type;
   }

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the words
    */
   public List<String> getWords () {
      return words;
   }

   /**
    * @param words
    *           the words to set
    */
   public void setWords (List<String> words) {
      this.words = words;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      if (obj instanceof CandidateEntity) {
         CandidateEntity entity = (CandidateEntity) obj;
         if (entity.getType().equals(this.getType()) && entity.getId() != null && entity.getId().equals(this.getId())) {
            return true;
         }
      }
      return false;
   }
}
