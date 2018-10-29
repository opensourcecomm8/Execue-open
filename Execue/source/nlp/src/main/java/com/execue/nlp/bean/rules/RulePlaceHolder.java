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


package com.execue.nlp.bean.rules;

import java.util.List;

import com.execue.core.common.bean.algorithm.BaseBean;

public class RulePlaceHolder extends BaseBean {

   /**
    *
    */
   private static final long serialVersionUID = 3554417172835063229L;

   private List<String>      ruleIds;
   private RulePlaceValue    placeValue;
   private List<String>      replacementCanidate;                    // list of partIds

   /**
    * @return Returns the placeValue.
    */
   public RulePlaceValue getPlaceValue () {
      return placeValue;
   }

   /**
    * @param placeValue
    *           The placeValue to set.
    */
   public void setPlaceValue (RulePlaceValue placeValue) {
      this.placeValue = placeValue;
   }

   /**
    * @return Returns the replacementCanidate.
    */
   public List<String> getReplacementCanidate () {
      return replacementCanidate;
   }

   /**
    * @param replacementCanidate
    *           The replacementCanidate to set.
    */
   public void setReplacementCanidate (List<String> replacementCanidate) {
      this.replacementCanidate = replacementCanidate;
   }

   /**
    * @return Returns the ruleIds.
    */
   public List<String> getRuleIds () {
      return ruleIds;
   }

   /**
    * @param ruleIds
    *           The ruleIds to set.
    */
   public void setRuleIds (List<String> ruleIds) {
      this.ruleIds = ruleIds;
   }
}
