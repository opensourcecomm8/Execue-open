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


package com.execue.handler.swi.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.Conversion;
import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.bean.qi.QIConversion;
import com.execue.core.common.bean.qi.suggest.SuggestionUnit;
import com.execue.core.common.type.ConversionType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.swi.IConversionServiceHandler;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IConversionService;

/**
 * @author Jtiwari
 * @since 03/07/09
 */
public class ConversionServiceHandlerImpl implements IConversionServiceHandler {

   private IConversionService conversionService;

   public QIConversion getQIConversion (String displayType) throws SWIException {
      QIConversion qConversion = new QIConversion();
      ConversionType conversionType = ConversionType.getType(displayType);
      if (ExecueCoreUtil.isEmpty(displayType) || conversionType == null) {
         return null;
      }
      switch (conversionType) {
         case DATE:
            List<DateFormat> dateFormats = conversionService.getAllDateFormats();
            qConversion = prepareQIConversion(dateFormats);
            break;
         case NUMBER:
            List<Conversion> formatConversions = conversionService.getConversionsByType(conversionType);
            qConversion = prepareQIConversion(formatConversions, new ArrayList<Conversion>());
            break;
         case DISTANCE:
         case CURRENCY:
         case TEMPERATURE:
            List<Conversion> unitConversions = conversionService.getConversionsByType(conversionType);
            List<Conversion> numberFormatConversions = conversionService.getConversionsByType(ConversionType.NUMBER);
            qConversion = prepareQIConversion(numberFormatConversions, unitConversions);
            break;
         default:
            List<Conversion> defaultUnitConversions = conversionService.getConversionsByType(conversionType);
            qConversion = prepareQIConversion(new ArrayList<Conversion>(), defaultUnitConversions);
            break;
      }
      return qConversion;
   }

   private QIConversion prepareQIConversion (List<Conversion> formatConversions, List<Conversion> unitConversions) {
      QIConversion qConversion = new QIConversion();
      List<SuggestionUnit> formatSuggestUnits = new ArrayList<SuggestionUnit>();
      for (Conversion conversion : formatConversions) {
         SuggestionUnit formatSuggestionUnit = new SuggestionUnit();
         formatSuggestionUnit.setConversionId(conversion.getId());
         formatSuggestionUnit.setName(conversion.getFormat());
         formatSuggestionUnit.setDisplayName(conversion.getUnitDisplay());
         formatSuggestUnits.add(formatSuggestionUnit);
      }
      List<SuggestionUnit> suggestUnits = new ArrayList<SuggestionUnit>();
      for (Conversion conversion : unitConversions) {
         SuggestionUnit suggestionUnit = new SuggestionUnit();
         suggestionUnit.setConversionId(conversion.getId());
         suggestionUnit.setName(conversion.getUnit());
         suggestionUnit.setDisplayName(conversion.getUnitDisplay());
         suggestUnits.add(suggestionUnit);
      }
      qConversion.setDataFormats(formatSuggestUnits);
      qConversion.setUnits(suggestUnits);
      return qConversion;
   }

   private QIConversion prepareQIConversion (List<DateFormat> dateFormats) {
      QIConversion qiConversion = new QIConversion();
      List<SuggestionUnit> formatSuggestUnits = new ArrayList<SuggestionUnit>();
      for (DateFormat dateFormat : dateFormats) {
         SuggestionUnit formatSuggestionUnit = new SuggestionUnit();
         formatSuggestionUnit.setConversionId(dateFormat.getId());
         formatSuggestionUnit.setName(dateFormat.getFormat());
         formatSuggestionUnit.setDisplayName(dateFormat.getFormat());
         formatSuggestUnits.add(formatSuggestionUnit);
      }
      qiConversion.setDataFormats(formatSuggestUnits);
      return qiConversion;
   }

   public IConversionService getConversionService () {
      return conversionService;
   }

   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }
}
