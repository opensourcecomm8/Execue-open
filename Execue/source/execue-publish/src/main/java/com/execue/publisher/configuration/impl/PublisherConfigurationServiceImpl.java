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


package com.execue.publisher.configuration.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.configuration.IConfiguration;
import com.execue.publisher.configuration.IPublisherConfigurationService;

public class PublisherConfigurationServiceImpl implements IPublisherConfigurationService {

   private static final String              DEFAULT_COLUMN_NAME_KEY                                     = "publisher.static-values.default-column-name";
   private static final String              DATA_BATCH_SIZE_KEY                                         = "publisher.static-values.batch-size";
   private static final String              MAX_DATA_TYPE_LENGTH_KEY                                    = "publisher.static-values.max-data-type-length";
   private static final String              MAX_SCALE_LENGTH_KEY                                        = "publisher.static-values.max-scale-length";
   private static final String              CHARACTERS_TO_ESCAPE_KEY                                    = "publisher.static-values.characters-to-escape";
   private static final String              PREFIX_TABLE_NAME_KEY                                       = "publisher.static-values.prefix-table-name";
   private static final String              ESCAPE_CHARACTER_KEY                                        = "publisher.static-values.escape-character";
   private static final String              DEFAULT_DATA_TYPE_KEY                                       = "publisher.static-values.default-data-type";
   private static final String              FALLBACK_DATA_TYPE_KEY                                      = "publisher.static-values.fallback-data-type";
   private static final String              COLUMN_COUNT_LIMIT_FOR_FALLBACK_DATA_TYPE                   = "publisher.static-values.column-count-limit-to-fallback-data-type";
   private static final String              FORCE_SYSTEM_EVALUATION_TO_AMEND_USER_SELECTION_FOR_LOOKUPS = "publisher.static-values.force-system-evaluation-to-amend-user-selection-for-lookups";
   private static final String              NUMBER_REGEX_KEY                                            = "publisher.static-values.number-regex";
   private static final String              INTEGER_REGEX_KEY                                           = "publisher.static-values.integer-regex";
   private static final String              DATE_REGEX_KEY                                              = "publisher.static-values.date-regex";
   private static final String              PLAIN_DATE_REGEX_KEY                                        = "publisher.static-values.plain-date-regex";
   private static final String              SPECIAL_CHARACTERS_REGEX_KEY                                = "publisher.static-values.escape-special-characters-regex";
   private static final String              STRING_DATA_TYPE_SIZE_BUFFER_KEY                            = "publisher.static-values.string-data-type-size-buffer";
   private static final String              INTEGER_DATA_TYPE_SIZE_BUFFER_KEY                           = "publisher.static-values.integer-data-type-size-buffer";
   private static final String              DECIMAL_DATA_TYPE_PRECISION_BUFFER_KEY                      = "publisher.static-values.decimal-data-type-precision-buffer";
   private static final String              DECIMAL_DATA_TYPE_SCALE_BUFFER_KEY                          = "publisher.static-values.decimal-data-type-scale-buffer";
   private static final String              MIN_DATE_FIELD_LENGTH_KEY                                   = "publisher.static-values.min-date-field-length";
   private static final String              MIN_PLAIN_DATE_FIELD_LENGTH_KEY                             = "publisher.static-values.min-plain-date-field-length";
   private static final String              SAMPLE_DATE_RECORDS_SIZE_KEY                                = "publisher.static-values.sample-date-records-size";
   private static final String              DROP_TEMP_TABLE_KEY                                         = "publisher.static-values.drop-temp-table";
   private static final String              LOOKUP_ELIGIBILITY_CRITERIA_PERCENTAGES_KEY                 = "publisher.static-values.lookup-eligibility-criteria.criteria.percentage";
   private static final String              LOOKUP_ELIGIBILITY_CRITERIA_RECORDS_KEY                     = "publisher.static-values.lookup-eligibility-criteria.criteria.records";
   private static final String              DATE_LOOKUP_ELIGIBILITY_CRITERIA_PERCENTAGES_KEY            = "publisher.static-values.date-lookup-eligibility-criteria.criteria.percentage";
   private static final String              DATE_LOOKUP_ELIGIBILITY_CRITERIA_RECORDS_KEY                = "publisher.static-values.date-lookup-eligibility-criteria.criteria.records";
   private static final String              MINIMUM_MEMBER_ELIGIBLITY_LOOKUP_KEY                        = "publisher.static-values.minimum-members-eligibility-lookup";
   private static final String              MAX_STRING_COLUMN_LENGTH_LOOKUP_ELIGIBLITY_KEY              = "publisher.static-values.max-string-column-length-lookup-eligiblity";
   private static final String              PUBLISHER_FILE_STORAGE_PATH_KEY                             = "publisher.static-values.publisher-file-storage-path";
   private static final String              PUBLISHER_FILENAME_TIMESTAMP_FORMAT_KEY                     = "publisher.static-values.publisher-filename-timestamp-format";
   private static final String              PUBLISHER_TABLE_NAME_RANDOM_STRING_LENGTH_KEY               = "publisher.static-values.tablename-randomstring-length";
   private static final String              SPECIAL_CHARACTERS_REGEX_FOR_TABLE_NAME_KEY                 = "publisher.static-values.escape-special-characters-for-table-name-regex";
   private static final String              ONTOLOGY_FILE_STORAGE_PATH_KEY                              = "ontology-file-storage-path";
   private static final String              INTEGER_DATA_TYPE_MAXIMUM_LENGTH_KEY                        = "publisher.static-values.integer-data-type-maximum-length";
   private static final String              COLUMN_VALIDATION_MESSAGE_PRECISION_KEY                     = "publisher.static-values.column-validation-message-precision";
   private static final String              COLUMN_VALIDATION_MESSAGE_SCALE_KEY                         = "publisher.static-values.column-validation-message-scale";
   private static final String              COLUMN_VALIDATION_MESSAGE_INVALID_DATEFORMAT_KEY            = "publisher.static-values.column-validation-message-invalid-dateformat";
   private static final String              COLUMN_VALIDATION_MESSAGE_INVALID_PRECISION_KEY             = "publisher.static-values.column-validation-message-invalid-precision";
   private static final String              COLUMN_VALIDATION_MESSAGE_INVALID_SCALE_KEY                 = "publisher.static-values.column-validation-message-invalid-scale";
   private static final String              COLUMN_VALIDATION_MESSAGE_INVALID_UNITTYPE_KEY              = "publisher.static-values.column-validation-message-invalid-unittype";
   private static final String              COLUMN_VALIDATION_MESSAGE_INVALID_UNIT_KEY                  = "publisher.static-values.column-validation-message-invalid-unit";
   private static final String              COLUMN_VALIDATION_MESSAGE_INVALID_DATETYPE_KEY              = "publisher.static-values.column-validation-message-invalid-datetype";
   private static final String              DATA_REPLACE_MAP_FOR_AVOIDING_SCRIPT_INJECTION_KEY          = "publisher.static-values.data-replace.for-avoiding-script-injection.map.key";
   private static final String              DATA_REPLACE_MAP_FOR_AVOIDING_SCRIPT_INJECTION_VALUE        = "publisher.static-values.data-replace.for-avoiding-script-injection.map.value";
   private IConfiguration                   publisherConfiguration;

