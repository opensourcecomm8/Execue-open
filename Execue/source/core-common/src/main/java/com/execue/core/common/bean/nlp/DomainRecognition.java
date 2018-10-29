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


package com.execue.core.common.bean.nlp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.graph.AbstractGraphComponent;
import com.execue.core.common.type.BehaviorType;

/**
 * @author Abhijit
 * @since Sep 18, 2008 - 11:34:41 AM
 */
public class DomainRecognition extends AbstractGraphComponent {

   protected String                    nlpTag;
   protected double                    weight;
   protected String                    groupId;
   protected int                       position;
   protected List<Integer>             originalPositions;
   protected String                    typeName;
   protected String                    conceptName;
   protected String                    conceptDisplayName;
   protected String                    relationDisplayName;
   protected String                    profileName;
   protected Long                      profileBedId;
   protected Long                      typeBEDId;
   protected Long                      conceptBEDId;
   protected Long                      relationBEDId;
   protected Long                      knowledgeId;
   protected Integer                   sentenceId;
   protected List<Long>                alternateBedIds;
   protected INormalizedData           normalizedData;
   protected WeightInformation         weightInformation;
   protected List<InstanceInformation> instanceInformations;
   protected Map<String, Boolean>      flags;
   protected Set<BehaviorType>         behaviors;

   // Overridden methods

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      if (!CollectionUtils.isEmpty(instanceInformations)) {
         return getInstanceDisplayString();
      }
      return getBusinessEntityName();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode () {
      StringBuffer sb = new StringBuffer();
      if (instanceInformations != null) {
         for (InstanceInformation instanceInformation : instanceInformations) {
            sb.append(instanceInformation.getInstanceDisplayName());
         }
      }
      sb.append(' ').append(nlpTag).append(' ').append(conceptBEDId).append(' ').append(position).append(' ').append(
               getType()).append(' ');
      return sb.toString().hashCode();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      boolean equals = false;
      if (obj instanceof DomainRecognition) {
         DomainRecognition rec = (DomainRecognition) obj;
         // TODO: NK: Check with AP, now using type name here??
         if (this.typeName != null && rec.getTypeName() != null) {
            equals = this.typeName.equalsIgnoreCase(rec.getTypeName());
         }
         equals &= this.nlpTag.equalsIgnoreCase(rec.getNlpTag());
         equals &= this.getEntityBeId().equals(rec.getEntityBeId());
         equals &= this.position == rec.getPosition();
         equals &= this.getType().equals(rec.getType());
      }
      return equals;
   }

