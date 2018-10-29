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

import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.type.OperatorType;

/**
 * This bean represents business condition for the business query
 * 
 * @author Vishay
 * @version 1.0
 * @since 18/06/09
 */
public class BusinessCondition {

   private BusinessTerm       lhsBusinessTerm;
   private OperatorType       operator;
   private OperandType        operandType;
   private List<BusinessTerm> rhsBusinessTerms;
   private BusinessQuery      rhsBusinessQuery;
   private List<QueryValue>   rhsValues;
   private Long               conversionId;
   private List<BusinessTerm> lhsBusinessTermVariations;
   private NormalizedDataType normalizedDataType;

   /**
    * @return the normalizedDataType
    */
   public NormalizedDataType getNormalizedDataType () {
      return normalizedDataType;
   }

   /**
    * @param normalizedDataType
    *           the normalizedDataType to set
    */
   public void setNormalizedDataType (NormalizedDataType normalizedDataType) {
      this.normalizedDataType = normalizedDataType;
   }

   public BusinessTerm getLhsBusinessTerm () {
      return lhsBusinessTerm;
   }

   public void setLhsBusinessTerm (BusinessTerm lhsBusinessTerm) {
      this.lhsBusinessTerm = lhsBusinessTerm;
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

   public BusinessQuery getRhsBusinessQuery () {
      return rhsBusinessQuery;
   }

   public void setRhsBusinessQuery (BusinessQuery rhsBusinessQuery) {
      this.rhsBusinessQuery = rhsBusinessQuery;
   }

   public List<QueryValue> getRhsValues () {
      return rhsValues;
   }

   public void setRhsValues (List<QueryValue> rhsValues) {
      this.rhsValues = rhsValues;
   }

   public List<BusinessTerm> getRhsBusinessTerms () {
      return rhsBusinessTerms;
   }

   public void setRhsBusinessTerms (List<BusinessTerm> rhsBusinessTerms) {
      this.rhsBusinessTerms = rhsBusinessTerms;
   }

   public Long getConversionId () {
      return conversionId;
   }

   public void setConversionId (Long conversionId) {
      this.conversionId = conversionId;
   }

   public List<BusinessTerm> getLhsBusinessTermVariations () {
      return lhsBusinessTermVariations;
   }

   public void setLhsBusinessTermVariations (List<BusinessTerm> lhsBusinessTermVariations) {
      this.lhsBusinessTermVariations = lhsBusinessTermVariations;
   }

}
