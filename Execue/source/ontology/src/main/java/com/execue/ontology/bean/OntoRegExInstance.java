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


package com.execue.ontology.bean;

public class OntoRegExInstance extends OntoInstance {

   private String expression;

   public OntoRegExInstance (String name) {
      super(name);
   }

   public OntoRegExInstance (String name, String expression) {
      super(name);
      this.expression = expression;
   }

   public String getExpression () {
      return this.expression;
   }

   public void setExpression (String expression) {
      this.expression = expression;
   }
}
