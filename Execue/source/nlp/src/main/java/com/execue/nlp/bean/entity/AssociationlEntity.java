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

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.EntityTripleDefinition;

public class AssociationlEntity implements IWeightedEntity, Cloneable, Serializable {

   protected double                     weight;
   private int                          subjectPostion;
   private int                          objectPostion;
   private int                          predicatePosition = -1;
   private List<EntityTripleDefinition> triples;
   private WeightInformation            weightInformation;

   public void setWeight (double weight) {
      this.weight = weight;
   }

   public double getWeight () {
      return weight;
   }

   public List<EntityTripleDefinition> getTriples () {
      return triples;
   }

   public void setTriples (List<EntityTripleDefinition> triples) {
      this.triples = triples;
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

   public void setTriple (EntityTripleDefinition triple) {
      if (this.triples == null) {
         this.triples = new ArrayList<EntityTripleDefinition>(1);
      }
      this.triples.add(triple);
   }

   public EntityTripleDefinition getTriple () {
      if (this.triples != null) {
         return this.triples.get(0);
      }
      return null;
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
}
