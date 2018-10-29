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
import java.util.Map;

import com.execue.core.common.bean.algorithm.BaseBean;
import com.execue.nlp.bean.rules.RuleRegexComponent;

public class TimeFrameRulesContent extends BaseBean {

   private List<RuleRegexComponent>        timeFrameComponents;
   private List<TimeFrameRule>             timeFrameRules;
   private Map<String, StaticTimeFormula>  staticTimeFormulae;
   private Map<String, DynamicTimeFormula> dynamicTimeFormulae;

   private int                             timeFrameRuleCount      = 0;
   private int                             timeFrameComponentCount = 0;

   public List<RuleRegexComponent> getTimeFrameComponents () {
      return timeFrameComponents;
   }

   public void setTimeFrameComponents (List<RuleRegexComponent> timeFrameComponents) {
      this.timeFrameComponents = timeFrameComponents;
      if (getTimeFrameComponents() != null) {
         setTimeFrameComponentCount(getTimeFrameComponents().size());
      }
   }

   public List<TimeFrameRule> getTimeFrameRules () {
      return timeFrameRules;
   }

   public void setTimeFrameRules (List<TimeFrameRule> timeFrameRules) {
      this.timeFrameRules = timeFrameRules;
      if (getTimeFrameRules() != null) {
         setTimeFrameRuleCount(getTimeFrameRules().size());
      }
   }

   public Map<String, StaticTimeFormula> getStaticTimeFormulae () {
      return staticTimeFormulae;
   }

   public void setStaticTimeFormulae (Map<String, StaticTimeFormula> staticTimeFormulae) {
      this.staticTimeFormulae = staticTimeFormulae;
   }

   public Map<String, DynamicTimeFormula> getDynamicTimeFormulae () {
      return dynamicTimeFormulae;
   }

   public void setDynamicTimeFormulae (Map<String, DynamicTimeFormula> dynamicTimeFormulae) {
      this.dynamicTimeFormulae = dynamicTimeFormulae;
   }

   public int getTimeFrameRuleCount () {
      return timeFrameRuleCount;
   }

   public void setTimeFrameRuleCount (int timeFrameRuleCount) {
      this.timeFrameRuleCount = timeFrameRuleCount;
   }

   public int getTimeFrameComponentCount () {
      return timeFrameComponentCount;
   }

   public void setTimeFrameComponentCount (int timeFrameComponentCount) {
      this.timeFrameComponentCount = timeFrameComponentCount;
   }

}
