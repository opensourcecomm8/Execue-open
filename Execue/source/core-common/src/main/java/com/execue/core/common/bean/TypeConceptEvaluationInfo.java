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


package com.execue.core.common.bean;

import java.util.List;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.ConceptBaseType;
import com.execue.core.common.type.OperationType;

public class TypeConceptEvaluationInfo {

   private BusinessEntityDefinition conceptBed;
   private ConceptBaseType          conceptBaseType;
   private List<BehaviorType>       behaviorTypes;
   private boolean                  nonRealizedType;
   private OperationType            operationType;
   private BusinessEntityDefinition detailedTypeBed;
   private BusinessEntityDefinition valueRealizationBed;

   public BusinessEntityDefinition getConceptBed () {
      return conceptBed;
   }

   public void setConceptBed (BusinessEntityDefinition conceptBed) {
      this.conceptBed = conceptBed;
   }

   public ConceptBaseType getConceptBaseType () {
      return conceptBaseType;
   }

   public void setConceptBaseType (ConceptBaseType conceptBaseType) {
      this.conceptBaseType = conceptBaseType;
   }

   public List<BehaviorType> getBehaviorTypes () {
      return behaviorTypes;
   }

   public void setBehaviorTypes (List<BehaviorType> behaviorTypes) {
      this.behaviorTypes = behaviorTypes;
   }

   public boolean isNonRealizedType () {
      return nonRealizedType;
   }

   public void setNonRealizedType (boolean nonRealizedType) {
      this.nonRealizedType = nonRealizedType;
   }

   public OperationType getOperationType () {
      return operationType;
   }

   public void setOperationType (OperationType operationType) {
      this.operationType = operationType;
   }

   public BusinessEntityDefinition getDetailedTypeBed () {
      return detailedTypeBed;
   }

   public void setDetailedTypeBed (BusinessEntityDefinition detailedTypeBed) {
      this.detailedTypeBed = detailedTypeBed;
   }

   public BusinessEntityDefinition getValueRealizationBed () {
      return valueRealizationBed;
   }

   public void setValueRealizationBed (BusinessEntityDefinition valueRealizationBed) {
      this.valueRealizationBed = valueRealizationBed;
   }

}
