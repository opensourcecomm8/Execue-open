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


package com.execue.nlp.bean.matrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.WeightInformation;
import com.execue.nlp.bean.NLPToken;
import com.execue.nlp.bean.entity.AssociationGroupEntity;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.ProfileEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;

/**
 * Holds NLP Tokens. Each token can have one and only one Recognition Entity. This object can act as input for the
 * Iteration and can also the involved in the summarization process.
 * 
 * @author Kaliki
 * @version 1.0
 */

public class Summary implements Serializable, Cloneable {

   private List<NLPToken>    NLPTokens;
   private int               id;
   private PossibilityStatus status;
   boolean                   groupRecognitionSummary;
   private WeightInformation weightInformation;

   /**
    * @return the weightInformation
    */
   public WeightInformation getWeightInformation () {
      if (weightInformation == null) {
         calculateWeightInformation();
      }
      return weightInformation;
   }

   private void calculateWeightInformation () {
      double recognitionQuality = getRecognitionQuality();
      double recognitionWeight = getRecognitionWeight();
      double associationQuality = getAssociationQuality();
      double associationWeight = getAssociationWeight();
      double summaryQuality = (recognitionQuality + associationQuality) / 2;
      double summaryWeight = recognitionWeight + associationWeight;
      weightInformation = new WeightInformation();
      weightInformation.setRecognitionQuality(summaryQuality);
      weightInformation.setRecognitionWeight(summaryWeight);
   }

   /**
    * @return
    */
   private double getAssociationWeight () {
      for (NLPToken token : NLPTokens) {
         if (token.getAssociationEntities() != null && !token.getAssociationEntities().isEmpty()) {
            AssociationGroupEntity age = (AssociationGroupEntity) token.getAssociationEntities().get(0);
            return age.getAssociationSumWeight();
         }
      }
      return 0;
   }

   /**
    * @return
    */
   private double getAssociationQuality () {
      for (NLPToken token : NLPTokens) {
         if (token.getAssociationEntities() != null && !token.getAssociationEntities().isEmpty()) {
            AssociationGroupEntity age = (AssociationGroupEntity) token.getAssociationEntities().get(0);
            return age.getQuality();
         }
      }
      return 0;
   }

   /**
    * @return
    */
   private double getRecognitionWeight () {
      double weight = 0;
      for (NLPToken token : NLPTokens) {
         if (token.getDefaultRecognitionEntity() instanceof OntoEntity) {
            weight = weight + token.getDefaultRecognitionEntity().getRecognitionWeight();
         }
      }
      return weight;
   }

   /**
    * @return
    */
   private double getRecognitionQuality () {
      double quality = 0;
      int totalRecognitions = 0;
      for (NLPToken token : NLPTokens) {
         if (token.getDefaultRecognitionEntity() instanceof OntoEntity) {
            quality = quality + token.getDefaultRecognitionEntity().getRecognitionQuality();
            totalRecognitions++;
         }
      }
      if (totalRecognitions == 0) {
         return quality;
      }
      return quality / totalRecognitions;
   }

   public Summary () {
      id = 1;
      status = PossibilityStatus.INDIVIDUAL_RECOGNITION;
   }

   public List<NLPToken> getNLPTokens () {
      return NLPTokens;
   }

   public void setNLPTokens (List<NLPToken> tokens) {
      NLPTokens = tokens;
   }

   public int getId () {
      return id;
   }

   public void setId (int id) {
      this.id = id;
   }

   public PossibilityStatus getStatus () {
      return status;
   }

   public void setStatus (PossibilityStatus status) {
      this.status = status;
   }

   // public boolean isPreProcessingComplete () {
   // return !status.equals(PossibilityStatus.PRE_PROCESS);
   // }
   //
   // public boolean isIndividualRecognitionComplete () {
   // return isPreProcessingComplete() && status.equals(PossibilityStatus.IN_PROCESS);
   // }
   //
   // public boolean isGroupRecognitionComplete () {
   // return isIndividualRecognitionComplete() && status.equals(PossibilityStatus.INDIVIDUAL_RECOGNITION_COMPLETE);
   // }
   //
   // public boolean isDomainRecognitionComplete () {
   // return isGroupRecognitionComplete() && status.equals(PossibilityStatus.DOMAIN_RECOGNITION_COMPLETE);
   // }
   //
   // public boolean isAssociationComplete () {
   // return isDomainRecognitionComplete() && status.equals(PossibilityStatus.ASSOCIATION_COMPLETE);
   // }

