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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.nlp.CandidateEntity;
import com.execue.nlp.bean.Group;
import com.execue.nlp.bean.entity.Association;
import com.execue.nlp.bean.entity.RecognitionEntity;

/**
 * Hold data for each possibility. Contains all Iteration for a given possibility.
 * 
 * @author kaliki
 */

public class Possibility {

   public static int             CREATED                     = 0;
   public static int             STARTED                     = 1;
   public static int             CANCELED                    = 9;
   public static int             COMPLETED                   = 10;

   private int                   status;
   private List<Iteration>       iterations;
   private int                   id;
   private int                   iterationIdCounter          = 0;
   private List<CandidateEntity> possibleCandidates          = new ArrayList<CandidateEntity>();

   // TODO -NA- Below 4 weight variable needs to be taken off once the new relevance logic is in place.
   private Double                appWeight                   = 0D;
   private Double                possibilityWeight           = 0D;
   private Double                standarizedPossiblityWeight = 0D;
   private Double                standarizedAppWeight        = 0D;

   private boolean               groupRecognitionPossibility;
   private boolean               centralConceptExists;
   private WeightInformation     weightInformationForExplicitEntities;
   private WeightInformation     weightInformation;
   private List<IWeightedEntity> recognitionEntities;
   private List<IWeightedEntity> unrecognizedEntities;
   private List<IWeightedEntity> unrecognizedBaseRecEntities;
   private List<Association>     associations;
   private WeightInformation     weightInfoForAssociation;
   /**
    * variable to maintain the implicit recognition counter for a possibility.
    */
   private int                   implicitRecognitionCounter;

   /**
    * flag to show if the semanticScoping is completed for the possibility
    */
   public boolean                scopingCompleted;
   private Model                 model;

   private boolean               nonAttributePossibility;
   private double                proximityPenalty;

   public Possibility () {
      super();
   }

   public Possibility (int id) {
      this.id = id;
   }

