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
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;

public class Association implements IWeightedEntity, Cloneable, Serializable {

   private static final long       serialVersionUID  = 1L;

   private int                     subjectPostion;
   private int                     objectPostion;
   private int                     predicatePosition = -1;
   private Long                    pathDefId;
   private int                     cardinality;
   // TODO: NK: should have the order by position
   private List<RecognitionEntity> pathComponent;
   private WeightInformation       weightInformation;
   private List<IWeightedEntity>   unAllowedRecs;
   private List<IWeightedEntity>   allowedRecs;
   private boolean                 forImplicitToken;
   private boolean                 instanceTripleExist;
   private boolean                 defaultPath;

   /**
    * @return the defaultPath
    */
   public boolean isDefaultPath () {
      return defaultPath;
   }

   /**
    * @param defaultPath
    *           the defaultPath to set
    */
   public void setDefaultPath (boolean defaultPath) {
      this.defaultPath = defaultPath;
   }

   /**
    * @return the instanceTripleExist
    */
   public boolean isInstanceTripleExist () {
      return instanceTripleExist;
   }

   /**
    * @param instanceTripleExist
    *           the instanceTripleExist to set
    */
   public void setInstanceTripleExist (boolean instanceTripleExist) {
      this.instanceTripleExist = instanceTripleExist;
   }

   /**
    * @return the forImplicitToken
    */
   public boolean isForImplicitToken () {
      return forImplicitToken;
   }

   /**
    * @param forImplicitToken
    *           the forImplicitToken to set
    */
   public void setForImplicitToken (boolean forImplicitToken) {
      this.forImplicitToken = forImplicitToken;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder();
      for (RecognitionEntity recEntity : pathComponent) {
         String word = "";
         if (recEntity instanceof PropertyEntity) {
            word = recEntity.getWord();
         } else {
            if (recEntity instanceof ConceptEntity) {
               word = ((OntoEntity) recEntity).getDisplayName() + "["
                        + ((ConceptEntity) recEntity).getConceptDisplayName() + "]";
            } else {
               word = ((OntoEntity) recEntity).getDisplayName();
            }
         }
         sb.append(word + " - ");
      }
      if (sb.length() > 3) {
         sb.replace(sb.length() - 3, sb.length(), "");
      }
      if (weightInformation != null) {
         sb.append(" - ").append(weightInformation.getFinalWeight());
      }
      return sb.toString();
   }

