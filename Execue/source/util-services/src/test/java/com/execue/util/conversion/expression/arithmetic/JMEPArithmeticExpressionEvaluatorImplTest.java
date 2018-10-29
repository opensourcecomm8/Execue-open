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


package com.execue.util.conversion.expression.arithmetic;

import org.apache.log4j.Logger;

import com.execue.util.conversion.expression.arithmetic.impl.JMEPArithmeticExpressionEvaluatorImpl;
import com.execue.util.conversion.expression.exception.ArithmeticExprEvalException;

public class JMEPArithmeticExpressionEvaluatorImplTest {

   private static final Logger log = Logger.getLogger(JMEPArithmeticExpressionEvaluatorImplTest.class);

   public void testEvaluateAsString () {
      try {
         String value = (new JMEPArithmeticExpressionEvaluatorImpl()).evaluateExpressionAsString("2000000/1000000.0");
         System.out.println(value);
      } catch (ArithmeticExprEvalException arithmeticExprEvalException) {
         arithmeticExprEvalException.printStackTrace();
      }
   }

   public static void main (String[] args) {
      new JMEPArithmeticExpressionEvaluatorImplTest().testEvaluateAsString();
   }
}
