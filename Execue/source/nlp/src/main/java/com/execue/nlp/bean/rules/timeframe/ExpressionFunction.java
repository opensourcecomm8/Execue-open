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

import java.util.HashMap;
import java.util.Map;

import com.execue.core.common.bean.algorithm.BaseBean;

public class ExpressionFunction extends BaseBean {

   private static final long              serialVersionUID = 5021886008725784661L;

   private ExpressionOperand              lhs;
   private ExpressionOperand              rhs;
   private String                         operator;
   private Map<String, ExpressionOperand> results          = new HashMap<String, ExpressionOperand>(); // function's

   // outcome as
   // boolean
   // against the
   // expressionOperand

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
    * @return Returns the operand.
    */
   public String getOperator () {
      return operator;
   }

   /**
    * @param operator
    *           The operand to set.
    */
   public void setOperator (String operator) {
      this.operator = operator;
   }

   /**
    * @return Returns the results.
    */
   public Map<String, ExpressionOperand> getResults () {
      return results;
   }

   /**
    * @param results
    *           The results to set.
    */
   public void setResults (Map<String, ExpressionOperand> results) {
      this.results = results;
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
}
