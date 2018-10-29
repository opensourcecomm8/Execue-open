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


package com.execue.swi.service.impl;

import java.util.List;

import com.execue.core.common.bean.entity.Conversion;
import com.execue.core.common.bean.entity.ConversionFormula;
import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.DateQualifier;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.dataaccess.IConversionDataAccessManager;
import com.execue.swi.service.IConversionService;

public class ConversionServiceImpl implements IConversionService {

   private IConversionDataAccessManager conversionDataAccessManager;

   public IConversionDataAccessManager getConversionDataAccessManager () {
      return conversionDataAccessManager;
   }

   public void setConversionDataAccessManager (IConversionDataAccessManager conversionDataAccessManager) {
      this.conversionDataAccessManager = conversionDataAccessManager;
   }

   public Conversion getConversionById (Long conversionId) throws SWIException {
      try {
         return conversionDataAccessManager.getConversionById(conversionId);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public List<Conversion> getConversionsByType (ConversionType conversionType) throws SWIException {
      try {
         return conversionDataAccessManager.getConversionsByType(conversionType);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public ConversionFormula getConversionFormula (ConversionType conversionType, String source, String target)
            throws SWIException {
      try {
         return conversionDataAccessManager.getConversionFormula(conversionType, source, target);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public Conversion getBaseConversion (ConversionType conversionType) throws SWIException {
      try {
         return conversionDataAccessManager.getBaseConversion(conversionType);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public List<DateFormat> getAllSupportedDateFormats () throws SWIException {
      try {
         return conversionDataAccessManager.getAllSupportedDateFormats();
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public List<DateFormat> getAllDateFormats () throws SWIException {
      try {
         return conversionDataAccessManager.getAllDateFormats();
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public DateFormat getDateFormat (Long id) throws SWIException {
      try {
         return conversionDataAccessManager.getDateFormat(id);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public DateFormat getSupportedDateFormat (String format, AssetProviderType assetProviderType) throws SWIException {
      try {
         return conversionDataAccessManager.getSupportedDateFormat(format, assetProviderType);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public List<DateFormat> getSupportedEvaluatedDateFormats (AssetProviderType assetProviderType,
            CheckType isPlainFormat) throws SWIException {
      try {
         return conversionDataAccessManager.getSupportedEvaluatedDateFormats(assetProviderType, isPlainFormat);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public DateQualifier getDateQualifier (String format) throws SWIException {
      try {
         return getConversionDataAccessManager().getDateQualifier(format);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public CheckType isDateFormatPlain (String format) throws SWIException {
      try {
         return getConversionDataAccessManager().isDateFormatPlain(format);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public CheckType isDateFormatSupported (String format) throws SWIException {
      try {
         return getConversionDataAccessManager().isDateFormatSupported(format);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public DataType getDateType (String format) throws SWIException {
      try {
         return getConversionDataAccessManager().getDateType(format);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public Long getQualifierBEDIdByFormatAndProviderType (String format, AssetProviderType assetProviderType)
            throws SWIException {
      try {
         return getConversionDataAccessManager().getQualifierBEDIdByFormatAndProviderType(format, assetProviderType);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public Long getDetailTypeBedId (ConversionType conversionType, String unit) throws SWIException {
      try {
         return getConversionDataAccessManager().getDetailTypeBedId(conversionType, unit);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public Long getValueRealizationBedId (ConversionType conversionType) throws SWIException {
      try {
         return getConversionDataAccessManager().getValueRealizationBedId(conversionType);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   public Conversion getConversionByConceptAndInstanceBedId (Long conceptBedId, Long instanceBedId) throws SWIException {
      try {
         return getConversionDataAccessManager().getConversionByConceptAndInstanceBedId(conceptBedId, instanceBedId);
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }
}
