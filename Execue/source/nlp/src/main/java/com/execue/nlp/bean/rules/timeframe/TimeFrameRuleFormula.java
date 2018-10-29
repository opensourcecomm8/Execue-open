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

import java.util.List;

import com.execue.core.common.bean.algorithm.BaseBean;

public class TimeFrameRuleFormula extends BaseBean {

   /**
    *
    */
   private static final long         serialVersionUID = 8110201573318228055L;

   private String                    id;
   private String                    stringForm;
   private TimeFrameRuleOperand      lhsOpernad;
   private TimeFrameRuleOperand      rhsOpernad;
   private String                    operator;
   private List<TimeFrameRuleResult> results;

   /**
    * @return Returns the id.
    */
   public String getId () {
      return id;
   }

   /**
    * @param id
    *           The id to set.
    */
   public void setId (String id) {
      this.id = id;
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
    * @return Returns the lhsOpernad.
    */
   public TimeFrameRuleOperand getLhsOpernad () {
      return lhsOpernad;
   }

   /**
    * @param lhsOpernad
    *           The lhsOpernad to set.
    */
   public void setLhsOpernad (TimeFrameRuleOperand lhsOpernad) {
      this.lhsOpernad = lhsOpernad;
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

   /**
    * @return Returns the results.
    */
   public List<TimeFrameRuleResult> getResults () {
      return results;
   }

   /**
    * @param results
    *           The results to set.
    */
   public void setResults (List<TimeFrameRuleResult> results) {
      this.results = results;
   }

   /**
    * @return Returns the rhsOpernad.
    */
   public TimeFrameRuleOperand getRhsOpernad () {
      return rhsOpernad;
   }

   /**
    * @param rhsOpernad
    *           The rhsOpernad to set.
    */
   public void setRhsOpernad (TimeFrameRuleOperand rhsOpernad) {
      this.rhsOpernad = rhsOpernad;
   }
}
