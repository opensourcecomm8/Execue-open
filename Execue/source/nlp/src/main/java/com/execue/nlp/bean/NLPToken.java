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


package com.execue.nlp.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.engine.barcode.token.TokenUtility;

/**
 * Token representation for NLP processing. This is loaded token should not be used outside the NLP processing.
 * 
 * @author kaliki
 */

public class NLPToken implements Cloneable, Serializable {

   private static final long       serialVersionUID = 1L;
   protected String                word;
   private List<IWeightedEntity>   associationEntities;
   protected List<Integer>         originalPositions;
   private List<RecognitionEntity> recognitionEntities;

   public NLPToken (String word) {
      this.word = word;
   }

   public NLPToken (String word, int position) {
      this.word = word;
      addOriginalPosition(position);
   }

   public NLPToken (NLPToken token) {
      this.word = token.word;
      addOriginalPositions(token.getOriginalPositions());
   }

   public NLPToken () {
   }

   public List<IWeightedEntity> getAssociationEntities () {
      if (associationEntities == null)
         return new ArrayList<IWeightedEntity>(1);
      return associationEntities;
   }

   public void setAssociationEntities (List<IWeightedEntity> associationEntities) {
      this.associationEntities = associationEntities;
   }

   public String getWord () {
      if (word == null)
         word = TokenUtility.getCurrentWordValue(this);
      return word;
   }

   public void setWord (String word) {
      this.word = word;
   }

   public List<Integer> getOriginalPositions () {
      return originalPositions;
   }

   public void setOriginalPositions (List<Integer> originalPositions) {
      this.originalPositions = originalPositions;
   }

   public void addRecognitionEntity (RecognitionEntity weightedEntity) {
      if (recognitionEntities == null) {
         recognitionEntities = new ArrayList<RecognitionEntity>();
      }
      if (weightedEntity.getReferedTokenPositions() != null) {
         weightedEntity.setStartPosition(weightedEntity.getReferedTokenPositions().get(0));
         weightedEntity.setEndPosition(weightedEntity.getReferedTokenPositions().get(
                  weightedEntity.getReferedTokenPositions().size() - 1));
      } else {
         weightedEntity.setStartPosition(weightedEntity.getPosition());
         weightedEntity.setEndPosition(weightedEntity.getPosition());
      }
      recognitionEntities.add(weightedEntity);
   }

   public RecognitionEntity getDefaultRecognitionEntity () {
      if (recognitionEntities == null || recognitionEntities.isEmpty()) {
         return null;
      }
      return recognitionEntities.get(0);
   }

   public void clearRecognitionEntities () {
      if (this.recognitionEntities != null) {
         this.recognitionEntities.clear();
      }
   }

   public void addAssociationEntity (IWeightedEntity weightedEntity) {
      if (associationEntities == null) {
         associationEntities = new ArrayList<IWeightedEntity>();
      }
      associationEntities.add(weightedEntity);
   }

   public void clearAssociationEntities () {
      if (associationEntities != null) {
         this.associationEntities.clear();
      }
   }

   public void addOriginalPosition (int pos) {
      if (this.originalPositions == null)
         originalPositions = new ArrayList<Integer>(1);
      if (!originalPositions.contains(pos))
         this.originalPositions.add(pos);
   }

   public void addOriginalPositions (List<Integer> positions) {
      if (this.originalPositions == null)
         originalPositions = new ArrayList<Integer>(1);
      HashSet<Integer> hs = new HashSet<Integer>(positions);
      hs.addAll(this.originalPositions);
      this.originalPositions.clear();
      this.originalPositions.addAll(hs);
   }

   public void clearOriginalPositions () {
      this.originalPositions.clear();
   }

   public boolean containsRecognition () {
      return (this.recognitionEntities != null && !this.recognitionEntities.isEmpty());
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      NLPToken newToken = (NLPToken) super.clone();
      if (this.originalPositions != null) {
         // It will be NULL for Virtual Token and clone method is same for both
         newToken.setOriginalPositions(new ArrayList<Integer>(this.originalPositions));
      }
      if (recognitionEntities != null) {
         List<RecognitionEntity> newEntityList = new ArrayList<RecognitionEntity>(recognitionEntities.size());
         for (IWeightedEntity entity : recognitionEntities) {
            newEntityList.add((RecognitionEntity) ((RecognitionEntity) entity).clone());
         }
         newToken.setRecognitionEntities(newEntityList);
      }
      if (associationEntities != null) {
         List<IWeightedEntity> newEntityList = new ArrayList<IWeightedEntity>(associationEntities.size());
         for (IWeightedEntity entity : associationEntities) {
            newEntityList.add((IWeightedEntity) entity.clone());
         }
         newToken.setAssociationEntities(newEntityList);
      }
      return newToken;
   }

   @Override
   public boolean equals (Object obj) {
      boolean equals = true;
      if (obj instanceof NLPToken) {
         NLPToken tempToken = (NLPToken) obj;
         equals = tempToken.getWord().equalsIgnoreCase(getWord());
         equals = equals && (this.recognitionEntities.size() == tempToken.getRecognitionEntities().size())
                  && (this.recognitionEntities.containsAll(tempToken.getRecognitionEntities()));
      }
      return equals;
   }

   @Override
   public String toString () {
      StringBuffer sb = new StringBuffer(getWord());
      for (IWeightedEntity entity : recognitionEntities) {
         sb.append(' ').append(entity.toString());
      }
      return sb.toString();
   }

   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   /**
    * @return the recognitionEntities
    */
   public List<RecognitionEntity> getRecognitionEntities () {
      return recognitionEntities;
   }

   /**
    * @param recognitionEntities
    *           the recognitionEntities to set
    */
   public void setRecognitionEntities (List<RecognitionEntity> recognitionEntities) {
      this.recognitionEntities = recognitionEntities;
   }
}
