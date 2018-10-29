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


package com.execue.nlp.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueStringUtil;
import com.execue.nlp.bean.rules.timeframe.DynamicTimeFormula;
import com.execue.nlp.bean.rules.timeframe.ExpressionFunction;
import com.execue.nlp.bean.rules.timeframe.ExpressionOperand;
import com.execue.nlp.bean.rules.timeframe.StaticTimeFormula;
import com.execue.nlp.bean.rules.timeframe.TimeFrameRulesContent;

public class TimeFrameRuleUtilities {

   private static final Logger log = Logger.getLogger(TimeFrameRuleUtilities.class);

   private TimeFrameRuleUtilities () {

   }

   public static String evaluateFormula (String formulaName, String unitValue, String unitType,
            Map<String, String> args, TimeFrameRulesContent rulesContent) {
      Object ref = rulesContent.getStaticTimeFormulae().get(formulaName);
      if (ref != null) {
         return evaluateStaticFormulaToNormalizedValues(formulaName, unitType, rulesContent);
      }
      ref = rulesContent.getDynamicTimeFormulae().get(formulaName);
      if (ref != null) {
         return evaluateDynamicFormula(formulaName, unitValue, unitType, args, rulesContent);
      }
      return "-99";
   }

   public static String evaluateFormulaDatasetSpecific (String formulaName, String unitValue, String unitType,
            Map args, TimeFrameRulesContent rulesContent) {
      Object ref = rulesContent.getStaticTimeFormulae().get(formulaName);
      if (ref != null) {
         return evaluateStaticFormulaDatasetSpecific(formulaName, unitType, args, rulesContent);
      }
      ref = rulesContent.getDynamicTimeFormulae().get(formulaName);
      if (ref != null) {
         return evaluateDynamicFormulaDatasetSpecific(formulaName, unitValue, unitType, args, rulesContent);
      }
      return "-99";
   }

   public static String evaluateStaticFormulaDatasetSpecific (String formulaName, String unitType, Map args,
            TimeFrameRulesContent rulesContent) {
      String output = "-99";

      output = evaluateStaticFormula(formulaName, unitType, rulesContent);

      // For Quarters and Months we need to get the Year Value also
      String yearValue = null;
      if (!"YEAR".equals(unitType)) {
         if (args.size() > 0) {
            yearValue = (String) args.get("yearUnit");
         } else {
            yearValue = evaluateStaticFormula(formulaName, "YEAR", rulesContent);
         }
      }

      if ("MONTH".equals(unitType)) {
         output = NLPUtilities.getNormalizedYearValue(yearValue).substring(1)
                  + ExecueConstants.TFM_MONTH.get(output).toString().substring(1);
      } else if ("QUARTER".equals(unitType)) {
         output = NLPUtilities.getNormalizedYearValue(yearValue).substring(1)
                  + NLPUtilities.getNormalizedQuarterValue(output).substring(1);
      } else if ("YEAR".equals(unitType)) {
         output = NLPUtilities.getNormalizedYearValue(output).substring(1);
      } else {
         log.error("Wrong Unit Type Came In" + unitType);
      }
      return output;
   }

   public static String evaluateStaticFormulaToNormalizedValues (String formulaName, String unitType,
            TimeFrameRulesContent rulesContent) {
      String output = "-99";

      output = evaluateStaticFormula(formulaName, unitType, rulesContent);

      // For Quarters and Months we need to get the Year Value also
      String yearValue = null;
      if (!"YEAR".equals(unitType)) {
         yearValue = evaluateStaticFormula(formulaName, "YEAR", rulesContent);
      }

      if ("MONTH".equals(unitType)) {
         output = ExecueConstants.TFM_MONTH.get(output) + "-" + NLPUtilities.getNormalizedYearValue(yearValue);
      } else if ("QUARTER".equals(unitType)) {
         output = NLPUtilities.getNormalizedQuarterValue(output) + "-" + NLPUtilities.getNormalizedYearValue(yearValue);
      } else if ("YEAR".equals(unitType)) {
         output = "Year" + NLPUtilities.getNormalizedYearValue(output);
      } else {
         log.error("Wrong Unit Type Came In" + unitType);
      }

      return output;
   }

