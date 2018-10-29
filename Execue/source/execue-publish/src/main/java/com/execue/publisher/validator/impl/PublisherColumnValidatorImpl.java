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


package com.execue.publisher.validator.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.core.common.bean.publisher.DBTableColumn;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.publisher.configuration.IPublisherConfigurationService;
import com.execue.publisher.evaluate.IPublisherDataEvaluationService;
import com.execue.publisher.exception.PublisherException;
import com.execue.publisher.util.PublisherUtilityHelper;
import com.execue.publisher.validator.IPublisherColumnValidator;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IConversionService;
import com.execue.swi.service.IPublishedFileRetrievalService;

/**
 * This validator is for validating the publisher file table columns for datatypes, name and size.
 * 
 * @author Vishay Gupta
 */
public class PublisherColumnValidatorImpl implements IPublisherColumnValidator {

   private IPublisherConfigurationService  publisherConfigurationService;
   private IPublishedFileRetrievalService  publishedFileRetrievalService;
   private IPublisherDataEvaluationService publisherDataEvaluationService;
   private IConversionService              conversionService;

   public List<DBTableColumn> normalizeColumnNames (String[] columns, boolean isColumnsAvailable)
            throws PublisherException {
      String specialCharacterRegex = publisherConfigurationService.getEscapeSpecialCharactersRegEx();

      String defaultColumnName = publisherConfigurationService.getDefaultColumnName();

      int maxDataTypeLength = publisherConfigurationService.getMaxDataTypeLenght();

      String dataType = publisherConfigurationService.getDefaultDataType();

      if (columns != null && columns.length > publisherConfigurationService.getColumnCountLimitToFallbackDataType()) {
         dataType = publisherConfigurationService.getFallbackDataType();
      }
      List<DBTableColumn> dbTableColumns = new ArrayList<DBTableColumn>();
      Set<String> columnNames = new HashSet<String>();
      Random random = new Random(100);
      if (isColumnsAvailable) {
         int counter = 1;
         for (String column : columns) {
            String normalizedName = PublisherUtilityHelper.normalizeName(column, specialCharacterRegex);
            if (ExecueCoreUtil.isEmpty(normalizedName)) {
               normalizedName = defaultColumnName + "_" + counter++;
            }
            dbTableColumns.add(prepareDBTableColumn(columnNames, normalizedName, random, maxDataTypeLength, dataType));
         }
      } else {
         int counter = 1;
         for (int i = 0; i < columns.length; i++) {
            String normalizedName = defaultColumnName + "_" + counter++;
            dbTableColumns.add(prepareDBTableColumn(columnNames, normalizedName, random, maxDataTypeLength, dataType));
         }
      }
      return dbTableColumns;
   }

   private DBTableColumn prepareDBTableColumn (Set<String> columnNames, String normalizedColumnName, Random random,
            int maxDataTypeLength, String dataType) {
      DBTableColumn dbTableColumn = new DBTableColumn();
      dbTableColumn.setColumnName(normalizedColumnName);
      if (!columnNames.add(dbTableColumn.getColumnName())) {
         dbTableColumn.setColumnName(dbTableColumn.getColumnName() + "_" + random.nextInt());
      }
      dbTableColumn.setPrecision(maxDataTypeLength);
      if (dataType.equalsIgnoreCase("string")) {
         dbTableColumn.setDataType(DataType.STRING);
      } else if (dataType.equalsIgnoreCase("text")) {
         dbTableColumn.setDataType(DataType.TEXT);
      }
      return dbTableColumn;
   }

