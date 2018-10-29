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

public class TimeFrameRuleResult extends BaseBean {

   /**
    *
    */
   private static final long serialVersionUID = -4508109198217004065L;

   private String            id;
   private String            compareOperator;
   private String            value;
   private String            valueType;

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
    * @return Returns the compareOperator.
    */
   public String getCompareOperator () {
      return compareOperator;
   }

   /**
    * @param compareOperator
    *           The compareOperator to set.
    */
   public void setCompareOperator (String compareOperator) {
      this.compareOperator = compareOperator;
   }

   /**
    * @return Returns the value.
    */
   public String getValue () {
      return value;
   }

   /**
    * @param value
    *           The value to set.
    */
   public void setValue (String value) {
      this.value = value;
   }

   /**
    * @return Returns the valueType.
    */
   public String getValueType () {
      return valueType;
   }

   /**
    * @param valueType
    *           The valueType to set.
    */
   public void setValueType (String valueType) {
      this.valueType = valueType;
   }
}
