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
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.nlp.type.AssociationDirectionType;

/**
 * @author kaliki
 */
public class CAAssociationInfo extends BaseBean {

   private List<EntityTripleDefinition> triples;
   private AssociationDirectionType     associationDirection;
   private String                       associationType;
   private String                       associationAllowed = "YES";
   private List<WeightReductionPart>    patternList;

   public List<EntityTripleDefinition> getTriples () {
      return triples;
   }

   public void setTriples (List<EntityTripleDefinition> triple) {
      this.triples = triple;
   }

   public AssociationDirectionType getAssociationDirection () {
      return associationDirection;
   }

   public void setAssociationDirection (AssociationDirectionType associationDirection) {
      this.associationDirection = associationDirection;
   }

   public String getAssociationAllowed () {
      return associationAllowed;
   }

   public void setAssociationAllowed (String associationAllowed) {
      this.associationAllowed = associationAllowed;
   }

   public String getAssociationType () {
      return associationType;
   }

   public void setAssociationType (String associationType) {
      this.associationType = associationType;
   }

   public List<WeightReductionPart> getPatternList () {
      return patternList;
   }

   public void setPatternList (List<WeightReductionPart> patternList) {
      this.patternList = patternList;
   }

   // Utility Methods

   public void addTriple (EntityTripleDefinition trpl) {
      if (this.triples == null)
         this.triples = new ArrayList<EntityTripleDefinition>(1);
      this.triples.add(trpl);
   }

   public boolean isAssociationAllowed () {
      return this.associationAllowed.equalsIgnoreCase("YES");
   }
}