   public static String evaluateStaticFormula (String formulaName, String unitType, TimeFrameRulesContent rulesContent) {
      String unitValue = "-99";
      StaticTimeFormula formula;
      Object ref = rulesContent.getStaticTimeFormulae().get(formulaName);
      if (ref != null) {
         formula = (StaticTimeFormula) ref;
         unitValue = formula.getUnitValue(unitType);
      } else {
         log.error("Static Formula Not Found in Config : " + formulaName);
      }
      return unitValue;

   }

   public static String evaluateDynamicFormula (String formulaName, String unitValue, String unitType, Map args,
            TimeFrameRulesContent rulesContent) {
      String output = "-99";
      unitType = unitType.toUpperCase();
      DynamicTimeFormula formula;
      Object ref = rulesContent.getDynamicTimeFormulae().get(formulaName);
      if (unitType.startsWith("ABSTRACT")) {
         ref = rulesContent.getDynamicTimeFormulae().get("ABSTRACT".toLowerCase() + formulaName);
      }
      if (ref == null) {
         log.error("Dynamic Formula Not Found in Config : " + formulaName);
         return output;
      }
      formula = (DynamicTimeFormula) ref;
      ExpressionOperand lhs = formula.getExpression().getLhs();
      ExpressionOperand rhs = formula.getExpression().getRhs();
      String lhsValue = null;
      String rhsValue = null;
      String yearUnit = null;
      String secondaryTimeUnit = null;
      String secondaryTimeUnitValue = null;

      if (args.size() > 0) {
         secondaryTimeUnit = (String) args.get("secondaryTimeUnit");
         secondaryTimeUnitValue = (String) args.get("secondaryTimeUnitValue");
      }

      ExpressionFunction lhsExpFunction;
      if (lhs.getFunction() != null) {
         lhsExpFunction = lhs.getFunction();
         if (secondaryTimeUnitValue != null) {
            lhs = evaluateExpressionFunction(lhsExpFunction, secondaryTimeUnit, secondaryTimeUnitValue, rulesContent);
         } else {
            lhs = (ExpressionOperand) lhsExpFunction.getResults().get("default");
         }
      }
      if ("KnowledgeBase".equals(lhs.getType())) {
         lhsValue = evaluateStaticFormula(lhs.getName(), unitType, rulesContent);
         yearUnit = NLPUtilities.getNormalizedYearValue(evaluateStaticFormula(lhs.getName(), "YEAR", rulesContent));
      } else {
         lhsValue = unitValue;
      }

      ExpressionFunction rhsExpFunction;
      if (rhs.getFunction() != null) {
         rhsExpFunction = rhs.getFunction();
         if (secondaryTimeUnitValue != null) {
            rhs = evaluateExpressionFunction(rhsExpFunction, secondaryTimeUnit, secondaryTimeUnitValue, rulesContent);
         } else {
            rhs = (ExpressionOperand) rhsExpFunction.getResults().get("default");
         }
      }
      if ("KnowledgeBase".equals(rhs.getType())) {
         rhsValue = evaluateStaticFormula(rhs.getName(), unitType, rulesContent);
         yearUnit = NLPUtilities.getNormalizedYearValue(evaluateStaticFormula(rhs.getName(), "YEAR", rulesContent));
      } else {
         rhsValue = unitValue;
      }

      // if (NLPConfigurationContext.showLog() && log.isDebugEnabled()) {
      // log.debug("Lhs Type : " + lhs.getType());
      // log.debug("Lhs Name : " + lhs.getName());
      // log.debug("Rhs Type : " + rhs.getType());
      // log.debug("Rhs Name : " + rhs.getName());
      // }

      if (args.size() > 0) {
         if ("YEAR".equals(secondaryTimeUnit)) {
            if (log.isDebugEnabled())
               log.debug("Year Taken From Secondary Unit Type");
            yearUnit = NLPUtilities.getNormalizedYearValue(secondaryTimeUnitValue);
         }
      }
      if (unitType.startsWith("ABSTRACT")) {
         output = getAbstractTimeRange(lhsValue, rhsValue, formula.getExpression().getOperator(), unitType);
      } else {
         output = getTimeRange(lhsValue, rhsValue, formula.getExpression().getOperator(), unitType, yearUnit);
      }
      return output;
   }