   // TODO -NA- need to revisit once way to handle multiplicity is decided
   public Long getEntityBeId () {
      if (!CollectionUtils.isEmpty(getInstanceInformations()) && getDefaultInstanceBedId() != null) {
         return getDefaultInstanceBedId();
      }
      if (this.profileBedId != null) {
         return this.profileBedId;
      }
      if (this.conceptBEDId != null) {
         return this.conceptBEDId;
      }
      if (this.relationBEDId != null) {
         return this.relationBEDId;
      }

      return this.typeBEDId;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.graph.IGraphComponent#getBusinessEntityName()
    */
   public String getBusinessEntityName () {
      if (this.profileName != null) {
         return this.profileName;
      } else if (this.conceptDisplayName != null) {
         return this.conceptDisplayName;
      } else if (this.conceptName != null) {
         return this.conceptName;
      } else if (this.relationDisplayName != null) {
         return this.relationDisplayName;
      } else {
         return this.typeName;
      }
   }

   public List<String> getInstanceNames () {
      List<String> instanceNames = new ArrayList<String>(1);
      if (this.getInstanceInformations() != null) {
         for (InstanceInformation instanceInformation : instanceInformations) {
            instanceNames.add(instanceInformation.getInstanceValue());
         }
      }
      return instanceNames;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.graph.IGraphComponent#getRecognitionDisplayName()
    */
   public String getRecognitionDisplayName () {
      if (!CollectionUtils.isEmpty(getInstanceInformations())) {
         return getInstanceDisplayString();
      } else if (this.profileName != null) {
         return this.profileName;
      } else if (this.conceptDisplayName != null) {
         return this.conceptDisplayName;
      } else if (this.relationDisplayName != null) {
         return this.relationDisplayName + "[Relation]";
      } else {
         return this.conceptName;
      }
   }

   public String getKeyWordMatchName () {
      if (!CollectionUtils.isEmpty(instanceInformations)) {
         return getInstanceDisplayString();
      } else if (this.profileName != null) {
         return this.profileName;
      } else if (this.conceptDisplayName != null) {
         return this.conceptDisplayName;
      } else if (this.relationDisplayName != null) {
         return ""; // No need for relation
      } else {
         return this.conceptName;
      }
   }

   /**
    * @return the nlpTag
    */
   public String getNlpTag () {
      return nlpTag;
   }

   /**
    * @param nlpTag
    *           the nlpTag to set
    */
   public void setNlpTag (String nlpTag) {
      this.nlpTag = nlpTag;
   }

   public String getInstanceDisplayString () {
      if (!CollectionUtils.isEmpty(getInstanceInformations()) && this.getDefaultInstanceDisplayName() != null) {
         StringBuilder sb = new StringBuilder();
         int i = 0;
         for (InstanceInformation instanceInformation : instanceInformations) {
            if (i > 0) {
               sb.append(",");
            }
            sb.append(instanceInformation.getInstanceDisplayName());
            i++;
         }
         if (this.conceptDisplayName != null) {
            sb.append(" As ").append(this.conceptDisplayName);
         }
         return sb.toString();
      } else if (!CollectionUtils.isEmpty(getInstanceInformations()) && getDefaultInstanceValue() != null) {
         StringBuilder sb = new StringBuilder();
         int i = 0;
         for (InstanceInformation instanceInformation : instanceInformations) {
            if (i > 0) {
               sb.append(",");
            }
            sb.append(instanceInformation.getInstanceValue());
            i++;
         }
         if (this.conceptDisplayName != null) {
            sb.append(" As ").append(this.conceptDisplayName);
         }
         return sb.toString();
      }
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.graph.AbstractGraphComponent#getWeight()
    */
   @Override
   public double getWeight () {
      return weight;
   }

   /**
    * @param weight
    *           the weight to set
    */
   public void setWeight (double weight) {
      this.weight = weight;
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
   public int getPosition () {
      return position;
   }

   /**
    * @param position
    *           the position to set
    */
   public void setPosition (int position) {
      this.position = position;
   }

   /**
    * @return the conceptName
    */
   public String getConceptName () {
      return conceptName;
   }

   /**
    * @param conceptName
    *           the conceptName to set
    */
   public void setConceptName (String conceptName) {
      this.conceptName = conceptName;
   }

   /**
    * @return the flags
    */
   public Map<String, Boolean> getFlags () {
      if (this.flags == null) {
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
    * @return the conceptBEDId
    */
   public Long getConceptBEDId () {
      return conceptBEDId;
   }

   /**
    * @param conceptBEDId
    *           the conceptBEDId to set
    */
   public void setConceptBEDId (Long conceptBEDId) {
      this.conceptBEDId = conceptBEDId;
   }

   /**
    * @return the originalPositions
    */
   public List<Integer> getOriginalPositions () {
      return originalPositions;
   }

   /**
    * @param originalPositions
    *           the originalPositions to set
    */
   public void setOriginalPositions (List<Integer> originalPositions) {
      this.originalPositions = originalPositions;
   }

   // Utility Methods

   /**
    * Get the value of flag specified by flag name
    * 
    * @param flagName
    *           Name of the flag for which true or false value is needed
    * @return TRUE or FALSE
    */
   public boolean getFlag (String flagName) {
      return this.flags != null && this.flags.containsKey(flagName) && this.flags.get(flagName);
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
   public void setFlag (String flagName) {
      if (this.flags == null) {
         this.flags = new HashMap<String, Boolean>(1);
      }
      this.flags.put(flagName, true);
   }

   public void setFlag (String flagName, Boolean val) {
      this.flags.put(flagName, val);
   }

   public void addOriginalPositions (Collection<Integer> positions) {
      if (this.originalPositions == null) {
         this.originalPositions = new ArrayList<Integer>();
      }
      this.originalPositions.addAll(new HashSet<Integer>(positions));
   }

   public void addOriginalPosition (int position) {
      if (this.originalPositions == null) {
         this.originalPositions = new ArrayList<Integer>();
      }
      this.originalPositions.add(position);
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
    * @return the typeBEDId
    */
   public Long getTypeBEDId () {
      return typeBEDId;
   }

   /**
    * @param typeBEDId
    *           the typeBEDId to set
    */
   public void setTypeBEDId (Long typeBEDId) {
      this.typeBEDId = typeBEDId;
   }

   /**
    * @return the typeName
    */
   public String getTypeName () {
      return typeName;
   }

   /**
    * @param typeName
    *           the typeName to set
    */
   public void setTypeName (String typeName) {
      this.typeName = typeName;
   }

   /**
    * @return the conceptDisplayName
    */
   public String getConceptDisplayName () {
      return conceptDisplayName;
   }

   /**
    * @param conceptDisplayName
    *           the conceptDisplayName to set
    */
   public void setConceptDisplayName (String conceptDisplayName) {
      this.conceptDisplayName = conceptDisplayName;
   }

   /**
    * @return the relationBEDId
    */
   public Long getRelationBEDId () {
      return relationBEDId;
   }

   /**
    * @param relationBEDId
    *           the relationBEDId to set
    */
   public void setRelationBEDId (Long relationBEDId) {
      this.relationBEDId = relationBEDId;
   }

   /**
    * @return the relationDisplayName
    */
   public String getRelationDisplayName () {
      return relationDisplayName;
   }

   /**
    * @param relationDisplayName
    *           the relationDisplayName to set
    */
   public void setRelationDisplayName (String relationDisplayName) {
      this.relationDisplayName = relationDisplayName;
   }

   /**
    * @return the knowledgeId
    */
   public Long getKnowledgeId () {
      return knowledgeId;
   }

   /**
    * @param knowledgeId
    *           the knowledgeId to set
    */
   public void setKnowledgeId (Long knowledgeId) {
      this.knowledgeId = knowledgeId;
   }

   /**
    * @return the alternateBedIds
    */
   public List<Long> getAlternateBedIds () {
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
    * @return the profileName
    */
   public String getProfileName () {
      return profileName;
   }

   /**
    * @param profileName
    *           the profileName to set
    */
   public void setProfileName (String profileName) {
      this.profileName = profileName;
   }

   /**
    * @return the profileBedId
    */
   public Long getProfileBedId () {
      return profileBedId;
   }

   /**
    * @param profileBedId
    *           the profileBedId to set
    */
   public void setProfileBedId (Long profileBedId) {
      this.profileBedId = profileBedId;
   }

   /**
    * @return the sentenceId
    */
   public Integer getSentenceId () {
      return sentenceId;
   }

   /**
    * @param sentenceId
    *           the sentenceId to set
    */
   public void setSentenceId (Integer sentenceId) {
      this.sentenceId = sentenceId;
   }

   /**
    * @return the instanceInfoList
    */
   public List<InstanceInformation> getInstanceInformations () {
      if (instanceInformations == null) {
         instanceInformations = new ArrayList<InstanceInformation>(1);
      }
      return instanceInformations;
   }

   /**
    * @param instanceInfoList
    *           the instanceInfoList to set
    */
   public void setInstanceInformations (List<InstanceInformation> instanceInformations) {
      this.instanceInformations = instanceInformations;
   }

   public String getDefaultInstanceValue () {
      if (CollectionUtils.isEmpty(instanceInformations)) {
         return null;
      }
      return getInstanceInformations().get(0).getInstanceValue();
   }

   public String getDefaultInstanceDisplayName () {
      if (CollectionUtils.isEmpty(instanceInformations)) {
         return null;
      }
      return getInstanceInformations().get(0).getInstanceDisplayName();
   }

   public String getDefaultInstanceDisplaySymbol () {
      if (CollectionUtils.isEmpty(instanceInformations)) {
         return null;
      }
      return getInstanceInformations().get(0).getDisplaySymbol();
   }

   public Long getDefaultInstanceBedId () {
      if (CollectionUtils.isEmpty(instanceInformations)) {
         return null;
      }
      return getInstanceInformations().get(0).getInstanceBedId();
   }

   public List<Long> getInstanceBedIds () {
      List<Long> instanceBedIds = new ArrayList<Long>(1);
      if (instanceInformations != null) {
         for (InstanceInformation instanceInformation : instanceInformations) {
            if (instanceInformation.getInstanceBedId() != null) {
               instanceBedIds.add(instanceInformation.getInstanceBedId());
            }
         }
         return instanceBedIds;
      }
      return null;
   }

   /**
    * @return the behaviors
    */
   public Set<BehaviorType> getBehaviors () {
      if (behaviors == null) {
         behaviors = new HashSet<BehaviorType>();
      }
      return behaviors;
   }

   /**
    * @param behaviors the behaviors to set
    */
   public void setBehaviors (Set<BehaviorType> behaviors) {
      this.behaviors = behaviors;
   }

   public boolean isMutliValuedRecognition () {
      return getBehaviors().contains(BehaviorType.MULTIVALUED);
   }
}
