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


package com.execue.util;

import java.util.Stack;
import java.util.StringTokenizer;

import com.execue.core.util.ExecueCoreUtil;

/**
 * @author abhijit
 * @since Mar 8, 2008 : 1:04:47 PM
 */
public class ExpressionEvaluator {

   public static boolean evaluateLogicalOperator (double lhsValue, double rhsValue, String operator) {
      boolean outPut = false;
      if ("&lt;".equals(operator) || "<".equals(operator)) {
         outPut = lhsValue < rhsValue;
      } else if ("&gt;".equals(operator) || ">".equals(operator)) {
         outPut = lhsValue > rhsValue;
      } else if ("=".equals(operator)) {
         outPut = lhsValue == rhsValue;
      } else if ("&gt;=".equals(operator) || ">=".equals(operator)) {
         outPut = lhsValue >= rhsValue;
      } else if ("&lt;=".equals(operator) || "<=".equals(operator)) {
         outPut = lhsValue <= rhsValue;
      }
      return outPut;
   }

   public static boolean evaluateLogicalOperator (Object lhsVal, Object rhsVal, Object operator) {
      boolean outPut = false;
      double lhsValue = Double.parseDouble((String) lhsVal);
      double rhsValue = Double.parseDouble((String) rhsVal);
      if ("&lt;".equals(operator) || "<".equals(operator)) {
         outPut = lhsValue < rhsValue;
      } else if ("&gt;".equals(operator) || ">".equals(operator)) {
         outPut = lhsValue > rhsValue;
      } else if ("=".equals(operator)) {
         outPut = lhsValue == rhsValue;
      } else if ("&gt;=".equals(operator) || ">=".equals(operator)) {
         outPut = lhsValue >= rhsValue;
      } else if ("&lt;=".equals(operator) || "<=".equals(operator)) {
         outPut = lhsValue <= rhsValue;
      }
      return outPut;
   }

   public static double evaluateArithmeticOperator (double lhsOutPut, double rhsOutPut, String operator) {
      double outPut = -99;
      if ("-".equals(operator)) {
         outPut = lhsOutPut - rhsOutPut;
      } else if ("+".equals(operator)) {
         outPut = lhsOutPut + rhsOutPut;
      }
      return outPut;
   }

   /**
    * Utility method to replace all occurrences of AND, OR logical operators with &,| logical symbols respectively.
    * 
    * @param expression
    * @return String updated expression
    */
   public static String replaceLogicalOperatorsToSymbols (String expression) {
      if (ExecueCoreUtil.isEmpty(expression)) {
         return expression;
      }
      expression = expression.replaceAll("[Aa][Nn][Dd]", "&");
      expression = expression.replaceAll("[Oo][Rr]", "|");
      return expression;
   }

   /**
    * This method takes the evaluates the logic expression. We can specify the operator AND as either word AND(case
    * insensitive) or symbol &. Similarly operator OR as word OR(case insensitive or symbol |). Space must be present in
    * between each tokens in the expression. Some sample examples of expressions are: expression = "( 0 AND 1 ) OR 1";
    * returns TRUE; expression = "( 1 & 0 ) | ( 0 | 1 ) | ( 1 & 0 )"; returns TRUE; expression = "0 OR ( 1 AND 0 )";
    * returns FALSE;
    * 
    * @param expression
    * @return true/false
    */
   public static boolean evaluate (String expression) {
      boolean evaluation = false;

      // Replace logical operators to logical symbols
      expression = replaceLogicalOperatorsToSymbols(expression);

      StringTokenizer s = new StringTokenizer(expression);

      // Initialize expr stack
      Stack<String> exprStack = new Stack<String>();

      // divides the input into tokens for input
      String token = "", operand1 = "", operator = "";

      // while there is input to be read
      while (s.hasMoreTokens()) {
         token = s.nextToken();
         if (token.equals("(")) {
            exprStack.push(token);
         } else if (token.equals(")")) {
            operand1 = exprStack.pop();
            if (exprStack.isEmpty()) {
               // throw error, if no starting parenthesis exist
               // System.out.println("Invalid expression: " + expression);
               break;
            } else {
               exprStack.pop(); // remove the starting parenthesis
               exprStack.push(operand1); // push back the operand
            }
         } else if (token.equals("&") || token.equals("|")) {
            exprStack.push(token);
         } else {
            if (!exprStack.isEmpty()) {
               operand1 = exprStack.pop();
               if (!operand1.equals("(") && (operand1.equals("&") || operand1.equals("|"))) {
                  if (token.equals("&") || token.equals("|")) {
                     // throw error
                     // System.out.println("Invalid expression: " + expression);
                     break;
                  }
                  operator = operand1;
                  operand1 = exprStack.pop();
                  String newValue = evaluate(Boolean.parseBoolean(operand1), operator, Boolean.parseBoolean(token));
                  exprStack.push(newValue);
               } else {
                  if (token.equals("&") || token.equals("|")) {
                     // throw error
                     // System.out.println("Invalid expression: " + expression);
                     break;
                  }
                  exprStack.push(operand1);
                  exprStack.push(token);
               }
            } else {
               if (token.equals("&") || token.equals("|")) {
                  // throw error
                  // System.out.println("Invalid expression: " + expression);
                  break;
               }
               exprStack.push(token);
            }
         }
      }

      // Finally, evaluate all the remaining operands
      if (!exprStack.isEmpty()) {
         evaluation = evaluateExpressionStack(exprStack);
      }
      return evaluation;
   }

   /**
    * @param evaluation
    * @param exprStack
    * @return
    */
   private static boolean evaluateExpressionStack (Stack<String> exprStack) {
      String token;
      String operand1;
      String operator;
      token = exprStack.pop();
      if (exprStack.isEmpty()) {
         return ExecueCoreUtil.isNotEmpty(token) && "true".equalsIgnoreCase(token);
      } else {
         operator = exprStack.pop();
         if (exprStack.isEmpty()) {
            // throw error and break;
            // System.out.println("Invalid expression left in expression stack: " + exprStack.toArray());
            return false;
         } else {
            operand1 = exprStack.pop();
            try {
               String newValue = evaluate(Boolean.parseBoolean(operand1), operator, Boolean.parseBoolean(token));
               exprStack.push(newValue);
            } catch (NumberFormatException nfe) {
               // throw error and break;
               // System.out.println("Invalid expression left in expression stack: " + operand1 + " " + operator + " "
               // + token);
               return false;
            }
         }
      }
      return evaluateExpressionStack(exprStack);
   }

   public static void main (String args[]) {
      String expr1 = "( false & true ) | true"; // TRUE
      String expr2 = "( true & false ) | ( false | true ) | ( true & false )"; // TRUE
      String expr3 = "false | ( true & false )"; // FALSE
      String expr4 = " false  & ( true & false )"; // INVALID EXPRESSION

      System.out.println(expr1 + ": " + evaluate(expr1));
      System.out.println(expr2 + ": " + evaluate(expr2));
      System.out.println(expr3 + ": " + evaluate(expr3));
      System.out.println(expr4 + ": " + evaluate(expr4));
   }

   private static String evaluate (boolean lshValue, String operator, boolean rhsValue) {
      boolean result = false;
      switch (operator.charAt(0)) {
         case '&': {
            result = lshValue & rhsValue;
            break;
         }
         case '|': {
            result = lshValue | rhsValue;
            break;
         }

         default: {
            result = false;
            break;
         }
      }
      return Boolean.toString(result);
   }

}