   public static String evaluateDynamicFormulaDatasetSpecific (String formulaName, String unitValue, String unitType,
            Map args, TimeFrameRulesContent rulesContent) {
      String output = "-99";
      unitType = unitType.toUpperCase();
      DynamicTimeFormula formula;
      Object ref = rulesContent.getDynamicTimeFormulae().get(formulaName);
      if (unitType.startsWith("ABSTRACT")) {
         ref = rulesContent.getDynamicTimeFormulae().get("ABSTRACT".toLowerCase() + formulaName);
      }
      if (ref == null) {
         log.error("Dynamic Formula Not Found in Config : " + formulaName);
         return output;
      }

      formula = (DynamicTimeFormula) ref;
      ExpressionOperand rhs = formula.getExpression().getRhs();
      String lhsValue = null;
      String rhsValue = null;
      String yearUnit = null;

      if (args.size() > 0) {
         lhsValue = (String) args.get("lhsValue");
         yearUnit = (String) args.get("yearUnit");
      } else {
         throw (new RuntimeException("LHS Value and Year Unit must be specified by calling method"));
      }

      if ("KnowledgeBase".equals(rhs.getType())) {
         if (rhs.getFunction() != null) {
            rhs = (ExpressionOperand) rhs.getFunction().getResults().get("default");
         }
         rhsValue = evaluateStaticFormula(rhs.getName(), unitType, rulesContent);
      } else {
         rhsValue = unitValue;
      }
      if (rhsValue == null) {
         if (rhs.getDefaultValue() != null) {
            rhsValue = rhs.getDefaultValue();
         } else {
            throw (new RuntimeException("RHS Value is NULL for formula " + formulaName));
         }
      }
      output = getTimeRangeDatasetSpecific(lhsValue, rhsValue, formula.getExpression().getOperator(), unitType,
               yearUnit);
      return output;
   }

   private static String getAbstractTimeRange (String lhs, String rhs, String operator, String unitType) {
      String timeRange = "-99";
      String startUnit;
      String endUnit;
      int startValue = 0;
      int endValue = 0;
      if (log.isDebugEnabled()) {
         log.debug("lhs : " + lhs);
         log.debug("rhs : " + rhs);
         log.debug("operator : " + operator);
         log.debug("unitType : " + unitType);
      }
      if ("-".equals(operator)) {
         startValue = Integer.parseInt(lhs) - Integer.parseInt(rhs);
         if (startValue < 0) {
            // TODO Throw ExecueNLPException - Maximum Fiscal Timeframe Limit is (lhs) value
         } else {
            startValue = startValue + 1;
         }
         endValue = Integer.parseInt(lhs);
      } else if ("+".equals(operator)) {
         endValue = Integer.parseInt(lhs) + Integer.parseInt(rhs);
         endValue = endValue - 1;

         startValue = Integer.parseInt(lhs);
      }

      if ("ABSTRACTMONTH".equals(unitType)) {
         startUnit = NLPUtilities.getNormalizedRelativeMonthValue((String.valueOf(startValue)));
         endUnit = NLPUtilities.getNormalizedRelativeMonthValue((String.valueOf(endValue)));
         timeRange = startUnit + " -- " + endUnit;
         if (startUnit.equalsIgnoreCase(endUnit))
            timeRange = startUnit;
      } else if ("ABSTRACTQUARTER".equals(unitType)) {
         startUnit = NLPUtilities.getNormalizedRelativeQuarterValue((String.valueOf(startValue)));
         endUnit = NLPUtilities.getNormalizedRelativeQuarterValue((String.valueOf(endValue)));
         timeRange = startUnit + " -- " + endUnit;
         if (startUnit.equalsIgnoreCase(endUnit))
            timeRange = startUnit;
      } else if ("ABSTRACTYEAR".equals(unitType)) {
         startUnit = NLPUtilities.getNormalizedRelativeYearValue(String.valueOf(startValue));
         endUnit = NLPUtilities.getNormalizedRelativeYearValue(String.valueOf(endValue));
         timeRange = startUnit + " -- " + endUnit;
         if (startUnit.equalsIgnoreCase(endUnit))
            timeRange = startUnit;
      } else {
         log.error("Wrong Unit Type Came In" + unitType);
         return timeRange;
      }
      return timeRange;
   }