   public List<String> validateColumnConversionType (Long fileTableId,
            List<PublishedFileTableDetails> fileTableColumns, Map<Long, PublishedFileTableDetails> fileTableColumnsMap,
            List<PublishedFileTableDetails> validTableColumns) throws SWIException, PublishedFileException {

      List<String> errorMessages = new ArrayList<String>();

      for (PublishedFileTableDetails uiTableColumn : fileTableColumns) {

         PublishedFileTableDetails dbTableColumn = fileTableColumnsMap.get(uiTableColumn.getId());

         ConversionType uiNormalizedConversionType = normalizeConversionType(uiTableColumn.getUnitType());

         ConversionType dbNormalizedConversionType = normalizeConversionType(dbTableColumn.getOriginalUnitType());

         switch (dbNormalizedConversionType) {
            case DEFAULT:
               if (ConversionType.DEFAULT.equals(uiNormalizedConversionType)) {
                  validTableColumns.add(uiTableColumn);
               } else if (ConversionType.DATE.equals(uiNormalizedConversionType)) {
                  // if format is plain, cannot be possible
                  if (CheckType.YES.equals(conversionService.isDateFormatPlain(uiTableColumn.getFormat()))) {
                     errorMessages.add("Date format not supported for Field Name : "
                              + uiTableColumn.getEvaluatedColumnName());
                  }
                  // format is complex
                  else {
                     if (isColumnValidDateType(fileTableId, dbTableColumn.getId(), uiTableColumn.getFormat())) {
                        uiTableColumn.setEvaluatedDataType(conversionService.getDateType(uiTableColumn.getFormat()));
                        validTableColumns.add(uiTableColumn);
                     } else {
                        errorMessages.add("Date format not supported for Field Name : "
                                 + uiTableColumn.getEvaluatedColumnName());
                     }
                  }
               } else {
                  errorMessages.add("String nature can not be changed to Number for Field Name : "
                           + uiTableColumn.getEvaluatedColumnDisplayName());
               }
               break;

            case NUMBER:
               if (ConversionType.NUMBER.equals(uiNormalizedConversionType)) {
                  validTableColumns.add(uiTableColumn);
               } else if (ConversionType.DEFAULT.equals(uiNormalizedConversionType)) {
                  uiTableColumn.setEvaluatedDataType(DataType.STRING);
                  int finalPrecision = dbTableColumn.getOriginalEvaluatedPrecision();
                  if (dbTableColumn.getOriginalEvaluatedScale() > 0) {
                     finalPrecision += dbTableColumn.getOriginalEvaluatedScale() + 1;
                  }
                  uiTableColumn.setEvaluatedPrecision(finalPrecision);
                  uiTableColumn.setEvaluatedScale(0);
                  validTableColumns.add(uiTableColumn);
               } else if (ConversionType.DATE.equals(uiNormalizedConversionType)) {
                  // if format is not plain
                  if (CheckType.NO.equals(conversionService.isDateFormatPlain(uiTableColumn.getFormat()))) {
                     errorMessages.add("Date format not supported for Field Name : "
                              + uiTableColumn.getEvaluatedColumnName());
                  } else {
                     // if format is plain
                     if (isColumnValidDateType(fileTableId, dbTableColumn.getId(), uiTableColumn.getFormat())) {
                        uiTableColumn.setEvaluatedDataType(conversionService.getDateType(uiTableColumn.getFormat()));
                        validTableColumns.add(uiTableColumn);
                     } else {
                        // format not valid
                        errorMessages.add("Date format not supported for Field Name : "
                                 + uiTableColumn.getEvaluatedColumnName());
                     }
                  }
               }
               break;
            case DATE:
               if (ConversionType.DATE.equals(uiNormalizedConversionType)) {
                  if (uiTableColumn.getFormat().equals(dbTableColumn.getFormat())) {
                     validTableColumns.add(uiTableColumn);
                  } else {
                     if (isColumnValidDateType(fileTableId, fileTableId, uiTableColumn.getFormat())) {
                        uiTableColumn.setEvaluatedDataType(conversionService.getDateType(uiTableColumn.getFormat()));
                        validTableColumns.add(uiTableColumn);
                     } else {
                        errorMessages.add("Date format not supported for Field Name : "
                                 + uiTableColumn.getEvaluatedColumnName());
                     }
                  }
               } else if (ConversionType.DEFAULT.equals(uiNormalizedConversionType)) {
                  uiTableColumn.setEvaluatedDataType(DataType.STRING);
                  validTableColumns.add(uiTableColumn);
               } else if (ConversionType.NUMBER.equals(uiNormalizedConversionType)) {
                  if (CheckType.YES.equals(conversionService.isDateFormatPlain(dbTableColumn.getFormat()))) {
                     uiTableColumn.setEvaluatedDataType(DataType.INT);
                     validTableColumns.add(uiTableColumn);
                  } else {
                     errorMessages.add("Date with complex format cannot be changed to Numeric Nature for Field Name : "
                              + uiTableColumn.getEvaluatedColumnName());
                  }
               }
               break;
         }
      }
      return errorMessages;
   }

   private ConversionType normalizeConversionType (ConversionType conversionType) {
      ConversionType normalizedConversionType = conversionType;
      switch (conversionType) {
         case NUMBER:
         case CURRENCY:
         case DISTANCE:
         case PERCENTAGE:
         case TEMPERATURE:
         case TIMEDURATION:
            normalizedConversionType = ConversionType.NUMBER;
            break;
      }
      return normalizedConversionType;
   }

   private boolean isColumnValidDateType (Long publishedFileTableId, Long publishedFileTableColumnId, String dateFormat)
            throws PublishedFileException {
      boolean isColumnValidDateType = false;
      try {
         PublishedFileTableInfo publishedFileTableInfo = getPublishedFileRetrievalService()
                  .getPublishedFileTableInfoById(publishedFileTableId);
         PublishedFileTableDetails userEvaluatedColumn = getPublishedFileRetrievalService()
                  .getPublishedFileTableDetailsById(publishedFileTableColumnId);
         QueryColumn queryColumn = new QueryColumn();
         queryColumn.setColumnName(userEvaluatedColumn.getBaseColumnName());
         queryColumn.setDataType(userEvaluatedColumn.getBaseDataType());
         if (publisherDataEvaluationService.isColumnDateTypeConfirmation(publishedFileTableInfo.getId(),
                  publishedFileTableInfo.getTempTableName(), queryColumn, dateFormat)) {
            isColumnValidDateType = true;
         }
      } catch (PublisherException publisherException) {
         isColumnValidDateType = false;
      }
      return isColumnValidDateType;
   }

   public IConversionService getConversionService () {
      return conversionService;
   }

   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }

   public IPublisherConfigurationService getPublisherConfigurationService () {
      return publisherConfigurationService;
   }

   public void setPublisherConfigurationService (IPublisherConfigurationService publisherConfigurationService) {
      this.publisherConfigurationService = publisherConfigurationService;
   }

   public IPublisherDataEvaluationService getPublisherDataEvaluationService () {
      return publisherDataEvaluationService;
   }

   public void setPublisherDataEvaluationService (IPublisherDataEvaluationService publisherDataEvaluationService) {
      this.publisherDataEvaluationService = publisherDataEvaluationService;
   }

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

}