   @SuppressWarnings ( { "CloneDoesntCallSuperClone" })
   @Override
   public Object clone () throws CloneNotSupportedException {
      Possibility possibilityToBeCloned = this;
      Possibility clonedPossiblity = new Possibility();
      clonedPossiblity.setModel(possibilityToBeCloned.getModel());
      clonedPossiblity.setScopingCompleted(possibilityToBeCloned.isScopingCompleted());
      clonedPossiblity.setStatus(possibilityToBeCloned.getStatus());
      clonedPossiblity.setId(possibilityToBeCloned.getId());
      clonedPossiblity.setIterationIdCounter(possibilityToBeCloned.getIterationIdCounter());
      List<Iteration> iterationsToBeCloned = possibilityToBeCloned.getIterations();
      List<Iteration> clonedIterations = new ArrayList<Iteration>();
      for (Iteration iteration : iterationsToBeCloned) {
         clonedIterations.add((Iteration) iteration.clone());
      }
      clonedPossiblity.setIterations(clonedIterations);
      List<CandidateEntity> candidateEntitesToBeCloned = possibilityToBeCloned.getPossibleCandidates();
      List<CandidateEntity> clonedCandidateEntities = new ArrayList<CandidateEntity>();
      for (CandidateEntity candidateEntity : candidateEntitesToBeCloned) {
         clonedCandidateEntities.add((CandidateEntity) candidateEntity.clone());
      }
      clonedPossiblity.setPossibleCandidates(clonedCandidateEntities);
      clonedPossiblity.setGroupRecognitionPossibility(possibilityToBeCloned.isGroupRecognitionPossibility());

      // NK: Added the below setters in NLP4
      clonedPossiblity.setAppWeight(possibilityToBeCloned.getAppWeight());
      clonedPossiblity.setPossibilityWeight(possibilityToBeCloned.getPossibilityWeight());
      clonedPossiblity.setStandarizedAppWeight(possibilityToBeCloned.getStandarizedAppWeight());
      clonedPossiblity.setStandarizedPossiblityWeight(possibilityToBeCloned.getStandarizedPossiblityWeight());
      clonedPossiblity.setWeightInformation(weightInformation);
      List<IWeightedEntity> weightedEntities = new ArrayList<IWeightedEntity>(1);

      // NK: Added empty check, as in NLP4 testing we were getting the possibility with empty recognition
      if (!CollectionUtils.isEmpty(possibilityToBeCloned.getRecognitionEntities())) {
         for (IWeightedEntity weightedEntity : possibilityToBeCloned.getRecognitionEntities()) {
            weightedEntities.add((IWeightedEntity) weightedEntity.clone());
         }
      }
      clonedPossiblity.setNonAttributePossibility(this.isNonAttributePossibility());
      clonedPossiblity.setRecognitionEntities(weightedEntities);
      // JM: setting the unrecognized entities
      clonedPossiblity.setUnrecognizedEntities(possibilityToBeCloned.getUnrecognizedEntities());
      clonedPossiblity.setCentralConceptExists(possibilityToBeCloned.isCentralConceptExists());
      clonedPossiblity.setImplicitRecognitionCounter(possibilityToBeCloned.getImplicitRecognitionCounter());
      if (possibilityToBeCloned.getWeightInfoForAssociation() != null) {
         clonedPossiblity.setWeightInfoForAssociation(possibilityToBeCloned.getWeightInfoForAssociation().clone());
      }
      clonedPossiblity.setProximityPenalty(possibilityToBeCloned.getProximityPenalty());
      List<IWeightedEntity> unRecBaseEntities = new ArrayList<IWeightedEntity>(1);
      if (!CollectionUtils.isEmpty(getUnrecognizedBaseRecEntities())) {
         unRecBaseEntities.addAll(getUnrecognizedBaseRecEntities());
      }
      clonedPossiblity.setUnrecognizedBaseRecEntities(unRecBaseEntities);
      return clonedPossiblity;
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
    * @return the proximityPenalty
    */
   public double getProximityPenalty () {
      return proximityPenalty;
   }

   /**
    * @param proximityPenalty
    *           the proximityPenalty to set
    */
   public void setProximityPenalty (double proximityPenalty) {
      this.proximityPenalty = proximityPenalty;
   }

   /**
    * @return the nonAttributePossibility
    */
   public boolean isNonAttributePossibility () {
      return nonAttributePossibility;
   }

   /**
    * @param nonAttributePossibility
    *           the nonAttributePossibility to set
    */
   public void setNonAttributePossibility (boolean nonAttributePossibility) {
      this.nonAttributePossibility = nonAttributePossibility;
   }

   public int getId () {
      return id;
   }

   public int getNextIterationId () {
      return ++iterationIdCounter;
   }

   public int getStatus () {
      return status;
   }

   public void setStatus (int status) {
      this.status = status;
   }

   public List<Iteration> getIterations () {
      if (iterations == null) {
         iterations = new ArrayList<Iteration>(1);
      }
      return iterations;
   }

   public void setIterations (List<Iteration> iterations) {
      this.iterations = iterations;
   }

   public void addIteration (Iteration iteration) {
      if (iterations == null) {
         iterations = new ArrayList<Iteration>();
      }
      Group group = new Group(getId(), iteration.getId());
      iteration.setGroup(group);
      iterations.add(iteration);
   }

   public List<CandidateEntity> getPossibleCandidates () {
      if (possibleCandidates == null) {
         possibleCandidates = new ArrayList<CandidateEntity>(1);
      }
      return possibleCandidates;
   }

   public void setPossibleCandidates (List<CandidateEntity> possibleCandidates) {
      this.possibleCandidates = possibleCandidates;
   }

   public void addPossibleCandidate (CandidateEntity possibleCandidate) {
      this.possibleCandidates.add(possibleCandidate);
   }

   public boolean isCompleted () {
      return status == COMPLETED || status == CANCELED;
   }

   public boolean isStarted () {
      return status == STARTED;
   }

   public Iteration getCurrentIteration () {
      if (iterations != null) {
         return iterations.get(iterations.size() - 1);
      }
      return null;
   }

   /**
    * Method to get the scopingCompleted
    * 
    * @return True if Scoping is completed
    */
   public boolean isScopingCompleted () {
      return scopingCompleted;
   }

   /**
    * Setter for scopingCompleted
    * 
    * @param scopingCompleted Set if Scoping is completed
    */
   public void setScopingCompleted (boolean scopingCompleted) {
      this.scopingCompleted = scopingCompleted;
   }

   public Model getModel () {
      return model;
   }

   public void setModel (Model model) {
      this.model = model;
   }

   public int getIterationIdCounter () {
      return iterationIdCounter;
   }

   public void setIterationIdCounter (int iterationIdCounter) {
      this.iterationIdCounter = iterationIdCounter;
   }

   public void setId (int id) {
      this.id = id;
   }

   public Double getAppWeight () {
      return appWeight;
   }

   public void setAppWeight (Double appWeight) {
      this.appWeight = appWeight;
   }

   public Double getStandarizedPossiblityWeight () {
      return standarizedPossiblityWeight;
   }

   public void setStandarizedPossiblityWeight (Double standarizedPossiblityWeight) {
      this.standarizedPossiblityWeight = standarizedPossiblityWeight;
   }

   /**
    * @return the standarizedAppWeight
    */
   public Double getStandarizedAppWeight () {
      return standarizedAppWeight;
   }

   /**
    * @param standarizedAppWeight
    *           the standarizedAppWeight to set
    */
   public void setStandarizedAppWeight (Double standarizedAppWeight) {
      this.standarizedAppWeight = standarizedAppWeight;
   }

   /**
    * @return the possibilityWeight
    */
   public Double getPossibilityWeight () {
      if (weightInformation != null) {
         return weightInformation.getFinalWeight();
      }
      return possibilityWeight;
   }

   /**
    * @param possibilityWeight
    *           the possibilityWeight to set
    */
   public void setPossibilityWeight (Double possibilityWeight) {
      this.possibilityWeight = possibilityWeight;
   }

   /**
    * @return the groupRecognitionPossibility
    */
   public boolean isGroupRecognitionPossibility () {
      return groupRecognitionPossibility;
   }

   /**
    * @param groupRecognitionPossibility
    *           the groupRecognitionPossibility to set
    */
   public void setGroupRecognitionPossibility (boolean groupRecognitionPossibility) {
      this.groupRecognitionPossibility = groupRecognitionPossibility;
   }

   /**
    * @return the weightInformation
    */
   public WeightInformation getWeightInformation () {
      if (weightInformation == null) {
         calculateWeightInformation();
      }
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
    * @return the recognitionEntities
    */
   public List<IWeightedEntity> getRecognitionEntities () {
      return recognitionEntities;
   }

   /**
    * @param recognitionEntities
    *           the recognitionEntities to set
    */
   public void setRecognitionEntities (List<IWeightedEntity> recognitionEntities) {
      this.recognitionEntities = recognitionEntities;
   }

   /**
    * @return the associations
    */
   public List<Association> getAssociations () {
      if (associations == null) {
         return new ArrayList<Association>(1);
      }
      return associations;
   }

   /**
    * @param associations
    *           the associations to set
    */
   public void setAssociations (List<Association> associations) {
      this.associations = associations;
   }

   /**
    * @param association the associations to set
    */
   public void addAssociations (Association association) {
      if (this.associations == null) {
         associations = new ArrayList<Association>(1);
      }
      associations.add(association);
   }

   /**
    * @return
    */
   private double getAssociationWeight () {
      double associationWeight = 0.0;
      for (Association association : getAssociations()) {
         associationWeight = associationWeight + association.getWeightInformation().getRecognitionWeight();
      }
      return associationWeight;
   }

   /**
    * @return
    */
   private double getAssociationQuality () {
      double associationQuality = 0.0;
      int count = 0;
      for (Association association : getAssociations()) {
         associationQuality = associationQuality + association.getWeightInformation().getRecognitionQuality();
         count++;
      }
      if (count < 1) {
         return associationQuality;
      } else {
         return associationQuality / count;
      }

   }

   /**
    * @return
    */
   private double getRecognitionWeight () {
      double weight = 0;
      for (IWeightedEntity recognitionEntity : getRecognitionEntities()) {
         weight = weight + ((RecognitionEntity) recognitionEntity).getRecognitionWeight();
      }

      return weight;
   }

   /**
    * @return
    */
   private double getRecognitionQuality () {
      double quality = 0;
      int totalRecognitions = 0;
      for (IWeightedEntity recognitionEntity : getRecognitionEntities()) {
         quality = quality + ((RecognitionEntity) recognitionEntity).getRecognitionQuality();
         totalRecognitions++;

      }
      if (totalRecognitions == 0) {
         return quality;
      }
      return quality / totalRecognitions;
   }

   public List<IWeightedEntity> getUnrecognizedEntities () {
      return unrecognizedEntities;
   }

   public void setUnrecognizedEntities (List<IWeightedEntity> unrecognizedEntities) {
      this.unrecognizedEntities = unrecognizedEntities;
   }

   public void addRecognitionEntities (IWeightedEntity weightedEntity) {
      if (this.recognitionEntities == null) {
         this.recognitionEntities = new ArrayList<IWeightedEntity>(1);
      }
      this.recognitionEntities.add(weightedEntity);

   }

   public boolean isCentralConceptExists () {
      return centralConceptExists;
   }

   public void setCentralConceptExists (boolean centralConceptExists) {
      this.centralConceptExists = centralConceptExists;
   }

   /**
    * @return the implicitRecognitionCounter
    */
   public int getImplicitRecognitionCounter () {
      return implicitRecognitionCounter;
   }

   /**
    * @return the implicitRecognitionCounter
    */
   public int getNextImplicitRecognitionCounter () {
      return --implicitRecognitionCounter;
   }

   /**
    * @param implicitRecognitionCounter
    *           the implicitRecognitionCounter to set
    */
   public void setImplicitRecognitionCounter (int implicitRecognitionCounter) {
      if (implicitRecognitionCounter >= 0) {
         return;
      }
      this.implicitRecognitionCounter = implicitRecognitionCounter;
   }

   /**
    * @return the weightInformationForExplicitEntities
    */
   public WeightInformation getWeightInformationForExplicitEntities () {
      return weightInformationForExplicitEntities;
   }

   /**
    * @param weightInformationForExplicitEntities
    *           the weightInformationForExplicitEntities to set
    */
   public void setWeightInformationForExplicitEntities (WeightInformation weightInformationForExplicitEntities) {
      this.weightInformationForExplicitEntities = weightInformationForExplicitEntities;
   }

   /**
    * @return the unrecognizedBaseRecEntities
    */
   public List<IWeightedEntity> getUnrecognizedBaseRecEntities () {
      return unrecognizedBaseRecEntities;
   }

   /**
    * @param unrecognizedBaseRecEntities
    *           the unrecognizedBaseRecEntities to set
    */
   public void setUnrecognizedBaseRecEntities (List<IWeightedEntity> unrecognizedBaseRecEntities) {
      this.unrecognizedBaseRecEntities = unrecognizedBaseRecEntities;
   }

   /**
    * @return the weightInfoForAssociation
    */
   public WeightInformation getWeightInfoForAssociation () {
      return weightInfoForAssociation;
   }

   /**
    * @param weightInfoForAssociation
    *           the weightInfoForAssociation to set
    */
   public void setWeightInfoForAssociation (WeightInformation weightInfoForAssociation) {
      this.weightInfoForAssociation = weightInfoForAssociation;
   }

   public WeightInformation getFrameworkWeightInformation () {
      WeightInformation wi = new WeightInformation();
      // TODO --AP--  This is temporary code and need to be substituted with correct weight information based on cloud component info
      int count = 0;
      for (IWeightedEntity ent : this.recognitionEntities) {
         if (ent instanceof RecognitionEntity) {
            RecognitionEntity recEnt = (RecognitionEntity) ent;
            if (recEnt.getFrameworksFound() != null && !recEnt.getFrameworksFound().isEmpty()) {
               count++;
            }
         }
      }
      wi.setRecognitionQuality(1.00);
      wi.setRecognitionWeight(10.00 * count);
      return wi;
   }

}