   // Utility Methods

   public void addNLPToken (NLPToken token) {
      if (NLPTokens == null) {
         NLPTokens = new ArrayList<NLPToken>();
      }
      NLPTokens.add(token);
   }

   /**
    * Method to get the average Onto recognition Weight for the Summary.
    * 
    * @return
    */
   public double getAverageOntoRecognitionWeight () {
      double totalWeight = 0;
      int totalRecognitions = 0;
      for (NLPToken token : NLPTokens) {
         if (token.getDefaultRecognitionEntity() instanceof OntoEntity) {
            totalWeight = totalWeight
                     + token.getDefaultRecognitionEntity().getWeightInformation().getRecognitionQuality();
         }
         totalRecognitions++;
      }
      if (totalRecognitions == 0) {
         return totalWeight;
      }
      return totalWeight / totalRecognitions;
   }

   public double getAverageAssociationWeight () {
      for (NLPToken token : NLPTokens) {
         if (token.getAssociationEntities() != null && !token.getAssociationEntities().isEmpty()) {
            AssociationGroupEntity age = (AssociationGroupEntity) token.getAssociationEntities().get(0);
            return age.averageWeight();
         }
      }
      return 0;
   }

   // Overridden Methods

   @SuppressWarnings ("unchecked")
   @Override
   public Object clone () throws CloneNotSupportedException {
      Summary summary = (Summary) super.clone();
      if (NLPTokens != null) {
         List<NLPToken> newNLPTokens = (List<NLPToken>) ((ArrayList<NLPToken>) NLPTokens).clone();
         for (int i = 0; i < NLPTokens.size(); i++) {
            NLPToken token = NLPTokens.get(i);
            NLPToken newToken = (NLPToken) token.clone();
            newNLPTokens.set(i, newToken);
         }
         summary.NLPTokens = newNLPTokens;
      }
      return summary;
   }

   @Override
   public boolean equals (Object obj) {
      boolean equals = false;
      if (obj instanceof Summary) {
         Summary otherSummary = (Summary) obj;
         equals = otherSummary.getNLPTokens().size() == getNLPTokens().size()
                  && getNLPTokens().containsAll(otherSummary.getNLPTokens());
      }
      return equals;
   }

   @Override
   public String toString () {
      StringBuffer sb = new StringBuffer();
      if (NLPTokens != null) {
         for (NLPToken tok : NLPTokens) {
            sb.append(tok.toString()).append(" ** ");
         }
      }
      return sb.toString().trim();
   }

   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   /**
    * @return the groupRecognitionSummary
    */
   public boolean isGroupRecognitionSummary () {
      return groupRecognitionSummary;
   }

   /**
    * @param groupRecognitionSummary
    *           the groupRecognitionSummary to set
    */
   public void setGroupRecognitionSummary (boolean groupRecognitionSummary) {
      this.groupRecognitionSummary = groupRecognitionSummary;
   }

   /**
    * Returns Alphabetical Key for this Summary
    * 
    * @return Stirng representation used for sorting alphabetically
    */
   public String getAlphabeticalKey () {
      StringBuffer sb = new StringBuffer();
      List<NLPToken> NLPTokens = this.getNLPTokens();
      if (NLPTokens != null) {
         for (NLPToken tok : NLPTokens) {
            List<RecognitionEntity> recognitionEntities = tok.getRecognitionEntities();
            for (RecognitionEntity recognitionEntity : recognitionEntities) {
               String word = recognitionEntity.getWord();
               if (recognitionEntity instanceof InstanceEntity) {
                  word = word + "-";
                  word = word + ((InstanceEntity) recognitionEntity).getInstanceDisplayString();
               } else if (recognitionEntity instanceof ProfileEntity) {
                  word = word + "-" + ((ProfileEntity) recognitionEntity).getProfileName();
               }
               sb.append(word.replaceAll(" ", "").toLowerCase());
            }
         }
      }
      return sb.toString();
   }
}
