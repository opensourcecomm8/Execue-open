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
 * Created on Aug 21, 2008
 */
package com.execue.nlp.bean.rules.association;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.algorithm.BaseBean;
import com.execue.nlp.rule.IWeightAssignmentRule;
import com.execue.nlp.type.AssociationDirectionType;

/**
 * Holds Association Weight
 * 
 * @author kaliki
 */
public class CAAssociationWeight extends BaseBean implements IWeightAssignmentRule {

   private AssociationDirectionType associationDirection;
   private double                   proximityReduction   = 0;
   private double                   weight               = 0;
   private List<CAAssociationWord>  associationWordsInfo = new ArrayList<CAAssociationWord>();

   public AssociationDirectionType getAssociationDirection () {
      return associationDirection;
   }

   public void setAssociationDirection (AssociationDirectionType associationDirection) {
      this.associationDirection = associationDirection;
   }

   public double getProximityReduction () {
      return proximityReduction;
   }

   public void setProximityReduction (double proximityReduction) {
      this.proximityReduction = proximityReduction;
   }

   public double getWeight () {
      return weight;
   }

   public void setWeight (double weight) {
      this.weight = weight;
   }

   public List<CAAssociationWord> getAssociationWordsInfo () {
      return associationWordsInfo;
   }

   public void setAssociationWordsInfo (List<CAAssociationWord> associationWordsInfo) {
      this.associationWordsInfo = associationWordsInfo;
   }
}
