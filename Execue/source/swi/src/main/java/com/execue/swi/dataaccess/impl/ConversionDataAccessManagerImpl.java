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


package com.execue.swi.dataaccess.impl;

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
import com.execue.dataaccess.swi.dao.IConversionDAO;
import com.execue.dataaccess.swi.dao.IDateFormatDAO;
import com.execue.swi.dataaccess.IConversionDataAccessManager;

public class ConversionDataAccessManagerImpl implements IConversionDataAccessManager {

   private IConversionDAO conversionDAO;
   private IDateFormatDAO dateFormatDAO;

   public IConversionDAO getConversionDAO () {
      return conversionDAO;
   }

   public void setConversionDAO (IConversionDAO conversionDAO) {
      this.conversionDAO = conversionDAO;
   }

   public Conversion getConversionById (Long conversionId) throws DataAccessException {
      return conversionDAO.getById(conversionId, Conversion.class);
   }

   public List<Conversion> getConversionsByType (ConversionType conversionType) throws DataAccessException {
      return conversionDAO.getConversionsByType(conversionType);
   }

   public ConversionFormula getConversionFormula (ConversionType conversionType, String source, String target)
            throws DataAccessException {
      return conversionDAO.getConversionFormula(conversionType, source, target);
   }

   public Conversion getBaseConversion (ConversionType conversionType) throws DataAccessException {
      return conversionDAO.getBaseConversion(conversionType);
   }

   public DateFormat getDateFormat (Long id) throws DataAccessException {
      return dateFormatDAO.getDateFormat(id);
   }

   public DateFormat getSupportedDateFormat (String format, AssetProviderType assetProviderType)
            throws DataAccessException {
      return dateFormatDAO.getSupportedDateFormat(format, assetProviderType);
   }

   public List<DateFormat> getSupportedEvaluatedDateFormats (AssetProviderType assetProviderType,
            CheckType isPlainFormat) throws DataAccessException {
      return dateFormatDAO.getSupportedEvaluatedDateFormats(assetProviderType, isPlainFormat);
   }

   public List<DateFormat> getAllSupportedDateFormats () throws DataAccessException {
      return dateFormatDAO.getAllSupportedDateFormats();
   }

   public DataType getDateType (String format) throws DataAccessException {
      return dateFormatDAO.getDateType(format);
   }

   public Long getQualifierBEDIdByFormatAndProviderType (String format, AssetProviderType assetProviderType)
            throws DataAccessException {
      return dateFormatDAO.getQualifierBEDIdByFormatAndProviderType(format, assetProviderType);
   }

   public List<DateFormat> getAllDateFormats () throws DataAccessException {
      return dateFormatDAO.getAllDateFormats();
   }

   public DateQualifier getDateQualifier (String format) throws DataAccessException {
      return dateFormatDAO.getDateQualifier(format);
   }

   public IDateFormatDAO getDateFormatDAO () {
      return dateFormatDAO;
   }

   public void setDateFormatDAO (IDateFormatDAO dateFormatDAO) {
      this.dateFormatDAO = dateFormatDAO;
   }

   public CheckType isDateFormatPlain (String format) throws DataAccessException {
      return dateFormatDAO.isDateFormatPlain(format);
   }

   public CheckType isDateFormatSupported (String format) throws DataAccessException {
      return dateFormatDAO.isDateFormatSupported(format);
   }

   public Long getDetailTypeBedId (ConversionType conversionType, String unit) throws DataAccessException {
      return conversionDAO.getDetailTypeBedId(conversionType, unit);
   }

   public Long getValueRealizationBedId (ConversionType conversionType) throws DataAccessException {
      return conversionDAO.getValueRealizationBedId(conversionType);
   }

   public Conversion getConversionByConceptAndInstanceBedId (Long conceptBedId, Long instanceBedId)
            throws DataAccessException {
      return conversionDAO.getConversionByConceptAndInstanceBedId(conceptBedId, instanceBedId);
   }

}
