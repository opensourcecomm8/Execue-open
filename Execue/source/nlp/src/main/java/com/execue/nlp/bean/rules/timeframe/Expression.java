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


package com.execue.nlp.bean.rules.timeframe;

import com.execue.core.common.bean.algorithm.BaseBean;

public class Expression extends BaseBean {

   private static final long serialVersionUID = 8154788114695329631L;

   private String            stringForm;
   private ExpressionOperand lhs;
   private ExpressionOperand rhs;
   private String            operator;

   /**
    * @return Returns the lhs.
    */
   public ExpressionOperand getLhs () {
      return lhs;
   }

   /**
    * @param lhs
    *           The lhs to set.
    */
   public void setLhs (ExpressionOperand lhs) {
      this.lhs = lhs;
   }

   /**
    * @return Returns the rhs.
    */
   public ExpressionOperand getRhs () {
      return rhs;
   }

   /**
    * @param rhs
    *           The rhs to set.
    */
   public void setRhs (ExpressionOperand rhs) {
      this.rhs = rhs;
   }

   /**
    * @return Returns the stringForm.
    */
   public String getStringForm () {
      return stringForm;
   }

   /**
    * @param stringForm
    *           The stringForm to set.
    */
   public void setStringForm (String stringForm) {
      this.stringForm = stringForm;
   }

   /**
    * @return Returns the operator.
    */
   public String getOperator () {
      return operator;
   }

   /**
    * @param operator
    *           The operator to set.
    */
   public void setOperator (String operator) {
      this.operator = operator;
   }
}
