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


package com.execue.swi.dataaccess;

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

public interface IConversionDataAccessManager {

   public List<Conversion> getConversionsByType (ConversionType conversionType) throws DataAccessException;

   public Conversion getConversionById (Long conversionId) throws DataAccessException;

   public ConversionFormula getConversionFormula (ConversionType conversionType, String source, String target)
            throws DataAccessException;

   public Conversion getBaseConversion (ConversionType conversionType) throws DataAccessException;

   public DateFormat getDateFormat (Long id) throws DataAccessException;

   public DateFormat getSupportedDateFormat (String format, AssetProviderType assetProviderType)
            throws DataAccessException;

   public List<DateFormat> getSupportedEvaluatedDateFormats (AssetProviderType assetProviderType,
            CheckType isPlainFormat) throws DataAccessException;

   public List<DateFormat> getAllSupportedDateFormats () throws DataAccessException;

   public List<DateFormat> getAllDateFormats () throws DataAccessException;

   public DateQualifier getDateQualifier (String format) throws DataAccessException;

   public CheckType isDateFormatPlain (String format) throws DataAccessException;

   public CheckType isDateFormatSupported (String format) throws DataAccessException;

   public DataType getDateType (String format) throws DataAccessException;

   public Long getQualifierBEDIdByFormatAndProviderType (String format, AssetProviderType assetProviderType)
            throws DataAccessException;

   public Long getDetailTypeBedId (ConversionType conversionType, String unit) throws DataAccessException;

   public Long getValueRealizationBedId (ConversionType conversionType) throws DataAccessException;

   public Conversion getConversionByConceptAndInstanceBedId (Long conceptBedId, Long instanceBedId)
            throws DataAccessException;
}
