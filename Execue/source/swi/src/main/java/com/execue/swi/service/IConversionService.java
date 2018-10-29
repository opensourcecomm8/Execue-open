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


package com.execue.swi.service;

import java.util.List;

import com.execue.core.common.bean.entity.Conversion;
import com.execue.core.common.bean.entity.ConversionFormula;
import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.DateQualifier;
import com.execue.swi.exception.SWIException;

public interface IConversionService {

   public List<Conversion> getConversionsByType (ConversionType conversionType) throws SWIException;

   public Conversion getConversionById (Long conversionId) throws SWIException;

   public ConversionFormula getConversionFormula (ConversionType conversionType, String source, String target)
            throws SWIException;

   public Long getValueRealizationBedId (ConversionType conversionType) throws SWIException;

   public Long getDetailTypeBedId (ConversionType conversionType, String unit) throws SWIException;

   public Conversion getBaseConversion (ConversionType conversionType) throws SWIException;

   public DateFormat getDateFormat (Long id) throws SWIException;

   public DateFormat getSupportedDateFormat (String format, AssetProviderType assetProviderType) throws SWIException;

   public List<DateFormat> getSupportedEvaluatedDateFormats (AssetProviderType assetProviderType,
            CheckType isPlainFormat) throws SWIException;

   public List<DateFormat> getAllSupportedDateFormats () throws SWIException;

   public List<DateFormat> getAllDateFormats () throws SWIException;

   public DateQualifier getDateQualifier (String format) throws SWIException;

   public CheckType isDateFormatPlain (String format) throws SWIException;

   public CheckType isDateFormatSupported (String format) throws SWIException;

   public Long getQualifierBEDIdByFormatAndProviderType (String format, AssetProviderType assetProviderType)
            throws SWIException;

   public DataType getDateType (String format) throws SWIException;

   public Conversion getConversionByConceptAndInstanceBedId (Long conceptBedId, Long instanceBedId) throws SWIException;

}
