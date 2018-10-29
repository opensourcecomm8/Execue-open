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


/**
 * 
 */
package com.execue.nlp.bean.association;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.Rule;
import com.execue.nlp.type.AssociationDirectionType;

/**
 * @author Nihar
 */
public class AssociationPath {

   private IWeightedEntity          source;
   private IWeightedEntity          destination;
   private AssociationDirectionType direction;
   private PathDefinition           path;
   private WeightInformation        weightInformation;
   List<IWeightedEntity>            unAllowedRecognitions;
   List<IWeightedEntity>            allowedRecognitions;
   List<IWeightedEntity>            allowedInProximity;
   private boolean                  validByAssociation;
   private boolean                  defaultPath;

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
    * @return the source
    */
   public IWeightedEntity getSource () {
      return source;
   }

   /**
    * @param source
    *           the source to set
    */
   public void setSource (IWeightedEntity source) {
      this.source = source;
   }

   /**
    * @return the destination
    */
   public IWeightedEntity getDestination () {
      return destination;
   }

   /**
    * @param destination
    *           the destination to set
    */
   public void setDestination (IWeightedEntity destination) {
      this.destination = destination;
   }

   /**
    * @return the direction
    */
   public AssociationDirectionType getDirection () {
      return direction;
   }

   /**
    * @param direction
    *           the direction to set
    */
   public void setDirection (AssociationDirectionType direction) {
      this.direction = direction;
   }

   /**
    * @return the path
    */
   public PathDefinition getPath () {
      return path;
   }

   /**
    * @param path
    *           the path to set
    */
   public void setPath (PathDefinition path) {
      this.path = path;
   }

   /**
    * This method returns true if the path is subset of the given input path
    * 
    * @param path
    *           the AssociationPath
    * @return the boolean true/false
    */
   public boolean isSubsetOf (AssociationPath path) {

      Set<Path> path1 = this.getPath().getPaths();
      Set<Path> path2 = path.getPath().getPaths();
      boolean isSubset = false;
      if (path1.size() < path2.size()) {
         List<EntityTripleDefinition> etds1 = this.getETDs();
         List<EntityTripleDefinition> etds2 = path.getETDs();
         if (etds2.containsAll(etds1)) {
            return true;
         }
      }
      return isSubset;
   }

   /**
    * This method returns the list of ETDs present in the association path
    * 
    * @return the List<EntityTripleDefinition>
    */
   public List<EntityTripleDefinition> getETDs () {
      List<EntityTripleDefinition> etds = new ArrayList<EntityTripleDefinition>(1);
      for (Path path : this.path.getPaths()) {
         etds.add(path.getEntityTripleDefinition());
      }
      return etds;
   }

   public Set<Rule> getRules () {
      return path.getPathRules();
   }

   /**
    * @return the unAllowedRecognitions
    */
   public List<IWeightedEntity> getUnAllowedRecognitions () {
      if (unAllowedRecognitions == null) {
         unAllowedRecognitions = new ArrayList<IWeightedEntity>(1);
      }
      return unAllowedRecognitions;
   }

   /**
    * @param unAllowedRecognition
    *           the unAllowedRecognitions to set
    */
   public void addUnAllowedRecognitions (IWeightedEntity unAllowedRecognition) {
      if (this.unAllowedRecognitions == null) {
         unAllowedRecognitions = new ArrayList<IWeightedEntity>(1);
      }
      unAllowedRecognitions.add(unAllowedRecognition);
   }

   /**
    * @return the ullowedRecognitions
    */
   public List<IWeightedEntity> getAllowedRecognitions () {
      return allowedRecognitions;
   }

   /**
    * @param ullowedRecognitions
    *           the ullowedRecognitions to set
    */
   public void setAllowedRecognitions (List<IWeightedEntity> ullowedRecognitions) {
      this.allowedRecognitions = ullowedRecognitions;
   }

   /**
    * @param allowedRecognition
    *           the unAllowedRecognitions to set
    */
   public void addAllowedRecognitions (IWeightedEntity allowedRecognition) {
      if (this.allowedRecognitions == null) {
         this.allowedRecognitions = new ArrayList<IWeightedEntity>(1);
      }
      this.allowedRecognitions.add(allowedRecognition);
   }

   /**
    * @return the allowedInProximity
    */
   public List<IWeightedEntity> getAllowedInProximity () {
      if (allowedInProximity == null) {
         allowedInProximity = new ArrayList<IWeightedEntity>(1);
      }
      return allowedInProximity;
   }

   /**
    * @param allowedInProximity
    *           the allowedInProximity to set
    */
   public void setAllowedInProximity (List<IWeightedEntity> allowedInProximity) {
      this.allowedInProximity = allowedInProximity;
   }

   /**
    * Return true if Association is valid based on relation in User Query
    * 
    * @return True or False
    */
   public boolean isValidByAssociation () {
      return validByAssociation;
   }

   /**
    * Set if Association is valid by relation in user query
    * 
    * @param validByAssociation
    *           True or False
    */
   public void setValidByAssociation (boolean validByAssociation) {
      this.validByAssociation = validByAssociation;
   }
}