   private static final Map<String, String> scriptingAvoidingInjectionMap                               = new HashMap<String, String>();

   public IConfiguration getPublisherConfiguration () {
      return publisherConfiguration;
   }

   public void setPublisherConfiguration (IConfiguration publisherConfiguration) {
      this.publisherConfiguration = publisherConfiguration;
   }

   @Override
   public Integer getBatchSize () {
      return getPublisherConfiguration().getInt(DATA_BATCH_SIZE_KEY);
   }

   @Override
   public Integer getColumnCountLimitToFallbackDataType () {
      return getPublisherConfiguration().getInt(COLUMN_COUNT_LIMIT_FOR_FALLBACK_DATA_TYPE);
   }

   @Override
   public String getColumnValidationMessageInvalidDateFormat () {
      return getPublisherConfiguration().getProperty(COLUMN_VALIDATION_MESSAGE_INVALID_DATEFORMAT_KEY);
   }

   @Override
   public String getColumnValidationMessageInvalidDateType () {
      return getPublisherConfiguration().getProperty(COLUMN_VALIDATION_MESSAGE_INVALID_DATETYPE_KEY);
   }

   @Override
   public String getColumnValidationMessageInvalidPrecision () {
      return getPublisherConfiguration().getProperty(COLUMN_VALIDATION_MESSAGE_INVALID_PRECISION_KEY);
   }

   @Override
   public String getColumnValidationMessageInvalidScale () {
      return getPublisherConfiguration().getProperty(COLUMN_VALIDATION_MESSAGE_INVALID_SCALE_KEY);
   }

   @Override
   public String getColumnValidationMessageInvalidUnit () {
      return getPublisherConfiguration().getProperty(COLUMN_VALIDATION_MESSAGE_INVALID_UNIT_KEY);
   }

