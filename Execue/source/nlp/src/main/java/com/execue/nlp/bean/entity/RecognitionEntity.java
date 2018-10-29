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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.nlp.RecognitionEntityType;

/**
 * @author Kaliki
 */
public class RecognitionEntity implements IWeightedEntity, Cloneable, Serializable {

   private static final long            serialVersionUID = 1L;
   protected String                     NLPtag;
   protected String                     groupId;
   protected Integer                    position;
   protected RecognitionEntityType      entityType;
   protected String                     word;
   protected List<Integer>              referedTokenPositions;
   protected List<Integer>              originalReferedTokenPositions; // This list contains the original referred
   // Token position based of user Query.
   protected INormalizedData            normalizedData;
   protected ClusterInformation         clusterInformation;
   /**
    * This variable will contain the information about previous rec Entity for which the hits needs to be updated. As of
    * Now This keeps Information about SflToken which are participating for the recognition of this entity.
    */
   private HitsUpdateInfo               hitsUpdateInfo;
   private WeightInformation            weightInformation;
   protected int                        startPosition;
   protected int                        endPosition;
   protected Map<String, Boolean>       flags;
   protected Integer                    iteration;
   protected Integer                    level;
   protected boolean                    tokenAltered;
   private List<Long>                   alternateBedIds;
   private String                       posTFIdentifier;
   private Map<Long, WeightInformation> frameworksFound;

   // Utility Methods
   @Override
   public Object clone () throws CloneNotSupportedException {
      RecognitionEntity entity = (RecognitionEntity) super.clone();
      entity.setNLPtag(this.getNLPtag());
      entity.setGroupId(this.getGroupId());
      entity.setPosition(this.getPosition());
      entity.setEntityType(this.getEntityType());
      entity.setWord(this.getWord());
      entity.setReferedTokenPositions(new ArrayList<Integer>(this.getReferedTokenPositions()));
      entity.setOriginalReferedTokenPositions(new ArrayList<Integer>(this.getOriginalReferedTokenPositions()));
      entity.setNormalizedData(this.getNormalizedData());
      entity.setClusterInformation(this.getClusterInformation());
      entity.setHitsUpdateInfo(this.hitsUpdateInfo);
      entity.setWeightInformation(this.getWeightInformation().clone());

      entity.setStartPosition(this.startPosition);
      entity.setEndPosition(this.endPosition);
      entity.setFlags(this.flags);
      entity.setAlternateBedIds(this.alternateBedIds);
      entity.setPosTFIdentifier(posTFIdentifier);
      if (this.getFrameworksFound() != null) {
         entity.setFrameworksFound(new HashMap<Long, WeightInformation>(this.getFrameworksFound()));
      }
      return entity;
   }

   @Override
   public String toString () {
      StringBuffer sb = new StringBuffer();
      if (this.NLPtag != null) {
         sb.append(NLPtag);
         sb.append(" ");
      }
      if (!CollectionUtils.isEmpty(this.getReferedTokenPositions())) {
         sb.append("[").append(StringUtils.join(this.getReferedTokenPositions(), ',')).append("]");
      }
      return sb.toString();
   }

   @Override
   public boolean equals (Object obj) {
      return (obj instanceof RecognitionEntity || obj instanceof String)
               && this.toString().equalsIgnoreCase(obj.toString());
   }

   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   public String print () {
      if (this instanceof PropertyEntity) {
         return ((PropertyEntity) this).getDisplayName() + " as PropertyEntity with weight " + this.getWeight();
      } else if (this instanceof InstanceEntity) {
         return ((InstanceEntity) this).getInstanceDisplayString() + " as " + ((TypeEntity) this).getTypeDisplayName()
                  + " with weight " + this.getWeight();
      } else if (this instanceof TypeEntity) {
         return ((TypeEntity) this).getDisplayName() + " as " + ((TypeEntity) this).getTypeDisplayName()
                  + " with weight " + this.getWeight();
      }
      return word;
   }

   /**
    * @return the posTFIdentifier
    */
   public String getPosTFIdentifier () {
      return posTFIdentifier;
   }

   /**
    * @param posTFIdentifier
    *           the posTFIdentifier to set
    */
   public void setPosTFIdentifier (String posTFIdentifier) {
      this.posTFIdentifier = posTFIdentifier;
   }