   private static String getTimeRange (String lhs, String rhs, String operator, String unitType, String yearUnit) {
      String timeRange = "-99";
      String startUnit;
      String endUnit;
      String endYearUnit = yearUnit;
      int startValue = 0;
      int endValue = 0;
      if (log.isDebugEnabled()) {
         log.debug("lhs : " + lhs);
         log.debug("rhs : " + rhs);
         log.debug("operator : " + operator);
         log.debug("unitType : " + unitType);
         log.debug("yearUnit : " + yearUnit);
      }
      if ("-".equals(operator)) {
         startValue = Integer.parseInt(lhs) - Integer.parseInt(rhs);
         if (startValue < 0) {
            String month = "0";
            int mon = 0;
            if ("MONTH".equals(unitType)) {
               month = lhs;
               mon = Integer.parseInt(rhs);
            } else if ("QUARTER".equals(unitType)) {
               month = String.valueOf(Integer.parseInt(lhs) * 3);
               mon = Integer.parseInt(rhs) * 3;
            } else {

               log.error(" lhs - rhs is negative, for Year Type. Handle This : " + lhs + " - " + rhs);
               return timeRange;
            }
            String startDate = month + "-" + yearUnit.substring(1); // Cards

            // String startDate = month + "-" + yearUnit;

            startDate = reduceMonths(startDate, mon);
            if ("MONTH".equals(unitType)) {
               startValue = Integer.parseInt(startDate.split("-")[0]);
            } else if ("QUARTER".equals(unitType)) {
               mon = Integer.parseInt(startDate.split("-")[0]);
               if (mon > 0 && mon <= 3) {
                  startValue = 1;
               } else if (mon > 3 && mon <= 6) {
                  startValue = 2;
               } else if (mon > 6 && mon <= 9) {
                  startValue = 3;
               } else {
                  startValue = 4;
               }
            }
            yearUnit = "Y" + startDate.split("-")[1]; // Cards

            // yearUnit = startDate.split("-")[1];

            if (log.isDebugEnabled()) {
               log.debug("startValue : " + startValue);
               log.debug("End Year Unit : " + yearUnit);
            }
         } else {
            startValue = startValue + 1;
         }
         if (Integer.parseInt(lhs) < Integer.parseInt(rhs) && yearUnit.equals(endYearUnit)) {
            endValue = Integer.parseInt(rhs);
         } else {
            endValue = Integer.parseInt(lhs);
         }

      } else if ("+".equals(operator)) {
         endValue = Integer.parseInt(lhs) + Integer.parseInt(rhs);
         if (("MONTH".equals(unitType) && startValue > 12) || ("QUARTER".equals(unitType) && startValue > 4)) {

            log.error(" lhs + rhs is more than valid value, Handle This : " + lhs + " - " + rhs);
            // We need to increase an year or more to get proper values
            return timeRange;
         }
         endValue = endValue - 1;
         if (Integer.parseInt(lhs) < Integer.parseInt(rhs)) {
            startValue = Integer.parseInt(lhs);
         } else {
            startValue = Integer.parseInt(rhs);
         }

      }

      if ("MONTH".equals(unitType)) {
         startUnit = (String) ExecueConstants.TFM_MONTH.get(String.valueOf(startValue));
         endUnit = (String) ExecueConstants.TFM_MONTH.get(String.valueOf(endValue));
         timeRange = getTimeRangeFinalValue(yearUnit, startUnit, endUnit, endYearUnit);
      } else if ("QUARTER".equals(unitType)) {
         startUnit = NLPUtilities.getNormalizedQuarterValue((String.valueOf(startValue)));
         endUnit = NLPUtilities.getNormalizedQuarterValue((String.valueOf(endValue)));
         timeRange = getTimeRangeFinalValue(yearUnit, startUnit, endUnit, endYearUnit);
      } else if ("YEAR".equals(unitType)) {
         startUnit = NLPUtilities.getNormalizedYearValue(String.valueOf(startValue));
         endUnit = NLPUtilities.getNormalizedYearValue(String.valueOf(endValue));
         timeRange = startUnit + " -- " + endUnit;
         if (startUnit.equalsIgnoreCase(endUnit))
            timeRange = startUnit;
      } else {
         log.error("Wrong Unit Type Came In" + unitType);
         return timeRange;
      }
      return timeRange;
   }

