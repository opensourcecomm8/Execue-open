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

public class ExpressionOperand extends BaseBean {

   private static final long  serialVersionUID = -563665527795429535L;

   private String             name;
   private String             type;
   private ExpressionFunction function;
   private String             defaultValue;

   /**
    * @return Returns the name.
    */
   public String getName () {
      return name;
   }

   /**
    * @param name
    *           The name to set.
    */
   public void setName (String name) {
      this.name = name;
   }

   /**
    * @return Returns the type.
    */
   public String getType () {
      return type;
   }

   /**
    * @param type
    *           The type to set.
    */
   public void setType (String type) {
      this.type = type;
   }

   /**
    * @return Returns the function.
    */
   public ExpressionFunction getFunction () {
      return function;
   }

   /**
    * @param function
    *           The function to set.
    */
   public void setFunction (ExpressionFunction function) {
      this.function = function;
   }

   public String getDefaultValue () {
      return defaultValue;
   }

   public void setDefaultValue (String defaultValue) {
      this.defaultValue = defaultValue;
   }
}
