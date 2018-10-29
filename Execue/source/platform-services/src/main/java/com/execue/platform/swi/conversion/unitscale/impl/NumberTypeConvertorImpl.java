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


package com.execue.platform.swi.conversion.unitscale.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Conversion;
import com.execue.core.common.bean.entity.ConversionFormula;
import com.execue.core.common.type.ConversionType;
import com.execue.swi.exception.SWIException;
import com.execue.platform.swi.conversion.unitscale.ITypeConvertor;
import com.execue.platform.swi.conversion.unitscale.exception.UnitScaleConversionException;
import com.execue.platform.swi.conversion.unitscale.exception.UnitScaleConversionExceptionCodes;
import com.execue.swi.service.IConversionService;
import com.execue.util.conversion.expression.arithmetic.IArithmeticExpressionEvaluator;
import com.execue.util.conversion.expression.exception.ArithmeticExprEvalException;

/**
 * This type convertor implementation is for Number formats. If source and target formats are same, then return the same
 * source value
 * 
 * @author Vishay
 * @version 1.0
 * @since 30/06/09
 */
public class NumberTypeConvertorImpl implements ITypeConvertor {

   private static final Logger            log = Logger.getLogger(NumberTypeConvertorImpl.class);

   private IConversionService             conversionService;

   private IArithmeticExpressionEvaluator arithmeticExpressionEvaluator;

   public String convert (Conversion sourceConversion, Conversion targetConversion, String value)
            throws UnitScaleConversionException {
      String sourceFormat = sourceConversion.getFormat();
      String targetFormat = targetConversion.getFormat();
      String targetValue = value;
      try {
         if (!sourceFormat.equalsIgnoreCase(targetFormat)) {
            ConversionFormula conversionFormula = getConversionService().getConversionFormula(ConversionType.NUMBER,
                     sourceFormat, targetFormat);
            if (conversionFormula == null) {
               log.error("Conversion Formula Not Found for Conversion Type [NUMBER], Source Format [" + sourceFormat
                        + "] and Target Format [" + targetFormat + "]");
               return targetValue;
            }
            targetValue = getConvertedValue(value, conversionFormula);
         }
      } catch (SWIException e) {
         throw new UnitScaleConversionException(UnitScaleConversionExceptionCodes.UNIT_SCALE_CONVERSION_FAILED, e);
      } catch (ArithmeticExprEvalException airthmeticExprEvalException) {
         throw new UnitScaleConversionException(airthmeticExprEvalException.getCode(), airthmeticExprEvalException);
      }
      return targetValue;
   }

   private String getConvertedValue (String value, ConversionFormula conversionFormula)
            throws ArithmeticExprEvalException {
      String formula = conversionFormula.getFormula();
      String replacedFormula = formula.replace("?", value);
      return getArithmeticExpressionEvaluator().evaluateExpressionAsString(replacedFormula);
   }

   public IConversionService getConversionService () {
      return conversionService;
   }

   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }

   public IArithmeticExpressionEvaluator getArithmeticExpressionEvaluator () {
      return arithmeticExpressionEvaluator;
   }

   public void setArithmeticExpressionEvaluator (IArithmeticExpressionEvaluator arithmeticExpressionEvaluator) {
      this.arithmeticExpressionEvaluator = arithmeticExpressionEvaluator;
   }

   public String convert (Conversion targetConversion, Date value) throws UnitScaleConversionException {
      // TODO Auto-generated method stub
      return null;
   }
}