   @Override
   public String getColumnValidationMessageInvalidUnitType () {
      return getPublisherConfiguration().getProperty(COLUMN_VALIDATION_MESSAGE_INVALID_UNITTYPE_KEY);
   }

   @Override
   public String getColumnValidationMessagePrecision () {
      return getPublisherConfiguration().getProperty(COLUMN_VALIDATION_MESSAGE_PRECISION_KEY);
   }

   @Override
   public String getColumnValidationMessageScale () {
      return getPublisherConfiguration().getProperty(COLUMN_VALIDATION_MESSAGE_SCALE_KEY);
   }

   @Override
   public String getDateRegEx () {
      return getPublisherConfiguration().getProperty(DATE_REGEX_KEY);
   }

   @Override
   public Integer getDecimalDataTypePrecisionBuffer () {
      return getPublisherConfiguration().getInt(DECIMAL_DATA_TYPE_PRECISION_BUFFER_KEY);
   }

   @Override
   public Integer getDecimalDataTypeScaleBuffer () {
      return getPublisherConfiguration().getInt(DECIMAL_DATA_TYPE_SCALE_BUFFER_KEY);
   }

   @Override
   public String getDefaultColumnName () {
      return getPublisherConfiguration().getProperty(DEFAULT_COLUMN_NAME_KEY);
   }

   @Override
   public String getDefaultDataType () {
      return getPublisherConfiguration().getProperty(DEFAULT_DATA_TYPE_KEY);
   }

   @Override
   public String getEscapeCharacter () {
      return getPublisherConfiguration().getProperty(ESCAPE_CHARACTER_KEY);
   }

   @Override
   public String getEscapeSpecialCharactersForTableNameRegEx () {
      return getPublisherConfiguration().getProperty(SPECIAL_CHARACTERS_REGEX_FOR_TABLE_NAME_KEY);
   }

   @Override
   public String getEscapeSpecialCharactersRegEx () {
      return getPublisherConfiguration().getProperty(SPECIAL_CHARACTERS_REGEX_KEY);
   }

   @Override
   public String getFallbackDataType () {
      return getPublisherConfiguration().getProperty(FALLBACK_DATA_TYPE_KEY);
   }

   @Override
   public Integer getIntegerDataTypeMaximumLength () {
      return getPublisherConfiguration().getInt(INTEGER_DATA_TYPE_MAXIMUM_LENGTH_KEY);
   }

   @Override
   public Integer getIntegerDataTypeSizeBuffer () {
      return getPublisherConfiguration().getInt(INTEGER_DATA_TYPE_SIZE_BUFFER_KEY);
   }

   @Override
   public String getIntegerRegEx () {
      return getPublisherConfiguration().getProperty(INTEGER_REGEX_KEY);
   }

   @Override
   public Integer getMaxDataTypeLenght () {
      return getPublisherConfiguration().getInt(MAX_DATA_TYPE_LENGTH_KEY);
   }

   @Override
   public Integer getMaxScaleLenght () {
      return getPublisherConfiguration().getInt(MAX_SCALE_LENGTH_KEY);
   }

   @Override
   public Integer getMaxStringColumnLengthLookupEligibility () {
      return getPublisherConfiguration().getInt(MAX_STRING_COLUMN_LENGTH_LOOKUP_ELIGIBLITY_KEY);
   }

   @Override
   public Integer getMinDateFieldLength () {
      return getPublisherConfiguration().getInt(MIN_DATE_FIELD_LENGTH_KEY);
   }

   @Override
   public Integer getMinPlainDateFieldLength () {
      return getPublisherConfiguration().getInt(MIN_PLAIN_DATE_FIELD_LENGTH_KEY);
   }

   @Override
   public String getNumberRegEx () {
      return getPublisherConfiguration().getProperty(NUMBER_REGEX_KEY);
   }

   @Override
   public String getPlainDateRegEx () {
      return getPublisherConfiguration().getProperty(PLAIN_DATE_REGEX_KEY);
   }

   @Override
   public String getPrefixTableName () {
      return getPublisherConfiguration().getProperty(PREFIX_TABLE_NAME_KEY);
   }

   @Override
   public String getPublisherFileNameTimeStampFormat () {
      return getPublisherConfiguration().getProperty(PUBLISHER_FILENAME_TIMESTAMP_FORMAT_KEY);
   }

   @Override
   public Integer getSampleDateRecordsSize () {
      return getPublisherConfiguration().getInt(SAMPLE_DATE_RECORDS_SIZE_KEY);
   }

   @Override
   public Integer getStringDataTypeSizeBuffer () {
      return getPublisherConfiguration().getInt(STRING_DATA_TYPE_SIZE_BUFFER_KEY);
   }

