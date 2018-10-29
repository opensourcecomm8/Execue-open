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


package com.execue.platform.swi.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.bean.swi.ColumnDataTypeMetaInfo;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.dataaccess.IGenericJDBCDAO;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.platform.swi.IJDBCSourceColumnMetaService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IConversionService;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

public class JDBCSourceColumnMetaServiceImpl implements IJDBCSourceColumnMetaService {

   private static final Logger               logger = Logger.getLogger(JDBCSourceColumnMetaServiceImpl.class);

   private QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory;

   private IConversionService                conversionService;

   private ISWIConfigurationService          swiConfigurationService;

   private IGenericJDBCDAO                   genericJDBCDAO;

   public ColumnDataTypeMetaInfo getColumnDataTypeMetaInfo (DataSource dataSource, String tableName, QueryColumn column)
            throws SWIException {

      ColumnDataTypeMetaInfo columnDataTypeMetaInfo = new ColumnDataTypeMetaInfo();

      try {

         List<DateFormat> dateFormats = conversionService.getSupportedEvaluatedDateFormats(
                  dataSource.getProviderType(), CheckType.NO);

         List<DateFormat> plainDateFormats = conversionService.getSupportedEvaluatedDateFormats(dataSource
                  .getProviderType(), CheckType.YES);

         String plainDateRegex = getSwiConfigurationService().getPlainDateRegex(dataSource.getProviderType());

         int plainDateMinLength = getSwiConfigurationService().getPlainDateFieldMinLength();

         String dateRegex = getSwiConfigurationService().getDateRegex(dataSource.getProviderType());

         int minLength = getSwiConfigurationService().getDateFieldMinLength();

         DateFormat finalDateFormat = new DateFormat();

         DataType dataType = getCorrectDataType(dataSource, tableName, column, dateFormats, plainDateFormats,
                  plainDateRegex, plainDateMinLength, dateRegex, minLength, finalDateFormat);

         columnDataTypeMetaInfo.setDataType(dataType);

         // ****** Setting the unit type based on the deduced data type *******************************//

         if (DataType.DATE.equals(dataType) || DataType.DATETIME.equals(dataType)) {
            columnDataTypeMetaInfo.setDateFormat(finalDateFormat.getFormat());
            columnDataTypeMetaInfo.setUnitType(ConversionType.DATE);
         } else if (DataType.INT.equals(dataType)) {
            if (CheckType.YES.equals(finalDateFormat.getIsPlainFormat())) {
               columnDataTypeMetaInfo.setDateFormat(finalDateFormat.getFormat());
               columnDataTypeMetaInfo.setUnitType(ConversionType.DATE);
            } else {
               columnDataTypeMetaInfo.setUnitType(ConversionType.NUMBER);
            }
         } else if (DataType.NUMBER.equals(dataType) || DataType.LARGE_INTEGER.equals(dataType)) {
            columnDataTypeMetaInfo.setUnitType(ConversionType.NUMBER);
         } else if (DataType.STRING.equals(dataType)) {
            columnDataTypeMetaInfo.setUnitType(ConversionType.DEFAULT);
         }

         // *********************************************************************************************//

         int precision = 0;
         int scale = 0;
         switch (columnDataTypeMetaInfo.getDataType()) {

            case INT:
               precision = getNonDecimalColumnPrecision(dataSource, tableName, column);
               columnDataTypeMetaInfo.setPrecision(precision);
               if (columnDataTypeMetaInfo.getPrecision() == 0) {
                  columnDataTypeMetaInfo.setPrecision(columnDataTypeMetaInfo.getPrecision()
                           + getSwiConfigurationService().getIntegerDataTypeBufferSize());
               } else if (columnDataTypeMetaInfo.getPrecision() > getSwiConfigurationService()
                        .getIntegerDataTypeMaximumLength()) {
                  columnDataTypeMetaInfo.setDataType(DataType.LARGE_INTEGER);
               }
               break;

            case STRING:
               precision = getNonDecimalColumnPrecision(dataSource, tableName, column);
               columnDataTypeMetaInfo.setPrecision(precision);
               if (columnDataTypeMetaInfo.getPrecision() == 0) {
                  columnDataTypeMetaInfo.setPrecision(columnDataTypeMetaInfo.getPrecision()
                           + getSwiConfigurationService().getStringDataTypeBufferSize());
               }
               break;

            case DATE:
            case DATETIME:
               precision = getNonDecimalColumnPrecision(dataSource, tableName, column);
               columnDataTypeMetaInfo.setPrecision(precision);
               if (columnDataTypeMetaInfo.getPrecision() == 0) {
                  columnDataTypeMetaInfo.setPrecision(columnDataTypeMetaInfo.getPrecision()
                           + getSwiConfigurationService().getStringDataTypeBufferSize());
               }
               break;

            case NUMBER:
               precision = getDecimalColumnPrecision(dataSource, tableName, column);
               scale = getDecimalColumnScale(dataSource, tableName, column);
               precision = precision + scale;

               columnDataTypeMetaInfo.setPrecision(precision);
               if (columnDataTypeMetaInfo.getPrecision() == 0) {
                  columnDataTypeMetaInfo.setPrecision(columnDataTypeMetaInfo.getPrecision()
                           + getSwiConfigurationService().getDecimalDataTypePrecisionBufferSize());
               }

               columnDataTypeMetaInfo.setScale(scale);

               if (columnDataTypeMetaInfo.getScale() == 0) {
                  columnDataTypeMetaInfo.setScale(columnDataTypeMetaInfo.getScale()
                           + getSwiConfigurationService().getDecimalDataTypeScaleBufferSize());
               }

               int maxScale = getSwiConfigurationService().getScaleMaximumLength();
               if (columnDataTypeMetaInfo.getScale() > maxScale) {
                  columnDataTypeMetaInfo.setScale(maxScale);
               }
               break;
            default:
               break;
         }
      } catch (SWIException ex) {
         throw new SWIException(ex.getCode(), ex);
      }
      return columnDataTypeMetaInfo;
   }

