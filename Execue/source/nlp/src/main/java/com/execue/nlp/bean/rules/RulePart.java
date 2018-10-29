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

import java.util.regex.Pattern;

import com.execue.core.common.bean.algorithm.BaseBean;

public class RulePart extends BaseBean {

   /**
    *
    */
   private static final long serialVersionUID = -9165193559808391868L;

   private String            id;
   private String            expression;
   private String            defaultConceptName;
   private boolean           required         = false;
   private String            expandedExpression;
   private Pattern           expandedExpressionPattern;

   /**
    * @return Returns the defaultConceptName.
    */
   public String getDefaultConceptName () {
      return defaultConceptName;
   }

   /**
    * @param defaultConceptName
    *           The defaultConceptName to set.
    */
   public void setDefaultConceptName (String defaultConceptName) {
      this.defaultConceptName = defaultConceptName;
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

   /**
    * @return Returns the expression.
    */
   public String getExpression () {
      return expression;
   }

   /**
    * @param expression
    *           The expression to set.
    */
   public void setExpression (String expression) {
      this.expression = expression;
   }

   /**
    * @return Returns the required.
    */
   public boolean isRequired () {
      return required;
   }

   /**
    * @param required
    *           The required to set.
    */
   public void setRequired (boolean required) {
      this.required = required;
   }

   /**
    * @return Returns the expandedExpresion.
    */
   public String getExpandedExpression () {
      return expandedExpression;
   }

   /**
    * @param expandedExpression
    *           The expandedExpresion to set.
    */
   public void setExpandedExpression (String expandedExpression) {
      this.expandedExpression = expandedExpression;
   }

   public Pattern getExpandedExpressionPattern () {
      return expandedExpressionPattern;
   }

   public void setExpandedExpressionPattern (Pattern expandedExpressionPattern) {
      this.expandedExpressionPattern = expandedExpressionPattern;
   }

}