   private static String getTimeRangeDatasetSpecific (String lhs, String rhs, String operator, String unitType,
            String yearUnit) {
      String timeRange = "-99";
      String startUnit;
      String endUnit;
      String endYearUnit = yearUnit;
      int startValue = 0;
      int endValue = 0;
      if (log.isDebugEnabled()) {
         log.debug("lhs : " + lhs);
         log.debug("rhs : " + rhs);
         log.debug("operator : " + operator);
         log.debug("unitType : " + unitType);
         log.debug("yearUnit : " + yearUnit);
      }
      if ("-".equals(operator)) {
         startValue = Integer.parseInt(lhs) - Integer.parseInt(rhs);
         if (startValue < 0) {
            String month = "0";
            int mon = 0;
            if ("MONTH".equals(unitType)) {
               month = lhs;
               mon = Integer.parseInt(rhs);
            } else if ("QUARTER".equals(unitType)) {
               month = String.valueOf(Integer.parseInt(lhs) * 3);
               mon = Integer.parseInt(rhs) * 3;
            } else {

               log.error(" lhs - rhs is negative, for Year Type. Handle This : " + lhs + " - " + rhs);
               return timeRange;
            }
            String startDate = month + "-" + yearUnit; // Cards

            startDate = reduceMonths(startDate, mon);
            if ("MONTH".equals(unitType)) {
               startValue = Integer.parseInt(startDate.split("-")[0]);
            } else if ("QUARTER".equals(unitType)) {
               mon = Integer.parseInt(startDate.split("-")[0]);
               if (mon > 0 && mon <= 3) {
                  startValue = 1;
               } else if (mon > 3 && mon <= 6) {
                  startValue = 2;
               } else if (mon > 6 && mon <= 9) {
                  startValue = 3;
               } else {
                  startValue = 4;
               }
            }
            yearUnit = startDate.split("-")[1]; // Cards

            if (log.isDebugEnabled()) {
               log.debug("startValue : " + startValue);
               log.debug("End Year Unit : " + yearUnit);
            }
         } else {
            startValue = startValue + 1;
         }
         if (Integer.parseInt(lhs) < Integer.parseInt(rhs) && yearUnit.equals(endYearUnit)) {
            endValue = Integer.parseInt(rhs);
         } else {
            endValue = Integer.parseInt(lhs);
         }

      } else if ("+".equals(operator)) {
         endValue = Integer.parseInt(lhs) + Integer.parseInt(rhs);
         if (("MONTH".equals(unitType) && startValue > 12) || ("QUARTER".equals(unitType) && startValue > 4)) {
            log.error(" lhs + rhs is more than valid value, Handle This : " + lhs + " - " + rhs);
            // We need to increase an year or more to get proper values
            return timeRange;
         }
         endValue = endValue - 1;
         if (Integer.parseInt(lhs) < Integer.parseInt(rhs)) {
            startValue = Integer.parseInt(lhs);
         } else {
            startValue = Integer.parseInt(rhs);
         }
      }

      if ("MONTH".equals(unitType)) {
         startUnit = (String) ExecueConstants.TFM_MONTH.get(String.valueOf(startValue));
         if (startUnit.startsWith("M"))
            startUnit = startUnit.substring(1);
         endUnit = (String) ExecueConstants.TFM_MONTH.get(String.valueOf(endValue));
         if (endUnit.startsWith("M"))
            endUnit = endUnit.substring(1);

         timeRange = getTimeRangeFinalValueDatasetSpecific(yearUnit, startUnit, endUnit, endYearUnit);
      } else if ("QUARTER".equals(unitType)) {
         startUnit = NLPUtilities.getNormalizedQuarterValue((String.valueOf(startValue)));
         if (startUnit.startsWith("Q"))
            startUnit = startUnit.substring(2);
         endUnit = NLPUtilities.getNormalizedQuarterValue((String.valueOf(endValue)));
         if (endUnit.startsWith("Q"))
            endUnit = endUnit.substring(2);

         timeRange = getTimeRangeFinalValueDatasetSpecific(yearUnit, startUnit, endUnit, endYearUnit);
      } else if ("YEAR".equals(unitType)) {
         startUnit = NLPUtilities.getNormalizedYearValue(String.valueOf(startValue));
         if (startUnit.startsWith("Y"))
            startUnit = startUnit.substring(1);
         endUnit = NLPUtilities.getNormalizedYearValue(String.valueOf(endValue));
         if (endUnit.startsWith("Y"))
            endUnit = endUnit.substring(1);
         timeRange = startUnit + " -- " + endUnit;
         if (startUnit.equalsIgnoreCase(endUnit))
            timeRange = startUnit;
      } else {
         log.error("Wrong Unit Type Came In" + unitType);
         return timeRange;
      }
      return timeRange;
   }

   private static String getTimeRangeFinalValue (String yearUnit, String startUnit, String endUnit, String endYearUnit) {
      String timeRange;

      // Cards
      timeRange = startUnit + "-" + yearUnit + " -- " + endUnit + "-" + endYearUnit;
      if ((startUnit + "-" + yearUnit).equalsIgnoreCase(endUnit + "-" + endYearUnit))
         timeRange = startUnit + "-" + yearUnit;

      return timeRange;
   }

