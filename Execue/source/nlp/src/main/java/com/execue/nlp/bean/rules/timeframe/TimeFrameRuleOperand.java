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
import com.execue.nlp.bean.rules.RuleFunction;

public class TimeFrameRuleOperand extends BaseBean {

   /**
    *
    */
   private static final long    serialVersionUID = -7589813141067881914L;

   private String               id;
   private RuleFunction         function;
   private TimeFrameRuleFormula formula;

   /**
    * @return Returns the formula.
    */
   public TimeFrameRuleFormula getFormula () {
      return formula;
   }

   /**
    * @param formula
    *           The formula to set.
    */
   public void setFormula (TimeFrameRuleFormula formula) {
      this.formula = formula;
   }

   /**
    * @return Returns the function.
    */
   public RuleFunction getFunction () {
      return function;
   }

   /**
    * @param function
    *           The function to set.
    */
   public void setFunction (RuleFunction function) {
      this.function = function;
   }

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
}
