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


package com.execue.core.common.bean.entity;

import com.execue.core.common.type.CheckType;

/**
 * This class represnts the Concept details.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/07/09
 */
public class ConceptDetails {

   private CheckType grainConcept        = CheckType.NO;
   private CheckType distributionConcept = CheckType.NO;
   private CheckType abstrat             = CheckType.NO;
   private CheckType attribute           = CheckType.NO;
   private CheckType enumration          = CheckType.NO;
   private CheckType quantitative        = CheckType.NO;
   private CheckType comparative         = CheckType.NO;

   public CheckType getGrainConcept () {
      return grainConcept;
   }

   public void setGrainConcept (CheckType grainConcept) {
      this.grainConcept = grainConcept;
   }

   public CheckType getDistributionConcept () {
      return distributionConcept;
   }

   public void setDistributionConcept (CheckType distributionConcept) {
      this.distributionConcept = distributionConcept;
   }

   public CheckType getAbstrat () {
      return abstrat;
   }

   public void setAbstrat (CheckType abstrat) {
      this.abstrat = abstrat;
   }

   public CheckType getAttribute () {
      return attribute;
   }

   public void setAttribute (CheckType attribute) {
      this.attribute = attribute;
   }

   public CheckType getEnumration () {
      return enumration;
   }

   public void setEnumration (CheckType enumration) {
      this.enumration = enumration;
   }

   public CheckType getQuantitative () {
      return quantitative;
   }

   public void setQuantitative (CheckType quantitative) {
      this.quantitative = quantitative;
   }

   public CheckType getComparative () {
      return comparative;
   }

   public void setComparative (CheckType comparative) {
      this.comparative = comparative;
   }
}