   /**
    * @return the alternateBedIds
    */
   public List<Long> getAlternateBedIds () {
      if (alternateBedIds == null) {
         alternateBedIds = new ArrayList<Long>(1);
      }
      return alternateBedIds;
   }

   /**
    * @param alternateBedIds
    *           the alternateBedIds to set
    */
   public void setAlternateBedIds (List<Long> alternateBedIds) {
      this.alternateBedIds = alternateBedIds;
   }

   /**
    * Method to add the given position to the referedTokenPositions
    * 
    * @param position
    */
   public void addReferedTokenPosition (int position) {
      if (referedTokenPositions == null) {
         referedTokenPositions = new ArrayList<Integer>(1);
      }
      referedTokenPositions.add(position);
   }

   /**
    * Method to return the recognition quality for this entity.
    * 
    * @return the weightInformation.getRecognitionQuality()
    */
   public double getRecognitionQuality () {
      return weightInformation.getRecognitionQuality();
   }

   /**
    * Method to get the Recognition Weight for this entity
    * 
    * @return the weightInformation.getRecognitionWeight()
    */
   public double getRecognitionWeight () {
      return weightInformation.getRecognitionWeight();
   }

   /**
    * @param recognitionQuality
    */
   public void setRecognitionQuality (double recognitionQuality) {
      weightInformation.setRecognitionQuality(recognitionQuality);
   }

   /**
    * @param recognitionWeight
    */
   public void setRecognitionWeight (double recognitionWeight) {
      weightInformation.setRecognitionWeight(recognitionWeight);
   }

   /**
    * @return the nLPtag
    */
   public String getNLPtag () {
      return NLPtag;
   }

   /**
    * @param ptag
    *           the nLPtag to set
    */
   public void setNLPtag (String ptag) {
      NLPtag = ptag;
   }

   /**
    * @return the weight
    */
   public double getWeight () {
      return weightInformation.getFinalWeight();
   }

   /**
    * @return the groupId
    */
   public String getGroupId () {
      return groupId;
   }

   /**
    * @param groupId
    *           the groupId to set
    */
   public void setGroupId (String groupId) {
      this.groupId = groupId;
   }

   /**
    * @return the position
    */
   public Integer getPosition () {
      if (position == null) {
         position = getStartPosition();
      }
      return position;
   }

   /**
    * @param position
    *           the position to set
    */
   public void setPosition (Integer position) {
      this.position = position;
   }

   /**
    * @return the entityType
    */
   public RecognitionEntityType getEntityType () {
      return entityType;
   }

   /**
    * @param entityType
    *           the entityType to set
    */
   public void setEntityType (RecognitionEntityType entityType) {
      this.entityType = entityType;
   }

   /**
    * @return the word
    */
   public String getWord () {
      return word;
   }

   /**
    * @param word
    *           the word to set
    */
   public void setWord (String word) {
      this.word = word;
   }

   /**
    * @return the referedTokenPositions
    */
   public List<Integer> getReferedTokenPositions () {
      return referedTokenPositions;
   }

   /**
    * @param referedTokenPositions
    *           the referedTokenPositions to set
    */
   public void setReferedTokenPositions (List<Integer> referedTokenPositions) {
      if (!CollectionUtils.isEmpty(referedTokenPositions)) {
         Collections.sort(referedTokenPositions);
         startPosition = referedTokenPositions.get(0);
         endPosition = referedTokenPositions.get(referedTokenPositions.size() - 1);
      }
      this.referedTokenPositions = referedTokenPositions;
   }

   /**
    * @return the normalizedData
    */
   public INormalizedData getNormalizedData () {
      return normalizedData;
   }

   /**
    * @param normalizedData
    *           the normalizedData to set
    */
   public void setNormalizedData (INormalizedData normalizedData) {
      this.normalizedData = normalizedData;
   }

   /**
    * @return the clusterInformation
    */
   public ClusterInformation getClusterInformation () {
      return clusterInformation;
   }