   private int getDecimalColumnScale (DataSource dataSource, String tableName, QueryColumn queryColumn)
            throws SWIException {
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName, null, dataSource.getOwner());
      String sqlStatementForColumnScale = getQueryGenerationUtilService(dataSource.getProviderType())
               .createSQLStatementForColumnScale(queryTable, queryColumn);
      logger.debug("SQL Query for decimal column scale " + sqlStatementForColumnScale);
      return executeSingleStatQueryExceptCount(dataSource, sqlStatementForColumnScale).intValue();
   }

   private int getDecimalColumnPrecision (DataSource dataSource, String tableName, QueryColumn column)
            throws SWIException {
      IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
               .getProviderType());
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName, null, dataSource.getOwner());
      String sqlStatementForDecimalColumnDecimalDataPrecision = queryGenerationUtilService
               .createSQLStatementForDecimalColumnDecimalDataPrecision(queryTable, column);
      logger.debug("SQL Query for decimal column decimal data precision "
               + sqlStatementForDecimalColumnDecimalDataPrecision);
      String sqlStatementForDecimalColumnIntegerDataPrecision = queryGenerationUtilService
               .createSQLStatementForDecimalColumnIntegerDataPrecision(queryTable, column);
      logger.debug("SQL Query for decimal column integer data precision "
               + sqlStatementForDecimalColumnIntegerDataPrecision);
      int decimalPrecision = executeSingleStatQueryExceptCount(dataSource,
               sqlStatementForDecimalColumnDecimalDataPrecision).intValue();
      int integerPrecision = executeSingleStatQueryExceptCount(dataSource,
               sqlStatementForDecimalColumnIntegerDataPrecision).intValue();
      return Math.max(decimalPrecision, integerPrecision);
   }

   private int getNonDecimalColumnPrecision (DataSource dataSource, String tableName, QueryColumn column)
            throws SWIException {
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName, null, dataSource.getOwner());
      String sqlStatementForNonDecimalColumnPrecision = getQueryGenerationUtilService(dataSource.getProviderType())
               .createSQLStatementForNonDecimalColumnPrecision(queryTable, column);
      logger.debug("SQL Query for non decimal column precision " + sqlStatementForNonDecimalColumnPrecision);
      return executeSingleStatQueryExceptCount(dataSource, sqlStatementForNonDecimalColumnPrecision).intValue();
   }

   private Long executeSingleStatQueryExceptCount (DataSource dataSource, String selectQuery) throws SWIException {
      ExeCueResultSet countEntireRecordsResultSet = executeSQLQuery(dataSource, new SelectQueryInfo(selectQuery));
      Long value = 0L;
      try {
         while (countEntireRecordsResultSet.next()) {
            value = countEntireRecordsResultSet.getLong(0);
         }
      } catch (Exception e) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return value;
   }

   private Long executeCountQuery (DataSource dataSource, SelectQueryInfo selectQueryInfo) throws SWIException {
      ExeCueResultSet countEntireRecordsResultSet = executeSQLQuery(dataSource, selectQueryInfo);
      Long value = 0L;
      try {
         while (countEntireRecordsResultSet.next()) {
            value = countEntireRecordsResultSet.getLong(0);
         }
      } catch (Exception e) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return value;
   }

   private DataType getCorrectDataType (DataSource dataSource, String tableName, QueryColumn column,
            List<DateFormat> dateFormats, List<DateFormat> plainDateFormats, String plainDateRegex,
            int plainDateMinLength, String dateRegex, int dateMinLength, DateFormat finalDateFormat)
            throws SWIException {

      DataType dataType = DataType.STRING;
      IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
               .getProviderType());
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName, null, dataSource.getOwner());
      SelectQueryInfo selectQueryInfo = queryGenerationUtilService.createCountNotNullRegexStatement(queryTable, column,
               null);
      logger.debug("SQL Query for counting all the records " + selectQueryInfo.getSelectQuery());
      Long countEntireRecords = executeCountQuery(dataSource, selectQueryInfo);
      selectQueryInfo = queryGenerationUtilService.createCountNotNullRegexStatement(queryTable, column,
               getSwiConfigurationService().getNumberRegex(dataSource.getProviderType()));
      logger.debug("SQL Query for counting all the number records " + selectQueryInfo.getSelectQuery());
      Long countNumberRecords = executeCountQuery(dataSource, selectQueryInfo);
      if (countEntireRecords.longValue() == countNumberRecords.longValue() && countNumberRecords > 0) {
         selectQueryInfo = queryGenerationUtilService.createCountNotNullRegexStatement(queryTable, column,
                  getSwiConfigurationService().getIntegerRegex(dataSource.getProviderType()));
         logger.debug("SQL Query for counting all the integer records " + selectQueryInfo.getSelectQuery());
         Long countIntegerRecords = executeCountQuery(dataSource, selectQueryInfo);
         if (countEntireRecords.longValue() == countIntegerRecords.longValue()) {
            dataType = DataType.INT;

            SelectQueryInfo dateRegexSizeNotNullStatement = queryGenerationUtilService
                     .createDateRegexSizeNotNullStatement(queryTable, column, plainDateRegex, plainDateMinLength);
            logger.debug("SQL Query for counting all the date records "
                     + dateRegexSizeNotNullStatement.getSelectQuery());
            Long countDateRecords = executeCountQuery(dataSource, dateRegexSizeNotNullStatement);
            for (DateFormat dateFormat : plainDateFormats) {
               if (isColumnDateType(dataSource, tableName, column, dateFormat, countEntireRecords, countDateRecords)) {
                  cloneDateFormat(finalDateFormat, dateFormat);
                  plainDateFormats.remove(dateFormat);
                  plainDateFormats.add(0, dateFormat);
                  break;
               }
            }
         } else {
            dataType = DataType.NUMBER;
         }
      } else {
         SelectQueryInfo dateRegexSizeNotNullStatement = queryGenerationUtilService
                  .createDateRegexSizeNotNullStatement(queryTable, column, dateRegex, dateMinLength);
         logger.debug("SQL Query for counting all the date records " + dateRegexSizeNotNullStatement.getSelectQuery());
         Long countDateRecords = executeCountQuery(dataSource, dateRegexSizeNotNullStatement);
         for (DateFormat dateFormat : dateFormats) {
            if (isColumnDateType(dataSource, tableName, column, dateFormat, countEntireRecords, countDateRecords)) {
               dataType = dateFormat.getDataType();
               cloneDateFormat(finalDateFormat, dateFormat);
               dateFormats.remove(dateFormat);
               dateFormats.add(0, dateFormat);
               break;
            }
         }
      }
      return dataType;
   }

   public static void cloneDateFormat (DateFormat clonedDateFormat, DateFormat toBeClonedDateFormat) {
      clonedDateFormat.setId(toBeClonedDateFormat.getId());
      clonedDateFormat.setFormat(toBeClonedDateFormat.getFormat());
      clonedDateFormat.setDbFormat(toBeClonedDateFormat.getDbFormat());
      clonedDateFormat.setAssetProviderType(toBeClonedDateFormat.getAssetProviderType());
      clonedDateFormat.setIsPlainFormat(toBeClonedDateFormat.getIsPlainFormat());
      clonedDateFormat.setQualifier(toBeClonedDateFormat.getQualifier());
      clonedDateFormat.setSupported(toBeClonedDateFormat.getSupported());
      clonedDateFormat.setQualifierBEDId(toBeClonedDateFormat.getQualifierBEDId());
   }

   public boolean isColumnDateType (DataSource dataSource, String tableName, QueryColumn column, DateFormat dateFormat,
            Long countEntireRecords, Long countDateRecords) throws SWIException {
      boolean isColumnDateType = false;
      IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
               .getProviderType());
      if (countEntireRecords.longValue() == countDateRecords.longValue() && countDateRecords.longValue() > 0) {
         QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName, null, dataSource.getOwner());
         String sampleRecordsForDateStatment = queryGenerationUtilService.createStatementForRandomRecords(queryTable,
                  column, getSwiConfigurationService().getSampleDateRecordsSize());
         List<String> sampleDateRecords = execueRecordsQuery(dataSource, sampleRecordsForDateStatment);
         if (isRecordsComplaintToDateFormat(sampleDateRecords, dateFormat)) {
            SelectQueryInfo countNotNullStringToDateStatement = queryGenerationUtilService
                     .createCountNotNullStringToDateStatement(queryTable, column, dateFormat.getDbFormat());
            logger.debug("SQL Query for counting str to date records "
                     + countNotNullStringToDateStatement.getSelectQuery());
            Long validDateRecords = executeCountQuery(dataSource, countNotNullStringToDateStatement);
            if (validDateRecords.longValue() == countEntireRecords.longValue()) {
               isColumnDateType = true;
            }
         }
      }
      return isColumnDateType;
   }

   private boolean isRecordsComplaintToDateFormat (List<String> sampleDateRecords, DateFormat dateFormat) {
      for (String record : sampleDateRecords) {
         if (!isDateValid(record, dateFormat)) {
            return false;
         }
      }
      return true;
   }

   private boolean isDateValid (String dateString, DateFormat dateFormat) {
      String datePattern = dateFormat.getFormat();
      if (dateString == null || datePattern == null || datePattern.length() <= 0) {
         return false;
      }
      SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
      formatter.setLenient(false);
      Date tempDate = null;
      try {
         tempDate = formatter.parse(dateString);
      } catch (ParseException e) {
         return false;
      }
      // TODO :-VG- if date comes like 4/8/1999 which is valid date for format dd/mm/yyyy will fail the below conditions
      // need to find better soultion. This issue is fixed by adding few more formats in parallel to existing ones like
      // "d/m/yyyy"
      if (!formatter.format(tempDate).equals(dateString)) {
         return false;
      }
      if (CheckType.YES.equals(dateFormat.getIsPlainFormat())) {
         if (datePattern.length() != dateString.length()) {
            return false;
         }
      }
      return true;
   }

   private List<String> execueRecordsQuery (DataSource dataSource, String sqlQuery) throws SWIException {
      ExeCueResultSet recordsResultsSet = executeSQLQuery(dataSource, new SelectQueryInfo(sqlQuery));
      List<String> records = new ArrayList<String>();
      try {
         while (recordsResultsSet.next()) {
            records.add(recordsResultsSet.getString(0));
         }
      } catch (Exception e) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return records;
   }

   public ExeCueResultSet executeSQLQuery (DataSource dataSource, SelectQueryInfo selectQueryInfo) throws SWIException {
      try {
         return getGenericJDBCDAO().executeQuery(dataSource.getName(), selectQueryInfo);
      } catch (DataAccessException e) {
         throw new SWIException(SWIExceptionCodes.UPLOADED_FILE_QUERY_EXECUTION_FAILED, e);
      }
   }

   public IConversionService getConversionService () {
      return conversionService;
   }

   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }

   private IQueryGenerationUtilService getQueryGenerationUtilService (AssetProviderType assetProviderType) {
      return getQueryGenerationUtilServiceFactory().getQueryGenerationUtilService(assetProviderType);
   }

   public QueryGenerationUtilServiceFactory getQueryGenerationUtilServiceFactory () {
      return queryGenerationUtilServiceFactory;
   }

   public void setQueryGenerationUtilServiceFactory (QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory) {
      this.queryGenerationUtilServiceFactory = queryGenerationUtilServiceFactory;
   }

   /**
    * @return the swiConfigurationService
    */
   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   /**
    * @param swiConfigurationService
    *           the swiConfigurationService to set
    */
   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   /**
    * @return the genericJDBCDAO
    */
   public IGenericJDBCDAO getGenericJDBCDAO () {
      return genericJDBCDAO;
   }

   /**
    * @param genericJDBCDAO
    *           the genericJDBCDAO to set
    */
   public void setGenericJDBCDAO (IGenericJDBCDAO genericJDBCDAO) {
      this.genericJDBCDAO = genericJDBCDAO;
   }

}
