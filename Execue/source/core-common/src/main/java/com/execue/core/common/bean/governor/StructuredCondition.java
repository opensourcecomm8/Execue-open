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


package com.execue.core.common.bean.governor;

import java.util.List;

import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.type.OperatorType;

/**
 * This bean represents structured condition for the structured query
 * 
 * @author Vishay
 * @version 1.0
 * @since 18/06/09
 */
public class StructuredCondition {

   private BusinessAssetTerm         lhsBusinessAssetTerm;
   private OperatorType              operator;
   private OperandType               operandType;
   private List<BusinessAssetTerm>   rhsBusinessAssetTerms;
   private StructuredQuery           rhsStructuredQuery;
   private List<QueryValue>          rhsValues;
   private Long                      conversionId;
   private String                    targetConversionUnit;
   private boolean                   subCondition;
   private List<StructuredCondition> subConditions;
   private BusinessTerm              originalLhsBusinessTerm;

   public BusinessAssetTerm getLhsBusinessAssetTerm () {
      return lhsBusinessAssetTerm;
   }

   public void setLhsBusinessAssetTerm (BusinessAssetTerm lhsBusinessAssetTerm) {
      this.lhsBusinessAssetTerm = lhsBusinessAssetTerm;
   }

   public OperatorType getOperator () {
      return operator;
   }

   public void setOperator (OperatorType operator) {
      this.operator = operator;
   }

   public OperandType getOperandType () {
      return operandType;
   }

   public void setOperandType (OperandType operandType) {
      this.operandType = operandType;
   }

   public StructuredQuery getRhsStructuredQuery () {
      return rhsStructuredQuery;
   }

   public void setRhsStructuredQuery (StructuredQuery rhsStructuredQuery) {
      this.rhsStructuredQuery = rhsStructuredQuery;
   }

   public List<QueryValue> getRhsValues () {
      return rhsValues;
   }

   public void setRhsValues (List<QueryValue> rhsValues) {
      this.rhsValues = rhsValues;
   }

   public List<BusinessAssetTerm> getRhsBusinessAssetTerms () {
      return rhsBusinessAssetTerms;
   }

   public void setRhsBusinessAssetTerms (List<BusinessAssetTerm> rhsBusinessAssetTerms) {
      this.rhsBusinessAssetTerms = rhsBusinessAssetTerms;
   }

   public Long getConversionId () {
      return conversionId;
   }

   public void setConversionId (Long conversionId) {
      this.conversionId = conversionId;
   }

   public String getTargetConversionUnit () {
      return targetConversionUnit;
   }

   public void setTargetConversionUnit (String targetConversionUnit) {
      this.targetConversionUnit = targetConversionUnit;
   }

   public boolean isSubCondition () {
      return subCondition;
   }

   public void setSubCondition (boolean subCondition) {
      this.subCondition = subCondition;
   }

   public BusinessTerm getOriginalLhsBusinessTerm () {
      return originalLhsBusinessTerm;
   }

   public void setOriginalLhsBusinessTerm (BusinessTerm originalLhsBusinessTerm) {
      this.originalLhsBusinessTerm = originalLhsBusinessTerm;
   }

   public List<StructuredCondition> getSubConditions () {
      return subConditions;
   }

   public void setSubConditions (List<StructuredCondition> subConditions) {
      this.subConditions = subConditions;
   }
}