   public String getCompareString () {
      StringBuilder sb = new StringBuilder();
      for (RecognitionEntity recEntity : pathComponent) {
         sb.append(recEntity.toString() + " - ");

      }
      if (sb.length() > 3) {
         sb.replace(sb.length() - 3, sb.length(), "");
      }
      if (weightInformation != null) {
         sb.append(" - ").append(weightInformation.getFinalWeight());
      }
      return sb.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof Association)
               && this.getCompareString().equalsIgnoreCase(((Association) obj).getCompareString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   public int getSubjectPostion () {
      return subjectPostion;
   }

   public void setSubjectPostion (int subjectPostion) {
      this.subjectPostion = subjectPostion;
   }

   public int getObjectPostion () {
      return objectPostion;
   }

   public void setObjectPostion (int objectPostion) {
      this.objectPostion = objectPostion;
   }

   public int getPredicatePosition () {
      return predicatePosition;
   }

   public void setPredicatePosition (int predicatePosition) {
      this.predicatePosition = predicatePosition;
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      return super.clone();
   }

   /**
    * @return the weightInformation
    */
   public WeightInformation getWeightInformation () {
      return weightInformation;
   }

   /**
    * @param weightInformation
    *           the weightInformation to set
    */
   public void setWeightInformation (WeightInformation weightInformation) {
      this.weightInformation = weightInformation;
   }

   /**
    * @return the pathComponent
    */
   public List<RecognitionEntity> getPathComponent () {
      return pathComponent;
   }

   /**
    * @param pathComponent
    *           the pathComponent to set
    */
   public void setPathComponent (List<RecognitionEntity> pathComponent) {
      this.pathComponent = pathComponent;
   }

   /**
    * @return the unAllowedRecs
    */
   public List<IWeightedEntity> getUnAllowedRecs () {
      return unAllowedRecs;
   }

   /**
    * @param unAllowedRecs
    *           the unAllowedRecs to set
    */
   public void setUnAllowedRecs (List<IWeightedEntity> unAllowedRecs) {
      this.unAllowedRecs = unAllowedRecs;
   }

   public double getWeight () {
      return weightInformation.getFinalWeight();
   }

   /**
    * @return the allowedRecs
    */
   public List<IWeightedEntity> getAllowedRecs () {
      return allowedRecs;
   }

   /**
    * @param allowedRecs
    *           the allowedRecs to set
    */
   public void setAllowedRecs (List<IWeightedEntity> allowedRecs) {
      this.allowedRecs = allowedRecs;
   }

   /**
    * @return the pathDefId
    */
   public Long getPathDefId () {
      return pathDefId;
   }

   /**
    * @param pathDefId
    *           the pathDefId to set
    */
   public void setPathDefId (Long pathDefId) {
      this.pathDefId = pathDefId;
   }

   /**
    * @return the cardinality
    */
   public int getCardinality () {
      return cardinality;
   }

   /**
    * @param cardinality
    *           the cardinality to set
    */
   public void setCardinality (int cardinality) {
      this.cardinality = cardinality;
   }

   /**
    * @param association
    * @param convertibleBeds
    * @return
    */
   public boolean conceptuallyEquals (Association association, List<Set<Long>> convertibleBeds) {
      RecognitionEntity sourceRec1 = this.getPathComponent().get(0);
      RecognitionEntity sourceRec2 = association.getPathComponent().get(0);
      RecognitionEntity destRec2 = association.getPathComponent().get(association.getPathComponent().size() - 1);
      RecognitionEntity destRec1 = this.getPathComponent().get(this.getPathComponent().size() - 1);
      if (this.pathDefId.equals(association.getPathDefId())
               && this.getPathComponent().get(0).equals(association.getPathComponent().get(0))) {
         return true;
      }
      if (sourceRec1.equals(sourceRec2)) {
         if (destRec1 instanceof ConceptEntity && destRec2 instanceof ConceptEntity) {
            ConceptEntity conceptEntity1 = (ConceptEntity) destRec1;
            ConceptEntity conceptEntity2 = (ConceptEntity) destRec2;
            for (Set<Long> beds : convertibleBeds) {
               if (beds.contains(conceptEntity1.getConceptBedId()) && beds.contains(conceptEntity2.getConceptBedId())) {
                  return true;
               }
            }
         }
      }
      return false;
   }

   public int getPositionDiffBetweenSourceAndDestination () {
      List<RecognitionEntity> components = getPathComponent();
      // get the closest distance between source and destination.
      if (!CollectionUtils.isEmpty(components)) {
         IWeightedEntity sourceEntity = components.get(0);
         IWeightedEntity destEntity = components.get(components.size() - 1);
         ReferedTokenPosition sourceReferedTokenPosition = new ReferedTokenPosition(((RecognitionEntity) sourceEntity)
                  .getReferedTokenPositions());
         ReferedTokenPosition destReferedTokenPosition = new ReferedTokenPosition(((RecognitionEntity) destEntity)
                  .getReferedTokenPositions());
         int sourceMin = sourceReferedTokenPosition.getReferedTokenPositions().first();
         int sourceMax = sourceReferedTokenPosition.getReferedTokenPositions().last();
         int destMin = destReferedTokenPosition.getReferedTokenPositions().first();
         int destMax = destReferedTokenPosition.getReferedTokenPositions().last();
         if (sourceMin > destMax) {
            return sourceMin - destMax;
         }
         if (destMin > sourceMax) {
            return destMin - sourceMax;
         }
      }
      int posDiff = 0;
      posDiff = getSubjectPostion() - getObjectPostion();
      if (posDiff < 0) {
         posDiff = posDiff * -1;
      }
      return posDiff;
   }
}