   private static String getTimeRangeFinalValueDatasetSpecific (String yearUnit, String startUnit, String endUnit,
            String endYearUnit) {
      String timeRange;

      // Newer Implementations
      timeRange = yearUnit + startUnit + " -- " + endYearUnit + endUnit;
      if ((yearUnit + startUnit).equalsIgnoreCase(endYearUnit + endUnit))
         timeRange = yearUnit + startUnit;

      return timeRange;
   }

   // TODO - Synonyms also needs to be checked here
   public static String getNormalizedUnitType (String timeUnit) {
      String unitType = timeUnit;
      if (ExecueStringUtil.matches(timeUnit, "month") || ExecueConstants.TFM_MONTH_SYNONYMS.contains(timeUnit)) {
         unitType = "MONTH";
      } else if (ExecueStringUtil.matches(timeUnit.toLowerCase(), "quarter")
               || ExecueConstants.TFM_QUARTER_SYNONYMS.contains(timeUnit)) {
         unitType = "QUARTER";
      } else if (ExecueStringUtil.matches(timeUnit.toLowerCase(), "year")
               || ExecueConstants.TFM_YEAR_SYNONYMS.contains(timeUnit)) {
         unitType = "YEAR";
      } else if (ExecueStringUtil.matches(timeUnit.toLowerCase(), "fiscalyear")
               || ExecueConstants.TFM_FISCAL_YEAR_SYNONYMS.contains(timeUnit)) {
         unitType = "ABSTRACTYEAR";
      } else if (ExecueStringUtil.matches(timeUnit.toLowerCase(), "fiscalmonth")
               || ExecueConstants.TFM_FISCAL_MONTH_SYNONYMS.contains(timeUnit)) {
         unitType = "ABSTRACTMONTH";
      } else if (ExecueStringUtil.matches(timeUnit.toLowerCase(), "fiscalquarter")
               || ExecueConstants.TFM_FISCAL_QUARTER_SYNONYMS.contains(timeUnit)) {
         unitType = "ABSTRACTQUARTER";
      }
      return unitType;
   }

   private static ExpressionOperand evaluateExpressionFunction (ExpressionFunction function, String enclosingTimeUnit,
            String enclosingTimeUnitValue, TimeFrameRulesContent rulesContent) {
      ExpressionOperand operand;

      ExpressionOperand lhs = function.getLhs();
      ExpressionOperand rhs = function.getRhs();

      double lhsValue = 0;
      double rhsValue = 0;

      if ("KnowledgeBase".equals(lhs.getType())) {
         lhsValue = Double.parseDouble(evaluateStaticFormula(lhs.getName(), enclosingTimeUnit, rulesContent));
      } else {
         lhsValue = Double.parseDouble(enclosingTimeUnitValue);
      }

      if ("KnowledgeBase".equals(rhs.getType())) {
         rhsValue = Double.parseDouble(evaluateStaticFormula(rhs.getName(), enclosingTimeUnit, rulesContent));
      } else {
         rhsValue = Double.parseDouble(enclosingTimeUnitValue);
      }

      String outcome = "default";

      if ("<".equals(function.getOperator())) {
         outcome = String.valueOf(lhsValue < rhsValue);
      } else if (">".equals(function.getOperator())) {
         outcome = String.valueOf(lhsValue > rhsValue);
      } else if (">=".equals(function.getOperator())) {
         outcome = String.valueOf(lhsValue >= rhsValue);
      } else if ("<=".equals(function.getOperator())) {
         outcome = String.valueOf(lhsValue <= rhsValue);
      } else if ("=".equals(function.getOperator())) {
         outcome = String.valueOf(lhsValue == rhsValue);
      }

      operand = (ExpressionOperand) function.getResults().get(outcome);

      return operand;
   }

   public static String reduceMonths (String toDate, int months) {
      String startDate;
      String my[] = toDate.split("-");
      int month = Integer.parseInt(my[0]);
      int year = Integer.parseInt(my[1]);
      GregorianCalendar cal = new GregorianCalendar();
      cal.set(year, month, 1);
      cal.add(Calendar.MONTH, -(months));
      startDate = formatDate(cal.getTime());
      return startDate;
   }

   private static String formatDate (Date date) {
      DateFormat f = new SimpleDateFormat("MM-yyyy");
      return f.format(date);
   }
}
