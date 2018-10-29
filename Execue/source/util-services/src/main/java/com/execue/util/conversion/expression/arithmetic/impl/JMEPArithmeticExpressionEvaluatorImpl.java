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


package com.execue.util.conversion.expression.arithmetic.impl;

import org.apache.log4j.Logger;

import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.util.conversion.expression.arithmetic.IArithmeticExpressionEvaluator;
import com.execue.util.conversion.expression.exception.ArithmeticExprEvalException;
import com.iabcinc.jmep.Expression;
import com.iabcinc.jmep.XExpression;

/**
 * JMEP is a library from Tigris.org (http://jmep.tigris.org/) for Mathematical Expression Evaluation and Processing.<br/>
 * This class is implementation for IArithmeticExpressionEvaluator based on JMEP.<br/> JMEP requires at least one
 * double value in the expression in order to get the result as Double.<br/> As of Version 4.0, this class handles only
 * Arithmetical Expressions only.
 * 
 * @author Raju Gottumukkala
 * @since 4.0
 */
public class JMEPArithmeticExpressionEvaluatorImpl implements IArithmeticExpressionEvaluator {

   private static final Logger log = Logger.getLogger(JMEPArithmeticExpressionEvaluatorImpl.class);

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.util.IArithmeticExpressionEvaluator#evaluateExpressionAsDouble(java.lang.String)
    */
   public Double evaluateExpressionAsDouble (String expression) throws ArithmeticExprEvalException {
      Double result = null;
      try {
         result = new Double(String.valueOf(evaluateExpression(expression)));
      } catch (XExpression xExpression) {
         log.error(xExpression);
         throw new ArithmeticExprEvalException(ExeCueExceptionCodes.ARITHMETIC_EXPRESSION_EVALUATION_FAILED,
                  xExpression);
      }
      return result;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.util.IArithmeticExpressionEvaluator#evaluateExpressionAsInteger(java.lang.String)
    */
   public Integer evaluateExpressionAsInteger (String expression) throws ArithmeticExprEvalException {
      Integer result = null;
      try {
         result = new Integer(String.valueOf(evaluateExpression(expression)));
      } catch (XExpression xExpression) {
         log.error(xExpression);
         throw new ArithmeticExprEvalException(ExeCueExceptionCodes.ARITHMETIC_EXPRESSION_EVALUATION_FAILED,
                  xExpression);
      }
      return result;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.util.IArithmeticExpressionEvaluator#evaluateExpressionAsString(java.lang.String)
    */
   public String evaluateExpressionAsString (String expression) throws ArithmeticExprEvalException {
      String result = null;
      try {
         result = String.valueOf(evaluateExpression(expression));
      } catch (XExpression xExpression) {
         log.error(xExpression);
         throw new ArithmeticExprEvalException(ExeCueExceptionCodes.ARITHMETIC_EXPRESSION_EVALUATION_FAILED,
                  xExpression);
      }
      return result;
   }

   /**
    * Create an Object of com.iabcinc.jmep.Expression with the passed in expression. Prases the expression and gets the
    * output by evaluating the parsed expression. This method is based on JMEP library.
    * 
    * @param expression
    *           (Arithmetical Expression)
    * @return
    * @throws XExpression
    */
   public Object evaluateExpression (String expression) throws XExpression {
      return (new Expression(expression)).evaluate();
   }

}
