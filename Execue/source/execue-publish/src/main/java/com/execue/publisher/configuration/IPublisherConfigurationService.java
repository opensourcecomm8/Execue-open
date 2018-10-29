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


package com.execue.publisher.configuration;

import java.util.List;
import java.util.Map;

public interface IPublisherConfigurationService {

   public String getDefaultColumnName ();

   public Integer getMaxDataTypeLenght ();

   public Boolean isDropTempTable ();

   public Integer getMaxScaleLenght ();

   public Integer getBatchSize ();

   public Integer getMaxStringColumnLengthLookupEligibility ();

   public Long getMinimumMembersEligibilityLookup ();

   public String getPrefixTableName ();

   public String getEscapeCharacter ();

   public String getDefaultDataType ();

   public String getFallbackDataType ();

   public Integer getColumnCountLimitToFallbackDataType ();

   public String getDateRegEx ();

   public String getIntegerRegEx ();

   public String getNumberRegEx ();

   public String getPlainDateRegEx ();

   public String getEscapeSpecialCharactersRegEx ();

   public String getEscapeSpecialCharactersForTableNameRegEx ();

   public Integer getStringDataTypeSizeBuffer ();

   public Integer getDecimalDataTypePrecisionBuffer ();

   public Integer getDecimalDataTypeScaleBuffer ();

   public Integer getIntegerDataTypeSizeBuffer ();

   public Integer getIntegerDataTypeMaximumLength ();

   public Integer getMinPlainDateFieldLength ();

   public Integer getMinDateFieldLength ();

   public Integer getSampleDateRecordsSize ();

   public String getPublisherFileNameTimeStampFormat ();

   public Integer getTableNameRandomStringLength ();

   public String getColumnValidationMessagePrecision ();

   public String getColumnValidationMessageScale ();

   public String getColumnValidationMessageInvalidDateFormat ();

   public String getColumnValidationMessageInvalidPrecision ();

   public String getColumnValidationMessageInvalidScale ();

   public String getColumnValidationMessageInvalidUnitType ();

   public String getColumnValidationMessageInvalidUnit ();

   public String getColumnValidationMessageInvalidDateType ();

   public List<String> getCharactersToEscape ();

   public List<Long> getLookupEligibilityCriteriaRecords ();

   public List<Double> getLookupEligibilityCriteriaPercentage ();

   public List<Long> getDateLookupEligibilityCriteriaRecords ();

   public List<Double> getDateLookupEligibilityCriteriaPercentage ();

   public Boolean isForceSysteEvalToAmendUserSelectionForLookups ();

   public String getPublisherFileStoragePath (String sourceType);

   public String getOntologyFileStoragePath (String sourceType);

   public void populateScriptAvoidingInjectionMap ();

   public Map<String, String> getScriptAvoidingInjectionMap ();

}