   /**
    * @param clusterInformation
    *           the clusterInformation to set
    */
   public void setClusterInformation (ClusterInformation clusterInformation) {
      this.clusterInformation = clusterInformation;
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
    * @return the startPosition
    */
   public int getStartPosition () {
      return startPosition;
   }

   /**
    * @param startPosition
    *           the startPosition to set
    */
   public void setStartPosition (int startPosition) {
      this.startPosition = startPosition;
   }

   /**
    * @return the endPosition
    */
   public int getEndPosition () {
      return endPosition;
   }

   /**
    * @param endPosition
    *           the endPosition to set
    */
   public void setEndPosition (int endPosition) {
      this.endPosition = endPosition;
   }

   /**
    * @return the flags
    */
   public Map<String, Boolean> getFlags () {
      if (flags == null) {
         flags = new HashMap<String, Boolean>(1);
      }
      return flags;
   }

   /**
    * @param flags
    *           the flags to set
    */
   public void setFlags (Map<String, Boolean> flags) {
      this.flags = flags;
   }

   /**
    * Remove the flag from the Status
    * 
    * @param flagName
    *           Name of the flag to be removed
    */
   public void removeFlag (String flagName) {
      if (this.flags != null) {
         this.flags.remove(flagName);
      }
   }

   /**
    * Set the flag named by flag name to TRUE
    * 
    * @param flagName
    *           name of the flag for which boolean value would be set
    */
   public void addFlag (String flagName) {
      if (this.flags == null) {
         this.flags = new HashMap<String, Boolean>(1);
      }
      this.flags.put(flagName, true);
   }

   public void setFlags (List<String> flagNames) {
      for (String flagName : flagNames) {
         getFlags().put(flagName, true);
      }
   }

   public void addFlags (Map<String, Boolean> flagsMap) {
      getFlags().putAll(flagsMap);
   }

   public void addFlag (String flagName, Boolean val) {
      getFlags().put(flagName, val);
   }

   /**
    * @return the iteration
    */
   public Integer getIteration () {
      return iteration;
   }

   /**
    * @param iteration
    *           the iteration to set
    */
   public void setIteration (Integer iteration) {
      this.iteration = iteration;
   }

   /**
    * @return the level
    */
   public Integer getLevel () {
      return level;
   }

   /**
    * @param level
    *           the level to set
    */
   public void setLevel (Integer level) {
      this.level = level;
   }

   /**
    * @return the originalReferedTokenPositions
    */
   public List<Integer> getOriginalReferedTokenPositions () {
      if (this.originalReferedTokenPositions == null) {
         originalReferedTokenPositions = new ArrayList<Integer>(1);
      }
      return originalReferedTokenPositions;
   }

   /**
    * @param position
    */
   public void addOriginalReferedTokenPosition (Integer position) {
      if (this.originalReferedTokenPositions == null) {
         originalReferedTokenPositions = new ArrayList<Integer>(1);
      }
      originalReferedTokenPositions.add(position);
   }

   /**
    * @param originalReferedTokenPositions
    *           the originalReferedTokenPositions to set
    */
   public void setOriginalReferedTokenPositions (List<Integer> originalReferedTokenPositions) {
      this.originalReferedTokenPositions = originalReferedTokenPositions;
   }

   /**
    * @param originalReferredTokenPositions
    */
   public void addOriginalReferedTokenPositions (java.util.Collection<Integer> originalReferredTokenPositions) {
      if (this.originalReferedTokenPositions == null) {
         originalReferedTokenPositions = new ArrayList<Integer>(1);
      }
      this.originalReferedTokenPositions.addAll(originalReferredTokenPositions);
   }

   /**
    * @return the hitsUpdateInfo
    */
   public HitsUpdateInfo getHitsUpdateInfo () {
      return hitsUpdateInfo;
   }

   /**
    * @param hitsUpdateInfo
    *           the hitsUpdateInfo to set
    */
   public void setHitsUpdateInfo (HitsUpdateInfo hitsUpdateInfo) {
      this.hitsUpdateInfo = hitsUpdateInfo;
   }

   /**
    * @return the tokenAltered
    */
   public boolean isTokenAltered () {
      return tokenAltered;
   }

   /**
    * @param tokenAltered
    *           the tokenAltered to set
    */
   public void setTokenAltered (boolean tokenAltered) {
      this.tokenAltered = tokenAltered;
   }

   public Map<Long, WeightInformation> getFrameworksFound () {
      return frameworksFound;
   }

   public void setFrameworksFound (Map<Long, WeightInformation> frameworksFound) {
      this.frameworksFound = frameworksFound;
   }

   public void addFoundFramework (Long frameworkID, WeightInformation wi) {
      if (this.frameworksFound == null) {
         this.frameworksFound = new HashMap<Long, WeightInformation>();
      }
      this.frameworksFound.put(frameworkID, wi);
   }
}