   @Override
   public Integer getTableNameRandomStringLength () {
      return getPublisherConfiguration().getInt(PUBLISHER_TABLE_NAME_RANDOM_STRING_LENGTH_KEY);
   }

   @Override
   public Boolean isDropTempTable () {
      return getPublisherConfiguration().getBoolean(DROP_TEMP_TABLE_KEY);
   }

   @Override
   public List<String> getCharactersToEscape () {
      return getPublisherConfiguration().getList(CHARACTERS_TO_ESCAPE_KEY);
   }

   @Override
   public List<Double> getLookupEligibilityCriteriaPercentage () {
      List<String> lookupEligiblityCriteriaPercentages = getPublisherConfiguration().getList(
               LOOKUP_ELIGIBILITY_CRITERIA_PERCENTAGES_KEY);
      List<Double> lookupEligiblityLongCriteriaPercentages = new ArrayList<Double>();
      for (String percentage : lookupEligiblityCriteriaPercentages) {
         lookupEligiblityLongCriteriaPercentages.add(Double.valueOf(percentage).doubleValue());
      }

      return lookupEligiblityLongCriteriaPercentages;
   }

   @Override
   public List<Long> getLookupEligibilityCriteriaRecords () {
      List<String> lookupEligiblityCriteriaRecords = getPublisherConfiguration().getList(
               LOOKUP_ELIGIBILITY_CRITERIA_RECORDS_KEY);
      List<Long> lookupEligiblityLongCriteriaRecords = new ArrayList<Long>();
      for (String recordCount : lookupEligiblityCriteriaRecords) {
         lookupEligiblityLongCriteriaRecords.add(Long.valueOf(recordCount).longValue());
      }
      return lookupEligiblityLongCriteriaRecords;
   }

   @Override
   public List<Double> getDateLookupEligibilityCriteriaPercentage () {
      List<String> lookupEligiblityCriteriaPercentages = getPublisherConfiguration().getList(
               DATE_LOOKUP_ELIGIBILITY_CRITERIA_PERCENTAGES_KEY);
      List<Double> lookupEligiblityLongCriteriaPercentages = new ArrayList<Double>();
      for (String percentage : lookupEligiblityCriteriaPercentages) {
         lookupEligiblityLongCriteriaPercentages.add(Double.valueOf(percentage).doubleValue());
      }

      return lookupEligiblityLongCriteriaPercentages;
   }

   @Override
   public List<Long> getDateLookupEligibilityCriteriaRecords () {
      List<String> lookupEligiblityCriteriaRecords = getPublisherConfiguration().getList(
               DATE_LOOKUP_ELIGIBILITY_CRITERIA_RECORDS_KEY);

      List<Long> lookupEligiblityLongCriteriaRecords = new ArrayList<Long>();
      for (String recordCount : lookupEligiblityCriteriaRecords) {
         lookupEligiblityLongCriteriaRecords.add(Long.valueOf(recordCount).longValue());
      }
      return lookupEligiblityLongCriteriaRecords;
   }

   @Override
   public Long getMinimumMembersEligibilityLookup () {
      return getPublisherConfiguration().getLong(MINIMUM_MEMBER_ELIGIBLITY_LOOKUP_KEY);
   }

   @Override
   public Boolean isForceSysteEvalToAmendUserSelectionForLookups () {
      return getPublisherConfiguration().getBoolean(FORCE_SYSTEM_EVALUATION_TO_AMEND_USER_SELECTION_FOR_LOOKUPS);
   }

   @Override
   public String getPublisherFileStoragePath (String sourceType) {
      return getPublisherConfiguration().getProperty(PUBLISHER_FILE_STORAGE_PATH_KEY + "." + sourceType);
   }

   @Override
   public String getOntologyFileStoragePath (String sourceType) {
      return getPublisherConfiguration().getProperty(ONTOLOGY_FILE_STORAGE_PATH_KEY + "." + sourceType);
   }

   @Override
   public void populateScriptAvoidingInjectionMap () {
      List<String> keys = getPublisherConfiguration().getList(DATA_REPLACE_MAP_FOR_AVOIDING_SCRIPT_INJECTION_KEY);
      List<String> values = getPublisherConfiguration().getList(DATA_REPLACE_MAP_FOR_AVOIDING_SCRIPT_INJECTION_VALUE);
      for (int index = 0; index < keys.size(); index++) {
         scriptingAvoidingInjectionMap.put(keys.get(index), values.get(index));
      }
   }

   public Map<String, String> getScriptAvoidingInjectionMap () {
      return scriptingAvoidingInjectionMap;
   }

}
